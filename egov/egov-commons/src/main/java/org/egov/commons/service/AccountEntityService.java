package org.egov.commons.service;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.egov.commons.Accountdetailkey;
import org.egov.commons.repository.AccountEntityRepository;
import org.egov.masters.model.AccountEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service 
@Transactional(readOnly = true)
public class AccountEntityService  {

	private final AccountEntityRepository accountEntityRepository;
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	public AccountEntityService(final AccountEntityRepository accountEntityRepository) {
		this.accountEntityRepository = accountEntityRepository;
	}


	@Autowired
	AccountDetailKeyService accountDetailKeyService;


	@Transactional
	public AccountEntity create(AccountEntity accountEntity) {
		accountEntity	= accountEntityRepository.save(accountEntity);		
		Accountdetailkey ac=new Accountdetailkey();
		ac.setDetailkey(accountEntity.getId());
		ac.setDetailname(accountEntity.getName());
		ac.setGroupid(1);
		ac.setAccountdetailtype(accountEntity.getAccountdetailtype());
		accountDetailKeyService.create(ac);
		return accountEntity;
	} 
	@Transactional
	public AccountEntity update(final AccountEntity accountEntity) {


		return accountEntityRepository.save(accountEntity);
	} 
	public List<AccountEntity> findAll() {
		return accountEntityRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	}
	public AccountEntity findByName(String name){
		return accountEntityRepository.findByName(name);
	}
	public AccountEntity findByCode(String code){
		return accountEntityRepository.findByCode(code);
	}
	public AccountEntity findOne(Integer id){
		return accountEntityRepository.findOne(id);
	}
	public List<AccountEntity> search(AccountEntity accountEntity){
	/*	  CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		  CriteriaQuery<AccountEntity> createQuery = cb.createQuery(AccountEntity.class);
		  Root<AccountEntity> ac=createQuery.from(AccountEntity.class);
		  if(accountEntity.getAccountdetailtype()!=null)
		  {
			  createQuery.
		  }
		  
		entityManager.createQuery(criteriaQuery);*/
		return accountEntityRepository.findAll();
	}
}