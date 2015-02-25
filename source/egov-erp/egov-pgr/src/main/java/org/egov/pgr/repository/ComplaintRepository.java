package org.egov.pgr.repository;

import org.egov.infra.persistence.service.HibernateRepository;
import org.egov.pgr.entity.Complaint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ComplaintRepository extends HibernateRepository <Complaint> {

    protected ComplaintRepository() {
        super(Complaint.class);
    }
}
