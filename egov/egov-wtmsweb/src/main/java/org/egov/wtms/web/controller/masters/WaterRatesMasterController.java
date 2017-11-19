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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.egov.wtms.masters.entity.WaterRatesDetails;
import org.egov.wtms.masters.entity.WaterRatesHeader;
import org.egov.wtms.masters.entity.enums.ConnectionType;
import org.egov.wtms.masters.service.PipeSizeService;
import org.egov.wtms.masters.service.UsageTypeService;
import org.egov.wtms.masters.service.WaterRatesDetailsService;
import org.egov.wtms.masters.service.WaterRatesHeaderService;
import org.egov.wtms.masters.service.WaterSourceService;
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
public class WaterRatesMasterController {

    @Autowired
    private WaterSourceService waterSourceService;

    @Autowired
    private UsageTypeService usageTypeService;

    @Autowired
    private WaterRatesHeaderService waterRatesHeaderService;

    @Autowired
    private WaterRatesDetailsService waterRatesDetailsService;

    @Autowired
    private PipeSizeService pipeSizeService;

    @RequestMapping(value = "/waterRatesMaster", method = RequestMethod.GET)
    public String viewForm(final Model model) {
        final WaterRatesHeader waterRatesHeader = new WaterRatesHeader();
        if (waterRatesHeader.getWaterRatesDetails().isEmpty())
            waterRatesHeader.addWaterRatesDetails(new WaterRatesDetails());
        waterRatesHeader.setConnectionType(ConnectionType.NON_METERED);
        model.addAttribute("waterRatesHeader", waterRatesHeader);
        model.addAttribute("waterRatesConnecionType", waterRatesHeader.getConnectionType());
        model.addAttribute("typeOfConnection", "WATERRATES");
        model.addAttribute("usageType", usageTypeService.getActiveUsageTypes());
        model.addAttribute("maxPipeSize", pipeSizeService.getAllActivePipeSize());
        model.addAttribute("waterSourceTypes", waterSourceService.getAllActiveWaterSourceTypes());
        model.addAttribute("reqAttr", "false");
        model.addAttribute("mode", "create");
        return "waterRates-master";
    }

    @RequestMapping(value = "/waterRatesMaster", method = RequestMethod.POST)
    public String createWaterRatesMasterDetails(@Valid @ModelAttribute WaterRatesHeader waterRatesHeader,
            final RedirectAttributes redirectAttrs, final Model model, final BindingResult resultBinder) {
        if (resultBinder.hasErrors())
            return "waterRates-master";
        final List<WaterRatesHeader> waterRatesHeaderTempList = waterRatesHeaderService
                .findByConnectionTypeAndUsageTypeAndWaterSourceAndPipeSize(waterRatesHeader.getConnectionType(),
                        waterRatesHeader.getUsageType(), waterRatesHeader.getWaterSource(),
                        waterRatesHeader.getPipeSize());
        WaterRatesDetails waterRatesDetailsTemp = null;
        WaterRatesHeader waterRatesHeaderTemp = null;
        for (final WaterRatesHeader waterRatesHeadertemp : waterRatesHeaderTempList) {
            waterRatesDetailsTemp = waterRatesDetailsService.findByWaterRatesHeaderAndFromDateAndToDate(
                    waterRatesHeadertemp, waterRatesHeader.getWaterRatesDetails().get(0).getFromDate(),
                    waterRatesHeader.getWaterRatesDetails().get(0).getToDate());
            if (waterRatesDetailsTemp != null) {
                waterRatesHeaderTemp = waterRatesHeaderTempList.get(0);
                break;
            }
        }
        if (waterRatesDetailsTemp != null) {
            redirectAttrs.addFlashAttribute("waterRatesHeader", waterRatesHeaderTemp);
            model.addAttribute("message", "Monthly Rent for Non-Meter Master Data already exists.");
            viewForm(model);
            return "waterRates-master";
        } else {
            waterRatesHeader.setActive(true);
            waterRatesHeader = buildWaterRateDetails(waterRatesHeader, waterRatesHeader.getWaterRatesDetails());
            waterRatesHeaderService.createWaterRatesHeader(waterRatesHeader);
            model.addAttribute("mode", "create");
            redirectAttrs.addFlashAttribute("waterRatesHeader", waterRatesHeader);
            model.addAttribute("message", "Monthly Rent for Non-Meter Master Data created successfully.");
        }

        return "waterRates-master-success";
    }

    @RequestMapping(value = "/waterRatesMaster/list", method = RequestMethod.GET)
    public String getWaterRatesMasterList(final Model model) {
        final List<WaterRatesHeader> waterRatesHeaderList = waterRatesHeaderService.findAllByConnectionType(ConnectionType.NON_METERED);
        model.addAttribute("waterRatesHeaderList", waterRatesHeaderList);
        return "waterRates-master-list";

    }

    @RequestMapping(value = "/waterRatesMaster/edit", method = RequestMethod.GET)
    public String getWaterRatesMaster(final Model model) {
        model.addAttribute("mode", "edit");
        return getWaterRatesMasterList(model);
    }

