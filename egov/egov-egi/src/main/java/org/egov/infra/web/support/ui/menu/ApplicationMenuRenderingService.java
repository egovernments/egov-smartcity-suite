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
import static org.egov.infra.web.support.ui.menu.Menu.APP_MENU_TITLE;
import static org.egov.infra.web.support.ui.menu.Menu.FAV_MENU_ICON;
import static org.egov.infra.web.support.ui.menu.Menu.FAV_MENU_TITLE;
import static org.egov.infra.web.support.ui.menu.Menu.NAVIGATION_NONE;
import static org.egov.infra.web.support.ui.menu.Menu.SELFSERVICE_MENU_ICON;
import static org.egov.infra.web.support.ui.menu.Menu.SELFSERVICE_MENU_TITLE;
import static org.egov.infra.web.support.ui.menu.Menu.SELFSERVICE_MODULE;

@Service
@Transactional(readOnly = true)
public class ApplicationMenuRenderingService {

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
        return menuLinks.parallelStream().filter(menuLink -> SELFSERVICE_MODULE.equals(menuLink.getName())).findFirst()
                .map(menuLink -> moduleService.getMenuLinksByParentModuleId(menuLink.getId(), user.getId()))
                .orElse(Collections.emptyList());

    }

    private void createApplicationMenu(List<MenuLink> menuLinks, List<MenuLink> favourites, User user, Menu menu) {
        Menu applicationMenu = createSubmenu("apps", APP_MENU_TITLE, APP_MENU_TITLE, NAVIGATION_NONE, APP_MENU_ICON, menu);
        menuLinks.stream().filter(menuLink -> !SELFSERVICE_MODULE.equals(menuLink.getName())).forEach(menuLink ->
                createSubmenuRoot(menuLink.getId(), menuLink.getDisplayName(), favourites, user, applicationMenu)
        );
    }

    private void createSelfServiceMenu(List<MenuLink> selfServices, Menu menu) {
        Menu selfServiceMenu = createSubmenu("ssMenu", SELFSERVICE_MENU_TITLE, SELFSERVICE_MENU_TITLE, NAVIGATION_NONE,
                SELFSERVICE_MENU_ICON, menu);
        selfServices.stream().forEach(selfService -> {
            Menu appLinks = new Menu();
            appLinks.setName(selfService.getName());
            appLinks.setLink("/" + selfService.getContextRoot() + selfService.getUrl());
            selfServiceMenu.getItems().add(appLinks);

        });
    }

    private void createFavouritesMenu(List<MenuLink> favourites, Menu menu) {
        Menu favouritesMenu = createSubmenu("favMenu", FAV_MENU_TITLE, FAV_MENU_TITLE, NAVIGATION_NONE, FAV_MENU_ICON, menu);
        favourites.stream().forEach(favourite -> {
            Menu appLinks = new Menu();
            appLinks.setId("fav-" + favourite.getId());
            appLinks.setName(favourite.getName());
            appLinks.setLink("/" + favourite.getUrl());
            appLinks.setIcon("fa fa-times-circle remove-favourite");
            favouritesMenu.getItems().add(appLinks);
        });
    }

    private void createSubmenuRoot(Long menuId, String menuName, List<MenuLink> favourites, User user, Menu parentMenu) {
        List<MenuLink> submodules = moduleService.getMenuLinksByParentModuleId(menuId, user.getId());
        if (!submodules.isEmpty()) {
            Menu submenu = createSubmenu(String.valueOf(menuId), menuName,
                    menuName, NAVIGATION_NONE, EMPTY, parentMenu);
            submodules.stream().forEach(submodule -> createApplicationLink(submodule, favourites, user, submenu));
        }
    }

    private Menu createSubmenu(String id, String name, String title, String link, String icon, Menu parent) {
        Menu submenuItem = new Menu();
        submenuItem.setId(id);
        submenuItem.setName(name);
        submenuItem.setIcon(icon);
        submenuItem.setLink(link);
        submenuItem.setItems(new LinkedList<>());
        parent.getItems().add(submenuItem);

        Menu submenu = new Menu();
        submenu.setTitle(title);
        submenu.setIcon(EMPTY);
        submenu.setItems(new LinkedList<>());
        submenuItem.getItems().add(submenu);

        return submenu;
    }

    private void createApplicationLink(MenuLink childMenuLink, List<MenuLink> favourites, User user, Menu parentMenu) {
        if (childMenuLink.isEnabled()) {
            Menu appLink = new Menu();
            appLink.setId("id-" + childMenuLink.getId().toString());
            appLink.setIcon(FAV_MENU_ICON + (favourites.contains(childMenuLink) ? " added-as-fav" : " add-to-favourites"));
            appLink.setName(childMenuLink.getName());
            appLink.setLink("/" + childMenuLink.getContextRoot() + childMenuLink.getUrl());
            parentMenu.getItems().add(appLink);
        } else {
            createSubmenuRoot(childMenuLink.getId(), childMenuLink.getName(), favourites, user, parentMenu);
        }
    }
}
