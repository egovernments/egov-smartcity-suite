package org.egov.pgr.repository;

import org.egov.infra.persistence.service.HibernateRepository;
import org.egov.pgr.entity.ComplaintType;
import org.springframework.stereotype.Repository;

@Repository
public class ComplaintTypeRepository extends HibernateRepository<ComplaintType> {

    public ComplaintTypeRepository() {
        super(ComplaintType.class);
    }
}
