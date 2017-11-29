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

import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.BoundaryType;

import java.util.Date;

public class BoundaryBuilder {

    // use this count where unique names,descriptions etc required
    private static long count;
    private final Boundary boundary;

    public BoundaryBuilder() {
        boundary = new Boundary();
        count++;
    }

    public BoundaryBuilder withUpdatedTime(final Date updatedTime) {
        boundary.setLastModifiedDate(updatedTime);
        return this;
    }

    public BoundaryBuilder withId(final Long id) {
        boundary.setId(id);
        return this;
    }

    public BoundaryBuilder withBoundaryType(final BoundaryType boundaryType) {
        boundary.setBoundaryType(boundaryType);
        return this;
    }

    public BoundaryBuilder withParent(final Boundary parent) {
        boundary.setParent(parent);
        return this;
    }

    public BoundaryBuilder withName(final String name) {
        boundary.setName(name);
        return this;
    }

    public BoundaryBuilder withBoundaryNum(final Long boundaryNum) {
        boundary.setBoundaryNum(boundaryNum);
        return this;
    }

    public BoundaryBuilder withFromDate(final Date fromDate) {
        boundary.setFromDate(fromDate);
        return this;
    }

    public BoundaryBuilder withToDate(final Date toDate) {
        boundary.setToDate(toDate);
        return this;
    }

    public BoundaryBuilder withIsHistory(final boolean isHistory) {
        boundary.setActive(isHistory);
        return this;
    }

    public BoundaryBuilder withBndryId(final Long bndryId) {
        boundary.setBndryId(bndryId);
        return this;
    }

    public BoundaryBuilder withBndryNameLocal(final String bndryNameLocal) {
        boundary.setLocalName(bndryNameLocal);
        return this;
    }

    public BoundaryBuilder withLng(final Float lng) {
        boundary.setLongitude(lng);
        return this;
    }

    public BoundaryBuilder withLat(final Float lat) {
        boundary.setLatitude(lat);
        return this;
    }

    public BoundaryBuilder withMaterializedPath(final String materializedPath) {
        boundary.setMaterializedPath(materializedPath);
        return this;
    }

    public BoundaryBuilder withDefaults() {
        withId(count);
        if (null == boundary.getLastModifiedDate())
            withUpdatedTime(new Date());

        if (null == boundary.getBoundaryType())
            withBoundaryType(new BoundaryTypeBuilder().withDefaults().build());

        if (null == boundary.getName())
            withName("test-boundary-" + count);
        if (null == boundary.getBoundaryNum())
            withBoundaryNum(Long.valueOf(count));

        if (null == boundary.getFromDate())
            withFromDate(new Date());
        if (null == boundary.getToDate())
            withToDate(new Date());
        if (boundary.isActive())
            withIsHistory(false);
        if (null == boundary.getBndryId())
            withBndryId(Long.valueOf(count));
        if (null == boundary.getLocalName())
            withBndryNameLocal("test-local");
        if (null == boundary.getLongitude())
            withLng(123232f);
        if (null == boundary.getLatitude())
            withLat(1423423f);
        if (null == boundary.getMaterializedPath())
            withMaterializedPath("1");

        return this;
    }

    public BoundaryBuilder withDbDefaults() {
        if (null == boundary.getLastModifiedDate())
            withUpdatedTime(new Date());

        if (null == boundary.getBoundaryType())
            withBoundaryType(new BoundaryTypeBuilder().withDbDefaults().build());

        if (null == boundary.getName())
            withName("test-boundary-" + count);
        if (null == boundary.getBoundaryNum())
            withBoundaryNum(Long.valueOf(count));

        if (null == boundary.getFromDate())
            withFromDate(new Date());
        if (null == boundary.getToDate())
            withToDate(new Date());
        if (boundary.isActive())
            withIsHistory(false);
        if (null == boundary.getBndryId())
            withBndryId(Long.valueOf(count));
        if (null == boundary.getLocalName())
            withBndryNameLocal("test-local");
        if (null == boundary.getLongitude())
            withLng(123232f);
        if (null == boundary.getLatitude())
            withLat(1423423f);
        if (null == boundary.getMaterializedPath())
            withMaterializedPath("1");
        return this;
    }

    public Boundary build() {
        return boundary;
    }
}