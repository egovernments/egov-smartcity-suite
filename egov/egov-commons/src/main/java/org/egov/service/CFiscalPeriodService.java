package org.egov.service;


import java.util.List;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.commons.CFiscalPeriod;
import org.egov.repository.CFiscalPeriodRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service 
@Transactional(readOnly = true)
public class CFiscalPeriodService  {

	private final CFiscalPeriodRepository cFiscalPeriodRepository;
	@PersistenceContext
private EntityManager entityManager;

	@Autowired
public CFiscalPeriodService(final CFiscalPeriodRepository cFiscalPeriodRepository) {
	 this.cFiscalPeriodRepository = cFiscalPeriodRepository;
  }

	 @Transactional
	 public CFiscalPeriod create(final CFiscalPeriod cFiscalPeriod) {
	return cFiscalPeriodRepository.save(cFiscalPeriod);
  } 
	 @Transactional
	 public CFiscalPeriod update(final CFiscalPeriod cFiscalPeriod) {
	return cFiscalPeriodRepository.save(cFiscalPeriod);
	  } 
	public List<CFiscalPeriod> findAll() {
	 return cFiscalPeriodRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	   }
	public CFiscalPeriod findByName(String name){
	return cFiscalPeriodRepository.findByName(name);
	}
	public CFiscalPeriod findOne(Long id){
	return cFiscalPeriodRepository.findOne(id);
	}
}
