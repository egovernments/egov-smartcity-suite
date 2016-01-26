package org.egov.repository;


import org.egov.commons.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository 
public interface BankRepository extends JpaRepository<Bank,Long> {

Bank findByName(String name);
Bank findByCode(String code);

}
