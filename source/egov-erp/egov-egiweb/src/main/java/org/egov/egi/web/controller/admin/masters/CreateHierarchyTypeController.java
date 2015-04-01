package org.egov.egi.web.controller.admin.masters;

import javax.validation.Valid;

import org.egov.infra.admin.master.entity.HierarchyType;
import org.egov.infra.admin.master.service.HierarchyTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/hierarchy-type/create")
public class CreateHierarchyTypeController {

    private HierarchyTypeService hierarchyTypeService;

    @Autowired
    public CreateHierarchyTypeController(HierarchyTypeService hierarchyTypeService) {
        this.hierarchyTypeService = hierarchyTypeService;
    }

    @ModelAttribute
    public HierarchyType hierarchyTypeModel() {
        return new HierarchyType();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String hierarchyTypeForm(Model model) {
        return "hierarchyType-form";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String createHierarchyType(@ModelAttribute @Valid HierarchyType hierarchyType, BindingResult errors,
            RedirectAttributes additionalAttr) {

        if (errors.hasErrors()) {
            return "hierarchyType-form";
        }

        hierarchyTypeService.createHierarchyType(hierarchyType);
        additionalAttr.addFlashAttribute("message", "Successfully created Hierarchy Type !");
       // throw new ConstraintViolationException();
        return "redirect:/controller/hierarchy-type/create";
    }
}
