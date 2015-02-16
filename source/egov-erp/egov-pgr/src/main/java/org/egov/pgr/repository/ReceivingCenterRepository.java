package org.egov.pgr.repository;

import org.egov.infra.persistence.service.HibernateRepository;
import org.egov.pgr.entity.ReceivingCenter;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ReceivingCenterRepository extends HibernateRepository<ReceivingCenter> {

    @Autowired
    protected ReceivingCenterRepository(final SessionFactory sessionFactory) {
        super(sessionFactory, ReceivingCenter.class);
    }
}
