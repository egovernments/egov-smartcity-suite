package org.egov.service;


import java.util.List;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.commons.Fundsource;
import org.egov.repository.FundsourceRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service 
@Transactional(readOnly = true)
public class FundsourceService  {

	private final FundsourceRepository fundsourceRepository;
	@PersistenceContext
private EntityManager entityManager;

	@Autowired
public FundsourceService(final FundsourceRepository fundsourceRepository) {
	 this.fundsourceRepository = fundsourceRepository;
  }

	 @Transactional
	 public Fundsource create(final Fundsource fundsource) {
	return fundsourceRepository.save(fundsource);
  } 
	 @Transactional
	 public Fundsource update(final Fundsource fundsource) {
	return fundsourceRepository.save(fundsource);
	  } 
	public List<Fundsource> findAll() {
	 return fundsourceRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	   }
	public Fundsource findByName(String name){
	return fundsourceRepository.findByName(name);
	}
	public Fundsource findByCode(String code){
	return fundsourceRepository.findByCode(code);
	}
	public Fundsource findOne(Long id){
	return fundsourceRepository.findOne(id);
	}
}
