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

package org.egov.pgr.service;

import org.egov.eis.entity.Assignment;
import org.egov.eis.entity.EmployeeView;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.EisCommonService;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.exception.ApplicationValidationException;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.workflow.service.OwnerGroupService;
import org.egov.pgr.entity.Complaint;
import org.egov.pims.commons.Position;
import org.egov.pims.service.EisUtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.egov.infra.config.core.ApplicationThreadLocals.getUserId;
import static org.egov.infra.security.utils.SecurityUtils.currentUserIsAnonymous;
import static org.egov.pgr.utils.constants.PGRConstants.DELIMITER_COLON;
import static org.egov.pgr.utils.constants.PGRConstants.GO_ROLE_NAME;
import static org.egov.pgr.utils.constants.PGRConstants.GRO_ROLE_NAME;
import static org.egov.pgr.utils.constants.PGRConstants.RO_ROLE_NAME;

@Service
public class GrievanceProcessFlowService {

    private static final String GRIEVANCE_REG_COMMENT = "Grievance registered with CRN : %s";

    @Autowired
    private ComplaintEscalationService escalationService;

    @Autowired
    private ComplaintRouterService complaintRouterService;

    @Autowired
    private ForwardSkippablePositionService forwardSkippablePositionService;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private PositionMasterService positionMasterService;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private UserService userService;

    @Autowired
    private GrievanceConfigurationService grievanceConfigurationService;

    @Autowired
    private OwnerGroupService<Position> ownerGroupService;

    @Autowired
    private EisUtilService eisService;

    @Autowired
    private EisCommonService eisCommonService;

    public void onRegistration(Complaint complaint) {
        Position assignee = complaintRouterService.getComplaintAssignee(complaint);
        complaint.transition()
                .start()
                .withSLAHours(complaint.getComplaintType().getSlaHours())
                .withSenderName(complaint.getComplainant().getName())
                .withComments(String.format(GRIEVANCE_REG_COMMENT, complaint.getCrn()))
                .withStateValue(complaint.getStatus().getName())
                .withOwner(assignee);
        complaint.setAssignee(assignee);
        complaint.setEscalationDate(escalationService.getExpiryDate(complaint));
        assignProcessOwner(complaint);
    }

    public void onUpdation(Complaint complaint) {
        User currentUser = securityUtils.getCurrentUser();
        String userName = securityUtils.currentUserIsCitizen() ?
                currentUser.getName() : currentUser.getUsername() + DELIMITER_COLON + currentUser.getName();
        if (!complaint.transitionCompleted() && complaint.completed()) {
            complaint.setCompletionDate(new Date());
            complaint.resolvedNow(true);
            if (securityUtils.currentUserIsEmployee())
                complaint.setCurrentOwner(currentUser);
            if (!userRoleAuthorizedToUpdate(currentUser)) {
                complaint.transition()
                        .end()
                        .withComments(complaint.approverComment())
                        .withStateValue(complaint.getStatus().getName())
                        .withSenderName(userName);
            } else {
                complaint.transition()
                        .end()
                        .withComments(complaint.approverComment())
                        .withStateValue(complaint.getStatus().getName())
                        .withSenderName(userName)
                        .withOwner(complaint.getState().getOwnerPosition());
            }
        } else if (!complaint.transitionCompleted() && complaint.hasNextOwner()) {
            Position owner = validateAndGetProcessOwner(complaint.nextOwnerId());
            complaint.setAssignee(owner);
            complaint.transition()
                    .progressWithStateCopy()
                    .withOwner(owner)
                    .withComments(complaint.approverComment())
                    .withSenderName(userName)
                    .withStateValue(complaint.getStatus().getName());
        } else if (complaint.sendToPreviousOwner() && canSendToPreviousAssignee(complaint)) {
            Position nextAssignee = complaint.previousAssignee();
            complaint.setDepartment(nextAssignee.getDeptDesig().getDepartment());
            complaint.setAssignee(nextAssignee);
            assignProcessOwner(complaint);
            complaint.transition()
                    .progressWithStateCopy()
                    .withComments(complaint.approverComment())
                    .withSenderName(userName)
                    .withStateValue(complaint.getStatus().getName())
                    .withOwner(nextAssignee);
        } else if (complaint.reopened()) {
            Position nextAssignee = complaint.getState().getOwnerPosition();
            complaint.setCompletionDate(null);
            if (grievanceConfigurationService.assignReopenedComplaintBasedOnRouterPosition())
                nextAssignee = complaintRouterService.getComplaintAssignee(complaint);
            complaint.transition()
                    .reopen()
                    .withComments(complaint.approverComment())
                    .withSenderName(userName).withOwner(nextAssignee)
                    .withStateValue(complaint.getStatus().getName());
        } else if (complaint.inprogress()) {
            complaint.transition()
                    .progressWithStateCopy()
                    .withComments(complaint.approverComment())
                    .withSenderName(userName)
                    .withStateValue(complaint.getStatus().getName());
        }

    }

    public boolean canSendToPreviousAssignee(Complaint complaint) {
        return complaint.hasState() && complaint.previousAssignee() != null &&
                forwardSkippablePositionService.isSkippablePosition(complaint.currentAssignee());
    }

    public boolean authorizedToUpdate(Complaint complaint) {
        User currentUser = securityUtils.getCurrentUser();
        return !currentUserIsAnonymous() && securityUtils.currentUserIsCitizen()
                ? complaint.getCreatedBy().equals(currentUser)
                : userRoleAuthorizedToUpdate(currentUser)
                || this.ownerGroupService.getOwnerGroupsByUserId(getUserId())
                .stream()
                .anyMatch(position -> position.equals(complaint.getState().getOwnerPosition()));
    }

    public boolean userRoleAuthorizedToUpdate(User user) {
        return user.hasAnyRole(grievanceConfigurationService.updateAllowedRoles());
    }

    public Set<EmployeeView> authorizedProcessOwners(Long department, Long designation) {
        String currentUserName = securityUtils.getCurrentUser().getUsername();
        HashMap<String, String> params = new HashMap<>();
        params.put("departmentId", String.valueOf(department));
        params.put("designationId", String.valueOf(designation));
        return eisService.getEmployeeInfoList(params)
                .stream()
                .filter(employeeView -> (employeeView.getEmployee().hasAnyRole(RO_ROLE_NAME, GO_ROLE_NAME, GRO_ROLE_NAME))
                        && !currentUserName.equals(employeeView.getUserName()))
                .collect(Collectors.toSet());
    }

    private void assignProcessOwner(Complaint complaint) {
        List<Assignment> assignments = assignmentService.getAssignmentsForPosition(complaint.getAssignee().getId(), new Date());
        complaint.setCurrentOwner(assignments.isEmpty() ? null : assignments.get(0).getEmployee());
        if (complaint.getCurrentOwner() == null) {
            Iterator<User> grievanceOfficers = userService
                    .getUsersByRoleNames(grievanceConfigurationService.updateAllowedRoles()).iterator();
            if (grievanceOfficers.hasNext())
                complaint.setCurrentOwner(grievanceOfficers.next());
        }
    }

    private Position validateAndGetProcessOwner(Long ownerId) {
        Position owner = positionMasterService.getPositionById(ownerId);
        if (owner != null) {
            User user = eisCommonService.getUserForPosition(ownerId, new Date());
            if (user != null && user.hasAnyRole(RO_ROLE_NAME, GO_ROLE_NAME, GRO_ROLE_NAME))
                return owner;
        }
        throw new ApplicationValidationException("PGR.003");
    }
}
