package org.egov.infra.web.controller.admin.masters;

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
@RequestMapping(value ="view-role/{name}")
public class ViewRoleController  {
	
	private RoleService roleService;

	@Autowired
	public ViewRoleController(RoleService roleService) {
		this.roleService = roleService;
	}
	 @ModelAttribute
	 public Role roleModel(@PathVariable String name) {
	        return roleService.getRoleByName(name);
	    }
	
	@RequestMapping(method = RequestMethod.GET)
    public String viewRole() {
        return "role-view";
	}

  	
}
    
  