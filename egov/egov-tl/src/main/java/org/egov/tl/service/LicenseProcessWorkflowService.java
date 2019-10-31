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

import org.apache.commons.lang3.StringUtils;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.DesignationService;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.entity.Boundary;
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

import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.egov.tl.utils.Constants.*;

@Service
@Transactional(readOnly = true)
public class LicenseProcessWorkflowService {

    private static final String ERROR_KEY_WF_INITIATOR_NOT_DEFINED = "error.wf.initiator.not.defined";
    private static final String ANY = "ANY";
    private static final String PROCESS_OWNER_NOT_FOUND = "No officials assigned to process this application";

    @Autowired
    private LicenseStatusService licenseStatusService;

    @Autowired
    @Qualifier("tradeLicenseWorkflowService")
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

    @Autowired
    private LicenseConfigurationService licenseConfigurationService;

    public void createNewLicenseWorkflowTransition(TradeLicense tradeLicense,
                                                   WorkflowBean workflowBean) {
        DateTime currentDate = new DateTime();
        User currentUser = securityUtils.getCurrentUser();
        List<Position> userPositions = positionMasterService.getPositionsForEmployee(currentUser.getId());
        if (BUTTONAPPROVE.equals(workflowBean.getWorkFlowAction()) && tradeLicense.isCollectionPending())
            workflowBean.setAdditionaRule(tradeLicense.isNewApplication() ? NEWLICENSECOLLECTION : RENEWLICENSECOLLECTION);
        WorkFlowMatrix workFlowMatrix = getWorkFlowMatrix(tradeLicense, workflowBean);
        if (!tradeLicense.hasState() || tradeLicense.transitionCompleted()) {
            Position wfInitiator = assignmentService
                    .getAllActiveEmployeeAssignmentsByEmpId(currentUser.getId())
                    .stream()
                    .filter(assignment -> workFlowMatrix.getCurrentDesignation().contains(assignment.getDesignation().getName()))
                    .findAny()
                    .orElseThrow(() -> new ValidationException(ERROR_KEY_WF_INITIATOR_NOT_DEFINED, PROCESS_OWNER_NOT_FOUND))
                    .getPosition();
            LicenseStateInfo licenseStateInfo = getLicenseStateInfo(workflowBean, wfInitiator,
                    workFlowMatrix, new LicenseStateInfo(), wfInitiator);
            initiateWfTransition(tradeLicense);
            tradeLicense.transition()
                    .withSLA(licenseUtils.getSlaForAppType(tradeLicense.getLicenseAppType()))
                    .withSenderName(currentUser.getUsername() + DELIMITER_COLON + currentUser.getName())
                    .withComments(workflowBean.getApproverComments())
                    .withNatureOfTask(tradeLicense.getLicenseAppType().getName())
                    .withStateValue(workFlowMatrix.getNextState()).withDateInfo(currentDate.toDate()).withOwner(wfInitiator)
                    .withNextAction(workFlowMatrix.getNextAction()).withInitiator(wfInitiator).withExtraInfo(licenseStateInfo);
        } else if (BUTTONCANCEL.equalsIgnoreCase(workflowBean.getWorkFlowAction())
                && userPositions.contains(tradeLicense.getCurrentState().getInitiatorPosition())) {
            tradeLicense.transition().end().withStateValue(LICENSE_STATUS_CANCELLED)
                    .withSenderName(currentUser.getUsername() + DELIMITER_COLON + currentUser.getName())
                    .withComments(workflowBean.getApproverComments())
                    .withNextAction(COMPLETED)
                    .withDateInfo(currentDate.toDate());
            updateCancelStatus(tradeLicense);
        } else if (SIGNWORKFLOWACTION.equals(workflowBean.getWorkFlowAction())) {
            tradeLicense.transition().end().withStateValue(WF_DIGI_SIGNED)
                    .withSenderName(currentUser.getUsername() + DELIMITER_COLON + currentUser.getName())
                    .withComments(workflowBean.getApproverComments()).withDateInfo(currentDate.toDate())
                    .withNextAction(workFlowMatrix.getCurrentStatus());
            activateLicense(tradeLicense);
        } else {
            State currentState = tradeLicense.getCurrentState();
            Position owner = getCurrentPositionByWorkFlowBean(workflowBean, currentState);
            LicenseStateInfo licenseStateInfo = getLicenseStateInfo(workflowBean, owner, workFlowMatrix,
                    tradeLicense.extraInfo(), (Position) currentState.getOwnerPosition());
            commonWorkflowTransition(tradeLicense, workflowBean, workFlowMatrix, licenseStateInfo);
        }
    }

