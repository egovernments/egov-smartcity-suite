package org.egov.tl.domain.service;


import java.util.List;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.tl.domain.entity.FeeType;
import org.egov.tl.domain.repository.FeeTypeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service 
@Transactional(readOnly = true)
public class FeeTypeService  {

	private final FeeTypeRepository feeTypeRepository;
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	public FeeTypeService(final FeeTypeRepository feeTypeRepository) {
		this.feeTypeRepository = feeTypeRepository;
	}

	@Transactional
	public FeeType create(final FeeType feeType) {
		return feeTypeRepository.save(feeType);
	} 
	@Transactional
	public FeeType update(final FeeType feeType) {
		return feeTypeRepository.save(feeType);
	} 
	public List<FeeType> findAll() {
		return feeTypeRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	}
	public FeeType findByName(String name){
		return feeTypeRepository.findByName(name);
	}
	public FeeType findByCode(String code){
		return feeTypeRepository.findByCode(code);
	}
}