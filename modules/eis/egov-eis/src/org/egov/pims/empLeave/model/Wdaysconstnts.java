package org.egov.pims.empLeave.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.egov.commons.CFinancialYear;
import org.egov.exceptions.EGOVRuntimeException;

public class Wdaysconstnts   implements java.io.Serializable

{
	public Integer id;
	public String name;
	public Character isactive;

	/**
	 * @return Returns the id.
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	public Character getIsactive() {
		return isactive;
	}
	public void setIsactive(Character isactive) {
		this.isactive = isactive;
	}
	
	public List getListOfHolidays(CFinancialYear cFinancialYear)
	{
		return new ArrayList();
		
	}
	
	/**
	 * Returns list of saturdays in month. Throws EGovRuntimeException if an
	 * invalid month is passed.
	 * @param month Integer representing month.Allowed values 1-12. 
	 * 				For example 1 for jan, 12 for Dec
	 * @param year
	 * @return
	 */
	protected List<String> getListOfAllSaturdays( int month, int year ) {
		if (month < 1 || month > 12) 
			throw new EGOVRuntimeException("Invalid month value :" + month);
		List<String> dtList = new ArrayList<String>();   
		Calendar cal = Calendar.getInstance();
		int calMonth= month-1;
		cal.set( year, calMonth, 1 );
		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		for (int i=1; i<=cal.getMaximum(Calendar.WEEK_OF_MONTH); i++) {
			cal.set(Calendar.WEEK_OF_MONTH, i);
			if (calMonth == cal.get(Calendar.MONTH))
				dtList.add(new java.sql.Date(cal.getTime().getTime()).toString());
		}

		return dtList;

	}
	
}
