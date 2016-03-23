package org.egov.commons.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.commons.Accountdetailtype;
import org.egov.commons.CFunction;
import org.egov.commons.repository.FunctionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	
	public List<CFunction> search(CFunction function){
		if(function.getName()!=null && function.getCode()!=null)
		{
			return functionRepository.findByNameContainingIgnoreCaseAndCodeContainingIgnoreCase(function.getName(),function.getCode());
		}else if(function.getName()!=null)
		{
			return functionRepository.findByNameContainingIgnoreCase(function.getName());
		}else if(function.getCode()!=null)
		{
			return functionRepository.findByCodeContainingIgnoreCase(function.getCode());
		}
		else
		{
		return functionRepository.findAll();
		}
	}
}