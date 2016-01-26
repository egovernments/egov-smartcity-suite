package org.egov.service;


import java.util.List;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.commons.Accountdetailkey;
import org.egov.repository.AccountdetailkeyRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service 
@Transactional(readOnly = true)
public class AccountdetailkeyService  {

	private final AccountdetailkeyRepository accountdetailkeyRepository;
	@PersistenceContext
private EntityManager entityManager;

	@Autowired
public AccountdetailkeyService(final AccountdetailkeyRepository accountdetailkeyRepository) {
	 this.accountdetailkeyRepository = accountdetailkeyRepository;
  }

	 @Transactional
	 public Accountdetailkey create(final Accountdetailkey accountdetailkey) {
	return accountdetailkeyRepository.save(accountdetailkey);
  } 
	 @Transactional
	 public Accountdetailkey update(final Accountdetailkey accountdetailkey) {
	return accountdetailkeyRepository.save(accountdetailkey);
	  } 
	public List<Accountdetailkey> findAll() {
	 return accountdetailkeyRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	   }
	public Accountdetailkey findOne(Long id){
	return accountdetailkeyRepository.findOne(id);
	}
}
