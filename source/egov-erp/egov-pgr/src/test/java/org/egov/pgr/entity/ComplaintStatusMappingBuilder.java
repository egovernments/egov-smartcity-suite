package org.egov.pgr.entity;

import java.lang.reflect.Field;

import org.egov.lib.rjbac.role.RoleImpl;

public class ComplaintStatusMappingBuilder {

    private final ComplaintStatusMapping complaintStatusMapping;

    public ComplaintStatusMappingBuilder() {
        complaintStatusMapping = new ComplaintStatusMapping();
    }

    public ComplaintStatusMappingBuilder withCurrentStatus(final ComplaintStatus currentStatus) {
        complaintStatusMapping.setCurrentStatus(currentStatus);
        return this;
    }

    public ComplaintStatusMappingBuilder withShowStatus(final ComplaintStatus showStatus) {
        complaintStatusMapping.setShowStatus(showStatus);
        return this;
    }

    public ComplaintStatusMappingBuilder withRole(final RoleImpl role) {
        complaintStatusMapping.setRole(role);
        return this;
    }

    public ComplaintStatusMappingBuilder withOrderNo(final Integer orderNo) {
        complaintStatusMapping.setOrderNo(orderNo);
        return this;
    }

    public ComplaintStatusMappingBuilder withId(final long id) {
        try {
            final Field idField = complaintStatusMapping.getClass().getSuperclass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(complaintStatusMapping, id);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    /*
     * public ComplaintStatusMappingBuilder withDefaults() { withId(22l);
     * withCurrentStatus(currentStatus); withShowStatus(showStatus);
     * withRole(role); withOrderNo(orderNo); return this; }
     */

    public ComplaintStatusMapping build() {
        return complaintStatusMapping;
    }
}