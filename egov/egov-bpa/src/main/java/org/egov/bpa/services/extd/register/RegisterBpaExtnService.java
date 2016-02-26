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
package org.egov.bpa.services.extd.register;

import static org.egov.bpa.constants.BpaConstants.OWNER_ADDRESS;
import static org.egov.bpa.constants.BpaConstants.PROPERTY_ADDRESS;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.egov.bpa.constants.BpaConstants;
import org.egov.bpa.models.extd.BpaAddressExtn;
import org.egov.bpa.models.extd.CMDALetterToPartyExtn;
import org.egov.bpa.models.extd.DocketFloorDetails;
import org.egov.bpa.models.extd.DocketViolations;
import org.egov.bpa.models.extd.InspectMeasurementDtlsExtn;
import org.egov.bpa.models.extd.InspectionDetailsExtn;
import org.egov.bpa.models.extd.InspectionExtn;
import org.egov.bpa.models.extd.LetterToPartyExtn;
import org.egov.bpa.models.extd.LpChecklistExtn;
import org.egov.bpa.models.extd.RegistrationChecklistExtn;
import org.egov.bpa.models.extd.RegistrationExtn;
import org.egov.bpa.models.extd.RegistrationFeeDetailExtn;
import org.egov.bpa.models.extd.RegistrationFeeExtn;
import org.egov.bpa.models.extd.RegnAutoDcrDtlsExtn;
import org.egov.bpa.models.extd.RegnAutoDcrExtn;
import org.egov.bpa.models.extd.RegnOfficialActionsExtn;
import org.egov.bpa.models.extd.RegnStatusDetailsExtn;
import org.egov.bpa.models.extd.RejectionChecklistExtn;
import org.egov.bpa.models.extd.ReportFeesDetailsExtn;
import org.egov.bpa.models.extd.masters.BpaFeeExtn;
import org.egov.bpa.models.extd.masters.BuildingCategoryExtn;
import org.egov.bpa.models.extd.masters.CheckListDetailsExtn;
import org.egov.bpa.models.extd.masters.DocumentHistoryExtn;
import org.egov.bpa.models.extd.masters.DocumentHistoryExtnDetails;
import org.egov.bpa.models.extd.masters.ServiceTypeExtn;
import org.egov.bpa.services.extd.autoDcr.AutoDcrExtnService;
import org.egov.bpa.services.extd.common.BpaCommonExtnService;
import org.egov.bpa.services.extd.common.BpaDmdCollExtnService;
import org.egov.bpa.services.extd.common.BpaNumberGenerationExtnService;
import org.egov.bpa.services.extd.common.BpaPimsInternalExtnServiceFactory;
import org.egov.bpa.services.extd.common.FeeExtnService;
import org.egov.bpa.services.extd.common.RegnStatusDetailExtnService;
import org.egov.bpa.services.extd.inspection.InspectionExtnService;
import org.egov.bpa.utils.extd.BpaFeeExtnComparator;
import org.egov.collection.integration.models.BillReceiptInfo;
import org.egov.commons.EgwStatus;
import org.egov.demand.model.EgDemandDetails;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.persistence.entity.enums.AddressType;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.reporting.viewer.ReportViewerUtil;
import org.egov.infra.utils.EgovThreadLocals;
/*import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.erpcollection.integration.models.BillReceiptInfo;*/
import org.egov.infstr.ValidationException;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.StringUtils;
/*import org.egov.infstr.utils.UtilityMethods;phionix todo*/
import org.egov.infstr.workflow.WorkFlowMatrix;
import org.egov.pims.commons.DeptDesig;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
/*import org.egov.pims.commons.service.EisCommonsManager;
import org.egov.pims.service.EisManager;*/
import org.egov.pims.model.PersonalInformation;
import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly=true)
public class RegisterBpaExtnService extends PersistenceService<RegistrationExtn, Long> {

	private Logger LOGGER = Logger.getLogger(getClass());
	private PersistenceService persistenceService;
	private static final String STATUSCODE    		    =    "statusObj.code";
	private static final String STATUSMODULE            =    "statusObj.moduletype";
	private BpaNumberGenerationExtnService bpaNumberGenerationExtnService;
	private BpaDmdCollExtnService bpaDmdCollExtnService;
	private BpaPimsInternalExtnServiceFactory bpaPimsExtnFactory;
	//private WorkflowService <RegistrationExtn> registrationWorkflowExtnService;
	private Long approverId;
	private String additionalRule;
	private String additionalState;
	private EgwStatus oldStatus;
	private BpaCommonExtnService bpaCommonExtnService;
	private AutoDcrExtnService autoDcrExtnService;
	//private EisCommonsManager eisCommonsManager;
	private SimpleDateFormat dateFormatter=new SimpleDateFormat("dd/MM/yyyy");
	
	/*public EisCommonsManager getEisCommonsManager() {
		return eisCommonsManager;
	}
	public void setEisCommonsManager(EisCommonsManager eisCommonsManager) {
		this.eisCommonsManager = eisCommonsManager;
	}*/
	protected PersistenceService<RegnOfficialActionsExtn, Long> regnOfficialActionsExtnService;
	private RegnStatusDetailExtnService regnStatusDetExtnService;
	//private EisManager eisMgr;
	private FeeExtnService feeExtnService;
	private ReportService reportService;
	private InspectionExtnService inspectionExtnService;
	
