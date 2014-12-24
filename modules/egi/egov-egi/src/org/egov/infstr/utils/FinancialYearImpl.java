/*
 * @(#)FinancialYearImpl.java 3.0, 18 Jun, 2013 12:03:33 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.utils;

import java.sql.Date;

public class FinancialYearImpl implements FinancialYear {

	private transient final Date startOnDate;
	private transient final Date endOnDate;
	private transient final Integer currentYear;

	/**
	 * Instantiates a new financial year impl.
	 * @param startOnDate the start on date
	 * @param endOnDate the end on on date
	 * @param currentYear the current year
	 */
	public FinancialYearImpl(final Date startOnDate, final Date endOnDate, final Integer currentYear) {
		this.startOnDate = startOnDate;
		this.endOnDate = endOnDate;
		this.currentYear = currentYear;
	}

	/*
	 * (non-Javadoc)
	 * @see org.egov.infstr.utils.FinancialYear#getCurrentYear()
	 */
	@Override
	public Integer getCurrentYear() {
		return this.currentYear;
	}

	/*
	 * (non-Javadoc)
	 * @see org.egov.infstr.utils.FinancialYear#getEndOnOnDate()
	 */
	@Override
	public Date getEndOnOnDate() {
		return this.endOnDate;
	}

	/*
	 * (non-Javadoc)
	 * @see org.egov.infstr.utils.FinancialYear#getStartOnDate()
	 */
	@Override
	public Date getStartOnDate() {
		return this.startOnDate;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder strBuf = new StringBuilder();
		strBuf.append("startOnDate:").append(this.startOnDate + ",");
		strBuf.append("endOnOnDate:").append(this.endOnDate + ",");
		strBuf.append("currentYear:").append(this.currentYear + ".");
		return strBuf.toString();
	}
}
