package org.egov.infra.web.controller.admin.masters.boundarytype;

import javax.validation.Valid;

import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.service.BoundaryTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/boundaryType/addChild/{name}")
public class AddChildBoundaryTypeController {

    private BoundaryTypeService boundaryTypeService;
    
    @Autowired
    public AddChildBoundaryTypeController(BoundaryTypeService boundaryTypeService){
        this.boundaryTypeService = boundaryTypeService;
    }
    
    @ModelAttribute
    public BoundaryType boundaryTypeModel(@PathVariable String name){
        BoundaryType child = new BoundaryType();
        BoundaryType parent = boundaryTypeService.getBoundaryTypeByName(name);
        child.setHierarchyType(parent.getHierarchyType());
        child.setParent(parent);
        return child;
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public String addChildForm(){
        return "boundaryType-addChild";
    }
    
    @RequestMapping(method =RequestMethod.POST)
    public String addChildBoundaryType(@Valid @ModelAttribute BoundaryType boundaryType,final BindingResult errors, RedirectAttributes redirectAttrs){
            if (errors.hasErrors())
                return "boundaryType-addChild";
            
            final String parentBoundaryTypeName = boundaryType.getParent().getName();
            //If child already exists for the boundary type, then show message and do not allow to add
            BoundaryType boundaryType1 = boundaryTypeService.getBoundaryTypeByParent(parentBoundaryTypeName);
            if(boundaryType1!=null){
                redirectAttrs.addFlashAttribute("errorMessage", "Child boundary type already exists!");
                return "redirect:/controller/boundaryType/addChild/"+parentBoundaryTypeName;
            }
            else{
                Long childHierarchy = 0l;
                Long parentHierarchy = boundaryType.getParent().getHierarchy();
                if(!parentBoundaryTypeName.isEmpty() && parentBoundaryTypeName!=null ){
                    childHierarchy = ++parentHierarchy;
                }
                boundaryType.setHierarchy(childHierarchy);
                boundaryTypeService.createBoundaryType(boundaryType);
                redirectAttrs.addFlashAttribute("message", "Child Boundary Type added successfully!");
                return "redirect:/controller/boundaryType/addChild/";
            }
    }
}
