package org.egov.service;


import java.util.List;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.commons.CVoucherHeader;
import org.egov.repository.CVoucherHeaderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service 
@Transactional(readOnly = true)
public class CVoucherHeaderService  {

	private final CVoucherHeaderRepository cVoucherHeaderRepository;
	@PersistenceContext
private EntityManager entityManager;

	@Autowired
public CVoucherHeaderService(final CVoucherHeaderRepository cVoucherHeaderRepository) {
	 this.cVoucherHeaderRepository = cVoucherHeaderRepository;
  }

	 @Transactional
	 public CVoucherHeader create(final CVoucherHeader cVoucherHeader) {
	return cVoucherHeaderRepository.save(cVoucherHeader);
  } 
	 @Transactional
	 public CVoucherHeader update(final CVoucherHeader cVoucherHeader) {
	return cVoucherHeaderRepository.save(cVoucherHeader);
	  } 
	public List<CVoucherHeader> findAll() {
	 return cVoucherHeaderRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	   }
	public CVoucherHeader findByName(String name){
	return cVoucherHeaderRepository.findByName(name);
	}
	public CVoucherHeader findOne(Long id){
	return cVoucherHeaderRepository.findOne(id);
	}
}
