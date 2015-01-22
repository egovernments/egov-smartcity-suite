package org.egov.pgrweb.controller;

import org.egov.lib.rjbac.dept.Department;
import org.egov.lib.rjbac.dept.ejb.api.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class ComplaintTypeController {

    private DepartmentService departmentService;

    @Autowired
    public ComplaintTypeController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @ModelAttribute("departments")
    public List<Department> departments() {
        return departmentService.getAllDepartments();
    }

    @RequestMapping("/complaint-type")
    public String complaintType() {
        return "complaint-type";
    }
}
