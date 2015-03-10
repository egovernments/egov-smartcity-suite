package org.egov.egi.web.controller.admin.masters;

import javax.validation.Valid;

import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value="update-role/{name}")
public class UpdateRoleController  {
	
	private RoleService roleService;

	
	@Autowired
	public UpdateRoleController(RoleService roleService) {
		this.roleService = roleService;
	}
	
	
	@ModelAttribute
	public Role roleModel(@PathVariable String name) {
	
		return roleService.getRoleByName(name);
	   
	}
	
	
	
	@RequestMapping(method = RequestMethod.GET)
    public String updateRoleForm() {
        return "role-update";
	}
	
	
	
	@RequestMapping(method =RequestMethod.POST)
    public String update(@Valid @ModelAttribute Role role, final BindingResult errors, RedirectAttributes redirectAttrs) {
        if (errors.hasErrors())
            return "update-role/"+role.getName();
        
        roleService.update(role);
		redirectAttrs.addFlashAttribute("message", "Successfully Updated Role !");
		
        return "redirect:/controller/update-role";
    }
    
  	
}
    
  