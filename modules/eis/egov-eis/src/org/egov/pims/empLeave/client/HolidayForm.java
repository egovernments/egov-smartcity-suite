package org.egov.pims.empLeave.client;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class HolidayForm  extends ActionForm
{
	public String [] holidayName;
	public String[]  holidayId;
	public String[]  financialId;
	public String[]  holidayDate;
	public String[] getFinancialId() {
		return financialId;
	}
	public void setFinancialId(String[] financialId) {
		this.financialId = financialId;
	}
	public String[] getHolidayDate() {
		return holidayDate;
	}
	public void setHolidayDate(String[] holidayDate) {
		this.holidayDate = holidayDate;
	}
	public String[] getHolidayId() {
		return holidayId;
	}
	public void setHolidayId(String[] holidayId) {
		this.holidayId = holidayId;
	}
	public String[] getHolidayName() {
		return holidayName;
	}
	public void setHolidayName(String[] holidayName) {
		this.holidayName = holidayName;
	}
	
	public void reset(ActionMapping mapping,HttpServletRequest req)
	{
		this.holidayDate	= new String[]{};
		this.holidayId 		= new String[]{};
		this.holidayName	= new String[]{};
		this.financialId	= new String[]{};
		
	}
	
}
