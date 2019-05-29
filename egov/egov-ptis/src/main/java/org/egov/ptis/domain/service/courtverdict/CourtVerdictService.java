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
package org.egov.ptis.domain.service.courtverdict;

import static java.lang.Boolean.FALSE;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.egov.ptis.constants.PropertyTaxConstants.ADDITIONAL_COMMISSIONER_DESIGN;
import static org.egov.ptis.constants.PropertyTaxConstants.ADMIN_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.ANONYMOUS_USER;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_COURT_VERDICT;
import static org.egov.ptis.constants.PropertyTaxConstants.ASSISTANT_COMMISSIONER_DESIGN;
import static org.egov.ptis.constants.PropertyTaxConstants.BUILT_UP_PROPERTY;
import static org.egov.ptis.constants.PropertyTaxConstants.CATEGORY_MIXED;
import static org.egov.ptis.constants.PropertyTaxConstants.CATEGORY_NON_RESIDENTIAL;
import static org.egov.ptis.constants.PropertyTaxConstants.CATEGORY_RESIDENTIAL;
import static org.egov.ptis.constants.PropertyTaxConstants.COMMISSIONER_DESGN;
import static org.egov.ptis.constants.PropertyTaxConstants.COMMISSIONER_DESIGNATIONS;
import static org.egov.ptis.constants.PropertyTaxConstants.DEPUTY_COMMISSIONER_DESIGN;
import static org.egov.ptis.constants.PropertyTaxConstants.FLOOR_MAP;
import static org.egov.ptis.constants.PropertyTaxConstants.LOCALITY;
import static org.egov.ptis.constants.PropertyTaxConstants.LOCATION_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.NATURE_COURT_VERDICT;
import static org.egov.ptis.constants.PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND;
import static org.egov.ptis.constants.PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_OFFICER_DESGN;
import static org.egov.ptis.constants.PropertyTaxConstants.SOURCE_ONLINE;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_CANCELLED;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_WORKFLOW;
import static org.egov.ptis.constants.PropertyTaxConstants.VACANT_PROPERTY;
import static org.egov.ptis.constants.PropertyTaxConstants.WARD;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_APPROVE;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_FORWARD;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_REJECT;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_COMMISSIONER_APPROVAL_PENDING;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_DIGITAL_SIGNATURE_PENDING;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_REJECTED;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_REVENUE_OFFICER_APPROVAL_PENDING;
import static org.egov.ptis.constants.PropertyTaxConstants.ZONAL_COMMISSIONER_DESIGN;
import static org.egov.ptis.constants.PropertyTaxConstants.ZONE;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.egov.commons.Installment;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.notification.service.NotificationService;
import org.egov.infra.persistence.entity.Address;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.ApplicationNumberGenerator;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.pims.commons.Position;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.domain.dao.property.PropertyTypeMasterDAO;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.BasicPropertyImpl;
import org.egov.ptis.domain.entity.property.CourtVerdict;
import org.egov.ptis.domain.entity.property.Floor;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyAddress;
import org.egov.ptis.domain.entity.property.PropertyID;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyOccupation;
import org.egov.ptis.domain.entity.property.PropertyOwnerInfo;
import org.egov.ptis.domain.entity.property.PropertyTypeMaster;
import org.egov.ptis.domain.entity.property.PropertyUsage;
import org.egov.ptis.domain.entity.property.StructureClassification;
import org.egov.ptis.domain.entity.property.vacantland.LayoutApprovalAuthority;
import org.egov.ptis.domain.entity.property.vacantland.VacantLandPlotArea;
import org.egov.ptis.domain.repository.courtverdict.CourtVerdictRepository;
import org.egov.ptis.domain.repository.master.occupation.PropertyOccupationRepository;
import org.egov.ptis.domain.repository.master.structureclassification.StructureClassificationRepository;
import org.egov.ptis.domain.repository.master.vacantland.LayoutApprovalAuthorityRepository;
import org.egov.ptis.domain.repository.master.vacantland.VacantLandPlotAreaRepository;
import org.egov.ptis.domain.service.property.PropertyService;
import org.egov.ptis.exceptions.TaxCalculatorExeption;
import org.egov.ptis.master.service.PropertyUsageService;
import org.egov.ptis.service.utils.PropertyTaxCommonUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

