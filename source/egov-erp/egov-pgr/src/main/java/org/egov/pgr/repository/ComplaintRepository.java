package org.egov.pgr.repository;

import org.egov.infra.persistence.service.HibernateRepository;
import org.egov.pgr.entity.Complaint;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ComplaintRepository extends HibernateRepository <Complaint> {

    @Autowired
    protected ComplaintRepository(SessionFactory sessionFactory) {
        super(sessionFactory, Complaint.class);
    }
}
