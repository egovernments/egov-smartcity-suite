/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 */

package org.egov.pgr.repository;

import java.util.List;

import org.egov.infra.admin.master.entity.Boundary;
import org.egov.pgr.entity.ComplaintRouter;
import org.egov.pgr.entity.ComplaintType;
import org.egov.pims.commons.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ComplaintRouterRepository extends JpaRepository<ComplaintRouter, Long> {

    public ComplaintRouter findByComplaintTypeAndBoundary(ComplaintType complaintType, Boundary location);

    @Query("select cr from ComplaintRouter cr where cr.complaintType=:complaintType and cr.boundary is null")
    public ComplaintRouter findByOnlyComplaintType(@Param("complaintType") ComplaintType complaintType);

    public ComplaintRouter findByBoundary(Boundary location);

    @Query("select cr from ComplaintRouter cr where cr.boundary.parent is null")
    public ComplaintRouter findGrievanceOfficer();

    @Query("select cr from ComplaintRouter cr where cr.complaintType=:complaintType and cr.position=:position and cr.boundary=:boundary")
    public ComplaintRouter findByComplaintTypeAndBoundaryAndPosition(
            @Param("complaintType") ComplaintType complaintType, @Param("boundary") Boundary boundary,
            @Param("position") Position position);

    @Query("select cr from ComplaintRouter cr where cr.complaintType=:complaintType and cr.position=:position and cr.boundary is null")
    public ComplaintRouter findByComplaintTypeAndPosition(@Param("complaintType") ComplaintType complaintType,
            @Param("position") Position position);

    @Query("select cr from ComplaintRouter cr where cr.boundary=:boundary and cr.position=:position and cr.complaintType is null")
    public ComplaintRouter findByBoundaryAndPosition(@Param("boundary") Boundary boundary,
            @Param("position") Position position);

    @Query("select cr from ComplaintRouter cr where cr.boundary.boundaryType.id=:boundaryTypeId ")
    List<ComplaintRouter> findRoutersByBoundaryType(@Param("boundaryTypeId") Long boundaryTypeId);

    @Query("select cr from ComplaintRouter cr ")
    List<ComplaintRouter> findRoutersByAll();

    @Query("select cr from ComplaintRouter cr where cr.boundary.boundaryType.id=:boundaryTypeId and cr.boundary.id=:boundaryId")
    List<ComplaintRouter> findRoutersByBoundaryAndBoundaryType(@Param("boundaryTypeId") Long boundaryTypeId,
            @Param("boundaryId") Long boundaryId);

    @Query("select cr from ComplaintRouter cr where cr.complaintType.id=:complaintTypeId ")
    List<ComplaintRouter> findRoutersByComplaintType(@Param("complaintTypeId") Long complaintTypeId);

    @Query("select cr from ComplaintRouter cr where cr.complaintType.id=:complaintTypeId and cr.boundary.boundaryType.id=:boundaryTypeId")
    List<ComplaintRouter> findRoutersByComplaintTypeAndBoundaryType(@Param("complaintTypeId") Long complaintTypeId,
            @Param("boundaryTypeId") Long boundaryTypeId);

    @Query("select cr from ComplaintRouter cr where cr.complaintType.id=:complaintTypeId and cr.boundary.boundaryType.id=:boundaryTypeId and cr.boundary.id=:boundaryId")
    List<ComplaintRouter> findRoutersByComplaintTypeAndBoundaryTypeAndBoundary(
            @Param("complaintTypeId") Long complaintTypeId, @Param("boundaryTypeId") Long boundaryTypeId,
            @Param("boundaryId") Long boundaryId);

    @Query("select cr from ComplaintRouter cr where cr.position.id=:positionId and cr.complaintType.id=:complaintTypeId and cr.boundary.boundaryType.id=:boundaryTypeId and cr.boundary.id=:boundaryId")
    List<ComplaintRouter> findRoutersByComplaintTypeAndBoundaryTypeAndBoundaryAndPosition(
            @Param("complaintTypeId") Long complaintTypeId, @Param("boundaryTypeId") Long boundaryTypeId,
            @Param("boundaryId") Long boundaryId, @Param("positionId") Long positionId);

    @Query("select cr from ComplaintRouter cr where cr.position.id=:positionId and cr.complaintType.id=:complaintTypeId and cr.boundary.boundaryType.id=:boundaryTypeId ")
    public List<ComplaintRouter> findRoutersByComplaintTypeAndBoundaryTypeAndPosition(
            @Param("complaintTypeId") Long complaintTypeId, @Param("boundaryTypeId") Long boundaryTypeId,
            @Param("positionId") Long positionId);

    @Query("select cr from ComplaintRouter cr where cr.position.id=:positionId and cr.complaintType.id=:complaintTypeId ")
    public List<ComplaintRouter> findRoutersByComplaintTypeAndPosition(@Param("complaintTypeId") Long complaintTypeId,
            @Param("positionId") Long positionId);

    @Query("select cr from ComplaintRouter cr where cr.position.id=:positionId and cr.complaintType.id=:complaintTypeId  and cr.boundary.id=:boundaryId")
    public List<ComplaintRouter> findRoutersByComplaintTypeAndBoundaryAndPosition(
            @Param("complaintTypeId") Long complaintTypeId, @Param("boundaryId") Long boundaryId,
            @Param("positionId") Long positionId);

    @Query("select cr from ComplaintRouter cr where  cr.complaintType.id=:complaintTypeId and  cr.boundary.id=:boundaryId")
    public List<ComplaintRouter> findRoutersByComplaintTypeAndBoundary(@Param("complaintTypeId") Long complaintTypeId,
            @Param("boundaryId") Long boundaryId);

    @Query("select cr from ComplaintRouter cr where  cr.complaintType.id=:complaintTypeId and cr.boundary.boundaryType.id=:boundaryTypeId ")
    public List<ComplaintRouter> findRoutersByComplaintTypeAndBoundaryTypecomplaintTypeId(
            @Param("complaintTypeId") Long complaintTypeId, @Param("boundaryTypeId") Long boundaryTypeId);

    @Query("select cr from ComplaintRouter cr where cr.position.id=:positionId and cr.boundary.boundaryType.id=:boundaryTypeId and cr.boundary.id=:boundaryId")
    public List<ComplaintRouter> findRoutersByBoundaryTypeAndBoundaryAndPosition(
            @Param("boundaryTypeId") Long boundaryTypeId, @Param("boundaryId") Long boundaryId,
            @Param("positionId") Long positionId);

    @Query("select cr from ComplaintRouter cr where cr.position.id=:positionId and cr.boundary.boundaryType.id=:boundaryTypeId ")
    public List<ComplaintRouter> findRoutersByBoundaryTypeAndPosition(@Param("boundaryTypeId") Long boundaryTypeId,
            @Param("positionId") Long positionId);

    @Query("select cr from ComplaintRouter cr where cr.position.id=:positionId ")
    public List<ComplaintRouter> findRoutersByPosition(@Param("positionId") Long positionId);

    @Query("select cr from ComplaintRouter cr where cr.position.id=:positionId and cr.boundary.id=:boundaryId")
    public List<ComplaintRouter> findRoutersByBoundaryAndPosition(@Param("boundaryId") Long boundaryId,
            @Param("positionId") Long positionId);

    @Query("select cr from ComplaintRouter cr where  cr.boundary.id=:boundaryId")
    public List<ComplaintRouter> findRoutersByBoundary(@Param("boundaryId") Long boundaryId);

    @Query("select cr from ComplaintRouter cr where cr.boundary.boundaryType.id=:boundaryTypeId and cr.boundary.id=:boundaryId")
    public List<ComplaintRouter> findRoutersByBoundaryTypeAndBoundary(@Param("boundaryTypeId") Long boundaryTypeId,
            @Param("boundaryId") Long boundaryId);

    @Query("select cr from ComplaintRouter cr where cr.boundary=:bndry and cr.complaintType is null")
    public ComplaintRouter findByOnlyBoundary(@Param("bndry") Boundary bndry);

    @Query("select cr from ComplaintRouter cr where cr.boundary.parent is null and  cr.complaintType is null and cr.boundary.boundaryType.hierarchyType.name=:hierarchyType")
    public ComplaintRouter findCityAdminGrievanceOfficer(@Param("hierarchyType") String hierarchyType);

    @Query("select cr from ComplaintRouter cr where cr.complaintType in :complaintTypes and cr.boundary in :boundaries")
    public List<ComplaintRouter> findRoutersByComplaintTypesBoundaries(
            @Param("complaintTypes") List<ComplaintType> complaintTypes, @Param("boundaries") List<Boundary> boundaries);

}