package org.egov.repository;


import org.egov.commons.Scheme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository 
public interface SchemeRepository extends JpaRepository<Scheme,Long> {

Scheme findByName(String name);

Scheme findByCode(String code);

}
