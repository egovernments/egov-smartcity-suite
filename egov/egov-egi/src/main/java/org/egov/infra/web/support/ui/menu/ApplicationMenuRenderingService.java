/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */

package org.egov.infra.web.support.ui.menu;

import org.egov.infra.admin.common.entity.MenuLink;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.egov.infra.web.support.ui.menu.Menu.APP_MENU_ICON;
import static org.egov.infra.web.support.ui.menu.Menu.APP_MENU_MAIN_ICON;
import static org.egov.infra.web.support.ui.menu.Menu.FAV_MENU_ICON;
import static org.egov.infra.web.support.ui.menu.Menu.NAVIGATION_NONE;
import static org.egov.infra.web.support.ui.menu.Menu.SELFSERVICE_MENU_ICON;

@Service
@Transactional(readOnly = true)
public class ApplicationMenuRenderingService {

    public static final String APP_MENU_TITLE = "Applications";
    public static final String FAV_MENU_TITLE = "Favourites";
    public static final String SELF_SERVICE_MENU_TITLE = "Self Service";
    public static final String SELF_SERVICE_MODULE_NAME = "EmployeeSelfService";

    @Autowired
    private ModuleService moduleService;

    public Menu getApplicationMenuForUser(User user) {
        List<MenuLink> menuLinks = moduleService.getMenuLinksForRoles(user.getRoles());
        Menu menu = new Menu();
        menu.setId("menuID");
        menu.setTitle(SPACE);
        menu.setIcon(APP_MENU_MAIN_ICON);
        menu.setItems(new LinkedList<>());
        List<MenuLink> favourites = moduleService.getUserFavouritesMenuLinks(user.getId());
        createApplicationMenu(menuLinks, favourites, user, menu);
        List<MenuLink> essMenus = extractSelfServiceMenus(menuLinks, user);
        if (!essMenus.isEmpty())
            createSelfServiceMenu(essMenus, menu);
        createFavouritesMenu(favourites, menu);
        return menu;
    }

    private List<MenuLink> extractSelfServiceMenus(List<MenuLink> menuLinks, User user) {
        return menuLinks.parallelStream().filter(menuLink -> SELF_SERVICE_MODULE_NAME.equals(menuLink.getName())).findFirst()
                .map(menuLink -> moduleService.getMenuLinksByParentModuleId(menuLink.getId(), user.getId()))
                .orElse(Collections.emptyList());

    }

    private void createApplicationMenu(List<MenuLink> menuLinks, List<MenuLink> favourites, User user, Menu parentMenu) {
        Menu applicationRootMenu = Menu.MenuBuilder.aMenu()
                .withId("apps").withName(APP_MENU_TITLE)
                .withTitle(APP_MENU_TITLE).withLink(NAVIGATION_NONE)
                .withIcon(APP_MENU_ICON).withItems(new LinkedList<>()).build();
        Menu applicationMenu = Menu.MenuBuilder.aMenu().withTitle(APP_MENU_TITLE)
                .withIcon(EMPTY).withItems(new LinkedList<>()).build();
        applicationRootMenu.getItems().add(applicationMenu);
        parentMenu.getItems().add(applicationRootMenu);
        menuLinks.stream().filter(menuLink -> !SELF_SERVICE_MODULE_NAME.equals(menuLink.getName())).forEach(menuLink ->
                createSubmenuRoot(menuLink.getId(), menuLink.getDisplayName(), favourites, user, applicationMenu)
        );
    }

    private void createSelfServiceMenu(List<MenuLink> selfServices, Menu parentMenu) {
        Menu selfServiceRootMenu = Menu.MenuBuilder.aMenu()
                .withId("ssMenu").withName(SELF_SERVICE_MENU_TITLE)
                .withTitle(SELF_SERVICE_MENU_TITLE).withLink(NAVIGATION_NONE)
                .withIcon(SELFSERVICE_MENU_ICON).withItems(new LinkedList<>()).build();
        Menu selfServiceMenu = Menu.MenuBuilder.aMenu().withTitle(SELF_SERVICE_MENU_TITLE)
                .withIcon(EMPTY).withItems(new LinkedList<>()).build();
        selfServiceRootMenu.getItems().add(selfServiceMenu);
        parentMenu.getItems().add(selfServiceRootMenu);
        selfServices.stream().forEach(selfService -> {
            Menu appLinks = new Menu();
            appLinks.setName(selfService.getName());
            appLinks.setLink("/" + selfService.getContextRoot() + selfService.getUrl());
            selfServiceMenu.getItems().add(appLinks);

        });
    }

    private void createFavouritesMenu(List<MenuLink> favourites, Menu parentMenu) {
        Menu favouritesRootMenu = Menu.MenuBuilder.aMenu()
                .withId("favMenu").withName(FAV_MENU_TITLE)
                .withTitle(FAV_MENU_TITLE).withLink(NAVIGATION_NONE)
                .withIcon(FAV_MENU_ICON).withItems(new LinkedList<>()).build();
        Menu favouritesMenu = Menu.MenuBuilder.aMenu().withTitle(FAV_MENU_TITLE)
                .withIcon(EMPTY).withItems(new LinkedList<>()).build();
        favouritesRootMenu.getItems().add(favouritesMenu);
        parentMenu.getItems().add(favouritesRootMenu);
        favourites.stream().forEach(favourite -> {
            Menu appLink = Menu.MenuBuilder.aMenu()
                    .withId("fav-" + favourite.getId()).withName(favourite.getName())
                    .withLink("/" + favourite.getUrl()).withIcon("fa fa-times-circle remove-favourite").build();
            favouritesMenu.getItems().add(appLink);
        });
    }

    private void createSubmenuRoot(Long menuId, String menuName, List<MenuLink> favourites, User user, Menu parentMenu) {
        List<MenuLink> submodules = moduleService.getMenuLinksByParentModuleId(menuId, user.getId());
        if (!submodules.isEmpty()) {
            Menu submenuRoot = Menu.MenuBuilder.aMenu()
                    .withId(String.valueOf(menuId)).withName(menuName)
                    .withTitle(menuName).withLink(NAVIGATION_NONE)
                    .withIcon(EMPTY).withItems(new LinkedList<>()).build();
            Menu submenu = Menu.MenuBuilder.aMenu().withTitle(menuName).withIcon(EMPTY).withItems(new LinkedList<>()).build();
            submenuRoot.getItems().add(submenu);
            parentMenu.getItems().add(submenuRoot);
            submodules.stream().forEach(submodule -> createApplicationLink(submodule, favourites, user, submenu));
        }
    }

    private void createApplicationLink(MenuLink childMenuLink, List<MenuLink> favourites, User user, Menu parentMenu) {
        if (childMenuLink.isEnabled()) {
            Menu appLink = Menu.MenuBuilder.aMenu()
                    .withId("id-" + childMenuLink.getId()).withName(childMenuLink.getName())
                    .withLink("/" + childMenuLink.getContextRoot() + childMenuLink.getUrl())
                    .withIcon(FAV_MENU_ICON + (favourites.contains(childMenuLink) ? " added-as-fav" : " add-to-favourites"))
                    .build();
            parentMenu.getItems().add(appLink);
        } else {
            createSubmenuRoot(childMenuLink.getId(), childMenuLink.getName(), favourites, user, parentMenu);
        }
    }
}
