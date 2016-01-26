package org.egov.repository;


import org.egov.commons.Bankaccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository 
public interface BankaccountRepository extends JpaRepository<Bankaccount,Long> {

//Bankaccount findByName(String name);

}
