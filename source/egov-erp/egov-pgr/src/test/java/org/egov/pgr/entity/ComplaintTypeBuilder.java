package org.egov.pgr.entity;

import org.egov.lib.rjbac.dept.Department;
import org.egov.lib.rjbac.dept.DepartmentImpl;

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
}
