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
import org.egov.eis.service.PositionMasterService;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.pims.commons.Position;
import org.egov.tl.entity.TradeLicense;
import org.egov.tl.entity.contracts.LicenseStateInfo;
import org.egov.tl.utils.LicenseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.egov.tl.utils.Constants.BUTTONREJECT;
import static org.egov.tl.utils.Constants.CLOSURE_ADDITIONAL_RULE;
import static org.egov.tl.utils.Constants.CLOSURE_APPTYPE_CODE;
import static org.egov.tl.utils.Constants.CLOSURE_LICENSE_REJECT;
import static org.egov.tl.utils.Constants.COMPLETED;
import static org.egov.tl.utils.Constants.DELIMITER_COLON;
import static org.egov.tl.utils.Constants.WF_DIGI_SIGNED;

@Service
@Transactional(readOnly = true)
public class LicenseClosureProcessflowService {

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private LicenseConfigurationService licenseConfigurationService;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private PositionMasterService positionMasterService;

    @Autowired
    private LicenseProcessWorkflowService licenseProcessWorkflowService;

    @Autowired
    @Qualifier("workflowService")
    private SimpleWorkflowService<TradeLicense> licenseWorkflowService;

    @Autowired
    private LicenseUtils licenseUtils;

    @Autowired
    private LicenseAppTypeService licenseAppTypeService;

    public void startClosureProcessflow(TradeLicense license) {
        if (securityUtils.currentUserIsEmployee()) {
            startClosureProcessByEmployee(license);
        } else {
            startClosureProcessByExternalUsers(license);
        }
    }

    private void startClosureProcessByEmployee(TradeLicense license) {
        User currentUser = securityUtils.getCurrentUser();
        List<Assignment> assignments = assignmentService.getAllActiveEmployeeAssignmentsByEmpId(currentUser.getId());
        Position flowInitiator = assignments.get(0).getPosition();

        WorkFlowMatrix workflowMatrix = getWorkFlowMatrix(license);
        Position processOwner = positionMasterService.getPositionById(license.getWorkflowContainer().getApproverPositionId());
        LicenseStateInfo licenseStateInfo = new LicenseStateInfo();
        licenseStateInfo.setOldAppType(license.getLicenseAppType().getName());
        if (workflowMatrix.isRejectEnabled() != null && workflowMatrix.isRejectEnabled())
            licenseStateInfo.setRejectionPosition(processOwner.getId());

        if (!license.hasState())
            license.transition().start();
        else
            license.transition().startNext();

        license.transition()
                .withSLA(licenseUtils.getSlaForAppType(licenseAppTypeService.getClosureLicenseApplicationType()))
                .withSenderName(currentUser.getName())
                .withComments(license.getWorkflowContainer().getApproverComments())
                .withStateValue(workflowMatrix.getNextState())
                .withDateInfo(new Date())
                .withOwner(processOwner)
                .withNextAction(workflowMatrix.getNextAction())
                .withExtraInfo(licenseStateInfo)
                .withNatureOfTask(licenseAppTypeService.getLicenseAppTypeByCode(CLOSURE_APPTYPE_CODE).getName())
                .withInitiator(flowInitiator);
    }

    private void startClosureProcessByExternalUsers(TradeLicense license) {
        WorkFlowMatrix workflowMatrix = getWorkFlowMatrix(license);
        List<Assignment> assignments = licenseProcessWorkflowService.getAssignments(workflowMatrix, license.getAdminWard());

        Position processOwner = assignments.get(0).getPosition();
        LicenseStateInfo licenseStateInfo = new LicenseStateInfo();
        licenseStateInfo.setOldAppType(license.getLicenseAppType().getName());
        if (workflowMatrix.isRejectEnabled() != null && workflowMatrix.isRejectEnabled())
            licenseStateInfo.setRejectionPosition(processOwner.getId());

        if (!license.hasState())
            license.transition().start();

        else
            license.transition().startNext();

        license.transition()
                .withSLA(licenseUtils.getSlaForAppType(licenseAppTypeService.getClosureLicenseApplicationType()))
                .withSenderName(securityUtils.getCurrentUser().getName())
                .withComments(license.getWorkflowContainer().getApproverComments())
                .withStateValue(workflowMatrix.getNextState())
                .withDateInfo(new Date())
                .withOwner(processOwner)
                .withNextAction(workflowMatrix.getNextAction())
                .withExtraInfo(licenseStateInfo)
                .withNatureOfTask(licenseAppTypeService.getLicenseAppTypeByCode(CLOSURE_APPTYPE_CODE).getName())
                .withInitiator(processOwner);
    }

