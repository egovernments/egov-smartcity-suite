package org.egov.infra.web.controller.admin.masters.role;

import java.util.List;

import javax.validation.Valid;

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
@RequestMapping("/role")
public class SearchRoleController {
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

    @RequestMapping(value = "/viewsearch", method = RequestMethod.GET)
    public String viewSearch(Model model) {

        return "role-viewsearch";

    }
    @RequestMapping(value = "/updatesearch", method = RequestMethod.GET)
    public String updateSearch(Model model) {

        return "role-updatesearch";

    }

    @RequestMapping(value = "/view", method = RequestMethod.POST)
    public String viewRole(@Valid @ModelAttribute Role role, final BindingResult errors, RedirectAttributes redirectAttrs) {

        return "redirect:/role/view/"+role.getName();

    }
    
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String updateSearch(@Valid @ModelAttribute Role role, final BindingResult errors, RedirectAttributes redirectAttrs) {

        return "redirect:/role/update/"+role.getName();
    }

}
