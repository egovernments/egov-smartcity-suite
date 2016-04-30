package org.egov.commons.service;

import org.egov.commons.CFunction;
import org.egov.commons.repository.FunctionRepository;
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
import javax.persistence.metamodel.Metamodel;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class FunctionService {

	private final FunctionRepository functionRepository;
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	public FunctionService(final FunctionRepository functionRepository) {
		this.functionRepository = functionRepository;
	}

	@Transactional
	public CFunction create(final CFunction function) {
		return functionRepository.save(function);
	}

	@Transactional
	public CFunction update(final CFunction function) {
		return functionRepository.save(function);
	}

	public List<CFunction> findAll() {
		return functionRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	}

	public CFunction findByName(String name) {
		return functionRepository.findByName(name);
	}

	public CFunction findByCode(String code) {
		return functionRepository.findByCode(code);
	}

	public CFunction findOne(Long id) {
		return functionRepository.findOne(id);
	}
	public List<CFunction> findAllIsNotLeafTrue() {
		return functionRepository.findByIsNotLeaf(true);
	}
	
	public List<CFunction> search(CFunction function){
		
		
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<CFunction> createQuery = cb.createQuery(CFunction.class);
		Root<CFunction> functions = createQuery.from(CFunction.class);
		createQuery.select(functions);
		Metamodel m = entityManager.getMetamodel();
		javax.persistence.metamodel.EntityType<CFunction> CFunction_ = m.entity(CFunction.class);
    
		List<Predicate> predicates = new ArrayList<Predicate>();
		if(function.getName()!=null)
		{
		String name="%"+function.getName().toLowerCase()+"%";
		predicates.add(cb.isNotNull(functions.get("name")));
		predicates.add(cb.like(cb.lower(functions.get(CFunction_.getDeclaredSingularAttribute("name", String.class))),name));
		}
		if(function.getCode()!=null)
		{
		String code="%"+function.getCode().toLowerCase()+"%";
		predicates.add(cb.isNotNull(functions.get("code")));
		predicates.add(cb.like(cb.lower(functions.get(CFunction_.getDeclaredSingularAttribute("code", String.class))),code));
		}
		if(function.getIsActive()==true)
		{
			predicates.add(cb.equal(functions.get(CFunction_.getDeclaredSingularAttribute("isActive", Boolean.class)),true));
		}
		if(function.getParentId()!=null)
		{
			/*predicates.add(cb.isNotNull(functions.get("id")));
			predicates.add(cb.equal(functions.get(CFunction_.getDeclaredSingularAttribute("id", Long.class)),function.getParentId().getId()));*/
			predicates.add(cb.equal(functions.get("parentId"), function.getParentId()));
		}
		
		
		
		createQuery.where(predicates.toArray(new Predicate[]{}));
		TypedQuery<CFunction> query=entityManager.createQuery(createQuery);

		List<CFunction> resultList = query.getResultList();
		return resultList;
	}
	
	
	
}