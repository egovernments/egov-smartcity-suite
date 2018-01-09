/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */
/**
 *
 */
package org.egov.ptis.actions.objection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.ResultPath;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.Area;
import org.egov.commons.EgwStatus;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.commons.entity.Source;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.EisCommonService;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.notification.service.NotificationService;
import org.egov.infra.persistence.entity.Address;
import org.egov.infra.reporting.engine.ReportFormat;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.reporting.viewer.ReportViewerUtil;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.ApplicationNumberGenerator;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.infra.workflow.service.WorkflowService;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.commons.Position;
import org.egov.portal.entity.PortalInbox;
import org.egov.ptis.actions.common.PropertyTaxBaseAction;
import org.egov.ptis.actions.view.ViewPropertyAction;
import org.egov.ptis.bean.PropertyNoticeInfo;
import org.egov.ptis.client.util.PropertyTaxNumberGenerator;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.dao.property.PropertyStatusDAO;
import org.egov.ptis.domain.dao.property.PropertyStatusValuesDAO;
import org.egov.ptis.domain.entity.enums.TransactionType;
import org.egov.ptis.domain.entity.objection.RevisionPetition;
import org.egov.ptis.domain.entity.property.*;
import org.egov.ptis.domain.entity.property.vacantland.LayoutApprovalAuthority;
import org.egov.ptis.domain.entity.property.vacantland.VacantLandPlotArea;
import org.egov.ptis.domain.repository.master.vacantland.LayoutApprovalAuthorityRepository;
import org.egov.ptis.domain.repository.master.vacantland.VacantLandPlotAreaRepository;
import org.egov.ptis.domain.service.notice.NoticeService;
import org.egov.ptis.domain.service.property.PropertyService;
import org.egov.ptis.domain.service.property.SMSEmailService;
import org.egov.ptis.domain.service.reassign.ReassignService;
import org.egov.ptis.domain.service.revisionPetition.RevisionPetitionService;
import org.egov.ptis.exceptions.TaxCalculatorExeption;
import org.egov.ptis.notice.PtNotice;
import org.egov.ptis.report.bean.PropertyAckNoticeInfo;
import org.egov.ptis.service.utils.PropertyTaxCommonUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import static org.egov.ptis.constants.PropertyTaxConstants.*;

/**
 * @author pradeep
 */
@ParentPackage("egov")
@Namespace("/revPetition")
@ResultPath(value = "/WEB-INF/jsp")
@Results({ @Result(name = "new", location = "revPetition/revisionPetition-new.jsp"),
		@Result(name = "message", location = "revPetition/revisionPetition-message.jsp"),
		@Result(name = "notice", location = "revPetition/revisionPetition-notice.jsp"),
		@Result(name = "view", location = "revPetition/revisionPetition-view.jsp"),
		@Result(name = RevisionPetitionAction.COMMON_FORM, location = "search/searchProperty-commonForm.jsp"),
		@Result(name = RevisionPetitionAction.DIGITAL_SIGNATURE_REDIRECTION, location = "revPetition/revisionPetition-digitalSignatureRedirection.jsp"),
		@Result(name = RevisionPetitionAction.MEESEVA_RESULT_ACK, location = "common/meesevaAck.jsp"),
		@Result(name = RevisionPetitionAction.MEESEVA_ERROR, location = "common/meeseva-errorPage.jsp") })
public class RevisionPetitionAction extends PropertyTaxBaseAction {
	private static final Logger logger = Logger.getLogger(RevisionPetitionAction.class);
	private static final String NOTEXISTS_POSITION = "notexists.position";
	private static final String APPROVE = "Approve";
	private static final String PRINT_ENDORESEMENT = "Print Endoresement";
	private static final String REJECT = "reject";
	private static final String CHOOSE = "----Choose----";
	private static final String REJECT_INSPECTION_STR = "Reject Inspection";
	private static final String FORWARD_TO_APPROVER = "forward to approver";
	private static final String CANCEL_UNCONSIDERED = "cancel unconsidered";
	private static final String HEARING_NOTCIE_EXCEPTION_MESSAGE = "Exception while generating Hearing Notcie : ";
	private static final String GENERAL_REVISION_PETETION = "GENERAL_REVISION_PETETION";
	private static final String REVISION_PETETION = "REVISION_PETETION";
	protected static final String DIGITAL_SIGNATURE_REDIRECTION = "digitalSignatureRedirection";
	private static final long serialVersionUID = 1L;
	protected static final String COMMON_FORM = "commonForm";
	private static final String REJECTED = "Rejected";
	public static final String STRUTS_RESULT_MESSAGE = "message";
	private static final String REVISION_PETITION_CREATED = "CREATED";
	private static final String REVISION_PETITION_HEARINGNOTICEGENERATED = "HEARINGNOTICEGENERATED";
	private static final String REVISION_PETITION_ENDORESEMENTGENERATED = "ENDORESEMTNTGENERATED";
	private static final String PREVIEW = "Preview";
	public static final String NOTICE = "notice";
	public static final String MEESEVA_ERROR = "error";
	public static final String OBJECTION_FORWARD = "objection.forward";
	public static final String REJECT_INSPECTION = "objection.inspection.rejection";


	private ViewPropertyAction viewPropertyAction = new ViewPropertyAction();
	private RevisionPetition objection = new RevisionPetition();
	private String propertyId;
	private transient Map<String, Object> viewMap;
	private transient RevisionPetitionService revisionPetitionService;
	protected transient WorkflowService<RevisionPetition> objectionWorkflowService;
	private String ownerName;
	private String propertyAddress;
	private transient PersistenceService<Property, Long> propertyImplService;
	private String propTypeObjId;
	private String[] floorNoStr = new String[275];
	private Boolean loggedUserIsEmployee = Boolean.TRUE;
	private transient PropertyService propService;
	private PropertyStatusValues propStatVal;
	private String reasonForModify;
	private SortedMap<Integer, String> floorNoMap;
	private Map<String, String> deviationPercentageMap;
	private Map<String, String> hearingTimingMap;
	private String areaOfPlot;

	private List<DocumentType> documentTypes = new ArrayList<>();
	private String northBoundary;
	private String southBoundary;
	private String eastBoundary;
	private String westBoundary;
	private Map<String, String> propTypeCategoryMap;
	private String reportId;
	private Long taxExemptedReason;
	private String currentStatus;
	private List<VacantLandPlotArea> vacantLandPlotAreaList = new ArrayList<>();
	private List<LayoutApprovalAuthority> layoutApprovalAuthorityList = new ArrayList<>();
	private Long vacantLandPlotAreaId;
	private Long layoutApprovalAuthorityId;
	private boolean citizenPortalUser;
        
	@Autowired
	private transient PropertyStatusValuesDAO propertyStatusValuesDAO;
	@Autowired
	private transient ReportService reportService;
	@Autowired
	private transient NoticeService noticeService;
	@Autowired
	private transient BasicPropertyDAO basicPropertyDAO;
	private transient PropertyTaxNumberGenerator propertyTaxNumberGenerator;
	@Autowired
	@Qualifier("workflowService")
	protected transient SimpleWorkflowService<RevisionPetition> revisionPetitionWorkFlowService;

	private boolean isShowAckMessage;
	@Autowired
	private transient PtDemandDao ptDemandDAO;
	@Autowired
	private transient SecurityUtils securityUtils;
	@Autowired
	private transient UserService userService;

	@Autowired
	private transient PropertyStatusDAO propertyStatusDAO;

	@Autowired
	private transient EgwStatusHibernateDAO egwStatusDAO;
	@Autowired
	private transient EisCommonService eisCommonService;
	@Autowired
	private transient PositionMasterService positionMasterService;

	@Autowired
	private transient ApplicationNumberGenerator applicationNumberGenerator;
	@Autowired
	private transient NotificationService notificationService;
	@Autowired
	private transient PropertyTaxCommonUtils propertyTaxCommonUtils;
	@Autowired
	private transient ReportViewerUtil reportViewerUtil;
	
	@Autowired
	private transient ReassignService reassignmentservice;

	private transient SMSEmailService sMSEmailService;
	private String actionType;
	private String fileStoreIds;
	private String ulbCode;
	private transient Map<String, Object> wfPropTaxDetailsMap;
	private boolean digitalSignEnabled;
	private Boolean isMeesevaUser = Boolean.FALSE;
	private String meesevaApplicationNumber;
	private String wfType;
	private boolean allowEditDocument = Boolean.FALSE;
	private Boolean showAckBtn = Boolean.FALSE;
	private String applicationSource;
	private boolean isGenerateAck = true;

	@Autowired
	private transient VacantLandPlotAreaRepository vacantLandPlotAreaRepository;

	@Autowired
	private transient LayoutApprovalAuthorityRepository layoutApprovalAuthorityRepository;

	@Autowired
	private transient CityService cityService;

	public RevisionPetitionAction() {

		addRelatedEntity("basicProperty", BasicPropertyImpl.class);
		addRelatedEntity("property.propertyDetail.propertyTypeMaster", PropertyTypeMaster.class);
		addRelatedEntity("property.propertyDetail.sitalArea", Area.class);

		addRelatedEntity("property", PropertyImpl.class);
		addRelatedEntity("property.propertyDetail.floorType", FloorType.class);
		addRelatedEntity("property.propertyDetail.roofType", RoofType.class);
		addRelatedEntity("property.propertyDetail.wallType", WallType.class);
		addRelatedEntity("property.propertyDetail.woodType", WoodType.class);
		this.addRelatedEntity("structureClassification", StructureClassification.class);
		this.addRelatedEntity("property.propertyDetail.apartment", Apartment.class);
	}

