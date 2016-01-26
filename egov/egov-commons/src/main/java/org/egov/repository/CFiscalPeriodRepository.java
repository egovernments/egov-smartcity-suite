package org.egov.repository;


import org.egov.commons.CFiscalPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository 
public interface CFiscalPeriodRepository extends JpaRepository<CFiscalPeriod,Long> {

CFiscalPeriod findByName(String name);

}
