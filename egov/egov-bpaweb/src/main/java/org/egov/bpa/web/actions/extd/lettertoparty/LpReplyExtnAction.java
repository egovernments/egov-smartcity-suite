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
package org.egov.bpa.web.actions.extd.lettertoparty;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.bpa.constants.BpaConstants;
import org.egov.bpa.models.extd.LetterToPartyExtn;
import org.egov.bpa.models.extd.LpChecklistExtn;
import org.egov.bpa.models.extd.RegistrationExtn;
import org.egov.bpa.services.extd.common.BpaCommonExtnService;
import org.egov.bpa.services.extd.common.BpaNumberGenerationExtnService;
import org.egov.bpa.services.extd.inspection.InspectionExtnService;
import org.egov.bpa.services.extd.lettertoparty.LetterToPartyExtnService;
import org.egov.bpa.services.extd.register.RegisterBpaExtnService;
import org.egov.commons.EgwStatus;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.reporting.viewer.ReportViewerUtil;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infstr.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Transactional(readOnly = true)
@Namespace("/lettertoparty")
@ParentPackage("egov")
public class LpReplyExtnAction extends BaseFormAction{
	
	private RegistrationExtn registration;
	private static final Logger LOGGER					= Logger.getLogger(LpReplyExtnAction.class);
	private LetterToPartyExtn letterParty;
	private Long registrationId;
	private String requestID;
	private LetterToPartyExtn letterPartyReply = new LetterToPartyExtn();
	private LetterToPartyExtnService letterToPartyExtnService;
	private InspectionExtnService inspectionExtnService;
	private BpaCommonExtnService bpaCommonExtnService;
	private String existLpReason;
	private String existLpRemarks;
	private User loginUser;
	private Long serviceTypeId;
	private List<LpChecklistExtn> lpChkListDet = new ArrayList<LpChecklistExtn>();
	private List<LpChecklistExtn> lpChkListnewDet = new ArrayList<LpChecklistExtn>();
	private BpaNumberGenerationExtnService bpaNumberGenerationExtnService;
	private String mode;
	private ReportService reportService;
	private Integer reportId = -1;
	//private EisManager eisManager;
	private String documentNum;
	private String existLpNum;
	private RegisterBpaExtnService registerBpaExtnService;
	private Long letterToPartyId;
	public void prepare() {
		super.prepare();
		loginUser=inspectionExtnService.getUserbyId((EgovThreadLocals.getUserId()));
		
		if(registration!=null && registration.getRequest_number()!=null) {
			registration=registerBpaExtnService.getRegistrationByPassingRequestNumber(registration.getRequest_number());
		}
		else if(!"".equals(getRequestID()) && getRequestID()!=null){
			registration=registerBpaExtnService.getRegistrationByPassingRequestNumber(getRequestID());
		}
		
		if(!"".equals(getRequestID()) && getRequestID()!=null && registration!=null){
			registration = letterToPartyExtnService.getRegistrationObjectbyId(registration.getId());
			letterParty = letterToPartyExtnService.getLatestLetterToPartyForRegObj(registration);
			if(letterParty != null) {
				getLetterToPartyDetails(letterParty);
				showCheckList();
			}
		}
		if (getRegistrationId() != null)  {
			registration = letterToPartyExtnService.getRegistrationObjectbyId(getRegistrationId());
			letterParty = letterToPartyExtnService.getLatestLetterToPartyForRegObj(registration);
			if(letterParty != null) {
				getLetterToPartyDetails(letterParty);
			}
		}
		if(letterParty!=null && letterParty.getDocumentid()!=null)			
			this.setDocumentNum(letterParty.getDocumentid());
	}
	@SkipValidation
	@Action(value = "/lpReplyExtn-newForm", results = { @Result(name = NEW,type = "dispatcher") })
	public String newForm() {
		
		if (letterParty == null || 
				(letterParty != null && letterParty.getIsHistory() != null && letterParty.getIsHistory().equals('Y'))) {
			
			if (letterParty == null) {
				
				addActionMessage(getMessage("lpreply.noLpresent.validate"));
			}
			else if (letterParty.getIsHistory().equals('Y')) {
				addActionMessage(getMessage("lpreply.Lpreceived.validate"));
				setMode(BpaConstants.MODEVIEW);
			}
			
			
			return NEW;
		} 
		else if(letterParty.getRegistration()!=null && letterParty.getRegistration().getEgwStatus()!=null && letterParty.getRegistration().getEgwStatus().getCode()!=null && 
				letterParty.getRegistration().getEgwStatus().getCode().equals(BpaConstants.LETTERTOPARTYSENT)) {
			
			List<LetterToPartyExtn>	 lpParty=letterToPartyExtnService.getLetterToPartyForRegnByComparingLPSentDateWithSysDateToTenDays(registration, letterParty.getSentDate());
			if(lpParty.size()>0) 
			{
				addActionMessage(getMessage("lpreply.actionvalidate.message"));
				setMode(BpaConstants.MODEVIEW);
			}
			else
			{
				getLetterToPartyDetails(letterParty);
				setMode("NEW");
				return NEW;
			}
		}
		else if(letterParty!=null && letterParty.getRegistration().getEgwStatus()!=null && letterParty.getRegistration().getEgwStatus().getCode()!=null && 
				letterParty.getRegistration().getEgwStatus().getCode().equals(BpaConstants.CREATEDLETTERTOPARTY))
		{
			addActionMessage(getMessage("lpreply.noLpresent.validate"));
		}
		setMode("noEditMode");
		return NEW;
	}
	
