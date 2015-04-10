package org.egov.infra.web.controller.admin.masters.boundarytype;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.entity.HierarchyType;
import org.egov.infra.admin.master.service.BoundaryTypeService;
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
@RequestMapping(value ="/create-boundaryType")
public class CreateBoundaryTypeController {

	private HierarchyTypeService hierarchyTypeService;
	private BoundaryTypeService boundaryTypeService;
	
	@Autowired
	public CreateBoundaryTypeController(BoundaryTypeService boundaryTypeService,HierarchyTypeService hierarchyTypeService) {
		this.boundaryTypeService = boundaryTypeService;
		this.hierarchyTypeService = hierarchyTypeService;
	}
	
	@ModelAttribute
	 public BoundaryType boundaryTypeModel() {
	        return new BoundaryType();
	 }
	
	@RequestMapping(method = RequestMethod.GET)
	public String newForm() {
	    return "boundaryType-form";
	}
	
	@ModelAttribute("hierarchyTypes")
	public List<HierarchyType> getHierarchyTypes(){
		final List<HierarchyType> heirarchyList = new ArrayList<HierarchyType>();
		List<HierarchyType> hierarchyTypeList = hierarchyTypeService.getAllHierarchyTypes();
		for (final HierarchyType hierarchyType : hierarchyTypeList) {
			BoundaryType bType = boundaryTypeService.getBoundaryTypeByHierarchyTypeNameAndLevel(hierarchyType.getName(),Integer.valueOf(1));
			if(bType == null){
				heirarchyList.add(hierarchyType);
			}
		}
		return heirarchyList;
	}
	
	@RequestMapping(method =RequestMethod.POST)
	public String create(@Valid @ModelAttribute BoundaryType boundaryType, final BindingResult errors, RedirectAttributes redirectAttrs) {
    	
        if (errors.hasErrors())
            return "boundaryType-form";
        
        boundaryType.setHierarchy(1l);
        boundaryTypeService.createBoundaryType(boundaryType);
        redirectAttrs.addFlashAttribute("message", "Boundary Type created successfully !");

        return "redirect:create-boundaryType";
    }
	
}
