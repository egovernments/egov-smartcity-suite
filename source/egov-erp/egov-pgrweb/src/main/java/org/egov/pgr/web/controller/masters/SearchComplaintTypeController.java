package org.egov.pgr.web.controller.masters;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
@RequestMapping("/complaint-type/update")
public class SearchComplaintTypeController {

    private ComplaintTypeService complaintTypeService;

    @Autowired
    public SearchComplaintTypeController(ComplaintTypeService complaintTypeService) {
        this.complaintTypeService = complaintTypeService;
    }

    @ModelAttribute
    public ComplaintType complaintTypeModel() {
        return new ComplaintType();
    }

    @ModelAttribute(value = "complaintTypes")
    public List<ComplaintType> listComplaintTypes() {
        return complaintTypeService.findAll();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showComplaintTypes(Model model) {

        return "complaintType-list";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String search(@ModelAttribute ComplaintType complaintType, final BindingResult errors,
            RedirectAttributes redirectAttrs, HttpServletRequest request) {

        if (errors.hasErrors())
            return "complaint-type";

        return "redirect:" + request.getRequestURI().split("/")[request.getRequestURI().split("/").length - 1] + "/"
                + complaintType.getName();
    }

}
