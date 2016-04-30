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
package org.egov.bpa.web.actions.extd.portal;

import net.sf.jasperreports.engine.JRException;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.bpa.constants.BpaConstants;
import org.egov.bpa.models.extd.AutoDcrExtn;
import org.egov.bpa.models.extd.BpaAddressExtn;
import org.egov.bpa.models.extd.InspectionExtn;
import org.egov.bpa.models.extd.LetterToPartyExtn;
import org.egov.bpa.models.extd.RegDocumentUpload;
import org.egov.bpa.models.extd.RegDocumentUploadDtls;
import org.egov.bpa.models.extd.RegistrationChecklistExtn;
import org.egov.bpa.models.extd.RegistrationExtn;
import org.egov.bpa.models.extd.ReportFeesDetailsExtn;
import org.egov.bpa.models.extd.SurveyNumber;
import org.egov.bpa.models.extd.masters.DocumentHistoryExtn;
import org.egov.bpa.models.extd.masters.VillageNameExtn;
import org.egov.bpa.services.extd.common.BpaCitizenPortalExtnService;
import org.egov.bpa.services.extd.common.BpaNumberGenerationExtnService;
import org.egov.bpa.services.extd.common.BpaSurvayorPortalExtnService;
import org.egov.bpa.services.extd.common.UtilsExtnService;
import org.egov.bpa.utils.ApplicationMode;
import org.egov.bpa.web.actions.extd.common.BpaExtnRuleBook;
import org.egov.bpa.web.actions.extd.register.RegisterBpaExtnAction;
import org.egov.collection.integration.models.BillReceiptInfo;
import org.egov.eis.entity.EmployeeView;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.pims.commons.Position;
import org.hibernate.Criteria;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Transactional(readOnly = true)
@Namespace("/portal")
@SuppressWarnings("serial")
@Results({ 
	@Result(name = "NOACCESS", type = "stream", location = "returnStream", params = { "contentType", "text/plain"}),
	@Result(name = CitizenRegisterBpaExtnAction.FEE_PAYMENT_PDF, type = "stream", location = "feePaymentReportPDF", params = { "contentType", "application/pdf"}),
	@Result(name = CitizenRegisterBpaExtnAction.DOWNLOAD, type ="stream", location = "fileInputStream",  params = { "contentType","${attachmentType}","contentDisposition", "filename=\"${attachmentName}\"" })

})
@ParentPackage("egov")

public class CitizenRegisterBpaExtnAction extends RegisterBpaExtnAction{
	private Logger LOGGER = Logger.getLogger(getClass());
	private String serviceType;
	private String serviceRegId;
	private String requestID;
	private String referenceNo; 
	private BpaCitizenPortalExtnService bpaCitizenPortalExtnService;
	private static String REGISTERED="Registered";
	private HashMap<String,String> citizenActionMap = new HashMap<String,String>();
	private HashMap<String,String> citizenActionPrintPermitMap = new HashMap<String,String>();
	private HashMap<String,String> citizenActionTabMap = new HashMap<String,String>();
//	private List<InspectionExtn> postponedInspectionDetails=new ArrayList<InspectionExtn>();	
	private String registrationHasBeenCancelled = "The Registration has been cancelled";
	private Boolean autoDcrFlag=Boolean.FALSE;
//	private List<SurveyorDetail> surveyordetailList=new ArrayList<SurveyorDetail>();
	private Long adminBoundaryId;
	private Boolean regInspected=Boolean.FALSE;
	private List<AppConfigValues> appConfigValuesSurveyordocUpload;
	private Boolean isDocUploadForSurveyorFlag=Boolean.FALSE;
	private String autoDcrNumberAutocomplete;
	private List<AutoDcrExtn>approvedAutoDcrList=new ArrayList<AutoDcrExtn>();
	private Long autoDcrId;
	UtilsExtnService utilsExtnService = new UtilsExtnService(); 
	private String regServiceTypeCode;
	private  String surveyorMobNo;
	public static final String DOWNLOAD = "download";
	private String attachmentName;
	private String attachmentType;
	private InputStream fileInputStream; 
	
	
	private BpaNumberGenerationExtnService bpaNumberGenerationExtnService;
	private BpaSurvayorPortalExtnService bpaSurvayorPortalExtnService;
	private Boolean documentTabEnabled=Boolean.FALSE;
	private Boolean autoDcrTabEnabled=Boolean.FALSE;
	private Boolean regFormTabEnabled=Boolean.FALSE;
	private Boolean inspectionTabEnabled=Boolean.FALSE;
	private Long regDocUpldDtlsId;
	/*public List<InspectionExtn> getPostponedInspectionDetails() {
		return postponedInspectionDetails;
	}

	public void setPostponedInspectionDetails(
			List<InspectionExtn> postponedInspectionDetails) {
		this.postponedInspectionDetails = postponedInspectionDetails;
	}*/

	 

