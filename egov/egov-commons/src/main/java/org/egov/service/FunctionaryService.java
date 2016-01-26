package org.egov.service;


import java.util.List;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.commons.Functionary;
import org.egov.repository.FunctionaryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service 
@Transactional(readOnly = true)
public class FunctionaryService  {

	private final FunctionaryRepository functionaryRepository;
	@PersistenceContext
private EntityManager entityManager;

	@Autowired
public FunctionaryService(final FunctionaryRepository functionaryRepository) {
	 this.functionaryRepository = functionaryRepository;
  }

	 @Transactional
	 public Functionary create(final Functionary functionary) {
	return functionaryRepository.save(functionary);
  } 
	 @Transactional
	 public Functionary update(final Functionary functionary) {
	return functionaryRepository.save(functionary);
	  } 
	public List<Functionary> findAll() {
	 return functionaryRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	   }
	public Functionary findByName(String name){
	return functionaryRepository.findByName(name);
	}
	public Functionary findByCode(String code){
	return functionaryRepository.findByCode(code);
	}
	public Functionary findOne(Long id){
	return functionaryRepository.findOne(id);
	}
}
