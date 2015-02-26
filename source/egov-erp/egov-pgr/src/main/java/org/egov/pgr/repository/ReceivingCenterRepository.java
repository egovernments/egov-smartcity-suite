package org.egov.pgr.repository;

import org.egov.infra.persistence.service.HibernateRepository;
import org.egov.pgr.entity.ReceivingCenter;
import org.springframework.stereotype.Repository;

@Repository
public class ReceivingCenterRepository extends HibernateRepository<ReceivingCenter> {

    protected ReceivingCenterRepository() {
        super(ReceivingCenter.class);
    }
}
