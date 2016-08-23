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
import static java.math.BigDecimal.ZERO;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.egov.ptis.constants.PropertyTaxConstants.ADDTIONAL_RULE_ALTER_ASSESSMENT;
import static org.egov.ptis.constants.PropertyTaxConstants.ADDTIONAL_RULE_BIFURCATE_ASSESSMENT;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_ALTER_ASSESSENT;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_GRP;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_NEW_ASSESSENT;
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_FIRSTHALF_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_STR_EDUCATIONAL_CESS;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_STR_GENERAL_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_STR_LIBRARY_CESS;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_STR_UNAUTHORIZED_PENALTY;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_STR_VACANT_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMOLITION;
import static org.egov.ptis.constants.PropertyTaxConstants.EXEMPTION;
import static org.egov.ptis.constants.PropertyTaxConstants.FILESTORE_MODULE_NAME;
import static org.egov.ptis.constants.PropertyTaxConstants.FLOOR_MAP;
import static org.egov.ptis.constants.PropertyTaxConstants.GENERAL_REVISION_PETITION;
import static org.egov.ptis.constants.PropertyTaxConstants.NATURE_ALTERATION;
import static org.egov.ptis.constants.PropertyTaxConstants.NATURE_BIFURCATION;
import static org.egov.ptis.constants.PropertyTaxConstants.NATURE_DEMOLITION;
import static org.egov.ptis.constants.PropertyTaxConstants.NATURE_GENERAL_REVISION_PETITION;
import static org.egov.ptis.constants.PropertyTaxConstants.NATURE_NEW_ASSESSMENT;
import static org.egov.ptis.constants.PropertyTaxConstants.NATURE_OF_USAGE_RESIDENCE;
import static org.egov.ptis.constants.PropertyTaxConstants.NATURE_TAX_EXEMPTION;
import static org.egov.ptis.constants.PropertyTaxConstants.NEW_ASSESSMENT;
import static org.egov.ptis.constants.PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_MODIFY_REASON_ADD_OR_ALTER;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_MODIFY_REASON_BIFURCATE;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_CANCELLED;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_APPROVE;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_FORWARD;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_REJECT;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_SAVE;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_ASSISTANT_APPROVAL_PENDING;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_ASSISTANT_APPROVED;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_COMMISSIONER_APPROVED;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_REJECTED;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.commons.Installment;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.DesignationService;
import org.egov.eis.service.EisCommonService;
import org.egov.eis.service.EmployeeService;
import org.egov.eis.service.PositionMasterService;
import org.egov.eis.web.actions.workflow.GenericWorkFlowAction;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.messaging.MessagingService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.infra.workflow.inbox.InboxRenderServiceDeligate;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Floor;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyDetail;
import org.egov.ptis.domain.entity.property.PropertyDocs;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyTypeMaster;
import org.egov.ptis.domain.entity.property.PropertyUsage;
import org.egov.ptis.domain.entity.property.WorkflowBean;
import org.egov.ptis.domain.service.property.PropertyService;
import org.egov.ptis.domain.service.property.SMSEmailService;
import org.egov.ptis.master.service.PropertyUsageService;
import org.egov.ptis.service.utils.PropertyTaxCommonUtils;
import org.hibernate.Query;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class PropertyTaxBaseAction extends GenericWorkFlowAction {
    private static Logger LOGGER = Logger.getLogger(PropertyTaxBaseAction.class);
    private static final long serialVersionUID = 1L;

    protected Boolean isApprPageReq = Boolean.TRUE;

    protected String indexNumber;
    protected String modelId;
    protected String userRole;
    protected String ackMessage;
    protected String userDesgn;
    protected String wfErrorMsg;
    final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");

    @Autowired
    protected AssignmentService assignmentService;
    @Autowired
    private InboxRenderServiceDeligate<StateAware> inboxRenderServiceDeligate;
    @Autowired
    protected EisCommonService eisCommonService;
    @Autowired
    @Qualifier("fileStoreService")
    protected FileStoreService fileStoreService;
    @Autowired
    @Qualifier("workflowService")
    private SimpleWorkflowService<PropertyImpl> propertyWorkflowService;
    @Autowired
    private MessagingService messagingService;
    @Autowired
    private PtDemandDao ptDemandDAO;
    @Autowired
    private PropertyService propertyService;
    @Autowired
    private EmployeeService employeeService;
    private SMSEmailService sMSEmailService;
    protected PropertyTaxUtil propertyTaxUtil;
    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    private UserService userService;
    @Autowired
    private PositionMasterService positionMasterService;
    private PropertyImpl propertyModel;
    protected WorkflowBean workflowBean;
    @Autowired
    private PropertyUsageService propertyUsageService;
    @Autowired
    protected PropertyTaxCommonUtils propertyTaxCommonUtils;

    private List<File> uploads = new ArrayList<File>();
    private List<String> uploadFileNames = new ArrayList<String>();
    private List<String> uploadContentTypes = new ArrayList<String>();
    protected Map<String, BigDecimal> propertyTaxDetailsMap = new HashMap<String, BigDecimal>(0);
    protected List<Hashtable<String, Object>> historyMap = new ArrayList<Hashtable<String, Object>>();

    protected Boolean propertyByEmployee = Boolean.TRUE;
    protected String userDesignationList = new String();

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
        final List<StateHistory> workflowHisObj = inboxRenderServiceDeligate.getWorkflowHistory(stateId);
        workflowBean.setWorkFlowHistoryItems(workflowHisObj);
        return workflowHisObj;
    }

    @SuppressWarnings("unchecked")
    protected void setupWorkflowDetails() {
        LOGGER.debug("Entered into setupWorkflowDetails | Start");
        if (workflowBean != null)
            LOGGER.debug("setupWorkflowDetails: Department: " + workflowBean.getDepartmentId() + " Designation: "
                    + workflowBean.getDesignationId());
        final AjaxCommonAction ajaxCommonAction = new AjaxCommonAction();
        ajaxCommonAction.setPersistenceService(persistenceService);
        ajaxCommonAction.setDesignationService(new DesignationService());
        ajaxCommonAction.setAssignmentService(getAssignmentService());
        List<Department> departmentsForLoggedInUser = Collections.EMPTY_LIST;
        departmentsForLoggedInUser = propertyTaxUtil.getDepartmentsForLoggedInUser(securityUtils.getCurrentUser());
        workflowBean.setDepartmentList(departmentsForLoggedInUser);
        workflowBean.setDesignationList(Collections.EMPTY_LIST);
        workflowBean.setAppoverUserList(Collections.EMPTY_LIST);
        LOGGER.debug("Exiting from setupWorkflowDetails | End");
    }

    protected void validateProperty(final Property property, final String areaOfPlot, final String dateOfCompletion,
            final String eastBoundary, final String westBoundary, final String southBoundary,
            final String northBoundary, final String propTypeId, final String zoneId, final String propOccId,
            final Long floorTypeId, final Long roofTypeId, final Long wallTypeId, final Long woodTypeId,
            final String modifyRsn, final Date propCompletionDate) {

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Entered into validateProperty");

        if (propTypeId == null || propTypeId.equals("-1"))
            addActionError(getText("mandatory.propType"));
        if (isBlank(property.getPropertyDetail().getCategoryType())
                || property.getPropertyDetail().getCategoryType().equals("-1"))
            addActionError(getText("mandatory.propcatType"));

        if (propTypeId != null && !propTypeId.equals("-1")) {
            final PropertyTypeMaster propTypeMstr = (PropertyTypeMaster) getPersistenceService().find(
                    "from PropertyTypeMaster ptm where ptm.id = ?", Long.valueOf(propTypeId));
            if (propTypeMstr != null) {
                final PropertyDetail propertyDetail = property.getPropertyDetail();
                final Date regDocDate = property.getBasicProperty().getRegdDocDate();
                if (propTypeMstr.getCode().equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND)) {
                    if (null != propertyDetail)
                        validateVacantProperty(propertyDetail, eastBoundary, westBoundary, southBoundary,
                                northBoundary, modifyRsn, propCompletionDate);
                } else if (null != propertyDetail.isAppurtenantLandChecked()) {
                    validateVacantProperty(propertyDetail, eastBoundary, westBoundary, southBoundary, northBoundary,
                            modifyRsn, propCompletionDate);
                    validateBuiltUpProperty(propertyDetail, floorTypeId, roofTypeId, areaOfPlot, regDocDate, modifyRsn);
                } else
                    validateBuiltUpProperty(propertyDetail, floorTypeId, roofTypeId, areaOfPlot, regDocDate, modifyRsn);
                validateFloor(propTypeMstr, property.getPropertyDetail().getFloorDetailsProxy(), property, areaOfPlot,
                        regDocDate, modifyRsn, propCompletionDate);
            }
        }

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Exiting from validateProperty");
    }

    private void validateVacantProperty(final PropertyDetail propertyDetail, final String eastBoundary,
            final String westBoundary, final String southBoundary, final String northBoundary, final String modifyRsn,
            final Date propCompletionDate) {

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Entered into validateVacantProperty");
        if (isBlank(propertyDetail.getSurveyNumber()))
            addActionError(getText("mandatory.surveyNo"));
        if (isBlank(propertyDetail.getPattaNumber()))
            addActionError(getText("mandatory.pattaNum"));
        if (null == propertyDetail.getSitalArea().getArea())
            addActionError(getText("mandatory.vacantLandArea"));
        if (null == propertyDetail.getDateOfCompletion())
            addActionError(getText("mandatory.dtOfCmpln"));
        if (null == propertyDetail.getCurrentCapitalValue())
            addActionError(getText("mandatory.capitalValue"));
        if (null == propertyDetail.getMarketValue())
            addActionError(getText("mandatory.marketValue"));
        if (propertyDetail.getCurrentCapitalValue() != null
                && propertyDetail.getCurrentCapitalValue() < Double
                        .parseDouble(PropertyTaxConstants.VACANTLAND_MIN_CUR_CAPITALVALUE))
            addActionError(getText("minvalue.capitalValue"));
        if (isBlank(eastBoundary))
            addActionError(getText("mandatory.eastBoundary"));
        if (isBlank(westBoundary))
            addActionError(getText("mandatory.westBoundary"));
        if (isBlank(southBoundary))
            addActionError(getText("mandatory.southBoundary"));
        if (isBlank(northBoundary))
            addActionError(getText("mandatory.northBoundary"));

        if (null != modifyRsn && null != propCompletionDate) {
            if (null != propCompletionDate && propertyDetail.getDateOfCompletion() != null) {
                if (!DateUtils.compareDates(propertyDetail.getDateOfCompletion(), propCompletionDate)) {
                    addActionError(getText("modify.vacant.completiondate.validate"));
                }
            }
        }

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Exiting from validateVacantProperty");

    }

    private void validateBuiltUpProperty(final PropertyDetail propertyDetail, final Long floorTypeId,
            final Long roofTypeId, final String areaOfPlot, final Date regDocDate, final String modifyRsn) {

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Eneterd into validateBuiltUpProperty");

        /*if (propertyDetail.isStructure())
            if (isBlank(propertyDetail.getSiteOwner()))
                addActionError(getText("mandatory.siteowner"));*/
        if (null != propertyDetail.isAppurtenantLandChecked() && null == propertyDetail.getExtentAppartenauntLand())
            addActionError(getText("mandatory.extentAppartnant"));
        else if (null == propertyDetail.isAppurtenantLandChecked() && isBlank(areaOfPlot))
            addActionError(getText("mandatory.extentsite"));
        else if (null == propertyDetail.isAppurtenantLandChecked()
                && ("".equals(areaOfPlot) || Double.valueOf(areaOfPlot) == 0))
            addActionError(getText("mandatory.extentsite.greaterthanzero"));
        if (floorTypeId == null || floorTypeId == -1)
            addActionError(getText("mandatory.floorType"));
        if (roofTypeId == null || roofTypeId == -1)
            addActionError(getText("mandatory.roofType"));

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Exiting from validateBuiltUpProperty");
    }

    private void validateFloor(final PropertyTypeMaster propTypeMstr, final List<Floor> floorList,
            final Property property, final String areaOfPlot, final Date regDocDate, final String modifyRsn,
            final Date propCompletionDate) {
        boolean buildingPlanNoValidationAdded;
        boolean buildingPlanDateValidationAdded;
        boolean buildingPlanPlinthAreaValidationAdded;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Entered into validateFloor \nPropertyTypeMaster:" + propTypeMstr + ", No of floors: "
                    + (floorList != null ? floorList : ZERO));

        if (!propTypeMstr.getCode().equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND)){
            if (floorList != null && floorList.size() > 0){
                for (final Floor floor : floorList) {
                    List<String> msgParams = null;
                    if (floor != null) {
                    	buildingPlanNoValidationAdded = false;
                        buildingPlanDateValidationAdded = false;
                        buildingPlanPlinthAreaValidationAdded = false;
                        msgParams = new ArrayList<String>();
                        if (floor.getFloorNo() == null || floor.getFloorNo().equals(-10))
                            addActionError(getText("mandatory.floorNO"));
                        msgParams.add(floor.getFloorNo() != null ? FLOOR_MAP.get(floor.getFloorNo()) : "N/A");

                        if (floor.getStructureClassification() == null
                                || floor.getStructureClassification().getId() == null
                                || floor.getStructureClassification().getId().toString().equals("-1"))
                            addActionError(getText("mandatory.constType", msgParams));
                        
                        if (!floor.getUnstructuredLand())
                        {
                            if (floor.getBuiltUpArea() == null || floor.getBuiltUpArea().getLength() == null
                                    || floor.getBuiltUpArea().getLength().equals("")) {
                                addActionError(getText("mandatory.assbleLength",msgParams));
                            }
                            if (floor.getBuiltUpArea() == null || floor.getBuiltUpArea().getBreadth() == null
                                    || floor.getBuiltUpArea().getBreadth().equals("")) {
                                addActionError(getText("mandatory.assbleWidth",msgParams));
                            }                          
                        }
                                              
                        if (floor.getPropertyUsage() == null || null == floor.getPropertyUsage().getId()
                                || floor.getPropertyUsage().getId().toString().equals("-1"))
                            addActionError(getText("mandatory.floor.usage", msgParams));
                        
                        if(StringUtils.isNotBlank(floor.getBuildingPermissionNo())){
                        	if(floor.getBuildingPermissionDate() == null){
                        		addActionError(getText("mandatory.floor.buildingplan.date", msgParams));
                        		buildingPlanDateValidationAdded = true;
                        	}
                        	if(floor.getBuildingPlanPlinthArea().getArea() == null){
                        		addActionError(getText("mandatory.floor.buildingplan.plintharea", msgParams));
                        		buildingPlanPlinthAreaValidationAdded = true;
                        	}
                        }
                        if(floor.getBuildingPermissionDate() !=null ){
                        	if(StringUtils.isBlank(floor.getBuildingPermissionNo())){
                       			addActionError(getText("mandatory.floor.buildingplan.number", msgParams));
                       			buildingPlanNoValidationAdded = true;
                        	}
                        	if(floor.getBuildingPlanPlinthArea().getArea() == null)
                        		if(!buildingPlanPlinthAreaValidationAdded)
                        			addActionError(getText("mandatory.floor.buildingplan.plintharea", msgParams));
                        }
                        if(floor.getBuildingPlanPlinthArea().getArea() != null){
                        	if(floor.getBuildingPermissionDate() == null)
                        		if(!buildingPlanDateValidationAdded)
                        			addActionError(getText("mandatory.floor.buildingplan.date", msgParams));
                        	if(StringUtils.isBlank(floor.getBuildingPermissionNo()))
                        		if(!buildingPlanNoValidationAdded)
                        			addActionError(getText("mandatory.floor.buildingplan.number", msgParams));
                        }

                        if (floor.getFirmName() == null || floor.getFirmName().isEmpty()
                                || floor.getFirmName().equals("")) {
                            if (floor.getPropertyUsage() != null && null != floor.getPropertyUsage().getId()
                                    && !floor.getPropertyUsage().getId().toString().equals("-1")) {  
                                final PropertyUsage pu = propertyUsageService.findById(Long.valueOf(floor
                                        .getPropertyUsage().getId()));
                                if (pu != null && !pu.getUsageName().equalsIgnoreCase(NATURE_OF_USAGE_RESIDENCE))
                                    addActionError(getText("mandatory.floor.firmName", msgParams));
                            }
                        }

                        if (floor.getPropertyOccupation() == null || null == floor.getPropertyOccupation().getId()
                                || floor.getPropertyOccupation().getId().toString().equals("-1"))
                            addActionError(getText("mandatory.floor.occ"));
                        
                        if(floor.getConstructionDate() == null || floor.getConstructionDate().equals(""))
                        	addActionError(getText("mandatory.floor.constrDate"));
                        
                        Date effDate = propertyTaxUtil.getEffectiveDateForProperty();
                        if (floor.getOccupancyDate() == null || floor.getOccupancyDate().equals(""))
                            addActionError(getText("mandatory.floor.docOcc"));
                        if (floor.getOccupancyDate() != null && !floor.getOccupancyDate().equals("")) {
                            if (floor.getOccupancyDate().after(new Date()))
                                addActionError(getText("mandatory.dtFlrBeforeCurr"));
                            if (floor.getOccupancyDate().before(effDate))
                                addActionError(getText("constrDate.before.6inst",msgParams));
                        }

                        if(floor.getOccupancyDate() != null && floor.getConstructionDate() != null 
                        		&& floor.getOccupancyDate().before(floor.getConstructionDate()))
                        	addActionError(getText("effectiveDate.before.constrDate.error"));
                        
                        if (floor.getBuiltUpArea() == null || floor.getBuiltUpArea().getArea() == null
                                || floor.getBuiltUpArea().getArea().equals("")) {
                            addActionError(getText("mandatory.assbleArea"));
                        } else if (StringUtils.isNotBlank(areaOfPlot)
                                && floor.getBuiltUpArea().getArea() > Double.valueOf(areaOfPlot))
                            addActionError(getText("assbleArea.notgreaterthan.extentsite"));

                        if (modifyRsn == null
                                || (modifyRsn != null && !modifyRsn.equals(PROPERTY_MODIFY_REASON_ADD_OR_ALTER) && !modifyRsn
                                        .equals(PROPERTY_MODIFY_REASON_BIFURCATE))) {
                            if (null != regDocDate && null != floor.getOccupancyDate()
                                    && !floor.getOccupancyDate().equals("")) {
                                if (DateUtils.compareDates(regDocDate, floor.getOccupancyDate()))
                                    addActionError(getText("regDate.notgreaterthan.occDate", msgParams));
                            }
                        }
                        if (null != modifyRsn && null != propCompletionDate) {
                            if (null != propCompletionDate && floor.getOccupancyDate() != null
                                    && !floor.getOccupancyDate().equals("")) {
                                if (!DateUtils.compareDates(floor.getOccupancyDate(), propCompletionDate)) {
                                    addActionError(getText("modify.builtup.occDate.validate", msgParams));
                                }
                            }
                        }

                    }
                }
            }
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Exiting from validate");
    }

    /**
     * Validates house number,assuming house number should be unique across ward
     * boundary
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
                && (basicProperty == null || basicProperty != null
                        && !basicProperty.getAddress().getHouseNoBldgApt().equals(houseNo)))
            addActionError(getText("houseNo.unique"));
    }

    /**
     * Get Designation for logged in user
     */
    public void setUserInfo() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Entered into setUserInfo");

        final Long userId = securityUtils.getCurrentUser().getId();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("setUserInfo: Logged in userId" + userId);
        final Designation designation = propertyTaxUtil.getDesignationForUser(userId);
        if (designation != null)
            setUserDesgn(designation.getName());
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Exit from setUserInfo");
    }
    
    /**
     * Get a String for All the Designations for logged in user
     */
    public void setUserDesignations() {
        final Long userId = securityUtils.getCurrentUser().getId();
        String designations=propertyTaxCommonUtils.getAllDesignationsForUser(userId);
        setUserDesignationList(designations);
    }

    /**
     * Workflow for new and addition/alteration of assessment
     * 
     * @param property
     */
    public void transitionWorkFlow(final PropertyImpl property) {
        final DateTime currentDate = new DateTime();
        final User user = securityUtils.getCurrentUser();
        final Assignment userAssignment = assignmentService.getPrimaryAssignmentForUser(user.getId());
        Position pos = null;
        Assignment wfInitiator = null;
        String nature = getNatureOfTask();

        if (!propertyByEmployee) {
            currentState = "Created";
            final Assignment assignment = propertyService.getUserPositionByZone(property.getBasicProperty());
            if (null != assignment) {
                approverPositionId = assignment.getPosition().getId();
                approverName = (assignment.getEmployee().getName()).concat("~").concat(
                        assignment.getPosition().getName());
            }
        } else {
            currentState = null;
            if (null != approverPositionId && approverPositionId != -1) {
                Assignment assignment = assignmentService.getAssignmentsForPosition(approverPositionId, new Date())
                        .get(0);
                approverName = assignment.getEmployee().getName().concat("~")
                        .concat(assignment.getPosition().getName());
            }
        }
        if (null != property.getId())
            wfInitiator = propertyService.getWorkflowInitiator(property);

        if (WFLOW_ACTION_STEP_REJECT.equalsIgnoreCase(workFlowAction)) {
            if (wfInitiator.equals(userAssignment)) {
                property.transition(true).end().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approverComments).withDateInfo(currentDate.toDate());
                property.setStatus(STATUS_CANCELLED);
                property.getBasicProperty().setUnderWorkflow(FALSE);
            } else {
                final String stateValue = property.getCurrentState().getValue().split(":")[0] + ":" + WF_STATE_REJECTED;
                property.transition(true).withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approverComments).withStateValue(stateValue).withDateInfo(currentDate.toDate())
                        .withOwner(wfInitiator.getPosition()).withNextAction(WF_STATE_ASSISTANT_APPROVAL_PENDING);
            }

        } else {
            if (null != approverPositionId && approverPositionId != -1)
                pos = (Position) persistenceService.find("from Position where id=?", approverPositionId);
            else if (WFLOW_ACTION_STEP_APPROVE.equalsIgnoreCase(workFlowAction))
                pos = positionMasterService.getPositionByUserId(securityUtils.getCurrentUser().getId());
            else
                pos = wfInitiator.getPosition();
            if (null == property.getState()) {
                final WorkFlowMatrix wfmatrix = propertyWorkflowService.getWfMatrix(property.getStateType(), null,
                        null, getAdditionalRule(), currentState, null);
                property.transition().start().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approverComments).withStateValue(wfmatrix.getNextState())
                        .withDateInfo(currentDate.toDate()).withOwner(pos).withNextAction(wfmatrix.getNextAction())
                        .withNatureOfTask(nature);
            } else if (property.getCurrentState().getNextAction().equalsIgnoreCase("END"))
                property.transition(true).end().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approverComments).withDateInfo(currentDate.toDate());
            else {
                final WorkFlowMatrix wfmatrix = propertyWorkflowService.getWfMatrix(property.getStateType(), null,
                        null, getAdditionalRule(), property.getCurrentState().getValue(), null);
                property.transition(true).withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approverComments).withStateValue(wfmatrix.getNextState())
                        .withDateInfo(currentDate.toDate()).withOwner(pos).withNextAction(wfmatrix.getNextAction());
            }
        }
        if (approverName != null && !approverName.isEmpty() && !approverName.equalsIgnoreCase("----Choose----")) {
            final String approvalmesg = " Succesfully Forwarded to : ";
            ackMessage = ackMessage == null ? approvalmesg : ackMessage + approvalmesg;
        } else if (workFlowAction != null && workFlowAction.equalsIgnoreCase("cancel")) {
            final String approvalmesg = " Succesfully Cancelled.";
            ackMessage = ackMessage == null ? approvalmesg : ackMessage + approvalmesg;
        }

        LOGGER.debug("Exiting method : transitionWorkFlow");
    }

    private String getNatureOfTask() {
        String nature = NEW_ASSESSMENT.equalsIgnoreCase(getAdditionalRule()) ? NATURE_NEW_ASSESSMENT
                : ADDTIONAL_RULE_ALTER_ASSESSMENT.equalsIgnoreCase(getAdditionalRule()) ? NATURE_ALTERATION
                        : ADDTIONAL_RULE_BIFURCATE_ASSESSMENT.equalsIgnoreCase(getAdditionalRule()) ? NATURE_BIFURCATION
                                : DEMOLITION.equalsIgnoreCase(getAdditionalRule()) ? NATURE_DEMOLITION
                                        : EXEMPTION.equalsIgnoreCase(getAdditionalRule()) ? NATURE_TAX_EXEMPTION
                                                : GENERAL_REVISION_PETITION.equalsIgnoreCase(getAdditionalRule()) ? NATURE_GENERAL_REVISION_PETITION
                                                        : "PropertyImpl";
        return nature;
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
        final User user = property.getBasicProperty().getPrimaryOwner();
        final String mobileNumber = user.getMobileNumber();
        final String emailid = user.getEmailId();
        final String applicantName = user.getName();
        final List<String> args = new ArrayList<String>();
        args.add(applicantName);
        String smsMsg = "";
        String emailSubject = "";
        String emailBody = "";

        final Map<String, BigDecimal> demandCollMap = ptDemandDAO.getDemandCollMap(property);
        if (null != property && null != property.getState()) {
            final State propertyState = property.getState();
            if (propertyState.getValue().endsWith(WF_STATE_ASSISTANT_APPROVED)) {
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
                } else if (APPLICATION_TYPE_ALTER_ASSESSENT.equals(applicationType)) {

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
                } else if (APPLICATION_TYPE_ALTER_ASSESSENT.equals(applicationType)) {
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
                args.add(demandCollMap.get(CURR_FIRSTHALF_DMD_STR).add(demandCollMap.get(ARR_DMD_STR)).toString());
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
                } else if (APPLICATION_TYPE_ALTER_ASSESSENT.equals(applicationType)) {
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
        if (mobileNumber != null)
            messagingService.sendSMS(mobileNumber, smsMsg);
        if (emailid != null)
            messagingService.sendEmail(emailid, emailSubject, emailBody);

    }

    public void preparePropertyTaxDetails(Property property) {
    	Map<String, Installment> installmentMap = propertyTaxUtil.getInstallmentsForCurrYear(new Date());
        Installment installmentFirstHalf = installmentMap.get(PropertyTaxConstants.CURRENTYEAR_FIRST_HALF);
        Installment installmentSecondHalf = installmentMap.get(PropertyTaxConstants.CURRENTYEAR_SECOND_HALF);
        Map<String, BigDecimal> demandCollMap = null;
        //Based on the current date, the tax details will be fetched for the respective installment
        if(DateUtils.between(new Date(), installmentFirstHalf.getFromDate(), installmentFirstHalf.getToDate()))
        	demandCollMap = propertyTaxUtil.prepareDemandDetForWorkflowProperty(property,installmentFirstHalf,installmentFirstHalf);
        else if(DateUtils.between(new Date(), installmentSecondHalf.getFromDate(), installmentSecondHalf.getToDate()))
        	demandCollMap = propertyTaxUtil.prepareDemandDetForWorkflowProperty(property,installmentFirstHalf,installmentSecondHalf);
       
        Ptdemand ptDemand = ptDemandDAO.getNonHistoryCurrDmdForProperty(property);
        if (null != ptDemand && ptDemand.getDmdCalculations() != null && ptDemand.getDmdCalculations().getAlv() != null)
            propertyTaxDetailsMap.put("ARV", ptDemand.getDmdCalculations().getAlv());
        else
            propertyTaxDetailsMap.put("ARV", BigDecimal.ZERO);

        propertyTaxDetailsMap.put(
                "eduCess",
                (demandCollMap.get(DEMANDRSN_STR_EDUCATIONAL_CESS) == null ? BigDecimal.ZERO : demandCollMap
                        .get(DEMANDRSN_STR_EDUCATIONAL_CESS)));
        propertyTaxDetailsMap.put(
                "libraryCess",
                (demandCollMap.get(DEMANDRSN_STR_LIBRARY_CESS) == null ? BigDecimal.ZERO : demandCollMap
                        .get(DEMANDRSN_STR_LIBRARY_CESS)));
        BigDecimal totalTax = BigDecimal.ZERO;
        if (!property.getPropertyDetail().getPropertyTypeMaster().getCode().equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND)) {
            propertyTaxDetailsMap.put("generalTax", demandCollMap.get(DEMANDRSN_STR_GENERAL_TAX) == null ? BigDecimal.ZERO : demandCollMap
                    .get(DEMANDRSN_STR_GENERAL_TAX));
            totalTax = (demandCollMap.get(DEMANDRSN_STR_GENERAL_TAX) == null ? BigDecimal.ZERO : demandCollMap
                            .get(DEMANDRSN_STR_GENERAL_TAX))
                    .add(demandCollMap.get(DEMANDRSN_STR_EDUCATIONAL_CESS) == null ? BigDecimal.ZERO : demandCollMap
                            .get(DEMANDRSN_STR_EDUCATIONAL_CESS))
                    .add(demandCollMap.get(DEMANDRSN_STR_LIBRARY_CESS) == null ? BigDecimal.ZERO : demandCollMap
                            .get(DEMANDRSN_STR_LIBRARY_CESS));
            // If unauthorized property, then add unauthorized penalty
            if (demandCollMap.get(DEMANDRSN_STR_UNAUTHORIZED_PENALTY) != null) {
                propertyTaxDetailsMap.put("unauthorisedPenalty", demandCollMap.get(DEMANDRSN_STR_UNAUTHORIZED_PENALTY));
                propertyTaxDetailsMap.put("totalTax",
                        totalTax.add(demandCollMap.get(DEMANDRSN_STR_UNAUTHORIZED_PENALTY)));
            } else {
                propertyTaxDetailsMap.put("totalTax", totalTax);
            }

        } else {
            propertyTaxDetailsMap.put("vacantLandTax",
                    demandCollMap.get(DEMANDRSN_STR_VACANT_TAX) == null ? BigDecimal.ZERO : demandCollMap.get(DEMANDRSN_STR_VACANT_TAX));
            totalTax = (demandCollMap.get(DEMANDRSN_STR_VACANT_TAX) == null ? BigDecimal.ZERO : demandCollMap
                    .get(DEMANDRSN_STR_VACANT_TAX)).add(
                    demandCollMap.get(DEMANDRSN_STR_LIBRARY_CESS) == null ? BigDecimal.ZERO : demandCollMap
                            .get(DEMANDRSN_STR_LIBRARY_CESS));
            if (demandCollMap.get(DEMANDRSN_STR_UNAUTHORIZED_PENALTY) != null) {
                propertyTaxDetailsMap.put("unauthorisedPenalty", demandCollMap.get(DEMANDRSN_STR_UNAUTHORIZED_PENALTY));
                propertyTaxDetailsMap.put("totalTax",
                        totalTax.add(demandCollMap.get(DEMANDRSN_STR_UNAUTHORIZED_PENALTY)));
            } else {
                propertyTaxDetailsMap.put("totalTax", totalTax);
            }
            propertyTaxDetailsMap.put("totalTax", totalTax);
        }
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

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public PropertyImpl getProperty() {
        return propertyModel;
    }

    public void setProperty(PropertyImpl property) {
        this.propertyModel = property;
    }

    public String getWfErrorMsg() {
        return wfErrorMsg;
    }

    public void setWfErrorMsg(String wfErrorMsg) { 
        this.wfErrorMsg = wfErrorMsg;

    }

    public List<Hashtable<String, Object>> getHistoryMap() {
        return historyMap;
    }

    public void setHistoryMap(List<Hashtable<String, Object>> historyMap) {
        this.historyMap = historyMap;
    }
    public String getUserDesignationList(){
        return userDesignationList;
    }
    public void setUserDesignationList(String userDesignationList) {
        this.userDesignationList = userDesignationList;
    }
    
    
}