package org.egov.infra.web.controller.admin.masters.department;

import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author subhash
 */
@Controller
@RequestMapping(value = "/department/view/{name}")
public class ViewDepartmentController {

    private static final String DEPARTMENT_VIEW = "department-view";

    DepartmentService departmentService;

    @Autowired
    public ViewDepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @ModelAttribute
    public Department departmentModel(@PathVariable String name) {
        return departmentService.getDepartmentByName(name);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String viewDepartment() {
        return DEPARTMENT_VIEW;
    }
}