	public InspectionExtnService getInspectionExtnService() {
		return inspectionExtnService;
	}
	public void setInspectionExtnService(InspectionExtnService inspectionExtnService) {
		this.inspectionExtnService = inspectionExtnService;
	}
	/**
	* Code Reviewed By Gauthami.
	*/
	@Transactional
	public RegistrationExtn closeRegistration(RegistrationExtn registration,
			String workFlowAction, String approverComments) {
		
		  bpaCommonExtnService.createStatusChange(registration,registration.getEgwStatus());
			
			if(registration.getEgwStatus().getCode().equals(BpaConstants.ORDERISSUEDTOAPPLICANT))
				{
				
				 String comments= (approverComments==null || "".equals(approverComments.trim()))?"":approverComments;
				//Pionix TODO:commenting as of now der is no WorkflowService
				/*bpaCommonExtnService.workFlowTransition(registration, "close registration",
						comments);*/
				}
			else  if(workFlowAction!=null && !"".equals(workFlowAction) && !BpaConstants.SCRIPT_SAVE.equalsIgnoreCase(workFlowAction)){
				String comments= (approverComments==null || "".equals(approverComments.trim()))?"":approverComments;
			
				/*bpaCommonExtnService.workFlowTransition(registration, workFlowAction,
						comments);
				*/
				//Pionix TODO:commenting as of now der is no WorkflowService
		   }
			
		  registration=persist(registration);
		  
	 	return registration;
		
		
	}
	@Transactional
	public RegistrationExtn createBpa( RegistrationExtn registration,BpaAddressExtn applicantrAddress,String autoDcr,BpaAddressExtn siteAddress,String boundaryStateId,String workFlowAction,String approverComments, Long existingbuildingCategoryId, Long proposedbuildingCategoryId,Boolean callWorkFlow, Position position)
	{
		oldStatus=registration.getEgwStatus();
		/**
		 * Demand
		 */
		
		//TODO: cos of Demand Framework
		
		/*if( registration.getServiceType()!=null &&  registration.getServiceType().getId()!=null &&  registration.getRegnDetails()!=null && registration.getRegnDetails().getSitalAreasqmt()!=null){
			
			EgDemand egDemand = null;			
			if (registration.getEgDemand() == null) {
				egDemand =bpaDmdCollExtnService.createDemand(registration.getServiceType().getId(), 
						registration.getRegnDetails().getSitalAreasqmt(),  BpaConstants.ADMISSIONFEE);
			}else if(registration.getEgwStatus()!=null && registration.getEgwStatus().getCode().equalsIgnoreCase(BpaConstants.CITIZENAPPLICATIONREGISTERED)){
			 
			    //{//TODO: NO NEED TO CALL THIS LOOP FOR EACH UPDATE.
				// required if, service type changed each time from UI or if area changed and fee changed based on area.
				//if(registration.getEgwStatus()!=null && registration.getEgwStatus().equals(BpaConstants.STATUS_APPLICATIONREGISTERED)){
				egDemand =bpaDmdCollExtnService.updateDemand(registration.getServiceType().getId(), 
						registration.getRegnDetails().getSitalAreasqmt(),  BpaConstants.ADMISSIONFEE, registration.getEgDemand());
				//}
			}			
			 if(egDemand != null) {
				 registration.setEgDemand(egDemand);
			 }
		}*/
		//adding additional rule for letter to party
	/*	if(registration.getEgwStatus()!=null && registration.getEgwStatus().getCode().equalsIgnoreCase(BpaConstants.CREATEDLETTERTOPARTY)){
			   registration.setAdditionalRule(BpaConstants.LETTERTOPARTYDETAILS); 
			   } 
		else
			   {
				   registration.setAdditionalRule(BpaConstants.BPAAPPLICATIONREGISTRATION);
			   }*/
		additionalRule=registration.getAdditionalRule();
		if(registration!=null && registration.getId()==null){
			EgwStatus egwStatus =bpaCommonExtnService.getstatusbyCode(BpaConstants.CITIZENAPPLICATIONREGISTERED);	  
				registration.setEgwStatus(egwStatus);
		} 
	  
	   registration.getRegnDetails().setRegistration(registration);	   
	   
	   if(existingbuildingCategoryId!=null && existingbuildingCategoryId!=-1){
	  	 BuildingCategoryExtn existingbuildingCategory = getBuildingCategorybyId(existingbuildingCategoryId);
	  	 registration.getRegnDetails().setExistingBldgCatg(existingbuildingCategory);
	   }
	   if(proposedbuildingCategoryId!=null && proposedbuildingCategoryId!=-1){
		   BuildingCategoryExtn proposedbuildingCategory = getBuildingCategorybyId(proposedbuildingCategoryId);
		   registration.getRegnDetails().setProposedBldgCatg(proposedbuildingCategory);
	   }

	   registration.addBpaAddress(createApplicantAddress(registration,applicantrAddress));
	   registration.addBpaAddress(createSitetAddress(registration,siteAddress,boundaryStateId));
	  
	   if(registration.getPlanSubmissionNum()==null || "".equals(registration.getPlanSubmissionNum())){
			String ppanumber=bpaNumberGenerationExtnService.generatePlanSubmissionNumber(registration.getServiceType(), bpaCommonExtnService.getZoneNameFromAdminboundaryid(registration.getAdminboundaryid()));
			if(ppanumber!=null)
				registration.setPlanSubmissionNum(ppanumber);
			
		}
	   if(registration.getApproverPositionId()!=null && registration.getApproverPositionId()!=-1)
	   {
		  approverId= registration.getApproverPositionId();
	   }
	
	   additionalRule=registration.getAdditionalRule();
		  
	   additionalState=registration.getAdditionalState();
	   if(additionalRule==null){
		   if(additionalState!=null&&(additionalState.equalsIgnoreCase(BpaConstants.ADDITONALRULEREJECTBPA)||additionalState.equalsIgnoreCase(BpaConstants.ADDITONALRULEREJECTBPA)))
			   additionalRule=BpaConstants.ADDITONALRULEREJECTBPA;
	   } 
	   registration.setAdditionalRule(additionalRule);
	   /*
	    *  System should automatically redirects the application to the Letter To Party Creator Inbox once After the  Letter To Party approval.. wil get positionByLpCreated user and setting approverId for workflow purpose..
	    */
	  		   
			   if(registration.getId()!=null && workFlowAction!=null &&
					   workFlowAction.equalsIgnoreCase(BpaConstants.SMSEMAILAPPROVELETTERTOPARTY))
			   {
				   LetterToPartyExtn letterToParty=(LetterToPartyExtn) getLetterToPartyForRegistrationObjectWhereSentDateisNull(registration);
				   if(letterToParty!=null && letterToParty.getCreatedBy()!=null){
						  Position pos = (Position) bpaCommonExtnService.getWfStatesByTypeandValue();
						  if(pos !=null )
						  {
							  approverId=pos.getId();
						  }
						   letterToParty.setSentDate(new Date());
						   if(letterToParty.getSentDate()!=null && registration.getEgwStatus().getCode().equals(BpaConstants.CREATEDLETTERTOPARTY)){
					  		  letterToParty.getRegistration().setEgwStatus(bpaCommonExtnService.getstatusbyCode(BpaConstants.LETTERTOPARTYSENT));
						   }
				 }
		   }
	   if(approverId!=null)
	   registration.setApproverPositionId(approverId);
	   registration.setAdditionalState(additionalState);
	  // Set<AutoDcr> autoDcrSet = new HashSet<AutoDcr>();
	   RegnAutoDcrExtn autoDcrObj=null;
	   if(autoDcr!=null && !"".equals(autoDcr) )
	   {
		   if( registration!=null && registration.getId()!=null)
			   autoDcrObj=autoDcrExtnService.getAutoDcrByRegId(registration.getId());		   
		   
		   if(autoDcrObj!=null)
			   autoDcrObj.setAutoDcrNum(autoDcr);
		   else
		  	   createAutoDcrObject(registration, autoDcr);	   
	
	   }
	   
	   if(workFlowAction!=null && !"".equals(workFlowAction) &&  (("Cancel Unconsidered".equalsIgnoreCase(workFlowAction) )
			   || ("Cancel LetterToParty".equalsIgnoreCase(workFlowAction))  )){ //("Cancel Unconsidered".equalsIgnoreCase(workFlowAction) )
		  registration.setOldStatus(bpaCommonExtnService.getoldStatus(registration));
	   }
	  /*
		 * If record saved first time and admission fees amount is greater than zero. then no need to forward this record to second level user,
		 * We need to collect admission fee first. Resetting forward user id in temp field and redirecting to collection screen.
		 */
	//	if(registration.getState()== null){
	  
			if(workFlowAction!=null && !"".equals(workFlowAction) && !(BpaConstants.SCRIPT_SAVE.equalsIgnoreCase(workFlowAction) || BpaConstants.SCRIPT_REJECT.equalsIgnoreCase(workFlowAction))){
				
				//IF Amount collection is pending then keep the record in draft.
			if (bpaCommonExtnService.isFeeCollectionPending(registration).doubleValue() > 0
					&& registration != null
					&& ((registration.getState() == null) || (/*registration.getEgwStatus() != null && registration.getEgwStatus().getDescription().equalsIgnoreCase(BpaConstants.STATUS_APPLICATIONREGISTERED)   && */
							registration.getState()!=null && registration.getState().getValue().equals(BpaConstants.WF_NEW_STATE))))
				workFlowAction=BpaConstants.SCRIPT_SAVE;            
			}
		//}
     	if(callWorkFlow)		
     		registration=createWorkflow(registration,workFlowAction,approverComments,position);
			 
			/* Set Sent date for Lp on Approval of LetterToparty and change the status To 
			* LETTERTOPARTYSENT
			*/

     	if(registration.getId()!=null && workFlowAction!=null &&workFlowAction.equalsIgnoreCase(BpaConstants.SMSEMAILAPPROVELETTERTOPARTY) && registration.getEgwStatus().getCode().equals(BpaConstants.CREATEDLETTERTOPARTY))
		   {
     		 	registration.setEgwStatus(bpaCommonExtnService.getstatusbyCode(BpaConstants.LETTERTOPARTYSENT));
				registration.setAdditionalRule(BpaConstants.BPAAPPLICATIONREGISTRATION);
		   }
       bpaCommonExtnService.createStatusChange(registration,oldStatus);
	  
	   if(registration.getSecurityKey()==null || "".equals(registration.getSecurityKey()))
		 //  registration.setSecurityKey(Utilities.getRandomString());//From Egi UtilityMethods changed   as to remove compile error
		   registration.setSecurityKey("");
	   if(registration.getId()==null)
		   registration=persist(registration);
	   else {
		   registration=merge(registration);
	   }
	   
		return registration;
	}
	
	
	
	private BuildingCategoryExtn getBuildingCategorybyId(Long buildingCategoryId) {
		BuildingCategoryExtn buildingCategory= (BuildingCategoryExtn) persistenceService.find("from BuildingCategoryExtn BC where BC.isActive=true and id=?",buildingCategoryId);
		return buildingCategory;
	}
	
	/*public BigDecimal isFeeCollectionPending(Registration registration ) {
		
		BigDecimal totalAmount=BigDecimal.ZERO;
		
		if (registration != null && registration.getEgDemand() != null) {
			Set<EgDemandDetails> demandDetailSet = registration
					.getEgDemand().getEgDemandDetails();

			for (EgDemandDetails demanddtl : demandDetailSet) {
				totalAmount = totalAmount.add(demanddtl.getAmount()
						.subtract(demanddtl.getAmtCollected()));
			}
		}
		return totalAmount;
	}*/
	
	@SuppressWarnings("unchecked")
	public boolean checkAutodcr(String autonum,Long CheckId) 
	{
		return autoDcrExtnService.checkAutodcrAlreadyExist(autonum, CheckId);
	}
	/*
	 * Checking Existing BuildingApproval Number is Unique in system?
	 */
	public boolean checkExistingBuildingApprovalNumberIsUnique(String existBA, Long regId)
	 {
			
			if (existBA != null && !"".equals(existBA)) {
				Criteria appCriteria = persistenceService.getSession()
						.createCriteria(RegistrationExtn.class, "register");
				appCriteria.add(Restrictions.eq("register.existingBANum", existBA));
				if(regId!=null)
					appCriteria.add(Restrictions.ne("register.id", regId));
				List<RegistrationExtn> regPPAnumList = appCriteria.list();
				if (regPPAnumList.size() > 0)
					return true;
			}
			return false;
		}
	/*
	 * Checking Existing PPA Number is Unique in system?
	 */
	public boolean checkExistingPpaNumberIsUnique(String existPPA, Long regId)
	 {
			
			if (existPPA != null && !"".equals(existPPA)) {
				Criteria appCriteria = persistenceService.getSession().createCriteria(RegistrationExtn.class, "register");
				appCriteria.add(Restrictions.eq("register.existingPPANum", existPPA));
				if(regId!=null)
					appCriteria.add(Restrictions.ne("register.id", regId));
				List<RegistrationExtn> regPPAnumList = appCriteria.list();
				if (regPPAnumList.size() > 0)
					return true;
			}
			return false;
		}
	/*
	 * CHECKING PPA NUMBER IS VALID NUMBER ? WITH STATUS OF PPA NUMBER..
	 */
	@SuppressWarnings("unchecked")
	public boolean checkPPANumberIsValid(String existPPA, Long regId)
	 {
			
			if (existPPA != null && !"".equals(existPPA)) {
				Criteria ppanumberCriteria = buildRegistrationCriteria(existPPA,regId);
				ppanumberCriteria.add(Restrictions.eq("register.planSubmissionNum", existPPA));
				List<RegistrationExtn> regPPAnumList = ppanumberCriteria.list();
				LOGGER.info(regPPAnumList.size());
				if (regPPAnumList.size()> 0){
					return true;
				}
			}
				return false;
			
		}
		private Criteria buildRegistrationCriteria(String existBA, Long regId) {
			Criteria appCriteria = persistenceService.getSession()
					.createCriteria(RegistrationExtn.class, "register")
					.createAlias("register.egwStatus", "statusObj");
				
			appCriteria.add(Restrictions.eq(STATUSMODULE, "BPAREGISTRATION"));
			appCriteria.add(Restrictions.disjunction().add(Restrictions.eq(STATUSCODE, BpaConstants.CANCELLED))
					.add(Restrictions.eq(STATUSCODE,BpaConstants.REJECTORDERISSUED))
					.add(Restrictions.eq(STATUSCODE,BpaConstants.ORDERISSUEDTOAPPLICANT)));
			if(regId!=null)
					appCriteria.add(Restrictions.ne("register.id", regId));
			return appCriteria;
		}
		/*
		 * Checking BuildingApproval NUMBER IS VALID NUMBER ? AND CHECKING STATUS OF PPA NUMBER..
		 */
		@SuppressWarnings("unchecked")
		public boolean checkBuildingApprovalNumberIsValid(String existBA, Long regId)
		 {
				if (existBA != null && !"".equals(existBA)) {
					Criteria banumCriteria = buildRegistrationCriteria(existBA, regId);
					banumCriteria.add(Restrictions.eq("register.baNum", existBA));
					List<RegistrationExtn> regBAnumList = banumCriteria.list();
					if (regBAnumList.size()> 0)
						return true;
				}
				return false;
		}
		
