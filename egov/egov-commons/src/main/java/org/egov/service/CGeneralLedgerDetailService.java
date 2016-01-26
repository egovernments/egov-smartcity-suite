package org.egov.service;


import java.util.List;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.commons.CGeneralLedgerDetail;
import org.egov.repository.CGeneralLedgerDetailRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service 
@Transactional(readOnly = true)
public class CGeneralLedgerDetailService  {

	private final CGeneralLedgerDetailRepository cGeneralLedgerDetailRepository;
	@PersistenceContext
private EntityManager entityManager;

	@Autowired
public CGeneralLedgerDetailService(final CGeneralLedgerDetailRepository cGeneralLedgerDetailRepository) {
	 this.cGeneralLedgerDetailRepository = cGeneralLedgerDetailRepository;
  }

	 @Transactional
	 public CGeneralLedgerDetail create(final CGeneralLedgerDetail cGeneralLedgerDetail) {
	return cGeneralLedgerDetailRepository.save(cGeneralLedgerDetail);
  } 
	 @Transactional
	 public CGeneralLedgerDetail update(final CGeneralLedgerDetail cGeneralLedgerDetail) {
	return cGeneralLedgerDetailRepository.save(cGeneralLedgerDetail);
	  } 
	public List<CGeneralLedgerDetail> findAll() {
	 return cGeneralLedgerDetailRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	   }
	public CGeneralLedgerDetail findOne(Long id){
	return cGeneralLedgerDetailRepository.findOne(id);
	}
}
