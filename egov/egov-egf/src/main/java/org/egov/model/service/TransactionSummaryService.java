package org.egov.model.service;


import java.util.List;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.model.contra.TransactionSummary;
import org.egov.model.repository.TransactionSummaryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service 
@Transactional(readOnly = true)
public class TransactionSummaryService  {

	private final TransactionSummaryRepository transactionSummaryRepository;
	@PersistenceContext
private EntityManager entityManager;

	@Autowired
public TransactionSummaryService(final TransactionSummaryRepository transactionSummaryRepository) {
	 this.transactionSummaryRepository = transactionSummaryRepository;
  }

	 @Transactional
	 public TransactionSummary create(final TransactionSummary transactionSummary) {
	return transactionSummaryRepository.save(transactionSummary);
  } 
	 @Transactional
	 public TransactionSummary update(final TransactionSummary transactionSummary) {
	return transactionSummaryRepository.save(transactionSummary);
	  } 
	public List<TransactionSummary> findAll() {
	 return transactionSummaryRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	   }
	public TransactionSummary findOne(Long id){
	return transactionSummaryRepository.findOne(id);
	}
}