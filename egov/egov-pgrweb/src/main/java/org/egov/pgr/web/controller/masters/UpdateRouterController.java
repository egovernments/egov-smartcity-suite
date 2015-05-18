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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/router")
class UpdateRouterController {

    private final BoundaryTypeService boundaryTypeService;
    private final ComplaintRouterService complaintRouterService;

    @Autowired
    public UpdateRouterController(final BoundaryTypeService boundaryTypeService,
            final ComplaintRouterService complaintRouterService) {
        this.boundaryTypeService = boundaryTypeService;
        this.complaintRouterService = complaintRouterService;
    }

    @ModelAttribute("boundaryTypes")
    public List<BoundaryType> boundaryTypes() {
        return boundaryTypeService.getBoundaryTypeByHierarchyTypeName("ADMINISTRATION");
    }

    @ModelAttribute
    public ComplaintRouter complaintRouter(@PathVariable final Long id) {
        return complaintRouterService.getRouterById(id);
    }

    @RequestMapping(value = "/update/{id}", method = GET)
    public String updateRouterForm(final Model model, @PathVariable final Long id) {
        return "router-update";
    }

    @RequestMapping(value = "/update/{id}", method = POST)
    public String update(@Valid @ModelAttribute final ComplaintRouter complaintRouter, final BindingResult errors,
            final RedirectAttributes redirectAttrs, final Model model) {
        if (errors.hasErrors())
            return "router-update";

        complaintRouterService.updateComplaintRouter(complaintRouter);
        redirectAttrs.addFlashAttribute("complaintRouter", complaintRouter);
        model.addAttribute("routerHeading", "Update Complaint Router");
        model.addAttribute("message", "Complaint Router updated successfully");
        return "router-success";
    }

    @RequestMapping(value = "/delete/{id}", method = POST)
    public String delete(@Valid @ModelAttribute final ComplaintRouter complaintRouter, final BindingResult errors,
            final RedirectAttributes redirectAttrs, final Model model) {
        if (errors.hasErrors())
            return "router-update";

        complaintRouterService.deleteComplaintRouter(complaintRouter);
        redirectAttrs.addFlashAttribute("complaintRouter", complaintRouter);
        model.addAttribute("routerHeading", "Delete Complaint Router");
        model.addAttribute("message", "Complaint Router deleted successfully");
        return "router-deleteMsg";

    }

}
