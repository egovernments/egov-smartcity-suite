/*******************************************************************************
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
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org
 ******************************************************************************/
package org.egov.stms.transactions.workflow;

import java.math.BigDecimal;
import java.util.Date;

import org.egov.commons.entity.Source;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.pims.commons.Position;
import org.egov.stms.transactions.entity.SewerageApplicationDetails;
import org.egov.stms.transactions.service.SewerageWorkflowService;
import org.egov.stms.utils.SewerageTaxUtils;
import org.egov.stms.utils.constants.SewerageTaxConstants;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * The Class ApplicationCommonWorkflow.
 */
public abstract class ApplicationWorkflowCustomImpl implements ApplicationWorkflowCustom {

    private static final String THIRD_PARTY_OPERATOR_CREATED = "Third Party operator created";

    private static final String NEW = "NEW";

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationWorkflowCustomImpl.class);

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private PositionMasterService positionMasterService;

    @Autowired
    private SewerageTaxUtils sewerageTaxUtils;
   
    @Autowired
    private SewerageWorkflowService sewerageWorkflowService;

    @Autowired
    @Qualifier("workflowService")
    private SimpleWorkflowService<SewerageApplicationDetails> sewerageApplicationWorkflowService;

    @Autowired
    public ApplicationWorkflowCustomImpl() {

    }
    
    @Override
    public void createCommonWorkflowTransition(final SewerageApplicationDetails sewerageApplicationDetails,
            final Long approvalPosition, final String approvalComent, final String additionalRule,
            final String workFlowAction) {
        if (LOG.isDebugEnabled())
            LOG.debug(" Create WorkFlow Transition Started  ...");
        final User user = securityUtils.getCurrentUser();
        final DateTime currentDate = new DateTime();
        Position pos = null;
        Assignment wfInitiator = null;
        String currState = "";
        WorkFlowMatrix wfmatrix = null;
        
        String natureOfwork =  sewerageApplicationDetails.getApplicationType().getName();

        if (null != sewerageApplicationDetails.getId()) {
            wfInitiator = sewerageWorkflowService.getWorkFlowInitiator(sewerageApplicationDetails);
        }
        if (SewerageTaxConstants.WFLOW_ACTION_STEP_REJECT.equalsIgnoreCase(workFlowAction)) {
            // rejection in b.w workflow, send application to the creator
                final String stateValue = SewerageTaxConstants.WF_STATE_REJECTED;
                
                if (sewerageTaxUtils.isInspectionFeeCollectionRequired()) {
                    wfmatrix = sewerageApplicationWorkflowService.getWfMatrix( 
                            sewerageApplicationDetails.getStateType(), null, null, additionalRule, stateValue,
                            SewerageTaxConstants.WFPA_REJECTED_INSPECTIONFEE_COLLECTION);
                    //throw new ValidationException(new ValidationException() );
                } else {    //pick workflowmatrix without inspecitonfee
                    wfmatrix = sewerageApplicationWorkflowService.getWfMatrix(
                         sewerageApplicationDetails.getStateType(), null, null, additionalRule, stateValue, SewerageTaxConstants.WF_STATE_REJECTED);
                } 
                sewerageApplicationDetails.transition().progressWithStateCopy().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent)
                        .withStateValue(stateValue).withDateInfo(currentDate.toDate())
                        .withOwner(wfInitiator.getPosition()).withNextAction("Application Rejected")
                        .withNatureOfTask(natureOfwork);
        } else if(SewerageTaxConstants.WFLOW_ACTION_STEP_CANCEL.equalsIgnoreCase(workFlowAction)){
            // Incase of reject / cancel from the creator, end the workflow
            sewerageApplicationDetails.transition().end().withSenderName(user.getUsername() + "::" + user.getName())
                    .withComments(approvalComent).withDateInfo(currentDate.toDate()).withNatureOfTask(natureOfwork);
        } else {
            if (null != approvalPosition && approvalPosition != -1 && !approvalPosition.equals(Long.valueOf(0)))
                pos = positionMasterService.getPositionById(approvalPosition);
            else
                pos = wfInitiator.getPosition();

            Boolean cscOperatorLoggedIn = sewerageWorkflowService.isCscOperator(user);

            if (null == sewerageApplicationDetails.getState() && cscOperatorLoggedIn) {
                currState = THIRD_PARTY_OPERATOR_CREATED;
                wfmatrix = sewerageApplicationWorkflowService.getWfMatrix(sewerageApplicationDetails.getStateType(), null,
                        null, additionalRule, currState, null);
                sewerageApplicationDetails.transition().start()
                        .withSenderName(user.getUsername() + "::" + user.getName()).withComments(approvalComent)
                        .withComments(approvalComent).withInitiator(wfInitiator != null ? wfInitiator.getPosition() : null)
                        .withStateValue(wfmatrix.getNextState()).withDateInfo(new Date()).withOwner(pos)
                        .withNextAction(wfmatrix.getNextAction()).withNatureOfTask(natureOfwork);
            } else if ((NEW.equalsIgnoreCase(sewerageApplicationDetails.getState().getValue())
                    && SewerageTaxConstants.APPLICATION_STATUS_COLLECTINSPECTIONFEE
                            .equalsIgnoreCase(sewerageApplicationDetails.getStatus().getCode()))
                    &&
                    sewerageWorkflowService.isCscOperator(sewerageApplicationDetails.getCreatedBy())) {
                  if (sewerageTaxUtils.isInspectionFeeCollectionRequired()) {
                    wfmatrix = sewerageApplicationWorkflowService.getWfMatrix(
                            sewerageApplicationDetails.getStateType(), null, null, additionalRule, NEW,
                            SewerageTaxConstants.WF_INSPECTIONFEE_COLLECTION);
                } else {    // pick workflowmatrix without inspecitonfee
                    wfmatrix = sewerageApplicationWorkflowService.getWfMatrix(
                            sewerageApplicationDetails.getStateType(), null, null, additionalRule, NEW, null);
                }
                sewerageApplicationDetails.transition().progressWithStateCopy()
                        .withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withStateValue(wfmatrix.getNextState())
                        .withDateInfo(currentDate.toDate()).withOwner(pos).withNextAction(wfmatrix.getNextAction())
                        .withNatureOfTask(natureOfwork);

            }else if (null == sewerageApplicationDetails.getState()){    // New Entry
                // If Inspection is configured, pick with inspection fee workflowmatrix by passing pendingaction
                if (sewerageTaxUtils.isInspectionFeeCollectionRequired()) {
                    wfmatrix = sewerageApplicationWorkflowService.getWfMatrix(
                            sewerageApplicationDetails.getStateType(), null, null, additionalRule, currState,
                            SewerageTaxConstants.WF_INSPECTIONFEE_COLLECTION);
                } else {    //pick workflowmatrix without inspecitonfee
                    wfmatrix = sewerageApplicationWorkflowService.getWfMatrix(
                            sewerageApplicationDetails.getStateType(), null, null, additionalRule, currState, null);
                }
                sewerageApplicationDetails.transition().start()
                        .withSenderName(user.getUsername() + "::" + user.getName()).withComments(approvalComent)
                        .withStateValue(wfmatrix.getNextState()).withDateInfo(new Date()).withOwner(pos)
                        .withNextAction(wfmatrix.getNextAction()).withNatureOfTask(natureOfwork);
            }  // End workflow on execute connection
            else if (SewerageTaxConstants.WF_STATE_CONNECTION_EXECUTION_BUTTON.equalsIgnoreCase(workFlowAction)) {
                wfmatrix = sewerageApplicationWorkflowService.getWfMatrix(sewerageApplicationDetails.getStateType(),
                        null, null, additionalRule, sewerageApplicationDetails.getCurrentState().getValue(), null);
                sewerageApplicationDetails.setStatus(sewerageTaxUtils.getStatusByCodeAndModuleType(
                        SewerageTaxConstants.APPLICATION_STATUS_SANCTIONED, SewerageTaxConstants.MODULETYPE));
                if (wfmatrix.getNextAction().equalsIgnoreCase("END"))
                    sewerageApplicationDetails.transition().end()
                            .withSenderName(user.getUsername() + "::" + user.getName()).withComments(approvalComent)
                            .withDateInfo(currentDate.toDate()).withNatureOfTask(natureOfwork);
            } // TODO : Need to check this usecase. 
            else if (null != approvalComent && "Receipt Cancelled".equalsIgnoreCase(approvalComent)) {  
                wfmatrix = sewerageApplicationWorkflowService.getWfMatrix(sewerageApplicationDetails.getStateType(),
                        null, null, additionalRule, SewerageTaxConstants.WF_STATE_ASSISTANT_APPROVED, null);
                sewerageApplicationDetails.transition().progressWithStateCopy().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withStateValue(wfmatrix.getNextState())
                        .withDateInfo(currentDate.toDate()).withOwner(pos).withNextAction(wfmatrix.getNextAction())
                        .withNatureOfTask(natureOfwork);
            } else {
                String pendingActions=null;
                
                if (sewerageApplicationDetails.getCurrentState().getValue().equalsIgnoreCase(SewerageTaxConstants.WF_STATE_REJECTED)){
                    if(sewerageTaxUtils.isInspectionFeeCollectionRequired()) {
                        pendingActions = SewerageTaxConstants.WFPA_REJECTED_INSPECTIONFEE_COLLECTION ; 
                    } else {
                        pendingActions = SewerageTaxConstants.WF_STATE_REJECTED ; 
                    } 
                    wfmatrix = sewerageApplicationWorkflowService.getWfMatrix(sewerageApplicationDetails.getStateType(),
                            null, null, additionalRule, sewerageApplicationDetails.getCurrentState().getValue(), pendingActions);
                    
                    String nextState = null;
                    if(sewerageTaxUtils.isInspectionFeeCollectionRequired()) {
                        if(sewerageApplicationDetails.getCurrentDemand().getAmtCollected().compareTo(BigDecimal.ZERO) == 0)
                            nextState=SewerageTaxConstants.WF_STATE_INSPECTIONFEE_PENDING;
                        else
                            nextState=SewerageTaxConstants.WF_STATE_INSPECTIONFEE_COLLECTED;
                    } else
                        nextState= wfmatrix.getNextState();
                    
                    sewerageApplicationDetails.transition().progressWithStateCopy().withSenderName(user.getUsername() + "::" + user.getName())
                    .withComments(approvalComent).withStateValue(nextState)
                    .withDateInfo(currentDate.toDate()).withOwner(pos).withNextAction(wfmatrix.getNextAction())
                    .withNatureOfTask(natureOfwork);
                    
                }  else{ 
                        wfmatrix = sewerageApplicationWorkflowService.getWfMatrix(sewerageApplicationDetails.getStateType(),
                        null, null, additionalRule, sewerageApplicationDetails.getCurrentState().getValue(), pendingActions);
                
                        sewerageApplicationDetails.transition().progressWithStateCopy().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withStateValue(wfmatrix.getNextState())
                        .withDateInfo(currentDate.toDate()).withOwner(pos).withNextAction(wfmatrix.getNextAction())
                        .withNatureOfTask(natureOfwork);
                }
            }  
        }
        if (LOG.isDebugEnabled())
            LOG.debug(" WorkFlow Transition Completed  ...");
    }
    
    @Override
    public void createCloseConnectionWorkflowTransition(final SewerageApplicationDetails sewerageApplicationDetails,
            final Long approvalPosition, final String approvalComent, final String additionalRule,
             String workFlowAction) {
        if (LOG.isDebugEnabled())
            LOG.debug(" Create WorkFlow Transition Started  ..."); 
        final User user = securityUtils.getCurrentUser();
        final DateTime currentDate = new DateTime();
        Position pos = null;
        Assignment wfInitiator = null;
        String currState = "";
        WorkFlowMatrix wfmatrix = null;
        
        String natureOfwork =  sewerageApplicationDetails.getApplicationType().getName();

        if (null != sewerageApplicationDetails.getId()) {
            wfInitiator = sewerageWorkflowService.getWorkFlowInitiator(sewerageApplicationDetails);
        }
            if (SewerageTaxConstants.WFLOW_ACTION_STEP_REJECT.equalsIgnoreCase(workFlowAction)) {
                // rejection in b.w workflow, send application to the creator
                    final String stateValue = SewerageTaxConstants.WF_STATE_REJECTED;
                    wfmatrix = sewerageApplicationWorkflowService.getWfMatrix(
                         sewerageApplicationDetails.getStateType(), null, null, additionalRule, stateValue, SewerageTaxConstants.WF_STATE_REJECTED);
                    sewerageApplicationDetails.transition().progressWithStateCopy().withSenderName(user.getUsername() + "::" + user.getName())
                            .withComments(approvalComent)
                            .withStateValue(stateValue).withDateInfo(currentDate.toDate())
                            .withOwner(wfInitiator.getPosition()).withNextAction("Application Rejected")
                            .withNatureOfTask(natureOfwork);
            } else if(SewerageTaxConstants.WFLOW_ACTION_STEP_CANCEL.equalsIgnoreCase(workFlowAction)){
                // Incase of reject / cancel from the creator, end the workflow
                sewerageApplicationDetails.transition().end().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withDateInfo(currentDate.toDate()).withNatureOfTask(natureOfwork);
            } else {
                if (null != approvalPosition && approvalPosition != -1 && !approvalPosition.equals(Long.valueOf(0)))
                    pos = positionMasterService.getPositionById(approvalPosition);
                else
                    pos = wfInitiator.getPosition();
                
                 // New Entry
                if (null == sewerageApplicationDetails.getState()) { 
                    wfmatrix = sewerageApplicationWorkflowService.getWfMatrix(
                            sewerageApplicationDetails.getStateType(), null, null, additionalRule, currState, null);
                    sewerageApplicationDetails.transition().start()
                            .withSenderName(user.getUsername() + "::" + user.getName()).withComments(approvalComent)
                            .withStateValue(wfmatrix.getNextState()).withDateInfo(new Date()).withOwner(pos)
                            .withNextAction(wfmatrix.getNextAction()).withNatureOfTask(natureOfwork);
                }  // End workflow on execute connection
                else if (SewerageTaxConstants.WF_STATE_CONNECTION_CLOSE_BUTTON.equalsIgnoreCase(workFlowAction)) {
                    wfmatrix = sewerageApplicationWorkflowService.getWfMatrix(sewerageApplicationDetails.getStateType(),
                            null, null, additionalRule, sewerageApplicationDetails.getCurrentState().getValue(), SewerageTaxConstants.WF_STATE_CLOSECONNECTION_NOTICEGENERATION_PENDING);
                    if (wfmatrix.getNextAction().equalsIgnoreCase("END"))
                        sewerageApplicationDetails.transition().end()
                                .withSenderName(user.getUsername() + "::" + user.getName()).withComments(approvalComent)
                                .withDateInfo(currentDate.toDate()).withNatureOfTask(natureOfwork);
                } 
                else {
                    String pendingActions=null;
                    if (sewerageApplicationDetails.getCurrentState().getValue().equalsIgnoreCase(SewerageTaxConstants.WF_STATE_REJECTED)){
                        pendingActions = SewerageTaxConstants.WF_STATE_REJECTED ; 
                        wfmatrix = sewerageApplicationWorkflowService.getWfMatrix(sewerageApplicationDetails.getStateType(),
                                null, null, additionalRule, sewerageApplicationDetails.getCurrentState().getValue(), pendingActions);
                        String nextState = wfmatrix.getNextState();
                        sewerageApplicationDetails.transition().progressWithStateCopy().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withStateValue(nextState)
                        .withDateInfo(currentDate.toDate()).withOwner(pos).withNextAction(wfmatrix.getNextAction())
                        .withNatureOfTask(natureOfwork);
                        
                    }  else{ 
                            wfmatrix = sewerageApplicationWorkflowService.getWfMatrix(sewerageApplicationDetails.getStateType(),
                            null, null, additionalRule, sewerageApplicationDetails.getCurrentState().getValue(), pendingActions);
                            sewerageApplicationDetails.transition().progressWithStateCopy().withSenderName(user.getUsername() + "::" + user.getName())
                            .withComments(approvalComent).withStateValue(wfmatrix.getNextState())
                            .withDateInfo(currentDate.toDate()).withOwner(pos).withNextAction(wfmatrix.getNextAction())
                            .withNatureOfTask(natureOfwork);
                    }
                }  
            }
            if (LOG.isDebugEnabled())
                LOG.debug(" WorkFlow Transition Completed  ...");
        }
}