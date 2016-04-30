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
package org.egov.bpa.web.actions.extd.register;

import net.sf.jasperreports.engine.JRException;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.bpa.constants.BpaConstants;
import org.egov.bpa.models.extd.*;
import org.egov.bpa.models.extd.masters.BpaFeeExtn;
import org.egov.bpa.models.extd.masters.BuildingCategoryExtn;
import org.egov.bpa.models.extd.masters.CheckListDetailsExtn;
import org.egov.bpa.models.extd.masters.ChecklistExtn;
import org.egov.bpa.models.extd.masters.DocumentHistoryExtn;
import org.egov.bpa.models.extd.masters.DocumentHistoryExtnDetails;
import org.egov.bpa.models.extd.masters.LpReasonExtn;
import org.egov.bpa.models.extd.masters.ServiceTypeExtn;
import org.egov.bpa.models.extd.masters.SurveyorNameExtn;
import org.egov.bpa.models.extd.masters.VillageNameExtn;
import org.egov.bpa.services.extd.Fee.RegistrationFeeExtnService;
import org.egov.bpa.services.extd.autoDcr.AutoDcrExtnService;
import org.egov.bpa.services.extd.bill.BpaBillExtnServiceImpl;
import org.egov.bpa.services.extd.bill.BpaBillableExtn;
import org.egov.bpa.services.extd.common.BpaCommonExtnService;
import org.egov.bpa.services.extd.common.BpaNumberGenerationExtnService;
import org.egov.bpa.services.extd.common.BpaPimsInternalExtnServiceFactory;
import org.egov.bpa.services.extd.common.FeeExtnService;
import org.egov.bpa.services.extd.common.RegnStatusDetailExtnService;
import org.egov.bpa.services.extd.common.UtilsExtnService;
import org.egov.bpa.services.extd.inspection.InspectionExtnService;
import org.egov.bpa.services.extd.lettertoparty.LetterToPartyExtnService;
import org.egov.bpa.services.extd.register.RegisterBpaExtnService;
import org.egov.bpa.services.extd.report.BpaReportExtnService;
import org.egov.bpa.utils.ApplicationMode;
import org.egov.bpa.web.actions.common.BpaRuleBook;
import org.egov.bpa.web.actions.extd.common.BpaExtnRuleBook;
import org.egov.collection.integration.models.BillReceiptInfo;
import org.egov.commons.EgwStatus;
import org.egov.commons.ObjectHistory;
import org.egov.demand.model.EgDemand;
import org.egov.eis.entity.EmployeeView;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.reporting.viewer.ReportViewerUtil;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infra.web.struts.actions.workflow.GenericWorkFlowAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infra.workflow.entity.State;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.workflow.WorkFlowMatrix;
import org.egov.pims.commons.Position;
import org.egov.pims.service.EisUtilService;
import org.egov.portal.entity.Citizen;
import org.hibernate.Criteria;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/*import org.egov.commons.service.CommonsManager;*/
/*import org.egov.erpcollection.integration.models.BillReceiptInfo;*/
/*import org.egov.pims.service.EisManager;*/

@Transactional(readOnly = true)
@Namespace("/extd/register")
@SuppressWarnings("serial")
@Results({ // Phionix TODO
		@Result(name = "NOACCESS", type = "stream", location = "returnStream", params = {
				"contentType", "text/plain" }),
		@Result(name = RegisterBpaExtnAction.FEE_PAYMENT_PDF, type = "stream", location = "feePaymentReportPDF", params = {
				"contentType", "application/pdf" }) })
@ParentPackage("egov")
public class RegisterBpaExtnAction extends GenericWorkFlowAction {

	private Logger LOGGER = Logger.getLogger(getClass());
	protected RegistrationExtn registration = new RegistrationExtn();
	protected String collectXML;
	private AutoDcrExtnService autoDcrExtnService;
	private String existPpaNumber;
	private String existBaNumber;
	protected Long existingbuildingCategoryId;
	protected Long proposedbuildingCategoryId;
	protected Integer admissionfeeAmount;
	List<ChecklistExtn> documentationchecklist;
	protected RegisterBpaExtnService registerBpaExtnService;
	private String mode;
	protected Long registrationId;
	protected String boundaryStateId;
	private FeeExtnService feeExtnService;
	protected BpaAddressExtn applicantrAddress;
	protected BpaAddressExtn siteAddress;
	protected DocumentHistoryExtn documentHistory;
	private State boundaryState;
	protected InspectionExtnService inspectionExtnService;
	protected List<InspectionExtn> postponedInspectionDetails = new ArrayList<InspectionExtn>();
	private List<RegistrationChecklistExtn> chkListDet = new ArrayList<RegistrationChecklistExtn>();
	private Long serviceTypeIdTemp;
	protected List<DocumentHistoryExtnDetails> documentHistoryDetailList = new ArrayList<DocumentHistoryExtnDetails>();
	private List<InspectionExtn> existingSiteInspectionDetails = new ArrayList<InspectionExtn>();
	private Long inspectionId;
	protected ReportService reportService;
	private RegistrationFeeExtnService registrationFeeExtnService;
	private BpaReportExtnService bpaReportExtnService;
	private Integer reportId = -1;
	private String documentNum;
	private String autonum;
	protected String mobileNumber;
	protected String emailId;
	protected BpaCommonExtnService bpaCommonExtnService;
	protected List<String> showActions = new ArrayList<String>();
	private List<String> showOrdAction = new ArrayList<String>();
	protected BpaBillExtnServiceImpl bpaBillExtnServiceImpl;
	protected BpaBillableExtn bpaBillableExtn;
	private BigDecimal totalcmda = BigDecimal.ZERO;
	private BigDecimal totalmwgwf = BigDecimal.ZERO;
	private String stateForValidate;
	private String approvedWorkflowState = "";
	private String printMode;
	private Map<String, String> applicantStatus = new LinkedHashMap<String, String>();
	// ----lp
	private List<LpChecklistExtn> lpChkListDet = new ArrayList<LpChecklistExtn>();
	private String returnStream = "You Have no permission to view";
	protected List<LetterToPartyExtn> existingLetterToPartyDetails = new ArrayList<LetterToPartyExtn>();
	protected List<CMDALetterToPartyExtn> existingCmdaLetterToPartyDetails = new ArrayList<CMDALetterToPartyExtn>();
	private LetterToPartyExtn letterToParty = new LetterToPartyExtn();
	private Long letterToPartyId;
	private Long letterToPartyCmdaId;
	private String additionalRule;
	private List<RejectionChecklistExtn> rejectChkListDet = new ArrayList<RejectionChecklistExtn>();
	private RegnStatusDetailExtnService regnStatusDetExtnService;
	private String rejectionMode;
	protected String autoDcrNum;
	public static final String FEE_PAYMENT_PDF = "feePaymentPdf";
	protected InputStream feePaymentReportPDF;
	private BpaNumberGenerationExtnService bpaNumberGenerationExtnService;
	private String reqdAction;
	private RegistrationExtn existReg;
	protected VillageNameExtn villgeobj;
	// protected Surveyor surveyorObj;//Phionix TODO
	private String rejectview;
	private String isPlotAreaEditable;
	private String additionalMode;// Phionix TODO

	protected Map<String, BillReceiptInfo> billReceiptInfoMap = new HashMap<String, BillReceiptInfo>();
	protected List<BillReceiptInfo> billRecptInfoList = new ArrayList<BillReceiptInfo>();
	private List<LpChecklistExtn> lpReplyChkListDet = new ArrayList<LpChecklistExtn>();
	private List<RegistrationDDDetailsExtn> labourWelfareddList = new ArrayList<RegistrationDDDetailsExtn>();
	private List<RegistrationDDDetailsExtn> cmdaddList = new ArrayList<RegistrationDDDetailsExtn>();
	private static final String LETTERTOPARTYFORM = "letterToPartyForm";
	private Date applicationdate;
	private List<DocketDocumentDetails> docketDocumentDtls = new ArrayList<DocketDocumentDetails>();
	private List<DocketConstructionStage> constructionStages = new ArrayList<DocketConstructionStage>();
	private List<DocketFloorDetails> docketFloorDetail = new ArrayList<DocketFloorDetails>();
	private List<DocketViolations> devContrlList = new ArrayList<DocketViolations>();
	private List<DocketViolations> setBackList = new ArrayList<DocketViolations>();
	private List<DocketViolations> parkingList = new ArrayList<DocketViolations>();
	private List<DocketViolations> generalList = new ArrayList<DocketViolations>();
	private Docket docket = new Docket();
	protected Boolean enableDocketSheetForView = Boolean.FALSE;
	private List<DocketViolations> minDistancePowerLineList = new ArrayList<DocketViolations>();
	private Date validateDate;
	private Integer approverDepartment;
	private Integer approverPositionId;
	private Integer approverDesignation;
	protected BpaPimsInternalExtnServiceFactory bpaPimsExtnFactory;
	private Boolean ppanum;
	private String bparoles;
	private List<AppConfigValues> appConfigValuesRoleList;
	// private CommonsManager commonsManager;
	private LetterToPartyExtnService letterToPartyExtnService;
	private BigDecimal mwgwfAmounttotal;
	private BigDecimal cmdaAmounttotal;
	protected String surveyorNameLocal;
	protected String surveyorCode;
	protected String surveyorClass;
	protected Long Surveyor;
	protected String userRole;
	protected Boolean currentstate;
	private EisUtilService eisService;
	private Boolean forwardNotApplicable = false;
	private Boolean serviceTypeCode = false;
	UtilsExtnService utilsExtnService = new UtilsExtnService();
	private String previousApproverComments;
	private Boolean showSave_ActionField = Boolean.TRUE;
	private Boolean isDocHistoryByAEorAEE = Boolean.FALSE;
	protected PersistenceService<RegDocumentUpload, Long> regDocUploadService;
	private List<RegnOfficialActionsExtnView> regnOfficialActionsList = new LinkedList<RegnOfficialActionsExtnView>();
	protected PersistenceService<RegnOfficialActionsExtn, Long> regnOfficialActionsExtnService;
	// private EisManager eisMgr;

	private CMDALetterToPartyExtn letterToCMDA = new CMDALetterToPartyExtn();

	public PersistenceService<RegDocumentUpload, Long> getRegDocUploadService() {
		return regDocUploadService;
	}

	public void setRegDocUploadService(
			PersistenceService<RegDocumentUpload, Long> regDocUploadService) {
		this.regDocUploadService = regDocUploadService;
	}

	public LetterToPartyExtnService getLetterToPartyExtnService() {
		return letterToPartyExtnService;
	}

	public void setLetterToPartyExtnService(
			LetterToPartyExtnService letterToPartyService) {
		this.letterToPartyExtnService = letterToPartyService;
	}

	private String regObjectHistoryRemarks;

	public RegisterBpaExtnAction() {
		this.addRelatedEntity("owner", Citizen.class);
		this.addRelatedEntity("adminboundaryid", Boundary.class);
		this.addRelatedEntity("locboundaryid", Boundary.class);
		this.addRelatedEntity("modifiedBy", User.class);
		this.addRelatedEntity("createdBy", User.class);
		this.addRelatedEntity("serviceType", ServiceTypeExtn.class);
		this.addRelatedEntity("surveyorName", SurveyorNameExtn.class);
		this.addRelatedEntity("state",
				org.egov.infra.workflow.entity.State.class);
		this.addRelatedEntity("regnDetails", RegnDetailsExtn.class);
		this.addRelatedEntity("regnDetails.existingBldgCatg",
				BuildingCategoryExtn.class);
		this.addRelatedEntity("regnDetails.proposedBldgCatg",
				BuildingCategoryExtn.class);
		this.addRelatedEntity("egDemand", EgDemand.class);
		this.addRelatedEntity("egwStatus", EgwStatus.class);
		this.addRelatedEntity("documentHistory", DocumentHistoryExtn.class);
		// lp
		this.addRelatedEntity("letterToParty", LetterToPartyExtn.class);
		this.addRelatedEntity("letterToParty.letterToPartyReason",
				LpReasonExtn.class);
		this.addRelatedEntity("rejection", RejectionExtn.class);
		this.addRelatedEntity("signDetails", RegnStatusDetailsExtn.class);
		this.addRelatedEntity("orderDetails", RegnStatusDetailsExtn.class);
		this.addRelatedEntity("orderIssueDet", RegnStatusDetailsExtn.class);
		this.addRelatedEntity("rejectOrdPrepDet", RegnStatusDetailsExtn.class);
		this.addRelatedEntity("rejectOrdIssDet", RegnStatusDetailsExtn.class);
		this.addRelatedEntity("challanDetails", RegnStatusDetailsExtn.class);
		this.addRelatedEntity("uploadFile", File.class);
		// this.addRelatedEntity("docUpload", RegDocumentUpload.class);
		// this.addRelatedEntity("documentList", RegDocumentUploadDtls.class);

	}

	public RegistrationExtn getModel() {
		return registration;
	}

	@SkipValidation
	public void prepare() {
		super.prepare();
		addDropdownData("serviceTypeList",
				bpaCommonExtnService.getAllActiveServiceTypeList());
		addDropdownData("proposedBuildingCategoryList",
				bpaCommonExtnService.getAllActiveBuildingCategoryList());
		addDropdownData("existingBuildingCategoryList",
				bpaCommonExtnService.getAllActiveBuildingCategoryList());
		// addDropdownData("surveyorNameList",
		// bpaCommonExtnService.getAllActiveSurveyorNameList());
		// Phionix TODO
		addDropdownData("bndryStateList",
				bpaCommonExtnService.getAllStatesOfIndia());
		addDropdownData("letterToPartyReasonList",
				bpaCommonExtnService.getAllLpReasonList());
		addDropdownData("bankList", bpaCommonExtnService.getAllBanks());
		addDropdownData("usageOfConstructionList",
				Arrays.asList(UsageOfConstruction.values()));
		applicantStatus = new LinkedHashMap<String, String>();
		applicantStatus.put(ApplicantStatus.OWNER.toString(),
				ApplicantStatus.OWNER.toString());
		applicantStatus.put(ApplicantStatus.LASSEE.toString(),
				ApplicantStatus.LASSEE.toString());
		applicantStatus.put(ApplicantStatus.POWER_OF_ATTORNEY.toString(),
				ApplicantStatus.POWER_OF_ATTORNEY.toString());

		addDropdownData("applicationModeList",
				Arrays.asList(ApplicationMode.values()));
		if (registration != null
				&& registration.getPlanSubmissionDate() != null)
			registration.setPlanSubmissionDate(new Date());

		if (registration != null && registration.getDocumentid() != null)
			this.setDocumentNum(registration.getDocumentid());
		appConfigValuesRoleList = bpaCommonExtnService.getAppConfigValue("BPA",
				BpaConstants.ROLELISTFORBPA);
		LOGGER.info(appConfigValuesRoleList.size());
		if (!appConfigValuesRoleList.isEmpty()) {
			bparoles = appConfigValuesRoleList.get(0).getValue();
		}
		userRole = bpaCommonExtnService.getUserRolesForLoggedInUser();
		if (userRole.contains(BpaConstants.PORTALUSERSURVEYORROLE)) {
			userRole = BpaConstants.PORTALUSERSURVEYORROLE;
		}
		User user = bpaCommonExtnService.getUserbyId((EgovThreadLocals
				.getUserId()));
		if (workFlowAction == null && user != null) {// &&
														// user.getIsPortalUser()==Boolean.TRUE))
			// Phionix TODO
			loggedInUserActions();
			if (!(showActions != null && showActions.size() != 0))
				showSave_ActionField = Boolean.FALSE; // If no actions to
														// perform, Do not show
														// save and action
														// buttons
		}
	}

	@SkipValidation
	@Action(value = "/registerBpaExtn-newForm", results = { @Result(name = NEW,type = "dispatcher") })
	public String newForm() {
		setApplicantrAddress(new BpaAddressExtn());
		setSiteAddress(new BpaAddressExtn());
		return NEW;
	}

	/*
	 * public Integer getZonalLevelAssistantForLoggedInUser() {
	 * 
	 * List<EmployeeView> employeeList=bpaPimsFactory.getEmployeeInfoList();
	 * if(employeeList!=null&&employeeList.size()!=0){ Integer posId=
	 * employeeList.get(0).getPosition().getId(); return posId; }else{ return
	 * null; } }
	 */

	/**
	 * @Description Returns position of a logged in user based on
	 *              userdepartment, designation and boundary
	 * @return
	 */
	// Phionix TODO
	public Long getZonalLevelAssistantForLoggedInUser() {
		Position pos = bpaPimsExtnFactory.getEmployeeInfoList(registration
				.getAdminboundaryid());
		if (pos != null) {
			Long posId = pos.getId();
			return posId;
		} else {
			return null;
		}
	}

	// Phionix TODO
	public Long getDepartmentForLoggedInUser() {

		return bpaPimsExtnFactory.getPrimaryDepartmentforLoggedinUser();
	}

	@ValidationErrorPage(NEW)
	@Action(value = "/registerBpaExtn-closeRegistration", results = { @Result(name = NEW,type = "dispatcher") })
	public String closeRegistration() {

		LOGGER.info(" closeRegistration || start");
		// registration=registerBpaService.closeRegistration(registration,workFlowAction,approverComments);
		registration = registerBpaExtnService
				.getRegistrationById(registrationId);
		registerBpaExtnService.closeRegistration(registration, workFlowAction,
				approverComments);
		confirmationAlertMessages();
		setMode(BpaConstants.MODEVIEW);
		LOGGER.info(" closeRegistration || end");
		return NEW;
	}

	/**
	 * @description returns Approvers having less work load
	 * @param employeeViewList
	 * @param designationId
	 * @return
	 */
	// Phionix TODO
	public Integer autoSelectApproverPosition(
			List<EmployeeView> employeeViewList, List<Long> designationId) {
		List<Long> posId = new ArrayList<Long>();
		if (employeeViewList != null && employeeViewList.size() != 0) {
			for (EmployeeView emp : employeeViewList) {
				posId.add(emp.getPosition().getId());
			}
			if (!posId.isEmpty()) {
				List<Object[]> result = utilsExtnService
						.getLessLoadedApproversForBpa(designationId, posId);
				if (result != null && !result.isEmpty()) {
					Object[] obj = result.get(0);
					if (obj.length != 0) {
						return Integer.valueOf(obj[0].toString());
					}
				}
			}
		}
		return null;
	}

	@ValidationErrorPage(NEW)
	@Action(value = "/registerBpaExtn-save", results = { @Result(name = NEW,type = "dispatcher") })
	public String save() {

		LOGGER.info(" RegisterBpaAction || Save || Start");
		List<Map<String, Object>> finalAttachmentList = new ArrayList<Map<String, Object>>();

		try {
			if (siteAddress != null && siteAddress.getVillageName() != null) {
				villgeobj = ((VillageNameExtn) persistenceService.find(
						"from VillageNameExtn where id=?", siteAddress
								.getVillageName().getId()));
				siteAddress.setVillageName(villgeobj);
			}
			setCheckListForRegistration();
			setDocumentNumberForRegistration();
			setRejectionCheckListForRegistration();

			/*
			 * if(getSurveyorCode()!=null && !"".equals(getSurveyorCode())) {
			 * surveyorObj=bpaCommonExtnService.getSurveyour(getSurveyor());
			 * registration.setSurveyorName(surveyorObj);
			 * 
			 * }
			 */// Phionix TODO

			/*
			 * If record saved first time and admission fees amount is greater
			 * than zero. then no need to forward this record to second level
			 * user, We need to collect admission fee first. Resetting forward
			 * user id in temp field and redirecting to collection screen.
			 */
			if (workFlowAction != null
					&& !"".equals(workFlowAction)
					&& !(BpaConstants.SCRIPT_SAVE
							.equalsIgnoreCase(workFlowAction) || BpaConstants.SCRIPT_REJECT
							.equalsIgnoreCase(workFlowAction))) {

				if (workFlowAction.equalsIgnoreCase("Approve Registration")
						&& registration != null
						&& registration.getState() != null
						&& registration.getState().getValue()
								.equalsIgnoreCase("Forwarded to Approval")) {
					List<EmployeeView> employeeList = bpaPimsExtnFactory
							.getEmployeeInfoList();// Phionix TODO
					List<Long> designationId = new ArrayList<Long>();
					if (employeeList != null && employeeList.size() != 0) {
						designationId
								.add(employeeList.get(0).getDepartment() != null ? (long) employeeList
										.get(0).getDepartment().getId()
										: null);
					}// Phionix TODO
					registration
							.setApproverPositionId((long) autoSelectApproverPosition(
									employeeList, designationId));
				}

				if ((admissionfeeAmount != null && admissionfeeAmount > 0)
						&& registration.getApproverPositionId() != null) {
					registration.setApproverId(registration
							.getApproverPositionId());
					// Phionix TODO
				}
			}
			if (!enableDocketSheetForView
					&& !mode.equals("reject")
					&& additionalRule != null
					&& !additionalRule
							.equals(BpaConstants.LETTERTOPARTYDETAILS)
					&& !additionalRule.equals(BpaConstants.LETTERTOCMDA)) {
				// For surviceType where code 1,3,6 populate values from
				// Surveyor Entries and Save with New Record For Official Users.
				String regServType = registration != null ? registration
						.getServiceType().getCode() : null;
				List<DocumentHistoryExtnDetails> documentHistodetailTempList = new ArrayList<DocumentHistoryExtnDetails>();
				if (regServType != null
						&& regServType
								.equals(BpaConstants.NEWBUILDINGONVACANTPLOTCODE)
						|| regServType
								.equals(BpaConstants.DEMOLITIONRECONSTRUCTIONCODE)
						|| regServType
								.equals(BpaConstants.ADDITIONALCONSTRUCTIONCODE)) {
					List<DocumentHistoryExtn> docHistoryFromOfficial = bpaCommonExtnService
							.getRegnDocumentHistoryObjectForAEorAEE(registration);
					// docHistoryFromOfficial this will give DocumentHistory
					// entered by AE/AEE .. if docHistoryFromOfficial Empty den
					// Populate VAlues from Surveyor and set Id for
					// DocumentHistory is NULL and Save New DocumentHistory and
					// details.
					if (docHistoryFromOfficial != null
							&& docHistoryFromOfficial.isEmpty()) {
						getDocumentHistory().setId(null);
						for (DocumentHistoryExtnDetails docdetail : documentHistoryDetailList) {
							DocumentHistoryExtnDetails docDetObj = new DocumentHistoryExtnDetails();
							docDetObj = docdetail;
							docDetObj.setId(null);
							documentHistodetailTempList.add(docDetObj);
						}
					} else {
						// MODIFY AE/AEE documentHistory
						documentHistodetailTempList = documentHistoryDetailList;
					}
				}
				// other than Servicetypes 1,3,6 assigning DocumenthistoryList
				// From UI
				else {
					documentHistodetailTempList = documentHistoryDetailList;
				}
				builddocumentHistoryExtraDeatils(documentHistory,
						documentHistodetailTempList);
			}

			registration = registerBpaExtnService.createBpa(registration,
					applicantrAddress, autoDcrNum, siteAddress,
					boundaryStateId, workFlowAction, approverComments,
					existingbuildingCategoryId, proposedbuildingCategoryId,
					true, null);
			setAdmissionFeeAmountForRegistration();
			BigDecimal totalAmount = bpaCommonExtnService
					.isFeeCollectionPending(registration);
			setBAandRejectNumberForRegistration(totalAmount);

			/*
			 * TODO: If workflow status is forward and first time saving this
			 * record then forwarded user is mandatory. Save forwarded user in
			 * temp variable along with registration. Dont call..
			 * workflow.transition() on create. Create if admissionfee amount is
			 * zero. Check any amount is pending for collection.
			 * 
			 * add workflow action in local variable..
			 */

			if (((workFlowAction != null
					&& !workFlowAction
							.equalsIgnoreCase(BpaConstants.SCRIPT_SAVE) && !workFlowAction
						.equalsIgnoreCase(BpaConstants.SCRIPT_REJECT)))
					&& (admissionfeeAmount != null && admissionfeeAmount > 0)) {

				if (registration != null
						&& registration.getId() != null /*
														 * &&
														 * registration.getEgwStatus
														 * ()!=null &&
														 * registration
														 * .getEgwStatus
														 * ().getDescription
														 * ().equalsIgnoreCase
														 * (BpaConstants.
														 * STATUS_APPLICATIONREGISTERED
														 * )
														 */
						&& registration.getState() != null
						&& registration.getState().getValue()
								.equalsIgnoreCase(BpaConstants.WF_NEW_STATE)
						&& totalAmount.doubleValue() > 0) {

					registration = registerBpaExtnService
							.getRegistrationById(registration.getId());
					bpaBillableExtn.setRegistration(registration);
					collectXML = URLEncoder.encode(bpaBillExtnServiceImpl
							.getBillXML(bpaBillableExtn));
					return "viewCollectFee";
				}

			}
			if (null != getAdditionalMode()
					&& getAdditionalMode().equals("editApprovedRecord")) {
				createObjectHistoryForRegistration();
			}

			smsAndEmailonSaveMethodCitizen(finalAttachmentList, totalAmount);
			smsOnSaveMethodOfficials();
			confirmationAlertMessages();

			// confirmationAlertMessages

		} catch (EGOVRuntimeException ex) {
			LOGGER.error("Inside BPA registration create Method"
					+ ex.getMessage());
			if (ex != null
					&& ex.getCause() != null
					&& ex.getCause().getMessage() != null
					&& ex.getCause().getMessage()
							.contains("DatabaseSequenceFirstTimeException")) {
				throw new ValidationException(
						Arrays.asList(new ValidationError(ex.getMessage(),
								"Unable to save change. Please submit again. ")));
			} else if (ex != null
					&& ex.getMessage() != null
					&& ex.getMessage().contains(
							"DatabaseSequenceFirstTimeException")) {
				throw new ValidationException(
						Arrays.asList(new ValidationError(ex.getMessage(),
								"Unable to save change. Please submit again. ")));
			} else if (ex != null && ex.getMessage() != null) {
				throw new ValidationException(
						Arrays.asList(new ValidationError(ex.getMessage(), ex
								.getMessage())));
			}
		}
		if (getMode().equals(BpaConstants.MODEREJECT)) {
			setRejectview(BpaConstants.MODEREJECT);
		}

		setRejectionDetailsforRegistration();

		setMode(BpaConstants.MODEVIEW);
		LOGGER.info(" RegisterBpaAction || Save || end");
		return SUCCESS;
	}