	private void getLetterToPartyDetails(LetterToPartyExtn letterParty) {
		setExistLpReason(letterParty.getLetterToPartyReason().getCode());
		setExistLpRemarks(letterParty.getLetterToPartyRemarks());
		setExistLpNum(letterParty.getLetterToPartyNumber());
	}
	protected String getMessage(String key)
	{
		return getText(key);
	}

	
	@ValidationErrorPage(NEW)	
	@Action(value = "/lpReplyExtn-createLpReply", results = { @Result(name = NEW,type = "dispatcher") })
	   public String createLpReply() {
		
		try{
	    Set<LpChecklistExtn> lpChecklistSet=new HashSet<LpChecklistExtn>(getLpChkListDet());
		EgwStatus oldStatus = letterParty.getRegistration().getEgwStatus();
		String ackNum = bpaNumberGenerationExtnService.generateLetterToReplyAckNumber();
		EgwStatus egwStatus =bpaCommonExtnService.getstatusbyCode(BpaConstants.LPREPLYRECEIVED,BpaConstants.NEWBPAREGISTRATIONMODULE); 
		
		letterParty.setReplyDate(new Date());
		letterParty.setIsHistory('Y');
		letterParty.setLetterToPartyReplyDesc(letterPartyReply.getLetterToPartyReplyDesc());
		letterParty.setLetterToPartyReplyRemarks(letterPartyReply.getLetterToPartyReplyRemarks());
		letterParty.setAcknowledgementNumber(ackNum);
		letterParty.getRegistration().setEgwStatus(egwStatus);
		setDocumentNumberForLetterToParty();
		letterParty.setLpChecklistSet(lpChecklistSet);
		for(LpChecklistExtn lpchkList : letterParty.getLpChecklistSet()) {
			lpchkList.setLetterToParty(letterParty);
			lpchkList.setLpChecklistType(BpaConstants.LPCHKLISTTYPE_REPLY);
		}
		addActionMessage("Letter To Party Reply Details Saved Successfully");
		bpaCommonExtnService.createStatusChange(registration, oldStatus);
		setMode(BpaConstants.MODEVIEW);
		setRegistrationId(registrationId);
		setLetterParty(letterParty);
		setLetterPartyReply(letterParty);
		
		if(null!=registration.getEgwStatus() && null!=registration.getEgwStatus().getCode() && registration.getEgwStatus().getCode().equalsIgnoreCase(BpaConstants.LETTERTOPARTYREPLYRECEIVED)) {
			
			List<Map<String, Object>> finalAttachmentList = new ArrayList<Map<String,Object>>();
			HashMap<String, Object> attachmentList = new HashMap<String, Object>(); 
			HashMap<String, Object> attachmentFileNames = new HashMap<String, Object>(); 
		    attachmentList.put("letterToPartyReply", generateLetterToPartyReplyPDF());
			attachmentFileNames.put("letterToPartyReplyFileName", "letterToPartyReplyPDF.pdf"); 
			finalAttachmentList.add(attachmentList);
			finalAttachmentList.add(attachmentFileNames); 
			buildEmail(registration,BpaConstants.SMSEMAILLPR,finalAttachmentList);			
			buildSMS(registration,BpaConstants.SMSEMAILLPR);  
		}
		
		
		
		/* COmmented for time being */
		//createNotification();
		}catch (Exception e)
		{
			e.printStackTrace();
			throw new EGOVRuntimeException(" Error in Creating LP reply " + e.getMessage());
		}
		return NEW;
	}

