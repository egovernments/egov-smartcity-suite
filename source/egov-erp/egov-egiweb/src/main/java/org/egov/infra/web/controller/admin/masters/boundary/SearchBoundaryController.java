package org.egov.infra.web.controller.admin.masters.boundary;

import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.HierarchyTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = { "/search-boundary", "/view-boundary", "/create-boundary"})
public class SearchBoundaryController {

    private BoundaryService boundaryService;
    private HierarchyTypeService hierarchyTypeService;
    
    @Autowired
    public SearchBoundaryController(BoundaryService boundaryService, HierarchyTypeService hierarchyTypeService){
        this.boundaryService = boundaryService; 
        this.hierarchyTypeService = hierarchyTypeService;
    }
    
    @ModelAttribute
    public Boundary boundaryModel() {
        return new Boundary();
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public String showSearchBoundaryForm(Model model) {
        model.addAttribute("hierarchyTypes", hierarchyTypeService.getAllHierarchyTypes());
        return "boundary-search";
    }
}
