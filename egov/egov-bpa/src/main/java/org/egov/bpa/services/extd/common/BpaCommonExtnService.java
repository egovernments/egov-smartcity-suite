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

import com.opensymphony.xwork2.ActionSupport;
import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;
import org.egov.bpa.constants.BpaConstants;
import org.egov.bpa.models.extd.CMDALetterToPartyExtn;
import org.egov.bpa.models.extd.InspectionExtn;
import org.egov.bpa.models.extd.RegistrationExtn;
import org.egov.bpa.models.extd.RegistrationFeeDetailExtn;
import org.egov.bpa.models.extd.RegistrationFeeExtn;
import org.egov.bpa.models.extd.RegnApprovalInformationExtn;
import org.egov.bpa.models.extd.RegnStatusDetailsExtn;
import org.egov.bpa.models.extd.ReportFeesDetailsExtn;
import org.egov.bpa.models.extd.masters.BpaFeeExtn;
import org.egov.bpa.models.extd.masters.BuildingCategoryExtn;
import org.egov.bpa.models.extd.masters.ChangeOfUsageExtn;
import org.egov.bpa.models.extd.masters.DocumentHistoryExtn;
import org.egov.bpa.models.extd.masters.DocumentHistoryExtnDetails;
import org.egov.bpa.models.extd.masters.LandBuildingTypesExtn;
import org.egov.bpa.models.extd.masters.LpReasonExtn;
import org.egov.bpa.models.extd.masters.ServiceTypeExtn;
import org.egov.bpa.models.extd.masters.StormWaterDrainExtn;
import org.egov.bpa.models.extd.masters.VillageNameExtn;
import org.egov.bpa.services.extd.inspection.InspectionExtnService;
import org.egov.collection.integration.models.BillReceiptInfo;
import org.egov.collection.integration.services.CollectionIntegrationServiceImpl;
import org.egov.commons.Bank;
import org.egov.commons.CFinancialYear;
import org.egov.commons.EgwSatuschange;
import org.egov.commons.EgwStatus;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.demand.dao.DemandGenericDao;
import org.egov.demand.dao.DemandGenericHibDao;
import org.egov.demand.model.BillReceipt;
import org.egov.demand.model.EgDemandDetails;
import org.egov.eis.service.EisCommonService;
import org.egov.eis.service.PersonalInformationService;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.entity.HierarchyType;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.BoundaryTypeService;
import org.egov.infra.admin.master.service.HierarchyTypeService;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infra.workflow.entity.State;
import org.egov.infstr.config.dao.AppConfigValuesDAO;
import org.egov.infstr.mail.Email;
import org.egov.infstr.mail.Email.Builder;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.DateUtils;
import org.egov.infstr.utils.StringUtils;
import org.egov.infstr.workflow.WorkFlowMatrix;
import org.egov.pims.commons.DeptDesig;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EisUtilService;
import org.egov.pims.service.EmployeeServiceImpl;
import org.egov.pims.utils.EisManagersUtill;
import org.egov.portal.entity.Citizen;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/*import org.egov.infstr.workflow.WorkflowService;
import org.egov.infstr.workflow.inbox.WorkFlowItemsService;*/
/*import org.egov.lib.rjbac.user.UserRole;*/
/*import org.egov.pims.commons.service.EisCommonsManager;
import org.egov.pims.empLeave.dao.HolidaysUlbDAO;
import org.egov.pims.empLeave.dao.LeaveDAOFactory;
import org.egov.pims.empLeave.model.CalendarYear;
import org.egov.pims.empLeave.model.SaturdayHoliday;
import org.egov.pims.empLeave.model.SecondSaturdayHoliday;
import org.egov.pims.service.EisManager;*/
/*import org.egov.portal.surveyor.model.Surveyor;
import org.egov.portal.surveyor.model.SurveyorDetail;*/
/*import org.egov.demand.dao.DemandGenericDao;
import org.egov.demand.dao.DemandGenericHibDao;
import org.egov.demand.model.BillReceipt;
import org.egov.demand.model.EgDemandDetails;
import org.egov.dms.services.FileManagementService;
import org.egov.erpcollection.integration.models.BillReceiptInfo;
import org.egov.erpcollection.integration.services.CollectionIntegrationServiceImpl;*/
/*import org.egov.lib.admbndry.HeirarchyType;*/
@Transactional(readOnly = true)
@SuppressWarnings("unchecked")

public class BpaCommonExtnService extends ActionSupport  {
	
	@Autowired
	private HierarchyTypeService hierarchyTypeService;
	
