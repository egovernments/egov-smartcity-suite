/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

        1) All versions of this program, verbatim or modified must carry this
           Legal Notice.

        2) Any misrepresentation of the origin of the material is prohibited. It
           is required that all modified versions of this material be marked in
           reasonable ways as different from the original version.

        3) This license does not grant any rights to any user of the program
           with regards to rights under trademark law for use of the trade names
           or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.pgr.web.controller.dashboard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.egov.pgr.service.dashboard.DashboardService;
import org.egov.pgr.web.contract.DataTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @RequestMapping("/home")
    public String home() {
        return "dashboard/home";
    }

    @RequestMapping(value = "/reg-resolution-trend", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<Collection<Integer>> registrationResolutionTrend() {
        final List<Collection<Integer>> regResTrendData = new ArrayList<>();
        regResTrendData.add(dashboardService.getComplaintRegistrationTrend());
        regResTrendData.add(dashboardService.getComplaintResolutionTrend());
        return regResTrendData;
    }

    @RequestMapping(value = "/monthly-aggregate", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<Map<String, Object>> monthlyAggregate() {
        return dashboardService.getMonthlyAggregate();
    }

    @RequestMapping(value = "/typewise-aggregate", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<Map<String, Object>> complaintTypewiseAggregate() {
        return dashboardService.getCompTypewiseAggregate();
    }

    @RequestMapping(value = "/wardwise-performance", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<Object> wardwisePerformance() {
        final List<Object> wardwisePerformanceData = new ArrayList<>();
        final List<Map<String, Object>> wardwisePerformance = dashboardService.getWardwisePerformance();
        wardwisePerformanceData.add(wardwisePerformance);
        final int size = wardwisePerformance.size();
        wardwisePerformanceData.add(new DataTable(0, size, size, size, wardwisePerformance));
        return wardwisePerformanceData;
    }
}
