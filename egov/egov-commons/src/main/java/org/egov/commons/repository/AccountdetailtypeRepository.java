package org.egov.commons.repository;


import org.egov.commons.Accountdetailtype;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository 
public interface AccountdetailtypeRepository extends JpaRepository<Accountdetailtype,Integer> {
public	Accountdetailtype findByName(String name);
public List<Accountdetailtype> findByNameContainingIgnoreCaseAndDescriptionContainingIgnoreCase(String name,String description);
public List<Accountdetailtype> findByNameContainingIgnoreCase(String name);
public List<Accountdetailtype> findByDescriptionContainingIgnoreCase(String description);
public List<Accountdetailtype>  findByFullQualifiedName(String fullQualifiedName);
}