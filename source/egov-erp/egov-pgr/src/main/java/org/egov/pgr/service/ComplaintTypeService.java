package org.egov.pgr.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.config.search.Index;
import org.egov.config.search.IndexType;
import org.egov.infra.search.elastic.annotation.Indexing;
import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.repository.ComplaintTypeRepository;
import org.hibernate.Session;
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
    
    public Page<ComplaintType> getListOfComplaintTypes(Integer pageNumber ,Integer pageSize) {
    	Pageable pageable = new PageRequest(pageNumber - 1, pageSize, Sort.Direction.ASC,"name");
        return complaintTypeRepository.findAll(pageable);
    }
}
