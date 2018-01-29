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

package org.egov.wtms.web.controller.application;

import static org.egov.infra.utils.JsonUtils.toJSON;
import static org.egov.wtms.utils.constants.WaterTaxConstants.REVENUE_HIERARCHY_TYPE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.REVENUE_WARD;

import java.util.ArrayList;
import java.util.List;

import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.wtms.application.entity.WaterConnExecutionDetails;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.entity.WaterConnectionExecutionResponse;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.masters.entity.MeterCost;
import org.egov.wtms.masters.service.ApplicationTypeService;
import org.egov.wtms.masters.service.MeterCostService;
import org.egov.wtms.reports.entity.ExecuteWaterConnectionAdaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/application/execute-update")
public class UpdateWaterConnectionExecutionController {

    private static final String ERR_WATER_RATES_NOT_DEFINED = "WaterRatesNotDefined";

    @Autowired
    private WaterConnectionDetailsService waterConnectionDetailsService;

    @Autowired
    private BoundaryService boundaryService;

    @Autowired
    private ApplicationTypeService applicationTypeService;

    @Autowired
    private MeterCostService meterCostService;

    @GetMapping(value = "/search")
    public String getSearchScreen(final Model model) {
        model.addAttribute("executeWaterApplicationDetails", new WaterConnExecutionDetails());
        model.addAttribute("applicationTypeList", applicationTypeService.getActiveApplicationTypes());
        model.addAttribute("revenueWardList",
                boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(REVENUE_WARD, REVENUE_HIERARCHY_TYPE));
        return "execute-update-search";
    }

    @GetMapping(value = "/search-form")
    public String getSearchForm(final Model model) {
        model.addAttribute("executeWaterApplicationDetails", new WaterConnExecutionDetails());
        model.addAttribute("applicationTypeList", applicationTypeService.getActiveApplicationTypes());
        model.addAttribute("revenueWardList",
                boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(REVENUE_WARD, REVENUE_HIERARCHY_TYPE));
        return "execute-search-screen";
    }

    @ModelAttribute
    public WaterConnExecutionDetails executeWaterApplicationDetails() {
        return new WaterConnExecutionDetails();
    }

    @PostMapping(value = "/search", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String searchResult(final WaterConnExecutionDetails executeWaterApplicationDetails) {
        final List<Object[]> detailList = waterConnectionDetailsService.getApplicationResultList(executeWaterApplicationDetails);
        return new StringBuilder(" { \"data\" : ")
                .append(toJSON(waterConnectionDetailsService.getConnExecutionObjectList(detailList),
                        WaterConnExecutionDetails.class,
                        ExecuteWaterConnectionAdaptor.class))
                .append("}")
                .toString();
    }

    @PostMapping(value = "/search-form", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String getMeteredApplicationSearchResult(final WaterConnExecutionDetails executeWaterApplicationDetails) {
        final List<Object[]> detailList = waterConnectionDetailsService.getMeteredApplicationList(executeWaterApplicationDetails);
        return new StringBuilder(" { \"data\" : ")
                .append(toJSON(waterConnectionDetailsService.getConnExecutionObjectList(detailList),
                        WaterConnExecutionDetails.class,
                        ExecuteWaterConnectionAdaptor.class))
                .append("}")
                .toString();
    }

    @PostMapping(value = "/result", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getSearchResult(@RequestBody final WaterConnectionExecutionResponse waterApplicationDetails) {
        final List<WaterConnectionDetails> connectionDetailsList = new ArrayList<>();
        final String validationStatus = waterConnectionDetailsService.validateInput(waterApplicationDetails,
                connectionDetailsList);
        if (ERR_WATER_RATES_NOT_DEFINED.equalsIgnoreCase(validationStatus))
            return ERR_WATER_RATES_NOT_DEFINED;
        final Boolean updateStatus = waterConnectionDetailsService.updateStatus(connectionDetailsList);
        return waterConnectionDetailsService.getResultStatus(waterApplicationDetails, validationStatus, updateStatus);
    }

    @GetMapping(value = "/search-result", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<MeterCost> getMeterDetails() {
        return meterCostService.findAll();
    }

    @PostMapping(value = "/search-result", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String updateMeterDetails(@RequestBody final WaterConnectionExecutionResponse executeWaterApplicationDetails) {
        final List<WaterConnectionDetails> applicationList = new ArrayList<>();
        final String validationResult = waterConnectionDetailsService.validateMeterDetails(executeWaterApplicationDetails,
                applicationList);
        final Boolean status = waterConnectionDetailsService.updateMeterDetails(applicationList);
        return waterConnectionDetailsService.getResultStatus(executeWaterApplicationDetails, validationResult, status);
    }
}
