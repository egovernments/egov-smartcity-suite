package org.egov.works.workorder.service;


import java.util.List;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.works.workorder.entity.WorkOrderMeasurementSheet;
import org.egov.works.workorder.repository.WorkOrderMeasurementSheetRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service 
@Transactional(readOnly = true)
public class WorkOrderMeasurementSheetService  {

	private final WorkOrderMeasurementSheetRepository workOrderMeasurementSheetRepository;
	@PersistenceContext
private EntityManager entityManager;

	@Autowired
public WorkOrderMeasurementSheetService(final WorkOrderMeasurementSheetRepository workOrderMeasurementSheetRepository) {
	 this.workOrderMeasurementSheetRepository = workOrderMeasurementSheetRepository;
  }

	 @Transactional
	 public WorkOrderMeasurementSheet create(final WorkOrderMeasurementSheet workOrderMeasurementSheet) {
	return workOrderMeasurementSheetRepository.save(workOrderMeasurementSheet);
  } 
	 @Transactional
	 public WorkOrderMeasurementSheet update(final WorkOrderMeasurementSheet workOrderMeasurementSheet) {
	return workOrderMeasurementSheetRepository.save(workOrderMeasurementSheet);
	  } 
	public List<WorkOrderMeasurementSheet> findAll() {
	 return workOrderMeasurementSheetRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	   }
	public WorkOrderMeasurementSheet findOne(Long id){
	return workOrderMeasurementSheetRepository.findOne(id);
	}
	public List<WorkOrderMeasurementSheet> search(WorkOrderMeasurementSheet workOrderMeasurementSheet){
	return workOrderMeasurementSheetRepository.findAll();
	}
}