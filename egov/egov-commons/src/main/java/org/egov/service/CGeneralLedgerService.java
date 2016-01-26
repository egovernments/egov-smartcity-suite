package org.egov.service;


import java.util.List;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.commons.CGeneralLedger;
import org.egov.repository.CGeneralLedgerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service 
@Transactional(readOnly = true)
public class CGeneralLedgerService  {

	private final CGeneralLedgerRepository cGeneralLedgerRepository;
	@PersistenceContext
private EntityManager entityManager;

	@Autowired
public CGeneralLedgerService(final CGeneralLedgerRepository cGeneralLedgerRepository) {
	 this.cGeneralLedgerRepository = cGeneralLedgerRepository;
  }

	 @Transactional
	 public CGeneralLedger create(final CGeneralLedger cGeneralLedger) {
	return cGeneralLedgerRepository.save(cGeneralLedger);
  } 
	 @Transactional
	 public CGeneralLedger update(final CGeneralLedger cGeneralLedger) {
	return cGeneralLedgerRepository.save(cGeneralLedger);
	  } 
	public List<CGeneralLedger> findAll() {
	 return cGeneralLedgerRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	   }
	public CGeneralLedger findOne(Long id){
	return cGeneralLedgerRepository.findOne(id);
	}
}
