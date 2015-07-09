package org.egov.pgr.web.controller.masters;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;

import javax.validation.Valid;

import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.service.BoundaryTypeService;
import org.egov.pgr.entity.ComplaintRouter;
import org.egov.pgr.service.ComplaintRouterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/router/create")
class CreateRouterController {

    private final BoundaryTypeService boundaryTypeService;
    private final ComplaintRouterService complaintRouterService;

    @Autowired
    public CreateRouterController(final BoundaryTypeService boundaryTypeService,
            final ComplaintRouterService complaintRouterService) {
        this.boundaryTypeService = boundaryTypeService;
        this.complaintRouterService = complaintRouterService;
    }

    @RequestMapping(method = GET)
    public String createRouterForm() {
        return "router-create";
    }

    @ModelAttribute("boundaryTypes")
    public List<BoundaryType> boundaryTypes() {
        return boundaryTypeService.getBoundaryTypeByHierarchyTypeName("ADMINISTRATION");
    }

    @ModelAttribute
    public ComplaintRouter complaintRouter() {
        return new ComplaintRouter();
    }

    @RequestMapping(method = POST)
    public String saveRouter(@Valid @ModelAttribute final ComplaintRouter complaintRouter, final BindingResult errors,
            final RedirectAttributes redirectAttrs, final Model model) {
        if (errors.hasErrors())
            return "router-create";
        if (complaintRouterService.validateRouter(complaintRouter)) {
            model.addAttribute("warning", "Complaint Router already exist");
            return "router-create";
        } else {
            complaintRouterService.createComplaintRouter(complaintRouter);
            redirectAttrs.addFlashAttribute("complaintRouter", complaintRouter);
            return "router-success";
        }
    }

}
