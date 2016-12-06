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

package org.egov.infra.reporting.engine;

import org.egov.infra.reporting.engine.ReportConstants.FileFormat;
import org.egov.infra.reporting.util.ReportUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Class representing the input passed for report generation.
 */
public class ReportRequest {
	/**
	 * Enumeration for report data source type (object/sql).
	 */
	public static enum ReportDataSourceType {
		JAVABEAN, SQL, HQL
	};

	private final ReportDataSourceType reportDataSourceType;
	private String reportTemplate;
	private FileFormat reportFormat;
	private Object reportInputData;
	private Map<String, Object> reportParams = new HashMap<String, Object>();

	/**
	 * Cache the report configuration properties so that we don't have to load them on every report creation
	 */
	private static final Properties reportConfig = ReportUtil.loadReportConfig();

	/**
	 * This flag is applicable only for PDF format. If set to true, the report will be generated such that a print dialog box is automatically shown whenever the PDF file is opened.
	 */
	private boolean printDialogOnOpenReport = false;

	/**
	 * Constructor
	 * @param reportTemplate Report template name (without extension)
	 * @param reportInputData Report input data. This can be one of the following: <br>
	 * 1. An array of objects <br>
	 * 2. A collection of objects <br>
	 * 3. A single object
	 * @param reportParams Report parameters. Key = parameter name, Value = parameter value
	 */
	public ReportRequest(final String reportTemplate, final Object reportInputData, final Map<String, Object> reportParams) {
		// Initialize the object
		initialize(reportTemplate, reportParams);

		this.reportInputData = reportInputData;
		// When report data is passed, the data source type is JAVABEAN
		this.reportDataSourceType = ReportDataSourceType.JAVABEAN;
	}

	/**
	 * Initializes the report input object
	 * @param reportTemplate Report template name (without extension)
	 * @param reportParams Report parameters. Key = parameter name, Value = parameter value
	 */
	private void initialize(final String reportTemplate, final Map<String, Object> reportParams) {
		this.reportTemplate = reportTemplate;
		this.reportParams = reportParams;

		if (reportConfig == null) {
			// Default report format = PDF
			this.reportFormat = FileFormat.PDF;
		} else {
			// Get report format from configuration
			this.reportFormat = FileFormat.valueOf(reportConfig.getProperty(this.reportTemplate, FileFormat.PDF.name()));
		}
	}

	/**
	 * Constructor to be used when report template contains the SQL/HQL query for fetching report data
	 * @param reportTemplate Report template name (without extension)
	 * @param reportInputData Report data in the form of an array of objects.
	 * @param reportParams Report parameters. Key = parameter name, Value = parameter value
	 * @param dataSourceType Report data source type (JDBC/HIBERNATE)
	 */
	public ReportRequest(final String reportTemplate, final Map<String, Object> reportParams, final ReportDataSourceType dataSourceType) {
		initialize(reportTemplate, reportParams);
		this.reportDataSourceType = dataSourceType;
	}

	/**
	 * Constructor to be used when report data is being passed as array of objects
	 * @param reportTemplate Report template name (without extension)
	 * @param reportInputData Report input data in the form of an array of objects.
	 * @param reportParams Report parameters. Key = parameter name, Value = parameter value
	 */
	public ReportRequest(final String reportTemplate, final Object[] reportInputData, final Map<String, Object> reportParams) {
		this(reportTemplate, (Object) reportInputData, reportParams);
	}

	/**
	 * Constructor to be used when report data is being passed as collection of objects
	 * @param reportTemplate Report template name (without extension)
	 * @param reportInputData Report input data in the form of a collection of objects.
	 * @param reportParams Report parameters. Key = parameter name, Value = parameter value
	 */
	@SuppressWarnings("unchecked")
	public ReportRequest(final String reportTemplate, final Collection reportInputData, final Map<String, Object> reportParams) {
		this(reportTemplate, (Object) reportInputData, reportParams);
	}

	/**
	 * @return the Report Template name (without extension)
	 */
	public String getReportTemplate() {
		return this.reportTemplate;
	}

	/**
	 * @param reportTemplate the Report Template to set
	 */
	public void setReportTemplate(final String reportTemplate) {
		this.reportTemplate = reportTemplate;
	}

	/**
	 * @return the Report Format
	 * @see org.egov.infra.reporting.engine.ReportConstants.FileFormat
	 */
	public FileFormat getReportFormat() {
		return this.reportFormat;
	}

	/**
	 * @param reportFormat the Report Format to set
	 * @see org.egov.infra.reporting.engine.ReportConstants.FileFormat
	 */
	public void setReportFormat(final FileFormat reportFormat) {
		this.reportFormat = reportFormat;
	}

	/**
	 * @return the Report Parameters
	 */
	public Map<String, Object> getReportParams() {
		return this.reportParams;
	}

	/**
	 * @param reportParams the Report Parameters to set
	 */
	public void setReportParams(final Map<String, Object> reportParams) {
		this.reportParams = reportParams;
	}

	/**
	 * @param reportInputData Data to be passed to report engine as report input data. This can be one of the following: <br>
	 * 1. An array of objects <br>
	 * 2. A collection of objects <br>
	 * 3. A single object
	 */
	public void setReportInputData(final Object reportInputData) {
		this.reportInputData = reportInputData;
	}

	/**
	 * @return the report data. This can be one of the following: <br>
	 * 1. An array of objects <br>
	 * 2. A collection of objects <br>
	 * 3. A single object
	 */
	public Object getReportInputData() {
		return this.reportInputData;
	}

	/**
	 * @return the Report Data Source Type
	 */
	public ReportDataSourceType getReportDataSourceType() {
		return this.reportDataSourceType;
	}

	/**
	 * @param printDialogOnOpenReport This flag is applicable only for PDF format. If set to true, the report will be generated such that a print dialog box is automatically shown whenever the PDF file is opened.
	 */
	public void setPrintDialogOnOpenReport(final boolean printDialogOnOpenReport) {
		this.printDialogOnOpenReport = printDialogOnOpenReport;
	}

	/**
	 * @return the flag printDialogOnOpenReport <br>
	 * This flag is applicable only for PDF format. If set to true, the report will be generated such that a print dialog box is automatically shown whenever the PDF file is opened.
	 */
	public boolean isPrintDialogOnOpenReport() {
		return this.printDialogOnOpenReport;
	}
}