	protected void builddocumentHistoryExtraDeatils(
			DocumentHistoryExtn docHistory,
			List<DocumentHistoryExtnDetails> documentHistodetailTempList) {
		if (docHistory != null) {
			if (docHistory.getWheatherdocumentEnclosed() != null
					&& docHistory.getWheatherdocumentEnclosed().equals(true)) {
				docHistory.setWheatherpartOfLayout(null);
				docHistory.setPlotDevelopedBy(null);
				docHistory.setWheatherFmsOrSketchCopyOfReg(null);
				docHistory.setWheatherplotDevelopedBy("");
			} else if (docHistory.getWheatherpartOfLayout() != null
					&& docHistory.getWheatherpartOfLayout().equals(true)) {
				docHistory.setWheatherFmsOrSketchCopyOfReg(null);
				// docHistory.setWheatherplotDevelopedBy("");
			} else if (docHistory.getPlotDevelopedBy() != null
					&& !docHistory.getPlotDevelopedBy().equals("Individual")) {
				docHistory.setWheatherplotDevelopedBy("");
			}

			Set<DocumentHistoryExtnDetails> unitSet = new HashSet<DocumentHistoryExtnDetails>();
			if (documentHistodetailTempList != null
					&& !documentHistodetailTempList.isEmpty()) {
				for (DocumentHistoryExtnDetails unitdetail : documentHistodetailTempList) {
					if (unitdetail != null
							&& unitdetail.getSurveyNumber() != null
							&& !"".equals(unitdetail.getSurveyNumber())) {

						unitdetail.setDocHistoryId(docHistory);
						unitSet.add(unitdetail);
					}
				}
				docHistory.getDocumentHistoryDetailSet().clear();
				docHistory.getDocumentHistoryDetailSet().addAll(unitSet);
			}
			documentHistodetailTempList = new ArrayList<DocumentHistoryExtnDetails>(
					docHistory.getDocumentHistoryDetailSet());

			docHistory.setRegistrationId(registration);

			User LoggedInuser = bpaCommonExtnService
					.getUserbyId((EgovThreadLocals.getUserId()));

			if (LoggedInuser != null) {
				// docHistory.setCreatedUser(LoggedInuser);//Phionix TODO
			}
			docHistory.setModifiedDate(new Date());
		}

		registration.getDocumenthistorySet().add(docHistory);

	}

	private void createObjectHistoryForRegistration() {
		if (null != registration && null != registration.getId()) { // !=registration.getModifiedDate()
			ObjectHistory objHistory = new ObjectHistory();
			// objHistory.setModifiedBy(registration.getModifiedBy());
			objHistory.setModifiedDate(new Date());
			// objHistory.setObjectType(commonsManager.getObjectTypeByType(BpaConstants.OBJECTTYPEBPAREGISTRATION));
			objHistory.setObjectId(Integer.valueOf(registration.getId()
					.toString()));
			objHistory.setRemarks(getRegObjectHistoryRemarks());
			// commonsManager.createObjectHistory(objHistory);
		}// phionix TODO

	}

	protected void confirmationAlertMessages() {
		String userNameWithDesignation = "@";

		if (registration != null && registration.getState() != null) {
			if (registration.getState().getValue() != null
					&& !registration.getState().getValue()
							.equals(BpaConstants.WF_END_STATE)
					&& !registration.getState().getValue()
							.equals(BpaConstants.WF_CANCELLED)
					&& !registration.getState().getValue()
							.equals(BpaConstants.WF_NEW_STATE))
				userNameWithDesignation = registerBpaExtnService
						.getEmpNameDesignation(registration.getState()
								.getOwnerPosition(), new Date());
		}// todo

		if ("".equals(mode) || mode == null)
			addActionMessage("Building Plan Application "
					+ registration.getPlanSubmissionNum()
					+ " is created successfully");
		else
			addActionMessage("Building Plan Application "
					+ registration.getPlanSubmissionNum()
					+ " is updated successfully");

		if (registration.getEgwStatus().getCode()
				.equals(BpaConstants.ORDERISSUEDTOAPPLICANT))
			addActionMessage("The File has been Closed.");
		// for rejection application to close
		else if (workFlowAction != null
				&& !"".equals(workFlowAction)
				&& (workFlowAction
						.equalsIgnoreCase(BpaConstants.CLOSEREGISTRATION)))
			addActionMessage("The File has been Closed.");

		else {
			if (!workFlowAction.equalsIgnoreCase(BpaConstants.SCRIPT_SAVE))
				if (!"@".equals(userNameWithDesignation))
					addActionMessage("The File has been forwarded to  "
							+ userNameWithDesignation);

		}
	}

	protected void setCheckListForRegistration() {
		registration.getRegistrationChecklistSet().clear();
		Set<RegistrationChecklistExtn> checkListDtls = new HashSet<RegistrationChecklistExtn>(
				getChkListDet());
		registration.setRegistrationChecklistSet(checkListDtls);
		for (RegistrationChecklistExtn checklistObj : registration
				.getRegistrationChecklistSet()) {
			checklistObj.setRegistration(registration);
			if (checklistObj.getIsChecked() != null) {
				if (checklistObj.getIsChecked().equals(Boolean.TRUE)) {
					checklistObj.setCheckListRemarks(checklistObj
							.getCheckListRemarks());
				}

				if (checklistObj.getUploadFile() != null) {/*
															 * File mainDir=new
															 * File("C:/pdf/");
															 * if
															 * (!mainDir.exists
															 * ()){ Boolean
															 * mainDirCreate
															 * =mainDir
															 * .mkdirs();
															 * LOGGER.info(
															 * "mainDirCreated--------->"
															 * +mainDirCreate);
															 * }
															 * checkAndAssignPermissionsForFile
															 * (mainDir);
															 * if(mainDir
															 * .exists() &&
															 * mainDir.canRead()
															 * &&
															 * mainDir.canWrite
															 * () &&
															 * mainDir.canExecute
															 * ()){ File file =
															 * new
															 * File("C:/pdf/"
															 * ,checklistObj
															 * .getUploadFile
															 * ().getName());
															 * 
															 * try {
															 * //FileUtils.
															 * writeByteArrayToFile
															 * (file,
															 * checklistObj
															 * .getUploadFile
															 * ().getFileData
															 * ());
															 * FileUtils.copyFile
															 * (checklistObj.
															 * getUploadFile(),
															 * file); } catch (
															 * FileNotFoundException
															 * e) { // TODO
															 * Auto-generated
															 * catch block
															 * e.printStackTrace
															 * (); } catch
															 * (IOException e) {
															 * // TODO
															 * Auto-generated
															 * catch block
															 * e.printStackTrace
															 * (); }
															 * 
															 * RegDocumentUpload
															 * docUploadObj= new
															 * RegDocumentUpload
															 * ();
															 * 
															 * docUploadObj.
															 * setObjectType(
															 * "RegistrationCheckList"
															 * ); docUploadObj.
															 * setReferenceId
															 * (checklistObj
															 * .getId());
															 * 
															 * RegDocumentUploadDtls
															 * docUploadDtlObj=
															 * new
															 * RegDocumentUploadDtls
															 * ();
															 * docUploadDtlObj
															 * .setContentType
															 * (new
															 * MimetypesFileTypeMap
															 * (
															 * ).getContentType(
															 * checklistObj
															 * .getUploadFile
															 * ()));
															 * docUploadDtlObj
															 * .setFileName
															 * (checklistObj
															 * .getUploadFile
															 * ().getName());
															 * docUploadDtlObj
															 * .setRegDocumentUpload
															 * (docUploadObj);
															 * docUploadObj
															 * .addDocumentList
															 * (docUploadDtlObj
															 * );
															 * 
															 * }
															 */
				}
			} else {
				checklistObj.setCheckListRemarks("");
			}
		}

	}

	private void checkAndAssignPermissionsForFile(File directory) {
		if (directory.exists()) {
			if (!directory.canRead())
				directory.setReadable(Boolean.TRUE);

			if (!directory.canWrite())
				directory.setWritable(Boolean.TRUE);

			if (!directory.canExecute())
				directory.setExecutable(Boolean.TRUE);
		}
	}

	protected void setDocumentNumberForRegistration() {
		if (getDocumentNum() != null && !getDocumentNum().equals("")) {
			registration.setDocumentid(getDocumentNum());
		}

		if (registration != null && registration.getDocumentid() != null) {
			this.setDocumentNum(registration.getDocumentid());
		}
	}

	private void setRejectionCheckListForRegistration() {
		if (getRejectChkListDet() != null && getRejectChkListDet().size() != 0) {
			Set<RejectionChecklistExtn> rejectcheckListDtls = new HashSet<RejectionChecklistExtn>(
					getRejectChkListDet());
			registration.getRejection().setRegistration(registration);
			for (RejectionChecklistExtn checklistObj : rejectcheckListDtls) {
				checklistObj.setRejection(registration.getRejection());
			}
			registration.getRejection().setRejectionChecklistSet(
					rejectcheckListDtls);

		}
	}

	// Show Docket Sheet For UI purpose in Approval cycle
	private void buildDocketObjectDetails() {
		List<InspectionExtn> inspectionList = inspectionExtnService
				.getSiteInspectionListforRegistrationObject(registration);

		if (inspectionList != null && !inspectionList.isEmpty()) {
			InspectionExtn inspectionObj = inspectionList.get(0);
			setInspectionId(inspectionObj.getId());
			setRegistrationId(registration.getId());

			if (inspectionObj.getDocket() != null) {
				docket = inspectionObj.getDocket();
				// check for not null
				constructionStages.addAll(inspectionObj.getDocket()
						.getConstructionStagesSet());
				docketDocumentDtls.addAll(inspectionObj.getDocket()
						.getDocumentEnclosedSet());

				for (DocketFloorDetails docketFloor : inspectionObj.getDocket()
						.getDocketFlrDtlsSet()) {
					if (docketFloor.getFloorNum() != null) {
						Map<Integer, String> floorNoMap = bpaCommonExtnService
								.getFloorNumberMap(inspectionObj.getDocket()
										.getFloorCount());
						docketFloor.setFloorName(floorNoMap.get(docketFloor
								.getFloorNum()));
					}
				}
				docketFloorDetail.addAll(inspectionObj.getDocket()
						.getDocketFlrDtlsSet());
				Set<DocketViolations> devContrlSet = new HashSet<DocketViolations>();
				Set<DocketViolations> setBackSet = new HashSet<DocketViolations>();
				Set<DocketViolations> parkingSet = new HashSet<DocketViolations>();
				Set<DocketViolations> generalSet = new HashSet<DocketViolations>();
				Set<DocketViolations> minDistancePowerLineSet = new HashSet<DocketViolations>();
				for (DocketViolations docViolations : inspectionObj.getDocket()
						.getViolationSet()) {
					if (docViolations.getCheckListDetails().getCheckList()
							.getChecklistType()
							.equals("DOCKETSHEET-VIOLATION-DEVCONTROLRULE")) {
						devContrlSet.add(docViolations);
					} else if (docViolations.getCheckListDetails()
							.getCheckList().getChecklistType()
							.equals("DOCKETSHEET-VIOLATION-SETBACK")) {
						setBackSet.add(docViolations);
					} else if (docViolations.getCheckListDetails()
							.getCheckList().getChecklistType()
							.equals("DOCKETSHEET-VIOLATION-PARKING")) {
						parkingSet.add(docViolations);
					} else if (docViolations.getCheckListDetails()
							.getCheckList().getChecklistType()
							.equals("DOCKETSHEET-VIOLATION-GENERAL")) {
						generalSet.add(docViolations);
					} else if (docViolations
							.getCheckListDetails()
							.getCheckList()
							.getChecklistType()
							.equals("DOCKETSHEET-VIOLATION-MINDISTANCEPOWERLINE")) {
						minDistancePowerLineSet.add(docViolations);
					}
				}
				devContrlList.addAll(devContrlSet);
				setBackList.addAll(setBackSet);
				parkingList.addAll(parkingSet);
				generalList.addAll(generalSet);
				minDistancePowerLineList.addAll(minDistancePowerLineSet);

			}

		}
	}

	/*
	 * For reclassification, we are not collecting any permit fee. We are
	 * generating Building and planning permit number on approval of record.
	 * Here if record submitted from citizen, then generate new number else use
	 * same plan submit number as plan permit number.
	 */
	private void setBAandRejectNumberForRegistration(BigDecimal totalAmount) {
		if (totalAmount != null && totalAmount.equals(BigDecimal.ZERO)) {
			if (null != registration.getEgwStatus()
					&& null != registration.getEgwStatus().getCode()
					&& registration.getEgwStatus().getCode()
							.equalsIgnoreCase(BpaConstants.STATUSAPPROVED)) {
				if (registration.getBaNum() == null
						|| "".equals(registration.getBaNum()))
					registration.setBaNum(bpaNumberGenerationExtnService
							.generateOrderNumber(registration));
				if (registration.getPlanPermitApprovalNum() == null
						|| "".equals(registration.getPlanPermitApprovalNum())) {
					if (registration.getServiceType() != null
							&& !registration.getServiceType().getIsCmdaType()
							&& registration.getRequest_number() != null) { // Mean
																			// record
																			// registered
																			// from
																			// citizen.
						registration
								.setPlanPermitApprovalNum(bpaNumberGenerationExtnService.generatePlanSubmissionNumber(
										registration.getServiceType(),
										bpaCommonExtnService
												.getZoneNameFromAdminboundaryid(registration
														.getAdminboundaryid())));
					} else
						registration.setPlanPermitApprovalNum(registration
								.getPlanSubmissionNum());

				}
				registration.setBaOrderDate(new Date());
			}
		}

		if (null != registration.getEgwStatus()
				&& null != registration.getEgwStatus().getCode()
				&& registration.getEgwStatus().getCode()
						.equalsIgnoreCase(BpaConstants.REJECTIONAPPROVED)) {
			if (registration.getRejection() != null
					&& registration.getRejection().getRejectionNumber() == null
					|| "".equals(registration.getRejection()
							.getRejectionNumber()))
				registration.getRejection().setRejectionNumber(
						(bpaNumberGenerationExtnService
								.generateRejectionOrderNumber()));
		}
	}

	protected void smsAndEmailonSaveMethodCitizen(
			List<Map<String, Object>> finalAttachmentList,
			BigDecimal totalAmount) {
		/*
		 * send sms and email to citizen on registration i.e., when record is
		 * forwarded for the 1st time and on approve. For Both Citizen through
		 * registered and COC Operator through registered Bpa records using this
		 * method to send SMS and Mail on forward...
		 */
		if (workFlowAction != null
				&& workFlowAction
						.equalsIgnoreCase(BpaConstants.FORWARDWORKFLOWSTATUS)) {
			if (("".equals(mode) || mode == null)
					|| (registration.getState() != null && registration
							.getState().getValue()
							.equalsIgnoreCase(BpaConstants.WF_NEW_STATE))
					|| (!"".equals(registration.getRequest_number())
							&& registration.getRequest_number() != null
							&& registration.getServiceRegistryId() != null
							&& registration.getState() != null && registration
							.getState()
							.getValue()
							.equalsIgnoreCase(
									BpaConstants.CITIZENAPPLICATIONREGISTERED))) {
				if (totalAmount != null && totalAmount.doubleValue() == 0) {
					bpaCommonExtnService.buildEmail(registration,
							BpaConstants.SMSEMAILSAVE, finalAttachmentList);
					bpaCommonExtnService.buildSMS(registration,
							BpaConstants.SMSEMAILSAVE);
				}
			}
		}
		/*
		 * Added workFlowAction !='Save' because in Assistant inbox for approved
		 * records On Save its sending SMS and Mail so Now sending SMS and Mail
		 * only on workflowaction!='Save' and status='Approved' and
		 * NextAction='Dispatch record'
		 */
		else if (null != workFlowAction
				&& !workFlowAction.equalsIgnoreCase(BpaConstants.SCRIPT_SAVE)
				&& null != registration.getEgwStatus()
				&& null != registration.getEgwStatus().getCode()
				&& registration.getEgwStatus().getCode()
						.equalsIgnoreCase(BpaConstants.STATUSAPPROVED)
				&& registration.getState() != null
				&& registration.getState().getNextAction()
						.equalsIgnoreCase(BpaConstants.DISPATCHRECORD)) {

			bpaCommonExtnService.buildEmail(registration,
					BpaConstants.SMSEMAILAPPROVE, null);
			bpaCommonExtnService.buildSMS(registration,
					BpaConstants.SMSEMAILAPPROVE);

		}
		/*
		 * send mail and SMS for Citizen after Letter To party Approved
		 */
		if (workFlowAction != null
				&& workFlowAction
						.equalsIgnoreCase(BpaConstants.SMSEMAILAPPROVELETTERTOPARTY)
				&& null != registration.getEgwStatus()
				&& null != registration.getEgwStatus().getCode()
				&& (registration.getEgwStatus().getCode()
						.equalsIgnoreCase(BpaConstants.LETTERTOPARTYSENT))) {

			List<LetterToPartyExtn> letterToParty = registerBpaExtnService
					.getLetterToPartyForRegistrationObject(registration);
			if (letterToParty != null && !letterToParty.isEmpty()) {
				setLetterToPartyId(letterToParty.get(0).getId());
			}
			HashMap<String, Object> attachmentList = new HashMap<String, Object>();
			HashMap<String, Object> attachmentFileNames = new HashMap<String, Object>();
			attachmentList.put("letterToParty", generateLetterToPartyPDF());
			attachmentFileNames.put("letterToPartyFileName",
					"letterToPartyPDF.pdf");
			finalAttachmentList.add(attachmentList);
			finalAttachmentList.add(attachmentFileNames);
			bpaCommonExtnService.buildEmail(registration,
					BpaConstants.SMSEMAILLP, finalAttachmentList);
			bpaCommonExtnService
					.buildSMS(registration, BpaConstants.SMSEMAILLP);
		}
		if (workFlowAction != null
				&& workFlowAction
						.equalsIgnoreCase(BpaConstants.CMDASMSEMAILAPPROVELETTERTOPARTY)
				&& null != registration.getEgwStatus()
				&& null != registration.getEgwStatus().getCode()
				&& (registration.getEgwStatus().getCode()
						.equalsIgnoreCase(BpaConstants.CMDALETTERTOPARTYSENT))) {

			List<CMDALetterToPartyExtn> letterToParty = registerBpaExtnService
					.getcmdaLetterToPartyForRegistrationObject(registration);
			if (letterToParty != null && !letterToParty.isEmpty()) {
				setLetterToPartyId(letterToParty.get(0).getId());
			}
			HashMap<String, Object> attachmentList = new HashMap<String, Object>();
			HashMap<String, Object> attachmentFileNames = new HashMap<String, Object>();
			attachmentList.put("letterToPartycmda",
					generateLetterToCMDAPDF(registration));
			attachmentFileNames.put("letterToPartyFileNamecmda",
					"letterToPartyPDF1.pdf");
			finalAttachmentList.add(attachmentList);
			finalAttachmentList.add(attachmentFileNames);
			bpaCommonExtnService.buildEmail(registration,
					BpaConstants.SMSEMAILCMDALP, finalAttachmentList);
			bpaCommonExtnService.buildSMS(registration,
					BpaConstants.SMSEMAILCMDALP);
		}

	}

