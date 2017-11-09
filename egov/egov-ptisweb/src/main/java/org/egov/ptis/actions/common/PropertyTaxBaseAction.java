/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 */
package org.egov.ptis.actions.common;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.math.BigDecimal.ZERO;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.egov.ptis.constants.PropertyTaxConstants.ADDTIONAL_RULE_ALTER_ASSESSMENT;
import static org.egov.ptis.constants.PropertyTaxConstants.ADDTIONAL_RULE_BIFURCATE_ASSESSMENT;
import static org.egov.ptis.constants.PropertyTaxConstants.ALTERATION_OF_ASSESSMENT;
import static org.egov.ptis.constants.PropertyTaxConstants.AMALGAMATION;
import static org.egov.ptis.constants.PropertyTaxConstants.ANONYMOUS_USER;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_ALTER_ASSESSENT;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_GRP;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_NEW_ASSESSENT;
import static org.egov.ptis.constants.PropertyTaxConstants.BILL_COLLECTOR_DESGN;
import static org.egov.ptis.constants.PropertyTaxConstants.CATEGORY_MIXED;
import static org.egov.ptis.constants.PropertyTaxConstants.CATEGORY_NON_RESIDENTIAL;
import static org.egov.ptis.constants.PropertyTaxConstants.CATEGORY_RESIDENTIAL;
import static org.egov.ptis.constants.PropertyTaxConstants.COMMISSIONER_DESGN;
import static org.egov.ptis.constants.PropertyTaxConstants.COMMISSIONER_DESIGNATIONS;
import static org.egov.ptis.constants.PropertyTaxConstants.CURRENTYEAR_FIRST_HALF;
import static org.egov.ptis.constants.PropertyTaxConstants.CURRENTYEAR_SECOND_HALF;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_FIRSTHALF_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_SECONDHALF_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_STR_EDUCATIONAL_CESS;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_STR_GENERAL_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_STR_LIBRARY_CESS;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_STR_UNAUTHORIZED_PENALTY;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_STR_VACANT_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMOLITION;
import static org.egov.ptis.constants.PropertyTaxConstants.EXEMPTION;
import static org.egov.ptis.constants.PropertyTaxConstants.FILESTORE_MODULE_NAME;
import static org.egov.ptis.constants.PropertyTaxConstants.FLOOR_MAP;
import static org.egov.ptis.constants.PropertyTaxConstants.JUNIOR_ASSISTANT;
import static org.egov.ptis.constants.PropertyTaxConstants.NATURE_ALTERATION;
import static org.egov.ptis.constants.PropertyTaxConstants.NATURE_AMALGAMATION;
import static org.egov.ptis.constants.PropertyTaxConstants.NATURE_BIFURCATION;
import static org.egov.ptis.constants.PropertyTaxConstants.NATURE_DEMOLITION;
import static org.egov.ptis.constants.PropertyTaxConstants.NATURE_NEW_ASSESSMENT;
import static org.egov.ptis.constants.PropertyTaxConstants.NATURE_OF_USAGE_RESIDENCE;
import static org.egov.ptis.constants.PropertyTaxConstants.NATURE_TAX_EXEMPTION;
import static org.egov.ptis.constants.PropertyTaxConstants.NEW_ASSESSMENT;
import static org.egov.ptis.constants.PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_MODIFY_REASON_ADD_OR_ALTER;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_MODIFY_REASON_BIFURCATE;
import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_INSPECTOR_DESGN;
import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_OFFICER_DESGN;
import static org.egov.ptis.constants.PropertyTaxConstants.SENIOR_ASSISTANT;
import static org.egov.ptis.constants.PropertyTaxConstants.SOURCEOFDATA_MOBILE;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_CANCELLED;
import static org.egov.ptis.constants.PropertyTaxConstants.TARGET_WORKFLOW_ERROR;
import static org.egov.ptis.constants.PropertyTaxConstants.TAX_COLLECTOR_DESGN;
import static org.egov.ptis.constants.PropertyTaxConstants.UD_REVENUE_INSPECTOR_APPROVAL_PENDING;
import static org.egov.ptis.constants.PropertyTaxConstants.VACANTLAND_MIN_CUR_CAPITALVALUE;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_APPROVE;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_FORWARD;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_REJECT;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_SAVE;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_ASSISTANT_APPROVAL_PENDING;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_ASSISTANT_APPROVED;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_COMMISSIONER_APPROVAL_PENDING;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_COMMISSIONER_APPROVED;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_DIGITAL_SIGNATURE_PENDING;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_REJECTED;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.egov.commons.Installment;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.DesignationService;
import org.egov.eis.service.EisCommonService;
import org.egov.eis.web.actions.workflow.GenericWorkFlowAction;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.notification.service.NotificationService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.infra.workflow.inbox.InboxRenderServiceDelegate;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.egov.ptis.domain.entity.document.DocumentTypeDetails;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Floor;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyDetail;
import org.egov.ptis.domain.entity.property.PropertyDocs;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyOwnerInfo;
import org.egov.ptis.domain.entity.property.PropertyTypeMaster;
import org.egov.ptis.domain.entity.property.PropertyUsage;
import org.egov.ptis.domain.entity.property.WorkflowBean;
import org.egov.ptis.domain.repository.master.vacantland.LayoutApprovalAuthorityRepository;
import org.egov.ptis.domain.service.property.PropertyService;
import org.egov.ptis.domain.service.property.SMSEmailService;
import org.egov.ptis.master.service.PropertyUsageService;
import org.egov.ptis.notice.PtNotice;
import org.egov.ptis.service.utils.PropertyTaxCommonUtils;
import org.hibernate.Query;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class PropertyTaxBaseAction extends GenericWorkFlowAction {

    private static Logger logger = Logger.getLogger(PropertyTaxBaseAction.class);
    private static final long serialVersionUID = 1L;

    private static final String CHOOSE = "----Choose----";
    private static final String CANCEL = "cancel";
    private static final String END = "END";
    private static final String UNAUTHORISED_PENALTY = "unauthorisedPenalty";
    private static final String TOTAL_TAX = "totalTax";
    public static final String MEESEVA_RESULT_ACK = "meesevaAck";
    protected Boolean isReassignEnabled = Boolean.FALSE;
    protected Long stateAwareId;
    protected String transactionType;

    protected Boolean isApprPageReq = Boolean.TRUE;

    protected String indexNumber;
    protected String modelId;
    protected String userRole;
    protected String ackMessage;
    protected String userDesgn;
    protected String wfErrorMsg;
    protected Boolean endorsementRequired = FALSE;
    protected String ownersName;
    protected List<PtNotice> endorsementNotices;
    protected String applicationNumber;
    protected String assessmentNumber;
    final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");

    @Autowired
    protected AssignmentService assignmentService;
    @Autowired
    private InboxRenderServiceDelegate<StateAware> inboxRenderServiceDeligate;
    @Autowired
    protected EisCommonService eisCommonService;
    @Autowired
    @Qualifier("fileStoreService")
    protected FileStoreService fileStoreService;
    @Autowired
    @Qualifier("workflowService")
    private SimpleWorkflowService<PropertyImpl> propertyWorkflowService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private PtDemandDao ptDemandDAO;
    @Autowired
    private PropertyService propertyService;
    private SMSEmailService sMSEmailService;
    protected PropertyTaxUtil propertyTaxUtil;
    @Autowired
    private SecurityUtils securityUtils;
    private PropertyImpl propertyModel;
    protected WorkflowBean workflowBean;
    @Autowired
    private PropertyUsageService propertyUsageService;
    @Autowired
    protected PropertyTaxCommonUtils propertyTaxCommonUtils;

    private List<File> uploads = new ArrayList<>();
    private List<String> uploadFileNames = new ArrayList<>();
    private List<String> uploadContentTypes = new ArrayList<>();
    protected Map<String, BigDecimal> propertyTaxDetailsMap = new HashMap<>(0);
    protected List<HashMap<String, Object>> historyMap = new ArrayList<>();

    protected Boolean propertyByEmployee = Boolean.TRUE;
    protected String userDesignationList;
    protected String applicationType;
    protected String initiator;

    @Autowired
    transient LayoutApprovalAuthorityRepository layoutApprovalAuthorityRepo;

    public List<File> getUpload() {
        return uploads;
    }

    public void setUpload(final List<File> uploads) {
        this.uploads = uploads;
    }

    public List<String> getUploadFileName() {
        return uploadFileNames;
    }

    public void setUploadFileName(final List<String> uploadFileNames) {
        this.uploadFileNames = uploadFileNames;
    }

    public List<String> getUploadContentType() {
        return uploadContentTypes;
    }

    public void setUploadContentType(final List<String> contentTypes) {
        uploadContentTypes = contentTypes;
    }

    protected void processAndStoreDocumentsWithReason(final BasicProperty basicProperty, final String reason) {
        if (!uploads.isEmpty()) {
            int fileCount = 0;
            for (final File file : uploads) {
                final FileStoreMapper fileStore = fileStoreService.store(file, uploadFileNames.get(fileCount),
                        uploadContentTypes.get(fileCount++), FILESTORE_MODULE_NAME);
                final PropertyDocs propertyDoc = new PropertyDocs();
                propertyDoc.setSupportDoc(fileStore);
                propertyDoc.setBasicProperty(basicProperty);
                propertyDoc.setReason(reason);
                basicProperty.addDocs(propertyDoc);
            }
        }
    }

    @Override
    public void validate() {

    }

    protected List<StateHistory> setUpWorkFlowHistory(final Long stateId) {
        final List<StateHistory> workflowHisObj = inboxRenderServiceDeligate.getStateHistory(stateId);
        if (workflowBean != null)
            workflowBean.setWorkFlowHistoryItems(workflowHisObj);
        return workflowHisObj;
    }

    protected void setupWorkflowDetails() {
        if (logger.isDebugEnabled())
            logger.debug("Entered into setupWorkflowDetails | Start");
        if (workflowBean != null && logger.isDebugEnabled())
            logger.debug("setupWorkflowDetails: Department: " + workflowBean.getDepartmentId() + " Designation: "
                    + workflowBean.getDesignationId());
        final AjaxCommonAction ajaxCommonAction = new AjaxCommonAction();
        ajaxCommonAction.setPersistenceService(persistenceService);
        ajaxCommonAction.setDesignationService(new DesignationService());
        ajaxCommonAction.setAssignmentService(getAssignmentService());
        final List<Department> departmentsForLoggedInUser = propertyTaxUtil
                .getDepartmentsForLoggedInUser(securityUtils.getCurrentUser());
        workflowBean.setDepartmentList(departmentsForLoggedInUser);
        workflowBean.setDesignationList(Collections.emptyList());
        workflowBean.setAppoverUserList(Collections.emptyList());
        if (logger.isDebugEnabled())
            logger.debug("Exiting from setupWorkflowDetails | End");
    }

    protected void validateProperty(final Property property, final String areaOfPlot, final String dateOfCompletion,
            final String eastBoundary, final String westBoundary, final String southBoundary,
            final String northBoundary, final String propTypeId, final String zoneId, final String propOccId,
            final Long floorTypeId, final Long roofTypeId, final Long wallTypeId, final Long woodTypeId,
            final String modifyRsn, final Date propCompletionDate, final Long vacantLandPlotAreaId,
            final Long layoutApprovalAuthorityId, final DocumentTypeDetails documentTypeDetails) {

        if (logger.isDebugEnabled())
            logger.debug("Entered into validateProperty");

        if (propTypeId == null || "-1".equals(propTypeId))
            addActionError(getText("mandatory.propType"));
        if (isBlank(property.getPropertyDetail().getCategoryType())
                || "-1".equals(property.getPropertyDetail().getCategoryType()))
            addActionError(getText("mandatory.propcatType"));

        if (propTypeId != null && !"-1".equals(propTypeId)) {
            final PropertyTypeMaster propTypeMstr = (PropertyTypeMaster) getPersistenceService().find(
                    "from PropertyTypeMaster ptm where ptm.id = ?", Long.valueOf(propTypeId));
            if (propTypeMstr != null) {
                Date regDocDate = null;
                final PropertyDetail propertyDetail = property.getPropertyDetail();
                if (documentTypeDetails != null && documentTypeDetails.getDocumentName() != null)
                    regDocDate = documentTypeDetails.getDocumentName()
                            .equals(PropertyTaxConstants.DOCUMENT_NAME_REGD_DOCUMENT)
                                    ? documentTypeDetails.getDocumentDate() : null;
                if (propTypeMstr.getCode().equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND)) {
                    if (null != propertyDetail)
                        validateVacantProperty(propertyDetail, eastBoundary, westBoundary, southBoundary,
                                northBoundary, modifyRsn, propCompletionDate, vacantLandPlotAreaId, layoutApprovalAuthorityId);
                } else if (null == ((PropertyImpl) property).getId() && TRUE.equals(propertyDetail.isAppurtenantLandChecked())) {
                    validateVacantProperty(propertyDetail, eastBoundary, westBoundary, southBoundary, northBoundary,
                            modifyRsn, propCompletionDate, vacantLandPlotAreaId, layoutApprovalAuthorityId);
                    validateBuiltUpProperty(propertyDetail, floorTypeId, roofTypeId, areaOfPlot, regDocDate, modifyRsn);
                } else
                    validateBuiltUpProperty(propertyDetail, floorTypeId, roofTypeId, areaOfPlot, regDocDate, modifyRsn);
                validateFloor(propTypeMstr, property.getPropertyDetail().getFloorDetailsProxy(), property, areaOfPlot,
                        regDocDate, modifyRsn, propCompletionDate);
            }
        }

        if (logger.isDebugEnabled())
            logger.debug("Exiting from validateProperty");
    }

    public void validateVacantProperty(final PropertyDetail propertyDetail, final String eastBoundary,
            final String westBoundary, final String southBoundary, final String northBoundary, final String modifyRsn,
            final Date propCompletionDate, final Long vacantLandPlotAreaId, final Long layoutApprovalAuthorityId) {

        if (logger.isDebugEnabled())
            logger.debug("Entered into validateVacantProperty");
        if (isBlank(propertyDetail.getSurveyNumber()))
            addActionError(getText("mandatory.surveyNo"));
        if (isBlank(propertyDetail.getPattaNumber()))
            addActionError(getText("mandatory.pattaNum"));
        if (null == propertyDetail.getSitalArea().getArea())
            addActionError(getText("mandatory.vacantLandArea"));
        final Date effDate = propertyTaxUtil.getEffectiveDateForProperty();
        if (null == propertyDetail.getDateOfCompletion() || "".equals(propertyDetail.getDateOfCompletion()))
            addActionError(getText("mandatory.dtOfCmpln"));
        else if (propertyDetail.getDateOfCompletion().before(effDate))
            addActionError(getText("vacant.effectiveDate.before.6inst"));

        if (null == propertyDetail.getCurrentCapitalValue())
            addActionError(getText("mandatory.capitalValue"));
        if (null == propertyDetail.getMarketValue())
            addActionError(getText("mandatory.marketValue"));
        if (propertyDetail.getCurrentCapitalValue() != null
                && propertyDetail.getCurrentCapitalValue() < Double
                        .parseDouble(VACANTLAND_MIN_CUR_CAPITALVALUE))
            addActionError(getText("minvalue.capitalValue"));
        if (isBlank(eastBoundary))
            addActionError(getText("mandatory.eastBoundary"));
        if (isBlank(westBoundary))
            addActionError(getText("mandatory.westBoundary"));
        if (isBlank(southBoundary))
            addActionError(getText("mandatory.southBoundary"));
        if (isBlank(northBoundary))
            addActionError(getText("mandatory.northBoundary"));
        if (vacantLandPlotAreaId == null || Long.valueOf(-1).equals(vacantLandPlotAreaId))
            addActionError(getText("mandatory.vacanland.plotarea"));
        if (layoutApprovalAuthorityId == null || Long.valueOf(-1).equals(layoutApprovalAuthorityId))
            addActionError(getText("mandatory.layout.authority"));
        if (!(layoutApprovalAuthorityId == null || Long.valueOf(-1).equals(layoutApprovalAuthorityId)) && !"No Approval"
                .equals(layoutApprovalAuthorityRepo.findOne(layoutApprovalAuthorityId).getName())) {
            if (isBlank(propertyDetail.getLayoutPermitNo()))
                addActionError(getText("mandatory.layout.permitno"));
            if (propertyDetail.getLayoutPermitDate() == null)
                addActionError(getText("mandatory.layout.permitdate"));
        }
        if (null != modifyRsn && null != propCompletionDate)
            if (null != propCompletionDate && propertyDetail.getDateOfCompletion() != null)
                if (!DateUtils.compareDates(propertyDetail.getDateOfCompletion(), propCompletionDate))
                    addActionError(getText("modify.vacant.completiondate.validate"));

        if (logger.isDebugEnabled())
            logger.debug("Exiting from validateVacantProperty");

    }

    public void validateBuiltUpProperty(final PropertyDetail propertyDetail, final Long floorTypeId,
            final Long roofTypeId, final String areaOfPlot, final Date regDocDate, final String modifyRsn) {
    	
    	final Date propCompletionDate = propertyService.getLowestDtOfCompFloorWise(propertyDetail.getFloorDetailsProxy());
        
    	if (logger.isDebugEnabled())
            logger.debug("Eneterd into validateBuiltUpProperty");

        if (TRUE.equals(propertyDetail.isAppurtenantLandChecked()) && null == propertyDetail.getExtentAppartenauntLand())
            addActionError(getText("mandatory.extentAppartnant"));
        else if (FALSE.equals(propertyDetail.isAppurtenantLandChecked()) && isBlank(areaOfPlot))
            addActionError(getText("mandatory.extentsite"));
        else if (FALSE.equals(propertyDetail.isAppurtenantLandChecked())
                && ("".equals(areaOfPlot) || Double.valueOf(areaOfPlot) == 0))
            addActionError(getText("mandatory.extentsite.greaterthanzero"));
        if (propertyDetail.getOccupancyCertificationDate() != null && propCompletionDate != null
                && propertyDetail.getOccupancyCertificationDate().before(propCompletionDate))
            addActionError(getText("occupancydate.before.constrDate.error"));

        if (logger.isDebugEnabled())
            logger.debug("Exiting from validateBuiltUpProperty");
    }

    public void validateFloor(final PropertyTypeMaster propTypeMstr, final List<Floor> floorList,
            final Property property, final String areaOfPlot, final Date regDocDate, final String modifyRsn,
            final Date propCompletionDate) {
        boolean buildingPlanNoValidationAdded;
        boolean buildingPlanDateValidationAdded;
        boolean buildingPlanPlinthAreaValidationAdded;
        if (logger.isDebugEnabled())
            logger.debug("Entered into validateFloor \nPropertyTypeMaster:" + propTypeMstr + ", No of floors: "
                    + (floorList != null ? floorList : ZERO));

        if (!propTypeMstr.getCode().equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND))
            if (floorList != null && !floorList.isEmpty())
                for (final Floor floor : floorList) {
                    List<String> msgParams;
                    if (floor != null) {
                        buildingPlanNoValidationAdded = false;
                        buildingPlanDateValidationAdded = false;
                        buildingPlanPlinthAreaValidationAdded = false;
                        msgParams = new ArrayList<>();
                        if (floor.getFloorNo() == null || floor.getFloorNo().equals(-10))
                            addActionError(getText("mandatory.floorNO"));
                        msgParams.add(floor.getFloorNo() != null ? FLOOR_MAP.get(floor.getFloorNo()) : "N/A");

                        if (floor.getStructureClassification() == null
                                || floor.getStructureClassification().getId() == null
                                || "-1".equals(floor.getStructureClassification().getId().toString()))
                            addActionError(getText("mandatory.constType", msgParams));
                        
                        if (!floor.getUnstructuredLand()) {
                            if (floor.getBuiltUpArea() == null || floor.getBuiltUpArea().getLength() == null
                                    || "".equals(floor.getBuiltUpArea().getLength()))
                                addActionError(getText("mandatory.assbleLength", msgParams));
                            if (floor.getBuiltUpArea() == null || floor.getBuiltUpArea().getBreadth() == null
                                    || "".equals(floor.getBuiltUpArea().getBreadth()))
                                addActionError(getText("mandatory.assbleWidth", msgParams));
                        }

                        if (floor.getPropertyUsage() == null || null == floor.getPropertyUsage().getId()
                                || "-1".equals(floor.getPropertyUsage().getId().toString()))
                            addActionError(getText("mandatory.floor.usage", msgParams));

                        if (StringUtils.isNotBlank(floor.getBuildingPermissionNo())) {
                            if (floor.getBuildingPermissionDate() == null) {
                                addActionError(getText("mandatory.floor.buildingplan.date", msgParams));
                                buildingPlanDateValidationAdded = true;
                            }
                            if (floor.getBuildingPlanPlinthArea().getArea() == null) {
                                addActionError(getText("mandatory.floor.buildingplan.plintharea", msgParams));
                                buildingPlanPlinthAreaValidationAdded = true;
                            }
                        }
                        if (floor.getBuildingPermissionDate() != null) {
                            if (StringUtils.isBlank(floor.getBuildingPermissionNo())) {
                                addActionError(getText("mandatory.floor.buildingplan.number", msgParams));
                                buildingPlanNoValidationAdded = true;
                            }
                            if (floor.getBuildingPlanPlinthArea().getArea() == null)
                                if (!buildingPlanPlinthAreaValidationAdded)
                                    addActionError(getText("mandatory.floor.buildingplan.plintharea", msgParams));
                        }
                        if (floor.getBuildingPlanPlinthArea().getArea() != null) {
                            if (floor.getBuildingPermissionDate() == null)
                                if (!buildingPlanDateValidationAdded)
                                    addActionError(getText("mandatory.floor.buildingplan.date", msgParams));
                            if (StringUtils.isBlank(floor.getBuildingPermissionNo()))
                                if (!buildingPlanNoValidationAdded)
                                    addActionError(getText("mandatory.floor.buildingplan.number", msgParams));
                        }

                        if (floor.getFirmName() == null || floor.getFirmName().isEmpty()
                                || "".equals(floor.getFirmName()))
                            if (floor.getPropertyUsage() != null && null != floor.getPropertyUsage().getId()
                                    && !"-1".equals(floor.getPropertyUsage().getId().toString())) {
                                final PropertyUsage pu = propertyUsageService.findById(Long.valueOf(floor
                                        .getPropertyUsage().getId()));
                                if (pu != null && !pu.getUsageName().equalsIgnoreCase(NATURE_OF_USAGE_RESIDENCE))
                                    addActionError(getText("mandatory.floor.firmName", msgParams));
                            }

                        if (floor.getPropertyOccupation() == null || null == floor.getPropertyOccupation().getId()
                                || "-1".equals(floor.getPropertyOccupation().getId().toString()))
                            addActionError(getText("mandatory.floor.occ"));

                        if (floor.getConstructionDate() == null || "".equals(floor.getConstructionDate()))
                            addActionError(getText("mandatory.floor.constrDate"));

                        final Date effDate = propertyTaxUtil.getEffectiveDateForProperty();
                        if (floor.getOccupancyDate() == null || "".equals(floor.getOccupancyDate()))
                            addActionError(getText("mandatory.floor.docOcc"));
                        if (floor.getOccupancyDate() != null && !"".equals(floor.getOccupancyDate())) {
                            if (floor.getOccupancyDate().after(new Date()))
                                addActionError(getText("mandatory.dtFlrBeforeCurr"));
                            if (floor.getOccupancyDate().before(effDate))
                                addActionError(getText("constrDate.before.6inst", msgParams));
                        }

                        if (floor.getOccupancyDate() != null && floor.getConstructionDate() != null
                                && floor.getOccupancyDate().before(floor.getConstructionDate()))
                            addActionError(getText("effectiveDate.before.constrDate.error"));

                        if (floor.getBuiltUpArea() == null || floor.getBuiltUpArea().getArea() == null
                                || "".equals(floor.getBuiltUpArea().getArea()))
                            addActionError(getText("mandatory.assbleArea"));
                        else if (StringUtils.isNotBlank(areaOfPlot)
                                && floor.getBuiltUpArea().getArea() > Double.valueOf(areaOfPlot))
                            addActionError(getText("assbleArea.notgreaterthan.extentsite"));

                        if (modifyRsn == null
                                || modifyRsn != null && !modifyRsn.equals(PROPERTY_MODIFY_REASON_ADD_OR_ALTER) && !modifyRsn
                                        .equals(PROPERTY_MODIFY_REASON_BIFURCATE))
                            if (null != regDocDate && null != floor.getOccupancyDate()
                                    && !"".equals(floor.getOccupancyDate()))
                                if (DateUtils.compareDates(regDocDate, floor.getOccupancyDate()))
                                    addActionError(getText("regDate.notgreaterthan.occDate", msgParams));
                        if (null != modifyRsn && null != propCompletionDate)
                            if (null != propCompletionDate && floor.getOccupancyDate() != null
                                    && !"".equals(floor.getOccupancyDate()))
                                if (!DateUtils.compareDates(floor.getOccupancyDate(), propCompletionDate))
                                    addActionError(getText("modify.builtup.occDate.validate", msgParams));

                    }
                }
        if (logger.isDebugEnabled())
            logger.debug("Exiting from validate");
    }

    /**
     * Validates house number,assuming house number should be unique across ward boundary
     *
     * @param wardId
     * @param houseNo
     * @param basicProperty
     */
    protected void validateHouseNumber(final Long wardId, final String houseNo, final BasicProperty basicProperty) {
        final Query qry = getPersistenceService()
                .getSession()
                .createQuery(
                        "from BasicPropertyImpl bp where bp.address.houseNoBldgApt = :houseNo and bp.boundary.id = :wardId and bp.active = 'Y'");
        qry.setParameter("houseNo", houseNo);
        qry.setParameter("wardId", wardId);
        // this condition is reqd bcoz, after rejection the validation shouldn't
        // happen for the same houseNo
        if (!qry.list().isEmpty()
                && (basicProperty == null || !basicProperty.getAddress().getHouseNoBldgApt().equals(houseNo)))
            addActionError(getText("houseNo.unique"));
    }

    /**
     * Get Designation for logged in user
     */
    public void setUserInfo() {
        if (logger.isDebugEnabled())
            logger.debug("Entered into setUserInfo");

        final Long userId = securityUtils.getCurrentUser().getId();
        if (logger.isDebugEnabled())
            logger.debug("setUserInfo: Logged in userId" + userId);
        final Designation designation = propertyTaxUtil.getDesignationForUser(userId);
        if (designation != null)
            setUserDesgn(designation.getName());
        if (logger.isDebugEnabled())
            logger.debug("Exit from setUserInfo");
    }

    /**
     * Get a String for All the Designations for logged in user
     */
    public void setUserDesignations() {
        final Long userId = securityUtils.getCurrentUser().getId();
        final String designations = propertyTaxCommonUtils.getAllDesignationsForUser(userId);
        setUserDesignationList(designations);
    }

    /**
     * Workflow for new and addition/alteration of assessment
     *
     * @param property
     */
    public void transitionWorkFlow(final PropertyImpl property) {
        Assignment wfInitiator = null;
        String approverDesignation = "";

        final Assignment assignment = getApproverAssignment(property);
        if (assignment != null) {
            approverDesignation = assignment.getDesignation().getName();
            if (!propertyByEmployee || ANONYMOUS_USER.equalsIgnoreCase(securityUtils.getCurrentUser().getName())
                    || propertyService.isCitizenPortalUser(securityUtils.getCurrentUser()))
                wfInitiator = assignment;
        }
        if (property.getId() != null)
            wfInitiator = propertyService.getWorkflowInitiator(property);
        else if (wfInitiator == null)
            wfInitiator = propertyTaxCommonUtils.getWorkflowInitiatorAssignment(securityUtils.getCurrentUser().getId());

        String loggedInUserDesignation = getDesignationByPositionAndLoggedInUser(property);

        if (JUNIOR_ASSISTANT.equals(loggedInUserDesignation) || SENIOR_ASSISTANT.equals(loggedInUserDesignation))
            loggedInUserDesignation = null;

        final String nature = getNatureOfTasks().get(getAdditionalRule());

        if (WFLOW_ACTION_STEP_REJECT.equalsIgnoreCase(workFlowAction))
            transitionReject(property, wfInitiator, approverDesignation, loggedInUserDesignation);
        else
            transition(property, wfInitiator, nature, approverDesignation, loggedInUserDesignation);
        prepareAckMessage();
        if (logger.isDebugEnabled())
            logger.debug("Exiting method : transitionWorkFlow");
    }

    /**
     * @param property
     * @return
     */
    private Assignment getApproverAssignment(final PropertyImpl property) {
        Assignment assignment = null;
        if (!propertyByEmployee || ANONYMOUS_USER.equalsIgnoreCase(securityUtils.getCurrentUser().getName())
                || propertyService.isCitizenPortalUser(securityUtils.getCurrentUser())) {
            currentState = "Created";
            if (propertyService.isCscOperator(securityUtils.getCurrentUser()))
                assignment = propertyService.getMappedAssignmentForCscOperator(property.getBasicProperty());
            else
                assignment = propertyService.getUserPositionByZone(property.getBasicProperty(), false);
            if (null != assignment) {
                approverPositionId = assignment.getPosition().getId();
                approverName = assignment.getEmployee().getName().concat("~").concat(
                        assignment.getPosition().getName());
            }
        } else {
            currentState = null;
            if (null != approverPositionId && approverPositionId != -1) {
                assignment = assignmentService.getAssignmentsForPosition(approverPositionId, new Date())
                        .get(0);
                approverName = assignment.getEmployee().getName().concat("~")
                        .concat(assignment.getPosition().getName());
            }
        }
        return assignment;
    }

    /**
     * @param property
     * @param approverDesignation
     * @return
     */
    private String getNextAction(final PropertyImpl property, final String approverDesignation) {
        String nextAction = "";
        if (WFLOW_ACTION_STEP_FORWARD.equalsIgnoreCase(workFlowAction)
                && COMMISSIONER_DESIGNATIONS.contains(approverDesignation))
            if (property.getCurrentState().getNextAction().equalsIgnoreCase(WF_STATE_DIGITAL_SIGNATURE_PENDING))
                nextAction = WF_STATE_DIGITAL_SIGNATURE_PENDING;
            else {
                final String designation = approverDesignation.split(" ")[0];
                if (designation.equalsIgnoreCase(COMMISSIONER_DESGN))
                    nextAction = WF_STATE_COMMISSIONER_APPROVAL_PENDING;
                else
                    nextAction = new StringBuilder().append(designation).append(" ")
                            .append(WF_STATE_COMMISSIONER_APPROVAL_PENDING)
                            .toString();
            }
        return nextAction;
    }

    /**
     * @param property
     * @param wfInitiator
     * @param nature
     * @param approverDesignation
     * @param loggedInUserDesignation
     */
    private void transition(final PropertyImpl property, final Assignment wfInitiator,
            final String nature, final String approverDesignation, final String loggedInUserDesignation) {
        final DateTime currentDate = new DateTime();
        final User user = securityUtils.getCurrentUser();
        Position pos;
        WorkFlowMatrix wfmatrix;
        if (WFLOW_ACTION_STEP_APPROVE.equalsIgnoreCase(workFlowAction))
            pos = property.getCurrentState().getOwnerPosition();
        else if (null != approverPositionId && approverPositionId != -1)
            pos = (Position) persistenceService.find("from Position where id=?", approverPositionId);
        else
            pos = wfInitiator.getPosition();

        if (property.getState() == null) {
            wfmatrix = propertyWorkflowService.getWfMatrix(property.getStateType(), null,
                    null, getAdditionalRule(), currentState, null);
            property.transition().start().withSenderName(user.getUsername() + "::" + user.getName())
                    .withComments(approverComments).withStateValue(wfmatrix.getNextState())
                    .withDateInfo(currentDate.toDate()).withOwner(pos).withNextAction(wfmatrix.getNextAction())
                    .withNatureOfTask(nature).withInitiator(wfInitiator != null ? wfInitiator.getPosition() : null);
        } else if (property.getCurrentState().getNextAction().equalsIgnoreCase(END))
            property.transition().end().withSenderName(user.getUsername() + "::" + user.getName())
                    .withComments(approverComments).withDateInfo(currentDate.toDate()).withNextAction(null);
        else {
            final String nextAction = getNextAction(property, approverDesignation);
            wfmatrix = propertyWorkflowService.getWfMatrix(property.getStateType(), null,
                    null, getAdditionalRule(), property.getCurrentState().getValue(),
                    property.getState().getNextAction(), null, loggedInUserDesignation);
            property.transition().progressWithStateCopy().withSenderName(user.getUsername() + "::" + user.getName())
                    .withComments(approverComments)
                    .withStateValue(wfmatrix.getNextState())
                    .withDateInfo(currentDate.toDate()).withOwner(pos)
                    .withNextAction(StringUtils.isNotBlank(nextAction) ? nextAction : wfmatrix.getNextAction());
        }
    }

    /**
     *
     */
    private void prepareAckMessage() {
        if (approverName != null && !approverName.isEmpty() && !approverName.equalsIgnoreCase(CHOOSE)) {
            final String approvalmesg = " Succesfully Forwarded to : ";
            ackMessage = ackMessage == null ? approvalmesg : ackMessage + approvalmesg;
        } else if (workFlowAction != null && workFlowAction.equalsIgnoreCase(CANCEL)) {
            final String approvalmesg = " Succesfully Cancelled.";
            ackMessage = ackMessage == null ? approvalmesg : ackMessage + approvalmesg;
        }
    }

    /**
     * @param property
     * @param wfInitiator
     * @param approverDesignation
     * @param loggedInUserDesignation
     */
    private void transitionReject(final PropertyImpl property, final Assignment wfInitiator, final String approverDesignation,
            final String loggedInUserDesignation) {
        final DateTime currentDate = new DateTime();
        final User user = securityUtils.getCurrentUser();
        Position owner = null;
        if (wfInitiator.getPosition().equals(property.getState().getOwnerPosition())) {
            property.transition().end().withSenderName(user.getUsername() + "::" + user.getName())
                    .withComments(approverComments).withDateInfo(currentDate.toDate()).withNextAction(null).withOwner((Position)null);
            property.setStatus(STATUS_CANCELLED);
            property.getBasicProperty().setUnderWorkflow(FALSE);
        } else {
            String nextAction = getNextAction(property, approverDesignation);
            if (REVENUE_OFFICER_DESGN.equalsIgnoreCase(loggedInUserDesignation)
                    || COMMISSIONER_DESIGNATIONS.contains(loggedInUserDesignation)) {
                nextAction = UD_REVENUE_INSPECTOR_APPROVAL_PENDING;
                final Assignment assignmentOnreject = propertyService.getUserOnRejection(property);
                owner = assignmentOnreject.getPosition();
                setInitiator(assignmentOnreject.getEmployee().getName().concat("~")
                        .concat(assignmentOnreject.getPosition().getName()));
            } else if (BILL_COLLECTOR_DESGN.equalsIgnoreCase(loggedInUserDesignation)
                    || TAX_COLLECTOR_DESGN.equalsIgnoreCase(loggedInUserDesignation)
                    || REVENUE_INSPECTOR_DESGN.equalsIgnoreCase(loggedInUserDesignation)) {
                nextAction = WF_STATE_ASSISTANT_APPROVAL_PENDING;
                setInitiator(wfInitiator.getEmployee().getName().concat("~")
                        .concat(wfInitiator.getPosition().getName()));
            }

            if (owner == null)
                owner = wfInitiator.getPosition();

            final String stateValue = property.getCurrentState().getValue().split(":")[0] + ":" + WF_STATE_REJECTED;
            property.transition().progressWithStateCopy().withSenderName(user.getUsername() + "::" + user.getName())
                    .withComments(approverComments).withStateValue(stateValue).withDateInfo(currentDate.toDate())
                    .withOwner(owner).withNextAction(
                            property.getBasicProperty().getSource().equals(SOURCEOFDATA_MOBILE)
                                    ? UD_REVENUE_INSPECTOR_APPROVAL_PENDING
                                    : nextAction);
        }
    }

    /**
     * @param property
     * @param user
     * @return
     */
    private String getDesignationByPositionAndLoggedInUser(final PropertyImpl property) {
        List<Assignment> assignments;
        String designation = "";
        if (property.getState() != null) {
            assignments = assignmentService.getAssignmentByPositionAndUserAsOnDate(
                    property.getCurrentState().getOwnerPosition().getId(), securityUtils.getCurrentUser().getId(), new Date());
            if (!assignments.isEmpty())
                designation = assignments.get(0).getDesignation().getName();
        }
        return designation;
    }

    private Map<String, String> getNatureOfTasks() {
        final Map<String, String> natureOfTasks = new HashMap<>();
        natureOfTasks.put(NEW_ASSESSMENT, NATURE_NEW_ASSESSMENT);
        natureOfTasks.put(ADDTIONAL_RULE_ALTER_ASSESSMENT, NATURE_ALTERATION);
        natureOfTasks.put(ADDTIONAL_RULE_BIFURCATE_ASSESSMENT, NATURE_BIFURCATION);
        natureOfTasks.put(DEMOLITION, NATURE_DEMOLITION);
        natureOfTasks.put(EXEMPTION, NATURE_TAX_EXEMPTION);
        natureOfTasks.put(AMALGAMATION, NATURE_AMALGAMATION);
        return natureOfTasks;
    }

    public void validateApproverDetails() {
        if (WFLOW_ACTION_STEP_FORWARD.equals(workFlowAction) || WFLOW_ACTION_STEP_SAVE.equals(workFlowAction))
            if (null != approverPositionId && approverPositionId == -1)
                addActionError(getText("property.workflow.approver.errormessage"));
        if (WFLOW_ACTION_STEP_REJECT.equals(workFlowAction) && isBlank(approverComments))
            addActionError(getText("property.workflow.remarks"));
    }

    /**
     * Build SMS and Email for new and addition/alteration assessment
     *
     * @param property
     * @param applicationType
     */
    public void buildEmailandSms(final PropertyImpl property, final String applicationType) {
        for (final PropertyOwnerInfo ownerInfo : property.getBasicProperty().getPropertyOwnerInfo())
            buildEmailAndSms(property, ownerInfo.getOwner(), applicationType);
    }

    private void buildEmailAndSms(final PropertyImpl property, final User user, final String applicationType) {
        final String mobileNumber = user.getMobileNumber();
        final String emailid = user.getEmailId();
        final String applicantName = user.getName();
        final List<String> args = new ArrayList<>();
        args.add(applicantName);
        String smsMsg = "";
        String emailSubject = "";
        String emailBody = "";

        final Map<String, BigDecimal> demandCollMap = ptDemandDAO.getDemandCollMap(property);
        if (null != property && null != property.getState()) {
            final State propertyState = property.getState();
            if (propertyState.getValue().endsWith(WF_STATE_ASSISTANT_APPROVED)
                    || propertyState.getValue().endsWith("NEW")) {
                args.add(property.getApplicationNo());
                if (APPLICATION_TYPE_NEW_ASSESSENT.equals(applicationType)) {
                    if (mobileNumber != null)
                        smsMsg = getText("msg.newpropertycreate.sms", args);
                    if (emailid != null) {
                        args.add(ApplicationThreadLocals.getMunicipalityName());
                        emailSubject = getText("msg.newpropertycreate.email.subject",
                                new String[] { property.getApplicationNo() });
                        emailBody = getText("msg.newpropertycreate.email", args);
                    }
                } else if (ALTERATION_OF_ASSESSMENT.equals(applicationType)
                        || APPLICATION_TYPE_ALTER_ASSESSENT.equals(applicationType)) {

                    if (mobileNumber != null)
                        smsMsg = getText("msg.alterAssessmentForward.sms", args);
                    if (emailid != null) {
                        args.add(ApplicationThreadLocals.getMunicipalityName());
                        emailSubject = getText("msg.alterAssessmentForward.email.subject",
                                new String[] { property.getApplicationNo() });
                        emailBody = getText("msg.alterAssessmentForward.email", args);
                    }
                } else if (APPLICATION_TYPE_GRP.equals(applicationType)) {
                    smsMsg = getText("msg.grpcreate.sms", args);
                    args.add(ApplicationThreadLocals.getMunicipalityName());
                    emailSubject = getText("msg.grpcreate.email.subject", new String[] { property.getApplicationNo() });
                    emailBody = getText("msg.grpcreate.email", args);

                }
            } else if (propertyState.getValue().endsWith(WF_STATE_REJECTED)) {
                args.add(property.getApplicationNo());
                args.add(ApplicationThreadLocals.getMunicipalityName());
                if (APPLICATION_TYPE_NEW_ASSESSENT.equals(applicationType)) {
                    if (mobileNumber != null)
                        smsMsg = getText("msg.newpropertyreject.sms", args);
                    if (emailid != null) {
                        emailSubject = getText("msg.newpropertyreject.email.subject",
                                new String[] { property.getApplicationNo() });
                        emailBody = getText("msg.newpropertyreject.email", args);
                    }
                } else if (ALTERATION_OF_ASSESSMENT.equals(applicationType)
                        || APPLICATION_TYPE_ALTER_ASSESSENT.equals(applicationType)) {
                    if (mobileNumber != null)
                        smsMsg = getText("msg.alterAssessmentReject.sms", args);

                    if (emailid != null) {
                        emailSubject = getText("msg.alterAssessmentReject.email.subject",
                                new String[] { property.getApplicationNo() });
                        emailBody = getText("msg.alterAssessmentReject.email", args);
                    }
                } else if (APPLICATION_TYPE_GRP.equals(applicationType)) {
                    smsMsg = getText("msg.grpreject.sms", args);
                    emailSubject = getText("msg.grpreject.email.subject", new String[] { property.getApplicationNo() });
                    emailBody = getText("msg.grpreject.email", args);

                }
            } else if (propertyState.getValue().endsWith(WF_STATE_COMMISSIONER_APPROVED)) {
                args.add(property.getBasicProperty().getUpicNo());
                final Map<String, Installment> installmentMap = propertyTaxUtil.getInstallmentsForCurrYear(new Date());
                final Installment installmentFirstHalf = installmentMap.get(CURRENTYEAR_FIRST_HALF);
                args.add(demandCollMap
                        .get(DateUtils.between(new Date(), installmentFirstHalf.getFromDate(), installmentFirstHalf.getToDate())
                                ? CURR_FIRSTHALF_DMD_STR : CURR_SECONDHALF_DMD_STR)
                        .toString());
                args.add(DateUtils.getFormattedDate(property.getBasicProperty().getPropOccupationDate(), "dd/MM/yyyy"));
                args.add(ApplicationThreadLocals.getMunicipalityName());
                if (APPLICATION_TYPE_NEW_ASSESSENT.equals(applicationType)) {
                    if (mobileNumber != null)
                        smsMsg = getText("msg.newpropertyapprove.sms", args);
                    if (emailid != null) {
                        emailSubject = getText("msg.newpropertyapprove.email.subject", new String[] { property
                                .getBasicProperty().getUpicNo() });
                        emailBody = getText("msg.newpropertyapprove.email", args);
                    }
                } else if (ALTERATION_OF_ASSESSMENT.equals(applicationType)
                        || APPLICATION_TYPE_ALTER_ASSESSENT.equals(applicationType)) {
                    if (mobileNumber != null)
                        smsMsg = getText("msg.alterAssessmentApprove.sms", args);
                    if (emailid != null) {
                        emailSubject = getText("msg.alterAssessmentApprove.email.subject");
                        emailBody = getText("msg.alterAssessmentApprove.email", args);
                    }
                } else if (APPLICATION_TYPE_GRP.equals(applicationType)) {
                    smsMsg = getText("msg.grpapprove.sms", args);
                    emailSubject = getText("msg.grpapprove.email.subject", new String[] { property.getApplicationNo() });
                    emailBody = getText("msg.grpapprove.email", args);

                }
            }
        }
        if (StringUtils.isNotBlank(mobileNumber) && StringUtils.isNotBlank(smsMsg))
            notificationService.sendSMS(mobileNumber, smsMsg);
        if (StringUtils.isNotBlank(emailid) && StringUtils.isNotBlank(emailSubject) && StringUtils.isNotBlank(emailBody))
            notificationService.sendEmail(emailid, emailSubject, emailBody);

    }

    public void preparePropertyTaxDetails(final Property property) {
        final Map<String, Installment> installmentMap = propertyTaxUtil.getInstallmentsForCurrYear(new Date());
        final Installment installmentFirstHalf = installmentMap.get(CURRENTYEAR_FIRST_HALF);
        final Installment installmentSecondHalf = installmentMap.get(CURRENTYEAR_SECOND_HALF);
        Map<String, BigDecimal> demandCollMap = null;
        // Based on the current date, the tax details will be fetched for the respective installment
        if (DateUtils.between(new Date(), installmentFirstHalf.getFromDate(), installmentFirstHalf.getToDate()))
            demandCollMap = propertyTaxUtil.prepareDemandDetForWorkflowProperty(property, installmentFirstHalf,
                    installmentFirstHalf);
        else if (DateUtils.between(new Date(), installmentSecondHalf.getFromDate(), installmentSecondHalf.getToDate()))
            demandCollMap = propertyTaxUtil.prepareDemandDetForWorkflowProperty(property, installmentFirstHalf,
                    installmentSecondHalf);

        final Ptdemand ptDemand = ptDemandDAO.getNonHistoryCurrDmdForProperty(property);
        if (null != ptDemand && ptDemand.getDmdCalculations() != null && ptDemand.getDmdCalculations().getAlv() != null)
            propertyTaxDetailsMap.put("ARV", ptDemand.getDmdCalculations().getAlv());
        else
            propertyTaxDetailsMap.put("ARV", BigDecimal.ZERO);

        propertyTaxDetailsMap.put(
                "eduCess",
                demandCollMap.get(DEMANDRSN_STR_EDUCATIONAL_CESS) == null ? BigDecimal.ZERO : demandCollMap
                        .get(DEMANDRSN_STR_EDUCATIONAL_CESS));
        propertyTaxDetailsMap.put(
                "libraryCess",
                demandCollMap.get(DEMANDRSN_STR_LIBRARY_CESS) == null ? BigDecimal.ZERO : demandCollMap
                        .get(DEMANDRSN_STR_LIBRARY_CESS));
        BigDecimal totalTax;
        if (!property.getPropertyDetail().getPropertyTypeMaster().getCode().equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND)) {
            propertyTaxDetailsMap.put("generalTax",
                    demandCollMap.get(DEMANDRSN_STR_GENERAL_TAX) == null ? BigDecimal.ZERO : demandCollMap
                            .get(DEMANDRSN_STR_GENERAL_TAX));
            totalTax = (demandCollMap.get(DEMANDRSN_STR_GENERAL_TAX) == null ? BigDecimal.ZERO : demandCollMap
                    .get(DEMANDRSN_STR_GENERAL_TAX))
                            .add(demandCollMap.get(DEMANDRSN_STR_EDUCATIONAL_CESS) == null ? BigDecimal.ZERO : demandCollMap
                                    .get(DEMANDRSN_STR_EDUCATIONAL_CESS))
                            .add(demandCollMap.get(DEMANDRSN_STR_LIBRARY_CESS) == null ? BigDecimal.ZERO : demandCollMap
                                    .get(DEMANDRSN_STR_LIBRARY_CESS));
            // If unauthorized property, then add unauthorized penalty
            if (demandCollMap.get(DEMANDRSN_STR_UNAUTHORIZED_PENALTY) != null) {
                propertyTaxDetailsMap.put(UNAUTHORISED_PENALTY, demandCollMap.get(DEMANDRSN_STR_UNAUTHORIZED_PENALTY));
                propertyTaxDetailsMap.put(TOTAL_TAX,
                        totalTax.add(demandCollMap.get(DEMANDRSN_STR_UNAUTHORIZED_PENALTY)));
            } else
                propertyTaxDetailsMap.put(TOTAL_TAX, totalTax);

        } else {
            propertyTaxDetailsMap.put("vacantLandTax",
                    demandCollMap.get(DEMANDRSN_STR_VACANT_TAX) == null ? BigDecimal.ZERO
                            : demandCollMap.get(DEMANDRSN_STR_VACANT_TAX));
            totalTax = (demandCollMap.get(DEMANDRSN_STR_VACANT_TAX) == null ? BigDecimal.ZERO : demandCollMap
                    .get(DEMANDRSN_STR_VACANT_TAX)).add(
                            demandCollMap.get(DEMANDRSN_STR_LIBRARY_CESS) == null ? BigDecimal.ZERO : demandCollMap
                                    .get(DEMANDRSN_STR_LIBRARY_CESS));
            if (demandCollMap.get(DEMANDRSN_STR_UNAUTHORIZED_PENALTY) != null) {
                propertyTaxDetailsMap.put(UNAUTHORISED_PENALTY, demandCollMap.get(DEMANDRSN_STR_UNAUTHORIZED_PENALTY));
                propertyTaxDetailsMap.put(TOTAL_TAX,
                        totalTax.add(demandCollMap.get(DEMANDRSN_STR_UNAUTHORIZED_PENALTY)));
            } else
                propertyTaxDetailsMap.put(TOTAL_TAX, totalTax);
            propertyTaxDetailsMap.put(TOTAL_TAX, totalTax);
        }
    }

    public void validateDocumentDetails(final DocumentTypeDetails documentTypeDetails) {
        if (documentTypeDetails.getDocumentName() == null || "-1".equals(documentTypeDetails.getDocumentName()))
            addActionError(getText("mandatory.doctype"));
        else {
            validateCertificateDocDetails(documentTypeDetails);
            validateWillDocDetails(documentTypeDetails);
            validateDecreeDocDetails(documentTypeDetails);
            validateRegisteredDocDetails(documentTypeDetails);
            if (PropertyTaxConstants.DOCUMENT_NAME_PATTA_CERTIFICATE.equals(documentTypeDetails.getDocumentName())
                    && documentTypeDetails.getProceedingNo().isEmpty())
                addActionError(getText("mandatory.dtd.procno"));
            if (PropertyTaxConstants.DOCUMENT_NAME_PATTA_CERTIFICATE.equals(documentTypeDetails.getDocumentName())
                    && documentTypeDetails.getProceedingDate() == null)
                addActionError(getText("mandatory.dtd.procdate"));
            if (PropertyTaxConstants.DOCUMENT_NAME_DECREE_BY_CIVILCOURT.equals(documentTypeDetails.getDocumentName())
                    && documentTypeDetails.getCourtName().isEmpty())
                addActionError(getText("mandatory.dtd.courtname"));
        }
    }

    public void validateCertificateDocDetails(final DocumentTypeDetails documentTypeDetails) {
        if (PropertyTaxConstants.DOCUMENT_NAME_PATTA_CERTIFICATE.equals(documentTypeDetails.getDocumentName())
                && documentTypeDetails.getDocumentNo().isEmpty())
            addActionError(getText("mandatory.dtd.certificate.no"));
        if (PropertyTaxConstants.DOCUMENT_NAME_PATTA_CERTIFICATE.equals(documentTypeDetails.getDocumentName())
                && documentTypeDetails.getDocumentDate() == null)
            addActionError(getText("mandatory.dtd.certificate.date"));
    }

    public void validateWillDocDetails(final DocumentTypeDetails documentTypeDetails) {
        if (checkWillDocDetails(documentTypeDetails) && documentTypeDetails.getDocumentNo().isEmpty())
            addActionError(getText("mandatory.dtd.deed.no"));
        if (checkWillDocDetails(documentTypeDetails) && documentTypeDetails.getDocumentDate() == null)
            addActionError(getText("mandatory.dtd.deed.date"));
    }

    public void validateDecreeDocDetails(final DocumentTypeDetails documentTypeDetails) {
        if (PropertyTaxConstants.DOCUMENT_NAME_DECREE_BY_CIVILCOURT.equals(documentTypeDetails.getDocumentName())
                && documentTypeDetails.getDocumentNo().isEmpty())
            addActionError(getText("mandatory.dtd.decree.no"));
        if (PropertyTaxConstants.DOCUMENT_NAME_DECREE_BY_CIVILCOURT.equals(documentTypeDetails.getDocumentName())
                && documentTypeDetails.getDocumentDate() == null)
            addActionError(getText("mandatory.dtd.decree.date"));
    }

    public void validateRegisteredDocDetails(final DocumentTypeDetails documentTypeDetails) {
        if (PropertyTaxConstants.DOCUMENT_NAME_REGD_DOCUMENT.equals(documentTypeDetails.getDocumentName())
                && documentTypeDetails.getDocumentNo().isEmpty())
            addActionError(getText("mandatory.dtd.registered.no"));
        if (PropertyTaxConstants.DOCUMENT_NAME_REGD_DOCUMENT.equals(documentTypeDetails.getDocumentName())
                && documentTypeDetails.getDocumentDate() == null)
            addActionError(getText("mandatory.dtd.registered.date"));
    }

    public Boolean checkWillDocDetails(final DocumentTypeDetails documentTypeDetails) {
        return PropertyTaxConstants.DOCUMENT_NAME_REGD_WILL_DOCUMENT.equals(documentTypeDetails.getDocumentName())
                || PropertyTaxConstants.DOCUMENT_NAME_UNREGD_WILL_DOCUMENT.equals(documentTypeDetails.getDocumentName());
    }
    
    public void populateUsages(String propertyCategory) {
        List<PropertyUsage> usageList = propertyUsageService.getAllActiveMixedPropertyUsages();
        // Loading property usages based on property category
        if (StringUtils.isNoneBlank(propertyCategory))
                if (propertyCategory.equals(CATEGORY_MIXED))
                        usageList = propertyUsageService.getAllActiveMixedPropertyUsages();
                else if (propertyCategory.equals(CATEGORY_RESIDENTIAL))
                        usageList = propertyUsageService.getResidentialPropertyUsages();
                else if (propertyCategory.equals(CATEGORY_NON_RESIDENTIAL))
                        usageList = propertyUsageService.getNonResidentialPropertyUsages();
        addDropdownData("UsageList", usageList);
    }
    
    public String multipleSubmitRedirect() {
        setWfErrorMsg(getText("error.multiple.submit"));
        return TARGET_WORKFLOW_ERROR;
    }

    public Boolean multipleSubmitCondition(PropertyImpl property, Long approverPositionId) {
        if (property.getId() != null) {
            if (null == approverPositionId && !property.getStatus().equals(PropertyTaxConstants.STATUS_WORKFLOW))
                return Boolean.TRUE;
            else
                return propertyTaxCommonUtils.isOwnerOfApplication(property, securityUtils.getCurrentUser(), approverPositionId);
        } else
            return Boolean.FALSE;
    }

    public WorkflowBean getWorkflowBean() {
        return workflowBean;
    }

    public void setWorkflowBean(final WorkflowBean workflowBean) {
        this.workflowBean = workflowBean;
    }

    public void setPropertyTaxUtil(final PropertyTaxUtil propertyTaxUtil) {
        this.propertyTaxUtil = propertyTaxUtil;
    }

    public String getUserDesgn() {
        return userDesgn;
    }

    public void setUserDesgn(final String userDesgn) {
        this.userDesgn = userDesgn;
    }

    public Boolean getIsApprPageReq() {
        return isApprPageReq;
    }

    public void setIsApprPageReq(final Boolean isApprPageReq) {
        this.isApprPageReq = isApprPageReq;
    }

    public String getIndexNumber() {
        return indexNumber;
    }

    public void setIndexNumber(final String indexNumber) {
        this.indexNumber = indexNumber;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(final String userRole) {
        this.userRole = userRole;
    }

    public AssignmentService getAssignmentService() {
        return assignmentService;
    }

    public void setAssignmentService(final AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @Override
    public String getApproverName() {
        return approverName;
    }

    @Override
    public void setApproverName(final String approverName) {
        this.approverName = approverName;
    }

    public SimpleWorkflowService<PropertyImpl> getPropertyWorkflowService() {
        return propertyWorkflowService;
    }

    public void setPropertyWorkflowService(final SimpleWorkflowService<PropertyImpl> propertyWorkflowService) {
        this.propertyWorkflowService = propertyWorkflowService;
    }

    public SMSEmailService getsMSEmailService() {
        return sMSEmailService;
    }

    public void setsMSEmailService(final SMSEmailService sMSEmailService) {
        this.sMSEmailService = sMSEmailService;
    }

    public Boolean getPropertyByEmployee() {
        return propertyByEmployee;
    }

    public void setPropertyByEmployee(final Boolean propertyByEmployee) {
        this.propertyByEmployee = propertyByEmployee;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(final String modelId) {
        this.modelId = modelId;
    }

    public PropertyImpl getProperty() {
        return propertyModel;
    }

    public void setProperty(final PropertyImpl property) {
        propertyModel = property;
    }

    public String getWfErrorMsg() {
        return wfErrorMsg;
    }

    public void setWfErrorMsg(final String wfErrorMsg) {
        this.wfErrorMsg = wfErrorMsg;

    }

    public List<HashMap<String, Object>> getHistoryMap() {
        return historyMap;
    }

    public void setHistoryMap(final List<HashMap<String, Object>> historyMap) {
        this.historyMap = historyMap;
    }

    public String getUserDesignationList() {
        return userDesignationList;
    }

    public void setUserDesignationList(final String userDesignationList) {
        this.userDesignationList = userDesignationList;
    }

    public void setApplicationType(final String applicationType) {
        this.applicationType = applicationType;
    }

    public String getApplicationType() {
        return applicationType;
    }

    public String getInitiator() {
        return initiator;
    }

    public void setInitiator(final String initiator) {
        this.initiator = initiator;
    }

    public Boolean getIsReassignEnabled() {
        return isReassignEnabled;
    }

    public void setIsReassignEnabled(Boolean isReassignEnabled) {
        this.isReassignEnabled = isReassignEnabled;
    }

    public Long getStateAwareId() {
        return stateAwareId;
    }

    public void setStateAwareId(Long stateAwareId) {
        this.stateAwareId = stateAwareId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Boolean getEndorsementRequired() {
        return endorsementRequired;
    }

    public void setEndorsementRequired(Boolean endorsementRequired) {
        this.endorsementRequired = endorsementRequired;
    }

    public String getOwnersName() {
        return ownersName;
    }

    public void setOwnersName(String ownersName) {
        this.ownersName = ownersName;
    }

    public List<PtNotice> getEndorsementNotices() {
        return endorsementNotices;
    }

    public void setEndorsementNotices(List<PtNotice> endorsementNotices) {
        this.endorsementNotices = endorsementNotices;
    }

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public void setApplicationNumber(String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }

    public String getAssessmentNumber() {
        return assessmentNumber;
    }

    public void setAssessmentNumber(String assessmentNumber) {
        this.assessmentNumber = assessmentNumber;
    }

}