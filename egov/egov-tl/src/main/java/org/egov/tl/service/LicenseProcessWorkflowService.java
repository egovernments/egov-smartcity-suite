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

package org.egov.tl.service;

import org.apache.commons.lang3.StringUtils;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.DesignationService;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.matrix.service.WorkFlowMatrixService;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.egov.tl.entity.TradeLicense;
import org.egov.tl.entity.WorkflowBean;
import org.egov.tl.entity.contracts.LicenseStateInfo;
import org.egov.tl.utils.LicenseUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.egov.tl.utils.Constants.*;

@Service
@Transactional(readOnly = true)
public class LicenseProcessWorkflowService {

    private static final String ERROR_KEY_WF_INITIATOR_NOT_DEFINED = "error.wf.initiator.not.defined";
    @Autowired
    protected LicenseStatusService licenseStatusService;
    @Autowired
    @Qualifier("workflowService")
    private SimpleWorkflowService<TradeLicense> licenseWorkflowService;
    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private PositionMasterService positionMasterService;

    @Autowired
    private LicenseUtils licenseUtils;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private DesignationService designationService;

    @Autowired
    private WorkFlowMatrixService workFlowMatrixService;

    @Autowired
    private ValidityService validityService;

    public void createNewLicenseWorkflowTransition(TradeLicense tradeLicense,
                                                   WorkflowBean workflowBean) {
        DateTime currentDate = new DateTime();
        User currentUser = securityUtils.getCurrentUser();
        State currentState = tradeLicense.getCurrentState();
        List<Position> userPositions = positionMasterService.getPositionsForEmployee(currentUser.getId());
        Position wfInitiator;
        if (BUTTONAPPROVE.equals(workflowBean.getWorkFlowAction()) && tradeLicense.isCollectionPending())
            if (tradeLicense.isNewApplication())
                workflowBean.setAdditionaRule(NEWLICENSECOLLECTION);
            else
                workflowBean.setAdditionaRule(RENEWLICENSECOLLECTION);
        WorkFlowMatrix workFlowMatrix = getWorkFlowMatrix(tradeLicense, workflowBean);

        if (!tradeLicense.hasState() || tradeLicense.transitionCompleted()) {
            wfInitiator = getWfInitiatorByUser(workFlowMatrix.getCurrentDesignation());
            LicenseStateInfo licenseStateInfo = getLicenseStateInfo(workflowBean, wfInitiator, workFlowMatrix, new LicenseStateInfo(), wfInitiator);
            initiateWfTransition(tradeLicense);
            tradeLicense.transition().withSenderName(currentUser.getUsername() + DELIMITER_COLON + currentUser.getName())
                    .withComments(workflowBean.getApproverComments())
                    .withNatureOfTask(tradeLicense.isReNewApplication() ? RENEWAL_NATUREOFWORK : NEW_NATUREOFWORK)
                    .withStateValue(workFlowMatrix.getNextState()).withDateInfo(currentDate.toDate()).withOwner(wfInitiator)
                    .withNextAction(workFlowMatrix.getNextAction()).withInitiator(wfInitiator).withExtraInfo(licenseStateInfo);
        } else if (BUTTONCANCEL.equalsIgnoreCase(workflowBean.getWorkFlowAction()) && userPositions.contains(tradeLicense.getCurrentState().getInitiatorPosition())) {
            tradeLicense.transition().end().withSenderName(currentUser.getUsername() + DELIMITER_COLON + currentUser.getName())
                    .withComments(workflowBean.getApproverComments())
                    .withDateInfo(currentDate.toDate());
            tradeLicense.setStatus(licenseStatusService.getLicenseStatusByCode(STATUS_CANCELLED));
            if (tradeLicense.isNewApplication())
                tradeLicense.setActive(false);
            tradeLicense.setNewWorkflow(false);
        } else if (SIGNWORKFLOWACTION.equals(workflowBean.getWorkFlowAction())) {
            tradeLicense.transition().end().withSenderName(currentUser.getUsername() + DELIMITER_COLON + currentUser.getName())
                    .withComments(workflowBean.getApproverComments()).withStateValue(workFlowMatrix.getCurrentState())
                    .withDateInfo(currentDate.toDate()).withNextAction(workFlowMatrix.getCurrentStatus());
            updateActiveStatus(tradeLicense);
        } else {
            Position owner = getCurrentPositionByWorkFlowBean(workflowBean, currentState);
            LicenseStateInfo licenseStateInfo = getLicenseStateInfo(workflowBean, owner, workFlowMatrix, tradeLicense.extraInfo(), (Position) currentState.getOwnerPosition());
            commonWorkflowTransition(tradeLicense, workflowBean, workFlowMatrix, licenseStateInfo);
        }
    }

