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

package org.egov.stms.web.controller.reports;

import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.web.support.ui.DataTable;
import org.egov.stms.report.service.SewerageBaseRegisterReportService;
import org.egov.stms.reports.entity.SewerageBaseRegisterResult;
import org.egov.stms.web.adapter.SewerageBaseRegisterAdaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.List;

import static org.egov.infra.web.utils.WebUtils.reportAsResponseEntity;
import static org.egov.stms.utils.constants.SewerageTaxConstants.BOUNDARYTYPE_WARD;
import static org.egov.stms.utils.constants.SewerageTaxConstants.HIERARCHYTYPE_REVENUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@Controller
@RequestMapping(value = "/reports")
public class SewerageBaseRegisterReportController {

    private static final String BASE_REGISTER_REPORT = "stms_base_register_report";

    @Autowired
    private BoundaryService boundaryService;
    @Autowired
    private SewerageBaseRegisterReportService sewerageBaseRegisterReportService;
    @Autowired
    private ReportService reportService;

    @ModelAttribute
    public void getPropertyModel(final Model model) {
        model.addAttribute("sewerageBaseRegisterResult", new SewerageBaseRegisterResult());
    }

    @RequestMapping(value = "/baseregistersearch", method = RequestMethod.GET)
    public String newSearchForm(final Model model) {
        model.addAttribute("wards",
                boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(BOUNDARYTYPE_WARD, HIERARCHYTYPE_REVENUE));
        return "baseRegisterSearch-form";
    }

    @RequestMapping(value = "/baseregisterresult", method = RequestMethod.POST, produces = TEXT_PLAIN_VALUE)
    @ResponseBody
    public String springPaginationDataTableUpdate(
            @ModelAttribute final SewerageBaseRegisterResult sewerageBaseRegisterResult, final Model model) {
        return new DataTable<>(sewerageBaseRegisterReportService.getBaseRegisterDetails(sewerageBaseRegisterResult, model),
                sewerageBaseRegisterResult.draw())
                .toJson(SewerageBaseRegisterAdaptor.class);

    }

    @GetMapping("/seweragebaseregister/grand-total")
    @ResponseBody
    public List<BigDecimal> sewerageBaseRegisterGrandTotal(final SewerageBaseRegisterResult sewerageBaseRegisterResult,
                                                           final Model model) {
        return sewerageBaseRegisterReportService.baseRegisterGrandTotal(sewerageBaseRegisterResult, model);
    }

    @GetMapping("/seweragebaseregister/download")
    @ResponseBody
    public ResponseEntity<InputStreamResource> downloadReport(final SewerageBaseRegisterResult sewerageBaseRegisterResult,
                                                              final Model model) {
        final ReportRequest reportRequest = new ReportRequest(BASE_REGISTER_REPORT,
                sewerageBaseRegisterReportService.getAllBaseRegisterRecords(sewerageBaseRegisterResult, model));
        reportRequest.setReportFormat(sewerageBaseRegisterResult.getPrintFormat());
        ReportOutput reportOutput = reportService.createReport(reportRequest);
        reportOutput.setReportName(BASE_REGISTER_REPORT);
        return reportAsResponseEntity(reportOutput);
    }

}