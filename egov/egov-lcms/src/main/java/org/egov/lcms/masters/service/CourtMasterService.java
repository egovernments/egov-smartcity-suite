package org.egov.lcms.masters.service;


import java.util.List;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.lcms.masters.entity.CourtMaster;
import org.egov.lcms.masters.repository.CourtMasterRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service 
@Transactional(readOnly = true)
public class CourtMasterService  {

	private final CourtMasterRepository courtMasterRepository;
	@PersistenceContext
private EntityManager entityManager;

	@Autowired
public CourtMasterService(final CourtMasterRepository courtMasterRepository) {
	 this.courtMasterRepository = courtMasterRepository;
  }

	 @Transactional
	 public CourtMaster create(final CourtMaster courtMaster) {
	return courtMasterRepository.save(courtMaster);
  } 
	 @Transactional
	 public CourtMaster update(final CourtMaster courtMaster) {
	return courtMasterRepository.save(courtMaster);
	  } 
	public List<CourtMaster> findAll() {
	 return courtMasterRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	   }
	public CourtMaster findByName(String name){
	return courtMasterRepository.findByName(name);
	}
	public CourtMaster findOne(Long id){
	return courtMasterRepository.findOne(id);
	}
	public List<CourtMaster> search(CourtMaster courtMaster){
	return courtMasterRepository.findAll();
	}
}