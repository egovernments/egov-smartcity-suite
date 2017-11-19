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
package org.egov.wtms.web.controller.masters;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.validation.Valid;

import org.egov.wtms.masters.entity.MeteredRates;
import org.egov.wtms.masters.entity.MeteredRatesDetail;
import org.egov.wtms.masters.service.MeteredRatesDetailComparatorById;
import org.egov.wtms.masters.service.MeteredRatesService;
import org.egov.wtms.masters.service.UsageSlabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/masters")
public class MeteredRatesMasterController {

    private static final String SLABNAME_LIST = "slabNameList";
    private static final String METERED_RATES = "meteredRates";
    private static final String METERED_RATE_CREATE = "metered-rate-create";

    @Autowired
    private MeteredRatesService meteredRatesService;

    @Autowired
    private UsageSlabService usageSlabService;

    @RequestMapping(value = "/metered-rate-create", method = GET)
    public String createMeteredRate(@ModelAttribute final MeteredRates meteredRates, final Model model) {
        model.addAttribute(SLABNAME_LIST, usageSlabService.getActiveUsageSlabs());
        model.addAttribute(METERED_RATES, meteredRates);
        return METERED_RATE_CREATE;
    }

    @RequestMapping(value = "/metered-rate-create/{slabName}", method = GET)
    public String viewMeteredRateForm(@PathVariable("slabName") final String slabName,
            final Model model) {
        final MeteredRatesDetailComparatorById rateDetailComparatorById = new MeteredRatesDetailComparatorById();
        model.addAttribute(SLABNAME_LIST, usageSlabService.getActiveUsageSlabs());
        model.addAttribute("mode", "edit");
        model.addAttribute("usageSlab", usageSlabService.findBySlabName(slabName));
        MeteredRates meteredRatesObj = meteredRatesService.findBySlabName(slabName);
        if (meteredRatesObj != null) {
            final List<MeteredRatesDetail> rateList = new ArrayList<>();
            for (final MeteredRatesDetail rates : meteredRatesObj.getRatesDetail())
                if (rates.getId() != null)
                    rateList.add(rates);
            meteredRatesObj.setRatesDetail(rateList);
            Collections.sort(meteredRatesObj.getRatesDetail(), rateDetailComparatorById);
        } else {
            meteredRatesObj = new MeteredRates();
            final List<MeteredRatesDetail> tempList = new ArrayList<>();
            meteredRatesObj.setSlabName(slabName);
            meteredRatesObj.setRatesDetail(tempList);
        }
        model.addAttribute(METERED_RATES, meteredRatesObj);

        return METERED_RATE_CREATE;
    }

    @RequestMapping(value = "/metered-rate-create", method = POST)
    public String saveMeteredRates(@Valid @ModelAttribute(METERED_RATES) final MeteredRates meteredRates,
            final BindingResult errors,
            final Model model) {

        if (errors.hasErrors()) {
            model.addAttribute(SLABNAME_LIST, usageSlabService.getActiveUsageSlabs());
            model.addAttribute(METERED_RATES, meteredRates);
            return METERED_RATE_CREATE;
        }

        for (final MeteredRatesDetail detail : meteredRates.getRatesDetail())
            if (detail.getId() == null)
                detail.setMeteredRate(meteredRates);
        meteredRatesService.save(meteredRates);
        model.addAttribute("message", "Metered Rates Created Successfully");
        return "metered-rate-success";
    }

    @RequestMapping(value = "/metered-rate-view", method = GET)
    public String getMeteredRates(@ModelAttribute final MeteredRates meteredRates, final Model model) {
        model.addAttribute(SLABNAME_LIST, usageSlabService.getActiveUsageSlabs());
        return "metered-rate-search";
    }

    @RequestMapping(value = "/metered-rate-search/{slabName}", method = GET)
    public String viewSearchResult(@PathVariable("slabName") final String slabName, final Model model) {
        final MeteredRates meteredRates = meteredRatesService.findBySlabName(slabName);
        model.addAttribute(SLABNAME_LIST, usageSlabService.getActiveUsageSlabs());
        if (meteredRates != null)
            model.addAttribute("meteredRates", meteredRates);
        else {
            final MeteredRates meterRates = new MeteredRates();
            meterRates.setSlabName(slabName);
            model.addAttribute("meteredRates", meterRates);
        }
        model.addAttribute("mode", "view");
        return "metered-rate-search";
    }

}
