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
package org.egov.bpa.web.actions.extd.fee;

import java.math.BigDecimal;
import java.security.acl.Owner;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.jackrabbit.core.security.user.UserImpl;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.bpa.constants.BpaConstants;
import org.egov.bpa.models.extd.RegistrationExtn;
import org.egov.bpa.models.extd.RegistrationFeeDetailExtn;
import org.egov.bpa.models.extd.RegistrationFeeExtn;
import org.egov.bpa.models.extd.ReportFeesDetailsExtn;
import org.egov.bpa.models.extd.masters.BpaFeeExtn;
import org.egov.bpa.services.extd.Fee.RegistrationFeeExtnService;
import org.egov.bpa.services.extd.common.BpaNumberGenerationExtnService;
import org.egov.bpa.services.extd.common.BpaPimsInternalExtnServiceFactory;
import org.egov.bpa.services.extd.register.RegisterBpaExtnService;
import org.egov.commons.EgwSatuschange;
import org.egov.commons.EgwStatus;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.reporting.engine.ReportService;
import org.egov.infstr.utils.StringUtils;

@ParentPackage("egov")
public class RevisedFeeExtnAction  extends FeeDetailsExtnAction{

	private RegistrationFeeExtn registrationFee = new RegistrationFeeExtn();
	private List<RegistrationFeeExtn> existingFeeDetails =new ArrayList<RegistrationFeeExtn>(0);
	private Long registrationFeeId;
	private List<RegistrationFeeDetailExtn> feeDetailsList=new ArrayList<RegistrationFeeDetailExtn>();
	private BigDecimal feeDetailTotal=BigDecimal.ZERO;
	private String feeDetailRemarks;
	private BpaNumberGenerationExtnService bpaNumberGenerationExtnService;
	private Boolean canProceed;
	private ReportService reportService;
	public static final String FEE_PAYMENT_PDF = "feePaymentPdf";
	private RegisterBpaExtnService registerBpaExtnService;
	private SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
	private final static  Logger LOGGER=Logger.getLogger(RevisedFeeExtnAction.class);
	private List<RegistrationFeeDetailExtn> CMDARegFeeList=new ArrayList();
	private List<RegistrationFeeDetailExtn> COCRegFeeList=new ArrayList();
	private List<RegistrationFeeDetailExtn> MWGWFRegFeeList=new ArrayList();
	private BpaPimsInternalExtnServiceFactory bpaPimsExtnFactory;
	private List<AppConfigValues> appConfigValuesRoleList;
	private String  bparoles;
	private String previousApproverComments;
	