	private void createAutoDcrObject(RegistrationExtn registration, String autoDcr) {
		RegnAutoDcrDtlsExtn autoDcrObj;
		autoDcrObj=new  RegnAutoDcrDtlsExtn();//phionix TODO
		autoDcrObj.setAutoDcrNum(autoDcr);
		autoDcrObj.setIsActive(true);
		autoDcrObj.setRegistration(registration);
		registration.addAutoDcr(autoDcrObj);
	}
	
	/**
	 * @param plotDoorNumber
	 * @param plotLandmark
	 * @param plotSurveyNumber
	 * @param plotNumber
	 * @param plotBlockNumber
	 * @param village
	 * @param cityTown
	 * @param  state
	 */
	private BpaAddressExtn createSitetAddress(RegistrationExtn registration,BpaAddressExtn siteAddress,String boundaryStateId) {
		AddressType addressTypeMaster = getAddressTypeMasterByName(PROPERTY_ADDRESS);
		/*org.egov.infra.workflow.entity.State st = (org.egov.infra.workflow.entity.State) getPersistenceService().find(
				"from State ATM where ATM.id=?",boundaryStateId);*/
		
		siteAddress.setAddressTypeMaster(addressTypeMaster);
		siteAddress.setIndianState(boundaryStateId);
		siteAddress.setRegistration(registration);
		   return siteAddress;
	}

	private AddressType getAddressTypeMasterByName(String addressTypeName ) {
		AddressType addressTypeMaster = (AddressType) getPersistenceService().find(
					"from AddressTypeMaster ATM where ATM.addressTypeName=?",addressTypeName);
		return addressTypeMaster;
	}
	/**
	 * @param applicantAddress1
	 * @param applicantAddress2
	 * @return
	 */
	private BpaAddressExtn createApplicantAddress(RegistrationExtn registration,BpaAddressExtn applicantrAddress) {
		
		AddressType addressTypeMaster = getAddressTypeMasterByName(OWNER_ADDRESS);
		applicantrAddress.setAddressTypeMaster(addressTypeMaster);
		
		applicantrAddress.setRegistration(registration);
		return applicantrAddress;
	}
	
	
	public RegistrationExtn getRegistrationById(Long registrationId) {

		return findById(registrationId);
		
	}
	@SuppressWarnings("unchecked")
	public Integer printInspectionForm(RegistrationExtn registration,
			Map<String, Object> session, String printMode,Boolean enableDocketSheetForView) {
		Integer reportId = null;
		if (registration != null) {
			ReportRequest reportInput = null;
			Map reportParams = new HashMap<String, Object>();

			List<InspectionExtn> inspectionList = inspectionExtnService
					.getAllSiteInspectionListforRegistrationObject(registration);
			InspectionExtn inspObj = null;
			if (!inspectionList.isEmpty()) {
				for (InspectionExtn inspectionObj : inspectionList) {
						if (!printMode.equals("OfficialPrint")) {
							Boolean surveyorrrole=bpaCommonExtnService.ShowUserROles(inspectionObj.getCreatedBy().getRoles(),BpaConstants.PORTALUSERSURVEYORROLE,"");
							if( surveyorrrole.equals(true)){
							inspObj = inspectionObj;
							reportParams.put("headerForReport",
									"Surveyor Inspection Details");
							}
						} else {
							Boolean arorArrrole=bpaCommonExtnService.ShowUserROles(inspectionObj.getCreatedBy().getRoles(),BpaConstants.BPAAEROLE,BpaConstants.BPAAEEROLE);
							if (printMode.equals("OfficialPrint") && arorArrrole.equals(true)) {
								if(inspObj==null){
								inspObj = inspectionObj;
								}
								reportParams.put("headerForReport",
										"AE/AEE Inspection Details");
							}
						}
				}
	
				if (inspObj != null) {
					reportParams.put("inspectionObj", inspObj);
					reportParams.put("inspectionObjDetail",
							inspObj.getInspectionDetails());
					List<InspectMeasurementDtlsExtn> inspectionMeasurementList = new ArrayList<InspectMeasurementDtlsExtn>(
							inspObj.getInspectionDetails()
									.getInspectMeasurementDtlsSet());
					for(InspectMeasurementDtlsExtn measureList :inspectionMeasurementList)
					{
						if(measureList.getInspectionSource()!=null && measureList.getInspectionSource().getCode().equals(BpaConstants.INSPECTIONSOURCEFORCONSTRUCTIONPLAN))
						{
							measureList.setHeader("Construction details as per Plan");
							reportParams.put("inspectionMeasurmenrObjectperplan",measureList);
						}
						else if(measureList.getInspectionSource()!=null && measureList.getInspectionSource().getCode().equals(BpaConstants.INSPECTIONSOURCEFORCONSTRUCTIONSITE)){
							measureList.setHeader("Construction details as per Site");
							reportParams.put("inspectionMeasurmenrObjectpersite",measureList);
						}
					}
					
							
				} else {
					reportParams.put("inspectionObj", new InspectionExtn());
					reportParams.put("inspectionObjDetail",
							new InspectionDetailsExtn());
					reportParams.put("inspectionMeasurementList",
							new ArrayList<InspectMeasurementDtlsExtn>());
				}
			} else {
				reportParams.put("inspectionObj", new InspectionExtn());
				reportParams.put("inspectionObjDetail",
						new InspectionDetailsExtn());
				reportParams.put("inspectionMeasurementList",
						new ArrayList<InspectMeasurementDtlsExtn>());
			}
			reportInput = new ReportRequest(BpaConstants.INSPECTIONDETAILSEXTN,
					registration, reportParams);
			if (reportInput != null)
				reportId = ReportViewerUtil.addReportToSession(
						reportService.createReport(reportInput), session);
		}
		if(enableDocketSheetForView!=null && enableDocketSheetForView.equals(Boolean.TRUE)){
		if(printMode!="" && printMode!=null && !printMode.equalsIgnoreCase("OfficialPrint")) 
			createRegnOfficialActions(registration,BpaConstants.VIEWED_SURVEYOR_INSPECTION);
		else
			createRegnOfficialActions(registration,BpaConstants.VIEWED_AE_AEE_INSPECTION);
		}
		return reportId;
	}
	public RegnOfficialActionsExtn getRegnOfficialActionsExtn(RegistrationExtn reg, User user) {
		return regnOfficialActionsExtnService.find(" from RegnOfficialActionsExtn where createdBy.id=? and registration.id=?", 
				user.getId(), reg.getId());
	}
	public void createRegnOfficialActions(RegistrationExtn registration, String viewType) {
		//PersonalInformation prsnlInfo=eisMgr.getEmpForUserId(Integer.valueOf(EgovThreadLocals.getUserId()));
		User user=new User();
		RegnOfficialActionsExtn regnOfficialActionsObj=getRegnOfficialActionsExtn(registration,user);
		if(regnOfficialActionsObj!=null){
			//regnOfficialActionsObj.setModifiedBy(prsnlInfo.getUserMaster());
			regnOfficialActionsObj.setModifiedDate(new Date());
			regnOfficialActionsExtnService.merge(regnOfficialActionsObj);
		}else{
			regnOfficialActionsObj=new RegnOfficialActionsExtn();
			regnOfficialActionsObj.setRegistration(registration);
			//regnOfficialActionsObj.setCreatedBy(prsnlInfo.getUserMaster());//TODO Phionix
			//regnOfficialActionsObj.setModifiedBy(prsnlInfo.getUserMaster());
			regnOfficialActionsObj.setCreatedDate(new Date());
			regnOfficialActionsObj.setModifiedDate(new Date());
			regnOfficialActionsExtnService.persist(regnOfficialActionsObj);
		}
		if(viewType.equalsIgnoreCase(BpaConstants.VIEWED_SURVEYOR_INSPECTION)){
			regnOfficialActionsObj.setViewedSurveyorInsp(true);
		}else if(viewType.equalsIgnoreCase(BpaConstants.VIEWED_AE_AEE_INSPECTION)){
			regnOfficialActionsObj.setViewedAE_AEEInsp(true);
		}else if(viewType.equalsIgnoreCase(BpaConstants.VIEWED_SURVEYOR_DOCDETAILS)){
			regnOfficialActionsObj.setViewedSurveyorDocDtls(true);
		}else if(viewType.equalsIgnoreCase(BpaConstants.VIEWED_AE_AEE_DOCDETAILS)){
			regnOfficialActionsObj.setViewedAE_AEEDocDtls(true);
		}else if(viewType.equalsIgnoreCase(BpaConstants.VIEWED_AUTODCR_DETAILS)){
			regnOfficialActionsObj.setViewedAutoDcrDtls(true);
		}else if(viewType.equalsIgnoreCase(BpaConstants.VIEWED_DOCKETSHEET)){
			regnOfficialActionsObj.setViewedDocketSheet(true);
		}
	}
	public RegistrationExtn getRegistrationByPassingRequestNumber(String  req_number) {

		if(req_number!=null && !"".equals(req_number) ) {
			Criteria registrationObjCriteria =getSession().createCriteria(RegistrationExtn.class, "register");
			registrationObjCriteria.add(Restrictions.eq("register.request_number", req_number));
		return (RegistrationExtn) registrationObjCriteria.uniqueResult();
		}else {
			return null;
		}
		
		
	}
	
