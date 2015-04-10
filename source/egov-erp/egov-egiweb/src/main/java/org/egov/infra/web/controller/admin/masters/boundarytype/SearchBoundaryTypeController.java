package org.egov.infra.web.controller.admin.masters.boundarytype;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.service.BoundaryTypeService;
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

	private BoundaryTypeService boundaryTypeService;
	
	@Autowired
	public SearchBoundaryTypeController(BoundaryTypeService boundaryTypeService){
		this.boundaryTypeService = boundaryTypeService;
	}
	
	@ModelAttribute
	public BoundaryType boundaryTypeModel(){
		return new BoundaryType();
	}
	
	@RequestMapping(value = "/boundaryType/view",method = RequestMethod.GET)
	public String viewSearch(Model model){
	    model.addAttribute("boundaryTypes", boundaryTypeService.getAllBoundaryTypes());
	    model.addAttribute("mode", "view");
	    return "boundaryType-search";
	}
	
	@RequestMapping(value = "/boundaryType/update",method = RequestMethod.GET)
        public String updateSearch(Model model){
	    model.addAttribute("boundaryTypes", boundaryTypeService.getAllBoundaryTypes());
	    model.addAttribute("mode", "update");
            return "boundaryType-search";
        }
	
	@RequestMapping(value = "/boundaryType/addChild",method = RequestMethod.GET)
        public String addChildSearch(Model model){
	    model.addAttribute("boundaryTypes", boundaryTypeService.getAllBoundaryTypes());
	    model.addAttribute("mode", "addChild");
            return "boundaryType-search";
        }
	
	@RequestMapping(value = { "/boundaryType/view", "/boundaryType/update", "/boundaryType/addChild" }, method =RequestMethod.POST)
	public String search(@ModelAttribute BoundaryType boundaryType,final BindingResult errors, RedirectAttributes redirectAttrs,HttpServletRequest request){
		
    	    if (errors.hasErrors())
                return "boundaryType-form";
    		
    	    String[] uriSplits = request.getRequestURI().split("/");
            String redirectURI =  uriSplits[uriSplits.length - 1] + "/" + boundaryType.getName();
	    return "redirect:"+redirectURI;
	}
}