@Service
public class CourtVerdictService {

    @Autowired
    private CourtVerdictRepository courtVerdictRepo;
    @Autowired
    private PropertyTaxUtil propertyTaxUtil;
    @Autowired
    @Qualifier("parentMessageSource")
    private MessageSource ptisMessageSource;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private PropertyTaxCommonUtils propertyTaxCommonUtils;
    @Autowired
    private BoundaryService boundaryService;
    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private PropertyService propertyService;
    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    private PropertyTypeMasterDAO propTypeMasterDAO;
    @Autowired
    private VacantLandPlotAreaRepository vacantLandPlotAreaRepo;
    @Autowired
    private LayoutApprovalAuthorityRepository layoutApprovalAuthorityRepo;
    @Autowired
    private PropertyOccupationRepository propOccRepo;
    @Autowired
    StructureClassificationRepository structureDAO;
    @Autowired
    PropertyUsageService propertyUsageService;
    @Autowired
    private PositionMasterService positionMasterService;
    @PersistenceContext
    EntityManager entityManager;
    @Autowired
    @Qualifier("workflowService")
    private SimpleWorkflowService<PropertyImpl> propertyWorkflowService;
    @Autowired
    private ApplicationNumberGenerator applicationNo;
    private Logger logger = Logger.getLogger(getClass());

    private String propertyCategory;

    public void addModelAttributes(final Model model, final BasicProperty basicProperty,
            final HttpServletRequest request) {

        Property property = null;
        if (null != basicProperty.getProperty())
            property = basicProperty.getActiveProperty();

        List<Map<String, Object>> wcDetails = propertyService.getWCDetails(basicProperty.getUpicNo(), request);
        List<Floor> floor = property.getPropertyDetail().getFloorDetails();
        property.getPropertyDetail().setFloorDetailsProxy(floor);

        List<PropertyUsage> usageList;
        TreeMap<Integer, String> flrNoMap = new TreeMap<>();
        flrNoMap.putAll(FLOOR_MAP);
        final List<Boundary> localityList = boundaryService
                .getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(LOCALITY, LOCATION_HIERARCHY_TYPE);
        final List<Boundary> zones = boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(ZONE,
                REVENUE_HIERARCHY_TYPE);
        final List<Boundary> electionWardList = boundaryService
                .getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(WARD, ADMIN_HIERARCHY_TYPE);

        final List<PropertyTypeMaster> propTypeList = propTypeMasterDAO.findAllExcludeEWSHS();
        final List<PropertyOccupation> propOccList = propOccRepo.findAll();
        final List<StructureClassification> structureList = structureDAO.findByIsActiveTrueOrderByTypeName();

        usageList = populateUsages(
                isNotBlank(propertyCategory) ? propertyCategory : property.getPropertyDetail().getCategoryType());

        List<VacantLandPlotArea> plotAreaList = vacantLandPlotAreaRepo.findAll();
        List<LayoutApprovalAuthority> layoutApprovalList = layoutApprovalAuthorityRepo.findAll();

        model.addAttribute("floor", floor);
        model.addAttribute("wcDetails", wcDetails);
        model.addAttribute("localityList", localityList);
        model.addAttribute("zones", zones);
        model.addAttribute("electionWardList", electionWardList);
        model.addAttribute("propTypeList", propTypeList);
        model.addAttribute("propOccList", propOccList);
        model.addAttribute("flrNoMap", flrNoMap);
        model.addAttribute("structureList", structureList);

        model.addAttribute("usageList", usageList);
        model.addAttribute("plotAreaList", plotAreaList);
        model.addAttribute("layoutApprovalList", layoutApprovalList);

    }

