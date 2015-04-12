package org.egov.infra.web.controller.admin.masters.boundarytype;

import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.service.BoundaryTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value ="/boundaryType/view/{id}")
public class ViewBoundaryTypeController {

	private BoundaryTypeService boundaryTypeService;
	
	@Autowired
	public ViewBoundaryTypeController(BoundaryTypeService boundaryTypeService){
		this.boundaryTypeService = boundaryTypeService;
	}
	
	@ModelAttribute
        public BoundaryType boundaryTypeModel(@PathVariable Long id){
            return boundaryTypeService.getBoundaryTypeById(id);
        }
	
	@RequestMapping(method = RequestMethod.GET)
	public String viewBoundaryType(){
		return "boundaryType-view";
	}
}
