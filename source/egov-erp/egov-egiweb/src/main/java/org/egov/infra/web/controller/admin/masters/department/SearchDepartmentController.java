package org.egov.infra.web.controller.admin.masters.department;

import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author subhash
 */
@Controller
@RequestMapping(value = "/department/")
public class SearchDepartmentController {

    private static final String DEPARTMENTS = "departments";
    private static final String UPDATE = "update";
    private static final String VIEW = "view";
    private static final String REDIRECT_DEPARTMENT_VIEW = "redirect:/department/view/";
    private static final String REDIRECT_DEPARTMENT_UPDATE = "redirect:/department/update/";
    private static final String DEPARTMENT_SEARCH = "department-search";
    DepartmentService departmentService;

    @Autowired
    public SearchDepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @ModelAttribute
    public Department departmentModel() {
        return new Department();
    }

    @RequestMapping(value = { VIEW, UPDATE }, method = RequestMethod.GET)
    public String searchForm(Model model) {
        model.addAttribute(DEPARTMENTS, departmentService.getAllDepartments());
        return DEPARTMENT_SEARCH;
    }

    @RequestMapping(value = VIEW, method = RequestMethod.POST)
    public String viewDepartment(@ModelAttribute Department department) {
        return REDIRECT_DEPARTMENT_VIEW + department.getName();
    }

    @RequestMapping(value = UPDATE, method = RequestMethod.POST)
    public String updateDepartmentForm(@ModelAttribute Department department) {
        return REDIRECT_DEPARTMENT_UPDATE + department.getName();
    }
}
