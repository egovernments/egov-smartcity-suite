package org.egov.commons.service;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.commons.Accountdetailtype;
import org.egov.commons.repository.AccountdetailtypeRepository;
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
	public Accountdetailtype findOne(Integer id){
		return accountdetailtypeRepository.findOne(id);
	}
	public List<Accountdetailtype> findByFullQualifiedName(String fullQualifiedName)
	{
		return accountdetailtypeRepository.findByFullQualifiedName(fullQualifiedName);
	}
	
	
	public List<Accountdetailtype> search(Accountdetailtype accountdetailtype){
		if(accountdetailtype.getName()!=null && accountdetailtype.getDescription()!=null)
		{
			return accountdetailtypeRepository.findByNameContainingIgnoreCaseAndDescriptionContainingIgnoreCase(accountdetailtype.getName(),accountdetailtype.getDescription());
		}else if(accountdetailtype.getName()!=null)
		{
			return accountdetailtypeRepository.findByNameContainingIgnoreCase(accountdetailtype.getName());
		}else if(accountdetailtype.getDescription()!=null)
		{
			return accountdetailtypeRepository.findByDescriptionContainingIgnoreCase(accountdetailtype.getDescription());
		}
		else
		{
		return accountdetailtypeRepository.findAll();
		}
	}
}