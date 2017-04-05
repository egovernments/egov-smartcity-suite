/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2017>  eGovernments Foundation
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

package org.egov.tl.web.controller.legacy;

import java.io.IOException;

import javax.validation.Valid;
import javax.validation.ValidationException;

import org.egov.tl.entity.TradeLicense;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/legacylicense")
public class ModifyLegacyLicenseController extends LegacyLicenseController {

    private static final String UPDATE_LEGACY_FORM = "updateform-legacylicense";

    @ModelAttribute("tradeLicense")
    public TradeLicense tradeLicense(@PathVariable final Long id) {
        return tradeLicenseService.getLicenseById(id);
    }

    @GetMapping(value = "/update/{id}")
    public String update(@ModelAttribute final TradeLicense tradeLicense, final Model model) {

        model.addAttribute("legacyInstallmentwiseFees", legacyService.legacyInstallmentwiseFees(tradeLicense));
        model.addAttribute("legacyFeePayStatus", legacyService.legacyFeePayStatus(tradeLicense));
        model.addAttribute("outstandingFee", tradeLicenseService.getOutstandingFee(tradeLicense));

        return UPDATE_LEGACY_FORM;
    }

    @PostMapping(value = "/update/{id}")
    public String update(@Valid @ModelAttribute final TradeLicense tradeLicense, final BindingResult errors,
            @RequestParam("files") final MultipartFile[] files, final Model model) throws IOException {
        if (errors.hasErrors()) {
            model.addAttribute("legacyInstallmentwiseFees", legacyService.legacyInstallmentwiseFees(tradeLicense));
            model.addAttribute("legacyFeePayStatus", legacyService.legacyFeePayStatus(tradeLicense));
            model.addAttribute("outstandingFee", tradeLicenseService.getOutstandingFee(tradeLicense));
            return UPDATE_LEGACY_FORM;
        }
        try {
            legacyService.storeDocument(tradeLicense, files);
        } catch (final ValidationException e) {
            errors.rejectValue("files", e.getMessage());
            return UPDATE_LEGACY_FORM;
        }

        legacyService.updateLegacy(tradeLicense);

        return "redirect:/legacylicense/view/" + tradeLicense.getApplicationNumber();
    }

}
