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

package org.egov.pgr.service;

import org.egov.eis.entity.Assignment;
import org.egov.eis.entity.Employee;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.EisCommonService;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.config.persistence.datasource.routing.annotation.ReadOnly;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.pgr.entity.Complaint;
import org.egov.pims.commons.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.egov.infra.utils.ApplicationConstant.NA;
import static org.egov.infra.utils.ApplicationConstant.SYSTEM_USERNAME;
import static org.egov.pgr.utils.constants.PGRConstants.COMPLAINT_ESCALATED;
import static org.egov.pgr.utils.constants.PGRConstants.DELIMITER_COLON;
import static org.egov.pgr.utils.constants.PGRConstants.NOASSIGNMENT;

@Service
@Transactional(readOnly = true)
public class ComplaintHistoryService {

    private static final String DATE = "date";
    private static final String COMMENT = "comments";
    private static final String UPDATEDBY = "updatedBy";
    private static final String UPDATEDUSERTYPE = "updatedUserType";
    private static final String USERTYPE = "usertype";
    private static final String USER = "user";
    private static final String STATUS = "status";
    private static final String DEPT = "department";
    private static final String CURRENT_ASSIGNEE = "%s [%s]";

    @Autowired
    private EisCommonService eisCommonService;

    @Autowired
    private AssignmentService assignmentService;

    @ReadOnly
    public List<HashMap<String, Object>> getComplaintHistory(Complaint complaint) {
        List<HashMap<String, Object>> complaintHistory = new ArrayList<>();
        if (complaint != null) {
            complaintHistory.add(buildComplaintHistory(complaint.getCurrentState()));
            complaintHistory.addAll(complaint.getStateHistory()
                    .stream()
                    .sorted(Comparator.comparing(StateHistory<Position>::getLastModifiedDate).reversed())
                    .map(stateHistory -> buildComplaintHistory(stateHistory.asState()))
                    .collect(Collectors.toList()));
        }
        return complaintHistory;
    }

    private HashMap<String, Object> buildComplaintHistory(State<Position> state) {
        HashMap<String, Object> history = new HashMap<>();
        history.put(DATE, state.getLastModifiedDate());
        history.put(COMMENT, defaultString(state.getComments()));
        history.put(STATUS, state.getLastModifiedBy().getUsername().equals(SYSTEM_USERNAME) ?
                COMPLAINT_ESCALATED : state.getValue());
        String[] senderName = state.getSenderName().split(DELIMITER_COLON);
        history.put(UPDATEDBY, senderName.length > 1 ? senderName[1] : senderName[0]);
        history.put(UPDATEDUSERTYPE, state.getLastModifiedBy().getType());
        Position ownerPosition = state.getOwnerPosition();
        User user = state.getOwnerUser();
        if (user == null && ownerPosition != null) {
            List<Assignment> assignments = assignmentService.getAssignmentsForPosition(ownerPosition.getId(),
                    state.getLastModifiedDate());
            if (ownerPosition.getDeptDesig() != null) {
                Optional<Employee> employee = assignments.isEmpty() ? Optional.empty() :
                        Optional.ofNullable(assignments.get(0).getEmployee());
                history.put(USER, employee.isPresent()
                        ? format(CURRENT_ASSIGNEE, employee.get().getName(), ownerPosition.getDeptDesig().getDesignation().getName())
                        : format(CURRENT_ASSIGNEE, NOASSIGNMENT, ownerPosition.getName()));
                history.put(USERTYPE, employee.isPresent() ? employee.get().getType() : EMPTY);
                history.put(DEPT, ownerPosition.getDeptDesig().getDepartment().getName());
            }
        } else {
            history.put(USER, user.getName());
            history.put(USERTYPE, user.getType());
            Department department = eisCommonService.getDepartmentForUser(user.getId());
            history.put(DEPT, department == null ? NA : department.getName());
        }
        return history;
    }
}
