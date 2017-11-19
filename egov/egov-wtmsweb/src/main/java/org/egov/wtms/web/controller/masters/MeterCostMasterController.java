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

import java.util.List;

import javax.validation.Valid;

import org.egov.wtms.masters.entity.MeterCost;
import org.egov.wtms.masters.service.MeterCostService;
import org.egov.wtms.masters.service.PipeSizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/masters")
public class MeterCostMasterController {

    private final PipeSizeService pipeSizeService;

    private final MeterCostService meterCostService;

    @Autowired
    public MeterCostMasterController(final PipeSizeService pipeSizeService, final MeterCostService meterCostService) {
        this.pipeSizeService = pipeSizeService;
        this.meterCostService = meterCostService;
    }

    @RequestMapping(value = "/meterCostMaster", method = RequestMethod.GET)
    public String viewForm(final Model model) {
        final MeterCost meterCost = new MeterCost();
        model.addAttribute("meterCost", meterCost);
        model.addAttribute("pipeSize", pipeSizeService.getAllActivePipeSize());
        model.addAttribute("reqAttr", "false");
        model.addAttribute("mode", "create");
        return "meter-cost-master";
    }

    @RequestMapping(value = "/meterCostMaster", method = RequestMethod.POST)
    public String createMeterCostMasterData(@Valid @ModelAttribute final MeterCost meterCost,
            final BindingResult errors, final RedirectAttributes redirectAttrs, final Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("pipeSize", pipeSizeService.getAllActivePipeSize());
            return "meter-cost-master";
        } else

            meterCostService.createMeterCost(meterCost);
        redirectAttrs.addFlashAttribute("meterCost", meterCost);
        model.addAttribute("message", "Meter Cost created successfully.");
        model.addAttribute("mode", "create");
        return "meter-cost-master-success";
    }

    @RequestMapping(value = "/meterCostMaster/list", method = RequestMethod.GET)
    public String getMeterCostMasterList(final Model model) {
        final List<MeterCost> meterCostList = meterCostService.findAll();
        model.addAttribute("meterCostList", meterCostList);
        return "meter-cost-master-list";
    }

    @RequestMapping(value = "/meterCostMaster/edit", method = RequestMethod.GET)
    public String getMeterCostMaster(final Model model) {
        model.addAttribute("mode", "edit");
        return getMeterCostMasterList(model);
    }

    @RequestMapping(value = "/meterCostMaster/edit/{meterCostId}", method = RequestMethod.GET)
    public String getMeterCostMasterDetails(final Model model, @PathVariable final String meterCostId) {
        final MeterCost meterCost = meterCostService.findOne(Long.parseLong(meterCostId));
        model.addAttribute("meterCost", meterCost);
        model.addAttribute("pipeSize", pipeSizeService.getAllActivePipeSize());
        model.addAttribute("reqAttr", "true");
        return "meter-cost-master";
    }

    @RequestMapping(value = "/meterCostMaster/edit/{meterCostId}", method = RequestMethod.POST)
    public String editMeterCostMasterData(@Valid @ModelAttribute final MeterCost meterCost, final BindingResult errors,
            final RedirectAttributes redirectAttrs, final Model model, @PathVariable final long meterCostId) {
        if (errors.hasErrors()) {
            model.addAttribute("pipeSize", pipeSizeService.getAllActivePipeSize());
            return "meter-cost-master";
        } else
            meterCostService.updateMeterCost(meterCost);
        redirectAttrs.addFlashAttribute("meterCost", meterCost);
        model.addAttribute("message", "Meter Cost updated successfully.");
        return "meter-cost-master-success";
    }
}
