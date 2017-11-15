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
package org.egov.tl.web.controller.masters.penaltyrates;

import org.egov.tl.entity.LicenseAppType;
import org.egov.tl.entity.PenaltyRates;
import org.egov.tl.entity.contracts.PenaltyForm;
import org.egov.tl.service.LicenseAppTypeService;
import org.egov.tl.service.PenaltyRatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@Controller
@RequestMapping("/penaltyRates")
public class PenaltyRatesController {

    private static final String PENALTYRATE_RESULT = "penaltyRates-result";

    @Autowired
    private PenaltyRatesService penaltyRatesService;

    @Autowired
    private LicenseAppTypeService licenseAppTypeService;

    @ModelAttribute("licenseAppTypes")
    public List<LicenseAppType> licenseAppTypes() {
        return licenseAppTypeService.findAllLicenseAppType();
    }

    @GetMapping("/create")
    public String penaltyRatesForm(@ModelAttribute PenaltyForm penaltyForm) {
        return "penaltyRates-create";
    }

    @PostMapping("/create")
    public String penaltyRatesCreate(@ModelAttribute PenaltyForm penaltyForm, BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors())
            return PENALTYRATE_RESULT;
        penaltyRatesService.create(penaltyForm.getPenaltyRates());
        redirectAttributes.addFlashAttribute("message", "msg.penaltyRate.created");
        return "penaltyRates-view";
    }

    @GetMapping("/view")
    public String searchForm(@ModelAttribute PenaltyForm penaltyForm) {

        return "penaltyRates-search";
    }

    @GetMapping("/search")
    public String search(@ModelAttribute PenaltyForm penaltyForm, BindingResult errors, Model model) {
        if (errors.hasErrors())
            return PENALTYRATE_RESULT;
        penaltyForm.setPenaltyRatesList(penaltyRatesService.search(penaltyForm.getLicenseAppType().getId()));
        model.addAttribute("penaltyForm", penaltyForm);
        return PENALTYRATE_RESULT;
    }

    @GetMapping(value = "/searchview")
    public String searchview(@ModelAttribute PenaltyForm penaltyForm, BindingResult errors, Model model) {
        if (errors.hasErrors())
            return "penaltyRates-search";
        penaltyForm.setPenaltyRatesList(penaltyRatesService.search(penaltyForm.getLicenseAppType().getId()));
        model.addAttribute("penaltyForm", penaltyForm);

        return "penaltyRates-viewResult";
    }


    @GetMapping(value = "/deleterow", produces = TEXT_PLAIN_VALUE)
    @ResponseBody
    public String removePenaltyRate(@RequestParam Long penaltyRateId) {
        PenaltyRates penaltyRates = penaltyRatesService.findOne(penaltyRateId);
        if (penaltyRates != null)
            penaltyRatesService.delete(penaltyRates);
        return "success";

    }
}
