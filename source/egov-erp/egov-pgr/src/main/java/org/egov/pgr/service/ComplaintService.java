package org.egov.pgr.service;

import static org.egov.pgr.entity.enums.ComplaintStatus.COMPLETED;
import static org.egov.pgr.entity.enums.ComplaintStatus.FORWARDED;
import static org.egov.pgr.entity.enums.ComplaintStatus.REGISTERED;
import static org.egov.pgr.entity.enums.ComplaintStatus.REJECTED;
import static org.egov.pgr.entity.enums.ComplaintStatus.WITHDRAWN;
import static org.egov.pgr.utils.constants.CommonConstants.DASH_DELIM;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.RandomStringUtils;
import org.egov.config.search.Index;
import org.egov.config.search.IndexType;
import org.egov.infra.citizen.inbox.entity.CitizenInbox;
import org.egov.infra.citizen.inbox.entity.CitizenInboxBuilder;
import org.egov.infra.citizen.inbox.entity.enums.MessageType;
import org.egov.infra.citizen.inbox.entity.enums.Priority;
import org.egov.infra.citizen.inbox.service.CitizenInboxService;
import org.egov.infra.search.elastic.annotation.Indexing;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.workflow.entity.State;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.commons.Module;
import org.egov.infstr.services.EISServeable;
import org.egov.lib.admbndry.CityWebsiteImpl;
import org.egov.pgr.entity.Complaint;
import org.egov.pgr.entity.enums.ComplaintStatus;
import org.egov.pgr.repository.ComplaintRepository;
import org.egov.pims.commons.Position;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
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

	@Autowired
	private ComplaintRepository complaintRepository;

	@Autowired
	private ComplaintStatusService complaintStatusService;

	@Autowired
	private SecurityUtils securityUtils;
	@Autowired
	private ComplaintRouterService complaintRouterService;

	@Autowired
	private EISServeable eisService;

	@Autowired
	private CitizenInboxService citizenInboxService;

	private static final Logger LOG = LoggerFactory.getLogger(ComplaintService.class);

	@Transactional
	@Indexing(name = Index.PGR, type = IndexType.COMPLAINT)
	public Complaint createComplaint(final Complaint complaint) {
		if (complaint.getCRN().isEmpty())
			complaint.setCRN(generateComplaintID());
		complaint.getComplainant().setUserDetail(securityUtils.getCurrentUser());
		complaint.setStatus(complaintStatusService.getByName("REGISTERED"));
		Position assignee = complaintRouterService.getAssignee(complaint);
		complaint.transition().start().withSenderName(complaint.getComplainant().getUserDetail().getName()).withComments("complaint registered with crn : " + complaint.getCRN())
				.withStateValue("Registered").withOwner(assignee).withDateInfo(new Date());

		complaint.setAssignee(assignee);
		complaint.setEscalationDate(new DateTime());
		Complaint savedComplaint = complaintRepository.save(complaint);
		pushMessage(savedComplaint);
		return savedComplaint;
	}

	@Transactional
	@Indexing(name = Index.PGR, type = IndexType.COMPLAINT)
	public Complaint update(final Complaint complaint, Long approvalPosition, String approvalComent) {
		Position owner = null;
		// Can append any other states to terminate workflow
		// if the status is change to completed then stop the workflow

		// If position is found then it is forwarding only
		if (null != approvalPosition && !approvalPosition.equals(Long.valueOf(0))) {
			owner = eisService.getPrimaryPositionForUser(approvalPosition, new Date());
			LOG.debug(owner.toString());
			complaint.setAssignee(owner);
			if (complaint.getStatus().getName().equalsIgnoreCase(ComplaintStatus.COMPLETED.toString())) {

				complaint.transition().withOwner(owner).withComments(approvalComent).withSenderName(securityUtils.getCurrentUser().getName()).withDateInfo(new Date()).end();
			} else {
				complaint.transition().withOwner(owner).withComments(approvalComent).withSenderName(securityUtils.getCurrentUser().getName()).withStateValue(State.STATE_FORWARDED)
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

					complaint.transition().withOwner(owner).withComments(approvalComent).withSenderName(securityUtils.getCurrentUser().getName()).withDateInfo(new Date()).end();
				} else {
					complaint.transition().withOwner(owner).withComments(approvalComent).withSenderName(securityUtils.getCurrentUser().getName())
							.withStateValue(State.STATE_UPDATED).withDateInfo(new Date());
				}

			} else {
				// This is updation by Citizen
				if (complaint.getStatus().getName().equalsIgnoreCase(ComplaintStatus.WITHDRAWN.toString())) {
					complaint.transition().withComments(approvalComent).withSenderName("").withDateInfo(new Date()).end();
				} else {
					complaint.transition().withComments(approvalComent).withSenderName("").withStateValue(State.STATE_UPDATED).withDateInfo(new Date());
				}
			}
		}

		// LOG.debug(complaint.getState().getOwnerPosition().getName());

		Complaint savedComplaint = complaintRepository.save(complaint);
		pushMessage(savedComplaint);
		return savedComplaint;
	}

	public String generateComplaintID() {
		return "CRN" + DASH_DELIM + RandomStringUtils.randomAlphanumeric(5);
	}

	public Complaint getComplaintById(final Long complaintID) {
		return complaintRepository.findOne(complaintID);
	}

	@PersistenceContext
	private EntityManager entityManager;

	public Session getCurrentSession() {
		return entityManager.unwrap(Session.class);
	}

	public List<Complaint> getComplaintsEligibleForEscalation() {
		final CityWebsiteImpl cityWebsite = (CityWebsiteImpl) getCurrentSession().getNamedQuery(CityWebsiteImpl.QUERY_CITY_BY_URL)
				.setString("url", EGOVThreadLocals.getDomainName()).uniqueResult();
		final Integer topLevelBoundaryId = cityWebsite.getBoundaryId().getBndryId();
		final Criteria criteria = getCurrentSession().createCriteria(Complaint.class, "complaint").
		// createAlias("complaint.location","boundary").
				createAlias("complaint.status", "complaintStatus");
		// TODO: BoundaryImpl pojo and hbm are not consistent
		/*
		 * if(null!=topLevelBoundaryId)
		 * criteria.add(Restrictions.eq("boundary.topLevelBoundaryID",
		 * topLevelBoundaryId));
		 */
		criteria.add(
				Restrictions.disjunction().add(Restrictions.eq("complaintStatus.name", COMPLETED.name())).add(Restrictions.eq("complaintStatus.name", REJECTED.name()))
						.add(Restrictions.eq("complaintStatus.name", WITHDRAWN.name())).add(Restrictions.eq("complaintStatus.name", FORWARDED.name()))
						.add(Restrictions.eq("complaintStatus.name", REGISTERED.name()))).add(Restrictions.lt("complaint.escalationDate", new DateTime().toDate()))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		return criteria.list();
	}

	private void pushMessage(Complaint savedComplaint) {

		CitizenInboxBuilder citizenInboxBuilder = new CitizenInboxBuilder(MessageType.USER_MESSAGE, "Test Header Message", "Test Detailed Message", savedComplaint.getCreatedDate()
				.toDate(), securityUtils.getCurrentUser(), Priority.High);
		String strQuery = "select md from Module md where md.moduleName=:name";
		Query hql = getCurrentSession().createQuery(strQuery);
		hql.setParameter("name", "PGR");

		citizenInboxBuilder.module((Module) hql.uniqueResult());
		citizenInboxBuilder.identifier(savedComplaint.getCRN());
		citizenInboxBuilder.link("pgr/view-complaint?complaintId=" + savedComplaint.getId());
		citizenInboxBuilder.state(savedComplaint.getState());
		citizenInboxBuilder.status(savedComplaint.getStatus().getName());

		CitizenInbox citizenInbox = citizenInboxBuilder.build();
		citizenInboxService.pushMessage(citizenInbox);
	}

}
