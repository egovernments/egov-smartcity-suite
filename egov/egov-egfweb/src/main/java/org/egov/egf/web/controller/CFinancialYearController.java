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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFiscalPeriod;
import org.egov.commons.repository.CFinancialYearRepository;
import org.egov.commons.service.CFinancialYearService;
import org.egov.egf.web.adaptor.CFinancialYearJsonAdaptor;
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

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Controller
@RequestMapping("/cfinancialyear")
public class CFinancialYearController {
    private final static String CFINANCIALYEAR_NEW = "cfinancialyear-new";
    private final static String CFINANCIALYEAR_RESULT = "cfinancialyear-result";
    private final static String CFINANCIALYEAR_EDIT = "cfinancialyear-edit";
    private final static String CFINANCIALYEAR_VIEW = "cfinancialyear-view";
    private final static String CFINANCIALYEAR_SEARCH = "cfinancialyear-search";

    @Autowired
    private CFinancialYearService cFinancialYearService;
    
    @Autowired
    private CFinancialYearRepository cFinancialYearRepository;

    @Autowired
    private MessageSource messageSource;


    private void prepareNewForm(final Model model) {
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String newForm(final Model model) {
        prepareNewForm(model);
        final SimpleDateFormat dtFormat = new SimpleDateFormat("dd/MM/yyyy");
        final CFinancialYear financialYear = new CFinancialYear();
        if (financialYear.getcFiscalPeriod().isEmpty())
            financialYear.addCFiscalPeriod(new CFiscalPeriod());
        final Date nextStartingDate = cFinancialYearService.getNextFinancialYearStartingDate();
        model.addAttribute("startingDate", dtFormat.format(nextStartingDate));
        model.addAttribute("CFinancialYear", financialYear);
        model.addAttribute("mode", "create");
        return CFINANCIALYEAR_NEW;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute CFinancialYear cFinancialYear, final BindingResult errors,
            final Model model, final RedirectAttributes redirectAttrs) {
        final Boolean flag = false;
        final Boolean isActive = true;
        if (errors.hasErrors()) {
            prepareNewForm(model);
            model.addAttribute("mode", "create");
            return CFINANCIALYEAR_NEW;
        }
        CFiscalPeriod fiscalPeriod = new CFiscalPeriod();
        final List<CFiscalPeriod> fiscalList = cFinancialYear.getcFiscalPeriod();
        for (final CFiscalPeriod fiscal : fiscalList) {
            fiscalPeriod = cFinancialYearService.findByFiscalName(fiscal.getName());
            if (fiscalPeriod != null) {
                prepareNewForm(model);
                redirectAttrs.addFlashAttribute("financialYear", cFinancialYear);
                model.addAttribute("message", "Entered Fiscal Period Name " + fiscalPeriod.getName()
                        + " already Exists");
                model.addAttribute("mode", "create");
                return CFINANCIALYEAR_NEW;
            }
        }
        cFinancialYear.setIsActive(isActive);
        cFinancialYear.setIsClosed(flag);
        cFinancialYear.setTransferClosingBalance(flag);
        cFinancialYear = buildFiscalPeriodDetails(cFinancialYear, cFinancialYear.getcFiscalPeriod());
        cFinancialYearService.create(cFinancialYear);
        redirectAttrs.addFlashAttribute("message",
                messageSource.getMessage("msg.cFinancialYear.success", null, Locale.ENGLISH));
        return "redirect:/cfinancialyear/result/" + cFinancialYear.getId();
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable("id") final Long id, final Model model) {
        final CFinancialYear cFinancialYear = cFinancialYearService.findOne(id);
        prepareNewForm(model);
        model.addAttribute("CFinancialYear", cFinancialYear);
        model.addAttribute("mode", "edit");
        return CFINANCIALYEAR_EDIT;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@ModelAttribute final CFinancialYear cFinancialYear, final BindingResult errors,
            final Model model, final RedirectAttributes redirectAttrs) {
        if (errors.hasErrors()) {
            prepareNewForm(model);
            return CFINANCIALYEAR_EDIT;
        }
        cFinancialYearService.update(cFinancialYear);
        redirectAttrs.addFlashAttribute("message",
                messageSource.getMessage("msg.cFinancialYear.success", null, Locale.ENGLISH));
        return "redirect:/cfinancialyear/result/" + cFinancialYear.getId();
    }

    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public String view(@PathVariable("id") final Long id, final Model model) {
        final CFinancialYear cFinancialYear = cFinancialYearService.findOne(id);
        prepareNewForm(model);
        model.addAttribute("CFinancialYear", cFinancialYear);
        return CFINANCIALYEAR_VIEW;
    }

    @RequestMapping(value = "/result/{id}", method = RequestMethod.GET)
    public String result(@PathVariable("id") final Long id, final Model model) {
        final CFinancialYear cFinancialYear = cFinancialYearService.findOne(id);
        model.addAttribute("CFinancialYear", cFinancialYear);
        return CFINANCIALYEAR_RESULT;
    }

    @RequestMapping(value = "/search/{mode}", method = RequestMethod.GET)
    public String search(@PathVariable("mode") final String mode, final Model model) {
        final CFinancialYear cFinancialYear = new CFinancialYear();
        model.addAttribute("financialYears", cFinancialYearService.findAll());
        prepareNewForm(model);
        model.addAttribute("CFinancialYear", cFinancialYear);
        return CFINANCIALYEAR_SEARCH;

    }

    @RequestMapping(value = "/ajaxsearch/{mode}", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String ajaxsearch(@PathVariable("mode") final String mode, final Model model,
            @ModelAttribute final CFinancialYear cFinancialYear) {
        final List<CFinancialYear> searchResultList = cFinancialYearService.search(cFinancialYear);
        final String result = new StringBuilder("{ \"data\":").append(toSearchResultJson(searchResultList)).append("}")
                .toString();
        return result;
    }

    public Object toSearchResultJson(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(CFinancialYear.class, new CFinancialYearJsonAdaptor())
                .create();
        final String json = gson.toJson(object);
        return json;
    }

    private CFinancialYear buildFiscalPeriodDetails(final CFinancialYear cFinancialYear,
            final List<CFiscalPeriod> fiscalPeriodDetail) {
        final Boolean flag = false;
        final Set<CFiscalPeriod> fiscalPeriodSet = new HashSet<CFiscalPeriod>();

        for (final CFiscalPeriod fpDetail : fiscalPeriodDetail) {
            fpDetail.setIsActive(flag);
            fpDetail.setIsActiveForPosting(flag);
            fpDetail.setIsClosed(flag);
            fpDetail.setcFinancialYear(cFinancialYear);
            fiscalPeriodSet.add(fpDetail);
        }

        cFinancialYear.getcFiscalPeriod().clear();

        cFinancialYear.getcFiscalPeriod().addAll(fiscalPeriodSet);

        return cFinancialYear;

    }
}