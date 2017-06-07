/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.pgr.service;

import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.egov.pgr.entity.enums.ComplaintStatus.FORWARDED;
import static org.egov.pgr.entity.enums.ComplaintStatus.PROCESSING;
import static org.egov.pgr.entity.enums.ComplaintStatus.REGISTERED;
import static org.egov.pgr.entity.enums.ComplaintStatus.REOPENED;
import static org.egov.pgr.utils.constants.PGRConstants.COMMENT;
import static org.egov.pgr.utils.constants.PGRConstants.COMPLAINTS_FILED;
import static org.egov.pgr.utils.constants.PGRConstants.COMPLAINTS_RESOLVED;
import static org.egov.pgr.utils.constants.PGRConstants.COMPLAINTS_UNRESOLVED;
import static org.egov.pgr.utils.constants.PGRConstants.COMPLAINT_ALL;
import static org.egov.pgr.utils.constants.PGRConstants.COMPLAINT_COMPLETED;
import static org.egov.pgr.utils.constants.PGRConstants.COMPLAINT_PENDING;
import static org.egov.pgr.utils.constants.PGRConstants.COMPLAINT_REGISTERED;
import static org.egov.pgr.utils.constants.PGRConstants.COMPLAINT_REJECTED;
import static org.egov.pgr.utils.constants.PGRConstants.COMPLETED_STATUS;
import static org.egov.pgr.utils.constants.PGRConstants.DATE;
import static org.egov.pgr.utils.constants.PGRConstants.DELIMITER_COLON;
import static org.egov.pgr.utils.constants.PGRConstants.DEPT;
import static org.egov.pgr.utils.constants.PGRConstants.ESCALATEDSTATUS;
import static org.egov.pgr.utils.constants.PGRConstants.GO_ROLE_NAME;
import static org.egov.pgr.utils.constants.PGRConstants.MODULE_NAME;
import static org.egov.pgr.utils.constants.PGRConstants.NOASSIGNMENT;
import static org.egov.pgr.utils.constants.PGRConstants.PENDING_STATUS;
import static org.egov.pgr.utils.constants.PGRConstants.REJECTED_STATUS;
import static org.egov.pgr.utils.constants.PGRConstants.RESOLVED_STATUS;
import static org.egov.pgr.utils.constants.PGRConstants.STATUS;
import static org.egov.pgr.utils.constants.PGRConstants.SYSTEMUSER;
import static org.egov.pgr.utils.constants.PGRConstants.UPDATEDBY;
import static org.egov.pgr.utils.constants.PGRConstants.UPDATEDUSERTYPE;
import static org.egov.pgr.utils.constants.PGRConstants.USER;
import static org.egov.pgr.utils.constants.PGRConstants.USERTYPE;
import static org.egov.pgr.utils.constants.PGRConstants.COMPLAINT_WITHDRAWN;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ValidationException;