    public void processCancellation(TradeLicense license) {
        User currentUser = securityUtils.getCurrentUser();
        license.transition().end()
                .withSenderName(currentUser.getUsername() + DELIMITER_COLON + currentUser.getName())
                .withComments(license.getWorkflowContainer().getApproverComments())
                .withDateInfo(new Date())
                .withNextAction(COMPLETED);
    }

    public void processApproval(TradeLicense license) {
        license.getWorkflowContainer().setAdditionalRule(CLOSURE_ADDITIONAL_RULE);
        WorkFlowMatrix workflowMatrix = getWorkFlowMatrix(license);
        User currentUser = securityUtils.getCurrentUser();
        license.transition().end()
                .withSenderName(currentUser.getUsername() + DELIMITER_COLON + currentUser.getName())
                .withComments(licenseConfigurationService.digitalSignEnabled() ? WF_DIGI_SIGNED : "Approved")
                .withDateInfo(new Date())
                .withStateValue(workflowMatrix.getNextState())
                .withNextAction(COMPLETED);
        license.setApprovedBy(currentUser);
    }

    public void processForward(TradeLicense license) {
        User currentUser = securityUtils.getCurrentUser();
        WorkFlowMatrix workflowMatrix = getWorkFlowMatrix(license);
        LicenseStateInfo licenseStateInfo = license.extraInfo();
        if (workflowMatrix.isRejectEnabled())
            licenseStateInfo.setRejectionPosition(license.getCurrentState().getOwnerPosition().getId());

        license.transition().progressWithStateCopy()
                .withSenderName(currentUser.getUsername() + DELIMITER_COLON + currentUser.getName())
                .withComments(license.getWorkflowContainer().getApproverComments())
                .withStateValue(workflowMatrix.getNextState())
                .withDateInfo(new Date())
                .withOwner(positionMasterService.getPositionById(license.getWorkflowContainer().getApproverPositionId()))
                .withNextAction(workflowMatrix.getNextAction())
                .withExtraInfo(licenseStateInfo);
    }

    public void processRejection(TradeLicense license) {
        User currentUser = securityUtils.getCurrentUser();
        WorkFlowMatrix workflowMatrix = getWorkFlowMatrix(license);
        LicenseStateInfo licenseStateInfo = license.extraInfo();
        Position processOwner;
        if (licenseStateInfo.getRejectionPosition() != null
                && !licenseStateInfo.getRejectionPosition().equals(license.getCurrentState().getOwnerPosition().getId()))
            processOwner = positionMasterService.getPositionById(licenseStateInfo.getRejectionPosition());
        else
            processOwner = license.getCurrentState().getInitiatorPosition();
        license.transition().progressWithStateCopy()
                .withSenderName(currentUser.getUsername() + DELIMITER_COLON + currentUser.getName())
                .withComments(license.getWorkflowContainer().getApproverComments())
                .withStateValue(workflowMatrix.getNextState())
                .withDateInfo(new Date())
                .withOwner(processOwner)
                .withNextAction(workflowMatrix.getNextAction())
                .withExtraInfo(licenseStateInfo);
    }

    public WorkFlowMatrix getWorkFlowMatrix(TradeLicense tradeLicense) {
        WorkflowContainer workflowContainer = tradeLicense.getWorkflowContainer();
        String additionalRule = BUTTONREJECT.equals(workflowContainer.getWorkFlowAction())
                ? CLOSURE_LICENSE_REJECT : workflowContainer.getAdditionalRule();
        WorkFlowMatrix workflowMatrix;
        if (tradeLicense.transitionInprogress()) {
            State<Position> state = tradeLicense.getState();
            workflowMatrix = this.licenseWorkflowService.getWfMatrix(
                    tradeLicense.getStateType(), "ANY", null, additionalRule,
                    workflowContainer.getCurrentState() == null ? state.getValue() : workflowContainer.getCurrentState(),
                    null, new Date(), workflowContainer.getCurrentDesignation() == null
                            ? "%" + state.getOwnerPosition().getDeptDesig().getDesignation().getName() + "%" :
                            workflowContainer.getCurrentDesignation());
        } else {
            workflowMatrix = this.licenseWorkflowService.getWfMatrix(tradeLicense.getStateType(), null,
                    null, workflowContainer.getAdditionalRule(), "Start", null, new Date(), null);
        }
        return workflowMatrix;
    }
}
