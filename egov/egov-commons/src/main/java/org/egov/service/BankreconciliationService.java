package org.egov.service;


import java.util.List;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.commons.Bankreconciliation;
import org.egov.repository.BankreconciliationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service 
@Transactional(readOnly = true)
public class BankreconciliationService  {

	private final BankreconciliationRepository bankreconciliationRepository;
	@PersistenceContext
private EntityManager entityManager;

	@Autowired
public BankreconciliationService(final BankreconciliationRepository bankreconciliationRepository) {
	 this.bankreconciliationRepository = bankreconciliationRepository;
  }

	 @Transactional
	 public Bankreconciliation create(final Bankreconciliation bankreconciliation) {
	return bankreconciliationRepository.save(bankreconciliation);
  } 
	 @Transactional
	 public Bankreconciliation update(final Bankreconciliation bankreconciliation) {
	return bankreconciliationRepository.save(bankreconciliation);
	  } 
	public List<Bankreconciliation> findAll() {
	 return bankreconciliationRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	   }
	public Bankreconciliation findOne(Long id){
	return bankreconciliationRepository.findOne(id);
	}
}
