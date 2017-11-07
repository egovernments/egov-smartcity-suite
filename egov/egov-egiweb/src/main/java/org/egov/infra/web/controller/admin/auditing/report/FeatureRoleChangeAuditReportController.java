/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2017  eGovernments Foundation
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

package org.egov.infra.web.controller.admin.auditing.report;

import org.egov.infra.admin.auditing.contract.FeatureRoleChangeAuditReportRequest;
import org.egov.infra.admin.auditing.service.FeatureAuditService;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.web.contract.response.FeatureRoleChangeAuditReportAdapter;
import org.egov.infra.web.support.ui.DataTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@Controller
@RequestMapping("audit/report/feature-role")
public class FeatureRoleChangeAuditReportController {

    @Autowired
    private FeatureAuditService featureAuditService;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private FeatureRoleChangeAuditReportAdapter featureRoleChangeAuditReportAdapter;

    @GetMapping
    public String featureRoleChangeAuditReportSearchView(Model model) {
        model.addAttribute("modules", moduleService.getAllTopModules());
        return "feature-role-audit-report";
    }

    @PostMapping(produces = TEXT_PLAIN_VALUE)
    @ResponseBody
    public String featureRoleChangeAuditReportSearchResult(
            @ModelAttribute FeatureRoleChangeAuditReportRequest featureRoleChangeAuditReportRequest) {
        return new DataTable<>(featureAuditService
                .getFeatureRoleChangeAudit(featureRoleChangeAuditReportRequest), featureRoleChangeAuditReportRequest.draw())
                .toJson(featureRoleChangeAuditReportAdapter);
    }
}
