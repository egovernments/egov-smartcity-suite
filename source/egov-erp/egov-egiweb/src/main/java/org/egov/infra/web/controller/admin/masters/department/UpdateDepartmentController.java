package org.egov.infra.web.controller.admin.masters.department;

import javax.validation.Valid;

import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author subhash
 */
@Controller
@RequestMapping(value = "/department/update/{name}")
public class UpdateDepartmentController {

    private static final String REDIRECT_VIEW_DEPARTMENT = "redirect:/department/view/";
    private static final String DEPARTMENT_UPDATE_FORM = "department-updateForm";
    DepartmentService departmentService;

    @Autowired
    public UpdateDepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @ModelAttribute
    public Department departmentModel(@PathVariable String name) {
        return departmentService.getDepartmentByName(name);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String updateDepartmentForm() {
        return DEPARTMENT_UPDATE_FORM;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String updateDepartment(@ModelAttribute @Valid Department department, BindingResult errors,
            RedirectAttributes redirectAttributes) {
        if (errors.hasErrors()) {
            return DEPARTMENT_UPDATE_FORM;
        }

        departmentService.updateDepartment(department);
        redirectAttributes.addFlashAttribute("message", "Successfully updated Department !");
        return REDIRECT_VIEW_DEPARTMENT + department.getName();
    }
}
