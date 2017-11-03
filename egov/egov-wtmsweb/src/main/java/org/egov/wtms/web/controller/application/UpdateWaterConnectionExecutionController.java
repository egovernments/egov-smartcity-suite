/*
 * eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) <2017>  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *      Further, all user interfaces, including but not limited to citizen facing interfaces,
 *         Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *         derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *      For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *      For any further queries on attribution, including queries on brand guidelines,
 *         please contact contact@egovernments.org
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.wtms.web.controller.application;

import static org.egov.wtms.utils.constants.WaterTaxConstants.REVENUE_HIERARCHY_TYPE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.REVENUE_WARD;

import java.util.ArrayList;
import java.util.List;

import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.wtms.application.entity.WaterConnExecutionDetails;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.entity.WaterConnectionExecutionResponse;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.masters.service.ApplicationTypeService;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Controller
@RequestMapping(value = "/application/execute-update")
public class UpdateWaterConnectionExecutionController {

    private static final String EMPTY_LIST = "EmptyList";
    private static final String UPDATE_FAILED = "UpdateExecutionFailed";
    private static final String SUCCESS = "Success";

    @Autowired
    private WaterConnectionDetailsService waterConnectionDetailsService;

    @Autowired
    private BoundaryService boundaryService;

    @Autowired
    private ApplicationTypeService applicationTypeService;

    @Autowired
    private ExecuteWaterConnectionAdaptor executeWaterApplicationAdaptor;

    @GetMapping(value = "/search")
    public String getSearchScreen(final Model model) {
        model.addAttribute("executeWaterApplicationDetails", new WaterConnExecutionDetails());
        model.addAttribute("applicationTypeList", applicationTypeService.getActiveApplicationTypes());
        model.addAttribute("revenueWardList",
                boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(REVENUE_WARD, REVENUE_HIERARCHY_TYPE));
        return "execute-update-search";
    }

    @ModelAttribute
    public WaterConnExecutionDetails executeWaterApplicationDetails() {
        return new WaterConnExecutionDetails();
    }

    @PostMapping(value = "/search", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String searchResult(final WaterConnExecutionDetails executeWaterApplicationDetails) {
        final List<WaterConnExecutionDetails> resultList = new ArrayList<>();
        final List<Object[]> detailList = waterConnectionDetailsService.getApplicationResultList(executeWaterApplicationDetails);

        for (final Object[] resultObject : detailList) {
            final WaterConnExecutionDetails details = new WaterConnExecutionDetails();
            if (resultObject[0] != null)
                details.setApplicationNumber(resultObject[0].toString());
            if (resultObject[1] != null)
                details.setConsumerNumber(resultObject[1].toString());
            if (resultObject[2] != null)
                details.setOwnerName(resultObject[2].toString());
            if (resultObject[3] != null)
                details.setApplicationType(resultObject[3].toString());
            if (resultObject[4] != null)
                details.setApplicationStatus(resultObject[4].toString());
            if (resultObject[5] != null)
                details.setApprovalDate(resultObject[5].toString());
            if (resultObject[6] != null)
                details.setRevenueWard(resultObject[6].toString());
            if (resultObject[7] != null)
                details.setId(Long.parseLong(resultObject[7].toString()));
            resultList.add(details);
        }

        return new StringBuilder(" { \"data\" : ").append(getJsonData(resultList)).append("}").toString();
    }

    @PostMapping(value = "/result", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getSearchResult(@RequestBody final WaterConnectionExecutionResponse waterApplicationDetails) {
        final List<WaterConnectionDetails> connectionDetailsList = new ArrayList<>();
        final String validationStatus = waterConnectionDetailsService.validateDate(waterApplicationDetails,
                connectionDetailsList);
        final Boolean updateStatus = waterConnectionDetailsService.updateStatus(connectionDetailsList);
        String response;
        if (waterApplicationDetails.getExecuteWaterApplicationDetails().length <= 0)
            response = EMPTY_LIST;
        else if (!validationStatus.isEmpty())
            response = validationStatus;
        else if (!updateStatus)
            response = UPDATE_FAILED;
        else
            response = SUCCESS;
        return response;
    }

    public Object getJsonData(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(WaterConnExecutionDetails.class, executeWaterApplicationAdaptor)
                .create();
        return gson.toJson(object);
    }

}
