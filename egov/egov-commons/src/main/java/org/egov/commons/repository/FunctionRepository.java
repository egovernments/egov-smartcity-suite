package org.egov.commons.repository;

import java.util.List;

import org.egov.commons.Accountdetailtype;
import org.egov.commons.CFunction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FunctionRepository extends JpaRepository<CFunction, Long> {
	CFunction findByName(String name);
	CFunction findByCode(String code);
	public List<CFunction> findByNameContainingIgnoreCaseAndCodeContainingIgnoreCase(String name,String code);
	public List<CFunction> findByNameContainingIgnoreCase(String name);
	public List<CFunction> findByCodeContainingIgnoreCase(String code);
	public List<CFunction> findByIsNotLeaf(Boolean isNotLeaf);
}