	private static final String hierarchyTypeName 		= "LOCATION";
	private static final Logger LOGGER					= Logger.getLogger(BpaCommonExtnService.class);
	@Autowired
	private BoundaryTypeService boundaryTypeService;
	@Autowired
	private BoundaryService boundaryService;
	private PersistenceService persistenceService;
	private PersistenceService regnPersistenceExtnService;
	private EisUtilService eisService;
	private EmployeeServiceImpl employeeServiceImpl; 
	private InspectionExtnService inspectionExtnService;
	private PersistenceService<EgwSatuschange, Integer> statusChangeService;
	private FeeExtnService feeExtnService;
	//	private FileManagementService fileManagementService; 
	//private EisManager eisManager;//phionix TODO: commenting der is workflowservice
	/*private WorkflowService <RegistrationExtn> registrationWorkflowExtnService;
	private WorkflowService <RegistrationFeeExtn>registrationFeeWorkflowExtnService;*/
	private CollectionIntegrationServiceImpl collectionIntegrationService;
  private EisCommonService eisCommonService;
  private PersonalInformationService personalInformationService;
    private List<Bank> listOfBanks = new ArrayList<Bank>();
    private @Autowired AppConfigValuesDAO appConfigValuesDAO;
	SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());	
	SimpleDateFormat sdf1 =new SimpleDateFormat("yyyy-M-d",Locale.getDefault());	
	private static Configuration config ;	
	private HashMap<String,String> email_smsAdditionalParams=new HashMap<String,String>(0);
	public static final String FEE_PAYMENT_PDF = "feePaymentPdf";
	private ReportService reportService;
	private BpaCitizenPortalExtnService bpaCitizenPortalExtnService;
	private BpaSurvayorPortalExtnService bpaSurvayorPortalExtnService; 
	private static String OBJECT_REG_EXTN="RegistrationExtn";
	
	
	public BpaCitizenPortalExtnService getBpaCitizenPortalExtnService() {
		return bpaCitizenPortalExtnService;
	}

	public void setBpaCitizenPortalExtnService(
			BpaCitizenPortalExtnService bpaCitizenPortalService) {
		this.bpaCitizenPortalExtnService = bpaCitizenPortalService;
	}

	public ReportService getReportService() {
		return reportService;
	}

	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	public List<Boundary> populateArea(Long wardId)
	{
		HierarchyType hType = null;
		List<Boundary> areaList=Collections.EMPTY_LIST;
		try{
			hType = hierarchyTypeService.getHierarchyTypeByName(hierarchyTypeName);
		}catch(Exception e){
			LOGGER.error("Error while loading areas - areas." + e.getMessage());
			throw new EGOVRuntimeException("Unable to load areas information",e);
		}
		BoundaryType childBoundaryType =boundaryTypeService.getBoundaryTypeByNameAndHierarchyType("Area", hType);
		Boundary parentBoundary = boundaryService.getBoundaryById((long)wardId.intValue());
		if(wardId!=-1){
			
		}
			//areaList =new ArrayList<Boundary>(boundaryDAO.getCrossHeirarchyBoundary(parentBoundary,parentBoundary.getBoundaryType(),childBoundaryType));
		//phionix TODO: no getCrossHeirarchyBoundary method in boundaryDAO
		
		return areaList;
	}

	public String getAppconfigValueResult(String module,String key,String defaultValue)
	{
		return appConfigValuesDAO.getAppConfigValue(module, key, defaultValue);

	}
	
	public List<AppConfigValues> getAppConfigValue(String moduleName,String key){
		return appConfigValuesDAO.getConfigValuesByModuleAndKey(moduleName, key);
	}

	
	/** for search */
	public String getValidActionDropdowns(String arguments, RegistrationExtn registration) {
		
		LOGGER.debug("BpaCommonService ||Started getValidActionDropdowns method");
		String message="noAuthority";
		if(BpaConstants.VIEWAPPLICATION.equals(arguments)){
			 message="viewApplication";
		}
		else if(BpaConstants.VIEWADMISSIONFEERECEIPT.equals(arguments)){
			message="viewAdmissionReceipt";
		}
		else if(BpaConstants.VIEWBUILDINGPERMITORDER.equals(arguments)){
			message="viewBPO";
		}
		else if(BpaConstants.VIEWCHALLANNOTICE.equals(arguments)){
			message="viewChallanNotice";
		}
		else if(BpaConstants.VIEWCHALLANRECEIPT.equals(arguments)){
			message="viewChallanReceipt";
		}
		else if(BpaConstants.VIEWINSPECTIONDETAILS.equals(arguments)){
			message="viewInspectionDetails";
		}
		else if(BpaConstants.VIEWPLANINGPERMITORDER.equals(arguments)){
			message="viewPPO";
		}
		else if(BpaConstants.VIEWREJECTIONORDER.equals(arguments)){
			if(registration.getEgwStatus().getCode().equals(BpaConstants.REJECTIONAPPROVED) || 
					registration.getEgwStatus().getCode().equals(BpaConstants.REJECTORDERPREPARED) ||
					registration.getEgwStatus().getCode().equals(BpaConstants.REJECTORDERISSUED)){
				
			message="viewRejectionOrder";
			
			}
		}
			else if(BpaConstants.ORDERISSUED.equals(arguments)){
				message="viewOI";
			
		}
		else if(BpaConstants.VIEWWORKFLOWHISTORY.equals(arguments)){
			message="viewWorkflow";
		}else if(BpaConstants.COLLECTFEE.equals(arguments)){
			BigDecimal finalFeeTobePaid= isFeeCollectionPending(registration);
			if(!finalFeeTobePaid.equals(BigDecimal.ZERO)){
				 message="collectFee";
			}else{
				message="amountIsZero";
			}
			
		}
		else if(BpaConstants.ADDINSPECTIONDETAILS.equals(arguments)){
			 message="addInspectionDetails";
		}
		else if(BpaConstants.ADDSITEINSPECTIONDETAILS.equals(arguments)){
			 message="addSiteInspection";
		}
		
		else if(BpaConstants.VIEWLETTERTOPARTY.equals(arguments)){
			 message="viewLetterToParty";
		}
		else if(BpaConstants.LETTERTOPARTYREPLY.equals(arguments)) {
			message="addLPReply";
		}
		else if(BpaConstants.VIEWFEETOBEPAID.equals(arguments)) {
			
			message="viewFeeCollection";
			
		}else if(BpaConstants.PRINTEXTERNALFEEDETAILS.equals(arguments)) {
			
			message="printExternalFee";
			
		}else if(BpaConstants.ADDREVISEDFEE.equals(arguments)) {
			
			message="addRevisedFee";
			
		}
		
		LOGGER.debug("BpaCommonService ||Ended getValidActionDropdowns method");
		return message;
	}
	
	public  String getUsertName(Integer id)
	{
		String name=null;		
		User user = getUserObjForPosition(id);
			if(user!=null)
				name=user.getUsername();
		return name ;
	}

	public User getUserObjForPosition(Integer id) {
		User user=eisService.getUserForPosition((long)Long.parseLong(id.toString()),DateUtils.today());
		return user;
		//phionix TODO: Intiger id into Long id
	}
	
	public User getUserbyId(Long userId) {
		
		return (User)persistenceService.find("from User where id=?",userId);
 	}
	/*
	 * API to get User Roles For LoggedIn user
	 */
	 public String getUserRolesForLoggedInUser(){
		 
		 String userRole="";
		 User user = getUserbyId((EgovThreadLocals.getUserId()));
			if (user != null) {
			/*	for (UserRole role : user.getUserRoles()) {
					if(role!=null){
					userRole=userRole+","+role.getRole().getRoleName().toString();
					}
				}*/
				//phionix TODO: UserRole Deleted in EGi ..
			}
			
			return userRole;
			}
	
	public List<String> getRoleNamesByPassingUserId(Long currUserid) {
		User user = getUserbyId(currUserid); //(User)persistenceService.find("from User where id=?",currUserid);
		List<String> roleList = new ArrayList<String>();
		for(Role role : user.getRoles())
			roleList.add(role.getName());
		return roleList;
	}
	/*public Surveyor getSurveyour(Long Id)
	{
		Surveyor surveyorObj=(Surveyor)persistenceService.find("from Surveyor where isEnabled=true and id=?",Id);
		return surveyorObj;
	}
	
	public SurveyorDetail getSurveyourDetail(String code, Long serveyorId)
 	{
		SurveyorDetail surveyourDetailObj=null;
 	
		if(code!=null && code!=""){
 		surveyourDetailObj=((SurveyorDetail)persistenceService.find("from SurveyorDetail where surveyor.code=? and surveyor.isEnabled=true " ,code));
 	
		}
		else if(serveyorId!=null)
		{
			surveyourDetailObj=(SurveyorDetail)persistenceService.find("from SurveyorDetail where surveyor.id=? and status='Active' ",serveyorId);

		}
		return surveyourDetailObj;
 	}
	
	public List<SurveyorDetail> getSurveyorForSelectedZone(Integer adminBoundaryId)
	{
		List<SurveyorDetail>surveyorObj=new ArrayList<SurveyorDetail>();
		if(adminBoundaryId!=null){
		BoundaryImpl boundary=	getBoundaryObjById(adminBoundaryId);
		surveyorObj=persistenceService.findAllBy("from SurveyorDetail where boundary.id=? and status='Active' ",boundary.getParent().getId());
		}
		else{
			surveyorObj=persistenceService.findAllBy("from SurveyorDetail where status='Active' ");
		}
		return surveyorObj;
	}
	*/
	
	public void buildEmail(RegistrationExtn registration,String type,List<Map<String, Object>> finalAttachmentList,HashMap mail_sms_additionalparams) {
		email_smsAdditionalParams=mail_sms_additionalparams;
		buildEmail( registration, type, finalAttachmentList);
	}
	/**
	 * SMS and Email
	 */
	public void buildEmail(RegistrationExtn registration,String type,List<Map<String, Object>> finalAttachmentList) {
		LOGGER.debug("BpaCommonService ||Started buildEmail method");
			Citizen citizenDetails = registration.getOwner();
			String allowEmail=getAppconfigValueResult(BpaConstants.BPAMODULENAME,"EMAIL_NOTIFICATION_ALLOWED_BPA",null);
			LOGGER.debug("Inside buildEmail Method || allowEmail --> "+allowEmail + " || type -->"+type);
			if(null!=allowEmail && allowEmail!="" && allowEmail.equalsIgnoreCase("YES")){
				if(citizenDetails !=null && citizenDetails.getEmailId()!=null && StringUtils.isNotEmpty(citizenDetails.getEmailId())){
					try{
						String body="";
						String subject="";
						Boolean flag=Boolean.FALSE;
						
						if(type.equalsIgnoreCase(BpaConstants.SMSEMAILSAVE) || type.equalsIgnoreCase("save")){
							flag=Boolean.TRUE;
							body=getText("registration.email.body",new String[]{citizenDetails.getName(), 
									registration.getPlanSubmissionNum(),registration.getSecurityKey(),
									getText("reports.title.corporation_name")}); 
							subject=getText("registration.email.subject",new String[]{registration.getPlanSubmissionNum(),DateUtils.getFormattedDate(registration.getPlanSubmissionDate(),"dd/MM/yyyy")});
						}
						if(type.equalsIgnoreCase(BpaConstants.SMSEMAILSECURITYKEY)){
							flag=Boolean.TRUE;
							body=getText("registration.securityKey.email.body",new String[]{citizenDetails.getName(), 
									registration.getPlanSubmissionNum(),registration.getSecurityKey(),
									getText("reports.title.corporation_name")}); 
							subject=getText("registration.email.subject",new String[]{registration.getPlanSubmissionNum(),DateUtils.getFormattedDate(registration.getPlanSubmissionDate(),"dd/MM/yyyy")});
						}
						if(type.equalsIgnoreCase(BpaConstants.SMSEMAILAPPROVE)){ 
							flag=Boolean.TRUE;
							body=getText("registration.approve.email.body",new String[]{citizenDetails.getName(),
									registration.getPlanSubmissionNum(),DateUtils.getFormattedDate(registration.getPlanSubmissionDate(),"dd/MM/yyyy"),
									getText("reports.title.corporation_name")});
							
							subject=getText("registration.approve.email.subject",new String[]{DateUtils.getFormattedDate(registration.getPlanSubmissionDate(),"dd/MM/yyyy")});
							
						}
						if(type.equalsIgnoreCase("Reject")){
							flag=Boolean.TRUE;
							body=getText("registration.reject.email.body",new String[]{citizenDetails.getName(), 
									registration.getPlanSubmissionNum(),DateUtils.getFormattedDate(registration.getPlanSubmissionDate(),"dd/MM/yyyy"),
									getText("reports.title.corporation_name")}); 
							subject=getText("registration.reject.email.subject",new String[]{registration.getPlanSubmissionNum(),DateUtils.getFormattedDate(registration.getPlanSubmissionDate(),"dd/MM/yyyy")});
						}
						if(type.equalsIgnoreCase(BpaConstants.SMSEMAILINSPECTION)){
							flag=Boolean.TRUE;
							List<InspectionExtn> inspectionList= inspectionExtnService.getInspectionListforRegistrationObject(registration);				
							if(!inspectionList.isEmpty() && inspectionList.size()==1){
								body=getText("registration.inspection.start.email.body",new String[]{citizenDetails.getName(), 
										registration.getPlanSubmissionNum(),DateUtils.getFormattedDate(inspectionList.get(0).getInspectionDate(),"dd/MM/yyyy"),
										getText("reports.title.corporation_name")}); 
								subject=getText("registration.inspection.start.email.subject",new String[]{DateUtils.getFormattedDate(inspectionList.get(0).getInspectionDate(),"dd/MM/yyyy")});
						
							}
							else if(!inspectionList.isEmpty() && inspectionList.size()>1){
								body=getText("registration.inspection.postpone.email.body",new String[]{citizenDetails.getName(), 
										registration.getPlanSubmissionNum(),DateUtils.getFormattedDate(inspectionList.get(0).getInspectionDate(),"dd/MM/yyyy"),
										getText("reports.title.corporation_name")}); 
								subject=getText("registration.inspection.postpone.email.subject",new String[]{DateUtils.getFormattedDate(inspectionList.get(0).getInspectionDate(),"dd/MM/yyyy")});
								
							}
						}
						else if(type.equalsIgnoreCase(BpaConstants.SMSEMAILLP)){
							flag=Boolean.TRUE;
							body=getText("registration.lettertoparty.email.body",new String[]{citizenDetails.getName(), 
									registration.getPlanSubmissionNum(),
									getText("reports.title.corporation_name")}); 
							subject=getText("registration.lettertoparty.email.subject",new String[]{registration.getPlanSubmissionNum()});
						}
						else if(type.equalsIgnoreCase("OrderPrepared")) {
							flag=Boolean.TRUE;
							body=getText("registration.orderprepared.update.email.body",new String[]{citizenDetails.getName(), 
									registration.getPlanSubmissionNum(), registration.getAdminboundaryid().getParent().getName(), 
									getText("reports.title.corporation_name")}); 
							subject=getText("registration.orderprepared.update.email.subject",new String[]{registration.getPlanSubmissionNum()});
						}
						else if(type.equalsIgnoreCase("OrderIssued")) {
							flag=Boolean.TRUE;
							body=getText("registration.orderissued.update.email.body",new String[]{citizenDetails.getName(), 
									registration.getPlanSubmissionNum(), 
									getText("reports.title.corporation_name")}); 
							subject=getText("registration.orderissued.update.email.subject",new String[]{registration.getPlanSubmissionNum()});
						}
						else if(type.equalsIgnoreCase("RejectOrderPrepared")) {
							flag=Boolean.TRUE;
							body=getText("registration.rejectorderprepared.update.email.body",new String[]{citizenDetails.getName(), 
									registration.getPlanSubmissionNum(), getText("reports.title.corporation_name")}); 
							subject=getText("registration.rejectorderprepared.update.email.subject",new String[]{registration.getPlanSubmissionNum()});
						}
						else if(type.equalsIgnoreCase("RejectOrderIssued")) {
							flag=Boolean.TRUE;
							body=getText("registration.rejectorderissued.update.email.body",new String[]{citizenDetails.getName(), 
									registration.getPlanSubmissionNum(), getText("reports.title.corporation_name")}); 
							subject=getText("registration.rejectorderissued.update.email.subject",new String[]{registration.getPlanSubmissionNum()});
						}
						else if(type.equalsIgnoreCase("FeePaid")) {
							flag=Boolean.TRUE;
							body=getText("registration.feepaid.email.body",new String[]{citizenDetails.getName(), 
									registration.getPlanSubmissionNum(), getText("reports.title.corporation_name")}); 
							subject=getText("registration.feepaid.email.subject",new String[]{registration.getPlanSubmissionNum()});
						}
						else if(type.equalsIgnoreCase(BpaConstants.SMSCHALLANNOTICESENTDATE)) {
							flag=Boolean.TRUE;
							body=getText("registration.challNoticeSentDate.email.body",new String[]{citizenDetails.getName(), 
									registration.getPlanSubmissionNum(), getText("reports.title.corporation_name")}); 
							subject=getText("registration.challNoticeSentDate.email.subject",new String[]{registration.getPlanSubmissionNum()});
						}
						else if(type.equalsIgnoreCase(BpaConstants.UPDATESIGNATURE)) {
							flag=Boolean.TRUE;
							body=getText("registration.updateSignature.email.body",new String[]{citizenDetails.getName(), 
									registration.getPlanSubmissionNum(), getText("reports.title.corporation_name")}); 
							subject=getText("registration.updateSignature.email.subject",new String[]{registration.getPlanSubmissionNum()});
						}
						else if(type.equalsIgnoreCase(BpaConstants.SMSEMAILREVISEDFEEAPPROVE)){ 
							flag=Boolean.TRUE;
							body=getText("registration.revisedfeeapprove.email.body",new String[]{citizenDetails.getName(),
									registration.getPlanSubmissionNum(),DateUtils.getFormattedDate(registration.getPlanSubmissionDate(),"dd/MM/yyyy"),
									getText("reports.title.corporation_name"),email_smsAdditionalParams.get("revisedchallanNo"),email_smsAdditionalParams.get("revisedFeeApprovedDate")});
							
							subject=getText("registration.revisedfeeapprove.email.subject",new String[]{DateUtils.getFormattedDate(registration.getPlanSubmissionDate(),"dd/MM/yyyy")});
							
						}
						if(flag){
	
							sendEmail(registration,body,subject,finalAttachmentList);
						}
						
					}
					catch(EGOVRuntimeException egovExp){ 
						//emailMsg = getText("registration.email.failure.msg1");
					}
				}else{
					//emailMsg= getText("registration.email.failure.msg2");
				}
			}
			LOGGER.debug("BpaCommonService ||Ended buildEmail method");
			}
	
	public Boolean isEmailSent(RegistrationExtn registration){
		String allowEmail=getAppconfigValueResult(BpaConstants.BPAMODULENAME,"EMAIL_NOTIFICATION_ALLOWED_BPA",null);
		if(null!=allowEmail && allowEmail!="" && allowEmail.equalsIgnoreCase("YES")){	
			if(null!=registration && registration.getOwner() !=null && registration.getOwner().getEmailId()!=null 
					&& StringUtils.isNotEmpty(registration.getOwner().getEmailId())){
				
				return true;
			}
		}
		return false;
	}
	//phionix TODO:  EgDemandDetails depaends commenting os of now
	public Map<String, List<BpaFeeExtn>> getGroupWiseFeeListByPassingRegistration(RegistrationExtn registrationObj,Long registrationFeeId){
			 
		 Map<String,List<BpaFeeExtn>>  registrationFeesesByFeeGroup= new HashMap<String,List<BpaFeeExtn>>();
		
		//Assumption: Demand will be generated on fee creation.Demand will be used for legacy records, where fee details are not saved in intermediate table.
		if(registrationObj!=null && registrationObj.getEgDemand()!=null&&registrationObj.getEgDemand().getEgDemandDetails()!=null){
			
			List<BpaFeeExtn> santionFeeList=feeExtnService.getAllSanctionedFeesbyServiceType(registrationObj.getServiceType().getId());
				
			HashMap<String,BigDecimal> feecodeamountmap=new HashMap<String,BigDecimal>();
			//HashMap<String,Long> feecodedemanddetailsIdmap=new HashMap<String,Long>();
			
			//Getting fee amounts from demand to  set into the sanctionfees  
			for(EgDemandDetails demandDetails:registrationObj.getEgDemand().getEgDemandDetails()){
				feecodeamountmap.put(demandDetails.getEgDemandReason().getEgDemandReasonMaster().getCode(), demandDetails.getAmount());
			}
			//phionix TODO: EgDemandDetails depaendency
			RegistrationFeeExtn latesetregistrationFeeObj=null;
			
			//Getting fee amounts from registrationdfeedetails to  set into the sanctionfees  
			if(registrationFeeId!=null)
				 latesetregistrationFeeObj= getRegistrationFeeById(registrationFeeId);
			else
				 latesetregistrationFeeObj= getLatestApprovedRegistrationFee(registrationObj);
			
			if(latesetregistrationFeeObj!=null){
				for(RegistrationFeeDetailExtn regFeeDtl:latesetregistrationFeeObj.getRegistrationFeeDetailsSet()){
					
					if(!feecodeamountmap.containsKey((regFeeDtl.getBpaFee().getFeeCode()))){ // if not present in feecodeamountmap
						feecodeamountmap.put(regFeeDtl.getBpaFee().getFeeCode(), regFeeDtl.getAmount());
					}
					else if(feecodeamountmap.containsKey((regFeeDtl.getBpaFee().getFeeCode()))){ // if not present in feecodeamountmap
						
						if(regFeeDtl.getAmount().compareTo(BigDecimal.ZERO) >0)
							feecodeamountmap.put(regFeeDtl.getBpaFee().getFeeCode(), regFeeDtl.getAmount());
					}
					
				}
				}
			
			for(BpaFeeExtn fees:santionFeeList){
				// removing COC,CMDA,Welafare group wise fees List: as per new requirement we are calculating all fees under one dd(under COC group)
				if(feecodeamountmap.get(fees.getFeeCode())!=null && feecodeamountmap.get(fees.getFeeCode()).compareTo(BigDecimal.ZERO)>0)	{
				fees.setFeeAmount(feecodeamountmap.get(fees.getFeeCode()));
				addFeeListToMap(registrationFeesesByFeeGroup, fees,BpaConstants.COCFEE);
				}
				if(fees.getFeeGroup().equalsIgnoreCase(BpaConstants.COCFEE)){
					
					if(feecodeamountmap.get(fees.getFeeCode())!=null && feecodeamountmap.get(fees.getFeeCode()).compareTo(BigDecimal.ZERO)>0)	{
						fees.setFeeAmount(feecodeamountmap.get(fees.getFeeCode()));
						addFeeListToMap(registrationFeesesByFeeGroup, fees,BpaConstants.COCFEE);
					}	
								
				}else if(fees.getFeeGroup().equalsIgnoreCase(BpaConstants.CMDAFEE)){
					if(feecodeamountmap.get(fees.getFeeCode())!=null && feecodeamountmap.get(fees.getFeeCode()).compareTo(BigDecimal.ZERO)>0)	{
						fees.setFeeAmount(feecodeamountmap.get(fees.getFeeCode()));
					addFeeListToMap(registrationFeesesByFeeGroup, fees,BpaConstants.CMDAFEE);
					}
				}else if(fees.getFeeGroup().equalsIgnoreCase(BpaConstants.MWGWFFEE)){
					if(feecodeamountmap.get(fees.getFeeCode())!=null && feecodeamountmap.get(fees.getFeeCode()).compareTo(BigDecimal.ZERO)>0)	{
						fees.setFeeAmount(feecodeamountmap.get(fees.getFeeCode()));
					addFeeListToMap(registrationFeesesByFeeGroup, fees,BpaConstants.MWGWFFEE);
					}
				}
			} 
		
		}
		
		return registrationFeesesByFeeGroup;
	}

	private void addFeeListToMap(
			Map<String, List<BpaFeeExtn>> registrationFeesesByFeeGroup, BpaFeeExtn fees,String feeType) {
		if (registrationFeesesByFeeGroup.get(feeType) == null) {
			registrationFeesesByFeeGroup.put(feeType, new ArrayList<BpaFeeExtn>());
		}
		registrationFeesesByFeeGroup.get(feeType).add(fees);
	}
	
	
	
	public void buildSMS(RegistrationExtn registration,String type,HashMap mail_sms_additionalparams) {
		email_smsAdditionalParams=mail_sms_additionalparams;
		buildSMS( registration, type);
	}
	public void buildSMS(RegistrationExtn registration,String type) { 
		LOGGER.debug("BpaCommonService ||Started buildSMS method");
			String smsMsg = null;
			Boolean flag=Boolean.FALSE;
			//Owner citizenDetails = registration.getOwner();
			String allowSms=getAppconfigValueResult(BpaConstants.BPAMODULENAME,"SMS_NOTIFICATION_ALLOWED_BPA",null);
			String citizenOrOfficial=null;
			LOGGER.debug("allowSms "+allowSms +" || type "+type);
			LOGGER.debug(" registration number "+registration!=null?registration.getPlanSubmissionNum():null);	
			if(null!=allowSms && allowSms!="" && allowSms.equalsIgnoreCase("YES")){
				//if(citizenDetails !=null && citizenDetails.getMobilePhone()!=null && StringUtils.isNotEmpty(citizenDetails.getMobilePhone())){
				
				if(type.equalsIgnoreCase(BpaConstants.SMSEMAILSAVE)){
						flag=Boolean.TRUE;						
						smsMsg = getText("registration.sms.msg",new String[]{registration.getOwner().getName(),registration.getPlanSubmissionNum(),registration.getSecurityKey(),DateUtils.getFormattedDate(registration.getPlanSubmissionDate(),"dd/MM/yyyy")});
					}
					else if(type.equalsIgnoreCase("save")){
						flag=Boolean.TRUE;						
						smsMsg = getText("registration.sms.msg",new String[]{registration.getOwner().getName(),registration.getPlanSubmissionNum(),registration.getSecurityKey(),DateUtils.getFormattedDate(registration.getPlanSubmissionDate(),"dd/MM/yyyy")});
						if(isEmailSent(registration)){
							smsMsg=smsMsg+ getText("registration.sms.withpdfmsg",new String[]{"Admission fee receipt"});
									//" And PDF format of Challan is sent to your mail, please check for your reference";
							
						}
					}
					else if(type.equalsIgnoreCase(BpaConstants.SMSEMAILSECURITYKEY)){
						flag=Boolean.TRUE;
						
						smsMsg = getText("registration.securityKey.sms.msg",new String[]{registration.getOwner().getName(),registration.getPlanSubmissionNum(),registration.getSecurityKey(),DateUtils.getFormattedDate(registration.getPlanSubmissionDate(),"dd/MM/yyyy")});
					}
					else if(type.equalsIgnoreCase(BpaConstants.SMSEMAILAPPROVE)){
						flag=Boolean.TRUE;
						
						smsMsg = getText("registration.approve.sms.msg",new String[]{registration.getOwner().getName(),registration.getPlanSubmissionNum(),DateUtils.getFormattedDate(registration.getPlanSubmissionDate(),"dd/MM/yyyy")});
					}
					else if(type.equalsIgnoreCase("Reject")){
						flag=Boolean.TRUE;
						
						smsMsg = getText("registration.reject.sms.msg",new String[]{registration.getOwner().getName(),registration.getPlanSubmissionNum(),DateUtils.getFormattedDate(registration.getPlanSubmissionDate(),"dd/MM/yyyy"),"rejected"});
					}
					else if(type.equalsIgnoreCase(BpaConstants.SMSEMAILINSPECTION)){
						flag=Boolean.TRUE;
						
						List<InspectionExtn> inspectionList= inspectionExtnService.getInspectionListforRegistrationObject(registration);				
						if(!inspectionList.isEmpty() && inspectionList.size()==1)
							smsMsg = getText("registration.inspection.start.sms.msg",new String[]{registration.getOwner().getName(),registration.getPlanSubmissionNum(),DateUtils.getFormattedDate(inspectionList.get(0).getInspectionDate(),"dd/MM/yyyy")});
						else if(!inspectionList.isEmpty() && inspectionList.size()>1)
							smsMsg = getText("registration.inspection.postponed.sms.msg",new String[]{registration.getOwner().getName(),registration.getPlanSubmissionNum(),DateUtils.getFormattedDate(inspectionList.get(0).getInspectionDate(),"dd/MM/yyyy")});			
					}
					else if(type.equalsIgnoreCase(BpaConstants.SMSEMAILLP)){
						
						flag=Boolean.TRUE; 
						smsMsg = getText("registration.lettertoparty.sms.msg",new String[]{registration.getOwner().getName(),registration.getPlanSubmissionNum(),DateUtils.getFormattedDate(registration.getPlanSubmissionDate(),"dd/MM/yyyy"),"rejected"});	
						if(isEmailSent(registration)){
							smsMsg=smsMsg+ getText("registration.sms.withpdfmsg",new String[]{"Letter(LP)"});
							//smsMsg=smsMsg+" And PDF format of Letter(LP) is sent to your mail, please check for your reference";
						}
					}
					else if(type.equalsIgnoreCase("OrderPrepared")) {
						
						flag=Boolean.TRUE;
						smsMsg = getText("registration.orderprepared.update.sms.msg",new String[]{registration.getOwner().getName(),registration.getPlanSubmissionNum(),registration.getAdminboundaryid().getParent().getName()});
					}
					else if(type.equalsIgnoreCase("OrderIssued")) {
						
						flag=Boolean.TRUE;
						smsMsg = getText("registration.orderissued.update.sms.msg",new String[]{registration.getOwner().getName(),registration.getPlanSubmissionNum()});
					}
					else if(type.equalsIgnoreCase("RejectOrderPrepared")) {
						
						flag=Boolean.TRUE;
						smsMsg = getText("registration.rejectordprepared.update.sms.msg",new String[]{registration.getOwner().getName(),registration.getPlanSubmissionNum()});
					}
					else if(type.equalsIgnoreCase("RejectOrderIssued")) {
						
						smsMsg = getText("registration.rejectordissued.update.sms.msg",new String[]{registration.getOwner().getName(),registration.getPlanSubmissionNum()});
					}
					else if(type.equalsIgnoreCase("FeePaid")) {
						//phionix TODO: changed as per In CITIZEN table FirstNAme into NAme
						flag=Boolean.TRUE;
						smsMsg = getText("registration.feePaid.sms.msg",new String[]{registration.getOwner().getName(),registration.getPlanSubmissionNum()});
						if(isEmailSent(registration)){
							smsMsg=smsMsg+ getText("registration.sms.withpdfmsg",new String[]{"Challan receipt"});
							//smsMsg=smsMsg+" And PDF format of Challan is sent to your mail, please check for your reference";
						}
					}
					else if(type.equalsIgnoreCase(BpaConstants.SMSCHALLANNOTICESENTDATE)) {
						//phionix TODO: changed as per In CITIZEN table FirstNAme into NAme
						flag=Boolean.TRUE;
						smsMsg = getText("registration.challNoticeSentDate.sms.msg",new String[]{registration.getOwner().getName(),registration.getPlanSubmissionNum()});
						if(isEmailSent(registration)){
							smsMsg=smsMsg+ getText("registration.sms.withpdfmsg",new String[]{"Challan Notice (Fee Details)"});
						}
					}
					else if(type.equalsIgnoreCase(BpaConstants.SMSEMAILASSISTANTTOAEEONSAVE)) {
						flag=Boolean.TRUE;
						citizenOrOfficial="Official";
						smsMsg = getText("registration.assistantToAeeOnSave.sms.msg",new String[]{registration.getPlanSubmissionNum()});
					}
					else if(type.equalsIgnoreCase(BpaConstants.SMSEMAILASSISTANTTOAEEONLP)) {
						flag=Boolean.TRUE;
						citizenOrOfficial="Official";
						smsMsg = getText("registration.assistantToAeeOnLP.sms.msg",new String[]{registration.getPlanSubmissionNum()});
					}
					else if(type.equalsIgnoreCase(BpaConstants.SMSAEEONAUTOGENERATIONOFSITEINSPECTIONDATE)) {
						flag=Boolean.TRUE;
						citizenOrOfficial="Official";
						smsMsg = getText("autoGenSiteInspectionDate.ToAee.sms.msg",new String[]{registration.getPlanSubmissionNum()});
					}
					else if(type.equalsIgnoreCase(BpaConstants.SMSEMAILREVISEDFEEAPPROVE)){ 
						flag=Boolean.TRUE;			
						smsMsg=getText("registration.revisedfeeapprove.sms.msg",new String[]{registration.getPlanSubmissionNum(),email_smsAdditionalParams.get("revisedchallanNo")});
						
					}
				}
				if(flag){
					sendSMS(registration,smsMsg,citizenOrOfficial);
					//smsSuccessMsg = getText("registration.sms.success.msg",new String[]{registration.getOwner().getFirstName()});
					}
			//	}
				LOGGER.debug("BpaCommonService ||Ended buildSMS method");	
	}
	
	public void buildEmailWithMessage(RegistrationExtn registration, String type,
			String emailBodyMessage, String emailSubjectMessage,
			String additionalMessage) {
		String body = "";
		String subject = "";
		Boolean flag = Boolean.FALSE;

		Citizen citizenDetails = registration.getOwner();

		if (type.equalsIgnoreCase(BpaConstants.SMSEMAILINSPECTION)) {
			flag = Boolean.TRUE;
			body = MessageFormat.format(
					emailBodyMessage,
					new String[] { citizenDetails.getName(),//phionix TODO: changed as per In CITIZEN table FirstNAme into NAme
							registration.getPlanSubmissionNum(),
							additionalMessage, BpaConstants.CITYNAME });
			subject = MessageFormat.format(emailSubjectMessage,
					new String[] { additionalMessage });
		}

		if (flag) {

			sendEmail(registration, body, subject, null);
		}

	}
	
	
	public void buildSMS(RegistrationExtn registration, String type,
			String smsMessage, String additionalMessage) {
		String smsMsg = null;
		Boolean flag = Boolean.FALSE;
		String citizenOrOfficial = null;

		if (type.equalsIgnoreCase(BpaConstants.SMSAEEONAUTOGENERATIONOFSITEINSPECTIONDATE)) {
			flag = Boolean.TRUE;
			citizenOrOfficial = "Official";
			smsMsg = MessageFormat.format(smsMessage, new String[] {
					registration.getPlanSubmissionNum(), additionalMessage });
		} else if (type.equalsIgnoreCase(BpaConstants.SMSEMAILINSPECTION)) {
			flag = Boolean.TRUE;
			smsMsg = MessageFormat.format(smsMessage,
					new String[] {
							(registration.getOwner() != null ? registration
									.getOwner().getName() : " "),
							registration.getPlanSubmissionNum(),
							additionalMessage });
		}

		if (flag) {
			sendSMS(registration, smsMsg, citizenOrOfficial);
			// smsSuccessMsg = getText("registration.sms.success.msg",new
			// String[]{registration.getOwner().getFirstName()});
		}
	}
	
	/*public void buildSMS(Registration registration,String type, String message) {
		String smsMsg = null;
		Boolean flag=Boolean.FALSE;
		String citizenOrOfficial=null;
	
		String allowSms=getAppconfigValueResult(BpaConstants.BPAMODULENAME,"SMS_NOTIFICATION_ALLOWED_BPA",null);
		
		if(null!=allowSms && allowSms!="" && allowSms.equalsIgnoreCase("YES")){
			if(type.equalsIgnoreCase(BpaConstants.SMSAEEONAUTOGENERATIONOFSITEINSPECTIONDATE)) {
				flag=Boolean.TRUE;
				citizenOrOfficial="Official";
			 smsMsg = getText("autoGenSiteInspectionDate.ToAee.sms.msg",new String[]{registration.getPlanSubmissionNum(),message} );	 
			}
		}else if(type.equalsIgnoreCase(BpaConstants.SMSEMAILINSPECTION)){
			flag=Boolean.TRUE;
			smsMsg = getText("registration.inspection.start.sms.msg",new String[]{(registration.getOwner()!=null?registration.getOwner().getFirstName():" "),registration.getPlanSubmissionNum(),message});
		}
		
		if(flag){
			sendSMS(registration,smsMsg,citizenOrOfficial);
			//smsSuccessMsg = getText("registration.sms.success.msg",new String[]{registration.getOwner().getFirstName()});
			}
	}*/
	public void sendSMS(RegistrationExtn registration,String smsMsg,String citizenOrOfficial){
		LOGGER.debug("BpaCommonService ||Started sendSMS method");	
		String mobileNumber=null;
		String surveyormobileNumber=null;
		if(null!=citizenOrOfficial && citizenOrOfficial.equals("Official")){
			//phionix TODO: cganged ->.getState().getOwner()->.getState().getOwnerUser()

			if(null!=registration && null!=registration.getState() && null!=registration.getState().getOwnerUser() && null!=registration.getState().getOwnerUser().getId()){
				User user=getUserObjForPosition((int)Long.parseLong(registration.getState().getOwnerUser().getId().toString()));
				if(user!=null && user.getMobileNumber()!=null)
					mobileNumber=user.getMobileNumber();
			}			
		}
		else{	
			Citizen citizenDetails = registration.getOwner();
			mobileNumber=citizenDetails.getMobileNumber();
			// adding to send SMS to surveyor onle for servicetypes 01,03,06 and application statuses are Unconsidered Order Issued,Order Issued to Applicant
			 if (registration.getServiceType() != null&& registration.getServiceType().getCode() != null && 
					 		(registration.getServiceType().getCode().equals(BpaConstants.NEWBUILDINGONVACANTPLOTCODE)
								|| registration.getServiceType().getCode().equals(BpaConstants.DEMOLITIONRECONSTRUCTIONCODE) || 
								registration.getServiceType().getCode().equals(BpaConstants.ADDITIONALCONSTRUCTIONCODE)))
			 {
			if(registration.getEgwStatus()!=null && registration.getEgwStatus().getCode()!=null && registration.getEgwStatus().getCode().equals(BpaConstants.ORDERISSUEDTOAPPLICANT) 
					||registration.getEgwStatus().getCode().equals(BpaConstants.REJECTORDERISSUED) )
			{
				//phionix TODO: no Surveyor Object in portal
				//surveyormobileNumber=((registration.getSurveyorName()!=null && registration.getSurveyorName().getUserDetail()!=null)?registration.getSurveyorName().getUserDetail().getMobileNumber():"");
			}
			 }
		}
		LOGGER.debug("Sending to Mobile "+mobileNumber); 
		LOGGER.debug("smsMsg "+smsMsg);
		LOGGER.debug("citizenOrOfficial "+citizenOrOfficial);
		
		if(null!=mobileNumber && StringUtils.isNotEmpty(mobileNumber)){
			/*try{
		//	HTTPSMS.sendSMS(null, null,smsMsg,"91"+mobileNumber,BpaConstants.SMS_MOBILE_NUMBER); 
			if(null!=surveyormobileNumber && StringUtils.isNotEmpty(surveyormobileNumber))
			{
			new HTTPSMS();
			//HTTPSMS.sendSMS(null, null,smsMsg,"91"+surveyormobileNumber,BpaConstants.SMS_MOBILE_NUMBER);
			}
			}catch(EGOVException e)  // Bypassing Exception..
			{
				LOGGER.debug("Error occured in sending SMS "+mobileNumber);
			}*///Phionix TODO

		}
		LOGGER.debug("BpaCommonService ||Ended sendSMS method");	
		
	}
	 public List<DocumentHistoryExtn> getRegnDocumentHistoryObjectBySurveyorOrOtherOfficials(RegistrationExtn registrationId){
		 List<DocumentHistoryExtn>  docHistoryList=null;
			if(registrationId!=null){
				if( isUserMappedToSurveyorRole()){ 
					docHistoryList= persistenceService.findAllBy("from DocumentHistoryExtn where registrationId=? and createdUser.id=? order by id desc",registrationId,(EgovThreadLocals.getUserId()));
					return docHistoryList;
				}
				else{
					docHistoryList=  persistenceService.findAllBy("from DocumentHistoryExtn where registrationId=? order by id desc",registrationId);
					return docHistoryList;
			}
			}
			else{
			return null;
			}
			
		}
	 public Boolean ShowUserROles(Set<Role>userRoleSet,String aeRole,String aeeRole)
		{
			Boolean isaeOrAeeRole=Boolean.FALSE;
			for(Role role:userRoleSet)
			{
				/*if(role!=null && role.getRoleName()!=null && (role.getRoleName().equals(aeRole) || role.getRoleName().equals(aeeRole)))
				{
						isaeOrAeeRole=Boolean.TRUE;
						break;
				}*/
			}
			return isaeOrAeeRole;
		}
	//For registration Workflow remarks
		public String getPreviousUserCommentsFromWorkfow(RegistrationExtn registration)
		{
			String ApproverComments="";
			if(registration!=null){
			List<State>statesList=null;//registration.getHistory();
			ApproverComments = getApproverCommentForRenAndReVisedFee(
					ApproverComments, statesList);
			}
			return ApproverComments;
		}
		
	 /*
	  * For Revised Fee workflow Remarks
	  */
		public String getPreviousUserCommentsFromWorkfowForRevisedFee(RegistrationFeeExtn registrationFee)
		{
			String ApproverComments="";
			if(registrationFee!=null){
			List<State>statesList = null;//registrationFee.getHistory();
			ApproverComments = getApproverCommentForRenAndReVisedFee(
					ApproverComments, statesList);
			}
			return ApproverComments;
		}
		/*
		 * common method for Registartion and Revised Workflow to get previuous user Remarks. this API will return Posiname/User First NAme=ApproverComments
		 */
		public String getApproverCommentForRenAndReVisedFee(
				String ApproverComments,List<State> statesList) {
			for(State staObj:statesList)
			{
				StringBuffer commentedApproverRemarks=new StringBuffer();
				if(staObj!=null ){//staObj.getText1()!=null && !"".equals(staObj.getText1()) && 
				if(!staObj.getValue().equals("NEW")){
					if(staObj.getCreatedBy()!=null){
					Position	pos=	eisService.getPrimaryPositionForUser(staObj.getCreatedBy().getId(),staObj.getCreatedDate().toDate());
					User user=(User)staObj.getCreatedBy(); //casting to UserImpl because staObj.getCreatedBy() is User In State Object..   
					String userName=( pos == null ? "" :pos.getName())+" /"+(user == null ? "" :user.getName() + " " + (user.getName() == null ? "" : user.getName()));
//				commentedApproverRemarks=commentedApproverRemarks.append("\n").append(userName+ " == " + new StringBuffer(""));/staObj.getText1()!=null && !"".equals(staObj.getText1()) && 
				if(ApproverComments==null && "".equals(ApproverComments)){
					ApproverComments=commentedApproverRemarks.toString();
				}
				else if(ApproverComments!=null){
					ApproverComments=ApproverComments+commentedApproverRemarks.toString();
				}
				}
				}
				}
				
			}
			return ApproverComments;
		}
		
	public Boolean isUserMappedToSurveyorRole() {
		if(EgovThreadLocals.getUserId()!=null){
			User user =getUserbyId( (EgovThreadLocals.getUserId()));
			/*for(Role role : user.getRoles())
				if(role.getRoleName()!=null && role.getRoleName().equalsIgnoreCase(BpaConstants.PORTALUSERSURVEYORROLE))
				{
					return true;
				}*/
				
		}
		return false;
	}
	public void sendEmail(RegistrationExtn registration,String body, String emailSubject,List<Map<String, Object>> attachmentList)
	{
		LOGGER.debug("BpaCommonService ||started sendEmail method");	
		try{
			
				
		if(registration!=null && registration.getOwner()!=null &&  registration.getOwner().getEmailId()!=null && StringUtils.isNotEmpty(registration.getOwner().getEmailId())){
			
			Builder builder = new Builder(registration.getOwner().getEmailId(),body)
			.subject(emailSubject);
			
			if(attachmentList != null && (!attachmentList.isEmpty()) && attachmentList.size() != 0){
				Map<String, InputStream> attachmentFiles=new HashMap<String, InputStream>();
				Map<String, String> attachmentFileNames=new HashMap<String, String>();
				for(Map<String, Object> mailAttachment: attachmentList) {
					if(mailAttachment.get("feePayment")!=null) 
						attachmentFiles.put("feePayment", (InputStream) mailAttachment.get("feePayment"));
					if(mailAttachment.get("feePaymentFileName")!=null)
						attachmentFileNames.put("feePaymentFileName",  mailAttachment.get("feePaymentFileName").toString());
					
					if(mailAttachment.get("planPermitProvisional")!=null) 
						attachmentFiles.put("planPermitProvisional", (InputStream) mailAttachment.get("planPermitProvisional"));
					if(mailAttachment.get("planPermitProvisionalName")!=null)
						attachmentFileNames.put("planPermitProvisionalName",  mailAttachment.get("planPermitProvisionalName").toString());
					 
					if(mailAttachment.get("buildingPermitProvisional")!=null) 
						attachmentFiles.put("buildingPermitProvisional", (InputStream) mailAttachment.get("buildingPermitProvisional"));
					if(mailAttachment.get("buildingPermitProvisionalName")!=null)
						attachmentFileNames.put("buildingPermitProvisionalName",  mailAttachment.get("buildingPermitProvisionalName").toString());
					
					if(mailAttachment.get("letterToParty")!=null) 
						attachmentFiles.put("letterToParty", (InputStream) mailAttachment.get("letterToParty"));
				 	if(mailAttachment.get("letterToPartyFileName")!=null)
						attachmentFileNames.put("letterToPartyFileName",  mailAttachment.get("letterToPartyFileName").toString());
						
				}
				
				if(attachmentFiles.get("feePayment")!=null){
					builder.attachment( attachmentFiles.get("feePayment"), attachmentFileNames.get("feePaymentFileName"),"fee Payment PDF");   
				}
				if(attachmentFiles.get("planPermitProvisional")!=null){
					builder.attachment( attachmentFiles.get("planPermitProvisional"), attachmentFileNames.get("planPermitProvisionalName"),"plan permit PDF");   
				}
				if(attachmentFiles.get("buildingPermitProvisional")!=null){
					builder.attachment( attachmentFiles.get("buildingPermitProvisional"), attachmentFileNames.get("buildingPermitProvisionalName"),"building permit PDF");   
				}
				if(attachmentFiles.get("letterToParty")!=null){
					builder.attachment( attachmentFiles.get("letterToParty"), attachmentFileNames.get("letterToPartyFileName"),"letter To Party PDF");   
				}
				//pionix TODO: builder.attachmentList is chaanged into  builder.attachment inEgi
				
			}
			LOGGER.debug("Sending to Email "+registration.getOwner().getEmailId());
			LOGGER.debug("emailSubject "+emailSubject);
			Email email = builder.build();	 			
			email.send();
		 }
		}
		catch(EGOVRuntimeException egovExp){
			LOGGER.error("Sending email is failed-"+egovExp.getMessage());
			throw egovExp;
		}
		LOGGER.debug("BpaCommonService ||Ended sendEmail method");	
	}

	 
	 public EgwStatus getstatusbyCode(String code) {
			return (EgwStatus) persistenceService.find("from EgwStatus where moduleType=? and lower(code)=? ", BpaConstants.NEWBPAREGISTRATIONMODULE,code.toLowerCase());
		}
	 public EgwStatus getstatusbyCode(String code,String moduleName) {
			return (EgwStatus) persistenceService.find("from EgwStatus where moduleType=? and lower(code)=?", moduleName,code.toLowerCase());
		}
	 
	 private EgwStatus getEgwstatusByStatusId(List<EgwSatuschange> registrationStatus) {
			return (EgwStatus) persistenceService.find("from EgwStatus where id=?",registrationStatus.get(0).getFromstatus());		
	 }
	 
	 public void createStatusChange(RegistrationExtn registration,EgwStatus oldStatus){
			LOGGER.debug("BpaCommonService ||started createStatusChange method");	
			if(oldStatus!=null&&oldStatus.getId()!=null&&null!=registration.getEgwStatus()&& !registration.getEgwStatus().getCode().equals(oldStatus.getCode())){
				EgwSatuschange change = new EgwSatuschange();		
				change.setFromstatus(oldStatus.getId());
				//change.setRemarks(registration.getStatusChangeRemarks());	//TODO:removed from	EgwSatuschange	
				change.setTostatus(registration.getEgwStatus().getId());
				change.setModuleid(registration.getId().intValue());
				change.setModuletype(BpaConstants.NEWBPAREGISTRATIONMODULE);
				change.setCreatedby((int) Long.parseLong(registration.getCreatedBy().getId().toString()));
				LOGGER.debug("BpaCommonService ||saving createStatusChange");	
				saveStatusChange(change);
				
				if(registration!=null  && registration.getRequest_number()!=null && !"".equals(registration.getRequest_number()))
				{
				// bpaCitizenPortalExtnService.updateServiceRequestRegistry(registration);
				//Pionix TODO:commenting as of now der is no bpaSurvayorPortalExtnService in portal
				/* if(bpaSurvayorPortalExtnService!=null && bpaSurvayorPortalExtnService.getServiceRequestRegistryByEntityRefNo(registration.getPlanSubmissionNum())!=null)
				 	bpaSurvayorPortalExtnService.updateServiceRequestRegistry(registration);*/
				}
			}
			LOGGER.debug("BpaCommonService ||ended createStatusChange method");	
		}
		
		
		public EgwStatus getoldStatus(RegistrationExtn registration){
			
			Criteria statuschangeCriteria= persistenceService.getSession().createCriteria(EgwSatuschange.class,"statuschange");
			statuschangeCriteria.add(Restrictions.eq("moduleid",Integer.parseInt(String.valueOf(registration.getId()))));
			statuschangeCriteria.add(Restrictions.eq("moduletype",BpaConstants.NEWBPAREGISTRATIONMODULE));
			statuschangeCriteria.add(Restrictions.eq("tostatus",Integer.parseInt(String.valueOf(registration.getEgwStatus().getId()))));
			statuschangeCriteria.addOrder(Order.desc("id"));
			List<EgwSatuschange> registrationStatus= statuschangeCriteria.list();
			if(registrationStatus.size()!=0){
			 	EgwStatus status=getEgwstatusByStatusId(registrationStatus);
			 	return status;
			}else return  registration.getEgwStatus();
				
		}
		
		public EgwStatus getAllOlderStatusforRegistration(RegistrationExtn registration){
		
			Criteria statuschangeCriteria = createStatusChangeCriteriaExcludingLP(registration);
			List<Integer> registrationStatusIds= statuschangeCriteria.list();
			EgwStatus status =null;
			status= getstatusbyCode(BpaConstants.STATUSAPPROVED);	//(EgwStatus) persistenceService.find("from EgwStatus where moduletype=? and code =?",BpaConstants.REGISTRATIONMODULE,"Approved");
			if(!registrationStatusIds.contains(status.getId())){			
				if(registration.getIsSanctionFeeRaised()!=null&&registration.getIsSanctionFeeRaised()){
					status=	getstatusbyCode(BpaConstants.FEESCREATED);//(EgwStatus) persistenceService.find("from EgwStatus where moduletype=? and code =?",BpaConstants.REGISTRATIONMODULE,BpaConstants.FEESCREATED);						
					return 	status;
				}
				InspectionExtn ins=null;
				ins=checkIfRegistrationIsInspected(registration,Boolean.TRUE);//(Inspection) persistenceService.find("from Inspection ins where ins.registration.id=? and isinspected=?",registration.getId(),Boolean.TRUE);
				if(ins!=null)	{
					status= getstatusbyCode(BpaConstants.INSPECTED);//(EgwStatus) persistenceService.find("from EgwStatus where moduletype=? and code =?",BpaConstants.REGISTRATIONMODULE,BpaConstants.INSPECTED);
					return 	status;
				}
	
				ins=checkIfRegistrationIsInspected(registration,Boolean.FALSE);//(Inspection) persistenceService.find("from Inspection ins where ins.registration.id=? and isinspected=?",registration.getId(),Boolean.FALSE);
				if(ins!=null)	{
					status=getstatusbyCode(BpaConstants.INSPECTIONSCHEDULED);//(EgwStatus) persistenceService.find("from EgwStatus where moduletype=? and code =?",BpaConstants.REGISTRATIONMODULE,BpaConstants.INSPECTIONSCHEDULED);
					return 	status;
				}
			}
				return null;
		}

		private InspectionExtn checkIfRegistrationIsInspected(RegistrationExtn registration,Boolean flag) {
			if(isUserMappedToAeOrAeeRole()){
				return (InspectionExtn) persistenceService.find("from InspectionExtn ins where ins.registration.id=? and isinspected=? and inspectedBy.id=?",registration.getId(),flag, (EgovThreadLocals.getUserId()));
			} 
			return null;
		}
		
		public Boolean isUserMappedToAeOrAeeRole() {
			if(EgovThreadLocals.getUserId()!=null){
				User user = getUserbyId( (EgovThreadLocals.getUserId()));
				for(Role role : user.getRoles())
					if(role.getName()!=null && (role.getName().equalsIgnoreCase(BpaConstants.BPAAEROLE) || 
							role.getName().equalsIgnoreCase(BpaConstants.BPAAEEROLE)))
					{
						return true;
					}
					
			}
			return false;
		}


		public Criteria createStatusChangeCriteriaExcludingLP(RegistrationExtn registration) {
			Criteria statuschangeCriteria= persistenceService.getSession().createCriteria(EgwSatuschange.class,"statuschange");
			
			statuschangeCriteria.add(Restrictions.eq("moduleid",Integer.parseInt(String.valueOf(registration.getId()))));
			statuschangeCriteria.add(Restrictions.eq("moduletype",BpaConstants.NEWBPAREGISTRATIONMODULE));		
			DetachedCriteria detachedCriteria= DetachedCriteria.forClass(EgwStatus.class);
			detachedCriteria.add(Restrictions.eq("moduletype",BpaConstants.NEWBPAREGISTRATIONMODULE));
			detachedCriteria.add(Restrictions.in("code",new String[]{BpaConstants.CREATEDLETTERTOPARTY,BpaConstants.LPREPLYRECEIVED}));
			detachedCriteria.setProjection(Projections.property("id"));					
			statuschangeCriteria.add(Property.forName("tostatus").notIn(detachedCriteria));								
			statuschangeCriteria.setProjection(Projections.distinct(Projections.property("tostatus")));
			return statuschangeCriteria;
		}
		
		public Criteria createStatusChangeCriteria(RegistrationExtn registration) {
			Criteria statuschangeCriteria= persistenceService.getSession().createCriteria(EgwSatuschange.class,"statuschange");			
			statuschangeCriteria.add(Restrictions.eq("moduleid",Integer.parseInt(String.valueOf(registration.getId()))));
			statuschangeCriteria.add(Restrictions.eq("moduletype",BpaConstants.NEWBPAREGISTRATIONMODULE));								
			statuschangeCriteria.setProjection(Projections.distinct(Projections.property("tostatus")));
			return statuschangeCriteria;
		}
		public List <EgwSatuschange> getStatusChangeByPassingRegistrationAndStatusCode(RegistrationExtn registration,String status) {
			EgwStatus statusObj =getstatusbyCode(status);
		
			Criteria statuschangeCriteria= persistenceService.getSession().createCriteria(EgwSatuschange.class,"statuschange");			
			statuschangeCriteria.add(Restrictions.eq("moduleid",Integer.parseInt(String.valueOf(registration.getId()))));
			statuschangeCriteria.add(Restrictions.eq("moduletype",BpaConstants.NEWBPAREGISTRATIONMODULE));								
			
			if(statusObj!=null)
				statuschangeCriteria.add(Restrictions.eq("tostatus",statusObj.getId()));
			else
				return Collections.EMPTY_LIST;
			
			//statuschangeCriteria.setProjection(Projections.distinct(Projections.property("tostatus")));
			
			return statuschangeCriteria.list();
		}
			

		public String getPrimaryStatusforRegistration(RegistrationExtn registration){
			
			LOGGER.debug("BpaCommonService ||started getPrimaryStatusforRegistration method");	
			if(registration.getId()!=null){
			Criteria statuschangeCriteria = createStatusChangeCriteriaExcludingLP(registration);
			List<Integer> registrationStatusIds= statuschangeCriteria.list();
			EgwStatus status =null;
			status=	getstatusbyCode(BpaConstants.STATUSAPPROVED);
			if(registrationStatusIds.contains(status.getId())){	
				return BpaConstants.PRIMARYSTATUSAPPROVE;
			}
			
			status=	getstatusbyCode(BpaConstants.REJECTIONAPPROVED);//(EgwStatus) persistenceService.find("from EgwStatus where moduletype=? and code =?",BpaConstants.REGISTRATIONMODULE,"Rejection Approved");
				if(null!=status && registrationStatusIds.contains(status.getId())){	
					return BpaConstants.PRIMARYSTATUSREJECT;
			}return null; 
			}else
				return null; 
		}
		
		public EgwSatuschange saveStatusChange(EgwSatuschange statusChange)
		{
			return statusChangeService.persist(statusChange);
		}
		/*
		 * To Validate Both Exist PPA Number and Exist BA Number Belongs to Same Application...
		 */
		public RegistrationExtn validateExistBaAndExistPpaOfSameApplication(String existPpa, String existBa)
		{
			return (RegistrationExtn) persistenceService.find(" from Registration where planSubmissionNum=? and baNum=?",existPpa,existBa);
			
		}
		
	public LandBuildingTypesExtn getLandBuildingTypesExtnByDescription(
			String landBuildingDescription) {
		
		if(landBuildingDescription!=null && !"".equals(landBuildingDescription))
		return (LandBuildingTypesExtn) persistenceService.find(
				" from LandBuildingTypesExtn where lower(description)=? ",
				landBuildingDescription.toLowerCase());
		else
			return null;

	}

		public List<EgwSatuschange> getEgwStatChangeListForRegn(RegistrationExtn registration) {
			List<EgwSatuschange> egwStatChangeList = statusChangeService.
					findAllBy("from EgwSatuschange where moduletype=? and moduleid=?",BpaConstants.NEWBPAREGISTRATIONMODULE,
							Integer.parseInt(registration.getId().toString()));
			
			return egwStatChangeList;
		}
		
		public EgwStatus getInitialEgwStatus(RegistrationExtn registration){
			LOGGER.debug("BpaCommonService ||started getInitialEgwStatus method");	
			
			EgwStatus status=null;
			Criteria statuschangeCriteria= persistenceService.getSession().createCriteria(EgwSatuschange.class,"statuschange");
			statuschangeCriteria.add(Restrictions.eq("moduleid",Integer.parseInt(String.valueOf( registration.getId()))));
			statuschangeCriteria.add(Restrictions.eq("moduletype",BpaConstants.NEWBPAREGISTRATIONMODULE));
			statuschangeCriteria.addOrder(Order.asc("id"));
			List<EgwSatuschange> registrationStatus= statuschangeCriteria.list();
			if (registrationStatus.size() != 0) {
				if (registrationStatus.get(0).getFromstatus() != null) {
					status = getEgwstatusByStatusId(registrationStatus);
				}
			} else
				status = registration.getEgwStatus();
			LOGGER.debug("BpaCommonService ||ended getInitialEgwStatus method");	
			return status;
		}
		
		public BigDecimal isFeeCollectionPending(RegistrationExtn registration){	
			LOGGER.debug("BpaCommonService ||started isFeeCollectionPending method");	
			
			BigDecimal totalAmount=BigDecimal.ZERO;
			
			//commenitng for Demand module
			/*if (registration != null && registration.getEgDemand() != null) {
				Set<EgDemandDetails> demandDetailSet = registration
						.getEgDemand().getEgDemandDetails();

				for (EgDemandDetails demanddtl : demandDetailSet) {
					totalAmount = totalAmount.add(demanddtl.getAmount()
							.subtract(demanddtl.getAmtCollected()));
				}
			}
			*/
			LOGGER.debug("BpaCommonService ||ended isFeeCollectionPending method");	
			
			return totalAmount;
			
		}
		
		public Boolean isPlanOrBuildingPermitAllowed(RegistrationExtn registrationObj,BigDecimal finalFeeTobePaid,String type){	
			LOGGER.debug("BpaCommonService ||started isPlanOrBuildingPermitAllowed method");	
			Boolean flag=Boolean.FALSE;
			if(null!=registrationObj && registrationObj.getEgwStatus()!=null &&
					registrationObj.getEgwStatus().getCode().equals(BpaConstants.STATUSAPPROVED)){
				
				flag = checkIfPlanBuildPermitAllowed(registrationObj,finalFeeTobePaid, type, flag);
			}
			LOGGER.debug("BpaCommonService ||ended isPlanOrBuildingPermitAllowed method");	
			return flag;			
		}
		
		public Boolean isPlanOrBuildingPermitAllowedForSearch(RegistrationExtn registrationObj,BigDecimal finalFeeTobePaid,String type){	
			LOGGER.debug("BpaCommonService ||started isPlanOrBuildingPermitAllowedForSearch method");	
			Boolean flag=Boolean.FALSE;
			if(null!=registrationObj && registrationObj.getEgwStatus()!=null && (
					registrationObj.getEgwStatus().getCode().equals(BpaConstants.STATUSAPPROVED) ||
					registrationObj.getEgwStatus().getCode().equals(BpaConstants.APPLICANTSIGNUPDATED) ||
					registrationObj.getEgwStatus().getCode().equals(BpaConstants.FILECONSIDERATIONCHECKED) ||
					registrationObj.getEgwStatus().getCode().equals(BpaConstants.ORDERISSUEDTOAPPLICANT) ||
					registrationObj.getEgwStatus().getCode().equals(BpaConstants.ORDERPREPARED))){
				
				flag = checkIfPlanBuildPermitAllowed(registrationObj,finalFeeTobePaid, type, flag);
			}
			LOGGER.debug("BpaCommonService ||ended isPlanOrBuildingPermitAllowedForSearch method");	
			return flag;			
		}


		private Boolean checkIfPlanBuildPermitAllowed(
				RegistrationExtn registrationObj, BigDecimal finalFeeTobePaid,
				String type, Boolean flag) {
			LOGGER.debug("BpaCommonService ||started checkIfPlanBuildPermitAllowed method");	
			if(finalFeeTobePaid.equals(BigDecimal.ZERO)){
				if(registrationObj.getServiceType()!=null){
					if(type.equalsIgnoreCase(BpaConstants.PLANPERMIT)){
					
						if(registrationObj.getServiceType().getCode().equals(BpaConstants.NEWBUILDINGONVACANTPLOTCODE) ||
								registrationObj.getServiceType().getCode().equals(BpaConstants.APPLICATIONFORDEMOLITIONCODE) ||
								registrationObj.getServiceType().getCode().equals(BpaConstants.DEMOLITIONRECONSTRUCTIONCODE) ||
								registrationObj.getServiceType().getCode().equals(BpaConstants.SUBDIVISIONOFLANDCODE) ||
								registrationObj.getServiceType().getCode().equals(BpaConstants.LAYOUTAPPPROVALCODE) ||
								registrationObj.getServiceType().getCode().equals(BpaConstants.RECLASSIFICATIONCODE) ||
								registrationObj.getServiceType().getCode().equals(BpaConstants.ADDITIONALCONSTRUCTIONCODE)){
							
							flag=Boolean.TRUE;				
						}	
					}
					else if(type.equalsIgnoreCase(BpaConstants.BUILDINGPERMIT)){
						if(registrationObj.getServiceType().getCode().equals(BpaConstants.NEWBUILDINGONVACANTPLOTCODE) ||
								registrationObj.getServiceType().getCode().equals(BpaConstants.DEMOLITIONRECONSTRUCTIONCODE) ||
								registrationObj.getServiceType().getCode().equals(BpaConstants.ADDITIONALCONSTRUCTIONCODE) ||
								registrationObj.getServiceType().getCode().equals(BpaConstants.CMDACODE)){
							
							flag=Boolean.TRUE;				
						}
					}
				}
			}
			LOGGER.debug("BpaCommonService ||ended checkIfPlanBuildPermitAllowed method");	
			return flag;
		}
	
		public Map<String, BillReceiptInfo> getReceiptInfoByRegistrationObject(RegistrationExtn registration) {
			LOGGER.debug("BpaCommonService ||started getReceiptInfoByRegistrationObject method");	
			Map<String,BillReceiptInfo> billReceiptInfoMap = new HashMap<String,BillReceiptInfo>();
					DemandGenericDao demandGenericDao = new DemandGenericHibDao();
					Set<String> receiptNumbetSet= new HashSet<String>();
					List<BillReceipt> billReceiptList=new ArrayList<BillReceipt>();
					
					if(registration!=null && registration.getEgDemand()!=null)
					{
						LOGGER.debug("called getBillReceipts method");	
						 billReceiptList=demandGenericDao.getBillReceipts(registration.getEgDemand());
						
					if(billReceiptList!=null && billReceiptList.size()>0) {	 
						
						for(BillReceipt billReceipt : billReceiptList)
						{
							receiptNumbetSet.add(billReceipt.getReceiptNumber());
						}
				
						if(receiptNumbetSet.size()>0)
							billReceiptInfoMap = collectionIntegrationService.getReceiptInfo(BpaConstants.EXTD_SERVICE_CODE, receiptNumbetSet);
					  }
					}
					LOGGER.debug("BpaCommonService ||ended getReceiptInfoByRegistrationObject method");	
				return billReceiptInfoMap;
		}

		
		
		public Boundary getZoneNameFromAdminboundaryid(Boundary adminboundaryid){
			
			if(adminboundaryid!=null){
				if( adminboundaryid.getBoundaryType()!=null){
					if( adminboundaryid.getBoundaryType().getName()!=null){
						if(adminboundaryid.getBoundaryType().getName().equals("Ward")){
							
							return adminboundaryid.getParent();
				
						}else if(adminboundaryid.getBoundaryType().getName().equals("Zone")){
							
							return adminboundaryid;
						}
					}
				}
			}	
			return null;
		}
		 public List<CMDALetterToPartyExtn> getcmdaLetterToPartyForRegistrationObject(RegistrationExtn registrationObj) {
				return  (List<CMDALetterToPartyExtn>)persistenceService. findAllBy("from CMDALetterToPartyExtn where registration=?  order by id desc",registrationObj);
			}
		/*
		public void createNotificationFinalFeeCollected(RegistrationExtn registration,BigDecimal finalFeeCollected) {
			
			final HashMap<String, String> fileDetails = new HashMap<String, String>();		
			User loginUser=getUserbyId(Integer.parseInt(EgovThreadLocals.getUserId()));
			fileDetails.put("fileCategory", "INTER DEPARTMENT");
			fileDetails.put("filePriority", "MEDIUM");
			fileDetails.put("fileHeading", "Collection is Done / Building Approval Fees is paid.");
			SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
			String fileSummary="";
			Date planSubmissionDate=registration.getPlanSubmissionDate();
			fileSummary = "Mr/Ms "+registration.getOwner().getName()+" paid the amount Rs. "+finalFeeCollected +" for Building Plan Application with Plan Submission Number "+ registration.getPlanSubmissionNum() +" dated on "+dateFormatter.format(planSubmissionDate);
			
			fileDetails.put("fileSummary", fileSummary);
			fileDetails.put("fileSource", "INTER DEPARTMENT");
			fileDetails.put("senderAddress", "");
			fileDetails.put("senderName", loginUser.getUsername());
			fileDetails.put("senderPhone", "");
			fileDetails.put("senderEmail", "");
			
			String user="";
		
			PersonalInformation emp = getEmpForPosition(registration);
			if(emp!=null)
				user=emp.getUserMaster().getId().toString();
		
		//	fileManagementService.generateFileNotification(fileDetails,user);	
			
		}

		public PersonalInformation getEmpForPosition(RegistrationExtn registration) {
			PersonalInformation emp=null;
			if(null!=registration && null!=registration.getState() && null!=registration.getState().getOwnerUser() ) //Todo: changing getState.getOwner -> getState.getOwnerUser()
				emp = personalInformationService.getEmpForPosition(registration.getState().getOwnerPosition().getId());
			//TODO Phionix : create getEmpForPosition in org.egov.eis.service.personalInformationService
			return emp;
			
		}
		
		public PersonalInformationService getPersonalInformationService() {
			return personalInformationService;
		}

		public void setPersonalInformationService(
				PersonalInformationService personalInformationService) {
			this.personalInformationService = personalInformationService;
		}
*/
		public HashMap<Integer,String> getStatusIdMap(){
			HashMap<Integer,String> statusMap=new HashMap<Integer,String>();
			List<EgwStatus>	 statusList=getAllStatusForBPA();
			for(EgwStatus status:statusList){
				statusMap.put(status.getId(), status.getCode());
			}
			return statusMap;
		}
		
		 public static Map<Integer,String> getFloorsMap(int value) 
		 {
			 int remainder=0;
			 Map<Integer,String> floorList=new HashMap<Integer,String>();
			 String key=null;
			 floorList.put(-3,BpaConstants.TERRACE);
			 floorList.put(-2,BpaConstants.BASEMENT);
			 floorList.put(-1,BpaConstants.STILT);
			 floorList.put(0, BpaConstants.GROUND);

			 for(int i=1;i<value;i++)
			 {
				 if(i>=10 && i<=20)
					 key=i+"th";
				 else{
					 remainder=i%10;
					 if(remainder == 1)
						 key = i+"st";
					 else if(remainder == 2)
						 key = i+"nd";
					 else if(remainder == 3)
						 key = i+"rd";	
					 else
						 key = i+"th";
				 }
				 floorList.put(i,key);
			 }

			 return floorList;
		 }
		
		 public  static Map<Integer,String> getFloorNumberMap(int value)
		 {
			 int remainder=0;
			 Map<Integer,String> floorList=new HashMap<Integer,String>();
			 String key=null;
			// floorList.put(-2,BpaConstants.TERRACE);
			// floorList.put(-1,BpaConstants.TILEDROOF);
			 floorList.put(0, BpaConstants.GROUNDFLOORFORREPORT);

			 for(int i=1;i<value;i++)
			 {
				 if(i>=10 && i<=20)
					 key=i+"th";
				 else{
					 remainder=i%10;
					 if(remainder == 1)
						 key = i+"st";
					 else if(remainder == 2)
						 key = i+"nd";
					 else if(remainder == 3)
						 key = i+"rd";	
					 else
						 key = i+"th";
				 }
				 floorList.put(i,key);
			 }

			 return floorList; 
		 }
		public static Map<String,String> unitClassification() {
			Map<String,String> unitClsfn=new HashMap<String, String>();
			unitClsfn.put("Single", "Single");
			unitClsfn.put("Multiple", "Multiple");
			
			return unitClsfn;
		}


	//Commenting whole thig as of now .. willdo changes for 1 lever workflow once app in  developemt
	/*public void workFlowTransition(RegistrationExtn registration,
		   String workFlowAction, String comments) {
	   Position position;
	   WorkFlowMatrix wfmatrix;
	   LOGGER.debug("BpaCommonService ||Starting workFlowTransition method for registration");
		 
	   if(registration.getAdditionalRule()!=null && (registration.getAdditionalRule().equals("RejectBPA") || registration.getAdditionalRule().equals("lettertoparty") ))
		   wfmatrix=registrationWorkflowExtnService.getWfMatrix(registration.getStateType(),null,null,registration.getAdditionalRule(),registration.getAdditionalState(),null);
	   else
		   wfmatrix=registrationWorkflowExtnService.getWfMatrix(registration.getStateType(),null,null,registration.getAdditionalRule(),registration.getCurrentState().getValue(),null);

	   if(workFlowAction.equalsIgnoreCase("approve") ||workFlowAction.equalsIgnoreCase("forward") ||
			   workFlowAction.equalsIgnoreCase("approve registration") || workFlowAction.equalsIgnoreCase("forward to approver") || 
			   workFlowAction.equalsIgnoreCase("approve unconsidered") || workFlowAction.equalsIgnoreCase("Approve LetterToParty"))
	   { 
		   if( registration.getApproverPositionId()!=null && registration.getApproverPositionId()!=-1)
			   position=(Position) persistenceService.find("from Position where id=?",registration.getApproverPositionId());
		   else
			   position=registration.getCurrentState().getOwner();

		   registration.changeState(wfmatrix.getNextState(),wfmatrix.getNextAction(),position,comments);

		   if (wfmatrix.getNextAction()!=null && wfmatrix.getNextAction().equalsIgnoreCase("END")){
			   registrationWorkflowExtnService.end(registration,registration.getCurrentState().getOwner(),comments);
			   LOGGER.debug("BpaCommonService ||Ended workflow for registration");
				
		   }
		   if(wfmatrix.getNextStatus()!=null )
			   registration.setEgwStatus(getstatusbyCode(wfmatrix.getNextStatus()));		

		   //registrationWorkflowService.transition(workFlowAction.toLowerCase(),registration, comments);
	   }else if (workFlowAction.equalsIgnoreCase("Reject") ||workFlowAction.equalsIgnoreCase("reject") )
	   {
		   if( registration.getPreviousStateOwnerId()!=null && registration.getPreviousStateOwnerId()!=-1)
			   position=(Position) persistenceService.find("from Position where id=?",registration.getPreviousStateOwnerId());
		   else
			   position=registration.getCurrentState().getOwner();

		   if( registration.getPreviousStateOwnerId()!=null && registration.getPreviousStateOwnerId()!=-1)
			   registration.changeState(registration.getPreviousObjectState(),registration.getPreviousObjectAction(),position,comments);
		   else
			   registration.changeState("Rejected", position,comments);

		     if (registration.getCurrentState()!=null && registration.getCurrentState().getPrevious()!=null && (registration.getCurrentState().getPrevious().getValue().equalsIgnoreCase("Rejected") || registration.getCurrentState().getPrevious().getValue().equalsIgnoreCase("NEW")))
			   registrationWorkflowExtnService.end(registration,registration.getCurrentState().getOwner(),comments);

		   if(workFlowAction.equalsIgnoreCase("Reject") && registration.getCurrentState().getPrevious().getValue().equalsIgnoreCase("Rejected"))
			   registration.setEgwStatus(getstatusbyCode("Cancelled"));



	   }else if (workFlowAction.equalsIgnoreCase("Cancel") ||workFlowAction.equalsIgnoreCase("cancel") )
	   {   LOGGER.debug("BpaCommonService ||Started cancel workflow for registration");
		   registration.changeState("Cancelled",registration.getCurrentState().getOwner(),comments);
		   registrationWorkflowExtnService.end(registration,registration.getCurrentState().getOwner(),comments);

	   }else if (workFlowAction.equalsIgnoreCase("reject registration"))
	   {
		   LOGGER.debug("BpaCommonService ||Started reject workflow for registration");
		   registration.changeState("Rejected",registration.getCurrentState().getOwner(),comments);
		   registrationWorkflowExtnService.end(registration,registration.getCurrentState().getOwner(),comments);
		   registration.setEgwStatus(getstatusbyCode("Cancelled"));
		   
		   if(registration!=null  && registration.getRequest_number()!=null && !"".equals(registration.getRequest_number()))
				bpaCitizenPortalExtnService.updateServiceRequestRegistry(registration);
		   
	   }else if (workFlowAction.equalsIgnoreCase("save lettertoparty"))
	   {
		   registration.changeState("LetterToParty saved",registration.getCurrentState().getOwner(),comments);

	   }else if (workFlowAction.equalsIgnoreCase("close registration"))
	   {
		   //registration=(Registration) regnPersistenceService.merge(registration);
		   //registration.changeState("Closed",eisCommonsManager.getPositionByUserId(registration.getCreatedBy().getId()),comments);

		   //registrationWorkflowService.transition("Closed", registration, comments);

		   registration.changeState("Closed",registration.getCurrentState().getOwner(),comments);
		   //System.out.print("aaaaaaaaaaaaaaa");
		   registrationWorkflowExtnService.end(registration,registration.getCurrentState().getOwner(),comments);
		   LOGGER.debug("BpaCommonService ||ended  workflow for registration");
 
	   }
	   else if (workFlowAction.equalsIgnoreCase("cancel unconsidered"))
	   {
            if( registration.getPreviousStateOwnerId()!=null && registration.getPreviousStateOwnerId()!=-1)
  			   position=(Position) persistenceService.find("from Position where id=?",registration.getPreviousStateOwnerId());
  		   else
  			   position=registration.getCurrentState().getOwner();

  		   if( registration.getPreviousStateOwnerId()!=null && registration.getPreviousStateOwnerId()!=-1)
  			   registration.changeState(registration.getPreviousObjectState(),registration.getPreviousObjectAction(),position,comments);
  		   else
  			   registration.changeState(BpaConstants.WORKFLOWSTATUSFORWARDEDTOAEORAEE, position,comments);

  		   registration.setEgwStatus(registration.getOldStatus());
	   }
	   else if (workFlowAction.equalsIgnoreCase("Cancel LetterToParty"))
			   {
				 registration.changeState(BpaConstants.WORKFLOWSTATUSFORWARDEDTOAEORAEE,
						   registration.getCurrentState().getPrevious().getNextAction(),registration.getCurrentState().getPrevious().getOwner(),comments);
				 registration.setAdditionalRule(BpaConstants.BPAAPPLICATIONREGISTRATION);
				   registration.setEgwStatus(registration.getOldStatus());
				  
			   }
			 
		}
   */
   
   /*public void workFlowTransition(RegistrationFeeExtn registrationFee,
		   String workFlowAction, String comments) {
	   Position position;
	   WorkFlowMatrix wfmatrix;
	   LOGGER.debug("BpaCommonService ||Starting workFlowTransition method for registration fee");
	 
		 wfmatrix=registrationFeeWorkflowExtnService.getWfMatrix(registrationFee.getStateType(),null,null,null,registrationFee.getCurrentState().getValue(),null);

	   if(workFlowAction.equalsIgnoreCase("approve") ||workFlowAction.equalsIgnoreCase("forward"))
	   { 
		   if( registrationFee.getApproverPositionId()!=null && registrationFee.getApproverPositionId()!=-1)
			   position=(Position) persistenceService.find("from Position where id=?",registrationFee.getApproverPositionId());
		   else
			   position=registrationFee.getCurrentState().getOwner();

		   registrationFee.changeState(wfmatrix.getNextState(),wfmatrix.getNextAction(),position,comments);

		   if (wfmatrix.getNextAction()!=null && wfmatrix.getNextAction().equalsIgnoreCase("END")){
			   registrationFeeWorkflowExtnService.end(registrationFee,registrationFee.getCurrentState().getOwner(),comments);
			   LOGGER.debug("BpaCommonService ||Ended workFlow");
		   }

		   if(wfmatrix.getNextStatus()!=null )
			   registrationFee.setEgwStatus(getstatusbyCode(wfmatrix.getNextStatus(), BpaConstants.BPAREGISTRATIONFEEMODULE));		

		   //registrationWorkflowService.transition(workFlowAction.toLowerCase(),registration, comments);
	   }
	   
	   else if (workFlowAction.equalsIgnoreCase("Reject"))
	   {
		   if( registrationFee.getPreviousStateOwnerId()!=null && registrationFee.getPreviousStateOwnerId()!=-1){
			   position=(Position) persistenceService.find("from Position where id=?",registrationFee.getPreviousStateOwnerId());
			   registrationFee.changeState(registrationFee.getPreviousObjectState(),registrationFee.getPreviousObjectAction(),position,comments);
		   }else{
			   registrationFee.changeState("Rejected", registrationFee.getCurrentState().getOwner(),comments);
		   }

		   LOGGER.debug("BpaCommonService ||Started Reject workFlow");
		if (registrationFee.getCurrentState().getValue().equals("Rejected") || registrationFee.getCurrentState().getValue().equals("NEW"))  {
			//registrationFee.changeState("Rejected",registrationFee.getCurrentState().getOwner(),comments) ;   
			registrationFeeWorkflowExtnService.end(registrationFee,registrationFee.getCurrentState().getOwner(),comments) ;
		  registrationFee.setEgwStatus(getstatusbyCode("Cancelled", BpaConstants.BPAREGISTRATIONFEEMODULE));
	    	
		}else
		  registrationFee.changeState("Rejected",registrationFee.getCurrentState().getPrevious().getOwner(),comments) ;   
		    
	
	   }

}*/

   public List getAllBanks() {
		listOfBanks = (List<Bank>) persistenceService.findAllBy("from Bank where isactive=true order by upper(name)");
		return listOfBanks;
   }
   public Bank getBankbyId(Integer bankId){
	   if(bankId!=null&&bankId!=-1){
	   Bank  bankObj = (Bank) persistenceService.find("from Bank where id=?",bankId);
         return bankObj;
	   }else return null;
   }

	public List<ServiceTypeExtn> getAllServiceTypeList() {
		return persistenceService.findAllBy("from ServiceTypeExtn order by code");
	}
   
   public List<ServiceTypeExtn> getAllActiveServiceTypeList() {
		List<ServiceTypeExtn> serviceTypeList = getPersistenceService().findAllBy(
				"from ServiceTypeExtn ST where ST.isActive=1 order by code");
		return serviceTypeList;
	}
   
   
   public List<BuildingCategoryExtn> getAllActiveBuildingCategoryList() {
		List<BuildingCategoryExtn> buildingCategoryList = persistenceService.findAllBy(
				"from BuildingCategoryExtn BC where BC.isActive=1");
		return buildingCategoryList;
	}
   
   //TODo: commenting this cos In portal projectb No Surveyor Entity
	/*public List<Surveyor> getAllActiveSurveyorNameList() {

		List<Surveyor> surveyorNameList = persistenceService.findAllBy("from Surveyor SN where SN.isEnabled=1 ");
		return surveyorNameList;
	}*/
	
	public List<VillageNameExtn> getAllActivevillageNameList() {

		List<VillageNameExtn> villageNameList = persistenceService.findAllBy("from VillageNameExtn VN where VN.isActive=1");
		
		return villageNameList;
	}
	public List<org.egov.infra.workflow.entity.State> getAllStatesOfIndia() {
		List <org.egov.infra.workflow.entity.State> bndryStateList = getPersistenceService().findAllBy("from State where stateConst='TN'");
		return bndryStateList;
	}

	public List<LpReasonExtn> getAllLpReasonList() {
		List<LpReasonExtn> lpReasonlist =persistenceService.findAllBy("from LpReasonExtn order by code ");
		return lpReasonlist;
	}
	
	public List<EgwStatus> getAllStatusForBPA() {
		return persistenceService.findAllBy("from EgwStatus where moduletype=? order by code ",BpaConstants.NEWBPAREGISTRATIONMODULE);
	}

	public Boundary getBoundaryObjById(Long adminboundaryid)
	{
		return( Boundary) persistenceService.find("from Boundary where id=?", adminboundaryid);
	}
	public List<Boundary>getZoneListById(Integer adminboundaryid)
	{
		return persistenceService.findAllBy("from Boundary bndry where bndry.id in(select bndtype.parent.id from BoundaryImpl bndtype where bndtype.id=?)  order by bndry.name ",adminboundaryid);
	}
	public List<Boundary>getZoneListByIdandName(Integer adminboundaryid)
	{
	return persistenceService.findAllBy("from Boundary bndry where bndry.boundaryType.id in(select id from BoundaryTypeImpl where name=?) and bndry.id=? order by bndry.name ",BpaConstants.ZONE_BNDRY_TYPE,adminboundaryid);
	}
	public List<Boundary>getZoneListByName()
	{
	return persistenceService.findAllBy("from Boundary bndry where bndry.boundaryType.id in(select id from BoundaryTypeImpl where name=?) order by bndry.id ",BpaConstants.ZONE_BNDRY_TYPE);
	
	}
	public PersistenceService getRegnPersistenceExtnService() {
		return regnPersistenceExtnService;
	}
	
	public void setRegnPersistenceExtnService(PersistenceService regnPersistenceService) {
		this.regnPersistenceExtnService = regnPersistenceService;
	}
	
	/*public void setEisCommonsManager(EisCommonsManager eisCommonsManager) {
		this.eisCommonsManager = eisCommonsManager;
	}
	
	public void setRegistrationWorkflowExtnService(
			WorkflowService<RegistrationExtn> registrationWorkflowService) {
		this.registrationWorkflowExtnService = registrationWorkflowService;
	}
	*/
	
	public CollectionIntegrationServiceImpl getCollectionIntegrationService() {
		return collectionIntegrationService;
	}