	public void buildEmail(RegistrationExtn registration,String type,List<Map<String, Object>> finalAttachmentList) {
		String allowEmail=bpaCommonExtnService.getAppconfigValueResult(BpaConstants.BPAMODULENAME,"EMAIL_NOTIFICATION_ALLOWED_BPA",null);
		String body="";
		String subject="";
		Boolean flag=Boolean.FALSE;
/*	//	Surveyor surveyor=registration.getSurveyorName();
		if(null!=allowEmail && allowEmail!="" && allowEmail.equalsIgnoreCase("YES")){
			if(surveyor!=null && surveyor.getUserDetail()!=null &&  surveyor.getUserDetail().getEmail()!=null &&  StringUtils.isNotEmpty(surveyor.getUserDetail().getEmail())){
				flag=Boolean.TRUE;
				body=getText("registration.lettertopartyreply.email.body",new String[]{registration.getSurveyorName().getUserDetail().getFirstName(), 
						registration.getPlanSubmissionNum(),letterParty.getLetterToPartyNumber(),
						getText("reports.title.corporation_name")}); 
				subject=getText("registration.lettertopartyreply.email.subject",new String[]{registration.getPlanSubmissionNum()});
				if(flag){
					sendEmail(registration,body,subject,finalAttachmentList);
				}
			}
		}*///TODO PHionix
	}
	