    private void initiateWfTransition(TradeLicense tradeLicense) {
        if (!tradeLicense.hasState())
            tradeLicense.transition().start();
        else
            tradeLicense.transition().startNext();
    }

    private LicenseStateInfo getLicenseStateInfo(WorkflowBean workflowBean, Position position, WorkFlowMatrix workFlowMatrix, LicenseStateInfo licenseStateInfo, Position currentPosition) {
        if (workFlowMatrix.isRejectEnabled() != null && workFlowMatrix.isRejectEnabled()) {
            licenseStateInfo.setRejectionPosition(currentPosition.getId());
        }
        if (workFlowMatrix.getNextref() != null)
            licenseStateInfo.setWfMatrixRef(workFlowMatrix.getNextref());
        else {
            WorkFlowMatrix nextWorkFlowMatrix = this.licenseWorkflowService.getWfMatrix("TradeLicense", "ANY",
                    null, workflowBean.getAdditionaRule(), workFlowMatrix.getNextState(), null, new Date(), "%" + position.getDeptDesig().getDesignation().getName() + "%");
            if (nextWorkFlowMatrix != null)
                licenseStateInfo.setWfMatrixRef(nextWorkFlowMatrix.getId());
        }
        return licenseStateInfo;
    }

    private void commonWorkflowTransition(TradeLicense tradeLicense, WorkflowBean workflowBean, WorkFlowMatrix workFlowMatrix, LicenseStateInfo licenseStateInfo) {
        DateTime currentDate = new DateTime();
        User currentUser = securityUtils.getCurrentUser();
        Position owner = getCurrentPositionByWorkFlowBean(workflowBean, tradeLicense.getCurrentState());

        if (!licenseUtils.isDigitalSignEnabled() && BUTTONAPPROVE.equalsIgnoreCase(workflowBean.getWorkFlowAction()) && !tradeLicense.isCollectionPending()) {
            tradeLicense.transition().end().withSenderName(currentUser.getUsername() + DELIMITER_COLON + currentUser.getName())
                    .withComments(workflowBean.getApproverComments()).withStateValue(workFlowMatrix.getNextState())
                    .withDateInfo(currentDate.toDate()).withOwner(tradeLicense.getCurrentState().getOwnerPosition()).withExtraInfo(licenseStateInfo);
            updateActiveStatus(tradeLicense);
        } else {
            tradeLicense.transition().progressWithStateCopy().withSenderName(currentUser.getUsername() + DELIMITER_COLON + currentUser.getName())
                    .withComments(workflowBean.getApproverComments())
                    .withStateValue(workFlowMatrix.getNextState()).withDateInfo(currentDate.toDate()).withOwner(owner)
                    .withNextAction(BUTTONAPPROVE.equalsIgnoreCase(workflowBean.getWorkFlowAction()) ? workFlowMatrix.getNextAction() : StringUtils.EMPTY).withExtraInfo(licenseStateInfo);
            if (BUTTONAPPROVE.equals(workflowBean.getWorkFlowAction()) && tradeLicense.isCollectionPending())
                tradeLicense.setStatus(licenseStatusService.getLicenseStatusByCode(STATUS_COLLECTIONPENDING));
            else
                tradeLicense.setStatus(licenseStatusService.getLicenseStatusByCode(STATUS_UNDERWORKFLOW));
        }

    }

    private Position getCurrentPositionByWorkFlowBean(WorkflowBean workflowBean, State<Position> currentState) {
        if (workflowBean.getApproverPositionId() != null && workflowBean.getWorkFlowAction() != null && !BUTTONAPPROVE.equals(workflowBean.getWorkFlowAction()))
            return positionMasterService.getPositionById(workflowBean.getApproverPositionId());
        else
            return currentState.getOwnerPosition();
    }

