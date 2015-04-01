package org.egov.egi.web.controller.admin.masters;

import org.egov.infra.admin.master.entity.HierarchyType;
import org.egov.infra.admin.master.service.HierarchyTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/hierarchy-type/view/{typeName}")
public class ViewHierarchyTypeController {

    private HierarchyTypeService hierarchyTypeService;
    
    @Autowired
    public ViewHierarchyTypeController(HierarchyTypeService hierarchyTypeService) {
        this.hierarchyTypeService = hierarchyTypeService;
    }
    
    @ModelAttribute
    public HierarchyType hierarchyTypeModel(@PathVariable String  typeName) {
           return hierarchyTypeService.getHierarchyTypeByName(typeName);
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public String hierarchyTypeViewForm() {
        return "hierarchyType-view";
    }
}
