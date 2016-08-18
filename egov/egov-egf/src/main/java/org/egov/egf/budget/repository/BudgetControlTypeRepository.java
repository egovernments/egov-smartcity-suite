package org.egov.egf.budget.repository;


import org.egov.egf.budget.model.BudgetControlType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository 
public interface BudgetControlTypeRepository extends JpaRepository<BudgetControlType,java.lang.Long> {

}