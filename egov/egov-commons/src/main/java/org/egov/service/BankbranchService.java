package org.egov.service;


import java.util.List;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.commons.Bankbranch;
import org.egov.repository.BankbranchRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service 
@Transactional(readOnly = true)
public class BankbranchService  {

	private final BankbranchRepository bankbranchRepository;
	@PersistenceContext
private EntityManager entityManager;

	@Autowired
public BankbranchService(final BankbranchRepository bankbranchRepository) {
	 this.bankbranchRepository = bankbranchRepository;
  }

	 @Transactional
	 public Bankbranch create(final Bankbranch bankbranch) {
	return bankbranchRepository.save(bankbranch);
  } 
	 @Transactional
	 public Bankbranch update(final Bankbranch bankbranch) {
	return bankbranchRepository.save(bankbranch);
	  } 
	public List<Bankbranch> findAll() {
	 return bankbranchRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	   }
	public Bankbranch findOne(Long id){
	return bankbranchRepository.findOne(id);
	}
}