	public RegistrationExtn getRegistrationByPassingReferenceNumber(
			String referenceNumber) {
		
		if(referenceNumber!=null && !"".equals(referenceNumber)) {
			Criteria registrationObjCriteria =getSession().createCriteria(RegistrationExtn.class, "register");
			//registrationObjCriteria.add(Restrictions.eq("register.serviceRegistryId", serviceRegId));
			registrationObjCriteria.add(Restrictions.eq("register.planSubmissionNum", referenceNumber));
			return  (RegistrationExtn) registrationObjCriteria.uniqueResult();
		}
		else { 
			return null;
		}
		
	}

	
	 /*
	  * To get Citeria for Total Amount of CMDA and MWGWF from Registrationfee details.
	  */
	@SuppressWarnings("unchecked")
	public List<RegistrationFeeDetailExtn> getCaptureDdAmountTotal(Long registrationId,RegistrationExtn registration)
	{
		Criteria captureDdDetail =getSession().createCriteria(RegistrationFeeDetailExtn.class,"registrationDdDetail");
		captureDdDetail.createAlias("registrationDdDetail.registrationFee", "registrationFee");
		captureDdDetail.createAlias("registrationDdDetail.bpaFee", "bpaFee");
		
		captureDdDetail.add(Restrictions.eq("registrationFee.registration.id", registrationId));
		captureDdDetail.add(Restrictions.disjunction().add(Restrictions.eq("bpaFee.feeGroup", BpaConstants.FEEGROUPCMDA))
				.add(Restrictions.eq("bpaFee.feeGroup", BpaConstants.FEEGROUPMWGWF)));
		captureDdDetail.add(Restrictions.eq("bpaFee.serviceType.id", (registration!=null && registration.getServiceType()!=null?registration.getServiceType().getId():"")));
		LOGGER.info("captureDdDetail size" +captureDdDetail.list());
		return captureDdDetail.list();
	}
	
	public BpaDmdCollExtnService getBpaDmdCollExtnService() {
		return bpaDmdCollExtnService;
	}
	public void setBpaDmdCollExtnService(BpaDmdCollExtnService bpaDmdCollService) {
		this.bpaDmdCollExtnService = bpaDmdCollService;
	}
	public List<CheckListDetailsExtn> getRegistrationCheckListforService(
			Long serviceTypeIdTemp) {
		Criteria checkListDet =getSession().createCriteria(CheckListDetailsExtn.class,"checklistdet");
		checkListDet.createAlias("checklistdet.checkList", "checkList");
		checkListDet.createAlias("checkList.serviceType", "servicetype");
		checkListDet.add(Restrictions.eq("servicetype.id", serviceTypeIdTemp));
		checkListDet.add(Restrictions.ilike("checkList.checklistType", "DOCUMENTATION"));
		checkListDet.addOrder(Order.asc("description")); 
		return checkListDet.list();
	}

	
	
	public List<CheckListDetailsExtn> getUnconsideredCheckListforService(
			Long serviceTypeIdTemp){
		Criteria checkListDet =getSession().createCriteria(CheckListDetailsExtn.class,"checklistdet");
		checkListDet.createAlias("checklistdet.checkList", "checkList");
		checkListDet.createAlias("checkList.serviceType", "servicetype");
		checkListDet.add(Restrictions.eq("servicetype.id", serviceTypeIdTemp));
		checkListDet.add(Restrictions.ilike("checkList.checklistType", "UNCONSIDERED"));
		return checkListDet.list();
	}
	
	private RegistrationExtn createWorkflow(RegistrationExtn registration,String workFlowAction,String approverComments, Position position) 
	{
		LOGGER.info("....start BPA Registration work flow....");
		try
		{
			
			if(registration.getState()== null){
				if(position!=null){
					//TODO:Commenting WorkflowService is not using and 1 level workflow they are using
					//registration = (RegistrationExtn) registrationWorkflowExtnService.start(registration, position, "Bpa Registration created.");
				}else
				{
					Position pos = bpaPimsExtnFactory.getPositionByUserId((EgovThreadLocals.getUserId()));
					//LOGGER.info("BPA Registration Service workflow service----->"+ Integer.valueOf(EgovThreadLocals.getUserId()));
					//registration = (RegistrationExtn) registrationWorkflowExtnService.start(registration, pos, "Bpa Registration created.");
					//TODO:Commenting WorkflowService is not using and 1 level workflow they are using
				}
			}
			 
			if(workFlowAction!=null && !"".equals(workFlowAction) && !BpaConstants.SCRIPT_SAVE.equalsIgnoreCase(workFlowAction)){
				String comments= (approverComments==null || "".equals(approverComments.trim()))?"":approverComments;
				
				/*
				 * In case of rejection, get previous state of record from matrix table. Search previous state owner from workflow state (registration.getHistory()).
				 */
				if(BpaConstants.SCRIPT_REJECT.equalsIgnoreCase(workFlowAction) || workFlowAction.equalsIgnoreCase("cancel unconsidered"))
				{
					WorkFlowMatrix wfMatrix= getPreviousStateFromWfMatrix(registration.getStateType(),null,null,additionalRule,registration.getCurrentState().getValue(),registration.getCurrentState().getNextAction());
					if(wfMatrix!=null)
					{
						registration.setPreviousObjectState(wfMatrix.getCurrentState());
						registration.setPreviousObjectAction(wfMatrix.getPendingActions());
						
						/*for(State state: registration.getHistory())
						{
							 if (state.getValue().equalsIgnoreCase(wfMatrix.getCurrentState()))
							 {
								 registration.setPreviousStateOwnerId(state.getOwner().getId());
								 break;
							 }
						}*/
					}
				
				}
				//Pionix TODO:commenting as of now der is no WorkflowService
			/*	bpaCommonExtnService.workFlowTransition(registration, workFlowAction,
									comments);*/
						
			}
		}catch(ValidationException ex)
		{
			LOGGER.error("...Error in BPA Registration work flow.in validation Exception..."+ex.getMessage());
			ex.printStackTrace();
			throw ex;
		}
		catch(Exception ex)
		{
			LOGGER.error("...Error in BPA Registration work flow.in Exception..."+ex.getMessage());
			ex.printStackTrace();
			 throw new EGOVRuntimeException(ex.getMessage()+"--Error in create Workflow");
		}
		  LOGGER.info("....end BPA Registration work flow....");
		return registration;
	}
		
	public WorkFlowMatrix getPreviousStateFromWfMatrix(final String type,
			final String department, final BigDecimal amountRule,
			final String additionalRule, final String currentState,
			final String pendingActions) {

		final Criteria wfMatrixCriteria = commonWorkFlowMatrixCriteria(type,
				additionalRule, currentState, pendingActions);
		if (department!=null && !"".equals(department)) {
			wfMatrixCriteria.add(Restrictions.eq("department", department));
		}else
			wfMatrixCriteria.add(Restrictions.eq("department", "ANY"));

		// Added restriction for amount rule
		if (amountRule != null && !BigDecimal.ZERO.equals(amountRule)) {
			final Criterion amount1st = Restrictions.conjunction()
					.add(Restrictions.le("fromQty", amountRule))
					.add(Restrictions.ge("toQty", amountRule));
			final Criterion amount2nd = Restrictions.conjunction()
					.add(Restrictions.le("fromQty", amountRule))
					.add(Restrictions.isNull("toQty"));
			wfMatrixCriteria.add(Restrictions.disjunction().add(amount1st)
					.add(amount2nd));

		}

		final List<WorkFlowMatrix> objectTypeList = wfMatrixCriteria.list();

		if (!objectTypeList.isEmpty()) {
			return objectTypeList.get(0);
		} 
		
		return null;

	}
	
	public String getEmpNameDesignation(Position position, Date date) {
		String empName = "";
		String designationName = "";

		if (position != null) {
			DeptDesig deptDesig = position.getDeptDesig();
			PersonalInformation personalInformation = null;
			try {
				/*personalInformation = eisMgr.getEmpForPositionAndDate(date,
						position.getId());*/
				
				if (personalInformation != null
						&& personalInformation.getEmployeeName() != null)
					empName = personalInformation.getEmployeeName();
				
			} catch (Exception e) {
				LOGGER.debug("exception " + e);
			}
			
			if (deptDesig != null) {

				Designation designationMaster = deptDesig.getDesignation();
				if(designationMaster!=null)
					designationName = designationMaster.getName();

			}
		}
		return empName + "@" + designationName;
	}
	