public void setCollectionIntegrationService(
		CollectionIntegrationServiceImpl collectionIntegrationService) {
	this.collectionIntegrationService = collectionIntegrationService;
}


public PersistenceService<EgwSatuschange, Integer> getStatusChangeService() {
	return statusChangeService;
}


public void setStatusChangeService(
		PersistenceService<EgwSatuschange, Integer> statusChangeService) {
	this.statusChangeService = statusChangeService;
}

public PersistenceService getPersistenceService() {
	return persistenceService;
}


public void setPersistenceService(PersistenceService persistenceService) {
	this.persistenceService = persistenceService;
}



public InspectionExtnService getInspectionExtnService() {
	return inspectionExtnService;
}


public void setInspectionExtnService(InspectionExtnService inspectionService) {
	this.inspectionExtnService = inspectionService;
}


public EisUtilService getEisService() {
	return eisService;
}


public void setEisService(EisUtilService eisService) {
	this.eisService = eisService;
}


public FeeExtnService getFeeExtnService() {
	return feeExtnService;
}


public void setFeeExtnService(FeeExtnService feeService) {
	this.feeExtnService = feeService;
}


/*public FileManagementService getFileManagementService() {
	return fileManagementService;
}


public void setFileManagementService(FileManagementService fileManagementService) {
	this.fileManagementService = fileManagementService;
}


public EisManager getEisManager() {
	return eisManager;
}


public void setEisManager(EisManager eisManager) {
	this.eisManager = eisManager;
}
*/

