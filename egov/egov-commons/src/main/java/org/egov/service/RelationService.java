package org.egov.service;


import java.util.List;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.commons.Relation;
import org.egov.repository.RelationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service 
@Transactional(readOnly = true)
public class RelationService  {

	private final RelationRepository relationRepository;
	@PersistenceContext
private EntityManager entityManager;

	@Autowired
public RelationService(final RelationRepository relationRepository) {
	 this.relationRepository = relationRepository;
  }

	 @Transactional
	 public Relation create(final Relation relation) {
	return relationRepository.save(relation);
  } 
	 @Transactional
	 public Relation update(final Relation relation) {
	return relationRepository.save(relation);
	  } 
	public List<Relation> findAll() {
	 return relationRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	   }
	public Relation findByName(String name){
	return relationRepository.findByName(name);
	}
	public Relation findByCode(String code){
	return relationRepository.findByCode(code);
	}
	public Relation findOne(Long id){
	return relationRepository.findOne(id);
	}
}