	/*
	 * Send SMS to AEE when record is forwarded and letter to reply is
	 * collected. (designation's of the employees for which SMS has to be sent
	 * is picked from Appconfig ) approverDesignation
	 */
	private void smsOnSaveMethodOfficials() {

		if (null != registration.getEgwStatus()
				&& null != registration.getEgwStatus().getCode()
				&& registration
						.getEgwStatus()
						.getCode()
						.equalsIgnoreCase(
								BpaConstants.CITIZENAPPLICATIONREGISTERED)
				&& workFlowAction != null
				&& workFlowAction
						.equalsIgnoreCase(BpaConstants.FORWARDWORKFLOWSTATUS)
				&& !approverDepartment.equals(-1)) {

			Boolean flag = Boolean.FALSE;
			// TODO Phionix
			// PersonalInformation emp =
			// bpaCommonExtnService.getEmpForPosition(registration);

			List<AppConfigValues> appConfigDesignationList = bpaCommonExtnService
					.getAppConfigValue(BpaConstants.BPAMODULENAME,
							BpaConstants.OFFICIALSDESIGLISTFORSMS);

			// List<EmployeeView> empViewList =
			// bpaPimsExtnFactory.getEVforDesignationsListByEmpAndDept(emp,
			// approverDepartment);
			// TODO PHionix
			/*
			 * Compare the Appconfig designation list with forwardedto employee
			 * designation list.
			 */
			/*
			 * if(!appConfigDesignationList.isEmpty() &&
			 * !empViewList.isEmpty()){ for(AppConfigValues
			 * appConfigValueObj:appConfigDesignationList){
			 * 
			 * for(EmployeeView ev:empViewList){
			 * 
			 * if(null!=appConfigValueObj && null!=appConfigValueObj.getValue()
			 * && null!=ev.getDesigId() &&
			 * null!=ev.getDesigId().getDesignationName()){
			 * 
			 * if(appConfigValueObj.getValue().equalsIgnoreCase(ev.getDesigId().
			 * getDesignationName())){ flag=Boolean.TRUE; } } } } }
			 */

			if (flag) {

				EgwStatus oldstatus = bpaCommonExtnService
						.getoldStatus(registration);

				if (null != oldstatus
						&& oldstatus.getCode().equalsIgnoreCase(
								BpaConstants.LPREPLYRECEIVED)) {

					bpaCommonExtnService.buildSMS(registration,
							BpaConstants.SMSEMAILASSISTANTTOAEEONLP);

				} else {

					bpaCommonExtnService.buildSMS(registration,
							BpaConstants.SMSEMAILASSISTANTTOAEEONSAVE);

				}
			}
		}
	}

	protected void setAdmissionFeeAmountForRegistration() {
		LOGGER.info(" RegisterBpaAction || setAdmissionFeeAmountForRegistration || Start");
		if (registration.getServiceType() != null
				&& registration.getServiceType().getId() != null
				&& registration.getRegnDetails() != null
				&& registration.getRegnDetails().getSitalAreasqmt() != null) {
			BigDecimal admissionfeeAmount = feeExtnService
					.getTotalFeeAmountByPassingServiceTypeandArea(registration
							.getServiceType().getId(), registration
							.getRegnDetails().getSitalAreasqmt(),
							BpaConstants.ADMISSIONFEE);
			registration.setAdmissionfeeAmount(admissionfeeAmount);
		} else {
			registration.setAdmissionfeeAmount(BigDecimal.ZERO);
		}
		LOGGER.info(" RegisterBpaAction || setAdmissionFeeAmountForRegistration || end");
	}

	@SkipValidation
	@Action(value = "/registerBpaExtn-viewForm", results = { @Result(name = NEW,type = "dispatcher") })
	public String viewForm() {
		LOGGER.info(" RegisterBpaAction || viewForm || Start");
		buildRegistrationForViewModify();
		setMode(BpaConstants.MODEVIEW);
		LOGGER.info(" RegisterBpaAction || viewForm || end");
		return NEW;
	}

	@SuppressWarnings("unchecked")
	@SkipValidation
	@Action(value = "/registerBpaExtn-modifyForm", results = { @Result(name = NEW,type = "dispatcher") })
	public String modifyForm() {
		LOGGER.info(" RegisterBpaAction || Save || Start");
		buildRegistrationForViewModify();
		buildOrderDetails();
		if (registration.getEgwStatus() != null
				&& (registration.getCurrentState().getValue()
						.equals(BpaConstants.FORWARDEDTORDCSTATE) || registration
						.getCurrentState().getValue()
						.equals(BpaConstants.FORWADREDTOSESTATE))) {
			buildOfficialViewActions();
		}

		if (registration.getEgwStatus().getCode()
				.equalsIgnoreCase(BpaConstants.UNDERREJECTION)
				|| registration.getEgwStatus().getCode()
						.equalsIgnoreCase(BpaConstants.REJECTIONAPPROVED)
				|| registration.getEgwStatus().getCode()
						.equalsIgnoreCase(BpaConstants.REJECTORDERPREPARED)
				|| registration.getEgwStatus().getCode()
						.equalsIgnoreCase(BpaConstants.REJECTORDERISSUED)) {

			return modifyrejectForm(registration);
		}
		// FIFO for AE/AEE inbox objects - Objects which are in Registered state
		// Commented for time being. EE level is removed from workflow
		/*
		 * if(registration.getCurrentState()!=null &&
		 * registration.getCurrentState
		 * ().getValue().equalsIgnoreCase(BpaConstants.APPLICATIONREGISTERED)){
		 * List<Position> pos =
		 * eisService.getPositionsForUser((EgovThreadLocals.getUserId()), new
		 * Date()); RegistrationExtn firstInboxItem = (RegistrationExtn)
		 * bpaCommonExtnService
		 * .getFirstWorkflowItemForType(pos,(EgovThreadLocals
		 * .getUserId()),RegistrationExtn.class.getSimpleName(),
		 * BpaConstants.APPLICATIONREGISTERED
		 * +","+registration.getServiceType().getId()); if
		 * (registration.getState() != null && firstInboxItem!=null &&
		 * registration
		 * .getState().getCreatedDate().after(firstInboxItem.getState
		 * ().getCreatedDate())) {
		 * addActionMessage(getText("registrationExtn.EE.fifo.validation",new
		 * String[] {firstInboxItem.getPlanSubmissionNum()}));
		 * forwardNotApplicable = true; } }
		 */

		// To remove approval information is SE inbox for Create LetterToParty
		// workflow
		if (registration.getCurrentState() != null
				&& (registration.getCurrentState().getValue()
						.equalsIgnoreCase("LpForworded") || registration
						.getCurrentState().getValue()
						.equalsIgnoreCase("LettertoCMDAForworded"))) {
			currentstate = Boolean.TRUE;
		} else {
			currentstate = Boolean.FALSE;
		}

		getChkListDet();
		if (registration.getEgwStatus() != null
				&& (registration.getEgwStatus().getCode()
						.equalsIgnoreCase(BpaConstants.CREATEDLETTERTOPARTY))) {
			registration.setAdditionalRule(BpaConstants.LETTERTOPARTYDETAILS);
			setAdditionalRule(BpaConstants.LETTERTOPARTYDETAILS);
			registration.setAdditionalState(registration.getState().getValue());
		} else if (registration.getEgwStatus() != null
				&& (registration.getEgwStatus().getCode()
						.equalsIgnoreCase(BpaConstants.CMDACREATEDLETTERTOPARTY))) {
			registration.setAdditionalRule(BpaConstants.LETTERTOCMDA);
			setAdditionalRule(BpaConstants.LETTERTOCMDA);
			registration.setAdditionalState(registration.getState().getValue());
		} else if (registration.getEgwStatus() != null
				&& registration.getEgwStatus().getCode()
						.equalsIgnoreCase(BpaConstants.LETTERTOPARTYSENT)) {
			registration
					.setAdditionalRule(BpaConstants.BPAAPPLICATIONREGISTRATION);
			setAdditionalRule(BpaConstants.BPAAPPLICATIONREGISTRATION);
		} else if (registration.getEgwStatus() != null
				&& registration.getEgwStatus().getCode()
						.equalsIgnoreCase(BpaConstants.CMDALETTERTOPARTYSENT)) {
			registration
					.setAdditionalRule(BpaConstants.BPAAPPLICATIONREGISTRATION);
			setAdditionalRule(BpaConstants.BPAAPPLICATIONREGISTRATION);
		} else {
			registration
					.setAdditionalRule(BpaConstants.BPAAPPLICATIONREGISTRATION);
			setAdditionalRule(BpaConstants.BPAAPPLICATIONREGISTRATION);
		}
		setMode(EDIT);
		setRejectionDetailsforRegistration();

		BigDecimal amountPending = bpaCommonExtnService
				.isFeeCollectionPending(registration);
		if (null != amountPending && amountPending.equals(BigDecimal.ZERO)) {
			setIsPlotAreaEditable("false");
		} else {
			setIsPlotAreaEditable("true");
			if (registration.getEgwStatus() != null
					&& registration.getEgwStatus().getCode()
							.equalsIgnoreCase(BpaConstants.CHALLANNOTICESENT))
				addActionMessage("Collect Fees to get further actions");
		}
		/*
		 * Validate Only if Record is entered By citizen setting noEdit MOde
		 * disable all the fields to be edit for Assistant and next level user..
		 */
		if (registration != null && registration.getRequest_number() != null
				&& registration.getServiceRegistryId() != null
				&& !"".equals(registration.getRequest_number())) {
			setMode(BpaConstants.MODENOEDIT);
		}
		if (EgovThreadLocals.getUserId() != null) {
			List<String> roleList = bpaCommonExtnService
					.getRoleNamesByPassingUserId((EgovThreadLocals.getUserId()));
			if (!BpaExtnRuleBook.getInstance().checkViewsforRoles(roleList,
					"registrationdetails")) {
				setMode(BpaConstants.MODENOEDIT);
			}
		}

		if (null != registration.getState()
				&& null != registration.getState().getNextAction()
				&& registration.getState().getNextAction()
						.equalsIgnoreCase(BpaConstants.DISPATCHRECORD)) {
			setMode(BpaConstants.MODENOEDIT);
		}
		HashMap<Integer, String> statusMap = bpaCommonExtnService
				.getStatusIdMap();
		Criteria statusCrit = bpaCommonExtnService
				.createStatusChangeCriteria(registration);
		List<Integer> statusIdList = statusCrit.list();
		for (Integer statusId : statusIdList) {
			if (statusMap.get(statusId).equals(
					BpaConstants.CREATEDLETTERTOPARTY)
					|| statusMap.get(statusId).equals(
							BpaConstants.CMDACREATEDLETTERTOPARTY)) {
				setMode(BpaConstants.MODENOEDIT);
				break;
			}
		}

		if (registration.getEgwStatus() != null
				&& (registration.getEgwStatus().getCode()
						.equals(BpaConstants.UNDERREJECTION))) {
			setRejectview(BpaConstants.MODEREJECT);
		} else {
			String status = bpaCommonExtnService
					.getPrimaryStatusforRegistration(registration);
			if (status != null
					&& status.equals(BpaConstants.PRIMARYSTATUSREJECT)) {
				setRejectview(BpaConstants.MODEREJECT);
			}
		}
		if (registration.getEgwStatus() != null
				&& registration.getEgwStatus().getCode()
						.equalsIgnoreCase(BpaConstants.CHALLANAMOUNTCOLLECTED)) {
			setAdditionalMode("editApprovedRecord");
		}

		/*
		 * if(registration.getEgwStatus()!=null &&
		 * registration.getEgwStatus().getCode
		 * ()!=null&&registration.getEgwStatus
		 * ().getCode().equalsIgnoreCase(BpaConstants
		 * .CREATEDLETTERTOPARTY)&&registration
		 * .getState()!=null&&registration.getState
		 * ().getNextAction()!=null&&registration
		 * .getState().getNextAction().equals("LP Raised-Send LettertoParty"))
		 * addActionMessage
		 * ("Enter Letter to Party Sent Date in the Letter to Party section");
		 */
		LOGGER.info(" RegisterBpaAction || Save || End");
		return NEW;
	}

	private void buildRegistrationForViewModify() {
		LOGGER.info(" RegisterBpaAction || buildRegistrationForViewModify || Start");
		registration = registerBpaExtnService
				.getRegistrationById(registrationId);
		if (registration != null) {
			buildRegistrationObject();
		}
		LOGGER.info(" RegisterBpaAction || buildRegistrationForViewModify || end");
	}

	protected void buildRegistrationObject() {
		if (registration != null) {
			RegnAutoDcrExtn autoDcrObj = autoDcrExtnService
					.getAutoDcrByRegId(registration.getId());
			if (autoDcrObj != null)
				setAutoDcrNum(autoDcrObj.getAutoDcrNum());
			for (BpaAddressExtn bpaAddressObj : registration.getBpaAddressSet()) {
				if (null != bpaAddressObj.getAddressTypeMaster()
						&& BpaConstants.PROPERTY_ADDRESS.equals(bpaAddressObj
								.getAddressTypeMaster())) {// Phionix TODO
					if (null != bpaAddressObj.getIndianState()
							&& bpaAddressObj.getIndianState() != null) {
						setBoundaryStateId(bpaAddressObj.getIndianState());
					}
					setSiteAddress(bpaAddressObj);
				} else if (null != bpaAddressObj.getAddressTypeMaster()
						&& BpaConstants.OWNER_ADDRESS.equals(bpaAddressObj
								.getAddressTypeMaster())) {// Phionix TODO

					setApplicantrAddress(bpaAddressObj);
				}
			}
			if (registration.getOwner() != null) {
				mobileNumber = registration.getOwner().getMobileNumber();
				emailId = registration.getOwner().getEmailId();
			}
			// In Approval Cycle Build Docket sheet and Document History For UI
			// show
			if (!bpaCommonExtnService.isUserMappedToSurveyorRole()
					&& registration.getEgwStatus() != null
					&& registration.getEgwStatus().getCode()
							.equals(BpaConstants.FILECONSIDERATIONCHECKED)
					&& (registration.getCurrentState().getValue()
							.equals(BpaConstants.FORWARDEDTORDCSTATE)
							|| registration.getCurrentState().getValue()
									.equals(BpaConstants.FORWARDEDTOEESTATE) || registration
							.getCurrentState().getValue()
							.equals(BpaConstants.FORWADREDTOSESTATE))) {

				enableDocketSheetForView = Boolean.TRUE;
			}
			if (registration.getCurrentState() != null
					&& registration
							.getCurrentState()
							.getValue()
							.equalsIgnoreCase(
									BpaConstants.WORKFLOWSTATUSFORWARDEDTOAEORAEE)) {
				stateForValidate = registration.getCurrentState().getValue();
			} else {
				stateForValidate = "";
			}
			if (registration.getCurrentState() != null
					&& registration
							.getCurrentState()
							.getValue()
							.equalsIgnoreCase(
									BpaConstants.APPROVEDWORKFLOWSTATUS)) {
				approvedWorkflowState = registration.getCurrentState()
						.getValue();
			}
			// For Official User and ServiceTypes only 01,03,06
			if (!bpaCommonExtnService.isUserMappedToSurveyorRole()
					&& registration.getServiceType() != null
					&& registration.getServiceType().getCode() != null
					&& (registration.getServiceType().getCode()
							.equals(BpaConstants.NEWBUILDINGONVACANTPLOTCODE)
							|| registration
									.getServiceType()
									.getCode()
									.equals(BpaConstants.DEMOLITIONRECONSTRUCTIONCODE) || registration
							.getServiceType().getCode()
							.equals(BpaConstants.ADDITIONALCONSTRUCTIONCODE))) {
				serviceTypeCode = Boolean.TRUE;
			}

			/*
			 * if(registration.getSurveyorName()!=null) {
			 * surveyorCode=registration.getSurveyorName().getCode();
			 * surveyorNameLocal=registration.getSurveyorName().getName();
			 * SurveyorDetail
			 * surveyourDetailObj=bpaCommonExtnService.getSurveyourDetail
			 * (surveyorCode,null); if(surveyourDetailObj!=null){
			 * surveyorClass=surveyourDetailObj.getSurveyorClass();
			 * setSurveyor(surveyourDetailObj.getSurveyor().getId()); } else{
			 * surveyorClass="II";
			 * setSurveyor(registration.getSurveyorName().getId()); }
			 * 
			 * }
			 */
			List<DocumentHistoryExtn> docHistoryList = bpaCommonExtnService
					.getRegnDocumentHistoryObjectBySurveyorOrOtherOfficials(registration);
			for (DocumentHistoryExtn docHistObj : docHistoryList) {
				if (docHistObj != null && docHistObj.getCreatedUser() != null)
					isDocHistoryByAEorAEE = bpaCommonExtnService.ShowUserROles(
							docHistObj.getCreatedUser().getRoles(),
							BpaConstants.BPAAEROLE, BpaConstants.BPAAEEROLE);
				if (isDocHistoryByAEorAEE.equals(true)) {
					break;
				}
			}
			/*
			 * To Show Previous User Approver Comments From Workflow..
			 */
			previousApproverComments = bpaCommonExtnService
					.getPreviousUserCommentsFromWorkfow(registration);

			if (registration != null && registration.getDocumentid() != null)
				this.setDocumentNum(registration.getDocumentid());
			if (null != registration.getRegnDetails()) {
				if (null != registration.getRegnDetails().getExistingBldgCatg()
						&& null != registration.getRegnDetails()
								.getExistingBldgCatg().getId()) {
					setExistingbuildingCategoryId(registration.getRegnDetails()
							.getExistingBldgCatg().getId());
				}
				if (null != registration.getRegnDetails().getProposedBldgCatg()
						&& null != registration.getRegnDetails()
								.getProposedBldgCatg().getId()) {
					setProposedbuildingCategoryId(registration.getRegnDetails()
							.getProposedBldgCatg().getId());
				}
			}
			// Build DocumentHistory For AE/AEE
			List<DocumentHistoryExtn> docHistoryFromOfficial = bpaCommonExtnService
					.getRegnDocumentHistoryObjectForAEorAEE(registration);
			String regServType = registration.getServiceType().getCode();
			if (bpaCommonExtnService.isUserMappedToAeOrAeeRole()) {
				// docHistory if documentHistory By AE/AEE is not entered and
				// other than servicetypes 02,04,05,07,08 will add new Document
				// Object
				if (docHistoryFromOfficial.isEmpty()
						&& (regServType
								.equals(BpaConstants.APPLICATIONFORDEMOLITIONCODE)
								|| regServType
										.equals(BpaConstants.SUBDIVISIONOFLANDCODE)
								|| regServType
										.equals(BpaConstants.LAYOUTAPPPROVALCODE)
								|| regServType.equals(BpaConstants.CMDACODE) || regServType
									.equals(BpaConstants.RECLASSIFICATIONCODE))) {
					setDocumentHistory(new DocumentHistoryExtn());
					documentHistoryDetailList
							.add(new DocumentHistoryExtnDetails());
				} else {
					// for Servicetypes 01,03,06 get documentHistory of Surveyor
					// and populate into AE/AEE Document History.. If AE/AEE is
					// not entered
					if (docHistoryFromOfficial == null
							|| docHistoryFromOfficial.isEmpty()) {
						List<DocumentHistoryExtn> docHistoryFromSurveyor = bpaCommonExtnService
								.getRegnDocumentHistoryObjectBySurveyorOrOtherOfficials(registration);
						if (docHistoryFromSurveyor != null
								&& !docHistoryFromSurveyor.isEmpty()) {
							setDocumentHistory(docHistoryFromSurveyor.get(0));
							documentHistoryDetailList = new ArrayList<DocumentHistoryExtnDetails>(
									docHistoryFromSurveyor.get(0)
											.getDocumentHistoryDetailSet());
						}
						if (documentHistoryDetailList.isEmpty()) {
							documentHistoryDetailList
									.add(new DocumentHistoryExtnDetails());
						}
					} else {
						// Modify Screen For AE/AEE once its saved..
						setDocumentHistory(docHistoryFromOfficial.get(0));
						documentHistoryDetailList = new ArrayList<DocumentHistoryExtnDetails>(
								getDocumentHistory()
										.getDocumentHistoryDetailSet());
						if (documentHistoryDetailList.isEmpty()) {
							documentHistoryDetailList
									.add(new DocumentHistoryExtnDetails());
						}

					}
					getDocumentHistDetailsWithDateFormat(documentHistoryDetailList);
				}
			}
			// Build DocumentHistory sFor Surveyor and Other Than AE/AEE
			// Official Users....
			else {
				getDocumentHistoryForViewAndModify();
			}
			setAdmissionFeeAmountForRegistration();

			postponedInspectionDetails = inspectionExtnService
					.getInspectionListforRegistrationObject(registration);
			for (InspectionExtn inspectiondetails : postponedInspectionDetails) {
				if (inspectiondetails.getParent() != null) {
					inspectiondetails.getParent().setPostponedDate(
							inspectiondetails.getInspectionDate());

				}

			}
			String UserRole = bpaCommonExtnService
					.getUserRolesForLoggedInUser();
			List<RegistrationChecklistExtn> checkListDtlsList = new ArrayList<RegistrationChecklistExtn>();
			if (UserRole != null
					&& !UserRole.contains(BpaConstants.PORTALUSERCITIZENROLE)
					&& !bpaCommonExtnService.isUserMappedToSurveyorRole()) {
				for (RegistrationChecklistExtn checkListDtlsObj : registration
						.getRegistrationChecklistSet()) {
					if (checkListDtlsObj != null
							&& checkListDtlsObj.getIsChecked() != null
							&& checkListDtlsObj.getIsChecked().equals(
									Boolean.TRUE)) {
						checkListDtlsList.add(checkListDtlsObj);
					}

				}
			} else {
				checkListDtlsList = new ArrayList<RegistrationChecklistExtn>(
						registration.getRegistrationChecklistSet());

			}
			if (checkListDtlsList != null && checkListDtlsList.size() > 0)
				Collections.sort(checkListDtlsList);

			String fileLocationName = bpaCommonExtnService
					.getAppconfigValueResult(BpaConstants.BPAMODULENAME,
							BpaConstants.BPA_FILE_UPLOAD_LOCATION, null);
			File mainDir = new File(
					fileLocationName
							+ "/"
							+ BpaConstants.BPAMODULENAME
							+ (registration.getInitialPlanSubmissionNum() != null ? "/"
									+ registration
											.getInitialPlanSubmissionNum()
											.replace("/", "") : (registration
									.getPlanSubmissionNum() != null ? "/"
									+ registration.getPlanSubmissionNum()
											.replace("/", "") : "")));
			for (RegistrationChecklistExtn chkList : checkListDtlsList) {
				/*
				 * if(chkList.getDocUpload()!=null) { RegDocumentUpload
				 * docUploadObj =
				 * regDocUploadService.findById(chkList.getDocUpload(), false);
				 * if (docUploadObj != null) { if (mainDir != null) { File
				 * docObj = new File(mainDir,
				 * docUploadObj.getDocumentList().get(0).getFileName()); if
				 * (docObj != null) { chkList.setUploadFile(docObj);
				 * chkList.setFileName
				 * (docUploadObj.getDocumentList().get(0).getFileName());
				 * chkList.setContentType(docUploadObj.getDocumentList().get(0).
				 * getContentType()); } } } }
				 */

			}

			setChkListDet(checkListDtlsList);
			existingSiteInspectionDetails = inspectionExtnService
					.getSiteInspectionListforRegistrationObject(registration);

		}
	}

