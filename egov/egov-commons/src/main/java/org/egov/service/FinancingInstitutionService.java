package org.egov.service;


import java.util.List;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.commons.FinancingInstitution;
import org.egov.repository.FinancingInstitutionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service 
@Transactional(readOnly = true)
public class FinancingInstitutionService  {

	private final FinancingInstitutionRepository financingInstitutionRepository;
	@PersistenceContext
private EntityManager entityManager;

	@Autowired
public FinancingInstitutionService(final FinancingInstitutionRepository financingInstitutionRepository) {
	 this.financingInstitutionRepository = financingInstitutionRepository;
  }

	 @Transactional
	 public FinancingInstitution create(final FinancingInstitution financingInstitution) {
	return financingInstitutionRepository.save(financingInstitution);
  } 
	 @Transactional
	 public FinancingInstitution update(final FinancingInstitution financingInstitution) {
	return financingInstitutionRepository.save(financingInstitution);
	  } 
	public List<FinancingInstitution> findAll() {
	 return financingInstitutionRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	   }
	public FinancingInstitution findByName(String name){
	return financingInstitutionRepository.findByName(name);
	}
	public FinancingInstitution findOne(Long id){
	return financingInstitutionRepository.findOne(id);
	}
}
