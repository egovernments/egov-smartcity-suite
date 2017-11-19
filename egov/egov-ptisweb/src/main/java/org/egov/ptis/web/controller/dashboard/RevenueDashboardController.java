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
package org.egov.ptis.web.controller.dashboard;

import org.egov.ptis.service.dashboard.RevenueDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 *
 * @author subhash
 *
 */
@Controller
@RequestMapping("/dashboard")
public class RevenueDashboardController {

    @Autowired
    private RevenueDashboardService revenueDashboardService;

    @RequestMapping("/home")
    public String home(final HttpSession session, final Model model) {
        return "dashboard/home";
    }

    @RequestMapping(value = "/performance-bar", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Map<String, Object> performanceBar() {
        return revenueDashboardService.getRevenueZonewiseBar();
    }

    @RequestMapping(value = "/performance-tabular", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<Map<String, Object>> performanceTabular() {
        return revenueDashboardService.getRevenueZonewisePerformance();
    }

    @RequestMapping(value = "/performance-tabularDrill/{zoneName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<Map<String, Object>> performanceTabularDrill(@PathVariable final String zoneName) {
        return revenueDashboardService.getWardwisePerformanceTab(zoneName);
    }

    @RequestMapping(value = "/revenueTrendForTheWeek", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<Map<String, Object>> revenueTrendForTheWeek() {
        return revenueDashboardService.revenueTrendForTheWeek();
    }
    
    @RequestMapping(value = "/revenueTrend", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Map<String, List<Object>> getAnnualZonewiseBar() {
        return revenueDashboardService.getAnnualZonewiseBar();
    }
    
    @RequestMapping(value = "/target-achieved", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Map<String, Collection<Double>> targetVsAchieved() {
        return revenueDashboardService.targetVsAchieved();
    }

    @RequestMapping(value = "/collections-paymentMode", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Map<String, Object> collectionsPaymentMode() {
        return revenueDashboardService.collectionsPaymentMode();
    }

    @RequestMapping(value = "/coverage-efficiency", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Map<String, Object> coverageEfficiency() {
        return revenueDashboardService.coverageEfficiency();
    }

    @RequestMapping(value = "/coverage-efficiency-ward/{zoneName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Map<String, Object> coverageEfficiencyWard(@PathVariable final String zoneName) {
        return revenueDashboardService.coverageEfficiencyWard(zoneName);
    }
}
