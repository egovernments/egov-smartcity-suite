package org.egov.infra.web.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.entity.enums.UserType;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.web.support.ui.Menu;
import org.egov.infstr.commons.Module;
import org.egov.infstr.commons.dao.ModuleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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

    @RequestMapping(method = RequestMethod.GET)
    public String showHome(final HttpSession session, final ModelMap modelData) {
        final User user = securityUtils.getCurrentUser();
        if (user.getType().equals(UserType.EMPLOYEE))
            return prepareOfficialHomePage(user, session, modelData);
        else
            return "redirect:/../portal/home";
    }

    @RequestMapping(value="/add-favourite",method=RequestMethod.GET) 
    public @ResponseBody boolean addFavourite(@RequestParam Long actionId) {
        return true;
    }
    
    @RequestMapping(value="/remove-favourite",method=RequestMethod.GET) 
    public @ResponseBody boolean removeFavourite(@RequestParam Long actionId) {
        return true;
    }
    
    private String prepareOfficialHomePage(final User user, final HttpSession session, final ModelMap modelData) {
        final List<Module> modules = moduleDAO.getModuleInfoForRoleIds(user.getRoles());
        final List<Module> selfServices = getEmployeeSelfService(modules, user);
        final List<Module> favourites = moduleDAO.getUserFavourites(user.getId());
        modelData.addAttribute("menu", prepareApplicationMenu(modules, favourites, selfServices, user));
        modelData.addAttribute("selfServices", selfServices);
        modelData.addAttribute("favourites", favourites);
        modelData.addAttribute("cityLogo", session.getAttribute("citylogo"));
        modelData.addAttribute("cityName", session.getAttribute("cityname"));
        modelData.addAttribute("userName", user.getName() == null ? "Anonymous" : user.getName());
        return "home";
    }

    private List<Module> getEmployeeSelfService(final List<Module> modules, final User user) {
        final List<Module> selfServices = new ArrayList<Module>();
        final Iterator<Module> moduleIterator = modules.iterator();
        while (moduleIterator.hasNext()) {
            final Module module = moduleIterator.next();
            if (module.getModuleName().equals("EmployeeSelfService")) {
                moduleIterator.remove();
                selfServices.addAll(moduleDAO.getApplicationModuleByParentId(module.getId(), user.getId()));
            }
        }
        return selfServices;
    }

    private String prepareApplicationMenu(final List<Module> modules, List<Module> favourites, List<Module> selfServices, final User user) {
        final Menu menu = new Menu();
        menu.setId("menuID");
        menu.setTitle("Hi, " + user.getName());
        menu.setIcon("fa fa-reorder");
        menu.setItems(new LinkedList<Menu>());
        createApplicationMenu(modules, user, menu);
        createSelfServiceMenu(selfServices, menu);
        createFavouritesMenu(favourites, menu);
        
        return "[" + new GsonBuilder().create().toJson(menu) + "]";
    }

    private void createApplicationMenu(final List<Module> modules, final User user, final Menu menu) {
        final Menu applicationMenu = createSubmenu("apps", "Applications", "Applications", "#", "fa fa-th floatLeft", menu);
        modules.stream().forEach(
                module -> {
                    createSubmenuRoot(module, user, createSubmenu(String.valueOf(module.getId()), module.getModuleDescription(),
                            module.getModuleDescription(), "#", "", applicationMenu));
                });
    }

    private void createFavouritesMenu(List<Module> favourites, final Menu menu) {
        final Menu favouritesMenu = createSubmenu("favMenu", "Favourites", "Favourites", "#", "fa fa-briefcase floatLeft", menu);
        favourites.stream().forEach(
                favourite -> {
                    final Menu appLinks = new Menu();
                    appLinks.setId(favourite.getId().toString());
                    appLinks.setName(favourite.getModuleName());
                    appLinks.setLink("/" +favourite.getBaseUrl());
                    appLinks.setIcon("fa fa-times-circle remove-feedback");
                    favouritesMenu.getItems().add(appLinks);
                });
    }

    private void createSelfServiceMenu(List<Module> selfServices, final Menu menu) {
        final Menu selfServiceMenu = createSubmenu("ssMenu", "Self Service", "Self Service", "#", "fa fa-ellipsis-h floatLeft", menu);
        selfServices.stream().forEach(
                selfService -> {
                    final Menu appLinks = new Menu();
                    appLinks.setName(selfService.getModuleName());
                    appLinks.setLink("/" + selfService.getContextRoot() + selfService.getBaseUrl());
                    selfServiceMenu.getItems().add(appLinks);
                    
                });
    }

    private void createSubmenuRoot(final Module parentModule, final User user, final Menu submenu) {
        final List<Module> submodules = moduleDAO.getApplicationModuleByParentId(parentModule.getId(), user.getId());
        submodules.stream().forEach(submodule -> createApplicationLink(submodule, user, submenu));
    }

    private void createApplicationLink(final Module submodule, final User user, final Menu parent) {
        if (submodule.getIsEnabled()) {
            final Menu appLink = new Menu();
            appLink.setId(submodule.getId().toString());
            appLink.setIcon("fa fa-star floatLeft");
            appLink.setName(submodule.getModuleName());
            appLink.setLink("/" + submodule.getContextRoot() + submodule.getBaseUrl());
            parent.getItems().add(appLink);
        } else {
            createSubmenuRoot(submodule, user, createSubmenu(String.valueOf(submodule.getId()), submodule.getModuleName(),
                    submodule.getModuleName(), "#", "", parent));
        }
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
