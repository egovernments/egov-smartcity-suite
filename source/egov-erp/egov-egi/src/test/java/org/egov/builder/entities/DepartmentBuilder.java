package org.egov.builder.entities;

import java.lang.reflect.Field;

import org.egov.infra.admin.master.entity.Department;

public class DepartmentBuilder {

    private final Department department;

    private static int count;

    public DepartmentBuilder() {
        department = new Department();
    }

    public DepartmentBuilder withName(final String departmentName) {
        department.setName(departmentName);
        return this;
    }

    public DepartmentBuilder withCode(final String code) {
        department.setCode(code);
        return this;
    }

    public DepartmentBuilder withDefaults() {
        withId(count);
        if (null == department.getName())
            withName("test-department-" + count);
        if (null == department.getCode())
            withCode("test-" + count);
        return this;
    }
    
    public DepartmentBuilder withId(final int id) {
        try {
            final Field idField = department.getClass().getSuperclass().getSuperclass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(department, id);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public DepartmentBuilder withDbDefaults() {
        if (null == department.getName())
            withName("test-department-" + count);
        if (null == department.getCode())
            withCode("test-" + count);
        return this;
    }

    public Department build() {
        return department;
    }
}