	@Override
	public void validate(){
		LOGGER.info(" RegisterBpaExtnAction || validate || Start");
		List<String> temp=new ArrayList<String>();
		temp.add(null);
		documentHistoryDetailList.removeAll(temp);
		if(!isUserMappedToSurveyorRole() || ( isUserMappedToSurveyorRole() && workFlowAction!=null && workFlowAction.equals(BpaConstants.FWD_AEORAEE)))
		{
		if(null==registration.getPlanSubmissionDate() ||"".equals(registration.getPlanSubmissionDate())){
			addFieldError("registration.planSubmissionDate", getMessage("registration.planSubmissionDate.required"));
		}
		if(registration.getServiceType()==null ){
			addFieldError("registration.serviceType", getMessage("registration.serviceType.required"));
		}
		if(registration.getAppType()==null || registration.getAppType().equals("-1") ){
			addFieldError("registration.appType", getMessage("registration.appType.required"));
		}
		if(registration.getAppMode()==null || registration.getAppMode().equals("-1") ){
			addFieldError("registration.appMode", getMessage("registration.appMode.required"));
		}
		if(registration.getAdminboundaryid()==null){
			addFieldError("registration.adminboundaryid", getMessage("adminboundaryid.required"));
		}
		if(registration.getLocboundaryid()==null){
			addFieldError("registration.adminboundaryid", getMessage("locboundaryid.required"));
		}
		if(registration.getAdminboundaryid() != null && registration.getServiceType()!=null && (registration.getId()==null || registration.getState()==null)){
			Position userPosition=null;
			List<EmployeeView> employeeViewList=new ArrayList();
			String errorMsg=null;
			if(registration.getServiceType().getCode().equals(BpaConstants.APPLICATIONFORDEMOLITIONCODE)) {
				employeeViewList=getZonalLevelDesignatedUser(BpaConstants.ASSISNTENGINEERDESIGNATION);
				errorMsg="citizenregistration.assistantEngJurisdictionMap.required";
			}
			else if(registration.getServiceType().getCode().equals(BpaConstants.SUBDIVISIONOFLANDCODE) ||
 					registration.getServiceType().getCode().equals(BpaConstants.LAYOUTAPPPROVALCODE) || 
 					registration.getServiceType().getCode().equals(BpaConstants.RECLASSIFICATION)) {
				employeeViewList=getZonalLevelDesignatedUser(BpaConstants.ASSISTANTEXECUTIVEENGINEERDESIGNATION);
				errorMsg="citizenregistration.assistantExeEngJurisdictionMap.required";
			}
			else if(registration.getServiceType().getCode().equals(BpaConstants.CMDACODE)){
				employeeViewList=getZonalLevelDesignatedUser(BpaConstants.ASSISNTENGINEERDESIGNATION);
				if(employeeViewList == null && employeeViewList.size() == 0){
					employeeViewList=getZonalLevelDesignatedUser(BpaConstants.ASSISTANTEXECUTIVEENGINEERDESIGNATION);
				}
				errorMsg="citizenregistration.assistantEngandExeEngJurisdictionMap.required";
			}
			if (employeeViewList != null && employeeViewList.size() != 0) {
				userPosition= employeeViewList.get(0).getPosition();
			}
			if (registration.getAdminboundaryid() != null && userPosition==null) {
				if(errorMsg!=null)
					addFieldError(errorMsg, getMessage(errorMsg));
			}
			if(userPosition!=null){
				registration.setApproverPositionId(userPosition.getId());
			}
		}
		if(!isUserMappedToSurveyorRole() || (isUserMappedToSurveyorRole() && regFormTabEnabled!=null && regFormTabEnabled.equals(true))){
			
		if(registration.getOwner()==null){
			addFieldError("registration.owner", getMessage("owner.required"));
		}else {
			if(registration.getOwner().getName()==null || "".equals(registration.getOwner().getName())){
				addFieldError("registration.owner.name", getMessage("owner.name.required"));			
			}
			/*if(registration.getOwner().getFatherName()==null || "".equals(registration.getOwner().getFatherName())){
				addFieldError("registration.owner.fathername", getMessage("owner.fathername.required"));
		}*///TODO PHionix
			if(registration.getId()==null ||registration.getState()==null){
			if(getMobileNumber()==null || "".equals(getMobileNumber())){
				addFieldError("registration.owner.mobilePhone", getMessage("owner.mobileNumber.required"));
		}
			}
		if(applicantrAddress.getStreetAddress1()==null || "".equals(applicantrAddress.getStreetAddress1())){
			addFieldError("applicant.CommunicationAddress.required", getMessage("applicant.CommunicationAddress.required"));
		}
		}
		
		if(registration.getServiceType()!=null && !(registration.getServiceType().getCode().equals(BpaConstants.NEWBUILDINGONVACANTPLOTCODE)
				|| registration.getServiceType().getCode().equals(BpaConstants.DEMOLITIONRECONSTRUCTIONCODE)
				|| registration.getServiceType().getCode().equals(BpaConstants.ADDITIONALCONSTRUCTIONCODE))){
			
		if(siteAddress.getPlotNumber()==null || "".equals(siteAddress.getPlotNumber())){
			addFieldError("registration.siteAddress.PlotNumber", getMessage("siteAddress.PlotNumber.required"));
		}
		if(siteAddress.getPlotSurveyNumber()==null || "".equals(siteAddress.getPlotSurveyNumber())){
			addFieldError("registration.siteAddress.PlotSurveyNumber", getMessage("siteAddress.PlotSurveyNumber.required"));
		}
	
		if(siteAddress.getVillageName()==null){
			addFieldError("registration.siteAddress.village", getMessage("siteAddress.village.required"));
		}
		else 
		if(siteAddress.getVillageName().getId()==null){
			addFieldError("registration.siteAddress.village", getMessage("siteAddress.village.required"));
		}
		if(siteAddress.getPlotBlockNumber()==null || "".equals(siteAddress.getPlotBlockNumber())){
			addFieldError("registration.siteAddress.PlotBlockNumber", getMessage("siteAddress.PlotBlockNumber.required"));
		}
		if(siteAddress.getCityTown()==null || "".equals(siteAddress.getCityTown()))
		{
			addFieldError("registration.siteAddress.CityTown", getMessage("siteAddress.CityTown.required"));
		}
		if(getBoundaryStateId()==null ||  "".equals(getBoundaryStateId() ))
		{
			addFieldError("registration.siteAddress.State", getMessage("siteAddress.StateName.required"));
		}
		}
		
		if(siteAddress.getPincode()==null || "".equals(siteAddress.getPincode()))
		{
			addFieldError("registration.siteAddress.Pincode", getMessage("siteAddress.Pincode.required"));
		}
		
		}
		if(getSurveyor()==null || getSurveyor()==-1){
			
			addFieldError("registration.surveyorName", getMessage("surveyor.required"));
		}
		if(!isUserMappedToSurveyorRole() ||(registration.getServiceType()!=null && registration.getServiceType().getIsDocUploadForCitizen()==Boolean.TRUE))
		{
			Set<RegistrationChecklistExtn> userEnteredCheckListDtls = new HashSet<RegistrationChecklistExtn>(getChkListDet());
			Boolean atLeastOneDocSelected = false;

			for (RegistrationChecklistExtn userChkList : userEnteredCheckListDtls) {
				if (userChkList.getIsChecked() != null	&& userChkList.getIsChecked()){
					if(userChkList.getDocUpload()==null &&  "".equals(userChkList.getFileName())){
					atLeastOneDocSelected = true;
					break;
					}
				}
			}
			if (atLeastOneDocSelected)
				addFieldError("registration.DocumentUplaod",getMessage("registration.Document.required"));
			}
		/*else if(registration.getSurveyorName().getName()==null){
			addFieldError("registration.surveyorName", getMessage("surveyorNameaxt.required"));
		}*/
		/*if(registration.getServiceType().getCode().equals(BpaConstants.APPLICATIONFORDEMOLITIONCODE)
				||registration.getServiceType().getCode().equals(BpaConstants.LAYOUTAPPPROVALCODE)
				||registration.getServiceType().getCode().equals(BpaConstants.CMDACODE)
				||registration.getServiceType().getCode().equals(BpaConstants.SUBDIVISIONOFLANDCODE)
				||registration.getServiceType().getCode().equals(BpaConstants.RECLASSIFICATIONCODE)){*/
			if(getSurveyorCode()==null || "".equals(getSurveyorCode())){
				addFieldError("registration.surveyorCode", getMessage("surveyorCode.required"));
			}
			if(getSurveyorNameLocal()==null || "".equals(getSurveyorNameLocal()))
			{
				addFieldError("registration.surveyorName", getMessage("surveyorNameatx.required"));
			}
			if(getSurveyorMobNo()==null || "".equals(getSurveyorMobNo()))
			{
				addFieldError("registration.surveyorMobNo", getMessage("surveyorMobNo.required"));
			}
		
		//}
		if( autoDcrTabEnabled!=null &&  autoDcrTabEnabled.equals(true)){
		if(getAutoDcrFlag().equals(Boolean.TRUE)  && isUserMappedToSurveyorRole())
		{
			if(getAutoDcrNum()==null || "".equals(getAutoDcrNum()))
			{
				addFieldError("registration.autoDcrNum", getMessage("autoDCRnumber.required"));
			}
		}
		}
		
		if(registration.getRegnDetails()==null){
			addFieldError("registration.regnDetails", getMessage("regnDetails.required"));  
		}
		else {
			if(registration.getRegnDetails().getSitalAreasqft()==null){
			addFieldError("registration.regnDetails.sitalAreasqft", getMessage("regnDetails.sitalAreasqft.required"));
			}
			if(registration.getRegnDetails().getSitalAreasqmt()==null){
				addFieldError("registration.regnDetails.sitalAreasqmt", getMessage("regnDetails.sitalAreasqmt.required"));
			}
		}
		if(registration.getServiceType()!=null ){
			if(isUserMappedToSurveyorRole()){
			if(registration.getServiceType().getIsPtisNumberRequired()!=null && registration.getServiceType().getIsPtisNumberRequired().equals(Boolean.TRUE)){
				if(registration.getPropertyid()==null || "".equals(registration.getPropertyid()) ){
					addFieldError("registration.propertyid", getMessage("property.required"));
				}
			}
			}
			if(registration.getServiceType().getISAutoDcrNumberRequired()!=null && registration.getServiceType().getISAutoDcrNumberRequired().equals(Boolean.TRUE)){
				if(getAutoDcrNum()==null || "".equals(getAutoDcrNum()))
				{
					addFieldError("registration.autoDcrNum", getMessage("autoDCRnumber.required"));
				
				}
				
			}
			if(getAutoDcrNum()!=null && !"".equals(getAutoDcrNum()) && validateAutodcr()){
		    	
		    	addFieldError("AutoDcrexist", getMessage("registration.autodcr.exist"));
		    }
			if(registration.getServiceType().getIsCmdaType()!=null && registration.getServiceType().getIsCmdaType().equals(Boolean.TRUE)){
				if(registration.getCmdaNum()==null || "".equals(registration.getCmdaNum())){
					addFieldError("registration.cmdaNum", getMessage("cmdaProposalNumber.required"));

				}
				if(registration.getCmdaRefDate()==null || "".equals(registration.getCmdaRefDate())){
					addFieldError("registration.cmdaRefDate", getMessage("cmdaRefDate.required"));

				}
			}
			/*if(registration.getServiceType().getIsDocUploadForCitizen()!=null && registration.getServiceType().getIsDocUploadForCitizen().equals(Boolean.TRUE)){
				if(getDocumentNum()==null || "".equals(getDocumentNum())){
					addFieldError("registration.DocumentUplaod", getMessage("registration.Document.required"));	
				}
			}*/
			
			if(registration!=null && (workFlowAction!=null && workFlowAction.equalsIgnoreCase(BpaConstants.FWD_AEORAEE)))
			{
				List<InspectionExtn> inspectionList=inspectionExtnService.getSiteInspectionListforRegistrationObject(registration);
				if(inspectionList==null||inspectionList.size()==0){
					addActionError("Inspection is pending for this registration,Please complete the inspection and try again");
				}else{
					if(isUserMappedToSurveyorRole()){
						List<EmployeeView> employeeViewList=new ArrayList();
						Position userPosition=null;
						String layoutType=null;
						String designationName=null;
						String msg=null;
						if(inspectionList.get(0)!=null && inspectionList.get(0).getLayoutType()!=null){
							layoutType=inspectionList.get(0).getLayoutType().getName();
							if(layoutType!=null && layoutType.equalsIgnoreCase(BpaConstants.UNAPPROVED_LAYOUT_TYPE)){
								designationName=BpaConstants.ASSISTANTEXECUTIVEENGINEERDESIGNATION;
								msg="citizenregistration.assistantExeEngJurisdictionMap.required";
							}else if(layoutType!=null && !layoutType.equalsIgnoreCase(BpaConstants.UNAPPROVED_LAYOUT_TYPE)){
								designationName=BpaConstants.ASSISNTENGINEERDESIGNATION;
								msg="citizenregistration.assistantEngJurisdictionMap.required";
							}
						}
						employeeViewList=getZonalLevelDesignatedUser(designationName);
						if (employeeViewList != null && employeeViewList.size() != 0) {
							userPosition= employeeViewList.get(0).getPosition();
						}
						if (registration.getAdminboundaryid() != null && userPosition==null) {
							addFieldError(msg, getMessage(msg));
						}else if (userPosition!=null){
							registration.setApproverPositionId(userPosition.getId());
						}
					}
						
				}
			}
		}
		}
		// on save Validate autoDcr Number is Allready Exist validation
				if(isUserMappedToSurveyorRole() && workFlowAction!=null && workFlowAction.equals("save")&& getAutoDcrNum()!=null && !"".equals(getAutoDcrNum()) && validateAutodcr()){
			    	
			    	addFieldError("AutoDcrexist", getMessage("registration.autodcr.exist"));
					}
		if(isUserMappedToSurveyorRole())
		{
			prepareSurveyorActions();
			if(registration!=null){
				 List<InspectionExtn> inspectionList=inspectionExtnService.findAllBy("from InspectionExtn where registration=? and isInspected=? order by id desc,inspectionDate desc",registration,Boolean.TRUE);
				 postponedInspectionDetails=inspectionExtnService.getInspectionListforRegistrationObject(registration);
				 for(InspectionExtn inspectiondetails:postponedInspectionDetails){
						if(inspectiondetails.getParent()!=null)
							inspectiondetails.getParent().setPostponedDate(inspectiondetails.getInspectionDate());
				 }
				 if(inspectionList.size()>0){ 
						regInspected=Boolean.TRUE;
					}
				 }
			if(workFlowAction!=null && workFlowAction.equals(BpaConstants.FWD_AEORAEE)){
			if(isDocUploadForSurveyorFlag==Boolean.TRUE)
			{
					Set<RegistrationChecklistExtn> userEnteredCheckListDtls = new HashSet<RegistrationChecklistExtn>(getChkListDet());
					Boolean atLeastOneDocSelected = false;

					for (RegistrationChecklistExtn userChkList : userEnteredCheckListDtls) {
						if (userChkList.getIsChecked() != null	&& userChkList.getIsChecked()){
							if(userChkList.getDocUpload()==null &&  "".equals(userChkList.getFileName())){
							atLeastOneDocSelected = true;
							break;
							}
						}
					}
					if (atLeastOneDocSelected)
						addFieldError("registration.DocumentUplaod",getMessage("registration.Document.required"));
				}
			List<DocumentHistoryExtn> docHistory=bpaCommonExtnService.getRegnDocumentHistoryObjectBySurveyorOrOtherOfficials(registration);
			if(documentHistory==null && (docHistory==null ||docHistory.isEmpty()) ){	
				addFieldError("documentHistry.Required","Document History Sheet Details are mandatory for this Registration.Please complete the DocumentSheet Details and try again ");	
			}
			}
		}
		if(registration.getId()==null && citizenActionMap.isEmpty()){
 			citizenActionMap.put("Submit", "save");
		}
		LOGGER.info(" RegisterBpaExtnAction || validate || end");
	}

	
	/*
	 * Ajax call On Change of Street As of now its disable and populate Surveyor Dropdown Based on Zones...
	 */
	@SkipValidation
	@Action(value = "/citizenRegisterBpaExtn-getSurveyorDeatilList", results = { @Result(name = "surveyor",type = "dispatcher") })
	public String getSurveyorDeatilList() 
	{
		 Boundary boundary=null;
		Set<Boundary> wardboundry =bpaCommonExtnService.getBoundaryParentObject(adminBoundaryId);
		StringBuffer bndryString = new StringBuffer();
		for (Boundary bndry : wardboundry) {
			boundary = bndry;
			bndryString.append(bndry.getId());
		}
		
		Integer StrToInt=Integer.parseInt(bndryString.toString());
//		surveyordetailList=bpaCommonExtnService.getSurveyorForSelectedZone(StrToInt);
//		addDropdownData("surveyordetailList",surveyordetailList );
		
		return "surveyor";
	}
	@SkipValidation
	public void prepare() { 
		super.prepare();
		addDropdownData("applicationModeList",Arrays.asList(ApplicationMode.GENERAL));
		addDropdownData("surveyorNameList",new ArrayList<>());
				//bpaCommonExtnService.getAllActiveSurveyorNameList());
		addDropdownData("surveyNumberList",Arrays.asList(SurveyNumber.values())); 
		getadminBoundaryForRegistrationBystreetId();
		if(registration != null && registration.getServiceType() != null && registration.getServiceType().getCode()!=null)
			regServiceTypeCode=registration.getServiceType().getCode(); 	 
	}
/*  
 *  to set AdminBoundaryId to registration Object On save....
 */
	private void getadminBoundaryForRegistrationBystreetId() {
		if(registration!=null && registration.getLocboundaryid()!=null ){
		Set<Boundary> adminboundry =bpaCommonExtnService.getBoundaryParentObject(registration.getLocboundaryid().getId());
		for(Boundary bndry:adminboundry)
		{
			Boundary wardObj=bpaCommonExtnService.getBoundaryObjById((long)bndry.getId());
			registration.setAdminboundaryid(wardObj);
		}
		}
	}
	@SkipValidation
	@Action(value = "/citizenRegisterBpaExtn-newCitizenForm", results = { @Result(name = NEW,type = "dispatcher") })
	public String newCitizenForm() {	
		
		setApplicantrAddress(new BpaAddressExtn());
		BpaAddressExtn siteAddressObj= new BpaAddressExtn();
		siteAddressObj.setCityTown(BpaConstants.CITY_NAME);
		setSiteAddress(siteAddressObj);
		LOGGER.info(" serviceType " + getServiceType());
		LOGGER.info(" serviceRegId " + getServiceRegId());
		LOGGER.info(" requestID " + getRequestID());
		registration.setPlanSubmissionDate(new Date()); 
		
		registration.setAppMode(ApplicationMode.GENERAL.getCode());
		registration.setAppType("New");
		if(getRequestID()!=null)
			registration.setRequest_number(getRequestID());
		if(getServiceRegId()!=null)
			registration.setServiceRegistryId(Long.valueOf(getServiceRegId()));
		getMobileNumberForLoggedInUserId();
		prepareCitizenActions();
		//TODO: GET SERVICE TYPE AND VALIDATE AND SET THE VALUE TO FORM.
		return NEW; 
	}
	/*
	 * To autoComplete For AutoDcrNumber in Surveyor inbox...
	 */
	@SuppressWarnings("unchecked")
	@SkipValidation
	@Action(value = "/citizenRegisterBpaExtn-autoCompleteapprovedAutoDcrNUmber", results = { @Result(name = "autoDcr",type = "dispatcher") })
	public String autoCompleteapprovedAutoDcrNUmber() {
		approvedAutoDcrList=persistenceService.findAllBy("from AutoDcrDtlsExtn auto where auto.id is not null and not exists (select 1 from RegnAutoDcrDtlsExtn regnautodcr, RegistrationExtn reg" +
				" where auto.autoDcrNum=regnautodcr.autoDcrNum and reg.id=regnautodcr.registration.id and reg.egwStatus.moduletype=? and reg.egwStatus.code not in(?,?)) and auto.autoDcrNum like ?", BpaConstants.NEWBPAREGISTRATIONMODULE,BpaConstants.NEWBPACANCELLEDSTATUS,BpaConstants.REJECTORDISSUED,"%"+autoDcrNumberAutocomplete.toUpperCase()+"%");
			return "autoDcr"; 
		}
	