	public void sendEmail(RegistrationExtn registration,String body, String emailSubject,List<Map<String, Object>> attachmentList)
	{
		try{//TODO PHionix
		/*if(registration!=null && registration.getSurveyorName()!=null &&  registration.getSurveyorName().getUserDetail()!=null &&
				registration.getSurveyorName().getUserDetail().getEmail()!=null && StringUtils.isNotEmpty(registration.getSurveyorName().getUserDetail().getEmail())){
			
			Builder builder = new Builder(registration.getSurveyorName().getUserDetail().getEmail(),body)
			.subject(emailSubject);
			
			if(attachmentList != null && (!attachmentList.isEmpty()) && attachmentList.size() != 0){
				Map<String, InputStream> attachmentFiles=new HashMap<String, InputStream>();
				Map<String, String> attachmentFileNames=new HashMap<String, String>();
				for(Map<String, Object> mailAttachment: attachmentList) {
					if(mailAttachment.get("letterToPartyReply")!=null) 
						attachmentFiles.put("letterToPartyReply", (InputStream) mailAttachment.get("letterToPartyReply"));
					if(mailAttachment.get("letterToPartyReplyFileName")!=null)
						attachmentFileNames.put("letterToPartyReplyFileName",  mailAttachment.get("letterToPartyReplyFileName").toString());
				}
				
				if(attachmentFiles.get("letterToPartyReply")!=null){
					builder.attachmentList( attachmentFiles.get("letterToPartyReply"), attachmentFileNames.get("letterToPartyReplyFileName"),"letter To Party Reply PDF");   
				}
			}
			Email email = builder.build();	 			
			email.send();
		 }*/
		}
		catch(EGOVRuntimeException egovExp){
			throw egovExp;
		}
	}
	
	
	public void buildSMS(RegistrationExtn registration,String type) { 
			String smsMsg = null;
			Boolean flag=Boolean.FALSE;
			//Surveyor surveyor=registration.getSurveyorName();
			String allowSms=bpaCommonExtnService.getAppconfigValueResult(BpaConstants.BPAMODULENAME,"SMS_NOTIFICATION_ALLOWED_BPA",null);
			if(null!=allowSms && allowSms!="" && allowSms.equalsIgnoreCase("YES")){
				/*if(surveyor!=null && surveyor.getUserDetail()!=null &&  surveyor.getUserDetail().getMobileNumber()!=null &&  StringUtils.isNotEmpty(surveyor.getUserDetail().getMobileNumber())){
					 if(type.equalsIgnoreCase(BpaConstants.SMSEMAILLP)){
								flag=Boolean.TRUE; 
								smsMsg = getText("registration.lettertopartyreply.sms.msg",new String[]{registration.getSurveyorName().getUserDetail().getFirstName(),registration.getPlanSubmissionNum(),DateUtils.getFormattedDate(registration.getPlanSubmissionDate(),"dd/MM/yyyy")});	
								if(isEmailSent(registration)){
									smsMsg=smsMsg+ getText("registration.lettertopartyreply.sms.withpdfmsg",new String[]{"Reply(LPR)"});
								}
						}//TODO PHionix
				}*/
			}
			if(flag){
				sendSMS(registration,smsMsg);
				}
	}
	
	public Boolean isEmailSent(RegistrationExtn registration){
		String allowEmail=bpaCommonExtnService.getAppconfigValueResult(BpaConstants.BPAMODULENAME,"EMAIL_NOTIFICATION_ALLOWED_BPA",null);
		if(null!=allowEmail && allowEmail!="" && allowEmail.equalsIgnoreCase("YES")){	
			/*if(null!=registration && registration.getSurveyorName() !=null && registration.getSurveyorName().getUserDetail()!=null && 
					registration.getSurveyorName().getUserDetail().getEmail()!=null && StringUtils.isNotEmpty(registration.getSurveyorName().getUserDetail().getEmail())){
				
				return true;//TODO PHionix
			}*/
		}
		return false;
	} 
	
	public void sendSMS(RegistrationExtn registration,String smsMsg){
		String mobileNumber=null;
		//mobileNumber=registration.getSurveyorName().getUserDetail().getMobileNumber();	 		
		if(null!=mobileNumber && StringUtils.isNotEmpty(mobileNumber)){
		
			/*try{
				//TODO PHionix
			//HTTPSMS.sendSMS(null, null,smsMsg,"91"+mobileNumber,BpaConstants.SMS_MOBILE_NUMBER);
			}catch(EGOVException e)  // Bypassing Exception..
			{
				LOGGER.debug("Error occured in sending SMS "+mobileNumber);
			}*/
		}
	}
	
	
	private InputStream generateLetterToPartyReplyPDF()
	{
			InputStream inputStream = null;
			ReportRequest reportRequest = null;
			Map<String, Object> reportParams = new HashMap<String, Object>();
			try{
			 if(!"".equals(getRequestID()) && getRequestID()!=null){
				registration=registerBpaExtnService.getRegistrationByPassingRequestNumber(getRequestID());
			}
			 else if(getRegistrationId()!=null){
			registration=letterToPartyExtnService.getRegistrationObjectbyId(registrationId);
		}
			Map<String,Object> reportData = constructLpReplyReportData(registration);
			
			List<LpChecklistExtn> checkListDtls=new ArrayList<LpChecklistExtn>(); 
			List<LpChecklistExtn> lpReplyChkListDet=new ArrayList<LpChecklistExtn>();
			if(null!=letterToPartyId && !"".equals(letterToPartyId)){
			 lpReplyChkListDet = registerBpaExtnService.getLetterToPartyCheckDtlsForType(letterToPartyId,
					BpaConstants.LPCHKLISTTYPE_REPLY);
			}else{
			 lpReplyChkListDet = registerBpaExtnService.getLetterToPartyCheckDtlsForType(letterParty.getId(),
						BpaConstants.LPCHKLISTTYPE_REPLY);
			}
			for(LpChecklistExtn lpChecklist:lpReplyChkListDet){
				
				if(null!=lpChecklist.getIsChecked() && lpChecklist.getLpChecklistType()!=null && lpChecklist.getLpChecklistType().equals(BpaConstants.LPCHKLISTTYPE_REPLY))
				{ 
					checkListDtls.add(lpChecklist);
				}
			}
			reportParams.put("lettertoPartyChecklistSet", checkListDtls); 
			
			
			reportRequest = new ReportRequest(BpaConstants.LPREPLYACKREPORTEXTN, reportData, reportParams);
			if(null!=reportRequest){
				ReportOutput reportOutput = reportService.createReport(reportRequest);				
				if (reportOutput != null && reportOutput.getReportOutputData() != null)
					inputStream = new ByteArrayInputStream(reportOutput.getReportOutputData());
				}	
			return inputStream;
		}catch (Exception e) { 
			throw new EGOVRuntimeException("Exception : " + e);
		}
	}

