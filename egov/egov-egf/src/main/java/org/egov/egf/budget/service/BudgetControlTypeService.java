package org.egov.egf.budget.service;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.egf.budget.model.BudgetControlType;
import org.egov.egf.budget.repository.BudgetControlTypeRepository;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service 
@Transactional(readOnly = true)
public class BudgetControlTypeService  {

	private final BudgetControlTypeRepository budgetControlTypeRepository;
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	public BudgetControlTypeService(final BudgetControlTypeRepository budgetCheckConfigRepository) {
		this.budgetControlTypeRepository = budgetCheckConfigRepository;
	}

	@Transactional
	public BudgetControlType create(final BudgetControlType budgetCheckConfig) {
		return budgetControlTypeRepository.save(budgetCheckConfig);
	} 
	@Transactional
	public BudgetControlType update(final BudgetControlType budgetCheckConfig) {
		return budgetControlTypeRepository.saveAndFlush(budgetCheckConfig);
	} 
	public List<BudgetControlType> findAll() {
		return budgetControlTypeRepository.findAll(new Sort(Sort.Direction.ASC, "value"));
	}
	public BudgetControlType findOne(Long id){
		return budgetControlTypeRepository.findOne(id);
	}
	public List<BudgetControlType> search(BudgetControlType budgetCheckConfig){
		return budgetControlTypeRepository.findAll();
	}
	
	public String getConfigValue()
	{
		List<BudgetControlType> configs = findAll();
		if(configs.size()==1)
		{
			return configs.get(0).getValue();
		}else if(configs.size()==0)
		{
			throw new ApplicationRuntimeException("Budget Check Configuration not defined");
		}else{
			throw new ApplicationRuntimeException("Multiple Budget Check Configurations  defined");
		}
	}
	
}