    public WorkFlowMatrix getWorkFlowMatrix(TradeLicense tradeLicense, WorkflowBean workflowBean) {
        WorkFlowMatrix wfmatrix;
        if (tradeLicense.hasState() && !tradeLicense.getState().isEnded()) {
            State<Position> state = tradeLicense.getState();
            wfmatrix = this.licenseWorkflowService.getWfMatrix(tradeLicense.getStateType(), ANY,
                    null, workflowBean.getAdditionaRule(), workflowBean.getCurrentState() != null ?
                            workflowBean.getCurrentState() : state.getValue(), state.getNextAction(), new Date(),
                    workflowBean.getCurrentDesignation() != null ? workflowBean.getCurrentDesignation() : "%"
                            + state.getOwnerPosition().getDeptDesig().getDesignation().getName() + "%");
        } else {
            wfmatrix = this.licenseWorkflowService.getWfMatrix(tradeLicense.getStateType(), ANY,
                    null, workflowBean.getAdditionaRule(), "Start", null,
                    new Date(), null);
        }
        return wfmatrix;
    }

    public void collectionWorkflowTransition(TradeLicense tradeLicense) {
        final DateTime currentDate = new DateTime();
        final User currentUser = securityUtils.getCurrentUser();
        final String collectionOperator = licenseUtils.getApplicationSenderName(currentUser.getType()
                , currentUser.getUsername() + DELIMITER_COLON + currentUser.getName()
                , tradeLicense.getLicensee().getApplicantName());
        LicenseStateInfo licenseStateInfo = tradeLicense.extraInfo();
        if (isNotBlank(tradeLicense.getState().getExtraInfo())) {
            WorkFlowMatrix workFlowMatrix = workFlowMatrixService.getWorkFlowObjectbyId(licenseStateInfo.getWfMatrixRef());
            if (workFlowMatrix != null) {
                if (licenseConfigurationService.digitalSignEnabled()
                        || STATUS_ACKNOWLEDGED.equals(tradeLicense.getStatus().getStatusCode())) {
                    tradeLicense.transition().progressWithStateCopy().withSenderName(collectionOperator)
                            .withComments(workFlowMatrix.getNextState())
                            .withStateValue(workFlowMatrix.getNextState()).withDateInfo(currentDate.toDate())
                            .withNextAction(workFlowMatrix.getNextAction());
                    tradeLicense.setStatus(licenseStatusService.getLicenseStatusByCode(STATUS_UNDERWORKFLOW));
                } else {
                    tradeLicense.transition().end().withSenderName(collectionOperator)
                            .withComments(workFlowMatrix.getNextState())
                            .withStateValue(workFlowMatrix.getNextState())
                            .withDateInfo(currentDate.toDate())
                            .withNextAction(COMPLETED);
                    activateLicense(tradeLicense);
                }
            }
        }
    }

