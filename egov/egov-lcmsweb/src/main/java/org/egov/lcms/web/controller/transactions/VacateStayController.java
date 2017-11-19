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
package org.egov.lcms.web.controller.transactions;

import org.egov.lcms.transactions.entity.LegalCaseInterimOrder;
import org.egov.lcms.transactions.entity.VacateStay;
import org.egov.lcms.transactions.service.LegalCaseInterimOrderService;
import org.egov.lcms.transactions.service.VacateStayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/vacatestay")
public class VacateStayController {

    @Autowired
    private LegalCaseInterimOrderService legalCaseInterimOrderService;

    @Autowired
    private VacateStayService vacateStayService;

    @RequestMapping(value = "/new/", method = RequestMethod.GET)
    public String viewForm(@ModelAttribute("vacateStay") VacateStay vacateStay,
            @RequestParam("lcInterimOrderId") final String lcInterimOrderId, final Model model,
            final HttpServletRequest request) {

        final LegalCaseInterimOrder legalCaseInterimOrder = getLcInterimOrder(lcInterimOrderId);
        final List<VacateStay> vacateStayList = getLcInterimOrder(lcInterimOrderId).getVacateStay();
        if (!vacateStayList.isEmpty())
            vacateStay = vacateStayList.get(0);
        model.addAttribute("legalCaseInterimOrder", legalCaseInterimOrder);
        model.addAttribute("vacateStay", vacateStay);
        model.addAttribute("lcNumber", legalCaseInterimOrder.getLegalCase().getLcNumber());
        model.addAttribute("legalCase", legalCaseInterimOrder.getLegalCase());
        model.addAttribute("mode", "create");
        return "vacatestay-new";
    }

    @ModelAttribute
    private LegalCaseInterimOrder getLcInterimOrder(@RequestParam("lcInterimOrderId") final String lcInterimOrderId) {
        final LegalCaseInterimOrder legalCaseInterimOrder = legalCaseInterimOrderService
                .findById(Long.parseLong(lcInterimOrderId));
        return legalCaseInterimOrder;
    }

    @RequestMapping(value = "/new/", method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute("vacateStay") final VacateStay vacateStay, final BindingResult errors,
            final RedirectAttributes redirectAttrs, @RequestParam("lcInterimOrderId") final String lcInterimOrderId,
            final HttpServletRequest request, final Model model) {

        final LegalCaseInterimOrder legalCaseInterimOrder = getLcInterimOrder(lcInterimOrderId);
        if (errors.hasErrors()) {
            model.addAttribute("legalCaseInterimOrder", legalCaseInterimOrder);
            return "vacatestay-new";
        } else
            vacateStay.setLegalCaseInterimOrder(legalCaseInterimOrder);
        vacateStayService.persist(vacateStay);
        model.addAttribute("mode", "create");
        model.addAttribute("lcInterimOrderId", legalCaseInterimOrder.getId());
        model.addAttribute("lcNumber", legalCaseInterimOrder.getLegalCase().getLcNumber());
        model.addAttribute("legalCase", legalCaseInterimOrder.getLegalCase());
        redirectAttrs.addFlashAttribute("vacateStay", vacateStay);
        model.addAttribute("message", "Vacate Stay Saved successfully.");
        return "vacatestay-success";

    }

}
