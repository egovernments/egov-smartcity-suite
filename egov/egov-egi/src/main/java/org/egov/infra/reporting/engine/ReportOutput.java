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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;

import static java.lang.String.format;
import static org.egov.infra.utils.ApplicationConstant.CONTENT_DISPOSITION_ATTACH;
import static org.egov.infra.utils.ApplicationConstant.CONTENT_DISPOSITION_INLINE;
import static org.egov.infra.utils.ApplicationConstant.DOT;

public class ReportOutput implements Serializable {
    private static final long serialVersionUID = -2559611205589631905L;
    private byte[] reportOutputData;
    private ReportFormat reportFormat;
    private String reportName;
    private ReportDisposition reportDisposition = ReportDisposition.INLINE;

    public ReportOutput() {
        //default constructor
    }

    public ReportOutput(byte[] reportOutputData, ReportRequest reportInput) {
        this.reportOutputData = reportOutputData;
        this.reportFormat = reportInput.getReportFormat();
    }

    public byte[] getReportOutputData() {
        return this.reportOutputData;
    }

    public void setReportOutputData(byte[] reportOutputData) {
        this.reportOutputData = reportOutputData;
    }

    public ReportFormat getReportFormat() {
        return this.reportFormat;
    }

    public void setReportFormat(ReportFormat reportFormat) {
        this.reportFormat = reportFormat;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public void setReportDisposition(ReportDisposition reportDisposition) {
        this.reportDisposition = reportDisposition;
    }

    public InputStream asInputStream() {
        return new ByteArrayInputStream(reportOutputData);
    }

    public String reportDisposition() {
        return new StringBuilder()
                .append(format(this.reportDisposition == ReportDisposition.INLINE ?
                        CONTENT_DISPOSITION_INLINE : CONTENT_DISPOSITION_ATTACH, reportName))
                .append(DOT).append(reportFormat)
                .toString();
    }
}
