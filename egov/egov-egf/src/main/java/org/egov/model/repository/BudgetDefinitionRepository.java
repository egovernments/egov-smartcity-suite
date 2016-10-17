package org.egov.model.repository;


import java.util.List;

import org.egov.model.budget.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository 
public interface BudgetDefinitionRepository extends JpaRepository<Budget,java.lang.Long> {
    
    public List<Budget> findByFinancialYearIdOrderByFinancialYearIdAscNameAsc(Long id);

    public List<Budget> findByIsbereIsOrderByFinancialYearIdAscNameAsc(String isBere);
    
    public List<Budget> findByIsbereIsAndFinancialYearIdIs(String bere,Long id);
    
    public List<Budget> findByFinancialYearIdIsAndReferenceBudgetIsNotNull(Long financialYearId);
    
    public List<Budget> findByIsActiveBudgetTrueAndIsbereIsAndFinancialYearIdIsAndIdNotIn(String isbere,Long financialYearId,List<Long> rbId);

    public List<Budget> findByIsbereIsAndFinancialYearIdIsAndIdNotIn(String isbere,Long financialYearId,List<Long> rbId);
    
    public List<Budget> findByIdIs(Long id);
    
    public List<Budget> findByIsbereIsAndFinancialYearIdIsAndIsPrimaryBudgetTrueAndParentIsNull(String isbere,Long financialYearId);
    
}