package org.egov.tl.domain.service;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.egov.tl.domain.entity.FeeMatrix;
import org.egov.tl.domain.entity.FeeMatrixDetail;
import org.egov.tl.domain.repository.FeeMatrixDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service 
@Transactional(readOnly = true)
public class FeeMatrixDetailService  {

	private final FeeMatrixDetailRepository feeMatrixDetailRepository;
	

	@Autowired
	public FeeMatrixDetailService(final FeeMatrixDetailRepository feeMatrixDetailRepository) {
		this.feeMatrixDetailRepository = feeMatrixDetailRepository;
	}

	public List<FeeMatrixDetail> findAll() {
		return feeMatrixDetailRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	}

	public FeeMatrixDetail findByLicenseFeeByRange(FeeMatrix feeMatrix,
			BigDecimal uom, Date date) {
	return 	feeMatrixDetailRepository.findFeeDetailList(feeMatrix,uom.intValue(),date) ;
		
	}
	
	public FeeMatrixDetail findByFeeMatrixDetailId(Long feeMatrixDetailId) {
            FeeMatrixDetail feeMatrixDetail = feeMatrixDetailRepository.findOne(feeMatrixDetailId);  
            return feeMatrixDetail;  
        }
	
	@Transactional
        public void delete(final FeeMatrixDetail feeMatrixDetail) {
	    feeMatrixDetailRepository.delete(feeMatrixDetail);
        } 
	
	
}