	@SkipValidation
	@Action(value = "/registerBpaExtn-showSurveyorDocHistoryDetails", results = { @Result(name = "showSurveyordocHistory",type = "dispatcher") })
	public String showSurveyorDocHistoryDetails() {
		if (registrationId != null)
			registration = registerBpaExtnService.findById(registrationId,
					false);

		List<DocumentHistoryExtn> survyrDocumentHistoryList = new ArrayList<DocumentHistoryExtn>(
				registration.getDocumenthistorySet());
		if (survyrDocumentHistoryList != null
				&& !survyrDocumentHistoryList.isEmpty()) {
			for (DocumentHistoryExtn docExtn : survyrDocumentHistoryList) {
				/*
				 * for(Role role : docExtn.getCreatedUser().getRoles()){
				 * if(role.getRoleName()!=null &&
				 * role.getRoleName().equalsIgnoreCase
				 * (BpaConstants.PORTALUSERSURVEYORROLE)) {
				 * setDocumentHistory(docExtn); break; }//TODO pHionix }
				 */
			}
			if (survyrDocumentHistoryList != null
					&& !survyrDocumentHistoryList.isEmpty()) {
				if (getDocumentHistory() != null) {
					documentHistoryDetailList = new ArrayList<DocumentHistoryExtnDetails>(
							getDocumentHistory().getDocumentHistoryDetailSet());
					getDocumentHistDetailsWithDateFormat(documentHistoryDetailList);
				}
			}
		}
		return "showSurveyordocHistory";
	}

	public void getDocumentHistoryForViewAndModify() {
		List<DocumentHistoryExtn> docHistory = bpaCommonExtnService
				.getRegnDocumentHistoryObjectBySurveyorOrOtherOfficials(registration);
		if (docHistory != null && !docHistory.isEmpty()) {

			setDocumentHistory(docHistory.get(0));
			documentHistoryDetailList = new ArrayList<DocumentHistoryExtnDetails>(
					getDocumentHistory().getDocumentHistoryDetailSet());
			if (documentHistoryDetailList.isEmpty()) {
				documentHistoryDetailList.add(new DocumentHistoryExtnDetails());
			}
			getDocumentHistDetailsWithDateFormat(documentHistoryDetailList);
		} else {
			setDocumentHistory(new DocumentHistoryExtn());
			documentHistoryDetailList.add(new DocumentHistoryExtnDetails());
		}
	}

	private void getDocumentHistDetailsWithDateFormat(
			List<DocumentHistoryExtnDetails> documentHistoryDetailList) {
		// for Format Date To Show in UI
		if (documentHistoryDetailList != null
				&& !documentHistoryDetailList.isEmpty()) {
			for (DocumentHistoryExtnDetails doc : documentHistoryDetailList) {
				Calendar cal = Calendar.getInstance();
				if (doc != null && doc.getRegistartionDate() != null) {
					Date dateReg = doc.getRegistartionDate();
					cal.setTime(dateReg);
					dateReg = cal.getTime();
					doc.setRegistartionDate(dateReg);
				}
			}

		}

	}

	protected String getMessage(String key) {
		return getText(key);
	}

	/*
	 * To Call Ajax for Exist PPA Number Onchange validation
	 */
	@SkipValidation
	@Action(value = "/registerBpaExtn-existPPAnumberforRegistration", results = { @Result(name = "ppanum",type = "dispatcher") })
	public String existPPAnumberforRegistration() {
		ppanum = registerBpaExtnService.checkPPANumberIsValid(existPpaNumber,
				registration.getId());
		return "ppanum";
	}

	/*
	 * To Call Ajax for Exist BA Number Onchange validation
	 */
	@SkipValidation
	@Action(value = "/registerBpaExtn-existBanumberCheckForAjax", results = { @Result(name = "ppanum",type = "dispatcher") })
	public String existBanumberCheckForAjax() {
		ppanum = registerBpaExtnService.checkBuildingApprovalNumberIsValid(
				existBaNumber, registration.getId());
		LOGGER.info(ppanum);
		return "ppanum";
	}

	// validation for autoDcrnumber
	// @SkipValidation
	public Boolean validateAutodcr() {
		return registerBpaExtnService.checkAutodcr(getAutoDcrNum(),
				registration.getId());
	}

	public Boolean validateSericeTypeOnSubmit() {
		ServiceTypeExtn servicetypeObj = registerBpaExtnService
				.getServiceTypeByRegistrationId(registration.getId());

		if (servicetypeObj != null
				&& registration != null
				&& registration.getEgwStatus() != null
				&& registration.getEgwStatus().getCode() != null
				&& (registration.getEgwStatus().getCode()
						.equals(BpaConstants.APPLICANTSIGNUPDATED)
						|| registration.getEgwStatus().getCode()
								.equals(BpaConstants.CHALLANAMOUNTCOLLECTED)
						|| registration.getEgwStatus().getCode()
								.equals(BpaConstants.CHALLANNOTICESENT)
						|| registration.getEgwStatus().getCode()
								.equals(BpaConstants.ORDERPREPARED) || registration
						.getEgwStatus().getCode()
						.equals(BpaConstants.STATUSAPPROVED))) {
			if (servicetypeObj.getId().equals(
					registration.getServiceType().getId())) {
				return false;
			}
			return true;
		} else {
			return false;
		}
	}

	/*
	 * validation for Unique ExistBaNumber in Table....
	 */
	public Boolean checkUniqueforExistBaNumber() {
		return registerBpaExtnService
				.checkExistingBuildingApprovalNumberIsUnique(
						registration.getExistingBANum(), registration.getId());
	}

	/*
	 * validation for Unique ExistBaNumber in Table....
	 */
	public Boolean checkUniqueforExistPPaNumber() {
		return registerBpaExtnService.checkExistingPpaNumberIsUnique(
				registration.getExistingPPANum(), registration.getId());
	}

	/*
	 * It will allow records to save only Applications have status as cancelled,
	 * Rejection order issued,order issued to applicant ...
	 */
	public Boolean existPPAnumberValidationforRegistration() {
		return registerBpaExtnService.checkPPANumberIsValid(
				registration.getExistingPPANum(), registration.getId());

	}

	/*
	 * It will allow records to save only Applications have status as cancelled,
	 * Rejection order issued,order issued to applicant ...
	 */
	public Boolean existBAnumberValidationforRegistration() {
		return registerBpaExtnService.checkBuildingApprovalNumberIsValid(
				registration.getExistingBANum(), registration.getId());

	}

	@Override
	public void validate() {
		LOGGER.info(" RegisterBpaAction || validate || Start");
		List<String> temp = new ArrayList<String>();
		temp.add(null);
		documentHistoryDetailList.removeAll(temp);
		if (null == registration.getPlanSubmissionDate()
				|| "".equals(registration.getPlanSubmissionDate())) {
			addFieldError("registration.planSubmissionDate",
					getMessage("registration.planSubmissionDate.required"));
		}
		if (registration.getServiceType() == null) {
			addFieldError("registration.serviceType",
					getMessage("registration.serviceType.required"));
		}
		if (registration.getId() != null && validateSericeTypeOnSubmit()) {
			addFieldError("registration.serviceType.id",
					getMessage("servicetype.allready.selected"));
		}
		if (registration.getAppType() == null
				|| registration.getAppType().equals("-1")) {
			addFieldError("registration.appType",
					getMessage("registration.appType.required"));
		}
		if (registration.getAppMode() == null
				|| registration.getAppMode().equals("-1")) {
			addFieldError("registration.appMode",
					getMessage("registration.appMode.required"));
		}
		if (registration.getAdminboundaryid() == null) {
			addFieldError("registration.adminboundaryid",
					getMessage("adminboundaryid.required"));
		}

		if (registration.getId() == null
				|| (registration.getState() != null && registration.getState()
						.getValue().equalsIgnoreCase(BpaConstants.WF_NEW_STATE))) {
			Long userPosition = null;
			if (registration.getAdminboundaryid() != null) {
				userPosition = getZonalLevelAssistantForLoggedInUser();
			}
			if (registration.getAdminboundaryid() != null
					&& userPosition == null) {
				addFieldError(
						"registration.assistantJurisdictionMap.required",
						getMessage("registration.assistantJurisdictionMap.required"));
			}
			registration.setApproverPositionId(userPosition);
		}

		/*
		 * if(registration.getLocboundaryid()==null){
		 * addFieldError("registration.locboundaryid",
		 * getMessage("locboundaryid.required")); }
		 */
		if (registration.getOwner() == null) {
			addFieldError("registration.owner", getMessage("owner.required"));
		} else {
			if (registration.getOwner().getName() == null
					|| "".equals(registration.getOwner().getName())) {
				addFieldError("registration.owner.name",
						getMessage("owner.name.required"));
			}
			/*
			 * if(registration.getOwner().getFatherName()==null ||
			 * "".equals(registration.getOwner().getFatherName())){
			 * addFieldError("registration.owner.fathername",
			 * getMessage("owner.fathername.required")); }
			 */
			if (applicantrAddress.getStreetAddress1() == null
					|| "".equals(applicantrAddress.getStreetAddress1())) {
				addFieldError("registration.applicantrAddress.StreetAddress1",
						getMessage("applicantrAddress.StreetAddress1.required"));
			}
			if (registration.getId() == null || registration.getState() == null) {
				if (getMobileNumber() == null || "".equals(getMobileNumber())) {
					addFieldError("registration.owner.mobilePhone",
							getMessage("owner.mobileNumber.required"));
				}
			}
			/*
			 * if(applicantrAddress.getStreetAddress2()==null ||
			 * "".equals(applicantrAddress.getStreetAddress2())){
			 * addFieldError("registration.applicantrAddress.StreetAddress2",
			 * getMessage("applicantrAddress.StreetAddress2.required")); }
			 */
		}
		if (siteAddress.getPlotNumber() == null
				|| "".equals(siteAddress.getPlotNumber())) {
			addFieldError("registration.siteAddress.PlotNumber",
					getMessage("siteAddress.PlotNumber.required"));
		}
		if (siteAddress.getPlotSurveyNumber() == null
				|| "".equals(siteAddress.getPlotSurveyNumber())) {
			addFieldError("registration.siteAddress.PlotSurveyNumber",
					getMessage("siteAddress.PlotSurveyNumber.required"));
		}
		/*
		 * if(siteAddress.getVillage()==null ||
		 * "".equals(siteAddress.getVillageName().getName()==-1)){
		 * addFieldError("registration.siteAddress.village",
		 * getMessage("siteAddress.village.required")); }
		 */
		if (siteAddress.getVillageName() == null) {
			addFieldError("registration.siteAddress.village",
					getMessage("siteAddress.village.required"));
		} else if (siteAddress.getVillageName().getId() == null) {
			addFieldError("registration.siteAddress.village",
					getMessage("siteAddress.village.required"));
		}
		if (siteAddress.getPlotBlockNumber() == null
				|| "".equals(siteAddress.getPlotBlockNumber())) {
			addFieldError("registration.siteAddress.PlotBlockNumber",
					getMessage("siteAddress.PlotBlockNumber.required"));
		}
		if (siteAddress.getCityTown() == null
				|| "".equals(siteAddress.getCityTown())) {
			addFieldError("registration.siteAddress.CityTown",
					getMessage("siteAddress.CityTown.required"));
		}
		if (getBoundaryStateId() == null || "".equals(getBoundaryStateId() )) {
			addFieldError("registration.siteAddress.State",
					getMessage("siteAddress.StateName.required"));
		}

		if (siteAddress.getPincode() == null
				|| "".equals(siteAddress.getPincode())) {
			addFieldError("registration.siteAddress.Pincode",
					getMessage("siteAddress.Pincode.required"));
		}
		if (registration.getExistingPPANum() != null
				&& !"".equals(registration.getExistingPPANum())) {
			/*
			 * Checking PPA NUMBER IS VALID NUMBER ? AND CHECKING STATUS OF PPA
			 * NUMBER..
			 */

			if (!existPPAnumberValidationforRegistration()) {
				addFieldError("registration.ExistBaNum",
						getMessage("registration.ExistPpaNum.present"));
			} else {
				// Checking uniqueness in existing ppa number
				if (checkUniqueforExistPPaNumber()) {
					addFieldError("registration.ExistBaNum",
							getMessage("registration.ExistPpaNumColumn.exist"));
				}

			}
		}
		if (registration.getExistingBANum() != null
				&& !"".equals(registration.getExistingBANum())) {
			/*
			 * Checking Building Approval NUMBER IS VALID NUMBER ? AND CHECKING
			 * STATUS OF BA NUMBER..
			 */
			if (!existBAnumberValidationforRegistration()) {
				addFieldError("registration.ExistBaNum",
						getMessage("registration.ExistBaNum.present"));
			} else {
				// Checking uniqueness in existing ba number
				if (checkUniqueforExistBaNumber()) {
					addFieldError("registration.ExistBaNum",
							getMessage("registration.ExistBaNumColumn.exist"));
				}
				/*
				 * To Validate Both Exist PPA Number and Exist BA Number Belongs
				 * to Same Application...
				 */
				else if (registration.getExistingPPANum() != null
						&& !"".equals(registration.getExistingPPANum())) {
					RegistrationExtn regObj = bpaCommonExtnService
							.validateExistBaAndExistPpaOfSameApplication(
									registration.getExistingPPANum(),
									registration.getExistingBANum());
					// LOGGER.info(regObj.getId());
					if (regObj == null) {
						addFieldError(
								"registration.ExistBaNum",
								getMessage("registration.ExistBaPpanum.SameApplication"));
					}

				}
			}
		}
		if (getSurveyor() == null || getSurveyor() == -1) {

			addFieldError("registration.surveyorName",
					getMessage("surveyor.required"));
		}
		/*
		 * else if(registration.getSurveyorName().getName()==null){
		 * addFieldError("registration.surveyorName",
		 * getMessage("surveyorName.required")); }
		 */

		if (getSurveyorCode() == null || "".equals(getSurveyorCode())) {
			addFieldError("registration.surveyorCode",
					getMessage("surveyorCode.required"));
		}
		if (getSurveyorNameLocal() == null || "".equals(getSurveyorNameLocal())) {
			addFieldError("registration.surveyorName",
					getMessage("surveyorNameatx.required"));
		}
		if (getSurveyorClass() == null || "".equals(getSurveyorClass())) {
			addFieldError("registration.surveyorClass",
					getMessage("surveyorClass.required"));
		}
		if (registration.getRegnDetails() == null) {
			addFieldError("registration.regnDetails",
					getMessage("regnDetails.required"));
		} else {
			if (registration.getRegnDetails().getSitalAreasqft() == null) {
				addFieldError("registration.regnDetails.sitalAreasqft",
						getMessage("regnDetails.sitalAreasqft.required"));
			}
			if (registration.getRegnDetails().getSitalAreasqmt() == null) {
				addFieldError("registration.regnDetails.sitalAreasqmt",
						getMessage("regnDetails.sitalAreasqmt.required"));
			}
		}
		if (registration.getServiceType() != null) {
			if (registration.getServiceType().getIsPtisNumberRequired() != null
					&& registration.getServiceType().getIsPtisNumberRequired()
							.equals(Boolean.TRUE)) {
				if (registration.getPropertyid() == null
						|| "".equals(registration.getPropertyid())) {
					addFieldError("registration.propertyid",
							getMessage("property.required"));
				}
			}
			if (registration.getServiceType().getISAutoDcrNumberRequired() != null
					&& registration.getServiceType()
							.getISAutoDcrNumberRequired().equals(Boolean.TRUE)) {
				if (getAutoDcrNum() == null || "".equals(getAutoDcrNum())) {
					addFieldError("registration.autoDcrNum",
							getMessage("autoDCRnumber.required"));

				}

			}
			if (getAutoDcrNum() != null && !"".equals(getAutoDcrNum())
					&& validateAutodcr()) {

				addFieldError("AutoDcrexist",
						getMessage("registration.autodcr.exist"));
			}
			if (registration.getServiceType().getIsCmdaType() != null
					&& registration.getServiceType().getIsCmdaType()
							.equals(Boolean.TRUE)) {
				if (registration.getCmdaNum() == null
						|| "".equals(registration.getCmdaNum())) {
					addFieldError("registration.cmdaNum",
							getMessage("cmdaProposalNumber.required"));

				}
				if (registration.getCmdaRefDate() == null
						|| "".equals(registration.getCmdaRefDate())) {
					addFieldError("registration.cmdaRefDate",
							getMessage("cmdaRefDate.required"));

				}
			}

		}
		// To validate Lp reply is mendatory for created Letter To Party except
		// reject Unconsidered action..
		if (!getMode().equals(BpaConstants.MODEREJECT)) {
			if ((workFlowAction != null
					&& (workFlowAction
							.equalsIgnoreCase(BpaConstants.FORWARDWORKFLOWSTATUS) || workFlowAction
							.equalsIgnoreCase(BpaConstants.REJECTWORKFLOWSTATUS)) && (registration
					.getEgwStatus() != null && registration.getEgwStatus()
					.getCode().equals(BpaConstants.LETTERTOPARTYSENT)))) {
				LetterToPartyExtn letterToParty = (LetterToPartyExtn) letterToPartyExtnService
						.getLatestLetterToPartyForRegObj(registration);
				if (letterToParty.getSentDate() != null) {
					List<LetterToPartyExtn> lpParty = letterToPartyExtnService
							.getLetterToPartyForRegnByComparingLPSentDateWithSysDateToTenDays(
									registration, letterToParty.getSentDate());
					// validate for less than or equal to 10 days .. not
					// allowing for any action till receiving Letter To party
					// Reply
					if (lpParty == null || lpParty.isEmpty()) {
						addActionError(getMessage("registration.lessThantendays.validate"));
					}
					// validate For greater Than 10 days .. Then allowing only
					// reject Unconsideration to proceed
					else if (lpParty != null && lpParty.size() > 0) {
						addActionError(getMessage("registration.greaterthantendays.validate"));
					}
				}
			}
			if ((workFlowAction != null
					&& (workFlowAction
							.equalsIgnoreCase(BpaConstants.FORWARDWORKFLOWSTATUS) || workFlowAction
							.equalsIgnoreCase(BpaConstants.REJECTWORKFLOWSTATUS)) && (registration
					.getEgwStatus() != null && registration.getEgwStatus()
					.getCode().equals(BpaConstants.CMDALETTERTOPARTYSENT)))) {
				addActionError(getMessage("registration.cmdalettertoparty.validate"));
			}

		}
		/*
		 * Inspection details are mandatory for all the services. Before
		 * forwarding to EE/SE/RDC, AE must enter these details in the system.
		 */
		if (null != workFlowAction
				&& workFlowAction
						.equalsIgnoreCase(BpaConstants.FORWARDWORKFLOWSTATUS)
				&& (getAdditionalRule() != null && (getAdditionalRule()
						.equals(BpaConstants.BPAAPPLICATIONREGISTRATION)))
				&& registration != null
				&& registration.getCurrentState() != null
				&& registration.getCurrentState().getValue() != null
				&& registration
						.getCurrentState()
						.getValue()
						.equalsIgnoreCase(
								BpaConstants.WORKFLOWSTATUSFORWARDEDTOAEORAEE)) {
			List<InspectionExtn> inspectionList = inspectionExtnService
					.getSiteInspectionListforRegistrationObject(registration);
			if (inspectionList == null || inspectionList.size() == 0) {
				addActionError("Inspection is pending for this registration,Please complete the inspection and try again");
			}

			List<DocumentHistoryExtn> docHistory = bpaCommonExtnService
					.getRegnDocumentHistoryObjectForAEorAEE(registration);
			if (documentHistory == null
					&& (docHistory == null || docHistory.isEmpty())) {
				addFieldError(
						"documentHistry.Required",
						"Document History Sheet Details are mandatory for this Registration.Please complete the DocumentSheet Details and try again ");
			}
			// TODO:For service type reclassification, check if approver
			// information entered or not. If no approval information entered,
			// then show error message.
			if (null != registration
					&& registration.getServiceType() != null
					&& registration.getServiceType().getCode() != null
					&& registration.getServiceType().getCode()
							.equals(BpaConstants.RECLASSIFICATIONCODE)) {
				List<RegnApprovalInformationExtn> ApproveInfoList = bpaCommonExtnService
						.getRegnApprovalInfobyRegistrationId(registration);
				if (ApproveInfoList == null || ApproveInfoList.size() == 0)

				{
					addActionError("Approval Information  is pending for this registration,Please complete the Approval Information and try again");

				}
			}
			/*
			 * Mandatory Fee details should be entered by AEE before forwarding
			 * the record to EE.
			 */
			if (null != registration && null != registration.getEgwStatus()
					&& null != registration.getEgwStatus().getCode()
					&& null != registration.getServiceType()
					&& null != registration.getServiceType().getId()) {
				// if(registration.getEgwStatus().getCode().equals(BpaConstants.INSPECTED)
				// ||registration.getEgwStatus().getCode().equals(BpaConstants.APPLICATIONREGISTERED))
				// Boolean statusChangehasFeecreat=Boolean.FALSE;
				// HashMap<Integer,String>
				// statusMap=bpaCommonService.getStatusIdMap();
				/*
				 * List<Integer> statusIdList=statusCrit.list(); for(Integer
				 * statusId:statusIdList){
				 * if(BpaConstants.FEESCREATED.equals(statusMap.get(statusId))){
				 * statusChangehasFeecreat=Boolean.TRUE; } }
				 */
				if (!registration.getEgwStatus().getCode()
						.equals(BpaConstants.FEESCREATED)) {
					List statusCrit = bpaCommonExtnService
							.getStatusChangeByPassingRegistrationAndStatusCode(
									registration, BpaConstants.FEESCREATED);
					if (statusCrit != null && statusCrit.isEmpty()) {
						List<BpaFeeExtn> santionFeeList = feeExtnService
								.getAllMandatorySanctionedFeesbyServiceType(registration
										.getServiceType().getId());
						if (santionFeeList != null && !santionFeeList.isEmpty()) {
							addActionError("Mandatory Fee Details are not entered for this Registration. Please Enter Fee Details and try again");
							/*
							 * if(santionFeeList==null &&
							 * documentHistoryDetailList.isEmpty())
							 * documentHistoryDetailList.add(new
							 * DocumentHistoryExtn()); }
							 */
						}
					}
				}
			}

			// if(null!=workFlowAction &&
			// workFlowAction.equals(BpaConstants.CLOSEREGISTRATION)){
			String status = bpaCommonExtnService
					.getPrimaryStatusforRegistration(registration);
			if (status != null
					&& status.equals(BpaConstants.PRIMARYSTATUSREJECT)) {
				if (!registration.getEgwStatus().getCode()
						.equals(BpaConstants.REJECTORDERISSUED))
					addActionError("Registration cannot be closed till Rejection order is issued.Please issue the Rejection order and try again");
				rejectChkListDet = new ArrayList(registration.getRejection()
						.getRejectionChecklistSet());
			} else if (status != null
					&& status.equals(BpaConstants.PRIMARYSTATUSAPPROVE)) {
				if (!registration.getEgwStatus().getCode()
						.equals(BpaConstants.ORDERISSUEDTOAPPLICANT)) {
					if (null != workFlowAction
							&& !workFlowAction
									.equalsIgnoreCase(BpaConstants.SCRIPT_SAVE)) {
						addActionError("Registration cannot be closed till  Order is issued.Please issue the Order and try again");
					}
				}
			}

		}

		LOGGER.info(" RegisterBpaAction || validate || end");
	}

