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
package org.egov.wtms.application.workflow;

import static org.egov.wtms.utils.constants.WaterTaxConstants.WFLOW_ACTION_STEP_REJECT;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WFLOW_ACTION_STEP_THIRDPARTY_CREATED;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WF_STATE_REJECTED;
import static org.egov.wtms.utils.constants.WaterTaxConstants.CLOSINGCONNECTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.RECONNECTIONCONNECTION;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.egov.demand.model.EgDemand;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.pims.commons.Position;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.model.enums.BasicPropertyStatus;
import org.egov.ptis.domain.service.property.PropertyExternalService;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.entity.WaterDemandConnection;
import org.egov.wtms.application.repository.WaterConnectionDetailsRepository;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.application.service.WaterConnectionSmsAndEmailService;
import org.egov.wtms.application.service.WaterDemandConnectionService;
import org.egov.wtms.masters.entity.enums.ConnectionStatus;
import org.egov.wtms.service.es.WaterChargeDocumentService;
import org.egov.wtms.utils.PropertyExtnUtils;
import org.egov.wtms.utils.WaterTaxUtils;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * The Class ApplicationCommonWorkflow.
 */
public abstract class ApplicationWorkflowCustomImpl implements ApplicationWorkflowCustom {

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationWorkflowCustomImpl.class);

    @Autowired
    private WaterConnectionDetailsRepository waterConnectionDetailsRepository;

    @Autowired
    private PropertyExtnUtils propertyExtnUtils;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private WaterDemandConnectionService waterDemandConnectionService;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private PositionMasterService positionMasterService;

    @Autowired
    private WaterTaxUtils waterTaxUtils;

    @Autowired
    private WaterChargeDocumentService waterChargeIndexService;

    @Autowired
    private UserService userService;

    @Autowired
    private WaterConnectionSmsAndEmailService waterConnectionSmsAndEmailService;

    @Autowired
    @Qualifier("workflowService")
    private SimpleWorkflowService<WaterConnectionDetails> waterConnectionWorkflowService;

    @Autowired
    private WaterConnectionDetailsService waterConnectionDetailsService;

    @Override
    public void createCommonWorkflowTransition(final WaterConnectionDetails waterConnectionDetails,
            final Long approvalPosition, final String approvalComent, final String additionalRule,
            final String workFlowAction) {
        if (LOG.isDebugEnabled())
            LOG.debug(" Create WorkFlow Transition Started  ...");
        final User user = securityUtils.getCurrentUser();
        final DateTime currentDate = new DateTime();
        User currentUser;
        final Assignment userAssignment = assignmentService.getPrimaryAssignmentForUser(user.getId());
        Position pos = null;
        Assignment wfInitiator = null;
        final Boolean recordCreatedBYNonEmployee;
        final Boolean recordCreatedBYCitizenPortal;

        if(user!=null && (CLOSINGCONNECTION.equalsIgnoreCase(waterConnectionDetails.getApplicationType().getCode()) ||
        		RECONNECTIONCONNECTION.equalsIgnoreCase(waterConnectionDetails.getApplicationType().getCode()))){
        	recordCreatedBYNonEmployee= waterTaxUtils.getCurrentUserRole(user);
            recordCreatedBYCitizenPortal = waterTaxUtils.isCitizenPortalUser(user);
        }
        else {
            recordCreatedBYNonEmployee = waterTaxUtils
                    .getCurrentUserRole(waterConnectionDetails.getCreatedBy());
            recordCreatedBYCitizenPortal = waterTaxUtils.isCitizenPortalUser(userService.getUserById(waterConnectionDetails.getCreatedBy().getId()));
        }
        String currState = "";
        final String loggedInUserDesignation = waterTaxUtils.loggedInUserDesignation(waterConnectionDetails);
        final String natureOfwork = getNatureOfTask(waterConnectionDetails);
        if (recordCreatedBYNonEmployee || recordCreatedBYCitizenPortal) {
            currState = WFLOW_ACTION_STEP_THIRDPARTY_CREATED;
            if (!waterConnectionDetails.getStateHistory().isEmpty())
            {
                wfInitiator = assignmentService.getPrimaryAssignmentForPositon(
                        waterConnectionDetails.getStateHistory().get(0).getOwnerPosition().getId());
                if (wfInitiator == null) {
                    final List<Assignment> assignmentList = assignmentService
                            .getAssignmentsForPosition(waterConnectionDetails.getStateHistory().get(0).getOwnerPosition().getId());
                    if (!assignmentList.isEmpty())
                        wfInitiator = assignmentList.get(0);
                }
            }
        } 
        else if (null != waterConnectionDetails.getId()) {
            currentUser = userService.getUserById(waterConnectionDetails.getCreatedBy().getId());
            if (currentUser != null && waterConnectionDetails.getLegacy().equals(true)) {
                for (final Role userrole : currentUser.getRoles())
                    if (userrole.getName().equals(WaterTaxConstants.ROLE_SUPERUSER)) {
                        final Position positionuser = waterTaxUtils.getZonalLevelClerkForLoggedInUser(
                                waterConnectionDetails.getConnection().getPropertyIdentifier());
                        if (positionuser != null) {
                            wfInitiator = assignmentService.getPrimaryAssignmentForPositionAndDate(positionuser.getId(),
                                    new Date());
                            if (wfInitiator == null) {
                                final List<Assignment> assignmentList = assignmentService
                                        .getAssignmentsForPosition(positionuser.getId());
                                if (!assignmentList.isEmpty())
                                    wfInitiator = assignmentList.get(0);
                            }
                            break;
                        }
                    }
            } else {
                wfInitiator = assignmentService
                        .getPrimaryAssignmentForUser(waterConnectionDetails.getCreatedBy().getId());

                if (wfInitiator == null) {
                    final List<Assignment> assignmtList = assignmentService
                            .getAllActiveEmployeeAssignmentsByEmpId(waterConnectionDetails.getCreatedBy().getId());
                    if (!assignmtList.isEmpty())
                        wfInitiator = assignmtList.get(0);

                }

            }
        }

        if (workFlowAction != null && WaterTaxConstants.WFLOW_ACTION_STEP_CANCEL.equalsIgnoreCase(workFlowAction)) {
            if (wfInitiator.equals(userAssignment)) {
                waterConnectionDetails.setConnectionStatus(ConnectionStatus.INACTIVE);
                if (waterConnectionDetails.getStatus() != null && waterConnectionDetails.getStatus().getCode()
                        .equals(WaterTaxConstants.APPLICATION_STATUS_VERIFIED)) {
                    final EgDemand demand = waterTaxUtils.getCurrentDemand(waterConnectionDetails).getDemand();
                    if (demand != null) {
                        final WaterDemandConnection waterDemandConnection = waterDemandConnectionService
                                .findByWaterConnectionDetailsAndDemand(waterConnectionDetails, demand);
                        demand.setIsHistory("Y");
                        demand.setModifiedDate(new Date());
                        waterDemandConnection.setDemand(demand);
                        waterDemandConnectionService.updateWaterDemandConnection(waterDemandConnection);
                    }
                }
                waterConnectionDetails.setStatus(waterTaxUtils.getStatusByCodeAndModuleType(
                        WaterTaxConstants.APPLICATION_STATUS_CANCELLED, WaterTaxConstants.MODULETYPE));
                waterConnectionDetails.transition().end().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withDateInfo(currentDate.toDate()).withNatureOfTask(natureOfwork).withNextAction("END");
                waterConnectionSmsAndEmailService.sendSmsAndEmailOnRejection(waterConnectionDetails, approvalComent);
                waterConnectionDetailsService.updateIndexes(waterConnectionDetails, null);
            }
        } else if (WFLOW_ACTION_STEP_REJECT.equalsIgnoreCase(workFlowAction)) {
            if (wfInitiator.equals(userAssignment)) {
                waterConnectionDetails.setConnectionStatus(ConnectionStatus.INACTIVE);
                if (waterConnectionDetails.getStatus() != null && waterConnectionDetails.getStatus().getCode()
                        .equals(WaterTaxConstants.APPLICATION_STATUS_ESTIMATENOTICEGEN)) {
                    final EgDemand demand = waterTaxUtils.getCurrentDemand(waterConnectionDetails).getDemand();
                    if (demand != null) {
                        final WaterDemandConnection waterDemandConnection = waterDemandConnectionService
                                .findByWaterConnectionDetailsAndDemand(waterConnectionDetails, demand);
                        demand.setIsHistory("Y");
                        demand.setModifiedDate(new Date());
                        waterDemandConnection.setDemand(demand);
                        waterDemandConnectionService.updateWaterDemandConnection(waterDemandConnection);
                    }
                }
                waterConnectionDetails.setStatus(waterTaxUtils.getStatusByCodeAndModuleType(
                        WaterTaxConstants.APPLICATION_STATUS_CANCELLED, WaterTaxConstants.MODULETYPE));
                waterConnectionDetails.transition().end().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withDateInfo(currentDate.toDate()).withNatureOfTask(natureOfwork).withNextAction("END");
                waterConnectionSmsAndEmailService.sendSmsAndEmailOnRejection(waterConnectionDetails, approvalComent);
                waterConnectionDetailsService.updateIndexes(waterConnectionDetails, null);
            } else {
                final String stateValue = WF_STATE_REJECTED;
                waterConnectionDetails.transition().progressWithStateCopy()
                        .withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withStateValue(stateValue).withDateInfo(currentDate.toDate())
                        .withOwner(wfInitiator.getPosition()).withNextAction("Application Rejected")
                        .withNatureOfTask(natureOfwork);
            }
        } else {
            if (null != approvalPosition && approvalPosition != -1 && !approvalPosition.equals(Long.valueOf(0)))
                pos = positionMasterService.getPositionById(approvalPosition);
            WorkFlowMatrix wfmatrix = null;
            if (null == waterConnectionDetails.getState()) {
                wfmatrix = waterConnectionWorkflowService.getWfMatrix(waterConnectionDetails.getStateType(), null, null,
                        additionalRule, currState, null);
                waterConnectionDetails.transition().start().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withStateValue(wfmatrix.getNextState()).withDateInfo(new Date())
                        .withOwner(pos).withNextAction(wfmatrix.getNextAction()).withNatureOfTask(natureOfwork);
            } else if (WaterTaxConstants.WF_STATE_TAP_EXECUTION_DATE_BUTTON.equalsIgnoreCase(workFlowAction)) {
                if (null != workFlowAction && !workFlowAction.isEmpty()
                        && workFlowAction.equalsIgnoreCase(WaterTaxConstants.WF_STATE_TAP_EXECUTION_DATE_BUTTON)
                        && waterConnectionDetails.getApplicationType().getCode()
                                .equalsIgnoreCase(WaterTaxConstants.CHANGEOFUSE)) {
                    final WaterConnectionDetails connectionToBeDeactivated = waterConnectionDetailsRepository
                            .findConnectionDetailsByConsumerCodeAndConnectionStatus(
                                    waterConnectionDetails.getConnection().getConsumerCode(), ConnectionStatus.ACTIVE);
                    connectionToBeDeactivated.setConnectionStatus(ConnectionStatus.INACTIVE);
                    connectionToBeDeactivated.setIsHistory(true);
                    waterConnectionDetailsRepository.saveAndFlush(connectionToBeDeactivated);
                }
                wfmatrix = waterConnectionWorkflowService.getWfMatrix(waterConnectionDetails.getStateType(), null, null,
                        additionalRule, waterConnectionDetails.getCurrentState().getValue(), null, null, loggedInUserDesignation);
                final AssessmentDetails assessmentDetailsFullFlag = propertyExtnUtils.getAssessmentDetailsForFlag(
                        waterConnectionDetails.getConnection().getPropertyIdentifier(),
                        PropertyExternalService.FLAG_FULL_DETAILS, BasicPropertyStatus.ALL);
                waterConnectionDetails.setStatus(waterTaxUtils.getStatusByCodeAndModuleType(
                        WaterTaxConstants.APPLICATION_STATUS_SANCTIONED, WaterTaxConstants.MODULETYPE));
                waterConnectionDetails.setConnectionStatus(ConnectionStatus.ACTIVE);
                waterConnectionDetailsService.updateIndexes(waterConnectionDetails, null);
                if (waterConnectionDetails.getApplicationType().getCode()
                        .equalsIgnoreCase(WaterTaxConstants.CHANGEOFUSE)) {
                    final BigDecimal amountTodisplayInIndex = waterConnectionDetailsService
                            .getTotalAmount(waterConnectionDetails);
                    waterConnectionDetails.setConnectionStatus(ConnectionStatus.ACTIVE);
                    waterChargeIndexService.createWaterChargeIndex(waterConnectionDetails, assessmentDetailsFullFlag,
                            amountTodisplayInIndex);
                }
                if (wfmatrix.getNextAction().equalsIgnoreCase("END"))
                    waterConnectionDetails.transition().end()
                            .withSenderName(user.getUsername() + "::" + user.getName()).withComments(approvalComent)
                            .withDateInfo(currentDate.toDate()).withNatureOfTask(natureOfwork).withNextAction("END");
            } else if (null != approvalComent && "Receipt Cancelled".equalsIgnoreCase(approvalComent)) {
                wfmatrix = waterConnectionWorkflowService.getWfMatrix(waterConnectionDetails.getStateType(), null, null,
                        additionalRule, "Asst engg approved", null);
                waterConnectionDetails.transition().progressWithStateCopy()
                        .withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withStateValue(wfmatrix.getNextState())
                        .withDateInfo(currentDate.toDate()).withOwner(pos).withNextAction(wfmatrix.getNextAction())
                        .withNatureOfTask(natureOfwork);
            } else if ((additionalRule.equals(WaterTaxConstants.WORKFLOW_CLOSUREADDITIONALRULE)
                    || additionalRule.equals(WaterTaxConstants.RECONNECTIONCONNECTION))
                    && (waterConnectionDetails.getCurrentState().getValue().equals("Closed")
                            || waterConnectionDetails.getCurrentState().getValue().equals("END"))) {
                if (currState != null && (waterTaxUtils.getCurrentUserRole() || waterTaxUtils.isCurrentUserCitizenRole()))
                    wfmatrix = waterConnectionWorkflowService.getWfMatrix(waterConnectionDetails.getStateType(), null, null,
                            additionalRule, currState, null);
                else
                    wfmatrix = waterConnectionWorkflowService.getWfMatrix(waterConnectionDetails.getStateType(), null, null,
                            additionalRule, null, null);
                if (wfmatrix != null && !wfmatrix.getNextAction().equalsIgnoreCase("END"))
                    waterConnectionDetails.transition().reopen()
                            .withSenderName(user.getUsername() + "::" + user.getName())
                            .withComments(approvalComent).withStateValue(wfmatrix.getNextState())
                            .withDateInfo(currentDate.toDate()).withOwner(pos).withNextAction(wfmatrix.getNextAction())
                            .withNatureOfTask(natureOfwork);
            } else {
                if (getloggedinUserDesignationForEstimationAndWorkOrderGeneratedStatus(waterConnectionDetails,
                        loggedInUserDesignation))

                    wfmatrix = getMatrixbyStatusAndLoggedInUser(waterConnectionDetails, additionalRule, workFlowAction,
                            loggedInUserDesignation);
                else
                    wfmatrix = waterConnectionWorkflowService.getWfMatrix(waterConnectionDetails.getStateType(), null,
                            null, additionalRule, waterConnectionDetails.getCurrentState().getValue(), null, null);
                if ((additionalRule.equals(WaterTaxConstants.WORKFLOW_CLOSUREADDITIONALRULE)
                        || additionalRule.equals(WaterTaxConstants.RECONNECTIONCONNECTION)) && wfmatrix != null
                        && wfmatrix.getNextAction().equalsIgnoreCase("END"))
                    waterConnectionDetails.transition().end()
                            .withSenderName(user.getUsername() + "::" + user.getName()).withComments(approvalComent)
                            .withDateInfo(currentDate.toDate()).withNatureOfTask(natureOfwork).withNextAction("END");
                else
                    waterConnectionDetails.transition().progressWithStateCopy()
                            .withSenderName(user.getUsername() + "::" + user.getName())
                            .withComments(approvalComent).withStateValue(wfmatrix.getNextState())
                            .withDateInfo(currentDate.toDate()).withOwner(pos).withNextAction(wfmatrix.getNextAction())
                            .withNatureOfTask(natureOfwork);
            }

        }
        if (LOG.isDebugEnabled())
            LOG.debug(" WorkFlow Transition Completed  ...");
    }

    /*
     * NOTE: AS per new wOrkflow using API to get currect matrix for loggedinUSer is COMM, SE ,ME,EE
     */
    protected WorkFlowMatrix getMatrixbyStatusAndLoggedInUser(final WaterConnectionDetails waterConnectionDetails,
            final String additionalRule, final String workFlowAction, final String loggedInUserDesignation) {
        WorkFlowMatrix wfmatrix = null;
        if (workFlowAction.equals(WaterTaxConstants.FORWARDWORKFLOWACTION)
                && waterConnectionDetails.getStatus().getCode()
                        .equals(WaterTaxConstants.APPLICATION_STATUS_DIGITALSIGNPENDING)
                &&
                loggedInUserDesignation.equalsIgnoreCase(WaterTaxConstants.DEPUTY_ENGINEER_DESIGN))
            wfmatrix = waterConnectionWorkflowService.getWfMatrix(waterConnectionDetails.getStateType(), null, null,
                    additionalRule, "Deputy Engineer Approved", null, null, loggedInUserDesignation);
        if (wfmatrix == null && workFlowAction.equals(WaterTaxConstants.FORWARDWORKFLOWACTION)
                && waterConnectionDetails.getStatus().getCode()
                        .equals(WaterTaxConstants.APPLICATION_STATUS_DIGITALSIGNPENDING)
                && (loggedInUserDesignation.equalsIgnoreCase(WaterTaxConstants.MUNICIPAL_ENGINEER_DESIGN)
                        || loggedInUserDesignation.equalsIgnoreCase(WaterTaxConstants.SUPERIENTEND_ENGINEER_DESIGN)
                        || loggedInUserDesignation.equalsIgnoreCase(WaterTaxConstants.EXECUTIVE_ENGINEER_DESIGN)))
            wfmatrix = waterConnectionWorkflowService.getWfMatrix(waterConnectionDetails.getStateType(), null, null,
                    additionalRule, WaterTaxConstants.EXECUTIVEENGINEERFORWARDED, null, null, loggedInUserDesignation);
        else if (workFlowAction.equals(WaterTaxConstants.FORWARDWORKFLOWACTION)
                && waterConnectionDetails.getStatus().getCode().equals(WaterTaxConstants.APPLICATION_STATUS_FEEPAID)
                && (loggedInUserDesignation.equalsIgnoreCase(WaterTaxConstants.MUNICIPAL_ENGINEER_DESIGN)
                        || loggedInUserDesignation.equalsIgnoreCase(WaterTaxConstants.DEPUTY_ENGINEER_DESIGN)
                        || loggedInUserDesignation.equalsIgnoreCase(WaterTaxConstants.ASSISTANT_ENGINEER_DESIGN)
                        || loggedInUserDesignation.equalsIgnoreCase(WaterTaxConstants.ASSISTANT_EXECUTIVE_ENGINEER_DESIGN)
                        || loggedInUserDesignation.equalsIgnoreCase(WaterTaxConstants.SUPERIENTEND_ENGINEER_DESIGN)
                        || loggedInUserDesignation.equalsIgnoreCase(WaterTaxConstants.EXECUTIVE_ENGINEER_DESIGN)
                        || loggedInUserDesignation.equalsIgnoreCase(WaterTaxConstants.TAP_INSPPECTOR_DESIGN)))
            wfmatrix = waterConnectionWorkflowService.getWfMatrix(waterConnectionDetails.getStateType(), null, null,
                    additionalRule, WaterTaxConstants.WF_STATE_STATE_FORWARD, null, null, loggedInUserDesignation);
        else if (workFlowAction.equals(WaterTaxConstants.APPROVEWORKFLOWACTION)
                && waterConnectionDetails.getStatus().getCode()
                        .equals(WaterTaxConstants.APPLICATION_STATUS_DIGITALSIGNPENDING)
                && (loggedInUserDesignation.equalsIgnoreCase(WaterTaxConstants.MUNICIPAL_ENGINEER_DESIGN)
                        || loggedInUserDesignation.equalsIgnoreCase(WaterTaxConstants.SUPERIENTEND_ENGINEER_DESIGN)
                        || loggedInUserDesignation.equalsIgnoreCase(WaterTaxConstants.EXECUTIVE_ENGINEER_DESIGN)))
            wfmatrix = waterConnectionWorkflowService.getWfMatrix(waterConnectionDetails.getStateType(), null, null,
                    additionalRule, WaterTaxConstants.EXECUTIVEENGINEERFORWARDED, null, null, loggedInUserDesignation);
        else if (wfmatrix == null
                && (waterConnectionDetails.getApplicationType().getCode().equals(WaterTaxConstants.NEWCONNECTION)
                        || waterConnectionDetails.getApplicationType().getCode().equals(WaterTaxConstants.CHANGEOFUSE)
                        || waterConnectionDetails.getApplicationType().getCode()
                                .equals(WaterTaxConstants.ADDNLCONNECTION))
                && workFlowAction.equals(WaterTaxConstants.SIGNWORKFLOWACTION))
            wfmatrix = waterConnectionWorkflowService.getWfMatrix(waterConnectionDetails.getStateType(), null, null,
                    additionalRule, WaterTaxConstants.WF_STATE_COMMISSIONER_APPROVED, null, null,
                    loggedInUserDesignation);
        if (wfmatrix == null && WaterTaxConstants.FORWARDWORKFLOWACTION.equalsIgnoreCase(workFlowAction)) {
            if (waterConnectionDetails.getStatus().getCode()
                    .equals(WaterTaxConstants.APPLICATION_STATUS_CLOSERINPROGRESS))
                wfmatrix = waterConnectionWorkflowService.getWfMatrix(waterConnectionDetails.getStateType(), null, null,
                        additionalRule, WaterTaxConstants.WF_STATE_CLOSURE_FORWARED_APPROVER, null, null,
                        loggedInUserDesignation);
            if (waterConnectionDetails.getStatus().getCode()
                    .equals(WaterTaxConstants.APPLICATION_STATUS__RECONNCTIONINPROGRESS))
                wfmatrix = waterConnectionWorkflowService.getWfMatrix(waterConnectionDetails.getStateType(), null, null,
                        additionalRule, WaterTaxConstants.WF_STATE_RECONN_FORWARED_APPROVER, null, null,
                        loggedInUserDesignation);
        }
        if (wfmatrix == null
                && (WaterTaxConstants.APPROVEWORKFLOWACTION.equalsIgnoreCase(workFlowAction)
                        || WaterTaxConstants.FORWARDWORKFLOWACTION.equalsIgnoreCase(workFlowAction))
                && (loggedInUserDesignation.equalsIgnoreCase(WaterTaxConstants.MUNICIPAL_ENGINEER_DESIGN)
                        || loggedInUserDesignation.equalsIgnoreCase(WaterTaxConstants.DEPUTY_ENGINEER_DESIGN)
                        || loggedInUserDesignation.equalsIgnoreCase(WaterTaxConstants.SUPERIENTEND_ENGINEER_DESIGN)
                        || loggedInUserDesignation.equalsIgnoreCase(WaterTaxConstants.EXECUTIVE_ENGINEER_DESIGN)
                        || loggedInUserDesignation.equalsIgnoreCase(WaterTaxConstants.COMMISSIONER_DESGN))) {
            if (waterConnectionDetails.getStatus().getCode()
                    .equals(WaterTaxConstants.APPLICATION_STATUS_CLOSERDIGSIGNPENDING))
                wfmatrix = waterConnectionWorkflowService.getWfMatrix(waterConnectionDetails.getStateType(), null, null,
                        additionalRule, WaterTaxConstants.WF_STATE_COLSURE_APPROVED, null, null,
                        loggedInUserDesignation);
            if (waterConnectionDetails.getStatus().getCode()
                    .equals(WaterTaxConstants.APPLICATION_STATUS_RECONNDIGSIGNPENDING))
                wfmatrix = waterConnectionWorkflowService.getWfMatrix(waterConnectionDetails.getStateType(), null, null,
                        additionalRule, WaterTaxConstants.WF_STATE_RECONN_APPROVED, null, null,
                        loggedInUserDesignation);
        }

        if (wfmatrix == null
                && waterConnectionDetails.getStatus().getCode().equals(WaterTaxConstants.APPLICATION_STATUS_VERIFIED)
                && (loggedInUserDesignation.equalsIgnoreCase(WaterTaxConstants.ASSISTANT_EXECUTIVE_ENGINEER_DESIGN) ||
                        loggedInUserDesignation.equalsIgnoreCase(WaterTaxConstants.TAP_INSPPECTOR_DESIGN)))
            wfmatrix = waterConnectionWorkflowService.getWfMatrix(waterConnectionDetails.getStateType(), null, null,
                    additionalRule, waterConnectionDetails.getCurrentState().getValue(), null, null,
                    null);
        if (wfmatrix == null)
            wfmatrix = waterConnectionWorkflowService.getWfMatrix(waterConnectionDetails.getStateType(), null, null,
                    additionalRule, waterConnectionDetails.getCurrentState().getValue(), null, null,
                    loggedInUserDesignation);
        return wfmatrix;
    }

    protected boolean getloggedinUserDesignationForEstimationAndWorkOrderGeneratedStatus(
            final WaterConnectionDetails waterConnectionDetails, final String loggedInUserDesignation) {
        return loggedInUserDesignation != null && !"".equals(loggedInUserDesignation)
                && (loggedInUserDesignation.equals(WaterTaxConstants.COMMISSIONER_DESGN)
                        || loggedInUserDesignation.equalsIgnoreCase(WaterTaxConstants.EXECUTIVE_ENGINEER_DESIGN)
                        || loggedInUserDesignation.equalsIgnoreCase(WaterTaxConstants.MUNICIPAL_ENGINEER_DESIGN)
                        || loggedInUserDesignation.equalsIgnoreCase(WaterTaxConstants.SUPERIENTEND_ENGINEER_DESIGN)
                        || loggedInUserDesignation.equalsIgnoreCase(WaterTaxConstants.TAP_INSPPECTOR_DESIGN)
                        || loggedInUserDesignation.equalsIgnoreCase(WaterTaxConstants.ASSISTANT_ENGINEER_DESIGN)
                        || loggedInUserDesignation.equalsIgnoreCase(WaterTaxConstants.DEPUTY_ENGINEER_DESIGN)
                        || loggedInUserDesignation.equalsIgnoreCase(WaterTaxConstants.ASSISTANT_EXECUTIVE_ENGINEER_DESIGN));
    }

    public String getNatureOfTask(final WaterConnectionDetails waterConnectionDetails) {
        final String wfTypeDisplayName = "Water Tap Connection";
        if (waterConnectionDetails.getStatus().getCode().equals(WaterTaxConstants.APPLICATION_STATUS_CLOSERINPROGRESS)
                || waterConnectionDetails.getStatus().getCode()
                        .equals(WaterTaxConstants.APPLICATION_STATUS_CLOSERINITIATED)
                || waterConnectionDetails.getStatus().getCode()
                        .equals(WaterTaxConstants.APPLICATION_STATUS_CLOSERDIGSIGNPENDING)
                || waterConnectionDetails.getStatus().getCode()
                        .equals(WaterTaxConstants.APPLICATION_STATUS_CLOSERAPRROVED)
                || waterConnectionDetails.getStatus().getCode()
                        .equals(WaterTaxConstants.APPLICATION_STATUS_CLOSERSANCTIONED))
            return "Closure " + wfTypeDisplayName;
        else if (waterConnectionDetails.getStatus().getCode().equals(WaterTaxConstants.WORKFLOW_RECONNCTIONINITIATED)
                || waterConnectionDetails.getStatus().getCode()
                        .equals(WaterTaxConstants.APPLICATION_STATUS__RECONNCTIONINPROGRESS)
                || waterConnectionDetails.getStatus().getCode()
                        .equals(WaterTaxConstants.APPLICATION_STATUS__RECONNCTIONAPPROVED)
                || waterConnectionDetails.getStatus().getCode()
                        .equals(WaterTaxConstants.APPLICATION_STATUS_RECONNDIGSIGNPENDING)
                || waterConnectionDetails.getStatus().getCode()
                        .equals(WaterTaxConstants.APPLICATION_STATUS__RECONNCTIONSANCTIONED))
            return "Reconnection " + wfTypeDisplayName;
        else if (waterConnectionDetails.getApplicationType().getCode().equals(WaterTaxConstants.NEWCONNECTION))
            return "New " + wfTypeDisplayName;
        else if (waterConnectionDetails.getApplicationType().getCode().equals(WaterTaxConstants.ADDNLCONNECTION))
            return "Additional " + wfTypeDisplayName;
        else if (waterConnectionDetails.getApplicationType().getCode().equals(WaterTaxConstants.CHANGEOFUSE))
            return "Change Of Usage " + wfTypeDisplayName;
        else
            return waterConnectionDetails.getApplicationType().getName() + " " + wfTypeDisplayName;
    }

}