    public void getWfWithThirdPartyOp(final TradeLicense license, final WorkflowBean workflowBean) {
        WorkFlowMatrix workFlowMatrix = this.licenseWorkflowService.getWfMatrix(license.getStateType(), null,
                null, workflowBean.getAdditionaRule(), workflowBean.getCurrentState(),
                null, new Date(), null);
        List<Assignment> assignmentList = getAssignments(workFlowMatrix, license.getAdminWard());
        if (assignmentList.isEmpty()) {
            throw new ValidationException(ERROR_KEY_WF_INITIATOR_NOT_DEFINED, ERROR_KEY_WF_INITIATOR_NOT_DEFINED);
        } else {
            String additionalRule = license.isNewApplication() ? NEWLICENSE : RENEWLICENSE;
            final Assignment wfAssignment = assignmentList.get(0);
            User currentUser = securityUtils.getCurrentUser();
            WorkFlowMatrix nextWorkFlowMatrix = this.licenseWorkflowService.getWfMatrix(license.getStateType(), ANY,
                    null, additionalRule, workFlowMatrix.getNextState(), workFlowMatrix.getNextAction(),
                    new Date(), "%" + wfAssignment.getDesignation().getName() + "%");
            LicenseStateInfo licenseStateInfo = new LicenseStateInfo();
            if (nextWorkFlowMatrix != null)
                licenseStateInfo.setWfMatrixRef(nextWorkFlowMatrix.getId());
            initiateWfTransition(license);
            license.transition()
                    .withSLA(licenseUtils.getSlaForAppType(license.getLicenseAppType()))
                    .withSenderName(licenseUtils.getApplicationSenderName(currentUser.getType(), currentUser.getName(),
                            license.getLicensee().getApplicantName()))
                    .withComments(workflowBean.getApproverComments())
                    .withNatureOfTask(license.getLicenseAppType().getName())
                    .withStateValue(workFlowMatrix.getNextState()).withDateInfo(new Date())
                    .withOwner(wfAssignment.getPosition()).withNextAction(workFlowMatrix.getNextAction())
                    .withInitiator(wfAssignment.getPosition()).withExtraInfo(licenseStateInfo);
        }
    }

    public void getRejectTransition(TradeLicense tradeLicense, WorkflowBean workflowBean) {
        User currentUser = securityUtils.getCurrentUser();
        workflowBean.setAdditionaRule(tradeLicense.isNewApplication() ? NEWLICENSEREJECT : RENEWLICENSEREJECT);
        WorkFlowMatrix workFlowMatrix = getWorkFlowMatrix(tradeLicense, workflowBean);
        Position ownerPosition;
        if (!tradeLicense.getCurrentState().getExtraInfo().isEmpty()) {
            LicenseStateInfo licenseStateInfo = tradeLicense.extraInfoAs(LicenseStateInfo.class);
            if (licenseStateInfo.getRejectionPosition() != null
                    && !licenseStateInfo.getRejectionPosition().equals(tradeLicense.getCurrentState().getOwnerPosition().getId()))
                ownerPosition = positionMasterService.getPositionById(licenseStateInfo.getRejectionPosition());
            else
                ownerPosition = tradeLicense.getCurrentState().getInitiatorPosition();
            tradeLicense.transition().progressWithStateCopy()
                    .withSenderName(currentUser.getUsername() + DELIMITER_COLON + currentUser.getName())
                    .withComments(workflowBean.getApproverComments())
                    .withStateValue(workFlowMatrix.getNextState()).withDateInfo(new DateTime().toDate())
                    .withOwner(ownerPosition).withNextAction(workFlowMatrix.getNextAction())
                    .withExtraInfo(licenseStateInfo);
            tradeLicense.setStatus(licenseStatusService.getLicenseStatusByCode(STATUS_REJECTED));
        }
    }

    public List<Assignment> getAssignments(WorkFlowMatrix workFlowMatrix, Boundary boundary) {
        Department nextAssigneeDept = departmentService.getDepartmentByName(workFlowMatrix.getDepartment());
        List<Designation> nextDesignation = designationService.
                getDesignationsByNames(Arrays.asList(StringUtils.upperCase(workFlowMatrix.getNextDesignation()).split(",")));
        List<Long> designationIds = new ArrayList<>();
        nextDesignation.forEach(designation -> designationIds.add(designation.getId()));
        List<Assignment> assignmentList = new ArrayList<>();
        if (licenseConfigurationService.jurisdictionBasedRoutingEnabled() && boundary != null)
            assignmentList = assignmentService
                    .getAssignmentsByDepartmentAndDesignationsAndBoundary(nextAssigneeDept.getId(), designationIds, boundary.getId());
        if (assignmentList.isEmpty())
            assignmentList = assignmentService.findByDepartmentDesignationsAndGivenDate(nextAssigneeDept.getId(), designationIds, new Date());
        return assignmentList;
    }

    private void updateCancelStatus(TradeLicense tradeLicense) {
        tradeLicense.setStatus(licenseStatusService.getLicenseStatusByCode(STATUS_CANCELLED));
        tradeLicense.setCollectionPending(false);
        if (tradeLicense.isNewApplication())
            tradeLicense.setActive(false);
    }