	/**
	 * @Description : on forward of Create Letter To Party returns the empName and designationName of selected user in workflow ..
	 * @param approverDesignationId
	 * @param approverPositionId
	 * @param date
	 * @return 
	 */
	public String getEmpNameDesignationByApproveDesgIdAndApproveId(Integer  approverDesignation, Integer approverPositionId,Date date) {
		String empName = "";
		String designationName = "";
		
		if(approverPositionId!=null){
			PersonalInformation personalInformation = null;
				try {
					//TODO: Commenting for EIS Changes Need
						/*personalInformation = eisMgr.getEmpForPositionAndDate(date,
								approverPositionId);*/
					
				if (personalInformation != null && personalInformation.getEmployeeFirstName() != null)
					empName = personalInformation.getEmployeeFirstName(); 
				
				} catch (Exception e) {
						LOGGER.debug("exception " + e);
				}
			
			if (approverDesignation != null) {
				Designation designation=	(Designation) persistenceService.find("from Designation where id=?",approverDesignation);
				if(designation!=null)
					designationName = designation.getName();

			}
		}
		return empName + "@" + designationName;
	}
	
	public ServiceTypeExtn getServiceTypeByRegistrationId(Long registrationId) {
		
		if(registrationId!=null)
		{
		return (ServiceTypeExtn)persistenceService.find("select reg.serviceType from RegistrationExtn reg where reg.id=?", registrationId);
		}
		else
		{ 
			return null;
		}
			
	}
	
	private Criteria commonWorkFlowMatrixCriteria(String type,String additionalRule,String currentState,String pendingActions){
		final Criteria commonWfMatrixCriteria=persistenceService.getSession().createCriteria(WorkFlowMatrix.class);
		commonWfMatrixCriteria.add(Restrictions.eq("objectType", type));

		if(StringUtils.isNotBlank(additionalRule)){
			commonWfMatrixCriteria.add(Restrictions.eq("additionalRule", additionalRule));
		}
		
		if(StringUtils.isNotBlank(pendingActions)){
			commonWfMatrixCriteria.add(Restrictions.ilike("nextAction", pendingActions,MatchMode.EXACT));
		}

		if(StringUtils.isNotBlank(currentState)){
			commonWfMatrixCriteria.add(Restrictions.ilike("nextState", currentState,MatchMode.EXACT));
		}
		return commonWfMatrixCriteria;
	}
	
 
	@Transactional
		public RegistrationExtn createLetterToParty(LetterToPartyExtn letterToParty,List<LpChecklistExtn> chkListDet,
				RegistrationExtn registrationObj,String workFlowAction, String approverComments){
			
			oldStatus=registrationObj.getEgwStatus();
			Set<LpChecklistExtn> checkListDtls=new HashSet<LpChecklistExtn>(chkListDet);
			letterToParty.setLpChecklistSet(checkListDtls);
			for(LpChecklistExtn lpChecklist:letterToParty.getLpChecklistSet()){
				lpChecklist.setLetterToParty(letterToParty);
				lpChecklist.setLpChecklistType(BpaConstants.LETTERTOPARTYDETAILS);
			}
			letterToParty.setRegistration(registrationObj);
			letterToParty.setLetterDate(new Date());
			if(letterToParty.getLetterToPartyNumber()==null || letterToParty.getLetterToPartyNumber().equals("")){
				letterToParty.setLetterToPartyNumber(bpaNumberGenerationExtnService.generateLetterToPartyNumber());
		   }
			List<InspectionExtn> inspectionList=getInspectionListforRegistrationObject(registrationObj);
			if(inspectionList!=null && !(inspectionList.isEmpty())){
				letterToParty.setInspection(inspectionList.get(0));
			}
			String UserRole=bpaCommonExtnService.getUserRolesForLoggedInUser();
			if(UserRole.contains(BpaConstants.PORTALUSERSURVEYORROLE))
			{
			letterToParty.getRegistration().setEgwStatus(bpaCommonExtnService.getstatusbyCode(BpaConstants.LETTERTOPARTYRAISED,BpaConstants.NEWBPAREGISTRATIONMODULE));
			}
			else{
			if(null!=letterToParty.getSentDate() && registrationObj.getEgwStatus().getCode().equals(BpaConstants.CREATEDLETTERTOPARTY)){
				letterToParty.getRegistration().setEgwStatus(bpaCommonExtnService.getstatusbyCode(BpaConstants.LETTERTOPARTYSENT));
			}
			else 
			{
				 if(!(registrationObj.getEgwStatus().getCode().equals(BpaConstants.LETTERTOPARTYSENT)))
					 letterToParty.getRegistration().setEgwStatus(bpaCommonExtnService.getstatusbyCode(BpaConstants.CREATEDLETTERTOPARTY));
			}
			}
			registrationObj.setAdditionalRule(BpaConstants.LETTERTOPARTYDETAILS);
			User LoggedInuser = bpaCommonExtnService.getUserbyId((EgovThreadLocals.getUserId()));
				if(LoggedInuser!=null)
				{
					letterToParty.setCreatedBy(LoggedInuser);
				}
			registrationObj.getLetterToPartySet().add(letterToParty);
			if(null!=registrationObj.getApproverPositionId() && registrationObj.getApproverPositionId()!=-1)
			   {
				  approverId= registrationObj.getApproverPositionId();
			   }
			if(letterToParty.getId()==null){
		    	registrationObj=persist(registrationObj);
		    }
		    else{
		    	registrationObj=merge(registrationObj);
		    }
		    
		    registrationObj.setApproverPositionId(approverId);
			createWorkflow(registrationObj,workFlowAction,approverComments,null);
			bpaCommonExtnService.createStatusChange(registrationObj,oldStatus);
			return registrationObj;
			}
		
	
		public List<InspectionExtn> getInspectionListforRegistrationObject(RegistrationExtn registrationObj) {

			return (List<InspectionExtn>) persistenceService.findAllBy("from InspectionExtn where registration=? and isInspected=? order by id desc,inspectionDate desc",registrationObj,Boolean.FALSE);
		}
		
		 public List<LpChecklistExtn> getLetterToPartyCheckDtlsForType(Long letterToPartyId,String checkListType) {
				
				Criteria checkListDet =getSession().createCriteria(LpChecklistExtn.class,"lpCheckList"); 
				checkListDet.createAlias("lpCheckList.letterToParty", "lettertopartydetails");
				checkListDet.createAlias("lpCheckList.checkListDetails", "chklistmstr");
				
				checkListDet.add(Restrictions.eq("letterToParty.id", letterToPartyId));
				checkListDet.add(Restrictions.eq("lpChecklistType", checkListType));
				checkListDet.addOrder(Order.asc("chklistmstr.description"));
				return checkListDet.list(); 
		}
		 
		 public List<CheckListDetailsExtn> getLPCheckListforService(
					Long serviceTypeIdTemp) {
				Criteria checkListDet =getSession().createCriteria(CheckListDetailsExtn.class,"checklistdet");
				checkListDet.createAlias("checklistdet.checkList", "checkList");
				checkListDet.createAlias("checkList.serviceType", "servicetype"); 
				checkListDet.add(Restrictions.eq("servicetype.id", serviceTypeIdTemp));
				checkListDet.add(Restrictions.ilike("checkList.checklistType", "LP"));
				checkListDet.addOrder(Order.asc("checklistdet.description"));  
				return checkListDet.list();
			}
	
	
		public List<LpChecklistExtn> getCheckedLPCheckListforReply(Long letterToPartyId)
		 {
			 	Criteria checkListDet =getSession().createCriteria(LpChecklistExtn.class,"lpCheckList");
			 	checkListDet.createAlias("lpCheckList.checkListDetails", "chklistmstr");
			 	checkListDet.add(Restrictions.eq("letterToParty.id", letterToPartyId));
			 	checkListDet.add(Restrictions.eq("lpCheckList.isChecked", Boolean.TRUE));
				checkListDet.addOrder(Order.asc("chklistmstr.description")); 
				return checkListDet.list();  
			}
		 
		 public ServiceTypeExtn getServiceTypeById(Long serviceTypeId) {

				return (ServiceTypeExtn) persistenceService.find(" from ServiceTypeExtn where id=?",serviceTypeId);
			}
					 
		 
		 public ServiceTypeExtn getServiceTypeByCode(String codeValue) {
				return (ServiceTypeExtn) persistenceService.find(" from ServiceTypeExtn where code=?",codeValue);
			}
		 
		 public List<LetterToPartyExtn> getLetterToPartyForRegistrationObject(RegistrationExtn registrationObj) {
				return  (List<LetterToPartyExtn>)persistenceService. findAllBy("from LetterToPartyExtn where registration=?  order by id desc",registrationObj);
			}
		 /*
		  * get LatestLetterToParty for registration  
		  */
		 public LetterToPartyExtn getLetterToPartyForRegistrationObjectWhereSentDateisNull(RegistrationExtn registrationObj) {
			 if(registrationObj!=null){
				return  (LetterToPartyExtn)persistenceService. find("from LetterToPartyExtn where registration=?  and sentDate is null and isHistory is null order by id desc",registrationObj);
			 }
			 else 
				 return null;
			}
		 
		 public LetterToPartyExtn getLetterToPartyById(Long letterTopartyId) {
			 if(letterTopartyId!=null){
				return (LetterToPartyExtn) persistenceService.find("from LetterToPartyExtn where id=?",letterTopartyId);
			 }
			 else
				 return null;
			 }
		
		public List<RejectionChecklistExtn> getRejectionCheckListforRegistration(Long id) {
			Criteria checkListDet =getSession().createCriteria(RejectionChecklistExtn.class,"rejectionCheckList");
			checkListDet.createAlias("rejectionCheckList.rejection", "rejection");
			checkListDet.createAlias("rejection.registration", "registration");
			checkListDet.add(Restrictions.eq("registration.id", id));
			checkListDet.addOrder(Order.asc("id"));
			return checkListDet.list();
			
		}
				
