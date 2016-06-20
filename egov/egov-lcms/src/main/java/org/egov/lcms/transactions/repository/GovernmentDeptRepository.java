package org.egov.lcms.transactions.repository;

import org.egov.lcms.masters.entity.GovernmentDept;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GovernmentDeptRepository extends JpaRepository<GovernmentDept, Long>   {
    
    GovernmentDept findByCode(String code);

}
