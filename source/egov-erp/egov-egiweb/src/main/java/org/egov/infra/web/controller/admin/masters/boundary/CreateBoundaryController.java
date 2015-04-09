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

@Controller
public class CreateBoundaryController {
    
    private BoundaryService boundaryService;
    private BoundaryTypeService boundaryTypeService;
    private HierarchyTypeService hierarchyTypeService;
    
    @Autowired
    public CreateBoundaryController(BoundaryService boundaryService, BoundaryTypeService boundaryTypeService,
            HierarchyTypeService hierarchyTypeService) {
        this.boundaryService = boundaryService;
        this.boundaryTypeService = boundaryTypeService;
        this.hierarchyTypeService = hierarchyTypeService;
    }
    
    @RequestMapping(value = { "/boundary/create", "/boundary/update" }, method = RequestMethod.POST)
    public String createOrUpdateBoundary(@Valid @ModelAttribute Boundary boundary, Model model,
            BindingResult errors) {

        if (errors.hasErrors()) {
            return "boundary-create";
        }

        BoundaryType boundaryTypeObj = boundaryTypeService.getBoundaryTypeById(boundary.getBoundaryTypeId());

        boundary.setBoundaryType(boundaryTypeObj);
        boundary.setHistory(false);
        
        boundaryService.createBoundary(boundary);
        
        model.addAttribute("message", "Boundary successfully created !");

        return "boundary-create";
    }
}
