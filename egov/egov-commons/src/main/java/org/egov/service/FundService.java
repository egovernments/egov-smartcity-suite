package org.egov.service;


import java.util.List;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.commons.Fund;
import org.egov.repository.FundRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service 
@Transactional(readOnly = true)
public class FundService  {

	private final FundRepository fundRepository;
	@PersistenceContext
private EntityManager entityManager;

	@Autowired
public FundService(final FundRepository fundRepository) {
	 this.fundRepository = fundRepository;
  }

	 @Transactional
	 public Fund create(final Fund fund) {
	return fundRepository.save(fund);
  } 
	 @Transactional
	 public Fund update(final Fund fund) {
	return fundRepository.save(fund);
	  } 
	public List<Fund> findAll() {
	 return fundRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	   }
	public Fund findByName(String name){
	return fundRepository.findByName(name);
	}
	public Fund findByCode(String code){
	return fundRepository.findByCode(code);
	}
	public Fund findOne(Long id){
	return fundRepository.findOne(id);
	}
}
