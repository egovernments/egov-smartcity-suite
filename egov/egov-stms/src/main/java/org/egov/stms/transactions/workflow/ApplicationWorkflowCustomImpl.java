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

package org.egov.stms.transactions.workflow;


import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.PositionMasterService;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.pims.commons.Position;
import org.egov.stms.transactions.entity.SewerageApplicationDetails;
import org.egov.stms.transactions.service.SewerageDemandService;
import org.egov.stms.transactions.service.SewerageWorkflowService;
import org.egov.stms.utils.SewerageTaxUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import static org.egov.stms.utils.constants.SewerageTaxConstants.WFLOW_ACTION_STEP_REJECT;
import static org.egov.stms.utils.constants.SewerageTaxConstants.WF_STATE_REJECTED;
import static org.egov.stms.utils.constants.SewerageTaxConstants.WFLOW_ACTION_STEP_CANCEL;
import static org.egov.stms.utils.constants.SewerageTaxConstants.ANONYMOUS_USER;
import static org.egov.stms.utils.constants.SewerageTaxConstants.WF_INSPECTIONFEE_COLLECTION;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_STATUS_COLLECTINSPECTIONFEE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPROVEWORKFLOWACTION;
import static org.egov.stms.utils.constants.SewerageTaxConstants.WF_STATE_PAYMENTDONE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.WF_STATE_EE_APPROVED;
import static org.egov.stms.utils.constants.SewerageTaxConstants.WF_STATE_CONNECTION_EXECUTION_BUTTON;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_STATUS_SANCTIONED;
import static org.egov.stms.utils.constants.SewerageTaxConstants.MODULETYPE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.WF_STATE_DEPUTY_EXE_APPROVED;
import static org.egov.stms.utils.constants.SewerageTaxConstants.CHANGEINCLOSETS_NOCOLLECTION;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_STATUS_FINALAPPROVED;
import static org.egov.stms.utils.constants.SewerageTaxConstants.NEWSEWERAGECONNECTION;
import static org.egov.stms.utils.constants.SewerageTaxConstants.CHANGEINCLOSETS;
import static org.egov.stms.utils.constants.SewerageTaxConstants.WF_STATE_ASSISTANT_APPROVED;
import static org.egov.stms.utils.constants.SewerageTaxConstants.WFPA_REJECTED_INSPECTIONFEE_COLLECTION;
import static org.egov.stms.utils.constants.SewerageTaxConstants.WF_STATE_INSPECTIONFEE_PENDING;
import static org.egov.stms.utils.constants.SewerageTaxConstants.WF_STATE_INSPECTIONFEE_COLLECTED;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_STATUS_ESTIMATENOTICEGEN;
import static org.egov.stms.utils.constants.SewerageTaxConstants.WF_STATE_CONNECTION_CLOSE_BUTTON;
import static org.egov.stms.utils.constants.SewerageTaxConstants.WF_STATE_CLOSECONNECTION_NOTICEGENERATION_PENDING;

/**
 * The Class ApplicationCommonWorkflow.
 */
public abstract class ApplicationWorkflowCustomImpl implements ApplicationWorkflowCustom {

    private static final String ANONYMOUS_CREATED = "Anonymous  Created";

    private static final String THIRD_PARTY_OPERATOR_CREATED = "Third Party operator created";

    private static final String CITIZEN_CREATED = "Citizen created";

