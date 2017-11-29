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

package org.egov.pgr.repository;

import org.egov.infra.admin.master.entity.Boundary;
import org.egov.pgr.entity.ComplaintRouter;
import org.egov.pgr.entity.ComplaintType;
import org.egov.pims.commons.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintRouterRepository extends JpaRepository<ComplaintRouter, Long>, JpaSpecificationExecutor<ComplaintRouter> {

    ComplaintRouter findByComplaintTypeAndBoundary(ComplaintType complaintType, Boundary location);

    @Query("select cr from ComplaintRouter cr where cr.complaintType=:complaintType and cr.boundary is null")
    ComplaintRouter findByOnlyComplaintType(@Param("complaintType") ComplaintType complaintType);

    ComplaintRouter findByBoundary(Boundary location);

    @Query("select cr from ComplaintRouter cr where cr.boundary.parent is null")
    ComplaintRouter findGrievanceOfficer();

    @Query("select cr from ComplaintRouter cr where cr.complaintType=:complaintType and cr.position=:position and cr.boundary=:boundary")
    ComplaintRouter findByComplaintTypeAndBoundaryAndPosition(
            @Param("complaintType") ComplaintType complaintType, @Param("boundary") Boundary boundary,
            @Param("position") Position position);

    @Query("select cr from ComplaintRouter cr where cr.complaintType=:complaintType and cr.position=:position and cr.boundary is null")
    ComplaintRouter findByComplaintTypeAndPosition(@Param("complaintType") ComplaintType complaintType,
                                                   @Param("position") Position position);

    @Query("select cr from ComplaintRouter cr where cr.boundary=:boundary and cr.position=:position and cr.complaintType is null")
    ComplaintRouter findByBoundaryAndPosition(@Param("boundary") Boundary boundary,
                                              @Param("position") Position position);

    @Query("select cr from ComplaintRouter cr where cr.position.id=:positionId and cr.complaintType.id=:complaintTypeId and cr.boundary.boundaryType.id=:boundaryTypeId and cr.boundary.id=:boundaryId")
    List<ComplaintRouter> findRoutersByComplaintTypeAndBoundaryTypeAndBoundaryAndPosition(
            @Param("complaintTypeId") Long complaintTypeId, @Param("boundaryTypeId") Long boundaryTypeId,
            @Param("boundaryId") Long boundaryId, @Param("positionId") Long positionId);

    @Query("select cr from ComplaintRouter cr where cr.position.id=:positionId and cr.complaintType.id=:complaintTypeId and cr.boundary.boundaryType.id=:boundaryTypeId ")
    List<ComplaintRouter> findRoutersByComplaintTypeAndBoundaryTypeAndPosition(
            @Param("complaintTypeId") Long complaintTypeId, @Param("boundaryTypeId") Long boundaryTypeId,
            @Param("positionId") Long positionId);

    @Query("select cr from ComplaintRouter cr where cr.position.id=:positionId and cr.complaintType.id=:complaintTypeId ")
    List<ComplaintRouter> findRoutersByComplaintTypeAndPosition(@Param("complaintTypeId") Long complaintTypeId,
                                                                @Param("positionId") Long positionId);

    @Query("select cr from ComplaintRouter cr where cr.position.id=:positionId and cr.complaintType.id=:complaintTypeId  and cr.boundary.id=:boundaryId")
    List<ComplaintRouter> findRoutersByComplaintTypeAndBoundaryAndPosition(
            @Param("complaintTypeId") Long complaintTypeId, @Param("boundaryId") Long boundaryId,
            @Param("positionId") Long positionId);

    @Query("select cr from ComplaintRouter cr where  cr.complaintType.id=:complaintTypeId and  cr.boundary.id=:boundaryId")
    List<ComplaintRouter> findRoutersByComplaintTypeAndBoundary(@Param("complaintTypeId") Long complaintTypeId,
                                                                @Param("boundaryId") Long boundaryId);

    @Query("select cr from ComplaintRouter cr where  cr.complaintType.id=:complaintTypeId and cr.boundary.boundaryType.id=:boundaryTypeId ")
    List<ComplaintRouter> findRoutersByComplaintTypeAndBoundaryTypecomplaintTypeId(
            @Param("complaintTypeId") Long complaintTypeId, @Param("boundaryTypeId") Long boundaryTypeId);

    @Query("select cr from ComplaintRouter cr where cr.position.id=:positionId and cr.boundary.boundaryType.id=:boundaryTypeId and cr.boundary.id=:boundaryId")
    List<ComplaintRouter> findRoutersByBoundaryTypeAndBoundaryAndPosition(
            @Param("boundaryTypeId") Long boundaryTypeId, @Param("boundaryId") Long boundaryId,
            @Param("positionId") Long positionId);

    @Query("select cr from ComplaintRouter cr where cr.position.id=:positionId and cr.boundary.boundaryType.id=:boundaryTypeId ")
    List<ComplaintRouter> findRoutersByBoundaryTypeAndPosition(@Param("boundaryTypeId") Long boundaryTypeId,
                                                               @Param("positionId") Long positionId);

    @Query("select cr from ComplaintRouter cr where cr.position.id=:positionId ")
    List<ComplaintRouter> findRoutersByPosition(@Param("positionId") Long positionId);

    @Query("select cr from ComplaintRouter cr where cr.position.id=:positionId and cr.boundary.id=:boundaryId")
    List<ComplaintRouter> findRoutersByBoundaryAndPosition(@Param("boundaryId") Long boundaryId,
                                                           @Param("positionId") Long positionId);

    @Query("select cr from ComplaintRouter cr where  cr.boundary.id=:boundaryId")
    List<ComplaintRouter> findRoutersByBoundary(@Param("boundaryId") Long boundaryId);

    @Query("select cr from ComplaintRouter cr where cr.boundary.boundaryType.id=:boundaryTypeId and cr.boundary.id=:boundaryId")
    List<ComplaintRouter> findRoutersByBoundaryTypeAndBoundary(@Param("boundaryTypeId") Long boundaryTypeId,
                                                               @Param("boundaryId") Long boundaryId);

    @Query("select cr from ComplaintRouter cr where cr.boundary=:bndry and cr.complaintType is null")
    ComplaintRouter findByOnlyBoundary(@Param("bndry") Boundary bndry);

    @Query("select cr from ComplaintRouter cr where cr.boundary.parent is null and  cr.complaintType is null and cr.boundary.boundaryType.hierarchyType.name=:hierarchyType")
    ComplaintRouter findCityAdminGrievanceOfficer(@Param("hierarchyType") String hierarchyType);

    @Query("select cr from ComplaintRouter cr where cr.complaintType in :complaintTypes and cr.boundary in :boundaries")
    List<ComplaintRouter> findRoutersByComplaintTypesBoundaries(
            @Param("complaintTypes") List<ComplaintType> complaintTypes, @Param("boundaries") List<Boundary> boundaries);

}