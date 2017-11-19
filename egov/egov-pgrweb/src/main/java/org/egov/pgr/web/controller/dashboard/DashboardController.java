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

package org.egov.pgr.web.controller.dashboard;

import org.egov.infra.web.support.ui.DataTable;
import org.egov.pgr.dashboard.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
    @ResponseBody
    public List<Collection<Integer>> registrationResolutionTrend() {
        final List<Collection<Integer>> regResTrendData = new ArrayList<>();
        regResTrendData.add(dashboardService.getComplaintRegistrationTrend());
        regResTrendData.add(dashboardService.getComplaintResolutionTrend());
        return regResTrendData;
    }

    @RequestMapping(value = "/monthly-aggregate", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Map<String, Object>> monthlyAggregate() {
        return dashboardService.getMonthlyAggregate();
    }

    @RequestMapping(value = "/typewise-aggregate", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Map<String, Object>> complaintTypewiseAggregate() {
        return dashboardService.getCompTypewiseAggregate();
    }

    @RequestMapping(value = "/ageing/{ward}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<List<Object>> wardwiseAgeing(@PathVariable final String ward) {
        return dashboardService.getAgeingByWard(ward);
    }

    @RequestMapping(value = "/wardwise-performance", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Object> wardwisePerformance() {
        final List<List<Map<String, Object>>> wardwisePerformance = dashboardService.getWardwisePerformance();
        final List<Object> performanceData = new LinkedList<>();
        performanceData.add(wardwisePerformance.get(0));
        final int size = wardwisePerformance.size();
        performanceData.add(new DataTable(0, size, size, size, wardwisePerformance.get(0)));
        performanceData.add(wardwisePerformance.get(1));
        return performanceData;
    }

    @RequestMapping(value = "/sla/{charttype}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List complaintSLA(@PathVariable final String charttype) {
        if ("pie".equals(charttype))
            return dashboardService.getComplaintSLA();
        else if ("gis".equals(charttype))
            return dashboardService.getOpenComplaintSLA();
        return Collections.emptyList();
    }

    @RequestMapping(value = "/wardwise-complaint-by-type/{typeid}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Map<String, Object>> wardwiseComplaintByComplaintType(@PathVariable final Long typeid,
                                                                      @RequestParam final String color) {
        return dashboardService.getWardwiseComplaintByComplaintType(typeid, color);
    }

    @RequestMapping(value = "/top-complaints")
    @ResponseBody
    public Map<String, Object> topComplaints() {
        return dashboardService.topComplaints();
    }

    @RequestMapping(value = "/gis-analysis")
    @ResponseBody
    public Map<String, List<Map<String, Object>>> gisAnalysis() {
        return dashboardService.getGISWardWiseAnalysis();
    }

}
