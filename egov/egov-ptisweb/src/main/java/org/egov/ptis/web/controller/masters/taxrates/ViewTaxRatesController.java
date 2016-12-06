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
package org.egov.ptis.web.controller.masters.taxrates;

import java.math.BigDecimal;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.ptis.master.service.TaxRatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/taxrates")
public class ViewTaxRatesController {

    private final TaxRatesService taxRatesService;
    private static final String TAXRATE_VIEW = "taxRates-view";
    private static final String TAXRATE_EDIT = "taxrates-edit";
    private static final String GENERAL_TAX_RESD = "GEN_TAX_RESD";
    private static final String GENERAL_TAX_NONRESD = "GEN_TAX_NR";
    private static final String EDUCATIONAL_TAX = "EDU_CESS";
    private static final String REDIRECT_URL = "redirect:/taxrates/edit";
    private static final String REDIRECT_VIEW_URL = "redirect:/taxrates/view";
    private static final String MESSAGE = "message";

    @Autowired
    public ViewTaxRatesController(final TaxRatesService taxRatesService) {
        this.taxRatesService = taxRatesService;
    }

    @ModelAttribute
    public AppConfigValues taxRatesModel() {
        return new AppConfigValues();
    }

    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public String showTaxRates(final Model model) {
        final Map<String, Double> taxRatesMap = taxRatesService.getTaxDetails();
        model.addAttribute("taxRates", taxRatesMap);
        return TAXRATE_VIEW;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String viewTaxRates(final Model model) {
        final Map<String, Double> taxRatesMap = taxRatesService.getTaxDetails();
        model.addAttribute("taxRates", taxRatesMap);
        return TAXRATE_EDIT;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String saveTaxRates(final Model model, final HttpServletRequest request,
            final RedirectAttributes redirectAttributes) {
        final String resTaxValue = request.getParameter("General Tax Residential-value");
        final String nonResTaxValue = request.getParameter("General Tax Non Residential-value");
        final String eduTax = request.getParameter("Education Cess-value");
        if (StringUtils.isNotBlank(resTaxValue) && StringUtils.isNotBlank(nonResTaxValue)
                && StringUtils.isNotBlank(eduTax) && validateTaxValues(resTaxValue, nonResTaxValue, eduTax)) {
            final StringBuilder newTaxStr = new StringBuilder();
            final AppConfigValues appConfigValue = taxRatesService.getAllTaxRates().get(0);
            final String[] rows = appConfigValue.getValue().split("\n");
            for (final String row : rows) {
                final String[] value = row.split("=");
                if (GENERAL_TAX_RESD.equalsIgnoreCase(value[0]))
                    newTaxStr.append("GEN_TAX_RESD=" + resTaxValue + "\n");
                else if (GENERAL_TAX_NONRESD.equalsIgnoreCase(value[0]))
                    newTaxStr.append("GEN_TAX_NR=" + nonResTaxValue + "\n");
                else if (EDUCATIONAL_TAX.equalsIgnoreCase(value[0]))
                    newTaxStr.append("EDU_CESS=" + eduTax + "\n");
                else
                    newTaxStr.append(rows[rows.length - 1] == row ? value[0] + "=" + value[1]
                            : value[0] + "=" + value[1] + "\n");

            }
            if (StringUtils.isNotBlank(newTaxStr)) {
                appConfigValue.setValue(newTaxStr.toString());
                taxRatesService.updateTaxRateAppconfig(appConfigValue);
                redirectAttributes.addFlashAttribute(MESSAGE, "msg.update.taxrates");
            }
            return REDIRECT_VIEW_URL;
        } else {
            redirectAttributes.addFlashAttribute(MESSAGE, "msg.taxrate.invalid");
            return REDIRECT_URL;
        }
    }

    private Boolean validateTaxValues(final String newRsdTax, final String newNonRsdTax, final String newEduTax) {
        boolean isValid = true;
        final AppConfigValues appConfigValue = taxRatesService.getAllTaxRates().get(0);
        final String[] rows = appConfigValue.getValue().split("\n");
        for (final String row : rows) {
            final String[] value = row.split("=");
            if (GENERAL_TAX_RESD.equalsIgnoreCase(value[0])
                    && new BigDecimal(newRsdTax).compareTo(new BigDecimal(value[1])) < 0
                    || GENERAL_TAX_NONRESD.equalsIgnoreCase(value[0])
                            && new BigDecimal(newNonRsdTax).compareTo(new BigDecimal(value[1])) < 0
                    || EDUCATIONAL_TAX.equalsIgnoreCase(value[0])
                            && new BigDecimal(newEduTax).compareTo(new BigDecimal(value[1])) < 0)
                isValid = false;
        }
        return isValid;
    }
}