	/* 
	 * Move Application directly to AE/AEE from LS/Citizen.
	 * For Service Type 1,3,6:  Layout type is Unapproved Layout fwd application to AEE otherwise AE
	 * For Service Type 2 fwd Application to AE, Service Type 4,5,8 fwd Application to AEE
	 * For Service Type 7 randomly fwd to AE / AEE
	 */
	public Integer getApproverPositionId(){// todo phinx
		List<EmployeeView> employeeViewList=new ArrayList();
		List<Long>  designationId=new ArrayList<Long>();
		String designationName=null;
		String inspLayoutName=null;
		if(isUserMappedToSurveyorRole() && registration!=null && (workFlowAction!=null && workFlowAction.equalsIgnoreCase(BpaConstants.FWD_AEORAEE)))
		{
			List<InspectionExtn> inspectionList=inspectionExtnService.getSiteInspectionListforRegistrationObject(registration);
			if(inspectionList!=null && inspectionList.get(0)!=null && inspectionList.get(0).getLayoutType()!=null){
				inspLayoutName= inspectionList.get(0).getLayoutType().getName();
			}
			if(inspLayoutName!=null && inspLayoutName.equalsIgnoreCase(BpaConstants.UNAPPROVED_LAYOUT_TYPE)){
				designationName=BpaConstants.ASSISTANTEXECUTIVEENGINEERDESIGNATION;
			}else if(inspLayoutName!=null && !inspLayoutName.equalsIgnoreCase(BpaConstants.UNAPPROVED_LAYOUT_TYPE)){
				designationName=BpaConstants.ASSISNTENGINEERDESIGNATION;
			}
		}
		else{
			 if(registration.getServiceType().getCode().equals(BpaConstants.CMDACODE)){
				List<EmployeeView> empViewList_AE=new ArrayList();
				List<EmployeeView> empViewList_AEE=new ArrayList();
				empViewList_AE=getZonalLevelDesignatedUser(BpaConstants.ASSISNTENGINEERDESIGNATION);
				empViewList_AEE=getZonalLevelDesignatedUser(BpaConstants.ASSISTANTEXECUTIVEENGINEERDESIGNATION);
				if (empViewList_AE != null && empViewList_AE.size() != 0) {
					 employeeViewList.addAll(empViewList_AE);
					 designationId.add(empViewList_AE.get(0).getDepartment()!=null?(long)empViewList_AE.get(0).getDepartment().getId():null);
				}
				if (empViewList_AEE != null && empViewList_AEE.size() != 0) {
					 employeeViewList.addAll(empViewList_AEE);
					 designationId.add(empViewList_AEE.get(0).getDepartment()!=null?(long)empViewList_AEE.get(0).getDepartment().getId():null);
				}
				return autoSelectApproverPosition(employeeViewList,designationId);
			 }else if(registration.getServiceType().getCode().equals(BpaConstants.APPLICATIONFORDEMOLITIONCODE)) {
				 designationName=BpaConstants.ASSISNTENGINEERDESIGNATION;
			 }else if(registration.getServiceType().getCode().equals(BpaConstants.SUBDIVISIONOFLANDCODE) ||
 					registration.getServiceType().getCode().equals(BpaConstants.LAYOUTAPPPROVALCODE) || 
 					registration.getServiceType().getCode().equals(BpaConstants.RECLASSIFICATION)) {
				 designationName=BpaConstants.ASSISTANTEXECUTIVEENGINEERDESIGNATION;
			 }
		}
		employeeViewList=getZonalLevelDesignatedUser(designationName);
		if (employeeViewList != null && employeeViewList.size() != 0) {
			 designationId.add(employeeViewList.get(0).getDepartment()!=null?(long)employeeViewList.get(0).getDepartment().getId():null);
		}
		return autoSelectApproverPosition(employeeViewList,designationId);
	
	}
	
