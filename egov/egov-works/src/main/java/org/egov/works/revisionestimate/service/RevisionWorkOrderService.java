package org.egov.works.revisionestimate.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.works.revisionestimate.entity.RevisionWorkOrder;
import org.egov.works.revisionestimate.repository.RevisionWorkOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RevisionWorkOrderService {

    @PersistenceContext
    private EntityManager entityManager;

    private final RevisionWorkOrderRepository revisionWorkOrderRepository;

    @Autowired
    public RevisionWorkOrderService(final RevisionWorkOrderRepository revisionWorkOrderRepository) {
        this.revisionWorkOrderRepository = revisionWorkOrderRepository;
    }

    @Transactional
    public RevisionWorkOrder create(RevisionWorkOrder revisionWorkOrder) {

        return revisionWorkOrderRepository.save(revisionWorkOrder);
    }

}
