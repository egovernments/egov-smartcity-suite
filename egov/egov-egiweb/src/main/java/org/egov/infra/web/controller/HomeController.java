/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It
	   is required that all modified versions of this material be marked in
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program
	   with regards to rights under trademark law for use of the trade names
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.infra.web.controller;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.egov.infra.admin.common.entity.Favourites;
import org.egov.infra.admin.common.service.FavouritesService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.entity.enums.UserType;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.web.support.ui.Menu;
import org.egov.infstr.commons.Module;
import org.egov.infstr.commons.dao.ModuleDao;
import org.egov.infstr.security.utils.CryptoHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.GsonBuilder;

@Controller
@RequestMapping(value = "/home")
public class HomeController {

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private ModuleDao moduleDAO;

    @Autowired
    private FavouritesService favouritesService;

    @Autowired
    private UserService userService;

    @RequestMapping
    public String showHome(final HttpSession session, final ModelMap modelData) {
        final User user = securityUtils.getCurrentUser();
        if (user.getType().equals(UserType.EMPLOYEE))
            return prepareOfficialHomePage(user, session, modelData);
        else
            return "redirect:/../portal/home";
    }

    @RequestMapping(value = "favourite/add", method = RequestMethod.POST)
    public @ResponseBody boolean addFavourite(@ModelAttribute final Favourites favourites) {
    	favouritesService.addToCurrentUserFavourite(favourites);
    	return favourites.getId() != null;
    }

    @RequestMapping(value = "favourite/remove")
    public @ResponseBody boolean removeFavourite(@RequestParam final Integer actionId) {
        return favouritesService.removeFromCurrentUserFavourite(actionId);
    }

    @RequestMapping(value = "password/update")
    public @ResponseBody String changePassword(@RequestParam final String currentPwd, @RequestParam final String newPwd, 
    		@RequestParam final String retypeNewPwd) {
    	final User user = securityUtils.getCurrentUser();
    	final String currentRawPwd = CryptoHelper.decrypt(user.getPassword());
    	if (currentRawPwd.equals(currentPwd)) {
    		if (newPwd.equals(retypeNewPwd)) {
    			user.setPassword(CryptoHelper.encrypt(newPwd));
    			//TODO Set next password expiry date
    			//user.setPwdExpiryDate(new DateTime().toDate());
    			userService.updateUser(user);
    			return "SUCCESS";
    		}
    		return "NEWPWD_UNMATCH";
    	} 
    	return "CURRPWD_UNMATCH";
    }
    
    @RequestMapping(value = "feedback/sent")
    public @ResponseBody boolean sendFeedback(@RequestParam final String subject, @RequestParam final String message) {
    	//TODO
    	return false;
    }
    
    @ModelAttribute("user")
    public User user() {
        return securityUtils.getCurrentUser();
    }

    @RequestMapping(value = "profile/edit", method = RequestMethod.GET)
    public String editProfile() {
        return "profile-edit";
    }

    @RequestMapping(value = "profile/edit", method = RequestMethod.POST)
    public String saveProfile(@Valid @ModelAttribute final User user, final BindingResult binder) {
        if (binder.hasErrors())
            return "profile-edit";
        userService.updateUser(user);
        return "profile-edit";
    }

    private String prepareOfficialHomePage(final User user, final HttpSession session, final ModelMap modelData) {
        modelData.addAttribute("menu", prepareApplicationMenu(moduleDAO.getModuleInfoForRoleIds(user.getRoles()), user));
        modelData.addAttribute("cityLogo", session.getAttribute("citylogo"));
        modelData.addAttribute("cityName", session.getAttribute("cityname"));
        modelData.addAttribute("userName", user.getName() == null ? "Anonymous" : user.getName());
        return "home";
    }

    private List<Module> getEmployeeSelfService(final List<Module> modules, final User user) {
        return modules.parallelStream().filter(module -> module.getModuleName().equals("EmployeeSelfService"))
                .findFirst().map(module -> moduleDAO.getApplicationModuleByParentId(module.getId(), user.getId()))
                .orElse(Collections.emptyList());

    }

