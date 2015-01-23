package org.egov.builder.entities;

import org.egov.lib.rjbac.dept.Department;
import org.egov.lib.rjbac.dept.DepartmentImpl;

public class DepartmentBuilder {

    private final DepartmentImpl department;

    public DepartmentBuilder() {
        department = new DepartmentImpl();
    }

    public DepartmentBuilder withName(String departmentName) {
        department.setDeptName(departmentName);
        return this;
    }

    public DepartmentBuilder withCode(String code) {
        department.setDeptCode(code);
        return this;
    }

    public Department build() {
        return department;
    }
}
