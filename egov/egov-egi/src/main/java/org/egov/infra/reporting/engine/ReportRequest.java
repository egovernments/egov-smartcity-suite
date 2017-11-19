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

package org.egov.infra.reporting.engine;

import org.egov.infra.reporting.util.ReportUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ReportRequest {
    private static final Properties REPORT_CONFIG = ReportUtil.loadReportConfig();
    private ReportDataSourceType reportDataSourceType;
    private String reportTemplate;
    private ReportFormat reportFormat;
    private Object reportInputData;
    private Map<String, Object> reportParams;
    private boolean printDialogOnOpenReport = false;


    public ReportRequest(String reportTemplate, Object reportInputData) {
        this(reportTemplate, reportInputData, new HashMap<>());
    }

    public ReportRequest(String reportTemplate, Object reportInputData, Map<String, Object> reportParams) {
        initialize(reportTemplate, reportParams);
        this.reportInputData = reportInputData;
        this.reportDataSourceType = ReportDataSourceType.JAVABEAN;
    }

    public ReportRequest(String reportTemplate, Map<String, Object> reportParams, ReportDataSourceType dataSourceType) {
        initialize(reportTemplate, reportParams);
        this.reportDataSourceType = dataSourceType;
    }

    public ReportRequest(String reportTemplate, Object[] reportInputData, Map<String, Object> reportParams) {
        this(reportTemplate, (Object) reportInputData, reportParams);
    }

    public ReportRequest(String reportTemplate, Collection reportInputData, Map<String, Object> reportParams) {
        this(reportTemplate, (Object) reportInputData, reportParams);
    }

    private void initialize(String reportTemplate, Map<String, Object> reportParams) {
        this.reportTemplate = reportTemplate;
        this.reportParams = reportParams;
        if (REPORT_CONFIG == null) {
            this.reportFormat = ReportFormat.PDF;
        } else {
            this.reportFormat = ReportFormat.valueOf(REPORT_CONFIG.getProperty(this.reportTemplate, ReportFormat.PDF.name()));
        }
    }

    public String getReportTemplate() {
        return this.reportTemplate;
    }

    public ReportFormat getReportFormat() {
        return this.reportFormat;
    }

    public void setReportFormat(ReportFormat reportFormat) {
        this.reportFormat = reportFormat;
    }

    public Map<String, Object> getReportParams() {
        return this.reportParams;
    }

    public void setReportParams(Map<String, Object> reportParams) {
        this.reportParams = reportParams;
    }

    public Object getReportInputData() {
        return this.reportInputData;
    }

    public ReportDataSourceType getReportDataSourceType() {
        return this.reportDataSourceType;
    }

    public boolean isPrintDialogOnOpenReport() {
        return this.printDialogOnOpenReport;
    }

    public void setPrintDialogOnOpenReport(boolean printDialogOnOpenReport) {
        this.printDialogOnOpenReport = printDialogOnOpenReport;
    }
}
