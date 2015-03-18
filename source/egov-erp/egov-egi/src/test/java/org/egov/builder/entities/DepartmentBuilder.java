package org.egov.builder.entities;

import org.egov.lib.rjbac.dept.Department;
import org.egov.lib.rjbac.dept.DepartmentImpl;

public class DepartmentBuilder {

    private final DepartmentImpl department;

    private static int count;

    public DepartmentBuilder() {
        department = new DepartmentImpl();
    }

    public DepartmentBuilder withName(final String departmentName) {
        department.setDeptName(departmentName);
        return this;
    }

    public DepartmentBuilder withCode(final String code) {
        department.setDeptCode(code);
        return this;
    }

    public DepartmentBuilder withDefaults() {
        withId(count);
        if (null == department.getDeptName())
            withName("test-department-" + count);
        if (null == department.getDeptCode())
            withCode("test-" + count);
        return this;
    }

    private DepartmentBuilder withId(final int id) {
        department.setId(id);
        return this;
    }

    public DepartmentBuilder withDbDefaults() {
        if (null == department.getDeptName())
            withName("test-department-" + count);
        if (null == department.getDeptCode())
            withCode("test-" + count);
        return this;
    }

    public Department build() {
        return department;
    }
}