    @RequestMapping(value = "/waterRatesMaster/edit/{waterRatesHeaderid}", method = RequestMethod.GET)
    public String getWaterRatesMasterData(final Model model, @PathVariable final String waterRatesHeaderid) {
        final WaterRatesHeader waterRatesHeader = waterRatesHeaderService.findBy(Long.parseLong(waterRatesHeaderid));
        waterRatesHeader.setConnectionType(ConnectionType.NON_METERED);
        model.addAttribute("typeOfConnection", "WATERRATES");
        model.addAttribute("waterRatesHeader", waterRatesHeader);
        model.addAttribute("waterRatesConnecionType", waterRatesHeader.getConnectionType());
        model.addAttribute("usageType", usageTypeService.getActiveUsageTypes());
        model.addAttribute("maxPipeSize", pipeSizeService.getAllActivePipeSize());
        model.addAttribute("waterSourceTypes", waterSourceService.getAllActiveWaterSourceTypes());
        model.addAttribute("reqAttr", "true");
        return "waterRates-master";

    }

    @RequestMapping(value = "/waterRatesMaster/edit/{waterRatesHeaderid}", method = RequestMethod.POST)
    public String editWaterRatesMasterData(@Valid @ModelAttribute WaterRatesHeader waterRatesHeader,
            final BindingResult resultBinder, final RedirectAttributes redirectAttrs,
            @PathVariable final Long waterRatesHeaderid, final Model model) {
        if (resultBinder.hasErrors())
            return "waterRates-master";

        final WaterRatesHeader waterRatesHeaderTemp = waterRatesHeaderService.findBy(waterRatesHeaderid);
        WaterRatesDetails waterRatesDetailsTemp = null;

        waterRatesDetailsTemp = waterRatesDetailsService.findByWaterRatesHeaderAndFromDateAndToDate(
                waterRatesHeaderTemp, waterRatesHeader.getWaterRatesDetails().get(0).getFromDate(),
                waterRatesHeader.getWaterRatesDetails().get(0).getToDate());

        if (waterRatesDetailsTemp != null) {
            waterRatesHeaderTemp.setUsageType(waterRatesHeader.getUsageType());
            waterRatesHeaderTemp.setPipeSize(waterRatesHeader.getPipeSize());
            waterRatesHeaderTemp.setWaterSource(waterRatesHeader.getWaterSource());
            waterRatesHeaderTemp.setActive(waterRatesHeader.isActive());
            waterRatesHeader = updateWateRatesetails(waterRatesHeaderTemp, waterRatesHeader.getWaterRatesDetails());

        } else
            waterRatesHeader = buildWaterRateDetails(waterRatesHeader, waterRatesHeader.getWaterRatesDetails());
        waterRatesHeaderService.updateWaterRatesHeader(waterRatesHeader);
        redirectAttrs.addFlashAttribute("waterRatesHeader", waterRatesHeader);
        model.addAttribute("message", "Monthly Rent for Non-Meter Master Data updated successfully.");
        return "waterRates-master-success";
    }

    private WaterRatesHeader buildWaterRateDetails(final WaterRatesHeader waterRatesHeader,
            final List<WaterRatesDetails> unitDetail) {
        final Set<WaterRatesDetails> unitSet = new HashSet<WaterRatesDetails>(0);

        for (final WaterRatesDetails unitdetail : unitDetail)
            if (unitdetail.getFromDate() != null && unitdetail.getToDate() != null
                    && !"".equals(unitdetail.getMonthlyRate())) {
                unitdetail.setWaterRatesHeader(waterRatesHeader);

                unitSet.add(unitdetail);

                waterRatesHeader.getWaterRatesDetails().clear();

                waterRatesHeader.getWaterRatesDetails().addAll(unitSet);
            }
        return waterRatesHeader;
    }

    private WaterRatesHeader updateWateRatesetails(final WaterRatesHeader waterRatesHeader,
            final List<WaterRatesDetails> newWaterRatesList) {
        final List<WaterRatesDetails> unitWaterRatesList = new ArrayList<>(0);
        for (final WaterRatesDetails waterRatesOld : waterRatesHeader.getWaterRatesDetails())
            for (final WaterRatesDetails waterRatesNew : newWaterRatesList)
                if (waterRatesNew.getFromDate() != null && waterRatesNew.getToDate() != null
                        && !"".equals(waterRatesNew.getMonthlyRate())) {
                    waterRatesOld.setWaterRatesHeader(waterRatesHeader);
                    waterRatesOld.setMonthlyRate(waterRatesNew.getMonthlyRate());
                    waterRatesOld.setFromDate(waterRatesNew.getFromDate());
                    waterRatesOld.setToDate(waterRatesNew.getToDate());
                    unitWaterRatesList.add(waterRatesOld);
                }
        waterRatesHeader.setWaterRatesDetails(unitWaterRatesList);
        return waterRatesHeader;
    }

}