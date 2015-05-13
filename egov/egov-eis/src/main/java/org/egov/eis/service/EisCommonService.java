/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It
	   is required that all modified versions of this material be marked in
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program
	   with regards to rights under trademark law for use of the trade names
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.eis.service;

import java.util.Date;
import java.util.List;

import org.egov.eis.repository.EmployeeDepartmentRepository;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.commons.Position;
import org.egov.pims.model.Assignment;
import org.egov.pims.model.EmployeeDepartment;
import org.egov.pims.model.PersonalInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * This service class provides API(s) which are required by modules depending on
 * EIS
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
    private UserService userService;

    @Autowired
    private PersonalInformationService personalInformationService;

    @Autowired
    private EmployeeDepartmentRepository employeeDepartmentRepository;

    /**
     * Returns the superior employee position
     *
     * @param objectId
     * @param posId
     * @return returns position object
     */
    public Position getSuperiorPositionByObjectTypeAndPositionFrom(final Integer objectId, final Long posId) {
        return positionHierarchyService.getPositionHierarchyByPosAndObjectType(posId, objectId).getToPosition();
    }

    /**
     * Returns the superior employee position
     *
     * @param objectId
     * @param objectSubType
     * @param posId
     * @return returns position object
     */
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
            return assignmentService.getAssignmentsForPosition(posId, givenDate).get(0).getEmployee().getUserMaster();
        } catch (final NullPointerException e) {
            throw new EGOVRuntimeException("User Not Found");
        } catch (final Exception e) {
            throw new EGOVRuntimeException(e.getMessage());
        }
    }

    /**
     * Returns employee designation for position
     *
     * @param posId
     * @return Designation object
     */
    public DesignationMaster getEmployeeDesignation(final Long posId) {
        return assignmentService.getPrimaryAssignmentForPositon(posId).getDesigId();
    }

    /**
     * Returns employee department for user
     *
     * @param userId
     * @return Department object
     */
    public Department getDepartmentForUser(final Long userId) {
        return assignmentService.getPrimaryAssignmentForUser(userId).getDeptId();
    }

    /**
     * Returns employee position for user
     *
     * @param userId
     * @return Position object
     */
    public Position getPositionByUserId(final Long userId) {
        return assignmentService.getPrimaryAssignmentForUser(userId).getPosition();
    }

    /**
     * Returns employee assignment
     *
     * @param empId
     * @return Assignment object
     */
    public Assignment getLatestAssignmentForEmployee(final Integer empId) {
        return assignmentService.getPriamryAssignmentForEmployee(empId);
    }

    /**
     * Returns latest employee primary assignment for a given date and employee
     * id
     *
     * @param empId
     * @param toDate
     * @return Assignment object
     */
    public Assignment getLatestAssignmentForEmployeeByToDate(final Integer empId, final Date toDate) {
        return assignmentService.getPrimaryAssignmentForEmployeeByToDate(empId, toDate);
    }

    /**
     * Returns primary assignment position for employee id
     *
     * @param empId
     * @return Position object
     */
    public Position getPrimaryAssignmentPositionForEmp(final Integer empId) {
        return assignmentService.getPriamryAssignmentForEmployee(empId).getPosition();
    }

    /**
     * Returns primary assignment's employee for position
     *
     * @param posId
     * @return PersonalInformation object
     */
    public PersonalInformation getPrimaryAssignmentEmployeeForPos(final Long posId) {
        return assignmentService.getPrimaryAssignmentForPositon(posId).getEmployee();
    }

    /**
     * Returns list of positions for an employee
     *
     * @param empId
     * @return List of position objects
     */
    public List<Position> getPositionsForEmployee(final Integer empId) {
        final List<Position> posList = null;
        final List<Assignment> assignList = assignmentService.getAllAssignmentsByEmpId(empId);
        for (final Assignment assign : assignList)
            posList.add(assign.getPosition());

        return posList;
    }

    /**
     * Returns list of employee for a given position
     *
     * @param posId
     * @return List of PersonalInformation
     */
    public List<PersonalInformation> getEmployeesForPosition(final Long posId) {
        final List<PersonalInformation> empList = null;

        final List<Assignment> assignList = assignmentService.getAssignmentsForPosition(posId);
        for (final Assignment assign : assignList)
            empList.add(assign.getEmployee());

        return empList;
    }

    /**
     * Returns employee for user
     *
     * @param userId
     * @return PersonalInformation object
     */
    public PersonalInformation getEmployeeByUserId(final Long userId) {
        return personalInformationService.getEmployeeByUserId(userId);
    }

    /**
     * Returns true if the given employee is an HOD
     *
     * @param assignId
     * @return true if HOD else false
     */
    public Boolean isHod(final Long assignId) {
        final List<EmployeeDepartment> hodList = employeeDepartmentRepository.getAllHodDepartments(assignId);
        return !hodList.isEmpty();
    }

    /**
     * Returns employee object for position id and given date
     *
     * @param posId
     * @param givenDate
     * @return Employee object
     */
    public PersonalInformation getEmployeeForPositionAndDate(final Long posId, final Date givenDate) {
        return assignmentService.getPrimaryAssignmentForPositionAndDate(posId, givenDate).getEmployee();
    }

}