	@SkipValidation
	@Action(value = "/registerBpaExtn-showCheckList", results = { @Result(name = "checklist",type = "dispatcher") })
	public String showCheckList() {
		LOGGER.info(" RegisterBpaExtnAction || showCheckList || Start");
		chkListDet.clear();
		registration.getRegistrationChecklistSet().clear();
		List<CheckListDetailsExtn> chkListDetails = registerBpaExtnService
				.getRegistrationCheckListforService(getServiceTypeIdTemp());
		for (CheckListDetailsExtn chkList : chkListDetails) {
			RegistrationChecklistExtn registrationchk = new RegistrationChecklistExtn();
			registrationchk.setCheckListDetails(chkList);
			chkListDet.add(registrationchk);
		}
		LOGGER.info(" RegisterBpaExtAction || showCheckList || end");
		return "checklist";
	}

	public void loggedInUserActions() {
		List<String> roleList = new ArrayList<String>();
		if (EgovThreadLocals.getUserId() != null) {
			roleList = bpaCommonExtnService
					.getRoleNamesByPassingUserId((EgovThreadLocals.getUserId()));
		}

		registration = registerBpaExtnService
				.getRegistrationById(getRegistrationId());
		if (registration != null) {
			String ServType = registration.getServiceType().getCode();
			if (registration.getEgwStatus() != null
					&& (registration.getEgwStatus().getCode()
							.equals(BpaConstants.CREATEDLETTERTOPARTY) || registration
							.getEgwStatus().getCode()
							.equals(BpaConstants.CMDACREATEDLETTERTOPARTY))) {
				// roleList =
				// bpaCommonService.getRoleNamesByPassingUserId((EgovThreadLocals.getUserId()));
				showActions = BpaExtnRuleBook.getInstance().getActionsByRoles(
						roleList, registration.getEgwStatus());
			} else if (registration.getEgwStatus() != null
					&& registration.getEgwStatus().getCode()
							.equals(BpaConstants.UNDERREJECTION)
					|| getAdditionalMode() != null
					&& getAdditionalMode().equals("rejection initiated")) {
				showActions = new ArrayList();
			} else {
				if (checkFeeforApprovedRegistration()) {
					EgwStatus previousStatusbeforeLp = null;

					String status = bpaCommonExtnService
							.getPrimaryStatusforRegistration(registration);
					if (status == null
							|| (!status
									.equals(BpaConstants.PRIMARYSTATUSREJECT) && !status
									.equals(BpaConstants.PRIMARYSTATUSAPPROVE))) {
						previousStatusbeforeLp = bpaCommonExtnService
								.getAllOlderStatusforRegistration(registration);
					}

					if (previousStatusbeforeLp == null) {
						showActions = BpaExtnRuleBook.getInstance()
								.getActionsByRoles(roleList,
										registration.getEgwStatus());
						showActions.remove(BpaConstants.ADDCHALLANSENTDATE);

						if ((ServType
								.equals(BpaConstants.APPLICATIONFORDEMOLITIONCODE)
								|| ServType
										.equals(BpaConstants.SUBDIVISIONOFLANDCODE)
								|| ServType
										.equals(BpaConstants.RECLASSIFICATIONCODE) || ServType
									.equals(BpaConstants.LAYOUTAPPPROVALCODE))
								&& showActions
										.contains(BpaConstants.BUILDINGMEASUREMENT)) {
							showActions
									.remove(BpaConstants.BUILDINGMEASUREMENT);
						}
						/*
						 * To get Capture DD details alone in Actions : To make
						 * it capture_DD_Details As mendatory Except ServiceType
						 * 2..
						 */

						// IF CITIZEN PAID ALL THE AMOUNT THROUGH ONLINE THEN NO
						// NEED TO CAPTURE DD INFORMATION. IF REQUIRED,
						// UNCOMMENT BELOW LINES.
						/*
						 * else if((!ServType.equals(BpaConstants.
						 * APPLICATIONFORDEMOLITIONCODE)) &&
						 * registration.getFeeDDSet().size() ==0 &&
						 * null!=registration.getEgwStatus() &&
						 * registration.getEgwStatus
						 * ().getCode().equals(BpaConstants
						 * .CHALLANAMOUNTCOLLECTED) &&
						 * showActions.contains(BpaConstants.UPDATESIGNATURE)){
						 * showActions.remove(BpaConstants.UPDATESIGNATURE); }
						 */

					} else {
						showActions = BpaExtnRuleBook.getInstance()
								.getActionsByRoles(roleList,
										previousStatusbeforeLp);
					}

					if (showActions != null
							&& !ServType
									.equals(BpaConstants.RECLASSIFICATIONCODE)) {
						showActions.remove(BpaConstants.ADDAPPROVALINFORMATION);
					}

					if (ServType.equals(BpaConstants.RECLASSIFICATIONCODE)) {
						if (showActions != null) {
							showActions.remove(BpaConstants.COLLECTFEEDETAILS);
							showActions.remove(BpaConstants.ADDCHALLANSENTDATE);
							showActions
									.remove(BpaConstants.BUILDINGMEASUREMENT);
							showActions.remove(BpaConstants.CAPTURE_DD_DETAILS);

							if (null != registration.getEgwStatus()
									&& registration
											.getEgwStatus()
											.getCode()
											.equals(BpaConstants.STATUSAPPROVED)) {
								showActions.remove(BpaConstants.ORDERPREPARED);
								showActions
										.remove(BpaConstants.PRINTBUILDINGPERMIT);
								showActions
										.remove(BpaConstants.PRINTPLANPERMIT);
							}
							if (null != registration.getEgwStatus()
									&& registration.getEgwStatus().getCode()
											.equals(BpaConstants.ORDERPREPARED)) {
								showActions.remove(BpaConstants.ORDERPREPARED);
							}
						}
					}
				} else {
					if (null != registration.getEgwStatus()
							&& registration.getEgwStatus().getCode()
									.equals(BpaConstants.STATUSAPPROVED)) {
						showActions = BpaExtnRuleBook.getInstance()
								.getActionsByRoles(roleList,
										registration.getEgwStatus());
						showActions.remove(BpaConstants.PRINTBUILDINGPERMIT);
						showActions.remove(BpaConstants.PRINTPLANPERMIT);
						showActions.remove(BpaConstants.UPDATESIGNATURE);
						showActions.remove(BpaConstants.ORDERPREPARED);
						showActions.remove(BpaConstants.BUILDINGMEASUREMENT);
						if (!ServType
								.equals(BpaConstants.APPLICATIONFORDEMOLITIONCODE)) {
							showActions.remove(BpaConstants.CAPTURE_DD_DETAILS);
						}

					} else {
						showActions = BpaExtnRuleBook.getInstance()
								.getActionsByRoles(roleList,
										registration.getEgwStatus());

					}

					// For service type reclassification, We are not capturing
					// fees. No need to send challan to citizen.
					if (ServType.equals(BpaConstants.RECLASSIFICATIONCODE)) {
						showActions.remove(BpaConstants.ADDCHALLANSENTDATE);
						showActions.remove(BpaConstants.BUILDINGMEASUREMENT);
						showActions.remove(BpaConstants.CAPTURE_DD_DETAILS);
					}
				}

				if (null != registration.getEgwStatus()
						&& registration.getEgwStatus().getCode()
								.equals(BpaConstants.APPLICATIONREGISTERED)
						&& showActions
								.contains(BpaConstants.CREATELETTERTOPARTY)) {
					showActions.remove(BpaConstants.CREATELETTERTOPARTY);
				}
				if (!showActions.isEmpty()
						&& (!showActions
								.contains(BpaConstants.CREATELETTERTOPARTY))
						&& null != registration.getEgwStatus()
						&& (registration.getEgwStatus().getCode()
								.equals(BpaConstants.FILECONSIDERATIONCHECKED) || registration
								.getEgwStatus().getCode()
								.equals(BpaConstants.INSPECTED))) {
					showActions.add(BpaConstants.CREATELETTERTOPARTY);
				}
				/*
				 * Capture DD Details is applicable only after Status Challen
				 * Amount is collected. and checking wheather fee details
				 * captured or wat..If its collected then removing this action
				 * to get Update Signature Action
				 */

				if (registration.getFeeDDSet().size() != 0
						&& showActions
								.contains(BpaConstants.CAPTURE_DD_DETAILS)) {
					showActions.remove(BpaConstants.CAPTURE_DD_DETAILS);
				}
				/*
				 * removing create letterToParty action if allready Letter To
				 * Party is present for registraion
				 */
				LetterToPartyExtn lpObject = letterToPartyExtnService
						.getLatestLetterToPartyForRegnTOUniqueValidate(registration);
				if (lpObject == null
						&& registration.getLetterToPartySet().size() != 0
						&& showActions
								.contains(BpaConstants.CREATELETTERTOPARTY)) {
					showActions.remove(BpaConstants.CREATELETTERTOPARTY);
				}
				if (registration.getServiceType() != null
						&& registration.getServiceType().getCode() != null
						&& registration.getServiceType().getCode()
								.equals(BpaConstants.CMDACODE)) {

					if ((registration.getEgwStatus() != null && registration
							.getEgwStatus().getCode()
							.equals(BpaConstants.LETTERTOPARTYSENT))) {
						LetterToPartyExtn letterToParty = (LetterToPartyExtn) letterToPartyExtnService
								.getLatestLetterToPartyForRegObj(registration);
						if (letterToParty.getSentDate() != null) {
							List<LetterToPartyExtn> lpParty = letterToPartyExtnService
									.getLetterToPartyForRegnByComparingLPSentDateWithSysDateToTenDays(
											registration,
											letterToParty.getSentDate());
							// validate for less than or equal to 10 days .. not
							// allowing for any action till receiving Letter To
							// party Reply
							// if(lpParty!=null && lpParty.size()>0)
							if (lpParty != null || lpParty.size() > 0) {
								showActions
										.add(BpaConstants.CREATELETTERTOCMDA);
							}
						}
					}

					if (registration.getEgwStatus().getCode()
							.equals(BpaConstants.CMDALETTERTOPARTYSENT)) {
						if (showActions
								.contains(BpaConstants.CREATELETTERTOCMDA)) {
							showActions.remove(BpaConstants.CREATELETTERTOCMDA);
						}
						if (showActions
								.contains(BpaConstants.ADDNEWSITEINSPECTIONDETAILS)) {
							showActions
									.remove(BpaConstants.ADDNEWSITEINSPECTIONDETAILS);
						}
						if (showActions.contains(BpaConstants.MODIFYFEEDETAILS)) {
							showActions.remove(BpaConstants.MODIFYFEEDETAILS);
						}
						if (showActions
								.contains(BpaConstants.COLLECTFEEDETAILS)) {
							showActions.remove(BpaConstants.COLLECTFEEDETAILS);
						}
						if (showActions
								.contains(BpaConstants.ADDAPPROVALINFORMATION)) {
							showActions
									.remove(BpaConstants.ADDAPPROVALINFORMATION);
						}
					}
					if (null != registration.getEgwStatus()
							&& (registration.getEgwStatus().getCode()
									.equals(BpaConstants.CMDALPREPLYRECEIVED))) {
						showActions.add(BpaConstants.CREATELETTERTOCMDA);
					}
				}
				if (registration.getServiceType() != null
						&& registration.getServiceType().getCode() != null
						&& !registration.getServiceType().getCode()
								.equals(BpaConstants.CMDACODE)
						&& showActions
								.contains(BpaConstants.CREATELETTERTOCMDA)) {
					showActions.remove(BpaConstants.CREATELETTERTOCMDA);
				}
				/*
				 * Removing FeeDatils and add second time siteinspection deatils
				 * if registration status is LETTERTOPARTYSENT.
				 */
				if (registration.getEgwStatus() != null
						&& registration.getEgwStatus().getCode()
								.equals(BpaConstants.LETTERTOPARTYSENT)) {
					showActions.remove(BpaConstants.COLLECTFEEDETAILS);
					showActions.remove(BpaConstants.ADDSITEINSPECTIONDETAIL);
					showActions
							.remove(BpaConstants.ADDNEWSITEINSPECTIONDETAILS);
					if (showActions
							.contains(BpaConstants.ADDAPPROVALINFORMATION)) {
						showActions.remove(BpaConstants.ADDAPPROVALINFORMATION);
					}
					if (showActions.contains(BpaConstants.MODIFYFEEDETAILS)) {
						showActions.remove(BpaConstants.MODIFYFEEDETAILS);
					}
				}
				// Building Measurement update feature is applicable when the
				// status is Approved. There is no status update so in this part
				// of code, removing it manually from the actions list
				if (registration.getApprdBuildingDetailsSet().size() != 0
						&& showActions
								.contains(BpaConstants.BUILDINGMEASUREMENT)) {
					showActions.remove(BpaConstants.BUILDINGMEASUREMENT);
				}
				// Building Measurement is mandatory in case of service
				// types(1,3,6,7). So Order Prepared action is not allowed
				// without Building Measurement update
				String regServType = registration.getServiceType().getCode();
				if (regServType
						.equals(BpaConstants.NEWBUILDINGONVACANTPLOTCODE)
						|| regServType
								.equals(BpaConstants.DEMOLITIONRECONSTRUCTIONCODE)
						|| regServType
								.equals(BpaConstants.ADDITIONALCONSTRUCTIONCODE)
						|| regServType.equals(BpaConstants.CMDACODE)) {

					// If Building Measurement has not yet been created, then
					// Order Prepared feature should be disabled
					if (registration.getApprdBuildingDetailsSet().size() == 0
							&& showActions.contains(BpaConstants.ORDERPREPARED)) {
						showActions.remove(BpaConstants.ORDERPREPARED);
					}
				}

			}
			checkForPermitPrintAction(registration);
		}
	}

	@SkipValidation
	@Action(value = "/registerBpaExtn-getActionsByLoginUserId", results = { @Result(name = "actionbuttons",type = "dispatcher") })
	public String getActionsByLoginUserId() {
		loggedInUserActions();
		return "actionbuttons";
	}

	public Boolean checkFeeforApprovedRegistration() {
		if (registration.getEgwStatus() != null
				&& registration.getEgwStatus().getCode()
						.equals(BpaConstants.STATUSAPPROVED)) {
			BigDecimal finalFeeTobePaid = bpaCommonExtnService
					.isFeeCollectionPending(registration);
			if (!finalFeeTobePaid.equals(BigDecimal.ZERO)) {
				return false;
			} else
				return true;
		} else
			return true;

	}

	/**
	 * checks if registration status is Approved and fee amount to be collected
	 * is zero and based on service type enables/disables Print Building Permit
	 * and Print Plan Permit actions
	 */
	private void checkForPermitPrintAction(RegistrationExtn registration) {
		BigDecimal finalFeeTobePaid = bpaCommonExtnService
				.isFeeCollectionPending(registration);
		if (finalFeeTobePaid.equals(BigDecimal.ZERO)) {
			if (showActions.contains(BpaConstants.PRINTBUILDINGPERMIT)) {
				if (!bpaCommonExtnService.isPlanOrBuildingPermitAllowed(
						registration, finalFeeTobePaid,
						BpaConstants.BUILDINGPERMIT)) {
					showActions.remove(BpaConstants.PRINTBUILDINGPERMIT);
				}
			}
			if (showActions.contains(BpaConstants.PRINTPLANPERMIT)) {
				if (!bpaCommonExtnService
						.isPlanOrBuildingPermitAllowed(registration,
								finalFeeTobePaid, BpaConstants.PLANPERMIT)) {
					showActions.remove(BpaConstants.PRINTPLANPERMIT);
				}
			}
		} else {
			showActions.remove(BpaConstants.PRINTBUILDINGPERMIT);
			showActions.remove(BpaConstants.PRINTPLANPERMIT);
		}
	}

	@SkipValidation
	@Action(value = "/registerBpaExtn-print", results = { @Result(name = "report",type = "dispatcher") })
	public String print() {
		LOGGER.debug("Entered into print method");
		// LOGGER.debug("Plan Submission Number in print : " +
		// registration.getPlanSubmissionNum());
		registration = registerBpaExtnService.findById(registration.getId(),
				false);

		if (registration != null)
			reportId = registerBpaExtnService.printRegistrationForm(
					registration, getSession());

		LOGGER.debug("Exit from print method");
		return "report";

	}

	@SkipValidation
	@Action(value = "/registerBpaExtn-printInspectionDetails", results = { @Result(name = "report",type = "dispatcher") })
	public String printInspectionDetails() {
		if (getRegistrationId() != null) {
			registration = registerBpaExtnService.findById(getRegistrationId(),
					false);
		} else {
			registration = registerBpaExtnService.findById(
					registration.getId(), false);
		}
		try {
			if (registration != null) {
				if (!bpaCommonExtnService.isUserMappedToSurveyorRole()
						&& registration.getEgwStatus() != null
						&& registration.getEgwStatus().getCode()
								.equals(BpaConstants.FILECONSIDERATIONCHECKED)
						&& (registration.getCurrentState() != null
								&& registration
										.getCurrentState()
										.getValue()
										.equals(BpaConstants.FORWARDEDTORDCSTATE)
								|| registration
										.getCurrentState()
										.getValue()
										.equals(BpaConstants.FORWARDEDTOEESTATE) || registration
								.getCurrentState().getValue()
								.equals(BpaConstants.FORWADREDTOSESTATE))) {
					// buildDocketObjectDetails();
					enableDocketSheetForView = Boolean.TRUE;
				}
				reportId = registerBpaExtnService.printInspectionForm(
						registration, getSession(), printMode,
						enableDocketSheetForView);
			}
		} catch (Exception e) {
			throw new EGOVRuntimeException("Exception : " + e);
		}

		return "report";
	}

	@Action(value = "/registerBpaExtn-printDocketSheet", results = { @Result(name = "report",type = "dispatcher") })
	public String printDocketSheet() {
		LOGGER.debug("Entered into print docket sheet method");

		registration = registerBpaExtnService.findById(registration.getId(),
				false);

		if (registration != null) {

			reportId = registerBpaExtnService.printDocketSheet(registration,
					getSession(), enableDocketSheetForView);
		}
		LOGGER.debug("Exit from print method");
		return "report";

	}

	@SkipValidation
	@Action(value = "/registerBpaExtn-printDocumentHistory", results = { @Result(name = "report",type = "dispatcher") })
	public String printDocumentHistory() {
		if (getRegistrationId() != null) {
			registration = registerBpaExtnService.findById(getRegistrationId(),
					false);
		} // TODO: else part not required
		else {
			registration = registerBpaExtnService.findById(
					registration.getId(), false);
		}
		try {
			if (registration != null) {
				if (!bpaCommonExtnService.isUserMappedToSurveyorRole()
						&& registration.getEgwStatus() != null
						&& registration.getEgwStatus().getCode()
								.equals(BpaConstants.FILECONSIDERATIONCHECKED)
						&& (registration.getCurrentState() != null
								&& registration
										.getCurrentState()
										.getValue()
										.equals(BpaConstants.FORWARDEDTORDCSTATE)
								|| registration
										.getCurrentState()
										.getValue()
										.equals(BpaConstants.FORWARDEDTOEESTATE) || registration
								.getCurrentState().getValue()
								.equals(BpaConstants.FORWADREDTOSESTATE))) {

					enableDocketSheetForView = Boolean.TRUE;
				}
				reportId = registerBpaExtnService.printDocumentHistoryForm(
						registration, getSession(), printMode,
						enableDocketSheetForView);
			}
		} catch (Exception e) {
			throw new EGOVRuntimeException("Exception : " + e);
		}
		return "report";

	}