	@ValidationErrorPage(NEW)
	@Action(value = "/citizenRegisterBpaExtn-save", results = { @Result(name = SUCCESS,type = "dispatcher") })
	public String save() {
		Position position=null;
		LOGGER.info(" RegisterBpaExtnAction || Save || Start");
		List<Map<String, Object>> finalAttachmentList = new ArrayList<Map<String,Object>>();
	
		try{
			if(siteAddress!=null && siteAddress.getVillageName()!=null) {
			villgeobj=((VillageNameExtn)persistenceService.find("from VillageNameExtn where id=?" ,siteAddress.getVillageName().getId()));
			siteAddress.setVillageName(villgeobj);
			}
			if(registration.getOwner()!=null){
			registration.getOwner().setMobileNumber(mobileNumber);
			registration.getOwner().setEmailId(emailId);
			}
			/*if(getSurveyorCode()!=null && !"".equals(getSurveyorCode()))
			{
				surveyorObj=bpaCommonExtnService.getSurveyour(getSurveyor());
				registration.setSurveyorName(surveyorObj);
			}*/
				setCheckListForRegistration(); 
				setDocumentNumberForRegistration(); 	//TODO: CHECH DOCUMENT IS SELECTED IN ANY OF CHECKLIST.		
			
			//Assumption: Admin boundary not editable in modify screen. Save first time only.
			if(registration!=null && registration.getApproverId()==null) {
				Integer approverId=getApproverPositionId();
				if(approverId!=null){
						EmployeeView empView=((EmployeeView) persistenceService.find("from EmployeeView where position.id=?",approverId));
						position=(empView!=null?empView.getPosition():null);
						registration.setApproverId((long)approverId);
				}
			}
			
			Boolean callWorkflow=false;
			if(((workFlowAction!=null && !workFlowAction.equalsIgnoreCase(BpaConstants.SCRIPT_SAVE) && !workFlowAction.equalsIgnoreCase(BpaConstants.SCRIPT_REJECT)))){
				if(registration.getServiceType().getCode().equals(BpaConstants.CMDACODE))
				{
						callWorkflow=true; 
				}
				if( workFlowAction.equalsIgnoreCase(BpaConstants.FWD_AEORAEE)) {
					if(bpaCommonExtnService.isFeeCollectionPending(registration).doubleValue()== 0)
						callWorkflow=true;
					}
			}
			//Only To save in Surveyor 
			if(isUserMappedToSurveyorRole() && (documentHistory!=null && (documentHistory.getId()==null || (documentHistory.getId()!=null && workFlowAction!=null && !"".equals(workFlowAction) && !BpaConstants.FWD_AEORAEE.equalsIgnoreCase(workFlowAction))))){
				builddocumentHistoryExtraDeatils(documentHistory,documentHistoryDetailList);
			}
			if(workFlowAction!=null && !"".equals(workFlowAction) && ("submit".equalsIgnoreCase(workFlowAction)|| BpaConstants.FWD_AEORAEE.equalsIgnoreCase(workFlowAction)))
				setWorkFlowAction("forward");	
			
			if(registration.getPlanSubmissionNum()==null || "".equals(registration.getPlanSubmissionNum())){
				if( registration.getServiceType()!=null && registration.getServiceType().getCode().equals(BpaConstants.CMDACODE))
				{
					registration.setPlanSubmissionNum(bpaNumberGenerationExtnService.generatePlanSubmissionNumber(registration.getServiceType(), bpaCommonExtnService.getZoneNameFromAdminboundaryid(registration.getAdminboundaryid())));
					
				}else{
				  registration.setPlanSubmissionNum(bpaNumberGenerationExtnService.generatePreliminaryRequestNumber(registration.getServiceType()));
				  registration.setInitialPlanSubmissionNum(registration.getPlanSubmissionNum());
				}
			}
			
			registration=registerBpaExtnService.createBpa(registration,applicantrAddress,autoDcrNum,siteAddress,boundaryStateId,workFlowAction,approverComments,existingbuildingCategoryId,proposedbuildingCategoryId,callWorkflow,position);
			uploadDocuments(registration);
			//documentHistoryList = new ArrayList<DocumentHistoryExtn>(registration.getDocumenthistorySet());
			
			
		if( registration!=null && registration.getId()!=null) {	
		//Check if already service req registry present in the system
//			ServiceRequestRegistry serviceRegistry=	bpaCitizenPortalExtnService.getServiceRequestRegistryByEntityRefNo(registration.getPlanSubmissionNum());
//			if(serviceRegistry==null){
//				bpaCitizenPortalExtnService.createServiceRequestRegistry(registration.getServiceRegistryId(), registration.getRequest_number(),  registration.getPlanSubmissionNum(),registration.getId().toString(), registration.getEgwStatus().getCode(),getRegistrationDetails() );	
//			}Todo Phionix
		}
		
			setAdmissionFeeAmountForRegistration();			
			BigDecimal totalAmount = bpaCommonExtnService.isFeeCollectionPending(registration);			
			
			/* TODO: If workflow status is forward and first time saving this record then forwarded user is mandatory.
			 * Save forwarded user in temp variable along with registration.
			 * Dont call.. workflow.transition() on create. Create if admissionfee amount is zero. 
			 * Check any amount is pending for collection.
			 * 
			 *   add workflow action in local variable.. 
			 */
			
			/*
			 * Pay the admission fee at LS Level for the Service 01,03,06
			 */
			
		if(((workFlowAction!=null && !workFlowAction.equalsIgnoreCase(BpaConstants.SCRIPT_SAVE) && !workFlowAction.equalsIgnoreCase(BpaConstants.SCRIPT_REJECT))) && (admissionfeeAmount!=null && admissionfeeAmount >0)){
			
			
			//check whether service is 01,03,06 ?
			if (registration != null && registration.getId() != null
				&& registration.getServiceType() != null
				&& registration.getServiceType().getCode() != null
				&& (registration.getServiceType().getCode().equals(BpaConstants.NEWBUILDINGONVACANTPLOTCODE)
						|| registration.getServiceType().getCode().equals(BpaConstants.DEMOLITIONRECONSTRUCTIONCODE) || 
						registration.getServiceType().getCode().equals(BpaConstants.ADDITIONALCONSTRUCTIONCODE))) {	
				
				if(getUserRole()!=null && !getUserRole().equals(BpaConstants.PORTALUSERSURVEYORROLE))
				{
					/*if(bpaSurvayorPortalExtnService.getServiceRequestRegistryByEntityRefNo(registration.getPlanSubmissionNum())==null){
						registration.setEgwStatus(bpaCommonExtnService.getstatusbyCode(BpaConstants.APPLICATION_FWDED_TO_LS));
						createSurveyorEntry(registration);
					}*///Todo Phionix
				}
					if(totalAmount.doubleValue() > 0 && getUserRole()!=null && getUserRole().equals(BpaConstants.PORTALUSERSURVEYORROLE)
							&& workFlowAction.equalsIgnoreCase("forward"))
					{
						registration = registerBpaExtnService.getRegistrationById(registration.getId());
						bpaBillableExtn.setRegistration(registration);
						collectXML = URLEncoder.encode(bpaBillExtnServiceImpl.getBillXML(bpaBillableExtn));
						return "viewCollectFee"; 
					}
					
				}else if (registration != null && registration.getId() != null){  //for citizen role other than service type 01,03,06
						if(totalAmount.doubleValue() > 0 ) {
							registration = registerBpaExtnService.getRegistrationById(registration.getId());
							bpaBillableExtn.setRegistration(registration);
							collectXML = URLEncoder.encode(bpaBillExtnServiceImpl.getBillXML(bpaBillableExtn));
							return "viewCollectFee"; 
				      }
				}
			}
		/*
		 *  sending SMS and Mail on forward from citizen.. removing sending SMS and Mail on Citizen Save As per coc requirements . and sending only on forward ..
		 */
			smsAndEmailonSaveMethodCitizen(finalAttachmentList, totalAmount);				 			
			confirmationAlertMessages();
			
						
			}catch(EGOVRuntimeException ex)
			{
				LOGGER.error("Inside BPA registration create Method"+ex.getMessage());
				if(ex!=null && ex.getCause()!=null  && ex.getCause().getMessage()!=null &&  ex.getCause().getMessage().contains("DatabaseSequenceFirstTimeException"))
				{
					throw  new ValidationException(Arrays.asList(new ValidationError(ex.getMessage(),"Unable to save change. Please submit again. "))); 					
				}else if(ex!=null && ex.getMessage()!=null && ex.getMessage().contains("DatabaseSequenceFirstTimeException")){
					throw  new ValidationException(Arrays.asList(new ValidationError(ex.getMessage(),"Unable to save change. Please submit again. "))); 
				}else if(ex!=null && ex.getMessage()!=null){
					throw  new ValidationException(Arrays.asList(new ValidationError(ex.getMessage(),ex.getMessage())));
				}
			}		
		
		//if(!getMode().equalsIgnoreCase("surveyorSave"))
		setMode("noEdit");
		prepareSurveyorActions();
		LOGGER.info(" RegisterBpaExtnAction || Save || end");
	   	return SUCCESS;
		}
	
