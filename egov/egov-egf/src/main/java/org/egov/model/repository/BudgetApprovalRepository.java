package org.egov.model.repository;

import java.util.List;

import org.egov.commons.CFinancialYear;
import org.egov.model.budget.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BudgetApprovalRepository extends JpaRepository<Budget, java.lang.Long> {

  public List<Budget> findByIsbereIsAndStatusIdIs(String isBere,Integer statusId);
  
  @Query("select be.financialYear.id from Budget be where isbere =:isbere and status.id=:statusId")
  public List<Budget> findFinancialYearList(@Param("isbere") final String isbere,
          @Param("statusId") final Integer statusId);

}