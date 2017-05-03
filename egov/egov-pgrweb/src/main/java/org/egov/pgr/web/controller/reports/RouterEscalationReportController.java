/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *     accountability and the service delivery of the government  organizations.
 *
 *      Copyright (C) 2016  eGovernments Foundation
 *
 *      The updated version of eGov suite of products as by eGovernments Foundation
 *      is available at http://www.egovernments.org
 *
 *      This program is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      any later version.
 *
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with this program. If not, see http://www.gnu.org/licenses/ or
 *      http://www.gnu.org/licenses/gpl.html .
 *
 *      In addition to the terms of the GPL license to be adhered to in using this
 *      program, the following additional terms are to be complied with:
 *
 *          1) All versions of this program, verbatim or modified must carry this
 *             Legal Notice.
 *
 *          2) Any misrepresentation of the origin of the material is prohibited. It
 *             is required that all modified versions of this material be marked in
 *             reasonable ways as different from the original version.
 *
 *          3) This license does not grant any rights to any user of the program
 *             with regards to rights under trademark law for use of the trade names
 *             or trademarks of eGovernments Foundation.
 *
 *    In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.pgr.web.controller.reports;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.reporting.engine.ReportConstants.FileFormat;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.web.support.ui.DataTable;
import org.egov.pgr.entity.dto.RouterEscalationForm;
import org.egov.pgr.entity.dto.RouterEscalationRequest;
import org.egov.pgr.service.ComplaintTypeCategoryService;
import org.egov.pgr.service.reports.RouterEscalationService;
import org.egov.pgr.web.controller.response.adaptor.RouterEscalationAdaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("routerescalation")
public class RouterEscalationReportController {

    @Autowired
    private ReportService reportService;

    private final ComplaintTypeCategoryService complaintTypeCategoryService;
    private final BoundaryService boundaryService;
    private final RouterEscalationService routerEscalationService;

    @Autowired
    public RouterEscalationReportController(final ComplaintTypeCategoryService complaintTypeCategoryService,
            final BoundaryService boundaryService, final RouterEscalationService routerEscalationService) {
        this.complaintTypeCategoryService = complaintTypeCategoryService;
        this.boundaryService = boundaryService;
        this.routerEscalationService = routerEscalationService;
    }

    @ModelAttribute
    public RouterEscalationForm routerEscalationForm() {
        return new RouterEscalationForm();
    }

    @GetMapping("/search-form")
    public String searchBaseRegister(final Model model) {
        model.addAttribute("categories", complaintTypeCategoryService.findAll());
        model.addAttribute("complaintTypes", Collections.emptyList());
        model.addAttribute("wardList",
                boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName("Ward", "ADMINISTRATION"));
        return "routerescalation-report";
    }

    @GetMapping(value = "/search-resultList", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String search(final RouterEscalationRequest routerEscalationRequest) throws IOException {
        return new DataTable<>(routerEscalationService.search(routerEscalationRequest),
                routerEscalationRequest.draw())
                        .toJson(RouterEscalationAdaptor.class);
    }

    @GetMapping("/reportgeneration")
    @ResponseBody
    public ResponseEntity<byte[]> generateReport(final RouterEscalationRequest routerEscalationRequest) {
        final Map<String, Object> responseParams = new HashMap<>();
        final ReportRequest reportRequest = new ReportRequest("pgr_routerescalation_report",
                routerEscalationService.prepareReport(routerEscalationRequest),
                responseParams);
        reportRequest.setReportFormat(routerEscalationRequest.getPrintFormat());
        return reportResponse(reportRequest);

    }

    private ResponseEntity<byte[]> reportResponse(final ReportRequest reportRequest) {
        final HttpHeaders headers = new HttpHeaders();
        if (reportRequest.getReportFormat().equals(FileFormat.PDF))
            headers.setContentType(MediaType.parseMediaType("application/pdf"));
        else if (reportRequest.getReportFormat().equals(FileFormat.XLS))
            headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.add("content-disposition", "inline;filename=DCB_Report." + reportRequest.getReportFormat());
        final ReportOutput reportOutput = reportService.createReport(reportRequest);
        return new ResponseEntity<>(reportOutput.getReportOutputData(), headers, HttpStatus.CREATED);
    }
}
