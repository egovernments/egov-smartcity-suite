package org.egov.model.repository;

import java.util.List;

import org.egov.model.budget.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BudgetDefinitionRepository extends JpaRepository<Budget, java.lang.Long> {

    public List<Budget> findByFinancialYearIdOrderByFinancialYearIdAscNameAsc(Long id);

    public List<Budget> findByIsbereIsOrderByFinancialYearIdAscNameAsc(String isBere);

    public List<Budget> findByIsbereIsAndFinancialYearIdIsOrderByFinancialYearIdAscNameAsc(String bere, Long id);

    @Query("from Budget be where isActiveBudget=true and isbere =:isbere and financialYear.id=:financialYearId and id not in :rbIds")
    public List<Budget> findReferenceBudget(@Param("isbere") final String isbere,
            @Param("financialYearId") final Long financialYearId, @Param("rbIds") final List<Long> rbIds);

    public List<Budget> findByIsActiveBudgetTrueAndIsbereIsAndFinancialYearIdIs(String isbere, Long financialYearId);

    public List<Budget> findByIsbereIsAndFinancialYearIdIsAndIdNotIn(String isbere, Long financialYearId,
            List<Long> rbId);

    public List<Budget> findByIsbereIsAndFinancialYearIdIsAndIsPrimaryBudgetTrueAndParentIsNull(String isbere,
            Long financialYearId);

    public List<Budget> findByFinancialYearIdIsOrderByFinancialYearIdAscNameAsc(Long financialYearId);

    
    @Query("select count(b) from Budget b where b.status.id =:statusId")
    Long countBudget(Integer statusId);
    
    public Long countByStatusIdInAndFinancialYearIdIs(Integer statusId,Long financialYearId);
    
    public Long countByIdNotInAndFinancialYearIdIs(List<Long> budgetId,Long financialYearId);
}