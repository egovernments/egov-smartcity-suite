package org.egov.pgr.entity;

import java.lang.reflect.Field;

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
        try {
            final Field idField = complaintRouter.getClass().getSuperclass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(complaintRouter, id);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
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