package org.egov.works.mb.service;


import java.util.List;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.works.mb.entity.MBMeasurementSheet;
import org.egov.works.mb.repository.MBMeasurementSheetRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service 
@Transactional(readOnly = true)
public class MBMeasurementSheetService  {

	private final MBMeasurementSheetRepository mBMeasurementSheetRepository;
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	public MBMeasurementSheetService(final MBMeasurementSheetRepository mBMeasurementSheetRepository) {
		this.mBMeasurementSheetRepository = mBMeasurementSheetRepository;
	}

	@Transactional
	public MBMeasurementSheet create(final MBMeasurementSheet mBMeasurementSheet) {
		return mBMeasurementSheetRepository.save(mBMeasurementSheet);
	} 
	@Transactional
	public MBMeasurementSheet update(final MBMeasurementSheet mBMeasurementSheet) {
		return mBMeasurementSheetRepository.save(mBMeasurementSheet);
	} 
	public List<MBMeasurementSheet> findAll() {
		return mBMeasurementSheetRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	}
	public MBMeasurementSheet findOne(Long id){
		return mBMeasurementSheetRepository.findOne(id);
	}
	public List<MBMeasurementSheet> search(MBMeasurementSheet mBMeasurementSheet){
		return mBMeasurementSheetRepository.findAll();
	}
}