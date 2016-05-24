package org.egov.repository.closeperiod;


import org.egov.commons.ClosedPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository 
public interface ClosedPeriodRepository extends JpaRepository<ClosedPeriod,Long> {
	ClosedPeriod findByCFinancialYearId(Long id);
}