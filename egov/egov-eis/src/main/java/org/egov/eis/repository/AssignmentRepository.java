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
package org.egov.eis.repository;

import org.egov.eis.entity.Assignment;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.entity.User;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    @Query(" from Assignment A where A.employee.id =:empId order by A.fromDate")
    List<Assignment> getAllAssignmentsByEmpId(@Param("empId") Long empId);

    @Query(" from Assignment A where A.fromDate<=current_date and A.toDate>=current_date and A.employee.id =:empId order by A.fromDate")
    List<Assignment> getAllActiveAssignmentsByEmpId(@Param("empId") Long empId);

    @Query(" from Assignment A where A.fromDate<=:givenDate and A.toDate>=:givenDate and A.position.id=:posId order by A.fromDate")
    List<Assignment> getAssignmentsForPosition(@Param("posId") Long posId, @Param("givenDate") Date givenDate);

    @Query(" from Assignment A where A.fromDate<=current_date and A.toDate>=current_date and A.primary=true and A.position.id=:posId")
    Assignment getPrimaryAssignmentForPosition(@Param("posId") Long posId);

    @Query(" from Assignment A where A.fromDate<=current_date and A.toDate>=current_date and A.primary=true and A.employee.id=:userId ")
    Assignment getPrimaryAssignmentForUser(@Param("userId") Long userId);

    @Query(" from Assignment A where A.fromDate<=:givenDate and A.toDate>=:givenDate and A.primary=true and A.employee.id=:empId ")
    Assignment getAssignmentByEmpAndDate(@Param("empId") Long empId, @Param("givenDate") Date givenDate);

    @Query(" from Assignment A where A.fromDate<=current_date and A.toDate>=current_date and A.primary=true and A.employee.id=:empId")
    Assignment getPrimaryAssignmentForEmployee(@Param("empId") Long empId);

    @Query(" from Assignment A where A.position.id=:posId and A.fromDate<=current_date and A.toDate>=current_date order by A.fromDate")
    List<Assignment> getAssignmentsForPosition(@Param("posId") Long posId);

    @Query(" from Assignment A where A.position.id=:posId and A.fromDate<=:givenDate and A.toDate>=:givenDate and A.primary=true and A.employee.active=true")
    Assignment getPrimaryAssignmentForPositionAndDate(@Param("posId") Long posId,
                                                      @Param("givenDate") Date givenDate);

    @Query(" from Assignment A where A.employee.id=:empId and A.primary=true and A.fromDate<=:fromDate and A.toDate>=:toDate")
    Assignment getPrimaryAssignmentForGivenRange(@Param("empId") Long empId, @Param("fromDate") Date fromDate,
                                                 @Param("toDate") Date toDate);

    @Query(" from Assignment A where A.designation.id=:designationId  and A.department.id=:departmentId and A.primary=true and A.fromDate<=:givenDate and A.toDate>=:givenDate ")
    List<Assignment> getPrimaryAssignmentForDepartmentAndDesignation(@Param("departmentId") Long departmentId,
                                                                     @Param("designationId") Long designationId, @Param("givenDate") Date givenDate);

    @Query(" from Assignment A where A.designation.id=:designationId  and A.department.id=:departmentId and A.fromDate<=:givenDate and A.toDate>=:givenDate order by A.primary desc")
    List<Assignment> getAllAssignmentForDepartmentAndDesignation(@Param("departmentId") Long departmentId,
                                                                 @Param("designationId") Long designationId, @Param("givenDate") Date givenDate);

    @Query(" from Assignment A where A.department.id=:departmentId and A.primary=true and A.fromDate<=:givenDate and A.toDate>=:givenDate ")
    List<Assignment> getPrimaryAssignmentForDepartment(@Param("departmentId") Long departmentId,
                                                       @Param("givenDate") Date givenDate);

    @Query(" from Assignment A where A.department.id=:departmentId and A.fromDate<=:givenDate and A.toDate>=:givenDate order by A.primary desc")
    List<Assignment> getAllAssignmentForDepartment(@Param("departmentId") Long departmentId,
                                                   @Param("givenDate") Date givenDate);

    @Query(" from Assignment A where A.department.id=:departmentId and A.fromDate<=:givenDate and A.toDate>=:givenDate "
            + " and A.position.name like '%'||:posName||'%' order by A.primary desc")
    List<Assignment> getAllAssignmentForDepartmentAndPositionNameLike(@Param("departmentId") Long departmentId,
                                                                      @Param("givenDate") Date givenDate, @Param("posName") String posName);

    @Query(" from Assignment A where  A.fromDate<=:givenDate and A.toDate>=:givenDate "
            + " and A.position.name like '%'||:posName||'%' order by A.primary desc")
    List<Assignment> getAllAssignmentForPositionNameLike(@Param("givenDate") Date givenDate,
                                                         @Param("posName") String posName);

    @Query(" from Assignment A where A.designation.id=:designationId  and A.primary=true and A.fromDate<=:givenDate and A.toDate>=:givenDate ")
    List<Assignment> getPrimaryAssignmentForDesignation(@Param("designationId") Long designationId,
                                                        @Param("givenDate") Date givenDate);

    @Query(" from Assignment A where A.designation.id=:designationId and A.fromDate<=:givenDate and A.toDate>=:givenDate order by A.primary desc")
    List<Assignment> getAllAssignmentForDesignation(@Param("designationId") Long designationId,
                                                    @Param("givenDate") Date givenDate);

    @Query(" from Assignment A where A.department.id=:deptId and A.designation.id=:desigId and "
            + "((:fromDate between A.fromDate and A.toDate) or (:toDate between A.fromDate and A.toDate) or (A.fromDate<=:fromDate and A.toDate>=:toDate))"
            + " and A.primary=true")
    List<Assignment> findByDeptDesigAndDates(@Param("deptId") Long deptId, @Param("desigId") Long desigId,
                                             @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

    @Query(" from Assignment A where A.department.id=:deptId and A.designation.id=:desigId and "
            + " A.fromDate<=:givenDate and A.toDate>=:givenDate ")
    List<Assignment> findAllAssignmentsByDeptDesigAndGivenDate(@Param("deptId") Long deptId,
                                                               @Param("desigId") Long desigId, @Param("givenDate") Date givenDate);

    @Query(" from Assignment A where A.department.id=:deptId and A.fromDate<=:givenDate and A.toDate>=:givenDate order by A.fromDate ")
    List<Assignment> findAllByDepartmentAndDate(@Param("deptId") Long deptId,
                                                @Param("givenDate") Date givenDate);

    @Query(" from Assignment A where A.employee.id=:empId and A.fromDate<=:givenDate and A.toDate>=:givenDate order by A.fromDate desc")
    List<Assignment> findByEmployeeAndGivenDate(@Param("empId") Long empId, @Param("givenDate") Date givenDate);

    @Query(" from Assignment A where A.fromDate<=current_date and A.toDate>=current_date and A.designation.name=:name and A.primary=true")
    List<Assignment> findPrimaryAssignmentForDesignationName(@Param("name") String name);

    @Query(" select ASSIGN from Assignment ASSIGN inner join ASSIGN.employee as EMP inner join EMP.jurisdictions as JRDN "
            + " where ASSIGN.designation.id=:desigId and ASSIGN.fromDate<=current_date and ASSIGN.toDate>=current_date "
            + " and JRDN.boundary.id in :boundaryIds and ASSIGN.employee.active=true order by ASSIGN.primary desc")
    List<Assignment> findByDesignationAndBoundary(@Param("desigId") final Long desigId,
                                                  @Param("boundaryIds") final Set<Long> boundaryIds);

    @Query(" select ASSIGN from Assignment ASSIGN inner join ASSIGN.employee as EMP inner join EMP.jurisdictions as JRDN "
            + " where ASSIGN.department.id=:deptId and ASSIGN.designation.id=:desigId and ASSIGN.fromDate<=current_date and ASSIGN.toDate>=current_date "
            + " and JRDN.boundary.id in :boundaryIds and ASSIGN.employee.active=true order by ASSIGN.primary desc")
    List<Assignment> findByDepartmentDesignationAndBoundary(@Param("deptId") final Long deptId,
                                                            @Param("desigId") final Long desigId, @Param("boundaryIds") final Set<Long> boundaryIds);

    @Query(" select ASSIGN from Assignment ASSIGN inner join ASSIGN.employee as EMP inner join EMP.jurisdictions as JRDN "
            + " where ASSIGN.department.id=:deptId and ASSIGN.fromDate<=current_date and ASSIGN.toDate>=current_date "
            + " and JRDN.boundary.id in :boundaryIds and ASSIGN.employee.active=true order by ASSIGN.primary desc")
    List<Assignment> findByDepartmentAndBoundary(@Param("deptId") final Long deptId,
                                                 @Param("boundaryIds") final Set<Long> boundaryIds);

    @Query(" select ASSIGN from Assignment ASSIGN where ASSIGN.designation.id=:designationId and "
            + " ASSIGN.employee.active=true and ASSIGN.fromDate<=current_date and ASSIGN.toDate>=current_date order by ASSIGN.primary desc")
    List<Assignment> getAllActiveAssignments(@Param("designationId") final Long designationId);


    @Query("select assignment.employee from Assignment assignment where  assignment.employee.active=true and assignment.designation.name in (:designation)")
    Set<User> getUsersByDesignations(@Param("designation") final String[] designation);

    @Query(" select distinct A.designation.roles from  Assignment A where A.fromDate<=current_date and A.toDate<current_date and A.employee.id =:empId")
    Set<Role> getRolesForExpiredAssignmentsByEmpId(@Param("empId") Long empId);

    @Query(" select distinct A.designation.roles from  Assignment A where A.fromDate<=current_date and A.toDate>=current_date and A.employee.id =:empId")
    Set<Role> getRolesForActiveAssignmentsByEmpId(@Param("empId") Long empId);

    @Query(" select A from Assignment A where A.department.id=:deptId and A.designation.id in :desigIds and A.fromDate<=:givenDate and A.toDate>=:givenDate")
    List<Assignment> findByDepartmentDesignationsAndGivenDate(@Param("deptId") Long deptId,
                                                              @Param("desigIds") final List<Long> desigIds, @Param("givenDate") Date givenDate);

    @Query(" select distinct A.position from Assignment A where upper(A.position.name) like upper(:positionName) ")
    List<Position> findEmployeePositions(@Param("positionName") final String positionName);

    @Query(" select A from Assignment A, HeadOfDepartments hod where hod.hod.id=:deptId and A.id = hod.assignment.id  and "
            + " A.fromDate<=:givenDate and A.toDate>=:givenDate order by A.primary, A.toDate desc")
    List<Assignment> findAllAssignmentsByHODDeptAndGivenDate(@Param("deptId") Long deptId,
                                                             @Param("givenDate") Date givenDate);

    @Query(" from Assignment A where  A.primary=true and A.fromDate<=:givenDate and A.toDate>=:givenDate "
            + " and A.employee.name like '%'||:empName||'%' order by A.employee.name desc")
    List<Assignment> getAllAssignmentForEmployeeNameLike(@Param("givenDate") Date givenDate,
                                                         @Param("empName") String empName);

    List<Assignment> findByDesignationId(Long desigId);

    List<Assignment> findByDepartmentId(Long deptId);

    List<Assignment> findByDepartmentIdAndDesignationId(Long deptId, Long desigId);

    @Query(" from Assignment A where A.position.id=:posId and ((:fromDate between A.fromDate and A.toDate)"
            + " or (:toDate between A.fromDate and A.toDate) or"
            + " (A.fromDate>=:fromDate and A.toDate<=:toDate)) and A.primary=true")
    List<Assignment> getPrimaryAssignmentForPositionAndDateRange(@Param("posId") Long posId,
                                                                 @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

    @Query(" from Assignment A where A.position.id=:posId and A.employee.id = :userId and A.fromDate<=:givenDate and A.toDate>=:givenDate ")
    List<Assignment> findByPositionAndEmployee(@Param("posId") Long posId, @Param("userId") Long userId,
                                               @Param("givenDate") Date givenDate);

    @Query(" from Assignment A where A.designation.id in(select desig.id from Designation desig where desig.code in :designationCode)" +
            " and A.department.id in (select depart.id from Department depart where depart.code = :departmentCode) " +
            "and A.fromDate<=:givenDate and A.toDate>=:givenDate ")
    List<Assignment> findByDepartmentCodeAndDesignationCode(@Param("departmentCode") String departmentCode
            , @Param("designationCode") List<String> desigCode
            , @Param("givenDate") Date givenDate);

    @Query("select distinct A.designation from  Assignment A where A.fromDate<=current_date and A.toDate>=current_date and trim(upper(A.designation.name)) in(:designationNames)")
    List<Designation> findAllDesignationByActiveAssignmentAndDesignationNames(@Param("designationNames") List<String> designationNames);
}