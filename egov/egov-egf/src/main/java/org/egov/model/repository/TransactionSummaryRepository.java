package org.egov.model.repository;


import org.egov.model.contra.TransactionSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository 
public interface TransactionSummaryRepository extends JpaRepository<TransactionSummary,Long> {

//TransactionSummary findByName(String name);

}
