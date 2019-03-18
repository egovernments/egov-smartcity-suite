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
package org.egov.tl.web.controller.transactions.demand;

import org.egov.commons.CFinancialYear;
import org.egov.commons.service.CFinancialYearService;
import org.egov.tl.entity.DemandGenerationLog;
import org.egov.tl.entity.DemandGenerationLogDetail;
import org.egov.tl.entity.TradeLicense;
import org.egov.tl.entity.contracts.DemandGenerationRequest;
import org.egov.tl.service.DemandGenerationService;
import org.egov.tl.service.TradeLicenseService;
import org.egov.tl.web.response.adaptor.DemandGenerationResponseAdaptor;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collection;
import java.util.stream.Collectors;

import static org.egov.infra.utils.JsonUtils.toJSON;
import static org.egov.tl.entity.enums.ProcessStatus.INCOMPLETE;
import static org.egov.tl.utils.Constants.LICENSE_STATUS_CANCELLED;
import static org.egov.tl.utils.Constants.MESSAGE;

@Controller
@RequestMapping("/demand")
public class DemandGenerationController {

    @Autowired
    private DemandGenerationService demandGenerationService;

    @Autowired
    private CFinancialYearService financialYearService;

    @Autowired
    @Qualifier("tradeLicenseService")
    private TradeLicenseService tradeLicenseService;

    @GetMapping("generate")
    public String newForm(Model model) {
        CFinancialYear financialYear = financialYearService.getLatestFinancialYear();
        if (financialYear == null) {
            DateTime currentFinYear = new DateTime();
            DateTime finYear = currentFinYear.plusYears(1);
            model.addAttribute("error", "error.financial.year.not.defined");
            model.addAttribute("finYear", currentFinYear.toString("yyyy") + "-" + finYear.toString("yy"));
        } else {
            DemandGenerationLog demandGenerationLog = demandGenerationService.getDemandGenerationLog(financialYear);
            if (demandGenerationLog.getInstallmentYear().equals(financialYear.getFinYearRange())) {
                model.addAttribute("demandGenerationLogDetails", toJSON(demandGenerationLog.getDetails(),
                        DemandGenerationLogDetail.class, DemandGenerationResponseAdaptor.class));
                model.addAttribute("licenseIds", tradeLicenseService.getLicenseIdsForDemandGeneration(financialYear));
                model.addAttribute("installmentYear", financialYear.getFinYearRange());
                model.addAttribute("pending", false);
            } else {
                model.addAttribute("demandGenerationLogDetails", toJSON(demandGenerationLog.getDetails()
                                .stream()
                                .filter(detail -> detail.getStatus().equals(INCOMPLETE))
                                .collect(Collectors.toList()),
                        DemandGenerationLogDetail.class, DemandGenerationResponseAdaptor.class));
                model.addAttribute("pending", true);
            }
        }
        return "demand-generate";
    }

    @PostMapping("generate")
    @ResponseBody
    public Iterable<DemandGenerationLogDetail> generateDemand(DemandGenerationRequest demandGenerationRequest) {
        Collection<DemandGenerationLogDetail> demandLogDetails = demandGenerationService.generateDemand(demandGenerationRequest);
        CFinancialYear financialYear = financialYearService.getLatestFinancialYear();
        demandGenerationService.updateDemandGenerationLog(financialYear);
        if (demandLogDetails.isEmpty()) {
            demandLogDetails = demandGenerationService.getDemandGenerationLog(financialYear).getDetails();
        }
        return demandLogDetails;
    }

    @GetMapping("generate/{licenseId}")
    public String generateDemandForLicense(@PathVariable Long licenseId, @RequestParam(required = false) boolean forPrevYear,
                                           Model model) {
        TradeLicense license = tradeLicenseService.getLicenseById(licenseId);
        if (license == null) {
            model.addAttribute("message", "error.license.not.found");
        } else if (!license.getIsActive()) {
            demandGenerationService.markDemandGenerationLogAsCompleted(license, LICENSE_STATUS_CANCELLED);
        } else {
            model.addAttribute("licenseNumber", license.getLicenseNumber());
            model.addAttribute("forPrevYear", forPrevYear);
            model.addAttribute("financialYear", forPrevYear ? license.getDemand().getEgInstallmentMaster().getFinYearRange() :
                    financialYearService.getLatestFinancialYear().getFinYearRange());
        }
        return "demandgenerate-result";
    }

    @PostMapping("generate/{licenseId}")
    public String generateDemandForLicense(@PathVariable Long licenseId, @RequestParam boolean forPrevYear,
                                           RedirectAttributes redirectAttrs) {
        boolean generationStatus = demandGenerationService.generateLicenseDemand(licenseId, forPrevYear);
        redirectAttrs.addFlashAttribute(MESSAGE, generationStatus ?
                "msg.demand.generation.completed" : "msg.demand.generation.incomplete");
        return "redirect:/demand/generate/" + licenseId;
    }
}