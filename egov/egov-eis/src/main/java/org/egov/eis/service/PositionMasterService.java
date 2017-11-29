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
import org.egov.eis.repository.PositionHierarchyRepository;
import org.egov.eis.repository.PositionMasterRepository;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.persistence.utils.PersistenceUtils;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Vaibhav.K
 */
@Service
@Transactional(readOnly = true)
public class PositionMasterService {

    private final PositionMasterRepository positionMasterRepository;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private PositionHierarchyRepository positionHierarchyRepository;

    @Autowired
    public PositionMasterService(final PositionMasterRepository positionMasterRepository) {
        this.positionMasterRepository = positionMasterRepository;
    }

    @Transactional
    public void createPosition(final Position position) {
        positionMasterRepository.save(position);
    }

    @Transactional
    public void updatePosition(final Position position) {
        positionMasterRepository.save(position);
    }

    @Transactional
    public void deletePosition(final Position position) {
        positionMasterRepository.delete(position);
    }

    public Position getPositionByName(final String name) {
        return positionMasterRepository.findByName(name);
    }

    public Position getPositionById(final Long posId) {
        return positionMasterRepository.findOne(posId);
    }

    public List<Position> getAllPositions() {
        return positionMasterRepository.findAll();
    }

    public List<Position> getAllPositionsByNameLike(final String name) {
        return positionMasterRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Position> getAllPositionsByDeptDesigId(final Long deptDesigId) {
        return positionMasterRepository.findAllByDeptDesig_Id(deptDesigId);
    }

    public List<Position> getPositionsForDeptDesigAndName(final Long departmentId, final Long designationId,
                                                          final Date fromDate, final Date toDate, final String posName) {
        List<Position> posList;
        final List<Assignment> assignList = assignmentService.getAssignmentsByDeptDesigAndDates(departmentId,
                designationId, fromDate, toDate);
        posList = positionMasterRepository
                .findByDeptDesig_Department_IdAndDeptDesig_Designation_IdAndNameContainingIgnoreCase(departmentId,
                        designationId, posName);
        for (final Assignment assign : assignList)
            posList.removeIf(m -> m.getId() == assign.getPosition().getId());
        return posList;
    }

    public List<Position> getPositionsForDeptDesigAndNameLike(final Long departmentId, final Long designationId,
                                                              final String posName) {
        return positionMasterRepository
                .findByDeptDesig_Department_IdAndDeptDesig_Designation_IdAndNameContainingIgnoreCase(departmentId,
                        designationId, posName);
    }

    public boolean validatePosition(final Position position) {
        if (position != null && position.getName() != null) {
            final List<Position> positionList = positionMasterRepository
                    .findByNameContainingIgnoreCase(position.getName());
            if (!positionList.isEmpty())
                return false;
        }
        return true;
    }

    public List<Position> getPageOfPositions(final Long departmentId, final Long designationId) {

        if (departmentId != 0 && designationId != 0)
            return positionMasterRepository.findPositionBydepartmentAndDesignation(departmentId, designationId);
        else if (departmentId != 0)
            return positionMasterRepository.findPositionBydepartment(departmentId);
        else if (designationId != 0)
            return positionMasterRepository.findPositionByDesignation(designationId);
        else
            return positionMasterRepository.findPositionByAll();
    }

    public List<Position> findByNameContainingIgnoreCase(final String positionName) {
        return positionMasterRepository.findByNameContainingIgnoreCase(positionName);
    }

    public Integer getTotalOutSourcedPosts(final Long departmentId, final Long designationId) {

        if (departmentId != 0 && designationId != 0)
            return positionMasterRepository.getTotalOutSourcedPostsByDepartmentAndDesignation(departmentId,
                    designationId);
        else if (designationId != 0)
            return positionMasterRepository.getTotalOutSourcedPostsByDesignation(designationId);
        else if (departmentId != 0)
            return positionMasterRepository.getTotalOutSourcedPostsByDepartment(departmentId);
        else
            return positionMasterRepository.getTotalOutSourcedPosts();

    }

    public Integer getTotalSanctionedPosts(final Long departmentId, final Long designationId) {

        if (departmentId != 0 && designationId != 0)
            return positionMasterRepository.getTotalSanctionedPostsByDepartmentAndDesignation(departmentId,
                    designationId);
        else if (designationId != 0)
            return positionMasterRepository.getTotalSanctionedPostsByDesignation(designationId);
        else if (departmentId != 0)
            return positionMasterRepository.getTotalSanctionedPostsByDepartment(departmentId);
        else
            return positionMasterRepository.getTotalSanctionedPosts();

    }

    public List<Position> getPositionsByDepartmentAndDesignationForGivenRange(final Long departmentId,
                                                                              final Long designationId, final Date givenDate) {

        final List<Position> positionList = new ArrayList<>();

        final List<Assignment> assignmentList = assignmentService
                .getPositionsByDepartmentAndDesignationForGivenRange(departmentId, designationId, givenDate);

        for (final Assignment assignmentObj : assignmentList) {
            //Unproxing position due lazy initialization
            positionList.add(PersistenceUtils.unproxy(assignmentObj.getPosition()));
        }

        return positionList;
    }

    public List<Position> getPositionsByDepartmentAndDesignationId(Long departmentId, Long designationId) {
        return getPositionsByDepartmentAndDesignationForGivenRange(departmentId, designationId, new Date());
    }

    public Position getCurrentPositionForUser(final Long userId) {
        final Assignment assign = assignmentService.getPrimaryAssignmentForEmployee(userId);
        if (assign != null)
            return assign.getPosition();
        return null;
    }

    /**
     * Returns the superior employee position
     *
     * @param objectId
     * @param posId
     * @return returns position object
     */
    public Position getSuperiorPositionByObjectTypeAndPositionFrom(final Integer objectId, final Long posId) {
        return positionHierarchyRepository.getPositionHierarchyByPosAndObjectType(posId, objectId).getToPosition();
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
        return positionHierarchyRepository.getPosHirByPosAndObjectTypeAndObjectSubType(posId, objectId, objectSubType)
                .getToPosition();
    }

    /**
     * Return list of positions of an employee for employee id and given date, if null is passed to given date then it is replaced
     * with current date
     *
     * @param employeeId
     * @param forDate
     * @return list of position object
     */
    public List<Position> getPositionsForEmployee(final Long employeeId, final Date forDate) {
        final Date userGivenDate = null == forDate ? new Date() : forDate;
        final Set<Position> positions = new HashSet<Position>();
        final List<Assignment> assignments = assignmentService.findByEmployeeAndGivenDate(employeeId, userGivenDate);
        for (final Assignment assign : assignments)
            positions.add(assign.getPosition());
        return new ArrayList<Position>(positions);
    }

    /**
     * Returns list of positions for an employee irrespective of assignment date
     *
     * @param empId
     * @return List of position objects
     */
    public List<Position> getPositionsForEmployee(final Long employeeId) {
        final Set<Position> positions = new HashSet<Position>();
        final List<Assignment> assignList = assignmentService.getAllAssignmentsByEmpId(employeeId);
        for (final Assignment assign : assignList)
            positions.add(assign.getPosition());
        return new ArrayList<>(positions);
    }

    public List<Position> getCurrentUserPositions() {
        return getPositionsForEmployee(ApplicationThreadLocals.getUserId());
    }

    /**
     * Returns employee position for user
     *
     * @param userId
     * @return Position object
     */
    public Position getPositionByUserId(final Long userId) {
        Assignment assignment;
        assignment = assignmentService.getPrimaryAssignmentForUser(userId);
        if (assignment != null)
            return assignment.getPosition();
        return null;
    }

    /**
     * Returns primary assignment position for employee id
     *
     * @param empId
     * @return Position object
     */
    public Position getPrimaryAssignmentPositionForEmp(final Long empId) {
        return assignmentService.getPrimaryAssignmentForEmployee(empId).getPosition();
    }

    public String generatePositionByDeptDesig(final Department department, final Designation designation) {

        String name = department.getCode() + "_" + designation.getCode() + "_";
        name += positionMasterRepository.getPositionSerialNumberByName(name) + 1;
        return name;
    }

    public List<Position> getPositionsByDepartment(final Long deptId) {
        final List<Position> positionList = new ArrayList<>();

        final List<Assignment> assignmentList = assignmentService.findAssignmentForDepartmentId(deptId);

        for (final Assignment assignmentObj : assignmentList)
            positionList.add(assignmentObj.getPosition());

        return positionList;
    }

    public List<Position> getPositionsByDesignation(final Long desigId) {
        final List<Position> positionList = new ArrayList<>();

        final List<Assignment> assignmentList = assignmentService.findAssignmentForDesignationId(desigId);

        for (final Assignment assignmentObj : assignmentList)
            positionList.add(assignmentObj.getPosition());

        return positionList;
    }

    public List<Position> getPositionsByDepartmentAndDesignation(final Long deptId, final Long desigId) {
        final List<Position> positionList = new ArrayList<>();

        final List<Assignment> assignmentList = assignmentService.findByDepartmentAndDesignation(deptId, desigId);

        for (final Assignment assignmentObj : assignmentList)
            positionList.add(assignmentObj.getPosition());

        return positionList;
    }

}