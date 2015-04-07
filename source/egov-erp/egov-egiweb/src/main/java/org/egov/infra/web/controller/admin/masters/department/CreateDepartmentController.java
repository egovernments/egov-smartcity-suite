package org.egov.infra.web.controller.admin.masters.department;

import javax.validation.Valid;

import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author subhash
 */

@Controller
@RequestMapping(value = "/create-department")
public class CreateDepartmentController {

    private static final String REDIRECT_DEPARTMENT_FORM = "redirect:create-department";
    private static final String DEPARTMENT_FORM = "department-form";
    private DepartmentService departmentService;

    @Autowired
    public CreateDepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @ModelAttribute
    public Department departmentModel() {
        return new Department();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String createDepartmentForm(Model model) {
        return DEPARTMENT_FORM;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String createDepartment(@Valid @ModelAttribute Department department, BindingResult errors,
            RedirectAttributes redirectAttrs) {
        if (errors.hasErrors()) {
            return DEPARTMENT_FORM;
        }

        departmentService.createDepartment(department);
        redirectAttrs.addFlashAttribute("message", "Successfully created Department !");

        return REDIRECT_DEPARTMENT_FORM;
    }
}