    private List<PropertyUsage> populateUsages(final String propertyCategory) {
        List<PropertyUsage> usageList = propertyUsageService.getAllActiveMixedPropertyUsages();
        // Loading property usages based on property category
        if (StringUtils.isNoneBlank(propertyCategory))
            if (propertyCategory.equals(CATEGORY_MIXED))
                usageList = propertyUsageService.getAllActiveMixedPropertyUsages();
            else if (propertyCategory.equals(CATEGORY_RESIDENTIAL))
                usageList = propertyUsageService.getResidentialPropertyUsages();
            else if (propertyCategory.equals(CATEGORY_NON_RESIDENTIAL))
                usageList = propertyUsageService.getNonResidentialPropertyUsages();
        return usageList;
    }

    public Map<String, String> validateProperty(CourtVerdict courtVerdict) {
        HashMap<String, String> errorMessages = new HashMap<>();

        if (StringUtils.isBlank(courtVerdict.getBasicProperty().getAddress().getAreaLocalitySector()))
            errorMessages.put("areaLocalitySector", "areaLocalitySector.required");
        if (StringUtils.isBlank(courtVerdict.getBasicProperty().getPropertyID().getArea().getName()))
            errorMessages.put("block", "blockId.required");
        if (courtVerdict.getBasicProperty().getPropertyID().getZone().getName() == null)
            errorMessages.put("zone", "zone.required");
        if (StringUtils.isBlank(courtVerdict.getBasicProperty().getPropertyID().getWard().getName()))
            errorMessages.put("ward", "ward.required");
        if (courtVerdict.getBasicProperty().getPropertyID().getElectionBoundary().getName() == null)
            errorMessages.put("electionWard", "electionWard.required");
        if (StringUtils.isBlank(courtVerdict.getBasicProperty().getActiveProperty().getPropertyDetail()
                .getPropertyTypeMaster().getType()))
            errorMessages.put("categoryType", "categoryType.required");
        if (courtVerdict.getBasicProperty().getActiveProperty().getPropertyDetail().getCategoryType() == null)
            errorMessages.put("propertyType", "propertyType.required");
        return errorMessages;
    }

