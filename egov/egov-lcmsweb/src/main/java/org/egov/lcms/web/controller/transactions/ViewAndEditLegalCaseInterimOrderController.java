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

import org.egov.lcms.masters.service.InterimOrderService;
import org.egov.lcms.transactions.entity.LcInterimOrderDocuments;
import org.egov.lcms.transactions.entity.LegalCaseInterimOrder;
import org.egov.lcms.transactions.service.LegalCaseInterimOrderService;
import org.egov.lcms.utils.LegalCaseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/lcinterimorder")
public class ViewAndEditLegalCaseInterimOrderController {

    @Autowired
    private LegalCaseInterimOrderService legalCaseInterimOrderService;

    @Autowired
    private InterimOrderService interimOrderService;

    @Autowired
    private LegalCaseUtil legalCaseUtil;

    @ModelAttribute
    public LegalCaseInterimOrder getLcInterimOrder(@PathVariable final String lcInterimOrderId) {
        final LegalCaseInterimOrder legalCaseInterimOrder = legalCaseInterimOrderService
                .findById(Long.parseLong(lcInterimOrderId));
        return legalCaseInterimOrder;
    }

    @RequestMapping(value = "/edit/{lcInterimOrderId}", method = RequestMethod.GET)
    public String edit(@PathVariable("lcInterimOrderId") final String lcInterimOrderId, final Model model,
            final HttpServletRequest request) {
        final LegalCaseInterimOrder legalCaseInterimOrder = getLcInterimOrder(lcInterimOrderId);
        model.addAttribute("interimOrders", interimOrderService.findAll());
        model.addAttribute("legalCaseInterimOrder", legalCaseInterimOrder);
        model.addAttribute("legalCase", legalCaseInterimOrder.getLegalCase());
        model.addAttribute("lcNumber", legalCaseInterimOrder.getLegalCase().getLcNumber());
        getLcInterimOrderDocuments(legalCaseInterimOrder);
        model.addAttribute("mode", "edit");
        return "lcinterimorder-edit";
    }

    @RequestMapping(value = "/edit/{lcInterimOrderId}", method = RequestMethod.POST)
    public String update(
            @Valid @ModelAttribute("legalCaseInterimOrder") final LegalCaseInterimOrder legalCaseInterimOrder,
            final BindingResult errors, final RedirectAttributes redirectAttrs,
            @PathVariable("lcInterimOrderId") final String lcInterimOrderId,
            @RequestParam("file") final MultipartFile[] files, final Model model) throws IOException, ParseException {
        if (errors.hasErrors()) {
            model.addAttribute("interimOrders", interimOrderService.findAll());
            return "lcinterimorder-edit";
        }
        legalCaseInterimOrderService.persist(legalCaseInterimOrder, files);
        redirectAttrs.addFlashAttribute("legalCaseInterimOrder", legalCaseInterimOrder);
        getLcInterimOrderDocuments(legalCaseInterimOrder);
        model.addAttribute("message", "InterimOrder updated successfully.");
        model.addAttribute("mode", "view");
        return "lcinterimorder-success";
    }

    @RequestMapping(value = "/view/{lcInterimOrderId}", method = RequestMethod.GET)
    public String view(@PathVariable("lcInterimOrderId") final String lcInterimOrderId, final Model model) {
        final LegalCaseInterimOrder legalCaseInterimOrder = getLcInterimOrder(lcInterimOrderId);
        model.addAttribute("legalCaseInterimOrder", legalCaseInterimOrder);
        model.addAttribute("lcNumber", legalCaseInterimOrder.getLegalCase().getLcNumber());
        getLcInterimOrderDocuments(legalCaseInterimOrder);
        model.addAttribute("mode", "view");
        return "lcinterimorder-success";
    }

    private LegalCaseInterimOrder getLcInterimOrderDocuments(final LegalCaseInterimOrder legalCaseInterimOrder) {
        List<LcInterimOrderDocuments> documentDetailsList = new ArrayList<LcInterimOrderDocuments>();
        documentDetailsList = legalCaseUtil.getLcInterimOrderDocumentList(legalCaseInterimOrder);
        legalCaseInterimOrder.setLcInterimOrderDocuments(documentDetailsList);
        return legalCaseInterimOrder;
    }

}
