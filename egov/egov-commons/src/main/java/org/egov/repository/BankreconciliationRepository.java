package org.egov.repository;


import org.egov.commons.Bankreconciliation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository 
public interface BankreconciliationRepository extends JpaRepository<Bankreconciliation,Long> {

//Bankreconciliation findByName(String name);

}
