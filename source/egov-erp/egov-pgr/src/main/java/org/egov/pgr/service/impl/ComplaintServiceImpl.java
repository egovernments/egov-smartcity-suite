package org.egov.pgr.service.impl;

import static org.egov.pgr.utils.constants.CommonConstants.DASH_DELIM;
import static org.egov.pgr.utils.constants.CommonConstants.MODULE_NAME;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.RandomStringUtils;
import org.egov.lib.rjbac.user.User;
import org.egov.pgr.entity.Complaint;
import org.egov.pgr.repository.ComplaintRepository;
import org.egov.pgr.service.ComplaintService;
import org.egov.pgr.utils.DateUtils;
import org.egov.pgr.utils.SecurityUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ComplaintServiceImpl implements ComplaintService {

	@Autowired
	ComplaintRepository complaintRepository;

	@Autowired
	private SecurityUtils securityUtils;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@Transactional
	public Complaint createComplaint(final Complaint complaint) {
		complaint.setComplaintID(generateComplaintID());
		// TODO Workflow will decide who is the assignee based on location data
		complaint.setAssignee(securityUtils.getCurrentUser());
		return complaintRepository.save(complaint);
	}

	@Override
	public String generateComplaintID() {
		return MODULE_NAME + DASH_DELIM + RandomStringUtils.randomAlphanumeric(5);
	}

	@Override
	public Page<Complaint> findAllCurrentUserComplaints(final Pageable pageable) {
		final User user = securityUtils.getCurrentUser();
		return complaintRepository.findByCreatedBy(user, pageable);
	}

	@Override
	public Page<Complaint> getComplaintByComplaintID(final String complaintID, final Pageable pageable) {
		return complaintRepository.findByComplaintID(complaintID, pageable);
	}

	@Override
	public Complaint getComplaintByComplaintID(final String complaintID) {
		return complaintRepository.findByComplaintID(complaintID);
	}

	@Override
	public Page<Complaint> getComplaintsLike(final Complaint complaint, final Date fromDate, final Date toDate, long totalComplaints, final Pageable pageable) {
		final Session session = entityManager.unwrap(Session.class);
		final Example complaintExample = Example.create(complaint).enableLike(MatchMode.ANYWHERE).ignoreCase().excludeZeroes();
		final Criteria criteria = session.createCriteria(Complaint.class).add(complaintExample);
		if (fromDate != null && toDate != null) {
			Date [] dates = DateUtils.getStartAndEnd(fromDate, toDate);
			criteria.add(Restrictions.between("createdDate", dates[0], dates[1]));
		} else if (fromDate != null) {
			criteria.add(Restrictions.ge("createdDate", DateUtils.getStart(fromDate)));
		} else if (toDate != null) {
			criteria.add(Restrictions.le("createdDate", DateUtils.getEnd(toDate)));
		}
		
		if (complaint.getComplainant() != null) {
			final Example complainantExample = Example.create(complaint.getComplainant()).enableLike(MatchMode.ANYWHERE).ignoreCase().excludeZeroes();
			criteria.createCriteria("complainant", JoinType.LEFT_OUTER_JOIN).add(complainantExample);
		}

		if (complaint.getBoundary() != null) {
			final Example boundaryExample = Example.create(complaint.getBoundary()).enableLike(MatchMode.ANYWHERE).ignoreCase().excludeZeroes();
			criteria.createCriteria("boundary", JoinType.LEFT_OUTER_JOIN).add(boundaryExample);
		}

		if (complaint.getComplaintType() != null) {
			final Example complaintTypeExample = Example.create(complaint.getComplaintType()).enableLike(MatchMode.ANYWHERE).ignoreCase().excludeZeroes();
			criteria.createCriteria("complaintType", JoinType.LEFT_OUTER_JOIN).add(complaintTypeExample);
			if (complaint.getComplaintType().getDepartment() != null) {
				final Example departmentExample = Example.create(complaint.getComplaintType().getDepartment()).enableLike(MatchMode.ANYWHERE).ignoreCase().excludeZeroes();
				criteria.createCriteria("complaintType.department", JoinType.LEFT_OUTER_JOIN).add(departmentExample);
			}
		}
		if (totalComplaints == 0) {
			totalComplaints = (long) criteria.setProjection(Projections.rowCount()).uniqueResult();
			criteria.setProjection(null);
		}
		final org.springframework.data.domain.Sort.Order order = pageable.getSort().iterator().next();
		criteria.addOrder(order.isAscending() ? Order.asc(order.getProperty()) : Order.desc(order.getProperty()));
		final List<Complaint> complaints = criteria.setFirstResult(pageable.getOffset()).setMaxResults(pageable.getPageSize()).list();
		return new PageImpl<Complaint>(complaints, pageable, totalComplaints);
	}

}
