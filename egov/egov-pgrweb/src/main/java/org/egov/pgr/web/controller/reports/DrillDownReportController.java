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

package org.egov.pgr.web.controller.reports;

import org.apache.commons.lang.StringUtils;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.web.support.ui.DataTable;
import org.egov.pgr.report.entity.contract.ComplaintDrillDownHelperAdaptor;
import org.egov.pgr.report.entity.contract.DrillDownComplaintTypeAdaptor;
import org.egov.pgr.report.entity.contract.DrillDownReportRequest;
import org.egov.pgr.report.entity.contract.ReportHelper;
import org.egov.pgr.report.service.DrillDownReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

import static org.egov.infra.web.utils.WebUtils.reportToResponseEntity;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@Controller
@RequestMapping("/report")
public class DrillDownReportController {

    @Autowired
    private DrillDownReportService drillDownReportService;

    @Autowired
    private ReportService reportService;

    @ModelAttribute
    public void getReportHelper(Model model) {
        ReportHelper reportHealperObj = new ReportHelper();
        model.addAttribute("reportHelper", reportHealperObj);

    }

    @GetMapping("drillDownReportByBoundary")
    public String searchAgeingReportByBoundaryForm(Model model) {
        model.addAttribute("mode", "ByBoundary");
        return "drillDown-search";
    }

    @GetMapping("drillDownReportByDept")
    public String searchAgeingReportByDepartmentForm(Model model) {
        model.addAttribute("mode", "ByDepartment");
        return "drillDown-search";
    }

    @GetMapping(value = "drillDown/resultList-update", produces = TEXT_PLAIN_VALUE)
    @ResponseBody
    public String searchDrillDownReport(DrillDownReportRequest reportRequest) {
        if (StringUtils.isNotBlank(reportRequest.getDeptid()) &&
                StringUtils.isNotBlank(reportRequest.getComplainttypeid()) && StringUtils.isNotBlank(reportRequest.getSelecteduserid())) {
            return new DataTable<>(drillDownReportService.pagedDrillDownRecordsByCompalintId(reportRequest),
                    reportRequest.draw())
                    .toJson(DrillDownComplaintTypeAdaptor.class);

        } else
            return new DataTable<>(drillDownReportService.pagedDrillDownRecords(reportRequest),
                    reportRequest.draw())
                    .toJson(ComplaintDrillDownHelperAdaptor.class);
    }

    @GetMapping("/drilldown/grand-total")
    @ResponseBody
    public Object[] drillDownReportGrandTotal(DrillDownReportRequest reportRequest) {
        return drillDownReportService.drillDownRecordsGrandTotal(reportRequest);
    }

    @GetMapping("/drilldown/download")
    @ResponseBody
    public ResponseEntity<InputStreamResource> downloadReport(DrillDownReportRequest request) {
        final ReportRequest reportRequest;
        final Map<String, Object> reportparam = new HashMap<>();
        if (StringUtils.isNotBlank(request.getDeptid()) &&
                StringUtils.isNotBlank(request.getComplainttypeid()) && StringUtils.isNotBlank(request.getSelecteduserid())) {
            reportRequest = new ReportRequest("pgr_functionarywise_report_comp",
                    drillDownReportService.getDrillDownRecordsByComplaintId(request), new HashMap<>());
        } else
            reportRequest = new ReportRequest("pgr_functionarywise_report",
                    drillDownReportService.getAllDrillDownRecords(request), new HashMap<>());
        reportparam.put("groupBy", request.getGroupBy());
        reportparam.put("reportTitle", request.getReportTitle());
        reportRequest.setReportParams(reportparam);
        reportRequest.setReportFormat(request.getPrintFormat());
        reportRequest.setReportName("pgr_drillDown_report");
        return reportToResponseEntity(reportRequest, reportService.createReport(reportRequest));
    }
}
