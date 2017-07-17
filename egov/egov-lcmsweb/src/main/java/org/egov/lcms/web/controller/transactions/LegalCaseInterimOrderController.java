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

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.egov.lcms.masters.service.InterimOrderService;
import org.egov.lcms.transactions.entity.LegalCase;
import org.egov.lcms.transactions.entity.LegalCaseInterimOrder;
import org.egov.lcms.transactions.service.LegalCaseInterimOrderService;
import org.egov.lcms.transactions.service.LegalCaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/lcinterimorder")
public class LegalCaseInterimOrderController {

    @Autowired
    private LegalCaseInterimOrderService legalCaseInterimOrderService;

    @Autowired
    private LegalCaseService legalcaseService;

    @Autowired
    private InterimOrderService interimOrderService;

    @RequestMapping(value = "/new/", method = RequestMethod.GET)
    public String viewForm(@ModelAttribute("legalCaseInterimOrder") final LegalCaseInterimOrder legalCaseInterimOrder,
            @RequestParam("lcNumber") final String lcNumber, final Model model, final HttpServletRequest request) {
        final LegalCase legalCase = getLegalCase(lcNumber, request);
        model.addAttribute("legalCase", legalCase);
        model.addAttribute("interimOrders", interimOrderService.getActiveInterimOrder());
        model.addAttribute("legalCaseInterimOrder", legalCaseInterimOrder);
        model.addAttribute("lcNumber", legalCase.getLcNumber());
        model.addAttribute("mode", "create");
        return "lcinterimorder-new";
    }

    @ModelAttribute
    private LegalCase getLegalCase(@RequestParam("lcNumber") final String lcNumber, final HttpServletRequest request) {
        final LegalCase legalCase = legalcaseService.findByLcNumber(lcNumber);
        return legalCase;
    }

    @RequestMapping(value = "/new/", method = RequestMethod.POST)
    public String create(
            @Valid @ModelAttribute("legalCaseInterimOrder") final LegalCaseInterimOrder legalCaseInterimOrder,
            final BindingResult errors, final RedirectAttributes redirectAttrs,
            @RequestParam("lcNumber") final String lcNumber, @RequestParam("file") final MultipartFile[] files,
            final HttpServletRequest request, final Model model) throws IOException, ParseException {
        final LegalCase legalCase = getLegalCase(lcNumber, request);
        if (errors.hasErrors()) {
            model.addAttribute("interimOrders", interimOrderService.getActiveInterimOrder());
            model.addAttribute("legalCase", legalCase);
            return "lcinterimorder-new";
        } else
            legalCaseInterimOrder.setLegalCase(legalCase);
        legalCaseInterimOrderService.persist(legalCaseInterimOrder, files);
        model.addAttribute("mode", "view");
        model.addAttribute("lcNumber", legalCase.getLcNumber());
        redirectAttrs.addFlashAttribute("legalCaseInterimOrder", legalCaseInterimOrder);
        model.addAttribute("message", "Interim Order Created successfully.");
        return "lcinterimorder-success";

    }

    @RequestMapping(value = "/list/", method = RequestMethod.GET)
    public String getInterimOrderList(final Model model, @RequestParam("lcNumber") final String lcNumber,
            final HttpServletRequest request) {
        final LegalCase legalCase = getLegalCase(lcNumber, request);
        final List<LegalCaseInterimOrder> lcInterimOrderList = legalCaseInterimOrderService.findByLCNumber(lcNumber);
        model.addAttribute("legalCase", legalCase);
        model.addAttribute("lcNumber", legalCase.getLcNumber());
        model.addAttribute("lcInterimOrderId", legalCase.getLegalCaseInterimOrder());
        model.addAttribute("lcInterimOrderList", lcInterimOrderList);
        return "lcinterimorder-list";

    }

}