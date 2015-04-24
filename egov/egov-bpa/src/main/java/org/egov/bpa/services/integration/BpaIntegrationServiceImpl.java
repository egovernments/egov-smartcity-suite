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
package org.egov.bpa.services.integration;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.egov.bpa.constants.BpaConstants;
import org.egov.bpa.models.Registration;
import org.egov.bpa.models.ReportFeesDetails;
import org.egov.bpa.models.masters.BpaFee;
import org.egov.demand.dao.DemandGenericDao;
import org.egov.demand.dao.DemandGenericHibDao;
import org.egov.demand.model.BillReceipt;
import org.egov.demand.model.EgDemand;
import org.egov.erpcollection.integration.models.BillReceiptInfo;
import org.egov.erpcollection.integration.models.ReceiptAccountInfo;
import org.egov.erpcollection.integration.services.CollectionIntegrationServiceImpl;
import org.egov.infstr.bpa.services.BpaIntegrationService;
import org.egov.infstr.services.PersistenceService;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class BpaIntegrationServiceImpl implements BpaIntegrationService {
	
	private Logger LOGGER = Logger.getLogger(getClass());
	private PersistenceService persistenceService;
	private static final String STATUSCODE    		    =    "statusObj.code";
	private static final String STATUSMODULE            =    "statusObj.moduletype";
	private CollectionIntegrationServiceImpl collectionIntegrationService;
	@Override
	//Returns map with key being BPA number and value being the validity of the BPA number
	public Map<String, Boolean> getListOfValidBuildingPlanApprovalNumbers(
			List<String> bpaNumberList) {
		// TODO Auto-generated method stub
		Map<String,Boolean> bpaDataMap = new HashMap<String, Boolean>();
		if(bpaNumberList!=null && !bpaNumberList.isEmpty()){
			for(String bpaNo : bpaNumberList){
				if(bpaNo!=null && bpaNo!=""){
					bpaDataMap.put(bpaNo, isValidBuildingPlanApprovalNumber(bpaNo));
				}
			}
		}
		return bpaDataMap;
	}


	@Override
	//Returns map with key being BPA number and value being the mobile number associated with the BPA number
	public Map<String, String> getListOfValidBuildingPlanApprovalMobNumbers(
			List<String> bpaNumberList) {
		// TODO Auto-generated method stub
		Map<String,String> bpaDataMap = new HashMap<String, String>();
		if(bpaNumberList!=null && !bpaNumberList.isEmpty()){
			for(String bpaNo : bpaNumberList){
				if(bpaNo!=null && bpaNo!=""){
					bpaDataMap.put(bpaNo, buildingPlanMobileNo(bpaNo));
				}
			}
		}
		return bpaDataMap;
	}

	/**
	 * @description Checks whether the given bpaNumber is in "Order Issued to Applicant" Status. Returns true if yes.
	 * @param bpaNumber
	 * @return
	 */
	private boolean isValidBuildingPlanApprovalNumber(String bpaNumber) {
		if (bpaNumber != null && !"".equals(bpaNumber)) {
			Criteria ppanumberCriteria = buildRegistrationCriteria(bpaNumber);
			List<Registration> regPPAnumList = ppanumberCriteria.list();
			if (regPPAnumList.size()> 0){
				return true;
			} 
		}
		return false;
	}
	/*
	 * (non-Javadoc)
	 * this API returns empty bpaFeesDataMap when there is no Collection happen for Road cut charges - TNEB,
	 * Road cut charges - CMWSSB SEWERAGE,Road cut charges - CMWSSB, WATER fees for perticular BPA Number...
	 * bpaFeesDataFinalMap returns  BpaNumber with FeesDescription and Collected Amount..
	 */
	public Map<String, Map<String, BigDecimal>> getFeeDetailsForBpaNumber(String bpaNumber)
	{
	
	Map<String,BigDecimal> bpaFeesDataMap = new HashMap<String, BigDecimal>();
	Map<String,Map<String,BigDecimal>> bpaFeesDataFinalMap = new HashMap<String, Map<String,BigDecimal>>(); 
	
	if(bpaNumber!=null && bpaNumber!=""){
	Registration registration=getRegistrationByBpaNumber(bpaNumber);
	
	/*
	 * Pass Demand and get the receipt details.
	 */
	if(registration!=null){
		getFeeDetailMapForReceipts(bpaFeesDataMap, registration);
	/*
	 * If AppType is Revised and Existing PPANumber is entered den it will execute and add previous above 3 fees Collected amount into latest values...
	 */
		
	
	while(registration.getExistingPPANum()!=null && !"".equals(registration.getExistingPPANum()))
	{
		registration=getRegistrationByBpaNumber(registration.getExistingPPANum());
		if(registration!=null)
		getFeeDetailMapForReceipts(bpaFeesDataMap, registration);
	
	 }
	
	}
	
	bpaFeesDataFinalMap.put(bpaNumber, bpaFeesDataMap);
	}
	
	return bpaFeesDataFinalMap;
}


	private void getFeeDetailMapForReceipts(Map<String, BigDecimal> bpaFeesDataMap,
			 Registration registration) {
		Set<String> receiptNumbetSet= new HashSet<String>();
		List<BillReceiptInfo> billRecptInfoList=new ArrayList<BillReceiptInfo>();
		DemandGenericDao demandGenericDao = new DemandGenericHibDao();
		
		
		List<BillReceipt> billReceiptList=demandGenericDao.getBillReceipts((List<EgDemand>) Arrays.asList(registration.getEgDemand()));
		
		//REQUIRED: if date not found,demandGenericDao.getBillReceipts(demand) returns null.
		if(billReceiptList!=null) {
			for(BillReceipt billReceipt : billReceiptList)
			{
				receiptNumbetSet.add(billReceipt.getReceiptNumber());
				}
		}
		/*
		 * For each receipts,  get the collected bill details.
		 */
		if(collectionIntegrationService!=null){
		Map<String,BillReceiptInfo> billReceiptInfoMap = collectionIntegrationService.getReceiptInfo(BpaConstants.SERVICE_CODE, receiptNumbetSet);
		for(Map.Entry<String,BillReceiptInfo> map: billReceiptInfoMap.entrySet())
		{
			billRecptInfoList.add(map.getValue());
		}
		for(BillReceiptInfo billRecptInfo :billRecptInfoList)
		{
			for(ReceiptAccountInfo recAccInfo : billRecptInfo.getAccountDetails())
			{
				if (recAccInfo!=null && recAccInfo.getCrAmount()!=null ) {
						String	demandMasterReasonDesc ="";
						if( recAccInfo.getDescription()!=null && !"".equals(recAccInfo.getDescription())){
						demandMasterReasonDesc =recAccInfo.getDescription().substring(0, recAccInfo.getDescription().indexOf(BpaConstants.COLL_RECEIPTDETAIL_DESC_PREFIX)).trim();
						}
								if((demandMasterReasonDesc!=null && demandMasterReasonDesc!="" && (demandMasterReasonDesc.equals(BpaConstants.ROADCUTCHARGESFORTNEB) 
										||demandMasterReasonDesc.equals(BpaConstants.ROADCUTCHARGESFORSEWERAGE)
										||demandMasterReasonDesc.equals(BpaConstants.ROADCUTCHARGERFORWATER) ))) 
								
								{
								BigDecimal reasonAmount=(bpaFeesDataMap.get(demandMasterReasonDesc)!=null?bpaFeesDataMap.get(demandMasterReasonDesc):BigDecimal.ZERO);
								bpaFeesDataMap.put(demandMasterReasonDesc, reasonAmount.add( recAccInfo.getCrAmount()));
								}
								
				}
			}
				
			}
		}
	}
	
	public List<BpaFee> getAllSanctionedFeesbyServiceTypeSortByOrderNumber(Long serviceTypeId){
		
		Criteria feeCrit=persistenceService.getSession().createCriteria(BpaFee.class,"bpafee")
				.createAlias("bpafee.serviceType", "servicetypeObj"); 
				feeCrit.add(Restrictions.eq("feeType", BpaConstants.SANCTIONEDFEE));
				feeCrit.add(Restrictions.eq("servicetypeObj.id", serviceTypeId));
				feeCrit.addOrder(Order.asc("feeDescription"));
		return feeCrit.list();
		
	}
	private List<ReportFeesDetails> getSanctionedReportFeeDtls(Long serviceTypeId) {
		List <ReportFeesDetails> reportFeeList =new LinkedList<ReportFeesDetails>();
		List<BpaFee> santionFeeList =new LinkedList<BpaFee>();
		santionFeeList=getAllSanctionedFeesbyServiceTypeSortByOrderNumber(serviceTypeId);
		//Collections.sort(santionFeeList, BpaFeeComparator.getFeeComparator(BpaFeeComparator.FEECODE_SORT));
		for(BpaFee fees:santionFeeList){
			ReportFeesDetails reportFeesDetails =new ReportFeesDetails();
				reportFeesDetails.setDescription(fees.getFeeDescription()!=null?fees.getFeeDescription():"");	
				reportFeeList.add(reportFeesDetails);
		}
		return reportFeeList;
	}
	/**
	 * @param bpaNumber
	 * @return
	 */
	private Criteria buildRegistrationCriteria(String bpaNumber) {
		Criteria appCriteria = persistenceService.getSession()
				.createCriteria(Registration.class, "register")
				.createAlias("register.egwStatus", "statusObj");
		appCriteria.add(Restrictions.eq(STATUSMODULE, "BPAREGISTRATION"));
		appCriteria.add(Restrictions.eq(STATUSCODE,BpaConstants.ORDERISSUEDTOAPPLICANT));
		if(bpaNumber!=null)
				appCriteria.add(Restrictions.eq("register.planSubmissionNum", bpaNumber));
		return appCriteria;
	}
	private Registration getRegistrationByBpaNumber(String bpaNumber)
	{
		return (Registration)persistenceService.find("from Registration where planSubmissionNum=? and  egwStatus.code=? ",bpaNumber,BpaConstants.ORDERISSUEDTOAPPLICANT);
	}
	
	/**
	 * @description Returns the mobile no associated to bpaNumber
	 * @param bpaNumber
	 * @return
	 */
	private String buildingPlanMobileNo(String bpaNumber) {
		if (bpaNumber != null && !"".equals(bpaNumber)) {
			Criteria mobNumberCriteria = buildRegistrationCriteria(bpaNumber);
			List<Registration> regPPAnumList = mobNumberCriteria.list();
			if (regPPAnumList.size()> 0){
				if(regPPAnumList.get(0).getOwner()!=null && 
						(regPPAnumList.get(0).getOwner().getMobilePhone()!="" && regPPAnumList.get(0).getOwner().getMobilePhone()!=null ))
				return regPPAnumList.get(0).getOwner().getMobilePhone();
			}
		}
		return "";	 
	}
	
	@Override
	//Returns map having jurisdiction information for a BPA Number.
	//Keys : bpaNumber, zone, ward, area, locality and street
	public Map<String, String> getJurisdictionForBPANumber(String bpaNumber) {
		Map<String,String> bpaDataMap = new HashMap<String, String>();
		if (bpaNumber != null && !"".equals(bpaNumber)) {
			Criteria appCriteria = persistenceService.getSession()
					.createCriteria(Registration.class, "register")
					.createAlias("register.egwStatus", "statusObj");
			appCriteria.add(Restrictions.eq(STATUSMODULE, "BPAREGISTRATION"));
			appCriteria.add(Restrictions.eq("register.planSubmissionNum", bpaNumber)); 
			List<Registration> regPPAnumList = appCriteria.list();
			if (regPPAnumList.size()> 0){
				bpaDataMap.put("bpaNumber",bpaNumber);
				if(regPPAnumList.get(0).getAdminboundaryid()!=null){
					if(regPPAnumList.get(0).getAdminboundaryid().getParent()!=null){
						bpaDataMap.put("zone", (regPPAnumList.get(0).getAdminboundaryid().getParent().getId()).toString());
					}
					bpaDataMap.put("ward", (regPPAnumList.get(0).getAdminboundaryid().getId()).toString());
				}
				else{
					bpaDataMap.put("zone", "");
					bpaDataMap.put("ward", "");
				}
				
				if(regPPAnumList.get(0).getLocboundaryid()!=null){
					if(regPPAnumList.get(0).getLocboundaryid().getParent()!=null){
						if(regPPAnumList.get(0).getLocboundaryid().getParent().getParent()!=null){
							bpaDataMap.put("area", (regPPAnumList.get(0).getLocboundaryid().getParent().getParent().getId()).toString());
						}
						bpaDataMap.put("locality", (regPPAnumList.get(0).getLocboundaryid().getParent().getId()).toString());
					}
					bpaDataMap.put("street", (regPPAnumList.get(0).getLocboundaryid().getId()).toString());
				}
				else{
					bpaDataMap.put("area", "");
					bpaDataMap.put("locality", "");
					bpaDataMap.put("street", "");
				}
			}
			else{
				bpaDataMap.put("bpaNumber",bpaNumber);
				bpaDataMap.put("zone", "");
				bpaDataMap.put("ward", "");
				bpaDataMap.put("area", "");
				bpaDataMap.put("locality", "");
				bpaDataMap.put("street", "");
			}
		}
		return bpaDataMap;
	} 
	
	
	public PersistenceService getPersistenceService() {
		return persistenceService;
	}

	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}


	public CollectionIntegrationServiceImpl getCollectionIntegrationService() {
		return collectionIntegrationService;
	}


	public void setCollectionIntegrationService(
			CollectionIntegrationServiceImpl collectionIntegrationService) {
		this.collectionIntegrationService = collectionIntegrationService;
	}


}