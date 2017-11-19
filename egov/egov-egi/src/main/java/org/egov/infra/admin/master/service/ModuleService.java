/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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

package org.egov.infra.admin.master.service;

import org.egov.infra.admin.common.entity.MenuLink;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.egov.infra.utils.StringUtils.EMPTY;

@Service
@Transactional(readOnly = true)
public class ModuleService {

    @Autowired
    private ModuleRepository moduleRepository;

    public Module getModuleByName(String moduleName) {
        return moduleRepository.findByName(moduleName);
    }

    public List<MenuLink> getMenuLinksForRoles(Set<Role> roles) {
        List<Object[]> elements = moduleRepository.fetchModulesForRoles(roles);
        List<MenuLink> menuLinks = new ArrayList<>();
        elements.forEach(element -> {
            MenuLink menuLink = new MenuLink();
            menuLink.setName(element[0] != null ? element[0].toString() : EMPTY);
            menuLink.setUrl(element[1] != null ? element[1].toString() : EMPTY);
            menuLink.setDisplayName(element[2] != null ? element[2].toString() : EMPTY);
            menuLink.setId(Long.valueOf(element[3] != null ? element[3].toString() : "0"));
            menuLinks.add(menuLink);
        });
        return menuLinks;
    }

    public List<MenuLink> getMenuLinksByParentModuleId(Long parentId, Long userId) {
        List<Object[]> elements = moduleRepository.fetchModulesByParentModuleId(parentId, userId);
        List<MenuLink> menuLinks = new ArrayList<>();
        elements.forEach(element -> {
            MenuLink menuLink = new MenuLink();
            menuLink.setId(Long.valueOf(element[0] != null ? element[0].toString() : "0"));
            menuLink.setName(element[1] != null ? element[1].toString() : EMPTY);
            menuLink.setUrl(element[2] != null ? element[2].toString() : EMPTY);
            menuLink.setEnabled(element[3] != null && element[3].toString().equals("A"));
            menuLink.setContextRoot(element[4] != null ? element[4].toString() : EMPTY);
            menuLinks.add(menuLink);
        });
        return menuLinks;
    }

    public List<MenuLink> getUserFavouritesMenuLinks(Long userId) {
        List<Object[]> elements = moduleRepository.fetchUserFavourateModules(userId);
        List<MenuLink> menuLinks = new ArrayList<>();
        elements.forEach(element -> {
            MenuLink menuLink = new MenuLink();
            menuLink.setId(Long.valueOf(element[0] != null ? element[0].toString() : "0"));
            menuLink.setName(element[1] != null ? element[1].toString() : EMPTY);
            menuLink.setUrl(element[2] != null && element[3] != null ? element[2].toString() + element[3] : EMPTY);
            menuLinks.add(menuLink);
        });
        return menuLinks;
    }

    public List<Module> getAllTopModules() {
        return moduleRepository.findByParentModuleIsNullAndEnabledTrueOrderByNameAsc();
    }

}