/*
 * Api to give the center date i.e., day4 in list of 3 dates ( day3,day4 and day5 ) to automatically Schedule SiteInspection ,
 *  if a record is forwarded to Official on day1 and no action is taken on record.
 */
public Date getInspectionDateForSchedular(RegistrationExtn registrationObj){
	LOGGER.debug("BpaCommonService ||Inside getInspectionDateForSchedular method ");
	List<String> siteInspectionDatesList=autoGenerateSiteInspectionDates(registrationObj);
	List<Date> siteInspectionDatesListDate=new ArrayList<Date>();
	
	if(!siteInspectionDatesList.isEmpty()){
		for(int i=0;i<siteInspectionDatesList.size();i++){
			siteInspectionDatesListDate.add(DateUtils.getDate(siteInspectionDatesList.get(i), "yyyy-MM-dd"));					
		}
		if(!siteInspectionDatesListDate.isEmpty()){
			 Collections.sort(siteInspectionDatesListDate);
			 return siteInspectionDatesListDate.get(siteInspectionDatesListDate.size()/2);
		  }
	 }
	return null;
	
}


	/*
	 * Api to give list of 3 dates in string format ( day3,day4 and day5 ) to Schedule SiteInspection
	 *  , if a recored is forwarded to Surveyor. (Considering plan submission date)
	 */
	public List<String> autoGenerateSiteInspectionDatesForSurveyor(RegistrationExtn registrationObj){
		List<String> siteInspectionDatesList = new ArrayList<String>();
		LOGGER.debug("BpaCommonService || Inside autoGenerateSiteInspectionDatesForSurveyor method ");
		if(null!=registrationObj && null!=registrationObj.getPlanSubmissionDate()){
			LOGGER.debug("registrationObj "+registrationObj.getPlanSubmissionNum());
			String siteInspectionDate=sdf.format(DateUtils.getDate(incrementDatebyNoOfDays(registrationObj.getPlanSubmissionDate(),2), "yyyy-MM-dd"));
			siteInspectionDatesList=finalSiteInspectionDates(siteInspectionDate);
		LOGGER.debug("siteInspectionDatesList "+siteInspectionDatesList);
		}
		return siteInspectionDatesList;
	}


