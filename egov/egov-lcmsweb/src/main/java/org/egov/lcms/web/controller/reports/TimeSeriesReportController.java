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

package org.egov.lcms.web.controller.reports;

import org.egov.infra.utils.JsonUtils;
import org.egov.lcms.reports.entity.LegalCommonReportResult;
import org.egov.lcms.reports.entity.TimeSeriesReportResult;
import org.egov.lcms.transactions.service.LegalCommonReportService;
import org.egov.lcms.transactions.service.TimeSeriesReportService;
import org.egov.lcms.utils.constants.LcmsConstants;
import org.egov.lcms.web.adaptor.TimeSeriesReportJsonAdaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/timeseriesreports")
public class TimeSeriesReportController {

    @Autowired
    private TimeSeriesReportService timeSeriesReportService;

    @Autowired
    private LegalCommonReportService legalCommonReportService;

    public @ModelAttribute("aggregatedByList") List<String> getAggregatedBy() {
        final List<String> aggregatedByList = new ArrayList<>();
        aggregatedByList.add(LcmsConstants.COURTNAME);
        aggregatedByList.add(LcmsConstants.PETITIONTYPE);
        aggregatedByList.add(LcmsConstants.CASESTATUS);
        aggregatedByList.add(LcmsConstants.CASECATEGORY);
        aggregatedByList.add(LcmsConstants.COURTTYPE);
        aggregatedByList.add(LcmsConstants.OFFICERINCHRGE);
        aggregatedByList.add(LcmsConstants.STANDINGCOUNSEL);
        return aggregatedByList;
    }

    public @ModelAttribute("period") List<String> getPeriod() {
        final List<String> period = new ArrayList<>();
        period.add(LcmsConstants.AGG_YEAR);
        period.add(LcmsConstants.AGG_MONTH);
        return period;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/timeSeriesReport")
    public String searchForm(final Model model) {
        model.addAttribute("timeSeriesReportResult", new TimeSeriesReportResult());
        model.addAttribute("currentDate", new Date());
        return "timeseriesreport-form";
    }

    @RequestMapping(value = "/timeSeriesReportresults", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String getTimeSeriesReportResult(@ModelAttribute final TimeSeriesReportResult timeSeriesReport,
            final HttpServletRequest request) throws ParseException {

        final List<TimeSeriesReportResult> timeSeriesReportList = timeSeriesReportService
                .getTimeSeriesReportsResults(timeSeriesReport);
        return new StringBuilder("{ \"data\":").append(
                JsonUtils.toJSON(timeSeriesReportList, TimeSeriesReportResult.class, TimeSeriesReportJsonAdaptor.class))
                .append("}").toString();
    }

    @RequestMapping(value = "/drilldownreportresult", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<LegalCommonReportResult> getDrillDownReportResult(
            @ModelAttribute final LegalCommonReportResult legalCommonReportObj,
            final HttpServletRequest request) throws ParseException {
        return legalCommonReportService.getLegalCommonReportsResults(legalCommonReportObj, null);

    }
}
