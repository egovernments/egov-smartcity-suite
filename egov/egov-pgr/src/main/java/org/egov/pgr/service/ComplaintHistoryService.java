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

import org.egov.eis.entity.Assignment;
import org.egov.eis.entity.Employee;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.EisCommonService;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.config.persistence.datasource.routing.annotation.ReadOnly;
import org.egov.infra.persistence.entity.enums.UserType;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.pgr.entity.Complaint;
import org.egov.pims.commons.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.egov.pgr.utils.constants.PGRConstants.COMMENT;
import static org.egov.pgr.utils.constants.PGRConstants.DATE;
import static org.egov.pgr.utils.constants.PGRConstants.DELIMITER_COLON;
import static org.egov.pgr.utils.constants.PGRConstants.DEPT;
import static org.egov.pgr.utils.constants.PGRConstants.ESCALATEDSTATUS;
import static org.egov.pgr.utils.constants.PGRConstants.NOASSIGNMENT;
import static org.egov.pgr.utils.constants.PGRConstants.STATUS;
import static org.egov.pgr.utils.constants.PGRConstants.SYSTEMUSER;
import static org.egov.pgr.utils.constants.PGRConstants.UPDATEDBY;
import static org.egov.pgr.utils.constants.PGRConstants.UPDATEDUSERTYPE;
import static org.egov.pgr.utils.constants.PGRConstants.USER;
import static org.egov.pgr.utils.constants.PGRConstants.USERTYPE;

@Service
@Transactional(readOnly = true)
public class ComplaintHistoryService {

    @Autowired
    private EisCommonService eisCommonService;

    @Autowired
    private AssignmentService assignmentService;

    @ReadOnly
    public List<HashMap<String, Object>> getHistory(Complaint complaint) {
        List<HashMap<String, Object>> historyTable = new ArrayList<>();
        State<Position> state = complaint.getState();
        HashMap<String, Object> map = new HashMap<>();
        map.put(DATE, state.getDateInfo());
        map.put(COMMENT, defaultString(state.getComments()));
        map.put(STATUS, state.getValue());
        if ("Complaint is escalated".equals(state.getComments())) {
            map.put(UPDATEDBY, SYSTEMUSER);
            map.put(STATUS, ESCALATEDSTATUS);
        } else if (!state.getLastModifiedBy().getType().equals(UserType.EMPLOYEE))
            map.put(UPDATEDBY, complaint.getComplainant().getName());
        else
            map.put(UPDATEDBY, defaultIfBlank(state.getSenderName(),
                    new StringBuilder().append(state.getLastModifiedBy().getUsername()).append(DELIMITER_COLON)
                            .append(state.getLastModifiedBy().getName()).toString()));
        map.put(UPDATEDUSERTYPE, state.getLastModifiedBy().getType());

        Position ownerPosition = state.getOwnerPosition();
        User user = state.getOwnerUser();
        if (user != null) {
            map.put(USER, user.getUsername() + DELIMITER_COLON + user.getName());
            map.put(USERTYPE, user.getType());
            Department department = eisCommonService.getDepartmentForUser(user.getId());
            map.put(DEPT, defaultString(department.getName()));
        } else if (ownerPosition != null && ownerPosition.getDeptDesig() != null) {
            List<Assignment> assignmentList = assignmentService.getAssignmentsForPosition(ownerPosition.getId(),
                    new Date());
            Optional<Employee> employee = !assignmentList.isEmpty()
                    ? Optional.ofNullable(assignmentList.get(0).getEmployee())
                    : Optional.empty();
            map.put(USER, employee.isPresent()
                    ? new StringBuilder().append(employee.get().getUsername()).append(DELIMITER_COLON)
                    .append(employee.get().getName()).append(DELIMITER_COLON)
                    .append(ownerPosition.getDeptDesig().getDesignation().getName()).toString()
                    : new StringBuilder().append(NOASSIGNMENT).append(DELIMITER_COLON).append(ownerPosition.getName())
                    .toString());
            map.put(USERTYPE, employee.isPresent() ? employee.get().getType() : EMPTY);
            map.put(DEPT, ownerPosition.getDeptDesig().getDepartment().getName());
        }
        historyTable.add(map);

        complaint.getStateHistory().stream()
                .sorted(Comparator.comparing(StateHistory<Position>::getLastModifiedDate).reversed())
                .forEach(stateHistory -> historyTable.add(constructComplaintHistory(complaint, stateHistory)));
        return historyTable;
    }

    private HashMap<String, Object> constructComplaintHistory(Complaint complaint, StateHistory<Position> stateHistory) {
        HashMap<String, Object> complaintHistory = new HashMap<>();
        complaintHistory.put(DATE, stateHistory.getDateInfo());
        complaintHistory.put(COMMENT, defaultString(stateHistory.getComments()));
        complaintHistory.put(STATUS, stateHistory.getValue());
        if ("Complaint is escalated".equals(stateHistory.getComments())) {
            complaintHistory.put(UPDATEDBY, SYSTEMUSER);
            complaintHistory.put(STATUS, ESCALATEDSTATUS);
        } else
            complaintHistory.put(UPDATEDBY, stateHistory.getLastModifiedBy().getType().equals(UserType.EMPLOYEE)
                    ? stateHistory.getSenderName() : complaint.getComplainant().getName());

        complaintHistory.put(UPDATEDUSERTYPE, stateHistory.getLastModifiedBy().getType());
        Position owner = stateHistory.getOwnerPosition();
        User userobj = stateHistory.getOwnerUser();
        if (userobj != null) {
            complaintHistory.put(USER, userobj.getUsername() + DELIMITER_COLON + userobj.getName());
            complaintHistory.put(USERTYPE, userobj.getType());
            Department department = eisCommonService.getDepartmentForUser(userobj.getId());
            complaintHistory.put(DEPT, department != null ? department.getName() : EMPTY);
        } else if (owner != null && owner.getDeptDesig() != null) {
            List<Assignment> assignments = assignmentService.getAssignmentsForPosition(owner.getId(), new Date());
            complaintHistory
                    .put(USER,
                            !assignments.isEmpty() ? new StringBuilder().append(assignments.get(0).getEmployee().getUsername())
                                    .append(DELIMITER_COLON).append(assignments.get(0).getEmployee().getName())
                                    .append(DELIMITER_COLON)
                                    .append(owner.getDeptDesig().getDesignation().getName()).toString()
                                    : NOASSIGNMENT + DELIMITER_COLON + owner.getName());
            complaintHistory.put(USERTYPE, !assignments.isEmpty() ? assignments.get(0).getEmployee().getType() : EMPTY);
            complaintHistory.put(DEPT, owner.getDeptDesig().getDepartment() != null
                    ? owner.getDeptDesig().getDepartment().getName() : EMPTY);
        }
        return complaintHistory;
    }
}