	@Override
	public RevisionPetition getModel() {

		return objection;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void prepare() {
		User user;
		// to merge the new values from jsp with existing
		if (objection.getId() != null)
			objection = revisionPetitionService.findById(objection.getId(), false);
		else if(parameters.get("objectionId")!=null)
                    objection = revisionPetitionService.findById(Long.valueOf(parameters.get("objectionId")[0]), false);
		if (objection != null && objection.getId() == null) {
			objection.setRecievedOn(new Date());
			user = securityUtils.getCurrentUser();
			if (user != null)
				objection.setRecievedBy(user.getName());
		}
		if (null != objection && null != objection.getState()){
			historyMap = propService.populateHistory(objection);
			ownersName = objection.getBasicProperty().getFullOwnerName();
                        applicationNumber =  objection.getObjectionNumber();
                        assessmentNumber = objection.getBasicProperty().getUpicNo();
                        endorsementNotices = propertyTaxCommonUtils.getEndorsementNotices(applicationNumber);
                        endorsementRequired = propertyTaxCommonUtils.getEndorsementGenerate(securityUtils.getCurrentUser().getId(),
                                    objection.getCurrentState());
                        if (PROPERTY_MODIFY_REASON_GENERAL_REVISION_PETITION.equals(objection.getType()))
                            transactionType = GENERAL_REVISION_PETITION;
                         else
                            transactionType = REVISION_PETITION;
		}
		loggedUserIsEmployee = propService.isEmployee(securityUtils.getCurrentUser())
				&& !ANONYMOUS_USER.equalsIgnoreCase(securityUtils.getCurrentUser().getName());
		isMeesevaUser = propService.isMeesevaUser(securityUtils.getCurrentUser());
		citizenPortalUser = propService.isCitizenPortalUser(securityUtils.getCurrentUser());
		super.prepare();
		setUserInfo();
		documentTypes = propService.getDocumentTypesForTransactionType(TransactionType.OBJECTION);
		final List<WallType> wallTypes = getPersistenceService().findAllBy("from WallType order by name");
		final List<WoodType> woodTypes = getPersistenceService().findAllBy("from WoodType order by name");
		final List<PropertyTypeMaster> propTypeList = getPersistenceService()
				.findAllBy("from PropertyTypeMaster where type != 'EWSHS' order by orderNo");
		final List<PropertyMutationMaster> propMutList = getPersistenceService()
				.findAllBy("from PropertyMutationMaster where type = 'MODIFY' and code in('OBJ')");
		final List<String> structureList = getPersistenceService().findAllBy("from StructureClassification where isActive = true order by typeName ");
		final List<PropertyOccupation> propOccList = getPersistenceService().findAllBy("from PropertyOccupation");
		final List<String> ageFacList = getPersistenceService().findAllBy("from DepreciationMaster");
		setFloorNoMap(FLOOR_MAP);
		addDropdownData("floorType", getPersistenceService().findAllBy("from FloorType order by name"));
		addDropdownData("roofType", getPersistenceService().findAllBy("from RoofType order by name"));
		final List<String> apartmentsList = getPersistenceService().findAllBy("from Apartment order by name");
		final List<String> taxExemptionReasonList = getPersistenceService()
				.findAllBy("from TaxExemptionReason where isActive = true order by name");
		addDropdownData("wallType", wallTypes);
		addDropdownData("woodType", woodTypes);
		addDropdownData("PropTypeMaster", propTypeList);
		addDropdownData("OccupancyList", propOccList);
		populateUsages(objection.getProperty() != null ? objection.getProperty().getPropertyDetail().getCategoryType() : "");
		addDropdownData("MutationList", propMutList);
		addDropdownData("StructureList", structureList);
		addDropdownData("AgeFactorList", ageFacList);
		addDropdownData("apartments", apartmentsList);
		addDropdownData("taxExemptionReasonList", taxExemptionReasonList);
		populatePropertyTypeCategory();
		setDeviationPercentageMap(DEVIATION_PERCENTAGE);
		setHearingTimingMap(HEARING_TIMINGS);
		digitalSignEnabled = propertyTaxCommonUtils.isDigitalSignatureEnabled();
		setVacantLandPlotAreaList(vacantLandPlotAreaRepository.findAll());
		setLayoutApprovalAuthorityList(layoutApprovalAuthorityRepository.findAll());
		addDropdownData("vacantLandPlotAreaList", vacantLandPlotAreaList);
		addDropdownData("layoutApprovalAuthorityList", layoutApprovalAuthorityList);
	}

	/**
	 * Method to create new revision petition.
	 *
	 * @return NEW
	 */
	@SkipValidation
	@Actions({ @Action(value = "/revPetition-newForm"), @Action(value = "/genRevPetition-newForm") })
	public String newForm() {
		RevisionPetition odlObjection;
		if (logger.isDebugEnabled())
			logger.debug("Entered into newForm");
		final BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByIndexNumAndParcelID(propertyId, null);
		if (basicProperty.getProperty().getStatus().equals(PropertyTaxConstants.STATUS_ISACTIVE)
				&& !wfType.equalsIgnoreCase(PropertyTaxConstants.PROPERTY_MODIFY_REASON_GENERAL_REVISION_PETITION)) {
			addActionError(getText("revPetition.demandActive"));
			return COMMON_FORM;
		} else if (!basicProperty.getProperty().getStatus().equals(PropertyTaxConstants.STATUS_ISACTIVE)
				&& wfType.equalsIgnoreCase(PropertyTaxConstants.PROPERTY_MODIFY_REASON_GENERAL_REVISION_PETITION)) {
			addActionError(getText("error.msg.demandInactive"));
			return COMMON_FORM;
		}
		odlObjection = revisionPetitionService.getExistingObjections(basicProperty);
		if (APPLICATION_TYPE_GRP.equalsIgnoreCase(wfType) && odlObjection != null) {
			isGenerateAck = false;
			addActionError(getText("property.rp.exist"));
			return COMMON_FORM;
		}

		getPropertyView(propertyId);

		if (objection != null && objection.getBasicProperty() != null
				&& objection.getBasicProperty().isUnderWorkflow()) {
			addActionMessage(
					getText("property.state.objected", new String[] { objection.getBasicProperty().getUpicNo() }));
			return STRUTS_RESULT_MESSAGE;
		}
		isMeesevaUser = propService.isMeesevaUser(securityUtils.getCurrentUser());
		if (isMeesevaUser)
			if (getMeesevaApplicationNumber() == null) {
				addActionMessage(getText("MEESEVA.005"));
				return MEESEVA_ERROR;
			} else
				objection.setMeesevaApplicationNumber(getMeesevaApplicationNumber());
		setFloorDetails(objection.getBasicProperty().getProperty());
		if (StringUtils.isBlank(applicationSource) && !citizenPortalUser
				&& propService.isEmployee(securityUtils.getCurrentUser())
				&& !propertyTaxCommonUtils.isEligibleInitiator(securityUtils.getCurrentUser().getId())) {
			addActionError(getText("initiator.noteligible"));
			return COMMON_FORM;
		}
		return NEW;
	}

	/**
	 * Create revision petition.
	 *
	 * @return
	 */
	@Action(value = "/revPetition")
	public String create() {
		if (logger.isDebugEnabled())
			logger.debug("ObjectionAction | Create | start " + objection);
		validateInitiator();
		if (hasActionErrors()) {
			getPropertyView(objection.getBasicProperty().getUpicNo());
			return NEW;
		}

		if (objection != null && objection.getBasicProperty() != null && objection.getState() == null
				&& objection.getBasicProperty().isUnderWorkflow()) {
			addActionMessage(
					getText("property.state.objected", new String[] { objection.getBasicProperty().getUpicNo() }));
			return STRUTS_RESULT_MESSAGE;
		}

		if (objection.getRecievedOn() == null) {
			addActionMessage(getText("mandatory.fieldvalue.receivedOn"));
			return NEW;
		}
		if (StringUtils.isBlank(objection.getSource()))
			objection.setSource(propertyTaxCommonUtils.setSourceOfProperty(securityUtils.getCurrentUser(),
					SOURCE_ONLINE.equalsIgnoreCase(applicationSource)));
		if (SOURCE_ONLINE.equalsIgnoreCase(applicationSource) && ApplicationThreadLocals.getUserId() == null)
			ApplicationThreadLocals.setUserId(securityUtils.getCurrentUser().getId());
		isMeesevaUser = propService.isMeesevaUser(securityUtils.getCurrentUser());
		if (objection.getObjectionNumber() == null)
			if (isMeesevaUser && getMeesevaApplicationNumber() != null)
				objection.setObjectionNumber(objection.getMeesevaApplicationNumber());
			else
				objection.setObjectionNumber(applicationNumberGenerator.generate());
		objection.getBasicProperty()
				.setStatus(propertyStatusDAO.getPropertyStatusByCode(PropertyTaxConstants.STATUS_OBJECTED_STR));
		objection.getBasicProperty().setUnderWorkflow(Boolean.TRUE);
		objection.setType(getWfType());
		propertyId = objection.getBasicProperty().getUpicNo();
		updateStateAndStatus(objection);
		if (NATURE_OF_WORK_RP.equalsIgnoreCase(wfType))
			addActionMessage(getText("objection.success") + objection.getObjectionNumber());
		else
			addActionMessage(getText("objection.grp.success") + objection.getObjectionNumber());
		revisionPetitionService.applyAuditing(objection.getState());
		if (!isMeesevaUser)
			revisionPetitionService.createRevisionPetition(objection);
		else {
			final HashMap<String, String> meesevaParams = new HashMap<>();
			meesevaParams.put("ADMISSIONFEE", "0");
			meesevaParams.put("APPLICATIONNUMBER", objection.getMeesevaApplicationNumber());
			objection.setApplicationNo(objection.getMeesevaApplicationNumber());
			revisionPetitionService.createRevisionPetition(objection, meesevaParams);
		}
		if (citizenPortalUser)
			if (NATURE_OF_WORK_RP.equalsIgnoreCase(wfType))
				propService.pushRevisionPetitionPortalMessage(objection, APPLICATION_TYPE_REVISION_PETITION);
			else
				propService.pushRevisionPetitionPortalMessage(objection, APPLICATION_TYPE_GRP);
		currentStatus = REVISION_PETITION_CREATED;
		sendEmailandSms(objection, REVISION_PETITION_CREATED);
		applicationNumber =  objection.getObjectionNumber();
		if (logger.isDebugEnabled())
			logger.debug("ObjectionAction | Create | End " + objection);
		return isMeesevaUser ? MEESEVA_RESULT_ACK : STRUTS_RESULT_MESSAGE;
	}

	private void validateInitiator() {
		Assignment assignment = null;
		if (isMeesevaUser || !loggedUserIsEmployee || citizenPortalUser) {
			assignment = propService.isCscOperator(securityUtils.getCurrentUser())
					? propService.getAssignmentByDeptDesigElecWard(objection.getBasicProperty()) : null;
			if (assignment == null)
				assignment = propService.getUserPositionByZone(objection.getBasicProperty(), false);
		} else if (objection.getId() == null)
			assignment = propertyTaxCommonUtils.getWorkflowInitiatorAssignment(securityUtils.getCurrentUser().getId());
		else if (Arrays.asList(RP_WF_REGISTERED, GRP_WF_REGISTERED, RP_NEW, GRP_NEW)
				.contains(objection.getState().getValue()))
			if (objection.getState().getInitiatorPosition() == null)
				assignment = revisionPetitionService.getWorkflowInitiator(objection);
			else {
				final List<Assignment> assignments = assignmentService
						.getAssignmentsForPosition(objection.getState().getInitiatorPosition().getId());
				if (!assignments.isEmpty())
					assignment = assignments.get(0);
			}
		if (assignment == null)
			addActionError(getText(NOTEXISTS_POSITION));
	}

	/**
	 * Method to add hearing date
	 *
	 * @return
	 */
	@Action(value = "/revPetition-addHearingDate")
	public String addHearingDate() {
		if (logger.isDebugEnabled())
			logger.debug("ObjectionAction | addHearingDate | start " + objection);
		validateInitiator();
		if (hasActionErrors()) {
			getPropertyView(objection.getBasicProperty().getUpicNo());
			return "view";
		}
		InputStream hearingNoticePdf = null;
		ReportOutput reportOutput = new ReportOutput();
		final String noticeNo = propertyTaxNumberGenerator
				.generateNoticeNumber(NOTICE_TYPE_REVISIONPETITION_HEARINGNOTICE);
		updateStateAndStatus(objection);
		reportOutput = revisionPetitionService.createHearingNoticeReport(reportOutput, objection, noticeNo);
		if (reportOutput != null && reportOutput.getReportOutputData() != null)
			hearingNoticePdf = new ByteArrayInputStream(reportOutput.getReportOutputData());
		if (hearingNoticePdf != null)
			noticeService.saveNotice(objection.getObjectionNumber(), noticeNo,
					NOTICE_TYPE_REVISIONPETITION_HEARINGNOTICE, objection.getBasicProperty(), hearingNoticePdf);// Save
																												// Notice
		revisionPetitionService.updateRevisionPetition(objection);
		sendEmailandSms(objection, REVISION_PETITION_HEARINGNOTICEGENERATED);
		if (logger.isDebugEnabled())
			logger.debug("ObjectionAction | addHearingDate | End " + objection);
		return STRUTS_RESULT_MESSAGE;
	}

	/**
	 * Generate Hearing notice
	 *
	 * @return
	 */
	@ValidationErrorPage(value = "view")
	@Action(value = "/revPetition-generateHearingNotice")
	public String generateHearingNotice() {
		objection.setGenerateSpecialNotice(Boolean.TRUE);
		updateStateAndStatus(objection);
		final PropertyImpl refNewProperty = propService.creteNewPropertyForObjectionWorkflow(
				objection.getBasicProperty(), objection.getObjectionNumber(), objection.getRecievedOn(),
				objection.getCreatedBy(), null, wfType);
		propertyImplService.getSession().flush();
		objection.setProperty(refNewProperty);
		revisionPetitionService.updateRevisionPetition(objection);
		return STRUTS_RESULT_MESSAGE;

	}

	@ValidationErrorPage(value = "view")
	@Action(value = "/revPetition-printHearingNotice")
	public String printHearingNotice() {
		final ReportOutput reportOutput = new ReportOutput();
		if (objection != null && objection.getObjectionNumber() != null) {
			final PtNotice ptNotice = noticeService.getNoticeByNoticeTypeAndApplicationNumber(
					NOTICE_TYPE_REVISIONPETITION_HEARINGNOTICE, objection.getObjectionNumber());
			final FileStoreMapper fsm = ptNotice.getFileStore();
			final File file = fileStoreService.fetch(fsm, FILESTORE_MODULE_NAME);
			byte[] bFile;
			try {
				bFile = FileUtils.readFileToByteArray(file);
			} catch (final IOException e) {

				throw new ApplicationRuntimeException(HEARING_NOTCIE_EXCEPTION_MESSAGE + e);
			}
			reportOutput.setReportOutputData(bFile);
			reportOutput.setReportFormat(ReportFormat.PDF);
			reportId = reportViewerUtil.addReportToTempCache(reportOutput);
		} else
			addActionMessage(getText("objection.nohearingNotice"));
		return NOTICE;
	}

	@ValidationErrorPage(value = "view")
	@Action(value = "/revPetition-recordHearingDetails")
	public String recordHearingDetails() throws TaxCalculatorExeption {

		if (logger.isDebugEnabled())
			logger.debug("ObjectionAction | recordHearingDetails | start "
					+ objection.getHearings().get(objection.getHearings().size() - 1));
		vaidatePropertyDetails();

		if (hasErrors())
			return "view";

		// set the auto generated hearing number
		if (null == objection.getHearings().get(objection.getHearings().size() - 1).getHearingNumber()) {
			final String hearingNumber = applicationNumberGenerator.generate();
			objection.getHearings().get(objection.getHearings().size() - 1).setHearingNumber(hearingNumber);
			addActionMessage(getText("hearingNum") + " " + hearingNumber);
		}

		updateStateAndStatus(objection);
		modifyBasicProp();
		propertyImplService.merge(objection.getProperty());
		revisionPetitionService.updateRevisionPetition(objection);
		if (logger.isDebugEnabled())
			logger.debug("ObjectionAction | recordHearingDetails | End "
					+ objection.getHearings().get(objection.getHearings().size() - 1));
		return STRUTS_RESULT_MESSAGE;

	}

	/**
	 * @description - allows the user to record the inspection details.
	 * @return String
	 * @throws TaxCalculatorExeption
	 */
	@ValidationErrorPage(value = "view")
	@Action(value = "/revPetition-recordInspectionDetails")
	public String recordInspectionDetails() {
		if (logger.isDebugEnabled())
			logger.debug("ObjectionAction | recordInspectionDetails | start "
					+ objection.getInspections().get(objection.getInspections().size() - 1));
		vaidatePropertyDetails();
		if (hasErrors()) {
			checkIfEligibleForDocEdit();
			return "view";
		}
		try {
			modifyBasicProp();
		} catch (final TaxCalculatorExeption e) {
			addActionError(getText("unitrate.error"));
			return "view";
		}

		final String designation = propService.getDesignationForPositionAndUser(
				objection.getCurrentState().getOwnerPosition().getId(), securityUtils.getCurrentUser().getId());
		if (REVENUE_INSPECTOR_DESGN.equals(designation))
			propService.processAndStoreDocument(objection.getDocuments());
		updateStateAndStatus(objection);
		if (logger.isDebugEnabled())
			logger.debug("ObjectionAction | recordInspectionDetails | End "
					+ objection.getInspections().get(objection.getInspections().size() - 1));
		propertyImplService.merge(objection.getProperty());
		revisionPetitionService.updateRevisionPetition(objection);
		return STRUTS_RESULT_MESSAGE;
	}

	@ValidationErrorPage(value = "view")
	@Action(value = "/revPetition-validateInspectionDetails")
	public String validateInspectionDetails() {
		updateStateAndStatus(objection);
		revisionPetitionService.updateRevisionPetition(objection);
		return STRUTS_RESULT_MESSAGE;
	}

	@ValidationErrorPage(value = "view")
	@Action(value = "/revPetition-rejectInspectionDetails")
	public String rejectInspectionDetails() {
		final List<Assignment> loggedInUserAssignment = assignmentService.getAssignmentByPositionAndUserAsOnDate(
				objection.getCurrentState().getOwnerPosition().getId(), securityUtils.getCurrentUser().getId(),
				new Date());
		final String loggedInUserDesignation = !loggedInUserAssignment.isEmpty()
				? loggedInUserAssignment.get(0).getDesignation().getName() : "";
		final Assignment initiator = propService.getUserOnRejection(objection);
		if (propertyTaxCommonUtils.isRoOrCommissioner(loggedInUserDesignation) && initiator == null) {
			getPropertyView(objection.getBasicProperty().getUpicNo());
			addActionError(getText("reject.error.initiator.inactive", Arrays.asList(REVENUE_INSPECTOR_DESGN)));
			return "view";
		}
		updateStateAndStatus(objection);
		revisionPetitionService.updateRevisionPetition(objection);
		return STRUTS_RESULT_MESSAGE;
	}

	/**
	 * Allows the user to record whether the objection is accepted or rejected.
	 *
	 * @return
	 */
	@ValidationErrorPage(value = "view")
	@Action(value = "/revPetition-recordObjectionOutcome")
	public String recordObjectionOutcome() {
		if (logger.isDebugEnabled())
			logger.debug("ObjectionAction | recordObjectionOutcome | start " + objection);

		if (hasErrors())
			return "view";

		if (WFLOW_ACTION_STEP_APPROVE.equalsIgnoreCase(workFlowAction)) {
			objection.setEgwStatus(egwStatusDAO.getStatusByModuleAndCode(PropertyTaxConstants.OBJECTION_MODULE,
					PropertyTaxConstants.OBJECTION_ACCEPTED));
			objection.getBasicProperty().getProperty().setStatus(STATUS_ISHISTORY);
			objection.getProperty().setStatus(STATUS_ISACTIVE);
			if (NATURE_OF_WORK_RP.equalsIgnoreCase(wfType))
				objection.getBasicProperty().addPropertyStatusValues(propService.createPropStatVal(
						objection.getBasicProperty(), REVISIONPETITION_STATUS_CODE, null, null, null, null, null));
			else
				objection.getBasicProperty().addPropertyStatusValues(propService.createPropStatVal(
						objection.getBasicProperty(), GRP_STATUS_CODE, null, null, null, null, null));
			propService.setWFPropStatValActive(objection.getBasicProperty());
		}

		updateStateAndStatus(objection);
		revisionPetitionService.updateRevisionPetition(objection);
		if (WFLOW_ACTION_STEP_FORWARD.equalsIgnoreCase(workFlowAction))
			addActionMessage(NATURE_OF_WORK_RP.equalsIgnoreCase(wfType) ? getText("objection.forward.success")
					: getText("objection.grp.forward.success"));
		else
			sendEmailandSms(objection, REVISION_PETITION_ENDORESEMENTGENERATED);
		if (WFLOW_ACTION_STEP_APPROVE.equalsIgnoreCase(workFlowAction))
			addActionMessage(NATURE_OF_WORK_RP.equalsIgnoreCase(wfType) ? getText("objection.outcome.success")
					: getText("objection.grp.outcome.success"));
		if (logger.isDebugEnabled())
			logger.debug("ObjectionAction | recordObjectionOutcome | End " + objection);
		return STRUTS_RESULT_MESSAGE;
	}

	/**
	 * @param property
	 * @param basicProperty
	 */
	public void generateSpecialNotice(final PropertyImpl property, final BasicPropertyImpl basicProperty) {
		final Map<String, Object> reportParams = new HashMap<>();
		ReportRequest reportInput;
		PropertyNoticeInfo propertyNotice;
		InputStream specialNoticePdf = null;
		String noticeNo = null;
		String natureOfWork;
		List<Assignment> loggedInUserAssignment;
		String loggedInUserDesignation;
		final PtNotice notice = noticeService
				.getNoticeByNoticeTypeAndApplicationNumber(NATURE_OF_WORK_RP.equalsIgnoreCase(objection.getType())
						? NOTICE_TYPE_RPPROCEEDINGS : NOTICE_TYPE_GRPPROCEEDINGS, objection.getObjectionNumber());
		reportParams.put("userId", ApplicationThreadLocals.getUserId());
		reportParams.put("userSignature", securityUtils.getCurrentUser().getSignature() != null
				? new ByteArrayInputStream(securityUtils.getCurrentUser().getSignature()) : "");
		ReportOutput reportOutput = new ReportOutput();
		if (WFLOW_ACTION_STEP_PRINT_NOTICE.equalsIgnoreCase(actionType) && notice != null) {
			final FileStoreMapper fsm = notice.getFileStore();
			final File file = fileStoreService.fetch(fsm, FILESTORE_MODULE_NAME);
			byte[] bFile;
			try {
				bFile = FileUtils.readFileToByteArray(file);
			} catch (final IOException e) {
				throw new ApplicationRuntimeException("Exception while generating Special Notcie : " + e);
			}
			reportOutput.setReportOutputData(bFile);
			reportOutput.setReportFormat(ReportFormat.PDF);
			reportId = reportViewerUtil.addReportToTempCache(reportOutput);
		} else {
			if (WFLOW_ACTION_STEP_SIGN.equals(actionType) && notice == null)
				noticeNo = propertyTaxNumberGenerator
						.generateNoticeNumber(NATURE_OF_WORK_RP.equalsIgnoreCase(objection.getType())
								? NOTICE_TYPE_RPPROCEEDINGS : NOTICE_TYPE_GRPPROCEEDINGS);
			propertyNotice = new PropertyNoticeInfo(property, noticeNo);
			final String cityGrade = cityService.getCityGrade();
			Boolean isCorporation;
			if (cityGrade != null && cityGrade != ""
					&& cityGrade.equalsIgnoreCase(PropertyTaxConstants.CITY_GRADE_CORPORATION))
				isCorporation = true;
			else
				isCorporation = false;
			reportParams.put("isCorporation", isCorporation);
			reportParams.put("cityName", cityService.getMunicipalityName());
			reportParams.put("logoPath", cityService.getCityLogoURL());
			reportParams.put("mode", "create");
			final User user = securityUtils.getCurrentUser();
                        loggedInUserAssignment = assignmentService.getAssignmentByPositionAndUserAsOnDate(
                                        objection.getCurrentState().getOwnerPosition().getId(), user.getId(), new Date());
                        loggedInUserDesignation = !loggedInUserAssignment.isEmpty()
                                        ? loggedInUserAssignment.get(0).getDesignation().getName() : "";
                        if (COMMISSIONER_DESGN.equalsIgnoreCase(loggedInUserDesignation))
                                reportParams.put("isCommissioner", true);
                        else
                                reportParams.put("isCommissioner", false);
			if (NATURE_OF_WORK_RP.equalsIgnoreCase(objection.getType()))
				natureOfWork = NATURE_REVISION_PETITION;
			else
				natureOfWork = NATURE_GENERAL_REVISION_PETITION;
			reportParams.put("natureOfWork", natureOfWork);
			revisionPetitionService.setNoticeInfo(property, propertyNotice, basicProperty, objection);
			final List<PropertyAckNoticeInfo> floorDetails = getFloorDetailsForNotice(property);
			propertyNotice.setFloorDetailsForNotice(floorDetails);
			reportInput = new ReportRequest(PropertyTaxConstants.REPORT_TEMPLATENAME_RP_SPECIAL_NOTICE, propertyNotice,
					reportParams);
			reportInput.setPrintDialogOnOpenReport(true);
			reportInput.setReportFormat(ReportFormat.PDF);
			reportOutput = reportService.createReport(reportInput);
			if (reportOutput != null && reportOutput.getReportOutputData() != null)
				specialNoticePdf = new ByteArrayInputStream(reportOutput.getReportOutputData());
			if (WFLOW_ACTION_STEP_SIGN.equals(actionType)) {
				if (notice == null) {
					final PtNotice savedNotice = noticeService.saveNotice(objection.getObjectionNumber(),
							noticeNo, NATURE_OF_WORK_RP.equalsIgnoreCase(objection.getType())
									? NOTICE_TYPE_RPPROCEEDINGS : NOTICE_TYPE_GRPPROCEEDINGS,
							objection.getBasicProperty(), specialNoticePdf);
					setFileStoreIds(savedNotice.getFileStore().getFileStoreId());
				} else {
					final PtNotice savedNotice = noticeService.updateNotice(notice, specialNoticePdf);
					setFileStoreIds(savedNotice.getFileStore().getFileStoreId());
				}
				noticeService.getSession().flush();
			} else
				reportId = reportViewerUtil.addReportToTempCache(reportOutput);
		}
	}

	/**
	 * @param property
	 * @param totalTax
	 * @return
	 */
	private List<PropertyAckNoticeInfo> getFloorDetailsForNotice(final PropertyImpl property) {
		final List<PropertyAckNoticeInfo> floorDetailsList = new ArrayList<>();
		final PropertyDetail detail = property.getPropertyDetail();
		PropertyAckNoticeInfo floorInfo;
		for (final Floor floor : detail.getFloorDetails()) {
			floorInfo = new PropertyAckNoticeInfo();
			floorInfo.setBuildingClassification(floor.getStructureClassification().getTypeName());
			floorInfo.setNatureOfUsage(floor.getPropertyUsage().getUsageName());
			floorInfo.setPlinthArea(new BigDecimal(floor.getBuiltUpArea().getArea()));
			floorInfo.setBuildingAge(floor.getDepreciationMaster().getDepreciationName());
			floorInfo.setMonthlyRentalValue(
					floor.getFloorDmdCalc() != null ? floor.getFloorDmdCalc().getMrv() : BigDecimal.ZERO);
			floorInfo.setYearlyRentalValue(
					floor.getFloorDmdCalc() != null ? floor.getFloorDmdCalc().getAlv() : BigDecimal.ZERO);
			floorInfo.setBldngFloorNo(FLOOR_MAP.get(floor.getFloorNo()));
			floorInfo.setTaxPayableForNewRates(BigDecimal.ZERO);

			floorDetailsList.add(floorInfo);
		}
		return floorDetailsList;
	}
	
	@Action(value = "/revPetition-printEnodresementNotice")
	public String printEnodresementNotice() {
		final ReportOutput reportOutput = new ReportOutput();
		if (objection != null && objection.getObjectionNumber() != null) {
			final PtNotice ptNotice = noticeService.getPtNoticeByNoticeNumberAndNoticeType(
					objection.getObjectionNumber()
							.concat(PropertyTaxConstants.NOTICE_TYPE_REVISIONPETITION_ENDORSEMENT_PREFIX),
					PropertyTaxConstants.NOTICE_TYPE_REVISIONPETITION_ENDORSEMENT);
			if (ptNotice != null) {
				final FileStoreMapper fsm = ptNotice.getFileStore();
				final File file = fileStoreService.fetch(fsm, FILESTORE_MODULE_NAME);
				byte[] bFile;
				try {
					bFile = FileUtils.readFileToByteArray(file);
				} catch (final IOException e) {

					throw new ApplicationRuntimeException(HEARING_NOTCIE_EXCEPTION_MESSAGE + e);
				}
				reportOutput.setReportOutputData(bFile);
				reportOutput.setReportFormat(ReportFormat.PDF);
				reportId = reportViewerUtil.addReportToTempCache(reportOutput);
			}
		} else
			addActionMessage(getText("objection.noendoresementNotice"));

		return NOTICE;
	}

    @SuppressWarnings("deprecation")
    @ValidationErrorPage(value = "view")
    @Action(value = "/revPetition-generateEnodresementNotice")
    public String generateEnodresementNotice() {
        ReportOutput reportOutput = new ReportOutput();
        InputStream endoresementPdf = null;
        if (objection.getGenerateSpecialNotice() != null && !objection.getGenerateSpecialNotice()) {
            objection.getBasicProperty()
                    .setStatus(propertyStatusDAO.getPropertyStatusByCode(PropertyTaxConstants.STATUS_CODE_ASSESSED));
            objection.getBasicProperty().setUnderWorkflow(Boolean.FALSE);
            objection.transition().end().withStateValue(PropertyTaxConstants.WFLOW_ACTION_END).withNextAction(null)
                    .withOwner((Position) null)
                    .withOwner(securityUtils.getCurrentUser()).withComments(approverComments);
        } else
            updateStateAndStatus(objection);

        reportOutput = revisionPetitionService.createEndoresement(reportOutput, objection);
        if (reportOutput != null && reportOutput.getReportOutputData() != null)
            endoresementPdf = new ByteArrayInputStream(reportOutput.getReportOutputData());
        noticeService.saveNotice(objection.getObjectionNumber(),
                objection.getObjectionNumber()
                        .concat(PropertyTaxConstants.NOTICE_TYPE_REVISIONPETITION_ENDORSEMENT_PREFIX),
                PropertyTaxConstants.NOTICE_TYPE_REVISIONPETITION_ENDORSEMENT, objection.getBasicProperty(),
                endoresementPdf);
        revisionPetitionService.updateRevisionPetition(objection);
        if (NATURE_OF_WORK_RP.equalsIgnoreCase(objection.getType()))
            addActionMessage(getText("objection.endoresementNotice.success"));
        else
            addActionMessage(getText("objection.grp.endoresementNotice.success"));
        if (logger.isDebugEnabled())
            logger.debug("ObjectionAction | generateEnodresementNotice | End " + objection);

        if (objection != null && objection.getObjectionNumber() != null) {
            final PtNotice ptNotice = noticeService.getPtNoticeByNoticeNumberAndNoticeType(
                    objection.getObjectionNumber()
                            .concat(PropertyTaxConstants.NOTICE_TYPE_REVISIONPETITION_ENDORSEMENT_PREFIX),
                    PropertyTaxConstants.NOTICE_TYPE_REVISIONPETITION_ENDORSEMENT);
            if (ptNotice != null) {
                final FileStoreMapper fsm = ptNotice.getFileStore();
                final File file = fileStoreService.fetch(fsm, FILESTORE_MODULE_NAME);
                byte[] bFile;
                try {
                    bFile = FileUtils.readFileToByteArray(file);
                } catch (final IOException e) {
                    throw new ApplicationRuntimeException(HEARING_NOTCIE_EXCEPTION_MESSAGE + e);
                }
                reportOutput.setReportOutputData(bFile);
                reportOutput.setReportFormat(ReportFormat.PDF);
                reportId = reportViewerUtil.addReportToTempCache(reportOutput);
            }
        }
        return NOTICE;
    }

    @ValidationErrorPage(value = "view")
    @Action(value = "/revPetition-generateSpecialNotice")
    public String generateSpecialNotice() {
        setUlbCode(ApplicationThreadLocals.getCityCode());

        if (objection.getGenerateSpecialNotice() != null && objection.getGenerateSpecialNotice()
                && !PREVIEW.equals(actionType))
            if (WFLOW_ACTION_STEP_PRINT_NOTICE.equals(actionType)) {
                objection.getBasicProperty().setStatus(
                        propertyStatusDAO.getPropertyStatusByCode(PropertyTaxConstants.STATUS_CODE_ASSESSED));
                objection.getBasicProperty().setUnderWorkflow(Boolean.FALSE);

                objection.transition().end().withOwner(securityUtils.getCurrentUser()).withComments(approverComments)
                        .withNextAction(null).withOwner(objection.getCurrentState().getOwnerPosition());
            } else if (!WFLOW_ACTION_STEP_SIGN.equals(actionType))
                updateStateAndStatus(objection);

        generateSpecialNotice(objection.getProperty(), (BasicPropertyImpl) objection.getBasicProperty());

        if (!WFLOW_ACTION_STEP_SIGN.equals(actionType))
            revisionPetitionService.updateRevisionPetition(objection);
        final ReportOutput reportOutput = new ReportOutput();
        if (objection != null && objection.getObjectionNumber() != null) {
            final PtNotice ptNotice = noticeService.getNoticeByNoticeTypeAndApplicationNumber(
                    objection.getObjectionNumber(), NOTICE_TYPE_RPPROCEEDINGS);
            if (ptNotice != null) {
                final FileStoreMapper fsm = ptNotice.getFileStore();
                final File file = fileStoreService.fetch(fsm, FILESTORE_MODULE_NAME);
                byte[] bFile;
                try {
                    bFile = FileUtils.readFileToByteArray(file);
                } catch (final IOException e) {

                    throw new ApplicationRuntimeException(HEARING_NOTCIE_EXCEPTION_MESSAGE + e);
                }
                reportOutput.setReportOutputData(bFile);
                reportOutput.setReportFormat(ReportFormat.PDF);
                if (!WFLOW_ACTION_STEP_SIGN.equals(actionType))
                    reportId = reportViewerUtil.addReportToTempCache(reportOutput);
            }
        }
        if (WFLOW_ACTION_STEP_PRINT_NOTICE.equals(actionType)) {
            if (wfType.equalsIgnoreCase(NATURE_OF_WORK_RP))
                propService.updateIndexes(objection, APPLICATION_TYPE_REVISION_PETITION);
            else
                propService.updateIndexes(objection, APPLICATION_TYPE_GRP);

            if (objection != null && objection.getSource() != null
                    && Source.CITIZENPORTAL.toString().equalsIgnoreCase(objection.getSource()))
                if (NATURE_OF_WORK_RP.equalsIgnoreCase(wfType))
                    propService.updatePortal(objection, APPLICATION_TYPE_REVISION_PETITION);
                else
                    propService.updatePortal(objection, APPLICATION_TYPE_GRP);
        }
        return WFLOW_ACTION_STEP_SIGN.equals(actionType) ? DIGITAL_SIGNATURE_REDIRECTION : NOTICE;
    }

	/**
	 * @param objection
	 * @param applicationType
	 */
	public void sendEmailandSms(final RevisionPetition objection, final String applicationType) {
		for (final PropertyOwnerInfo ownerInfo : objection.getBasicProperty().getPropertyOwnerInfo())
			sendEmailAndSms(objection, ownerInfo.getOwner(), applicationType);
	}

	private void sendEmailAndSms(final RevisionPetition objection, final User user, final String applicationType) {
		if (objection != null) {
			final String mobileNumber = user.getMobileNumber();
			final String emailid = user.getEmailId();
			final String applicantName = user.getName();
			final List<String> args = new ArrayList<>();
			args.add(NATURE_OF_WORK_RP.equalsIgnoreCase(wfType) ? NATURE_REVISION_PETITION
					: NATURE_GENERAL_REVISION_PETITION);
			args.add(applicantName);
			String smsMsg = "";
			String emailSubject = "";
			String emailBody = "";

			if (applicationType.equalsIgnoreCase(REVISION_PETITION_CREATED)) {
				args.add(objection.getObjectionNumber());
				args.add(sMSEmailService.getCityName());
				if (mobileNumber != null)
					smsMsg = getText("msg.revPetitioncreate.sms", args);
				if (emailid != null) {
					emailSubject = getText("msg.revPetitioncreate.email.subject", args);
					emailBody = getText("msg.revPetitioncreate.email", args);
				}
			} else if (applicationType.equalsIgnoreCase(REVISION_PETITION_HEARINGNOTICEGENERATED)) {

				if (objection.getHearings() != null && !objection.getHearings().isEmpty()) {
					args.add(DateUtils.getFormattedDate(objection.getHearings().get(0).getPlannedHearingDt(),
							DATE_FORMAT_DDMMYYY));
					args.add(objection.getHearings().get(0).getHearingVenue());
					args.add(objection.getHearings().get(0).getHearingTime());
					args.add(sMSEmailService.getCityName());
					if (mobileNumber != null)
						smsMsg = getText("msg.revPetitionHearingNotice.sms", args);
					if (emailid != null) {
						emailSubject = getText("msg.revPetitionHearingNotice.email.subject", args);
						emailBody = getText("msg.revPetitionHearingNotice.email", args);
					}
				}
			} else if (applicationType.equalsIgnoreCase(REVISION_PETITION_ENDORESEMENTGENERATED)) {
				args.add(sMSEmailService.getCityName());
				if (mobileNumber != null)
					smsMsg = getText("msg.revPetitionEndoresement.sms", args);
				if (emailid != null) {
					emailSubject = getText("msg.revPetitionHearingNotice.email.subject", args);
					emailBody = getText("msg.revPetitionEndoresement.email", args);
				}
			}
			if (StringUtils.isNotBlank(mobileNumber))
				notificationService.sendSMS(mobileNumber, smsMsg);
			if (StringUtils.isNotBlank(emailid))
				notificationService.sendEmail(emailid, emailSubject, emailBody);
		}
	}

	/**
	 * @param property
	 */
	private void setFloorDetails(final Property property) {
		if (logger.isDebugEnabled())
			logger.debug("Entered into setFloorDetails, Property: " + property);

		final List<Floor> floors = property.getPropertyDetail().getFloorDetails();
		objection.getBasicProperty().getProperty().getPropertyDetail().setFloorDetails(floors);
		if (null != objection.getProperty())
			objection.getProperty().getPropertyDetail().setFloorDetailsProxy(floors);

		int i = 0;
		for (final Floor flr : floors) {
			floorNoStr[i] = FLOOR_MAP.get(flr.getFloorNo());
			i++;
		}
		if (logger.isDebugEnabled())
			logger.debug("Exiting from setFloorDetails: ");
	}

	@Action(value = "/revPetition-view")
	public String view() {
		if (logger.isDebugEnabled())
			logger.debug("ObjectionAction | view | Start");
		getPropertyView(objection.getBasicProperty().getUpicNo());
		isReassignEnabled = reassignmentservice.isReassignEnabled();
                stateAwareId = objection.getId();
		if (objection.getBasicProperty() != null
				&& objection.getBasicProperty().getPropertyID() != null) {
			final PropertyID propertyID = objection.getBasicProperty().getPropertyID();
			northBoundary = propertyID.getNorthBoundary();
			southBoundary = propertyID.getSouthBoundary();
			eastBoundary = propertyID.getEastBoundary();
			westBoundary = propertyID.getWestBoundary();
		}
		populatePropertyTypeCategory();
		populateLayoutAndVltArea();
		checkIfEligibleForDocEdit();
		if (objection != null && objection.getProperty() != null) {
			setReasonForModify(objection.getProperty().getPropertyDetail().getPropertyMutationMaster().getCode());
			if (objection.getProperty().getPropertyDetail().getSitalArea() != null)
				setAreaOfPlot(objection.getProperty().getPropertyDetail().getSitalArea().getArea().toString());

			if (!objection.getProperty().getPropertyDetail().getFloorDetails().isEmpty())
				setFloorDetails(objection.getProperty());
			if (objection.getProperty().getPropertyDetail().getPropertyTypeMaster() != null)
				propTypeObjId = objection.getProperty().getPropertyDetail().getPropertyTypeMaster().getId().toString();
		}

		setOwnerName(objection.getBasicProperty().getProperty());
		setPropertyAddress(objection.getBasicProperty().getAddress());

		propStatVal = propertyStatusValuesDAO.getLatestPropertyStatusValuesByPropertyIdAndreferenceNo(
				objection.getBasicProperty().getUpicNo(), objection.getObjectionNumber());

		if (objection.getState() != null) {
			if (!objection.getState().getHistory().isEmpty())
				setUpWorkFlowHistory(objection.getState().getId());
			historyMap = propService.populateHistory(objection);
		}
		setOwnerName(objection.getBasicProperty().getProperty());
		setPropertyAddress(objection.getBasicProperty().getAddress());
		setWfType(objection.getType());
		if (logger.isDebugEnabled())
			logger.debug("ObjectionAction | view | End");
		return "view";
	}

	private void checkIfEligibleForDocEdit() {
		final String designation = propService.getDesignationForPositionAndUser(
				objection.getCurrentState().getOwnerPosition().getId(), securityUtils.getCurrentUser().getId());
		if (objection.getCurrentState().getValue().endsWith(STATUS_REJECTED)
				|| REVENUE_INSPECTOR_DESGN.equals(designation))
			setAllowEditDocument(Boolean.TRUE);
	}

	public String viewObjectionDetails() {
		if (logger.isDebugEnabled())
			logger.debug("ObjectionAction | viewObjectionDetails | Start");
		objection = revisionPetitionService.find("from Objection where objectionNumber like ?",
				objection.getObjectionNumber());
		setOwnerName(objection.getBasicProperty().getProperty());
		setPropertyAddress(objection.getBasicProperty().getAddress());
		if (logger.isDebugEnabled())
			logger.debug("ObjectionAction | viewObjectionDetails | End");
		return "viewDetails";
	}

	@Action(value = "/revPetition-reject")
	public String rejectRevisionPetition() {

		updateStateAndStatus(objection);
		revisionPetitionService.updateRevisionPetition(objection);
		return STRUTS_RESULT_MESSAGE;
	}

	public String updateRecordObjection() {

		revisionPetitionService.updateRevisionPetition(objection);

		updateStateAndStatus(objection);
		return STRUTS_RESULT_MESSAGE;
	}

	private void updateStateAndStatus(final RevisionPetition revPetition) {
		if (logger.isDebugEnabled())
			logger.debug("ObjectionAction | updateStateAndStatus | Start");
		Position position = null;
		User user;
		User loggedInUser;
		WorkFlowMatrix wfmatrix;
		user = securityUtils.getCurrentUser();
		loggedInUser = user;
		Assignment wfInitiator;
		List<Assignment> loggedInUserAssign;
		String loggedInUserDesignation = "";
		String pendingAction;
		if (objection.getState() != null) {
			loggedInUserAssign = assignmentService.getAssignmentByPositionAndUserAsOnDate(
					objection.getCurrentState().getOwnerPosition().getId(), user.getId(), new Date());
			loggedInUserDesignation = !loggedInUserAssign.isEmpty()
					? loggedInUserAssign.get(0).getDesignation().getName() : null;
		}

		if (loggedInUserDesignation != null && (JUNIOR_ASSISTANT.equals(loggedInUserDesignation)
				|| SENIOR_ASSISTANT.equals(loggedInUserDesignation)))
			loggedInUserDesignation = null;

		if (objection.getId() != null)
			wfInitiator = revisionPetitionService.getWorkflowInitiator(objection);
		else
			wfInitiator = propertyTaxCommonUtils.getWorkflowInitiatorAssignment(user.getId());

		if (approverPositionId != null && approverPositionId != -1)
			position = positionMasterService.getPositionById(approverPositionId);
		if (WFLOW_ACTION_STEP_APPROVE.equalsIgnoreCase(workFlowAction) && loggedInUserDesignation != null
				&& loggedInUserDesignation.endsWith(COMMISSIONER_DESGN))
			pendingAction = new StringBuilder().append(loggedInUserDesignation).append(" ").append("Approval Pending")
					.toString();
		else
			pendingAction = getPendingActions();

		if (null == objection.getState()) {
			if (!citizenPortalUser && loggedUserIsEmployee && !ANONYMOUS_USER.equalsIgnoreCase(user.getName()))
				wfmatrix = revisionPetitionWorkFlowService.getWfMatrix(objection.getStateType(), null, null,
						getAdditionalRule(), null, null, null);
			else
				wfmatrix = revisionPetitionWorkFlowService.getWfMatrix(objection.getStateType(), null, null,
						getAdditionalRule(), "Created", null, null);
		} else if (objection.getCurrentState().getValue().equalsIgnoreCase(PropertyTaxConstants.RP_INSPECTION_COMPLETE)
				|| objection.getCurrentState().getValue()
						.equalsIgnoreCase(PropertyTaxConstants.GRP_INSPECTION_COMPLETE))
			wfmatrix = revisionPetitionWorkFlowService.getWfMatrix(objection.getStateType(), null, null,
					getAdditionalRule(), objection.getCurrentState().getValue(),
					objection.getCurrentState().getNextAction(), null, loggedInUserDesignation);
		else if (!PropertyTaxConstants.OBJECTION_CREATED.equalsIgnoreCase(objection.getEgwStatus().getCode())
				&& loggedInUserDesignation != null && loggedInUserDesignation.endsWith("Commissioner")
				&& (WFLOW_ACTION_STEP_APPROVE.equalsIgnoreCase(workFlowAction)
						|| WFLOW_ACTION_STEP_FORWARD.equalsIgnoreCase(workFlowAction)))
			wfmatrix = revisionPetitionWorkFlowService.getWfMatrix(objection.getStateType(), null, null,
					getAdditionalRule(), objection.getCurrentState().getValue(), pendingAction, null,
					loggedInUserDesignation);
		else
			wfmatrix = revisionPetitionWorkFlowService.getWfMatrix(objection.getStateType(), null, null,
					getAdditionalRule(), objection.getCurrentState().getValue(),
					pendingAction != null ? pendingAction : null, null, null);
		if (revPetition.getState() == null) {
			if (position == null && (approverPositionId == null || approverPositionId != -1)) {
				Assignment assignment;
				if (propService.isCscOperator(user))
					assignment = propService.getMappedAssignmentForCscOperator(objection.getBasicProperty());
				else
					assignment = propService.getUserPositionByZone(objection.getBasicProperty(), false);
				if (assignment != null)
					position = assignment.getPosition();
			}
			updateRevisionPetitionStatus(wfmatrix, objection, PropertyTaxConstants.OBJECTION_CREATED);

			if (position != null)
				user = eisCommonService.getUserForPosition(position.getId(), new Date());

			objection.transition().start().withNextAction(wfmatrix.getPendingActions())
					.withStateValue(wfmatrix.getNextState()).withDateInfo(new DateTime().toDate()).withOwner(position)
					.withSenderName(loggedInUser.getUsername() + "::" + loggedInUser.getName()).withOwner(user)
					.withComments(approverComments).withNextAction(wfmatrix.getNextAction())
					.withInitiator(wfInitiator != null ? wfInitiator.getPosition() : null)
					.withNatureOfTask(NATURE_OF_WORK_RP.equalsIgnoreCase(wfType) ? NATURE_REVISION_PETITION
							: NATURE_GENERAL_REVISION_PETITION);

			if (loggedUserIsEmployee && user != null)
				addActionMessage(getText(OBJECTION_FORWARD,
						new String[] { user.getName().concat("~").concat(position.getName()) }));
			if (wfType.equalsIgnoreCase(NATURE_OF_WORK_RP))
				propService.updateIndexes(objection, APPLICATION_TYPE_REVISION_PETITION);
			else
				propService.updateIndexes(objection, APPLICATION_TYPE_GRP);
			if (Source.CITIZENPORTAL.toString().equalsIgnoreCase(objection.getSource())) {
				final PortalInbox portalInbox = propService.getPortalInbox(objection.getObjectionNumber());
				if (portalInbox != null)
					propService.updatePortal(objection, "RP".equalsIgnoreCase(objection.getType())
							? PropertyTaxConstants.APPLICATION_TYPE_REVISION_PETITION : APPLICATION_TYPE_GRP);
			}

		} else if (workFlowAction != null && !"".equals(workFlowAction)
				&& !WFLOW_ACTION_STEP_SAVE.equalsIgnoreCase(workFlowAction)) {

			if (WFLOW_ACTION_STEP_REJECT.equalsIgnoreCase(workFlowAction)
					|| workFlowAction.equalsIgnoreCase(CANCEL_UNCONSIDERED)) {

				wfmatrix = revisionPetitionWorkFlowService.getPreviousStateFromWfMatrix(objection.getStateType(), null,
						null, getAdditionalRule(), objection.getCurrentState().getValue(),
						objection.getCurrentState().getNextAction());
				if (approverPositionId == null || approverPositionId != -1)
					position = objection.getCurrentState().getOwnerPosition();
			}
			if (PropertyTaxConstants.WFLOW_ACTION_STEP_SIGN.equalsIgnoreCase(workFlowAction)) {
				if (propService.isEmployee(objection.getCreatedBy()))
					position = assignmentService.getPrimaryAssignmentForUser(objection.getCreatedBy().getId())
							.getPosition();
				else if (!objection.getStateHistory().isEmpty())
					position = assignmentService.getPrimaryAssignmentForPositon(
							objection.getStateHistory().get(0).getOwnerPosition().getId()).getPosition();
				else
					position = objection.getState().getOwnerPosition();
			} else if (position == null)
				position = positionMasterService.getPositionByUserId(securityUtils.getCurrentUser().getId());

			if (wfmatrix != null)
				workFlowTransition(objection, workFlowAction, approverComments, wfmatrix, position, loggedInUser);
			// Update elastic search index on each workflow.
			if (wfType.equalsIgnoreCase(NATURE_OF_WORK_RP))
				propService.updateIndexes(objection, APPLICATION_TYPE_REVISION_PETITION);
			else
				propService.updateIndexes(objection, APPLICATION_TYPE_GRP);
			if (Source.CITIZENPORTAL.toString().equalsIgnoreCase(objection.getSource())) {
				final PortalInbox portalInbox = propService.getPortalInbox(objection.getObjectionNumber());
				if (portalInbox != null)
					propService.updatePortal(objection, "RP".equalsIgnoreCase(objection.getType())
							? PropertyTaxConstants.APPLICATION_TYPE_REVISION_PETITION : APPLICATION_TYPE_GRP);
			}

		} else if (workFlowAction != null && !"".equals(workFlowAction)
				&& WFLOW_ACTION_STEP_SAVE.equalsIgnoreCase(workFlowAction))
			addActionMessage(getText("file.save"));
		checkToDisplayAckButton();

	}

	/**
	 * @param objection
	 * @param workFlowAction
	 * @param comments
	 * @param wfmatrix
	 * @param position
	 * @param loggedInUser
	 */
	public void workFlowTransition(final RevisionPetition objection, final String workFlowAction, final String comments,
			final WorkFlowMatrix wfmatrix, Position position, final User loggedInUser) {
		boolean positionFoundInHistory = false;
		Assignment nextAssignment;
		String loggedInUserDesignation;
		String nextAction = null;
		final String nextState = null;
		User user;
		List<Assignment> loggedInUserAssign;
		if (logger.isDebugEnabled())
			logger.debug("revisionpetitionaction ||Starting workFlowTransition method for objection");
		user = securityUtils.getCurrentUser();
		loggedInUserAssign = assignmentService.getAssignmentByPositionAndUserAsOnDate(
				objection.getCurrentState().getOwnerPosition().getId(), user.getId(), new Date());
		loggedInUserDesignation = !loggedInUserAssign.isEmpty() ? loggedInUserAssign.get(0).getDesignation().getName()
				: null;
		final Assignment wfInitiator = revisionPetitionService.getWorkflowInitiator(objection);
		if (WFLOW_ACTION_STEP_FORWARD.equalsIgnoreCase(workFlowAction)
				|| workFlowAction.equalsIgnoreCase("approve objection")
				|| workFlowAction.equalsIgnoreCase(FORWARD_TO_APPROVER)) {

			if (wfmatrix != null && (wfmatrix.getNextStatus() != null
					&& wfmatrix.getNextStatus().equalsIgnoreCase(PropertyTaxConstants.OBJECTION_HEARING_FIXED)
					|| wfmatrix.getCurrentState().equalsIgnoreCase(PropertyTaxConstants.RP_INSPECTIONVERIFIED)
					|| wfmatrix.getCurrentState().equalsIgnoreCase(PropertyTaxConstants.RP_WF_REGISTERED)
					|| objection.getState().getValue().equalsIgnoreCase(PropertyTaxConstants.GRP_WF_REGISTERED))) {
				for (final StateHistory<Position> stateHistoryObj : objection.getState().getHistory()) {
					if (stateHistoryObj.getValue().equalsIgnoreCase(PropertyTaxConstants.RP_CREATED)) {
						position = stateHistoryObj.getOwnerPosition();
						final User sender = eisCommonService.getUserForPosition(position.getId(), new Date());
						if (sender != null)
							addActionMessage(getText(OBJECTION_FORWARD,
									new String[] { sender.getName().concat("~").concat(position.getName()) }));

						positionFoundInHistory = true;
						break;
					}
					if (stateHistoryObj.getValue().equalsIgnoreCase(PropertyTaxConstants.RP_WF_REGISTERED)
							&& !loggedInUserDesignation.endsWith(COMMISSIONER_DESGN)) {
						position = wfInitiator.getPosition();
						addActionMessage(getText(OBJECTION_FORWARD, new String[] { wfInitiator.getEmployee().getName()
								.concat("~").concat(wfInitiator.getPosition().getName()) }));

						// First time when commisioner forwarding record from
						// Ulb operator, then only we need to change status.
						if (objection.getEgwStatus() != null && objection.getEgwStatus().getCode()
								.equalsIgnoreCase(PropertyTaxConstants.OBJECTION_CREATED))
							updateRevisionPetitionStatus(wfmatrix, objection,
									PropertyTaxConstants.OBJECTION_HEARING_FIXED);

						positionFoundInHistory = true;
						break;
					}
				}
				if (!positionFoundInHistory && objection.getState() != null)
					if (objection.getState().getValue().equalsIgnoreCase(PropertyTaxConstants.RP_CREATED)
							|| objection.getState().getValue().equalsIgnoreCase(PropertyTaxConstants.RP_WF_REGISTERED)
							|| objection.getState().getValue().equalsIgnoreCase(PropertyTaxConstants.GRP_CREATED)
							|| objection.getState().getValue()
									.equalsIgnoreCase(PropertyTaxConstants.GRP_WF_REGISTERED)) {
						positionFoundInHistory = true;
						updateRevisionPetitionStatus(wfmatrix, objection, PropertyTaxConstants.OBJECTION_HEARING_FIXED);
						position = objection.getState().getInitiatorPosition() != null
								? objection.getState().getInitiatorPosition() : wfInitiator.getPosition();
						addActionMessage(getText(OBJECTION_FORWARD, new String[] { wfInitiator.getEmployee().getName()
								.concat("~").concat(wfInitiator.getPosition().getName()) }));
					}
			}
			if (approverPositionId != null && approverPositionId != -1
					&& workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_FORWARD)
					&& (loggedInUserDesignation.equalsIgnoreCase(ASSISTANT_COMMISSIONER_DESIGN)
							|| loggedInUserDesignation.equalsIgnoreCase(DEPUTY_COMMISSIONER_DESIGN)
							|| loggedInUserDesignation.equalsIgnoreCase(ADDITIONAL_COMMISSIONER_DESIGN)
							|| loggedInUserDesignation.equalsIgnoreCase(ZONAL_COMMISSIONER_DESIGN)
							|| loggedInUserDesignation.equalsIgnoreCase(PropertyTaxConstants.REVENUE_OFFICER_DESGN)))
				if (objection.getState().getNextAction()
						.equalsIgnoreCase(PropertyTaxConstants.OBJECTION_PRINT_ENDORSEMENT)
						|| objection.getState().getNextAction()
								.equalsIgnoreCase(PropertyTaxConstants.WF_STATE_DIGITAL_SIGNATURE_PENDING))
					nextAction = objection.getState().getNextAction();
				else {
					nextAssignment = assignmentService.getAssignmentsForPosition(approverPositionId, new Date()).get(0);
					final String nextDesignation = nextAssignment.getDesignation().getName();
					position = positionMasterService.getPositionById(approverPositionId);
					final String designation = nextDesignation.split(" ")[0];
					nextAction = nextDesignation.equalsIgnoreCase(COMMISSIONER_DESGN)
							? WF_STATE_COMMISSIONER_APPROVAL_PENDING
							: new StringBuilder().append(designation).append(" ")
									.append(WF_STATE_COMMISSIONER_APPROVAL_PENDING).toString();

				}

			objection.transition().progressWithStateCopy()
					.withStateValue(nextState != null ? nextState : wfmatrix.getNextState()).withOwner(position)
					.withSenderName(loggedInUser.getUsername() + "::" + loggedInUser.getName())
					.withDateInfo(new DateTime().toDate())
					.withNextAction(nextAction != null ? nextAction : wfmatrix.getNextAction())
					.withComments(approverComments);

			if (wfmatrix.getNextAction() != null && wfmatrix.getNextAction().equalsIgnoreCase("END"))
                objection.transition().end().withStateValue(wfmatrix.getNextState())
                        .withOwner(objection.getCurrentState().getOwnerPosition())
                        .withSenderName(loggedInUser.getUsername() + "::" + loggedInUser.getName())
                        .withNextAction(wfmatrix.getNextAction()).withDateInfo(new DateTime().toDate())
                        .withComments(approverComments).withNextAction(null)
                        .withOwner(objection.getCurrentState().getOwnerPosition());

			if (wfmatrix.getNextStatus() != null)
				updateRevisionPetitionStatus(wfmatrix, objection, null);
			if (approverName != null && !approverName.isEmpty() && !approverName.equalsIgnoreCase(CHOOSE))
				addActionMessage(getText(OBJECTION_FORWARD,
						new String[] { approverName.concat("~").concat(position.getName()) }));
			else if (loggedInUser != null && !positionFoundInHistory)
				addActionMessage(getText(OBJECTION_FORWARD,
						new String[] { loggedInUser.getName().concat("~").concat(position.getName()) }));

		} else if (workFlowAction.equalsIgnoreCase(REJECT_INSPECTION_STR)) {
			final List<StateHistory<Position>> stateHistoryList = objection.getStateHistory();
			Assignment wfInit = null;
			for (final StateHistory<Position> stateHistoryObj : stateHistoryList)
				if (stateHistoryObj.getValue().equalsIgnoreCase(PropertyTaxConstants.RP_HEARINGCOMPLETED)
						|| stateHistoryObj.getValue().equalsIgnoreCase(PropertyTaxConstants.GRP_HEARINGCOMPLETED)) {
					position = stateHistoryObj.getOwnerPosition();
					wfInit = propService.getUserOnRejection(objection);
					break;
				}
			if (wfInit != null) {
				objection.setEgwStatus(egwStatusDAO.getStatusByModuleAndCode(PropertyTaxConstants.OBJECTION_MODULE,
						PropertyTaxConstants.OBJECTION_HEARING_COMPLETED));

				if (position != null) {
					objection.transition().progressWithStateCopy().withNextAction(OBJECTION_RECORD_INSPECTIONDETAILS)
							.withStateValue(PropertyTaxConstants.WF_STATE_REJECTED).withOwner(position)
							.withSenderName(loggedInUser.getUsername() + "::" + loggedInUser.getName())
							.withDateInfo(new DateTime().toDate()).withComments(approverComments);
					final String actionMessage = propertyTaxUtil.getApproverUserName(position.getId());
					if (actionMessage != null)
						addActionMessage(getText(OBJECTION_FORWARD, new String[] { actionMessage }));
				}
			} else
				addActionMessage(getText(REJECT_INSPECTION) + objection.getBasicProperty().getUpicNo());

		} else if (workFlowAction.equalsIgnoreCase(REJECT)) {
			final List<StateHistory<Position>> stateHistoryList = objection.getStateHistory();
			for (final StateHistory<Position> stateHistoryObj : stateHistoryList)
				if (stateHistoryObj.getValue().equalsIgnoreCase(objection.getCurrentState().getValue())) {
					position = stateHistoryObj.getOwnerPosition();
					break;
				}
			if (objection.getCurrentState() != null
					&& (objection.getCurrentState().getValue().equalsIgnoreCase(REJECTED) || objection.getCurrentState()
							.getValue().equalsIgnoreCase(PropertyTaxConstants.RP_CREATED))) {
                objection.transition().end().withStateValue(wfmatrix.getNextState()).withOwner(position)
                        .withSenderName(loggedInUser.getUsername() + "::" + loggedInUser.getName())
                        .withNextAction(wfmatrix.getNextAction()).withDateInfo(new DateTime().toDate())
                        .withComments(approverComments).withNextAction(null)
                        .withOwner(objection.getCurrentState().getOwnerPosition());

				updateRevisionPetitionStatus(wfmatrix, objection, REJECTED);

			} else {// ASSUMPTION HERE IS WE ALREADY HAVE PREVIOUS WF MATRIX
				// DATA.
				objection.transition().progressWithStateCopy().withStateValue(wfmatrix.getCurrentState())
						.withOwner(position).withSenderName(loggedInUser.getUsername() + "::" + loggedInUser.getName())
						.withDateInfo(new DateTime().toDate()).withNextAction(wfmatrix.getPendingActions())
						.withComments(approverComments);

				if (workFlowAction.equalsIgnoreCase(REJECT))
					updateRevisionPetitionStatus(wfmatrix, objection, null);
			}

			if (approverName != null && !approverName.isEmpty() && !approverName.equalsIgnoreCase(CHOOSE))
				addActionMessage(getText(OBJECTION_FORWARD,
						new String[] { approverName.concat("~").concat(position.getName()) }));
			else if (loggedInUser != null)
				addActionMessage(getText(OBJECTION_FORWARD,
						new String[] { loggedInUser.getName().concat("~").concat(position.getName()) }));
		} else if (workFlowAction.equalsIgnoreCase(PRINT_ENDORESEMENT)) {
			position = objection.getState().getOwnerPosition();
			objection.transition().progressWithStateCopy().withStateValue(wfmatrix.getCurrentState())
					.withOwner(position).withSenderName(loggedInUser.getUsername() + "::" + loggedInUser.getName())
					.withDateInfo(new DateTime().toDate()).withNextAction(wfmatrix.getNextAction())
					.withComments(approverComments);
			if (logger.isDebugEnabled())
				logger.debug("revisionpetitionaction ||ended  workflow for objection");

		} else if (WFLOW_ACTION_STEP_SIGN.equalsIgnoreCase(workFlowAction))
			objection.transition().progressWithStateCopy().withStateValue(wfmatrix.getCurrentState())
					.withOwner(position).withSenderName(loggedInUser.getUsername() + "::" + loggedInUser.getName())
					.withDateInfo(new DateTime().toDate()).withNextAction(wfmatrix.getNextAction())
					.withComments(approverComments);
		else if (workFlowAction.equalsIgnoreCase(APPROVE)) {
			position = objection.getState().getOwnerPosition();
			objection.transition().progressWithStateCopy().withStateValue(wfmatrix.getNextState()).withOwner(position)
					.withSenderName(loggedInUser.getUsername() + "::" + loggedInUser.getName())
					.withDateInfo(new DateTime().toDate()).withNextAction(wfmatrix.getNextAction())
					.withComments(approverComments);
		}
		revisionPetitionService.applyAuditing(objection.getState());
	}

