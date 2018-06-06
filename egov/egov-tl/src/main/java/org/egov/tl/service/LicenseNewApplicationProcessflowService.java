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

package org.egov.tl.service;

import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.DesignationService;
import org.egov.eis.service.PositionMasterService;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.egov.tl.entity.TradeLicense;
import org.egov.tl.entity.contracts.LicenseStateInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.egov.infra.utils.StringUtils.EMPTY;
import static org.egov.tl.utils.Constants.COMPLETED;
import static org.egov.tl.utils.Constants.DELIMITER_COLON;
import static org.egov.tl.utils.Constants.LICENSE_STATUS_CANCELLED;
import static org.egov.tl.utils.Constants.NEWLICENSE;
import static org.egov.tl.utils.Constants.NEWLICENSEREJECT;
import static org.egov.tl.utils.Constants.NEW_NATUREOFWORK;
import static org.egov.tl.utils.Constants.PERCENTILE;
import static org.egov.tl.utils.Constants.WF_DIGI_SIGNED;

@Service
@Transactional(readOnly = true)
public class LicenseNewApplicationProcessflowService {

    private static final String ANY = "ANY";

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private DesignationService designationService;

    @Autowired
    private PositionMasterService positionMasterService;

    @Autowired
    private LicenseConfigurationService licenseConfigurationService;

    @Autowired
    @Qualifier("workflowService")
    private SimpleWorkflowService<TradeLicense> licenseWorkflowService;

    public void startNewApplicationProcessflow(TradeLicense tradeLicense) {
        if (securityUtils.currentUserIsEmployee()) {
            startNewApplicationProcessByEmployee(tradeLicense);
        } else {
            startNewApplicationByExternalUsers(tradeLicense);
        }
    }

    private void startNewApplicationProcessByEmployee(TradeLicense tradeLicense) {

        User currentUser = securityUtils.getCurrentUser();

        List<Assignment> assignments = assignmentService.getAllActiveEmployeeAssignmentsByEmpId(currentUser.getId());
        Position flowInitiator = assignments.get(0).getPosition();

        WorkFlowMatrix workflowMatrix = getWorkFlowMatrix(tradeLicense);
        LicenseStateInfo licenseStateInfo = new LicenseStateInfo();
        WorkFlowMatrix nextWorkFlowMatrix = nextWorkFlowMatrix(tradeLicense, workflowMatrix, flowInitiator);

        if (nextWorkFlowMatrix != null) {
            licenseStateInfo.setWfMatrixRef(nextWorkFlowMatrix.getId());
        }
        if (workflowMatrix.isRejectEnabled() != null && workflowMatrix.isRejectEnabled()) {
            licenseStateInfo.setRejectionPosition(flowInitiator.getId());
        }

        tradeLicense.transition().start()
                .withSenderName(currentUser.getUsername() + DELIMITER_COLON + currentUser.getName())
                .withComments(tradeLicense.getWorkflowContainer().getApproverComments())
                .withStateValue(workflowMatrix.getNextState())
                .withDateInfo(new Date())
                .withOwner(flowInitiator)
                .withNatureOfTask(NEW_NATUREOFWORK)
                .withNextAction(workflowMatrix.getNextAction())
                .withInitiator(flowInitiator)
                .withExtraInfo(licenseStateInfo);
    }

    private void startNewApplicationByExternalUsers(TradeLicense tradeLicense) {

        WorkFlowMatrix workflowMatrix = getWorkFlowMatrix(tradeLicense);
        List<Assignment> assignments = getAssignments(workflowMatrix);
        Position processOwner = assignments.get(0).getPosition();

        tradeLicense.getWorkflowContainer().setAdditionalRule(NEWLICENSE);
        WorkFlowMatrix nextWorkFlowMatrix = nextWorkFlowMatrix(tradeLicense, workflowMatrix, processOwner);

        LicenseStateInfo licenseStateInfo = new LicenseStateInfo();
        if (nextWorkFlowMatrix != null)
            licenseStateInfo.setWfMatrixRef(nextWorkFlowMatrix.getId());

        tradeLicense.transition().start()
                .withSenderName(securityUtils.getCurrentUser().getName())
                .withComments(tradeLicense.getWorkflowContainer().getApproverComments())
                .withStateValue(workflowMatrix.getNextState())
                .withDateInfo(new Date())
                .withOwner(processOwner)
                .withNextAction(workflowMatrix.getNextAction())
                .withExtraInfo(licenseStateInfo)
                .withNatureOfTask(NEW_NATUREOFWORK)
                .withInitiator(processOwner)
                .withExtraInfo(licenseStateInfo);
    }

