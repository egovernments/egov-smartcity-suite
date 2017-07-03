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

import static org.egov.infra.web.utils.WebUtils.reportToResponseEntity;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.web.support.ui.DataTable;
import org.egov.infstr.services.Page;
import org.egov.pgr.entity.dto.AgeingReportForm;
import org.egov.pgr.entity.dto.AgeingReportRequest;
import org.egov.pgr.service.reports.AgeingReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/report")
public class AgeingReportController {

    @Autowired
    private AgeingReportService ageingReportService;

    @Autowired
    private ReportService reportService;

    @ModelAttribute
    public void getReportHelper(final Model model) {
        final ReportHelper reportHealperObj = new ReportHelper();
        final Map<String, String> status = new LinkedHashMap<>();
        status.put("Completed", "Completed");
        status.put("Pending", "Pending");
        status.put("Rejected", "Rejected");
        model.addAttribute("status", status);
        model.addAttribute("reportHelper", reportHealperObj);

    }

    @GetMapping("ageingReportByBoundary")
    public String searchAgeingReportByBoundaryForm(final Model model) {
        model.addAttribute("mode", "ByBoundary");
        return "ageing-search";
    }

    @GetMapping("ageingReportByDept")
    public String searchAgeingReportByDepartmentForm(final Model model) {
        model.addAttribute("mode", "ByDepartment");
        return "ageing-search";
    }

    @GetMapping(value = "/ageing/resultList-update", produces = TEXT_PLAIN_VALUE)
    @ResponseBody
    public String searchAgeingReport(final AgeingReportRequest request) throws IOException {
        final Page<AgeingReportForm> ageingreport = ageingReportService.pagedAgeingRecords(request);
        final long draw = request.draw();
        return new DataTable<>(ageingreport, draw)
                .toJson(AgeingReportHelperAdaptor.class);
    }

    @GetMapping("/ageing/grand-total")
    @ResponseBody
    public Object[] ageingReportGrandTotal(final AgeingReportRequest request) {
        return ageingReportService.ageingReportGrandTotal(request);
    }

    @GetMapping("/ageing/download")
    @ResponseBody
    public ResponseEntity<InputStreamResource> downloadReport(final AgeingReportRequest request) {
        final ReportRequest reportRequest = new ReportRequest("pgr_ageing_report",
                ageingReportService.getAllAgeingReportRecords(request), new HashMap<>());
        final Map<String, Object> reportparam = new HashMap<>();
        reportparam.put("status", request.getStatus());
        reportRequest.setReportParams(reportparam);
        reportRequest.setReportFormat(request.getPrintFormat());
        reportRequest.setReportName("pgr_ageing_report");
        return reportToResponseEntity(reportRequest, reportService.createReport(reportRequest));
    }
}
