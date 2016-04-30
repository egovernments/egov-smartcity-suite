package org.egov.commons.repository;


import org.egov.commons.Fund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository 
public interface FundRepository extends JpaRepository<Fund,Integer> {  
	Fund findByName(String name);
	Fund findByCode(String code);
	public List<Fund> findByNameContainingIgnoreCaseOrCodeContainingIgnoreCaseOrIsactive(String name,String code,Boolean isactive);
	public List<Fund> findByCodeContainingIgnoreCase(String code);
	public List<Fund> findByIsactive(Boolean isactive);
	public List<Fund> findByNameContainingIgnoreCase(String name);
	public List<Fund> findByIsnotleaf(Boolean isnotleaf);   

}