package org.egov.pims.empLeave.dao;


import java.util.Date;
import java.util.List;

import org.egov.commons.CFinancialYear;
import org.egov.pims.empLeave.model.CalendarYear;
import org.egov.pims.empLeave.model.HolidaysUlb;

public interface HolidaysUlbDAO extends org.egov.infstr.dao.GenericDAO
{
	public HolidaysUlb getHolidaysUlbByID(Integer id) ;
	public HolidaysUlb getHolidaysUlbByDate(Date date) ;
	public List getHolidaysUlbByMonth(Integer monthId);
	public List getHolidayListByFinalsialYearId(CFinancialYear cFinancialYear);
	//Api to get List of holidays for calendar year
	public List getHolidayListByCalendarYearID(CalendarYear calendarYear);
	public List getHolidaysUlbsFotFinalsialYearId(CFinancialYear cFinancialYear);

}