	@SkipValidation
	@Action(value = "/registerBpaExtn-saveLetterToParty", results = { @Result(name = SUCCESS,type = "dispatcher") })
	public String saveLetterToParty() {
		String userNameWithDesignation = "@";
		RegistrationExtn newregistration = registerBpaExtnService
				.getRegistrationById(registrationId);
		if (registration.getApproverPositionId() != null
				&& registration.getApproverPositionId() != -1) {
			newregistration.setApproverPositionId(registration
					.getApproverPositionId());
		}
		newregistration.setAdditionalRule(registration.getAdditionalRule());
		newregistration.setAdditionalState(registration.getAdditionalState());
		List<LetterToPartyExtn> previouslpList = letterToPartyExtnService
				.getListofLetterToPartyForRegObj(registration, letterToParty);
		if (previouslpList.size() >= 1) {
			for (LetterToPartyExtn lpdtl : previouslpList) {
				lpdtl.setIsHistory('Y');
			}
		}
		// enetr all mendatory fields and press Enter keyword its taking
		// workFlowAction as save....to avaid this test case
		if (workFlowAction != null && !"".equals(workFlowAction)
				&& (BpaConstants.SCRIPT_SAVE.equalsIgnoreCase(workFlowAction))) {
			workFlowAction = BpaConstants.SAVELETTERTOPARTY;
		}

		registration = registerBpaExtnService.createLetterToParty(
				letterToParty, lpChkListDet, newregistration, workFlowAction,
				approverComments);
		List<LetterToPartyExtn> letterToParty = registerBpaExtnService
				.getLetterToPartyForRegistrationObject(registration);
		registration.setLetterToParty(letterToParty.get(0));
		setLetterToPartyId(letterToParty.get(0).getId());

		if (null != registration.getEgwStatus()
				&& null != registration.getEgwStatus().getCode()
				&& (registration.getEgwStatus().getCode()
						.equalsIgnoreCase(BpaConstants.LETTERTOPARTYRAISED) || registration
						.getEgwStatus().getCode()
						.equalsIgnoreCase(BpaConstants.LETTERTOPARTYSENT))) {

			List<Map<String, Object>> finalAttachmentList = new ArrayList<Map<String, Object>>();
			HashMap<String, Object> attachmentList = new HashMap<String, Object>();
			HashMap<String, Object> attachmentFileNames = new HashMap<String, Object>();
			attachmentList.put("letterToParty", generateLetterToPartyPDF());
			attachmentFileNames.put("letterToPartyFileName",
					"letterToPartyPDF.pdf");
			finalAttachmentList.add(attachmentList);
			finalAttachmentList.add(attachmentFileNames);
			bpaCommonExtnService.buildEmail(registration,
					BpaConstants.SMSEMAILLP, finalAttachmentList);
			bpaCommonExtnService
					.buildSMS(registration, BpaConstants.SMSEMAILLP);
		}

		if ("".equals(mode) || mode == null) {
			addActionMessage("Letter To Party "
					+ letterToParty.get(0).getLetterToPartyNumber()
					+ " is created successfully");
		} else if (mode.equals("modify")) {
			addActionMessage("Letter To Party "
					+ letterToParty.get(0).getLetterToPartyNumber()
					+ " is updated successfully");
		}

		else if (mode.equals("enterSentDate")) {
			addActionMessage("Letter To Party "
					+ letterToParty.get(0).getLetterToPartyNumber()
					+ " has been sent to Applicant");
		}
		if (workFlowAction != null
				&& workFlowAction
						.equalsIgnoreCase(BpaConstants.FORWARDWORKFLOWSTATUS)) {
			userNameWithDesignation = registerBpaExtnService
					.getEmpNameDesignationByApproveDesgIdAndApproveId(
							approverDesignation, approverPositionId, new Date());
			if (!"@".equals(userNameWithDesignation))
				addActionMessage("The File has been forwarded to  "
						+ userNameWithDesignation);
		}
		setMode(BpaConstants.MODEVIEW);
		return SUCCESS;

	}

	@SkipValidation
	@Action(value = "/registerBpaExtn-saveLetterToCMDA", results = { @Result(name = SUCCESS,type = "dispatcher") })
	public String saveLetterToCMDA() {
		String userNameWithDesignation = "@";
		RegistrationExtn newregistration = registerBpaExtnService
				.getRegistrationById(registrationId);
		if (registration.getApproverPositionId() != null
				&& registration.getApproverPositionId() != -1) {
			newregistration.setApproverPositionId(registration
					.getApproverPositionId());
		}
		newregistration.setAdditionalRule(registration.getAdditionalRule());
		newregistration.setAdditionalState(registration.getAdditionalState());
		List<CMDALetterToPartyExtn> previouslpList = letterToPartyExtnService
				.getListofCMDALetterToPartyForRegObj(registration, letterToCMDA);
		if (previouslpList.size() >= 1) {
			for (CMDALetterToPartyExtn lpdtl : previouslpList) {
				lpdtl.setIsHistory('Y');
			}
		}
		// enetr all mendatory fields and press Enter keyword its taking
		// workFlowAction as save....to avaid this test case
		if (workFlowAction != null && !"".equals(workFlowAction)
				&& (BpaConstants.SCRIPT_SAVE.equalsIgnoreCase(workFlowAction))) {
			workFlowAction = BpaConstants.SAVELETTERTOCMDA;
		}

		registration = registerBpaExtnService
				.createCMDALetterToParty(letterToCMDA, newregistration,
						workFlowAction, approverComments);

		List<LetterToPartyExtn> letterToParty = registerBpaExtnService
				.getLetterToPartyForRegistrationObject(registration);
		registration.setLetterToParty(letterToParty.get(0));
		setLetterToPartyId(letterToParty.get(0).getId());

		if (null != registration.getEgwStatus()
				&& null != registration.getEgwStatus().getCode()
				&& (registration.getEgwStatus().getCode()
						.equalsIgnoreCase(BpaConstants.CMDALETTERTOPARTYSENT))) {

			/*
			 * List<Map<String, Object>> finalAttachmentList = new
			 * ArrayList<Map<String,Object>>(); HashMap<String, Object>
			 * attachmentList = new HashMap<String, Object>(); HashMap<String,
			 * Object> attachmentFileNames = new HashMap<String, Object>();
			 * attachmentList.put("letterToCMDA", generateLetterToPartyPDF());
			 * attachmentFileNames.put("letterToCMDAFileName",
			 * "letterToCMDAPDF.pdf"); finalAttachmentList.add(attachmentList);
			 * finalAttachmentList.add(attachmentFileNames);
			 */
			bpaCommonExtnService.buildEmail(registration,
					BpaConstants.SMSEMAILCMDALP, null);
			bpaCommonExtnService.buildSMS(registration,
					BpaConstants.SMSEMAILCMDALP);
		}

		if ("".equals(mode) || mode == null) {
			addActionMessage("Letter To CMDA "
					+ letterToCMDA.getLetterToPartyNumber()
					+ " is created successfully");
		} else if (mode.equals("modify")) {
			addActionMessage("Letter To CMDA "
					+ letterToCMDA.getLetterToPartyNumber()
					+ " is updated successfully");
		}

		/*
		 * else if(mode.equals("enterSentDate")) {
		 * addActionMessage("Letter To Party "
		 * +letterToParty.get(0).getLetterToPartyNumber()
		 * +" has been sent to Applicant"); }
		 */
		if (workFlowAction != null
				&& workFlowAction
						.equalsIgnoreCase(BpaConstants.FORWARDWORKFLOWSTATUS)) {
			userNameWithDesignation = registerBpaExtnService
					.getEmpNameDesignationByApproveDesgIdAndApproveId(
							approverDesignation, approverPositionId, new Date());
			if (!"@".equals(userNameWithDesignation))
				addActionMessage("The File has been forwarded to  "
						+ userNameWithDesignation);
		}
		setMode(BpaConstants.MODEVIEW);
		return SUCCESS;

	}

	@SkipValidation
	@Action(value = "/registerBpaExtn-showLetterToPartyCheckList", results = { @Result(name = "lettertopartychecklist",type = "dispatcher") })
	public String showLetterToPartyCheckList() {
		ServiceTypeExtn serviceType = registerBpaExtnService
				.getServiceTypeById(serviceTypeIdTemp);
		if (letterToPartyId == null || letterToPartyId.equals("")) {
			if (serviceType.getCode() != null) {
				List<CheckListDetailsExtn> chkListDetails = registerBpaExtnService
						.getLPCheckListforService(serviceTypeIdTemp);
				for (CheckListDetailsExtn chkList : chkListDetails) {
					LpChecklistExtn lpCheckList = new LpChecklistExtn();
					lpCheckList.setCheckListDetails(chkList);
					lpChkListDet.add(lpCheckList);
				}
			}
		} else {
			if (serviceType.getCode() != null) {
				lpChkListDet = registerBpaExtnService
						.getLetterToPartyCheckDtlsForType(letterToPartyId,
								BpaConstants.LETTERTOPARTYDETAILS);
			}
		}
		setMode(mode);
		return "lettertopartychecklist";
	}

	@SkipValidation
	@Action(value = "/registerBpaExtn-showExistingLetterToPartyDetails", results = { @Result(name = "existingLetterToPartyDtls",type = "dispatcher") })
	public String showExistingLetterToPartyDetails() {

		if (EgovThreadLocals.getUserId() != null) {
			List<String> roleList = bpaCommonExtnService
					.getRoleNamesByPassingUserId((EgovThreadLocals.getUserId()));
			if (!BpaExtnRuleBook.getInstance().checkViewsforRoles(roleList,
					"lettertoparty")) {
				returnStream = returnStream.concat(" Letter To Party Details");
				return "NOACCESS";
			}
		}
		if (registrationId != null) {
			registration = registerBpaExtnService
					.getRegistrationById(registrationId);
			existingLetterToPartyDetails = registerBpaExtnService
					.getLetterToPartyForRegistrationObject(registration);
		}

		setMode(mode);
		return "existingLetterToPartyDtls";
	}

	@SkipValidation
	@Action(value = "/registerBpaExtn-showExistingLetterToPartyCMDADetails", results = { @Result(name = "existingCmdaLetterToPartyDtls",type = "dispatcher") })
	public String showExistingLetterToPartyCMDADetails() {

		if (EgovThreadLocals.getUserId() != null) {
			List<String> roleList = bpaCommonExtnService
					.getRoleNamesByPassingUserId((EgovThreadLocals.getUserId()));
			if (!BpaRuleBook.getInstance().checkViewsforRoles(roleList,
					"lettertoparty")) {
				returnStream = returnStream.concat(" Letter To Party Details");
				return "NOACCESS";
			}
		}
		if (letterToPartyId != null) {
			LetterToPartyExtn lpObj = registerBpaExtnService
					.getLetterToPartyById(letterToPartyId);
			if (lpObj != null) {
				existingCmdaLetterToPartyDetails.addAll(lpObj
						.getCmdaLetterToPartySet());
			}

		}

		setMode(mode);
		return "existingCmdaLetterToPartyDtls";
	}

	@SkipValidation
	@Action(value = "/registerBpaExtn-viewLetterToPartyForm", results = { @Result(name = LETTERTOPARTYFORM,type = "dispatcher") })
	public String viewLetterToPartyForm() {
		registration = registerBpaExtnService
				.getRegistrationById(registrationId);
		List<LetterToPartyExtn> letterToPartyList = registerBpaExtnService
				.getLetterToPartyForRegistrationObject(registration);
		if (!letterToPartyList.isEmpty())
			letterToParty = registerBpaExtnService
					.getLetterToPartyById(letterToPartyList.get(0).getId());
		registration.setLetterToParty(letterToParty);
		setLetterToPartyId(letterToParty.getId());
		setMode(BpaConstants.MODEVIEW);
		return LETTERTOPARTYFORM;
	}

	@SkipValidation
	@Action(value = "/registerBpaExtn-modifyLetterToPartyForm", results = { @Result(name = LETTERTOPARTYFORM,type = "dispatcher") })
	public String modifyLetterToPartyForm() {
		letterToParty = registerBpaExtnService
				.getLetterToPartyById(letterToPartyId);
		setRegistration(letterToParty.getRegistration());
		registration.setLetterToParty(letterToParty);
		List<LpChecklistExtn> checkListDtlsList = new ArrayList<LpChecklistExtn>(
				letterToParty.getLpChecklistSet());
		setLpChkListDet(checkListDtlsList);
		setLetterToPartyId(letterToParty.getId());
		setMode(mode);
		return LETTERTOPARTYFORM;
	}

	// ---------------lp end

	/**
	 * RegnStatusDetails (Signature,Order Preparation, Order Issue
	 * Details,Reject Order Preparation Details, Reject Order Issue Details)
	 */
	@SkipValidation
	protected void buildOrderDetails() {

		if (registrationId != null) {
			registration = registerBpaExtnService.findById(registrationId,
					false);
			setApplicationdate(registration.getPlanSubmissionDate());
			if (registration != null) {
				if (registration.getRegnStatusDetailsSet() != null) {
					for (RegnStatusDetailsExtn regnStatDet : registration
							.getRegnStatusDetailsSet()) {
						if (regnStatDet.getStatus().getCode()
								.equals(BpaConstants.CHALLANNOTICESENT)) {
							registration.setChallanDetails(regnStatDet);
							setValidateDate(regnStatDet.getStatusdate());
						}
						if (regnStatDet.getStatus().getCode()
								.equals(BpaConstants.APPLICANTSIGNUPDATED)) {
							registration.setSignDetails(regnStatDet);
							setValidateDate(regnStatDet.getStatusdate());
						}
						if (regnStatDet.getStatus().getCode()
								.equals(BpaConstants.ORDERPREPARED)) {
							registration.setOrderDetails(regnStatDet);
							setValidateDate(regnStatDet.getStatusdate());
						}
						if (regnStatDet.getStatus().getCode()
								.equals(BpaConstants.ORDERISSUEDTOAPPLICANT)) {
							registration.setOrderIssueDet(regnStatDet);
							setValidateDate(regnStatDet.getStatusdate());
						}
						if (regnStatDet.getStatus().getCode()
								.equals(BpaConstants.REJECTORDERPREPARED)) {
							registration.setRejectOrdPrepDet(regnStatDet);
							// setValidateDate(regnStatDet.getStatusdate());
						}
						if (regnStatDet.getStatus().getCode()
								.equals(BpaConstants.REJECTORDERISSUED)) {
							registration.setRejectOrdIssDet(regnStatDet);
							// setValidateDate(regnStatDet.getStatusdate());
						}

					}
				}
			}
		}

	}

	public void buildOfficialViewActions() {
		List<RegnOfficialActionsExtn> regnOffcialActionsList = new LinkedList<RegnOfficialActionsExtn>();
		regnOffcialActionsList = registerBpaExtnService
				.getOfficialActionsByRegId(registration.getId());
		List<String> type = new ArrayList<String>();
		type.add(BpaConstants.VIEWED_SURVEYOR_INSPECTION);
		type.add(BpaConstants.VIEWED_AE_AEE_INSPECTION);
		type.add(BpaConstants.VIEWED_SURVEYOR_DOCDETAILS);
		type.add(BpaConstants.VIEWED_AE_AEE_DOCDETAILS);
		type.add(BpaConstants.VIEWED_AUTODCR_DETAILS);
		type.add(BpaConstants.VIEWED_DOCKETSHEET);
		createRegOfficialViewObj(type, regnOffcialActionsList);
	}

	public void createRegOfficialViewObj(List<String> typeList,
			List<RegnOfficialActionsExtn> regnOffcialActionsList) {
		if (typeList != null && !typeList.isEmpty()) {
			for (String viewType : typeList) {
				RegnOfficialActionsExtnView regnOffcialActionsExtnViewObj = new RegnOfficialActionsExtnView();
				for (RegnOfficialActionsExtn record : regnOffcialActionsList) {
					OfficialActions officialActionObj = new OfficialActions();
					officialActionObj.setUserName(record.getCreatedBy()
							.getName());
					if (viewType
							.equalsIgnoreCase(BpaConstants.VIEWED_SURVEYOR_INSPECTION))
						officialActionObj.setActionsValue(record
								.getViewedSurveyorInsp());
					else if (viewType
							.equalsIgnoreCase(BpaConstants.VIEWED_AE_AEE_INSPECTION))
						officialActionObj.setActionsValue(record
								.getViewedAE_AEEInsp());
					if (viewType
							.equalsIgnoreCase(BpaConstants.VIEWED_SURVEYOR_DOCDETAILS))
						officialActionObj.setActionsValue(record
								.getViewedSurveyorDocDtls());
					if (viewType
							.equalsIgnoreCase(BpaConstants.VIEWED_AE_AEE_DOCDETAILS))
						officialActionObj.setActionsValue(record
								.getViewedAE_AEEDocDtls());
					if (viewType
							.equalsIgnoreCase(BpaConstants.VIEWED_AUTODCR_DETAILS))
						officialActionObj.setActionsValue(record
								.getViewedAutoDcrDtls());
					if (viewType
							.equalsIgnoreCase(BpaConstants.VIEWED_DOCKETSHEET))
						officialActionObj.setActionsValue(record
								.getViewedDocketSheet());

					regnOffcialActionsExtnViewObj
							.addOfficialActions(officialActionObj);
				}
				regnOffcialActionsExtnViewObj.setViewType(viewType);
				regnOfficialActionsList.add(regnOffcialActionsExtnViewObj);

			}
		}
	}

	private void setRejectionDetailsforRegistration() {
		if (registration.getRejection() != null)
			rejectChkListDet = new ArrayList(registration.getRejection()
					.getRejectionChecklistSet());
	}

	@SkipValidation
	@Action(value = "/registerBpaExtn-showOrderDetails", results = { @Result(name = "existingOrdDet",type = "dispatcher") })
	public String showOrderDetails() {
		buildOrderDetails();
		return "existingOrdDet";
	}

	@SkipValidation
	@Action(value = "/registerBpaExtn-updateOrders", results = { @Result(name = SUCCESS,type = "dispatcher") })
	public String updateOrders() {
		LOGGER.info("Within updateOrders method-Before existReg");
		// existReg =
		// (Registration)persistenceService.find("from org.egov.bpa.models.Registration where id=?"
		// ,registrationId);

		if (registrationId != null)
			existReg = registerBpaExtnService
					.getRegistrationById(registrationId);
		// Registration existReg =
		// registerBpaService.findById(registrationId,false);
		LOGGER.info("Within updateOrders method-After existReg" + existReg);

		if (reqdAction != null && !"".equals(reqdAction) && existReg != null) {
			if (reqdAction != null
					&& reqdAction.equals(BpaConstants.ADDCHALLANSENTDATE)) {
				existReg.setChallanDetails(registration.getChallanDetails());
				registration = registerBpaExtnService
						.updateChallanSentDate(existReg);
				List<Map<String, Object>> finalAttachmentList = new ArrayList<Map<String, Object>>();
				BigDecimal totalAmount = bpaCommonExtnService
						.isFeeCollectionPending(registration);
				if (!totalAmount.equals(BigDecimal.ZERO)) {
					HashMap<String, Object> attachmentList = new HashMap<String, Object>();
					HashMap<String, Object> attachmentFileNames = new HashMap<String, Object>();
					List<ReportFeesDetailsExtn> reportFeeDetailsList = registerBpaExtnService
							.getSanctionedReportFeeDtls(registration);
					attachmentList.put("feePayment", bpaCommonExtnService
							.generateFeePaymentReportPDF(registration,
									reportFeeDetailsList));
					attachmentFileNames.put("feePaymentFileName",
							"feePaymentReportPDF.pdf");
					finalAttachmentList.add(attachmentList);
					finalAttachmentList.add(attachmentFileNames);
				}
				bpaCommonExtnService.buildEmail(registration,
						BpaConstants.SMSCHALLANNOTICESENTDATE,
						finalAttachmentList);
				bpaCommonExtnService.buildSMS(registration,
						BpaConstants.SMSCHALLANNOTICESENTDATE);
				addActionMessage("Challan Send Date Updated Successfully.");
			} else if (reqdAction.equals(BpaConstants.UPDATESIGNATURE)) {
				existReg.setSignDetails(registration.getSignDetails());
				registration = registerBpaExtnService.updateSignature(existReg);
				List<Map<String, Object>> finalAttachmentList = new ArrayList<Map<String, Object>>();
				HashMap<String, Object> attachmentList = new HashMap<String, Object>();
				HashMap<String, Object> attachmentFileNames = new HashMap<String, Object>();
				if (registration != null
						&& registration.getServiceType() != null
						&& registration.getServiceType().getCode() != null) {
					if (!registration.getServiceType().getCode()
							.equals(BpaConstants.CMDACODE)) {
						attachmentList
								.put("planPermitProvisional",
										bpaReportExtnService
												.getPlanningpermitReportDetails(registration));
						attachmentFileNames.put("planPermitProvisionalName",
								"PlanPermitProvisional.pdf");
					}
					if (registration.getServiceType().getCode()
							.equals(BpaConstants.NEWBUILDINGONVACANTPLOTCODE)
							|| registration
									.getServiceType()
									.getCode()
									.equals(BpaConstants.DEMOLITIONRECONSTRUCTIONCODE)
							|| registration
									.getServiceType()
									.getCode()
									.equals(BpaConstants.ADDITIONALCONSTRUCTIONCODE)
							|| registration.getServiceType().getCode()
									.equals(BpaConstants.CMDACODE)) {
						attachmentList
								.put("buildingPermitProvisional",
										bpaReportExtnService
												.getBuildingPermitReportdetails(registration));
						attachmentFileNames.put(
								"buildingPermitProvisionalName",
								"buildingPermitProvisional.pdf");
					}

					finalAttachmentList.add(attachmentList);
					finalAttachmentList.add(attachmentFileNames);
					bpaCommonExtnService.buildEmail(registration,
							BpaConstants.UPDATESIGNATURE, finalAttachmentList);
					addActionMessage("Signature Updated Successfully.");
				}

			} else if (reqdAction.equals(BpaConstants.ORDERPREPARED)) {
				existReg.setOrderDetails(registration.getOrderDetails());
				registration = registerBpaExtnService
						.updateOrderPrepStatus(existReg);
				addActionMessage("Order Prepared Successfully.");
			} else if (reqdAction.equals(BpaConstants.ORDERISSUED)) {
				existReg.setOrderIssueDet(registration.getOrderIssueDet());
				registration = registerBpaExtnService
						.updateOrderIssueStatus(existReg);
				closeRegistration();
			} else if (reqdAction.equals(BpaConstants.REJECTORDPREP)) {
				existReg.setRejectOrdPrepDet(registration.getRejectOrdPrepDet());
				registration = registerBpaExtnService
						.rejectOrderPrepStatus(existReg);
				addActionMessage("Unconsidered Order Prepared Successfully.");
			} else if (reqdAction.equals(BpaConstants.REJECTORDISSUED)) {
				existReg.setRejectOrdIssDet(registration.getRejectOrdIssDet());
				registration = registerBpaExtnService
						.rejectOrderIssueStatus(existReg);
				addActionMessage("Unconsidered Order Issued Successfully.");
			}
			buildRegistrationForViewModify();
			buildOrderDetails();
			setMode(BpaConstants.MODEVIEW);
			setRejectionDetailsforRegistration();
			String status = bpaCommonExtnService
					.getPrimaryStatusforRegistration(registration);
			if (status != null
					&& status.equals(BpaConstants.PRIMARYSTATUSREJECT)) {
				setRejectview(BpaConstants.MODEREJECT);
			}
		}
		return SUCCESS;
	}

	@SkipValidation
	@Action(value = "/registerBpaExtn-orderForm", results = { @Result(name = "orderDetForm",type = "dispatcher") })
	public String orderForm() {
		buildOrderDetails();
		setMode(EDIT);

		return "orderDetForm";
	}

	public String modifyrejectForm(RegistrationExtn registration) {
		setRejectionMode(BpaConstants.MODEFROMMODIFY);

		return rejectForm();
	}

