package org.egov.pgr.repository;

import org.egov.infra.persistence.service.HibernateRepository;
import org.egov.pgr.entity.ComplaintType;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ComplaintTypeRepository extends HibernateRepository<ComplaintType> {

    @Autowired
    public ComplaintTypeRepository(SessionFactory sessionFactory) {
        super(sessionFactory, ComplaintType.class);
    }
}
