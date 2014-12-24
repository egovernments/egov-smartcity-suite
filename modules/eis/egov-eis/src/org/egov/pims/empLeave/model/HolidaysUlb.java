package org.egov.pims.empLeave.model;

import java.util.Date;

import org.egov.commons.CFinancialYear;

public class HolidaysUlb implements java.io.Serializable
{
	public Integer id;

	public CFinancialYear financialId;
	public CalendarYear calendarId;
	public Date holidayDate;
	public String  holidayName;
	public Integer  month;


	public org.egov.commons.CFinancialYear getFinancialId() {
		return financialId;
	}
	public void setFinancialId(org.egov.commons.CFinancialYear financialId) {
		this.financialId = financialId;
	}
	public Date getHolidayDate() {
		return holidayDate;
	}
	public void setHolidayDate(Date holidayDate) {
		this.holidayDate = holidayDate;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getHolidayName() {
		return holidayName;
	}
	public void setHolidayName(String holidayName) {
		this.holidayName = holidayName;
	}
	public Integer getMonth() {
		return month;
	}
	public void setMonth(Integer month) {
		this.month = month;
	}
	public CalendarYear getCalendarId() {
		return calendarId;
	}
	public void setCalendarId(CalendarYear calendarId) {
		this.calendarId = calendarId;
	}


}
