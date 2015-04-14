package org.egov.infra.web.controller.admin.masters.boundary;

import javax.validation.Valid;

import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.BoundaryTypeService;
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
public class BoundaryController {

    private static final String REDIRECT_URL_VIEW = "redirect:/view-boundary/";
    
    private BoundaryService boundaryService;
    private BoundaryTypeService boundaryTypeService;

    @Autowired
    public BoundaryController(BoundaryService boundaryService, BoundaryTypeService boundaryTypeService) {
        this.boundaryService = boundaryService;
        this.boundaryTypeService = boundaryTypeService;
    }

    @ModelAttribute
    public void getBoundary(@PathVariable Long[] ids, Model model) {
        int paramLength = ids.length;
        Long boundaryId = null;

        if (paramLength > 2 && ids[2] != null) {
            boundaryId = ids[2];
            model.addAttribute("boundary", boundaryService.getBoundaryById(boundaryId));
        } else {
            if (model.asMap().get("boundary") == null) {
                model.addAttribute("boundary", new Boundary());
            }
        }

        Long boundaryTypeId = ids[1];
        BoundaryType boundaryType = boundaryTypeService.getBoundaryTypeById(boundaryTypeId);
        model.addAttribute("boundaryType", boundaryType);
        model.addAttribute("hierarchyType", boundaryType.getHierarchyType());
    }

    @RequestMapping(value = "/view-boundary/{ids}", method = RequestMethod.GET)
    public String viewBoundaryDetails() {
        return "boundary-view";
    }

    @RequestMapping(value = "/create-boundary/{ids}", method = RequestMethod.GET)
    public String showCreateBoundaryForm(Model model) {
        model.addAttribute("isUpdate", false);
        return "boundary-create";
    }

    @RequestMapping(value = "/update-boundary/{ids}", method = RequestMethod.GET)
    public String showUpdateBoundaryForm(Model model) {
        model.addAttribute("isUpdate", true);
        return "boundary-create";
    }
    
    @RequestMapping(value = "/list-boundaries/{ids}", method = RequestMethod.GET)
    public String showPaginationForm(Model model) {
        return "view-boundaries";
    }
    
    @RequestMapping(value = "/update-boundary/{ids}", method = RequestMethod.POST)
    public String UpdateBoundary(@Valid @ModelAttribute Boundary boundary, Model model,
            BindingResult errors, RedirectAttributes redirectAttributes) {

        if (errors.hasErrors()) {
            return "boundary-create";
        }

        BoundaryType boundaryTypeObj = boundaryTypeService.getBoundaryTypeById(boundary.getBoundaryTypeId());

        boundary.setBoundaryType(boundaryTypeObj);
        boundary.setHistory(false);
        
        boundaryService.updateBoundary(boundary);
        
        redirectAttributes.addFlashAttribute("boundary", boundary);
        redirectAttributes.addFlashAttribute("message", "Boundary successfully updated !");
        
        String pathVars = boundaryTypeObj.getHierarchyType().getId() + "," + boundaryTypeObj.getId();

        return REDIRECT_URL_VIEW + pathVars;
    }
}
