package org.egov.repository;


import org.egov.commons.CFunction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository 
public interface CFunctionRepository extends JpaRepository<CFunction,Long> {

CFunction findByName(String name);
CFunction findByCode(String code);

}
