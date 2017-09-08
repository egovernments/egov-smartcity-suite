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
package org.egov.ptis.web.controller.masters.rebatePeriod;

import org.egov.commons.Installment;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.domain.entity.property.RebatePeriod;
import org.egov.ptis.domain.service.property.RebatePeriodService;
import org.egov.ptis.service.utils.PropertyTaxCommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "/rebatePeriod/create")
public class CreateRebatePeriodController {

    private RebatePeriodService rebatePeriodService;
    private PropertyTaxUtil propertyTaxUtil;
    @Autowired
    private PropertyTaxCommonUtils propertyTaxCommonUtils;

    @Autowired
    public CreateRebatePeriodController(RebatePeriodService rebatePeriodService, PropertyTaxUtil propertyTaxUtil) {
        this.propertyTaxUtil = propertyTaxUtil;
        this.rebatePeriodService = rebatePeriodService;
    }

    @ModelAttribute
    public RebatePeriod rebatePeriodModel() {
        Installment currentInstallment = propertyTaxCommonUtils.getCurrentInstallment();
        RebatePeriod rebatePeriod = null;
        if (currentInstallment != null) {
            rebatePeriod = rebatePeriodService.getRebateForCurrInstallment(currentInstallment.getId());
            if (rebatePeriod != null) {
            } else {
                rebatePeriod = new RebatePeriod();
                rebatePeriod.setInstallment(currentInstallment);
            }
        }
        return rebatePeriod;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String newForm(final Model model) {
        return "rebatePeriod-form";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute RebatePeriod rebatePeriod, final BindingResult errors,
            RedirectAttributes redirectAttrs, final Model model) {

        if (errors.hasErrors())
            return "rebatePeriod-form";

        rebatePeriodService.saveRebatePeriod(rebatePeriod);
        model.addAttribute("message", "Rebate period saved successfully !");

        return "rebatePeriod-success";
    }

}
