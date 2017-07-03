/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
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

package org.egov.infra.reporting.viewer;

import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.reporting.engine.ReportConstants;
import org.egov.infra.reporting.engine.ReportFormat;
import org.egov.infra.reporting.engine.ReportOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;

public class ReportViewer extends HttpServlet {
    private static final long serialVersionUID = -8123054583553229608L;
    private static final Logger LOGGER = LoggerFactory.getLogger(ReportViewer.class);
    private static final String REPORT_ERROR_CONTENT = "<html><body><b>ERROR: %s!</b></body></html>";

    @Autowired
    private transient ReportViewerUtil reportViewerUtil;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        renderReport(resp, req);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        renderReport(resp, req);
    }

    private void renderReport(HttpServletResponse resp, HttpServletRequest request) {
        String reportId = request.getParameter(ReportConstants.REQ_PARAM_REPORT_ID);
        try {
            ReportOutput reportOutput = reportViewerUtil.getReportOutputFormCache(reportId);
            if (reportOutput == null) {
                renderHtml(resp, "Report output not available");
                return;
            }

            ReportFormat reportFormat = reportOutput.getReportFormat();
            if (reportFormat == null) {
                renderHtml(resp, "Report format not available");
                return;
            }

            byte[] reportData = reportOutput.getReportOutputData();
            if (reportData == null) {
                renderHtml(resp, "Report data not available");
                return;
            }

            renderReport(resp, reportData, reportFormat);
        } catch (Exception e) {
            LOGGER.error("Invalid report id [{}]", reportId, e);
            renderHtml(resp, "Report can not be rendered");
        } finally {
            reportViewerUtil.removeReportOutputFromCache(reportId);
        }
    }

    private void renderHtml(HttpServletResponse resp, String htmContent) {
        renderReport(resp, String.format(REPORT_ERROR_CONTENT, htmContent).getBytes(), ReportFormat.HTM);
    }

    private void renderReport(HttpServletResponse resp, byte[] reportData, ReportFormat reportFormat) {
        try (BufferedOutputStream outputStream = new BufferedOutputStream(resp.getOutputStream())) {
            resp.setHeader(ReportConstants.HTTP_HEADER_CONTENT_DISPOSITION, ReportViewerUtil.getContentDisposition(reportFormat));
            resp.setContentType(ReportViewerUtil.getContentType(reportFormat));
            resp.setContentLength(reportData.length);
            outputStream.write(reportData);
        } catch (Exception e) {
            LOGGER.error("Exception in rendering report with format [{}]!", e);
            throw new ApplicationRuntimeException("Error occurred in report viewer", e);
        }
    }
}
