/**
 * eGov suite of products aim to improve the internal efficiency,transparency, accountability and the service delivery of the
 * government organizations.
 * 
 * Copyright (C) <2015> eGovernments Foundation
 * 
 * The updated version of eGov suite of products as by eGovernments Foundation is available at http://www.egovernments.org
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 * http://www.gnu.org/licenses/ or http://www.gnu.org/licenses/gpl.html .
 * 
 * In addition to the terms of the GPL license to be adhered to in using this program, the following additional terms are to be
 * complied with:
 * 
 * 1) All versions of this program, verbatim or modified must carry this Legal Notice.
 * 
 * 2) Any misrepresentation of the origin of the material is prohibited. It is required that all modified versions of this
 * material be marked in reasonable ways as different from the original version.
 * 
 * 3) This license does not grant any rights to any user of the program with regards to rights under trademark law for use of the
 * trade names or trademarks of eGovernments Foundation.
 * 
 * In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.infra.admin.master.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.entity.HierarchyType;
import org.egov.infra.admin.master.repository.BoundaryRepository;
import org.egov.infra.exception.NoSuchObjectException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BoundaryService {

    private final BoundaryRepository boundaryRepository;

    @Autowired
    private CrossHierarchyService crossHierarchyService;

    @Autowired
    public BoundaryService(final BoundaryRepository boundaryRepository) {
        this.boundaryRepository = boundaryRepository;
    }

    @Transactional
    public Boundary createBoundary(final Boundary boundary) {
        return boundaryRepository.save(boundary);
    }

    @Transactional
    public void updateBoundary(final Boundary boundary) {
        boundaryRepository.save(boundary);
    }

    public Boundary getBoundaryById(final Long id) {
        return boundaryRepository.findOne(id);
    }

    public Boundary getBoundaryByName(final String name) {
        return boundaryRepository.findByName(name);
    }

    public List<Boundary> getAllBoundaries() {
        return boundaryRepository.findAll();
    }

    public List<Boundary> getBoundaryByNameLike(final String name) {
        return boundaryRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Boundary> getAllBoundariesByBoundaryTypeId(final Long boundaryTypeId) {
        return boundaryRepository.findBoundariesByBoundaryType(boundaryTypeId);
    }

    public List<Boundary> getPageOfBoundaries(final Long boundaryTypeId) {
        return boundaryRepository.findBoundariesByBoundaryType(boundaryTypeId);
    }

    public Boundary getBoundaryByTypeAndNo(final BoundaryType boundaryType, final Long boundaryNum) {
        return boundaryRepository.findBoundarieByBoundaryTypeAndBoundaryNum(boundaryType, boundaryNum);
    }

    // TODO - Later - Use materializedPath instead of recursive calling
    public List<Boundary> getParentBoundariesByBoundaryId(final Long boundaryId) throws NoSuchObjectException {
        List<Boundary> boundaryList = new ArrayList<Boundary>();
        final Boundary bndry = getBoundaryById(boundaryId);
        if (bndry != null) {
            boundaryList.add(bndry);
            if (bndry.getParent() != null)
                boundaryList = getParentBoundariesByBoundaryId(bndry.getParent().getId());
        } else
            throw new NoSuchObjectException("bndry.Obj.null");
        return boundaryList;
    }

    public Map<String, List<Boundary>> getSecondLevelBoundaryByHierarchyType(final HierarchyType hierarchyType) {
        List<Boundary> boundaryList = new ArrayList<Boundary>();
        final Map<String, List<Boundary>> boundaryMap = new HashMap<String, List<Boundary>>();

        boundaryList = boundaryRepository.findActiveBoundariesByHierarchyTypeAndLevelAndAsOnDate(hierarchyType, 2l,
                new Date());
        String bryName = null;
        for (final Boundary boundary : boundaryList)
            bryName = boundary.getBoundaryType().getName();
        if (bryName != null)
            boundaryMap.put(bryName, boundaryList);

        return boundaryMap;
    }

    public Boundary getActiveBoundaryByIdAsOnCurrentDate(final Long id) {
        return boundaryRepository.findActiveBoundaryByIdAndAsOnDate(id, new Date());
    }

    public Boundary getActiveBoundaryById(final Long id) {
        return boundaryRepository.findActiveBoundaryById(id);
    }

    public List<Boundary> getActiveBoundariesByBoundaryTypeId(final Long boundaryTypeId) {
        return boundaryRepository.findActiveBoundariesByBoundaryTypeId(boundaryTypeId);
    }

    public List<Boundary> getTopLevelBoundaryByHierarchyType(final HierarchyType hierarchyType) {
        return boundaryRepository.findActiveBoundariesByHierarchyTypeAndLevelAndAsOnDate(hierarchyType, 1l, new Date());
    }

    public List<Boundary> getActiveChildBoundariesByBoundaryId(final Long boundaryId) {
        return boundaryRepository.findActiveChildBoundariesByBoundaryIdAndAsOnDate(boundaryId, new Date());
    }

    public List<Boundary> getChildBoundariesByBoundaryId(final Long boundaryId) {
        return boundaryRepository.findChildBoundariesByBoundaryIdAndAsOnDate(boundaryId, new Date());
    }

    public Boundary getActiveBoundaryByBndryNumAndTypeAndHierarchyTypeCode(final Long bndryNum,
            final String boundaryType, final String hierarchyTypeCode) {
        return boundaryRepository.findActiveBoundaryByBndryNumAndTypeAndHierarchyTypeCodeAndAsOnDate(bndryNum,
                boundaryType, hierarchyTypeCode, new Date());
    }

    public List<Boundary> getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(final String boundaryTypeName,
            final String hierarchyTypeName) {
        return boundaryRepository.findActiveBoundariesByBndryTypeNameAndHierarchyTypeName(boundaryTypeName,
                hierarchyTypeName);
    }

    public List<Boundary> getBoundariesByBndryTypeNameAndHierarchyTypeName(final String boundaryTypeName,
            final String hierarchyTypeName) {
        return boundaryRepository.findBoundariesByBndryTypeNameAndHierarchyTypeName(boundaryTypeName,
                hierarchyTypeName);
    }

    public List<Boundary> getBondariesByNameAndType(final String boundaryName, final Long boundaryTypeId) {
        return boundaryRepository.findByNameAndBoundaryType(boundaryName, boundaryTypeId);
    }

    public Boolean validateBoundary(final BoundaryType boundaryType) {
        final Boundary bndry = boundaryRepository.findByBoundaryTypeNameAndHierarchyTypeNameAndLevel(
                boundaryType.getName(), boundaryType.getHierarchyType().getName(), 1l);
        if (bndry != null)
            return true;
        else
            return false;
    }

    public List<Boundary> getBondariesByNameAndBndryTypeAndHierarchyType(final String boundaryTypeName,
            final String hierarchyTypeName, final String name) {
        return boundaryRepository.findActiveBoundariesByNameAndBndryTypeNameAndHierarchyTypeName(boundaryTypeName,
                hierarchyTypeName, name);
    }

    public List<Map<String, Object>> getBoundaryDataByNameLike(final String name) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        crossHierarchyService
                .getChildBoundaryNameAndBndryTypeAndHierarchyType("Locality", "Location", "Administration", '%' + name + '%')
                .stream().forEach(location -> {
                    Map<String, Object> res = new HashMap<String, Object>();
                    res.put("id", location.getId());
                    res.put("name", location.getChild().getName() + " - " + location.getParent().getName());
                    list.add(res);
                });

        /*
         * getBoundaryByNameLike(name).stream() .forEach( location -> { Map<String, Object> res = new HashMap<String, Object>();
         * res.put("id", location.getId()); if (location.isRoot()) res.put("name", location.getName()); else { Boundary
         * currentLocation = location; StringBuilder loc = new StringBuilder(); String delim = ""; while
         * (!currentLocation.isRoot()) { loc.append(delim).append( currentLocation.getName()); delim = ","; currentLocation =
         * currentLocation .getParent(); if (currentLocation.isRoot()) { loc.append(delim).append( currentLocation.getName());
         * break; } } res.put("name", loc.toString()); list.add(res); } });
         */

        return list;

    }

    public List<Boundary> findActiveChildrenWithParent(final Long parentBoundaryId) {
        return boundaryRepository.findActiveChildrenWithParent(parentBoundaryId);
    }

    public List<Boundary> findActiveBoundariesForMpath(Set<String> mpath) {
        return boundaryRepository.findActiveBoundariesForMpath(mpath);
    }

    public List<Boundary> findAllChildrenWithoutParent(final Long parentBoundaryId) {
        return boundaryRepository.findAllChildrenWithOutParent(parentBoundaryId);
    }

    public List<Boundary> findActiveChildrenWitouthParent(final Long parentBoundaryId) {
        return boundaryRepository.findActiveChildrenWithOutParent(parentBoundaryId);
    }

    public List<Boundary> findActiveImmediateChildrenWithOutParent(final Long parentBoudaryId) {
        return boundaryRepository.findActiveImmediateChildrenWithOutParent(parentBoudaryId);
    }

    public List<Boundary> findAllParents() {
        return boundaryRepository.findAllParents();
    }

    public String getMaterializedPath(final Boundary child, final Boundary parent) {
        String mpath = "";
        int childSize = 0;
        if (null == parent) {
            mpath = String.valueOf(boundaryRepository.findAllParents().size() + 1);
        }
        else
            childSize = boundaryRepository.findActiveImmediateChildrenWithOutParent(parent.getId()).size();
        if (mpath.isEmpty()) {
            if (null != child) {
                if (!child.getMaterializedPath().equalsIgnoreCase(parent.getMaterializedPath() + "." + childSize)) {
                    childSize += 1;
                    mpath = parent.getMaterializedPath() + "." + childSize;
                }
                else
                    mpath = child.getMaterializedPath();
            }
            else {
                childSize += 1;
                mpath = parent.getMaterializedPath() + "." + childSize;
            }
        }

        return mpath;
    }
}