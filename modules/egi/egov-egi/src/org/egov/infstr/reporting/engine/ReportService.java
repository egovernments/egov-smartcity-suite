/*
 * @(#)ReportService.java 3.0, 17 Jun, 2013 3:00:45 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.reporting.engine;

/**
 * Interface for report service. The code for report generation should use this interface.
 */
public interface ReportService {
	/**
	 * Creates report using given report input object, and returns the generated report output object.
	 * @param reportInput Input to be passed to report engine for generating the report
	 * @return Report output object
	 * @see ReportRequest
	 * @see ReportOutput
	 */
	public ReportOutput createReport(ReportRequest reportInput);

	/**
	 * @param templateName Template name to be checked
	 * @return true if the template name passed is a valid one and can be used for report generation using this service.
	 */
	public boolean isValidTemplate(String templateName);
}
