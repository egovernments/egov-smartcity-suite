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

import static org.egov.ptis.constants.PropertyTaxConstants.ANONYMOUS_USER;
import static org.egov.ptis.constants.PropertyTaxConstants.APPEALPETITION_CODE;
import static org.egov.ptis.constants.PropertyTaxConstants.APPEAL_NEW;
import static org.egov.ptis.constants.PropertyTaxConstants.APPEAL_PETITION;
import static org.egov.ptis.constants.PropertyTaxConstants.APPEAL_WF_REGISTERED;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_APPEAL_PETITION;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_GRP;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_REVISION_PETITION;
import static org.egov.ptis.constants.PropertyTaxConstants.ASSISTANT_DESGN;
import static org.egov.ptis.constants.PropertyTaxConstants.COMMISSIONER_DESGN;
import static org.egov.ptis.constants.PropertyTaxConstants.DATE_FORMAT_DDMMYYY;
import static org.egov.ptis.constants.PropertyTaxConstants.DEVIATION_PERCENTAGE;
import static org.egov.ptis.constants.PropertyTaxConstants.FILESTORE_MODULE_NAME;
import static org.egov.ptis.constants.PropertyTaxConstants.FLOOR_MAP;
import static org.egov.ptis.constants.PropertyTaxConstants.GENERAL_REVISION_PETITION;
import static org.egov.ptis.constants.PropertyTaxConstants.GRP_NEW;
import static org.egov.ptis.constants.PropertyTaxConstants.GRP_STATUS_CODE;
import static org.egov.ptis.constants.PropertyTaxConstants.GRP_WF_REGISTERED;
import static org.egov.ptis.constants.PropertyTaxConstants.HEARING_TIMINGS;
import static org.egov.ptis.constants.PropertyTaxConstants.JUNIOR_ASSISTANT;
import static org.egov.ptis.constants.PropertyTaxConstants.NATURE_APPEALPETITION;
import static org.egov.ptis.constants.PropertyTaxConstants.NATURE_GENERAL_REVISION_PETITION;
import static org.egov.ptis.constants.PropertyTaxConstants.NATURE_OF_WORK_RP;
import static org.egov.ptis.constants.PropertyTaxConstants.NATURE_REVISION_PETITION;
import static org.egov.ptis.constants.PropertyTaxConstants.NON_VAC_LAND_PROPERTY_TYPE_CATEGORY;
import static org.egov.ptis.constants.PropertyTaxConstants.NOTICE_TYPE_APPEALPETITION_HEARINGNOTICE;
import static org.egov.ptis.constants.PropertyTaxConstants.NOTICE_TYPE_APPEALPROCEEDINGS;
import static org.egov.ptis.constants.PropertyTaxConstants.NOTICE_TYPE_REVISIONPETITION_HEARINGNOTICE;
import static org.egov.ptis.constants.PropertyTaxConstants.NOTICE_TYPE_RPPROCEEDINGS;
import static org.egov.ptis.constants.PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_MODIFY_REASON_ADD_OR_ALTER;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_MODIFY_REASON_GENERAL_REVISION_PETITION;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_MODIFY_REASON_REVISION_PETITION;
import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_INSPECTOR_DESGN;
import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_OFFICER_DESGN;
import static org.egov.ptis.constants.PropertyTaxConstants.REVISIONPETITION_STATUS_CODE;
import static org.egov.ptis.constants.PropertyTaxConstants.REVISION_PETITION;
import static org.egov.ptis.constants.PropertyTaxConstants.RP_NEW;
import static org.egov.ptis.constants.PropertyTaxConstants.RP_WF_REGISTERED;
import static org.egov.ptis.constants.PropertyTaxConstants.SENIOR_ASSISTANT;
import static org.egov.ptis.constants.PropertyTaxConstants.SOURCE_ONLINE;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_ISACTIVE;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_ISHISTORY;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_REJECTED;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_WORKFLOW;
import static org.egov.ptis.constants.PropertyTaxConstants.VAC_LAND_PROPERTY_TYPE_CATEGORY;
import static org.egov.ptis.constants.PropertyTaxConstants.WARDSECRETARY_TRANSACTIONID_CODE;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_NEW;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_APPROVE;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_FORWARD;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_PRINT_NOTICE;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_SIGN;
import static org.egov.ptis.constants.PropertyTaxConstants.ZONE;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_APPEALPETITION;
import static org.egov.ptis.constants.PropertyTaxConstants.NATURE_OF_WORK_GRP;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_REJECT;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_REJECT_TO_CANCEL;
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
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.ResultPath;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.Area;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.commons.entity.Source;
import org.egov.eis.entity.Assignment;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.integration.event.model.enums.ApplicationStatus;
import org.egov.infra.integration.event.model.enums.TransactionStatus;
import org.egov.infra.integration.service.ThirdPartyService;
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
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.infra.workflow.service.WorkflowService;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.commons.Position;
import org.egov.ptis.actions.common.PropertyTaxBaseAction;
import org.egov.ptis.actions.view.ViewPropertyAction;
import org.egov.ptis.bean.PropertyNoticeInfo;
import org.egov.ptis.client.util.PropertyTaxNumberGenerator;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.dao.property.PropertyStatusDAO;
import org.egov.ptis.domain.dao.property.PropertyStatusValuesDAO;
import org.egov.ptis.domain.entity.document.DocumentTypeDetails;
import org.egov.ptis.domain.entity.enums.TransactionType;
import org.egov.ptis.domain.entity.objection.Petition;
import org.egov.ptis.domain.entity.property.Apartment;
import org.egov.ptis.domain.entity.property.AppealPetitionReasons;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.BasicPropertyImpl;
import org.egov.ptis.domain.entity.property.BuiltUpProperty;
import org.egov.ptis.domain.entity.property.Document;
import org.egov.ptis.domain.entity.property.DocumentType;
import org.egov.ptis.domain.entity.property.Floor;
import org.egov.ptis.domain.entity.property.FloorType;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyDetail;
import org.egov.ptis.domain.entity.property.PropertyID;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyMutationMaster;
import org.egov.ptis.domain.entity.property.PropertyOccupation;
import org.egov.ptis.domain.entity.property.PropertyOwnerInfo;
import org.egov.ptis.domain.entity.property.PropertyStatusValues;
import org.egov.ptis.domain.entity.property.PropertyTypeMaster;
import org.egov.ptis.domain.entity.property.RoofType;
import org.egov.ptis.domain.entity.property.StructureClassification;
import org.egov.ptis.domain.entity.property.VacantProperty;
import org.egov.ptis.domain.entity.property.WallType;
import org.egov.ptis.domain.entity.property.WoodType;
import org.egov.ptis.domain.entity.property.vacantland.LayoutApprovalAuthority;
import org.egov.ptis.domain.entity.property.vacantland.VacantLandPlotArea;
import org.egov.ptis.domain.repository.AppealPetitionReason.AppealPetitionReasonRepository;
import org.egov.ptis.domain.repository.master.vacantland.LayoutApprovalAuthorityRepository;
import org.egov.ptis.domain.repository.master.vacantland.VacantLandPlotAreaRepository;
import org.egov.ptis.domain.service.notice.NoticeService;
import org.egov.ptis.domain.service.property.PropertyPersistenceService;
import org.egov.ptis.domain.service.property.PropertyService;
import org.egov.ptis.domain.service.property.PropertyThirdPartyService;
import org.egov.ptis.domain.service.property.SMSEmailService;
import org.egov.ptis.domain.service.reassign.ReassignService;
import org.egov.ptis.domain.service.revisionPetition.RevisionPetitionService;
import org.egov.ptis.domain.service.voucher.DemandVoucherService;
import org.egov.ptis.event.EventPublisher;
import org.egov.ptis.exceptions.TaxCalculatorExeption;
import org.egov.ptis.notice.PtNotice;
import org.egov.ptis.report.bean.PropertyAckNoticeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

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
    private static final String REJECT_ERROR_INITIATOR_INACTIVE = "reject.error.initiator.inactive";
    private static final Logger logger = Logger.getLogger(RevisionPetitionAction.class);
    private static final String NOTEXISTS_POSITION = "notexists.position";
    private static final String HEARING_NOTCIE_EXCEPTION_MESSAGE = "Exception while generating Hearing Notcie : ";
    private static final String GENERAL_REVISION_PETETION = "GENERAL_REVISION_PETETION";
    private static final String REVISION_PETETION = "REVISION_PETETION";
    protected static final String DIGITAL_SIGNATURE_REDIRECTION = "digitalSignatureRedirection";
    private static final long serialVersionUID = 1L;
    protected static final String COMMON_FORM = "commonForm";
    public static final String STRUTS_RESULT_MESSAGE = "message";
    private static final String REVISION_PETITION_CREATED = "CREATED";
    private static final String REVISION_PETITION_HEARINGNOTICEGENERATED = "HEARINGNOTICEGENERATED";
    private static final String REVISION_PETITION_ENDORESEMENTGENERATED = "ENDORESEMTNTGENERATED";
    private static final String PREVIEW = "Preview";
    public static final String NOTICE = "notice";
    public static final String MEESEVA_ERROR = "error";
    public static final String OBJECTION_FORWARD = "objection.forward";
    public static final String REJECT_INSPECTION = "objection.inspection.rejection";
    public static final String APPLICATION_SOURCE = "applicationSource";

    private ViewPropertyAction viewPropertyAction = new ViewPropertyAction();
    private Petition objection = new Petition();
    private String propertyId;
    private transient Map<String, Object> viewMap;
    private transient RevisionPetitionService revisionPetitionService;
    protected transient WorkflowService<Petition> objectionWorkflowService;
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
    private Long zoneId;
    private Map<String, String> propTypeCategoryMap;
    private String reportId;
    private Long taxExemptedReason;
    private String currentStatus;
    private List<VacantLandPlotArea> vacantLandPlotAreaList = new ArrayList<>();
    private List<LayoutApprovalAuthority> layoutApprovalAuthorityList = new ArrayList<>();
    private Long vacantLandPlotAreaId;
    private Long layoutApprovalAuthorityId;
    private boolean citizenPortalUser;
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
    private boolean isGenerateAck = true;
    private transient PropertyTaxNumberGenerator propertyTaxNumberGenerator;
    private boolean isShowAckMessage;
    private Boolean superStructure = Boolean.FALSE;
    private transient List<DocumentType> assessmentDocumentTypesRP = new ArrayList<>();
    private transient List<String> assessmentDocumentNames;
    private transient DocumentTypeDetails documentTypeDetails = new DocumentTypeDetails();
    private boolean editOwnerDetails = false;

    @Autowired
    private transient PropertyStatusValuesDAO propertyStatusValuesDAO;
    @Autowired
    private transient ReportService reportService;
    @Autowired
    private transient NoticeService noticeService;
    @Autowired
    private transient BasicPropertyDAO basicPropertyDAO;
    @Autowired
    @Qualifier("workflowService")
    protected transient SimpleWorkflowService<Petition> revisionPetitionWorkFlowService;
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
    private transient ApplicationNumberGenerator applicationNumberGenerator;
    @Autowired
    private transient NotificationService notificationService;
    @Autowired
    private transient ReportViewerUtil reportViewerUtil;
    @Autowired
    private transient ReassignService reassignmentservice;
    @Autowired
    private transient VacantLandPlotAreaRepository vacantLandPlotAreaRepository;
    @Autowired
    private transient LayoutApprovalAuthorityRepository layoutApprovalAuthorityRepository;
    @Autowired
    private transient CityService cityService;
    @Autowired
    transient PropertyPersistenceService basicPropertyService;
    @PersistenceContext
    private transient EntityManager entityManager;
    @Autowired
    private BoundaryService boundaryService;
    @Autowired
    private DemandVoucherService demandVoucherService;
    @Autowired
    private EventPublisher eventPublisher;
    @Autowired
    private transient PropertyThirdPartyService propertyThirdPartyService;
    @Autowired
    private ThirdPartyService thirdPartyService;
    @Autowired
    private transient AppealPetitionReasonRepository appealPetitionReasonRepository;

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
    public Petition getModel() {

        return objection;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void prepare() {
        User user;
        // to merge the new values from jsp with existing
        if (objection.getId() != null)
            objection = revisionPetitionService.findById(objection.getId(), false);
        else if (parameters.get("objectionId") != null)
            objection = revisionPetitionService.findById(Long.valueOf(parameters.get("objectionId")[0]), false);
        if (objection != null && objection.getId() == null) {
            objection.setRecievedOn(new Date());
            user = securityUtils.getCurrentUser();
            if (user != null)
                objection.setRecievedBy(user.getName());
        }
        if (null != objection && null != objection.getState()) {
            historyMap = propService.populateHistory(objection);
            ownersName = objection.getBasicProperty().getFullOwnerName();
            applicationNumber = objection.getObjectionNumber();
            assessmentNumber = objection.getBasicProperty().getUpicNo();
            endorsementNotices = propertyTaxCommonUtils.getEndorsementNotices(applicationNumber);
            endorsementRequired = propertyTaxCommonUtils.getEndorsementGenerate(securityUtils.getCurrentUser().getId(),
                    objection.getCurrentState());
            if (PROPERTY_MODIFY_REASON_GENERAL_REVISION_PETITION.equals(objection.getType()))
                transactionType = GENERAL_REVISION_PETITION;
            else if (WFLOW_ACTION_APPEALPETITION.equals(objection.getType()))
                transactionType = APPEALPETITION_CODE;
            else
                transactionType = REVISION_PETITION;
            setSuperStructure(objection.getBasicProperty().getProperty().getPropertyDetail().isStructure());
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
        final List<String> structureList = getPersistenceService()
                .findAllBy("from StructureClassification where isActive = true order by typeName ");
        final List<PropertyOccupation> propOccList = getPersistenceService().findAllBy("from PropertyOccupation");
        final List<String> ageFacList = getPersistenceService().findAllBy("from DepreciationMaster");
        setFloorNoMap(FLOOR_MAP);
        addDropdownData("floorType", getPersistenceService().findAllBy("from FloorType order by name"));
        addDropdownData("roofType", getPersistenceService().findAllBy("from RoofType order by name"));
        final List<String> apartmentsList = getPersistenceService().findAllBy("from Apartment order by name");
        final List<Boundary> zones = boundaryService
                .getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(ZONE, REVENUE_HIERARCHY_TYPE);
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
        addDropdownData("zones", zones);
        addDropdownData("taxExemptionReasonList", taxExemptionReasonList);
        populatePropertyTypeCategory();
        setDeviationPercentageMap(DEVIATION_PERCENTAGE);
        setHearingTimingMap(HEARING_TIMINGS);
        digitalSignEnabled = propertyTaxCommonUtils.isDigitalSignatureEnabled();
        setVacantLandPlotAreaList(vacantLandPlotAreaRepository.findAll());
        setLayoutApprovalAuthorityList(layoutApprovalAuthorityRepository.findAll());
        addDropdownData("vacantLandPlotAreaList", vacantLandPlotAreaList);
        addDropdownData("layoutApprovalAuthorityList", layoutApprovalAuthorityList);
        assessmentDocumentTypesRP = propService.getDocumentTypesForTransactionType(TransactionType.CREATE_ASMT_DOC);
        setAssessmentDocumentNames(PropertyTaxConstants.ASSESSMENT_DOCUMENT_NAMES_RP);
        addDropdownData("assessmentDocumentNameList", assessmentDocumentNames);
        addDropdownData("appealReasonList", appealPetitionReasonRepository.findAll());
    }

    /**
     * Method to create new revision petition.
     *
     * @return NEW
     */
    @SkipValidation
    @Actions({ @Action(value = "/revPetition-newForm"), @Action(value = "/genRevPetition-newForm"),
            @Action(value = "/appealpetition-newform") })
    public String newForm() {
        Petition odlObjection;
        final BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(propertyId);
        if (basicProperty.getProperty().getStatus().equals(PropertyTaxConstants.STATUS_ISACTIVE)
                && (!wfType.equalsIgnoreCase(PropertyTaxConstants.PROPERTY_MODIFY_REASON_GENERAL_REVISION_PETITION)
                        && (!wfType.equalsIgnoreCase(PropertyTaxConstants.WFLOW_ACTION_APPEALPETITION)))) {
            addActionError(getText("revPetition.demandActive"));
            return COMMON_FORM;
        } else if (!basicProperty.getProperty().getStatus().equals(PropertyTaxConstants.STATUS_ISACTIVE)
                && (wfType.equalsIgnoreCase(PropertyTaxConstants.PROPERTY_MODIFY_REASON_GENERAL_REVISION_PETITION)
                        || wfType.equalsIgnoreCase(PropertyTaxConstants.WFLOW_ACTION_APPEALPETITION))) {
            addActionError(getText("error.msg.demandInactive"));
            return COMMON_FORM;
        }
        odlObjection = revisionPetitionService.getExistingObjections(basicProperty);

        if((NATURE_APPEALPETITION.equalsIgnoreCase(wfType))){
        if ((odlObjection == null)) {
            isGenerateAck = false;
            addActionError(getText("cannot.apply.appeal.petition"));
            return COMMON_FORM;
        }else if(!odlObjection.getType().equals(NATURE_OF_WORK_RP))
        {
            isGenerateAck = false;
            addActionError(getText("cannot.apply.appeal.petition"));
            return COMMON_FORM;
        }
         final BigDecimal totalDue = propService.getTotalPropertyTaxDue(basicProperty);
         if (totalDue.compareTo(BigDecimal.ZERO) > 0) {
             isGenerateAck = false;
             addActionError(getText("tax.dues.error"));
             return COMMON_FORM;
         }
        }
        getPropertyView(propertyId);
        if (!thirdPartyService.isValidWardSecretaryRequest(wsPortalRequest)) {
            addActionMessage(getText("WS.002"));
            return MEESEVA_ERROR;
        }
        if (objection != null && objection.getBasicProperty() != null
                && objection.getBasicProperty().isUnderWorkflow()) {
            addActionMessage(
                    getText("property.state.objected", new String[] { objection.getBasicProperty().getUpicNo() }));
            return STRUTS_RESULT_MESSAGE;
        }
        isMeesevaUser = propService.isMeesevaUser(securityUtils.getCurrentUser());
        if (isMeesevaUser) {
            if (getMeesevaApplicationNumber() == null) {
                addActionMessage(getText("MEESEVA.005"));
                return MEESEVA_ERROR;
            } else
                objection.setMeesevaApplicationNumber(getMeesevaApplicationNumber());
        } else if (thirdPartyService.isWardSecretaryRequest(wsPortalRequest)) {
            final HttpServletRequest request = ServletActionContext.getRequest();

            if (ThirdPartyService.validateWardSecretaryRequest(
                    request.getParameter(WARDSECRETARY_TRANSACTIONID_CODE), request.getParameter(APPLICATION_SOURCE))) {
                addActionMessage(getText("WS.001"));
                return COMMON_FORM;
            } else if (Source.WARDSECRETARY.toString().equalsIgnoreCase(request.getParameter(APPLICATION_SOURCE))) {
                transactionId = request.getParameter(WARDSECRETARY_TRANSACTIONID_CODE);
                applicationSource = request.getParameter(APPLICATION_SOURCE);
            }

        }
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
        Map<String, String> wsDetails = new HashMap<>();
        validateInitiator();
        final BasicProperty baicProperty = objection.getBasicProperty();
        if (hasActionErrors()) {
            getPropertyView(baicProperty.getUpicNo());
            return NEW;
        }
        if (objection != null && baicProperty != null && objection.getState() == null
                && baicProperty.isUnderWorkflow()) {
            addActionMessage(
                    getText("property.state.objected", new String[] { baicProperty.getUpicNo() }));
            return STRUTS_RESULT_MESSAGE;
        }
        if (objection.getRecievedOn() == null && getWfType() != null
                && !WFLOW_ACTION_APPEALPETITION.equalsIgnoreCase(objection.getType())) {

            addActionMessage(getText("mandatory.fieldvalue.receivedOn"));
            return NEW;

        }
        if (StringUtils.isBlank(objection.getSource()))
            objection.setSource(propertyTaxCommonUtils.setSourceOfProperty(securityUtils.getCurrentUser(),
                    SOURCE_ONLINE.equalsIgnoreCase(applicationSource)));
        if (SOURCE_ONLINE.equalsIgnoreCase(applicationSource) && ApplicationThreadLocals.getUserId() == null)
            ApplicationThreadLocals.setUserId(securityUtils.getCurrentUser().getId());

        if (thirdPartyService.isWardSecretaryRequest(wsPortalRequest)
                && ThirdPartyService.validateWardSecretaryRequest(transactionId, applicationSource)) {
            addActionError(getText("WS.001"));
            return NEW;
        }

        try {
            isMeesevaUser = propService.isMeesevaUser(securityUtils.getCurrentUser());
            if (objection.getObjectionNumber() == null)
                if (isMeesevaUser && getMeesevaApplicationNumber() != null)
                    objection.setObjectionNumber(objection.getMeesevaApplicationNumber());
                else
                    objection.setObjectionNumber(applicationNumberGenerator.generate());
            baicProperty
                    .setStatus(propertyStatusDAO.getPropertyStatusByCode(PropertyTaxConstants.STATUS_OBJECTED_STR));
            baicProperty.setUnderWorkflow(Boolean.TRUE);
            objection.setType(getWfType());
            propertyId = baicProperty.getUpicNo();
            if(WFLOW_ACTION_APPEALPETITION.equalsIgnoreCase(objection.getType()) && objection.getReasons()!=null)
                objection.setAppealReasons(getAppealReasonDetails(objection));
            addAllActionMessages(revisionPetitionService.updateStateAndStatus(objection, approverPositionId, workFlowAction,
                    approverComments, approverName,wsPortalRequest));
            checkToDisplayAckButton();
            if (NATURE_OF_WORK_RP.equalsIgnoreCase(wfType))
                addActionMessage(getText("objection.success") + objection.getObjectionNumber());
            else if (WFLOW_ACTION_APPEALPETITION.equalsIgnoreCase(wfType)) {
                addActionMessage(getText("appeal.petition.success") + objection.getObjectionNumber());
            } else
                addActionMessage(getText("objection.grp.success") + objection.getObjectionNumber());
            revisionPetitionService.applyAuditing(objection.getState());
            if (thirdPartyService.isWardSecretaryRequest(wsPortalRequest)) {
                revisionPetitionService.createRevisionPetition(objection);
                wsDetails = revisionPetitionService.getViewURLAndMsgForWS(objection, wfType);
            } else if (isMeesevaUser) {
                final HashMap<String, String> meesevaParams = new HashMap<>();
                meesevaParams.put("ADMISSIONFEE", "0");
                meesevaParams.put("APPLICATIONNUMBER", objection.getMeesevaApplicationNumber());
                objection.setApplicationNo(objection.getMeesevaApplicationNumber());
                revisionPetitionService.createRevisionPetition(objection, meesevaParams);
            } else {
                revisionPetitionService.createRevisionPetition(objection);
            }
            revisionPetitionService.updateIndexAndPushToPortalInbox(objection);
            if (citizenPortalUser)
                if (NATURE_OF_WORK_RP.equalsIgnoreCase(wfType))
                    propService.pushRevisionPetitionPortalMessage(objection, APPLICATION_TYPE_REVISION_PETITION);
                else if (WFLOW_ACTION_APPEALPETITION.equalsIgnoreCase(wfType))
                    propService.pushRevisionPetitionPortalMessage(objection, APPLICATION_TYPE_APPEAL_PETITION);
                else
                    propService.pushRevisionPetitionPortalMessage(objection, APPLICATION_TYPE_GRP);
            currentStatus = REVISION_PETITION_CREATED;
            sendEmailandSms(objection, REVISION_PETITION_CREATED);
            applicationNumber = objection.getObjectionNumber();
            if (thirdPartyService.isWardSecretaryRequest(wsPortalRequest))
                eventPublisher.publishWSEvent(transactionId, TransactionStatus.SUCCESS,
                        objection.getObjectionNumber(), ApplicationStatus.INPROGRESS, wsDetails.get("viewURL"),
                        wsDetails.get("succeessMsg"));
        } catch (Exception ex) {
            logger.error("Error in creating petition application", ex);
            if (thirdPartyService.isWardSecretaryRequest(wsPortalRequest))
                eventPublisher.publishWSEvent(transactionId, TransactionStatus.FAILED,
                        objection.getObjectionNumber(), null, null, wsDetails.get("failureMsg"));
            clearMessages();
            showAckBtn = Boolean.FALSE;
            addActionMessage(getText("petition.app.error"));
        }
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
        else if (Arrays.asList(RP_WF_REGISTERED, GRP_WF_REGISTERED, RP_NEW, GRP_NEW,APPEAL_WF_REGISTERED,APPEAL_NEW)
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
        validateInitiator();
        if (hasActionErrors()) {
            getPropertyView(objection.getBasicProperty().getUpicNo());
            return "view";
        }
        InputStream hearingNoticePdf = null;
        ReportOutput reportOutput = new ReportOutput();
        final String noticeNo = propertyTaxNumberGenerator
                .generateNoticeNumber(objection.getType().equalsIgnoreCase(WFLOW_ACTION_APPEALPETITION)
                        ? NOTICE_TYPE_APPEALPETITION_HEARINGNOTICE : NOTICE_TYPE_REVISIONPETITION_HEARINGNOTICE);
        addAllActionMessages(revisionPetitionService.updateStateAndStatus(objection, approverPositionId, workFlowAction,
                approverComments, approverName,wsPortalRequest));
        reportOutput = revisionPetitionService.createHearingNoticeReport(reportOutput, objection, noticeNo);
        if (reportOutput != null && reportOutput.getReportOutputData() != null)
            hearingNoticePdf = new ByteArrayInputStream(reportOutput.getReportOutputData());
        if (hearingNoticePdf != null)
            noticeService.saveNotice(objection.getObjectionNumber(), noticeNo,
                    objection.getType().equalsIgnoreCase(WFLOW_ACTION_APPEALPETITION) ? NOTICE_TYPE_APPEALPETITION_HEARINGNOTICE
                            : NOTICE_TYPE_REVISIONPETITION_HEARINGNOTICE,
                    objection.getBasicProperty(), hearingNoticePdf);
        revisionPetitionService.updateRevisionPetition(objection);
        revisionPetitionService.updateIndexAndPushToPortalInbox(objection);
        sendEmailandSms(objection, REVISION_PETITION_HEARINGNOTICEGENERATED);
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
        addAllActionMessages(revisionPetitionService.updateStateAndStatus(objection, approverPositionId, workFlowAction,
                approverComments, approverName,wsPortalRequest));
        final PropertyImpl refNewProperty = propService.creteNewPropertyForObjectionWorkflow(
                objection.getBasicProperty(), objection.getObjectionNumber(), objection.getRecievedOn(),
                objection.getCreatedBy(), null, wfType);
        propertyImplService.getSession().flush();
        objection.setProperty(refNewProperty);
        revisionPetitionService.updateRevisionPetition(objection);
        revisionPetitionService.updateIndexAndPushToPortalInbox(objection);
        return STRUTS_RESULT_MESSAGE;

    }

    @ValidationErrorPage(value = "view")
    @Action(value = "/revPetition-printHearingNotice")
    public String printHearingNotice() {
        final ReportOutput reportOutput = new ReportOutput();
        if (objection != null && objection.getObjectionNumber() != null) {
            final PtNotice ptNotice = noticeService
                    .getNoticeByNoticeTypeAndApplicationNumber(objection.getType().equalsIgnoreCase(WFLOW_ACTION_APPEALPETITION)
                            ? NOTICE_TYPE_APPEALPETITION_HEARINGNOTICE : NOTICE_TYPE_REVISIONPETITION_HEARINGNOTICE,
                            objection.getObjectionNumber());
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
        vaidatePropertyDetails();
        if (hasErrors())
            return "view";
        // set the auto generated hearing number
        if (null == objection.getHearings().get(objection.getHearings().size() - 1).getHearingNumber()) {
            final String hearingNumber = applicationNumberGenerator.generate();
            objection.getHearings().get(objection.getHearings().size() - 1).setHearingNumber(hearingNumber);
            addActionMessage(getText("hearingNum") + " " + hearingNumber);
        }

        addAllActionMessages(revisionPetitionService.updateStateAndStatus(objection, approverPositionId, workFlowAction,
                approverComments, approverName,wsPortalRequest));
        modifyBasicProp();
        propertyImplService.merge(objection.getProperty());
        revisionPetitionService.updateRevisionPetition(objection);
        revisionPetitionService.updateIndexAndPushToPortalInbox(objection);
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
        final BasicProperty basicProperty = objection.getBasicProperty();
        final PropertyImpl property = objection.getProperty();
        vaidatePropertyDetails();
        if (superStructureRP(objection) && isEditOwnerDetails()) {
            validateOwnerDetails(property);
            validateDocumentDetails(getDocumentTypeDetails());
            basicPropertyService.createOwners(property, basicProperty,
                    basicProperty.getPrimaryOwner().getAddress().get(0));
        }
        final String designation = propService.getDesignationForPositionAndUser(
                objection.getCurrentState().getOwnerPosition().getId(), securityUtils.getCurrentUser().getId());
        if (hasErrors()) {
            checkIfEligibleForDocEdit();
            if (REVENUE_INSPECTOR_DESGN.equals(designation) && WFLOW_ACTION_APPEALPETITION.equalsIgnoreCase(objection.getType())) {
                documentTypes.addAll(propService.getDocumentTypesForTransactionType(TransactionType.APPEALPETITION));
                objection.getDocuments().add(new Document());
            }
            return "view";
        }
        try {
            modifyBasicProp();
        } catch (final TaxCalculatorExeption e) {
            addActionError(getText("unitrate.error"));
            if (REVENUE_INSPECTOR_DESGN.equals(designation) && WFLOW_ACTION_APPEALPETITION.equalsIgnoreCase(objection.getType())) {
                documentTypes.addAll(propService.getDocumentTypesForTransactionType(TransactionType.APPEALPETITION));
                objection.getDocuments().add(new Document());
            }
            return "view";
        }
        if (revisionPetitionService.validateDemand(objection)) {
            if (PROPERTY_MODIFY_REASON_GENERAL_REVISION_PETITION.equals(objection.getType()))
                addActionError(getText("grp.tax.increase.msg"));
            else
                addActionError(getText("rp.tax.increase.msg"));
            return "view";
        }
        if (REVENUE_INSPECTOR_DESGN.equals(designation)) {
            propService.processAndStoreDocument(objection.getDocuments());
            if ((getDocumentTypeDetails() != null && isEditOwnerDetails())) {
                propService.processAndStoreDocument(property.getAssessmentDocuments());
                propService.saveDocumentTypeDetails(basicProperty, getDocumentTypeDetails());
            } else {
                if (!property.getAssessmentDocuments().isEmpty())
                    property.getAssessmentDocuments().clear();
            }
        }
        if (assignmentService.getAssignmentsForPosition(
                objection.getStateHistory().get(0).getOwnerPosition().getId(), new Date()).isEmpty()) {
            addActionError(getText(REJECT_ERROR_INITIATOR_INACTIVE, Arrays.asList(ASSISTANT_DESGN)));
            return "view";
        }
        addAllActionMessages(revisionPetitionService.updateStateAndStatus(objection, approverPositionId, workFlowAction,
                approverComments, approverName,wsPortalRequest));
        if (superStructureRP(objection) && isEditOwnerDetails())
            basicPropertyService.update(basicProperty);
        propertyImplService.merge(property);
        revisionPetitionService.updateRevisionPetition(objection);
        revisionPetitionService.updateIndexAndPushToPortalInbox(objection);
        return STRUTS_RESULT_MESSAGE;
    }

    @ValidationErrorPage(value = "view")
    @Action(value = "/revPetition-validateInspectionDetails")
    public String validateInspectionDetails() {
        if (assignmentService.getAssignmentsForPosition(
                objection.getStateHistory().get(0).getOwnerPosition().getId(), new Date()).isEmpty()) {
            addActionError(getText(REJECT_ERROR_INITIATOR_INACTIVE, Arrays.asList(ASSISTANT_DESGN)));
            return "view";
        }
        addAllActionMessages(revisionPetitionService.updateStateAndStatus(objection, approverPositionId, workFlowAction,
                approverComments, approverName,wsPortalRequest));
        revisionPetitionService.updateRevisionPetition(objection);
        revisionPetitionService.updateIndexAndPushToPortalInbox(objection);
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
            addActionError(getText(REJECT_ERROR_INITIATOR_INACTIVE, Arrays.asList(REVENUE_INSPECTOR_DESGN)));
            return "view";
        }
        addAllActionMessages(revisionPetitionService.updateStateAndStatus(objection, approverPositionId, workFlowAction,
                approverComments, approverName,wsPortalRequest));
        revisionPetitionService.updateRevisionPetition(objection);
        revisionPetitionService.updateIndexAndPushToPortalInbox(objection);
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
        if (hasErrors())
            return "view";
        final BasicProperty basicProperty = objection.getBasicProperty();
        final PropertyImpl property = objection.getProperty();
        if (WFLOW_ACTION_STEP_APPROVE.equalsIgnoreCase(workFlowAction)) {
            objection.setEgwStatus(egwStatusDAO.getStatusByModuleAndCode(PropertyTaxConstants.OBJECTION_MODULE,
                    PropertyTaxConstants.OBJECTION_ACCEPTED));
            Property oldProperty = basicProperty.getProperty();
            propService.copyCollection((PropertyImpl) oldProperty, (PropertyImpl) oldProperty);
            propService.copyCollection((PropertyImpl) oldProperty, property);
            basicProperty.getProperty().setStatus(STATUS_ISHISTORY);
            property.setStatus(STATUS_ISACTIVE);

            if (NATURE_OF_WORK_RP.equalsIgnoreCase(wfType)) {
                basicProperty.addPropertyStatusValues(propService.createPropStatVal(
                        basicProperty, REVISIONPETITION_STATUS_CODE, null, null, null, null, null));
                demandVoucherService.createDemandVoucher(property, (PropertyImpl) oldProperty,
                        propertyTaxCommonUtils.prepareApplicationDetailsForDemandVoucher(APPLICATION_TYPE_GRP,
                                PropertyTaxConstants.NO_ACTION));
            } else if (WFLOW_ACTION_APPEALPETITION.equalsIgnoreCase(wfType)) {
                basicProperty.addPropertyStatusValues(propService.createPropStatVal(
                        basicProperty, APPEAL_PETITION, null, null, null, null, null));
                demandVoucherService.createDemandVoucher(property, (PropertyImpl) oldProperty,
                        propertyTaxCommonUtils.prepareApplicationDetailsForDemandVoucher(APPLICATION_TYPE_APPEAL_PETITION,
                                PropertyTaxConstants.NO_ACTION));
            }
            else {
                basicProperty.addPropertyStatusValues(propService.createPropStatVal(
                        basicProperty, GRP_STATUS_CODE, null, null, null, null, null));
                demandVoucherService.createDemandVoucher(property, (PropertyImpl) oldProperty,
                        propertyTaxCommonUtils.prepareApplicationDetailsForDemandVoucher(APPLICATION_TYPE_GRP,
                                PropertyTaxConstants.NO_ACTION));
            }
            propService.setWFPropStatValActive(basicProperty);
        }

        addAllActionMessages(revisionPetitionService.updateStateAndStatus(objection, approverPositionId, workFlowAction,
                approverComments, approverName,wsPortalRequest));
        revisionPetitionService.updateRevisionPetition(objection);
        revisionPetitionService.updateIndexAndPushToPortalInbox(objection);
        if(Source.WARDSECRETARY.toString().equalsIgnoreCase(objection.getSource()) && WFLOW_ACTION_STEP_APPROVE.equalsIgnoreCase(workFlowAction)){
            publishUpdateEvent(WFLOW_ACTION_STEP_APPROVE,false);
            
        }
        if (WFLOW_ACTION_STEP_FORWARD.equalsIgnoreCase(workFlowAction))
            addActionMessage(
                    getText("petition.forward.success", new String[] { revisionPetitionService.returWorkflowType(wfType) }));
        else
            sendEmailandSms(objection, REVISION_PETITION_ENDORESEMENTGENERATED);
        if (WFLOW_ACTION_STEP_APPROVE.equalsIgnoreCase(workFlowAction))
            addActionMessage(
                    getText("petition.outcome.success", new String[] { revisionPetitionService.returWorkflowType(wfType) }));
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
                .getNoticeByNoticeTypeAndApplicationNumber(revisionPetitionService.returnNoticeType(objection.getType()),
                        objection.getObjectionNumber());
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
                        .generateNoticeNumber(revisionPetitionService.returnNoticeType(objection.getType()));
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
            else if (WFLOW_ACTION_APPEALPETITION.equalsIgnoreCase(objection.getType()))
                natureOfWork = NATURE_APPEALPETITION;
            else
                natureOfWork = NATURE_GENERAL_REVISION_PETITION;
            reportParams.put("natureOfWork", natureOfWork);
            revisionPetitionService.setNoticeInfo(property, propertyNotice, basicProperty, objection);
            final List<PropertyAckNoticeInfo> floorDetails = getFloorDetailsForNotice(property);
            propertyNotice.setFloorDetailsForNotice(floorDetails);
            reportInput = new ReportRequest(WFLOW_ACTION_APPEALPETITION.equalsIgnoreCase(objection.getType())
                    ? PropertyTaxConstants.REPORT_TEMPLATENAME_APPEAL_SPECIAL_NOTICE
                    : PropertyTaxConstants.REPORT_TEMPLATENAME_RP_SPECIAL_NOTICE, propertyNotice,
                    reportParams);
            reportInput.setPrintDialogOnOpenReport(true);
            reportInput.setReportFormat(ReportFormat.PDF);
            reportOutput = reportService.createReport(reportInput);
            if (reportOutput != null && reportOutput.getReportOutputData() != null)
                specialNoticePdf = new ByteArrayInputStream(reportOutput.getReportOutputData());
            if (WFLOW_ACTION_STEP_SIGN.equals(actionType)) {
                if (notice == null) {
                    final PtNotice savedNotice = noticeService.saveNotice(objection.getObjectionNumber(),
                            noticeNo, revisionPetitionService.returnNoticeType(objection.getType()), objection.getBasicProperty(), specialNoticePdf);
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
    public String printEndorsementNotice() {
        final ReportOutput reportOutput = new ReportOutput();
        if (objection != null && objection.getObjectionNumber() != null) {
            final PtNotice ptNotice = noticeService.getPtNoticeByNoticeNumberAndNoticeType(
                    objection.getObjectionNumber()
                            .concat(PropertyTaxConstants.NOTICE_TYPE_REVISIONPETITION_ENDORSEMENT_PREFIX),
                    (WFLOW_ACTION_APPEALPETITION.equalsIgnoreCase(objection.getType())
                            ? PropertyTaxConstants.NOTICE_TYPE_APPEALPETITION_ENDORSEMENT
                            : PropertyTaxConstants.NOTICE_TYPE_REVISIONPETITION_ENDORSEMENT));
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

    @ValidationErrorPage(value = "view")
    @Action(value = "/revPetition-generateEnodresementNotice")
    public String generateEndorsementNotice() {
        ReportOutput reportOutput = new ReportOutput();
        InputStream endoresementPdf = null;
        if (objection.getGenerateSpecialNotice() != null && !objection.getGenerateSpecialNotice()) {
            objection.getBasicProperty()
                    .setStatus(propertyStatusDAO.getPropertyStatusByCode(PropertyTaxConstants.STATUS_CODE_ASSESSED));
            objection.getBasicProperty().setUnderWorkflow(Boolean.FALSE);
            objection.transition().end().withStateValue(PropertyTaxConstants.WFLOW_ACTION_END).withNextAction(null)
                    .withOwner((Position) null)
                    .withOwner(securityUtils.getCurrentUser()).withComments(approverComments);
        } else {
            if (assignmentService.getAssignmentsForPosition(
                    objection.getStateHistory().get(0).getOwnerPosition().getId(), new Date()).isEmpty()) {
                addActionError(getText(REJECT_ERROR_INITIATOR_INACTIVE,
                        Arrays.asList(JUNIOR_ASSISTANT + "/" + SENIOR_ASSISTANT)));
                return "view";
            }
            addAllActionMessages(revisionPetitionService.updateStateAndStatus(objection, approverPositionId,
                    workFlowAction, approverComments, approverName,wsPortalRequest));
        }
        reportOutput = revisionPetitionService.createEndoresement(reportOutput, objection);
        if (reportOutput != null && reportOutput.getReportOutputData() != null)
            endoresementPdf = new ByteArrayInputStream(reportOutput.getReportOutputData());
        noticeService.saveNotice(objection.getObjectionNumber(),
                objection.getObjectionNumber()
                        .concat(PropertyTaxConstants.NOTICE_TYPE_REVISIONPETITION_ENDORSEMENT_PREFIX),
                WFLOW_ACTION_APPEALPETITION.equalsIgnoreCase(objection.getType())
                        ? PropertyTaxConstants.NOTICE_TYPE_APPEALPETITION_ENDORSEMENT
                        : PropertyTaxConstants.NOTICE_TYPE_REVISIONPETITION_ENDORSEMENT,
                objection.getBasicProperty(),
                endoresementPdf);
        revisionPetitionService.updateRevisionPetition(objection);
        revisionPetitionService.updateIndexAndPushToPortalInbox(objection);
        addActionMessage(
                getText("petition.endoresementNotice.success", new String[] {revisionPetitionService.returWorkflowType(wfType)}));
        if (objection != null && objection.getObjectionNumber() != null) {
            final PtNotice ptNotice = noticeService.getPtNoticeByNoticeNumberAndNoticeType(
                    objection.getObjectionNumber()
                            .concat(PropertyTaxConstants.NOTICE_TYPE_REVISIONPETITION_ENDORSEMENT_PREFIX),
                    WFLOW_ACTION_APPEALPETITION.equalsIgnoreCase(objection.getType())
                            ? PropertyTaxConstants.NOTICE_TYPE_APPEALPETITION_ENDORSEMENT
                            : PropertyTaxConstants.NOTICE_TYPE_REVISIONPETITION_ENDORSEMENT);
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
                addAllActionMessages(revisionPetitionService.updateStateAndStatus(objection, approverPositionId, workFlowAction,
                        approverComments, approverName,wsPortalRequest));

        generateSpecialNotice(objection.getProperty(), (BasicPropertyImpl) objection.getBasicProperty());

        if (!WFLOW_ACTION_STEP_SIGN.equals(actionType))
            revisionPetitionService.updateRevisionPetition(objection);
        final ReportOutput reportOutput = new ReportOutput();
        if (objection != null && objection.getObjectionNumber() != null) {
            final PtNotice ptNotice = noticeService.getNoticeByNoticeTypeAndApplicationNumber(
                    objection.getObjectionNumber(), objection.getType().equalsIgnoreCase(WFLOW_ACTION_APPEALPETITION)
                            ? NOTICE_TYPE_APPEALPROCEEDINGS : NOTICE_TYPE_RPPROCEEDINGS);
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
            else if (wfType.equalsIgnoreCase(WFLOW_ACTION_APPEALPETITION))
                propService.updateIndexes(objection, APPLICATION_TYPE_APPEAL_PETITION);
            else
                propService.updateIndexes(objection, APPLICATION_TYPE_GRP);

            if (objection != null && objection.getSource() != null
                    && Source.CITIZENPORTAL.toString().equalsIgnoreCase(objection.getSource()))
                if (NATURE_OF_WORK_RP.equalsIgnoreCase(wfType))
                    propService.updatePortal(objection, APPLICATION_TYPE_REVISION_PETITION);
                else if (wfType.equalsIgnoreCase(WFLOW_ACTION_APPEALPETITION))
                    propService.updatePortal(objection, APPLICATION_TYPE_APPEAL_PETITION);
                else
                    propService.updatePortal(objection, APPLICATION_TYPE_GRP);
        }
        return WFLOW_ACTION_STEP_SIGN.equals(actionType) ? DIGITAL_SIGNATURE_REDIRECTION : NOTICE;
    }

    /**
     * @param objection
     * @param applicationType
     */
    public void sendEmailandSms(final Petition petition, final String applicationType) {
        for (final PropertyOwnerInfo ownerInfo : petition.getBasicProperty().getPropertyOwnerInfo())
            sendEmailAndSms(petition, ownerInfo.getOwner(), applicationType);
    }

    private void sendEmailAndSms(final Petition petition, final User user, final String applicationType) {
        if (petition != null) {
            final String mobileNumber = user.getMobileNumber();
            final String emailid = user.getEmailId();
            final String applicantName = user.getName();
            final List<String> args = new ArrayList<>();
            args.add(NATURE_OF_WORK_RP.equalsIgnoreCase(wfType) ? NATURE_REVISION_PETITION
                    : WFLOW_ACTION_APPEALPETITION.equalsIgnoreCase(petition.getType()) ? NATURE_APPEALPETITION
                            : NATURE_GENERAL_REVISION_PETITION);
            args.add(applicantName);
            String smsMsg = "";
            String emailSubject = "";
            String emailBody = "";

            if (applicationType.equalsIgnoreCase(REVISION_PETITION_CREATED)) {
                args.add(petition.getObjectionNumber());
                args.add(sMSEmailService.getCityName());
                if (mobileNumber != null)
                    smsMsg = getText("msg.revPetitioncreate.sms", args);
                if (emailid != null) {
                    emailSubject = getText("msg.revPetitioncreate.email.subject", args);
                    emailBody = getText("msg.revPetitioncreate.email", args);
                }
            } else if (applicationType.equalsIgnoreCase(REVISION_PETITION_HEARINGNOTICEGENERATED)) {

                if (petition.getHearings() != null && !petition.getHearings().isEmpty()) {
                    args.add(DateUtils.getFormattedDate(petition.getHearings().get(0).getPlannedHearingDt(),
                            DATE_FORMAT_DDMMYYY));
                    args.add(petition.getHearings().get(0).getHearingVenue());
                    args.add(petition.getHearings().get(0).getHearingTime());
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
        final List<Floor> floors = property.getPropertyDetail().getFloorDetails();
        objection.getBasicProperty().getProperty().getPropertyDetail().setFloorDetails(floors);
        if (null != objection.getProperty())
            objection.getProperty().getPropertyDetail().setFloorDetailsProxy(floors);
        int i = 0;
        for (final Floor flr : floors) {
            floorNoStr[i] = FLOOR_MAP.get(flr.getFloorNo());
            i++;
        }
    }

    @Action(value = "/revPetition-view")
    public String view() {
        final BasicProperty basicProperty = objection.getBasicProperty();
        getPropertyView(basicProperty.getUpicNo());
        if (superStructureRP(objection)) {
            basicProperty.setPropertyOwnerInfoProxy(basicProperty.getPropertyOwnerInfo());
            try {
                final Query query = entityManager.createNamedQuery("DOCUMENT_TYPE_DETAILS_BY_ID");
                query.setParameter("basicProperty", basicProperty.getId());
                setDocumentTypeDetails((DocumentTypeDetails) query.getSingleResult());
            } catch (final Exception e) {
                logger.error("No Document type details present for Basicproperty " + e);
            }
        }
        isReassignEnabled = reassignmentservice.isReassignEnabled();
        stateAwareId = objection.getId();
        if (basicProperty != null
                && basicProperty.getPropertyID() != null) {
            final PropertyID propertyID = basicProperty.getPropertyID();
            northBoundary = propertyID.getNorthBoundary();
            southBoundary = propertyID.getSouthBoundary();
            eastBoundary = propertyID.getEastBoundary();
            westBoundary = propertyID.getWestBoundary();
        }
        if(objection.getState()!=null && !objection.getState().getValue().contains("Registered")){
        final String designation = propService.getDesignationForPositionAndUser(
                objection.getCurrentState().getOwnerPosition().getId(), securityUtils.getCurrentUser().getId());
            if ((REVENUE_INSPECTOR_DESGN.equals(designation) || REVENUE_OFFICER_DESGN.equals(designation)
                    || designation.contains(COMMISSIONER_DESGN))
                    && WFLOW_ACTION_APPEALPETITION.equalsIgnoreCase(objection.getType())) {
                documentTypes.addAll(propService.getDocumentTypesForTransactionType(TransactionType.APPEALPETITION));
                objection.getDocuments().add(new Document());
            }
            
        }
        populatePropertyTypeCategory();
        populateLayoutAndVltArea();
        checkIfEligibleForDocEdit();
        if (objection != null && objection.getProperty() != null) {
            final PropertyDetail propertyDetail = objection.getProperty().getPropertyDetail();
            setReasonForModify(propertyDetail.getPropertyMutationMaster().getCode());
            if (propertyDetail.getSitalArea() != null)
                setAreaOfPlot(propertyDetail.getSitalArea().getArea().toString());

            if (!propertyDetail.getFloorDetails().isEmpty())
                setFloorDetails(objection.getProperty());
            if (propertyDetail.getPropertyTypeMaster() != null)
                propTypeObjId = propertyDetail.getPropertyTypeMaster().getId().toString();
            setSuperStructure(propertyDetail.isStructure());
        }

        setOwnerName(basicProperty.getProperty());
        setPropertyAddress(basicProperty.getAddress());
        propStatVal = propertyStatusValuesDAO.getLatestPropertyStatusValuesByPropertyIdAndreferenceNo(
                basicProperty.getUpicNo(), objection.getObjectionNumber());

        if (objection.getState() != null) {
            if (!objection.getState().getHistory().isEmpty())
                setUpWorkFlowHistory(objection.getState().getId());
            historyMap = propService.populateHistory(objection);
        }
        setOwnerName(basicProperty.getProperty());
        setPropertyAddress(basicProperty.getAddress());
        setWfType(objection.getType());
        List<String> reasonList = objection.getAppealReasons().stream().map(AppealPetitionReasons::getDescription)
                .collect(Collectors.toList());
         objection.setReasons(String.join(",", reasonList));
        if (!objection.getCurrentState().getValue().endsWith(WFLOW_ACTION_NEW)
                && WFLOW_ACTION_APPEALPETITION.equalsIgnoreCase(objection.getType()))
            objection.setAppealOtherRemarks("N/A");
        return "view";
    }

    private void checkIfEligibleForDocEdit() {
        final String designation = propService.getDesignationForPositionAndUser(
                objection.getCurrentState().getOwnerPosition().getId(), securityUtils.getCurrentUser().getId());
        if (objection.getCurrentState().getValue().endsWith(STATUS_REJECTED) ||
                objection.getCurrentState().getValue().endsWith(WFLOW_ACTION_NEW)
                || REVENUE_INSPECTOR_DESGN.equals(designation))
            setAllowEditDocument(Boolean.TRUE);
    }

    public String viewObjectionDetails() {
        objection = revisionPetitionService.find("from Objection where objectionNumber like ?",
                objection.getObjectionNumber());
        setOwnerName(objection.getBasicProperty().getProperty());
        setPropertyAddress(objection.getBasicProperty().getAddress());
        return "viewDetails";
    }

    @Action(value = "/revPetition-reject")
    public String rejectRevisionPetition() {

        revisionPetitionService.cancelObjection(objection);
        objection.getDocuments().clear();
        revisionPetitionService.updateRevisionPetition(objection);
        revisionPetitionService.updateIndexAndPushToPortalInbox(objection);
        
        if(Source.WARDSECRETARY.toString().equalsIgnoreCase(objection.getSource())){
            publishUpdateEvent(WFLOW_ACTION_STEP_REJECT,"Closed".equalsIgnoreCase(objection.getState().getValue()));
            
        }
        if (objection.getType().equalsIgnoreCase(NATURE_OF_WORK_GRP))
            addActionMessage(getText("objection.cancelled"));
        else
            addActionMessage(getText("petition.appeal.cancelled"));    
        return STRUTS_RESULT_MESSAGE;
    }

    public String updateRecordObjection() {

        revisionPetitionService.updateRevisionPetition(objection);

        addAllActionMessages(revisionPetitionService.updateStateAndStatus(objection, approverPositionId, workFlowAction,
                approverComments, approverName,wsPortalRequest));
        return STRUTS_RESULT_MESSAGE;
    }

    private void modifyBasicProp() throws TaxCalculatorExeption {
        Date propCompletionDate = null;
        final PropertyDetail propertyDetail = objection.getProperty().getPropertyDetail();
        final BasicProperty basicProperty = objection.getBasicProperty();
        final PropertyImpl property = objection.getProperty();
        final Long oldPropTypeId = propertyDetail.getPropertyTypeMaster().getId();

        if (propTypeObjId != null && !propTypeObjId.trim().isEmpty() && !"-1".equals(propTypeObjId)) {
            final PropertyTypeMaster propTypeMstr = (PropertyTypeMaster) getPersistenceService()
                    .find("from PropertyTypeMaster ptm where ptm.id = ?", Long.valueOf(propTypeObjId));

            if (!propTypeMstr.getCode().equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND))
                propCompletionDate = propService
                        .getLowestDtOfCompFloorWise(propertyDetail.getFloorDetailsProxy());
            else {
                propCompletionDate = propertyDetail.getDateOfCompletion();
                setAreaOfPlot(propertyDetail.getSitalArea().getArea().toString());
            }

        }
        if (propCompletionDate != null) {
            basicProperty.setPropOccupationDate(propCompletionDate);
            property.setEffectiveDate(propCompletionDate);
        }

        propService.createProperty(property, getAreaOfPlot() != null ? getAreaOfPlot() : "",
                reasonForModify, propTypeObjId != null ? propTypeObjId : null,
                propertyDetail.getPropertyUsage() != null
                        ? propertyDetail.getPropertyUsage().getId().toString() : null,
                propertyDetail.getPropertyOccupation() != null
                        ? propertyDetail.getPropertyOccupation().getId().toString() : null,
                STATUS_WORKFLOW, property.getDocNumber(), "",
                propertyDetail.getFloorType() != null
                        ? propertyDetail.getFloorType().getId() : null,
                propertyDetail.getRoofType() != null
                        ? propertyDetail.getRoofType().getId() : null,
                propertyDetail.getWallType() != null
                        ? propertyDetail.getWallType().getId() : null,
                propertyDetail.getWoodType() != null
                        ? propertyDetail.getWoodType().getId() : null,
                taxExemptedReason,
                propertyDetail.getPropertyDepartment() != null
                        ? propertyDetail.getPropertyDepartment().getId() : null,
                getVacantLandPlotAreaId(), getLayoutApprovalAuthorityId(), Boolean.FALSE);

        updatePropertyID(basicProperty);
        basicProperty.getPropertyID().setZone(boundaryService.getBoundaryById(getZoneId()));
        final PropertyTypeMaster propTypeMstr = (PropertyTypeMaster) getPersistenceService()
                .find("from PropertyTypeMaster ptm where ptm.code = ?", OWNERSHIP_TYPE_VAC_LAND);

        if (oldPropTypeId == propTypeMstr.getId() && Long.parseLong(propTypeObjId) != propTypeMstr.getId()
                || oldPropTypeId != propTypeMstr.getId() && Long.parseLong(propTypeObjId) == propTypeMstr.getId())
            if (propTypeMstr != null && StringUtils.equals(propTypeMstr.getId().toString(), propTypeObjId))
                propService.changePropertyDetail(property, new VacantProperty(), 0);
            else
                propService.changePropertyDetail(property, new BuiltUpProperty(),
                        propertyDetail.getFloorDetails().size());
        if (property.getPropertyModifyReason().equalsIgnoreCase(PROPERTY_MODIFY_REASON_REVISION_PETITION) &&
                basicProperty.getProperty().getPropertyModifyReason()
                        .equalsIgnoreCase(PROPERTY_MODIFY_REASON_ADD_OR_ALTER)) {
            revisionPetitionService.modifyRPDemand(property,
                    (PropertyImpl) basicProperty.getProperty());
        } else
            propService.modifyDemand(property, (PropertyImpl) basicProperty.getProperty());

        if (propertyDetail.getLayoutApprovalAuthority() != null && "No Approval"
                .equals(propertyDetail.getLayoutApprovalAuthority().getName())) {
            propertyDetail.setLayoutPermitNo(null);
            propertyDetail.setLayoutPermitDate(null);
        }

    }

    /**
     * @param propertyId
     */
    private void getPropertyView(final String propertyId) {
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
    }

    public void vaidatePropertyDetails() {
        if (reasonForModify == null || reasonForModify.equals("-1"))
            addActionError(getText("mandatory.rsnForMdfy"));
        validateProperty(objection.getProperty(), getAreaOfPlot() != null ? getAreaOfPlot() : "", eastBoundary,
                westBoundary, southBoundary, northBoundary, propTypeObjId != null ? propTypeObjId : null,
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
        } else if (WFLOW_ACTION_APPEALPETITION.equalsIgnoreCase(wfType)) {
            serviceType = APPEAL_PETITION;
            applicationType = APPEAL_PETITION;
        } 
        else {
            serviceType = GENERAL_REVISION_PETITION;
            applicationType = GENERAL_REVISION_PETETION;
        }
        if (ANONYMOUS_USER.equalsIgnoreCase(securityUtils.getCurrentUser().getName()))
            setApplicationSource(SOURCE_ONLINE.toLowerCase());
        reportOutput = propertyTaxUtil.generateCitizenCharterAcknowledgement(propertyId, applicationType, serviceType,
                applicationNumber);
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

    public Boolean superStructureRP(final Petition petition) {
        final Boolean b = petition.getProperty() != null ? petition.getProperty().getPropertyDetail().isStructure()
                : petition.getBasicProperty().getProperty().getPropertyDetail().isStructure();
        return b && !PROPERTY_MODIFY_REASON_GENERAL_REVISION_PETITION.equals(petition.getType());
    }

    public void addAllActionMessages(final Map<String, String[]> actionMessages) {
        for (final Map.Entry<String, String[]> entry : actionMessages.entrySet()) {
            if (ArrayUtils.isEmpty(entry.getValue()))
                addActionMessage(getText(entry.getKey()));
            addActionMessage(getText(entry.getKey(), entry.getValue()));
        }

    }

    @Override
    public String getPendingActions() {
        if (objection != null && objection.getId() != null) {
            if (PropertyTaxConstants.RP_INSPECTIONVERIFIED.equalsIgnoreCase(objection.getCurrentState().getValue())
                    || PropertyTaxConstants.GRP_INSPECTIONVERIFIED
                            .equalsIgnoreCase(objection.getCurrentState().getValue())
                    || PropertyTaxConstants.APPEAL_INSPECTIONVERIFIED
                            .equalsIgnoreCase(objection.getCurrentState().getValue())
                    || objection.getCurrentState().getValue().endsWith("Forwarded")
                    || objection.getCurrentState().getValue().endsWith("Approved"))
                return objection.getCurrentState().getNextAction();
            else
                return null;
        } else
            return null;

    }

    @Override
    public String getAdditionalRule() {
        String addittionalRule;
        if (PROPERTY_MODIFY_REASON_GENERAL_REVISION_PETITION.equals(objection.getType()))
            addittionalRule = GENERAL_REVISION_PETITION;
        else if (WFLOW_ACTION_APPEALPETITION.equalsIgnoreCase(objection.getType()))
            addittionalRule = APPEALPETITION_CODE;
        else
            addittionalRule = REVISION_PETITION;
        return addittionalRule;
    }

    @Action(value = "/revPetition-rejecttocancel")
    public String rejectToCancelRevisionPetition() {
        objection.getProperty().setStatus(PropertyTaxConstants.STATUS_CANCELLED);
        revisionPetitionService.updateStateAndStatus(objection, approverPositionId, workFlowAction,
                approverComments, approverName,wsPortalRequest);
        revisionPetitionService.updateRevisionPetition(objection);
        revisionPetitionService.updateIndexAndPushToPortalInbox(objection);
        if(Source.WARDSECRETARY.toString().equalsIgnoreCase(objection.getSource())){
            publishUpdateEvent(WFLOW_ACTION_STEP_REJECT_TO_CANCEL,true);
            
        }
        
        StringBuilder build = new StringBuilder(" ").append(securityUtils.getCurrentUser().getName())
                .append(" with Assessment Number: ")
                .append(objection.getBasicProperty().getUpicNo());
        String message = getText("petition.forward.cancel")+build.toString();
        addActionMessage(
                getText(message,
                        new String[] { revisionPetitionService.returWorkflowType(wfType)}));
        return STRUTS_RESULT_MESSAGE;
    }
    
    private void publishUpdateEvent(final String action, final boolean isCancelled) {
        String remarks = null;
        if (NATURE_OF_WORK_RP.equalsIgnoreCase(wfType)) {
            if (isCancelled)
                remarks = WFLOW_ACTION_STEP_REJECT_TO_CANCEL.equalsIgnoreCase(action)
                        ? "Property Revision Petition Rejected to Cancel" : "Property Revision Petition Cancelled";
            else
                remarks = WFLOW_ACTION_STEP_APPROVE.equalsIgnoreCase(action) ? "Property Revision Petition Approved"
                        : "Property Revision Petition Rejected";
        } else if (NATURE_OF_WORK_GRP.equalsIgnoreCase(wfType))
            if (isCancelled)
                remarks = WFLOW_ACTION_STEP_REJECT_TO_CANCEL.equalsIgnoreCase(action)
                        ? "Property General Revision Petition Rejected to Cancel"
                        : "Property General Revision Petition Cancelled";
            else
                remarks = WFLOW_ACTION_STEP_APPROVE.equalsIgnoreCase(action) ? "Property General Revision Petition Approved"
                        : "Property General Revision Petition Rejected";

        propertyThirdPartyService.publishUpdateEvent(objection.getApplicationNo(), action, remarks);

    }

    public List<AppealPetitionReasons> getAppealReasonDetails(Petition petition) {
        String[] appeal = petition.getReasons().split(",");
        List<AppealPetitionReasons> appealList = new ArrayList<>();
        for (String value : appeal) {
            appealList.add(appealPetitionReasonRepository.findByCode(value.trim()));
        }
        return appealList;

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

    public Petition getObjection() {
        return objection;
    }

    public void setObjection(final Petition objection) {
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

    public void setObjectionWorkflowService(final WorkflowService<Petition> objectionWorkflowService) {
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

    public Long getZoneId() {
        return zoneId;
    }

    public void setZoneId(Long zoneId) {
        this.zoneId = zoneId;
    }

    public SimpleWorkflowService<Petition> getRevisionPetitionWorkFlowService() {
        return revisionPetitionWorkFlowService;
    }

    public void setRevisionPetitionWorkFlowService(
            final SimpleWorkflowService<Petition> revisionPetitionWorkFlowService) {
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

    public Boolean isSuperStructure() {
        return superStructure;
    }

    public void setSuperStructure(final Boolean superStructure) {
        this.superStructure = superStructure;
    }

    public List<DocumentType> getAssessmentDocumentTypesRP() {
        return assessmentDocumentTypesRP;
    }

    public void setAssessmentDocumentTypesRP(final List<DocumentType> assessmentDocumentTypesRP) {
        this.assessmentDocumentTypesRP = assessmentDocumentTypesRP;
    }

    public List<String> getAssessmentDocumentNames() {
        return assessmentDocumentNames;
    }

    public void setAssessmentDocumentNames(final List<String> assessmentDocumentNames) {
        this.assessmentDocumentNames = assessmentDocumentNames;
    }

    public DocumentTypeDetails getDocumentTypeDetails() {
        return documentTypeDetails;
    }

    public void setDocumentTypeDetails(final DocumentTypeDetails documentTypeDetails) {
        this.documentTypeDetails = documentTypeDetails;
    }

    public boolean isEditOwnerDetails() {
        return editOwnerDetails;
    }

    public void setEditOwnerDetails(boolean editOwnerDetails) {
        this.editOwnerDetails = editOwnerDetails;
    }
}