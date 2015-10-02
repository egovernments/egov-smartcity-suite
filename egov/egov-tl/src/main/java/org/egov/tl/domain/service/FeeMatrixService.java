package org.egov.tl.domain.service;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.tl.domain.entity.FeeMatrix;
import org.egov.tl.domain.entity.FeeMatrixDetail;
import org.egov.tl.domain.repository.FeeMatrixRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service 
@Transactional(readOnly = true)
public class FeeMatrixService  {

	private final FeeMatrixRepository feeMatrixRepository;
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	public FeeMatrixService(final FeeMatrixRepository feeMatrixRepository) {
		this.feeMatrixRepository = feeMatrixRepository;
	}

	@Transactional
	public FeeMatrix create(final FeeMatrix feeMatrix) {
		
		String genUniqueNo = feeMatrix.genUniqueNo();
		feeMatrix.setUniqueNo(genUniqueNo);
		if(!feeMatrix.getFeeMatrixDetail().isEmpty())
		{
			for(FeeMatrixDetail fd:feeMatrix.getFeeMatrixDetail())
			{
				fd.setFeeMatrix(feeMatrix);
			}
		}
		return feeMatrixRepository.save(feeMatrix);
	} 
	@Transactional
	public FeeMatrix update(final FeeMatrix feeMatrix) {
		String genUniqueNo = feeMatrix.genUniqueNo();
		feeMatrix.setUniqueNo(genUniqueNo);
		return feeMatrixRepository.save(feeMatrix);
	} 
	public List<FeeMatrix> findAll() {
		return feeMatrixRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	}
	/*public FeeMatrix findByName(String name){
		return feeMatrixRepository.findByName(name);
	}*/

	public FeeMatrix search(FeeMatrix feeMatrix) {
		return feeMatrixRepository.findByExample(feeMatrix);	
	}
}