	public String getPreviousApproverComments() {
		return previousApproverComments;
	}
	public void setPreviousApproverComments(String previousApproverComments) {
		this.previousApproverComments = previousApproverComments;
	}
	public List<AppConfigValues> getAppConfigValuesRoleList() {
		return appConfigValuesRoleList;
	}
	public void setAppConfigValuesRoleList(
			List<AppConfigValues> appConfigValuesRoleList) {
		this.appConfigValuesRoleList = appConfigValuesRoleList;
	}
	public String getBparoles() {
		return bparoles;
	}
	public void setBparoles(String bparoles) {
		this.bparoles = bparoles;
	}
	public BpaPimsInternalExtnServiceFactory getBpaPimsExtnFactory() {
		return bpaPimsExtnFactory;
	}
	public void setBpaPimsExtnFactory(BpaPimsInternalExtnServiceFactory bpaPimsFactory) {
		this.bpaPimsExtnFactory = bpaPimsFactory;
	}
	public List<RegistrationFeeDetailExtn> getCMDARegFeeList() {
		return CMDARegFeeList;
	}
	public void setCMDARegFeeList(List<RegistrationFeeDetailExtn> regFeeList) {
		CMDARegFeeList = regFeeList;
	}
	public List<RegistrationFeeDetailExtn> getCOCRegFeeList() {
		return COCRegFeeList;
	}
	public void setCOCRegFeeList(List<RegistrationFeeDetailExtn> regFeeList) {
		COCRegFeeList = regFeeList;
	}
	public List<RegistrationFeeDetailExtn> getMWGWFRegFeeList() {
		return MWGWFRegFeeList;
	}
	public void setMWGWFRegFeeList(List<RegistrationFeeDetailExtn> regFeeList) {
		MWGWFRegFeeList = regFeeList;
	}
	public RegisterBpaExtnService getRegisterBpaExtnService() {
		return registerBpaExtnService;
	}
	public void setRegisterBpaExtnService(RegisterBpaExtnService registerBpaService) {
		this.registerBpaExtnService = registerBpaService;
	}
	public ReportService getReportService() {
		return reportService;
	}
	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}
	public Boolean getCanProceed() {
		return canProceed;
	}
	public void setCanProceed(Boolean canProceed) {
		this.canProceed = canProceed;
	}
	public BpaNumberGenerationExtnService getBpaNumberGenerationExtnService() {
		return bpaNumberGenerationExtnService;
	}
	public void setBpaNumberGenerationExtnService(
			BpaNumberGenerationExtnService bpaNumberGenerationService) {
		this.bpaNumberGenerationExtnService = bpaNumberGenerationService;
	}
	public RevisedFeeExtnAction(){
		this.addRelatedEntity("egwStatus", EgwStatus.class);
		this.addRelatedEntity("registration", RegistrationExtn.class);
		this.addRelatedEntity("modifiedBy", UserImpl.class);
		this.addRelatedEntity("createdBy", UserImpl.class);
		this.addRelatedEntity("state", org.egov.infstr.models.State.class);
	}
	@Override
	public StateAware getModel() {
		return registrationFee;
	}

	public void prepare()
	{
		super.prepare();
		if(registrationFeeId!=null)
			registrationFee=registrationFeeExtnService.getRegistrationFeeById(registrationFeeId);
		appConfigValuesRoleList=bpaCommonExtnService.getAppConfigValue("BPA",BpaConstants.ROLELISTFORBPA);

		LOGGER.info(appConfigValuesRoleList.size());
		if(!appConfigValuesRoleList.isEmpty()){

		bparoles= appConfigValuesRoleList.get(0).getValue();
		}
	}




	public String newForm(){
		LOGGER.debug("Start newForm");
		if(registrationFeeId!=null){
		if(registrationObj!=null && registrationObj.getId()!=null && registrationFeeExtnService.getAlreadyCreatedRegistrationFee(registrationObj.getId())==0)
			setCanProceed(Boolean.TRUE);
			else{
				setCanProceed(Boolean.FALSE);
				//Move following message to bpaconstants.java
				addActionError(" Revised Fee has already been created for the Registration " +registrationObj.getPlanSubmissionNum()+
						" ,Please approve or cancel this Revised Fee before creating a new Revised Fee");
			}
		}
		prepareNewForm();
		registrationFee.setRegistration(registrationObj);
		prepareExistingFeeDetails();
		registrationFee.setFeeDate(new Date());
		registrationFee.setIsRevised(Boolean.TRUE);
		LOGGER.debug("Exit newForm");
		return NEW;

	}

	private void prepareNewForm() {
		LOGGER.debug("Start prepareNewForm");
		if(registrationObj!=null){
			santionFeeList=	feeExtnService.getAllSanctionedFeesbyServiceType(registrationObj.getServiceType().getId());
			for(BpaFeeExtn fe:santionFeeList){
				RegistrationFeeDetailExtn feedtl=new RegistrationFeeDetailExtn();
				feedtl.setBpaFee(fe);
				feedtl.setAmount(BigDecimal.ZERO);
				feeDetailsList.add(feedtl);
			}

		}

		registrationFee.setFeeRemarks(BpaConstants.FeeRemarks);

		splitRegFeelistintosublists();
		LOGGER.debug("Exit prepareNewForm");
	}

	private  void prepareExistingFeeDetails() {
		LOGGER.debug("Start prepareExistingFeeDetails");
		if(registrationFee!=null&&registrationFee.getRegistration()!=null)
		existingFeeDetails=registrationFeeExtnService.getPriorFeeDetailsExcludingCurrentRegFeeId(registrationFee.getRegistration().getId(),registrationFee.getId());

		if(existingFeeDetails.isEmpty())
			checkforLegacyFee(existingFeeDetails);
		LOGGER.debug("Exit prepareExistingFeeDetails");
	}


	private void checkforLegacyFee(List<RegistrationFeeExtn> existingFeeDetails) {
		LOGGER.debug("Start checkforLegacyFee");
		Boolean isDetailInFeeTable=Boolean.FALSE;

		if(existingFeeDetails.isEmpty()){

			//Dummy registration object created to show fee details in UI for legacy data.
			RegistrationFeeExtn regFee=	new RegistrationFeeExtn();
			if(registrationObj!=null){
				//Get fee created date from egw status change table.
					List<EgwSatuschange> statuschangeList=	registrationFeeExtnService.getFileConsiderationCheckedDate(registrationObj);
					if(statuschangeList!=null && !statuschangeList.isEmpty()){
						regFee.setFeeDate(statuschangeList.get(0).getLastmodifieddate());
					}

					regFee.setRegistration(registrationObj);
					regFee.setLegacyFee("true");

					regFee.setEgwStatus(bpaCommonExtnService.getstatusbyCode(BpaConstants.BPAREGISTRATIONFEEMODULESTATUSAPPROVED,BpaConstants.BPAREGISTRATIONFEEMODULE));
					regFee.setChallanNumber("NA"); //As challan number not generated for legacy data.
					existingFeeDetails.add(regFee);
			}
		}
		LOGGER.debug("Exit checkforLegacyFee");
		}


	public String showExistingFeeDetails(){
		LOGGER.debug("Start showExistingFeeDetails");
		if(registrationId!=null)
		existingFeeDetails=registrationFeeExtnService.getpreviousFeeDetails(registrationId);
		if(existingFeeDetails.size()>0)
		existingFeeDetails=existingFeeDetails.subList(1, existingFeeDetails.size());
		LOGGER.debug("Exit showExistingFeeDetails");
		return "previousFeeRegistration";
	}


	public String showFeeDetails(){
		LOGGER.debug("Start showFeeDetails");
	 feeDetailsList =	registrationFeeExtnService.getRegistrationFeeDetails(getRegistrationFeeId());
	 for(RegistrationFeeDetailExtn regfeedtl:feeDetailsList){
		 feeDetailTotal=feeDetailTotal.add(regfeedtl.getAmount()) ;
	 }
	 setFeeDetailRemarks(registrationFeeExtnService.getRegistrationFeeById(getRegistrationFeeId()).getFeeRemarks());
	 splitRegFeelistintosublists();
		LOGGER.debug("Exit showFeeDetails");
	 return "feeDetail";
	}

	public String save(){
		LOGGER.debug("Start save");

		// First time, Add legacy fee details into registration fee table.
		if((registrationFeeExtnService.getNonRevisedRegistrationFees(registrationFee.getRegistration().getId())==null)){
			super.buildVieworModify(registrationFee.getRegistration());
			 // get the date from egstatus table
			List<EgwSatuschange> statuschangeList=	registrationFeeExtnService.getFileConsiderationCheckedDate(registrationObj);
			Date feeDate=null;
			if(!statuschangeList.isEmpty()){
				 feeDate=(statuschangeList.get(0).getLastmodifieddate());
			}
			LOGGER.debug("saving legacy fees in registration fee");
			 registrationFeeExtnService.saveLegecyFeesinRegistrationFee(registrationFee.getRegistration(),santionFeeList,feeDate);

		}

		mergeRegSublistintoFees();

		LOGGER.debug("saving  registration fee");
		//Persist new registration fee details
		registrationFee= registrationFeeExtnService.saveRegistrationFee(registrationFee,getFeeDetailsList(),workFlowAction,approverComments);
		EgwStatus oldStatus = null;

		if(registrationFee.getRegistration()!=null)
		 oldStatus= registrationFee.getRegistration().getEgwStatus();

		if(registrationFee!=null&&registrationFee.getEgwStatus()!=null && registrationFee.getEgwStatus().getCode().equalsIgnoreCase(BpaConstants.STATUSAPPROVED)){


			BoundaryImpl zone=(BoundaryImpl) bpaCommonExtnService.getZoneNameFromAdminboundaryid(registrationFee.getRegistration().getAdminboundaryid());
			Integer size=  registrationFeeExtnService.getRevisedApprovedRegistrationFeeSize(registrationFee.getRegistration().getId());
			registrationFee.setChallanNumber(bpaNumberGenerationExtnService.generateRevisedFeeChallanNumberFormat(zone,size));
			super.buildVieworModify(registrationFee.getRegistration());
			for(int i=0;i<santionFeeList.size();i++){ // santionFeeList for demand generation by service type.

				for(int j=0;j<getFeeDetailsList().size();j++){ // getFeeDetailsList is new data getting from UI
					if(getFeeDetailsList().get(j).getBpaFee().getId().equals(santionFeeList.get(i).getId())){
						santionFeeList.get(i).setFeeAmount(getFeeDetailsList().get(j).getAmount());
					}
				}
			}
			LOGGER.debug("saving approved revised registration fees in demand");
			feeDetailsExtnService.save(santionFeeList,registrationFee.getRegistration());
			if(registrationFee.getRegistration()!=null)
			feeDetailsExtnService.removeCMDAandMWGWFDetailsForLegacy(registrationFee.getRegistration());
			registrationFee.getRegistration().setEgwStatus(bpaCommonExtnService.getstatusbyCode(BpaConstants.CHALLANNOTICESENT));
		/*	RegnStatusDetails regStatusDtl=		new RegnStatusDetails();
			regStatusDtl.setStatusdate(new Date());
			registerBpaService.createRegnStatusDetails(regStatusDtl,
					BpaConstants.CHALLANNOTICESENT, registrationFee.getRegistration());*/

			//Preparing data for print

			List<Map<String, Object>> finalAttachmentList = new ArrayList<Map<String,Object>>();

				HashMap<String, Object> attachmentList = new HashMap<String, Object>();
				HashMap<String, Object> attachmentFileNames = new HashMap<String, Object>();
				List<ReportFeesDetailsExtn> reportFeeDetailsList=registerBpaExtnService.getSanctionedReportFeeDtls(registrationFee.getRegistration());
				attachmentList.put("feePayment", bpaCommonExtnService.generateFeePaymentReportPDF(registrationFee.getRegistration(),reportFeeDetailsList));
				attachmentFileNames.put("feePaymentFileName", "revisedFeePDF.pdf");
				finalAttachmentList.add(attachmentList);
				finalAttachmentList.add(attachmentFileNames);

			HashMap<String,String> additionalparams=new HashMap<String,String>(0);
			additionalparams.put("revisedchallanNo", registrationFee.getChallanNumber()); // TODO: add challan number in print format.
			additionalparams.put("revisedFeeApprovedDate", sdf.format(new Date()));


			Owner citizenDetails =null;

			if(registrationFee!=null && registrationFee.getRegistration()!=null)
				citizenDetails=registrationFee.getRegistration().getOwner();

			if(citizenDetails !=null && citizenDetails.getEmailAddress()!=null && StringUtils.isNotEmpty(citizenDetails.getEmailAddress())){

				try{
					bpaCommonExtnService.buildEmail(registrationFee.getRegistration(),BpaConstants.SMSEMAILREVISEDFEEAPPROVE,finalAttachmentList,additionalparams);
			}catch(Exception ex){
				LOGGER.debug("Error occured in send sms for registration ");
			}
			}
			if(citizenDetails !=null && citizenDetails.getMobilePhone()!=null && StringUtils.isNotEmpty(citizenDetails.getMobilePhone()))
				try{
					bpaCommonExtnService.buildSMS(registrationFee.getRegistration(),BpaConstants.SMSEMAILREVISEDFEEAPPROVE,additionalparams);
				}catch(Exception ex){
					LOGGER.debug("Error occured in send sms for registration ");
				}
		}else if(registrationFee.getEgwStatus().getCode().equalsIgnoreCase(BpaConstants.CANCELLED)){
			registrationFee.getRegistration().setEgwStatus(bpaCommonExtnService.getstatusbyCode(BpaConstants.CHALLANNOTICESENT));
			/*RegnStatusDetails regStatusDtl=		new RegnStatusDetails();
			regStatusDtl.setStatusdate(new Date());
			registerBpaService.createRegnStatusDetails(regStatusDtl,
					BpaConstants.CHALLANNOTICESENT, registrationFee.getRegistration());*/
		}
		else{

			registrationFee.getRegistration().setEgwStatus(bpaCommonExtnService.getstatusbyCode(BpaConstants.RevisedFeeInitiated));
				/*RegnStatusDetails regStatusDtl=		new RegnStatusDetails();
				regStatusDtl.setStatusdate(registrationFee.getCreatedDate());
				registerBpaService.createRegnStatusDetails(regStatusDtl,
						BpaConstants.RevisedFeeInitiated, registrationFee.getRegistration());*/

		}
		bpaCommonExtnService.createStatusChange(registrationFee.getRegistration(),oldStatus);
		prepareExistingFeeDetails();
		confirmationAlertMessages();
		buildVieworModifyRegFee();
		setMode("view");
		LOGGER.debug("Exit save");

		return NEW;
	}



  @SkipValidation
	public String modify(){
	  LOGGER.debug("Start modify");
	prepareExistingFeeDetails();
	  buildVieworModifyRegFee();
	  setMode("edit");
	  LOGGER.debug("Exit modify");
		return NEW;
	}

	private void confirmationAlertMessages() {
		 LOGGER.debug("Start confirmationAlertMessages");
		String  userNameWithDesignation="@";

		if(registrationFee!=null && registrationFee.getState()!=null){
			if(registrationFee.getState().getValue()!=null &&  !registrationFee.getState().getValue().equals(BpaConstants.WF_END_STATE) &&
					!registrationFee.getState().getValue().equals(BpaConstants.WF_CANCELLED) &&
					!registrationFee.getState().getValue().equals(BpaConstants.WF_NEW_STATE) )
				userNameWithDesignation=bpaCommonExtnService.getEmpNameDesignation(registrationFee.getState().getOwner(), new Date());
		}
		String Challanno =	"";
		if(registrationFee.getChallanNumber()!=null&&!registrationFee.getChallanNumber().equals(""))
			Challanno=registrationFee.getChallanNumber();
		if("".equals(mode) || mode==null) {

			addActionMessage(" Revised Registration Fee "+Challanno +" is created successfully");
		}
		else
			addActionMessage(" Revised Registration Fee "+ Challanno +" is updated successfully");


		   if(registrationFee.getEgwStatus()!=null&&registrationFee.getEgwStatus().getCode().equals(BpaConstants.STATUSAPPROVED))
				   addActionMessage("Revised Registration Fee has been Approved.");

		   else
		   {
				   if( !"@".equals(userNameWithDesignation) )
						addActionMessage("The Revised Registration Fee has been forwarded to  "+userNameWithDesignation );

		   }
		   LOGGER.debug("Exit confirmationAlertMessages");
	}

	public void splitRegFeelistintosublists(){
		 LOGGER.debug("Start splitRegFeelistintosublists");
		CMDARegFeeList.clear();
		COCRegFeeList.clear();
		MWGWFRegFeeList.clear();
		for(RegistrationFeeDetailExtn regfees:feeDetailsList){

			if(regfees.getBpaFee().getFeeGroup()!=null){
			if(regfees.getBpaFee().getFeeGroup().equals(BpaConstants.CMDAFEE)){
				CMDARegFeeList.add(regfees);
			}
			else if(regfees.getBpaFee().getFeeGroup().equals(BpaConstants.COCFEE)){
				COCRegFeeList.add(regfees);
			}
			else if(regfees.getBpaFee().getFeeGroup().equals(BpaConstants.MWGWFFEE)){
				MWGWFRegFeeList.add(regfees);
			}
			}
		}
		  LOGGER.debug("Exit splitRegFeelistintosublists");
	}


	public void mergeRegSublistintoFees(){
		 LOGGER.debug("Start mergeRegSublistintoFees");
		for(RegistrationFeeDetailExtn regfees:CMDARegFeeList){
			feeDetailsList.add(regfees);
		}
		for(RegistrationFeeDetailExtn regfees:COCRegFeeList){
			feeDetailsList.add(regfees);
		}
		for(RegistrationFeeDetailExtn regfees:MWGWFRegFeeList){
			feeDetailsList.add(regfees);
		}
		  LOGGER.debug("Exit mergeRegSublistintoFees");
	}



  public void buildVieworModifyRegFee(){
	  LOGGER.debug("Start buildVieworModifyRegFee");
	  feeDetailsList=registrationFeeExtnService.getRegistrationFeeDetails(registrationFee.getId());
	  splitRegFeelistintosublists();
	  /*
		 * To Show Previous User Approver Comments From Workflow..
		 */
	  if(registrationFee!=null){
		  previousApproverComments=bpaCommonExtnService.getPreviousUserCommentsFromWorkfowForRevisedFee(registrationFee);
		  }
	  LOGGER.debug("Exit buildVieworModifyRegFee");
  }

  public  Integer getDepartmentForLoggedInUser() {

		return bpaPimsExtnFactory.getPrimaryDepartmentforLoggedinUser();
	}

	public String getFeeDetailRemarks() {
		return feeDetailRemarks;
	}

	public void setFeeDetailRemarks(String feeDetailRemarks) {
		this.feeDetailRemarks = feeDetailRemarks;
	}

	public BigDecimal getFeeDetailTotal() {
		return feeDetailTotal;
	}

	public void setFeeDetailTotal(BigDecimal feeDetailTotal) {
		this.feeDetailTotal = feeDetailTotal;
	}

		public List<RegistrationFeeDetailExtn> getFeeDetailsList() {
		return feeDetailsList;
	}

	public void setFeeDetailsList(List<RegistrationFeeDetailExtn> feeDetailsList) {
		this.feeDetailsList = feeDetailsList;
	}

		public Long getRegistrationFeeId() {
		return registrationFeeId;
	}

	public void setRegistrationFeeId(Long registrationFeeId) {
		this.registrationFeeId = registrationFeeId;
	}

		public RegistrationFeeExtnService getRegistrationFeeExtnService() {
			return registrationFeeExtnService;
		}

		public void setRegistrationFeeExtnService(
				RegistrationFeeExtnService registrationFeeService) {
			this.registrationFeeExtnService = registrationFeeService;
		}
	public List<RegistrationFeeExtn> getExistingFeeDetails() {
		return existingFeeDetails;
	}
	public void setExistingFeeDetails(List<RegistrationFeeExtn> existingFeeDetails) {
		this.existingFeeDetails = existingFeeDetails;
	}
}
