package org.egov.pgr.service;

import static org.egov.pgr.entity.enums.ComplaintStatus.COMPLETED;
import static org.egov.pgr.entity.enums.ComplaintStatus.FORWARDED;
import static org.egov.pgr.entity.enums.ComplaintStatus.REGISTERED;
import static org.egov.pgr.entity.enums.ComplaintStatus.REJECTED;
import static org.egov.pgr.entity.enums.ComplaintStatus.WITHDRAWN;
import static org.egov.pgr.utils.constants.CommonConstants.DASH_DELIM;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.RandomStringUtils;
import org.egov.config.search.Index;
import org.egov.config.search.IndexType;
import org.egov.infra.search.elastic.annotation.Indexing;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.lib.admbndry.CityWebsiteImpl;
import org.egov.pgr.entity.Complaint;
import org.egov.pgr.repository.ComplaintRepository;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
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
        complaint.getComplainant().setUserDetail(securityUtils.getCurrentUser());
        complaint.setStatus(complaintStatusService.getByName("REGISTERED"));
        //Sample workflow not for production
        complaint.transition().start().withSenderName(complaint.getComplainant().getUserDetail().getName())
        .withComments("complaint registered with crn : "+complaint.getCRN()).withStateValue("Registered");
        // TODO Workflow will decide who is the assignee based on location data 
        // add .withOwner(position) to the workflow.
        complaint.setAssignee(null);
        complaint.setEscalationDate(new DateTime());
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

    @PersistenceContext
    private EntityManager entityManager;
    
    public Session  getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }
    
    public List<Complaint> getComplaintsEligibleForEscalation(){
        final CityWebsiteImpl cityWebsite = (CityWebsiteImpl)getCurrentSession().getNamedQuery(CityWebsiteImpl.QUERY_CITY_BY_URL).setString("url", EGOVThreadLocals.getDomainName()).uniqueResult();
        final Integer topLevelBoundaryId = cityWebsite.getBoundaryId().getBndryId();
        final Criteria criteria = getCurrentSession().createCriteria(Complaint.class,"complaint").
               // createAlias("complaint.location","boundary").
                createAlias("complaint.status","complaintStatus");
        //TODO: BoundaryImpl pojo and hbm are not consistent
        /*if(null!=topLevelBoundaryId)
             criteria.add(Restrictions.eq("boundary.topLevelBoundaryID", topLevelBoundaryId));*/
        criteria.add(Restrictions.disjunction().
        add(Restrictions.eq("complaintStatus.name",COMPLETED.name())). 
              add(Restrictions.eq("complaintStatus.name",REJECTED.name())).
              add(Restrictions.eq("complaintStatus.name",WITHDRAWN.name())).
              add(Restrictions.eq("complaintStatus.name", FORWARDED.name())).
              add(Restrictions.eq("complaintStatus.name", REGISTERED.name()))).
              add(Restrictions.lt("complaint.escalationDate", new DateTime().toDate())).
              setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        
        
        return criteria.list();
    }
   
}
