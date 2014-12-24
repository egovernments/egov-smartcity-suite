package org.egov.pims.empLeave.dao;

import org.egov.pims.empLeave.model.CalendarYear;

public interface CalendarYearDao extends org.egov.infstr.dao.GenericDAO
{
	
	public String getCurrentYearId();
	public String getYearIdByGivenDate(String estDate);
	public CalendarYear getCalendarYearById(Long id);
}
