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
package org.egov.ptis.web.controller.reports;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.egov.commons.CFinancialYear;
import org.egov.commons.RegionalHeirarchy;
import org.egov.commons.RegionalHeirarchyType;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.commons.service.RegionalHeirarchyService;
import org.egov.infra.config.core.LocalizationSettings;
import org.egov.ptis.domain.entity.property.BillCollectorDailyCollectionReportResult;
import org.egov.ptis.domain.service.report.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.GsonBuilder;

/**
 * @author Pradeep
 */
@Controller
@RequestMapping(value = "/reports")
public class GeneralDialyCollectionReportController {

    private static final String BILL_COLLECTOR_COLL_REPORT_FORM = "bcDailyCollectionReport-form";
    private static final String ULBWISE_COLL_REPORT_FORM = "ulbWiseCollectionReport-form";
    private static final String ULBWISE_DCB_REPORT_FORM = "ulbWiseDcbReport-form";

    private String district = "DISTRICT";
    private String city = "CITY";

    @Autowired
    private FinancialYearDAO financialYearDAO;

    @Autowired
    private RegionalHeirarchyService regionalHeirarchyService;

    @Autowired
    private ReportService reportService;

    @ModelAttribute("previousFinancialYear")
    public CFinancialYear previousFinancialYear() {
        return financialYearDAO.getPreviousFinancialYearByDate(new Date());
    }

    @ModelAttribute("currentFinancialYear")
    public CFinancialYear currentFinancialYear() {
        return financialYearDAO.getFinancialYearByDate(new Date());
    }

    @ModelAttribute("regions")
    public List<RegionalHeirarchy> getRegions() {
        return regionalHeirarchyService.getActiveRegionalHeirarchyByRegion(RegionalHeirarchyType.REGION);
    }

    @RequestMapping(value = "/getRegionHeirarchyByType", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<RegionalHeirarchy> getRegionHeirarchyByType(@RequestParam final String regionName,
            @RequestParam final String type) {

        if (type != null && type.equalsIgnoreCase(district))
            return regionalHeirarchyService.getActiveChildRegionHeirarchyByPassingParentNameAndType(
                    RegionalHeirarchyType.DISTRICT, regionName);
        else {

            if (type != null && type.equalsIgnoreCase(city))
                return regionalHeirarchyService.getActiveChildRegionHeirarchyByPassingParentNameAndType(
                        RegionalHeirarchyType.CITY, regionName);
        }
        return Collections.emptyList();
    }

    @ModelAttribute("bcDailyCollectionReportResult")
    public BillCollectorDailyCollectionReportResult bcDailyCollectionReportResultModel() {
        return new BillCollectorDailyCollectionReportResult();
    }

    @RequestMapping(value = "/billcollectorDailyCollectionReport-form", method = RequestMethod.GET)
    public String searchForm(final Model model) {
        BillCollectorDailyCollectionReportResult bcDailyCollectionReportResult = new BillCollectorDailyCollectionReportResult();

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        bcDailyCollectionReportResult.setGeneratedDate(dateFormat.format(calendar.getTime()));
        model.addAttribute("bcDailyCollectionReportResult", bcDailyCollectionReportResult);
        return BILL_COLLECTOR_COLL_REPORT_FORM;
    }

    @RequestMapping(value = "/ulbWiseDcbReport-form", method = RequestMethod.GET)
    public String searchUlbWiseDcbForm(final Model model, @RequestParam final String type) {
        BillCollectorDailyCollectionReportResult bcDailyCollectionReportResult = new BillCollectorDailyCollectionReportResult();

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        model.addAttribute("typeValue", type);
        calendar.add(Calendar.DATE, -1);
        bcDailyCollectionReportResult.setGeneratedDate(dateFormat.format(calendar.getTime()));
        model.addAttribute("bcDailyCollectionReportResult", bcDailyCollectionReportResult);
        return ULBWISE_DCB_REPORT_FORM;
    }

    @RequestMapping(value = "/ulbWiseCollectionReport-form", method = RequestMethod.GET)
    public String searchUlbWiseForm() {
        return ULBWISE_COLL_REPORT_FORM;
    }

    @RequestMapping(value = "/ulbWiseDCBList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void searchUlbWiseDcb(final HttpServletResponse response,
            BillCollectorDailyCollectionReportResult bcDailyCollectionReportResult) throws IOException {
        IOUtils.write(
                "{ \"data\":"
                        + new GsonBuilder().setDateFormat(LocalizationSettings.datePattern()).create()
                                .toJson(reportService.getUlbWiseDcbCollection(new Date(), bcDailyCollectionReportResult))
                        + "}",
                response.getWriter());
    }

    @RequestMapping(value = "/ulbWiseCollectionList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void searchUlbWise(final HttpServletResponse response) throws IOException {
        IOUtils.write(
                "{ \"data\":"
                        + new GsonBuilder().setDateFormat(LocalizationSettings.datePattern()).create()
                                .toJson(reportService.getUlbWiseDailyCollection(new Date()))
                        + "}",
                response.getWriter());
    }

    @RequestMapping(value = "/billcollectorDailyCollectionReportList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void search(final HttpServletResponse response, BillCollectorDailyCollectionReportResult bcDailyCollectionReportResult)
            throws IOException {
        IOUtils.write(
                "{ \"data\":"
                        + new GsonBuilder().setDateFormat(LocalizationSettings.datePattern()).create()
                                .toJson(reportService.getBillCollectorWiseDailyCollection(new Date(),
                                        bcDailyCollectionReportResult))
                        + "}",
                response.getWriter());
    }

}
