/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.infra.web.controller.admin.masters.config;

import org.egov.infra.admin.master.entity.AppConfig;
import org.egov.infra.admin.master.service.AppConfigService;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.web.controller.admin.masters.config.adaptor.AppConfigJsonAdaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static org.egov.infra.utils.JsonUtils.toJSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping("/app/config")
public class ViewAppConfigController {

    private AppConfigService appConfigService;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    public ViewAppConfigController(AppConfigService appConfigService) {
        this.appConfigService = appConfigService;
    }

    @RequestMapping(value = "/formodule/{moduleName}", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<AppConfig> getAppConfigsForModule(@PathVariable String moduleName) {
        return appConfigService.getAllAppConfigByModuleName(moduleName);
    }

    @RequestMapping(value = "/view", method = GET)
    public String viewAppConfig(Model model) {
        model.addAttribute("modules", moduleService.getAllTopModules());
        return "app-config-view";
    }

    @RequestMapping(value = "/list", method = GET, produces = TEXT_PLAIN_VALUE)
    @ResponseBody
    public String showAppConfigs(@RequestParam(required = false) String moduleName,
                                           @RequestParam Integer start,
                                           @RequestParam Integer length) {
        int pageNumber = start / length + 1;
        final Page<AppConfig> pagedAppConfigs = appConfigService.getAllAppConfig(moduleName, pageNumber, length == -1 ? Integer.MAX_VALUE : length);
        final StringBuilder appConfigJSONData = new StringBuilder();
        appConfigJSONData.append("{\"draw\": ").append("0");
        appConfigJSONData.append(",\"recordsTotal\":").append(pagedAppConfigs.getTotalElements());
        appConfigJSONData.append(",\"totalDisplayRecords\":").append(pagedAppConfigs.getTotalElements());
        appConfigJSONData.append(",\"recordsFiltered\":").append(pagedAppConfigs.getTotalElements());
        appConfigJSONData.append(",\"data\":").append(toJSON(pagedAppConfigs.getContent(), AppConfig.class, AppConfigJsonAdaptor.class)).append("}");
        return appConfigJSONData.toString();
    }

}