	protected void setDocumentNumberForLetterToParty() {
		if(getDocumentNum()!=null && !getDocumentNum().equals(""))
		{	
			letterParty.setDocumentid(getDocumentNum());
		}
		
		if(letterParty!=null && letterParty.getDocumentid()!=null)
		{	this.setDocumentNum(letterParty.getDocumentid());
		}
	}
	@SkipValidation
	@Action(value = "/lpReplyExtn-showCheckList", results = { @Result(name = "checklist",type = "dispatcher") })
	public String showCheckList() {
		
		if(lpChkListDet.size() == 0) {
			lpChkListDet.clear();
			if(letterParty!=null){
			letterParty.getLpChecklistSet().clear();
			}
			if(letterParty!=null && registration.getServiceType() != null) {
				lpChkListnewDet = registerBpaExtnService.getCheckedLPCheckListforReply(letterParty.getId());
				for(LpChecklistExtn lpList: lpChkListnewDet) {
					LpChecklistExtn newlpcheck=new LpChecklistExtn();
					newlpcheck.setCheckListDetails(lpList.getCheckListDetails());
					lpChkListDet.add(newlpcheck);
			}
				
			}
		}
		return "checklist";
	}
	@Action(value = "/lpReplyExtn-ackPrint", results = { @Result(name = "ackReport",type = "dispatcher") })
	public String ackPrint() {
		try{
			ReportRequest reportRequest = null;
			Map<String, Object> reportParams = new HashMap<String, Object>();
			 if(!"".equals(getRequestID()) && getRequestID()!=null){
				registration=registerBpaExtnService.getRegistrationByPassingRequestNumber(getRequestID());
			}
			 else if(getRegistrationId()!=null){
			registration=letterToPartyExtnService.getRegistrationObjectbyId(registrationId);
		}
			Map<String,Object> reportData = constructLpReplyReportData(registration);
			
			List<LpChecklistExtn> checkListDtls=new ArrayList<LpChecklistExtn>(); 
			List<LpChecklistExtn> lpReplyChkListDet=new ArrayList<LpChecklistExtn>();
			if(null!=letterToPartyId && !"".equals(letterToPartyId)){
			 lpReplyChkListDet = registerBpaExtnService.getLetterToPartyCheckDtlsForType(letterToPartyId,
					BpaConstants.LPCHKLISTTYPE_REPLY);
			}else{
			 lpReplyChkListDet = registerBpaExtnService.getLetterToPartyCheckDtlsForType(letterParty.getId(),
						BpaConstants.LPCHKLISTTYPE_REPLY);
			}
			for(LpChecklistExtn lpChecklist:lpReplyChkListDet){
				
				if(null!=lpChecklist.getIsChecked() && lpChecklist.getLpChecklistType()!=null && lpChecklist.getLpChecklistType().equals(BpaConstants.LPCHKLISTTYPE_REPLY))
				{ 
					checkListDtls.add(lpChecklist);
				}
			}
			reportParams.put("lettertoPartyChecklistSet", checkListDtls); 
			
			
			reportRequest = new ReportRequest(BpaConstants.LPREPLYACKREPORTEXTN, reportData, reportParams);
			reportRequest.setPrintDialogOnOpenReport(true);
			reportId = ReportViewerUtil.addReportToSession(reportService.createReport(reportRequest), getSession());

			return "ackReport";
		} catch (Exception e) { 
			throw new EGOVRuntimeException("Exception : " + e);
		}
		
	}
	
