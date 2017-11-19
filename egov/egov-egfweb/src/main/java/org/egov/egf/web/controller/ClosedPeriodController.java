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

package org.egov.egf.web.controller;

import java.util.List;

import org.egov.commons.service.CFinancialYearService;
import org.egov.egf.model.ClosedPeriod;
import org.egov.egf.web.adaptor.ClosedPeriodJsonAdaptor;
import org.egov.services.closeperiod.ClosedPeriodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Controller
@RequestMapping("/closedperiod")
public class ClosedPeriodController {
    private static final String CLOSEDPERIOD_RESULT = "closedperiod-result";
    private static final String CLOSEDPERIOD_EDIT = "closedperiod-edit";
    private static final String CLOSEDPERIOD_SEARCH = "closedperiod-search";
    private static final String CLOSEDPERIOD = "closedPeriod";
    @Autowired
    private ClosedPeriodService closedPeriodService;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private CFinancialYearService cFinancialYearService;

    private void prepareNewForm(final Model model) {
        model.addAttribute("cFinancialYears", cFinancialYearService.findAll());
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable("id") final Long id, final Model model) {
        final ClosedPeriod closedPeriod = closedPeriodService.findOne(id);

        prepareNewForm(model);
        model.addAttribute(CLOSEDPERIOD, closedPeriod);
        return CLOSEDPERIOD_EDIT;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@ModelAttribute final ClosedPeriod closedPeriod,
            final BindingResult errors, final Model model,
            final RedirectAttributes redirectAttrs) {
        if (errors.hasErrors()) {
            prepareNewForm(model);
            return CLOSEDPERIOD_EDIT;
        }
        final List<ClosedPeriod> closePer = closedPeriodService.findAll();
        final Long cId = closedPeriod.getcFinancialYearId().getId();
        if (!closePer.isEmpty()) {
            for (final ClosedPeriod cp : closePer)
                if (cp.getcFinancialYearId().getId() == closedPeriod.getcFinancialYearId().getId()
                        && !closedPeriod.getIsClosed()) {
                    closedPeriodService.delete(cp);
                    redirectAttrs.addFlashAttribute("message", messageSource.getMessage("msg.reopenedperiod.success", null, null));
                    return "redirect:/closedperiod/result/" + cId;
                }
        } else if (closePer.isEmpty() && closedPeriod.getIsClosed()) {
            closedPeriodService.update(closedPeriod);
            redirectAttrs.addFlashAttribute("message", messageSource
                    .getMessage("msg.closedPeriod.success", null, null));
            return "redirect:/closedperiod/result/" + closedPeriod.getId();
        }

        if (!closedPeriod.getcFinancialYearId().getIsClosed()
                && !closedPeriod.getIsClosed()) {
            redirectAttrs.addFlashAttribute("message", messageSource
                    .getMessage("msg.reopenedperiod.success", null, null));
            return "redirect:/closedperiod/result/" + cId;

        }

        closedPeriodService.update(closedPeriod);
        redirectAttrs.addFlashAttribute("message", messageSource.getMessage(
                "msg.closedPeriod.success", null, null));
        return "redirect:/closedperiod/result/" + closedPeriod.getId();

    }

    @RequestMapping(value = "/result/{id}", method = RequestMethod.GET)
    public String result(@PathVariable("id") final Long id, final Model model) {
        final ClosedPeriod closedPeriod = closedPeriodService.findOne(id);
        model.addAttribute(CLOSEDPERIOD, closedPeriod);
        return CLOSEDPERIOD_RESULT;
    }

    @RequestMapping(value = "/search/{mode}", method = RequestMethod.GET)
    public String search(@PathVariable("mode") final String mode, final Model model) {
        final ClosedPeriod closedPeriod = new ClosedPeriod();
        prepareNewForm(model);
        model.addAttribute(CLOSEDPERIOD, closedPeriod);
        return CLOSEDPERIOD_SEARCH;

    }

    @RequestMapping(value = "/ajaxsearch/{mode}", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String ajaxsearch(
            @PathVariable("mode") final String mode, final Model model,
            @ModelAttribute final ClosedPeriod closedPeriod) {
        final List<ClosedPeriod> searchResultList = closedPeriodService
                .search(closedPeriod);

        return new StringBuilder("{ \"data\":")
                .append(toSearchResultJson(searchResultList)).append("}")
                .toString();
    }

    public Object toSearchResultJson(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(ClosedPeriod.class,
                new ClosedPeriodJsonAdaptor()).create();
        return gson.toJson(object);
    }
}