    @Transactional
    public CourtVerdict saveCourtVerdict(CourtVerdict courtVerdict, Long approvalPosition, final String approvalComent,
            final String additionalRule, final String workFlowAction, final Boolean propertyByEmployee) {
        final User user = securityUtils.getCurrentUser();
        final DateTime currentDate = new DateTime();
        Position pos = null;
        Assignment wfInitiator = null;
        String currentState;
        Assignment assignment;
        String approverDesignation = "";
        String nextAction = null;
        String loggedInUserDesignation = "";
        String loggedInUserDesig = "";
        List<Assignment> loggedInUserAssign;
        if (courtVerdict.getState() != null) {
            loggedInUserAssign = assignmentService.getAssignmentByPositionAndUserAsOnDate(
                    courtVerdict.getCurrentState().getOwnerPosition().getId(), user.getId(), new Date());
            loggedInUserDesig = !loggedInUserAssign.isEmpty() ? loggedInUserAssign.get(0).getDesignation().getName()
                    : "";
        }
        if (SOURCE_ONLINE.equalsIgnoreCase(courtVerdict.getSource()) && ApplicationThreadLocals.getUserId() == null)
            ApplicationThreadLocals.setUserId(securityUtils.getCurrentUser().getId());
        if (propertyService.isCitizenPortalUser(user) || !propertyByEmployee
                || ANONYMOUS_USER.equalsIgnoreCase(user.getName())) {
            currentState = "Created";
            assignment = propertyService.getUserPositionByZone(courtVerdict.getBasicProperty(), false);
            wfInitiator = assignment;
            if (null != assignment)
                approvalPosition = assignment.getPosition().getId();
        } else {
            currentState = "Created";
            if (null != approvalPosition && approvalPosition != 0) {
                assignment = assignmentService.getAssignmentsForPosition(approvalPosition, new Date()).get(0);
                assignment.getEmployee().getName().concat("~").concat(assignment.getPosition().getName());
                approverDesignation = assignment.getDesignation().getName();
            }
        }
        if (courtVerdict.getState() != null)
            loggedInUserDesignation = getLoggedInUserDesignation(
                    courtVerdict.getCurrentState().getOwnerPosition().getId(), securityUtils.getCurrentUser());
        if (WFLOW_ACTION_STEP_FORWARD.equalsIgnoreCase(workFlowAction)
                && (courtVerdict.getId() == null || REVENUE_OFFICER_DESGN.contains(loggedInUserDesig))
                && (COMMISSIONER_DESIGNATIONS.contains(approverDesignation))) {

            final String designation = approverDesignation.split(" ")[0];
            nextAction = getWorkflowNextAction(designation);
        }

        if (courtVerdict.getId() != null && courtVerdict.getState() != null)
            wfInitiator = propertyService.getWorkflowInitiator(courtVerdict.getBasicProperty().getActiveProperty());
        else if (wfInitiator == null)
            wfInitiator = propertyTaxCommonUtils.getWorkflowInitiatorAsRO(user.getId());

        if (WFLOW_ACTION_STEP_REJECT.equalsIgnoreCase(workFlowAction)) {
            if (wfInitiator.getPosition().equals(courtVerdict.getState().getOwnerPosition())) {
                courtVerdict.transition().end().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withDateInfo(currentDate.toDate()).withNextAction(null)
                        .withOwner(courtVerdict.getState().getOwnerPosition());
                courtVerdict.setStatus(STATUS_CANCELLED);
                courtVerdict.getBasicProperty().setUnderWorkflow(FALSE);
            } else {
                final Assignment assignmentOnreject = getUserAssignmentOnReject(loggedInUserDesignation, courtVerdict);
                if (assignmentOnreject != null) {
                    nextAction = "Revenue Officer Approval Pending";
                    wfInitiator = assignmentOnreject;
                } else
                    nextAction = WF_STATE_REVENUE_OFFICER_APPROVAL_PENDING;
                final String stateValue = courtVerdict.getCurrentState().getValue().split(":")[0] + ":"
                        + WF_STATE_REJECTED;
                courtVerdict.transition().progressWithStateCopy()
                        .withSenderName(user.getUsername() + "::" + user.getName()).withComments(approvalComent)
                        .withStateValue(stateValue).withDateInfo(currentDate.toDate())
                        .withOwner(wfInitiator.getPosition()).withNextAction(nextAction);
                buildSMS(courtVerdict, workFlowAction);
            }

        } else {
            if (WFLOW_ACTION_STEP_APPROVE.equalsIgnoreCase(workFlowAction))
                pos = courtVerdict.getCurrentState().getOwnerPosition();
            else if (null != approvalPosition && approvalPosition != -1 && !approvalPosition.equals(Long.valueOf(0)))
                pos = positionMasterService.getPositionById(approvalPosition);
            WorkFlowMatrix wfmatrix;
            if (null == courtVerdict.getState()) {
                wfmatrix = propertyWorkflowService.getWfMatrix(courtVerdict.getStateType(), null, null, additionalRule,
                        currentState, null);
                courtVerdict.transition().start().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withStateValue(wfmatrix.getNextState()).withDateInfo(new Date())
                        .withOwner(pos)
                        .withNextAction(wfmatrix.getNextAction() == null
                                ? getNextAction(courtVerdict.getProperty(), nextAction, workFlowAction)
                                : wfmatrix.getNextAction())
                        .withNatureOfTask(NATURE_COURT_VERDICT)
                        .withInitiator(wfInitiator != null ? wfInitiator.getPosition() : null)
                        .withSLA(propertyService.getSlaValue(APPLICATION_TYPE_COURT_VERDICT));
            } else if (courtVerdict.getCurrentState().getNextAction().equalsIgnoreCase("END"))
                courtVerdict.transition().end().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withDateInfo(currentDate.toDate()).withNextAction(null)
                        .withOwner(courtVerdict.getCurrentState().getOwnerPosition());
            else {

                wfmatrix = propertyWorkflowService.getWfMatrix(courtVerdict.getStateType(), null, null, additionalRule,
                        courtVerdict.getCurrentState().getValue(), courtVerdict.getCurrentState().getNextAction(), null,
                        loggedInUserDesignation);
                courtVerdict.transition().progressWithStateCopy()
                        .withSenderName(user.getUsername() + "::" + user.getName()).withComments(approvalComent)
                        .withStateValue(wfmatrix.getNextState()).withDateInfo(currentDate.toDate()).withOwner(pos)
                        .withNextAction(StringUtils.isNotBlank(nextAction) ? nextAction : wfmatrix.getNextAction());

                if (workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_APPROVE))
                    buildSMS(courtVerdict, workFlowAction);
            }
        }

