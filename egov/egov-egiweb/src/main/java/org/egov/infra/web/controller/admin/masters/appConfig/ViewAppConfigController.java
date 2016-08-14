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

import org.apache.commons.io.IOUtils;
import org.egov.infra.admin.master.entity.AppConfig;
import org.egov.infra.admin.master.entity.AppConfigAdaptor;
import org.egov.infra.admin.master.service.AppConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static org.egov.infra.web.utils.WebUtils.toJSON;

@Controller
@RequestMapping("/appConfig")
public class ViewAppConfigController extends MultiActionController {

    private AppConfigService appConfigService;
    public static final String CONTENTTYPE_JSON = "application/json";

    @Autowired
    public ViewAppConfigController(AppConfigService appConfigService) {
        this.appConfigService = appConfigService;
    }

    @RequestMapping(value = "view", method = RequestMethod.GET)
    public String AppConfigViewForm(@ModelAttribute AppConfig appConfig, Model model) {
        return "view-appconfig";

    }
    
    @RequestMapping(value = "/viewList/{id}", method = RequestMethod.GET)
    public String viewAppConfigForm(final Model model, @ModelAttribute AppConfig appConfig,@PathVariable final Long id) {
    	appConfig=appConfigService.findById(id);
    	model.addAttribute("appConfig", appConfig);
        return "appConfigList-view";
    }

    @RequestMapping(value = "ajax/result", method = RequestMethod.GET)
    public @ResponseBody void springPaginationDataTables(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int pageStart = Integer.valueOf(request.getParameter("start"));
        int pageSize = Integer.valueOf(request.getParameter("length"));
        int pageNumber = pageStart / pageSize + 1;
        List<AppConfig> totalRecords = appConfigService.findAll();

        if (pageSize == -1) {
            pageSize = totalRecords.size();
        }

        final List<AppConfig> appConfigs = appConfigService.getListOfAppConfig(pageNumber, pageSize).getContent();
        final StringBuilder appConfigJSONData = new StringBuilder();
        appConfigJSONData.append("{\"draw\": ").append("0");
        appConfigJSONData.append(",\"recordsTotal\":").append(totalRecords.size());
        appConfigJSONData.append(",\"totalDisplayRecords\":").append(appConfigs.size());
        appConfigJSONData.append(",\"recordsFiltered\":").append(totalRecords.size());
        appConfigJSONData.append(",\"data\":").append(toJSON(appConfigs, AppConfig.class, AppConfigAdaptor.class)).append("}");
        response.setContentType(CONTENTTYPE_JSON);
        IOUtils.write(appConfigJSONData, response.getWriter());
    }

}
