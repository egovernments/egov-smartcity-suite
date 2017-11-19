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
package org.egov.builder.entities;

import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.entity.HierarchyType;

import java.util.Date;

public class BoundaryTypeBuilder {

    private final BoundaryType boundaryTypeImpl;

    // use this count where unique names,desciptions etc required
    private static long count;

    public BoundaryTypeBuilder() {
        boundaryTypeImpl = new BoundaryType();
        count++;
    }

    public BoundaryTypeBuilder withUpdatedTime(final Date updatedTime) {
        boundaryTypeImpl.setLastModifiedDate(updatedTime);
        return this;
    }

    public BoundaryTypeBuilder withId(final Long id) {
        boundaryTypeImpl.setId(id);
        return this;
    }

    public BoundaryTypeBuilder withName(final String name) {
        boundaryTypeImpl.setName(name);
        return this;
    }

    public BoundaryTypeBuilder withHierarchy(final Long hierarchy) {
        boundaryTypeImpl.setHierarchy(hierarchy);
        return this;
    }

    public BoundaryTypeBuilder withHeirarchyType(final HierarchyType hierarchyType) {
        boundaryTypeImpl.setHierarchyType(hierarchyType);
        return this;
    }

    public BoundaryTypeBuilder withId(final long id) {
    	boundaryTypeImpl.setId(id);
        return this;
    }

    public BoundaryTypeBuilder withDefaults() {
        withId(count);
        if (null == boundaryTypeImpl.getLastModifiedDate())
            withUpdatedTime(new Date());

        if (null == boundaryTypeImpl.getName())
            withName("test-BoundaryType-" + count);

        if (null != boundaryTypeImpl.getHierarchy() && 0 == boundaryTypeImpl.getHierarchy())
            withHierarchy(Long.valueOf(count));
        if (null != boundaryTypeImpl.getHierarchyType())
            withHeirarchyType(new HeirarchyTypeBuilder().withDefaults().build());
        return this;
    }

    public BoundaryTypeBuilder withDbDefaults() {
        if (null != boundaryTypeImpl.getLastModifiedDate())
            withUpdatedTime(new Date());

        if (null == boundaryTypeImpl.getName())
            withName("test-BoundaryType-" + count);

        
        if (null == boundaryTypeImpl.getHierarchy())
            withHierarchy(Long.valueOf(count));

        if (null == boundaryTypeImpl.getHierarchyType())
            withHeirarchyType(new HeirarchyTypeBuilder().withDbDefaults().build());

        return this;
    }

    public BoundaryType build() {
        return boundaryTypeImpl;
    }
}