    public void processForward(TradeLicense tradeLicense) {

        User currentUser = securityUtils.getCurrentUser();
        WorkFlowMatrix workflowMatrix = getWorkFlowMatrix(tradeLicense);
        Position owner = positionMasterService.getPositionById(tradeLicense.getWorkflowContainer().getApproverPositionId());

        LicenseStateInfo licenseStateInfo = tradeLicense.extraInfo();
        WorkFlowMatrix nextWorkFlowMatrix = nextWorkFlowMatrix(tradeLicense, workflowMatrix, owner);
        if (nextWorkFlowMatrix != null) {
            licenseStateInfo.setWfMatrixRef(nextWorkFlowMatrix.getId());
        }
        if (workflowMatrix.isRejectEnabled() != null && workflowMatrix.isRejectEnabled()) {
            licenseStateInfo.setRejectionPosition(tradeLicense.getCurrentState().getOwnerPosition().getId());
        }


        tradeLicense.transition().progressWithStateCopy()
                .withSenderName(currentUser.getUsername() + DELIMITER_COLON + currentUser.getName())
                .withComments(tradeLicense.getWorkflowContainer().getApproverComments())
                .withStateValue(workflowMatrix.getNextState())
                .withDateInfo(new Date())
                .withOwner(owner)
                .withNextAction(EMPTY)
                .withExtraInfo(licenseStateInfo);
    }

    public void processApprove(TradeLicense tradeLicense) {

        User currentUser = securityUtils.getCurrentUser();
        WorkFlowMatrix workflowMatrix = getWorkFlowMatrix(tradeLicense);
        LicenseStateInfo licenseStateInfo = tradeLicense.extraInfo();

        WorkFlowMatrix nextWorkFlowMatrix = nextWorkFlowMatrix(tradeLicense, workflowMatrix, tradeLicense.getCurrentState().getOwnerPosition());
        if (nextWorkFlowMatrix != null) {
            licenseStateInfo.setWfMatrixRef(nextWorkFlowMatrix.getId());
        }

        if (!licenseConfigurationService.digitalSignEnabled() && !tradeLicense.isCollectionPending()) {
            tradeLicense.transition().end()
                    .withSenderName(currentUser.getUsername() + DELIMITER_COLON + currentUser.getName())
                    .withComments(tradeLicense.getWorkflowContainer().getApproverComments())
                    .withStateValue(workflowMatrix.getNextState())
                    .withDateInfo(new Date())
                    .withNextAction(COMPLETED);
        } else {
            tradeLicense.transition().progressWithStateCopy()
                    .withSenderName(currentUser.getUsername() + DELIMITER_COLON + currentUser.getName())
                    .withComments(tradeLicense.getWorkflowContainer().getApproverComments())
                    .withStateValue(workflowMatrix.getNextState())
                    .withDateInfo(new Date())
                    .withOwner(tradeLicense.getCurrentState().getOwnerPosition())
                    .withNextAction(workflowMatrix.getNextAction())
                    .withExtraInfo(licenseStateInfo);
        }
    }

    public void processReject(TradeLicense tradeLicense) {
        User currentUser = securityUtils.getCurrentUser();
        LicenseStateInfo licenseStateInfo = tradeLicense.extraInfo();
        Position owner;
        if (licenseStateInfo.getRejectionPosition() != null
                && !licenseStateInfo.getRejectionPosition().equals(tradeLicense.getCurrentState().getOwnerPosition().getId())) {
            owner = positionMasterService.getPositionById(licenseStateInfo.getRejectionPosition());
        } else {
            owner = tradeLicense.getCurrentState().getInitiatorPosition();
        }

        tradeLicense.getWorkflowContainer().setAdditionalRule(NEWLICENSEREJECT);
        WorkFlowMatrix workFlowMatrix = getWorkFlowMatrix(tradeLicense);
        tradeLicense.transition().progressWithStateCopy()
                .withSenderName(currentUser.getUsername() + DELIMITER_COLON + currentUser.getName())
                .withComments(tradeLicense.getWorkflowContainer().getApproverComments())
                .withStateValue(workFlowMatrix.getNextState())
                .withDateInfo(new Date())
                .withOwner(owner)
                .withNextAction(workFlowMatrix.getNextAction())
                .withExtraInfo(licenseStateInfo);
    }

