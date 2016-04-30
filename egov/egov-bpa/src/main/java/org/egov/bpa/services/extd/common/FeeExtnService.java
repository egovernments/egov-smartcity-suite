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
package org.egov.bpa.services.extd.common;

import org.egov.bpa.constants.BpaConstants;
import org.egov.bpa.models.extd.LandBldngZoneingExtn;
import org.egov.bpa.models.extd.masters.BpaFeeDetailExtn;
import org.egov.bpa.models.extd.masters.BpaFeeExtn;
import org.egov.bpa.models.extd.masters.ServiceTypeExtn;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.services.PersistenceService;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Transactional(readOnly=true)
public class FeeExtnService extends PersistenceService<BpaFeeExtn, Long>{
	
	
	private PersistenceService persistenceService;

	public PersistenceService getPersistenceService() {
		return persistenceService;
	}

	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}
	
	
	public List<BpaFeeDetailExtn> getFeeDetailByServiceTypeandArea(Long serviceTypeId, BigDecimal areasqmt, String feeType) {
		Criteria feeCrit = createCriteriaforFeeAmount(serviceTypeId, areasqmt, feeType);
		return feeCrit.list();
	}

	public List<BpaFeeDetailExtn> getListOfFeeDetails(Boolean greaterThanToAreaInSqmt,Boolean checkOnlyFromQty,Long serviceTypeId, String feeCode, BigDecimal areasqmt, String feeType,String feeSubType,LandBldngZoneingExtn landBldgZone,Long floorNmr,String usageType,String additionalTypeFld) {

		Criteria feeCrit=getSession().createCriteria(BpaFeeDetailExtn.class,"bpafeeDtl")
				.createAlias("bpafeeDtl.bpafee", "bpaFeeObj")
				.createAlias("bpaFeeObj.serviceType", "servicetypeObj"); 
		 
		if(usageType!=null)
			feeCrit.createAlias("bpafeeDtl.usageType", "usageTypes"); 

		feeCrit.add(Restrictions.eq("servicetypeObj.id", serviceTypeId));
		feeCrit.add(Restrictions.eq("bpaFeeObj.isActive", Boolean.TRUE));
		if (feeType != null){
			feeCrit.add(Restrictions.ilike("bpaFeeObj.feeType", feeType));
		}
		if (feeSubType != null){
			feeCrit.add(Restrictions.ilike("subType", feeSubType));
		}
		
		if (additionalTypeFld != null){
			feeCrit.add(Restrictions.ilike("additionalType", additionalTypeFld));
		}
		
		if (landBldgZone != null) {
			feeCrit.add(Restrictions.ilike("landUseZone", landBldgZone));
		}
		if (feeCode != null){
			feeCrit.add(Restrictions.ilike("bpaFeeObj.feeCode", feeCode));
		}
		if(floorNmr!=null){
			feeCrit.add(Restrictions.ilike("floorNumber", floorNmr));
		}
		else{
			feeCrit.add(Restrictions.isNull("floorNumber"));
		}
		
		if(usageType!=null){
			feeCrit.add(Restrictions.ilike("usageTypes.name", usageType));
		}
		
		
		if(areasqmt!=null && !checkOnlyFromQty && !greaterThanToAreaInSqmt){
			Criterion fromfeeCriterion=Restrictions.le("fromAreasqmt", areasqmt);
			Criterion tofeeCriterion=Restrictions.ge("toAreasqmt", areasqmt);
			feeCrit.add(Restrictions.or(Restrictions.isNull("fromAreasqmt"), Restrictions.and(fromfeeCriterion, tofeeCriterion)));
		} else if(areasqmt!=null && checkOnlyFromQty){
			
			Criterion fromfeeCriterion=Restrictions.le("fromAreasqmt", areasqmt);
			feeCrit.add(Restrictions.or(Restrictions.isNull("fromAreasqmt"), fromfeeCriterion));
		}else if(areasqmt!=null && greaterThanToAreaInSqmt){
			
			Criterion fromfeeCriterion=Restrictions.ge("toAreasqmt", areasqmt);
			feeCrit.add(Restrictions.or(Restrictions.isNull("toAreasqmt"), fromfeeCriterion));
		}
		feeCrit.add(Restrictions.le("startDate",new Date())).add(Restrictions.or(Restrictions.isNull("endDate"),Restrictions.ge("endDate",new Date())));
		
		if(checkOnlyFromQty||greaterThanToAreaInSqmt)
		{
			feeCrit.addOrder(Order.asc("fromAreasqmt"));
		}
		return feeCrit.list();
	
		
	}

	public BpaFeeDetailExtn getFeeDetails(Long serviceTypeId, String feeCode, BigDecimal areasqmt, String feeType,String feeSubType,LandBldngZoneingExtn landBldgZone,Long floorNmr,String usageType,String additionalTypeFld) {
		Criteria feeCrit=getSession().createCriteria(BpaFeeDetailExtn.class,"bpafeeDtl")
				.createAlias("bpafeeDtl.bpafee", "bpaFeeObj")
				//.createAlias("bpafeeDtl.usageType", "usageTypes")
				.createAlias("bpaFeeObj.serviceType", "servicetypeObj"); 
		 
		if(usageType!=null)
			feeCrit.createAlias("bpafeeDtl.usageType", "usageTypes"); 

		feeCrit.add(Restrictions.eq("servicetypeObj.id", serviceTypeId));
		feeCrit.add(Restrictions.eq("bpaFeeObj.isActive", Boolean.TRUE));
		if (feeType != null){
			feeCrit.add(Restrictions.ilike("bpaFeeObj.feeType", feeType));
		}
		if (feeSubType != null){
			feeCrit.add(Restrictions.ilike("subType", feeSubType));
		}
		
		if (additionalTypeFld != null){
			feeCrit.add(Restrictions.ilike("additionalType", additionalTypeFld));
		}
		
		if (landBldgZone != null) {
			feeCrit.add(Restrictions.ilike("landUseZone", landBldgZone));
		}
		if (feeCode != null){
			feeCrit.add(Restrictions.ilike("bpaFeeObj.feeCode", feeCode));
		}
		if(floorNmr!=null){
			feeCrit.add(Restrictions.ilike("floorNumber", floorNmr));
		}
		else{
			feeCrit.add(Restrictions.isNull("floorNumber"));
		}
		
		if(usageType!=null){
			feeCrit.add(Restrictions.ilike("usageTypes.name", usageType));
		}
		
		
		if(areasqmt!=null){
			Criterion fromfeeCriterion=Restrictions.le("fromAreasqmt", areasqmt);
			Criterion tofeeCriterion=Restrictions.ge("toAreasqmt", areasqmt);
			feeCrit.add(Restrictions.or(Restrictions.isNull("fromAreasqmt"), Restrictions.and(fromfeeCriterion, tofeeCriterion)));
		} 
		feeCrit.add(Restrictions.le("startDate",new Date())).add(Restrictions.or(Restrictions.isNull("endDate"),Restrictions.ge("endDate",new Date())));
		
		return (BpaFeeDetailExtn) feeCrit.uniqueResult();
	}
	
	private Criteria createCriteriaforFeeAmount(Long serviceTypeId,BigDecimal areasqmt, String feeType) {
		
		
		Criteria feeCrit=getSession().createCriteria(BpaFeeDetailExtn.class,"bpafeeDtl")
				.createAlias("bpafeeDtl.bpafee", "bpaFeeObj")
				.createAlias("bpaFeeObj.serviceType", "servicetypeObj"); 
		 
		feeCrit.add(Restrictions.eq("servicetypeObj.id", serviceTypeId));
		feeCrit.add(Restrictions.eq("bpaFeeObj.isActive",Boolean.TRUE));
		
		if(feeType!=null)
			feeCrit.add(Restrictions.ilike("bpaFeeObj.feeType", feeType));
		
		if(areasqmt!=null){
		
			Criterion fromfeeCriterion=Restrictions.le("fromAreasqmt", areasqmt);
			Criterion tofeeCriterion=Restrictions.ge("toAreasqmt", areasqmt);
			Criterion isFixedAmountCriterion=Restrictions.eq("bpaFeeObj.isFixedAmount", Boolean.TRUE);
			Criterion isNotFixedAmountCriterion=Restrictions.eq("bpaFeeObj.isFixedAmount", Boolean.FALSE);
			Criterion feeCriterion =(Restrictions.and(fromfeeCriterion, tofeeCriterion));
			Criterion tofeeisnullCriterion =(Restrictions.and(isNotFixedAmountCriterion, feeCriterion));	
			feeCrit.add(Restrictions.or(isFixedAmountCriterion, tofeeisnullCriterion));
		} 


		feeCrit.add(Restrictions.le("startDate",new Date())).add(Restrictions.or(Restrictions.isNull("endDate"),Restrictions.ge("endDate",new Date())));
		
		
    		//feeCrit.add(Restrictions.and(Restrictions.between("startDate", new Date()), Restrictions.ge("endDate", new Date())));
    
		return feeCrit;
	}
	
	/*
	 * Pass service typeid , area in sqmt and feetype to get the amount. Returns
	 * zero as default value. Assumption : if the isfixedamount field value is
	 * true, then no need to check sqmt validation with fromAreasqmt and
	 * toAreasqmt fields.
	 */
	public BigDecimal getTotalFeeAmountByPassingServiceTypeandArea(Long serviceTypeId,
			BigDecimal areasqmt, String feeType) throws EGOVRuntimeException {
		BigDecimal totalAmount = BigDecimal.ZERO;

		List<BpaFeeDetailExtn> bpaFeeDetails = new ArrayList<BpaFeeDetailExtn>();
		ServiceTypeExtn service = null;
		if (serviceTypeId != null) {
			Criteria feeCrit = createCriteriaforFeeAmount(serviceTypeId,
					areasqmt, feeType);
			bpaFeeDetails = feeCrit.list();

			for (BpaFeeDetailExtn feeDetail : bpaFeeDetails) {
				totalAmount=totalAmount.add(feeDetail.getAmount());
			}

		} else
			throw new EGOVRuntimeException("Service Type Id is mandatory.");

		return totalAmount;
	}
	
	
	public List<BpaFeeExtn> getAllSanctionedFeesbyServiceType(Long serviceTypeId){
		
		Criteria feeCrit = bpaFeeDetailCriteria(serviceTypeId);
		return feeCrit.list();
		
	}

	private Criteria bpaFeeDetailCriteria(Long serviceTypeId) {
		Criteria feeCrit=getSession().createCriteria(BpaFeeExtn.class,"bpafee")
		.createAlias("bpafee.serviceType", "servicetypeObj"); 
		feeCrit.add(Restrictions.eq("feeType", BpaConstants.SANCTIONEDFEE));
		feeCrit.add(Restrictions.eq("servicetypeObj.id", serviceTypeId));
		feeCrit.addOrder(Order.asc("feeDescription"));
		return feeCrit;
	}
	public List<BpaFeeExtn> getAllMandatorySanctionedFeesbyServiceType(Long serviceTypeId){
		
		Criteria feeCrit = bpaFeeDetailCriteria(serviceTypeId);
		feeCrit.add(Restrictions.eq("isMandatory", Boolean.TRUE));
		return feeCrit.list();
		
	}

	public List<BpaFeeExtn> getAllSanctionedFeesbyServiceTypeSortByOrderNumber(Long serviceTypeId){
		
		Criteria feeCrit = bpaFeeDetailCriteria(serviceTypeId);
		//feeCrit.addOrder(Order.asc("orderNumber")); 
		return feeCrit.list();
		
	}


}
