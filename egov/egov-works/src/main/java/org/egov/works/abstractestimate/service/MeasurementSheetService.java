package org.egov.works.abstractestimate.service;


import java.util.List;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.works.abstractestimate.entity.MeasurementSheet;
import org.egov.works.abstractestimate.repository.MeasurementSheetRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service 
@Transactional(readOnly = true)
public class MeasurementSheetService  {

	private final MeasurementSheetRepository measurementSheetRepository;
	@PersistenceContext
private EntityManager entityManager;

	@Autowired
public MeasurementSheetService(final MeasurementSheetRepository measurementSheetRepository) {
	 this.measurementSheetRepository = measurementSheetRepository;
  }

	 @Transactional
	 public MeasurementSheet create(final MeasurementSheet measurementSheet) {
	return measurementSheetRepository.save(measurementSheet);
  } 
	 @Transactional
	 public MeasurementSheet update(final MeasurementSheet measurementSheet) {
	return measurementSheetRepository.save(measurementSheet);
	  } 
	public List<MeasurementSheet> findAll() {
	 return measurementSheetRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	   }
	public MeasurementSheet findOne(Long id){
	return measurementSheetRepository.findOne(id);
	}
	public List<MeasurementSheet> search(MeasurementSheet measurementSheet){
	return measurementSheetRepository.findAll();
	}
}