/*
 * Api to give list of 3 dates in string format ( day3,day4 and day5 ) to Schedule SiteInspection
 *  , if a recored is forwarded to Official on day1.
 */
public List<String> autoGenerateSiteInspectionDates(RegistrationExtn registrationObj){

	List<String> siteInspectionDatesList = new ArrayList<String>();
	LOGGER.debug("BpaCommonService || Inside autoGenerateSiteInspectionDates method ");
	
	if(null!=registrationObj && null!=registrationObj.getState() && null!=registrationObj.getState().getCreatedDate()){
		LOGGER.debug("registrationObj "+registrationObj.getPlanSubmissionNum());
		String siteInspectionDate=sdf.format(DateUtils.getDate(incrementDatebyNoOfDays(registrationObj.getState().getCreatedDate().toDate(),2), "yyyy-MM-dd"));
		siteInspectionDatesList=finalSiteInspectionDates(siteInspectionDate);
	}
		return siteInspectionDatesList;
}

public List<String> finalSiteInspectionDates(String siteInspectionDate){
	List<String> siteInspectionDatesList = new ArrayList<String>();
	List<String> holidaysList = new ArrayList<String>();
	CFinancialYear cFinancialYear=getFinancialYear();
	
	String isHolidaylistIncluded=getAppconfigValueResult(BpaConstants.BPAMODULENAME,BpaConstants.HOLIDAYLIST_INCLUDED,null);		
	if(null!=isHolidaylistIncluded && !"".equals(isHolidaylistIncluded) &&  isHolidaylistIncluded.equalsIgnoreCase("YES")){			
	/*	HolidaysUlbDAO holidaysUlbHibernateDAO = LeaveDAOFactory.getDAOFactory().getHolidaysUlbDAO();
		holidaysList.addAll(holidaysUlbHibernateDAO.getHolidayListByFinalsialYearId(cFinancialYear));
	*/
		}
	
	String isSaturdayHoliday=getAppconfigValueResult(BpaConstants.BPAMODULENAME,BpaConstants.IS_SATURDAY_HOLIDAY,null);			
	String isSecondSaturdayHoliday=getAppconfigValueResult(BpaConstants.BPAMODULENAME,BpaConstants.IS_SECONDSATURDAY_HOLIDAY,null);		
	/*if(null!=isSaturdayHoliday && !"".equals(isSaturdayHoliday) && isSaturdayHoliday.equalsIgnoreCase("YES")){
		SaturdayHoliday sh=new SaturdayHoliday();
		holidaysList.addAll(sh.getListOfHolidays(cFinancialYear));
	} 
	else if(null!=isSecondSaturdayHoliday && !"".equals(isSecondSaturdayHoliday) && isSecondSaturdayHoliday.equalsIgnoreCase("YES")){	
		SecondSaturdayHoliday ssh=new SecondSaturdayHoliday();
		holidaysList.addAll(ssh.getListOfHolidays(cFinancialYear));
	}TODO: IN EIS no SaturdayHoliday,SecondSaturdayHoliday present
	*/
	String isSundayHoliday=getAppconfigValueResult(BpaConstants.BPAMODULENAME,BpaConstants.IS_SUNDAY_HOLIDAY,null);
	if(null!=isSundayHoliday && !"".equals(isSundayHoliday) && isSundayHoliday.equalsIgnoreCase("YES")){	
		holidaysList.addAll(EisManagersUtill.getSundaysForGivenCurrentFinYear(cFinancialYear));
	}
	
	while(true){
		Date siDate=DateUtils.getDate(siteInspectionDate, "yyyy-MM-dd");
		if(holidaysList.contains(siteInspectionDate) ){
			siteInspectionDate=incrementDatebyNoOfDays(siDate,1); 
		}else{
			if(siteInspectionDatesList.isEmpty() || siteInspectionDatesList.size()<3){						
				siteInspectionDatesList.add(sdf1.format(siDate));
				siteInspectionDate=incrementDatebyNoOfDays(siDate,1);
			}
			else if(siteInspectionDatesList.size()>=3){
				break;
			}
		}				
	}
LOGGER.debug("siteInspectionDatesList "+siteInspectionDatesList);
return siteInspectionDatesList;
}
	
	public String incrementDatebyNoOfDays(Date date,int noOfDays)
	{
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(calendar.DAY_OF_MONTH, noOfDays);			
		return sdf.format(calendar.getTime());
	}
	
	public CFinancialYear getFinancialYear() {
		 FinancialYearDAO finYearDAO=null;
		 CFinancialYear finyear=null;
		 String financialYearId=null;
				 //CommonsDAOFactory.getFinancialYearDAO(); Pheonix TODO: commenting der is NO CommonsDAOFactory.getFinancialYearDAO()
		if(finYearDAO!=null){
		  financialYearId= finYearDAO.getCurrYearFiscalId();//String.valueOf(4);
		
		}
		if(financialYearId==null || financialYearId.equals(""))
			throw new EGOVRuntimeException("Financial year Id doesnot Exist.");
		else
			finyear=(CFinancialYear)finYearDAO.findById(Long.valueOf(financialYearId),false);
		return finyear;
	}
	//Pionix TODO:commenting as of now CalendarYear is not Present in PIMS
	
	/*public CalendarYear getCalendarYear() {
		//CalendarYear calYear = null;
		String calId = null;
		//calId = EisManagersUtill.getEmpLeaveManager().getCurrentYearId(); //TODO: Commenting in PIMS no getEmpLeaveManager().
		if(calId==null || calId.equals(""))
			throw new EGOVRuntimeException("Calendar year Id doesnot Exist.");
		else
		//calYear = EisManagersUtill.getEmpLeaveManager().getCalendarYearById(Long.valueOf(calId));
		return calYear;
	}*/
	
	public WorkFlowMatrix getPreviousStateFromWfMatrix(final String type,
			final String department, final BigDecimal amountRule,
			final String additionalRule, final String currentState,
			final String pendingActions) {
		LOGGER.debug("BpaCommonService ||Inside getPreviousStateFromWfMatrix method ");
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
		LOGGER.debug("BpaCommonService ||Exiting getPreviousStateFromWfMatrix method ");
		return null;

	}
	/*
	 * On  Approve letterToParty move record directly into Letter ToParty creator inbox...
	 */
	public Position getWfStatesByTypeandValue()
	{
		Position pos=null;
		List<org.egov.infra.workflow.entity.State> wfstate=persistenceService.findAllBy("from org.egov.infra.workflow.entity.State where type=? and value=? and nextAction=? order by id desc",BpaConstants.NEWREGISTRATION_WFSTATETYPE,BpaConstants.LECREATED_WFSTATETYPE,BpaConstants.LPRAISED_WFNEXTACTION);
		if(wfstate!=null)
			pos=wfstate.get(0).getOwnerPosition();//pheonix TODO:changing as EGI Changed into GetPrevious.getOwner to GetOnewPosition
		return pos;
	}
	/*
	 * On  Approve RegistrationExtn move record directly into Initial EE inbox inbox...
	 */
	public Position getWfStatesByTypeandValueForApproveRegistration()
	{
		Position pos=null;
		List<org.egov.infra.workflow.entity.State> wfstate=persistenceService.findAllBy("from org.egov.infra.workflow.entity.State where type=? and value=? and nextAction=? order by id desc",BpaConstants.NEWREGISTRATION_WFSTATETYPE,BpaConstants.LECREATED_WFSTATETYPE,BpaConstants.LPRAISED_WFNEXTACTION);
		if(wfstate!=null)
			pos=wfstate.get(0).getOwnerPosition();//.getPrevious().getOwner();
		//Pheonix TODO: in state table removed some column for Previous and Next so commenting
		return pos;
	}
	private Criteria commonWorkFlowMatrixCriteria(String type,String additionalRule,String currentState,String pendingActions){

		final Criteria commonWfMatrixCriteria=persistenceService.getSession().createCriteria(WorkFlowMatrix.class);
		commonWfMatrixCriteria.add(Restrictions.eq("objectType", type));

		if(StringUtils.isNotBlank(additionalRule)){
			commonWfMatrixCriteria.add(Restrictions.eq("additionalRule", additionalRule));
		}

		if(StringUtils.isNotBlank(pendingActions)){
			commonWfMatrixCriteria.add(Restrictions.ilike("nextAction", pendingActions,MatchMode.ANYWHERE));
		}

		if(StringUtils.isNotBlank(currentState)){
			commonWfMatrixCriteria.add(Restrictions.ilike("nextState", currentState,MatchMode.ANYWHERE));
		}
		//else {
		//	commonWfMatrixCriteria.add(Restrictions.ilike("nextState", "NEW",MatchMode.ANYWHERE));
		//}

		return commonWfMatrixCriteria;
	}
	
