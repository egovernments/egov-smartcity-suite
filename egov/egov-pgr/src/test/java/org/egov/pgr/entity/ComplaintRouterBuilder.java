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
package org.egov.pgr.entity;

import org.egov.infra.admin.master.entity.Boundary;
import org.egov.pims.commons.Position;

public class ComplaintRouterBuilder {

    private final ComplaintRouter complaintRouter;

    public ComplaintRouterBuilder() {
        complaintRouter = new ComplaintRouter();
    }

    public ComplaintRouterBuilder withComplaintType(final ComplaintType complaintType) {
        complaintRouter.setComplaintType(complaintType);
        return this;
    }

    public ComplaintRouterBuilder withBoundary(final Boundary boundary) {
        complaintRouter.setBoundary(boundary);
        return this;
    }

    public ComplaintRouterBuilder withPosition(final Position position) {
        complaintRouter.setPosition(position);
        return this;
    }

    public ComplaintRouterBuilder withId(final long id) {
        complaintRouter.setId(id);
        return this;
    }

    public ComplaintRouterBuilder withDefaults() {
        // withId(22l);
        withComplaintType(new ComplaintTypeBuilder().withDefaults().build());
        // withBoundary(boundary);
        // withPosition(position);
        return this;
    }

    public ComplaintRouter build() {
        return complaintRouter;
    }
}