package org.egov.repository;


import org.egov.commons.Fundsource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository 
public interface FundsourceRepository extends JpaRepository<Fundsource,Long> {

Fundsource findByName(String name);

Fundsource findByCode(String code);

}
