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

package org.egov.tl.web.controller.masters.penaltyrates;

import org.egov.tl.entity.LicenseAppType;
import org.egov.tl.entity.PenaltyRates;
import org.egov.tl.entity.contracts.PenaltyRatesRequest;
import org.egov.tl.service.LicenseAppTypeService;
import org.egov.tl.service.PenaltyRatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/penaltyrates/create")
public class CreatePenaltyRatesController {

    @Autowired
    private PenaltyRatesService penaltyRatesService;

    @Autowired
    private LicenseAppTypeService licenseAppTypeService;

    @ModelAttribute("penaltyRate")
    public PenaltyRatesRequest penaltyRate() {
        return new PenaltyRatesRequest();
    }

    @ModelAttribute("licenseAppTypes")
    public List<LicenseAppType> licenseAppTypes() {
        return licenseAppTypeService.getDisplayableLicenseAppTypes();
    }

    @GetMapping
    public String penaltyRatesForm() {
        return "penaltyrate-create";
    }

    @GetMapping("/{appType}")
    @ResponseBody
    public boolean checkPenaltyRatesExist(@PathVariable Long appType) {
        return !penaltyRatesService.getPenaltyRatesByLicenseAppTypeId(appType).isEmpty();
    }

    @PostMapping
    public String penaltyRatesCreate(@Valid @ModelAttribute("penaltyRate") PenaltyRatesRequest penaltyRate, BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes, Model model) {
        List<PenaltyRates> penaltyRates = penaltyRatesService.getPenaltyRatesByLicenseAppType(penaltyRate.getLicenseAppType());
        if (!penaltyRates.isEmpty()) {
            model.addAttribute("error", "error.penalty.rate.exist");
            return "penaltyrate-create";
        }

        if (bindingResult.hasErrors())
            return "penaltyrate-create";
        penaltyRatesService.create(penaltyRate.getPenaltyRateData());
        redirectAttributes.addFlashAttribute("message", "msg.penalty.rate.created");
        return "redirect:/penaltyrates/create";
    }
}
