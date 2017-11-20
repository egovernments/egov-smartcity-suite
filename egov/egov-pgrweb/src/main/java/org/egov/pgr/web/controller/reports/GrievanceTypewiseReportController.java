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

package org.egov.pgr.web.controller.reports;

import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.utils.StringUtils;
import org.egov.infra.web.support.ui.DataTable;
import org.egov.pgr.report.entity.contract.DrilldownAdaptor;
import org.egov.pgr.report.entity.contract.DrilldownReportRequest;
import org.egov.pgr.report.entity.contract.GrievanceDrilldownReportAdaptor;
import org.egov.pgr.report.service.GrievanceTypewiseReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.egov.infra.reporting.util.ReportUtil.reportAsResponseEntity;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@Controller
@RequestMapping("/report/grievancetypewise")
public class GrievanceTypewiseReportController {

    @Autowired
    private GrievanceTypewiseReportService grievanceTypewiseReportService;

    @GetMapping
    public String grievanceTypewiseSearchForm() {
        return "complaintTypeReport-search";
    }

    @PostMapping(produces = TEXT_PLAIN_VALUE)
    @ResponseBody
    public String searchGrievanceTypewiseReport(DrilldownReportRequest reportRequest) {
        if (StringUtils.isNotBlank(reportRequest.getComplaintDateType()) && StringUtils.isNotBlank(reportRequest.getStatus())) {
            return new DataTable<>(grievanceTypewiseReportService.pagedGrievanceTypewiseRecordsByCompalintId(reportRequest),
                    reportRequest.draw())
                    .toJson(DrilldownAdaptor.class);

        } else
            return new DataTable<>(grievanceTypewiseReportService.pagedGrievanceTypewiseRecords(reportRequest),
                    reportRequest.draw())
                    .toJson(GrievanceDrilldownReportAdaptor.class);
    }

    @GetMapping("grand-total")
    @ResponseBody
    public Object[] grievanceTypewiseGrandTotal(DrilldownReportRequest reportRequest) {
        return grievanceTypewiseReportService.grievanceTypewiseReportGrandTotal(reportRequest);
    }

    @GetMapping("download")
    @ResponseBody
    public ResponseEntity<InputStreamResource> downloadGrievanceTypewiseReport(DrilldownReportRequest reportCriteria) {
        ReportOutput reportOutput;
        if (isNotBlank(reportCriteria.getComplaintDateType()) && isNotBlank(reportCriteria.getStatus()))
            reportOutput = grievanceTypewiseReportService.generateGrievanceTypewiseReportByComplaintId(reportCriteria);
        else
            reportOutput = grievanceTypewiseReportService.generateGrievanceTypewiseReport(reportCriteria);
        reportOutput.setReportName("grievancetypewise_report");
        return reportAsResponseEntity(reportOutput);
    }
}
