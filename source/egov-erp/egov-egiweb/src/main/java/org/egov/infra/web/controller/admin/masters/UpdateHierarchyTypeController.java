package org.egov.infra.web.controller.admin.masters;

import javax.validation.Valid;

import org.egov.infra.admin.master.entity.HierarchyType;
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
@RequestMapping("/hierarchy-type/update/{name}")
public class UpdateHierarchyTypeController {

    private static final String STR_MSG_UPDATE_SUCCESS = "Successfully updated Hierarchy Type !";
    
    private HierarchyTypeService hierarchyTypeService;
    
    @Autowired
    public UpdateHierarchyTypeController(HierarchyTypeService hierarchyTypeService) {
        this.hierarchyTypeService = hierarchyTypeService;
    }
    
    @ModelAttribute
    public HierarchyType hierarchyTypeModel(@PathVariable String  name) {
           return hierarchyTypeService.getHierarchyTypeByName(name);
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public String hierarchyTypeUpdateForm(Model model) {
        return "hierarchyType-updateForm";
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public String updateHierarchyType(@ModelAttribute @Valid HierarchyType hierarchyType, BindingResult errors,
            RedirectAttributes additionalAttr) {

        if (errors.hasErrors()) {
            return "hierarchyType-updateForm";
        }

        //hierarchyTypeService.updateHierarchyType(hierarchyType);
        additionalAttr.addFlashAttribute("message", STR_MSG_UPDATE_SUCCESS);

        return "redirect:/controller/hierarchy-type/update/" + hierarchyType.getName();
    }
}