	private Map<String,Object> constructLpReplyReportData(RegistrationExtn registration) {
		Map<String,Object> reportData = new HashMap<String,Object>();
		LetterToPartyExtn lpReply = letterToPartyExtnService.getLatestLetterToPartyForRegObj(registration);
		String address = registration.getBpaOwnerAddress();
		
		String EMPTYSTRING = "";
		if(registration!=null && registration.getAdminboundaryid()!=null)
		{
		if(registration.getAdminboundaryid().getParent()!=null)
			reportData.put("zone", registration.getAdminboundaryid().getParent().getName());
		else
			reportData.put("zone", EMPTYSTRING);
		
		reportData.put("ward", registration.getAdminboundaryid().getName());
		
		}
		reportData.put("planSubmissionNum", registration.getPlanSubmissionNum());
		if( registration!=null && registration.getOwner()!=null)
			reportData.put("applicantName", registration.getOwner().getName());//TODO phionix
		else
			reportData.put("applicantName", EMPTYSTRING);
		reportData.put("address", address);
		reportData.put("ackNo", lpReply.getAcknowledgementNumber());
		reportData.put("replyDate", lpReply.getReplyDate());
		
		reportData.put("lpDate", lpReply.getLetterDate());
		reportData.put("lpNumber", lpReply.getLetterToPartyNumber());
		
		if(lpReply!=null && lpReply.getLetterToPartyReason()!=null)
			reportData.put("lpReason", lpReply.getLetterToPartyReason().getDescription());
		else
			reportData.put("lpReason",EMPTYSTRING);
		if(lpReply.getLetterToPartyReplyRemarks()!=null) {
			reportData.put("remarks", lpReply.getLetterToPartyReplyRemarks());
		}
		
		
		return reportData;
	}
	@Override
	public Object getModel() {
		return letterPartyReply;
	}
	
	/* Commented for time being */
	/*
	@SkipValidation
	public void createNotification() {
		final HashMap<String, String> fileDetails = new HashMap<String, String>();
	
		fileDetails.put("fileCategory", "INTER DEPARTMENT");
		fileDetails.put("filePriority", "MEDIUM");
		fileDetails.put("fileHeading", "Letter to Party REPLY is captured ");
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
		String fileSummary="";
		Date planSubmissionDate=registration.getPlanSubmissionDate();
		fileSummary = "Letter to Party REPLY is captured for Building Plan Application with Plan Submission Number "+ registration.getPlanSubmissionNum() +" dated on "+dateFormatter.format(planSubmissionDate);
		
		fileDetails.put("fileSummary", fileSummary);
		fileDetails.put("fileSource", "INTER DEPARTMENT");
		fileDetails.put("senderAddress", "");
		fileDetails.put("senderName", loginUser.getUserName());
		fileDetails.put("senderPhone", "");
		fileDetails.put("senderEmail", "");
		
		String user="";
	
		PersonalInformation emp = eisManager.getEmployeeforPosition(registration.getState().getOwner());
		if(emp!=null)
			user=emp.getUserMaster().getId().toString();
	
		fileManagementService.generateFileNotification(fileDetails,user);	
		
}
*/
	
