package org.egov.service;


import java.util.List;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.commons.Scheme;
import org.egov.repository.SchemeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service 
@Transactional(readOnly = true)
public class SchemeService  {

	private final SchemeRepository schemeRepository;
	@PersistenceContext
private EntityManager entityManager;

	@Autowired
public SchemeService(final SchemeRepository schemeRepository) {
	 this.schemeRepository = schemeRepository;
  }

	 @Transactional
	 public Scheme create(final Scheme scheme) {
	return schemeRepository.save(scheme);
  } 
	 @Transactional
	 public Scheme update(final Scheme scheme) {
	return schemeRepository.save(scheme);
	  } 
	public List<Scheme> findAll() {
	 return schemeRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	   }
	public Scheme findByName(String name){
	return schemeRepository.findByName(name);
	}
	public Scheme findByCode(String code){
	return schemeRepository.findByCode(code);
	}
	public Scheme findOne(Long id){
	return schemeRepository.findOne(id);
	}
}