	private void updateRevisionPetitionStatus(final WorkFlowMatrix wfmatrix, final RevisionPetition objection,
			final String status) {

		EgwStatus egwStatus = null;
		if (status != null && !"".equals(status))
			egwStatus = egwStatusDAO.getStatusByModuleAndCode(PropertyTaxConstants.OBJECTION_MODULE, status);

		else if (wfmatrix != null && wfmatrix.getNextStatus() != null && objection != null)
			egwStatus = egwStatusDAO.getStatusByModuleAndCode(PropertyTaxConstants.OBJECTION_MODULE,
					wfmatrix.getNextStatus());
		if (egwStatus != null)
			objection.setEgwStatus(egwStatus);

	}

	private void modifyBasicProp() throws TaxCalculatorExeption {
		Date propCompletionDate = null;
		final Long oldPropTypeId = objection.getProperty().getPropertyDetail().getPropertyTypeMaster().getId();

		if (propTypeObjId != null && !propTypeObjId.trim().isEmpty() && !"-1".equals(propTypeObjId)) {
			final PropertyTypeMaster propTypeMstr = (PropertyTypeMaster) getPersistenceService()
					.find("from PropertyTypeMaster ptm where ptm.id = ?", Long.valueOf(propTypeObjId));

			if (!propTypeMstr.getCode().equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND))
				propCompletionDate = propService
						.getLowestDtOfCompFloorWise(objection.getProperty().getPropertyDetail().getFloorDetailsProxy());
			else
				propCompletionDate = objection.getProperty().getPropertyDetail().getDateOfCompletion();

		}
		if (propCompletionDate != null) {
			objection.getBasicProperty().setPropOccupationDate(propCompletionDate);
			objection.getProperty().setEffectiveDate(propCompletionDate);
		}

