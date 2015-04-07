package org.egov.infra.web.support.formatter;

import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
public class DepartmentFormatter implements Formatter<Department> {


    private DepartmentService departmentService;

    @Autowired
    public DepartmentFormatter(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @Override
    public Department parse(String departmentCode, Locale locale) throws ParseException {
        return departmentService.getDepartmentByCode(departmentCode);
    }

    @Override
    public String print(Department department, Locale locale) {
        return department.getName();
    }

}