	@SuppressWarnings("unchecked")
	@SkipValidation
	@Action(value = "/registerBpaExtn-rejectForm", results = { @Result(name = NEW,type = "dispatcher") })
	public String rejectForm() {

		LOGGER.info(" RegisterBpaAction || Save || Start");
		if (getRejectionMode() == null
				|| (getRejectionMode() != null && !getRejectionMode().equals(
						BpaConstants.MODEFROMMODIFY)))
			buildRegistrationForViewModify();
		if (registration.getRejection() == null) {
			List<CheckListDetailsExtn> chkListDetails = registerBpaExtnService
					.getUnconsideredCheckListforService(registration
							.getServiceType().getId());
			for (CheckListDetailsExtn chkList : chkListDetails) {
				RejectionChecklistExtn registrationchk = new RejectionChecklistExtn();
				registrationchk.setCheckListDetails(chkList);
				rejectChkListDet.add(registrationchk);
			}
		} else {
			rejectChkListDet = new ArrayList(registration.getRejection()
					.getRejectionChecklistSet());
		}

		setMode(BpaConstants.MODEREJECT);
		setAdditionalRule(BpaConstants.ADDITONALRULEREJECTBPA);
		registration.setAdditionalRule(BpaConstants.ADDITONALRULEREJECTBPA);
		List<String> roleList = bpaCommonExtnService
				.getRoleNamesByPassingUserId((EgovThreadLocals.getUserId()));
		if (getRejectionMode() == null
				|| (getRejectionMode() != null && !getRejectionMode().equals(
						BpaConstants.MODEFROMMODIFY))) {
			// Changed from "rejection initiated" to "Forwarded to AE" as part
			// of workflow change for Reject Unconsidered
			registration
					.setAdditionalState(BpaConstants.WORKFLOWSTATUSFORWARDEDTOAEORAEE);
		} else {
			registration.setAdditionalState(registration.getState().getValue());
		}

		LOGGER.info(" RegisterBpaAction || Save || End");
		return NEW;

	}

	@SkipValidation
	@Action(value = "/registerBpaExtn-letterToPartyForm", results = { @Result(name = LETTERTOPARTYFORM,type = "dispatcher") })
	public String letterToPartyForm() {
		registration = registerBpaExtnService
				.getRegistrationById(registrationId);
		setRegistrationId(registrationId);
		registration.setAdditionalRule(BpaConstants.LETTERTOPARTYDETAILS);
		setAdditionalRule(BpaConstants.LETTERTOPARTYDETAILS);
		registration.setAdditionalState("LP Initiated");
		/*
		 * To validate For this registration site inspection is added by
		 * official users..Before Proceeding Create LetterToParty
		 */
		User LoggedInuser = bpaCommonExtnService.getUserbyId((EgovThreadLocals
				.getUserId()));
		String UserRole = bpaCommonExtnService.getUserRolesForLoggedInUser();
		if (!UserRole.contains(BpaConstants.PORTALUSERSURVEYORROLE)
				&& LoggedInuser != null) {
			List<InspectionExtn> inspectionList = inspectionExtnService
					.getSiteInspectionListforRegistrationObjectWhereInsDetailsEnetered(
							registration, LoggedInuser);
			if (inspectionList == null || inspectionList.size() == 0) {
				addActionMessage(getMessage("registration.validateinspection.mendatory"));
				return NEW;
			}
		}
		return LETTERTOPARTYFORM;
	}

	@SkipValidation
	@Action(value = "/registerBpaExtn-letterToCMDAForm", results = { @Result(name = "letterToCMDA",type = "dispatcher") })
	public String letterToCMDAForm() {
		registration = registerBpaExtnService
				.getRegistrationById(registrationId);
		setRegistrationId(registrationId);
		registration.setAdditionalRule(BpaConstants.LETTERTOCMDA);
		setAdditionalRule(BpaConstants.LETTERTOCMDA);
		registration
				.setAdditionalState(BpaConstants.LETTERTOCMDA_WF_INITIALSTATUS);

		return "letterToCMDA";
	}

	public List<String> getValidActions() {
		try {
			if (getAdditionalRule() != null
					&& (getAdditionalRule().equals(
							BpaConstants.ADDITONALRULEREJECTBPA) || getAdditionalRule() != null
							&& (getAdditionalRule().equals(
									BpaConstants.LETTERTOPARTYDETAILS) || getAdditionalRule()
									.equals(BpaConstants.LETTERTOCMDA)))) {
				List<String> validActions = Collections.emptyList();
				validActions = this.customizedWorkFlowService
						.getNextValidActions(this.getModel().getStateType(),
								this.getWorkFlowDepartment(),
								this.getAmountRule(), this.getAdditionalRule(),
								this.getModel().getAdditionalState(),
								this.getPendingActions());
				return validActions;

			} else
				return super.getValidActions();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return new ArrayList();
		}
	}

	public String getNextAction() {
		try {
			if ((getAdditionalRule() != null
					&& getAdditionalRule().equals(
							BpaConstants.ADDITONALRULEREJECTBPA) || getAdditionalRule() != null
					&& (getAdditionalRule().equals(
							BpaConstants.LETTERTOPARTYDETAILS) || getAdditionalRule()
							.equals(BpaConstants.LETTERTOCMDA)))
					&& (this.getModel().getAdditionalState()
							.equals(BpaConstants.ADDITONALRULEREJECTBPA)
							|| this.getModel().getAdditionalState()
									.equals("LetterToParty saved")
							|| this.getModel().getAdditionalState()
									.equals("rejection initiated")
							|| this.getModel().getAdditionalState()
									.equals("UnconsiderdBPA")
							|| this.getModel().getAdditionalState()
									.equals("LP Initiated")
							|| this.getModel().getAdditionalState()
									.equals("LetterToCMDA saved") || this
							.getModel().getAdditionalState()
							.equals("LetterToCMDA Initiated"))) {
				WorkFlowMatrix wfMatrix = null;
				String tempcurrentstate = this.getModel().getAdditionalState();
				wfMatrix = this.customizedWorkFlowService.getWfMatrix(this
						.getModel().getStateType(), this
						.getWorkFlowDepartment(), this.getAmountRule(), this
						.getAdditionalRule(), tempcurrentstate, this
						.getPendingActions());
				return wfMatrix == null ? "" : wfMatrix.getNextAction();
			} else
				return super.getNextAction();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return null;
		}
	}

	private InputStream generateLetterToPartyPDF() {
		InputStream inputStream = null;
		ReportRequest reportRequest = null;
		Map<String, Object> reportParams = new HashMap<String, Object>();
		LetterToPartyExtn letterToParty = generateLetterToPartyPrint(reportParams);
		reportRequest = new ReportRequest(BpaConstants.LETTERTOPARTYNOTICEEXTN,
				letterToParty, reportParams);
		if (null != reportRequest) {
			ReportOutput reportOutput = reportService
					.createReport(reportRequest);
			if (reportOutput != null
					&& reportOutput.getReportOutputData() != null)
				inputStream = new ByteArrayInputStream(
						reportOutput.getReportOutputData());
		}
		return inputStream;
	}

	private InputStream generateLetterToCMDAPDF(RegistrationExtn registration) {
		InputStream inputStream = null;
		ReportRequest reportRequest = null;
		Map<String, Object> reportParams = new HashMap<String, Object>();
		Map<String, Object> reportData = bpaCommonExtnService
				.generateCmdaLetterToPartyPrint(registration, null);
		reportParams.put("lpDate", reportData.get("lpDate"));
		reportParams.put("cmdaDate", reportData.get("cmdaDate"));
		reportParams.put("replyDate", reportData.get("replyDate"));
		reportRequest = new ReportRequest(
				BpaConstants.CMDALPREPLYACKREPORTEXTN, reportData, reportParams);
		reportRequest.setPrintDialogOnOpenReport(true);
		if (null != reportRequest) {
			ReportOutput reportOutput = reportService
					.createReport(reportRequest);
			if (reportOutput != null
					&& reportOutput.getReportOutputData() != null)
				inputStream = new ByteArrayInputStream(
						reportOutput.getReportOutputData());
		}
		return inputStream;
	}

	@SkipValidation
	@Action(value = "/registerBpaExtn-printLettertoParty", results = { @Result(name = "lpReportPrint",type = "dispatcher") })
	public String printLettertoParty() {
		try {
			ReportRequest reportRequest = null;
			Map<String, Object> reportParams = new HashMap<String, Object>();
			LetterToPartyExtn letterToParty = generateLetterToPartyPrint(reportParams);
			reportRequest = new ReportRequest(
					BpaConstants.LETTERTOPARTYNOTICEEXTN, letterToParty,
					reportParams);
			reportRequest.setPrintDialogOnOpenReport(true);
			reportId = ReportViewerUtil.addReportToSession(
					reportService.createReport(reportRequest), getSession());

			return "lpReportPrint";
		} catch (Exception e) {
			throw new EGOVRuntimeException("Exception : " + e);
		}

	}

	private LetterToPartyExtn generateLetterToPartyPrint(
			Map<String, Object> reportParams) {
		String fromAddressToLp = "";
		Set<LpChecklistExtn> checkListDtls = new HashSet<LpChecklistExtn>();
		LetterToPartyExtn letterToParty = registerBpaExtnService
				.getLetterToPartyById(letterToPartyId);
		if (letterToParty != null)
			for (LpChecklistExtn lpChecklist : letterToParty
					.getLpChecklistSet()) {

				if (null != lpChecklist.getIsChecked()
						&& lpChecklist.getLpChecklistType() != null
						&& lpChecklist.getLpChecklistType().equals(
								"lettertoparty")) {
					checkListDtls.add(lpChecklist);
				}
			}
		reportParams.put("lettertoPartyChecklistSet", checkListDtls);
		/*
		 * To change the From Address of Letter to Party if fromAddressToLp
		 * variable is empty then From Address should be as old address..
		 */
		if (letterToParty != null
				&& letterToParty.getRegistration().getAdminboundaryid() != null
				&& letterToParty.getRegistration().getAdminboundaryid()
						.getParent() != null
				&& letterToParty.getRegistration().getAdminboundaryid()
						.getParent().getParent() != null)

			if (letterToParty.getRegistration().getAdminboundaryid()
					.getParent().getParent().getName()
					.equalsIgnoreCase(BpaConstants.NORTHREGION)) {
				fromAddressToLp = BpaConstants.ASSISTANTADDRESS + "\n"
						+ BpaConstants.NORTHREGION_ADDRESS;
			} else if (letterToParty.getRegistration().getAdminboundaryid()
					.getParent().getParent().getName()
					.equalsIgnoreCase(BpaConstants.SOUTHREGION)) {
				fromAddressToLp = BpaConstants.ASSISTANTADDRESS + "\n"
						+ BpaConstants.SOUTHREGION_ADDRESS;

			} else if (letterToParty.getRegistration().getAdminboundaryid()
					.getParent().getParent().getName()
					.equalsIgnoreCase(BpaConstants.CENTRALREGION)) {
				fromAddressToLp = BpaConstants.ASSISTANTADDRESS + "\n"
						+ BpaConstants.CENTRALREGION_ADDRESS;
			} else {
				fromAddressToLp = "";
			}
		reportParams.put("fromAddressToLp", fromAddressToLp);
		if (letterToParty != null && letterToParty.getRegistration() != null) {
			if (letterToParty.getRegistration().getCmdaNum() == null
					|| "".equals(letterToParty.getRegistration().getCmdaNum())) {
				reportParams
						.put("registerReferenceNumber",
								"Application No : "
										+ (letterToParty.getRegistration()
												.getPlanSubmissionNum() != null ? letterToParty
												.getRegistration()
												.getPlanSubmissionNum() : "")
										+ " Dated :"
										+ (letterToParty.getRegistration()
												.getPlanSubmissionDate() != null ? new SimpleDateFormat(
												"dd/MM/yyyy")
												.format(letterToParty
														.getRegistration()
														.getPlanSubmissionDate())
												: ""));
			} else {
				reportParams
						.put("registerReferenceNumber",
								"CDMA Letter No : "
										+ (letterToParty.getRegistration()
												.getCmdaNum() != null ? letterToParty
												.getRegistration().getCmdaNum()
												: "")
										+ " Dated :"
										+ (letterToParty.getRegistration()
												.getCmdaRefDate() != null ? new SimpleDateFormat(
												"dd/MM/yyyy")
												.format(letterToParty
														.getRegistration()
														.getCmdaRefDate()) : ""));
			}
		}
		return letterToParty;
	}

	@SkipValidation
	@Action(value = "/registerBpaExtn-feePaymentPdf", results = { @Result(name = FEE_PAYMENT_PDF,type = "dispatcher") })
	public String feePaymentPdf() throws JRException, Exception {
		if (registrationId != null)
			registration = registerBpaExtnService.findById(registrationId,
					false);
		List<ReportFeesDetailsExtn> reportFeeDetailsList = registerBpaExtnService
				.getSanctionedReportFeeDtls(registration);
		feePaymentReportPDF = bpaCommonExtnService.generateFeePaymentReportPDF(
				registration, reportFeeDetailsList);
		return FEE_PAYMENT_PDF;
	}

	@SkipValidation
	@Action(value = "/registerBpaExtn-getCollectedReceipts", results = { @Result(name = "receipts",type = "dispatcher") })
	public String getCollectedReceipts() {

		if (registrationId != null)
			registration = registerBpaExtnService.findById(registrationId,
					false);

		if (registration != null) {
			billReceiptInfoMap = registerBpaExtnService
					.getCollectedReceiptsByRegistrationId(registration);

			for (Map.Entry<String, BillReceiptInfo> map : billReceiptInfoMap
					.entrySet()) {
				billRecptInfoList.add(map.getValue());
			}
		}

		return "receipts";
	}

	@SkipValidation
	@Action(value = "/registerBpaExtn-showLetterToPartyReplyCheckList", results = { @Result(name = "lettertopartyreplychklist",type = "dispatcher") })
	public String showLetterToPartyReplyCheckList() {
		ServiceTypeExtn serviceType = registerBpaExtnService
				.getServiceTypeById(serviceTypeIdTemp);
		if (letterToPartyId == null || letterToPartyId.equals("")) {
			if (serviceType.getCode() != null) {
				List<CheckListDetailsExtn> chkListDetails = registerBpaExtnService
						.getLPCheckListforService(serviceTypeIdTemp);
				for (CheckListDetailsExtn chkList : chkListDetails) {
					LpChecklistExtn lpCheckList = new LpChecklistExtn();
					lpCheckList.setCheckListDetails(chkList);
					lpReplyChkListDet.add(lpCheckList);
				}
			}
		} else {
			if (serviceType.getCode() != null) {
				lpReplyChkListDet = registerBpaExtnService
						.getLetterToPartyCheckDtlsForType(letterToPartyId,
								BpaConstants.LPCHKLISTTYPE_REPLY);
			}
		}
		setMode(mode);
		return "lettertopartyreplychklist";
	}

	@SkipValidation
	@Action(value = "/registerBpaExtn-captureDDForm", results = { @Result(name = "ddForm",type = "dispatcher") })
	public String captureDDForm() {
		buildDDDetails();
		/*
		 * To check FeeDetails Entry is there For current records.
		 */
		List<RegistrationFeeExtn> registrationFee = registrationFeeExtnService
				.getpreviousFeeDetails(registrationId);
		if (null != registration && registrationFee.size() > 0) {
			/*
			 * To get Total Amount of CMDA Deatils and MWGWF details FROM
			 * RegistrationFeeDetails table. to confirm values are entered for
			 * CMDA and MWGwF..
			 */
			Set<RegistrationFeeDetailExtn> captureAmountList = bpaCommonExtnService
					.getLatestApprovedRegistrationFee(registration)
					.getRegistrationFeeDetailsSet();
			// List<RegistrationFeeDetail>
			// captureAmountList=registerBpaService.getCaptureDdAmountTotal(registrationId,registration);
			for (RegistrationFeeDetailExtn chkList : captureAmountList) {
				if (chkList != null) {
					if (chkList.getBpaFee() != null
							&& chkList.getBpaFee().getFeeGroup() != null
							&& chkList.getBpaFee().getFeeGroup()
									.equals(BpaConstants.FEEGROUPCMDA)) {
						if (chkList.getAmount() != null) {
							totalcmda = totalcmda.add(chkList.getAmount());

						}

					} else if (chkList.getBpaFee() != null
							&& chkList.getBpaFee().getFeeGroup() != null
							&& chkList.getBpaFee().getFeeGroup()
									.equals(BpaConstants.FEEGROUPMWGWF)) {
						if (chkList.getAmount() != null) {
							totalmwgwf = totalmwgwf.add(chkList.getAmount());
						}

					}
				}
			}
		}

		if (labourWelfareddList.isEmpty()) {
			labourWelfareddList.add(new RegistrationDDDetailsExtn());
			cmdaddList.add(new RegistrationDDDetailsExtn());
			addDropdownData("bankList", bpaCommonExtnService.getAllBanks());
			registration.setExternalfeecollectedDate(new Date());
			return "ddForm";
		} else {
			labourWelfareddList.clear();
			cmdaddList.clear();
			return viewDDForm();
		}
	}

	@Transactional
	@SkipValidation
	@Action(value = "/registerBpaExtn-captureDDdetails", results = { @Result(name = "ddForm",type = "dispatcher") })
	public String captureDDdetails() {
		// Date tempDate= registration.getExternalfeecollectedDate();
		// registration=registerBpaService.find("from Registration where id=?",getRegistrationId());
		registration = registerBpaExtnService
				.getRegistrationById(getRegistrationId());
		Set<RegistrationDDDetailsExtn> ddDetailsSet = new HashSet<RegistrationDDDetailsExtn>();
		/*
		 * To check FeeDetails Entry is there For current records. AND if not
		 * there den it wil not do equal validation for fee datils for CMDA and
		 * MWGWF and Total DD Amount of both while capturing DD Deatils.
		 * getpreviousFeeDetails from registrationFeeService
		 */
		List<RegistrationFeeExtn> registrationFee = registrationFeeExtnService
				.getpreviousFeeDetails(registrationId);
		if (registrationFee.size() > 0) {
			if (!(mwgwfAmounttotal.equals(totalmwgwf))
					|| !(cmdaAmounttotal.equals(totalcmda))) {

				addActionError("MWGWF and CMDA Fees entered is incorrect.");
				// addDropdownData("bankList",bpaCommonService.getAllBanks());
				if (cmdaddList.isEmpty()) {
					cmdaddList.add(new RegistrationDDDetailsExtn());
				}
				if (labourWelfareddList.isEmpty()) {
					labourWelfareddList.add(new RegistrationDDDetailsExtn());
				}
				// buildDDDetails();
				List<String> temp = new ArrayList<String>();
				temp.add(null);
				labourWelfareddList.removeAll(temp);
				cmdaddList.removeAll(temp);

				return "ddForm";

			}
		}

		for (RegistrationDDDetailsExtn dddtls : getLabourWelfareddList()) {
			if (dddtls != null) {
				if (dddtls.getLwddAmount() != null
						&& !dddtls.getLwddAmount().equals("")) {
					dddtls.setDdAmount(dddtls.getLwddAmount());
				} else
					continue;
				if (dddtls.getLwddbankName() != null
						&& !dddtls.getLwddbankName().equals(""))
					dddtls.setDdBank(bpaCommonExtnService.getBankbyId(dddtls
							.getLwddbankName()));
				else
					continue;
				if (dddtls.getLwddDate() != null
						&& !dddtls.getLwddDate().equals(""))
					dddtls.setDdDate(dddtls.getLwddDate());
				else
					continue;
				if (dddtls.getLwddNumber() != null
						&& !dddtls.getLwddNumber().equals(""))
					dddtls.setDdNumber(dddtls.getLwddNumber());
				else
					continue;
				dddtls.setDdType("LabourWelfare");
				dddtls.setRegistration(registration);
				/*
				 * dddtls.setDdType("LabourWelfare");
				 * dddtls.setRegistration(registration);
				 * dddtls.setDdAmount(dddtls.getLwddAmount());
				 * dddtls.setDdBank(bpaCommonService
				 * .getBankbyId(dddtls.getLwddbankName()));
				 * dddtls.setDdDate(dddtls.getLwddDate());
				 * dddtls.setDdNumber(dddtls.getLwddNumber());
				 */
				ddDetailsSet.add(dddtls);
			}
		}
		for (RegistrationDDDetailsExtn dddtls : getCmdaddList()) {
			if (dddtls != null) {
				if (dddtls.getMsddAmount() != null
						&& !dddtls.getMsddAmount().equals("")) {
					dddtls.setDdAmount(dddtls.getMsddAmount());
				} else
					continue;
				if (dddtls.getMsddbankName() != null
						&& !dddtls.getMsddbankName().equals(""))
					dddtls.setDdBank(bpaCommonExtnService.getBankbyId(dddtls
							.getMsddbankName()));
				else
					continue;
				if (dddtls.getMsddDate() != null
						&& !dddtls.getMsddDate().equals(""))
					dddtls.setDdDate(dddtls.getMsddDate());
				else
					continue;
				if (dddtls.getMsddNumber() != null
						&& !dddtls.getMsddNumber().equals(""))
					dddtls.setDdNumber(dddtls.getMsddNumber());
				else
					continue;
				dddtls.setDdType("CMDA");
				dddtls.setRegistration(registration);
				ddDetailsSet.add(dddtls);
			}
		}
		registration.getFeeDDSet().clear();
		registration.setFeeDDSet(ddDetailsSet);
		registration.setExternalfeecollectedDate(registration
				.getExternalfeecollectedDate());
		registerBpaExtnService.persist(registration);
		labourWelfareddList.clear();
		cmdaddList.clear();
		return viewDDForm();
		// setMode(BpaConstants.MODEVIEW);
		// return "ddForm";
	}

	@SkipValidation
	@Action(value = "/registerBpaExtn-viewDDForm", results = { @Result(name = "ddForm",type = "dispatcher") })
	public String viewDDForm() {
		// addDropdownData("bankList",bpaCommonService.getAllBanks());
		buildDDDetails();
		if (cmdaddList.isEmpty()) {
			cmdaddList.add(new RegistrationDDDetailsExtn());
		}
		if (labourWelfareddList.isEmpty()) {
			labourWelfareddList.add(new RegistrationDDDetailsExtn());
		}
		setMode(BpaConstants.MODEVIEW);
		return "ddForm";
	}

	@SkipValidation
	@Action(value = "/registerBpaExtn-printExternalFeeDetails", results = { @Result(name = "externalFeeDetails",type = "dispatcher") })
	public String printExternalFeeDetails() {
		registration = registerBpaExtnService.findById(getRegistrationId(),
				false);

		if (registration != null && registration.getFeeDDSet() != null
				&& !registration.getFeeDDSet().isEmpty()) {
			reportId = registerBpaExtnService.printExternalFeeDetails(
					registration, getSession());
			return "externalFeeDetails";
		}
		addActionMessage("No Data found. External Fees details are not available for the selected Building Plan Application.");
		return "noExternalFeeDetails";

	}

