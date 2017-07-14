/*
 * eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) <2017>  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 * 	Further, all user interfaces, including but not limited to citizen facing interfaces,
 *         Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *         derived works should carry eGovernments Foundation logo on the top right corner.
 *
 * 	For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 * 	For any further queries on attribution, including queries on brand guidelines,
 *         please contact contact@egovernments.org
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.tl.web.controller.report;

import org.egov.infra.reporting.engine.ReportDisposition;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.web.support.ui.DataTable;
import org.egov.tl.entity.dto.InstallmentWiseDCBRequest;
import org.egov.tl.entity.view.InstallmentWiseDCB;
import org.egov.tl.service.InstallmentwiseDCBReportService;
import org.egov.tl.web.response.adaptor.InstallmentWiseDCBResponse;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static org.egov.infra.utils.StringUtils.appendTimestamp;
import static org.egov.infra.web.utils.WebUtils.reportToResponseEntity;

@Controller
@RequestMapping("/report/dcb/yearwise")
public class InstallmentwiseDCBReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private InstallmentwiseDCBReportService installmentWiseDCBService;

    @ModelAttribute
    public InstallmentWiseDCB installmentWiseDCBForm() {
        return new InstallmentWiseDCB();
    }

    @GetMapping("/search")
    public String search(Model model) {
        model.addAttribute("financialYears", installmentWiseDCBService.getFinancialYears());
        return "yearwiseDCBReport-search";
    }

    @PostMapping(value = "/search", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String result(final InstallmentWiseDCBRequest installmentWiseDCBRequest) {
        return new DataTable<>(installmentWiseDCBService.getReportResult(installmentWiseDCBRequest),
                installmentWiseDCBRequest.draw()).toJson(InstallmentWiseDCBResponse.class);
    }

    @GetMapping("/grand-total")
    @ResponseBody
    public Object[] yearWiseGrandTotal(final InstallmentWiseDCBRequest installmentWiseDCBRequest) {
        return installmentWiseDCBService.reportGrandTotal(installmentWiseDCBRequest);
    }

    @GetMapping("/download")
    @ResponseBody
    public ResponseEntity<InputStreamResource> downloadYearWiseReport(final InstallmentWiseDCBRequest installmentWiseDCBRequest) {
        final ReportRequest reportRequest = new ReportRequest("tl_dcb_report",
                installmentWiseDCBService.prepareReport(installmentWiseDCBRequest), new HashMap<>());
        final Map<String, Object> reportparam = new HashMap<>();
        reportparam.put("year", installmentWiseDCBRequest.getInstallment());
        reportRequest.setReportParams(reportparam);
        reportRequest.setReportFormat(installmentWiseDCBRequest.getPrintFormat());
        reportRequest.setReportName(appendTimestamp("tl_dcb_report" ));
        reportRequest.setReportDisposition(ReportDisposition.ATTACHMENT);
        return reportToResponseEntity(reportRequest, reportService.createReport(reportRequest));

    }
}