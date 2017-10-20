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

import org.egov.infra.persistence.entity.enums.UserType;
import org.egov.infra.security.audit.contract.LoginAuditReportRequest;
import org.egov.infra.security.audit.entity.LoginAudit;
import org.egov.infra.security.audit.service.LoginAuditService;
import org.egov.infra.web.support.ui.DataTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@Controller
@RequestMapping("/audit/report/login")
public class LoginAuditReportController {

    @Autowired
    private LoginAuditService loginAuditService;

    @ModelAttribute("userTypes")
    public List<UserType> userTypes() {
        return Arrays.asList(UserType.values());
    }

    @ModelAttribute
    public LoginAuditReportRequest loginAuditReportRequest() {
        return new LoginAuditReportRequest();
    }

    @GetMapping
    public String loginAuditReportSearchView() {
        return "login-audit-report";
    }

    @PostMapping(produces = APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public DataTable<LoginAudit> loginAuditReport(@ModelAttribute LoginAuditReportRequest loginAuditReportRequest) {
        return new DataTable<>(loginAuditService.getLoginAudits(loginAuditReportRequest),
                loginAuditReportRequest.draw());
    }
}
