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

package org.egov.mrs.application.service.workflow;

import static org.egov.mrs.application.MarriageConstants.ANONYMOUS_USER;
import static org.egov.mrs.application.MarriageConstants.MODULE_NAME;
import static org.egov.mrs.application.MarriageConstants.MRG_ROLEFORNONEMPLOYEE;
import static org.egov.mrs.application.MarriageConstants.MRG_WORKFLOWDEPARTEMENT_FOR_CSCOPERATOR;
import static org.egov.mrs.application.MarriageConstants.MRG_WORKFLOWDESIGNATION_FOR_CSCOPERATOR;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.egov.commons.entity.Source;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.DesignationService;
import org.egov.eis.service.PositionMasterService;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.mrs.application.MarriageConstants;
import org.egov.mrs.domain.entity.MarriageRegistration;
import org.egov.mrs.domain.entity.ReIssue;
import org.egov.pims.commons.Designation;
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
    private SimpleWorkflowService<MarriageRegistration> marriageRegistrationWorkflowService;

    @Autowired
    @Qualifier("workflowService")
    private SimpleWorkflowService<ReIssue> reIssueWorkflowService;

    @Autowired
    private PositionMasterService positionMasterService;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    protected AssignmentService assignmentService;

    @Autowired
    private AppConfigValueService appConfigValuesService;
    @Autowired
    private DesignationService designationService;
    @Autowired
    private DepartmentService departmentService;

    private enum WorkflowType {
        MarriageRegistration, ReIssue
    }

    public void transition(final MarriageRegistration registration, final WorkflowContainer workflowContainer,
            final String approvalComent) {

        final User user = securityUtils.getCurrentUser();
        final String natureOfTask = "Marriage Registration :: New Registration";
        WorkFlowMatrix workflowMatrix;
        Position nextStateOwner = null;
        String nextState = null;
        String nextAction = null;
        String currentState;
        Assignment assignment = getWorkFlowInitiator(registration);

        final Boolean isCscOperator = isCscOperator(user);
        boolean loggedUserIsMeesevaUser = isMeesevaUser(user);
        boolean citizenPortalUser = isCitizenPortalUser(user);

        // In case of CSC Operator or online user or meeseva  will execute this block 
        if (isCscOperator || ANONYMOUS_USER.equalsIgnoreCase(securityUtils.getCurrentUser().getName()) || loggedUserIsMeesevaUser||citizenPortalUser ) {
            currentState = MarriageConstants.CSC_OPERATOR_CREATED;
            nextStateOwner = positionMasterService.getPositionById(workflowContainer.getApproverPositionId());
            if (nextStateOwner != null) {
                final List<Assignment> assignmentList = assignmentService.getAssignmentsForPosition(nextStateOwner.getId());
                assignment = !assignmentList.isEmpty() ? assignmentList.get(0) : null;
            }
            workflowMatrix = marriageRegistrationWorkflowService.getWfMatrix(WorkflowType.MarriageRegistration.name(), null, null,
                    REGISTRATION_ADDNL_RULE, currentState, null);
            nextState = workflowMatrix.getNextState();
            nextAction = workflowMatrix.getNextAction();
            if(org.apache.commons.lang.StringUtils.isBlank(registration.getSource()) || !loggedUserIsMeesevaUser)
             if(isCscOperator)
                 registration.setSource(Source.CSC.toString());
             else if(citizenPortalUser)
                 registration.setSource(Source.CITIZENPORTAL.toString());
             else
                 registration.setSource(MarriageConstants.SOURCE_ONLINE);    
        }

        else if (workflowContainer == null) {
            nextStateOwner = assignmentService.getPrimaryAssignmentForUser(registration.getCreatedBy().getId()).getPosition();
            workflowMatrix = marriageRegistrationWorkflowService.getWfMatrix(WorkflowType.MarriageRegistration.name(), null, null,
                    REGISTRATION_ADDNL_RULE, registration.getCurrentState().getValue(), null);
            nextState = workflowMatrix.getNextState();
            nextAction = workflowMatrix.getNextAction();
        } else if (workflowContainer.getWorkFlowAction().equalsIgnoreCase(STEP_FORWARD)) {
            // FORWARD case, 2 states, when workflow is not started then NEW else next level user

            nextStateOwner = positionMasterService.getPositionById(workflowContainer.getApproverPositionId());

            if (registration.getCurrentState() == null)
                workflowMatrix = marriageRegistrationWorkflowService.getWfMatrix(WorkflowType.MarriageRegistration.name(), null,
                        null,
                        REGISTRATION_ADDNL_RULE, STATE_NEW, workflowContainer.getPendingActions());
            else
                workflowMatrix = marriageRegistrationWorkflowService.getWfMatrix(WorkflowType.MarriageRegistration.name(), null,
                        null,
                        REGISTRATION_ADDNL_RULE, registration.getCurrentState().getValue(),
                        null);

            nextState = workflowMatrix.getNextState();
            nextAction = workflowMatrix.getNextAction();

        } else if (workflowContainer.getWorkFlowAction().equalsIgnoreCase(STEP_REJECT)) {

            // Whatever the level of workflow, whenever rejected should come back to the initiator i.e., from where the
            // workflow
            // started

            // As there is only one step approval, following line would not work,

            nextStateOwner = assignment != null ? assignment.getPosition() : null;
            nextState = APPROVER_REJECTED;
            nextAction = INITIATOR_INITIAL_STATE;
        } else if (workflowContainer.getWorkFlowAction().equalsIgnoreCase(STEP_CANCEL) ||
                workflowContainer.getWorkFlowAction().equalsIgnoreCase(STEP_PRINT_CERTIFICATE))
            nextAction = STATE_END;
        else if (workflowContainer.getWorkFlowAction().equalsIgnoreCase(STEP_APPROVE)) {
            // On Approve, pick workflow matrix based on digital signature configuration
            if (workflowContainer.getPendingActions()
                    .equalsIgnoreCase(MarriageConstants.WFLOW_PENDINGACTION_APPRVLPENDING_DIGISIGN)){
                nextStateOwner = assignmentService.getPrimaryAssignmentForUser(user.getId()).getPosition();
                workflowMatrix = marriageRegistrationWorkflowService.getWfMatrix(WorkflowType.MarriageRegistration.name(), null, null,
                        REGISTRATION_ADDNL_RULE, registration.getCurrentState().getValue(),
                        workflowContainer.getPendingActions());

                nextState = workflowMatrix.getNextState();
                nextAction = workflowMatrix.getNextAction();}
                
            else
               // nextStateOwner = assignment != null ? assignment.getPosition() : null;
           
            nextAction = STATE_END;


        } else if (workflowContainer.getWorkFlowAction().equalsIgnoreCase(MarriageConstants.WFLOW_ACTION_STEP_DIGISIGN)) {
            /*nextStateOwner = assignment != null ? assignment.getPosition() : null;
            workflowMatrix = marriageRegistrationWorkflowService.getWfMatrix(WorkflowType.MarriageRegistration.name(), null, null,
                    REGISTRATION_ADDNL_RULE, registration.getCurrentState().getValue(), null);

            nextState = workflowMatrix.getNextState();
            nextAction = workflowMatrix.getNextAction();*/
            nextAction = STATE_END;
        }

        transition(registration, approvalComent, user, natureOfTask, nextStateOwner, nextState, nextAction, assignment);

    }

    public void transition(final ReIssue reIssue, final WorkflowContainer workflowContainer, final String approvalComent) {

        final User user = securityUtils.getCurrentUser();
        final String natureOfTask = "Marriage Registration :: Re-Issue";

        WorkFlowMatrix workflowMatrix;
        Position nextStateOwner = null;
        String nextState = null;
        String nextAction = null;
        String currentState;
        Assignment assignment = getWorkFlowInitiatorForReissue(reIssue);
        final Boolean isCscOperator = isCscOperator(user);
        boolean citizenPortalUser = isCitizenPortalUser(user);
        // In case of CSC Operator will execute this block
        if (isCscOperator || ANONYMOUS_USER.equalsIgnoreCase(securityUtils.getCurrentUser().getName())|| citizenPortalUser) {
            currentState = MarriageConstants.CSC_OPERATOR_CREATED;
            nextStateOwner = positionMasterService.getPositionById(workflowContainer.getApproverPositionId());
            if (nextStateOwner != null) {
                final List<Assignment> assignmentList = assignmentService.getAssignmentsForPosition(nextStateOwner.getId());
                assignment = !assignmentList.isEmpty() ? assignmentList.get(0) : null;
            }
            workflowMatrix = marriageRegistrationWorkflowService.getWfMatrix(WorkflowType.ReIssue.name(), null, null,
                    REGISTRATION_ADDNL_RULE, currentState, null);
            nextState = workflowMatrix.getNextState();
            nextAction = workflowMatrix.getNextAction();
            if (citizenPortalUser)
                reIssue.setSource(Source.CITIZENPORTAL.name());
            else
                reIssue.setSource(isCscOperator ? Source.CSC.name() : MarriageConstants.SOURCE_ONLINE);

        } else if (workflowContainer == null) {
            nextStateOwner = assignment != null ? assignment.getPosition() : null;
            workflowMatrix = reIssueWorkflowService.getWfMatrix(WorkflowType.ReIssue.name(), null, null,
                    REGISTRATION_ADDNL_RULE, reIssue.getCurrentState().getValue(), null);
            nextState = workflowMatrix.getNextState();
            nextAction = workflowMatrix.getNextAction();
        } else if (workflowContainer.getWorkFlowAction().equalsIgnoreCase(STEP_FORWARD)) {
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

            nextStateOwner = assignment != null ? assignment.getPosition() : null;
            nextState = APPROVER_REJECTED;
            nextAction = INITIATOR_INITIAL_STATE;
        } else if (workflowContainer.getWorkFlowAction().equalsIgnoreCase(STEP_CANCEL_REISSUE) ||
                workflowContainer.getWorkFlowAction().equalsIgnoreCase(STEP_PRINT_CERTIFICATE))
            nextAction = STATE_END;
        else if (workflowContainer.getWorkFlowAction().equalsIgnoreCase(STEP_APPROVE)) {
            // On Approve, pick workflow matrix based on digital signature configuration
            if (workflowContainer.getPendingActions()
                    .equalsIgnoreCase(MarriageConstants.WFLOW_PENDINGACTION_APPRVLPENDING_DIGISIGN)) {
                nextStateOwner = assignmentService.getPrimaryAssignmentForUser(user.getId()).getPosition();
                workflowMatrix = reIssueWorkflowService.getWfMatrix(WorkflowType.ReIssue.name(), null, null,
                        REGISTRATION_ADDNL_RULE, reIssue.getCurrentState().getValue(), workflowContainer.getPendingActions());

                nextState = workflowMatrix.getNextState();
                nextAction = workflowMatrix.getNextAction();

            } else
                nextAction = STATE_END;
        } else if (workflowContainer.getWorkFlowAction().equalsIgnoreCase(MarriageConstants.WFLOW_ACTION_STEP_DIGISIGN)) {
            nextAction = STATE_END;
        }

        transition(reIssue, approvalComent, user, natureOfTask, nextStateOwner, nextState, nextAction, assignment);

    }

    private void transition(final StateAware itemInWorkflow, final String approvalComent, final User user,
            final String natureOfTask,
            final Position nextStateOwner, final String nextState, final String nextAction, final Assignment assignment) {
        if (itemInWorkflow.getState() == null)
            itemInWorkflow.transition().start()
                    .withSenderName(user.getUsername() + "::" + user.getName())
                    .withComments(approvalComent)
                    .withStateValue(nextState)
                    .withDateInfo(new Date())
                    .withOwner(nextStateOwner)
                    .withInitiator(assignment != null ? assignment.getPosition() : null)
                    .withNextAction(nextAction)
                    .withNatureOfTask(natureOfTask);
        else if (nextAction != null && nextAction.equalsIgnoreCase(STATE_END))
            itemInWorkflow.transition().end().withSenderName(user.getUsername() + "::" + user.getName()).withNextAction(STATE_END)
                    .withComments(approvalComent).withDateInfo(new Date());
        else
            itemInWorkflow.transition().progressWithStateCopy()
                    .withSenderName(user.getUsername() + "::" + user.getName())
                    .withComments(approvalComent)
                    .withStateValue(nextState)
                    .withDateInfo(new Date())
                    .withOwner(nextStateOwner)
                    .withNextAction(nextAction)
                    .withNatureOfTask(natureOfTask);
    }

    /**
     * Returns Designation for third party user
     *
     * @return
     */
    public String getDesignationForThirdPartyUser() {
        final List<AppConfigValues> appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(MODULE_NAME,
                MRG_ROLEFORNONEMPLOYEE);
        return !appConfigValue.isEmpty() ? appConfigValue.get(0).getValue() : org.apache.commons.lang.StringUtils.EMPTY;
    }

    /**
     * Checks whether user is an employee or not
     *
     * @param user
     * @return
     */
    public Boolean isEmployee(final User user) {
        for (final Role role : user.getRoles())
            for (final AppConfigValues appconfig : getThirdPartyUserRoles())
                if (role != null && appconfig != null && role.getName().equals(appconfig.getValue()))
                    return false;
        return true;
    }

    /**
     * Returns third party user roles
     *
     * @return
     */
    public List<AppConfigValues> getThirdPartyUserRoles() {
        final List<AppConfigValues> appConfigValueList = appConfigValuesService.getConfigValuesByModuleAndKey(
                MODULE_NAME, MRG_ROLEFORNONEMPLOYEE);
        return !appConfigValueList.isEmpty() ? appConfigValueList : Collections.emptyList();
    }

    /**
     * Checks whether user is csc operator or not
     *
     * @param user
     * @return
     */
    public Boolean isCscOperator(final User user) {
        final List<AppConfigValues> appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(MODULE_NAME,
                MRG_ROLEFORNONEMPLOYEE);
        final String rolesForNonEmployee = !appConfigValue.isEmpty() ? appConfigValue.get(0).getValue() : null;
        for (final Role role : user.getRoles())
            if (role != null && rolesForNonEmployee != null && role.getName().equalsIgnoreCase(rolesForNonEmployee))
                return true;
        return false;
    }
    
    /**
     * Checks whether user is csc operator or not
     *
     * @param user
     * @return
     */
    public Boolean isCitizenPortalUser(final User user) {
        for (final Role role : user.getRoles())
            if (role != null && role.getName().equalsIgnoreCase(MarriageConstants.ROLE_CITIZEN))
                return true;
        return false;
    }
    
    /**
     * Returns Designation for property tax csc operator workflow
     *
     * @return
     */
    public String getDesignationForCscOperatorWorkFlow() {
        final List<AppConfigValues> appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(MODULE_NAME,
                MRG_WORKFLOWDESIGNATION_FOR_CSCOPERATOR);
        return !appConfigValue.isEmpty() ? appConfigValue.get(0).getValue() : null;
    }

    /**
     * Returns Department for property tax csc operator workflow
     *
     * @return
     */
    public String getDepartmentForCscOperatorWorkFlow() {
        final List<AppConfigValues> appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(MODULE_NAME,
                MRG_WORKFLOWDEPARTEMENT_FOR_CSCOPERATOR);
        return !appConfigValue.isEmpty() ? appConfigValue.get(0).getValue() : null;
    }

    public Assignment getUserPositionByZone(final MarriageRegistration marriageRegistration, final ReIssue reIssue) {
        final String designationStr = getDesignationForCscOperatorWorkFlow();
        final String departmentStr = getDepartmentForCscOperatorWorkFlow();
        final String[] department = departmentStr.split(",");
        final String[] designation = designationStr.split(",");
        List<Assignment> assignment = new ArrayList<>();
        Long boundaryId = null;
        if (reIssue == null)
            boundaryId = marriageRegistration != null && marriageRegistration.getZone() != null
                    ? marriageRegistration.getZone().getId() : null;
        else if (marriageRegistration == null)
            boundaryId = reIssue != null && reIssue.getZone() != null ? reIssue.getZone().getId() : null;

        for (final String dept : department) {
            Long deptId = null;
            final Department deptObject = departmentService.getDepartmentByName(dept);
            if (deptObject != null)
                deptId = departmentService.getDepartmentByName(dept).getId();
            if (deptId != null)
                for (final String desg : designation) {
                    final Designation desigObject = designationService.getDesignationByName(desg);
                    if (desigObject != null) {
                        if (boundaryId == null)
                            assignment = assignmentService.findByDepartmentAndDesignation(deptId, desigObject.getId());
                        else
                            assignment = assignmentService.findAssignmentByDepartmentDesignationAndBoundary(deptId,
                                    desigObject.getId(),
                                    boundaryId);
                        if (!assignment.isEmpty())
                            break;
                    }

                }
            if (!assignment.isEmpty())
                break;
        }
        return !assignment.isEmpty() ? assignment.get(0) : null;
    }

    public Assignment getMappedAssignmentForCscOperator(final MarriageRegistration marriageRegistration, final ReIssue reIssue) {

        return getUserPositionByZone(marriageRegistration, reIssue);
    }

    public Assignment getWorkFlowInitiator(final MarriageRegistration marriageRegistration) {
        if (marriageRegistration != null)
            if (marriageRegistration.getState() != null
                    && marriageRegistration.getState().getInitiatorPosition() != null) {
                final List<Assignment> assignmentList = assignmentService
                        .getAssignmentsForPosition(marriageRegistration.getState().getInitiatorPosition().getId());
                return !assignmentList.isEmpty() ? assignmentList.get(0) : null;
            } else if (isEmployee(marriageRegistration.getCreatedBy())
                    && !ANONYMOUS_USER.equalsIgnoreCase(securityUtils.getCurrentUser().getName()))
                return assignmentService.getPrimaryAssignmentForUser(marriageRegistration
                        .getCreatedBy().getId());
        return null;
    }

    public Assignment getWorkFlowInitiatorForReissue(final ReIssue reIssue) {
        if (reIssue != null)
            if (reIssue.getState() != null
                    && reIssue.getState().getInitiatorPosition() != null) {
                final List<Assignment> assignmentList = assignmentService
                        .getAssignmentsForPosition(reIssue.getState().getInitiatorPosition().getId());
                return !assignmentList.isEmpty() ? assignmentList.get(0) : null;
            } else if (reIssue.getCreatedBy() != null && isEmployee(reIssue.getCreatedBy()))
                return assignmentService.getPrimaryAssignmentForUser(reIssue
                        .getCreatedBy().getId());
        return null;
    }

    public Assignment getUserAssignment(final User user, final MarriageRegistration marriageRegistration, final ReIssue reIssue) {
        Assignment assignment;
        if (isCscOperator(user))
            assignment = getMappedAssignmentForCscOperator(marriageRegistration, reIssue);
        else
            assignment = getWorkFlowInitiator(marriageRegistration);
        return assignment;
    }

    public boolean validateAssignmentForCscUser(final MarriageRegistration marriageRegistration, final ReIssue reIssue,
            final Boolean isEmployee) {
        return (!isEmployee && (marriageRegistration != null || reIssue != null)
                && getUserPositionByZone(marriageRegistration, reIssue) == null) ? false : true;
    }
    
    
    /**
     * Checks whether user is meeseva user or not
     *
     * @param user
     * @return
     */
    public Boolean isMeesevaUser(final User user) {
            for (final Role role : user.getRoles())
                    if (role != null && role.getName().equalsIgnoreCase(MarriageConstants.MEESEVA_OPERATOR_ROLE))
                            return true;
            return false;
    }

}