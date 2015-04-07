package org.egov.pgr.entity;

import java.lang.reflect.Field;

import org.egov.builder.entities.DepartmentBuilder;
import org.egov.infra.admin.master.entity.Department;

public class ComplaintTypeBuilder {

    private final ComplaintType complaintType;

    public static int count;

    public ComplaintTypeBuilder() {
        complaintType = new ComplaintType();
        count++;
    }

    public ComplaintTypeBuilder withName(final String name) {
        complaintType.setName(name);
        return this;
    }

    public ComplaintTypeBuilder withDepartment(final Department department) {
        complaintType.setDepartment(department);
        return this;
    }

    public ComplaintType build() {
        return complaintType;
    }

    public ComplaintTypeBuilder withDefaults() {
        final Department dept = new DepartmentBuilder().withCode("PR").withName("Roads and Canals").build();
        withId(44L);
        withName("Potholes");
        withDepartment(dept);
        return this;
    }

    public ComplaintTypeBuilder withDbDefaults() {
        withName("test-complainttype-" + count);
        withDepartment(new DepartmentBuilder().withDbDefaults().build());
        return this;
    }

    public ComplaintTypeBuilder withId(final long id) {
        try {
            final Field idField = complaintType.getClass().getSuperclass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(complaintType, id);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public static class Builder {
    }
}
