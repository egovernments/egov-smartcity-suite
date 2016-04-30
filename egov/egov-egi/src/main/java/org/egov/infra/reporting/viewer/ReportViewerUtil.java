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
import org.egov.infra.reporting.engine.ReportConstants;
import org.egov.infra.reporting.engine.ReportConstants.FileFormat;
import org.egov.infra.reporting.engine.ReportOutput;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility methods related to report viewing
 */
public final class ReportViewerUtil {
	// Content types used for rendering files of different types
	private static final Map<FileFormat, String> contentTypes = getContentTypes();

	/**
	 * @return Array of content types in appropriate order. This order is same as the order of file formats present in the FileFormat enumeration
	 */
	private static Map<FileFormat, String> getContentTypes() {
		final Map<FileFormat, String> contentTypes = new HashMap<FileFormat, String>();
		contentTypes.put(FileFormat.PDF, "application/pdf");
		contentTypes.put(FileFormat.XLS, "application/vnd.ms-excel");
		contentTypes.put(FileFormat.RTF, "application/rtf");
		contentTypes.put(FileFormat.HTM, "text/html");
		contentTypes.put(FileFormat.TXT, "text/plain");
		contentTypes.put(FileFormat.CSV, "text/plain");
		return contentTypes;
	}

	/**
	 * Private constructor to silence PMD warning of "all static methods"
	 */
	private ReportViewerUtil() {

	}

	/**
	 * Adds given report output to an internal session variable and returns the key. This key needs to be passed to the report viewer servlet for displaying the report in browser.
	 * @param reportOutput The report output object to be added to session
	 * @param session The session variables map
	 * @return Key of the report output object in the session variables map
	 */
	@SuppressWarnings("unchecked")
	public static Integer addReportToSession(final ReportOutput reportOutput, final Map<String, Object> session) {
		Integer nextKey = 0;

		// Synchronized to ensure that multiple reports created by the user
		// simultaneously do not overwrite each other.
		synchronized (session) {
			LRUCache<Integer, ReportOutput> reportOutputCache = (LRUCache<Integer, ReportOutput>) session.get(ReportConstants.ATTRIB_EGOV_REPORT_OUTPUT_MAP);

			if (reportOutputCache == null) {
				reportOutputCache = new LRUCache<Integer, ReportOutput>(1, 10);
				session.put(ReportConstants.ATTRIB_EGOV_REPORT_OUTPUT_MAP, reportOutputCache);
			}

			nextKey = reportOutputCache.size();
			reportOutputCache.put(nextKey, reportOutput);
		}

		return nextKey;
	}

	/**
	 * Adds given report output to an internal session variable and returns the key. This key needs to be passed to the report viewer servlet for displaying the report in browser.
	 * @param reportOutput The report output object to be added to session
	 * @param session The HTTP session object
	 * @return Key of the report output object in the session variables map
	 */
	@SuppressWarnings("unchecked")
	public static Integer addReportToSession(final ReportOutput reportOutput, final HttpSession session) {
		Integer nextKey = 0;

		// Synchronized to ensure that multiple reports created by the user
		// simultaneously do not overwrite each other.
		synchronized (session) {
			LRUCache<Integer, ReportOutput> reportOutputCache = (LRUCache<Integer, ReportOutput>) session.getAttribute(ReportConstants.ATTRIB_EGOV_REPORT_OUTPUT_MAP);

			if (reportOutputCache == null) {
				reportOutputCache = new LRUCache<Integer, ReportOutput>(1, 10);
				session.setAttribute(ReportConstants.ATTRIB_EGOV_REPORT_OUTPUT_MAP, reportOutputCache);
			}

			nextKey = reportOutputCache.size();
			reportOutputCache.put(nextKey, reportOutput);
		}

		return nextKey;
	}

	/**
	 * @param fileFormat File format which content type is to be returned
	 * @return content type string for given file format. This can be set in the "Content-type" http header while rendering a file in browser
	 */
	public static String getContentType(final FileFormat fileFormat) {
		return contentTypes.get(fileFormat);
	}

	/**
	 * @param fileFormat File format for which content disposition is to be returned
	 * @return content type string for given file format. This can be set in the "Content-disposition" http header while rendering a file in browser
	 */
	protected static String getContentDisposition(final FileFormat fileFormat) {
		return "inline; filename=report." + fileFormat.toString();
	}
}
