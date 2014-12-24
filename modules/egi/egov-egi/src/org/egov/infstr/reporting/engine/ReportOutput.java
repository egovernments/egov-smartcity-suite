/*
 * @(#)ReportOutput.java 3.0, 17 Jun, 2013 3:00:13 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.reporting.engine;

import org.egov.infstr.reporting.engine.ReportConstants.FileFormat;

/**
 * Class to represent a generated report
 */
public class ReportOutput {
	private byte[] reportOutputData;
	private FileFormat reportFormat;

	/**
	 * Default constructor
	 */
	public ReportOutput() {

	}

	/**
	 * Constructor
	 * @param reportOutputData The report output data as byte array
	 * @param reportInput The report input object
	 * @see ReportRequest
	 */
	public ReportOutput(final byte[] reportOutputData, final ReportRequest reportInput) {
		this.reportOutputData = reportOutputData;
		this.reportFormat = reportInput.getReportFormat();
	}

	/**
	 * @return the Report Data
	 */
	public byte[] getReportOutputData() {
		return this.reportOutputData;
	}

	/**
	 * @param reportOutputData the Report Output Data to set
	 */
	public void setReportOutputData(final byte[] reportOutputData) {
		this.reportOutputData = reportOutputData;
	}

	/**
	 * @return the Report Format
	 */
	public FileFormat getReportFormat() {
		return this.reportFormat;
	}

	/**
	 * @param reportFormat the Report Format to set
	 */
	public void setReportFormat(final FileFormat reportFormat) {
		this.reportFormat = reportFormat;
	}
}
