package org.egov.service;


import java.util.List;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.commons.Bankaccount;
import org.egov.repository.BankaccountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service 
@Transactional(readOnly = true)
public class BankaccountService  {

	private final BankaccountRepository bankaccountRepository;
	@PersistenceContext
private EntityManager entityManager;

	@Autowired
public BankaccountService(final BankaccountRepository bankaccountRepository) {
	 this.bankaccountRepository = bankaccountRepository;
  }

	 @Transactional
	 public Bankaccount create(final Bankaccount bankaccount) {
	return bankaccountRepository.save(bankaccount);
  } 
	 @Transactional
	 public Bankaccount update(final Bankaccount bankaccount) {
	return bankaccountRepository.save(bankaccount);
	  } 
	public List<Bankaccount> findAll() {
	 return bankaccountRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	   }
	public Bankaccount findOne(Long id){
	return bankaccountRepository.findOne(id);
	}
}
