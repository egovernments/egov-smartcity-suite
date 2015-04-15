package org.egov.infra.web.controller.admin.masters.role;


import javax.validation.Valid;

import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
@Controller
@RequestMapping(value ="/role/create")
public class CreateRoleController  {
	private RoleService roleService;

	@Autowired
	 public CreateRoleController(RoleService roleService) {
		this.roleService = roleService;
	}

	@ModelAttribute
	 public Role roleModel() {
	        return new Role();
	    }
	
	@RequestMapping(method = RequestMethod.GET)
	public String newForm() {
	        return "role-form";
	    }
	
	
    
    @RequestMapping(method =RequestMethod.POST)
    public String create(@Valid @ModelAttribute Role role, final BindingResult errors, RedirectAttributes redirectAttrs) {
    	
        String SUCCESS = "";
        if (errors.hasErrors())
            return "role-form";
        
        
           roleService.createRole(role);
	   redirectAttrs.addFlashAttribute("message", "Successfully created Role !");
	   SUCCESS = "redirect:/role/view/"+role.getName();
        
        return SUCCESS;
    }
    
}
    
  