	private void buildDDDetails() {

		// registration=registerBpaService.find("from Registration where id=?",getRegistrationId());
		registration = registerBpaExtnService
				.getRegistrationById(getRegistrationId());
		for (RegistrationDDDetailsExtn dddtls : registration.getFeeDDSet()) {
			if (dddtls.getDdType().equals("LabourWelfare")) {
				labourWelfareddList.add(prepareDDDetails(dddtls));

			} else if (dddtls.getDdType().equals("CMDA")) {
				cmdaddList.add(prepareDDDetails(dddtls));
			}

		}

	}

	private RegistrationDDDetailsExtn prepareDDDetails(
			RegistrationDDDetailsExtn dddtls) {

		if (dddtls.getDdType().equals("LabourWelfare")) {
			dddtls.setLwddAmount(dddtls.getDdAmount());
			dddtls.setLwddbankName(dddtls.getDdBank() != null ? dddtls
					.getDdBank().getId() : -1);
			dddtls.setLwddNumber(dddtls.getDdNumber());
			dddtls.setLwddDate(dddtls.getDdDate());
		} else if (dddtls.getDdType().equals("CMDA")) {
			dddtls.setMsddAmount(dddtls.getDdAmount());
			dddtls.setMsddbankName(dddtls.getDdBank() != null ? dddtls
					.getDdBank().getId() : -1);
			dddtls.setMsddNumber(dddtls.getDdNumber());
			dddtls.setMsddDate(dddtls.getDdDate());
		}
		return dddtls;
	}

	public RegistrationExtn getRegistration() {
		return registration;
	}

	public void setRegistration(RegistrationExtn registration) {
		this.registration = registration;
	}

	public Integer getAdmissionfeeAmount() {
		return admissionfeeAmount;
	}

	public void setAdmissionfeeAmount(Integer admissionfeeAmount) {
		this.admissionfeeAmount = admissionfeeAmount;
	}

	public List<ChecklistExtn> getDocumentationchecklist() {
		return documentationchecklist;
	}

	public void setDocumentationchecklist(
			List<ChecklistExtn> documentationchecklist) {
		this.documentationchecklist = documentationchecklist;
	}

	public RegisterBpaExtnService getRegisterBpaExtnService() {
		return registerBpaExtnService;
	}

	public void setRegisterBpaExtnService(
			RegisterBpaExtnService registerBpaService) {
		this.registerBpaExtnService = registerBpaService;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public Long getRegistrationId() {
		return registrationId;
	}

	public void setRegistrationId(Long registrationId) {
		this.registrationId = registrationId;
	}

	public FeeExtnService getFeeExtnService() {
		return feeExtnService;
	}

	public void setFeeExtnService(FeeExtnService feeService) {
		this.feeExtnService = feeService;
	}

	public BpaAddressExtn getApplicantrAddress() {
		return applicantrAddress;
	}

	public void setApplicantrAddress(BpaAddressExtn applicantrAddress) {
		this.applicantrAddress = applicantrAddress;
	}

	public BpaAddressExtn getSiteAddress() {
		return siteAddress;
	}

	public void setSiteAddress(BpaAddressExtn siteAddress) {
		this.siteAddress = siteAddress;
	}

	public State getBoundaryState() {
		return boundaryState;
	}

	public void setBoundaryState(State boundaryState) {
		this.boundaryState = boundaryState;
	}

	public List<RegistrationChecklistExtn> getChkListDet() {
		return chkListDet;
	}

	public void setChkListDet(List<RegistrationChecklistExtn> chkListDet) {
		this.chkListDet = chkListDet;
	}

	public Long getServiceTypeIdTemp() {
		return serviceTypeIdTemp;
	}

	public void setServiceTypeIdTemp(Long serviceTypeIdTemp) {
		this.serviceTypeIdTemp = serviceTypeIdTemp;
	}

	public List<InspectionExtn> getExistingSiteInspectionDetails() {
		return existingSiteInspectionDetails;
	}

	public void setExistingSiteInspectionDetails(
			List<InspectionExtn> existingSiteInspectionDetails) {
		this.existingSiteInspectionDetails = existingSiteInspectionDetails;
	}

	public Long getInspectionId() {
		return inspectionId;
	}

	public void setInspectionId(Long inspectionId) {
		this.inspectionId = inspectionId;
	}

	public List<InspectionExtn> getPostponedInspectionDetails() {
		return postponedInspectionDetails;
	}

	public void setPostponedInspectionDetails(
			List<InspectionExtn> postponedInspectionDetails) {
		this.postponedInspectionDetails = postponedInspectionDetails;
	}

	public InspectionExtnService getInspectionExtnService() {
		return inspectionExtnService;
	}

	public void setInspectionExtnService(InspectionExtnService inspectionService) {
		this.inspectionExtnService = inspectionService;
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

	public Long getExistingbuildingCategoryId() {
		return existingbuildingCategoryId;
	}

	public void setExistingbuildingCategoryId(Long existingbuildingCategoryId) {
		this.existingbuildingCategoryId = existingbuildingCategoryId;
	}

	public Long getProposedbuildingCategoryId() {
		return proposedbuildingCategoryId;
	}

	public void setProposedbuildingCategoryId(Long proposedbuildingCategoryId) {
		this.proposedbuildingCategoryId = proposedbuildingCategoryId;
	}

	public List<LpChecklistExtn> getLpChkListDet() {
		return lpChkListDet;
	}

	public void setLpChkListDet(List<LpChecklistExtn> lpChkListDet) {
		this.lpChkListDet = lpChkListDet;
	}

	public String getReturnStream() {
		return returnStream;
	}

	public void setReturnStream(String returnStream) {
		this.returnStream = returnStream;
	}

	public List<LetterToPartyExtn> getExistingLetterToPartyDetails() {
		return existingLetterToPartyDetails;
	}

	public void setExistingLetterToPartyDetails(
			List<LetterToPartyExtn> existingLetterToPartyDetails) {
		this.existingLetterToPartyDetails = existingLetterToPartyDetails;
	}

	public LetterToPartyExtn getLetterToParty() {
		return letterToParty;
	}

	public void setLetterToParty(LetterToPartyExtn letterToParty) {
		this.letterToParty = letterToParty;
	}

	public Long getLetterToPartyId() {
		return letterToPartyId;
	}

	public void setLetterToPartyId(Long letterToPartyId) {
		this.letterToPartyId = letterToPartyId;
	}

	public String getAdditionalRule() {
		return additionalRule;
	}

	public void setAdditionalRule(String additionalRule) {
		this.additionalRule = additionalRule;
	}

	public BpaCommonExtnService getBpaCommonExtnService() {
		return bpaCommonExtnService;
	}

	public void setBpaCommonExtnService(BpaCommonExtnService bpaCommonService) {
		this.bpaCommonExtnService = bpaCommonService;
	}

	public List<String> getShowActions() {
		return showActions;
	}

	public void setShowActions(List<String> showActions) {
		this.showActions = showActions;
	}

	public String getDocumentNum() {
		return documentNum;
	}

	public void setDocumentNum(String documentNum) {
		this.documentNum = documentNum;
	}

	public List<RejectionChecklistExtn> getRejectChkListDet() {
		if (rejectChkListDet != null && rejectChkListDet.size() > 0)
			Collections.sort(rejectChkListDet);
		return rejectChkListDet;
	}

	public BpaBillExtnServiceImpl getBpaBillExtnServiceImpl() {
		return bpaBillExtnServiceImpl;
	}

	public void setBpaBillExtnServiceImpl(
			BpaBillExtnServiceImpl bpaBillServiceImpl) {
		this.bpaBillExtnServiceImpl = bpaBillServiceImpl;
	}

	public BpaBillableExtn getBpaBillableExtn() {
		return bpaBillableExtn;
	}

	public void setBpaBillableExtn(BpaBillableExtn bpaBillable) {
		this.bpaBillableExtn = bpaBillable;
	}

	public String getCollectXML() {
		return collectXML;
	}

	@SuppressWarnings("deprecation")
	public void setCollectXML(String collectXML) {
		this.collectXML = java.net.URLDecoder.decode(collectXML);
	}

	public void setRejectChkListDet(
			List<RejectionChecklistExtn> rejectChkListDet) {
		this.rejectChkListDet = rejectChkListDet;
	}

	public RegnStatusDetailExtnService getRegnStatusDetExtnService() {
		return regnStatusDetExtnService;
	}

	public void setRegnStatusDetExtnService(
			RegnStatusDetailExtnService regnStatusDetService) {
		this.regnStatusDetExtnService = regnStatusDetService;
	}

	/*
	 * public FileManagementService getFileManagementService() { return
	 * fileManagementService;
	 */

	public String getRejectionMode() {
		return rejectionMode;
	}

	public void setRejectionMode(String rejectionMode) {
		this.rejectionMode = rejectionMode;
	}

	public String getAutoDcrNum() {
		return autoDcrNum;
	}

	public void setAutoDcrNum(String autoDcrNum) {
		this.autoDcrNum = autoDcrNum;
	}

	public AutoDcrExtnService getAutoDcrExtnService() {
		return autoDcrExtnService;
	}

	public void setAutoDcrExtnService(AutoDcrExtnService autoDcrService) {
		this.autoDcrExtnService = autoDcrService;
	}

	public InputStream getFeePaymentReportPDF() {
		return feePaymentReportPDF;
	}

	public void setFeePaymentReportPDF(InputStream feePaymentReportPDF) {
		this.feePaymentReportPDF = feePaymentReportPDF;
	}

	public BpaNumberGenerationExtnService getBpaNumberGenerationExtnService() {
		return bpaNumberGenerationExtnService;
	}

	public void setBpaNumberGenerationExtnService(
			BpaNumberGenerationExtnService bpaNumberExtnGenerationService) {
		this.bpaNumberGenerationExtnService = bpaNumberExtnGenerationService;
	}

	public List<String> getShowOrdAction() {
		return showOrdAction;
	}

	public void setShowOrdAction(List<String> showOrdAction) {
		this.showOrdAction = showOrdAction;
	}

	public String getAutonum() {
		return autonum;
	}

	public void setAutonum(String autonum) {
		this.autonum = autonum;
	}

	public String getReqdAction() {
		return reqdAction;
	}

	public void setReqdAction(String reqdAction) {
		this.reqdAction = reqdAction;
	}

	public String getIsPlotAreaEditable() {
		return isPlotAreaEditable;
	}

	public void setIsPlotAreaEditable(String isPlotAreaEditable) {
		this.isPlotAreaEditable = isPlotAreaEditable;
	}

	public List<BillReceiptInfo> getBillRecptInfoList() {
		return billRecptInfoList;
	}

	public void setBillRecptInfoList(List<BillReceiptInfo> billRecptInfoList) {
		this.billRecptInfoList = billRecptInfoList;
	}

	public List<LpChecklistExtn> getLpReplyChkListDet() {
		return lpReplyChkListDet;
	}

	public void setLpReplyChkListDet(List<LpChecklistExtn> lpReplyChkListDet) {
		this.lpReplyChkListDet = lpReplyChkListDet;
	}

	public List<RegistrationDDDetailsExtn> getLabourWelfareddList() {
		return labourWelfareddList;
	}

	public void setLabourWelfareddList(
			List<RegistrationDDDetailsExtn> labourWelfareddList) {
		this.labourWelfareddList = labourWelfareddList;
	}

	public List<RegistrationDDDetailsExtn> getCmdaddList() {
		return cmdaddList;
	}

	public void setCmdaddList(List<RegistrationDDDetailsExtn> cmdaddList) {
		this.cmdaddList = cmdaddList;
	}

	public String getAdditionalMode() {
		return additionalMode;
	}

	public void setAdditionalMode(String additionalMode) {
		this.additionalMode = additionalMode;
	}

	public String getRejectview() {
		return rejectview;
	}

	public void setRejectview(String rejectview) {
		this.rejectview = rejectview;
	}

	public Date getApplicationdate() {
		return applicationdate;
	}

	public void setApplicationdate(Date applicationdate) {
		this.applicationdate = applicationdate;
	}

	public Date getValidateDate() {
		return validateDate;
	}

	public void setValidateDate(Date validateDate) {
		this.validateDate = validateDate;
	}

	public Integer getApproverDepartment() {
		return approverDepartment;
	}

	public void setApproverDepartment(Integer approverDepartment) {
		this.approverDepartment = approverDepartment;
	}

	public BpaPimsInternalExtnServiceFactory getBpaPimsExtnFactory() {
		return bpaPimsExtnFactory;
	}

	public void setBpaPimsExtnFactory(
			BpaPimsInternalExtnServiceFactory bpaPimsFactory) {
		this.bpaPimsExtnFactory = bpaPimsFactory;
	}

	public String getExistPpaNumber() {
		return existPpaNumber;
	}

	public void setExistPpaNumber(String existPpaNumber) {
		this.existPpaNumber = existPpaNumber;
	}

	public String getExistBaNumber() {
		return existBaNumber;
	}

	public void setExistBaNumber(String existBaNumber) {
		this.existBaNumber = existBaNumber;
	}

	public Boolean getPpanum() {
		return ppanum;
	}

	public void setPpanum(Boolean ppanum) {
		this.ppanum = ppanum;
	}

	public String getBparoles() {
		return bparoles;
	}

	public void setBparoles(String bparoles) {
		this.bparoles = bparoles;
	}

	public List<AppConfigValues> getAppConfigValuesRoleList() {
		return appConfigValuesRoleList;
	}

	public void setAppConfigValuesRoleList(
			List<AppConfigValues> appConfigValuesRoleList) {
		this.appConfigValuesRoleList = appConfigValuesRoleList;
	}

	/*
	 * public CommonsManager getCommonsManager() { return commonsManager; }
	 * 
	 * public void setCommonsManager(CommonsManager commonsManager) {
	 * this.commonsManager = commonsManager; }
	 */

	public String getRegObjectHistoryRemarks() {
		return regObjectHistoryRemarks;
	}

	public void setRegObjectHistoryRemarks(String regObjectHistoryRemarks) {
		this.regObjectHistoryRemarks = regObjectHistoryRemarks;
	}

	public BigDecimal getTotalcmda() {
		return totalcmda;
	}

	public void setTotalcmda(BigDecimal totalcmda) {
		this.totalcmda = totalcmda;
	}

	public BigDecimal getTotalmwgwf() {
		return totalmwgwf;
	}

	public void setTotalmwgwf(BigDecimal totalmwgwf) {
		this.totalmwgwf = totalmwgwf;
	}

	public BigDecimal getMwgwfAmounttotal() {
		return mwgwfAmounttotal;
	}

	public void setMwgwfAmounttotal(BigDecimal mwgwfAmounttotal) {
		this.mwgwfAmounttotal = mwgwfAmounttotal;
	}

	public BigDecimal getCmdaAmounttotal() {
		return cmdaAmounttotal;
	}

	public void setCmdaAmounttotal(BigDecimal cmdaAmounttotal) {
		this.cmdaAmounttotal = cmdaAmounttotal;
	}

	public RegistrationFeeExtnService getRegistrationFeeExtnService() {
		return registrationFeeExtnService;
	}

	public void setRegistrationFeeExtnService(
			RegistrationFeeExtnService registrationFeeService) {
		this.registrationFeeExtnService = registrationFeeService;
	}

	public Integer getApproverPositionId() {
		return approverPositionId;
	}

	public void setApproverPositionId(Integer approverPositionId) {
		this.approverPositionId = approverPositionId;
	}

	public Integer getApproverDesignation() {
		return approverDesignation;
	}

	public void setApproverDesignation(Integer approverDesignation) {
		this.approverDesignation = approverDesignation;
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

	public String getSurveyorNameLocal() {
		return surveyorNameLocal;
	}

	public void setSurveyorNameLocal(String surveyorNameLocal) {
		this.surveyorNameLocal = surveyorNameLocal;
	}

	public String getSurveyorCode() {
		return surveyorCode;
	}

	public void setSurveyorCode(String surveyorCode) {
		this.surveyorCode = surveyorCode;
	}

	public String getSurveyorClass() {
		return surveyorClass;
	}

	public void setSurveyorClass(String surveyorClass) {
		this.surveyorClass = surveyorClass;
	}

	public Long getSurveyor() {
		return Surveyor;
	}

	public void setSurveyor(Long surveyor) {
		Surveyor = surveyor;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public Boolean getCurrentstate() {
		return currentstate;
	}

	public void setCurrentstate(Boolean currentstate) {
		this.currentstate = currentstate;
	}

	public EisUtilService getEisService() {
		return eisService;
	}

	public void setEisService(EisUtilService eisService) {
		this.eisService = eisService;
	}

	public Boolean getForwardNotApplicable() {
		return forwardNotApplicable;
	}

	public void setForwardNotApplicable(Boolean forwardNotApplicable) {
		this.forwardNotApplicable = forwardNotApplicable;
	}

	public BpaReportExtnService getBpaReportExtnService() {
		return bpaReportExtnService;
	}

	public void setBpaReportExtnService(
			BpaReportExtnService bpaReportExtnService) {
		this.bpaReportExtnService = bpaReportExtnService;
	}

	public String getStateForValidate() {
		return stateForValidate;
	}

	public void setStateForValidate(String stateForValidate) {
		this.stateForValidate = stateForValidate;
	}

	public Boolean getServiceTypeCode() {
		return serviceTypeCode;
	}

	public void setServiceTypeCode(Boolean serviceTypeCode) {
		this.serviceTypeCode = serviceTypeCode;
	}

	public List<DocketDocumentDetails> getDocketDocumentDtls() {
		return docketDocumentDtls;
	}

	public DocumentHistoryExtn getDocumentHistory() {
		return documentHistory;
	}

	public void setDocumentHistory(DocumentHistoryExtn documentHistory) {
		this.documentHistory = documentHistory;
	}

	public List<DocumentHistoryExtnDetails> getDocumentHistoryDetailList() {
		return documentHistoryDetailList;
	}

	public void setDocumentHistoryDetailList(
			List<DocumentHistoryExtnDetails> documentHistoryDetailList) {
		this.documentHistoryDetailList = documentHistoryDetailList;
	}

	public void setDocketDocumentDtls(
			List<DocketDocumentDetails> docketDocumentDtls) {
		this.docketDocumentDtls = docketDocumentDtls;
	}

	public List<DocketConstructionStage> getConstructionStages() {
		return constructionStages;
	}

	public void setConstructionStages(
			List<DocketConstructionStage> constructionStages) {
		this.constructionStages = constructionStages;
	}

	public List<DocketFloorDetails> getDocketFloorDetail() {
		return docketFloorDetail;
	}

	public void setDocketFloorDetail(List<DocketFloorDetails> docketFloorDetail) {
		this.docketFloorDetail = docketFloorDetail;
	}

	public List<DocketViolations> getDevContrlList() {
		return devContrlList;
	}

	public void setDevContrlList(List<DocketViolations> devContrlList) {
		this.devContrlList = devContrlList;
	}

	public List<DocketViolations> getSetBackList() {
		return setBackList;
	}

	public void setSetBackList(List<DocketViolations> setBackList) {
		this.setBackList = setBackList;
	}

	public List<DocketViolations> getParkingList() {
		return parkingList;
	}

	public void setParkingList(List<DocketViolations> parkingList) {
		this.parkingList = parkingList;
	}

	public List<DocketViolations> getGeneralList() {
		return generalList;
	}

	public void setGeneralList(List<DocketViolations> generalList) {
		this.generalList = generalList;
	}

	public List<DocketViolations> getMinDistancePowerLineList() {
		return minDistancePowerLineList;
	}

	public void setMinDistancePowerLineList(
			List<DocketViolations> minDistancePowerLineList) {
		this.minDistancePowerLineList = minDistancePowerLineList;
	}

	public Docket getDocket() {
		return docket;
	}

	public void setDocket(Docket docket) {
		this.docket = docket;
	}

	public Map<String, String> getApplicantStatus() {
		return applicantStatus;
	}

	public void setApplicantStatus(Map<String, String> applicantStatus) {
		this.applicantStatus = applicantStatus;
	}

	public Boolean getEnableDocketSheetForView() {
		return enableDocketSheetForView;
	}

	public void setEnableDocketSheetForView(Boolean enableDocketSheetForView) {
		this.enableDocketSheetForView = enableDocketSheetForView;
	}

	public String getPreviousApproverComments() {
		return previousApproverComments;
	}

	public void setPreviousApproverComments(String previousApproverComments) {
		this.previousApproverComments = previousApproverComments;
	}

	public Boolean getShowSave_ActionField() {
		return showSave_ActionField;
	}

	public void setShowSave_ActionField(Boolean showSave_ActionField) {
		this.showSave_ActionField = showSave_ActionField;
	}

	public String getPrintMode() {
		return printMode;
	}

	public void setPrintMode(String printMode) {
		this.printMode = printMode;
	}

	public Boolean getIsDocHistoryByAEorAEE() {
		return isDocHistoryByAEorAEE;
	}

	public void setIsDocHistoryByAEorAEE(Boolean isDocHistoryByAEorAEE) {
		this.isDocHistoryByAEorAEE = isDocHistoryByAEorAEE;
	}

	public List<RegnOfficialActionsExtnView> getRegnOfficialActionsList() {
		return regnOfficialActionsList;
	}

	public void setRegnOfficialActionsList(
			List<RegnOfficialActionsExtnView> regnOfficialActionsList) {
		this.regnOfficialActionsList = regnOfficialActionsList;
	}

	public PersistenceService<RegnOfficialActionsExtn, Long> getRegnOfficialActionsExtnService() {
		return regnOfficialActionsExtnService;
	}

	public void setRegnOfficialActionsExtnService(
			PersistenceService<RegnOfficialActionsExtn, Long> regnOfficialActionsExtnService) {
		this.regnOfficialActionsExtnService = regnOfficialActionsExtnService;
	}

	public String getApprovedWorkflowState() {
		return approvedWorkflowState;
	}

	public List<CMDALetterToPartyExtn> getExistingCmdaLetterToPartyDetails() {
		return existingCmdaLetterToPartyDetails;
	}

	public void setExistingCmdaLetterToPartyDetails(
			List<CMDALetterToPartyExtn> existingCmdaLetterToPartyDetails) {
		this.existingCmdaLetterToPartyDetails = existingCmdaLetterToPartyDetails;
	}

	public void setApprovedWorkflowState(String approvedWorkflowState) {
		this.approvedWorkflowState = approvedWorkflowState;
	}

	public CMDALetterToPartyExtn getLetterToCMDA() {
		return letterToCMDA;
	}

	public void setLetterToCMDA(CMDALetterToPartyExtn letterToCMDA) {
		this.letterToCMDA = letterToCMDA;
	}

	public Long getLetterToPartyCmdaId() {
		return letterToPartyCmdaId;
	}

	public void setLetterToPartyCmdaId(Long letterToPartyCmdaId) {
		this.letterToPartyCmdaId = letterToPartyCmdaId;
	}

	public String getBoundaryStateId() {
		return boundaryStateId;
	}

	public void setBoundaryStateId(String boundaryStateId) {
		this.boundaryStateId = boundaryStateId;
	}

}
