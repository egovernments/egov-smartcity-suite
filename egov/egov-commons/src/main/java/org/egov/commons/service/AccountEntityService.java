package org.egov.commons.service;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.Transient;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Metamodel;

import org.egov.commons.Accountdetailkey;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.repository.AccountEntityRepository;
import org.egov.commons.utils.EntityType;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.masters.model.AccountEntity;
import org.hibernate.FlushMode;
import org.springframework.beans.factory.annotation.Autowired;
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
	
	public List<AccountEntity> search(AccountEntity  accountEntity){
	 
		if(accountEntity.getAccountdetailtype()!=null && accountEntity.getAccountdetailtype().getId()==null)
		{
			accountEntity.setAccountdetailtype(null);
		}else
		{
			accountEntity.setAccountdetailtype(accountdetailtypeService.findOne(accountEntity.getAccountdetailtype().getId()));
		}
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<AccountEntity> createQuery = cb.createQuery(AccountEntity.class);
		Root<AccountEntity> accountEntitys = createQuery.from(AccountEntity.class);
		createQuery.select(accountEntitys);
		Metamodel m = entityManager.getMetamodel();
		javax.persistence.metamodel.EntityType<AccountEntity> AccountEntity_ = m.entity(AccountEntity.class);
    
		List<Predicate> predicates = new ArrayList<Predicate>();
		if(accountEntity.getName()!=null)
		{
		String name="%"+accountEntity.getName().toLowerCase()+"%";
		predicates.add(cb.isNotNull(accountEntitys.get("name")));
		predicates.add(cb.like(cb.lower(accountEntitys.get(AccountEntity_.getDeclaredSingularAttribute("name", String.class))),name));
		}
		if(accountEntity.getCode()!=null)
		{
		String code="%"+accountEntity.getCode().toLowerCase()+"%";
		predicates.add(cb.isNotNull(accountEntitys.get("code")));
		predicates.add(cb.like(cb.lower(accountEntitys.get(AccountEntity_.getDeclaredSingularAttribute("code", String.class))),code));
		}
		if(accountEntity.getAccountdetailtype()!=null)
		{
			predicates.add(cb.equal(accountEntitys.get("accountdetailtype"), accountEntity.getAccountdetailtype()));
		}
		
		createQuery.where(predicates.toArray(new Predicate[]{}));
		TypedQuery<AccountEntity> query=entityManager.createQuery(createQuery);
		//query.setFlushMode(FlushModeType.COMMIT);

		List<AccountEntity> resultList = query.getResultList();
		return resultList;
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