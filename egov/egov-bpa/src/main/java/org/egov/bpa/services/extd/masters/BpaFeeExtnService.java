/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.bpa.services.extd.masters;


import org.egov.bpa.constants.BpaConstants;
import org.egov.bpa.models.extd.masters.BpaFeeDetailExtn;
import org.egov.bpa.models.extd.masters.BpaFeeExtn;
import org.egov.bpa.models.extd.masters.ServiceTypeExtn;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.services.PersistenceService;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Transactional(readOnly=true)
public class BpaFeeExtnService extends PersistenceService<BpaFeeExtn, Long>
{
	
	
	@SuppressWarnings("rawtypes")
	private PersistenceService persistenceService;
	BpaFeeExtn bpafee=new BpaFeeExtn();
	
	public boolean checkCode(String code,Long id) 
	{
		BpaFeeExtn relation = null;
		boolean nameExistsOrNot = false;
		if(code!=null && !"".equals(code) && id!=null)
			relation =(BpaFeeExtn)persistenceService.find("from BpaFeeExtn where feeCode=? and id!=?",code,id);
		
		else if(code!=null && !"".equals(code))
			relation =(BpaFeeExtn)persistenceService.find("from BpaFeeExtn where feeCode=? ",code);
	
		if(relation!=null) {
			nameExistsOrNot=true;
			code=""; 
		}
		return nameExistsOrNot;
	}

	
	public boolean checkcombination(String feeTypeid,Long ServiceId,String desc) 
	{
		BpaFeeExtn relation = null;
			boolean nameExistsOrNot = false;
			if(feeTypeid!="-1" && !"".equals(feeTypeid) && ServiceId!=-1 && !"".equals(ServiceId) && desc!="" && !"".equals(desc))
				relation =(BpaFeeExtn)persistenceService.find("from BpaFeeExtn where feeType=? and serviceType.id=? and feeDescription=?" ,feeTypeid,ServiceId,desc);
				if(relation!=null) {
				nameExistsOrNot=true;
				
			}
			return nameExistsOrNot;
		}

public Long getMaxFeeOrderNum(Long serviceTypeId){
		
		Criteria feeCrit=getSession().createCriteria(BpaFeeExtn.class,"bpafee")
		.createAlias("bpafee.serviceType", "servicetypeObj"); 
		feeCrit.setProjection(Projections.max("orderNumber"));
		feeCrit.add(Restrictions.eq("feeType", BpaConstants.SANCTIONEDFEE));
		feeCrit.add(Restrictions.eq("servicetypeObj.id", serviceTypeId));
		return (Long) feeCrit.uniqueResult();
	}
		
	@Transactional
	public BpaFeeExtn save(BpaFeeExtn bpafee,List<BpaFeeDetailExtn> feeDetailList,BigDecimal fixedPrice,String mode)
	{
		
    	bpafee.setModifiedDate(new Date());
    	 if(bpafee.getIsMandatory()==null){
		bpafee.setIsMandatory(false); 
    	 }
    	 //---logic for ordernum begins------
    	 if(!mode.equals("edit") && !mode.equals(BpaConstants.MODEVIEW)){
    	Long maxOrderNum= getMaxFeeOrderNum( bpafee.getServiceType().getId());
    	if(maxOrderNum==null)
    	{
    		bpafee.setOrderNumber(new Long(0));
    	}else{
    		bpafee.setOrderNumber(maxOrderNum+1);
    	}
    	 }else
    	 {
    		 bpafee.setOrderNumber(bpafee.getOrderNumber());
    	 }//---ordernum ends-----
     	bpafee=buildFeeDetails(bpafee,feeDetailList,fixedPrice);
     	//bpafee.getId();
		if(bpafee.getId()!=null)
			bpafee=merge(bpafee);			
		else
			bpafee=persist(bpafee);
		
	
		return bpafee;
	}

	private BpaFeeExtn buildFeeDetails(BpaFeeExtn bpafee,List<BpaFeeDetailExtn> unitDetail, BigDecimal fixedPrice)
	{
		Set<BpaFeeDetailExtn> unitSet=new HashSet<BpaFeeDetailExtn>();
    	
			for(BpaFeeDetailExtn unitdetail:unitDetail)
	    	{
				
	    		unitdetail.setBpafee(bpafee);
	    	
	    		if(bpafee.getIsFixedAmount() && fixedPrice!=null && bpafee!=null &&  bpafee.getFeeType()!=null && bpafee.getFeeType().contentEquals("AdmissionFee"))
	    		    		unitdetail.setAmount(fixedPrice);
	    		unitSet.add(unitdetail);
	    	}	
		
	/*	for(BpaFeeDetail unitdetail:unitDetail)
    	{
    		unitdetail.setBpafee(bpafee);
    		unitSet.add(unitdetail);
    	}
    */	//bpafee.getFeeType();
    	bpafee.getFeedetailsesList().clear();
   	 if(bpafee.getFeeType().contentEquals("Sanction Fees")){
   		bpafee.getFeedetailsesList().removeAll(unitSet);
   		
   	 }
   	 else
   	 {
    	bpafee.getFeedetailsesList().addAll(unitSet);
   	 }	
    	return bpafee;
    	
	}

	public BpaFeeExtn getFeeById(Long bpafeeId)
	{  
		/* bpafee= (BpaFee) persistenceService.find("from BpaFee where id=? ", bpafeeId);
		return bpafee;*/
	if(bpafeeId==null)
				throw  new EGOVRuntimeException("Fees Type Id is null");
			return findById(bpafeeId);
    }
	
	
	public PersistenceService getPersistenceService() {
		return persistenceService;
	}

	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}


	public boolean checkFeeDescriptionByFeeAndServiceType(
			String feeDescription,ServiceTypeExtn serviceType,
			Long id) {
		BpaFeeExtn result = null;
		boolean descAlreadyExists = false;
		
		if( id!=null ){
			if(feeDescription!=null && !"".equals(feeDescription) && serviceType!=null && serviceType.getId()!=null )
				result =(BpaFeeExtn)persistenceService.find("from BpaFeeExtn where lower(feeDescription)=? and serviceType.id=?  and id!=?",feeDescription.toLowerCase(),serviceType.getId(),id);
			
		}else{
			if(feeDescription!=null && !"".equals(feeDescription) && serviceType!=null && serviceType.getId()!=null )
				result =(BpaFeeExtn)persistenceService.find("from BpaFeeExtn where lower(feeDescription)=? and serviceType.id=? ",feeDescription.toLowerCase(),serviceType.getId());
		    }
		
		if(result!=null) {
			descAlreadyExists=true;
		 }
		return descAlreadyExists;
	}

	
	
	
	
}