package org.egov.pgr.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.config.search.Index;
import org.egov.config.search.IndexType;
import org.egov.infra.search.elastic.annotation.Indexing;
import org.egov.pgr.entity.Complaint;
import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.repository.ComplaintTypeRepository;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ComplaintTypeService {

    private final ComplaintTypeRepository complaintTypeRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public ComplaintTypeService(final ComplaintTypeRepository complaintTypeRepository) {
        this.complaintTypeRepository = complaintTypeRepository;
    }

    public ComplaintType findBy(final Long complaintTypeId) {
        return complaintTypeRepository.findOne(complaintTypeId);
    }

    @Indexing(name = Index.PGR, type = IndexType.COMPLAINT_TYPE)
    @Transactional
    public ComplaintType createComplaintType(final ComplaintType complaintType) {
        return complaintTypeRepository.save(complaintType);
    }

    @Transactional
    public void updateComplaintType(final ComplaintType complaintType) {
        complaintTypeRepository.save(complaintType);
    }

    public List<ComplaintType> findAll() {
        return complaintTypeRepository.findAll();
    }

    public List<ComplaintType> findAllByNameLike(final String name) {
        return complaintTypeRepository.findByNameContainingIgnoreCase(name);
    }

    public ComplaintType findByName(final String name) {
        return complaintTypeRepository.findByName(name);
    }

    public ComplaintType load(final Long id) {
        // FIXME alternative ?
        return (ComplaintType) entityManager.unwrap(Session.class).load(ComplaintType.class, id);
    }

    public Page<ComplaintType> getListOfComplaintTypes(Integer pageNumber, Integer pageSize) {
        Pageable pageable = new PageRequest(pageNumber - 1, pageSize, Sort.Direction.ASC, "name");
        return complaintTypeRepository.findAll(pageable);
    }

    /**
     * List top 5 complaint types filed in last one month
     * 
     * @return complaint Type list
     */
    public List<ComplaintType> getFrequentlyFiledComplaints() {

        DateTime previousDate = new DateTime();
        DateTime currentDate = new DateTime();
        previousDate = previousDate.minusMonths(1);

        Criteria criteria = entityManager.unwrap(Session.class).createCriteria(Complaint.class, "complaint");
        criteria.setProjection(Projections.projectionList().add(Projections.property("complaint.complaintType"))
                .add(Projections.count("complaint.complaintType").as("count"))
                .add(Projections.groupProperty("complaint.complaintType")));
        criteria.add(Restrictions.between("complaint.createdDate", previousDate.toDate(), currentDate.toDate()));
        criteria.setMaxResults(5).addOrder(Order.desc("count"));
        List<Object> resultList = criteria.list();
        List<ComplaintType> complaintTypeList = new ArrayList<ComplaintType>();

        for (Object row : resultList) {
            Object[] columns = (Object[]) row;
            complaintTypeList.add((ComplaintType) columns[0]);
        }
        return complaintTypeList;

    }
}
