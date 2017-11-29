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

package org.egov.infra.admin.master.service;

import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.entity.HierarchyType;
import org.egov.infra.admin.master.repository.BoundaryTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class BoundaryTypeService {

    private final BoundaryTypeRepository boundaryTypeRepository;

    @Autowired
    public BoundaryTypeService(final BoundaryTypeRepository boundaryTypeRepository) {
        this.boundaryTypeRepository = boundaryTypeRepository;
    }

    @Transactional
    public void createBoundaryType(final BoundaryType boundaryType) {
        boundaryTypeRepository.save(boundaryType);
    }

    @Transactional
    public void updateBoundaryType(final BoundaryType boundaryType) {
        boundaryTypeRepository.save(boundaryType);
    }

    public BoundaryType getBoundaryTypeById(final Long id) {
        return boundaryTypeRepository.findOne(id);
    }

    public BoundaryType getBoundaryTypeByName(final String name) {
        return boundaryTypeRepository.findByName(name);
    }

    public BoundaryType getBoundaryTypeByHierarchyTypeNameAndLevel(final String name, final Long hierarchyLevel) {
        return boundaryTypeRepository.findByHierarchyTypeNameAndLevel(name, hierarchyLevel);
    }

    public List<BoundaryType> getAllBoundarTypesByHierarchyTypeId(final Long hierarchyTypeId) {
        return boundaryTypeRepository.findByHierarchyTypeId(hierarchyTypeId);
    }

    public BoundaryType getBoundaryTypeByParent(final Long parentId) {
        return boundaryTypeRepository.findByParent(parentId);
    }

    public BoundaryType getBoundaryTypeByIdAndHierarchyType(final Long id, final Long hierarchyTypeId) {
        return boundaryTypeRepository.findByIdAndHierarchy(id, hierarchyTypeId);
    }

    public BoundaryType setHierarchyLevel(final BoundaryType boundaryType, final String mode) {
        if ("create".equalsIgnoreCase(mode))
            boundaryType.setHierarchy(1l);
        else {
            final Long parentBoundaryTypeId = boundaryType.getParent().getId();
            Long childHierarchy = 0l;
            Long parentHierarchy = boundaryType.getParent().getHierarchy();
            if (parentBoundaryTypeId != null)
                childHierarchy = ++parentHierarchy;
            boundaryType.setHierarchy(childHierarchy);
        }
        return boundaryType;
    }

    public BoundaryType getBoundaryTypeByNameAndHierarchyType(final String name, final HierarchyType hierarchyType) {
        return boundaryTypeRepository.findByNameAndHierarchyType(name, hierarchyType);
    }

    public BoundaryType getBoundaryTypeByNameAndHierarchyTypeName(final String boundaryTypename,
                                                                  final String hierarchyTypeName) {
        return boundaryTypeRepository.findByNameAndHierarchyTypeName(boundaryTypename, hierarchyTypeName);
    }

    public List<BoundaryType> getBoundaryTypeByHierarchyTypeName(final String name) {
        return boundaryTypeRepository.findByHierarchyTypeName(name);
    }

    public List<BoundaryType> getBoundaryTypeByHierarchyTypeNames(final Set<String> names) {
        return boundaryTypeRepository.findByHierarchyTypeNames(names);
    }
}
