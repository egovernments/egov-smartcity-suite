package org.egov.service;


import java.util.List;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.commons.SubScheme;
import org.egov.repository.SubSchemeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service 
@Transactional(readOnly = true)
public class SubSchemeService  {

	private final SubSchemeRepository subSchemeRepository;
	@PersistenceContext
private EntityManager entityManager;

	@Autowired
public SubSchemeService(final SubSchemeRepository subSchemeRepository) {
	 this.subSchemeRepository = subSchemeRepository;
  }

	 @Transactional
	 public SubScheme create(final SubScheme subScheme) {
	return subSchemeRepository.save(subScheme);
  } 
	 @Transactional
	 public SubScheme update(final SubScheme subScheme) {
	return subSchemeRepository.save(subScheme);
	  } 
	public List<SubScheme> findAll() {
	 return subSchemeRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	   }
	public SubScheme findByName(String name){
	return subSchemeRepository.findByName(name);
	}
	public SubScheme findByCode(String code){
	return subSchemeRepository.findByCode(code);
	}
	public SubScheme findOne(Long id){
	return subSchemeRepository.findOne(id);
	}
}
