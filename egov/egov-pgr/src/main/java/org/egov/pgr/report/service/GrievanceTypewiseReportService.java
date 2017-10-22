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

package org.egov.pgr.report.service;

import org.egov.infra.config.persistence.datasource.routing.annotation.ReadOnly;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infstr.services.Page;
import org.egov.pgr.report.entity.contract.DrilldownReportRequest;
import org.egov.pgr.report.entity.view.DrilldownReportView;
import org.egov.pgr.report.repository.GrievanceTypewiseReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class GrievanceTypewiseReportService {

    @Autowired
    private GrievanceTypewiseReportRepository grievanceTypewiseReportRepository;

    @Autowired
    private ReportService reportService;

    @ReadOnly
    public Page<DrilldownReportView> pagedGrievanceTypewiseRecords(DrilldownReportRequest reportRequest) {
        return grievanceTypewiseReportRepository.findGrievanceTypewiseRecord(reportRequest);
    }

    @ReadOnly
    public Page<DrilldownReportView> pagedGrievanceTypewiseRecordsByCompalintId(DrilldownReportRequest reportRequest) {
        return grievanceTypewiseReportRepository.findGrievanceTypewiseRecordsByComplaintId(reportRequest);
    }

    @ReadOnly
    public Object[] grievanceTypewiseReportGrandTotal(DrilldownReportRequest reportRequest) {
        return grievanceTypewiseReportRepository.findGrandTotal(reportRequest);
    }

    @ReadOnly
    public ReportOutput generateGrievanceTypewiseReport(DrilldownReportRequest reportCriteria) {
        Map<String, Object> reportParams = new HashMap<>();
        reportParams.put("type", "complaintwise");
        ReportRequest reportRequest = new ReportRequest("pgr_functionarywise_report",
                grievanceTypewiseReportRepository.findGrievanceTypewiseRecordList(reportCriteria), reportParams);
        reportRequest.setReportFormat(reportCriteria.getPrintFormat());
        return reportService.createReport(reportRequest);
    }

    @ReadOnly
    public ReportOutput generateGrievanceTypewiseReportByComplaintId(DrilldownReportRequest reportCriteria) {
        Map<String, Object> reportParams = new HashMap<>();
        reportParams.put("type", "complaintwise");
        ReportRequest reportRequest = new ReportRequest("pgr_functionarywise_report_comp",
                grievanceTypewiseReportRepository.findGrievanceTypewiseRecordlistByComplaintId(reportCriteria), reportParams);
        reportRequest.setReportFormat(reportCriteria.getPrintFormat());
        return reportService.createReport(reportRequest);
    }
}
