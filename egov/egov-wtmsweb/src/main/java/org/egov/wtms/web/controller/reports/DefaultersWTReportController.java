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
package org.egov.wtms.web.controller.reports;

import static org.egov.infra.web.utils.WebUtils.toJSON;
import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_HIERARCHY_TYPE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.egov.demand.model.EgDemandDetails;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.wtms.application.entity.DefaultersReport;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.service.ConnectionDemandService;
import org.egov.wtms.application.service.DefaultersWTReportService;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.masters.entity.enums.ConnectionStatus;
import org.egov.wtms.utils.DemandComparatorByInstallmentOrder;
import org.egov.wtms.utils.WaterTaxUtils;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/report/defaultersWTReport/search")
public class DefaultersWTReportController {

    @Autowired
    private BoundaryService boundaryService;

    @Autowired
    private DefaultersWTReportService defaultersWTReportService;

    @Autowired
    public WaterConnectionDetailsService waterConnectionDetailsService;

    @Autowired
    public ConnectionDemandService connectionDemandService;

    @Autowired
    private WaterTaxUtils waterTaxUtils;

    @RequestMapping(method = GET)
    public String search(final Model model) {
        model.addAttribute("currentDate", new Date());
        return "defaultersWTReport-search";
    }

    @ModelAttribute
    public DefaultersReport reportModel() {
        return new DefaultersReport();
    }

    public @ModelAttribute("revenueWards") List<Boundary> revenueWardList() {
        return boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(WaterTaxConstants.REVENUE_WARD,
                REVENUE_HIERARCHY_TYPE);
    }

    public @ModelAttribute("topDefaultersList") List<Integer> defaultersList() {
        final List<Integer> topdefaultersList = new ArrayList<Integer>();
        topdefaultersList.add(10);
        topdefaultersList.add(50);
        topdefaultersList.add(100);
        topdefaultersList.add(500);
        topdefaultersList.add(1000);
        return topdefaultersList;
    }

    @RequestMapping(value = "/result", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody void searchResult(final HttpServletRequest request, final HttpServletResponse response)
            throws IOException, ParseException {
        String ward = "";
        String topDefaulters = "";
        String fromAmount = "";
        String toAmount = "";

        if (null != request.getParameter("ward"))
            ward = request.getParameter("ward");
        if (null != request.getParameter("topDefaulters"))
            topDefaulters = request.getParameter("topDefaulters");
        if (null != request.getParameter("fromAmount"))
            fromAmount = request.getParameter("fromAmount");
        if (null != request.getParameter("toAmount"))
            toAmount = request.getParameter("toAmount");
        List<DefaultersReport> defaultersreportlist = new ArrayList<DefaultersReport>();
        defaultersreportlist = defaultersWTReportService.getDefaultersReportDetails(fromAmount, toAmount, ward,
                topDefaulters, Integer.valueOf(request.getParameter("start")),
                Integer.valueOf(request.getParameter("length")));
        final long foundRows = defaultersWTReportService.getTotalCount(fromAmount, toAmount, ward, topDefaulters);
        String result = null;
        for (final DefaultersReport dd : defaultersreportlist)
            dd.setDuePeriodFrom(getDuePeriodFrom(dd.getHscNo()));
        result = new StringBuilder("{ \"draw\":").append(request.getParameter("draw")).append(", \"recordsTotal\":")
                .append(foundRows).append(", \"recordsFiltered\":").append(foundRows).append(", \"data\":")
                .append(toJSON(defaultersreportlist, DefaultersReport.class, DefaultersReportAdaptor.class)).append("}")
                .toString();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        IOUtils.write(result, response.getWriter());
    }

    public String getDuePeriodFrom(final String consumerCode) {
        final WaterConnectionDetails waterConnectionDetails = waterConnectionDetailsService
                .findByApplicationNumberOrConsumerCodeAndStatus(consumerCode, ConnectionStatus.ACTIVE);
        if (waterConnectionDetails != null) {
            final DemandComparatorByInstallmentOrder demandComparatorByOrderId = new DemandComparatorByInstallmentOrder();
            final Set<EgDemandDetails> egdemandtemplist = new HashSet<EgDemandDetails>();
            final Set<EgDemandDetails> demnadDetList = waterTaxUtils.getCurrentDemand(waterConnectionDetails)
                    .getDemand().getEgDemandDetails();
            for (final EgDemandDetails egDemandTemp : demnadDetList)
                if (!egDemandTemp.getAmount().equals(egDemandTemp.getAmtCollected()))
                    egdemandtemplist.addAll(egDemandTemp.getEgDemand().getEgDemandDetails());
            final List<EgDemandDetails> egdemandlist = new ArrayList<EgDemandDetails>(egdemandtemplist);
            if (egdemandlist.isEmpty())
                return "";
            else {
                Collections.sort(egdemandlist, demandComparatorByOrderId);
                return egdemandlist.get(0).getEgDemandReason().getEgInstallmentMaster().getDescription();
            }
        } else
            return "";
    }
}