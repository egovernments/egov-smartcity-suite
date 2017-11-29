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
package org.egov.ptis.web.controller.masters.taxrates;

import org.apache.commons.lang3.StringUtils;
import org.egov.demand.bean.DemandReasonDetailsBean;
import org.egov.demand.model.EgDemandReasonDetails;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.ptis.master.service.TaxRatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/taxrates")
public class TaxRatesController {

    private final TaxRatesService taxRatesService;
    private static final String TAXRATE_VIEW = "taxRates-view";
    private static final String TAXRATE_EDIT = "taxrates-edit";
    private static final String GENERAL_TAX_RESD = "GEN_TAX_RESD";
    private static final String GENERAL_TAX_NONRESD = "GEN_TAX_NR";
    private static final String EDUCATIONAL_TAX = "EDU_CESS";
    private static final String REDIRECT_URL = "redirect:/taxrates/appconfig/edit";
    private static final String REDIRECT_VIEW_URL = "redirect:/taxrates/appconfig/view";
    private static final String MESSAGE = "message";
    private static final String TOTAL_TAX_RESD = "TOT_RESD_TAX";
    private static final String TOTAL_TAX_NONRESD = "TOT_NR_RESD_TAX";
    private static final String TAX_RATES_FORM = "taxRatesForm";

    @Autowired
    public TaxRatesController(final TaxRatesService taxRatesService) {
        this.taxRatesService = taxRatesService;
    }

    @ModelAttribute
    public AppConfigValues taxRatesModel() {
        return new AppConfigValues();
    }

    @RequestMapping(value = "/appconfig/view", method = RequestMethod.GET)
    public String showAppConfigTaxRates(final Model model) {
        final Map<String, Double> taxRatesMap = taxRatesService.getTaxDetails();
        model.addAttribute("taxRates", taxRatesMap);
        return TAXRATE_VIEW;
    }

    @RequestMapping(value = "/appconfig/edit", method = RequestMethod.GET)
    public String viewTaxRates(final Model model) {
        final Map<String, Double> taxRatesMap = taxRatesService.getTaxDetails();
        model.addAttribute("taxRates", taxRatesMap);
        return TAXRATE_EDIT;
    }

    @RequestMapping(value = "/appconfig/edit", method = RequestMethod.POST)
    public String saveAppConfigTaxRates(@ModelAttribute final DemandReasonDetailsBean taxRatesForm, final Model model,
            final HttpServletRequest request, final RedirectAttributes redirectAttributes) {
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

    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public String showTaxRates(final Model model) {
        DemandReasonDetailsBean taxRatesForm = new DemandReasonDetailsBean();
        taxRatesForm.setDemandReasonDetails(taxRatesService.excludeOldTaxHeads(taxRatesService.getTaxRates()));
        model.addAttribute(TAX_RATES_FORM, taxRatesForm);
        addTotalTaxHeadsToModel(model);
        return "taxrates-view";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String taxRatesEditView(final Model model) {
        DemandReasonDetailsBean taxRatesForm = new DemandReasonDetailsBean();
        List<EgDemandReasonDetails> egDemandReasonDetails = taxRatesService.getTaxRates();
        taxRatesForm.setDemandReasonDetails(taxRatesService.excludeOldTaxHeads(egDemandReasonDetails));
        model.addAttribute("taxRatesForm", taxRatesForm);
        addTotalTaxHeadsToModel(model);
        return "taxrates-editview";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String saveTaxRates(@ModelAttribute final DemandReasonDetailsBean taxRatesForm, final Model model,
            final HttpServletRequest request, final RedirectAttributes redirectAttributes) {
        EgDemandReasonDetails existingDemandReasonDetails;
        for (EgDemandReasonDetails egDemandReasonDetail : taxRatesForm.getDemandReasonDetails()) {
            existingDemandReasonDetails = taxRatesService.getDemandReasonDetailsById(egDemandReasonDetail.getId());
            existingDemandReasonDetails.setPercentage(egDemandReasonDetail.getPercentage());
            taxRatesService.updateTaxRates(existingDemandReasonDetails);
        }
        redirectAttributes.addFlashAttribute(MESSAGE, "msg.update.taxrates");
        return "redirect:/taxrates/view";
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

    @SuppressWarnings("unused")
    private Boolean validate(final DemandReasonDetailsBean taxRatesForm, final Model model,
            final HttpServletRequest request) {
        EgDemandReasonDetails existingDemandReasonDetails;
        BigDecimal netResdPercentage = BigDecimal.ZERO;
        BigDecimal netNonResdPercentage = BigDecimal.ZERO;
        List<EgDemandReasonDetails> updatedTaxRatesForm = new ArrayList<>();
        for (EgDemandReasonDetails egDemandReasonDetail : taxRatesForm.getDemandReasonDetails()) {
            existingDemandReasonDetails = taxRatesService.getDemandReasonDetailsById(egDemandReasonDetail.getId());
            existingDemandReasonDetails.setPercentage(egDemandReasonDetail.getPercentage());
            updatedTaxRatesForm.add(existingDemandReasonDetails);
            if(existingDemandReasonDetails.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster().endsWith("Non Residential"))
                netNonResdPercentage = netNonResdPercentage.add(existingDemandReasonDetails.getPercentage());
            else 
                netResdPercentage = netResdPercentage.add(existingDemandReasonDetails.getPercentage());
        }
        if (new BigDecimal(request.getParameter("genTaxResd")).compareTo(netResdPercentage) != 0) {
            taxRatesForm.setDemandReasonDetails(updatedTaxRatesForm);
            model.addAttribute(MESSAGE, "msg.taxrate.resd.valid");
            model.addAttribute(TAX_RATES_FORM, taxRatesForm);
            addTotalTaxHeadsToModel(model);
            return false;
        } else if (new BigDecimal(request.getParameter("genTaxNonResd")).compareTo(netNonResdPercentage) != 0) {
            taxRatesForm.setDemandReasonDetails(updatedTaxRatesForm);
            model.addAttribute(MESSAGE, "msg.taxrate.nresd.valid");
            model.addAttribute(TAX_RATES_FORM, taxRatesForm);
            addTotalTaxHeadsToModel(model);
            return false;
        }
        return true;
    }

    private void addTotalTaxHeadsToModel(Model model) {
        BigDecimal totRsdTax=BigDecimal.ZERO;
        BigDecimal totNRsdTax=BigDecimal.ZERO;
        BigDecimal eduTax=BigDecimal.ZERO;
        for (EgDemandReasonDetails drd : taxRatesService.getTaxRates()) {
            if (drd.getEgDemandReason().getEgDemandReasonMaster().getCode().equals(TOTAL_TAX_RESD))
                totRsdTax=drd.getPercentage();
            if (drd.getEgDemandReason().getEgDemandReasonMaster().getCode().equals(TOTAL_TAX_NONRESD))
                totNRsdTax=drd.getPercentage();
            if(drd.getEgDemandReason().getEgDemandReasonMaster().getCode().equals(EDUCATIONAL_TAX))
                eduTax=drd.getPercentage();
        }
        totRsdTax=totRsdTax.add(eduTax);
        totNRsdTax=totNRsdTax.add(eduTax);
        model.addAttribute("genTaxResd", totRsdTax.setScale(2, BigDecimal.ROUND_CEILING));
        model.addAttribute("genTaxNonResd", totNRsdTax.setScale(2, BigDecimal.ROUND_CEILING));
    }
}
