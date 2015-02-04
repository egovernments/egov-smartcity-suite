package org.egov.pgr.entity;

import org.egov.builder.entities.DepartmentBuilder;
import org.egov.lib.rjbac.dept.Department;
import org.egov.lib.rjbac.dept.DepartmentImpl;

import java.lang.reflect.Field;

public class ComplaintTypeBuilder {

    private final ComplaintType complaintType;

    public ComplaintTypeBuilder() {
        complaintType = new ComplaintType();
    }

    public ComplaintTypeBuilder withName(String name) {
        complaintType.setName(name);
        return this;
    }

    public ComplaintTypeBuilder withDepartment(Department department) {
        complaintType.setDepartment((DepartmentImpl) department);
        return this;
    }

    public ComplaintType build() {
        return complaintType;
    }

    public ComplaintTypeBuilder withDefaults() {
        Department dept = new DepartmentBuilder().withCode("PR").withName("Roads and Canals").build();

        withId(44L);
        withName("Potholes");
        withDepartment(dept);
        return this;
    }

    public ComplaintTypeBuilder withId(long id) {
        try {
            Field idField  = complaintType.getClass().getSuperclass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(complaintType, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }
}
