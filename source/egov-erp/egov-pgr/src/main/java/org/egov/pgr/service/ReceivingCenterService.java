package org.egov.pgr.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.pgr.entity.ReceivingCenter;
import org.egov.pgr.repository.ReceivingCenterRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ReceivingCenterService {

    @PersistenceContext
    private EntityManager entityManager;
    private final ReceivingCenterRepository receivingCenterRepository;

    @Autowired
    public ReceivingCenterService(final ReceivingCenterRepository receivingCenterRepository) {
        this.receivingCenterRepository = receivingCenterRepository;
    }

    public ReceivingCenter findByRCenterId(final Long receivingCenterId) {
        return receivingCenterRepository.findOne(receivingCenterId);
    }

    public List<ReceivingCenter> findAll() {
        return receivingCenterRepository.findAll();
    }

    public ReceivingCenter load(final Long receivingCenterId) {
        return (ReceivingCenter) entityManager.unwrap(Session.class).load(ReceivingCenter.class, receivingCenterId);
    }
}
