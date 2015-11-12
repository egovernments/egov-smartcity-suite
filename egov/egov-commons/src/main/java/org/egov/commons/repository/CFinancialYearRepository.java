package org.egov.commons.repository;

import org.egov.commons.CFinancialYear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository 
public interface CFinancialYearRepository extends JpaRepository<CFinancialYear,Long>{
    
}
