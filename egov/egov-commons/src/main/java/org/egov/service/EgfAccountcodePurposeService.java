package org.egov.service;


import java.util.List;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.commons.EgfAccountcodePurpose;
import org.egov.repository.EgfAccountcodePurposeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service 
@Transactional(readOnly = true)
public class EgfAccountcodePurposeService  {

	private final EgfAccountcodePurposeRepository egfAccountcodePurposeRepository;
	@PersistenceContext
private EntityManager entityManager;

	@Autowired
public EgfAccountcodePurposeService(final EgfAccountcodePurposeRepository egfAccountcodePurposeRepository) {
	 this.egfAccountcodePurposeRepository = egfAccountcodePurposeRepository;
  }

	 @Transactional
	 public EgfAccountcodePurpose create(final EgfAccountcodePurpose egfAccountcodePurpose) {
	return egfAccountcodePurposeRepository.save(egfAccountcodePurpose);
  } 
	 @Transactional
	 public EgfAccountcodePurpose update(final EgfAccountcodePurpose egfAccountcodePurpose) {
	return egfAccountcodePurposeRepository.save(egfAccountcodePurpose);
	  } 
	public List<EgfAccountcodePurpose> findAll() {
	 return egfAccountcodePurposeRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	   }
	public EgfAccountcodePurpose findByName(String name){
	return egfAccountcodePurposeRepository.findByName(name);
	}
	public EgfAccountcodePurpose findOne(Long id){
	return egfAccountcodePurposeRepository.findOne(id);
	}
}
