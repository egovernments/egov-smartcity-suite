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

package org.egov.infra.admin.master.service;

import java.util.List;

import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.entity.CrossHierarchy;
import org.egov.infra.admin.master.repository.CrossHierarchyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CrossHierarchyService {

    private final CrossHierarchyRepository crossHierarchyRepository;

    @Autowired
    public CrossHierarchyService(final CrossHierarchyRepository crossHierarchyRepository) {
        this.crossHierarchyRepository = crossHierarchyRepository;
    }

    @Transactional
    public CrossHierarchy create(final CrossHierarchy crossHierarchy) {
        return crossHierarchyRepository.save(crossHierarchy);
    }

    @Transactional
    public CrossHierarchy update(final CrossHierarchy crossHierarchy) {
        return crossHierarchyRepository.save(crossHierarchy);
    }

    @Transactional
    public void delete(final CrossHierarchy crossHierarchy) {
        crossHierarchyRepository.delete(crossHierarchy);
    }

    public List<Boundary> getCrossHierarchyChildrens(final Boundary boundary, final BoundaryType boundaryType) {
        return crossHierarchyRepository.findByParentAndChildBoundaryType(boundary, boundaryType);
    }

    public List<CrossHierarchy> getChildBoundaryNameAndBndryTypeAndHierarchyType(final String boundaryTypeName,
            final String hierarchyTypeName, final String parenthierarchyTypeName, final String name) {
        return crossHierarchyRepository.findActiveBoundariesByNameAndBndryTypeNameAndHierarchyTypeName(boundaryTypeName,
                hierarchyTypeName, parenthierarchyTypeName, name);
    }

    public List<Boundary> getChildBoundariesNameAndBndryTypeAndHierarchyType(final String boundaryTypeName,
            final String hierarchyTypeName) {
        return crossHierarchyRepository.findChildBoundariesNameAndBndryTypeAndHierarchyType(boundaryTypeName,
                hierarchyTypeName);
    }

    public List<Boundary> getParentBoundaryByChildBoundaryAndParentBoundaryType(final Long childId,
            final Long parentTypeId) {
        return crossHierarchyRepository.findParentBoundaryByChildBoundaryAndParentBoundaryType(childId, parentTypeId);
    }

    public List<Boundary> getActiveChildBoundariesByBoundaryId(final Long id) {
        return crossHierarchyRepository.findActiveBoundariesById(id);
    }

    public CrossHierarchy findById(final Long id) {
        return crossHierarchyRepository.findOne(id);
    }

    public List<CrossHierarchy> findAllByBoundaryTypes(final BoundaryType parentType, final BoundaryType childType) {
        return crossHierarchyRepository.findByParentTypeAndChildType(parentType, childType);
    }

    public List<Boundary> findChildBoundariesByParentBoundary(final String boundaryTypeName,
            final String hierarchyTypeName, final String parentBoundary) {
        return crossHierarchyRepository.findChildBoundariesByParentBoundary(boundaryTypeName, hierarchyTypeName,
                parentBoundary);
    }

    public CrossHierarchy findAllByParentAndChildBoundary(final Long parentId, final Long childId) {
        return crossHierarchyRepository.findBoundariesByParentAndChildBoundary(parentId, childId);
    }
}