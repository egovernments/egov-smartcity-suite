package org.egov.pgrweb.formatter;

import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.lib.rjbac.dept.ejb.api.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
public class DepartmentFormatter implements Formatter<DepartmentImpl> {


    private DepartmentService departmentService;

    @Autowired
    public DepartmentFormatter(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @Override
    public DepartmentImpl parse(String departmentCode, Locale locale) throws ParseException {
        return (DepartmentImpl) departmentService.getDepartmentByCode(departmentCode);
    }

    @Override
    public String print(DepartmentImpl department, Locale locale) {
        return department.getDeptName();
    }

}
