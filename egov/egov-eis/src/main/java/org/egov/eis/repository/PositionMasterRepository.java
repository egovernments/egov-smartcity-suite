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

import org.egov.pims.commons.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Vaibhav.K
 */
@Repository
public interface PositionMasterRepository extends JpaRepository<Position, Long> {

    Position findByName(String name);

    List<Position> findByNameContainingIgnoreCase(String name);

    List<Position> findAllByDeptDesig_Id(Long id);

    @Query("select cr from Position cr ")
    List<Position> findPositionByAll();

    @Query("select cr from Position cr where cr.deptDesig.department.id=:departmentId and cr.deptDesig.designation.id=:designationId")
    List<Position> findPositionBydepartmentAndDesignation(@Param("departmentId") Long departmentId,
            @Param("designationId") Long designationId);

    @Query("select cr from Position cr where cr.deptDesig.department.id=:departmentId ")
    List<Position> findPositionBydepartment(@Param("departmentId") Long departmentId);

    @Query("select cr from Position cr where  cr.deptDesig.designation.id=:designationId")
    List<Position> findPositionByDesignation(@Param("designationId") Long designationId);
    
    List<Position> findByDeptDesig_Department_IdAndDeptDesig_Designation_IdAndNameContainingIgnoreCase(Long deptId,Long desigId,String name);

    @Query("select count(*)  from Position cr where cr.deptDesig.department.id=:departmentId and cr.deptDesig.designation.id=:designationId and cr.postOutsourced is true")
    Integer getTotalOutSourcedPostsByDepartmentAndDesignation(@Param("departmentId") Long departmentId,
            @Param("designationId") Long designationId);

    @Query("select count(*)  from Position cr where cr.deptDesig.department.id=:departmentId and cr.postOutsourced is true ")
    Integer getTotalOutSourcedPostsByDepartment(@Param("departmentId") Long departmentId);

    @Query("select count(*)  from Position cr where  cr.deptDesig.designation.id=:designationId and cr.postOutsourced is true")
    Integer getTotalOutSourcedPostsByDesignation(@Param("designationId") Long designationId);

    @Query("select count(*)  from Position cr where cr.postOutsourced is true")
    Integer getTotalOutSourcedPosts();

    @Query("select count(*)  from Position cr where cr.deptDesig.department.id=:departmentId and cr.deptDesig.designation.id=:designationId ")
    Integer getTotalSanctionedPostsByDepartmentAndDesignation(@Param("departmentId") Long departmentId,
            @Param("designationId") Long designationId);

    @Query("select count(*)  from Position cr where cr.deptDesig.department.id=:departmentId  ")
    Integer getTotalSanctionedPostsByDepartment(@Param("departmentId") Long departmentId);

    @Query("select count(*)  from Position cr where  cr.deptDesig.designation.id=:designationId ")
    Integer getTotalSanctionedPostsByDesignation(@Param("designationId") Long designationId);

    @Query("select count(*)  from Position cr ")
    Integer getTotalSanctionedPosts();

    @Query("select count(*)  from Position cr where cr.name like :name||'%' ")
    Integer getPositionSerialNumberByName(@Param("name") final String name);

}
