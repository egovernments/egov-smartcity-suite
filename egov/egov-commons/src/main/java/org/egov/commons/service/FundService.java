package org.egov.commons.service;

import org.egov.commons.Fund;
import org.egov.commons.repository.FundRepository;
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
public class FundService {

	private final FundRepository fundRepository;
	@PersistenceContext
	private EntityManager entityManager;
	

	@Autowired
	public FundService(final FundRepository fundRepository) {
		this.fundRepository = fundRepository;
	}

	@Transactional
	public Fund create(final Fund fund) {
		if(fund.getParentId()!=null && fund.getParentId().getId()==null)
			fund.setParentId(null);
		return fundRepository.save(fund);
	}

	@Transactional
	public Fund update(final Fund fund) {
		return fundRepository.save(fund);
	}

	public List<Fund> findAll() {
		return fundRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	}

	public Fund findByName(String name) {
		return fundRepository.findByName(name);
	}

	public Fund findByCode(String code) {
		return fundRepository.findByCode(code);
	}

	public Fund findOne(Integer id) {
		return fundRepository.findOne(id);
	}

	public List<Fund> search(Fund fund) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Fund> createQuery = cb.createQuery(Fund.class);
		Root<Fund> funds = createQuery.from(Fund.class);
		createQuery.select(funds);
		Metamodel m = entityManager.getMetamodel();
		EntityType<Fund> Fund_ = m.entity(Fund.class);
    
		List<Predicate> predicates = new ArrayList<Predicate>();
		if(fund.getName()!=null)
		{
		String name="%"+fund.getName().toLowerCase()+"%";
		predicates.add(cb.isNotNull(funds.get("name")));
		predicates.add(cb.like(cb.lower(funds.get(Fund_.getDeclaredSingularAttribute("name", String.class))),name));
		}
		if(fund.getCode()!=null)
		{
		String code="%"+fund.getCode().toLowerCase()+"%";
		predicates.add(cb.isNotNull(funds.get("code")));
		predicates.add(cb.like(cb.lower(funds.get(Fund_.getDeclaredSingularAttribute("code", String.class))),code));
		}
		if(fund.getIsactive())
		{
			predicates.add(cb.equal(funds.get("isactive"), true));
		}
		if(fund.getParentId()!=null)
		{
			predicates.add(cb.equal(funds.get("parentId"), fund.getParentId()));
		}
		
		createQuery.where(predicates.toArray(new Predicate[]{}));
		TypedQuery<Fund> query=entityManager.createQuery(createQuery);
		List<Fund> resultList = query.getResultList();
		return resultList;
		
		
		
	}

	public List<Fund> findByIsnotleaf() {
		return fundRepository.findByIsnotleaf(true);
	}
}