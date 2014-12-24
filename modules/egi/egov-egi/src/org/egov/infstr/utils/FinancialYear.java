/*
 * @(#)FinancialYear.java 3.0, 18 Jun, 2013 12:03:07 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.utils;

import java.sql.Date;

public interface FinancialYear {

	/**
	 * Gets the current year.
	 * @return the current year
	 */
	Integer getCurrentYear();

	/**
	 * Gets the start on date.
	 * @return the start on date
	 */
	Date getStartOnDate();

	/**
	 * Gets the end on on date.
	 * @return the end on on date
	 */
	Date getEndOnOnDate();
}
