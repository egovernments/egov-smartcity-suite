package org.egov.model.repository;

import java.util.List;

import org.egov.model.budget.BudgetDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BudgetDetailRepository extends JpaRepository<BudgetDetail, java.lang.Long> {

    @Query("from BudgetDetail be where id in :rbIds")
    public List<BudgetDetail> findBudget(@Param("rbIds") final List<Long> rbIds);
    
}