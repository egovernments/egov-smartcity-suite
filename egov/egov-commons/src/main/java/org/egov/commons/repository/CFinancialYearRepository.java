package org.egov.commons.repository;

import java.util.List;

import org.egov.commons.CFinancialYear;
import org.egov.commons.CFiscalPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CFinancialYearRepository extends JpaRepository<CFinancialYear, Long> {
    @Query("from CFinancialYear where endingDate > current_date order by financialyear asc")
    List<CFinancialYear> getAllFinancialYears();

    CFinancialYear findByFinYearRange(String finYearRange);

    @Query("from CFinancialYear where finYearRange=:finYearRange order by id desc")
    List<CFinancialYear> findByFinancialYearRange(@Param("finYearRange") String finYearRange);

    @Query("from CFinancialYear order by id desc")
    List<CFinancialYear> getFinYearLastDate();

    @Query("from CFiscalPeriod where name=:name order by id desc")
    CFiscalPeriod findByFiscalName(@Param("name") String name);
}