        return courtVerdictRepo.save(courtVerdict);
    }

    private String getNextAction(final PropertyImpl property, final String approverDesignation, String workFlowAction) {
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
                            .append(WF_STATE_COMMISSIONER_APPROVAL_PENDING).toString();
            }
        return nextAction;
    }

    public String getLoggedInUserDesignation(final Long posId, final User user) {
        final List<Assignment> loggedInUserAssign = assignmentService.getAssignmentByPositionAndUserAsOnDate(posId,
                user.getId(), new Date());
        return !loggedInUserAssign.isEmpty() ? loggedInUserAssign.get(0).getDesignation().getName() : null;
    }

    private String getWorkflowNextAction(final String designation) {
        String nextAction;
        if (designation.equalsIgnoreCase(COMMISSIONER_DESGN))
            nextAction = WF_STATE_COMMISSIONER_APPROVAL_PENDING;
        else
            nextAction = new StringBuilder().append(designation).append(" ")
                    .append(WF_STATE_COMMISSIONER_APPROVAL_PENDING).toString();
        return nextAction;
    }

    public Assignment getUserAssignmentOnReject(final String loggedInUserDesignation, final CourtVerdict courtVerdict) {
        Assignment assignmentOnreject = null;
        if (loggedInUserDesignation.equalsIgnoreCase(REVENUE_OFFICER_DESGN)
                || loggedInUserDesignation.equalsIgnoreCase(ASSISTANT_COMMISSIONER_DESIGN)
                || loggedInUserDesignation.equalsIgnoreCase(ADDITIONAL_COMMISSIONER_DESIGN)
                || loggedInUserDesignation.equalsIgnoreCase(DEPUTY_COMMISSIONER_DESIGN)
                || loggedInUserDesignation.equalsIgnoreCase(COMMISSIONER_DESGN)
                || loggedInUserDesignation.equalsIgnoreCase(ZONAL_COMMISSIONER_DESIGN))
            assignmentOnreject = propertyService.getUserOnRejection(courtVerdict);

        return assignmentOnreject;

    }

    public void buildSMS(final CourtVerdict courtVerdict, final String workFlowAction) {
        for (final PropertyOwnerInfo ownerInfo : courtVerdict.getBasicProperty().getPropertyOwnerInfo())
            if (StringUtils.isNotBlank(ownerInfo.getOwner().getMobileNumber()))
                buildSms(courtVerdict, ownerInfo.getOwner(), workFlowAction);
    }

    private void buildSms(final CourtVerdict courtVerdict, final User user, final String workFlowAction) {
        final String assessmentNo = courtVerdict.getBasicProperty().getUpicNo();
        final String mobileNumber = user.getMobileNumber();
        final String applicantName = user.getName();
        String smsMsg = "";
        if (workFlowAction.equals(WFLOW_ACTION_STEP_FORWARD)) {
            // to be enabled once acknowledgement feature is developed
            /*
             * smsMsg = messageSource.getMessage("msg.initiateexemption.sms", new String[] { applicantName, assessmentNo }, null);
             */
        } else if (workFlowAction.equals(WFLOW_ACTION_STEP_REJECT))
            smsMsg = ptisMessageSource.getMessage("msg.rejectexemption.sms",
                    new String[] { applicantName, assessmentNo, ApplicationThreadLocals.getMunicipalityName() }, null);
        else if (workFlowAction.equals(WFLOW_ACTION_STEP_APPROVE)) {
            final Installment installment = propertyTaxUtil.getInstallmentListByStartDate(new Date()).get(0);
            final Date effectiveDate = org.apache.commons.lang3.time.DateUtils.addDays(installment.getToDate(), 1);
            smsMsg = ptisMessageSource.getMessage("msg.approveexemption.sms",
                    new String[] { applicantName, assessmentNo,
                            new SimpleDateFormat("dd/MM/yyyy").format(effectiveDate),
                            ApplicationThreadLocals.getMunicipalityName() },
                    null);
        }

        if (StringUtils.isNotBlank(mobileNumber))
            notificationService.sendSMS(mobileNumber, smsMsg);

    }

    public CourtVerdict updatePropertyDetails(CourtVerdict courtVerdict) {
        final Character status = STATUS_WORKFLOW;
        courtVerdict.getBasicProperty().setUnderWorkflow(true);
        Date propCompletionDate;
        PropertyImpl newProperty;
        newProperty = courtVerdict.getProperty();
        PropertyTypeMaster propTypeMaster = propTypeMasterDAO.getPropertyTypeMasterByCode(
                courtVerdict.getProperty().getPropertyDetail().getPropertyTypeMaster().getCode());
        newProperty.getPropertyDetail().setPropertyTypeMaster(propTypeMaster);

        if (propTypeMaster.getType().equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND_STR))
            newProperty.getPropertyDetail().setPropertyType(VACANT_PROPERTY);
        else
            newProperty.getPropertyDetail().setPropertyType(BUILT_UP_PROPERTY);

        setPropertyID(newProperty);
        updatePropAddress(newProperty.getBasicProperty());

        newProperty = propertyService.createProperty(newProperty,
                newProperty.getPropertyDetail().getSitalArea().getArea().toString(),
                newProperty.getPropertyDetail().getPropertyMutationMaster().getCode(),
                propTypeMaster.getId().toString(), null, null, status, newProperty.getPropertyDetail().getDocNumber(),
                null,
                newProperty.getPropertyDetail().getFloorType() != null
                        ? newProperty.getPropertyDetail().getFloorType().getId() : null,
                newProperty.getPropertyDetail().getRoofType() != null
                        ? newProperty.getPropertyDetail().getRoofType().getId() : null,
                newProperty.getPropertyDetail().getWallType() != null
                        ? newProperty.getPropertyDetail().getWallType().getId() : null,
                newProperty.getPropertyDetail().getWoodType() != null
                        ? newProperty.getPropertyDetail().getWoodType().getId() : null,
                null, null, newProperty.getPropertyDetail().getVacantLandPlotArea().getId(),
                newProperty.getPropertyDetail().getLayoutApprovalAuthority().getId(), Boolean.FALSE);

        if (!newProperty.getPropertyDetail().getPropertyTypeMaster().getCode()
                .equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND))
            propCompletionDate = propertyService
                    .getLowestDtOfCompFloorWise(newProperty.getPropertyDetail().getFloorDetails());
        else
            propCompletionDate = newProperty.getPropertyDetail().getDateOfCompletion();
        newProperty.getBasicProperty().setPropOccupationDate(propCompletionDate);

        if (newProperty != null && !newProperty.getDocuments().isEmpty())
            propertyService.processAndStoreDocument(newProperty.getDocuments());
        if (propTypeMaster != null && propTypeMaster.getCode().equals(OWNERSHIP_TYPE_VAC_LAND))
            newProperty.setPropertyDetail(propertyService
                    .changePropertyDetail(newProperty, newProperty.getPropertyDetail(), 0).getPropertyDetail());
        if (newProperty.getPropertyDetail().getLayoutApprovalAuthority() != null
                && "No Approval".equals(newProperty.getPropertyDetail().getLayoutApprovalAuthority().getName())) {
            newProperty.getPropertyDetail().setLayoutPermitNo(null);
            newProperty.getPropertyDetail().setLayoutPermitDate(null);
        }
        newProperty.getPropertyDetail().setStructure(false);

        courtVerdict.setBasicProperty((BasicPropertyImpl) newProperty.getBasicProperty());
        courtVerdict.setProperty(newProperty);
        courtVerdict.setApplicationNumber(applicationNo.generate());

        return courtVerdict;

    }

    private void updatePropAddress(BasicProperty basicProperty) {
        Address propAddress = basicProperty.getAddress();
        propAddress.setHouseNoBldgApt(basicProperty.getAddress().getHouseNoBldgApt());
        propAddress.setAreaLocalitySector(basicProperty.getPropertyID().getLocality().getName());
        propAddress.setStreetRoadLine(basicProperty.getPropertyID().getWard().getName());
        propAddress.setCityTownVillage(ApplicationThreadLocals.getCityName());

        basicProperty.setAddress((PropertyAddress) propAddress);
    }

    private void setPropertyID(PropertyImpl property) {
        PropertyID propertyID = property.getBasicProperty().getPropertyID();
        if (propertyID != null) {
            if (propertyID.getZone().getId() != null)
                property.getBasicProperty().getPropertyID()
                        .setZone(boundaryService.getBoundaryById(propertyID.getZone().getId()));
            if (propertyID.getWard().getId() != null)
                property.getBasicProperty().getPropertyID()
                        .setWard(boundaryService.getBoundaryById(propertyID.getWard().getId()));
            if (propertyID.getLocality().getId() != null)
                property.getBasicProperty().getPropertyID()
                        .setLocality(boundaryService.getBoundaryById(propertyID.getLocality().getId()));
            if (propertyID.getArea().getId() != null)
                property.getBasicProperty().getPropertyID()
                        .setArea(boundaryService.getBoundaryById(propertyID.getArea().getId()));
            if (propertyID.getElectionBoundary().getId() != null)
                property.getBasicProperty().getPropertyID()
                        .setElectionBoundary(boundaryService.getBoundaryById(propertyID.getElectionBoundary().getId()));
            if (propertyID.getNorthBoundary() != null)
                property.getBasicProperty().getPropertyID().setNorthBoundary(propertyID.getNorthBoundary());
            if (propertyID.getSouthBoundary() != null)
                property.getBasicProperty().getPropertyID().setSouthBoundary(propertyID.getSouthBoundary());
            if (propertyID.getEastBoundary() != null)
                property.getBasicProperty().getPropertyID().setEastBoundary(propertyID.getEastBoundary());
            if (propertyID.getWestBoundary() != null)
                property.getBasicProperty().getPropertyID().setWestBoundary(propertyID.getWestBoundary());

        }

    }

    public PropertyImpl modifyDemand(PropertyImpl newProperty, PropertyImpl oldProperty) {
        PropertyImpl modProperty = null;

        try {
            modProperty = (PropertyImpl) propertyService.modifyDemand(newProperty, oldProperty);
        } catch (final TaxCalculatorExeption e) {

            logger.error("forward : There are no Unit rates defined for chosen combinations", e);
            return newProperty;
        }
        return modProperty;
    }

    public CourtVerdict getCourtVerdictById(final Long id) {
        return courtVerdictRepo.findOne(id);
    }

}