		public RegistrationExtn createRegnStatusDetails(RegnStatusDetailsExtn regnStatusDetail, 
				String statusCode, RegistrationExtn registration) {
			EgwStatus egwStatus =bpaCommonExtnService.getstatusbyCode(statusCode);
			regnStatusDetail.setRegistration(registration);
			regnStatusDetail.setStatus(egwStatus);
			registration.addRegnStatusDetails(regnStatusDetail);
			regnStatusDetExtnService.create(regnStatusDetail);
			return registration;
		}
		
		
		public RegistrationExtn updateChallanSentDate(RegistrationExtn registration) {
			EgwStatus oldStatus = registration.getEgwStatus();
			
			if(registration.getChallanDetails().getStatusdate()!=null) {
				registration = createRegnStatusDetails(registration.getChallanDetails(), 
						BpaConstants.CHALLANNOTICESENT, registration);
				registration.setEgwStatus(bpaCommonExtnService.getstatusbyCode(BpaConstants.CHALLANNOTICESENT));
				bpaCommonExtnService.createStatusChange(registration, oldStatus);
				registration = merge(registration);
			}
			return registration;
		}
		
		@Transactional
		public RegistrationExtn updateSignature(RegistrationExtn registration) {
			EgwStatus oldStatus = registration.getEgwStatus();
			
			if(registration.getSignDetails().getStatusdate()!=null) {
				registration = createRegnStatusDetails(registration.getSignDetails(), 
						BpaConstants.APPLICANTSIGNUPDATED, registration);
				registration.setEgwStatus(bpaCommonExtnService.getstatusbyCode(BpaConstants.APPLICANTSIGNUPDATED));
				bpaCommonExtnService.createStatusChange(registration, oldStatus);
				registration = merge(registration);
			}
			return registration;
		}
		@Transactional
		public RegistrationExtn updateOrderPrepStatus(RegistrationExtn registration) {
			EgwStatus oldStatus = registration.getEgwStatus();
			
			if(registration.getOrderDetails().getStatusdate()!=null) {
				registration = createRegnStatusDetails(registration.getOrderDetails(), 
						BpaConstants.ORDERPREPARED, registration);
				registration.setEgwStatus(bpaCommonExtnService.getstatusbyCode(BpaConstants.ORDERPREPARED));
				bpaCommonExtnService.createStatusChange(registration, oldStatus);
				bpaCommonExtnService.buildEmail(registration,"OrderPrepared",null);
				bpaCommonExtnService.buildSMS(registration,"OrderPrepared");
				registration = merge(registration);
			}
			return registration;
		}
		@Transactional
		public RegistrationExtn updateOrderIssueStatus(RegistrationExtn registration) {
			EgwStatus oldStatus = registration.getEgwStatus();
			
			if(registration.getOrderIssueDet().getStatusdate()!=null) {
				registration = createRegnStatusDetails(registration.getOrderIssueDet(), 
						BpaConstants.ORDERISSUEDTOAPPLICANT, registration);
				registration.setEgwStatus(bpaCommonExtnService.getstatusbyCode(BpaConstants.ORDERISSUEDTOAPPLICANT));
				bpaCommonExtnService.createStatusChange(registration, oldStatus);
				bpaCommonExtnService.buildEmail(registration,"OrderIssued",null);
				bpaCommonExtnService.buildSMS(registration,"OrderIssued");
				registration = merge(registration);
			}
			return registration;
		}
@Transactional
		public RegistrationExtn rejectOrderPrepStatus(RegistrationExtn registration) {
			EgwStatus oldStatus = registration.getEgwStatus();
			
			if(registration.getRejectOrdPrepDet().getStatusdate()!=null) {
				registration = createRegnStatusDetails(registration.getRejectOrdPrepDet(), 
						BpaConstants.REJECTORDERPREPARED, registration);
				registration.setEgwStatus(bpaCommonExtnService.getstatusbyCode(BpaConstants.REJECTORDERPREPARED));
				bpaCommonExtnService.createStatusChange(registration, oldStatus);
				bpaCommonExtnService.buildEmail(registration,"RejectOrderPrepared",null);
				bpaCommonExtnService.buildSMS(registration,"RejectOrderPrepared");
				registration = merge(registration);
			}
			return registration;
		}
		@Transactional
		public RegistrationExtn rejectOrderIssueStatus(RegistrationExtn registration) {
			EgwStatus oldStatus = registration.getEgwStatus();
			
			if(registration.getRejectOrdIssDet().getStatusdate()!=null) {
				registration = createRegnStatusDetails(registration.getRejectOrdIssDet(), 
						BpaConstants.REJECTORDERISSUED, registration);
				registration.setEgwStatus(bpaCommonExtnService.getstatusbyCode(BpaConstants.REJECTORDERISSUED));
				bpaCommonExtnService.createStatusChange(registration, oldStatus);
				bpaCommonExtnService.buildEmail(registration,"RejectOrderIssued",null);
				bpaCommonExtnService.buildSMS(registration,"RejectOrderIssued");
				registration = merge(registration);
			}
			return registration;
		}
		
		public List<ReportFeesDetailsExtn> getSanctionedReportFeeDtls(RegistrationExtn registration) {
			List <ReportFeesDetailsExtn> reportFeeList =new LinkedList<ReportFeesDetailsExtn>();
			if(registration!=null && registration.getEgDemand()!=null&&registration.getEgDemand().getEgDemandDetails()!=null){
				Set<EgDemandDetails> demandDetailsSet=registration.getEgDemand().getEgDemandDetails();
				HashMap<String,String> demandRsnMstrCodeMap=new HashMap<String,String>();
				HashMap<String,BigDecimal> demandAmountMap=new HashMap<String,BigDecimal>();
				for(EgDemandDetails demandDetails:demandDetailsSet){
					demandRsnMstrCodeMap.put(demandDetails.getEgDemandReason().getEgDemandReasonMaster().getCode(),demandDetails.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster());
					demandAmountMap.put(demandDetails.getEgDemandReason().getEgDemandReasonMaster().getCode(), demandDetails.getAmount());
				}
				List<BpaFeeExtn> santionFeeList=feeExtnService.getAllSanctionedFeesbyServiceType(registration.getServiceType().getId());
				if(!santionFeeList.isEmpty())
					Collections.sort(santionFeeList, BpaFeeExtnComparator.getFeeComparator(BpaFeeExtnComparator.FEECODE_SORT));
				for(BpaFeeExtn fees:santionFeeList){
					ReportFeesDetailsExtn reportFeesDetails =new ReportFeesDetailsExtn();
					if(null!=demandRsnMstrCodeMap.get(fees.getFeeCode())){
					reportFeesDetails.setDescription(demandRsnMstrCodeMap.get(fees.getFeeCode()));
					reportFeesDetails.setAmount(demandAmountMap.get(fees.getFeeCode()));
					reportFeeList.add(reportFeesDetails);
					}
				} 
				}
			return reportFeeList;
		}
		
