package org.egov.repository;


import org.egov.commons.Relation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository 
public interface RelationRepository extends JpaRepository<Relation,Long> {

Relation findByName(String name);

Relation findByCode(String code);

}
