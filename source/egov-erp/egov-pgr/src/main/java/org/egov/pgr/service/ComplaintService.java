package org.egov.pgr.service;

import static org.egov.pgr.utils.constants.CommonConstants.DASH_DELIM;

import org.apache.commons.lang.RandomStringUtils;
import org.egov.config.search.Index;
import org.egov.config.search.IndexType;
import org.egov.infra.search.elastic.annotation.Indexing;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.lib.rjbac.user.UserImpl;
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

    @Autowired
    private ComplaintStatusService complaintStatusService;

    @Autowired
    private SecurityUtils securityUtils;

    @Transactional
    @Indexing(name = Index.PGR, type = IndexType.COMPLAINT)
    public Complaint createComplaint(final Complaint complaint) {
        if (complaint.getCRN().isEmpty())
            complaint.setCRN(generateComplaintID());
        complaint.getComplainant().setUserDetail((UserImpl) securityUtils.getCurrentUser());
        complaint.setStatus(complaintStatusService.getByName("REGISTERED"));
        //Sample workflow not for production
        complaint.transition().start().withSenderName(complaint.getComplainant().getUserDetail().getFirstName())
        .withComments("complaint registered with crn : "+complaint.getCRN()).withStateValue("Registered");
        // TODO Workflow will decide who is the assignee based on location data 
        // add .withOwner(position) to the workflow.
        complaint.setAssignee(null);
        return complaintRepository.save(complaint);
    }

    @Transactional
    @Indexing(name = Index.PGR, type = IndexType.COMPLAINT)
    public Complaint update(final Complaint complaint) {
        return complaintRepository.save(complaint);
    }

    public String generateComplaintID() {
        return "CRN" + DASH_DELIM + RandomStringUtils.randomAlphanumeric(5);
    }

    public Complaint getComplaintById(final Long complaintID) {
        return complaintRepository.findOne(complaintID);
    }

    /*
     * public Page<Complaint> findAllCurrentUserComplaints(final Pageable
     * pageable) { final User user = securityUtils.getCurrentUser().get();
     * return complaintRepository.findByCreatedBy(user, pageable); } public
     * Page<Complaint> getComplaintByComplaintID(final String complaintID, final
     * Pageable pageable) { return
     * complaintRepository.findByComplaintID(complaintID, pageable); } public
     * Complaint getComplaintByComplaintID(final String complaintID) { return
     * complaintRepository.findByComplaintID(complaintID); }
     */

    /*
     * public Page<Complaint> getComplaintsLike(final Complaint complaint, final
     * Date fromDate, final Date toDate, long totalComplaints, final Pageable
     * pageable) { final Session session = entityManager.unwrap(Session.class);
     * final Example complaintExample =
     * Example.create(complaint).enableLike(MatchMode
     * .ANYWHERE).ignoreCase().excludeZeroes(); final Criteria criteria =
     * session.createCriteria(Complaint.class).add(complaintExample); if
     * (fromDate != null && toDate != null) { Date[] dates =
     * DateUtils.getStartAndEnd(fromDate, toDate);
     * criteria.add(Restrictions.between("createdDate", dates[0], dates[1])); }
     * else if (fromDate != null) { criteria.add(Restrictions.ge("createdDate",
     * DateUtils.getStart(fromDate))); } else if (toDate != null) {
     * criteria.add(Restrictions.le("createdDate", DateUtils.getEnd(toDate))); }
     * if (complaint.getComplainant() != null) { final Example
     * complainantExample =
     * Example.create(complaint.getComplainant()).enableLike
     * (MatchMode.ANYWHERE).ignoreCase().excludeZeroes();
     * criteria.createCriteria("complainant",
     * JoinType.LEFT_OUTER_JOIN).add(complainantExample); } if
     * (complaint.getBoundary() != null) { final Example boundaryExample =
     * Example
     * .create(complaint.getBoundary()).enableLike(MatchMode.ANYWHERE).ignoreCase
     * ().excludeZeroes(); criteria.createCriteria("boundary",
     * JoinType.LEFT_OUTER_JOIN).add(boundaryExample); } if
     * (complaint.getComplaintType() != null) { final Example
     * complaintTypeExample =
     * Example.create(complaint.getComplaintType()).enableLike
     * (MatchMode.ANYWHERE).ignoreCase().excludeZeroes();
     * criteria.createCriteria("complaintType",
     * JoinType.LEFT_OUTER_JOIN).add(complaintTypeExample); if
     * (complaint.getComplaintType().getDepartment() != null) { final Example
     * departmentExample =
     * Example.create(complaint.getComplaintType().getDepartment
     * ()).enableLike(MatchMode.ANYWHERE).ignoreCase().excludeZeroes();
     * criteria.createCriteria("complaintType.department",
     * JoinType.LEFT_OUTER_JOIN).add(departmentExample); } } if (totalComplaints
     * == 0) { totalComplaints = (long)
     * criteria.setProjection(Projections.rowCount()).uniqueResult();
     * criteria.setProjection(null); } final
     * org.springframework.data.domain.Sort.Order order =
     * pageable.getSort().iterator().next();
     * criteria.addOrder(order.isAscending() ? Order.asc(order.getProperty()) :
     * Order.desc(order.getProperty())); final List<Complaint> complaints =
     * criteria
     * .setFirstResult(pageable.getOffset()).setMaxResults(pageable.getPageSize
     * ()).list(); return new PageImpl<>(complaints, pageable, totalComplaints);
     * }
     */
}
