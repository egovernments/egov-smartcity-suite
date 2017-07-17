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
package org.egov.lcms.web.controller.reports;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.lcms.reports.entity.LegalCommonReportResult;
import org.egov.lcms.utils.constants.LcmsConstants;
import org.egov.lcms.web.controller.transactions.GenericLegalCaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/reports")
public class DueLegalCaseReportController  extends GenericLegalCaseController {

    public @ModelAttribute("reportTypeList") List<String> getReportByTypes() {
        final List<String> reportTypeList = new ArrayList<String>();
        reportTypeList.add(LcmsConstants.DUEPWRREPORT);
        reportTypeList.add(LcmsConstants.DUECAREPORT);
        reportTypeList.add(LcmsConstants.DUEJUDGEMENTIMPLPREPORT);
        reportTypeList.add(LcmsConstants.DUEEMPLOYEEHEARINGREPORT);
        return reportTypeList;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/dueReport")
    public String dueReportForm(final Model model) {
        model.addAttribute("legalCommonReport", new LegalCommonReportResult());
        return "duereport-form";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/caDueReport")
    public String caDueForm(final Model model) {
        model.addAttribute("currentDate", new Date());
        return "counterAffidavitDueReport-form";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/pwrDueReport")
    public String searchForm(final Model model) {
        model.addAttribute("currentDate", new Date());
        return "pwrDueReport-form";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/judgementImplDueReport")
    public String searchJudgementImplDueForm(final Model model) {
        model.addAttribute("currentDate", new Date());
        return "judgementImplDueReport-form";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/employeeHearingDueReport")
    public String searchHearingDueForm(final Model model) {
        model.addAttribute("currentDate", new Date());
        return "employeehearingDueReport-form";
    }
}
