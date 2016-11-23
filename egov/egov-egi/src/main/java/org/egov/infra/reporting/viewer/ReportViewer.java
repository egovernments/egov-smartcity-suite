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
import org.egov.infra.reporting.engine.ReportConstants.FileFormat;
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

/**
 * Report viewer servlet - displays a report in the browser setting appropriate content type
 */
public class ReportViewer extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(ReportViewer.class);

	@Autowired
	private ReportViewerUtil reportViewerUtil;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
    }

	@Override
	protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
		final String reportId = req.getParameter(ReportConstants.REQ_PARAM_REPORT_ID);
		try {
            ReportOutput reportOutput = reportViewerUtil.getReportOutputFormCache(reportId);
            renderReport(resp, reportOutput);
		} catch (final Exception e) {
			LOGGER.error("Invalid report id [" + reportId + "]", e);
            throw new ApplicationRuntimeException("Error occurred while generating report", e);
		} finally {
			reportViewerUtil.removeReportOutputFromCache(reportId);
		}
	}

	/**
	 * Render the report to browser by setting appropriate content type
	 * @param resp HTTP response object
	 * @param reportOutput Report output object to be rendered
	 */
	private void renderReport(final HttpServletResponse resp, final ReportOutput reportOutput) {
		if (reportOutput == null) {
			renderHtml(resp, "<html><body><b>ERROR: Report output not available!</b></body></html>");
			return;
		}

		final FileFormat reportFormat = reportOutput.getReportFormat();
		if (reportFormat == null) {
			renderHtml(resp, "<html><body><b>ERROR: Report format not available!</b></body></html>");
			return;
		}

		final byte[] reportData = reportOutput.getReportOutputData();
		if (reportData == null) {
			renderHtml(resp, "<html><body><b>ERROR: Report data not available!</b></body></html>");
			return;
		}

		renderReport(resp, reportData, reportFormat);
	}

    /**
     * Renders given html content to the response
     * @param resp Http Servlet Response object
     * @param htmContent HTML content to be rendered
     */
    private void renderHtml(final HttpServletResponse resp, final String htmContent) {
        renderReport(resp, htmContent.getBytes(), FileFormat.HTM);
    }

	/**
	 * Renders given report data in given format to the browser
	 * @param resp The HTTP Servlet response object
	 * @param reportData Report data as byte array
	 * @param reportFormat Report format
	 */
	private void renderReport(final HttpServletResponse resp, final byte[] reportData, final FileFormat reportFormat) {
		try (BufferedOutputStream outputStream = new BufferedOutputStream(resp.getOutputStream());) {
			// Render report data to browser
            resp.setHeader(ReportConstants.HTTP_HEADER_CONTENT_DISPOSITION, ReportViewerUtil.getContentDisposition(reportFormat));
            resp.setContentType(ReportViewerUtil.getContentType(reportFormat));
            resp.setContentLength(reportData.length);
			outputStream.write(reportData);
		} catch (final Exception e) {
			final String errMsg = "Exception in rendering report with format [" + reportFormat + "]!";
			LOGGER.error(errMsg, e);
			throw new ApplicationRuntimeException(errMsg, e);
		}
	}
}
