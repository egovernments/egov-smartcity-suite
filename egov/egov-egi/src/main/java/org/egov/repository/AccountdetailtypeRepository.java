package org.egov.repository;


import org.egov.commons.Accountdetailtype;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository 
public interface AccountdetailtypeRepository extends JpaRepository<Accountdetailtype,Long> {

Accountdetailtype findByName(String name);

}
