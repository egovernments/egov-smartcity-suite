/*
 * @(#)ReportFormAction.java 3.0, 14 Jun, 2013 12:49:10 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.web.actions;

import java.util.HashMap;
import java.util.Map;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.reporting.engine.ReportConstants.FileFormat;
import org.egov.infstr.reporting.engine.ReportOutput;
import org.egov.infstr.reporting.engine.ReportRequest;
import org.egov.infstr.reporting.engine.ReportRequest.ReportDataSourceType;
import org.egov.infstr.reporting.engine.ReportService;
import org.egov.infstr.reporting.viewer.ReportViewerUtil;

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
	private Integer reportId = -1;

	/**
	 * The result code for report action
	 */
	public static final String REPORT = "report";

	/**
	 * The report format. default = PDF
	 */
	private FileFormat reportFormat = FileFormat.PDF;

	/**
	 * The report data source type (jdbc/hibernate)
	 */
	private ReportDataSourceType dataSourceType = ReportDataSourceType.SQL;

	/**
	 * The report data
	 */
	private Object reportData = null;

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
	public void setReportFormat(final FileFormat reportFormat) {
		this.reportFormat = reportFormat;
	}

	/**
	 * @return the reportFormat
	 */
	public FileFormat getReportFormat() {
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
	public Integer getReportId() {
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
				throw new EGOVRuntimeException("Report Data not set!");
			}
			reportInput = new ReportRequest(reportTemplateName, this.reportData, this.reportParams);
		} else {
			// SQL/HQL
			reportInput = new ReportRequest(reportTemplateName, this.reportParams, this.dataSourceType);
		}
		reportInput.setReportFormat(this.reportFormat);

		// Create the report and add to session
		final ReportOutput reportOutput = this.reportService.createReport(reportInput);
		this.reportId = ReportViewerUtil.addReportToSession(reportOutput, getSession());
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
