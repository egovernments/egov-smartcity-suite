package org.egov.service;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.commons.CFinancialYear;
import org.egov.commons.repository.CFinancialYearRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service 
@Transactional(readOnly = true)
public class CFinancialYearService  {

	private final CFinancialYearRepository cFinancialYearRepository;
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	public CFinancialYearService(final CFinancialYearRepository cFinancialYearRepository) {
		this.cFinancialYearRepository = cFinancialYearRepository;
	}

	@Transactional
	public CFinancialYear create(final CFinancialYear cFinancialYear) {
		return cFinancialYearRepository.save(cFinancialYear);
	} 
	@Transactional
	public CFinancialYear update(final CFinancialYear cFinancialYear) {
		return cFinancialYearRepository.save(cFinancialYear);
	} 
	public List<CFinancialYear> findAll() {
		return cFinancialYearRepository.findAll(new Sort(Sort.Direction.ASC, "finYearRange"));
	}
	public CFinancialYear findOne(Long id){
		return cFinancialYearRepository.findOne(id);
	}
}
