package org.egov.pgr.repository;

import org.egov.pgr.entity.ComplaintStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ComplaintStatusRepository extends JpaRepository<ComplaintStatus,Long>{
    ComplaintStatus findByName(String name);
}
