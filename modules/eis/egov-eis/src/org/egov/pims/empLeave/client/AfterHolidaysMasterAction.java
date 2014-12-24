package org.egov.pims.empLeave.client;



import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.CFinancialYear;
import org.egov.commons.service.CommonsService;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.pims.empLeave.model.CalendarYear;
import org.egov.pims.empLeave.model.HolidaysUlb;
import org.egov.pims.empLeave.model.Wdaysconstnts;
import org.egov.pims.utils.EisManagersUtill;


/*
 * deepak yn
 *
 *
 *
 */
public class AfterHolidaysMasterAction extends DispatchAction
{
	public final static  Logger LOGGER = Logger.getLogger(AfterHolidaysMasterAction.class.getClass());
	private CommonsService commonsService;
	

	public ActionForward saveOrUpdateHolidays(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	throws IOException,ServletException
	{
		String target =null;
		String alertMessage=null;
		HolidaysUlb holidaysUlb =null;
		HolidayForm leaveForm = (HolidayForm)form;
		String[] holidayId = leaveForm.getHolidayId();
		String[] holidayDate = leaveForm.getHolidayDate();
		String[] holidayName = leaveForm.getHolidayName();
		SimpleDateFormat sqlUtil = new SimpleDateFormat("dd-MMM-yyyy",Locale.getDefault());
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
		//String wdaysconstntsId = req.getParameter("wdaysconstntsId");
		
		CFinancialYear financialYear =null;
		
		try
		{
			/*
			if(!wdaysconstntsId.equals("0"))
			{
				resetWorkingConstants();
				Wdaysconstnts Wdaysconstnts = EisManagersUtill.getEmpLeaveService().getWdaysconstntsByID(new Integer(wdaysconstntsId));
				Wdaysconstnts.setIsactive(new Character('1'));
				EisManagersUtill.getEmpLeaveService().updateWdaysconstnts(Wdaysconstnts);
				
			}
			*/
			if(holidayDate[0]!=null && !holidayDate[0].equals(""))
			{
				for (int len = 0; len < holidayDate.length; len++)
				{
					if(holidayDate[len]!=null && !holidayDate[len].equals(""))
					{
						if(holidayId[len].equals("0") || holidayId[len].equals(""))
						{
							LOGGER.info("holidayId[len]"+holidayId[len]);
							holidaysUlb =new HolidaysUlb();
							holidaysUlb.setHolidayDate(getDateString(holidayDate[len]));
							
							//set Calendar id 
							Date fromDte=sdf.parse(holidayDate[len]);
							String calId=EisManagersUtill.getEmpLeaveService().getYearIdByGivenDate(sqlUtil.format(fromDte));
							CalendarYear calendarYear=EisManagersUtill.getEmpLeaveService().getCalendarYearById(Long.valueOf(calId));
							
							/*
							financialYear = EisManagersUtill.getCommonsManager().findFinancialYearById(new Long(financialId));
							holidaysUlb.setFinancialId(financialYear);*/
							//set calendar year id
							
							//String finId=sqlUtil.format(fromDte);
							//financialYear=EisManagersUtill.getCommonsService().findFinancialYearById(Long.valueOf(finId));
							financialYear=EisManagersUtill.getCommonsService().getFinancialYearByDate(fromDte);
							holidaysUlb.setFinancialId(financialYear);
							
							holidaysUlb.setCalendarId(calendarYear);
							holidaysUlb.setHolidayName(holidayName[len]);
							EisManagersUtill.getEmpLeaveService().createHolidaysUlb(holidaysUlb);
							EisManagersUtill.updateHolidaySetForYear(getYear(getDateString(holidayDate[len])),getMonth(getDateString(holidayDate[len])));
						}
						else
						{
							LOGGER.info("holidayId[len]"+holidayId[len]);
							Date fromDte=sdf.parse(holidayDate[len]);
							String calId=EisManagersUtill.getEmpLeaveService().getYearIdByGivenDate(sqlUtil.format(fromDte));
							CalendarYear calendarYear=EisManagersUtill.getEmpLeaveService().getCalendarYearById(Long.valueOf(calId));
							
							
							holidaysUlb= EisManagersUtill.getEmpLeaveService().getHolidayId(Integer.valueOf(holidayId[len]));
							
							//String finId=sqlUtil.format(fromDte);
							//financialYear=commonsService.findFinancialYearById(Long.valueOf(finId));
							financialYear=EisManagersUtill.getCommonsService().getFinancialYearByDate(fromDte);
							holidaysUlb.setFinancialId(financialYear);
							
							/*
							financialYear = EisManagersUtill.getCommonsManager().findFinancialYearById(new Long(financialId));
							holidaysUlb.setFinancialId(financialYear);*/
							
							holidaysUlb.setCalendarId(calendarYear);
							holidaysUlb.setHolidayDate(getDateString(holidayDate[len]));
							holidaysUlb.setHolidayName(holidayName[len]);
							EisManagersUtill.getEmpLeaveService().updateHolidaysUlb(holidaysUlb);
							
							//call EisManagerUtill.updateHolidaySetForYear(pass year and month from holidayDate[len])
							
							EisManagersUtill.updateHolidaySetForYear(getYear(getDateString(holidayDate[len])),getMonth(getDateString(holidayDate[len])));
							
						}

					}

				}
				
				
			
			}
			Set<Integer> holidaySet = (Set<Integer>)req.getSession().getAttribute("delHolidays");
			if(null!=holidaySet){
			for(Integer delId : holidaySet){
				HolidaysUlb delHoliday = EisManagersUtill.getEmpLeaveService().getHolidayById(delId);
				if(delHoliday != null){
					EisManagersUtill.getEmpLeaveService().deleteHoliday(delHoliday); 
					//EisManagersUtill.updateHolidaySetForYear(getYear(holidayDate[len]),getMonth(holidayDate[len]));
					//call EisManagerUtill.updateHolidaySetForYear(pass year and month from delHoliday.getHolidayDate())
					EisManagersUtill.updateHolidaySetForYear(getYear(delHoliday.getHolidayDate()),getMonth(delHoliday.getHolidayDate()));
				
				}
				}
			}			
			
			target = "success";
			alertMessage="Executed Create or Modify Holiday Master successfully";

	}catch(Exception ex)
	{
	   target = "error";
	   LOGGER.info("Exception Encountered!!!"+ex.getMessage());
	   throw new EGOVRuntimeException("Exception:" + ex.getMessage(),ex);
	}
	
	req.setAttribute("alertMessage", alertMessage);
	return mapping.findForward(target);
	}
	
	private java.util.Date getDateString(String dateString)
	{

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
		java.util.Date d = new java.util.Date();
		try
		{
			d = dateFormat.parse(dateString);

		} catch (Exception e)
		{
			
			throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
		}
		return d;
}
	private int getYear(Date givenDate)
	{
			int year=0;
		    Calendar cal = Calendar.getInstance();
			cal.setTime(givenDate);
			year = cal.get(Calendar.YEAR);
		
		return year;
		
	}
	private int getMonth(Date givenDate)
	{
		int month=0;
		Calendar cal = Calendar.getInstance();
		cal.setTime(givenDate);
			
		month = cal.get(Calendar.MONTH)+1;
		
		return month;
		
	}

	public CommonsService getCommonsService() {
		return commonsService;
	}

	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}
	
}
