package org.egov.tl.domain.service;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.commons.CFinancialYear;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.tl.domain.entity.FeeMatrix;
import org.egov.tl.domain.entity.FeeMatrixDetail;
import org.egov.tl.domain.entity.FeeType;
import org.egov.tl.domain.entity.License;
import org.egov.tl.domain.entity.LicenseAppType;
import org.egov.tl.domain.entity.TradeLicense;
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
	private FeeTypeService feeTypeService;
	
	@Autowired
	private FeeMatrixDetailService feeMatrixDetailService;
	

	@Autowired
	public FeeMatrixService(final FeeMatrixRepository feeMatrixRepository) {
		this.feeMatrixRepository = feeMatrixRepository;
	}
	
    @Autowired
    private FinancialYearDAO financialYearDAO;

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
		
		return feeMatrixRepository.findByUniqueNo(feeMatrix.getUniqueNo());
	}
/**
 * 
 * @param license
 * @return
 * Will return the list of fees for the selected combination
 * 1.It will fetch all fee types  from the system
 * 2. Apply the rule and parameters for that 
 * 3. return it by adding to the feeMatrixDetailList
 * 4. Uses switch . So After adding feetype in the system 
 * it should be coded here to say how what parameter to be applied for the fetch  
 * 
 */
	public List<FeeMatrixDetail> findFeeList(TradeLicense license) {
	
		List<FeeType> allFees = feeTypeService.findAll();
	 
		
	    Long uomId = license.getUom().getId();
	    // Make decision is charges for NEW and Renew Same
	    
	    // Make decision is charges for Permanent  and Temporary are Same

	    String uniqueNo = generateFeeMatirixUniqueNo(license);
	    if(uniqueNo==null)
	    {
	    
	    	
	    }
	    Date applicationDate = license.getApplicationDate();
	    
	    CFinancialYear financialYearByDate=financialYearDAO.getFinancialYearByDate(applicationDate);
		
	    BigDecimal totalFee=BigDecimal.ZERO;
	    List<FeeMatrixDetail> feeMatrixDetailList=new ArrayList<FeeMatrixDetail>();
	    FeeMatrixDetail feeMatrixDetail=null;
	    FeeMatrix feeMatrix=null;
	    for(FeeType fee:allFees)
	    {
	    	if(fee.getFeeProcessType().equals(FeeType.FeeProcessType.RANGE))
	    	{

	    switchLoop:switch(fee.getCode())
	    		{
	    		//First find License Fee with UOM		
	    		case "LF" : 
	    			feeMatrix = feeMatrixRepository.findByUniqueNo(uniqueNo+"-"+fee.getId()+"-"+uomId+"-"+financialYearByDate.getId());
	    			if(feeMatrix==null)
	    			{
	    				throw new ApplicationRuntimeException("License Fee Structure  is not defined for the selected combination");
	    			}
	    			feeMatrixDetail = feeMatrixDetailService.findByLicenseFeeByRange(feeMatrix,license.getTradeArea_weight(),license.getApplicationDate(),financialYearByDate.getId());
	    			if(feeMatrixDetail==null)
	    			{
	    				throw new ApplicationRuntimeException("License Fee Structure range is not defined for the selected combination");
	    			}
	    			feeMatrixDetailList.add(feeMatrixDetail);
	    			break switchLoop;
	    			
	    			
	    		/**
	    		 * Assuming the below fee types will have single UOM through out So exclude UOM and find
	    		 */
	    		case "MF":
	    			if(license.getTotalHP()!=null && license.getTotalHP().compareTo(BigDecimal.ZERO)==1 )
	    			{
	    				feeMatrix = feeMatrixRepository.findByUniqueNoLike(uniqueNo+"-"+fee.getId()+"%"+"-"+financialYearByDate.getId());
	    				if(feeMatrix==null)
	    				{
	    					throw new ApplicationRuntimeException("Motor Fee Structure  is not defined for the selected combination");
	    				}
	    				feeMatrixDetail = feeMatrixDetailService.findByLicenseFeeByRange(feeMatrix,license.getTotalHP(),license.getApplicationDate(),financialYearByDate.getId());
	    				if(feeMatrixDetail==null)
	    				{
	    					throw new ApplicationRuntimeException("Motor Fee Structure range is not defined for the entered capacity");
	    				}
	    				feeMatrixDetailList.add(feeMatrixDetail);
	    			}
	    			break switchLoop;

	    			//Find Work force fee    
	    		case "WF" :
	    			if(license.getWorkersCapacity()!=null && license.getWorkersCapacity().compareTo(BigDecimal.ZERO)==1 )
	    			{
	    			feeMatrix = feeMatrixRepository.findByUniqueNoLike(uniqueNo+"-"+fee.getId()+"%"+"-"+financialYearByDate.getId());
	    			if(feeMatrix==null)
	    			{
	    				throw new ApplicationRuntimeException("Workforce Fee Structure  is not defined for the selected combination");
	    			}
	    			feeMatrixDetail=feeMatrixDetailService.findByLicenseFeeByRange(feeMatrix,license.getTradeArea_weight(),license.getApplicationDate(),financialYearByDate.getId());
	    			if(feeMatrixDetail==null)
	    			{
	    				throw new ApplicationRuntimeException("Workforce Fee Structure range is not defined for the entered capacity");
	    			}
	    			
	    		//	totalFee=	totalFee.add(feeMatrixDetail.getAmount());
	    			feeMatrixDetailList.add(feeMatrixDetail);
	    			}
	    			break switchLoop;
	    		}
				
				
			  	
			}
		}
		
	    return feeMatrixDetailList;	
	}

	private String generateFeeMatirixUniqueNo(License license) {
		
		    return license.getBuildingType().getId()+"-"+license.getLicenseAppType().getId()+"-"+license.getCategory().getId()
		    +"-"+license.getTradeName().getId();
		
	}
}