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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.egov.lcms.masters.service.InterimOrderService;
import org.egov.lcms.transactions.entity.LcInterimOrder;
import org.egov.lcms.transactions.entity.LegalCase;
import org.egov.lcms.transactions.service.LcInterimOrderService;
import org.egov.lcms.transactions.service.LegalCaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/lcinterimorder")
public class LcInterimOrderController {

    @Autowired
    private LcInterimOrderService lcInterimOrderService;

    @Autowired
    private LegalCaseService legalcaseService;

    @Autowired
    private InterimOrderService interimOrderService;

    private void prepareNewForm(final Model model) {
        model.addAttribute("interimOrders", interimOrderService.findAll());

    }

    @RequestMapping(value = "/new/{lcNumber}", method = RequestMethod.GET)
    public String viewForm(@ModelAttribute("lcInterimOrder") final LcInterimOrder lcInterimOrder,
            @PathVariable final String lcNumber, final Model model, final HttpServletRequest request) {
        prepareNewForm(model);
        final LegalCase legalCase = getLegalCase(lcNumber, request);
        model.addAttribute("legalCase", legalCase);
        model.addAttribute("lcInterimOrder", lcInterimOrder);
        model.addAttribute("mode", "create");
        return "lcinterimorder-new";
    }

    @ModelAttribute
    private LegalCase getLegalCase(@PathVariable final String lcNumber, final HttpServletRequest request) {
        final LegalCase legalCase = legalcaseService.findByLcNumber(lcNumber);
        return legalCase;
    }

    @RequestMapping(value = "/new/{lcNumber}", method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute("lcInterimOrder") final LcInterimOrder lcInterimOrder,
            final BindingResult errors, final RedirectAttributes redirectAttrs, @PathVariable final String lcNumber,
            final HttpServletRequest request, final Model model) {
        final LegalCase legalCase = getLegalCase(lcNumber, request);
        if (errors.hasErrors()) {
            prepareNewForm(model);
            model.addAttribute("legalCase", legalCase);
            return "lcinterimorder-new";
        } else
            lcInterimOrder.setLegalCase(legalCase);
        lcInterimOrderService.persist(lcInterimOrder);
        model.addAttribute("mode", "create");
        redirectAttrs.addFlashAttribute("lcInterimOrder", lcInterimOrder);
        model.addAttribute("message", "Interim Order Created successfully.");
        return "lcinterimorder-success";

    }
}