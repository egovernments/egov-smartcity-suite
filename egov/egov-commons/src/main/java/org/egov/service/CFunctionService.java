package org.egov.service;


import java.util.List;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.commons.CFunction;
import org.egov.repository.CFunctionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service 
@Transactional(readOnly = true)
public class CFunctionService  {

	private final CFunctionRepository cFunctionRepository;
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	public CFunctionService(final CFunctionRepository cFunctionRepository) {
		this.cFunctionRepository = cFunctionRepository;
	}

	@Transactional
	public CFunction create(final CFunction cFunction) {
		return cFunctionRepository.save(cFunction);
	} 
	@Transactional
	public CFunction update(final CFunction cFunction) {
		return cFunctionRepository.save(cFunction);
	} 
	public List<CFunction> findAll() {
		return cFunctionRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	}
	public CFunction findByName(String name){
		return cFunctionRepository.findByName(name);
	}
	public CFunction findByCode(String code){
		return cFunctionRepository.findByCode(code);
	}
	public CFunction findOne(Long id){
		return cFunctionRepository.findOne(id);
	}
}
