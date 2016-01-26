package org.egov.repository;


import org.egov.commons.Functionary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository 
public interface FunctionaryRepository extends JpaRepository<Functionary,Long> {

Functionary findByName(String name);

Functionary findByCode(String code);

}
