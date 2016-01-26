package org.egov.repository;


import org.egov.commons.Bankbranch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository 
public interface BankbranchRepository extends JpaRepository<Bankbranch,Long> {

//Bankbranch findByName(String name);

}
