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

import org.egov.builder.entities.BoundaryBuilder;
import org.egov.eis.entity.PositionBuilder;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.pgr.entity.enums.ReceivingMode;
import org.egov.pims.commons.Position;

import java.util.Set;

public class ComplaintBuilder {

    private final Complaint complaint;

    private static int count;

    public ComplaintBuilder() {
        complaint = new Complaint();
        count++;
    }

    public ComplaintBuilder withCRN(final String CRN) {
        complaint.setCrn(CRN);
        return this;
    }

    public ComplaintBuilder withComplaintType(final ComplaintType complaintType) {
        complaint.setComplaintType(complaintType);
        return this;
    }

    public ComplaintBuilder withComplainant(final Complainant complainant) {
        complaint.setComplainant(complainant);
        return this;
    }

    public ComplaintBuilder withAssignee(final Position assignee) {
        complaint.setAssignee(assignee);
        return this;
    }

    public ComplaintBuilder withLocation(final Boundary location) {
        complaint.setLocation(location);
        return this;
    }

    public ComplaintBuilder withStatus(final ComplaintStatus status) {
        complaint.setStatus(status);
        return this;
    }

    public ComplaintBuilder withDetails(final String details) {
        complaint.setDetails(details);
        return this;
    }

    public ComplaintBuilder withLandmarkDetails(final String landmarkDetails) {
        complaint.setLandmarkDetails(landmarkDetails);
        return this;
    }

    public ComplaintBuilder withReceivingMode(final ReceivingMode receivingMode) {
        complaint.setReceivingMode(receivingMode);
        return this;
    }

    public ComplaintBuilder withReceivingCenter(final ReceivingCenter receivingCenter) {
        complaint.setReceivingCenter(receivingCenter);
        return this;
    }

    public ComplaintBuilder withSupportDocs(final Set supportDocs) {
        complaint.setSupportDocs(supportDocs);
        return this;
    }

    public ComplaintBuilder withLng(final double lng) {
        complaint.setLng(lng);
        return this;
    }

    public ComplaintBuilder withLat(final double lat) {
        complaint.setLat(lat);
        return this;
    }

    public ComplaintBuilder withId(final long id) {
        complaint.setId(id);
        return this;
    }

    public ComplaintBuilder withDefaults() {
        withId(count);
        if (null == complaint.getCrn())
            withCRN("TEST-CMXJ4-" + count);
        if (null == complaint.getComplaintType())
            withComplaintType(new ComplaintTypeBuilder().withDefaults().build());
        if (null == complaint.getComplainant())
            withComplainant(new ComplainantBuilder().withDefaults().build());
        if (null == complaint.getAssignee())
            withAssignee(new PositionBuilder().withName("testPos").build());
        // if(null==complaint.getLocation())
        // withLocation(new Boundary);
        if (null == complaint.getStatus())
            withStatus(new ComplaintStatusBuilder().withDefaults().build());
        withDetails("test-Description");
        withLandmarkDetails("test-LandDetails");
        if (null == complaint.getReceivingMode())
            withReceivingMode(ReceivingMode.WEBSITE);
        withReceivingCenter(new ReceivingCenterBuilder().withDefaults().build());
        // withSupportDocs(supportDocs);
        withLng(2222L);
        withLat(4444L);
        return this;
    }

    public ComplaintBuilder withDbDefaults() {
        if (null == complaint.getCrn())
            withCRN("TEST-CMXJ4-" + count);
        if (null == complaint.getComplaintType())
            withComplaintType(new ComplaintTypeBuilder().withDefaults().build());
        if (null == complaint.getComplainant())
            withComplainant(new ComplainantBuilder().withDefaults().build());
        if (null == complaint.getAssignee())
            withAssignee(new PositionBuilder().withName("testPos").build());
        if (null == complaint.getLocation())
            withLocation(new BoundaryBuilder().withDbDefaults().build());
        if (null == complaint.getStatus())
            withStatus(new ComplaintStatusBuilder().withDefaults().build());
        withDetails("test-Description");
        withLandmarkDetails("test-LandDetails");
        if (null == complaint.getReceivingMode())
            withReceivingMode(ReceivingMode.WEBSITE);
        withReceivingCenter(new ReceivingCenterBuilder().withDefaults().build());
        // withSupportDocs(supportDocs);
        withLng(2222L);
        withLat(4444L);
        return this;
    }

    public Complaint build() {
        return complaint;
    }
}