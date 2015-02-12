package org.egov.pgr.service;

import static org.egov.pgr.utils.constants.CommonConstants.DASH_DELIM;
import static org.egov.pgr.utils.constants.CommonConstants.MODULE_NAME;

import org.apache.commons.lang.RandomStringUtils;
import org.egov.pgr.entity.Complaint;
import org.egov.pgr.repository.ComplaintRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ComplaintService {

    @Autowired
    private ComplaintRepository complaintRepository;

    @Transactional
    public void createComplaint(final Complaint complaint) {
        complaint.setCRN(generateComplaintID());
        // TODO Workflow will decide who is the assignee based on location data
        complaint.setAssignee(null);
        complaintRepository.create(complaint);
    }


    public String generateComplaintID() {
        return MODULE_NAME + DASH_DELIM + RandomStringUtils.randomAlphanumeric(5);
    }
    
    public Complaint getComplaintByComplaintID(final Long complaintID) {
        return complaintRepository.get(complaintID);
    }

/*
    public Page<Complaint> findAllCurrentUserComplaints(final Pageable pageable) {
        final User user = securityUtils.getCurrentUser().get();
        return complaintRepository.findByCreatedBy(user, pageable);
    }


    public Page<Complaint> getComplaintByComplaintID(final String complaintID, final Pageable pageable) {
        return complaintRepository.findByComplaintID(complaintID, pageable);
    }


    public Complaint getComplaintByComplaintID(final String complaintID) {
        return complaintRepository.findByComplaintID(complaintID);
    }
*/

    /*public Page<Complaint> getComplaintsLike(final Complaint complaint, final Date fromDate, final Date toDate, long totalComplaints, final Pageable pageable) {
        final Session session = entityManager.unwrap(Session.class);
        final Example complaintExample = Example.create(complaint).enableLike(MatchMode.ANYWHERE).ignoreCase().excludeZeroes();
        final Criteria criteria = session.createCriteria(Complaint.class).add(complaintExample);
        if (fromDate != null && toDate != null) {
            Date[] dates = DateUtils.getStartAndEnd(fromDate, toDate);
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
        return new PageImpl<>(complaints, pageable, totalComplaints);
    }
*/
}
