/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
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
package org.egov.wtms.application.workflow;

import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.egov.wtms.utils.constants.WaterTaxConstants.ADDNLCONNECTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.ADDNL_WATER_TAP_CONNECTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPLICATION_STATUS_CANCELLED;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPLICATION_STATUS_CLOSERDIGSIGNPENDING;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPLICATION_STATUS_CLOSERINPROGRESS;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPLICATION_STATUS_DIGITALSIGNPENDING;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPLICATION_STATUS_ESTIMATENOTICEGEN;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPLICATION_STATUS_FEEPAID;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPLICATION_STATUS_RECONNCTIONINPROGRESS;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPLICATION_STATUS_RECONNDIGSIGNPENDING;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPLICATION_STATUS_VERIFIED;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPROVEWORKFLOWACTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.ASSISTANT_EXECUTIVE_ENGINEER_DESIGN;
import static org.egov.wtms.utils.constants.WaterTaxConstants.CHANGEOFUSE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.CHANGEOFUSE_WATER_TAP_CONNECTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.CLOSECONNECTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.CLOSINGCONNECTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.CLOSURE_WATER_TAP_CONNECTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.COMMISSIONER_DESGN;
import static org.egov.wtms.utils.constants.WaterTaxConstants.COMM_APPROVAL_PENDING;
import static org.egov.wtms.utils.constants.WaterTaxConstants.DEPUTY_ENGINEER_DESIGN;
import static org.egov.wtms.utils.constants.WaterTaxConstants.EXECUTIVE_ENGINEER_DESIGN;
import static org.egov.wtms.utils.constants.WaterTaxConstants.FORWARDWORKFLOWACTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.JUNIOR_ASSISTANT_DESIGN;
import static org.egov.wtms.utils.constants.WaterTaxConstants.MODULETYPE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.MUNICIPAL_ENGINEER_DESIGN;
import static org.egov.wtms.utils.constants.WaterTaxConstants.NEWCONNECTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.NEW_WATER_TAP_CONNECTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.RECONNECTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.RECONN_WATER_TAP_CONNECTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.REGLZN_WATER_TAP_CONNECTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.REGULARIZE_CONNECTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.ROLE_APPROVERROLE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.ROLE_SUPERUSER;
import static org.egov.wtms.utils.constants.WaterTaxConstants.SENIOR_ASSISTANT_DESIGN;
import static org.egov.wtms.utils.constants.WaterTaxConstants.SIGNWORKFLOWACTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.SUPERIENTEND_ENGINEER_DESIGN;
import static org.egov.wtms.utils.constants.WaterTaxConstants.SUPERINTENDING_ENGINEER_DESIGNATION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.TAP_INSPPECTOR_DESIGN;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WFLOW_ACTION_STEP_CANCEL;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WFLOW_ACTION_STEP_REJECT;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WFLOW_ACTION_STEP_THIRDPARTY_CREATED;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WF_STATE_AE_APPROVAL_PENDING;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WF_STATE_CANCELLED;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WF_STATE_CLOSURE_APPROVED;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WF_STATE_CLOSURE_FORWARED_APPROVER;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WF_STATE_COMMISSIONER_APPROVED;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WF_STATE_RECONN_APPROVED;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WF_STATE_RECONN_FORWARED_APPROVER;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WF_STATE_REJECTED;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.egov.commons.entity.Source;
import org.egov.demand.model.EgDemand;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.pims.commons.Position;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.entity.WaterDemandConnection;
import org.egov.wtms.application.repository.WaterConnectionDetailsRepository;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.application.service.WaterConnectionSmsAndEmailService;
import org.egov.wtms.application.service.WaterDemandConnectionService;
import org.egov.wtms.masters.entity.enums.ConnectionStatus;
import org.egov.wtms.masters.service.ApplicationProcessTimeService;
import org.egov.wtms.utils.WaterTaxUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * The Class ApplicationCommonWorkflow.
 */
