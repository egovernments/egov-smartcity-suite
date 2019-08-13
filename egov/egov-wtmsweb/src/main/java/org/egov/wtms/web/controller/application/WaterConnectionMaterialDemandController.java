/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
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

import static org.egov.infra.utils.ApplicationConstant.NO;
import static org.egov.infra.utils.ApplicationConstant.YES;
import static org.egov.infra.utils.JsonUtils.toJSON;
import static org.egov.wtms.utils.constants.WaterTaxConstants.REVENUE_HIERARCHY_TYPE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.REVENUE_WARD;
import static org.egov.wtms.utils.constants.WaterTaxConstants.NEWCONNECTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.ADDNLCONNECTION;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.wtms.application.entity.WaterConnExecutionDetails;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.service.ConnectionDemandService;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.masters.entity.ApplicationType;
import org.egov.wtms.masters.entity.ConnectionAddress;
import org.egov.wtms.masters.service.ApplicationTypeService;
import org.egov.wtms.reports.entity.ExecuteWaterConnectionAdaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/application/material-demand")
public class WaterConnectionMaterialDemandController {

    @Autowired
    private ApplicationTypeService applicationTypeService;

    @Autowired
    private BoundaryService boundaryService;

    @Autowired
    private WaterConnectionDetailsService waterConnectionDetailsService;

    @Autowired
    private ConnectionDemandService connectionDemandService;

    @GetMapping(value = "/search")
	public String searchApplications(final Model model) {
		List<ApplicationType> applicationTypes = new ArrayList<>();
		applicationTypes.add(applicationTypeService.findByCode(NEWCONNECTION));
		applicationTypes.add(applicationTypeService.findByCode(ADDNLCONNECTION));
		model.addAttribute("waterApplicationDetails", new WaterConnExecutionDetails());
		model.addAttribute("applicationTypeList", applicationTypes);
		model.addAttribute("revenueWardList", boundaryService
				.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(REVENUE_WARD, REVENUE_HIERARCHY_TYPE));
		return "material-demand-search-form";
	}

    @PostMapping(value = "/search", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String getSearchResult(@ModelAttribute final WaterConnExecutionDetails waterApplicationDetails, final Model model) {
        final List<ConnectionAddress> searchResultList = waterConnectionDetailsService
                .getSearchResultList(waterApplicationDetails);

        return new StringBuilder(" { \"data\" : ")
                .append(toJSON(waterConnectionDetailsService.getApplicationObjectList(searchResultList),
                        WaterConnExecutionDetails.class, ExecuteWaterConnectionAdaptor.class))
                .append("}")
                .toString();
    }

    @GetMapping(value = "/update/{applicationnumber}")
    public String viewApplicationDetails(@PathVariable("applicationnumber") final String applicationNumber, final Model model) {
        final WaterConnectionDetails waterConnectionDetails = waterConnectionDetailsService
                .findByApplicationNumber(applicationNumber);
        if (waterConnectionDetails != null)
            model.addAttribute("waterConnectionDetails", waterConnectionDetails);
        model.addAttribute("ulbMaterialDropdownValues", Arrays.asList(YES, NO));
        model.addAttribute("waterApplicationDetails", new WaterConnExecutionDetails());
        return "material-demand-view-application";
    }

    @PostMapping(value = "/update/{applicationnumber}")
    public String updateApplicationDetails(@PathVariable("applicationnumber") final String applicationNumber,
            @ModelAttribute final WaterConnectionDetails waterConnectionDetails, final Model model) {
        model.addAttribute("message", connectionDemandService.updateUlbMaterial(applicationNumber, waterConnectionDetails));
        model.addAttribute("mode", "success");
        model.addAttribute("waterConnectionDetails", applicationNumber != null
                ? waterConnectionDetailsService.findByApplicationNumber(applicationNumber) : waterConnectionDetails);
        return "material-demand-view-application";
    }

}
