/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2017  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
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
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.pgr.service;

import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.pgr.entity.Complaint;
import org.egov.pims.commons.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

import static org.egov.pgr.utils.constants.PGRConstants.DELIMITER_COLON;
import static org.egov.pgr.utils.constants.PGRConstants.GO_ROLE_NAME;

@Service
public class ComplaintProcessFlowService {

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

    public void onRegistration(Complaint complaint) {
        Position assignee = complaintRouterService.getAssignee(complaint);
        complaint.transition()
                .start()
                .withSenderName(complaint.getComplainant().getName())
                .withComments(String.format(GRIEVANCE_REG_COMMENT, complaint.getCrn()))
                .withStateValue(complaint.getStatus().getName())
                .withOwner(assignee).withDateInfo(new Date());
        complaint.setAssignee(assignee);
        complaint.setEscalationDate(escalationService.getExpiryDate(complaint));
    }

    public void onUpdation(Complaint complaint) {

        User currentUser = securityUtils.getCurrentUser();
        String userName;
        if (securityUtils.currentUserIsCitizen())
            userName = currentUser.getName();
        else
            userName = currentUser.getUsername() + DELIMITER_COLON + currentUser.getName();
        if (!complaint.transitionCompleted() && complaint.completed()) {
            complaint.setDepartment(complaint.getAssignee().getDeptDesig().getDepartment());
            if (!currentUser.hasRole(GO_ROLE_NAME))
                complaint.transition().end().withComments(complaint.approverComment())
                        .withStateValue(complaint.getStatus().getName()).withSenderName(userName)
                        .withDateInfo(new Date());

            else
                complaint.transition().end().withComments(complaint.approverComment())
                        .withStateValue(complaint.getStatus().getName()).withSenderName(userName)
                        .withDateInfo(new Date()).withOwner(complaint.getState().getOwnerPosition());
        } else if (complaint.hasNextOwner()) {
            Position owner = positionMasterService.getPositionById(complaint.nextOwnerId());
            complaint.setAssignee(owner);
            complaint.setDepartment(complaint.getAssignee().getDeptDesig().getDepartment());
            complaint.transition().progressWithStateCopy().withOwner(owner).withComments(complaint.approverComment()).withSenderName(userName)
                    .withStateValue(complaint.getStatus().getName()).withDateInfo(new Date());
        } else if (complaint.sendToPreviousOwner() && canSendToPreviousAssignee(complaint)) {
            Position nextAssignee = complaint.previousAssignee();
            complaint.setDepartment(nextAssignee.getDeptDesig().getDepartment());
            complaint.setAssignee(nextAssignee);
            complaint.transition().progressWithStateCopy().withComments(complaint.approverComment()).withSenderName(userName)
                    .withStateValue(complaint.getStatus().getName()).withOwner(nextAssignee).withDateInfo(new Date());

        } else if (complaint.reopened()) {
            complaint.transition().reopen().withComments(complaint.approverComment()).withSenderName(userName)
                    .withStateValue(complaint.getStatus().getName()).withDateInfo(new Date());
        } else if (complaint.inprogress()) {
            complaint.transition().progressWithStateCopy().withComments(complaint.approverComment()).withSenderName(userName)
                    .withStateValue(complaint.getStatus().getName()).withDateInfo(new Date());
        }
    }

    public boolean canSendToPreviousAssignee(Complaint complaint) {
        return complaint.hasState() && complaint.previousAssignee() != null &&
                forwardSkippablePositionService.isSkippablePosition(complaint.currentAssignee());
    }
}