import org.apache.commons.lang3.time.DateUtils;
import org.egov.eis.entity.Assignment;
import org.egov.eis.entity.Employee;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.EisCommonService;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.admin.master.service.RoleService;
import org.egov.infra.config.persistence.datasource.routing.annotation.ReadOnly;
import org.egov.infra.persistence.entity.enums.UserType;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.ApplicationNumberGenerator;
import org.egov.infra.utils.StringUtils;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.pgr.config.properties.PgrApplicationProperties;
import org.egov.pgr.entity.Complaint;
import org.egov.pgr.entity.enums.ComplaintStatus;
import org.egov.pgr.repository.ComplaintRepository;
import org.egov.pgr.service.es.ComplaintIndexService;
import org.egov.pims.commons.Position;
import org.egov.portal.entity.PortalInbox;
import org.egov.portal.entity.PortalInboxBuilder;
import org.egov.portal.service.PortalInboxService;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ComplaintService {

    private static final String RE_OPENED = "REOPENED";

    private static final String COMPLAINT_STATUS_NAME = "complaintStatus.name";

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
    private BoundaryService boundaryService;
    @Autowired
    private ApplicationNumberGenerator applicationNumberGenerator;
    @Autowired
    private EscalationService escalationService;
    @Autowired
    private PositionMasterService positionMasterService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private AssignmentService assignmentService;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private ComplaintIndexService complaintIndexService;

    @Autowired
    private PgrApplicationProperties pgrApplicationProperties;

    @Autowired
    private PriorityService priorityService;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private ForwardSkippablePositionService forwardSkippablePositionService;

    @Autowired
    private ComplaintCommunicationService complaintCommunicationService;

    @Autowired
    private PortalInboxService portalInboxService;

    @Transactional
    public Complaint createComplaint(final Complaint complaint) {

        if (StringUtils.isBlank(complaint.getCrn()))
            complaint.setCrn(applicationNumberGenerator.generate());
        final User user = securityUtils.getCurrentUser();
        complaint.getComplainant().setUserDetail(user);
        if (!SecurityUtils.isCurrentUserAnonymous() && securityUtils.currentUserType().equals(UserType.CITIZEN)) {
            complaint.getComplainant().setEmail(user.getEmailId());
            complaint.getComplainant().setName(user.getName());
            complaint.getComplainant().setMobile(user.getMobileNumber());
        }
        complaint.setStatus(complaintStatusService.getByName(COMPLAINT_REGISTERED));
        if (complaint.getLocation() == null && complaint.getLat() > 0 && complaint.getLng() > 0)
            try {
                final Long bndryId = boundaryService.getBoundaryIdFromShapefile(complaint.getLat(), complaint.getLng());
                if (bndryId != null && bndryId != 0) {
                    final Boundary location = boundaryService.getBoundaryById(bndryId);
                    complaint.setLocation(location);
                } else
                    throw new ValidationException("gis.location.info.not.found");
            } catch (final Exception e) {
                LOG.error("No GIS data found", e);
                throw new ValidationException("gis.location.info.not.found");
            }

        final Position assignee = complaintRouterService.getAssignee(complaint);
        complaint.transition().start().withSenderName(complaint.getComplainant().getName())
                .withComments("Grievance registered with Complaint Number : " + complaint.getCrn())
                .withStateValue(complaint.getStatus().getName()).withOwner(assignee).withDateInfo(new Date());

        complaint.setAssignee(assignee);
        complaint.setEscalationDate(new Date());
        complaint.setEscalationDate(escalationService.getExpiryDate(complaint));

        if (null != complaint.getComplaintType() && null != complaint.getComplaintType().getDepartment())
            complaint.setDepartment(complaint.getComplaintType().getDepartment());
        else if (null != assignee)
            complaint.setDepartment(assignee.getDeptDesig().getDepartment());
        if (complaint.getPriority() == null)
            complaint.setPriority(priorityService.getPriorityByCode(pgrApplicationProperties.defaultComplaintPriority()));
        complaintRepository.saveAndFlush(complaint);
        if (securityUtils.currentUserIsCitizen())
            pushPortalInboxMessage(complaint);
        complaintCommunicationService.sendRegistrationMessage(complaint);

        complaintIndexService.createComplaintIndex(complaint);

        return complaint;
    }

    /**
     * @param complaint
     * @param nextOwnerId
     * @param approvalComment
     * @return If the status is changed to completed/withdrawn then terminate/end the workflow. Even if the poistion is selected
     * no need to consider position as it is end of workflow.else If position is found then it is forwarding only. Else it is
     * update by official or citizen
     */

    @Transactional
    public Complaint update(final Complaint complaint, final Long nextOwnerId, final String approvalComment,
                            final boolean sendToPrevAssignee) {
        final Role goRole = roleService.getRoleByName(GO_ROLE_NAME);
        String userName;
        if (securityUtils.getCurrentUser().getType().equals(UserType.CITIZEN))
            userName = securityUtils.getCurrentUser().getName();
        else
            userName = securityUtils.getCurrentUser().getUsername() + DELIMITER_COLON + securityUtils.getCurrentUser().getName();
        if (!complaint.getState().isEnded()
                && (complaint.getStatus().getName().equalsIgnoreCase(ComplaintStatus.COMPLETED.toString())
                || complaint.getStatus().getName().equalsIgnoreCase(ComplaintStatus.WITHDRAWN.toString())
                || complaint.getStatus().getName().equalsIgnoreCase(ComplaintStatus.REJECTED.toString()))) {
            complaint.setDepartment(complaint.getAssignee().getDeptDesig().getDepartment());
            LOG.debug("Terminating Grievance Workflow");
            if (!securityUtils.getCurrentUser().getRoles().contains(goRole))
                complaint.transition().end().withComments(approvalComment)
                        .withStateValue(complaint.getStatus().getName()).withSenderName(userName)
                        .withDateInfo(new Date());

            else
                complaint.transition().end().withComments(approvalComment)
                        .withStateValue(complaint.getStatus().getName()).withSenderName(userName)
                        .withDateInfo(new Date()).withOwner(complaint.getState().getOwnerPosition());
        } else if (nextOwnerId != null && !nextOwnerId.equals(0L)) {
            final Position owner = positionMasterService.getPositionById(nextOwnerId);
            complaint.setAssignee(owner);
            complaint.setDepartment(complaint.getAssignee().getDeptDesig().getDepartment());
            complaint.transition().progressWithStateCopy().withOwner(owner).withComments(approvalComment).withSenderName(userName)
                    .withStateValue(complaint.getStatus().getName()).withDateInfo(new Date());
        } else if (sendToPrevAssignee && canSendToPreviousAssignee(complaint)) {
            final Position nextAssignee = complaint.previousAssignee();
            complaint.setDepartment(nextAssignee.getDeptDesig().getDepartment());
            complaint.setAssignee(nextAssignee);
            complaint.transition().progressWithStateCopy().withComments(approvalComment).withSenderName(userName)
                    .withStateValue(complaint.getStatus().getName()).withOwner(nextAssignee).withDateInfo(new Date());

        } else if (complaint.getState().isEnded() && complaint.getStatus().getName().equalsIgnoreCase(ComplaintStatus.REOPENED.toString())) {
            complaint.transition().reopen().withComments(approvalComment).withSenderName(userName)
                    .withStateValue(complaint.getStatus().getName()).withDateInfo(new Date());
        } else {
            complaint.transition().progressWithStateCopy().withComments(approvalComment).withSenderName(userName)
                    .withStateValue(complaint.getStatus().getName()).withDateInfo(new Date());
        }

        complaintRepository.saveAndFlush(complaint);
        complaintIndexService.updateComplaintIndex(complaint, nextOwnerId, approvalComment);
        pushUpdatePortalInboxMessage(complaint);
        complaintCommunicationService.sendUpdateMessage(complaint);

        return complaint;
    }

    public Complaint getComplaintById(final Long complaintID) {
        return complaintRepository.findOne(complaintID);
    }

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    public Complaint getComplaintByCRN(final String crn) {
        return complaintRepository.findByCrn(crn);
    }

    public List<Complaint> getComplaintsEligibleForEscalation() {
        final Criteria criteria = getCurrentSession().createCriteria(Complaint.class, "complaint")
                .createAlias("complaint.status", "complaintStatus");

        criteria.add(Restrictions.disjunction().add(Restrictions.eq(COMPLAINT_STATUS_NAME, REOPENED.name()))
                .add(Restrictions.eq(COMPLAINT_STATUS_NAME, FORWARDED.name()))
                .add(Restrictions.eq(COMPLAINT_STATUS_NAME, PROCESSING.name()))
                .add(Restrictions.eq(COMPLAINT_STATUS_NAME, REGISTERED.name())))
                .add(Restrictions.lt("complaint.escalationDate", new DateTime().toDate()))
                .setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

        return criteria.list();
    }

    private String getHeaderMessage(final Complaint savedComplaint) {
        final StringBuilder headerMessage = new StringBuilder();
        if (COMPLAINT_REGISTERED.equals(savedComplaint.getStatus().getName()))
            headerMessage.append("Grievance Recorded");
        else
            headerMessage.append("Grievance Redressal");
        return headerMessage.toString();
    }

    public List<HashMap<String, Object>> getHistory(final Complaint complaint) {
        final List<HashMap<String, Object>> historyTable = new ArrayList<>();
        final State state = complaint.getState();
        final HashMap<String, Object> map = new HashMap<>();
        map.put(DATE, state.getDateInfo());
        map.put(COMMENT, defaultString(state.getComments()));
        map.put(STATUS, state.getValue());
        if ("Complaint is escalated".equals(state.getComments())) {
            map.put(UPDATEDBY, SYSTEMUSER);
            map.put(STATUS, ESCALATEDSTATUS);
        } else if (!state.getLastModifiedBy().getType().equals(UserType.EMPLOYEE))
            map.put(UPDATEDBY, complaint.getComplainant().getName());
        else
            map.put(UPDATEDBY, defaultIfBlank(state.getSenderName(),
                    new StringBuilder().append(state.getLastModifiedBy().getUsername()).append(DELIMITER_COLON)
                            .append(state.getLastModifiedBy().getName()).toString()));
        map.put(UPDATEDUSERTYPE, state.getLastModifiedBy().getType());

        final Position ownerPosition = state.getOwnerPosition();
        final User user = state.getOwnerUser();
        if (user != null) {
            map.put(USER, user.getUsername() + DELIMITER_COLON + user.getName());
            map.put(USERTYPE, user.getType());
            final Department department = eisCommonService.getDepartmentForUser(user.getId());
            map.put(DEPT, defaultString(department.getName()));
        } else if (ownerPosition != null && ownerPosition.getDeptDesig() != null) {
            final List<Assignment> assignmentList = assignmentService.getAssignmentsForPosition(ownerPosition.getId(),
                    new Date());
            final Optional<Employee> employee = !assignmentList.isEmpty()
                    ? Optional.ofNullable(assignmentList.get(0).getEmployee())
                    : Optional.empty();
            map.put(USER, employee.isPresent()
                    ? new StringBuilder().append(employee.get().getUsername()).append(DELIMITER_COLON)
                    .append(employee.get().getName()).append(DELIMITER_COLON)
                    .append(ownerPosition.getDeptDesig().getDesignation().getName()).toString()
                    : new StringBuilder().append(NOASSIGNMENT).append(DELIMITER_COLON).append(ownerPosition.getName())
                    .toString());
            map.put(USERTYPE, employee.isPresent() ? employee.get().getType() : EMPTY);
            map.put(DEPT, ownerPosition.getDeptDesig().getDepartment().getName());
        }
        historyTable.add(map);

        complaint.getStateHistory().stream().sorted(Comparator.comparing(StateHistory::getLastModifiedDate).reversed())
                .forEach(stateHistory -> historyTable.add(constructComplaintHistory(complaint, stateHistory)));
        return historyTable;
    }

    private HashMap<String, Object> constructComplaintHistory(final Complaint complaint, final StateHistory stateHistory) {
        final HashMap<String, Object> complaintHistory = new HashMap<>();
        complaintHistory.put(DATE, stateHistory.getDateInfo());
        complaintHistory.put(COMMENT, defaultString(stateHistory.getComments()));
        complaintHistory.put(STATUS, stateHistory.getValue());
        if ("Complaint is escalated".equals(stateHistory.getComments())) {
            complaintHistory.put(UPDATEDBY, SYSTEMUSER);
            complaintHistory.put(STATUS, ESCALATEDSTATUS);
        } else
            complaintHistory.put(UPDATEDBY, stateHistory.getLastModifiedBy().getType().equals(UserType.EMPLOYEE)
                    ? stateHistory.getSenderName() : complaint.getComplainant().getName());

        complaintHistory.put(UPDATEDUSERTYPE, stateHistory.getLastModifiedBy().getType());
        final Position owner = stateHistory.getOwnerPosition();
        final User userobj = stateHistory.getOwnerUser();
        if (userobj != null) {
            complaintHistory.put(USER, userobj.getUsername() + DELIMITER_COLON + userobj.getName());
            complaintHistory.put(USERTYPE, userobj.getType());
            final Department department = eisCommonService.getDepartmentForUser(userobj.getId());
            complaintHistory.put(DEPT, department != null ? department.getName() : EMPTY);
        } else if (owner != null && owner.getDeptDesig() != null) {
            final List<Assignment> assignments = assignmentService.getAssignmentsForPosition(owner.getId(), new Date());
            complaintHistory
                    .put(USER,
                            !assignments.isEmpty() ? new StringBuilder().append(assignments.get(0).getEmployee().getUsername())
                                    .append(DELIMITER_COLON).append(assignments.get(0).getEmployee().getName())
                                    .append(DELIMITER_COLON)
                                    .append(owner.getDeptDesig().getDesignation().getName()).toString()
                                    : NOASSIGNMENT + DELIMITER_COLON + owner.getName());
            complaintHistory.put(USERTYPE, !assignments.isEmpty() ? assignments.get(0).getEmployee().getType() : EMPTY);
            complaintHistory.put(DEPT, owner.getDeptDesig().getDepartment() != null
                    ? owner.getDeptDesig().getDepartment().getName() : EMPTY);
        }
        return complaintHistory;
    }

    @ReadOnly
    public Page<Complaint> getLatest(final int page, final int pageSize) {
        final int offset = page - 1;
        return complaintRepository.findByLatestComplaint(securityUtils.getCurrentUser(), new PageRequest(offset, pageSize));
    }

    @ReadOnly
    public Page<Complaint> getMyComplaint(final int page, final int pageSize) {
        final int offset = page - 1;
        return complaintRepository.findByMyComplaint(securityUtils.getCurrentUser(), new PageRequest(offset, pageSize));
    }

    @ReadOnly
    public List<Complaint> getNearByComplaint(final int page, final float lat, final float lng, final int distance,
                                              final int pageSize) {
        final Long offset = (page - 1L) * pageSize;
        final Long limit = pageSize + 1L;
        return complaintRepository.findByNearestComplaint(securityUtils.getCurrentUser().getId(), Float.valueOf(lat),
                Float.valueOf(lng), Long.valueOf(distance), limit, offset);
    }

    public String getEmailBody(final Complaint complaint) {
        final String formattedCreatedDate = new SimpleDateFormat("dd/MM/yyyy HH:mm")
                .format(complaint.getCreatedDate());
        final StringBuilder emailBody = new StringBuilder()
                .append(" %0D%0A Grievance Details -  %0D%0A %0D%0A CRN - ").append(complaint.getCrn())
                .append(" %0D%0A Grievance Type -")
                .append(complaint.getComplaintType().getName());
        if (complaint.getDepartment() != null)
            emailBody.append("  %0D%0A Grievance department  - ").append(complaint.getDepartment().getName());
        if (complaint.getComplainant().getName() != null)
            emailBody.append("  %0D%0A Complainant name - ").append(complaint.getComplainant().getName());
        if (complaint.getComplainant().getMobile() != null)
            emailBody.append("  %0D%0A Complainant mobile number - ").append(complaint.getComplainant().getMobile());
        if (complaint.getLocation() != null)
            emailBody.append("  %0D%0A Location details - ").append(complaint.getLocation().getName());

        emailBody.append(" %0D%0A Grievance details - ").append(complaint.getDetails()).append(" %0D%0A Grievance status -")
                .append(complaint.getStatus().getName()).append(" %0D%0A Grievance Registration Date - ")
                .append(formattedCreatedDate);
        return emailBody.toString();
    }

    @ReadOnly
    public List<Complaint> getPendingGrievances() {
        final User user = securityUtils.getCurrentUser();
        final String[] pendingStatus = {COMPLAINT_REGISTERED, "FORWARDED", "PROCESSING", "NOTCOMPLETED", RE_OPENED};
        final Criteria criteria = entityManager.unwrap(Session.class).createCriteria(Complaint.class, "complaint")
                .createAlias("complaint.state", "state").createAlias("complaint.status", "status");
        criteria.add(Restrictions.in("status.name", pendingStatus));
        criteria.add(
                Restrictions.in("complaint.assignee", positionMasterService.getPositionsForEmployee(user.getId(), new Date())));
        return criteria.list();
    }

    @ReadOnly
    public Page<Complaint> getMyPendingGrievances(final int page, final int pageSize) {
        final int offset = page - 1;
        return complaintRepository.findMyComplaintyByStatus(securityUtils.getCurrentUser(), PENDING_STATUS,
                new PageRequest(offset, pageSize));
    }

    @ReadOnly
    public Page<Complaint> getMyCompletedGrievances(final int page, final int pageSize) {
        final int offset = page - 1;
        return complaintRepository.findMyComplaintyByStatus(securityUtils.getCurrentUser(), COMPLETED_STATUS,
                new PageRequest(offset, pageSize));
    }

    @ReadOnly
    public Page<Complaint> getMyRejectedGrievances(final int page, final int pageSize) {
        final int offset = page - 1;
        return complaintRepository.findMyComplaintyByStatus(securityUtils.getCurrentUser(), REJECTED_STATUS,
                new PageRequest(offset, pageSize));
    }

    @ReadOnly
    public Map<String, Integer> getMyComplaintsCount() {
        final HashMap<String, Integer> complaintsCount = new HashMap<>();
        complaintsCount.put(COMPLAINT_ALL,
                complaintRepository.getMyComplaintsTotalCount(securityUtils.getCurrentUser()).intValue());
        complaintsCount.put(COMPLAINT_PENDING,
                complaintRepository.getMyComplaintCountByStatus(securityUtils.getCurrentUser(), PENDING_STATUS).intValue());
        complaintsCount.put(COMPLAINT_COMPLETED,
                complaintRepository.getMyComplaintCountByStatus(securityUtils.getCurrentUser(), COMPLETED_STATUS).intValue());
        complaintsCount.put(COMPLAINT_REJECTED,
                complaintRepository.getMyComplaintCountByStatus(securityUtils.getCurrentUser(), REJECTED_STATUS).intValue());
        return complaintsCount;
    }

    @ReadOnly
    public Map<String, Integer> getComplaintsTotalCount() {
        final HashMap<String, Integer> complaintsCount = new HashMap<>();
        complaintsCount.put(COMPLAINTS_FILED, complaintRepository.getTotalComplaintsCount().intValue());
        complaintsCount.put(COMPLAINTS_RESOLVED, complaintRepository.getComplaintsTotalCountByStatus(RESOLVED_STATUS).intValue());
        complaintsCount.put(COMPLAINTS_UNRESOLVED,
                complaintRepository.getComplaintsTotalCountByStatus(PENDING_STATUS).intValue());
        return complaintsCount;
    }

    @ReadOnly
    public List<Complaint> getOpenComplaints() {
        final List<String> statusList = Arrays.asList(COMPLAINT_REGISTERED, "FORWARDED", RE_OPENED, "PROCESSING");
        return complaintRepository.findByStatusNameIn(statusList);
    }

    public boolean canSendToPreviousAssignee(final Complaint complaint) {
        return complaint.hasState() && complaint.previousAssignee() != null &&
                forwardSkippablePositionService.isSkippablePosition(complaint.currentAssignee());
    }

    private void pushPortalInboxMessage(final Complaint savedComplaint) {

        final String link = "/pgr/complaint/update/" + savedComplaint.getCrn();

        final Integer slaHours = savedComplaint.getComplaintType().getSlaHours();

        final StringBuilder detailedMessage = new StringBuilder();
        detailedMessage.append("Complaint Type : ").append(savedComplaint.getComplaintType().getName());
        if (savedComplaint.getLocation() != null && StringUtils.isNotBlank(savedComplaint.getLocation().getName()))
            detailedMessage.append(" in ").append(savedComplaint.getLocation().getName());
        final PortalInboxBuilder portalInboxBuilder = new PortalInboxBuilder(moduleService.getModuleByName(MODULE_NAME),
                savedComplaint.getStateType(), savedComplaint.getCrn(), savedComplaint.getCrn(), savedComplaint.getId(),
                getHeaderMessage(savedComplaint), detailedMessage.toString(), link,
                false, savedComplaint.getStatus().getName(),
                slaHours != null ? DateUtils.addHours(new Date(), slaHours) : null, savedComplaint.getState(),
                Arrays.asList(securityUtils.getCurrentUser()));

        final PortalInbox portalInbox = portalInboxBuilder.build();

        portalInboxService.pushInboxMessage(portalInbox);
    }

    private void pushUpdatePortalInboxMessage(final Complaint savedComplaint) {

        final String link = "/pgr/complaint/update/" + savedComplaint.getCrn();

        boolean resolved = false;
        if (savedComplaint.getStatus().getName().equalsIgnoreCase(COMPLAINT_COMPLETED)
                || savedComplaint.getStatus().getName().equalsIgnoreCase(COMPLAINT_REJECTED)
                || savedComplaint.getStatus().getName().equalsIgnoreCase(COMPLAINT_WITHDRAWN))
            resolved = true;

        portalInboxService.updateInboxMessage(savedComplaint.getCrn(), moduleService.getModuleByName(MODULE_NAME).getId(),
                savedComplaint.getStatus().getName(), resolved, null, savedComplaint.getState(), savedComplaint.getCreatedBy(),
                savedComplaint.getCrn(), link);
    }
}