    private Position getWfInitiatorByUser(String designation) {
        User currentUser = securityUtils.getCurrentUser();
        List<Assignment> assignments = assignmentService.getAllActiveEmployeeAssignmentsByEmpId(currentUser.getId());
        Optional<Assignment> empAssignment;
        if (!assignments.isEmpty()) {
            empAssignment = assignments.stream().filter(assignment ->
                    designation.contains(assignment.getDesignation().getName()))
                    .findAny();
            if (empAssignment.isPresent())
                return empAssignment.get().getPosition();
            else
                throw new ValidationException(ERROR_KEY_WF_INITIATOR_NOT_DEFINED, "No officials assigned to process this application");
        } else
            throw new ValidationException(ERROR_KEY_WF_INITIATOR_NOT_DEFINED, "No officials assigned to process this application");

    }

    public WorkFlowMatrix getWorkFlowMatrix(TradeLicense tradeLicense, WorkflowBean workflowBean) {
        WorkFlowMatrix wfmatrix;
        if (tradeLicense.hasState() && !tradeLicense.getState().isEnded()) {
            State<Position> state = tradeLicense.getState();
            wfmatrix = this.licenseWorkflowService.getWfMatrix(tradeLicense.getStateType(), "ANY",
                    null, workflowBean.getAdditionaRule(), workflowBean.getCurrentState() != null ? workflowBean.getCurrentState() : state.getValue(), null, new Date(), workflowBean.getCurrentDesignation() != null ? workflowBean.getCurrentDesignation() : "%" + state.getOwnerPosition().getDeptDesig().getDesignation().getName() + "%");
        } else
            wfmatrix = this.licenseWorkflowService.getWfMatrix(tradeLicense.getStateType(), "ANY",
                    null, workflowBean.getAdditionaRule(), "Start", null, new Date(), null);
        return wfmatrix;
    }

    public void collectionWorkflowTransition(TradeLicense tradeLicense) {
        final DateTime currentDate = new DateTime();
        final User currentUser = securityUtils.getCurrentUser();
        LicenseStateInfo licenseStateInfo = tradeLicense.extraInfo();
        if (!StringUtils.isEmpty(tradeLicense.getState().getExtraInfo())) {
            WorkFlowMatrix workFlowMatrix = workFlowMatrixService.getWorkFlowObjectbyId(Long.valueOf(licenseStateInfo.getWfMatrixRef()));
            if (workFlowMatrix != null) {
                if (licenseUtils.isDigitalSignEnabled() || STATUS_ACKNOWLEDGED.equals(tradeLicense.getStatus().getStatusCode())) {
                    tradeLicense.transition().progressWithStateCopy().withSenderName(currentUser.getUsername() + DELIMITER_COLON + currentUser.getName())
                            .withComments(workFlowMatrix.getNextState())
                            .withStateValue(workFlowMatrix.getNextState()).withDateInfo(currentDate.toDate())
                            .withNextAction(workFlowMatrix.getNextAction());
                    tradeLicense.setStatus(licenseStatusService.getLicenseStatusByCode(STATUS_UNDERWORKFLOW));
                } else {
                    tradeLicense.transition().end().withSenderName(currentUser.getUsername() + DELIMITER_COLON + currentUser.getName())
                            .withComments(workFlowMatrix.getNextState()).withStateValue(workFlowMatrix.getNextState())
                            .withDateInfo(currentDate.toDate());
                    updateActiveStatus(tradeLicense);
                }
            }
        }
    }

    private void updateActiveStatus(TradeLicense tradeLicense) {
        tradeLicense.setStatus(licenseStatusService.getLicenseStatusByCode(STATUS_ACTIVE));
        tradeLicense.setActive(true);
        tradeLicense.setLegacy(false);
        validityService.applyLicenseValidity(tradeLicense);
        tradeLicense.setNewWorkflow(false);
    }

