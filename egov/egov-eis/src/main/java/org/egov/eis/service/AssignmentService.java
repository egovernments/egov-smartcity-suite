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

import org.apache.commons.lang3.StringUtils;
import org.egov.eis.entity.Assignment;
import org.egov.eis.entity.Employee;
import org.egov.eis.entity.HeadOfDepartments;
import org.egov.eis.reports.entity.EmployeeAssignmentSearch;
import org.egov.eis.reports.entity.EmployeePositionSearch;
import org.egov.eis.repository.AssignmentRepository;
import org.egov.eis.repository.HeadOfDepartmentsRepository;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This service class is used to query all employee related assignments
 *
 * @author Vaibhav.K
 */
@Service
@Transactional(readOnly = true)
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final HeadOfDepartmentsRepository employeeDepartmentRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private BoundaryService boundaryService;

    @Autowired
    public AssignmentService(final AssignmentRepository assignmentRepository,
                             final HeadOfDepartmentsRepository employeeDepartmentRepository) {
        this.assignmentRepository = assignmentRepository;
        this.employeeDepartmentRepository = employeeDepartmentRepository;
    }

    public Assignment getAssignmentById(final Long id) {
        return assignmentRepository.findOne(id);
    }

    public List<Assignment> getAllAssignmentsByEmpId(final Long empId) {
        return assignmentRepository.getAllAssignmentsByEmpId(empId);
    }

    public List<Assignment> getAllActiveEmployeeAssignmentsByEmpId(final Long empId) {
        return assignmentRepository.getAllActiveAssignmentsByEmpId(empId);
    }

    public List<Assignment> getAssignmentsForPosition(final Long posId, final Date givenDate) {
        return assignmentRepository.getAssignmentsForPosition(posId, givenDate);
    }

    public Assignment getPrimaryAssignmentForPositon(final Long posId) {
        return assignmentRepository.getPrimaryAssignmentForPosition(posId);
    }

    @Transactional
    public void createAssignment(final Assignment assignment) {
        assignmentRepository.save(assignment);
    }

    @Transactional
    public void updateAssignment(final Assignment assignment) {
        assignmentRepository.save(assignment);
    }

    public Assignment getPrimaryAssignmentForUser(final Long userId) {
        return assignmentRepository.getPrimaryAssignmentForUser(userId);
    }

    public Assignment getPrimaryAssignmentForEmployee(final Long empId) {
        return assignmentRepository.getPrimaryAssignmentForEmployee(empId);
    }

    public Assignment getPrimaryAssignmentForEmployeeByToDate(final Long empId, final Date toDate) {
        return assignmentRepository.getAssignmentByEmpAndDate(empId, toDate);
    }

    public List<Assignment> getAssignmentsForPosition(final Long posId) {
        return assignmentRepository.getAssignmentsForPosition(posId);
    }

    public Boolean isHod(final Long assignId) {
        final List<HeadOfDepartments> hodList = employeeDepartmentRepository.getAllHodDepartments(assignId);
        return !hodList.isEmpty();
    }

    public Assignment getPrimaryAssignmentForPositionAndDate(final Long posId, final Date givenDate) {
        return assignmentRepository.getPrimaryAssignmentForPositionAndDate(posId, givenDate);
    }

    public Assignment getPrimaryAssignmentForGivenRange(final Long empId, final Date fromDate, final Date toDate) {
        return assignmentRepository.getPrimaryAssignmentForGivenRange(empId, fromDate, toDate);
    }

    public List<Assignment> getPositionsByDepartmentAndDesignationForGivenRange(final Long departmentId,
                                                                                final Long designationId, final Date givenDate) {

        if (departmentId != null && designationId != null)
            return assignmentRepository.getPrimaryAssignmentForDepartmentAndDesignation(departmentId, designationId,
                    givenDate);
        else if (designationId != null)
            return assignmentRepository.getPrimaryAssignmentForDesignation(designationId, givenDate);
        else if (departmentId != null)
            return assignmentRepository.getPrimaryAssignmentForDepartment(departmentId, givenDate);
        return new ArrayList<>();

    }

    public List<Assignment> getAllPositionsByDepartmentAndDesignationForGivenRange(final Long departmentId,
                                                                                   final Long designationId, final Date givenDate) {

        if (departmentId != null && designationId != null)
            return assignmentRepository.getAllAssignmentForDepartmentAndDesignation(departmentId, designationId,
                    givenDate);
        else if (designationId != null)
            return assignmentRepository.getAllAssignmentForDesignation(designationId, givenDate);
        else if (departmentId != null)
            return assignmentRepository.getAllAssignmentForDepartment(departmentId, givenDate);
        return new ArrayList<>();

    }

    public List<Assignment> getAllPositionsByDepartmentAndPositionNameForGivenRange(final Long departmentId,
                                                                                    final String positionName) {
        return assignmentRepository.getAllAssignmentForDepartmentAndPositionNameLike(departmentId, new Date(),
                positionName);
    }

    public List<Assignment> getAssignmentsByDeptDesigAndDates(final Long deptId, final Long desigId,
                                                              final Date fromDate, final Date toDate) {
        return assignmentRepository.findByDeptDesigAndDates(deptId, desigId, fromDate, toDate);
    }

    public List<Assignment> findAllAssignmentsByDeptDesigAndDates(final Long deptId, final Long desigId,
                                                                  final Date givenDate) {
        return assignmentRepository.findAllAssignmentsByDeptDesigAndGivenDate(deptId, desigId, givenDate);
    }

    public List<Assignment> findByEmployeeAndGivenDate(final Long empId, final Date givenDate) {
        return assignmentRepository.findByEmployeeAndGivenDate(empId, givenDate);
    }

    public List<Assignment> findPrimaryAssignmentForDesignationName(final String name) {
        return assignmentRepository.findPrimaryAssignmentForDesignationName(name);
    }

    public List<Assignment> findByDesignationAndBoundary(final Long desigId, final Long boundaryId) {
        return assignmentRepository.findByDesignationAndBoundary(desigId, getBoundaries(boundaryId));
    }

    public List<Assignment> findByDepartmentDesignationAndBoundary(final Long deptId, final Long desigId,
                                                                   final Long boundaryId) {

        List<Assignment> assignments = null;
        if (null == deptId)
            assignments = assignmentRepository.findByDesignationAndBoundary(desigId, getRequiredBoundaries(boundaryId));
        else if (null == desigId)
            assignments = assignmentRepository.findByDepartmentAndBoundary(deptId, getRequiredBoundaries(boundaryId));
        else
            assignments = assignmentRepository.findByDepartmentDesignationAndBoundary(deptId, desigId,
                    getRequiredBoundaries(boundaryId));
        return assignments;
    }

    public List<Assignment> findByDeptDesgnAndParentAndActiveChildBoundaries(final Long deptId, final Long desigId,
                                                                             final Long boundaryId) {
        return assignmentRepository.findByDepartmentDesignationAndBoundary(deptId, desigId,
                getBoundaries(boundaryId));
    }

    public List<Assignment> findAssignmentByDepartmentDesignationAndBoundary(final Long deptId, final Long desigId,
                                                                             final Long boundaryId) {

        List<Assignment> assignments;
        Set<Long> boundaries = new HashSet<>();
        boundaries.add(boundaryId);
        if (null == deptId)
            assignments = assignmentRepository.findByDesignationAndBoundary(desigId, boundaries);
        else if (null == desigId)
            assignments = assignmentRepository.findByDepartmentAndBoundary(deptId, boundaries);
        else
            assignments = assignmentRepository.findByDepartmentDesignationAndBoundary(deptId, desigId,
                    boundaries);
        return assignments;
    }

    private Set<Long> getBoundaries(final Long boundaryId) {
        final Set<Long> bndIds = new HashSet<>();
        final List<Boundary> boundaries = boundaryService.findActiveChildrenWithParent(boundaryId);
        boundaries.forEach(bndry -> bndIds.add(bndry.getId()));
        return bndIds;
    }

    public Set<Long> getRequiredBoundaries(final Long boundaryId) {
        final Set<Long> bndIds = new HashSet<>();
        final Boundary childBndry = boundaryService.getBoundaryById(boundaryId);
        String childmpath = childBndry.getMaterializedPath();
        final Set<String> mpathStr = new HashSet<>();
        mpathStr.add(childBndry.getMaterializedPath());
        for (int i = 0; i < childmpath.length(); i++) {
            childmpath = childmpath.substring(0, childmpath.lastIndexOf('.'));
            mpathStr.add(childmpath);
        }

        final List<Boundary> boundaries = boundaryService.findActiveBoundariesForMpath(mpathStr);
        boundaries.forEach(bndry -> bndIds.add(bndry.getId()));
        return bndIds;
    }

    public List<Assignment> getAllActiveAssignments(final Long designationId) {
        return assignmentRepository.getAllActiveAssignments(designationId);
    }

    @Transactional
    public Employee removeDeletedAssignments(final Employee employee, final String removedAssignIds) {
        if (null != removedAssignIds)
            for (final String id : removedAssignIds.split(","))
                employee.getAssignments().remove(assignmentRepository.findOne(Long.valueOf(id)));
        return employee;
    }

    public Set<User> getUsersByDesignations(final String[] designationNames) {
        return assignmentRepository.getUsersByDesignations(designationNames);
    }

    public Set<Role> getRolesForExpiredAssignmentsByEmpId(final Long empId) {
        return assignmentRepository.getRolesForExpiredAssignmentsByEmpId(empId);
    }

    public Set<Role> getRolesForActiveAssignmentsByEmpId(final Long empId) {
        return assignmentRepository.getRolesForActiveAssignmentsByEmpId(empId);
    }

    public List<Assignment> findByDepartmentDesignationsAndGivenDate(final Long deptId, final List<Long> desigIds,
                                                                     final Date givenDate) {
        return assignmentRepository.findByDepartmentDesignationsAndGivenDate(deptId, desigIds, givenDate);
    }

    public List<Assignment> getAllAssignmentsByPositionNameForGivenRange(final String positionName) {
        return assignmentRepository.getAllAssignmentForPositionNameLike(new Date(), positionName);
    }

    public List<Position> findPositionsForEmployees(final String name) {
        return assignmentRepository.findEmployeePositions("%" + name + "%");
    }

    public List<Employee> searchEmployeeAssignments(final EmployeeAssignmentSearch employeeAssignmentSearch) {
        final StringBuilder queryString = new StringBuilder();
        queryString.append("select distinct(assign.employee) from Assignment assign where assign.id is not null ");
        if (StringUtils.isNotBlank(employeeAssignmentSearch.getEmployeeCode()))
            queryString.append(" AND assign.employee.code =:code ");
        if (StringUtils.isNotBlank(employeeAssignmentSearch.getEmployeeName()))
            queryString.append(" AND upper(assign.employee.name) like :name ");
        if (employeeAssignmentSearch.getDepartment() != null)
            queryString.append(" AND assign.department.id =:department ");
        if (employeeAssignmentSearch.getDesignation() != null)
            queryString.append(" AND assign.designation.id =:designation ");
        if (employeeAssignmentSearch.getPosition() != null)
            queryString.append(" AND assign.position.id =:position ");
        if (employeeAssignmentSearch.getAssignmentDate() != null)
            queryString.append(" AND assign.fromDate <=:assignDate AND assign.toDate >= :assignDate ");
        queryString.append(" Order by assign.employee.code, assign.employee.name ");
        final Query query = entityManager.unwrap(Session.class).createQuery(queryString.toString());
        if (StringUtils.isNotBlank(employeeAssignmentSearch.getEmployeeCode()))
            query.setParameter("code", employeeAssignmentSearch.getEmployeeCode());
        if (StringUtils.isNotBlank(employeeAssignmentSearch.getEmployeeName()))
            query.setParameter("name", "%" + employeeAssignmentSearch.getEmployeeName().toUpperCase() + "%");
        if (employeeAssignmentSearch.getDepartment() != null)
            query.setParameter("department", employeeAssignmentSearch.getDepartment());
        if (employeeAssignmentSearch.getDesignation() != null)
            query.setParameter("designation", employeeAssignmentSearch.getDesignation());
        if (employeeAssignmentSearch.getPosition() != null)
            query.setParameter("position", employeeAssignmentSearch.getPosition());
        if (employeeAssignmentSearch.getAssignmentDate() != null)
            query.setParameter("assignDate", employeeAssignmentSearch.getAssignmentDate());
        return query.list();
    }

    public List<Assignment> getAssignmentList(final EmployeePositionSearch employeePositionSearch) {
        final StringBuilder queryString = new StringBuilder();
        queryString.append("select distinct(assignment) from Assignment assignment where assignment.id is not null ");
        if (employeePositionSearch.getPosition() != null)
            queryString.append(" AND assignment.position.id =:pos ");
        if (employeePositionSearch.getIsPrimary() != null)
            queryString.append(" AND assignment.primary =:primary ");
        Query queryResult = entityManager.unwrap(Session.class).createQuery(queryString.toString());
        queryResult = setParametersToQuery(employeePositionSearch, queryResult);

        return queryResult.list();
    }

    public List<Assignment> findAllAssignmentsByHODDeptAndDates(final Long deptId, final Date givenDate) {
        return assignmentRepository.findAllAssignmentsByHODDeptAndGivenDate(deptId, givenDate);
    }

    private Query setParametersToQuery(final EmployeePositionSearch employeePositionSearch, final Query queryResult) {
        if (employeePositionSearch.getPosition() != null)
            queryResult.setParameter("pos", Long.valueOf(employeePositionSearch.getPosition()));
        if (employeePositionSearch.getIsPrimary() != null)
            queryResult.setParameter("primary", Boolean.valueOf(employeePositionSearch.getIsPrimary()));
        return queryResult;
    }

    public List<Assignment> getAllAssignmentForEmployeeNameLike(final String empName) {
        return assignmentRepository.getAllAssignmentForEmployeeNameLike(new Date(), empName);
    }

    public List<Assignment> findAssignmentForDepartmentId(final Long deptId) {
        return assignmentRepository.findByDepartmentId(deptId);
    }

    public List<Assignment> findAssignmentForDesignationId(final Long desigId) {
        return assignmentRepository.findByDesignationId(desigId);
    }

    public List<Assignment> findByDepartmentAndDesignation(final Long deptId, final Long desigId) {
        return assignmentRepository.findByDepartmentIdAndDesignationId(deptId, desigId);
    }

    public List<Assignment> getPrimaryAssignmentForPositionAndDateRange(final Long posId, final Date fromDate,
                                                                        final Date toDate) {
        return assignmentRepository.getPrimaryAssignmentForPositionAndDateRange(posId, fromDate, toDate);
    }

    public List<Assignment> getAssignmentByPositionAndUserAsOnDate(final Long posId, final Long userId, final Date currDate) {
        return assignmentRepository.findByPositionAndEmployee(posId, userId, currDate);
    }

    public List<Assignment> findByDepartmentCodeAndDesignationCode(String departmentCode, List<String> desigCode) {
        return assignmentRepository.findByDepartmentCodeAndDesignationCode(departmentCode, desigCode, new Date());
    }

    public List<Designation> getDesignationsByActiveAssignmentAndDesignationNames(List<String> designationNames) {
        return assignmentRepository.findAllDesignationByActiveAssignmentAndDesignationNames(designationNames);
    }
}