    private static final String NEW = "NEW";

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationWorkflowCustomImpl.class);

    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    private PositionMasterService positionMasterService;
    @Autowired
    private SewerageTaxUtils sewerageTaxUtils;
    @Autowired
    private SewerageWorkflowService sewerageWorkflowService;
    @Autowired
    private SewerageDemandService sewerageDemandService;
    @Autowired
    @Qualifier("workflowService")
    private SimpleWorkflowService<SewerageApplicationDetails> sewerageApplicationWorkflowService;

    @Override
    public void createCommonWorkflowTransition(final SewerageApplicationDetails sewerageApplicationDetails) {
        if (LOG.isDebugEnabled())
            LOG.debug(" Create WorkFlow Transition Started  ...");
        final User user = securityUtils.getCurrentUser();
        final DateTime currentDate = new DateTime();
        Position pos;
        Assignment wfInitiator = null;
        String currState = StringUtils.EMPTY;
        WorkFlowMatrix wfmatrix;
        WorkflowContainer workflowContainer = sewerageApplicationDetails.getWorkflowContainer();
        String natureOfWork = sewerageApplicationDetails.getApplicationType().getName();
        String additionalRule = workflowContainer.getAdditionalRule();
        String workFlowAction = workflowContainer.getWorkFlowAction();
        String approverComment = workflowContainer.getApproverComments();
        Long approverPosition = workflowContainer.getApproverPositionId();
        if (sewerageApplicationDetails.getId() != null) {
            wfInitiator = sewerageWorkflowService.getWorkFlowInitiator(sewerageApplicationDetails);
        }
        if (WFLOW_ACTION_STEP_REJECT.equalsIgnoreCase(workFlowAction)) {
            // rejection in b.w workflow, send application to the creator
            sewerageApplicationDetails.transition().progressWithStateCopy()
                    .withSenderName(user.getUsername() + "::" + user.getName())
                    .withComments(approverComment)
                    .withStateValue(WF_STATE_REJECTED).withDateInfo(currentDate.toDate())
                    .withOwner(wfInitiator.getPosition()).withNextAction("Application Rejected")
                    .withNatureOfTask(natureOfWork);
        } else if (WFLOW_ACTION_STEP_CANCEL.equalsIgnoreCase(workFlowAction)) {
            // Incase of reject / cancel from the creator, end the workflow
            sewerageApplicationDetails.transition().end()
                    .withSenderName(user.getUsername() + "::" + user.getName())
                    .withComments(approverComment)
                    .withDateInfo(currentDate.toDate())
                    .withNatureOfTask(natureOfWork);
        } else {
            if (approverPosition != null && approverPosition > 0)
                pos = positionMasterService.getPositionById(approverPosition);
            else
                pos = wfInitiator.getPosition();

            Boolean cscOperatorLoggedIn = sewerageWorkflowService.isCscOperator(user);
            Boolean citizenPortalUser = sewerageTaxUtils.isCitizenPortalUser(user);
            boolean isWardSecretaryOperator = sewerageWorkflowService.isWardSecretaryUser(user);
            if (sewerageApplicationDetails.getState() == null
                    && (cscOperatorLoggedIn || citizenPortalUser || isWardSecretaryOperator
                    || ANONYMOUS_USER.equalsIgnoreCase(securityUtils.getCurrentUser().getUsername()))) {
                if (cscOperatorLoggedIn || isWardSecretaryOperator)
                    currState = THIRD_PARTY_OPERATOR_CREATED;
                else if (citizenPortalUser && sewerageApplicationDetails.getState() == null)
                    currState = CITIZEN_CREATED;
                else if (citizenPortalUser && "Fee collection Pending".equals(sewerageApplicationDetails.getState().getValue()))
                    currState = sewerageApplicationDetails.getState().getValue();
                else
                    currState = ANONYMOUS_CREATED;
                wfmatrix = sewerageApplicationWorkflowService.getWfMatrix(sewerageApplicationDetails.getStateType(), null,
                        null, additionalRule, currState, null);
                if (citizenPortalUser) {
                    if (sewerageApplicationDetails.getCurrentDemand() != null
                            && !sewerageDemandService
                            .checkAnyTaxIsPendingToCollect(sewerageApplicationDetails.getCurrentDemand()))
                        sewerageApplicationDetails.transition().start().withOwner(pos).withInitiator(pos);

                } else
                    sewerageApplicationDetails.transition().start().withOwner(pos).withInitiator(pos);
                sewerageApplicationDetails.transition()
                        .withSLA(sewerageTaxUtils.getSlaAppConfigValues(sewerageApplicationDetails.getApplicationType()))
                        .withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approverComment)
                        .withStateValue(wfmatrix.getNextState())
                        .withDateInfo(new Date())
                        .withNextAction(wfmatrix.getNextAction())
                        .withNatureOfTask(natureOfWork);
            } else if (null == sewerageApplicationDetails.getState()) {
                if (sewerageTaxUtils.isInspectionFeeCollectionRequired())
                    wfmatrix = sewerageApplicationWorkflowService.getWfMatrix(
                            sewerageApplicationDetails.getStateType(), null, null, additionalRule, currState,
                            WF_INSPECTIONFEE_COLLECTION);
                else
                    wfmatrix = sewerageApplicationWorkflowService.getWfMatrix(
                            sewerageApplicationDetails.getStateType(), null, null, additionalRule, currState, null);

                sewerageApplicationDetails.transition().start()
                        .withSLA(sewerageTaxUtils.getSlaAppConfigValues(sewerageApplicationDetails.getApplicationType()))
                        .withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approverComment)
                        .withStateValue(wfmatrix.getNextState())
                        .withDateInfo(new Date())
                        .withOwner(pos)
                        .withInitiator(sewerageWorkflowService.getWorkFlowInitiator(sewerageApplicationDetails).getPosition())
                        .withNextAction(wfmatrix.getNextAction())
                        .withNatureOfTask(natureOfWork);
            } else if (sewerageApplicationDetails.getState() != null
                    && (NEW.equalsIgnoreCase(sewerageApplicationDetails.getState().getValue())
                    && APPLICATION_STATUS_COLLECTINSPECTIONFEE.equalsIgnoreCase(sewerageApplicationDetails.getStatus().getCode()))
                    && (sewerageWorkflowService.isCscOperator(sewerageApplicationDetails.getCreatedBy())
                    || ANONYMOUS_USER.equalsIgnoreCase(sewerageApplicationDetails.getCreatedBy().getUsername())
                    || sewerageTaxUtils.isCitizenPortalUser(sewerageApplicationDetails.getCreatedBy()))) {
                if (sewerageTaxUtils.isInspectionFeeCollectionRequired())
                    wfmatrix = sewerageApplicationWorkflowService.getWfMatrix(
                            sewerageApplicationDetails.getStateType(), null, null, additionalRule, NEW,
                            WF_INSPECTIONFEE_COLLECTION);
                else
                    wfmatrix = sewerageApplicationWorkflowService.getWfMatrix(sewerageApplicationDetails.getStateType(),
                            null, null, additionalRule, NEW, null);

                sewerageApplicationDetails.transition().progressWithStateCopy()
                        .withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approverComment).withStateValue(wfmatrix.getNextState())
                        .withDateInfo(currentDate.toDate()).withOwner(pos).withNextAction(wfmatrix.getNextAction())
                        .withNatureOfTask(natureOfWork);

            } else if (APPROVEWORKFLOWACTION.equalsIgnoreCase(workFlowAction)
                    && (WF_STATE_PAYMENTDONE.equalsIgnoreCase(sewerageApplicationDetails.getState().getValue())
                    || WF_STATE_EE_APPROVED.equalsIgnoreCase(sewerageApplicationDetails.getState().getValue()))) {
                sewerageApplicationDetails.transition().end()
                        .withSenderName(user.getUsername() + "::" + user.getName()).withComments(approverComment)
                        .withDateInfo(currentDate.toDate()).withNatureOfTask(natureOfWork);
            } else if (WF_STATE_CONNECTION_EXECUTION_BUTTON.equalsIgnoreCase(workFlowAction)) {
                wfmatrix = sewerageApplicationWorkflowService.getWfMatrix(sewerageApplicationDetails.getStateType(),
                        null, null, additionalRule, sewerageApplicationDetails.getCurrentState().getValue(), null);
                sewerageApplicationDetails.setStatus(sewerageTaxUtils.getStatusByCodeAndModuleType(
                        APPLICATION_STATUS_SANCTIONED, MODULETYPE));
                if (wfmatrix.getNextAction().equalsIgnoreCase("END"))
                    sewerageApplicationDetails.transition().end()
                            .withSenderName(user.getUsername() + "::" + user.getName()).withComments(approverComment)
                            .withDateInfo(currentDate.toDate()).withNatureOfTask(natureOfWork);
            } else if (WF_STATE_DEPUTY_EXE_APPROVED.equalsIgnoreCase(sewerageApplicationDetails.getState().getValue())
                    && CHANGEINCLOSETS_NOCOLLECTION.equalsIgnoreCase(additionalRule) && APPROVEWORKFLOWACTION.equalsIgnoreCase(workFlowAction)) {
                sewerageApplicationDetails.setStatus(sewerageTaxUtils.getStatusByCodeAndModuleType(
                        APPLICATION_STATUS_FINALAPPROVED, MODULETYPE));
                sewerageApplicationDetails.transition().end()
                        .withSenderName(user.getUsername() + "::" + user.getName()).withComments(approverComment)
                        .withDateInfo(currentDate.toDate()).withNatureOfTask(natureOfWork);
            } else if (WF_STATE_DEPUTY_EXE_APPROVED.equalsIgnoreCase(sewerageApplicationDetails.getState().getValue())
                    && (NEWSEWERAGECONNECTION.equalsIgnoreCase(additionalRule) || CHANGEINCLOSETS.equalsIgnoreCase(additionalRule))) {
                wfmatrix = sewerageApplicationWorkflowService.getWfMatrix(
                        sewerageApplicationDetails.getStateType(), null, null, additionalRule, sewerageApplicationDetails.getCurrentState().getValue(), null);
                sewerageApplicationDetails.transition().progressWithStateCopy()
                        .withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approverComment).withStateValue(wfmatrix.getNextState())
                        .withDateInfo(currentDate.toDate()).withOwner(user).withNextAction(wfmatrix.getNextAction())
                        .withNatureOfTask(natureOfWork);

            }

            // TODO : Need to check this usecase. 
            else if (approverComment != null && "Receipt Cancelled".equalsIgnoreCase(approverComment)) {
                wfmatrix = sewerageApplicationWorkflowService.getWfMatrix(sewerageApplicationDetails.getStateType(),
                        null, null, additionalRule, WF_STATE_ASSISTANT_APPROVED, null);
                sewerageApplicationDetails.transition().progressWithStateCopy().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approverComment).withStateValue(wfmatrix.getNextState())
                        .withDateInfo(currentDate.toDate()).withOwner(pos).withNextAction(wfmatrix.getNextAction())
                        .withNatureOfTask(natureOfWork);
            } else {
                String pendingActions = null;
                if (sewerageApplicationDetails.getCurrentState().getValue().equalsIgnoreCase(WF_STATE_REJECTED)) {
                    if (sewerageTaxUtils.isInspectionFeeCollectionRequired())
                        pendingActions = WFPA_REJECTED_INSPECTIONFEE_COLLECTION;
                    else
                        pendingActions = WF_STATE_REJECTED;

                    wfmatrix = sewerageApplicationWorkflowService.getWfMatrix(sewerageApplicationDetails.getStateType(),
                            null, null, additionalRule, sewerageApplicationDetails.getCurrentState().getValue(), pendingActions);

                    String nextState = null;
                    if (sewerageTaxUtils.isInspectionFeeCollectionRequired()) {
                        if (sewerageApplicationDetails.getCurrentDemand().getAmtCollected().compareTo(BigDecimal.ZERO) == 0)
                            nextState = WF_STATE_INSPECTIONFEE_PENDING;
                        else
                            nextState = WF_STATE_INSPECTIONFEE_COLLECTED;
                    } else
                        nextState = wfmatrix.getNextState();

                    sewerageApplicationDetails.transition().progressWithStateCopy().withSenderName(user.getUsername() + "::" + user.getName())
                            .withComments(approverComment).withStateValue(nextState)
                            .withDateInfo(currentDate.toDate()).withOwner(pos).withNextAction(wfmatrix.getNextAction())
                            .withNatureOfTask(natureOfWork);

                } else {
                    if (sewerageApplicationDetails.getStatus().getCode().equalsIgnoreCase(APPLICATION_STATUS_ESTIMATENOTICEGEN))
                        pos = sewerageApplicationDetails.getState().getOwnerPosition();
                    wfmatrix = sewerageApplicationWorkflowService.getWfMatrix(sewerageApplicationDetails.getStateType(),
                            null, null, additionalRule,
                            sewerageApplicationDetails.getCurrentState().getValue(), pendingActions);
                    sewerageApplicationDetails.transition().progressWithStateCopy().withSenderName(user.getUsername()
                            + "::" + user.getName()).withComments(approverComment).withStateValue(wfmatrix.getNextState())
                            .withDateInfo(currentDate.toDate()).withOwner(pos).withNextAction(wfmatrix.getNextAction())
                            .withNatureOfTask(natureOfWork);
                }
            }
        }
        if (LOG.isDebugEnabled())
            LOG.debug(" WorkFlow Transition Completed  ...");
    }

    @Override
    public void createCloseConnectionWorkflowTransition(final SewerageApplicationDetails sewerageApplicationDetails) {
        if (LOG.isDebugEnabled())
            LOG.debug(" Create WorkFlow Transition Started  ...");
        final User user = securityUtils.getCurrentUser();
        final DateTime currentDate = new DateTime();
        Position pos;
        Assignment wfInitiator = null;
        String currState = StringUtils.EMPTY;
        WorkFlowMatrix wfmatrix;
        WorkflowContainer workflowContainer = sewerageApplicationDetails.getWorkflowContainer();
        String natureOfWork = sewerageApplicationDetails.getApplicationType().getName();
        String additionalRule = workflowContainer.getAdditionalRule();
        String workFlowAction = workflowContainer.getWorkFlowAction();
        String approverComment = workflowContainer.getApproverComments();
        Long approverPosition = workflowContainer.getApproverPositionId();

        if (sewerageApplicationDetails.getId() != null) {
            wfInitiator = sewerageWorkflowService.getWorkFlowInitiator(sewerageApplicationDetails);
        }
        if (WFLOW_ACTION_STEP_REJECT.equalsIgnoreCase(workFlowAction)) {
            // rejection in b.w workflow, send application to the creator
            final String stateValue = WF_STATE_REJECTED;
            sewerageApplicationDetails.transition().progressWithStateCopy().withSenderName(user.getUsername() + "::" + user.getName())
                    .withComments(approverComment)
                    .withStateValue(stateValue).withDateInfo(currentDate.toDate())
                    .withOwner(wfInitiator.getPosition()).withNextAction("Application Rejected")
                    .withNatureOfTask(natureOfWork);
        } else if (WFLOW_ACTION_STEP_CANCEL.equalsIgnoreCase(workFlowAction)) {
            sewerageApplicationDetails.transition().end().withSenderName(user.getUsername() + "::" + user.getName())
                    .withComments(approverComment).withDateInfo(currentDate.toDate()).withNatureOfTask(natureOfWork);
        } else {
            if (approverPosition != null && approverPosition > 0)
                pos = positionMasterService.getPositionById(approverPosition);
            else
                pos = wfInitiator.getPosition();

            if (sewerageApplicationDetails.getState() == null) {
                wfmatrix = sewerageApplicationWorkflowService.getWfMatrix(
                        sewerageApplicationDetails.getStateType(), null, null, additionalRule, currState, null);
                sewerageApplicationDetails.transition().start()
                        .withSLA(sewerageTaxUtils.getSlaAppConfigValues(sewerageApplicationDetails.getApplicationType()))
                        .withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approverComment)
                        .withStateValue(wfmatrix.getNextState())
                        .withDateInfo(new Date())
                        .withOwner(pos)
                        .withInitiator(sewerageWorkflowService.getWorkFlowInitiator(sewerageApplicationDetails).getPosition())
                        .withNextAction(wfmatrix.getNextAction())
                        .withNatureOfTask(natureOfWork);
            } else if ((WF_STATE_DEPUTY_EXE_APPROVED.equalsIgnoreCase(sewerageApplicationDetails.getState().getValue()) ||
                    WF_STATE_EE_APPROVED.equalsIgnoreCase(sewerageApplicationDetails.getState().getValue()))
                    && APPROVEWORKFLOWACTION.equalsIgnoreCase(workFlowAction)) {
                sewerageApplicationDetails.transition().end()
                        .withSenderName(user.getUsername() + "::" + user.getName()).withComments(approverComment)
                        .withDateInfo(currentDate.toDate()).withNatureOfTask(natureOfWork);
            } else if (WF_STATE_CONNECTION_CLOSE_BUTTON.equalsIgnoreCase(workFlowAction)) {
                wfmatrix = sewerageApplicationWorkflowService.getWfMatrix(sewerageApplicationDetails.getStateType(),
                        null, null, additionalRule,
                        sewerageApplicationDetails.getCurrentState().getValue(), WF_STATE_CLOSECONNECTION_NOTICEGENERATION_PENDING);
                if (wfmatrix.getNextAction().equalsIgnoreCase("END"))
                    sewerageApplicationDetails.transition().end()
                            .withSenderName(user.getUsername() + "::" + user.getName())
                            .withComments(approverComment)
                            .withDateInfo(currentDate.toDate())
                            .withNatureOfTask(natureOfWork);
            } else {
                String pendingActions = null;
                if (sewerageApplicationDetails.getCurrentState().getValue().equalsIgnoreCase(WF_STATE_REJECTED)) {
                    pendingActions = WF_STATE_REJECTED;
                    wfmatrix = sewerageApplicationWorkflowService.getWfMatrix(sewerageApplicationDetails.getStateType(),
                            null, null, additionalRule, sewerageApplicationDetails.getCurrentState().getValue(), pendingActions);
                    String nextState = wfmatrix.getNextState();
                    sewerageApplicationDetails.transition().progressWithStateCopy()
                            .withSenderName(user.getUsername() + "::" + user.getName())
                            .withComments(approverComment)
                            .withStateValue(nextState)
                            .withDateInfo(currentDate.toDate())
                            .withOwner(pos)
                            .withNextAction(wfmatrix.getNextAction())
                            .withNatureOfTask(natureOfWork);

                } else {
                    wfmatrix = sewerageApplicationWorkflowService.getWfMatrix(sewerageApplicationDetails.getStateType(),
                            null, null, additionalRule, sewerageApplicationDetails.getCurrentState().getValue(), pendingActions);
                    sewerageApplicationDetails.transition().progressWithStateCopy()
                            .withSenderName(user.getUsername() + "::" + user.getName())
                            .withComments(approverComment)
                            .withStateValue(wfmatrix.getNextState())
                            .withDateInfo(currentDate.toDate())
                            .withOwner(pos)
                            .withNextAction(wfmatrix.getNextAction())
                            .withNatureOfTask(natureOfWork);
                }
            }
        }
        if (LOG.isDebugEnabled())
            LOG.debug(" WorkFlow Transition Completed  ...");
    }
}