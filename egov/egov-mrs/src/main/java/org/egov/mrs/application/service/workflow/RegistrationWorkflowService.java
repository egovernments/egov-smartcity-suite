/* eGov suite of products aim to improve the internal efficiency,transparency,
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

package org.egov.mrs.application.service.workflow;

import java.util.Date;

import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.PositionMasterService;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.mrs.application.MarriageConstants;
import org.egov.mrs.domain.entity.MarriageRegistration;
import org.egov.mrs.domain.entity.ReIssue;
import org.egov.pims.commons.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class RegistrationWorkflowService {

    private static final String REGISTRATION_ADDNL_RULE = "MARRIAGE REGISTRATION";
    private static final String STATE_NEW = "NEW";
    private static final String STATE_END = "END";
    private static final String STEP_CANCEL = "Cancel Registration";
    private static final String STEP_CANCEL_REISSUE = "Cancel ReIssue";
    private static final String STEP_REJECT = "Reject";
    private static final String STEP_APPROVE = "Approve";
    private static final String STEP_FORWARD = "Forward";
    private static final String STEP_PRINT_CERTIFICATE = "Print Certificate";
    private static final String APPROVER_REJECTED = "Approver Rejected Application";
    private static final String INITIATOR_INITIAL_STATE = "Revenue Clerk Approval Pending";
    @Autowired
    @Qualifier("workflowService")
    private SimpleWorkflowService<MarriageRegistration> registrationWorkflowService;
    
    @Autowired
    @Qualifier("workflowService")
    private SimpleWorkflowService<ReIssue> reIssueWorkflowService;

    @Autowired
    private PositionMasterService positionMasterService;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    protected AssignmentService assignmentService;

    private enum WorkflowType {
        MarriageRegistration, ReIssue
    }

    public void transition(MarriageRegistration registration, WorkflowContainer workflowContainer, String approvalComent) {

        final User user = securityUtils.getCurrentUser();
        String natureOfTask = "Marriage Registration :: New Registration";
        
        WorkFlowMatrix workflowMatrix = null;
        Position nextStateOwner = null;
        String nextState = null;
        String nextAction = null;

        if (workflowContainer == null) {
            nextStateOwner = assignmentService.getPrimaryAssignmentForUser(registration.getCreatedBy().getId()).getPosition();
            workflowMatrix = registrationWorkflowService.getWfMatrix(WorkflowType.MarriageRegistration.name(), null, null,
                    REGISTRATION_ADDNL_RULE, registration.getCurrentState().getValue(), null);
            nextState = workflowMatrix.getNextState();
            nextAction = workflowMatrix.getNextAction();
        } else {

            if (workflowContainer.getWorkFlowAction().equalsIgnoreCase(STEP_FORWARD)) {
                // FORWARD case, 2 states, when workflow is not started then NEW else next level user

                nextStateOwner = positionMasterService.getPositionById(workflowContainer.getApproverPositionId());
                
                if (registration.getCurrentState() == null)
                    workflowMatrix = registrationWorkflowService.getWfMatrix(WorkflowType.MarriageRegistration.name(), null, null,
                            REGISTRATION_ADDNL_RULE, STATE_NEW, workflowContainer.getPendingActions());
                else
                    workflowMatrix = registrationWorkflowService.getWfMatrix(WorkflowType.MarriageRegistration.name(), null, null,
                            REGISTRATION_ADDNL_RULE, registration.getCurrentState().getValue(), workflowContainer.getPendingActions());
                
                nextState = workflowMatrix.getNextState();
                nextAction = workflowMatrix.getNextAction();

            } else if (workflowContainer.getWorkFlowAction().equalsIgnoreCase(STEP_REJECT)) {

                // Whatever the level of workflow, whenever rejected should come back to the initiator i.e., from where the
                // workflow
                // started
                
                // As there is only one step approval, following line would not work, 
                //nextStateOwner = assignmentService
                //      .getPrimaryAssignmentForUser(itemInWorkflow.getStateHistory().get(0).getCreatedBy().getId()).getPosition();
                nextStateOwner = assignmentService
                      .getPrimaryAssignmentForUser(registration.getCreatedBy().getId()).getPosition();
                
                
                nextState = APPROVER_REJECTED;
                nextAction = INITIATOR_INITIAL_STATE;
            } else if (workflowContainer.getWorkFlowAction().equalsIgnoreCase(STEP_CANCEL) || 
                    workflowContainer.getWorkFlowAction().equalsIgnoreCase(STEP_PRINT_CERTIFICATE)) {
                nextAction = STATE_END;
            } else if (workflowContainer.getWorkFlowAction().equalsIgnoreCase(STEP_APPROVE)) {
                //On Approve, pick workflow matrix based on digital signature configuration
                if(workflowContainer.getPendingActions().equalsIgnoreCase(MarriageConstants.WFLOW_PENDINGACTION_APPRVLPENDING_DIGISIGN))
                    nextStateOwner = this.assignmentService.getPrimaryAssignmentForUser(user.getId()).getPosition();
                else
                    nextStateOwner = assignmentService.getPrimaryAssignmentForUser(registration.getCreatedBy().getId()).getPosition();
                workflowMatrix = registrationWorkflowService.getWfMatrix(WorkflowType.MarriageRegistration.name(), null, null,
                        REGISTRATION_ADDNL_RULE, registration.getCurrentState().getValue(), workflowContainer.getPendingActions());
                 
                nextState = workflowMatrix.getNextState();
                nextAction = workflowMatrix.getNextAction();
            } else if (workflowContainer.getWorkFlowAction().equalsIgnoreCase(MarriageConstants.WFLOW_ACTION_STEP_DIGISIGN)) { 
                nextStateOwner = assignmentService.getPrimaryAssignmentForUser(registration.getCreatedBy().getId()).getPosition();
                workflowMatrix = registrationWorkflowService.getWfMatrix(WorkflowType.MarriageRegistration.name(), null, null,
                        REGISTRATION_ADDNL_RULE, registration.getCurrentState().getValue(), null);
                
                nextState = workflowMatrix.getNextState();
                nextAction = workflowMatrix.getNextAction();
            }
        }

        transition(registration, approvalComent, user, natureOfTask, nextStateOwner, nextState, nextAction); 

    }

    public void transition(ReIssue reIssue, WorkflowContainer workflowContainer, String approvalComent) {

        final User user = securityUtils.getCurrentUser();
        String natureOfTask = "Marriage Registration :: Re-Issue";
        
        WorkFlowMatrix workflowMatrix = null;
        Position nextStateOwner = null;
        String nextState = null;
        String nextAction = null;

        if (workflowContainer == null) {
            nextStateOwner = assignmentService.getPrimaryAssignmentForUser(reIssue.getCreatedBy().getId()).getPosition();
            workflowMatrix = reIssueWorkflowService.getWfMatrix(WorkflowType.ReIssue.name(), null, null,
                    REGISTRATION_ADDNL_RULE, reIssue.getCurrentState().getValue(), null);
            nextState = workflowMatrix.getNextState();
            nextAction = workflowMatrix.getNextAction();
        } else {

            if (workflowContainer.getWorkFlowAction().equalsIgnoreCase(STEP_FORWARD)) {
                // FORWARD case, 2 states, when workflow is not started then NEW else next level user

                nextStateOwner = positionMasterService.getPositionById(workflowContainer.getApproverPositionId());
                
                if (reIssue.getCurrentState() == null)
                    workflowMatrix = reIssueWorkflowService.getWfMatrix(WorkflowType.ReIssue.name(), null, null,
                            REGISTRATION_ADDNL_RULE, STATE_NEW, workflowContainer.getPendingActions());
                else
                    workflowMatrix = reIssueWorkflowService.getWfMatrix(WorkflowType.ReIssue.name(), null, null,
                            REGISTRATION_ADDNL_RULE, reIssue.getCurrentState().getValue(), workflowContainer.getPendingActions());
                
                nextState = workflowMatrix.getNextState();
                nextAction = workflowMatrix.getNextAction();

            } else if (workflowContainer.getWorkFlowAction().equalsIgnoreCase(STEP_REJECT)) {
                // Whatever the level of workflow, whenever rejected should come back to the initiator i.e., from where the
                // workflow
                // started
                
                // As there is only one step approval, following line would not work, 
                //nextStateOwner = assignmentService
                //      .getPrimaryAssignmentForUser(itemInWorkflow.getStateHistory().get(0).getCreatedBy().getId()).getPosition();
                nextStateOwner = assignmentService
                      .getPrimaryAssignmentForUser(reIssue.getCreatedBy().getId()).getPosition();
                
                nextState = APPROVER_REJECTED ;
                nextAction = INITIATOR_INITIAL_STATE;
            }else if (workflowContainer.getWorkFlowAction().equalsIgnoreCase(STEP_CANCEL_REISSUE) ||
                    workflowContainer.getWorkFlowAction().equalsIgnoreCase(STEP_PRINT_CERTIFICATE)) {
                nextAction = STATE_END;
            } else if (workflowContainer.getWorkFlowAction().equalsIgnoreCase(STEP_APPROVE)) {
                //On Approve, pick workflow matrix based on digital signature configuration
                if(workflowContainer.getPendingActions().equalsIgnoreCase(MarriageConstants.WFLOW_PENDINGACTION_APPRVLPENDING_DIGISIGN))
                    nextStateOwner = this.assignmentService.getPrimaryAssignmentForUser(user.getId()).getPosition();
                else
                    nextStateOwner = assignmentService.getPrimaryAssignmentForUser(reIssue.getCreatedBy().getId()).getPosition();
                workflowMatrix = reIssueWorkflowService.getWfMatrix(WorkflowType.ReIssue.name(), null, null,
                        REGISTRATION_ADDNL_RULE, reIssue.getCurrentState().getValue(), workflowContainer.getPendingActions());
                 
                nextState = workflowMatrix.getNextState();
                nextAction = workflowMatrix.getNextAction();
            } else if (workflowContainer.getWorkFlowAction().equalsIgnoreCase(MarriageConstants.WFLOW_ACTION_STEP_DIGISIGN)) { 
                nextStateOwner = assignmentService.getPrimaryAssignmentForUser(reIssue.getCreatedBy().getId()).getPosition();
                workflowMatrix = reIssueWorkflowService.getWfMatrix(WorkflowType.ReIssue.name(), null, null,
                        REGISTRATION_ADDNL_RULE, reIssue.getCurrentState().getValue(), null);
                
                nextState = workflowMatrix.getNextState();
                nextAction = workflowMatrix.getNextAction();
            }
        }

        transition(reIssue, approvalComent, user, natureOfTask, nextStateOwner, nextState, nextAction);

    }
    
    private void transition(StateAware itemInWorkflow, String approvalComent, final User user, String natureOfTask,
            Position nextStateOwner, String nextState, String nextAction) {
        if (itemInWorkflow.getState() == null)
            itemInWorkflow.transition().start()
                    .withSenderName(user.getUsername() + "::" + user.getName())
                    .withComments(approvalComent)
                    .withStateValue(nextState)
                    .withDateInfo(new Date())
                    .withOwner(nextStateOwner)
                    .withNextAction(nextAction)
                    .withNatureOfTask(natureOfTask);
        else if (nextAction.equalsIgnoreCase(STATE_END))
            itemInWorkflow.transition(true).end().withSenderName(user.getUsername() + "::" + user.getName())
                    .withComments(approvalComent).withDateInfo(new Date());
        else
            itemInWorkflow.transition(true)
                    .withSenderName(user.getUsername() + "::" + user.getName())
                    .withComments(approvalComent)
                    .withStateValue(nextState)
                    .withDateInfo(new Date())
                    .withOwner(nextStateOwner)
                    .withNextAction(nextAction)
                    .withNatureOfTask(natureOfTask);
    }

}