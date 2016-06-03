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

import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.ptis.master.service.TaxRatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.egov.ptis.constants.PropertyTaxConstants.TAX_RATES;
import static org.egov.ptis.constants.PropertyTaxConstants.TAX_RATES_TEMP;

@Controller
@RequestMapping(value = "/taxrates/view")
public class ViewTaxRatesController {

    private final TaxRatesService taxRatesService;

    @Autowired
    public ViewTaxRatesController(final TaxRatesService taxRatesService) {
        this.taxRatesService = taxRatesService;
    }

    @ModelAttribute
    public AppConfigValues taxRatesModel() {
        return new AppConfigValues();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showTaxRates(Model model) {
        List<AppConfigValues> taxRates = taxRatesService.getAllTaxRates();
        Map<String, Double> taxRatesMap = new HashMap<String, Double>();
        for (AppConfigValues appConfig : taxRates) {
            String rows[] = appConfig.getValue().split("\n");
            for (String row : rows) {
                String value[] = row.split("=");
                if (!TAX_RATES_TEMP.contains(value[0])) {
                    taxRatesMap.put(TAX_RATES.get(value[0]), Double.parseDouble(value[1]));
                }
            }

        }
        Map<String, Double> taxRatesDisplayMap = new LinkedHashMap<String, Double>();
        taxRatesDisplayMap.put("General Tax Residential", taxRatesMap.get("General Tax Residential"));
        taxRatesDisplayMap.put("General Tax Non Residential", taxRatesMap.get("General Tax Non Residential"));
        taxRatesDisplayMap.put("Education Cess", taxRatesMap.get("Education Cess"));
        taxRatesDisplayMap.put("Vacant Land Tax", taxRatesMap.get("Vacant Land Tax"));
        taxRatesDisplayMap.put("Library Cess", taxRatesMap.get("Library Cess"));
        model.addAttribute("taxRates", taxRatesDisplayMap);
        return "taxRates-view";
    }

}
