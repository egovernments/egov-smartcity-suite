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

package org.egov.infra.admin.master.repository;

import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.entity.HierarchyType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.hibernate.jpa.QueryHints.HINT_CACHEABLE;

@Repository
public interface BoundaryRepository extends JpaRepository<Boundary, Long> {

    @QueryHints({@QueryHint(name = HINT_CACHEABLE, value = "true")})
    Boundary findByName(String name);

    List<Boundary> findByNameContainingIgnoreCase(String name);

    List<Boundary> findByBoundaryTypeId(Long boundaryType);

    Page<Boundary> findByBoundaryTypeId(Long boundaryType, Pageable pageable);

    Boundary findByBoundaryTypeAndBoundaryNum(BoundaryType boundaryType, Long boundaryNum);

    Boundary findByIdAndBoundaryType(Long boundaryId, BoundaryType boundaryType);

    @Query("select b from Boundary b where b.active=true AND b.boundaryType.id =:boundaryTypeId order by b.name")
    List<Boundary> findActiveBoundariesByBoundaryTypeId(@Param("boundaryTypeId") Long boundaryTypeId);

    @Query("select b from Boundary b where b.active=true AND b.boundaryType.hierarchyType = :hierarchyType " +
            "AND b.boundaryType.hierarchy = :hierarchyLevel AND ((b.toDate IS NULL AND b.fromDate <= :asOnDate) " +
            "OR (b.toDate IS NOT NULL AND b.fromDate <= :asOnDate AND b.toDate >= :asOnDate)) order by b.name")
    List<Boundary> findActiveBoundariesByHierarchyTypeAndLevelAndAsOnDate(
            @Param("hierarchyType") HierarchyType hierarchyType, @Param("hierarchyLevel") Long hierarchyLevel,
            @Param("asOnDate") Date asOnDate);

    @Query("select b from Boundary b where b.active=true AND b.parent is not null AND b.parent.id = :parentBoundaryId " +
            "AND ((b.toDate IS NULL AND b.fromDate <= :asOnDate) OR (b.toDate IS NOT NULL AND b.fromDate <= :asOnDate " +
            "AND b.toDate >= :asOnDate)) order by b.name")
    List<Boundary> findActiveChildBoundariesByBoundaryIdAndAsOnDate(@Param("parentBoundaryId") Long parentBoundaryId,
                                                                    @Param("asOnDate") Date asOnDate);

    @Query("from Boundary BND where BND.active=true AND BND.materializedPath like (select B.materializedPath " +
            "from Boundary B where B.id=:parentId)||'%'")
    List<Boundary> findActiveChildrenWithParent(@Param("parentId") Long parentId);

    @Query("from Boundary BND where BND.active=true AND BND.materializedPath in :mpath ")
    List<Boundary> findActiveBoundariesForMpath(@Param("mpath") final Set<String> mpath);

    @Query("select b from Boundary b where b.parent is not null AND b.parent.id = :parentBoundaryId " +
            "AND ((b.toDate IS NULL AND b.fromDate <= :asOnDate) OR (b.toDate IS NOT NULL AND b.fromDate <= :asOnDate " +
            "AND b.toDate >= :asOnDate)) order by b.name")
    List<Boundary> findChildBoundariesByBoundaryIdAndAsOnDate(@Param("parentBoundaryId") Long parentBoundaryId,
                                                              @Param("asOnDate") Date asOnDate);

    @Query("select b from Boundary b where b.active=true AND b.boundaryNum = :boundaryNum " +
            "AND b.boundaryType.name = :boundaryType AND upper(b.boundaryType.hierarchyType.code) = :hierarchyTypeCode " +
            "AND ((b.toDate IS NULL AND b.fromDate <= :asOnDate) OR (b.toDate IS NOT NULL AND b.fromDate <= :asOnDate " +
            "AND b.toDate >= :asOnDate))")
    Boundary findActiveBoundaryByBndryNumAndTypeAndHierarchyTypeCodeAndAsOnDate(@Param("boundaryNum") Long boundaryNum,
                                                                                @Param("boundaryType") String boundaryType,
                                                                                @Param("hierarchyTypeCode") String hierarchyTypeCode,
                                                                                @Param("asOnDate") Date asOnDate);

    @Query("select b from Boundary b where b.active=true AND upper(b.boundaryType.name) = upper(:boundaryTypeName) " +
            "AND upper(b.boundaryType.hierarchyType.name) = upper(:hierarchyTypeName) order by b.name")
    List<Boundary> findActiveBoundariesByBndryTypeNameAndHierarchyTypeName(
            @Param("boundaryTypeName") String boundaryTypeName, @Param("hierarchyTypeName") String hierarchyTypeName);

    @Query("select b from Boundary b where upper(b.boundaryType.name) = UPPER(:boundaryTypeName) " +
            "AND upper(b.boundaryType.hierarchyType.name) = UPPER(:hierarchyTypeName) order by b.id")
    List<Boundary> findBoundariesByBndryTypeNameAndHierarchyTypeName(@Param("boundaryTypeName") String boundaryTypeName,
                                                                     @Param("hierarchyTypeName") String hierarchyTypeName);

    @Query("select b from Boundary b where upper(b.boundaryType.name) = UPPER(:boundaryTypeName) " +
            "AND upper(b.boundaryType.hierarchyType.name) = UPPER(:hierarchyTypeName) order by b.id")
    Boundary findBoundaryByBndryTypeNameAndHierarchyTypeName(@Param("boundaryTypeName") String boundaryTypeName,
                                                             @Param("hierarchyTypeName") String hierarchyTypeName);

    @Query("select b from Boundary b where b.active=true and UPPER(b.name) like UPPER(:boundaryName) " +
            "and b.boundaryType.id=:boundaryTypeId order by b.boundaryNum asc")
    List<Boundary> findByNameAndBoundaryTypeOrderByBoundaryNumAsc(@Param("boundaryName") String boundaryName,
                                                                  @Param("boundaryTypeId") Long boundaryTypeId);

    @Query("select b from Boundary b where b.boundaryType.name=:boundaryType and b.boundaryType.hierarchyType.name=:hierarchyType " +
            "and b.boundaryType.hierarchy=:hierarchyLevel")
    Boundary findByBoundaryTypeNameAndHierarchyTypeNameAndLevel(@Param("boundaryType") String boundaryType,
                                                                @Param("hierarchyType") String hierarchyType,
                                                                @Param("hierarchyLevel") Long hierarchyLevel);

    @Query("select b from Boundary b where b.active=true AND upper(b.boundaryType.name) = upper(:boundaryTypeName) " +
            "AND upper(b.boundaryType.hierarchyType.name) = upper(:hierarchyTypeName) AND UPPER(b.name) " +
            "like UPPER(:name)||'%' order by b.id")
    List<Boundary> findActiveBoundariesByNameAndBndryTypeNameAndHierarchyTypeName(
            @Param("boundaryTypeName") String boundaryTypeName, @Param("hierarchyTypeName") String hierarchyTypeName,
            @Param("name") String name);

    @Query("from Boundary BND where BND.active=true AND BND.parent.id=:parentId)")
    List<Boundary> findActiveImmediateChildrenWithOutParent(@Param("parentId") Long parentId);

    @Query("from Boundary BND where BND.parent is null")
    List<Boundary> findAllParents();

    List<Boundary> findByBoundaryTypeOrderByBoundaryNumAsc(BoundaryType boundaryType);
}