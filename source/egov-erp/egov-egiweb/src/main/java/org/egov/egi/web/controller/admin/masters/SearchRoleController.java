package org.egov.egi.web.controller.admin.masters;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
@Controller
public class SearchRoleController  {
	private RoleService roleService;
	
	
	@Autowired
	 public SearchRoleController(RoleService roleService) {
		this.roleService = roleService;
	}

	@ModelAttribute
	 public Role roleModel() {
	        return new Role();
	    }
	
	
	@ModelAttribute("roles")
     public List<Role> roles() {
		return roleService.getAllRoles();
	 }
	  
	  
	@RequestMapping(value ="view-role",method = RequestMethod.GET)
	public String viewSearch(Model model) {
		
        return "role-search";
        
	    }
	

	
	@RequestMapping(value ="update-role",method = RequestMethod.GET)
	public String updateSearch(Model model) {
		
        return "role-search";
        
	}
	
	  @RequestMapping(method =RequestMethod.POST)
	    public String search(@ModelAttribute Role role, final BindingResult errors, RedirectAttributes redirectAttrs,HttpServletRequest request) {
	    	
	        if (errors.hasErrors())
	            return "role-form";
	  
	        return "redirect:"+request.getRequestURI().split("/")[request.getRequestURI().split("/").length-1]+"/"+role.getName();
	    }
	  
	
}
    
  