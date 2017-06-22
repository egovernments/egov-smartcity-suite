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

import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.utils.StringUtils;
import org.egov.infra.web.support.ui.DataTable;
import org.egov.pgr.entity.dto.FunctionarywiseReportRequest;
import org.egov.pgr.service.reports.FunctionaryWiseReportService;
import org.egov.pgr.web.controller.response.adaptor.FunctionarywiseCompTypeAdaptor;
import org.egov.pgr.web.controller.response.adaptor.FunctionarywiseDrillDownAdaptor;
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

import static org.egov.infra.web.utils.WebUtils.reportToResponseEntity;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@Controller
@RequestMapping(value = "/functionaryWiseReport")
public class FunctionaryWiseReportController {

    @Autowired
    private FunctionaryWiseReportService functionaryWiseReportService;

    @Autowired
    private ReportService reportService;

    @ModelAttribute
    public void getReportHelper(Model model) {
        model.addAttribute("reportHelper", new ReportHelper());

    }

    @GetMapping("search")
    public String searchAgeingReportByBoundaryForm() {
        return "functionaryWise-search";
    }

    @GetMapping(value = "result", produces = TEXT_PLAIN_VALUE)
    @ResponseBody
    public String searchFunctionarywiseReport(FunctionarywiseReportRequest functionarywiseReportRequest) {
        if (StringUtils.isNotBlank(functionarywiseReportRequest.getUsrid()) &&
                StringUtils.isNotBlank(functionarywiseReportRequest.getStatus())) {
            return new DataTable<>(functionaryWiseReportService.pagedFunctionarwiseReportByCompalints(functionarywiseReportRequest),
                    functionarywiseReportRequest.draw())
                    .toJson(FunctionarywiseCompTypeAdaptor.class);

        } else
            return new DataTable<>(functionaryWiseReportService.pagedFunctionarwiseRecords(functionarywiseReportRequest),
                    functionarywiseReportRequest.draw())
                    .toJson(FunctionarywiseDrillDownAdaptor.class);
    }

    @GetMapping("grand-total")
    @ResponseBody
    public Object[] functionarywiseGrandTotal(FunctionarywiseReportRequest request) {
        return functionaryWiseReportService.functionarywiseReportGrandTotal(request);
    }

    @GetMapping("download")
    @ResponseBody
    public ResponseEntity<InputStreamResource> downloadReport(FunctionarywiseReportRequest request) {
        final ReportRequest reportRequest;
        if (StringUtils.isNotBlank(request.getUsrid()) && StringUtils.isNotBlank(request.getStatus())) {
            reportRequest = new ReportRequest("pgr_functionarywise_report_comp",
                    functionaryWiseReportService.getFunctionarywiseRecordsByEmployee(request), new HashMap<>());
        } else
            reportRequest = new ReportRequest("pgr_functionarywise_report",
                    functionaryWiseReportService.getAllFunctionarywiseRecords(request), new HashMap<>());
        reportRequest.setReportFormat(request.getPrintFormat());
        reportRequest.setReportName("pgr_functionarywise_report");
        return reportToResponseEntity(reportRequest, reportService.createReport(reportRequest));
    }

}