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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/complaint-type")
public class CreateComplaintTypeController {

    private DepartmentService departmentService;
    private ComplaintTypeService complaintTypeService;

    @Autowired
    public CreateComplaintTypeController(DepartmentService departmentService, ComplaintTypeService complaintTypeService) {
        this.departmentService = departmentService;
        this.complaintTypeService = complaintTypeService;
    }

    @ModelAttribute("departments")
    public List<Department> departments() {
        return departmentService.getAllDepartments();
    }

    @ModelAttribute
    public ComplaintType complaintTypeModel() {
        return new ComplaintType();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String complaintTypeForm() {
        return "complaint-type";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String createComplaintType(@Valid @ModelAttribute ComplaintType complaintType, BindingResult errors, RedirectAttributes redirectAttrs,Model model) {
        if (errors.hasErrors()) {
            return "complaint-type";
        }
        complaintTypeService.createComplaintType(complaintType);
        String message = "Complaint Type created Successfully";
        redirectAttrs.addFlashAttribute("complaintType", complaintType);
        model.addAttribute("message", message);
        return "complaintType-success";
    }
}
