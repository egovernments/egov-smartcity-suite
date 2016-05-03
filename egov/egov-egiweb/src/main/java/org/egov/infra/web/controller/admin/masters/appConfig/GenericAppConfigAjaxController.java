/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 */

package org.egov.infra.web.controller.admin.masters.appConfig;

import org.egov.infra.admin.master.entity.AppConfig;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.service.AppConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Repository
@RequestMapping(value = "/appConfig")
public class GenericAppConfigAjaxController {

    private final AppConfigService appConfigValueService;

    @Autowired
    public GenericAppConfigAjaxController(final AppConfigService appConfigValueService) {
        this.appConfigValueService = appConfigValueService;
    }

    @RequestMapping(value = { "/modules" }, method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<Module> getAllModulesByNameLike(@RequestParam final String moduleName,
            final HttpServletResponse response) throws IOException {
        final String likemoduleName = "%" + moduleName + "%";
        return appConfigValueService.findByNameContainingIgnoreCase(likemoduleName);
    }

    @RequestMapping(value = "/ajax-appConfigpopulate", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<AppConfig> getAppConfigs(
            @ModelAttribute("appConfig") @RequestParam final Long appModuleName) {
        final List<AppConfig> appConfig = appConfigValueService.findAllByModule(appModuleName);
        // FIXME this is hack for lazy loaded collection
        appConfig.forEach(appConfigs -> appConfigs.toString());
        return appConfig;
    }

}
