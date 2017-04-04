package org.egov.bpa.repository;

import org.egov.bpa.application.entity.BpaStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BpaStatusRepository extends JpaRepository<BpaStatus,Long>{

    BpaStatus findByCode(String code);
    BpaStatus findByModuleTypeContainingIgnoreCaseAndCode(String moduleType,String code);
}
