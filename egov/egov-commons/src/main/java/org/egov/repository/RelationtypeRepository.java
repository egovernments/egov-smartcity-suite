package org.egov.repository;


import org.egov.commons.Relationtype;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository 
public interface RelationtypeRepository extends JpaRepository<Relationtype,Long> {

Relationtype findByName(String name);

Relationtype findByCode(String code);

}