    public void processCancellation(TradeLicense tradeLicense) {
        User currentUser = securityUtils.getCurrentUser();
        tradeLicense.transition().end()
                .withSenderName(currentUser.getUsername() + DELIMITER_COLON + currentUser.getName())
                .withStateValue(LICENSE_STATUS_CANCELLED)
                .withComments(tradeLicense.getWorkflowContainer().getApproverComments())
                .withDateInfo(new Date())
                .withNextAction(COMPLETED);
    }

    public void processDigiSign(TradeLicense tradeLicense) {
        User currentUser = securityUtils.getCurrentUser();
        tradeLicense.transition().end()
                .withSenderName(currentUser.getUsername() + DELIMITER_COLON + currentUser.getName())
                .withStateValue(WF_DIGI_SIGNED)
                .withComments(WF_DIGI_SIGNED)
                .withDateInfo(new Date())
                .withNextAction(COMPLETED);
    }

    private WorkFlowMatrix nextWorkFlowMatrix(TradeLicense tradeLicense, WorkFlowMatrix workflowMatrix, Position owner) {
        return licenseWorkflowService.getWfMatrix(tradeLicense.getStateType(), ANY, null,
                tradeLicense.getWorkflowContainer().getAdditionalRule(),
                workflowMatrix.getNextState(), workflowMatrix.getNextAction(), new Date(),
                PERCENTILE + owner.getDeptDesig().getDesignation().getName() + PERCENTILE);
    }

    public WorkFlowMatrix getWorkFlowMatrix(TradeLicense tradeLicense) {
        WorkFlowMatrix workflowMatrix;
        WorkflowContainer workflowContainer = tradeLicense.getWorkflowContainer();
        if (tradeLicense.transitionInprogress()) {
            State<Position> state = tradeLicense.getState();
            workflowMatrix = licenseWorkflowService.getWfMatrix(tradeLicense.getStateType(), ANY, null,
                    workflowContainer.getAdditionalRule(), workflowContainer.getCurrentState() == null ?
                            state.getValue() : workflowContainer.getCurrentState(), state.getNextAction(), new Date(),
                    workflowContainer.getCurrentDesignation() == null
                            ? PERCENTILE + state.getOwnerPosition().getDeptDesig().getDesignation().getName() + PERCENTILE
                            : workflowContainer.getCurrentDesignation());
        } else {
            workflowMatrix = licenseWorkflowService.getWfMatrix(tradeLicense.getStateType(), null, null,
                    workflowContainer.getAdditionalRule(), "Start", null, new Date());
        }
        return workflowMatrix;
    }

    public List<Assignment> getAssignments(WorkFlowMatrix workflowMatrix) {
        Department nextAssigneeDept = departmentService.getDepartmentByName(workflowMatrix.getDepartment());
        List<Designation> nextDesig = designationService.getDesignationsByNames(
                Arrays.asList(workflowMatrix.getNextDesignation().toUpperCase().split(",")));
        List<Long> designationIds = new ArrayList<>();
        nextDesig.forEach(designation -> designationIds.add(designation.getId()));
        return assignmentService.findByDepartmentDesignationsAndGivenDate(nextAssigneeDept.getId(), designationIds, new Date());
    }

    public boolean validateAssignee(TradeLicense tradeLicense) {
        if (securityUtils.currentUserIsEmployee()) {
            List<Assignment> assignments = assignmentService
                    .getAllActiveEmployeeAssignmentsByEmpId(securityUtils.getCurrentUser().getId());
            return assignments.isEmpty() ? true : assignments
                    .stream()
                    .noneMatch(assignment -> getWorkFlowMatrix(tradeLicense)
                            .getCurrentDesignation().contains(assignment.getDesignation().getName()));
        } else {
            return getAssignments(getWorkFlowMatrix(tradeLicense)).isEmpty();
        }
    }
}