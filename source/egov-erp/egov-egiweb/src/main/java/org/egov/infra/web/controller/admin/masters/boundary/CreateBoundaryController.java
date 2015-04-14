package org.egov.infra.web.controller.admin.masters.boundary;

import javax.validation.Valid;

import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.entity.CityWebsite;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.BoundaryTypeService;
import org.egov.infra.admin.master.service.HierarchyTypeService;
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
public class CreateBoundaryController {
    
    private static final String REDIRECT_URL_VIEW = "redirect:/view-boundary/";
            
    private BoundaryService boundaryService;
    private BoundaryTypeService boundaryTypeService;
    
    @Autowired
    public CreateBoundaryController(BoundaryService boundaryService, BoundaryTypeService boundaryTypeService) {
        this.boundaryService = boundaryService;
        this.boundaryTypeService = boundaryTypeService;
    }
    
    @RequestMapping(value = "/boundary/create", method = RequestMethod.POST)
    public String createOrUpdateBoundary(@Valid @ModelAttribute Boundary boundary, Model model,
            BindingResult errors, RedirectAttributes redirectAttributes) {

        if (errors.hasErrors()) {
            return "boundary-create";
        }

        BoundaryType boundaryTypeObj = boundaryTypeService.getBoundaryTypeById(boundary.getBoundaryTypeId());

        boundary.setBoundaryType(boundaryTypeObj);
        boundary.setHistory(false);
        
        boundaryService.createBoundary(boundary);
        
        redirectAttributes.addFlashAttribute("boundary", boundary);
        redirectAttributes.addFlashAttribute("message", "Boundary successfully created !");
        
        String pathVars = boundaryTypeObj.getHierarchyType().getId() + "," + boundaryTypeObj.getId();

        return REDIRECT_URL_VIEW + pathVars;
    }
}
