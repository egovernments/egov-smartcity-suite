package org.egov.pgr.web.controller.masters;

import java.util.List;

import javax.validation.Valid;

import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.service.ComplaintTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/complaint-type/update/{name}")
public class UpdateComplaintTypeController {

    private DepartmentService departmentService;
    private ComplaintTypeService complaintTypeService;

    @Autowired
    public UpdateComplaintTypeController(DepartmentService departmentService, ComplaintTypeService complaintTypeService) {
        this.departmentService = departmentService;
        this.complaintTypeService = complaintTypeService;
    }

    @ModelAttribute("departments")
    public List<Department> departments() {
        return departmentService.getAllDepartments();
    }

    @ModelAttribute
    public ComplaintType complaintTypeModel(@PathVariable String name) {
        return complaintTypeService.findByName(name);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String complaintTypeFormForUpdate() {
        return "complaint-type";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String updateComplaintType(@Valid @ModelAttribute ComplaintType complaintType, BindingResult errors,
            RedirectAttributes redirectAttrs,Model model) {
        if (errors.hasErrors()) {
            return "complaint-type";
        }

        complaintTypeService.updateComplaintType(complaintType);
        String message = "Complaint Type updated Successfully";
        redirectAttrs.addFlashAttribute("complaintType", complaintType);
        model.addAttribute("message", message);

        return "complaintType-success";
    }
}