		propService.createProperty(objection.getProperty(), getAreaOfPlot() != null ? getAreaOfPlot() : "",
				reasonForModify, propTypeObjId != null ? propTypeObjId : null,
				objection.getProperty().getPropertyDetail().getPropertyUsage() != null
						? objection.getProperty().getPropertyDetail().getPropertyUsage().getId().toString() : null,
				objection.getProperty().getPropertyDetail().getPropertyOccupation() != null
						? objection.getProperty().getPropertyDetail().getPropertyOccupation().getId().toString() : null,
				STATUS_WORKFLOW, objection.getProperty().getDocNumber(), "",
				objection.getProperty().getPropertyDetail().getFloorType() != null
						? objection.getProperty().getPropertyDetail().getFloorType().getId() : null,
				objection.getProperty().getPropertyDetail().getRoofType() != null
						? objection.getProperty().getPropertyDetail().getRoofType().getId() : null,
				objection.getProperty().getPropertyDetail().getWallType() != null
						? objection.getProperty().getPropertyDetail().getWallType().getId() : null,
				objection.getProperty().getPropertyDetail().getWoodType() != null
						? objection.getProperty().getPropertyDetail().getWoodType().getId() : null,
				taxExemptedReason,
				objection.getProperty().getPropertyDetail().getPropertyDepartment() != null
						? objection.getProperty().getPropertyDetail().getPropertyDepartment().getId() : null,
				getVacantLandPlotAreaId(), getLayoutApprovalAuthorityId(), Boolean.FALSE);