		public Map<String, BillReceiptInfo> getCollectedReceiptsByRegistrationId(RegistrationExtn registration) {
			return bpaCommonExtnService.getReceiptInfoByRegistrationObject(registration);
		}
		 public List<CMDALetterToPartyExtn> getcmdaLetterToPartyForRegistrationObject(RegistrationExtn registrationObj) {
				return  (List<CMDALetterToPartyExtn>)persistenceService. findAllBy("from CMDALetterToPartyExtn where registration=?  order by id desc",registrationObj);
			}
	public Integer 	printDocketSheet (RegistrationExtn registration,
			Map<String, Object> session,Boolean enableDocketSheetForView) {
		Integer reportId = null;
		if (registration != null) {
			ReportRequest reportInput = null; 
			Map reportParams = new HashMap<String, Object>();
			Set<RegistrationChecklistExtn> checkListDtls = new HashSet<RegistrationChecklistExtn>();
			
			
			List<InspectionExtn> inspectionList=inspectionExtnService.getSiteInspectionListforRegistrationObject(registration);
			List<ReportFeesDetailsExtn> reportFeeDetailsList=getSanctionedReportFeeDtls(registration);
			
			RegistrationFeeExtn registrationFeeObj=bpaCommonExtnService.getLatestApprovedRegistrationFee(registration);
			Map<String,List<BpaFeeExtn>>  registrationFeesesByFeeGroup=bpaCommonExtnService.getGroupWiseFeeListByPassingRegistration(registration,registrationFeeObj!=null? registrationFeeObj.getId():null);
			
			if(registrationFeesesByFeeGroup!=null && registrationFeesesByFeeGroup.size()>0){
					reportParams.put("reportFeeList", registrationFeesesByFeeGroup.get(BpaConstants.COCFEE));
			}
			
			 reportParams.put("reportFeeList", reportFeeDetailsList);
			 //BpaConstants.OWNER_ADDRESS
			 
			 reportParams.put("doorNumber", " ");
			 reportParams.put("surveyNumber", " ");
			 reportParams.put("blockNumber", " ");
			 reportParams.put("revenueVillage", " ");
			 reportParams.put("StreetName", " ");
			 
			 
			 for (BpaAddressExtn bpaAddressObj : registration.getBpaAddressSet()) {
					if (null != bpaAddressObj.getAddressTypeMaster() && BpaConstants.PROPERTY_ADDRESS.equals(bpaAddressObj.getAddressTypeMaster())) {//Pionix TODO:commenting they AddressType as ENum
						reportParams.put("doorNumber",  bpaAddressObj.getPlotDoorNumber());
						reportParams.put("surveyNumber",  bpaAddressObj.getPlotSurveyNumber());
						reportParams.put("blockNumber",  bpaAddressObj.getPlotBlockNumber());
					if(bpaAddressObj.getVillageName()!=null)
						reportParams.put("revenueVillage",  bpaAddressObj.getVillageName().getName());
						
					if (registration.getLocboundaryid() != null && registration.getLocboundaryid().getName() != null) 
							reportParams.put("StreetName",  registration.getLocboundaryid().getName() + registration.getLocboundaryid().getParent().getName());
				    }
			    }
			 
			if (inspectionList != null && !inspectionList.isEmpty()) {
				InspectionExtn inspectionObj = inspectionList.get(0);
				reportParams.put("inspectionObj", inspectionObj);
				reportParams.put("inspectionDate", (inspectionObj.getInspectionDate()));
				reportParams.put("docketObj", inspectionObj.getDocket());
				
			if(inspectionObj.getDocket()!=null){
				reportParams.put("docketObjConstructionStages", inspectionObj.getDocket().getConstructionStagesSet());
				reportParams.put("docketDocumentsEnclosed", inspectionObj.getDocket().getDocumentEnclosedSet());
				
				
				for(DocketFloorDetails docketFloor:inspectionObj.getDocket().getDocketFlrDtlsSet())
				 {
					 if(docketFloor.getFloorNum()!=null){
						 Map<Integer, String> floorNoMap= bpaCommonExtnService.getFloorNumberMap(inspectionObj.getDocket().getFloorCount());
						 docketFloor.setFloorName(floorNoMap.get(docketFloor.getFloorNum()));
						}
				 }
				reportParams.put("devFloorSet",inspectionObj.getDocket().getDocketFlrDtlsSet());
				 Set <DocketViolations> 	devContrlSet = new HashSet<DocketViolations>();
				 Set <DocketViolations> 	setBackSet = new HashSet<DocketViolations>();
				 Set <DocketViolations> 	parkingSet = new HashSet<DocketViolations>();
				 Set <DocketViolations> 	generalSet = new HashSet<DocketViolations>();
				 Set <DocketViolations> 	minDistancePowerLineSet = new HashSet<DocketViolations>();
				for( DocketViolations docViolations: inspectionObj.getDocket().getViolationSet())
				{
					if(docViolations.getCheckListDetails().getCheckList().getChecklistType().equals("DOCKETSHEET-VIOLATION-DEVCONTROLRULE"))
					{
						devContrlSet.add(docViolations);
					}
					else if(docViolations.getCheckListDetails().getCheckList().getChecklistType().equals("DOCKETSHEET-VIOLATION-SETBACK"))
					{
						setBackSet.add(docViolations);
					}
					else if(docViolations.getCheckListDetails().getCheckList().getChecklistType().equals("DOCKETSHEET-VIOLATION-PARKING"))
					{
						parkingSet.add(docViolations);
					}
					else if(docViolations.getCheckListDetails().getCheckList().getChecklistType().equals("DOCKETSHEET-VIOLATION-GENERAL"))
					{
						generalSet.add(docViolations);
					}
					else if(docViolations.getCheckListDetails().getCheckList().getChecklistType().equals("DOCKETSHEET-VIOLATION-MINDISTANCEPOWERLINE"))
					{
						minDistancePowerLineSet.add(docViolations);
					}
				}
				 reportParams.put("devContrlSet",devContrlSet);
				 reportParams.put("setBackSet",setBackSet);
				 reportParams.put("parkingSet",parkingSet);
				 reportParams.put("generalSet",generalSet);
				 reportParams.put("minDistancePowerLineSet",minDistancePowerLineSet);
			  }

			}
			
			for (RegistrationChecklistExtn regChecklist : registration  
					.getRegistrationChecklistSet()) {
				if (null != regChecklist.getIsChecked()) {
					checkListDtls.add(regChecklist);
				}
			}
			
			reportInput = new ReportRequest(
					BpaConstants.DOCKETSHEETPRINT, registration,
					reportParams);
			if (reportInput != null)
				reportId=ReportViewerUtil.addReportToSession(
						reportService.createReport(reportInput), session);

		}
		return reportId;
	}

	@SuppressWarnings("unchecked")
	public Integer printRegistrationForm(RegistrationExtn registration,
			Map<String, Object> session) {
		Integer reportId = null;
		if (registration != null) {
			ReportRequest reportInput = null; 
			Map reportParams = new HashMap<String, Object>();
			Set<RegistrationChecklistExtn> checkListDtls = new HashSet<RegistrationChecklistExtn>();

			for (RegistrationChecklistExtn regChecklist : registration  
					.getRegistrationChecklistSet()) {
				if (null != regChecklist.getIsChecked()) {
					checkListDtls.add(regChecklist);
				}
			}
			registration.getRegistrationChecklistSet().removeAll(
					registration.getRegistrationChecklistSet());
			registration.setRegistrationChecklistSet(checkListDtls);
			RegnAutoDcrExtn autoDcrObj = autoDcrExtnService
					.getAutoDcrByRegId(registration.getId());
			if (autoDcrObj != null) {
				reportParams.put("autoDCRNum", autoDcrObj.getAutoDcrNum());
			}
			List<DocumentHistoryExtn> DocHistoryListForPrint=bpaCommonExtnService.getRegnDocumentHistoryObject(registration);
			if(DocHistoryListForPrint!=null){
				reportParams.put("docementaryHistoryList", DocHistoryListForPrint);
			}
			reportInput = new ReportRequest(
					BpaConstants.REGISTRATIONEXTNACKNOWLEDGEMENT, registration,
					reportParams);
			if (reportInput != null)
				reportId=ReportViewerUtil.addReportToSession(
						reportService.createReport(reportInput), session);

		}
		return reportId;
	}
	
	public Integer printExternalFeeDetails(RegistrationExtn registration,
			Map<String, Object> session) {
		Integer reportId = null;
		Map<String, Object> reportData = new HashMap<String, Object>();
		Map<String, Object> reportParams = new HashMap<String, Object>();
		ReportRequest reportRequest = null;

		if (registration != null && registration.getFeeDDSet()!=null && !registration.getFeeDDSet().isEmpty()) {
			reportData.put("registration", registration);
			reportData.put("FeeDDSet", registration.getFeeDDSet());
		
			reportRequest = new ReportRequest(
					BpaConstants.EXPTERNALFEEEXTNACKREPORT, reportData,
					reportParams);
			reportRequest.setPrintDialogOnOpenReport(true);
			reportId = ReportViewerUtil.addReportToSession(
					reportService.createReport(reportRequest), session);

		}
		return reportId;
	}
	@Transactional
	public RegistrationExtn createCMDALetterToParty(CMDALetterToPartyExtn letterToParty,
			RegistrationExtn registrationObj,String workFlowAction, String approverComments){
		
		oldStatus=registrationObj.getEgwStatus();
		letterToParty.setRegistration(registrationObj);
		if(letterToParty.getLetterToPartyNumber()==null || letterToParty.getLetterToPartyNumber().equals("")){
			letterToParty.setLetterToPartyNumber(bpaNumberGenerationExtnService.generateCMDALetterToPartyNumber());
	   }
		String UserRole=bpaCommonExtnService.getUserRolesForLoggedInUser();
		
		if( registrationObj.getEgwStatus().getCode().equals(BpaConstants.CREATEDCMDALETTERTOPARTY)){
			letterToParty.getRegistration().setEgwStatus(bpaCommonExtnService.getstatusbyCode(BpaConstants.CMDALETTERTOPARTYSENT));
		}
		else 
		{
			 if(!(registrationObj.getEgwStatus().getCode().equals(BpaConstants.CMDALETTERTOPARTYSENT)))
				 letterToParty.getRegistration().setEgwStatus(bpaCommonExtnService.getstatusbyCode(BpaConstants.CREATEDCMDALETTERTOPARTY));
		}
		registrationObj.setAdditionalRule(BpaConstants.LETTERTOCMDA);
		User LoggedInuser = bpaCommonExtnService.getUserbyId((EgovThreadLocals.getUserId()));
			if(LoggedInuser!=null)
			{
				letterToParty.setCreatedBy(LoggedInuser);
				letterToParty.setCreatedDate(new Date()); 
			} 
			
	List <LetterToPartyExtn> lToP = getLetterToPartyForRegistrationObject(registrationObj);
	registrationObj.setLetterToParty(lToP.get(0));
	
		
		registrationObj.getLetterToParty().getCmdaLetterToPartySet().add(letterToParty);
		letterToParty.setLetterToParty(registrationObj.getLetterToParty());
		if(null!=registrationObj.getApproverPositionId() && registrationObj.getApproverPositionId()!=-1)
		   {
			  approverId= registrationObj.getApproverPositionId();
		   }
		if(letterToParty.getId()==null){
	    	registrationObj=persist(registrationObj);
	    }
	    else{
	    	registrationObj=merge(registrationObj);
	    }
	    
	    registrationObj.setApproverPositionId(approverId);
		createWorkflow(registrationObj,workFlowAction,approverComments,null);
		bpaCommonExtnService.createStatusChange(registrationObj,oldStatus);
		return registrationObj;
		}
	public RegistrationExtn getRegistrationByPassingCheckListNumber(Long requestNum) {
		RegistrationChecklistExtn regnChkObj=null;
		Criteria registrationChkListObjCriteria =getSession().createCriteria(RegistrationChecklistExtn.class, "registerChkList");
		registrationChkListObjCriteria.add(Restrictions.eq("id", requestNum));
		regnChkObj=(RegistrationChecklistExtn) registrationChkListObjCriteria.uniqueResult();
		return (regnChkObj!=null? regnChkObj.getRegistration():null);
	}
	
