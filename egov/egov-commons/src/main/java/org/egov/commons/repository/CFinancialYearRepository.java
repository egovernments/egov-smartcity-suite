package org.egov.commons.repository;

import java.util.List;

import org.egov.commons.CFinancialYear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CFinancialYearRepository extends JpaRepository<CFinancialYear, Long> {
    @Query("from CFinancialYear where endingDate > current_date order by financialyear asc")
    List<CFinancialYear> getAllFinancialYears();
}
