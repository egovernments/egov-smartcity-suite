package org.egov.pims.empLeave.model;

import org.egov.commons.CFinancialYear;

public class DatePeriodFY implements java.io.Serializable
{
	java.util.Date fromDate;
	java.util.Date toDate;
	CFinancialYear financial;
	CalendarYear calendarYear;
	public CFinancialYear getFinancial() {
		return financial;
	}
	public void setFinancial(CFinancialYear financial) {
		this.financial = financial;
	}
	public java.util.Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(java.util.Date fromDate) {
		this.fromDate = fromDate;
	}
	public java.util.Date getToDate() {
		return toDate;
	}
	public void setToDate(java.util.Date toDate) {
		this.toDate = toDate;
	}
	public CalendarYear getCalendarYear() {
		return calendarYear;
	}
	public void setCalendarYear(CalendarYear calendarYear) {
		this.calendarYear = calendarYear;
	}
	
}
