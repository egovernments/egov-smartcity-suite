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
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.infstr.workflow.WorkFlowMatrix;
import org.egov.mrs.domain.entity.Registration;
import org.egov.pims.commons.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistrationWorkflowService {

    private static final String REGISTRATION_ADDNL_RULE = "MARRIAGE REGISTRATION";
    private static final String STATE_NEW = "NEW";
    private static final String STATE_REJECTED = "Rejected";
    private static final String STEP_REJECT = "Reject";
    private static final String STEP_APPROVE = "Approve";
    private static final String STEP_FORWARD = "Forward";

    @Autowired
    private SimpleWorkflowService<Registration> registrationWorkflowService;

    @Autowired
    private PositionMasterService positionMasterService;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    protected AssignmentService assignmentService;

    private enum WorkflowType {
        Registration, ReIssue
    }

    public void transition(StateAware stateAware, WorkflowContainer workflowContainer, String approvalComent) {

        final User user = securityUtils.getCurrentUser();
        final String natureOfTask = "Marriage Registration :: New Registration";

        String currentStateValue = stateAware.getCurrentState() == null ? null : stateAware.getCurrentState().getValue();

        currentStateValue = workflowContainer.getWorkFlowAction().equalsIgnoreCase("reject") ? "Rejected" : currentStateValue;

        WorkFlowMatrix workflowMatrix = null;
        Position nextStateOwner = null;
        String currentState = null;

        if (workflowContainer.getWorkFlowAction().equalsIgnoreCase(STEP_FORWARD)) {
            // FORWARD case, 2 states, when workflow is not started then NEW else next level user

            nextStateOwner = positionMasterService.getPositionById(workflowContainer.getApproverPositionId());

            if (stateAware.getCurrentState() == null) 
                workflowMatrix = registrationWorkflowService.getWfMatrix(WorkflowType.Registration.name(), null, null,
                        REGISTRATION_ADDNL_RULE, STATE_NEW, workflowContainer.getPendingActions());
            else 
                workflowMatrix = registrationWorkflowService.getWfMatrix(WorkflowType.Registration.name(), null, null,
                        REGISTRATION_ADDNL_RULE, "Clerk approved", workflowContainer.getPendingActions());

        } else if (workflowContainer.getWorkFlowAction().equalsIgnoreCase(STEP_REJECT)) {

            // Whatever the level of workflow, whenever rejected should come back to the initiator i.e., from where the workflow
            // started
            nextStateOwner = assignmentService
                    .getPrimaryAssignmentForUser(stateAware.getStateHistory().get(0).getCreatedBy().getId()).getPosition();
            workflowMatrix = registrationWorkflowService.getWfMatrix(WorkflowType.Registration.name(), null, null,
                    REGISTRATION_ADDNL_RULE, STATE_REJECTED, workflowContainer.getPendingActions());

        } else if (workflowContainer.getWorkFlowAction().equalsIgnoreCase(STEP_APPROVE)) {
            
            nextStateOwner = assignmentService.getPrimaryAssignmentForUser(stateAware.getCreatedBy().getId()).getPosition();
            workflowMatrix = registrationWorkflowService.getWfMatrix(WorkflowType.Registration.name(), null, null,
                    REGISTRATION_ADDNL_RULE, stateAware.getCurrentState().getValue(), null);
        }

        
        if (stateAware.getState() == null)
            stateAware.transition().start()
                .withSenderName(user.getUsername() + "::" + user.getName())
                .withComments(approvalComent)
                .withStateValue(workflowMatrix.getNextState())
                .withDateInfo(new Date())
                .withOwner(nextStateOwner)
                .withNextAction(workflowMatrix.getNextAction())
                .withNatureOfTask(natureOfTask);
        else if (stateAware.getCurrentState().getNextAction().equalsIgnoreCase("END"))
            stateAware.transition(true).end().withSenderName(user.getUsername() + "::" + user.getName())
                .withComments(approvalComent).withDateInfo(new Date());
        else
            stateAware.transition(true)
                .withSenderName(user.getUsername() + "::" + user.getName())
                .withComments(approvalComent)
                .withStateValue(workflowMatrix.getNextState())
                .withDateInfo(new Date())
                .withOwner(nextStateOwner)
                .withNextAction(workflowMatrix.getNextAction())
                .withNatureOfTask(natureOfTask);

    }

}
