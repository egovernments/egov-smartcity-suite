package org.egov.infra.web.controller.admin.masters;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
@RequestMapping(value = { "/hierarchy-type/view", "hierarchy-type/update" })
public class SearchHierarchyTypeController {

    private HierarchyTypeService hierarchyTypeService;

    @Autowired
    public SearchHierarchyTypeController(HierarchyTypeService hierarchyTypeService) {
        this.hierarchyTypeService = hierarchyTypeService;
    }
    
    @ModelAttribute
    public HierarchyType hierarchyTypeModel() {
           return new HierarchyType();
    }

    @ModelAttribute(value = "hierarchyTypes")
    public List<HierarchyType> listHierarchyTypes() {
        return hierarchyTypeService.getAllHierarchyTypes();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showHierarchyTypes(Model model) {
        return "hierarchyType-list";
    }
}
 