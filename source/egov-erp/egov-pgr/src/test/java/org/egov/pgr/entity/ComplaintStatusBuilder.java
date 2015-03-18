package org.egov.pgr.entity;

import java.lang.reflect.Field;

public class ComplaintStatusBuilder {

    private final ComplaintStatus complaintStatus;

    public ComplaintStatusBuilder() {
        complaintStatus = new ComplaintStatus();
    }

    public ComplaintStatusBuilder withName(final String name) {
        complaintStatus.setName(name);
        return this;
    }

    public ComplaintStatusBuilder withId(final long id) {
        try {
            final Field idField = complaintStatus.getClass().getSuperclass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(complaintStatus, id);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public ComplaintStatusBuilder withDefaults() {
        // withId(22l);
        withName("Registered");
        return this;
    }

    public ComplaintStatus build() {
        return complaintStatus;
    }
}