package org.egov.service;


import java.util.List;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.commons.EgfRecordStatus;
import org.egov.repository.EgfRecordStatusRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service 
@Transactional(readOnly = true)
public class EgfRecordStatusService  {

	private final EgfRecordStatusRepository egfRecordStatusRepository;
	@PersistenceContext
private EntityManager entityManager;

	@Autowired
public EgfRecordStatusService(final EgfRecordStatusRepository egfRecordStatusRepository) {
	 this.egfRecordStatusRepository = egfRecordStatusRepository;
  }

	 @Transactional
	 public EgfRecordStatus create(final EgfRecordStatus egfRecordStatus) {
	return egfRecordStatusRepository.save(egfRecordStatus);
  } 
	 @Transactional
	 public EgfRecordStatus update(final EgfRecordStatus egfRecordStatus) {
	return egfRecordStatusRepository.save(egfRecordStatus);
	  } 
	public List<EgfRecordStatus> findAll() {
	 return egfRecordStatusRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	   }
	public EgfRecordStatus findOne(Long id){
	return egfRecordStatusRepository.findOne(id);
	}
}
