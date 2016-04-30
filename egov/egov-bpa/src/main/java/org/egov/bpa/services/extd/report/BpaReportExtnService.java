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
package org.egov.bpa.services.extd.report;

import org.apache.log4j.Logger;
import org.egov.bpa.constants.BpaConstants;
import org.egov.bpa.models.extd.RegistrationExtn;
import org.egov.bpa.models.extd.RegistrationFeeDetailExtn;
import org.egov.bpa.models.extd.RegistrationFeeExtn;
import org.egov.bpa.models.extd.RegnStatusDetailsExtn;
import org.egov.bpa.models.extd.ReportFeesDetailsExtn;
import org.egov.bpa.models.extd.masters.BpaFeeExtn;
import org.egov.bpa.services.extd.common.BpaCommonExtnService;
import org.egov.bpa.services.extd.common.FeeExtnService;
import org.egov.collection.integration.models.BillReceiptInfo;
import org.egov.collection.integration.models.ReceiptAccountInfo;
import org.egov.collection.integration.models.ReceiptInstrumentInfo;
import org.egov.collection.integration.services.CollectionIntegrationServiceImpl;
import org.egov.demand.dao.DemandGenericDao;
import org.egov.demand.dao.DemandGenericHibDao;
import org.egov.demand.model.BillReceipt;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infstr.utils.DateUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BpaReportExtnService  {
	private Logger LOGGER = Logger.getLogger(getClass());

	private CollectionIntegrationServiceImpl collectionIntegrationService;
	private BpaCommonExtnService bpaCommonExtnService;
	private FeeExtnService feeExtnService;
	private ReportService reportService;
	
	/*
	 * For to send Provisional Planning Permit certificate as a attachment while upadating applicant Signature date
	 */
	public InputStream getPlanningpermitReportDetails(RegistrationExtn registration) {
		InputStream inputStream = null;
		ReportRequest reportRequest = null;
		Map <String,Object>reportParams = new HashMap<String,Object>();
		List<ReportFeesDetailsExtn> reportFeesDtlsList = getReportFeesDetails(registration,BpaConstants.PLANPERMIT);
		Map<String, ReportFeesDetailsExtn> reportfeeMap=  new HashMap<String, ReportFeesDetailsExtn>();
		
		for (ReportFeesDetailsExtn regnFee : reportFeesDtlsList) {
			reportfeeMap.put(regnFee.getDescription(), regnFee);
		}

		RegistrationFeeExtn regnFeeObj=bpaCommonExtnService.getLatestApprovedRegistrationFee(registration);
		
		if(regnFeeObj!=null){

		  for ( RegistrationFeeDetailExtn regnFeeDtl: regnFeeObj.getRegistrationFeeDetailsSet())
			{
			 	if(regnFeeDtl.getBpaFee().getFeeGroup().equals(BpaConstants.CMDAFEE))
			 		{
			 		
			 		if( !reportfeeMap.containsKey(regnFeeDtl.getBpaFee().getFeeDescription())){
						ReportFeesDetailsExtn reportFeesDetails = new ReportFeesDetailsExtn();
						reportFeesDetails.setDescription(regnFeeDtl.getBpaFee()
								.getFeeDescription());
						reportFeesDetails.setAmount(regnFeeDtl.getAmount());
						reportFeesDtlsList.add(reportFeesDetails);
			 		}else if(reportfeeMap.containsKey(regnFeeDtl.getBpaFee().getFeeDescription()) && regnFeeDtl.getAmount().compareTo(BigDecimal.ZERO)>0){
			 		{
			 			reportfeeMap.get(regnFeeDtl.getBpaFee().getFeeDescription()).setAmount(regnFeeDtl.getAmount());
			 		}
			 		}
			 	}
			}
		
		} 
		reportParams.put("reportFeeList", reportFeesDtlsList);
		if(registration.getServiceType()!=null && registration.getServiceType().getCode()!=null){
			getReceiptinstrumentInfo(registration,reportParams,BpaConstants.PLANPERMIT);
		if(registration.getServiceType().getCode().equals(BpaConstants.RECLASSIFICATIONCODE))
		{
			getApprovedDateByPassingRegistrationObject(reportParams,registration,null);
			
		reportRequest = new ReportRequest(BpaConstants.PLANPERMITFORRECLASSIFICATIONEXTNCITIZEN,registration, reportParams);
		}
		
		else if(registration.getServiceType().getCode().equals(BpaConstants.APPLICATIONFORDEMOLITIONCODE))
		{
			getApprovedDateByPassingRegistrationObject(reportParams,registration,null);
		reportRequest = new ReportRequest(BpaConstants.PLANPERMITFORDEMOLITIONCITIZEN,registration, reportParams);
		}
		else if(registration.getServiceType().getCode().equals(BpaConstants.NEWBUILDINGONVACANTPLOTCODE)
				||registration.getServiceType().getCode().equals(BpaConstants.DEMOLITIONRECONSTRUCTIONCODE)
				|| registration.getServiceType().getCode().equals(BpaConstants.SUBDIVISIONOFLANDCODE)
				||registration.getServiceType().getCode().equals(BpaConstants.LAYOUTAPPPROVALCODE)
				||registration.getServiceType().getCode().equals(BpaConstants.ADDITIONALCONSTRUCTIONCODE))
		{
			getApprovedDateByPassingRegistrationObject(reportParams,
					registration,BpaConstants.PLANPERMIT);
		reportRequest = new ReportRequest(BpaConstants.PLANPERMITCITIZEN,registration, reportParams);
		}
		}
		ReportOutput reportOutput = reportService.createReport(reportRequest);				
		if (reportOutput != null && reportOutput.getReportOutputData() != null)
			inputStream = new ByteArrayInputStream(reportOutput.getReportOutputData());
	
	
	
		return inputStream;
	}
	/*
	 * For to send Provisional Building Permit certificate as a attachment while upadating applicant Signature date
	 */
	public InputStream getBuildingPermitReportdetails(
			 RegistrationExtn registration) {
		InputStream inputStream = null;
		ReportRequest reportRequest = null;
		Map <String,Object>reportParams = new HashMap<String,Object>();
		List<ReportFeesDetailsExtn> reportFeesDtlsList = getReportFeesDetails(registration,BpaConstants.BUILDINGPERMIT);
		
		
		
		reportParams.put("reportFeeList", reportFeesDtlsList);
		
		if(registration!=null && registration.getServiceType()!=null && registration.getServiceType().getCode().equals(BpaConstants.CMDACODE) && registration.getCmdaNum()!=null && registration.getCmdaRefDate()!=null)
		{
			reportParams.put("PlanSubmissionNumber", registration.getCmdaNum()+" - "+DateUtils.getDefaultFormattedDate(registration.getCmdaRefDate()));
		}else {
			reportParams.put("PlanSubmissionNumber", registration.getPlanSubmissionNum());
		}
		reportParams.put("reportFeeList", reportFeesDtlsList);

		getApprovedDateByPassingRegistrationObject(reportParams,
				registration,BpaConstants.BUILDINGPERMIT);
		getReceiptinstrumentInfo(registration,reportParams,BpaConstants.BUILDINGPERMIT);  
		if(registration.getServiceType()!=null && registration.getServiceType().getCode().equals(BpaConstants.NEWBUILDINGONVACANTPLOTCODE)
				||registration.getServiceType().getCode().equals(BpaConstants.DEMOLITIONRECONSTRUCTIONCODE)
				||registration.getServiceType().getCode().equals(BpaConstants.ADDITIONALCONSTRUCTIONCODE)
				||registration.getServiceType().getCode().equals(BpaConstants.CMDACODE))
		{
		reportRequest = new ReportRequest(BpaConstants.BUILDINGPERMITCITIZEN,registration, reportParams);
		}
		ReportOutput reportOutput = reportService.createReport(reportRequest);				
		if (reportOutput != null && reportOutput.getReportOutputData() != null)
			inputStream = new ByteArrayInputStream(reportOutput.getReportOutputData());
	
	
	
		return inputStream;
		
	}
	/**
	 * @param registration
	 * @param buildingpermit 
	 * @return fee details for Plan Permit and Building permit
	 */
	public List<ReportFeesDetailsExtn> getReportFeesDetails(RegistrationExtn registration, String printPermitFor) {
		
		List<BillReceiptInfo> billRecptInfoList=new ArrayList<BillReceiptInfo>();
		
		List<ReportFeesDetailsExtn> reportFeeDetailsList=getSanctionedReportFeeDtls(registration,printPermitFor);
		
		for(ReportFeesDetailsExtn reportFeesDtls:reportFeeDetailsList)
		{
			reportFeesDtls.setAmount(BigDecimal.ZERO);
		}
		Map<String, BillReceiptInfo> billReceiptInfoMap = getReceiptInfoMap(registration);
		
		for(Map.Entry<String,BillReceiptInfo> map: billReceiptInfoMap.entrySet())
		{
			billRecptInfoList.add(map.getValue());
		}
		for(BillReceiptInfo billRecptInfo :billRecptInfoList)
		{
			for(ReceiptAccountInfo recAccInfo : billRecptInfo.getAccountDetails())
			{
				if (recAccInfo!=null && recAccInfo.getCrAmount()!=null && recAccInfo.getCrAmount().compareTo(BigDecimal.ZERO) > 0) {
			String	demandMasterReasonDesc ="";
			if( recAccInfo.getDescription()!=null && !"".equals(recAccInfo.getDescription())){
			demandMasterReasonDesc =recAccInfo.getDescription().substring(0, recAccInfo.getDescription().indexOf(BpaConstants.COLL_RECEIPTDETAIL_DESC_PREFIX)).trim();
			}
			for(ReportFeesDetailsExtn reportFeesDtls:reportFeeDetailsList)
			{
				if(null!=reportFeesDtls.getDescription() && !"".equals(reportFeesDtls.getDescription())){
					
				if(reportFeesDtls.getDescription().equalsIgnoreCase(demandMasterReasonDesc))
				{
					if(reportFeesDtls.getAmount()!=null && reportFeesDtls.getAmount().doubleValue() > 0 )
					   reportFeesDtls.setAmount(reportFeesDtls.getAmount().add(recAccInfo.getCrAmount()));
					else
						 reportFeesDtls.setAmount(recAccInfo.getCrAmount());
				}
				}
			}
		
			}
			}
		}
		return reportFeeDetailsList;
	}
	public void getApprovedDateByPassingRegistrationObject(
			Map<String, Object> reportParams, RegistrationExtn registration, String permitType) {
		if(registration.getRegnStatusDetailsSet()!=null) {
			for(RegnStatusDetailsExtn regnStatDet : registration.getRegnStatusDetailsSet()) {
				if(regnStatDet.getStatus().getCode().equals(BpaConstants.APPLICANTSIGNUPDATED)) {
					reportParams.put("approvedDate",regnStatDet.getStatusdate());
					
				if(permitType!=null && !"".equals(permitType)){
						if(permitType.equals(BpaConstants.BUILDINGPERMIT)){	
							reportParams.put("validityDate", DateUtils.add(DateUtils.add(regnStatDet.getStatusdate(), Calendar.MONTH, 6),Calendar.DAY_OF_MONTH,-1));
							reportParams.put("constructionValidityDate", DateUtils.add(DateUtils.add(regnStatDet.getStatusdate(), Calendar.YEAR, 2),Calendar.DAY_OF_MONTH,-1));
						}else if(permitType.equals(BpaConstants.PLANPERMIT)){	
							reportParams.put("validityDate", DateUtils.add(DateUtils.add(regnStatDet.getStatusdate(), Calendar.MONTH, 6),Calendar.DAY_OF_MONTH,-1));
							reportParams.put("constructionValidityDate", DateUtils.add(DateUtils.add(regnStatDet.getStatusdate(), Calendar.YEAR, 3),Calendar.DAY_OF_MONTH,-1));
						}
					}
				}
			}	
		}
	}
	public void getReceiptinstrumentInfo(RegistrationExtn registration,Map<String, Object> reportParams,String printPermitFor)
	{
		List<BillReceiptInfo> billRecptInfoList=new ArrayList<BillReceiptInfo>();
		boolean instrumentDetailsPrepared=false;
		Map<String, BillReceiptInfo> billReceiptInfoMap = getReceiptInfoMap(registration);
		List<ReportFeesDetailsExtn> reportFeeDetailsList=getSanctionedReportFeeDtls(registration,printPermitFor);
		for(Map.Entry<String,BillReceiptInfo> map: billReceiptInfoMap.entrySet())
		{
		billRecptInfoList.add(map.getValue());
		}
		for( BillReceiptInfo billeciept:billRecptInfoList)
		{
			for(ReceiptAccountInfo recAccInfo : billeciept.getAccountDetails())
			{
				if (recAccInfo!=null && recAccInfo.getCrAmount()!=null && recAccInfo.getCrAmount().compareTo(BigDecimal.ZERO) > 0) {
					String	demandMasterReasonDesc ="";
					if( recAccInfo.getDescription()!=null && !"".equals(recAccInfo.getDescription())){
					demandMasterReasonDesc =recAccInfo.getDescription().substring(0, recAccInfo.getDescription().indexOf(BpaConstants.COLL_RECEIPTDETAIL_DESC_PREFIX)).trim();
					}
					for(ReportFeesDetailsExtn reportFeesDtls:reportFeeDetailsList)
					{
					if(null!=reportFeesDtls.getDescription() && !"".equals(reportFeesDtls.getDescription()) && reportFeesDtls.getDescription().equalsIgnoreCase(demandMasterReasonDesc)){
						
						for(ReceiptInstrumentInfo   instrument:billeciept.getInstrumentDetails())
						{
							reportParams.put("instrumentAmount", instrument.getInstrumentAmount());
							reportParams.put("instrumentDate", instrument.getInstrumentDate());
							reportParams.put("instrumentBank", instrument.getBankName());
							reportParams.put("instrumentNumber", instrument.getInstrumentNumber());
							instrumentDetailsPrepared=true;
						}
						
						break;
					
					}
				}
					if(instrumentDetailsPrepared)
						break;
			}
				if(instrumentDetailsPrepared)
					break;
		}
			if(instrumentDetailsPrepared)
				break;
		}	
	}
	public Map<String, BillReceiptInfo> getReceiptInfoMap(RegistrationExtn registration) {
		DemandGenericDao demandGenericDao = new DemandGenericHibDao();
		Set<String> receiptNumbetSet= new HashSet<String>();
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
		Map<String,BillReceiptInfo> billReceiptInfoMap = collectionIntegrationService.getReceiptInfo(BpaConstants.EXTD_SERVICE_CODE, receiptNumbetSet);
		return billReceiptInfoMap;
	}
	
	/**
	 * returns EgDemand Reason Master List
	 * @param buildingpermit 
	 */
	private List<ReportFeesDetailsExtn> getSanctionedReportFeeDtls(RegistrationExtn registration, String printPermitFor) {
		List <ReportFeesDetailsExtn> reportFeeList =new LinkedList<ReportFeesDetailsExtn>();
		List<BpaFeeExtn> santionFeeList =new LinkedList<BpaFeeExtn>();
		String printType;
		
		if(registration.getEgDemand()!=null&&registration.getEgDemand().getEgDemandDetails()!=null){
			Set<EgDemandDetails> demandDetailsSet=registration.getEgDemand().getEgDemandDetails();
			HashMap<String,String> demandRsnMstrCodeMap=new HashMap<String,String>();
			for(EgDemandDetails demandDetails:demandDetailsSet){
				demandRsnMstrCodeMap.put(demandDetails.getEgDemandReason().getEgDemandReasonMaster().getCode(),demandDetails.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster());
			}
			santionFeeList=feeExtnService.getAllSanctionedFeesbyServiceTypeSortByOrderNumber(registration.getServiceType().getId());
			for(BpaFeeExtn fees:santionFeeList){
				ReportFeesDetailsExtn reportFeesDetails =new ReportFeesDetailsExtn();
				if(!fees.getIsPlanningPermitFee())
				{
					printType=BpaConstants.BUILDINGPERMIT;
				}else
				{
					printType=BpaConstants.PLANPERMIT;
				}
				
				// DONOTDELETE:The following line will show only the feeses which are present in demand. 
				if(printPermitFor.equals(printType)){	
				reportFeesDetails.setDescription(fees.getFeeDescription()!=null?fees.getFeeDescription():"");	
					if(fees.getFeeDescription()!=null)
					{
						if(fees.getFeeDescription().equalsIgnoreCase("CMDA Development Charges"))
						{
							reportFeesDetails.setDescriptionInLocal("âò.âç.ô.Æ. îëëÐçèìÐÌæÐ æìÐìúëÐÐ"+"/");
						}else if(fees.getFeeDescription().equalsIgnoreCase("Building License Fees"))
						{
							reportFeesDetails.setDescriptionInLocal("æìÐìì ãè¨ëæÐ æìÐìúëÐÐ"+"/");
						}else if(fees.getFeeDescription().equalsIgnoreCase("Demolition fees"))
						{
							reportFeesDetails.setDescriptionInLocal("êæèÐçÐµæÐ æìÐìúëÐ"+"/");
						}else if(fees.getFeeDescription().equalsIgnoreCase("Tentative improvement charges"))
						{
							reportFeesDetails.setDescriptionInLocal("îêèõèá íç¨ô¨ÕêÐê¨æÐ æìÐìúëÐ"+"/");
						}else if(fees.getFeeDescription().equalsIgnoreCase("Scrutiny Fees"))
						{
							reportFeesDetails.setDescriptionInLocal("ÔèÐåÐêèáÐ²æÐ æìÐìúëÐ"+"/");
						}else if(fees.getFeeDescription().equalsIgnoreCase("Security Deposit"))
						{
							reportFeesDetails.setDescriptionInLocal("ç¨éä éôçÐµêÐ âêèéæ"+"/");
						}else if(fees.getFeeDescription().equalsIgnoreCase("Road cut charges - TNEB"))
						{
							reportFeesDetails.setDescriptionInLocal("òèéù âôìÐÌæÐæèä æìÐìúëÐ (ë¨.ôè)"+"/");
						}else if(fees.getFeeDescription().equalsIgnoreCase("Road cut charges - CMWSSB SEWERAGE"))
						{
							reportFeesDetails.setDescriptionInLocal("òèéù âôìÐÌæÐæèä æìÐìúëÐ (æÈ¨² å©èÐ)"+"/");
						}else if(fees.getFeeDescription().equalsIgnoreCase("Plan Charges"))
						{
							reportFeesDetails.setDescriptionInLocal("ê¨ìÐìæÐ æìÐìúëÐ"+"/");
						}else if(fees.getFeeDescription().equalsIgnoreCase("Regularisation Charges - Land"))
						{
							reportFeesDetails.setDescriptionInLocal("ëéä ãìÐç¨è¨² Ëé÷çÐçÌêÐÊêùÐ æìÐìúëÐ"+"/");
						} else if(fees.getFeeDescription().equalsIgnoreCase("Open space reservation charges"))
						{
							reportFeesDetails.setDescriptionInLocal("ê¨÷åÐêâôó¨ ¬ìøÊæÐæ©Ì æìÐìúëÐ "+"/");
						} 
						else if(fees.getFeeDescription().equalsIgnoreCase("Regularisation Charges (penalty under section 244A)"))
						{
							reportFeesDetails.setDescriptionInLocal("ôõäªËé÷ çÌêÐÊëÐ æìÐìúëÐ - 244 A ç¨è¨ô¨äª æ©ÈÐ "+"/");
						} else if(fees.getFeeDescription().equalsIgnoreCase("Road cut charges - CMWSSB, WATER"))
						{
							reportFeesDetails.setDescriptionInLocal("òèéù âôìÐÌæÐæèä æìÐìúëÐ (Æïå©èÐ)"+"/");
						}
						else
							reportFeesDetails.setDescriptionInLocal("");
						
					}else
						reportFeesDetails.setDescriptionInLocal("");
				reportFeeList.add(reportFeesDetails);
				}
			} 
			}
		return reportFeeList;
	}
	public CollectionIntegrationServiceImpl getCollectionIntegrationService() {
		return collectionIntegrationService;
	}
	public void setCollectionIntegrationService(
			CollectionIntegrationServiceImpl collectionIntegrationService) {
		this.collectionIntegrationService = collectionIntegrationService;
	}
	public BpaCommonExtnService getBpaCommonExtnService() {
		return bpaCommonExtnService;
	}
	public void setBpaCommonExtnService(BpaCommonExtnService bpaCommonExtnService) {
		this.bpaCommonExtnService = bpaCommonExtnService;
	}
	public FeeExtnService getFeeExtnService() {
		return feeExtnService;
	}
	public void setFeeExtnService(FeeExtnService feeExtnService) {
		this.feeExtnService = feeExtnService;
	}
	public ReportService getReportService() {
		return reportService;
	}
	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}
	
}
