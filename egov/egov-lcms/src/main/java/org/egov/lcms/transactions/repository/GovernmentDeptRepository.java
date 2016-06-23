package org.egov.lcms.transactions.repository;

import org.egov.lcms.masters.entity.GovernmentDepartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GovernmentDeptRepository extends JpaRepository<GovernmentDepartment, Long>   {
    
    GovernmentDepartment findByCode(String code);

}
