package org.egov.service;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.commons.Bank;
import org.egov.repository.BankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service 
@Transactional(readOnly = true)
public class BankService  {

	private final BankRepository bankRepository;
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	public BankService(final BankRepository bankRepository) {
		this.bankRepository = bankRepository;
	}

	@Transactional
	public Bank create(final Bank bank) {
		return bankRepository.save(bank);
	} 
	@Transactional
	public Bank update(final Bank bank) {
		return bankRepository.save(bank);
	} 
	public List<Bank> findAll() {
		return bankRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	}
	public Bank findByName(String name){
		return bankRepository.findByName(name);
	}
	public Bank findByCode(String code){
		return bankRepository.findByCode(code);
	}
	public Bank findOne(Long id){
		return bankRepository.findOne(id);
	}
}
