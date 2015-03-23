package org.egov.pgr.entity;

import java.lang.reflect.Field;
import java.util.Set;

import org.egov.builder.entities.BoundaryBuilder;
import org.egov.eis.entity.PositionBuilder;
import org.egov.lib.admbndry.BoundaryImpl;
import org.egov.pgr.entity.enums.ReceivingMode;
import org.egov.pims.commons.Position;

public class ComplaintBuilder {

    private final Complaint complaint;

    private static int count;

    public ComplaintBuilder() {
        complaint = new Complaint();
        count++;
    }

    public ComplaintBuilder withCRN(final String CRN) {
        complaint.setCRN(CRN);
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

    public ComplaintBuilder withLocation(final BoundaryImpl location) {
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
        try {
            final Field idField = complaint.getClass().getSuperclass().getSuperclass().getSuperclass()
                    .getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(complaint, id);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public ComplaintBuilder withDefaults() {
        withId(count);
        if (null == complaint.getCRN())
            withCRN("TEST-CMXJ4-" + count);
        if (null == complaint.getComplaintType())
            withComplaintType(new ComplaintTypeBuilder().withDefaults().build());
        if (null == complaint.getComplainant())
            withComplainant(new ComplainantBuilder().withDefaults().build());
        if (null == complaint.getAssignee())
            withAssignee(new PositionBuilder().withName("testPos").build());
        // if(null==complaint.getLocation())
        // withLocation(new BoundaryImpl);
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
        if (null == complaint.getCRN())
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