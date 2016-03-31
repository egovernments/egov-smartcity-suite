package org.egov.commons.service;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Metamodel;

import org.egov.commons.Accountdetailtype;
import org.egov.commons.repository.AccountdetailtypeRepository;
import org.egov.masters.model.AccountEntity;
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
	
	
	public List<Accountdetailtype> search(Accountdetailtype accountdetailtype,String mode){
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Accountdetailtype> createQuery = cb.createQuery(Accountdetailtype.class);
		Root<Accountdetailtype> accountdetailtypes = createQuery.from(Accountdetailtype.class);
		createQuery.select(accountdetailtypes);
		Metamodel m = entityManager.getMetamodel();
		javax.persistence.metamodel.EntityType<Accountdetailtype> Accountdetailtype_ = m.entity(Accountdetailtype.class);
    
		List<Predicate> predicates = new ArrayList<Predicate>();
		if(accountdetailtype.getName()!=null)
		{
		String name="%"+accountdetailtype.getName().toLowerCase()+"%";
		predicates.add(cb.isNotNull(accountdetailtypes.get("name")));
		predicates.add(cb.like(cb.lower(accountdetailtypes.get(Accountdetailtype_.getDeclaredSingularAttribute("name", String.class))),name));
		}
		if(accountdetailtype.getDescription()!=null)
		{
		String code="%"+accountdetailtype.getDescription().toLowerCase()+"%";
		predicates.add(cb.isNotNull(accountdetailtypes.get("description")));
		predicates.add(cb.like(cb.lower(accountdetailtypes.get(Accountdetailtype_.getDeclaredSingularAttribute("description", String.class))),code));
		}
		if(mode.equalsIgnoreCase("edit"))
		{
			predicates.add(cb.equal(accountdetailtypes.get("fullQualifiedName"), "org.egov.masters.model.AccountEntity"));
		}
		createQuery.where(predicates.toArray(new Predicate[]{}));
		TypedQuery<Accountdetailtype> query=entityManager.createQuery(createQuery);

		List<Accountdetailtype> resultList = query.getResultList();
		return resultList;	
	}
}