		updatePropertyID(objection.getBasicProperty());
		final PropertyTypeMaster propTypeMstr = (PropertyTypeMaster) getPersistenceService()
				.find("from PropertyTypeMaster ptm where ptm.code = ?", OWNERSHIP_TYPE_VAC_LAND);

		if (oldPropTypeId == propTypeMstr.getId() && Long.parseLong(propTypeObjId) != propTypeMstr.getId()
				|| oldPropTypeId != propTypeMstr.getId() && Long.parseLong(propTypeObjId) == propTypeMstr.getId())
			if (propTypeMstr != null && StringUtils.equals(propTypeMstr.getId().toString(), propTypeObjId))
				propService.changePropertyDetail(objection.getProperty(), new VacantProperty(), 0);
			else
				propService.changePropertyDetail(objection.getProperty(), new BuiltUpProperty(),
						objection.getProperty().getPropertyDetail().getFloorDetails().size());
		propService.modifyDemand(objection.getProperty(), (PropertyImpl) objection.getBasicProperty().getProperty());

		if (objection.getProperty().getPropertyDetail().getLayoutApprovalAuthority() != null && "No Approval"
				.equals(objection.getProperty().getPropertyDetail().getLayoutApprovalAuthority().getName())) {
			objection.getProperty().getPropertyDetail().setLayoutPermitNo(null);
			objection.getProperty().getPropertyDetail().setLayoutPermitDate(null);
		}

	}

	/**
	 * @param propertyId
	 */
	private void getPropertyView(final String propertyId) {
		if (logger.isDebugEnabled())
			logger.debug("ObjectionAction | getPropertyView | Start");
		viewPropertyAction.setPersistenceService(persistenceService);
		viewPropertyAction.setBasicPropertyDAO(basicPropertyDAO);
		viewPropertyAction.setPtDemandDAO(ptDemandDAO);
		viewPropertyAction.setPropertyId(propertyId);
		viewPropertyAction.setPropertyTaxUtil(propertyTaxUtil);
		viewPropertyAction.setPropertyTaxCommonUtils(propertyTaxCommonUtils);
		viewPropertyAction.setUserService(userService);
		viewPropertyAction.setPropService(propService);
		viewPropertyAction.setSession(getSession());
		viewPropertyAction.viewForm();
		objection.setBasicProperty(viewPropertyAction.getBasicProperty());
		viewMap = viewPropertyAction.getViewMap();
		// Show revised tax details for RO and commissioner, along with existing
		// taxes
		if (objection.getEgwStatus() != null && (objection.getEgwStatus().getCode()
				.equalsIgnoreCase(PropertyTaxConstants.OBJECTION_INSPECTION_COMPLETED)
				|| objection.getEgwStatus().getCode()
						.equalsIgnoreCase(PropertyTaxConstants.OBJECTION_INSPECTION_VERIFY)))
			wfPropTaxDetailsMap = propertyTaxCommonUtils
					.getTaxDetailsForWorkflowProperty(viewPropertyAction.getBasicProperty());
		if (logger.isDebugEnabled())
			logger.debug("ObjectionAction | getPropertyView | End");
	}

	public void vaidatePropertyDetails() {
		if (reasonForModify == null || reasonForModify.equals("-1"))
			addActionError(getText("mandatory.rsnForMdfy"));
		validateProperty(objection.getProperty(), getAreaOfPlot() != null ? getAreaOfPlot() : "", null, eastBoundary,
				westBoundary, southBoundary, northBoundary, propTypeObjId != null ? propTypeObjId : null, ownerName,
				ownerName,
				objection.getProperty().getPropertyDetail().getFloorType() != null
						? objection.getProperty().getPropertyDetail().getFloorType().getId() : null,
				objection.getProperty().getPropertyDetail().getRoofType() != null
						? objection.getProperty().getPropertyDetail().getRoofType().getId() : null,
				objection.getProperty().getPropertyDetail().getWallType() != null
						? objection.getProperty().getPropertyDetail().getWallType().getId() : null,
				objection.getProperty().getPropertyDetail().getWoodType() != null
						? objection.getProperty().getPropertyDetail().getWoodType().getId() : null,
				null,
				objection.getProperty().getPropertyDetail().getDateOfCompletion() != null
						? objection.getProperty().getPropertyDetail().getDateOfCompletion() : null,
				getVacantLandPlotAreaId(), getLayoutApprovalAuthorityId(), null);

	}

	private void populatePropertyTypeCategory() {
		PropertyTypeMaster propTypeMstr = null;
		if (propTypeObjId != null && !propTypeObjId.trim().isEmpty() && !propTypeObjId.equals("-1"))
			propTypeMstr = (PropertyTypeMaster) getPersistenceService()
					.find("from PropertyTypeMaster ptm where ptm.id = ?", Long.valueOf(propTypeObjId));
		else if (objection != null && objection.getProperty() != null
				&& objection.getProperty().getPropertyDetail() != null
				&& objection.getProperty().getPropertyDetail().getPropertyTypeMaster() != null
				&& objection.getProperty().getPropertyDetail().getPropertyTypeMaster().getId() != -1)
			propTypeMstr = objection.getProperty().getPropertyDetail().getPropertyTypeMaster();
		else if (objection.getBasicProperty() != null)
			propTypeMstr = objection.getBasicProperty().getProperty().getPropertyDetail().getPropertyTypeMaster();

		if (propTypeMstr != null) {
			if (propTypeMstr.getCode().equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND))
				setPropTypeCategoryMap(VAC_LAND_PROPERTY_TYPE_CATEGORY);
			else
				setPropTypeCategoryMap(NON_VAC_LAND_PROPERTY_TYPE_CATEGORY);
		} else
			setPropTypeCategoryMap(Collections.emptyMap());
	}

	@Action(value = "/revPetition-printAck")
	public String printAck() {
		String serviceType;
		String applicationType;
		ReportOutput reportOutput;
		if (NATURE_OF_WORK_RP.equalsIgnoreCase(wfType)) {
			serviceType = REVISION_PETITION;
			applicationType = REVISION_PETETION;
		} else {
			serviceType = GENERAL_REVISION_PETITION;
			applicationType = GENERAL_REVISION_PETETION;
		}
		if (ANONYMOUS_USER.equalsIgnoreCase(securityUtils.getCurrentUser().getName()))
			setApplicationSource(SOURCE_ONLINE.toLowerCase());
		reportOutput = propertyTaxUtil.generateCitizenCharterAcknowledgement(propertyId, applicationType, serviceType, applicationNumber);
		reportId = reportViewerUtil.addReportToTempCache(reportOutput);
		return NOTICE;
	}

	public void populateLayoutAndVltArea() {
		if (objection.getProperty() != null) {
			if (objection.getProperty().getPropertyDetail().getVacantLandPlotArea() != null)
				vacantLandPlotAreaId = objection.getProperty().getPropertyDetail().getVacantLandPlotArea().getId();
			if (objection.getProperty().getPropertyDetail().getLayoutApprovalAuthority() != null)
				layoutApprovalAuthorityId = objection.getProperty().getPropertyDetail().getLayoutApprovalAuthority()
						.getId();
		}
	}

	public List<Floor> getFloorDetails() {
		return new ArrayList<>(objection.getBasicProperty().getProperty().getPropertyDetail().getFloorDetails());
	}

	public Map<String, String> getPropTypeCategoryMap() {
		return propTypeCategoryMap;
	}

	public void setPropTypeCategoryMap(final Map<String, String> propTypeCategoryMap) {
		this.propTypeCategoryMap = propTypeCategoryMap;
	}

	public RevisionPetition getObjection() {
		return objection;
	}

	public void setObjection(final RevisionPetition objection) {
		this.objection = objection;
	}

	public String getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(final String propertyId) {
		this.propertyId = propertyId;
	}

	public Map<String, Object> getViewMap() {
		return viewMap;
	}

	public RevisionPetitionService getRevisionPetitionService() {
		return revisionPetitionService;
	}

	private void updatePropertyID(final BasicProperty basicProperty) {
		final PropertyID boundaryDetails = basicProperty.getPropertyID();
		if (boundaryDetails != null) {
			boundaryDetails.setEastBoundary(getEastBoundary());
			boundaryDetails.setWestBoundary(getWestBoundary());
			boundaryDetails.setNorthBoundary(getNorthBoundary());
			boundaryDetails.setSouthBoundary(getSouthBoundary());
		}
	}

	public void setRevisionPetitionService(final RevisionPetitionService revisionPetitionService) {
		this.revisionPetitionService = revisionPetitionService;
	}

	public PersistenceService<Property, Long> getPropertyImplService() {
		return propertyImplService;
	}

	public void setPropertyImplService(final PersistenceService<Property, Long> propertyImplService) {
		this.propertyImplService = propertyImplService;
	}

	public void setViewPropertyAction(final ViewPropertyAction viewPropertyAction) {
		this.viewPropertyAction = viewPropertyAction;
	}

	public void setObjectionWorkflowService(final WorkflowService<RevisionPetition> objectionWorkflowService) {
		this.objectionWorkflowService = objectionWorkflowService;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(final Property property) {
		if (property != null)
			ownerName = property.getBasicProperty().getFullOwnerName();
	}

	public String getPropertyAddress() {
		return propertyAddress;
	}

	public void setPropertyAddress(final Address address) {
		if (address != null)
			propertyAddress = address.toString();
	}

	public void setPropService(final PropertyService propService) {
		this.propService = propService;
	}

	public boolean getIsShowAckMessage() {
		return isShowAckMessage;
	}

	public void setIsShowAckMessage(final boolean isShowAckMessage) {
		this.isShowAckMessage = isShowAckMessage;
	}

	public void setBasicPropertyDAO(final BasicPropertyDAO basicPropertyDAO) {
		this.basicPropertyDAO = basicPropertyDAO;
	}

	public void setUserService(final UserService userService) {
		this.userService = userService;
	}

	public PropertyStatusValues getPropStatVal() {
		return propStatVal;
	}

	public void setPropStatVal(final PropertyStatusValues propStatVal) {
		this.propStatVal = propStatVal;
	}

	public String getReasonForModify() {
		return reasonForModify;
	}

	public void setReasonForModify(final String reasonForModify) {
		this.reasonForModify = reasonForModify;
	}

	public SortedMap<Integer, String> getFloorNoMap() {
		return floorNoMap;
	}

	public void setFloorNoMap(final SortedMap<Integer, String> floorNoMap) {
		this.floorNoMap = floorNoMap;
	}

	public String getPropTypeObjId() {
		return propTypeObjId;
	}

	public void setPropTypeObjId(final String propTypeObjId) {
		this.propTypeObjId = propTypeObjId;
	}

	public String getReportId() {
		return reportId;
	}

	public Map<String, String> getDeviationPercentageMap() {
		return deviationPercentageMap;
	}

	public void setDeviationPercentageMap(final Map<String, String> deviationPercentageMap) {
		this.deviationPercentageMap = deviationPercentageMap;
	}

	public String[] getFloorNoStr() {
		return floorNoStr;
	}

	public void setFloorNoStr(final String[] floorNoStr) {
		this.floorNoStr = floorNoStr;
	}

	public List<DocumentType> getDocumentTypes() {
		return documentTypes;
	}

	public void setDocumentTypes(final List<DocumentType> documentTypes) {
		this.documentTypes = documentTypes;
	}

	public Map<String, String> getHearingTimingMap() {
		return hearingTimingMap;
	}

	public void setHearingTimingMap(final Map<String, String> hearingTimingMap) {
		this.hearingTimingMap = hearingTimingMap;
	}

	public String getNorthBoundary() {
		return northBoundary;
	}

	public void setNorthBoundary(final String northBoundary) {
		this.northBoundary = northBoundary;
	}

	public String getSouthBoundary() {
		return southBoundary;
	}

	public void setSouthBoundary(final String southBoundary) {
		this.southBoundary = southBoundary;
	}

	public String getEastBoundary() {
		return eastBoundary;
	}

	public void setEastBoundary(final String eastBoundary) {
		this.eastBoundary = eastBoundary;
	}

	public String getWestBoundary() {
		return westBoundary;
	}

	public void setWestBoundary(final String westBoundary) {
		this.westBoundary = westBoundary;
	}

	public SimpleWorkflowService<RevisionPetition> getRevisionPetitionWorkFlowService() {
		return revisionPetitionWorkFlowService;
	}

	public void setRevisionPetitionWorkFlowService(
			final SimpleWorkflowService<RevisionPetition> revisionPetitionWorkFlowService) {
		this.revisionPetitionWorkFlowService = revisionPetitionWorkFlowService;
	}

	public PropertyTaxNumberGenerator getPropertyTaxNumberGenerator() {
		return propertyTaxNumberGenerator;
	}

	public void setPropertyTaxNumberGenerator(final PropertyTaxNumberGenerator propertyTaxNumberGenerator) {
		this.propertyTaxNumberGenerator = propertyTaxNumberGenerator;
	}

	@Override
	public SMSEmailService getsMSEmailService() {
		return sMSEmailService;
	}

	@Override
	public void setsMSEmailService(final SMSEmailService sMSEmailService) {
		this.sMSEmailService = sMSEmailService;
	}

	public Long getTaxExemptedReason() {
		return taxExemptedReason;
	}

	public void setTaxExemptedReason(final Long taxExemptedReason) {
		this.taxExemptedReason = taxExemptedReason;
	}

	public Boolean getLoggedUserIsEmployee() {
		return loggedUserIsEmployee;
	}

	public void setLoggedUserIsEmployee(final Boolean loggedUserIsEmployee) {
		this.loggedUserIsEmployee = loggedUserIsEmployee;
	}

	public String getAreaOfPlot() {
		return areaOfPlot;
	}

	public void setAreaOfPlot(final String areaOfPlot) {
		this.areaOfPlot = areaOfPlot;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(final String actionType) {
		this.actionType = actionType;
	}

	public String getFileStoreIds() {
		return fileStoreIds;
	}

	public void setFileStoreIds(final String fileStoreIds) {
		this.fileStoreIds = fileStoreIds;
	}

	public String getUlbCode() {
		return ulbCode;
	}

	public void setUlbCode(final String ulbCode) {
		this.ulbCode = ulbCode;
	}

	@Override
	public List<HashMap<String, Object>> getHistoryMap() {
		return historyMap;
	}

	@Override
	public void setHistoryMap(final List<HashMap<String, Object>> historyMap) {
		this.historyMap = historyMap;
	}

	public Map<String, Object> getWfPropTaxDetailsMap() {
		return wfPropTaxDetailsMap;
	}

	public boolean isDigitalSignEnabled() {
		return digitalSignEnabled;
	}

	public void setDigitalSignEnabled(final boolean digitalSignEnabled) {
		this.digitalSignEnabled = digitalSignEnabled;
	}

	public String getMeesevaApplicationNumber() {
		return meesevaApplicationNumber;
	}

	public void setMeesevaApplicationNumber(final String meesevaApplicationNumber) {
		this.meesevaApplicationNumber = meesevaApplicationNumber;
	}

	public String getWfType() {
		return wfType;
	}

	public void setWfType(final String wfType) {
		this.wfType = wfType;
	}

	@Override
	public String getApplicationType() {
		return applicationType;
	}

	@Override
	public void setApplicationType(final String applicationType) {
		this.applicationType = applicationType;
	}

	@Override
	public String getAdditionalRule() {
		String addittionalRule;
		if (PROPERTY_MODIFY_REASON_GENERAL_REVISION_PETITION.equals(wfType))
			addittionalRule = GENERAL_REVISION_PETITION;
		else
			addittionalRule = REVISION_PETITION;
		return addittionalRule;
	}

	@Override
	public String getCurrentDesignation() {
		return objection.getId() != null
				&& !(objection.getCurrentState().getValue().endsWith(STATUS_REJECTED)
						|| objection.getCurrentState().getValue().endsWith(WFLOW_ACTION_NEW)
						|| objection.getCurrentState().getValue()
								.endsWith(PropertyTaxConstants.GRP_RP_HEARING_DATE_FIXED))
										? propService.getDesignationForPositionAndUser(
												objection.getCurrentState().getOwnerPosition().getId(),
												securityUtils.getCurrentUser().getId())
										: null;
	}

	public String getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(final String currentStatus) {
		this.currentStatus = currentStatus;
	}

	@Override
	public String getPendingActions() {
		if (objection != null && objection.getId() != null) {
			if (PropertyTaxConstants.RP_INSPECTIONVERIFIED.equalsIgnoreCase(objection.getCurrentState().getValue())
					|| PropertyTaxConstants.GRP_INSPECTIONVERIFIED
							.equalsIgnoreCase(objection.getCurrentState().getValue())
					|| objection.getCurrentState().getValue().endsWith("Forwarded")
					|| objection.getCurrentState().getValue().endsWith("Approved"))
				return objection.getCurrentState().getNextAction();
			else
				return null;
		} else
			return null;

	}

	public boolean isAllowEditDocument() {
		return allowEditDocument;
	}

	public void setAllowEditDocument(final boolean allowEditDocument) {
		this.allowEditDocument = allowEditDocument;
	}

	public Boolean getShowAckBtn() {
		return showAckBtn;
	}

	public void setShowAckBtn(final Boolean showAckBtn) {
		this.showAckBtn = showAckBtn;
	}

	public List<VacantLandPlotArea> getVacantLandPlotAreaList() {
		return vacantLandPlotAreaList;
	}

	public void setVacantLandPlotAreaList(final List<VacantLandPlotArea> vacantLandPlotAreaList) {
		this.vacantLandPlotAreaList = vacantLandPlotAreaList;
	}

	public List<LayoutApprovalAuthority> getLayoutApprovalAuthorityList() {
		return layoutApprovalAuthorityList;
	}

	public void setLayoutApprovalAuthorityList(final List<LayoutApprovalAuthority> layoutApprovalAuthorityList) {
		this.layoutApprovalAuthorityList = layoutApprovalAuthorityList;
	}

	public Long getVacantLandPlotAreaId() {
		return vacantLandPlotAreaId;
	}

	public void setVacantLandPlotAreaId(final Long vacantLandPlotAreaId) {
		this.vacantLandPlotAreaId = vacantLandPlotAreaId;
	}

	public Long getLayoutApprovalAuthorityId() {
		return layoutApprovalAuthorityId;
	}

	public void setLayoutApprovalAuthorityId(final Long layoutApprovalAuthorityId) {
		this.layoutApprovalAuthorityId = layoutApprovalAuthorityId;
	}

	public String getApplicationSource() {
		return applicationSource;
	}

	public void setApplicationSource(final String applicationSource) {
		this.applicationSource = applicationSource;
	}

	public boolean isGenerateAck() {
		return isGenerateAck;
	}

	public void setGenerateAck(final boolean isGenerateAck) {
		this.isGenerateAck = isGenerateAck;
	}

	public boolean isCitizenPortalUser() {
		return citizenPortalUser;
	}

	public void setCitizenPortalUser(final boolean citizenPortalUser) {
		this.citizenPortalUser = citizenPortalUser;
	}

	private void checkToDisplayAckButton() {
		if (getModel().getId() == null)
			showAckBtn = Boolean.TRUE;
	}
}