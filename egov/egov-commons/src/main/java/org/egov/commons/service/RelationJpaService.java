package org.egov.commons.service;

import org.egov.commons.Accountdetailkey;
import org.egov.commons.Bank;
import org.egov.commons.Relation;
import org.egov.commons.repository.RelationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class RelationJpaService {

	private final RelationRepository relationRepository;
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	AccountDetailKeyService accountDetailKeyService;
	
	@Autowired
	AccountdetailtypeService accountdetailtypeService;
	@Autowired
	public RelationJpaService(final RelationRepository relationRepository) {
		this.relationRepository = relationRepository;
	}

	@Transactional
	public Relation create(Relation relation) {
		 relation =relationRepository.save(relation);
		 
		 Accountdetailkey ac=new Accountdetailkey();
		 ac.setDetailkey(relation.getId());
		 ac.setDetailname(relation.getName());
		 ac.setGroupid(1);
		 ac.setAccountdetailtype(accountdetailtypeService.findByName("Supplier"));
		 accountDetailKeyService.create(ac);
		 
		 return relation;
	}

	@Transactional
	public Relation update(final Relation relation) {
		//this code is to handle non jpa object save since it dont have version once moved bank to jpa remove this
		if(relation.getBank()!=null && relation.getBank().getId()!=null)
		{
		Integer id = relation.getBank().getId();
		entityManager.detach(relation.getBank());
		Bank bank = entityManager.find(Bank.class, id);
		relation.setBank(bank);
		}
		else
		{
			relation.setBank(null);
		}
		return relationRepository.save(relation);
	}

	public List<Relation> findAll() {
		return relationRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	}

	public Relation findByName(String name) {
		return relationRepository.findByName(name);
	}

	public Relation findByCode(String code) {
		return relationRepository.findByCode(code);
	}

	public Relation findOne(Integer id) {
		return relationRepository.findOne(id);
	}

	public List<Relation> search(Relation relation) {
		
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Relation> createQuery = cb.createQuery(Relation.class);
		Root<Relation> relations = createQuery.from(Relation.class);
		createQuery.select(relations);
		Metamodel m = entityManager.getMetamodel();
		EntityType<Relation> Relation_ = m.entity(Relation.class);
    
		List<Predicate> predicates = new ArrayList<Predicate>();
		if(relation.getName()!=null)
		{
		String name="%"+relation.getName().toLowerCase()+"%";
		predicates.add(cb.isNotNull(relations.get("name")));
		predicates.add(cb.like(cb.lower(relations.get(Relation_.getDeclaredSingularAttribute("name", String.class))),name));
		}
		if(relation.getCode()!=null)
		{
		String code="%"+relation.getCode().toLowerCase()+"%";
		predicates.add(cb.isNotNull(relations.get("code")));
		predicates.add(cb.like(cb.lower(relations.get(Relation_.getDeclaredSingularAttribute("code", String.class))),code));
		}
		if(relation.getMobile()!=null )
		{
			String mobile=relation.getMobile();
			predicates.add(cb.isNotNull(relations.get("mobile")));
			predicates.add(cb.like(cb.lower(relations.get(Relation_.getDeclaredSingularAttribute("mobile", String.class))),mobile));
		}
		
		if(relation.getPanno()!=null )
		{
			String panno=relation.getPanno();
			predicates.add(cb.isNotNull(relations.get("panno")));
			predicates.add(cb.like(cb.lower(relations.get(Relation_.getDeclaredSingularAttribute("panno", String.class))),panno));
		}
		
		createQuery.where(predicates.toArray(new Predicate[]{}));
		TypedQuery<Relation> query=entityManager.createQuery(createQuery);
		List<Relation> resultList = query.getResultList();
		return resultList;
		
	} 
	
}