    public void getWfWithThirdPartyOp(final TradeLicense license, final WorkflowBean workflowBean) {
        WorkFlowMatrix workFlowMatrix = this.licenseWorkflowService.getWfMatrix(license.getStateType(), null,
                null, workflowBean.getAdditionaRule(), workflowBean.getCurrentState(), null, new Date(), null);
        List<Assignment> assignmentList = getAssignments(workFlowMatrix);
        if (!assignmentList.isEmpty()) {
            String additionalRule = license.isNewApplication() ? NEWLICENSE : RENEWLICENSE;
            final Assignment wfAssignment = assignmentList.get(0);
            WorkFlowMatrix nextWorkFlowMatrix = this.licenseWorkflowService.getWfMatrix(license.getStateType(), "ANY",
                    null, additionalRule, workFlowMatrix.getNextState(), null, new Date(), "%" + wfAssignment.getDesignation().getName() + "%");
            LicenseStateInfo licenseStateInfo = new LicenseStateInfo();
            if (nextWorkFlowMatrix != null)
                licenseStateInfo.setWfMatrixRef(nextWorkFlowMatrix.getId());
            initiateWfTransition(license);
            license.transition().withSenderName(
                    wfAssignment.getEmployee().getUsername() + DELIMITER_COLON + wfAssignment.getEmployee().getName())
                    .withComments(workflowBean.getApproverComments())
                    .withNatureOfTask(license.isReNewApplication() ? RENEWAL_NATUREOFWORK : NEW_NATUREOFWORK)
                    .withStateValue(workFlowMatrix.getNextState()).withDateInfo(new Date()).withOwner(wfAssignment.getPosition())
                    .withNextAction(workFlowMatrix.getNextAction()).withInitiator(wfAssignment.getPosition()).withExtraInfo(licenseStateInfo);
        } else
            throw new ValidationException(ERROR_KEY_WF_INITIATOR_NOT_DEFINED, ERROR_KEY_WF_INITIATOR_NOT_DEFINED);
    }

    public void getRejectTransition(TradeLicense tradeLicense, WorkflowBean workflowBean) {
        User currentUser = securityUtils.getCurrentUser();
        workflowBean.setAdditionaRule(tradeLicense.isNewApplication() ? NEWLICENSEREJECT : RENEWLICENSEREJECT);
        WorkFlowMatrix workFlowMatrix = getWorkFlowMatrix(tradeLicense, workflowBean);
        Position ownerPosition;
        if (!tradeLicense.getCurrentState().getExtraInfo().isEmpty()) {
            LicenseStateInfo licenseStateInfo = tradeLicense.extraInfoAs(LicenseStateInfo.class);
            if (licenseStateInfo.getRejectionPosition() != null && !licenseStateInfo.getRejectionPosition().equals(tradeLicense.getCurrentState().getOwnerPosition().getId()))
                ownerPosition = positionMasterService.getPositionById(licenseStateInfo.getRejectionPosition());
            else
                ownerPosition = tradeLicense.getCurrentState().getInitiatorPosition();
            tradeLicense.transition().progressWithStateCopy().withSenderName(currentUser.getUsername() + DELIMITER_COLON + currentUser.getName())
                    .withComments(workflowBean.getApproverComments())
                    .withStateValue(workFlowMatrix.getNextState()).withDateInfo(new DateTime().toDate()).withOwner(ownerPosition)
                    .withNextAction(workFlowMatrix.getNextAction()).withExtraInfo(licenseStateInfo);
            tradeLicense.setStatus(licenseStatusService.getLicenseStatusByCode(STATUS_REJECTED));
        }
    }

    private List<Assignment> getAssignments(WorkFlowMatrix workFlowMatrix) {
        Department nextAssigneeDept = departmentService.getDepartmentByName(workFlowMatrix.getDepartment());
        List<Designation> nextDesig = designationService.getDesignationsByNames(Arrays.asList(StringUtils.upperCase(workFlowMatrix.getNextDesignation()).split(",")));
        List<Assignment> assignmentList = getAssignmentsForDeptAndDesignation(nextAssigneeDept, nextDesig);
        if (assignmentList.isEmpty())
            throw new ValidationException(ERROR_KEY_WF_INITIATOR_NOT_DEFINED, ERROR_KEY_WF_INITIATOR_NOT_DEFINED);
        return assignmentList;
    }

    private List<Assignment> getAssignmentsForDeptAndDesignation(Department nextAssigneeDept, List<Designation> nextAssigneeDesig) {
        List<Long> designationIds = new ArrayList<>();
        nextAssigneeDesig.forEach(designation -> designationIds.add(designation.getId()));
        return assignmentService.
                findByDepartmentDesignationsAndGivenDate(nextAssigneeDept.getId(), designationIds, new Date());
    }

    public void setLicenseWorkflowService(SimpleWorkflowService<TradeLicense> licenseWorkflowService) {
        this.licenseWorkflowService = licenseWorkflowService;
    }
}
