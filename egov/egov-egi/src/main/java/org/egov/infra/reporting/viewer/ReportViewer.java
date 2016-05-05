/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 */

package org.egov.infra.reporting.viewer;

import org.egov.infra.cache.impl.LRUCache;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.reporting.engine.ReportConstants;
import org.egov.infra.reporting.engine.ReportConstants.FileFormat;
import org.egov.infra.reporting.engine.ReportOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedOutputStream;
import java.io.IOException;

/**
 * Report viewer servlet - displays a report in the browser setting appropriate content type
 */
public class ReportViewer extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(ReportViewer.class);

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest , javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest , javax.servlet.http.HttpServletResponse)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
		final HttpSession session = req.getSession();
		ReportOutput reportOutput = null;
		Integer reportId = null;

		// Read report related session attributes
		final LRUCache<Integer, ReportOutput> reportOutputCache = (LRUCache<Integer, ReportOutput>) session.getAttribute(ReportConstants.ATTRIB_EGOV_REPORT_OUTPUT_MAP);

		final String reportIdStr = req.getParameter(ReportConstants.REQ_PARAM_REPORT_ID);
		try {
			reportId = Integer.valueOf(reportIdStr);
			reportOutput = reportOutputCache.get(reportId);
		} catch (final NumberFormatException nfe) {
			LOGGER.error("Invalid report id [" + reportIdStr + "]", nfe);
		} finally {
			renderReport(resp, reportOutput);
			// Remove report output object from the cache after rendering it
			reportOutputCache.remove(reportId);
		}
	}

	/**
	 * Renders given html content to the response
	 * @param resp Http Servlet Response object
	 * @param htmContent HTML content to be rendered
	 * @throws IOException
	 */
	private void renderHtml(final HttpServletResponse resp, final String htmContent) throws IOException {
		renderReport(resp, htmContent.getBytes(), FileFormat.HTM);
	}

	/**
	 * Render the report to browser by setting appropriate content type
	 * @param resp HTTP response object
	 * @param reportOutput Report output object to be rendered
	 * @throws IOException
	 */
	private void renderReport(final HttpServletResponse resp, final ReportOutput reportOutput) throws IOException {
		if (reportOutput == null) {
			renderHtml(resp, "<html><body><b>ERROR: Report output not available!</b></body></html>");
			return;
		}

		final FileFormat reportFormat = reportOutput.getReportFormat();
		if (reportFormat == null) {
			renderHtml(resp, "<html><body><b>ERROR: Report Format not available!</b></body></html>");
			return;
		}

		final byte[] reportData = reportOutput.getReportOutputData();
		if (reportData == null) {
			renderHtml(resp, "<html><body><b>ERROR: Report Data not available!</b></body></html>");
			return;
		}

		renderReport(resp, reportData, reportFormat);
	}

	/**
	 * Sets HTTP headers required to render a report in given format
	 * @param resp HTTP servlet response object
	 * @param reportFormat Report format
	 * @param reportSize Size of the report being rendered
	 */
	private void setHttpHeaders(final HttpServletResponse resp, final FileFormat reportFormat, final int reportSize) {
		resp.setHeader(ReportConstants.HTTP_HEADER_CONTENT_DISPOSITION, ReportViewerUtil.getContentDisposition(reportFormat));
		resp.setContentType(ReportViewerUtil.getContentType(reportFormat));
		resp.setContentLength(reportSize);
	}

	/**
	 * Renders given report data in given format to the browser
	 * @param resp The HTTP Servlet response object
	 * @param reportData Report data as byte array
	 * @param reportFormat Report format
	 * @throws IOException
	 */
	private void renderReport(final HttpServletResponse resp, final byte[] reportData, final FileFormat reportFormat) throws IOException {
		BufferedOutputStream outputStream = null;
		try {
			outputStream = new BufferedOutputStream(resp.getOutputStream());
			// Render report data to browser
			setHttpHeaders(resp, reportFormat, reportData.length);
			outputStream.write(reportData);
		} catch (final Exception e) {
			final String errMsg = "Exception in rendering report with format [" + reportFormat + "]!";
			LOGGER.error(errMsg, e);
			throw new ApplicationRuntimeException(errMsg, e);
		} finally {
			if (outputStream != null) {
				outputStream.close();
			}
		}
	}
}