public abstract class ApplicationWorkflowCustomImpl implements ApplicationWorkflowCustom {

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationWorkflowCustomImpl.class);
    private static final String APPLICATION_REJECTED = "Application Rejected";

    @Autowired
    private WaterConnectionDetailsRepository waterConnectionDetailsRepository;

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
    private UserService userService;

    @Autowired
    private WaterConnectionSmsAndEmailService waterConnectionSmsAndEmailService;

    @Autowired
    @Qualifier("workflowService")
    private SimpleWorkflowService<WaterConnectionDetails> waterConnectionWorkflowService;

    @Autowired
    private WaterConnectionDetailsService waterConnectionDetailsService;

    @Autowired
    private ApplicationProcessTimeService applicationProcessTimeService;

    @Override
    public void createCommonWorkflowTransition(WaterConnectionDetails waterConnectionDetails,
            Long approvalPosition, String approvalComent, String additionalRule,
            String workFlowAction) {
        if (LOG.isDebugEnabled())
            LOG.debug(" Create WorkFlow Transition Started  ...");
        User user = securityUtils.getCurrentUser();
        DateTime currentDate = new DateTime();

        Position ownerPosition = null;
        Boolean recordCreatedBYNonEmployee;
        Boolean recordCreatedBYCitizenPortal;
        Boolean recordCreatedByAnonymousUser;
        Boolean recordCreatedBySuperUser;
        Boolean recordCreatedByRoleAdmin;

        if (approvalPosition != null && approvalPosition != -1 && !approvalPosition.equals(Long.valueOf(0)))
            ownerPosition = positionMasterService.getPositionById(approvalPosition);

        if (user != null && user.getId() != waterConnectionDetails.getCreatedBy().getId()
                && (CLOSINGCONNECTION.equalsIgnoreCase(waterConnectionDetails.getApplicationType().getCode())
                        || RECONNECTION.equalsIgnoreCase(waterConnectionDetails.getApplicationType().getCode()))) {
            recordCreatedBYNonEmployee = waterTaxUtils.getCurrentUserRole(user);
            recordCreatedBYCitizenPortal = waterTaxUtils.isCitizenPortalUser(user);
            recordCreatedByAnonymousUser = waterTaxUtils.isAnonymousUser(user);
            recordCreatedBySuperUser = waterTaxUtils.isSuperUser(user);
            recordCreatedByRoleAdmin = waterTaxUtils.isRoleAdmin(user);
        } else {
            User initiator = userService.getUserById(waterConnectionDetails.getCreatedBy().getId());
            recordCreatedBYNonEmployee = waterTaxUtils.getCurrentUserRole(waterConnectionDetails.getCreatedBy());
            recordCreatedBYCitizenPortal = waterTaxUtils.isCitizenPortalUser(initiator);
            recordCreatedByAnonymousUser = waterTaxUtils.isAnonymousUser(initiator);
            recordCreatedBySuperUser = waterTaxUtils.isSuperUser(initiator);
            recordCreatedByRoleAdmin = waterTaxUtils.isRoleAdmin(initiator);
        }

        String currState = "";
        Assignment wfInitiator = null;
        String loggedInUserDesignation = waterTaxUtils.loggedInUserDesignation(waterConnectionDetails);
        String natureOfwork = getNatureOfTask(waterConnectionDetails);

        if (recordCreatedBYNonEmployee || recordCreatedBYCitizenPortal || recordCreatedByAnonymousUser || recordCreatedBySuperUser
                || recordCreatedByRoleAdmin) {
            currState = WFLOW_ACTION_STEP_THIRDPARTY_CREATED;
            if (!waterConnectionDetails.getStateHistory().isEmpty()) {
                wfInitiator = assignmentService.getPrimaryAssignmentForPositon(
                        waterConnectionDetails.getStateHistory().get(0).getOwnerPosition().getId());
                if (wfInitiator == null) {
                    List<Assignment> assignmentList = assignmentService.getAssignmentsForPosition(
                            waterConnectionDetails.getStateHistory().get(0).getOwnerPosition().getId());
                    if (!assignmentList.isEmpty())
                        wfInitiator = assignmentList.get(0);
                }
            }

        } else if (waterConnectionDetails.getId() != null) {
            User currentUser = userService.getUserById(waterConnectionDetails.getCreatedBy().getId());
            if (currentUser != null && waterConnectionDetails.getLegacy().equals(true)) {
                for (Role userrole : currentUser.getRoles())
                    if (userrole.getName().equals(ROLE_SUPERUSER) || ROLE_APPROVERROLE.equals(userrole.getName())) {
                        Position positionuser = waterTaxUtils.getZonalLevelClerkForLoggedInUser(
                                waterConnectionDetails.getConnection().getPropertyIdentifier(), false);
                        if (positionuser != null) {
                            wfInitiator = assignmentService.getPrimaryAssignmentForPositionAndDate(positionuser.getId(),
                                    new Date());

                            if (wfInitiator == null) {
                                List<Assignment> assignmentList = assignmentService
                                        .getAssignmentsForPosition(positionuser.getId());
                                if (!assignmentList.isEmpty())
                                    wfInitiator = assignmentList.get(0);
                            }
                            break;
                        }
                    }
            } else if (WFLOW_ACTION_STEP_REJECT.equalsIgnoreCase(workFlowAction)) {
                Position position = waterTaxUtils
                        .getZonalLevelClerkForLoggedInUser(waterConnectionDetails.getConnection().getPropertyIdentifier(), false);
                if (position != null) {
                    wfInitiator = assignmentService.getPrimaryAssignmentForPositionAndDate(position.getId(), new Date());

                    if (wfInitiator == null) {
                        List<Assignment> assignmentList = assignmentService.getAssignmentsForPosition(position.getId());
                        if (!assignmentList.isEmpty())
                            wfInitiator = assignmentList.get(0);
                    }
                }
            } else {
                wfInitiator = assignmentService.getPrimaryAssignmentForUser(waterConnectionDetails.getCreatedBy().getId());

                if (wfInitiator == null) {
                    List<Assignment> assignmtList = assignmentService.getAllActiveEmployeeAssignmentsByEmpId(
                            waterConnectionDetails.getCreatedBy().getId());
                    if (!assignmtList.isEmpty())
                        wfInitiator = assignmtList.get(0);

                }

            }
        }
        if (workFlowAction != null && WFLOW_ACTION_STEP_CANCEL.equalsIgnoreCase(workFlowAction)) {
            waterConnectionDetails.setConnectionStatus(ConnectionStatus.INACTIVE);
            if (!CLOSINGCONNECTION.equalsIgnoreCase(waterConnectionDetails.getApplicationType().getCode())) {
                EgDemand demand = waterDemandConnectionService.getCurrentDemand(waterConnectionDetails).getDemand();
                if (demand != null) {
                    WaterDemandConnection waterDemandConnection = waterDemandConnectionService
                            .findByWaterConnectionDetailsAndDemand(waterConnectionDetails, demand);
                    demand.setIsHistory("Y");
                    demand.setModifiedDate(new Date());
                    waterDemandConnection.setDemand(demand);
                    waterDemandConnectionService.updateWaterDemandConnection(waterDemandConnection);
                }
            }

            waterConnectionDetails.setStatus(waterTaxUtils.getStatusByCodeAndModuleType(
                    APPLICATION_STATUS_CANCELLED, MODULETYPE));
            waterConnectionDetails.transition().end().withSenderName(user.getUsername() + "::" + user.getName())
                    .withComments(approvalComent).withStateValue(WF_STATE_CANCELLED).withOwner(ownerPosition)
                    .withDateInfo(currentDate.toDate())
                    .withNatureOfTask(natureOfwork)
                    .withNextAction("END");
            waterConnectionDetailsRepository.save(waterConnectionDetails);
            waterConnectionSmsAndEmailService.sendSmsAndEmailOnRejection(waterConnectionDetails, approvalComent);
            waterConnectionDetailsService.updateIndexes(waterConnectionDetails);

        } else if (WFLOW_ACTION_STEP_REJECT.equalsIgnoreCase(workFlowAction)) {
            if (wfInitiator != null && wfInitiator.equals(assignmentService.getPrimaryAssignmentForUser(user.getId()))) {
                waterConnectionDetails.setConnectionStatus(ConnectionStatus.INACTIVE);
                if (waterConnectionDetails.getStatus() != null
                        && waterConnectionDetails.getStatus().getCode().equals(APPLICATION_STATUS_ESTIMATENOTICEGEN)) {
                    EgDemand demand = waterDemandConnectionService.getCurrentDemand(waterConnectionDetails).getDemand();
                    if (demand != null) {
                        WaterDemandConnection waterDemandConnection = waterDemandConnectionService
                                .findByWaterConnectionDetailsAndDemand(waterConnectionDetails, demand);
                        demand.setIsHistory("Y");
                        demand.setModifiedDate(new Date());
                        waterDemandConnection.setDemand(demand);
                        waterDemandConnectionService.updateWaterDemandConnection(waterDemandConnection);
                    }
                }
                waterConnectionDetails.setStatus(waterTaxUtils.getStatusByCodeAndModuleType(
                        APPLICATION_STATUS_CANCELLED, MODULETYPE));
                waterConnectionDetails.transition().end().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withDateInfo(currentDate.toDate()).withNatureOfTask(natureOfwork)
                        .withNextAction("END");
                waterConnectionSmsAndEmailService.sendSmsAndEmailOnRejection(waterConnectionDetails, approvalComent);
                waterConnectionDetailsService.updateIndexes(waterConnectionDetails);
            } else {
                if (ownerPosition == null && wfInitiator != null)
                    ownerPosition = wfInitiator.getPosition();
                waterConnectionDetails.transition().progressWithStateCopy()
                        .withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withStateValue(WF_STATE_REJECTED).withDateInfo(currentDate.toDate())
                        .withOwner(ownerPosition)
                        .withNextAction(APPLICATION_REJECTED)
                        .withNatureOfTask(natureOfwork);
            }
        } else {
            WorkFlowMatrix wfmatrix;
            if (waterConnectionDetails.getState() == null) {
                if (REGULARIZE_CONNECTION.equalsIgnoreCase(waterConnectionDetails.getApplicationType().getCode()) &&
                        isDesignationOfClerk())
                    currState = "NEW";
                wfmatrix = waterConnectionWorkflowService.getWfMatrix(waterConnectionDetails.getStateType(), null, null,
                        additionalRule, currState, null,
                        REGULARIZE_CONNECTION.equalsIgnoreCase(waterConnectionDetails.getApplicationType().getCode())
                                ? waterConnectionDetails.getApplicationDate()
                                : null);

                Integer sla = applicationProcessTimeService.getApplicationProcessTime(
                        waterConnectionDetails.getApplicationType(),
                        waterConnectionDetails.getCategory());
                if (sla == null)
                    throw new ApplicationRuntimeException("err.applicationprocesstime.undefined");
                waterConnectionDetails.transition().start().withSenderName(user.getUsername() + "::" + user.getName())
                        .withSLA(new LocalDateTime().plusDays(sla).toDate()).withInitiator(ownerPosition)
                        .withComments(approvalComent).withStateValue(wfmatrix.getNextState()).withDateInfo(new Date())
                        .withOwner(ownerPosition).withNextAction(wfmatrix.getNextAction()).withNatureOfTask(natureOfwork);
            } else if (SIGNWORKFLOWACTION.equalsIgnoreCase(workFlowAction))
                waterConnectionDetails.transition().end().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withDateInfo(currentDate.toDate()).withNatureOfTask(natureOfwork)
                        .withNextAction("END");
            else if (null != approvalComent && "Receipt Cancelled".equalsIgnoreCase(approvalComent)) {
                wfmatrix = waterConnectionWorkflowService.getWfMatrix(waterConnectionDetails.getStateType(), null, null,
                        additionalRule, "Asst engg approved", null);
                waterConnectionDetails.transition().progressWithStateCopy()
                        .withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withStateValue(wfmatrix.getNextState())
                        .withDateInfo(currentDate.toDate()).withOwner(ownerPosition).withNextAction(wfmatrix.getNextAction())
                        .withNatureOfTask(natureOfwork);
            } else if ((additionalRule.equals(CLOSECONNECTION) || additionalRule.equals(RECONNECTION))
                    && (waterConnectionDetails.getCurrentState().getValue().equals("Closed")
                            || waterConnectionDetails.getCurrentState().getValue().equals("END")
                            || "Cancelled".equalsIgnoreCase(waterConnectionDetails.getCurrentState().getValue()))) {
                if (currState != null && (waterTaxUtils.getCurrentUserRole() || waterTaxUtils.isCurrentUserCitizenRole()
                        || waterTaxUtils.isMeesevaUser(user) || waterTaxUtils.isAnonymousUser(user) 
                        || (waterConnectionDetails.getSource() != null
                                && Source.WARDSECRETARY.toString().equalsIgnoreCase(waterConnectionDetails.getSource().toString()))))
                    wfmatrix = waterConnectionWorkflowService.getWfMatrix(waterConnectionDetails.getStateType(), null, null,
                            additionalRule, currState, null);
                else
                    wfmatrix = waterConnectionWorkflowService.getWfMatrix(waterConnectionDetails.getStateType(), null, null,
                            additionalRule, null, null);
                if (wfmatrix != null && !wfmatrix.getNextAction().equalsIgnoreCase("END"))
                    waterConnectionDetails.transition().reopen()
                            .withSenderName(user.getUsername() + "::" + user.getName())
                            .withComments(approvalComent).withStateValue(wfmatrix.getNextState())
                            .withDateInfo(currentDate.toDate()).withOwner(ownerPosition).withNextAction(wfmatrix.getNextAction())
                            .withNatureOfTask(natureOfwork);
            } else {
                if (REGULARIZE_CONNECTION.equalsIgnoreCase(waterConnectionDetails.getApplicationType().getCode())) {
                    if (SENIOR_ASSISTANT_DESIGN.equalsIgnoreCase(loggedInUserDesignation) ||
                            JUNIOR_ASSISTANT_DESIGN.equalsIgnoreCase(loggedInUserDesignation))
                        wfmatrix = waterConnectionWorkflowService.getWfMatrix(waterConnectionDetails.getStateType(), null,
                                null, additionalRule, waterConnectionDetails.getCurrentState().getValue(),
                                waterConnectionDetailsService.getReglnConnectionPendingAction(waterConnectionDetails,
                                        null, workFlowAction),
                                waterConnectionDetails.getApplicationDate());
                    else
                        wfmatrix = waterConnectionWorkflowService.getWfMatrix(waterConnectionDetails.getStateType(), null,
                                null, additionalRule, waterConnectionDetails.getCurrentState().getValue(),
                                waterConnectionDetailsService.getReglnConnectionPendingAction(waterConnectionDetails,
                                        loggedInUserDesignation, workFlowAction),
                                waterConnectionDetails.getApplicationDate());
                } else if (isCurrentUserApprover(loggedInUserDesignation))
                    wfmatrix = getMatrixbyStatusAndLoggedInUser(waterConnectionDetails, additionalRule, workFlowAction,
                            loggedInUserDesignation);
                else if (APPLICATION_STATUS_FEEPAID.equalsIgnoreCase(waterConnectionDetails.getStatus().getCode()) &&
                        WF_STATE_REJECTED.equalsIgnoreCase(waterConnectionDetails.getState().getValue()))
                    wfmatrix = waterConnectionWorkflowService.getWfMatrix(waterConnectionDetails.getStateType(), null,
                            null, additionalRule, waterConnectionDetails.getCurrentState().getValue(),
                            WF_STATE_AE_APPROVAL_PENDING, null);
                else
                    wfmatrix = waterConnectionWorkflowService.getWfMatrix(waterConnectionDetails.getStateType(), null,
                            null, additionalRule, waterConnectionDetails.getCurrentState().getValue(), null, null);
                if ((additionalRule.equals(CLOSECONNECTION) || additionalRule.equals(RECONNECTION)) && wfmatrix != null
                        && wfmatrix.getNextAction().equalsIgnoreCase("END"))
                    waterConnectionDetails.transition().end()
                            .withSenderName(user.getUsername() + "::" + user.getName()).withComments(approvalComent)
                            .withDateInfo(currentDate.toDate()).withNatureOfTask(natureOfwork).withNextAction("END");
                else
                    waterConnectionDetails.transition().progressWithStateCopy()
                            .withSenderName(user.getUsername() + "::" + user.getName())
                            .withComments(approvalComent).withStateValue(wfmatrix.getNextState())
                            .withDateInfo(currentDate.toDate()).withOwner(ownerPosition).withNextAction(wfmatrix.getNextAction())
                            .withNatureOfTask(natureOfwork);
            }
        }
        if (LOG.isDebugEnabled())
            LOG.debug(" WorkFlow Transition Completed  ...");
    }

    /*
     * NOTE: AS per new wOrkflow using API to get currect matrix for loggedinUSer is COMM, SE ,ME,EE
     */
    private WorkFlowMatrix getMatrixbyStatusAndLoggedInUser(WaterConnectionDetails waterConnectionDetails,
            String additionalRule, String workFlowAction,
            String loggedInUserDesignation) {

        WorkFlowMatrix wfmatrix = getWorkFlowMatrix(workFlowAction, loggedInUserDesignation, waterConnectionDetails,
                additionalRule);
        String connectionStatus = waterConnectionDetails.getStatus().getCode();
        if (wfmatrix == null
                && (APPROVEWORKFLOWACTION.equalsIgnoreCase(workFlowAction)
                        || FORWARDWORKFLOWACTION.equalsIgnoreCase(workFlowAction))
                && (connectionStatus.equals(APPLICATION_STATUS_CLOSERDIGSIGNPENDING)
                        || connectionStatus.equals(APPLICATION_STATUS_RECONNDIGSIGNPENDING))) {
            String currentState = connectionStatus.equals(APPLICATION_STATUS_CLOSERDIGSIGNPENDING)
                    ? WF_STATE_CLOSURE_APPROVED : WF_STATE_RECONN_APPROVED;
            wfmatrix = waterConnectionWorkflowService.getWfMatrix(waterConnectionDetails.getStateType(), null, null,
                    additionalRule, currentState, null, null, loggedInUserDesignation);
        } else if (wfmatrix == null && waterConnectionDetails.getStatus().getCode().equals(APPLICATION_STATUS_VERIFIED)
                && (loggedInUserDesignation.equalsIgnoreCase(ASSISTANT_EXECUTIVE_ENGINEER_DESIGN)
                        || loggedInUserDesignation.equalsIgnoreCase(TAP_INSPPECTOR_DESIGN)))
            wfmatrix = waterConnectionWorkflowService.getWfMatrix(waterConnectionDetails.getStateType(), null, null,
                    additionalRule, waterConnectionDetails.getCurrentState().getValue(), null, null, null);
        else if (Arrays.asList(NEWCONNECTION, ADDNLCONNECTION, CHANGEOFUSE)
                .contains(waterConnectionDetails.getApplicationType().getCode())
                && (APPLICATION_STATUS_FEEPAID.equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())
                        || APPLICATION_STATUS_DIGITALSIGNPENDING.equalsIgnoreCase(waterConnectionDetails.getStatus().getCode()))
                && COMMISSIONER_DESGN.equalsIgnoreCase(loggedInUserDesignation))
            wfmatrix = waterConnectionWorkflowService.getWfMatrix(waterConnectionDetails.getStateType(), null, null,
                    additionalRule, waterConnectionDetails.getCurrentState().getValue(), COMM_APPROVAL_PENDING, null,
                    loggedInUserDesignation);
        else
            wfmatrix = waterConnectionWorkflowService.getWfMatrix(waterConnectionDetails.getStateType(), null, null,
                    additionalRule, waterConnectionDetails.getCurrentState().getValue(), null, null,
                    loggedInUserDesignation);
        return wfmatrix;
    }

    public WorkFlowMatrix getWorkFlowMatrix(final String workFlowAction, final String loggedInUserDesignation,
            final WaterConnectionDetails waterConnectionDetails, final String additionalRule) {
        WorkFlowMatrix workFlowMatrix = null;
        if (workFlowAction.equals(FORWARDWORKFLOWACTION)
                && (waterConnectionDetails.getStatus().getCode()
                        .equals(APPLICATION_STATUS_DIGITALSIGNPENDING) ||
                        waterConnectionDetails.getStatus().getCode().equals(APPLICATION_STATUS_FEEPAID))) {
            workFlowMatrix = waterConnectionWorkflowService.getWfMatrix(waterConnectionDetails.getStateType(), null, null,
                    additionalRule, waterConnectionDetails.getState().getValue(), null, null, loggedInUserDesignation);
            if (workFlowMatrix == null)
                workFlowMatrix = waterConnectionWorkflowService.getWfMatrix(waterConnectionDetails.getStateType(), null, null,
                        additionalRule, waterConnectionDetails.getState().getValue(), null, null);
        } else if (workFlowAction.equals(APPROVEWORKFLOWACTION)
                && waterConnectionDetails.getStatus().getCode()
                        .equals(APPLICATION_STATUS_DIGITALSIGNPENDING)
                && (loggedInUserDesignation.equalsIgnoreCase(MUNICIPAL_ENGINEER_DESIGN)
                        || loggedInUserDesignation.equalsIgnoreCase(SUPERINTENDING_ENGINEER_DESIGNATION)
                        || loggedInUserDesignation.equalsIgnoreCase(EXECUTIVE_ENGINEER_DESIGN)
                        || loggedInUserDesignation.equalsIgnoreCase(DEPUTY_ENGINEER_DESIGN)))
            workFlowMatrix = waterConnectionWorkflowService.getWfMatrix(waterConnectionDetails.getStateType(), null, null,
                    additionalRule, waterConnectionDetails.getState().getValue(), null, null, loggedInUserDesignation);
        else if (Arrays.asList(NEWCONNECTION, CHANGEOFUSE, ADDNLCONNECTION)
                .contains(waterConnectionDetails.getApplicationType().getCode())
                && workFlowAction.equals(SIGNWORKFLOWACTION))
            workFlowMatrix = waterConnectionWorkflowService.getWfMatrix(waterConnectionDetails.getStateType(), null, null,
                    additionalRule, WF_STATE_COMMISSIONER_APPROVED, null, null,
                    loggedInUserDesignation);
        if (workFlowMatrix == null && FORWARDWORKFLOWACTION.equalsIgnoreCase(workFlowAction)) {
            if (waterConnectionDetails.getStatus().getCode()
                    .equals(APPLICATION_STATUS_CLOSERINPROGRESS))
                workFlowMatrix = waterConnectionWorkflowService.getWfMatrix(waterConnectionDetails.getStateType(), null, null,
                        additionalRule, WF_STATE_CLOSURE_FORWARED_APPROVER, null, null,
                        loggedInUserDesignation);
            if (waterConnectionDetails.getStatus().getCode()
                    .equals(APPLICATION_STATUS_RECONNCTIONINPROGRESS))
                workFlowMatrix = waterConnectionWorkflowService.getWfMatrix(waterConnectionDetails.getStateType(), null, null,
                        additionalRule, WF_STATE_RECONN_FORWARED_APPROVER, null, null,
                        loggedInUserDesignation);
        }
        return workFlowMatrix;
    }

    private boolean isCurrentUserApprover(String loggedInUserDesignation) {
        return isNotBlank(loggedInUserDesignation) &&
                Arrays.asList(COMMISSIONER_DESGN, EXECUTIVE_ENGINEER_DESIGN, MUNICIPAL_ENGINEER_DESIGN, DEPUTY_ENGINEER_DESIGN,
                        SUPERIENTEND_ENGINEER_DESIGN, SUPERINTENDING_ENGINEER_DESIGNATION,
                        ASSISTANT_EXECUTIVE_ENGINEER_DESIGN).contains(loggedInUserDesignation);
    }

    public String getNatureOfTask(final WaterConnectionDetails waterConnectionDetails) {
        if (CLOSINGCONNECTION.equalsIgnoreCase(waterConnectionDetails.getApplicationType().getCode()))
            return CLOSURE_WATER_TAP_CONNECTION;
        else if (RECONNECTION.equalsIgnoreCase(waterConnectionDetails.getApplicationType().getCode()))
            return RECONN_WATER_TAP_CONNECTION;
        else if (NEWCONNECTION.equalsIgnoreCase(waterConnectionDetails.getApplicationType().getCode()))
            return NEW_WATER_TAP_CONNECTION;
        else if (ADDNLCONNECTION.equalsIgnoreCase(waterConnectionDetails.getApplicationType().getCode()))
            return ADDNL_WATER_TAP_CONNECTION;
        else if (CHANGEOFUSE.equalsIgnoreCase(waterConnectionDetails.getApplicationType().getCode()))
            return CHANGEOFUSE_WATER_TAP_CONNECTION;
        else
            return REGLZN_WATER_TAP_CONNECTION;
    }

    public boolean isDesignationOfClerk() {
        List<Assignment> assignments;
        assignments = assignmentService.getAllAssignmentsByEmpId(securityUtils.getCurrentUser().getId());
        for (Assignment assignment : assignments)
            if (Arrays.asList(SENIOR_ASSISTANT_DESIGN, JUNIOR_ASSISTANT_DESIGN)
                    .contains(assignment.getPosition().getDeptDesig().getDesignation().getName()))
                return true;
        return false;
    }

}
