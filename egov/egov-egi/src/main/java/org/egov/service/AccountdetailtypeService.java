package org.egov.service;


import java.util.List;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.commons.Accountdetailtype;
import org.egov.repository.AccountdetailtypeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service 
@Transactional(readOnly = true)
public class AccountdetailtypeService  {

	private final AccountdetailtypeRepository accountdetailtypeRepository;
	@PersistenceContext
private EntityManager entityManager;

	@Autowired
public AccountdetailtypeService(final AccountdetailtypeRepository accountdetailtypeRepository) {
	 this.accountdetailtypeRepository = accountdetailtypeRepository;
  }

	 @Transactional
	 public Accountdetailtype create(final Accountdetailtype accountdetailtype) {
	return accountdetailtypeRepository.save(accountdetailtype);
  } 
	 @Transactional
	 public Accountdetailtype update(final Accountdetailtype accountdetailtype) {
	return accountdetailtypeRepository.save(accountdetailtype);
	  } 
	public List<Accountdetailtype> findAll() {
	 return accountdetailtypeRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	   }
	public Accountdetailtype findByName(String name){
	return accountdetailtypeRepository.findByName(name);
	}
	public Accountdetailtype findOne(Long id){
	return accountdetailtypeRepository.findOne(id);
	}
}
