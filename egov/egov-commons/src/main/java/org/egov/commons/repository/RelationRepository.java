package org.egov.commons.repository;


import org.egov.commons.Relation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.lang.String;
import java.util.List;


@Repository 
public interface RelationRepository extends JpaRepository<Relation,Integer> {

	Relation findByCode(String code);
	Relation findByName(String name);
	List<Relation> findByNameOrCodeOrPannoOrMobile(String name,String code,String panno,String mobile);
	
	
}