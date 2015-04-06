package org.egov.egi.web.controller.admin.masters;

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
@RequestMapping("/boundaryType/update/{name}")
public class UpdateBoundaryTypeController {

private BoundaryTypeService boundaryTypeService;
	
	@Autowired
	public UpdateBoundaryTypeController(BoundaryTypeService boundaryTypeService){
		this.boundaryTypeService = boundaryTypeService;
	}
	
	@ModelAttribute
	public BoundaryType boundaryTypeModel(@PathVariable String name){
		return boundaryTypeService.getBoundaryTypeByName(name);
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public String updateBoundaryTypeForm(){
		return "boundaryType-update";
	}
	
	@RequestMapping(method =RequestMethod.POST)
	public String updateBoundaryType(@Valid @ModelAttribute BoundaryType boundaryType,final BindingResult errors, RedirectAttributes redirectAttrs){
		if (errors.hasErrors())
            return "boundaryType-update";
		
		boundaryTypeService.updateBoundaryType(boundaryType);
		redirectAttrs.addFlashAttribute("message", "Boundary type updated successfully!");
		return "redirect:/controller/boundaryType/update/"+boundaryType.getName();
	}
}