	// save both existing and new attachemtns
	@Transactional
	@Action(value = "/citizenRegisterBpaExtn-uploadDocuments")
		public void uploadDocuments(RegistrationExtn registration) {
			
			
			Set <RegistrationChecklistExtn> userEnteredCheckListDtls=new HashSet<RegistrationChecklistExtn>(getChkListDet());			
			RegDocumentUpload docUploadObj=null;
			 RegDocumentUploadDtls docUploadDtlObj=null;
			for(RegistrationChecklistExtn userChkList:userEnteredCheckListDtls){
				// if file selected from UI then save the data.		
				if(userChkList.getFileName()!=null && !("").equals(userChkList.getFileName()))
				{	
					String fileLocationName=bpaCommonExtnService.getAppconfigValueResult(BpaConstants.BPAMODULENAME,BpaConstants.BPA_FILE_UPLOAD_LOCATION,null);
					File mainDir=new File(fileLocationName);
					 User usr = (User) getPersistenceService().getSession().load(User.class, (EgovThreadLocals.getUserId()));
						
					for(RegistrationChecklistExtn checklistObj:registration.getRegistrationChecklistSet()){
						
						//TODO: CHECK THIS CONDITION AGAIN.
						if(checklistObj.getIsChecked()!=null && checklistObj.getCheckListDetails()!=null && userChkList.getCheckListDetails()!=null && 
								checklistObj.getCheckListDetails().getCode().equals(userChkList.getCheckListDetails().getCode())){
								if(!mainDir.exists()){
									Boolean mainDirCreate=mainDir.mkdirs();
									LOGGER.info("mainDirCreated--------->" +mainDirCreate);
								}
								checkAndAssignPermissionsForFile(mainDir);
								
								if(mainDir.exists() && mainDir.canRead() && mainDir.canWrite() && mainDir.canExecute()){
									
								if(checklistObj.getDocUpload()!=null){	//TODO: IF FILE DELETED FROM UI.. WE NEED TO HANDLE.
									docUploadObj=regDocUploadService.findById(checklistObj.getDocUpload(), false);
									if(docUploadObj!=null)
										checklistObj.setDocUpload(docUploadObj.getId());
									//	docUploadDtlObj =docUploadObj.getDocumentList().get(0); //Assuming, there will be only one attachment present for each row.
									//if (docUploadDtlObj == null) {
									//		docUploadDtlObj = new RegDocumentUploadDtls();
									//	}
									// docUploadDtlObj.setContentType(new MimetypesFileTypeMap().getContentType(userChkList.getUploadFile()));
									// docUploadDtlObj.setFileName(userChkList.getFileName());
									// docUploadDtlObj.setRegDocumentUpload(docUploadObj);
									// docUploadObj.addDocumentList(docUploadDtlObj);
								    // docUploadObj.setReferenceId(checklistObj.getId());
									 //docUploadObj.setCreatedBy(usr);
									// docUploadObj.setCreatedDate(new Date());
									//regDocUploadService.merge(docUploadObj);
								}else{
								
									File dir=new File(fileLocationName+"/"+BpaConstants.BPAMODULENAME+"/"+ registration.getPlanSubmissionNum().replace("/", ""));
									
									if(!dir.exists()){
										Boolean dirCreate=dir.mkdirs();
										LOGGER.info("dirCreated--------->" +dirCreate);
									}
									
									
									File file = new File(dir,userChkList.getFileName());
									LOGGER.info("mainDirCreated--------->" +userChkList.getFileName());
									try {
										 FileUtils.copyFile(userChkList.getUploadFile(), file);//todo: delete operation required ?
									} catch (FileNotFoundException e) {
										e.printStackTrace();
									} catch (IOException e) {
											e.printStackTrace();
									}
									 docUploadObj= new RegDocumentUpload();
									
									 docUploadObj.setObjectType("RegistrationCheckList");
									 docUploadDtlObj =new RegDocumentUploadDtls();
									// docUploadDtlObj.setContentType(new MimetypesFileTypeMap().getContentType(userChkList.getUploadFile()));
									 docUploadDtlObj.setContentType(userChkList.getContentType());
									 docUploadDtlObj.setFileName(userChkList.getFileName());
									 docUploadDtlObj.setRegDocumentUpload(docUploadObj);
									 docUploadObj.addDocumentList(docUploadDtlObj);
									 docUploadObj.setReferenceId(checklistObj.getId());
									// docUploadObj.setCreatedBy(usr);
									 docUploadObj.setCreatedDate(new Date());
									 regDocUploadService.persist(docUploadObj);
									}
							
								 checklistObj.setDocUpload(docUploadObj.getId());
						}
								
					}
						
					}
					
				  }else
				  {
					  for(RegistrationChecklistExtn checklistObj:registration.getRegistrationChecklistSet()){
							
							//If the record initially saved, now deleted from UI, then remove from db also. Currently we are not deleting from file location (TODO)
							if(userChkList!=null && userChkList.getId()!=null && checklistObj!=null && checklistObj.getId().equals(userChkList.getId()) && checklistObj.getIsChecked()!=null && checklistObj.getDocUpload()!=null ){
								docUploadObj=regDocUploadService.findById(checklistObj.getDocUpload(), false);
								if(docUploadObj!=null) 
									regDocUploadService.delete(docUploadObj);
							}
					  
				  }
				}
			
			
			}
		 }
		@SkipValidation
		@Action(value = "/citizenRegisterBpaExtn-downLoadFile", results = { @Result(name = DOWNLOAD,type = "dispatcher") })
		public String downLoadFile()   
		{
			RegDocumentUpload docUpld= regDocUploadService.findById(regDocUpldDtlsId, false);
			if(docUpld!=null && docUpld.getDocumentList().size()>0){ 
				String fileLocationName=bpaCommonExtnService.getAppconfigValueResult(BpaConstants.BPAMODULENAME,BpaConstants.BPA_FILE_UPLOAD_LOCATION,null);
				 registration=registerBpaExtnService.getRegistrationByPassingCheckListNumber(docUpld.getReferenceId());
					File mainDir = new File(fileLocationName+"/"+BpaConstants.BPAMODULENAME+ (registration!=null && registration.getInitialPlanSubmissionNum()!=null? "/"+registration.getInitialPlanSubmissionNum().replace("/", ""):(registration.getPlanSubmissionNum()!=null ? "/"+registration.getPlanSubmissionNum().replace("/", ""):"")));
					
			//	File mainDir=new File(fileLocationName);
				try {
					if (mainDir != null) {
						File fileObj=new File(mainDir,docUpld.getDocumentList().get(0).getFileName());
						if(fileObj!=null)
						fileInputStream = new ByteArrayInputStream(FileUtils.readFileToByteArray(fileObj));
						setAttachmentName(docUpld.getDocumentList().get(0).getFileName()); 
						setAttachmentType(docUpld.getDocumentList().get(0).getContentType());
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					addFieldError("document.doesnot.exist", getMessage("document.doesnot.exist"));
					return "error";
				}
				
				
			} 
			return DOWNLOAD;
		}
		
		
		public Long getRegDocUpldDtlsId() {
			return regDocUpldDtlsId;
		}


		public void setRegDocUpldDtlsId(Long regDocUpldDtlsId) {
			this.regDocUpldDtlsId = regDocUpldDtlsId;
		}


		private void checkAndAssignPermissionsForFile(File directory) {
			if(directory.exists()){
			if( !directory.canRead())			
				directory.setReadable(Boolean.TRUE);			
			
			if(!directory.canWrite())			
				directory.setWritable(Boolean.TRUE);
			
			if(!directory.canExecute())		
				directory.setExecutable(Boolean.TRUE);
			}
		}
		
/*	private void createSurveyorEntry(RegistrationExtn registration) {
		Surveyor surveyor=null;
		
		if(registration.getSurveyorName()!=null)
		{
		 surveyor=registration.getSurveyorName();
		}
		else{
		surveyor = utilsExtnService.getSurveyorForBpa(registration);
		}
		//LOGGER.debug("createSurveyorObject Chosen surveyor for BPA registartion " + registration.getPlanSubmissionNum()
		//		+ " is " + surveyor.getName());
		
		if (bpaCitizenPortalExtnService != null	&& bpaCitizenPortalExtnService.getPortalIntegrationService() != null
				) {
			
			bpaSurvayorPortalExtnService.createServiceRequestRegistry(
					bpaCitizenPortalExtnService.getPortalIntegrationService().getServiceRegistryById(registration.getServiceRegistryId()), registration,
					APPLICATION_FWDED_TO_LS, registration.getOwner().getName(), surveyor);
			
			bpaCitizenPortalExtnService.updateServiceRequestRegistry(registration);
//		}//TODO PHIONIX
	}
*/
	
	/*
	 * get MobileNumber and emailId from Logged In Citizen User Id...
	 */
	
	public void getMobileNumberForLoggedInUserId (){
		
	User user = bpaCommonExtnService.getUserbyId((EgovThreadLocals.getUserId()));
		if (user != null) {
			mobileNumber=user.getMobileNumber();
			emailId=user.getEmailId();
		}
	
	}
	
	public String getRegistrationDetails() {
		StringBuffer registrationDetails = new StringBuffer();
	
			if(registration!=null && registration.getOwner()!=null )
			{
				registrationDetails.append("Applicant Name : ");
				registrationDetails.append(((registration.getOwner().getName()==null)?"":""+registration.getOwner().getName()));
				registrationDetails.append(((registration.getOwner().getName()==null)?"":" "+registration.getOwner().getName()));	
				//registrationDetails.append(((registration.getOwner().getLastName()==null)?"":" "+registration.getOwner().getLastName()));
			}
	return (registrationDetails!=null?registrationDetails.toString():REGISTERED);
}
	
	@SuppressWarnings("unchecked")
	@SkipValidation
	@Action(value = "/citizenRegisterBpaExtn-modifyForm", results = { @Result(name = NEW,type = "dispatcher") })
	public String modifyForm()
	{	
		LOGGER.info(" CitizenRegisterBpaExtnAction || Modify || Start");
		if(registration!=null && registration.getRequest_number()==null)
		{
			addFieldError("invalid.service.request", getMessage("invalid.service.request"));
			return "error";
		}
		registration=registerBpaExtnService.getRegistrationByPassingRequestNumber( registration.getRequest_number());
		
		buildRegistrationObject();
		buildOrderDetails();
		getChkListDet();
		getMobileNumberForLoggedInUserId ();
		getAutoDcrflagForServiceType();
		getDocUploadMendatoryForSurveyor();
		registration.setAdditionalRule(BpaConstants.BPAAPPLICATIONREGISTRATION);
		setAdditionalRule(BpaConstants.BPAAPPLICATIONREGISTRATION);
		
		setMode("editForCitizen");
		
		setIsPlotAreaEditable("true");
		citizenActionMap.put("Submit", "save");
		
		return NEW;
	}
	public void getAutoDcrflagForServiceType()
	{
		if(getUserRole()!=null && getUserRole().equals(BpaConstants.PORTALUSERSURVEYORROLE))
		if (registration != null
				&& registration.getServiceType() != null
				&& registration.getServiceType().getCode() != null
				&& (registration.getServiceType().getCode().equals(BpaConstants.NEWBUILDINGONVACANTPLOTCODE)
						|| registration.getServiceType().getCode().equals(BpaConstants.DEMOLITIONRECONSTRUCTIONCODE) || 
						registration.getServiceType().getCode().equals(BpaConstants.ADDITIONALCONSTRUCTIONCODE))) {

			autoDcrFlag = Boolean.TRUE;
		}
	}
	
	
	
	
	public void getDocUploadMendatoryForSurveyor()
	{
		appConfigValuesSurveyordocUpload=bpaCommonExtnService.getAppConfigValue("BPA",BpaConstants.SURVEYORDOCUPLOAD);
		if(appConfigValuesSurveyordocUpload!=null && appConfigValuesSurveyordocUpload.size()>0)
		{
			
			String Docflag=appConfigValuesSurveyordocUpload.get(0).getValue();
			List<String>list=Arrays.asList(Docflag.split(","));
			if(!list.isEmpty() && list.contains(registration.getServiceType().getCode()))
			{
			isDocUploadForSurveyorFlag=Boolean.TRUE;
			}
		}
		
	}
	
	@SkipValidation
	@Action(value = "/citizenRegisterBpaExtn-viewForm", results = { @Result(name = NEW,type = "dispatcher") })
	public String viewForm() 
	{		
		
		LOGGER.info(" CitizenRegisterBpaExtnAction || viewForm || Start"); 
		if(getReferenceNo()==null) //TODO: CHECK IS IT BELONG TO SAME USER.
		{ 
			if(getRequestID()==null){
				addFieldError("invalid.service.request", getMessage("invalid.service.request"));
				return "error";
			}
		}
		if(getReferenceNo()!=null) {
			registration=registerBpaExtnService.getRegistrationByPassingReferenceNumber(getReferenceNo()); 
		}
		else if(getRequestID()!=null){
			registration=registerBpaExtnService.getRegistrationByPassingRequestNumber(getRequestID());
		}
		if(registration!=null){
		 List<InspectionExtn> inspectionList=inspectionExtnService.findAllBy("from InspectionExtn where registration=? and isInspected=? order by id desc,inspectionDate desc",registration,Boolean.TRUE);
			if(inspectionList.size()>0){ 
				regInspected=Boolean.TRUE;
			}
		 }
		
		/*
		 * Show Default Page as Site inspection details screen in LS Login.
		 */
		if(isUserMappedToSurveyorRole() && !regInspected)
			return "doSiteInspection";
		
		if(registration!=null){
				getMobileNumberForLoggedInUserId ();
				getAutoDcrflagForServiceType();
				getDocUploadMendatoryForSurveyor();
				buildRegistrationObject();		 
				setMode(BpaConstants.MODEVIEW);
				
				if(isUserMappedToSurveyorRole())
				{
					prepareSurveyorActions();
				}else{
					prepareCitizenActions();
				}
				if(registration.getServiceType() != null && registration.getServiceType().getCode()!=null)
					regServiceTypeCode=registration.getServiceType().getCode(); 
			//	surveyorMobNo=registration.getSurveyorName().getUserDetail().getMobileNumber();
		}else
		{
			addFieldError("invalid.service.request", getMessage("invalid.service.request"));
			return "error";
		}
		
		LOGGER.info(" CitizenRegisterBpaExtnAction || viewForm || end");
		return NEW;
	}

	public Boolean isUserMappedToSurveyorRole() {
		if(EgovThreadLocals.getUserId()!=null){
			User user = bpaCommonExtnService.getUserbyId((EgovThreadLocals.getUserId()));
			/*for(Role role : user.getRoles())
				if(role.getRoleName()!=null && role.getRoleName().equalsIgnoreCase(BpaConstants.PORTALUSERSURVEYORROLE))
				{
					return true;
				}*///TODO  Phionix
				
		}
		return false;
	}
	
	public void prepareSurveyorActions() {
	 if(registration!=null && registration.getId()!=null)
	 {
			 if(registration!=null & registration.getEgwStatus()!=null){ 
					 
				 if(registration.getEgwStatus().getCode().equalsIgnoreCase(BpaConstants.APPLICATIONFORWARDEDTOLS) ||
							 registration.getEgwStatus().getCode().equalsIgnoreCase(BpaConstants.SITEINSPECTIONSCHEDULEDBYLS) ||
							 registration.getEgwStatus().getCode().equalsIgnoreCase(BpaConstants.INSPECTEDBYLS) )
					{
					 citizenActionMap.put("Save", "save"); 
					  citizenActionMap.put(BpaConstants.FWD_AEORAEE, "save"); 
						//citizenActionMap.put(BpaConstants.DISCARDREGISTRATION, "discard");
					}
			 }			
	 }
	}
	
	public void prepareCitizenActions() {
		
		// IF RECORD  NOT YET SUBMITTED TO ASSISTENT, THEN CITIZEN CAN MODIFY/DELETE RECORD. 
 		if(registration.getId()!=null && registration.getState()==null && (!registration.getEgwStatus().getCode().equals("Cancelled") && 
 				registration.getEgwStatus().getCode().equals(BpaConstants.NEWREGISTRATION_CITIZENREGISTERED))){
			citizenActionMap.put(BpaConstants.DISCARDREGISTRATION, BpaExtnRuleBook.ACTIONMETHODMAP.get(BpaConstants.DISCARDREGISTRATION));
			citizenActionMap.put(BpaConstants.MODIFYREGISTRATION, BpaExtnRuleBook.ACTIONMETHODMAP.get(BpaConstants.MODIFYREGISTRATION));
			
		} 
 		
 		if(registration.getId()==null ){
 			citizenActionMap.put("Submit", "save");
		}
 		if( registration != null
				&& registration.getServiceType() != null
				&& registration.getServiceType().getCode() != null && registration.getEgwStatus()!=null && (registration.getEgwStatus().getCode().equals(BpaConstants.ORDERISSUEDTOAPPLICANT)|| registration.getEgwStatus().getCode().equals(BpaConstants.ORDERPREPARED) ||registration.getEgwStatus().getCode().equals(BpaConstants.APPLICANTSIGNUPDATED) ) )
 		{
 				citizenActionPrintPermitMap.put(BpaConstants.PRINTPPCERTIFICATEFORCITIZEN, BpaConstants.PRINTPPCERTIFICATEFORCITIZEN);
 		
		 		if((registration.getServiceType().getCode().equals(BpaConstants.NEWBUILDINGONVACANTPLOTCODE)||
		 					registration.getServiceType().getCode().equals(BpaConstants.DEMOLITIONRECONSTRUCTIONCODE) ||
		 					registration.getServiceType().getCode().equals(BpaConstants.ADDITIONALCONSTRUCTIONCODE)))
		 		{
				citizenActionPrintPermitMap.put(
						BpaConstants.PRINTBPCERTIFICATEFORCITIZEN,
						BpaConstants.PRINTBPCERTIFICATEFORCITIZEN);

			}	
		 		else if((registration.getServiceType().getCode().equals(BpaConstants.CMDACODE)))
		 		{
				citizenActionPrintPermitMap.clear();
				citizenActionPrintPermitMap.put(
						BpaConstants.PRINTBPCERTIFICATEFORCITIZEN,
						BpaConstants.PRINTBPCERTIFICATEFORCITIZEN);
			}
 		}
 		List<String> roleList = new ArrayList<String>();
		if(EgovThreadLocals.getUserId()!=null){
			roleList = bpaCommonExtnService.getRoleNamesByPassingUserId((EgovThreadLocals.getUserId()));				
		}
		showActions=  BpaExtnRuleBook.getInstance().getActionsByRoles(roleList,registration.getEgwStatus());
		
		HashMap<Integer,String> statusMap=bpaCommonExtnService.getStatusIdMap();
		
		if(showActions.contains(BpaConstants.VIEWADMISSIONFEERECEIPT) && !registration.getServiceType().getCode().equals(BpaConstants.CMDACODE)){
			if(registration.getId()!=null && registration.getState()!=null)
				citizenActionTabMap.put(BpaConstants.VIEWADMISSIONFEERECEIPT, BpaExtnRuleBook.ACTIONMETHODMAP.get(BpaConstants.VIEWADMISSIONFEERECEIPT));
		}
		if (registration != null && registration.getId() != null) {
			Criteria statusCrit=bpaCommonExtnService.createStatusChangeCriteria(registration);
			List<Integer> statusIdList=statusCrit.list();	
				
		for(Integer statusId:statusIdList){
			if(statusMap.get(statusId).equals(BpaConstants.LETTERTOPARTYSENT)){

				if(showActions.contains(BpaConstants.VIEWLETTERTOPARTY))
					citizenActionTabMap.put(BpaConstants.VIEWLETTERTOPARTY, BpaExtnRuleBook.ACTIONMETHODMAP.get(BpaConstants.VIEWLETTERTOPARTY));

			}/*else if(statusMap.get(statusId).equals(BpaConstants.INSPECTIONSCHEDULED)){
 
				if(showActions.contains(BpaConstants.VIEWINSPECTIONSCHEDULE))
					citizenActionTabMap.put(BpaConstants.VIEWINSPECTIONSCHEDULE, BpaExtnRuleBook.ACTIONMETHODMAP.get(BpaConstants.VIEWINSPECTIONSCHEDULE));

			}*/else if(statusMap.get(statusId).equals(BpaConstants.CHALLANNOTICESENT)){

				if(showActions.contains(BpaConstants.VIEWFEETOBEPAID))
					citizenActionTabMap.put(BpaConstants.VIEWFEETOBEPAID, BpaExtnRuleBook.ACTIONMETHODMAP.get(BpaConstants.VIEWFEETOBEPAID));
				
				// IF PAYMENT IS PENDING, THEN SHOW PAY FEE ONLINE BUTTON IN
				// UI.This option will be provided if the record is approved and
				// challan status in "challan notice sent to citizen"
				if (registration != null && registration.getId() != null) {
					if (bpaCommonExtnService.isFeeCollectionPending(registration).doubleValue() > 0) {
						citizenActionTabMap.put(BpaConstants.PAYFEEONLINE,
								BpaExtnRuleBook.ACTIONMETHODMAP
										.get(BpaConstants.PAYFEEONLINE));
					}
				}

			}
			else if(statusMap.get(statusId).equals(BpaConstants.CHALLANAMOUNTCOLLECTED)){

				if(showActions.contains(BpaConstants.VIEWADMISSIONFEERECEIPT)){
					if(citizenActionTabMap.get(BpaConstants.VIEWADMISSIONFEERECEIPT)==null)
						citizenActionTabMap.put(BpaConstants.VIEWADMISSIONFEERECEIPT, BpaExtnRuleBook.ACTIONMETHODMAP.get(BpaConstants.VIEWADMISSIONFEERECEIPT));
				}
			}
			
		}
		}
		 //citizenActionTabMap.put(BpaConstants.VIEWINSPECTIONSCHEDULE, BpaExtnRuleBook.ACTIONMETHODMAP.get(BpaConstants.VIEWINSPECTIONSCHEDULE));
		 //citizenActionTabMap.put(BpaConstants.VIEWLETTERTOPARTY, BpaExtnRuleBook.ACTIONMETHODMAP.get(BpaConstants.VIEWLETTERTOPARTY));
	
	}
	
	//@SkipValidation
	/*public String showInspectionSchedule()
	{
		HibernateUtil.getCurrentSession().setFlushMode(FlushMode.MANUAL);
		
			postponedInspectionDetails=inspectionExtnService.getInspectionListforRegistrationObject(registration);
		
			for(InspectionExtn inspectiondetails:postponedInspectionDetails){
				if(inspectiondetails.getParent()!=null)
					inspectiondetails.getParent().setPostponedDate(inspectiondetails.getInspectionDate());
			}
		
       return "inspectionschedule";
	}*/
	
	@SkipValidation
	@Action(value = "/citizenRegisterBpaExtn-onlineFeePayment", results = { @Result(name = "viewCollectFee",type = "dispatcher") })
	public String onlineFeePayment()
	{ 
		BigDecimal totalAmount = bpaCommonExtnService.isFeeCollectionPending(registration);			
		
		if (registration != null && registration.getId() != null){ 
			if(totalAmount.doubleValue() > 0 ) {
				registration = registerBpaExtnService.getRegistrationById(registration.getId());
				bpaBillableExtn.setRegistration(registration);
				collectXML = URLEncoder.encode(bpaBillExtnServiceImpl.getBillXML(bpaBillableExtn));
				return "viewCollectFee";
	      }
		}

		addActionMessage("There is no fee payment pending for the selected record. Invalid Request.");
		return "error";
	}
	
	@SkipValidation
	@Action(value = "/citizenRegisterBpaExtn-showLettertoParty", results = { @Result(name = "lettertoparty",type = "dispatcher") })
	public String showLettertoParty()
	{
		
			existingLetterToPartyDetails=registerBpaExtnService.getLetterToPartyForRegistrationObject(registration);
			if(registration!=null && registration.getServiceType().getCode().equals(BpaConstants.CMDACODE)){
			for(LetterToPartyExtn lepmainObj:existingLetterToPartyDetails){
			if(lepmainObj!=null && lepmainObj.getCmdaLetterToPartySet().size()>0){
			existingCmdaLetterToPartyDetails.addAll(lepmainObj.getCmdaLetterToPartySet());
			}
			}
			}
		
		return "lettertoparty";

	}
	@SkipValidation
	@Action(value = "/citizenRegisterBpaExtn-feePaymentPdf", results = { @Result(name = FEE_PAYMENT_PDF,type = "dispatcher") })
	public String feePaymentPdf()  throws JRException, Exception {
		feePaymentReportPDF = generateFeePaymentReportPDF();
		return FEE_PAYMENT_PDF;
	}
	
	@SkipValidation
	@Action(value = "/citizenRegisterBpaExtn-discard")
	public String discard(){
		if(registration.getId()!=null){
			registration = registerBpaExtnService.getRegistrationById(registration.getId());
			registration.setEgwStatus(bpaCommonExtnService.getstatusbyCode(BpaConstants.WF_CANCELLED,BpaConstants.NEWBPAREGISTRATIONMODULE));
			//bpaCitizenPortalExtnService.updateServiceRequestRegistry(registration);
			if(isUserMappedToSurveyorRole()){
			//bpaSurvayorPortalExtnService.updateServiceRequestRegistry(registration);///TODO  Phionix
			//	bpaCommonExtnService.buildEmail(registration,BpaConstants.EMAILONDISCARDRECORDONSURVEYOR,finalAttachmentList);
			//	bpaCommonExtnService.buildSMS(registration,BpaConstants.SMSONDISCARDRECORDONSURVEYOR);
			}
			addActionMessage(registrationHasBeenCancelled);
		}else{	
			addActionMessage("Invalid Request.");

		}
		return "error"; 
	} 
	private InputStream generateFeePaymentReportPDF()
	{
		if(registration!=null && registration.getId()!=null){
			registration = registerBpaExtnService.getRegistrationById(registration.getId());
	   List<ReportFeesDetailsExtn> reportFeeDetailsList=registerBpaExtnService.getSanctionedReportFeeDtls(registration);
		return bpaCommonExtnService.generateFeePaymentReportPDF(registration,reportFeeDetailsList);
		}
		return feePaymentReportPDF;
	}
	
	private Map<String,Object> createHeaderParams(RegistrationExtn registration, String type,BigDecimal finalAmount){
		Map<String,Object> reportParams = new HashMap<String,Object>();
		List<ReportFeesDetailsExtn> reportFeeDetailsList=registerBpaExtnService.getSanctionedReportFeeDtls(registration);
		if(type.equalsIgnoreCase(FEE_PAYMENT_PDF)){
			reportParams.put("planSubmissionNumber", registration.getPlanSubmissionNum());
			reportParams.put("dateofPlanSubmission", registration.getPlanSubmissionDate());
			reportParams.put("applicantName", registration.getOwner().getName());
			reportParams.put("applicantAddress", registration.getBpaOwnerAddress());
			reportParams.put("reportFeeList", reportFeeDetailsList);
		}
		return reportParams; 
	}
	@SkipValidation
	@Action(value = "/citizenRegisterBpaExtn-showCollectedFeeReceipts", results = { @Result(name = "receipts",type = "dispatcher") })
	public String showCollectedFeeReceipts()
	{
		if(registration!=null){
		registrationId=registration.getId();
		billReceiptInfoMap=registerBpaExtnService.getCollectedReceiptsByRegistrationId(registration);
			
			for(Map.Entry<String,BillReceiptInfo> map: billReceiptInfoMap.entrySet())
			{
				billRecptInfoList.add(map.getValue());
			}
		}
		return "receipts";
		
	}
	
	public List<EmployeeView> getZonalLevelDesignatedUser(String designationName) {

		return bpaPimsExtnFactory.getEmployeeInfoList(registration.getAdminboundaryid(),designationName,getBparoles());
		
	}
	public Position getZonalLevelExecEngineerDesignatedUser() {

		return bpaPimsExtnFactory.getExecEngineerDesignationEmployeeInfoList(registration.getAdminboundaryid());
	}
	/*public List<SurveyorDetail> getZonalLevelPortalSurveyorUser() {
	List<SurveyorDetail> survList=bpaCommonExtnService.getSurveyorForSelectedZone(registration.getAdminboundaryid().getId());
	return survList;
		
	}*////TODO  Phionix
	
	@SkipValidation
	public String showTermsandConditions(){ 
		return "termsAndCoditions"; 
	}


	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public String getServiceRegId() {
		return serviceRegId;
	}

	public void setServiceRegId(String serviceRegId) {
		this.serviceRegId = serviceRegId;
	}
	
	public String getRequestID() {
		return requestID;
	}

	public void setRequestID(String requestID) {
		this.requestID = requestID;
	}

	public BpaCitizenPortalExtnService getBpaCitizenPortalExtnService() {
		return bpaCitizenPortalExtnService;
	}

	public void setBpaCitizenPortalExtnService(
			BpaCitizenPortalExtnService bpaCitizenPortalService) {
		this.bpaCitizenPortalExtnService = bpaCitizenPortalService;
	}

	public String getReferenceNo() {
		return referenceNo;
	}

	public void setReferenceNo(String referenceNumber) {
		this.referenceNo = referenceNumber;
	}

	//TODO: Get approver position by zone and ward.
	//Based on service type populate checklist, autodcr, service type.
	

	public HashMap<String, String> getCitizenActionMap() {
		return citizenActionMap;
	}

	public void setCitizenActionMap(HashMap<String, String> citizenActionMap) {
		this.citizenActionMap = citizenActionMap;
	}

	public HashMap<String, String> getCitizenActionTabMap() {
		return citizenActionTabMap;
	}

	public void setCitizenActionTabMap(HashMap<String, String> citizenActionTabMap) {
		this.citizenActionTabMap = citizenActionTabMap;
	}

	public HashMap<String, String> getCitizenActionPrintPermitMap() {
		return citizenActionPrintPermitMap;
	}
	public void setCitizenActionPrintPermitMap(
			HashMap<String, String> citizenActionPrintPermitMap) {
		this.citizenActionPrintPermitMap = citizenActionPrintPermitMap;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	
	public Boolean getAutoDcrFlag() {
		return autoDcrFlag;
	}

	public void setAutoDcrFlag(Boolean autoDcrFlag) {
		this.autoDcrFlag = autoDcrFlag;
	}


/*//TODO PHionix
	public List<SurveyorDetail> getSurveyordetailList() {
		return surveyordetailList;
	}

	public void setSurveyordetailList(List<SurveyorDetail> surveyordetailList) {
		this.surveyordetailList = surveyordetailList;
	}*/


	public Long getAdminBoundaryId() {
		return adminBoundaryId;
	}


	public void setAdminBoundaryId(Long adminBoundaryId) {
		this.adminBoundaryId = adminBoundaryId;
	}


	public Boolean getRegInspected() {
		return regInspected;
	}

	public void setRegInspected(Boolean regInspected) {
		this.regInspected = regInspected;
	}
	
	public Boolean getIsDocUploadForSurveyorFlag() {
		return isDocUploadForSurveyorFlag;
	}
	public void setIsDocUploadForSurveyorFlag(Boolean isDocUploadForSurveyorFlag) {
		this.isDocUploadForSurveyorFlag = isDocUploadForSurveyorFlag;
	}
	public String getAutoDcrNumberAutocomplete() {
		return autoDcrNumberAutocomplete;
	}
	public void setAutoDcrNumberAutocomplete(String autoDcrNumberAutocomplete) {
		this.autoDcrNumberAutocomplete = autoDcrNumberAutocomplete;
	}
	public List<AutoDcrExtn> getApprovedAutoDcrList() {
		return approvedAutoDcrList;
	}
	public void setApprovedAutoDcrList(List<AutoDcrExtn> approvedAutoDcrList) {
		this.approvedAutoDcrList = approvedAutoDcrList;
	}
		public Long getAutoDcrId() {
		return autoDcrId;
	}
	public void setAutoDcrId(Long autoDcrId) {
		this.autoDcrId = autoDcrId;
	}
	public Boolean getDocumentTabEnabled() {
		return documentTabEnabled;
	}
	public void setDocumentTabEnabled(Boolean documentTabEnabled) {
		this.documentTabEnabled = documentTabEnabled;
	}
	public Boolean getAutoDcrTabEnabled() {
		return autoDcrTabEnabled;
	}
	public void setAutoDcrTabEnabled(Boolean autoDcrTabEnabled) {
		this.autoDcrTabEnabled = autoDcrTabEnabled;
	}
	public Boolean getRegFormTabEnabled() {
		return regFormTabEnabled;
	}
	public void setRegFormTabEnabled(Boolean regFormTabEnabled) {
		this.regFormTabEnabled = regFormTabEnabled;
	}
	
	public Boolean getInspectionTabEnabled() {
		return inspectionTabEnabled;
	}
	public void setInspectionTabEnabled(Boolean inspectionTabEnabled) {
		this.inspectionTabEnabled = inspectionTabEnabled;
	}
	
	
	public BpaNumberGenerationExtnService getBpaNumberGenerationExtnService() {
		return bpaNumberGenerationExtnService;
	}
	public void setBpaNumberGenerationExtnService(
			BpaNumberGenerationExtnService bpaNumberGenerationExtnService) {
		this.bpaNumberGenerationExtnService = bpaNumberGenerationExtnService;
	}
	public BpaSurvayorPortalExtnService getBpaSurvayorPortalExtnService() {
		return bpaSurvayorPortalExtnService;
	}
	public void setBpaSurvayorPortalExtnService(
			BpaSurvayorPortalExtnService bpaSurvayorPortalExtnService) {
		this.bpaSurvayorPortalExtnService = bpaSurvayorPortalExtnService;
	}
	public String getRegServiceTypeCode() {
		return regServiceTypeCode;
	}
	public void setRegServiceTypeCode(String regServiceTypeCode) {
		this.regServiceTypeCode = regServiceTypeCode;
	}
	public String getSurveyorMobNo() {
		return surveyorMobNo;
	}
	public void setSurveyorMobNo(String surveyorMobNo) {
		this.surveyorMobNo = surveyorMobNo;
	}
	public String getAttachmentName() {
		return attachmentName;
	}
	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}
	public InputStream getFileInputStream() {
		return fileInputStream;
	}
	public void setFileInputStream(InputStream fileInputStream) {
		this.fileInputStream = fileInputStream;
	}


	public String getAttachmentType() {
		return attachmentType;
	}


	public void setAttachmentType(String attachmentType) {
		this.attachmentType = attachmentType;
	}
	
}
