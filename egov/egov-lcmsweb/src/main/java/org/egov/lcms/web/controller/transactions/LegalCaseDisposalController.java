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
package org.egov.lcms.web.controller.transactions;

import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.egov.lcms.transactions.entity.LegalCase;
import org.egov.lcms.transactions.entity.LegalCaseDisposal;
import org.egov.lcms.transactions.service.LegalCaseDisposalService;
import org.egov.lcms.transactions.service.LegalCaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/legalcasedisposal")
public class LegalCaseDisposalController {

    @Autowired
    private LegalCaseDisposalService legalCaseDisposalService;

    @Autowired
    private LegalCaseService legalCaseService;

    @ModelAttribute
    private LegalCase getLegalCase(@RequestParam("lcNumber") final String lcNumber) {
        final LegalCase legalCase = legalCaseService.findByLcNumber(lcNumber);
        return legalCase;
    }

    @RequestMapping(value = "/new/", method = RequestMethod.GET)
    public String newForm(@ModelAttribute final LegalCaseDisposal legalCaseDisposal, final Model model,
            @RequestParam("lcNumber") final String lcNumber, final HttpServletRequest request) {
        final LegalCase legalCase = getLegalCase(lcNumber);
        model.addAttribute("legalCase", legalCase);
        model.addAttribute("legalCaseDisposal", legalCaseDisposal);
        model.addAttribute("mode", "create");
        return "legalcaseDisposal-new";
    }

    @RequestMapping(value = "/new/", method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute final LegalCaseDisposal legalCaseDisposal, final BindingResult errors,
            @RequestParam("lcNumber") final String lcNumber, final RedirectAttributes redirectAttrs, final Model model,
            final HttpServletRequest request) throws ParseException {
        final LegalCase legalCase = getLegalCase(lcNumber);
        if (errors.hasErrors()) {
            model.addAttribute("legalcase", legalCase);
            return "legalcaseDisposal-new";
        } else
            legalCaseDisposal.setLegalCase(legalCase);
        legalCaseDisposalService.persist(legalCaseDisposal);
        redirectAttrs.addFlashAttribute("legalCaseDisposal", legalCaseDisposal);
        model.addAttribute("message", "Case is closed successfully.");
        model.addAttribute("mode", "create");
        return "legalcaseDisposal-success";
    }

}
