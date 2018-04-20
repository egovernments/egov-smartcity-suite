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
package org.egov.tl.web.controller.transactions.closure;

import org.egov.tl.entity.TradeLicense;
import org.egov.tl.web.validator.closure.CreateLicenseClosureValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

import static org.egov.tl.utils.Constants.AUTO;
import static org.egov.tl.utils.Constants.MESSAGE;


@Controller
@RequestMapping(value = "/license/closure/{licenseId}")
public class CreateLicenseClosureController extends LicenseClosureProcessflowController {

    private static final String REDIRECT_TO_VIEW = "redirect:/license/success/";
    private static final String LICENSECLOSURE = "license-closure";

    @Autowired
    private CreateLicenseClosureValidator createLicenseClosureValidator;

    @GetMapping
    public String showClosureForm(@ModelAttribute TradeLicense license, RedirectAttributes redirectAttributes) {
        if (license.transitionInprogress()) {
            redirectAttributes.addFlashAttribute(MESSAGE, "msg.license.process");
            return REDIRECT_TO_VIEW + license.getId();
        }
        license.setApplicationNumber(AUTO);
        return LICENSECLOSURE;
    }

    @PostMapping
    public String createClosure(@Valid @ModelAttribute TradeLicense tradeLicense, BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {
        createLicenseClosureValidator.validate(tradeLicense, bindingResult);
        if (bindingResult.hasErrors())
            return LICENSECLOSURE;
        if (tradeLicense.transitionInprogress()) {
            redirectAttributes.addFlashAttribute(MESSAGE, "msg.license.process");
            return REDIRECT_TO_VIEW + tradeLicense.getId();
        }
        licenseClosureService.createClosure(tradeLicense);
        redirectAttributes.addFlashAttribute(MESSAGE, "msg.closure.initiated");
        return REDIRECT_TO_VIEW + tradeLicense.getId();
    }

}