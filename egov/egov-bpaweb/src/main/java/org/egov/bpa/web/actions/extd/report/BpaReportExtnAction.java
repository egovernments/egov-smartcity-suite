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
package org.egov.bpa.web.actions.extd.report;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.egov.bpa.constants.BpaConstants;
import org.egov.bpa.models.extd.LetterToPartyExtn;
import org.egov.bpa.models.extd.LpChecklistExtn;
import org.egov.bpa.models.extd.RegistrationDDDetailsExtn;
import org.egov.bpa.models.extd.RegistrationExtn;
import org.egov.bpa.models.extd.RegistrationFeeDetailExtn;
import org.egov.bpa.models.extd.RegistrationFeeExtn;
import org.egov.bpa.models.extd.RejectionChecklistExtn;
import org.egov.bpa.models.extd.ReportFeesDetailsExtn;
import org.egov.bpa.services.extd.common.BpaCommonExtnService;
import org.egov.bpa.services.extd.common.FeeExtnService;
import org.egov.bpa.services.extd.register.RegisterBpaExtnService;
import org.egov.bpa.services.extd.report.BpaReportExtnService;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.reporting.viewer.ReportViewerUtil;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.workflow.entity.State;
import org.egov.infstr.utils.DateUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BpaReportExtnAction extends BaseFormAction{

	private Logger LOGGER = Logger.getLogger(getClass());
	private RegisterBpaExtnService registerBpaExtnService;
	private ReportService reportService;
	private Integer reportId = -1;
	private Long registrationId;
	private Object reportInputData;
	private String reportTemplate;
	private String fromAddressToLp;
	private String printMode;
	private Long letterToPartyId;
	private Long letterToPartyCmdaId;
	private FeeExtnService feeExtnService;
	private BpaReportExtnService bpaReportExtnService;
	private BpaCommonExtnService bpaCommonExtnService;
	
	

	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}
	@Action(value = "/bpaReportExtn-printReport", results = { @Result(name = "report",type = "dispatcher") })
	public String printReport()
	{
		
		LOGGER.debug("Entered into print method");
		ReportRequest reportInput = null;
		Map reportParams = new HashMap<String, Object>();
		reportInput = prepareReportInputData(reportInput);
		reportId = ReportViewerUtil.addReportToSession(reportService.createReport(reportInput), getSession());
		LOGGER.info("Exit from print method"+reportId);
		LOGGER.debug("Exit from print method");
		return "report";
	}

	/**
	 * @param reportInput
	 * @return
	 */
	private ReportRequest prepareReportInputData(ReportRequest reportInput) {
				
		Map <String,Object>reportParams = new HashMap<String,Object>();
		
		RegistrationExtn registration=registerBpaExtnService.getRegistrationById(registrationId);
		if(registration!=null && printMode.equalsIgnoreCase(BpaConstants.PLANPERMIT) && registration.getServiceType().getCode().equals(BpaConstants.APPLICATIONFORDEMOLITIONCODE))
		{
			printMode=BpaConstants.PLANPERMITFORDEMOLITION;
		}
		if(registration!=null && printMode.equalsIgnoreCase(BpaConstants.PLANPERMIT) && registration.getServiceType().getCode().equals(BpaConstants.RECLASSIFICATION))
		{
			printMode=BpaConstants.PLANPERMITFORRECLASSIFICATION;
		}
		if(registration!=null && printMode.equalsIgnoreCase(BpaConstants.PLANPERMITCITIZEN) && registration.getServiceType().getCode().equals(BpaConstants.RECLASSIFICATION))
		{
			printMode=BpaConstants.PLANPERMITFORRECLASSIFICATIONEXTNCITIZEN;
		}
		if(registration!=null && printMode.equalsIgnoreCase(BpaConstants.PLANPERMITCITIZEN) && registration.getServiceType().getCode().equals(BpaConstants.APPLICATIONFORDEMOLITIONCODE))
		{
			printMode=BpaConstants.PLANPERMITFORDEMOLITIONCITIZEN;
		}
		if(registration!=null && printMode.equalsIgnoreCase(BpaConstants.REGISTRATIONACKNOWLEDGEMENT)){
		  reportInput=new ReportRequest(BpaConstants.REGISTRATIONEXTNACKNOWLEDGEMENT, registration, null);
		}
		else if(registration!=null && printMode.equalsIgnoreCase(BpaConstants.REJECTIONNOTICE))
			{	
				
				State stateValue=getApprovedDate(registration,"UnconsiderdBPA");
				Set<RejectionChecklistExtn> checkListDetails=new HashSet<RejectionChecklistExtn>();
					for(RejectionChecklistExtn rejectionChecklist:registration.getRejection().getRejectionChecklistSet()){
						
						if(rejectionChecklist!=null && null!=rejectionChecklist.getIsChecked() )
						{ 
							checkListDetails.add(rejectionChecklist);
						}
					}
				registration.getRejection().setRejectionChecklistSet(checkListDetails);
				reportParams.put("approvedDate", stateValue.getLastModifiedDate()!=null?stateValue.getLastModifiedDate():null);
				reportInput=new ReportRequest(BpaConstants.REJECTIONNOTICEEXTN, registration, reportParams);
			}
		
		else if(registration!=null && printMode.equalsIgnoreCase(BpaConstants.BUILDINGPERMIT))
		{
			getBuildingPermitReportdetails(reportParams, registration);
			
			reportInput=new ReportRequest(BpaConstants.BUILDINGPERMITEXTN, registration, reportParams);
		}
		else if(registration!=null && printMode.equalsIgnoreCase(BpaConstants.BUILDINGPERMITCITIZEN))
		{
			getBuildingPermitReportdetails(reportParams, registration);
			
			reportInput=new ReportRequest(BpaConstants.BUILDINGPERMITCITIZEN, registration, reportParams);
		}
		else if(registration!=null && printMode.equalsIgnoreCase(BpaConstants.PLANPERMIT))
		{
			getPlanningpermitReportDetails(reportParams, registration);
			reportInput=new ReportRequest(BpaConstants.PLANPERMITEXTN, registration, reportParams);
		}
		else if(registration!=null && printMode.equalsIgnoreCase(BpaConstants.PLANPERMITCITIZEN))
		{
			getPlanningpermitReportDetails(reportParams, registration);
			reportInput=new ReportRequest(BpaConstants.PLANPERMITCITIZEN, registration, reportParams);
		}
		else if(registration!=null && printMode.equalsIgnoreCase(BpaConstants.PLANPERMITFORDEMOLITION))
		{
			bpaReportExtnService.getApprovedDateByPassingRegistrationObject(reportParams,
					registration,null);
			getFromAddressToReport(registration); 
			fromAddressToLp=fromAddressToLp.replace(BpaConstants.ASSISTANTADDRESS, "The Asst.Exe.Engineer (T.P)/\nAssistant Engineer (T.P)");
			reportParams.put("fromAddressToLp",fromAddressToLp);
			  reportInput=new ReportRequest(BpaConstants.PLANPERMITFORDEMOLITIONEXTN, registration, reportParams);
			
		}
		else if(registration!=null && printMode.equalsIgnoreCase(BpaConstants.PLANPERMITFORDEMOLITIONCITIZEN))
		{
			bpaReportExtnService.getApprovedDateByPassingRegistrationObject(reportParams,
					registration,null);
			if(registration!=null && registration.getAdminboundaryid()!=null && registration.getAdminboundaryid().getParent()!=null && registration.getAdminboundaryid().getParent().getParent()!=null)
				getFromAddressToReport(registration);
			reportParams.put("fromAddressToLp",fromAddressToLp);
			  reportInput=new ReportRequest(BpaConstants.PLANPERMITFORDEMOLITIONCITIZEN, registration, reportParams);
			
		}
		else if(registration!=null && printMode.equalsIgnoreCase(BpaConstants.PLANPERMITFORRECLASSIFICATION))
		{
			bpaReportExtnService.getApprovedDateByPassingRegistrationObject(reportParams,
					registration,null);
			  reportInput=new ReportRequest(BpaConstants.PLANPERMITFORRECLASSIFICATIONEXTN, registration, reportParams);
			
		}
		else if(registration!=null && printMode.equalsIgnoreCase(BpaConstants.PLANPERMITFORRECLASSIFICATIONEXTNCITIZEN))
		{
			bpaReportExtnService.getApprovedDateByPassingRegistrationObject(reportParams,
					registration,null);
		 reportInput=new ReportRequest(BpaConstants.PLANPERMITFORRECLASSIFICATIONEXTNCITIZEN, registration, reportParams);
			
		}
		else if(letterToPartyId!=null)
		{
			Set<LpChecklistExtn> checkListDtls=new HashSet<LpChecklistExtn>();
		//TODO: get letter to party by type. where checklist type as "lettertoparty"
			LetterToPartyExtn letterToParty=registerBpaExtnService.getLetterToPartyById(letterToPartyId);
			
			//RegistrationExtn regForLP=registerBpaExtnService.getRegistrationById(letterToParty.getRegistration().getId());
			for(LpChecklistExtn lpChecklist:letterToParty.getLpChecklistSet()){
				
				if(null!=lpChecklist.getIsChecked() && lpChecklist.getLpChecklistType()!=null && lpChecklist.getLpChecklistType().equals("lettertoparty"))
				{ 
					checkListDtls.add(lpChecklist);
				}
			}
			/*
			 * To change the From Address of Letter to Party 
			 * if fromAddressToLp variable is empty then From Address should be as old address..
			 */
			if(letterToParty!=null && letterToParty.getRegistration().getAdminboundaryid()!=null && letterToParty.getRegistration().getAdminboundaryid().getParent()!=null && letterToParty.getRegistration().getAdminboundaryid().getParent().getParent()!=null)
			
			if(letterToParty.getRegistration().getAdminboundaryid().getParent().getParent().getName().equalsIgnoreCase(BpaConstants.NORTHREGION))
			{
				fromAddressToLp=BpaConstants.ASSISTANTADDRESS+ "\n" +BpaConstants.NORTHREGION_ADDRESS;
			}
			else if(letterToParty.getRegistration().getAdminboundaryid().getParent().getParent().getName().equalsIgnoreCase(BpaConstants.SOUTHREGION))
			{
				fromAddressToLp=BpaConstants.ASSISTANTADDRESS + "\n"  + BpaConstants.SOUTHREGION_ADDRESS;
										
			}
			else if(letterToParty.getRegistration().getAdminboundaryid().getParent().getParent().getName().equalsIgnoreCase(BpaConstants.CENTRALREGION))
			{
				fromAddressToLp=BpaConstants.ASSISTANTADDRESS + "\n" + BpaConstants.CENTRALREGION_ADDRESS;
			}
			else
			{
				fromAddressToLp="";
			}
			if(letterToParty!=null && letterToParty.getRegistration()!=null) {	
				if(letterToParty.getRegistration().getCmdaNum()==null || "".equals(letterToParty.getRegistration().getCmdaNum()))
				{
					reportParams.put("registerReferenceNumber", "Application No : "+(letterToParty.getRegistration().getPlanSubmissionNum()!=null?letterToParty.getRegistration().getPlanSubmissionNum():"")+" Dated :"+(letterToParty.getRegistration().getPlanSubmissionDate()!=null? new SimpleDateFormat("dd/MM/yyyy").format(letterToParty.getRegistration().getPlanSubmissionDate()):""));
				}
				else{
					reportParams.put("registerReferenceNumber", "CDMA Letter No : "+(letterToParty.getRegistration().getCmdaNum()!=null?letterToParty.getRegistration().getCmdaNum():"")+" Dated :"+(letterToParty.getRegistration().getCmdaRefDate()!=null? new SimpleDateFormat("dd/MM/yyyy").format(letterToParty.getRegistration().getCmdaRefDate()):""));
				}
			}
			//letterToParty.getLpChecklistSet().removeAll(letterToParty.getLpChecklistSet());
		//	letterToParty.setLpChecklistSet(checkListDtls);
			reportParams.put("fromAddressToLp",fromAddressToLp);
			reportParams.put("lettertoPartyChecklistSet", checkListDtls); 
			reportInput=new ReportRequest(BpaConstants.LETTERTOPARTYNOTICEEXTN, letterToParty, reportParams);
		}
		else if(letterToPartyCmdaId!=null){
			
			Map<String,Object> reportData = bpaCommonExtnService.generateCmdaLetterToPartyPrint(null,letterToPartyCmdaId);	
			reportParams.put("lpDate", reportData.get("lpDate"));
			reportParams.put("cmdaDate", reportData.get("cmdaDate"));
			reportParams.put("replyDate", reportData.get("replyDate"));
			reportInput=new ReportRequest(BpaConstants.CMDALPREPLYACKREPORTEXTN, reportData, reportParams);
			reportInput.setPrintDialogOnOpenReport(true);
		}
		return reportInput;
	}
	
	private void getFromAddressToReport(RegistrationExtn registration) {
		if(registration.getAdminboundaryid().getParent().getParent().getName().equalsIgnoreCase(BpaConstants.NORTHREGION))
		{
			fromAddressToLp=BpaConstants.ASSISTANTADDRESS+ "\n" +BpaConstants.NORTHREGION_ADDRESS;
		}
		else if(registration.getAdminboundaryid().getParent().getParent().getName().equalsIgnoreCase(BpaConstants.SOUTHREGION))
		{
			fromAddressToLp=BpaConstants.ASSISTANTADDRESS + "\n"  + BpaConstants.SOUTHREGION_ADDRESS;
									
		}
		else if(registration.getAdminboundaryid().getParent().getParent().getName().equalsIgnoreCase(BpaConstants.CENTRALREGION))
		{
			fromAddressToLp=BpaConstants.ASSISTANTADDRESS + "\n" + BpaConstants.CENTRALREGION_ADDRESS;
		}
		else
		{
			fromAddressToLp="";
		}
		
	}

	private void getPlanningpermitReportDetails(
			Map<String, Object> reportParams, RegistrationExtn registration) {
		List<ReportFeesDetailsExtn> reportFeesDtlsList = bpaReportExtnService.getReportFeesDetails(registration,BpaConstants.PLANPERMIT);
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

		//State stateValue=getApprovedDate(registration,"Approved");
		
/*	if(registration.getBaOrderDate()!=null){
		//	reportParams.put("validityDate", DateUtils.add(registration.getBaOrderDate(), Calendar.MONTH, 6));
		//	reportParams.put("constructionValidityDate", DateUtils.add(registration.getBaOrderDate(), Calendar.YEAR, 3));
			
			reportParams.put("validityDate", DateUtils.add(DateUtils.add(registration.getBaOrderDate(), Calendar.MONTH, 6),Calendar.DAY_OF_MONTH,-1));
			reportParams.put("constructionValidityDate", DateUtils.add(DateUtils.add(registration.getBaOrderDate(), Calendar.YEAR, 3),Calendar.DAY_OF_MONTH,-1));
			
		} */
		//reportParams.put("approvedDate", stateValue.getModifiedDate()!=null?stateValue.getModifiedDate():null);
		bpaReportExtnService.getApprovedDateByPassingRegistrationObject(reportParams,
				registration,BpaConstants.PLANPERMIT);
		//Add External Fee details into planning permit print
		addExternalFeeDetails(registration, reportParams,BpaConstants.EXTERNALFEECMDA);
		bpaReportExtnService.getReceiptinstrumentInfo(registration,reportParams,BpaConstants.PLANPERMIT);  
	}

	private void getBuildingPermitReportdetails(
			Map<String, Object> reportParams, RegistrationExtn registration) {
		List<ReportFeesDetailsExtn> reportFeesDtlsList = bpaReportExtnService.getReportFeesDetails(registration,BpaConstants.BUILDINGPERMIT);
		
		// Add External Fee details into building plan approval print
		addExternalFeeDetails(registration, reportParams,BpaConstants.EXTERNALFEELABOURWELFARE);			
//	State stateValue=getApprovedDate(registration,"Approved");
		reportParams.put("reportFeeList", reportFeesDtlsList);
		
		if(registration!=null && registration.getServiceType()!=null && registration.getServiceType().getCode().equals(BpaConstants.CMDACODE) && registration.getCmdaNum()!=null && registration.getCmdaRefDate()!=null)
		{
			reportParams.put("PlanSubmissionNumber", registration.getCmdaNum()+" - "+DateUtils.getDefaultFormattedDate(registration.getCmdaRefDate()));
		}else {
			reportParams.put("PlanSubmissionNumber", registration.getPlanSubmissionNum());
		}
		
		/*if(registration.getBaOrderDate()!=null){
			//reportParams.put("validityDate", DateUtils.add(registration.getBaOrderDate(), Calendar.MONTH, 6));
			//reportParams.put("constructionValidityDate", DateUtils.add(registration.getBaOrderDate(), Calendar.YEAR, 2));
			reportParams.put("validityDate", DateUtils.add(DateUtils.add(registration.getBaOrderDate(), Calendar.MONTH, 6),Calendar.DAY_OF_MONTH,-1));
			reportParams.put("constructionValidityDate", DateUtils.add(DateUtils.add(registration.getBaOrderDate(), Calendar.YEAR, 2),Calendar.DAY_OF_MONTH,-1));
			
			
		}*/
//	reportParams.put("approvedDate", stateValue.getModifiedDate()!=null?stateValue.getModifiedDate():null);
		bpaReportExtnService.getReceiptinstrumentInfo(registration,reportParams,BpaConstants.BUILDINGPERMIT);  
		bpaReportExtnService.getApprovedDateByPassingRegistrationObject(reportParams,
				registration,BpaConstants.BUILDINGPERMIT);
	}

	
	private State getApprovedDate(RegistrationExtn registration,String value) {
		State stateValue=	registration.getState();

		if(null!=stateValue && null!=stateValue.getValue()){
			while(!value.equals(stateValue.getValue())){/*
				if(null!=stateValue.getPrevious() && null!=stateValue.getPrevious().getValue())
					stateValue=stateValue.getPrevious();	
				else
					break;*///TODO PHIOnix
			}
		}
		return stateValue;
	}


	/**
	 * Will be used to extract DD details (External Fees) of concerned registration object.
	 * @param registration
	 * @param reportParams
	 * @param typeofDdType
	 */
	private void addExternalFeeDetails(RegistrationExtn registration,
			Map<String, Object> reportParams, String typeofDdType) {
		StringBuffer externalFee = new StringBuffer();

		if (registration != null && !registration.getFeeDDSet().isEmpty()) {
			for (RegistrationDDDetailsExtn regnDDDetail : registration
					.getFeeDDSet()) {
				if (regnDDDetail.getDdType() != null
						&& regnDDDetail.getDdType().equals(typeofDdType)) {
					externalFee.append("DD Number: ");
					externalFee.append(regnDDDetail.getDdNumber()).append(" ");
					externalFee.append(" Amount: ");
					externalFee.append(regnDDDetail.getDdAmount()).append(
							" Rs. ");

					if (regnDDDetail.getDdDate() != null) {
						externalFee.append(" Date:");
						externalFee.append(DateUtils
								.getDefaultFormattedDate(regnDDDetail
										.getDdDate()));
					}
					if (regnDDDetail.getDdBank() != null && regnDDDetail.getDdBank()!=null ) {
						externalFee.append(" Bank:");
						externalFee.append(regnDDDetail.getDdBank().getName());
					}
					externalFee.append(" \n");
				}

			}

		}

		if (externalFee.length() == 0) {
			externalFee.append(BpaConstants.EXTERNALFEENODATAFOUND);
		}
		reportParams.put("externalFeeDetail", externalFee.toString());
	}

	
	




	public ReportService getReportService() {
		return reportService;
	}

	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	public Integer getReportId() {
		return reportId;
	}

	public void setReportId(Integer reportId) {
		this.reportId = reportId;
	}

	public Long getRegistrationId() {
		return registrationId;
	}

	public void setRegistrationId(Long registrationId) {
		this.registrationId = registrationId;
	}

	public Object getReportInputData() {
		return reportInputData;
	}

	public void setReportInputData(Object reportInputData) {
		this.reportInputData = reportInputData;
	}

	public String getReportTemplate() {
		return reportTemplate;
	}

	public void setReportTemplate(String reportTemplate) {
		this.reportTemplate = reportTemplate;
	}

	public String getPrintMode() {
		return printMode;
	}

	public void setPrintMode(String printMode) {
		this.printMode = printMode;
	}

	public void setLetterToPartyId(Long letterToPartyId) {
		this.letterToPartyId = letterToPartyId;
	}


	

	public RegisterBpaExtnService getRegisterBpaExtnService() {
		return registerBpaExtnService;
	}

	public void setRegisterBpaExtnService(
			RegisterBpaExtnService registerBpaExtnService) {
		this.registerBpaExtnService = registerBpaExtnService;
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

	public String getFromAddressToLp() {
		return fromAddressToLp;
	}

	public void setFromAddressToLp(String fromAddressToLp) {
		this.fromAddressToLp = fromAddressToLp;
	}

	public BpaReportExtnService getBpaReportExtnService() {
		return bpaReportExtnService;
	}

	public void setBpaReportExtnService(BpaReportExtnService bpaReportExtnService) {
		this.bpaReportExtnService = bpaReportExtnService;
	}

	public Long getLetterToPartyCmdaId() {
		return letterToPartyCmdaId;
	}

	public void setLetterToPartyCmdaId(Long letterToPartyCmdaId) {
		this.letterToPartyCmdaId = letterToPartyCmdaId;
	}

	
}
