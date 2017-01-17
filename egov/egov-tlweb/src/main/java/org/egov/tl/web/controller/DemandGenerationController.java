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
package org.egov.tl.web.controller;

import org.egov.commons.CFinancialYear;
import org.egov.commons.service.CFinancialYearService;
import org.egov.tl.entity.DemandGenerationLog;
import org.egov.tl.service.DemandGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/demand-generation")
public class DemandGenerationController {

    @Autowired
    private DemandGenerationService demandGenerationService;

    @Autowired
    private CFinancialYearService financialYearService;

    @ModelAttribute("financialYearList")
    public List<CFinancialYear> financialYearList() {
        return financialYearService.getAllFinancialYears();
    }

    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String newForm() {
        return "demand-generate";
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public String generateDemand(@RequestParam String installmentYear, RedirectAttributes responseAttribs) {
        DemandGenerationLog bulkDemandGenerationLog = demandGenerationService.generateDemand(installmentYear);
        responseAttribs.addFlashAttribute("demandGenerationLog", bulkDemandGenerationLog);
        responseAttribs.addFlashAttribute("message",
                "msg.demand.generation." + bulkDemandGenerationLog.getDemandGenerationStatus());
        return "redirect:/demand-generation/create";
    }

    @RequestMapping(value = "regenerate", method = RequestMethod.POST)
    public String regenerateDemand(@RequestParam String installmentYear, RedirectAttributes responseAttribs) {
        DemandGenerationLog bulkDemandGenerationLog = demandGenerationService.regenerateDemand(installmentYear);
        responseAttribs.addFlashAttribute("demandGenerationLog", bulkDemandGenerationLog);
        responseAttribs.addFlashAttribute("message",
                "msg.demand.generation." + bulkDemandGenerationLog.getDemandGenerationStatus());
        return "redirect:/demand-generation/create";
    }
}