/*	 public WorkflowService<RegistrationFeeExtn> getRegistrationFeeWorkflowExtnService() {
			return registrationFeeWorkflowExtnService;
		}

		public void setRegistrationFeeWorkflowExtnService(
				WorkflowService<RegistrationFeeExtn> registrationFeeWorkflowService) {
			this.registrationFeeWorkflowExtnService = registrationFeeWorkflowService;
		}*/
	//Phionix TODO: commenting cos no workflowService 
		
		public String getEmpNameDesignation(Position position, Date date) {
			
			LOGGER.debug("BpaCommonService ||Starting getEmpNameDesignation method ");
			String empName = "";
			String designationName = "";

			if (position != null) {
				DeptDesig deptDesig = position.getDeptDesig();
				PersonalInformation personalInformation = null;
				try {
					//phionix TODO:added getEmpForPositionAndDate in eisCommonsManager
					personalInformation = employeeServiceImpl.getEmpForPositionAndDate(date,Integer.parseInt(position.getId().toString()));
					//pheonix TODO:getEmpForPositionAndDate no API IN PIMS
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
			LOGGER.debug("BpaCommonService ||Exiting getEmpNameDesignation method ");
			return empName + "@" + designationName;
		}
		
		public InputStream generateFeePaymentReportPDF(RegistrationExtn registration,List<ReportFeesDetailsExtn> reportFeeDetailsList)
		{
			LOGGER.debug("BpaCommonService ||Starting generateFeePaymentReportPDF method ");
			InputStream inputStream = null;
			ReportRequest reportRequest = null;
			String fromAddress="";
			BigDecimal finalFeeTobePaid= isFeeCollectionPending(registration);
		
		if (registration != null){	
			if (registration.getRegnStatusDetailsSet()!=null && !registration.getRegnStatusDetailsSet().isEmpty()){
				if(registration.getRegnStatusDetailsSet()!=null) {
					for(RegnStatusDetailsExtn regnStatDet : registration.getRegnStatusDetailsSet()) {
						if(regnStatDet.getStatus().getCode().equals(BpaConstants.CHALLANNOTICESENT)) {
							registration.setChallanDetails(regnStatDet);
						}					
					}
				}
			}
			
			//TODO: get feeid as new parameter for this api. By default use getLatestApprovedRegistrationFee api.
			
			RegistrationFeeExtn registrationFeeObj=getLatestApprovedRegistrationFee(registration);
			registration.setRegistrationFeeChallanNumber(registrationFeeObj!=null? registrationFeeObj.getChallanNumber():"NA");
			
			Map<String,Object> reportParams =createHeaderParams(registration,FEE_PAYMENT_PDF,finalFeeTobePaid,reportFeeDetailsList);
			
			//In case of revised cases, get challan date from fee object. Else get challan send date from status change object.
			if(registrationFeeObj!=null)
				reportParams.put("challanDate", registrationFeeObj.getFeeDate());
			else if(registration.getChallanDetails()!=null)
				reportParams.put("challanDate", registration.getChallanDetails().getStatusdate());
			
			if(registration!=null && registration.getAdminboundaryid()!=null && registration.getAdminboundaryid().getParent()!=null && registration.getAdminboundaryid().getParent().getParent()!=null)
			
				if(registration.getAdminboundaryid().getParent().getParent().getName().equalsIgnoreCase(BpaConstants.NORTHREGION))
				{
					fromAddress=BpaConstants.ASSISTANTADDRESS+ "\n" +BpaConstants.NORTHREGION_ADDRESS;
				}
				else if(registration.getAdminboundaryid().getParent().getParent().getName().equalsIgnoreCase(BpaConstants.SOUTHREGION))
				{
					fromAddress=BpaConstants.ASSISTANTADDRESS + "\n"  + BpaConstants.SOUTHREGION_ADDRESS;
											
				}
				else if(registration.getAdminboundaryid().getParent().getParent().getName().equalsIgnoreCase(BpaConstants.CENTRALREGION))
				{
					fromAddress=BpaConstants.ASSISTANTADDRESS + "\n" + BpaConstants.CENTRALREGION_ADDRESS;
				}
				else
				{
					fromAddress="";
				}
			
			reportParams.put("fromAddress",fromAddress);
			 Map<String,List<BpaFeeExtn>>  registrationFeesesByFeeGroup = null;
			 //phionix TODOD:
			 //=getGroupWiseFeeListByPassingRegistration(registration,registrationFeeObj!=null? registrationFeeObj.getId():null);
			reportParams.put("reportFeeList", registrationFeesesByFeeGroup.get(BpaConstants.COCFEE));
			//reportParams.put("reportFeeListForCMDA", registrationFeesesByFeeGroup.get(BpaConstants.CMDAFEE));
			//reportParams.put("reportFeeListForMWGWF", registrationFeesesByFeeGroup.get(BpaConstants.MWGWFFEE));	
			reportRequest = new ReportRequest("ChallanReportExtn",registration, reportParams);
			
			ReportOutput reportOutput = reportService.createReport(reportRequest);				
			if (reportOutput != null && reportOutput.getReportOutputData() != null)
				inputStream = new ByteArrayInputStream(reportOutput.getReportOutputData());
		}
		
		LOGGER.debug("BpaCommonService ||Exiting generateFeePaymentReportPDF method ");
			return inputStream;
		}

		
		public Map<String,Object> createHeaderParams(RegistrationExtn registration, String type,BigDecimal finalAmount,List<ReportFeesDetailsExtn> reportFeeDetailsList){
			Map<String,Object> reportParams = new HashMap<String,Object>();
			
			if(type.equalsIgnoreCase(FEE_PAYMENT_PDF)){
				reportParams.put("planSubmissionNumber", registration.getPlanSubmissionNum());
				reportParams.put("dateofPlanSubmission", registration.getPlanSubmissionDate());
				reportParams.put("applicantName", registration.getOwner().getName());
				reportParams.put("applicantAddress", registration.getBpaOwnerAddress());
				reportParams.put("reportFeeList", reportFeeDetailsList);
			}
			return reportParams; 
		}
		
		public RegistrationFeeExtn getLatestApprovedRegistrationFee(RegistrationExtn registrationObj){
			LOGGER.debug("BpaCommonService ||Starting getLatestApprovedRegistrationFee method ");
			if(registrationObj!=null){
			Criteria feeCrit=persistenceService.getSession().createCriteria(RegistrationFeeExtn.class).createAlias("egwStatus", "status");
			feeCrit.add(Restrictions.eq("registration.id", registrationObj.getId()));
			feeCrit.add(Restrictions.eq("status.code", "Approved"));
			feeCrit.addOrder(Order.desc("id"));
			List<RegistrationFeeExtn> regFeeList =  feeCrit.list();
			LOGGER.debug("BpaCommonService ||Exiting getLatestApprovedRegistrationFee method ");
				return regFeeList.isEmpty()?null:regFeeList.get(0);
			 }
			return null;
		}
		public RegistrationFeeExtn getRegistrationFeeById(Long registrationFeeId) {
			 LOGGER.debug("Enter getRegistrationFee inside bpacommon service");
			 if(registrationFeeId!=null){
			Criteria feedtlCrit=persistenceService.getSession().createCriteria(RegistrationFeeExtn.class);
			feedtlCrit.add(Restrictions.eq("id", registrationFeeId));
			 LOGGER.debug("Exit getRegistrationFee inside bpacommon service");
			 return (RegistrationFeeExtn) feedtlCrit.uniqueResult();
			 }
			return null;
			}
		public BoundaryType  getBoundaryTypeByPassingBoundaryTypeAndHierarchy(String boundaryType, String heirarchyType)
		{
			HierarchyType hierarchy=null;
			try {
				hierarchy = hierarchyTypeService.getHierarchyTypeByName(heirarchyType);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} /*catch (TooManyValuesException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			if(hierarchy!=null)
				return boundaryTypeService.getBoundaryTypeByNameAndHierarchyType(boundaryType,hierarchy);
			
			return null;
		}
		/*
		 * By passing Street Id Get Ward Object means crossHierarchyParent ..
		 */
		public Set<Boundary> getBoundaryParentObject(Long adminBoundaryId)
		{
			Set<Boundary> wardboundry=new HashSet<Boundary>();
			if(adminBoundaryId!=null){
			Boundary childrenBoundary=boundaryService.getActiveBoundaryByIdAsOnCurrentDate(adminBoundaryId);
			//wardboundry = hierarchyTypeService.getCrossHeirarchyParent(childrenBoundary);TODO phionix
			}
			return wardboundry;
		}
		/*
		 * Moved From approvalInformationService to cpaCommonservice..
		 * TODO:Code reviwed by Pradeep
		 */
		public List<ChangeOfUsageExtn> getChangeOfUse() {

			List<ChangeOfUsageExtn> changeOfUseList = persistenceService.findAllBy("from ChangeOfUsageExtn VN where VN.isActive=1");
			
			return changeOfUseList;
		}
		 public List<RegnApprovalInformationExtn> getRegnApprovalInfobyRegistrationId(RegistrationExtn registrationObj){
			
			 return (List<RegnApprovalInformationExtn>)  persistenceService.findAllBy("from RegnApprovalInformationExtn where registration=? order by id desc",registrationObj);

			}
		 public List<DocumentHistoryExtn> getRegnDocumentHistoryObject(RegistrationExtn registrationId){
			 List<DocumentHistoryExtn>  docHistoryList=null;
				if(registrationId!=null){
			  docHistoryList=  persistenceService.findAllBy("from DocumentHistoryExtn where registrationId=? order by id desc",registrationId);
			 return docHistoryList;
				}
				else 
				{
					return docHistoryList;
				}
			}

		public void setBpaSurvayorPortalExtnService(
				BpaSurvayorPortalExtnService bpaSurvayorPortalExtnService) {
			this.bpaSurvayorPortalExtnService = bpaSurvayorPortalExtnService;
		}
		
		 public List<StormWaterDrainExtn> getAllStormWaterDrain(){
				
			 return (List<StormWaterDrainExtn>)  persistenceService.findAllBy("from StormWaterDrainExtn order by id asc");

			}

		
		public Object getFirstWorkflowItemForType(List owner, Integer userId,
					String wfType, String subType) {
				LOGGER.debug("BPACommonExtnService.getFirstWorkflowItemForType() started for " + owner + userId + wfType + subType);
				String regnDtls[] = null;
				final StringBuilder query = new StringBuilder(" FROM ");
		        query.append(wfType).append(
		                " WF left join fetch WF.state ST where ST.type=:wfType and ST.owner in(:owner) and " + 
		                "ST.next is null and ST.value !=:end and " + "" +
		                "not (ST.value =:new and WF.createdBy =:userId) ");
		        	if(subType!=null) {
		        		regnDtls=subType.split(",");
		        		if(regnDtls.length>0 && regnDtls[0]!=null && regnDtls[0]!="" && wfType.equalsIgnoreCase(OBJECT_REG_EXTN)) {
		        			query.append(" and WF.state.value=:category ") ; 
		        		}
		        		if(regnDtls.length>0 && regnDtls[1]!=null && regnDtls[1]!="" && wfType.equalsIgnoreCase(OBJECT_REG_EXTN)) {
		        			query.append(" and WF.serviceType.id=:sTypeId ") ; 
		        		}
		        	}
		                query.append("order by ST.createdDate ASC");
				final Query qry = this.persistenceService.getSession().createQuery(query.toString());
				qry.setMaxResults(1);
				qry.setParameterList("owner", owner);
				qry.setString("wfType", wfType);
			//	qry.setString("end", State.END);  TODO:NEW is pointing some NEW.class and NO END in State.java
				//qry.setString("new", State.NEW);
				qry.setInteger("userId", userId);
				if(subType!=null) {
					if(regnDtls.length>0 && regnDtls[0]!=null && regnDtls[0]!="")	
						qry.setString("category", regnDtls[0]);
					if(regnDtls.length>0 && regnDtls[1]!=null && regnDtls[1]!="")
						qry.setString("sTypeId", regnDtls[1]);
				}
				qry.setReadOnly(true);
				Object results = qry.uniqueResult();
				LOGGER.debug("BPACommonExtnService.getOldestWorkflowItemForType() started for " + owner + userId + wfType + subType);
				return results;
			}
		 public List<DocumentHistoryExtn>getAllDocumentHistoryList(RegistrationExtn registrationId)
		 {
			 List<DocumentHistoryExtn>  docHistoryList=null; 
			 docHistoryList=  persistenceService.findAllBy("from DocumentHistoryExtn where registrationId=? order by id desc",registrationId);
			return docHistoryList;
		 }
		 
		 public List<DocumentHistoryExtn> getRegnDocumentHistoryObjectForAEorAEE(RegistrationExtn registrationId){
			 List<DocumentHistoryExtn>  docHistoryList=null;
				if(registrationId!=null && isUserMappedToAeOrAeeRole()){ 
					docHistoryList= persistenceService.findAllBy("from DocumentHistoryExtn where registrationId=? and createdUser.id=? order by id desc",registrationId, (EgovThreadLocals.getUserId()));
					return docHistoryList;
				}
				
				return docHistoryList;
				}
		 public List<DocumentHistoryExtnDetails> getRegnDocumentHistoryDetailObject(DocumentHistoryExtn documentHistoryId){
			 List<DocumentHistoryExtnDetails>  docHistoryList=null;
				if(documentHistoryId!=null){
			  docHistoryList=  persistenceService.findAllBy("from DocumentHistoryExtnDetails where docHistoryId=? order by id desc",documentHistoryId);
			 return docHistoryList;
				}
				else 
				{
					return docHistoryList;
				}
			}
		 public  Map<String,Object> generateCmdaLetterToPartyPrint(RegistrationExtn registration,Long cmdaLpid) 
			{
				
				Map<String,Object> reportData = new HashMap<String,Object>();
				 String fromAddressToLp="";
				 String address ="";
				 CMDALetterToPartyExtn lpReply=null;
				 if(registration!=null){
				 List<CMDALetterToPartyExtn> lpReplyList = getcmdaLetterToPartyForRegistrationObject(registration);
				  if(lpReplyList!=null && lpReplyList.size()>0){
					  lpReply=lpReplyList.get(0);
				  }
				 }
				  else if (cmdaLpid!=null)
					  {
						  lpReply=(CMDALetterToPartyExtn) persistenceService. find("from CMDALetterToPartyExtn where id=?  order by id desc",cmdaLpid);	 
					  }
				  String EMPTYSTRING = "";
					if(registration!=null){
						 address = registration.getBpaOwnerAddress();
					if(registration!=null && registration.getAdminboundaryid()!=null)
					{
					if(registration.getAdminboundaryid().getParent()!=null && registration.getAdminboundaryid().getParent().getParent()!=null)
					{
						reportData.put("region",registration.getAdminboundaryid().getParent().getParent().getName());
					}
					if(registration.getAdminboundaryid().getParent()!=null)
						reportData.put("zone", registration.getAdminboundaryid().getParent().getName());
					else
						reportData.put("zone", EMPTYSTRING);
					
					reportData.put("ward", registration.getAdminboundaryid().getName());
					
					}
					reportData.put("planSubmissionNum", registration.getPlanSubmissionNum());
					if( registration!=null && registration.getOwner()!=null)
						reportData.put("applicantName", registration.getOwner().getName());
					else
						reportData.put("applicantName", EMPTYSTRING);
					reportData.put("cmdaDate", registration.getCmdaRefDate()!=null ? registration.getCmdaRefDate():null);
					
					reportData.put("cmdaNumber", registration.getCmdaNum()!=null && registration.getCmdaNum()!=""? registration.getCmdaNum():"");
					
					}
					else
					{
						 address = lpReply.getRegistration().getBpaOwnerAddress();
						if(registration!=null && registration.getAdminboundaryid()!=null)
						{
						if(lpReply.getRegistration().getAdminboundaryid().getParent()!=null && lpReply.getRegistration().getAdminboundaryid().getParent().getParent()!=null)
						{
							reportData.put("region",lpReply.getRegistration().getAdminboundaryid().getParent().getParent().getName());
						}
						if(lpReply.getRegistration().getAdminboundaryid().getParent()!=null)
							reportData.put("zone", lpReply.getRegistration().getAdminboundaryid().getParent().getName());
						else
							reportData.put("zone", EMPTYSTRING);
						
						reportData.put("ward", lpReply.getRegistration().getAdminboundaryid().getName());
						
						}
						reportData.put("planSubmissionNum", lpReply.getRegistration().getPlanSubmissionNum());
						if( lpReply.getRegistration()!=null && lpReply.getRegistration().getOwner()!=null)
							reportData.put("applicantName", lpReply.getRegistration().getOwner().getName());
						else
							reportData.put("applicantName", EMPTYSTRING);
						reportData.put("cmdaDate", lpReply.getRegistration().getCmdaRefDate()!=null ? lpReply.getRegistration().getCmdaRefDate():null);
						
						reportData.put("cmdaNumber", lpReply.getRegistration().getCmdaNum()!=null && lpReply.getRegistration().getCmdaNum()!=""? lpReply.getRegistration().getCmdaNum():"");
						
					}
					if(lpReply!=null && lpReply.getRegistration().getAdminboundaryid()!=null && lpReply.getRegistration().getAdminboundaryid().getParent()!=null && lpReply.getRegistration().getAdminboundaryid().getParent().getParent()!=null)
						
						if(lpReply.getRegistration().getAdminboundaryid().getParent().getParent().getName().equalsIgnoreCase(BpaConstants.NORTHREGION))
						{
							fromAddressToLp=BpaConstants.ASSISTANTADDRESS+ "\n" +BpaConstants.NORTHREGION_ADDRESS;
						}
						else if(lpReply.getRegistration().getAdminboundaryid().getParent().getParent().getName().equalsIgnoreCase(BpaConstants.SOUTHREGION))
						{
							fromAddressToLp=BpaConstants.ASSISTANTADDRESS + "\n"  + BpaConstants.SOUTHREGION_ADDRESS;
													
						}
						else if(lpReply.getRegistration().getAdminboundaryid().getParent().getParent().getName().equalsIgnoreCase(BpaConstants.CENTRALREGION))
						{
							fromAddressToLp=BpaConstants.ASSISTANTADDRESS + "\n" + BpaConstants.CENTRALREGION_ADDRESS;
						}
						else
						{
							fromAddressToLp=EMPTYSTRING;
						}
					reportData.put("fromAddressToLp", fromAddressToLp);
					
					reportData.put("address", address);
					reportData.put("ackNo", lpReply.getAcknowledgementNumber());
					reportData.put("replyDate", lpReply.getReplyDate());
					reportData.put("lpDate", lpReply.getLetterToParty().getLetterDate()!=null ?lpReply.getLetterToParty().getLetterDate():null);
					reportData.put("lpNumber", lpReply.getLetterToPartyNumber());
					reportData.put("lpReason", lpReply.getLpReason());
					
					if(lpReply.getLpReplyRemarks()!=null) {
						reportData.put("remarks", lpReply.getLpReplyRemarks());
					}
				return reportData;
			}
		/*@Override
		public EisCommonService getEisCommonService() {
			return eisCommonService;
		}

		public void setEisCommonService(EisCommonService eisCommonService) {
			this.eisCommonService = eisCommonService;
		}*/

		public EmployeeServiceImpl getEmployeeServiceImpl() {
			return employeeServiceImpl;
		}

		public void setEmployeeServiceImpl(EmployeeServiceImpl employeeServiceImpl) {
			this.employeeServiceImpl = employeeServiceImpl;
		}
		
}
