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
package org.egov.eis.service;

import org.egov.eis.entity.Assignment;
import org.egov.eis.entity.Employee;
import org.egov.eis.entity.HeadOfDepartments;
import org.egov.eis.repository.HeadOfDepartmentsRepository;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.egov.pims.model.PersonalInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This service class provides API(s) which are required by modules depending on EIS
 *
 * @author Vaibhav.K
 */
@Service
@Transactional(readOnly = true)
public class EisCommonService {

    @Autowired
    private PositionHierarchyService positionHierarchyService;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private PersonalInformationService personalInformationService;

    @Autowired
    private HeadOfDepartmentsRepository employeeDepartmentRepository;

    /**
     * Refer to position master service for the same API
     *
     * @param objectId
     * @param posId
     * @return returns position object
     */
    @Deprecated
    public Position getSuperiorPositionByObjectTypeAndPositionFrom(final Integer objectId, final Long posId) {
        return positionHierarchyService.getPositionHierarchyByPosAndObjectType(posId, objectId).getToPosition();
    }

    /**
     * Refer to position master service for the same API
     *
     * @param objectId
     * @param objectSubType
     * @param posId
     * @return returns position object
     */
    @Deprecated
    public Position getSuperiorPositionByObjectAndObjectSubTypeAndPositionFrom(final Integer objectId,
            final String objectSubType, final Long posId) {
        return positionHierarchyService.getPosHirByPosAndObjectTypeAndObjectSubType(posId, objectId, objectSubType)
                .getToPosition();
    }

    /**
     * Returns user for the given position
     *
     * @param posId
     * @param givenDate
     * @return User object
     */
    public User getUserForPosition(final Long posId, final Date givenDate) {
        try {
            return assignmentService.getAssignmentsForPosition(posId, givenDate).get(0).getEmployee();
        } catch (final NullPointerException e) {
            throw new ApplicationRuntimeException("User Not Found");
        } catch (final Exception e) {
            throw new ApplicationRuntimeException(e.getMessage());
        }
    }

    /**
     * Refer to designation service
     *
     * @param posId
     * @return Designation object
     */
    @Deprecated
    public Designation getEmployeeDesignation(final Long posId) {
        return assignmentService.getPrimaryAssignmentForPositon(posId).getDesignation();
    }

    /**
     * Returns employee department for user
     *
     * @param userId
     * @return Department object
     */
    public Department getDepartmentForUser(final Long userId) {
        return assignmentService.getPrimaryAssignmentForUser(userId).getDepartment();
    }

    /**
     * Returns employee position for user
     *
     * @param userId
     * @return Position object
     */
    @Deprecated
    public Position getPositionByUserId(final Long userId) {
        return assignmentService.getPrimaryAssignmentForUser(userId).getPosition();
    }

    /**
     * Please refer the same API from Assignment service
     *
     * @param empId
     * @return Assignment object
     */
    @Deprecated
    public Assignment getLatestAssignmentForEmployee(final Long empId) {
        return assignmentService.getPrimaryAssignmentForEmployee(empId);
    }

    /**
     * Please refer the same API from Assignment service
     *
     * @param empId
     * @param toDate
     * @return Assignment object
     */
    @Deprecated
    public Assignment getLatestAssignmentForEmployeeByToDate(final Long empId, final Date toDate) {
        return assignmentService.getPrimaryAssignmentForEmployeeByToDate(empId, toDate);
    }

    public Assignment getLatestAssignmentForEmployeeByDate(final Long empId, final Date toDate) {
        return assignmentService.findByEmployeeAndGivenDate(empId, toDate).get(0);
     }
    /**
     * Refer to Position master service for the same API
     *
     * @param empId
     * @return Position object
     */
    @Deprecated
    public Position getPrimaryAssignmentPositionForEmp(final Long empId) {
        return assignmentService.getPrimaryAssignmentForEmployee(empId).getPosition();
    }

    /**
     * Refer to employee service for the same API
     *
     * @param posId
     * @return Employee object
     */
    @Deprecated
    public Employee getPrimaryAssignmentEmployeeForPos(final Long posId) {
        return assignmentService.getPrimaryAssignmentForPositon(posId).getEmployee();
    }

    /**
     * Refer EmployeeService for getting Employee object by user id i.e. EmployeeService.getEmployeeById
     *
     * @param userId
     * @return PersonalInformation object
     */
    @Deprecated
    public PersonalInformation getEmployeeByUserId(final Long userId) {
        return personalInformationService.getEmployeeByUserId(userId);
    }

    /**
     * Refer the same API in assignment service
     *
     * @param assignId
     * @return true if HOD else false
     */
    @Deprecated
    public Boolean isHod(final Long assignId) {
        final List<HeadOfDepartments> hodList = employeeDepartmentRepository.getAllHodDepartments(assignId);
        return !hodList.isEmpty();
    }

    /**
     * Refer the same API in employee service
     *
     * @param posId
     * @param givenDate
     * @return Employee object
     */
    @Deprecated
    public Employee getEmployeeForPositionAndDate(final Long posId, final Date givenDate) {
        return assignmentService.getPrimaryAssignmentForPositionAndDate(posId, givenDate).getEmployee();
    }

    /**
     * Get all active users by designation as per the current date
     *
     * @param designationId
     * @return List of active users
     */
    public List<User> getAllActiveUsersByGivenDesig(final Long designationId) {
        final Set<User> users = new HashSet<User>();
        final List<Assignment> assignments = assignmentService.getAllActiveAssignments(designationId);
        for (final Assignment assign : assignments)
            users.add(assign.getEmployee());
        return new ArrayList<User>(users);
    }

}