	public LetterToPartyExtn getLetterParty() {
		return letterParty;
	}

	public void setLetterParty(LetterToPartyExtn letterParty) {
		this.letterParty = letterParty;
	}

	public Long getRegistrationId() {
		return registrationId;
	}

	public void setRegistrationId(Long registrationId) {
		this.registrationId = registrationId;
	}

	public LetterToPartyExtn getLetterPartyReply() {
		return letterPartyReply;
	}

	public void setLetterPartyReply(LetterToPartyExtn letterPartyReply) {
		this.letterPartyReply = letterPartyReply;
	}

	public LetterToPartyExtnService getLetterToPartyExtnService() {
		return letterToPartyExtnService;
	}

	public void setLetterToPartyExtnService(LetterToPartyExtnService letterToPartyService) {
		this.letterToPartyExtnService = letterToPartyService;
	}

	public String getExistLpReason() {
		return existLpReason;
	}

	public void setExistLpReason(String existLpReason) {
		this.existLpReason = existLpReason;
	}

	public String getExistLpRemarks() {
		return existLpRemarks;
	}

	public void setExistLpRemarks(String existLpRemarks) {
		this.existLpRemarks = existLpRemarks;
	}

	
	public InspectionExtnService getInspectionExtnService() {
		return inspectionExtnService;
	}

	public void setInspectionExtnService(InspectionExtnService inspectionService) {
		this.inspectionExtnService = inspectionService;
	}

	public BpaCommonExtnService getBpaCommonExtnService() {
		return bpaCommonExtnService;
	}

	public void setBpaCommonExtnService(BpaCommonExtnService bpaCommonService) {
		this.bpaCommonExtnService = bpaCommonService;
	}

	

	public RegistrationExtn getRegistration() {
		return registration;
	}

	public void setRegistration(RegistrationExtn registration) {
		this.registration = registration;
	}

	public Long getServiceTypeId() {
		return serviceTypeId;
	}

	public void setServiceTypeId(Long serviceTypeId) {
		this.serviceTypeId = serviceTypeId;
	}

	public List<LpChecklistExtn> getLpChkListDet() {
		return lpChkListDet;
	}
	public void setLpChkListDet(List<LpChecklistExtn> lpChkListDet) {
		this.lpChkListDet = lpChkListDet;
	}
	public BpaNumberGenerationExtnService getBpaNumberGenerationExtnService() {
		return bpaNumberGenerationExtnService;
	}

	public void setBpaNumberGenerationExtnService(BpaNumberGenerationExtnService bpaNumberGenerationService) {
		this.bpaNumberGenerationExtnService = bpaNumberGenerationService;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
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

	

	public User getLoginUser() {
		return loginUser;
	}
	public void setLoginUser(User loginUser) {
		this.loginUser = loginUser;
	}
	public String getExistLpNum() {
		return existLpNum;
	}

	public void setExistLpNum(String existLpNum) {
		this.existLpNum = existLpNum;
	}
	public RegisterBpaExtnService getRegisterBpaExtnService() {
		return registerBpaExtnService;
	}
	public void setRegisterBpaExtnService(RegisterBpaExtnService registerBpaService) {
		this.registerBpaExtnService = registerBpaService;
	}
	public List<LpChecklistExtn> getLpChkListnewDet() {
		return lpChkListnewDet;
	}
	public void setLpChkListnewDet(List<LpChecklistExtn> lpChkListnewDet) {
		this.lpChkListnewDet = lpChkListnewDet;
	}
	public Long getLetterToPartyId() {
		return letterToPartyId;
	}
	public void setLetterToPartyId(Long letterToPartyId) {
		this.letterToPartyId = letterToPartyId;
	}
	
	public String getRequestID() {
		return requestID;
	}
	public void setRequestID(String requestID) {
		this.requestID = requestID;
	}
	
	public String getDocumentNum() {
		return documentNum;
	}
	public void setDocumentNum(String documentNum) {
		this.documentNum = documentNum;
	}
}
