package org.egov.service;


import java.util.List;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.commons.Relationtype;
import org.egov.repository.RelationtypeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service 
@Transactional(readOnly = true)
public class RelationtypeService  {

	private final RelationtypeRepository relationtypeRepository;
	@PersistenceContext
private EntityManager entityManager;

	@Autowired
public RelationtypeService(final RelationtypeRepository relationtypeRepository) {
	 this.relationtypeRepository = relationtypeRepository;
  }

	 @Transactional
	 public Relationtype create(final Relationtype relationtype) {
	return relationtypeRepository.save(relationtype);
  } 
	 @Transactional
	 public Relationtype update(final Relationtype relationtype) {
	return relationtypeRepository.save(relationtype);
	  } 
	public List<Relationtype> findAll() {
	 return relationtypeRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	   }
	public Relationtype findByName(String name){
	return relationtypeRepository.findByName(name);
	}
	public Relationtype findByCode(String code){
	return relationtypeRepository.findByCode(code);
	}
	public Relationtype findOne(Long id){
	return relationtypeRepository.findOne(id);
	}
}
