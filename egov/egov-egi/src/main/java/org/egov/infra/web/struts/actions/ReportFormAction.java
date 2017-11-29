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

package org.egov.infra.web.struts.actions;

import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.reporting.engine.ReportDataSourceType;
import org.egov.infra.reporting.engine.ReportFormat;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.reporting.viewer.ReportViewerUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * Infrastructure class that provides generic reporting functionality. Can be
 * extended by action classes for reports screen. Provides action method
 * "report" for report generation. Implementing classes need to implement
 * methods {@code ReportFormAction#getReportTemplateName()} and {@code
 * ReportFormAction#criteria()}
 */
public abstract class ReportFormAction extends BaseFormAction {

	private static final long serialVersionUID = 1L;

	/**
	 * The map of criteria parameters
	 */
	private final Map<String, Object> reportParams = new HashMap<String, Object>();

	/**
	 * The report service used for creating reports
	 */
	private ReportService reportService;

	/**
	 * The ID of created report
	 */
	private String reportId;

	/**
	 * The result code for report action
	 */
	public static final String REPORT = "report";

	/**
	 * The report format. default = PDF
	 */
	private ReportFormat reportFormat = ReportFormat.PDF;

	/**
	 * The report data source type (jdbc/hibernate)
	 */
	private ReportDataSourceType dataSourceType = ReportDataSourceType.SQL;

	/**
	 * The report data
	 */
	private Object reportData = null;

	@Autowired
	private ReportViewerUtil reportViewerUtil;
	/*
	 * (non-Javadoc)
	 * @see com.opensymphony.xwork2.ModelDriven#getModel()
	 */
	@Override
	public Object getModel() {
		return null;
	}

	/**
	 * @param reportService the reportService to set
	 */
	public void setReportService(final ReportService reportService) {
		this.reportService = reportService;
	}

	/**
	 * Clears all criteria parameters
	 */
	protected void clearCriteriaParams() {
		this.reportParams.clear();
	}

	/**
	 * Sets the given report parameter
	 * @param paramName Name of report parameter
	 * @param paramValue Value of report parameter
	 */
	public void setReportParam(final String paramName, final Object paramValue) {
		this.reportParams.put(paramName, paramValue);
	}

	/**
	 * Returns value of the given report parameter
	 * @param paramName Name of report parameter
	 */
	protected Object getReportParam(final String paramName) {
		return this.reportParams.get(paramName);
	}

	/**
	 * @param reportFormat the reportFormat to set
	 */
	public void setReportFormat(final ReportFormat reportFormat) {
		this.reportFormat = reportFormat;
	}

	/**
	 * @return the reportFormat
	 */
	public ReportFormat getReportFormat() {
		return this.reportFormat;
	}

	/**
	 * @param dataSourceType the dataSourceType to set
	 */
	public void setDataSourceType(final ReportDataSourceType dataSourceType) {
		this.dataSourceType = dataSourceType;
	}

	/**
	 * @return the dataSourceType
	 */
	public ReportDataSourceType getDataSourceType() {
		return this.dataSourceType;
	}

	/**
	 * @param reportData the reportData to set
	 */
	public void setReportData(final Object reportData) {
		this.reportData = reportData;
	}

	/**
	 * @return the reportData
	 */
	public Object getReportData() {
		return this.reportData;
	}

	/**
	 * @return the reportId
	 */
	public String getReportId() {
		return this.reportId;
	}

	/**
	 * Action method that creates the report
	 * @return report
	 */
	public String report() {
		ReportRequest reportInput = null;
		final String reportTemplateName = getReportTemplateName();

		if (this.dataSourceType == ReportDataSourceType.JAVABEAN) {
			// Array/Collection of objects
			if (this.reportData == null) {
				throw new ApplicationRuntimeException("Report Data not set!");
			}
			reportInput = new ReportRequest(reportTemplateName, this.reportData, this.reportParams);
		} else {
			// SQL/HQL
			reportInput = new ReportRequest(reportTemplateName, this.reportParams, this.dataSourceType);
		}
		reportInput.setReportFormat(this.reportFormat);

		// Create the report and add to session
		final ReportOutput reportOutput = this.reportService.createReport(reportInput);
		this.reportId = reportViewerUtil.addReportToTempCache(reportOutput);
		return REPORT;
	}

	/**
	 * The criteria action method. Should populate the required data (e.g. drop down data and default values for criteria fields) and forward to appropriate jsp.
	 * @return Result of criteria action
	 */
	public abstract String criteria();

	/**
	 * @return the report template name
	 */
	protected abstract String getReportTemplateName();
}
