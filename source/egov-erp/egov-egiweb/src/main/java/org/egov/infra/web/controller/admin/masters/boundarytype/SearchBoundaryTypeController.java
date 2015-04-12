package org.egov.infra.web.controller.admin.masters.boundarytype;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.egov.infra.admin.master.entity.BoundaryType;
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
public class SearchBoundaryTypeController {

        private HierarchyTypeService hierarchyTypeService;
	private BoundaryTypeService boundaryTypeService;
	
	@Autowired
	public SearchBoundaryTypeController(BoundaryTypeService boundaryTypeService,HierarchyTypeService hierarchyTypeService){
		this.boundaryTypeService = boundaryTypeService;
		this.hierarchyTypeService = hierarchyTypeService;
	}
	
	@ModelAttribute
	public BoundaryType boundaryTypeModel(){
		return new BoundaryType();
	}
	
	@RequestMapping(value = "/boundaryType/view",method = RequestMethod.GET)
	public String viewSearch(Model model){
	    model.addAttribute("hierarchyTypes", hierarchyTypeService.getAllHierarchyTypes());
	    model.addAttribute("mode", "view");
	    return "boundaryType-search";
	}
	
	@RequestMapping(value = "/boundaryType/update",method = RequestMethod.GET)
        public String updateSearch(Model model){
	    model.addAttribute("hierarchyTypes", hierarchyTypeService.getAllHierarchyTypes());
	    model.addAttribute("mode", "update");
            return "boundaryType-search";
        }
	
	@RequestMapping(value = "/boundaryType/addChild",method = RequestMethod.GET)
        public String addChildSearch(Model model){
	    model.addAttribute("hierarchyTypes", hierarchyTypeService.getAllHierarchyTypes());
	    model.addAttribute("mode", "addChild");
            return "boundaryType-search";
        }
	
	@RequestMapping(value = { "/boundaryType/view", "/boundaryType/update", "/boundaryType/addChild" }, method =RequestMethod.POST)
	public String search(@ModelAttribute BoundaryType boundaryType,final BindingResult errors, RedirectAttributes redirectAttrs,HttpServletRequest request){
		
    	    if (errors.hasErrors())
                return "boundaryType-form";
    		
    	    String[] uriSplits = request.getRequestURI().split("/");
    	    String[] idSplit = boundaryType.getName().split(",");
    	    Long boundaryId = Long.valueOf(idSplit[idSplit.length - 1]);
            String redirectURI =  uriSplits[uriSplits.length - 1] + "/" + boundaryId;
	    return "redirect:"+redirectURI;
	}
}
