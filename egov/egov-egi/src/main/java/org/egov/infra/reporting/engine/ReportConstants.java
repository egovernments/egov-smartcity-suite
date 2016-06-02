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

package org.egov.infra.reporting.engine;

/**
 * Reports related constants
 */
public class ReportConstants {

	// Enumeration of supported report formats
	public static enum FileFormat {
		PDF, XLS, RTF, HTM, TXT, CSV
	};

	/**
	 * The path (in CLASSPATH) where the report engine will look for images during report generation.
	 */
	public static final String IMAGES_BASE_PATH = "/egi/resources/global/images/";

	/**
	 * Path of the directory that is checked first for any customised report templates before looking at the standard template path. i.e. System will first check in /custom/reports/templates, and then in /reports/templates
	 */
	public static final String CUSTOM_DIR_NAME = "/custom";

	/**
	 * The based directory for report templates. As of now the only sub-directory is templates. However later it may host more sub-directories related to reports as and when required.
	 */
	public static final String REPORTS_BASE_PATH = "/reports/";

	/**
	 * Name of the report templates directory. It is appended to the reports base path to arrive at the relative path (in CLASSPATH) where the system looks for report templates
	 */
	public static final String TEMPLATE_DIR_NAME = "templates/";

	/**
	 * The properties file used for fetching report configuration (format) for a given report template. TODO: Support report configuration in DB
	 */
	public static final String REPORT_CONFIG_FILE = "/config/reports.properties";

	// UTF-8 character encoding
	public static final String CHARACTER_ENCODING_UTF8 = "UTF-8";

	// Request parameters
	public static final String REQ_PARAM_REPORT_ID = "reportId";

	// Constants related to number formatting
	public static final int AMOUNT_PRECISION_DEFAULT = 2;

	// HTTP Headers
	public static final String HTTP_HEADER_CONTENT_DISPOSITION = "Content-disposition";
	
	public static final String IMAGE_CONTEXT_PATH = "/egi";
}
