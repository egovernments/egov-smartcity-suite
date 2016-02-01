package org.egov.commons.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.commons.Accountdetailkey;
import org.egov.commons.Bank;
import org.egov.commons.Relation;
import org.egov.commons.repository.RelationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
		if(relation.getName()!=null ||relation.getCode()!=null || relation.getPanno()!=null |relation.getMobile()!=null)
			return relationRepository.findByNameOrCodeOrPannoOrMobile(relation.getName(), relation.getCode(),relation.getPanno(), relation.getMobile());
		else
			return relationRepository.findAll();
	} 
	
	
	
	
	
}