/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *     accountability and the service delivery of the government  organizations.
 *
 *      Copyright (C) 2017 eGovernments Foundation
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

package org.egov.tl.web.controller;

import static org.egov.tl.utils.Constants.LOCALITY;
import static org.egov.tl.utils.Constants.LOCATION_HIERARCHY_TYPE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.reporting.engine.ReportConstants.FileFormat;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.web.support.ui.DataTable;
import org.egov.tl.entity.dto.BaseRegisterRequest;
import org.egov.tl.entity.view.BaseRegister;
import org.egov.tl.service.BaseRegisterService;
import org.egov.tl.service.LicenseCategoryService;
import org.egov.tl.service.LicenseStatusService;
import org.egov.tl.utils.Constants;
import org.egov.tl.web.response.adaptor.BaseRegisterResponseAdaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = { "baseregister", "/public/baseregister" })
public class BaseRegisterController {
    private final LicenseStatusService licenseStatusService;

    @Autowired
    private final BaseRegisterService baseRegisterService;

    private final LicenseCategoryService licenseCategoryService;

    private final BoundaryService boundaryService;

    @Autowired
    private ReportService reportService;

    @Autowired
    public BaseRegisterController(final BoundaryService boundaryService, final LicenseCategoryService licenseCategoryService,
            final BaseRegisterService baseRegisterService, final LicenseStatusService licenseStatusService) {
        this.boundaryService = boundaryService;
        this.licenseCategoryService = licenseCategoryService;
        this.baseRegisterService = baseRegisterService;
        this.licenseStatusService = licenseStatusService;
    }

    @GetMapping(value = "/search-form")
    public String searchBaseRegister(final Model model) {
        model.addAttribute("baseRegister", new BaseRegister());
        model.addAttribute("categories", licenseCategoryService.getCategoriesOrderByName());
        model.addAttribute("subcategories", Collections.emptyList());
        model.addAttribute("statusList", licenseStatusService.findAll());
        model.addAttribute("filters", Arrays.asList("All", "Defaulters"));
        boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(
                LOCALITY, LOCATION_HIERARCHY_TYPE);
        model.addAttribute("wardList", boundaryService.getBoundariesByBndryTypeNameAndHierarchyTypeName(Constants.REVENUE_WARD,
                Constants.REVENUE_HIERARCHY_TYPE));
        return "baseregister-report";
    }

    @GetMapping(value = "/search-resultList", produces = TEXT_PLAIN_VALUE)
    @ResponseBody
    public String search(final BaseRegisterRequest baseRegisterRequest) {
        return new DataTable<>(baseRegisterService.generatebasereport(baseRegisterRequest),
                baseRegisterRequest.draw())
                        .toJson(BaseRegisterResponseAdaptor.class);
    }

    @GetMapping("/reportwisetotal")
    @ResponseBody
    public Object[] reportTotal(final BaseRegisterRequest baseRegisterRequest) {
        return baseRegisterService.basereporttotal(baseRegisterRequest);
    }

    @GetMapping("/report")
    @ResponseBody
    public ResponseEntity<byte[]> searchReport(final BaseRegisterRequest baseRegisterRequest) {
        final Map<String, Object> responseParams = new HashMap<>();
        final ReportRequest reportRequest = new ReportRequest("tl_baseregister_report",
                baseRegisterService.preparereport(baseRegisterRequest), responseParams);
        reportRequest.setReportFormat(baseRegisterRequest.getPrintFormat());
        return reportResponse(reportRequest);

    }

    private ResponseEntity<byte[]> reportResponse(final ReportRequest reportRequest) {
        final HttpHeaders headers = new HttpHeaders();
        if (reportRequest.getReportFormat().equals(FileFormat.PDF))
            headers.setContentType(MediaType.parseMediaType("application/pdf"));
        else if (reportRequest.getReportFormat().equals(FileFormat.XLS))
            headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.add("content-disposition", "inline;filename=Base_Register_Report." + reportRequest.getReportFormat());
        final ReportOutput reportOutput = reportService.createReport(reportRequest);
        return new ResponseEntity<>(reportOutput.getReportOutputData(), headers, HttpStatus.CREATED);
    }
}