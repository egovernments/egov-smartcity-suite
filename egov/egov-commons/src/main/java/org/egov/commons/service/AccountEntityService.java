package org.egov.commons.service;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.commons.Accountdetailkey;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.repository.AccountEntityRepository;
import org.egov.commons.utils.EntityType;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.masters.model.AccountEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service 
@Transactional(readOnly = true)
public class AccountEntityService implements  EntityTypeService {

	private final AccountEntityRepository accountEntityRepository;
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private AccountdetailtypeService accountdetailtypeService;

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
	@Override
	public List<? extends EntityType> getAllActiveEntities(Integer accountDetailTypeId) {
		Accountdetailtype	accountdetailtype=	accountdetailtypeService.findOne(accountDetailTypeId);
		List<AccountEntity> activeEntityList = accountEntityRepository.findByAccountdetailtypeAndIsactive(accountdetailtype, true);
		return activeEntityList;
	}
	@Override
	public List<? extends EntityType> filterActiveEntities(String filterKey, int maxRecords,
			Integer accountDetailTypeId) {
		//final Integer pageSize = maxRecords > 0 ? maxRecords : null;
		//Pageable pageable= new PageRequest(1, maxRecords);
        final List<EntityType> entities = new ArrayList<EntityType>();
        filterKey = "%" + filterKey + "%";
        List<AccountEntity>  pagedEntities=    accountEntityRepository.findBy20(accountDetailTypeId,filterKey);
       entities.addAll(pagedEntities);
        return entities;
	}
	@Override
	public List getAssetCodesForProjectCode(Integer accountdetailkey) throws ValidationException {
		return null;
	}
	@Override
	public List<? extends EntityType> validateEntityForRTGS(List<Long> idsList) throws ValidationException {
	 
		return null;
	}
	@Override
	public List<? extends EntityType> getEntitiesById(List<Long> idsList) throws ValidationException {
		/*Iterable<Integer> it;
		for(Long l:idsList)
		{
		
		}
		return accountEntityRepository.findAll(it);  */
		return null;
	}
}