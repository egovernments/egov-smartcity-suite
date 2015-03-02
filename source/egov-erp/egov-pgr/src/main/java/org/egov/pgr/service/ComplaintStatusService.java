package org.egov.pgr.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.pgr.entity.ComplaintStatus;
import org.egov.pgr.repository.ComplaintStatusRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ComplaintStatusService {

    @PersistenceContext
    private EntityManager entityManager;

    private final ComplaintStatusRepository complaintStatusRepository;

    @Autowired
    public ComplaintStatusService(final ComplaintStatusRepository complaintStatusRepository) {
        this.complaintStatusRepository = complaintStatusRepository;
    }

    public ComplaintStatus load(final Long id) {
        return (ComplaintStatus) entityManager.unwrap(Session.class).load(ComplaintStatus.class, id);
    }

    public ComplaintStatus getByName(final String name) {
        return complaintStatusRepository.findByName(name);
    }

}
