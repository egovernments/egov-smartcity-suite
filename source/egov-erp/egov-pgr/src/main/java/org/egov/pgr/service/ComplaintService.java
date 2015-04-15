package org.egov.pgr.service;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.apache.commons.lang3.StringUtils.upperCase;
import static org.egov.pgr.entity.enums.ComplaintStatus.COMPLETED;
import static org.egov.pgr.entity.enums.ComplaintStatus.FORWARDED;
import static org.egov.pgr.entity.enums.ComplaintStatus.REGISTERED;
import static org.egov.pgr.entity.enums.ComplaintStatus.REJECTED;
import static org.egov.pgr.entity.enums.ComplaintStatus.WITHDRAWN;
import static org.egov.pgr.utils.constants.CommonConstants.DASH_DELIM;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.commons.service.CommonsService;
import org.egov.config.search.Index;
import org.egov.config.search.IndexType;
import org.egov.eis.service.EisCommonService;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.entity.enums.UserType;
import org.egov.infra.citizen.inbox.entity.CitizenInbox;
import org.egov.infra.citizen.inbox.entity.CitizenInboxBuilder;
import org.egov.infra.citizen.inbox.entity.enums.MessageType;
import org.egov.infra.citizen.inbox.entity.enums.Priority;
import org.egov.infra.citizen.inbox.service.CitizenInboxService;
import org.egov.infra.search.elastic.annotation.Indexing;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.EmailUtils;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.infstr.commons.Module;
import org.egov.infstr.notification.HTTPSMS;
import org.egov.infstr.services.EISServeable;
import org.egov.lib.admbndry.BoundaryDAO;
import org.egov.pgr.entity.Complaint;
import org.egov.pgr.entity.enums.ComplaintStatus;
import org.egov.pgr.repository.ComplaintRepository;
import org.egov.pims.commons.Position;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ComplaintService {

    private static final Logger LOG = LoggerFactory.getLogger(ComplaintService.class);

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private ComplaintStatusService complaintStatusService;

    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    private ComplaintRouterService complaintRouterService;

    @Autowired
    private EisCommonService eisCommonService;
    @Autowired
    private EISServeable eisService;

    @Autowired
    private CitizenInboxService citizenInboxService;

    @Autowired
    private CommonsService commonsService;

    @Autowired
    private BoundaryDAO boundaryDAO;

    @Autowired
    private EmailUtils emailUtils;

    @Transactional
    @Indexing(name = Index.PGR, type = IndexType.COMPLAINT)
    public Complaint createComplaint(final Complaint complaint) {
        if (complaint.getCRN().isEmpty())
            complaint.setCRN(generateCRN());
        final User user = securityUtils.getCurrentUser();
        complaint.getComplainant().setUserDetail(user);
        if (!securityUtils.isCurrentUserAnonymous() && user.getType().equals(UserType.CITIZEN)) {
            complaint.getComplainant().setEmail(user.getEmailId());
            complaint.getComplainant().setName(user.getName());
            complaint.getComplainant().setMobile(user.getMobileNumber());
        }
        complaint.setStatus(complaintStatusService.getByName("REGISTERED"));
        final Position assignee = complaintRouterService.getAssignee(complaint);
        complaint.transition().start().withSenderName(complaint.getComplainant().getUserDetail().getName())
                .withComments("complaint registered with crn : " + complaint.getCRN()).withStateValue("Registered")
                .withOwner(assignee).withDateInfo(new Date());

        complaint.setAssignee(assignee);
        complaint.setEscalationDate(new DateTime());
        if (complaint.getLocation() == null && complaint.getLat() != 0.0 && complaint.getLng() != 0.0) {
            final Long bndryId = commonsService.getBndryIdFromShapefile(complaint.getLat(), complaint.getLng());
            if (bndryId != null) {
                final Boundary location = boundaryDAO.getAllBoundaryById(bndryId);
                complaint.setLocation(location);
            }

        }
        final Complaint savedComplaint = complaintRepository.save(complaint);
        pushMessage(savedComplaint);
        sendEmailandSms(complaint);
        return savedComplaint;
    }

    @Transactional
    @Indexing(name = Index.PGR, type = IndexType.COMPLAINT)
    public Complaint update(final Complaint complaint, final Long approvalPosition, final String approvalComent) {
        Position owner = null;
        // Can append any other states to terminate workflow
        // if the status is change to completed then stop the workflow

        // If position is found then it is forwarding only

        if (false == complaint.getComplaintType().isLocationRequired())
            complaint.setLocation(null);

        if (null != approvalPosition && !approvalPosition.equals(Long.valueOf(0))) {
            owner = eisService.getPrimaryPositionForUser(approvalPosition, new Date());
            LOG.debug(owner.toString());
            complaint.setAssignee(owner);
            if (complaint.getStatus().getName().equalsIgnoreCase(ComplaintStatus.COMPLETED.toString())) {
                LOG.debug("callling transition........");
                complaint.transition().withOwner(owner).withComments(approvalComent)
                        .withSenderName(securityUtils.getCurrentUser().getName()).withDateInfo(new Date()).end();
            } else {
                LOG.debug("callling transition........");
                complaint.transition().withOwner(owner).withComments(approvalComent)
                        .withSenderName(securityUtils.getCurrentUser().getName()).withStateValue(State.STATE_FORWARDED)
                        .withDateInfo(new Date());
            }
        } else if (null != securityUtils.getCurrentUser()) {
            // If positon is not selected then it is updation like change
            // complaint type ,status or coments updation only.
            owner = eisService.getPrimaryPositionForUser(securityUtils.getCurrentUser().getId(), new Date());
            // if owner is found then he is an employee
            if (null != owner) {
                LOG.debug(owner.getName());
                if (complaint.getStatus().getName().equalsIgnoreCase(ComplaintStatus.COMPLETED.toString())) {
                    LOG.debug("callling transition...........");
                    complaint.transition().withOwner(owner).withComments(approvalComent)
                            .withSenderName(securityUtils.getCurrentUser().getName()).withDateInfo(new Date()).end();
                } else {
                    LOG.debug("callling transition........");
                    complaint.transition().withOwner(owner).withComments(approvalComent)
                            .withSenderName(securityUtils.getCurrentUser().getName())
                            .withStateValue(State.STATE_UPDATED).withDateInfo(new Date());
                }

            } else // This is updation by Citizen
            if (complaint.getStatus().getName().equalsIgnoreCase(ComplaintStatus.WITHDRAWN.toString()))
                complaint.transition().withComments(approvalComent).withSenderName("").withDateInfo(new Date()).end();
            else
                complaint.transition().withComments(approvalComent).withSenderName("")
                        .withStateValue(State.STATE_UPDATED).withDateInfo(new Date());
        }

        final Complaint savedComplaint = complaintRepository.save(complaint);
        pushMessage(savedComplaint);
        return savedComplaint;
    }

    public String generateCRN() {
        return upperCase(randomAlphabetic(3)) + DASH_DELIM + randomNumeric(3);
    }

    public Complaint getComplaintById(final Long complaintID) {
        return complaintRepository.findOne(complaintID);
    }

    @PersistenceContext
    private EntityManager entityManager;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    public Complaint getComplaintByCrnNo(final String crnNo) {
        final Criteria criteria = getCurrentSession().createCriteria(Complaint.class, "complaint").add(
                Restrictions.ilike("complaint.CRN", crnNo));
        return (Complaint) criteria.uniqueResult();
    }

    public List<Complaint> getComplaintsEligibleForEscalation() {
        final Criteria criteria = getCurrentSession().createCriteria(Complaint.class, "complaint").createAlias(
                "complaint.status", "complaintStatus");

        criteria.add(
                Restrictions.disjunction().add(Restrictions.eq("complaintStatus.name", COMPLETED.name()))
                .add(Restrictions.eq("complaintStatus.name", REJECTED.name()))
                .add(Restrictions.eq("complaintStatus.name", WITHDRAWN.name()))
                .add(Restrictions.eq("complaintStatus.name", FORWARDED.name()))
                .add(Restrictions.eq("complaintStatus.name", REGISTERED.name())))
                .add(Restrictions.lt("complaint.escalationDate", new DateTime().toDate()))
                .setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

        return criteria.list();
    }

    private void pushMessage(final Complaint savedComplaint) {

        final CitizenInboxBuilder citizenInboxBuilder = new CitizenInboxBuilder(MessageType.USER_MESSAGE,
                getHeaderMessage(savedComplaint), getDetailedMessage(savedComplaint), savedComplaint.getCreatedDate(),
                securityUtils.getCurrentUser(), Priority.High);
        final String strQuery = "select md from Module md where md.moduleName=:name";
        final Query hql = getCurrentSession().createQuery(strQuery);
        hql.setParameter("name", "PGR");

        citizenInboxBuilder.module((Module) hql.uniqueResult());
        citizenInboxBuilder.identifier(savedComplaint.getCRN());
        citizenInboxBuilder.link("/pgr/view-complaint?crnNo=" + savedComplaint.getCRN());
        citizenInboxBuilder.state(savedComplaint.getState());
        citizenInboxBuilder.status(savedComplaint.getStatus().getName());

        final CitizenInbox citizenInbox = citizenInboxBuilder.build();
        citizenInboxService.pushMessage(citizenInbox);
    }

    private String getHeaderMessage(final Complaint savedComplaint) {
        final StringBuilder headerMessage = new StringBuilder();
        if (savedComplaint.getStatus().getName().equals("REGISTERED"))
            headerMessage.append("Grievance Recorded");
        else
            headerMessage.append("Grievance Redressal");
        return headerMessage.toString();
    }

    private String getDetailedMessage(final Complaint savedComplaint) {
        final StringBuilder detailedMessage = new StringBuilder();
        detailedMessage
                .append("Complaint No. ")
                .append(savedComplaint.getCRN())
                .append(" regarding ")
                .append(savedComplaint.getComplaintType().getName())
                .append(" was marked as ")
                .append(savedComplaint.getStatus().getName())
                .append(" by ")
                .append(savedComplaint.getState().getSenderName().equals("Unknown") ? "you" : savedComplaint.getState()
                        .getSenderName())
                .append(". Please help us to improve our quality of service by giving your feedback on the quality of service by clicking <a>here</a>.");
        return detailedMessage.toString();
    }

    public List<Hashtable<String, Object>> getHistory(final Complaint complaint) {
        User user = null;
        final List<Hashtable<String, Object>> historyTable = new ArrayList<Hashtable<String, Object>>();
        if (!complaint.getStateHistory().isEmpty() && complaint.getStateHistory() != null)
            for (final StateHistory stateHistory : complaint.getStateHistory()) {
                final Hashtable<String, Object> map = new Hashtable<String, Object>(0);
                map.put("date", stateHistory.getDateInfo());
                map.put("comments", stateHistory.getComments());
                final Position ownerPosition = stateHistory.getOwnerPosition();
                user = stateHistory.getOwnerUser();
                if (null != user) {
                    map.put("user", user.getUsername());
                    map.put("department",
                            null != eisCommonService.getDepartmentForUser(user.getId()) ? eisCommonService
                                    .getDepartmentForUser(user.getId()).getName() : "");
                } else if (null != ownerPosition && null != ownerPosition.getDeptDesigId()) {
                    user = eisCommonService.getUserForPosition(ownerPosition.getId(), new Date());
                    map.put("user", null != user.getUsername() ? user.getUsername() : "");
                    map.put("department", null != ownerPosition.getDeptDesigId().getDeptId() ? ownerPosition
                            .getDeptDesigId().getDeptId().getName() : "");
                }

                historyTable.add(map);
            }
        return historyTable;
    }

    public void sendEmailandSms(final Complaint complaint) {

        final String formattedCreatedDate = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(complaint.getCreatedDate().toDate());

        final StringBuffer emailBody = new StringBuffer()
        .append("Dear ")
        .append(complaint.getComplainant().getName())
        .append(",\n \n \tThank you for registering a complaint (")
        .append(complaint.getCRN())
        .append("). Your complaint is registered successfully.\n \tPlease use this number for all future references.")
        .append("\n \n Complaint Details - \n \n Complaint type - ")
        .append(complaint.getComplaintType().getName()).append(" \n Location details - ")
        .append(complaint.getLocation().getName()).append("\n Complaint description - ")
        .append(complaint.getDetails()).append("\n Complaint status -").append(complaint.getStatus().getName())
        .append("\n Complaint Registration Date - ").append(formattedCreatedDate);
        final StringBuffer emailSubject = new StringBuffer().append("Registered Complaint -")
                .append(complaint.getCRN()).append(" successfuly");
        final StringBuffer smsBody = new StringBuffer().append("Dear ").append(complaint.getComplainant().getName())
                .append(", Thank you for registering a complaint (").append(complaint.getCRN())
                .append("). Please use this number for all future references.");
        if (complaint.getComplainant().getEmail() != null)
            emailUtils.sendMail(complaint.getComplainant().getEmail(), emailBody.toString(), emailSubject.toString());
        if (complaint.getComplainant().getMobile() != null)
            HTTPSMS.sendSMS(smsBody.toString(), "91" + complaint.getComplainant().getMobile());

    }

}