	/*
	 *  Select all bpa registration's where 
	 *  1. Registered status RECORD
		2. CURRENT state IS forwarded TO AEorAEE -- record should be in AE or AEE inbox
		3. inspection DATE NOT yet decided by AE or AEE
		4. created date should be lass than 2 days from current date.
	 */
	public List<RegistrationExtn> getAllBpaRegistrationForSiteInspectionDateUpdation()
 {
		Criteria registrations = getSession().createCriteria(
				RegistrationExtn.class, "reg");
		registrations.createAlias("reg.egwStatus", "status");
		registrations.createAlias("reg.state", "state");
		registrations.addOrder(Order.asc("reg.id"));
		registrations.createCriteria("inspectionSet", "inspect",
				CriteriaSpecification.LEFT_JOIN);
		
		// If inspection is null, then only fetch
		registrations.add(Restrictions.isNull("inspect.id"));

		// Selected "registered" status record.
		DetachedCriteria detachedCriteriaStatus = DetachedCriteria
				.forClass(EgwStatus.class);
		detachedCriteriaStatus.add(Restrictions.eq("moduletype",
				BpaConstants.NEWBPAREGISTRATIONMODULE));
		detachedCriteriaStatus.add(Restrictions.in("code",
				new String[] { BpaConstants.APPLICATIONREGISTERED }));
		detachedCriteriaStatus.setProjection(Projections.property("id"));
		//The record should be in "AE or AEE" inbox
		registrations.add(Restrictions.eq("state.value",
				BpaConstants.WORKFLOWSTATUSFORWARDEDTOAEORAEE));
		registrations.add(Property.forName("status.id").in(
				detachedCriteriaStatus));
		registrations.add(Property.forName("state.createdDate").le(
				getCurrentDateWithOutTiming()));
		registrations.setMaxResults(300);
		LOGGER.debug("....Inspection query..." + registrations);
		return registrations.list();
	}
	public List<RegnOfficialActionsExtn> getOfficialActionsByRegId(Long registrationId) {
		List<RegnOfficialActionsExtn> regnOfficialActionsList=new ArrayList<RegnOfficialActionsExtn>();
		regnOfficialActionsList=regnOfficialActionsExtnService.findAllBy(" from RegnOfficialActionsExtn where registration.id=?", registrationId);
		return regnOfficialActionsList;
	}
	

	@SuppressWarnings("unchecked")
	public Integer printDocumentHistoryForm(RegistrationExtn registration,
			Map<String, Object> session, String printMode ,Boolean enableDocketSheetForView) {
		Integer reportId = null;
		if (registration != null) {
			ReportRequest reportInput = null;
			Map reportParams = new HashMap<String, Object>();
			List<DocumentHistoryExtn> DocHistoryListForPrint = new ArrayList<DocumentHistoryExtn>();
			DocHistoryListForPrint = bpaCommonExtnService.getAllDocumentHistoryList(registration);
			if (!DocHistoryListForPrint.isEmpty()) {
				DocumentHistoryExtn DocHistoryObj = null;
				//if (printMode != null && ) {
					for (DocumentHistoryExtn docExtn : DocHistoryListForPrint) {
						if (!printMode.equals("OfficialPrint")) {
							Boolean surveyorrrole=bpaCommonExtnService.ShowUserROles(docExtn.getCreatedUser().getRoles(),BpaConstants.PORTALUSERSURVEYORROLE,"");
							if(surveyorrrole.equals(true)) {
								DocHistoryObj = docExtn;
								reportParams.put("headerForReport","Surveyor Document History Sheet");
								break;
							}
						}
							else {
								Boolean arorArrrole=bpaCommonExtnService.ShowUserROles(docExtn.getCreatedUser().getRoles(),BpaConstants.BPAAEROLE,BpaConstants.BPAAEEROLE);
								if (printMode.equals("OfficialPrint") && arorArrrole.equals(true)) {
									DocHistoryObj = docExtn;
									reportParams.put("headerForReport",
											"AE/AEE Document History Sheet");
								}
							
					}
			
				}
				if (DocHistoryObj != null) {
					List<DocumentHistoryExtnDetails> DocumentHistoryExtndetails = new ArrayList<DocumentHistoryExtnDetails>(DocHistoryObj.getDocumentHistoryDetailSet());
					if(registration!=null && registration.getServiceType()!=null && registration.getServiceType().getCode()!=null && 
							(registration.getServiceType().getCode().equals(BpaConstants.NEWBUILDINGONVACANTPLOTCODE)
							||registration.getServiceType().getCode().equals(BpaConstants.DEMOLITIONRECONSTRUCTIONCODE)
							||registration.getServiceType().getCode().equals(BpaConstants.ADDITIONALCONSTRUCTIONCODE)
							||registration.getServiceType().getCode().equals(BpaConstants.SUBDIVISIONOFLANDCODE)
							||registration.getServiceType().getCode().equals(BpaConstants.LAYOUTAPPPROVALCODE))){
						reportParams.put("documenthistory", DocHistoryObj);
					}
					else
					{
						reportParams.put("documenthistory",	new DocumentHistoryExtn());						
					}
					reportParams.put("docementaryHistoryList",DocumentHistoryExtndetails);
					
				} else {
					reportParams.put("documenthistory",
							new DocumentHistoryExtn());
					reportParams.put("docementaryHistoryList",
							new ArrayList<DocumentHistoryExtnDetails>());
				}
			} else {
				reportParams.put("documenthistory", new DocumentHistoryExtn());
				reportParams.put("docementaryHistoryList",
						new ArrayList<DocumentHistoryExtnDetails>());
			}
			

			reportInput = new ReportRequest(BpaConstants.DOCUMENTHISTORYPRINT,
					registration, reportParams);
			if (reportInput != null)
				reportId = ReportViewerUtil.addReportToSession(
						reportService.createReport(reportInput), session);
			if(enableDocketSheetForView!=null && enableDocketSheetForView.equals(Boolean.TRUE)){
			if(printMode!="" && printMode!=null && !printMode.equalsIgnoreCase("OfficialPrint")) 
				createRegnOfficialActions(registration,BpaConstants.VIEWED_SURVEYOR_DOCDETAILS);
			else
				createRegnOfficialActions(registration,BpaConstants.VIEWED_AE_AEE_DOCDETAILS);
		}
		}
		return reportId;
	}
	public RegistrationExtn getRegistrationByPassingServiceReqNumber(String requestNum) {
	
		Criteria registrationObjCriteria =getSession().createCriteria(RegistrationExtn.class, "register");
		registrationObjCriteria.add(Restrictions.ilike("request_number", requestNum));
		return (RegistrationExtn) registrationObjCriteria.uniqueResult();
	}

	public Date getCurrentDateWithOutTiming() {
		GregorianCalendar cal =  new GregorianCalendar();
		cal.setTime(new Date());
		// cal.add( Calendar.DAY_OF_YEAR, -2);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		return cal.getTime();
	}
	
	public PersistenceService getPersistenceService() {
		return persistenceService;
	}
	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}
	
	public BpaNumberGenerationExtnService getBpaNumberGenerationExtnService() {
		return bpaNumberGenerationExtnService;
	}
	public void setBpaNumberGenerationExtnService(
			BpaNumberGenerationExtnService bpaNumberGenerationService) {
		this.bpaNumberGenerationExtnService = bpaNumberGenerationService;
	}
	
		public AutoDcrExtnService getAutoDcrExtnService() {
			return autoDcrExtnService;
		}
		public void setAutoDcrExtnService(AutoDcrExtnService autoDcrService) {
			this.autoDcrExtnService = autoDcrService;
		}
		/*public EisManager getEisMgr() {
			return eisMgr;
		}
		public void setEisMgr(EisManager eisMgr) {
			this.eisMgr = eisMgr;
		}
*/
		
		public void setReportService(ReportService reportService) {
			this.reportService = reportService;
		}
		public FeeExtnService getFeeExtnService() {
			return feeExtnService;
		}
		public void setFeeExtnService(FeeExtnService feeService) {
			this.feeExtnService = feeService;
		}
		public BpaCommonExtnService getBpaCommonExtnService() {
			return bpaCommonExtnService;
		}
		public void setBpaCommonExtnService(BpaCommonExtnService bpaCommonService) {
			this.bpaCommonExtnService = bpaCommonService;
		}
		public EgwStatus getOldStatus() {
			return oldStatus;
		}
		public void setOldStatus(EgwStatus oldStatus) {
			this.oldStatus = oldStatus;
		}
		
		public BpaPimsInternalExtnServiceFactory getBpaPimsExtnFactory() {
			return bpaPimsExtnFactory;
		}
		public void setBpaPimsExtnFactory(BpaPimsInternalExtnServiceFactory bpaPimsFactory) {
			this.bpaPimsExtnFactory = bpaPimsFactory;
		}
		//Pionix TODO:commenting as of now der is no WorkflowService
		/*public WorkflowService<RegistrationExtn> getRegistrationWorkflowExtnService() {
			return registrationWorkflowExtnService;
		}
		public void setRegistrationWorkflowExtnService(
				WorkflowService<RegistrationExtn> registrationWorkflowService) {
			this.registrationWorkflowExtnService = registrationWorkflowService;
		}*/
	
		public Long getApproverId() {
			return approverId;
		}
		public void setApproverId(Long approverId) {
			this.approverId = approverId;
		}
		public String getAdditionalRule() {
			return additionalRule;
		}
		public void setAdditionalRule(String additionalRule) {
			this.additionalRule = additionalRule;
		}
		
		public RegnStatusDetailExtnService getRegnStatusDetExtnService() {
			return regnStatusDetExtnService;
		}
		public void setRegnStatusDetExtnService(RegnStatusDetailExtnService regnStatusDetService) {
			this.regnStatusDetExtnService = regnStatusDetService;
		}
		public PersistenceService<RegnOfficialActionsExtn, Long> getRegnOfficialActionsExtnService() {
			return regnOfficialActionsExtnService;
		}
		public void setRegnOfficialActionsExtnService(
				PersistenceService<RegnOfficialActionsExtn, Long> regnOfficialActionsExtnService) {
			this.regnOfficialActionsExtnService = regnOfficialActionsExtnService;
		}
		
		
}