    private void initiateWfTransition(TradeLicense tradeLicense) {
        if (tradeLicense.hasState()) {
            tradeLicense.transition().startNext();
        } else {
            tradeLicense.transition().start();
        }
    }

    private LicenseStateInfo getLicenseStateInfo(WorkflowBean workflowBean, Position position,
                                                 WorkFlowMatrix workFlowMatrix, LicenseStateInfo licenseStateInfo,
                                                 Position currentPosition) {
        if (workFlowMatrix.isRejectEnabled() != null && workFlowMatrix.isRejectEnabled()) {
            licenseStateInfo.setRejectionPosition(currentPosition.getId());
        }
        if (workFlowMatrix.getNextref() == null) {
            WorkFlowMatrix nextWorkFlowMatrix = this.licenseWorkflowService.getWfMatrix(TRADELICENSE, ANY,
                    null, workflowBean.getAdditionaRule(), workFlowMatrix.getNextState(), workFlowMatrix.getNextAction(),
                    new Date(), "%" + position.getDeptDesig().getDesignation().getName() + "%");
            if (nextWorkFlowMatrix != null)
                licenseStateInfo.setWfMatrixRef(nextWorkFlowMatrix.getId());
        } else {
            licenseStateInfo.setWfMatrixRef(workFlowMatrix.getNextref());
        }
        return licenseStateInfo;
    }

    private void commonWorkflowTransition(TradeLicense tradeLicense, WorkflowBean workflowBean,
                                          WorkFlowMatrix workFlowMatrix, LicenseStateInfo licenseStateInfo) {
        DateTime currentDate = new DateTime();
        User currentUser = securityUtils.getCurrentUser();
        Position owner = getCurrentPositionByWorkFlowBean(workflowBean, tradeLicense.getCurrentState());

        if (BUTTONAPPROVE.equals(workflowBean.getWorkFlowAction()))
            tradeLicense.setApprovedBy(currentUser);

        if (!licenseConfigurationService.digitalSignEnabled() && BUTTONAPPROVE.equalsIgnoreCase(workflowBean.getWorkFlowAction())
                && !tradeLicense.isCollectionPending()) {
            tradeLicense.transition().end().withStateValue(workFlowMatrix.getNextState())
                    .withSenderName(currentUser.getUsername() + DELIMITER_COLON + currentUser.getName())
                    .withComments(workflowBean.getApproverComments())
                    .withDateInfo(currentDate.toDate())
                    .withNextAction(COMPLETED);
            activateLicense(tradeLicense);
        } else {
            tradeLicense.transition().progressWithStateCopy()
                    .withSenderName(currentUser.getUsername() + DELIMITER_COLON + currentUser.getName())
                    .withComments(workflowBean.getApproverComments())
                    .withStateValue(workFlowMatrix.getNextState()).withDateInfo(currentDate.toDate()).withOwner(owner)
                    .withNextAction(BUTTONAPPROVE.equalsIgnoreCase(workflowBean.getWorkFlowAction()) ? workFlowMatrix.getNextAction() : EMPTY)
                    .withExtraInfo(licenseStateInfo);
            if (BUTTONAPPROVE.equals(workflowBean.getWorkFlowAction()) && tradeLicense.isCollectionPending())
                tradeLicense.setStatus(licenseStatusService.getLicenseStatusByCode(STATUS_COLLECTIONPENDING));
            else
                tradeLicense.setStatus(licenseStatusService.getLicenseStatusByCode(STATUS_UNDERWORKFLOW));
        }

    }

    private void activateLicense(TradeLicense tradeLicense) {
        tradeLicense.setStatus(licenseStatusService.getLicenseStatusByCode(STATUS_ACTIVE));
        tradeLicense.setActive(true);
        tradeLicense.setLegacy(false);
        validityService.applyLicenseValidity(tradeLicense);
    }

    private Position getCurrentPositionByWorkFlowBean(WorkflowBean workflowBean, State<Position> currentState) {
        return workflowBean.getApproverPositionId() == null || workflowBean.getApproverPositionId() == -1
                || workflowBean.getWorkFlowAction() == null
                || BUTTONAPPROVE.equals(workflowBean.getWorkFlowAction()) ? currentState.getOwnerPosition()
                        : positionMasterService.getPositionById(workflowBean.getApproverPositionId());
    }
}