    private String prepareApplicationMenu(final List<Module> modules, final User user) {
        final Menu menu = new Menu();
        menu.setId("menuID");
        menu.setTitle("Hi, " + user.getName());
        menu.setIcon("fa fa-reorder");
        menu.setItems(new LinkedList<Menu>());
        final List<Module> favourites = moduleDAO.getUserFavourites(user.getId());
        createApplicationMenu(modules, favourites, user, menu);
        createSelfServiceMenu(getEmployeeSelfService(modules, user), menu);
        createFavouritesMenu(favourites, menu);

        return "[" + new GsonBuilder().create().toJson(menu) + "]";
    }

    private void createApplicationMenu(final List<Module> modules, final List<Module> favourites, final User user,
            final Menu menu) {
        final Menu applicationMenu = createSubmenu("apps", "Applications", "Applications", "javascript:void(0);", "fa fa-th floatLeft",
                menu);
        modules.stream()
                .filter(module -> !module.getModuleName().equals("EmployeeSelfService"))
                .forEach(
                        module -> {
                            createSubmenuRoot(
                                    module,
                                    favourites,
                                    user,
                                    createSubmenu(String.valueOf(module.getId()), module.getModuleDescription(),
                                            module.getModuleDescription(), "javascript:void(0);", "", applicationMenu));
                        });
    }

    private void createFavouritesMenu(final List<Module> favourites, final Menu menu) {
        final Menu favouritesMenu = createSubmenu("favMenu", "Favourites", "Favourites", "javascript:void(0);",
                "fa fa-briefcase floatLeft", menu);
        favourites.stream().forEach(favourite -> {
            final Menu appLinks = new Menu();
            appLinks.setId("fav-"+favourite.getId());
            appLinks.setName(favourite.getModuleName());
            appLinks.setLink("/" + favourite.getBaseUrl());
            appLinks.setIcon("fa fa-times-circle remove-favourite");
            favouritesMenu.getItems().add(appLinks);
        });
    }

    private void createSelfServiceMenu(final List<Module> selfServices, final Menu menu) {
        final Menu selfServiceMenu = createSubmenu("ssMenu", "Self Service", "Self Service", "javascript:void(0);",
                "fa fa-ellipsis-h floatLeft", menu);
        selfServices.stream().forEach(selfService -> {
            final Menu appLinks = new Menu();
            appLinks.setName(selfService.getModuleName());
            appLinks.setLink("/" + selfService.getContextRoot() + selfService.getBaseUrl());
            selfServiceMenu.getItems().add(appLinks);

        });
    }

    private void createSubmenuRoot(final Module parentModule, final List<Module> favourites, final User user,
            final Menu submenu) {
        final List<Module> submodules = moduleDAO.getApplicationModuleByParentId(parentModule.getId(), user.getId());
        submodules.stream().forEach(submodule -> createApplicationLink(submodule, favourites, user, submenu));
    }

    private void createApplicationLink(final Module submodule, final List<Module> favourites, final User user,
            final Menu parent) {
        if (submodule.getIsEnabled()) {
            final Menu appLink = new Menu();
            appLink.setId(submodule.getId().toString());
            appLink.setIcon("fa fa-star floatLeft "
                    + (favourites.contains(submodule) ? "added-as-fav" : "add-to-favourites"));
            appLink.setName(submodule.getModuleName());
            appLink.setLink("/" + submodule.getContextRoot() + submodule.getBaseUrl());
            parent.getItems().add(appLink);
        } else
            createSubmenuRoot(
                    submodule,
                    favourites,
                    user,
                    createSubmenu(String.valueOf(submodule.getId()), submodule.getModuleName(),
                            submodule.getModuleName(), "javascript:void(0);", "", parent));
    }

    private Menu createSubmenu(final String id, final String name, final String title, final String link,
            final String icon, final Menu parent) {
        final Menu submenuItem = new Menu();
        submenuItem.setId(id);
        submenuItem.setName(name);
        submenuItem.setIcon(icon);
        submenuItem.setLink(link);
        submenuItem.setItems(new LinkedList<Menu>());
        parent.getItems().add(submenuItem);

        final Menu submenu = new Menu();
        submenu.setTitle(title);
        submenu.setIcon("");
        submenu.setItems(new LinkedList<Menu>());
        submenuItem.getItems().add(submenu);

        return submenu;
    }

}

