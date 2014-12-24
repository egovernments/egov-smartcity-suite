package org.egov.pims.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;


import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.CFinancialYear;
import org.egov.commons.service.CommonsService;
import org.egov.commons.service.CommonsServiceImpl;
import org.egov.infstr.commons.service.GenericCommonsService;
import org.egov.infstr.commons.service.GenericCommonsServiceImpl;
import org.egov.lib.rjbac.dept.ejb.api.DepartmentService;
import org.egov.lib.rjbac.dept.ejb.server.DepartmentServiceImpl;
import org.egov.lib.rjbac.user.ejb.api.UserService;
import org.egov.lib.rjbac.user.ejb.server.UserServiceImpl;
import org.egov.lib.rrbac.services.RbacService;
import org.egov.lib.rrbac.services.RbacServiceImpl;
import org.egov.pims.commons.client.FinancialYearFailureException;
import org.egov.pims.commons.service.EisCommonsService;
import org.egov.pims.commons.service.EisCommonsServiceImpl;
import org.egov.pims.empLeave.model.CalendarYear;
import org.egov.pims.empLeave.model.DatePeriodFY;
import org.egov.pims.empLeave.service.EmpLeaveService;
import org.egov.pims.empLeave.service.EmpLeaveServiceImpl;
import org.egov.pims.service.EmployeeService;
import org.egov.pims.service.EmployeeServiceImpl;


public class EisManagersUtill {

	private static final Logger LOGGER = Logger.getLogger(EisManagersUtill.class);
	
	
	static List list = new ArrayList();
	static boolean RESET = true;
	static boolean RESETMONTH = true;
	static Map monthsVsDays = new TreeMap();
	static Map yearAndMon = new HashMap();
	static Map monthStrVsDays = new TreeMap();
	static boolean RESETSTRMONTH = true;
	static Set<String> setOfHolForMon  = new HashSet<String>();
	static long finId = 0;
	static int year = 0;
	static Map monthVsYear = new TreeMap();
	static boolean RESETFyER = true;
	static boolean RESETHOLMON = true;
	static Map finYearMap = new HashMap();
	private static CommonsService commonsService;




	
	public static void  updateFYMonth()
	{
		
		try {
			monthVsYear = new TreeMap();
			Map fYearVsmonthVsYear = new HashMap();
			List list = EisManagersUtill.getCommonsService().getAllFinancialYearList();
			int[] monthAry = {4,5,6,7,8,9,10,11,12,1,2,3};
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
			for(Iterator iter = list.iterator();iter.hasNext();)
			{
				 fYearVsmonthVsYear = new HashMap();
				CFinancialYear financialYear = (CFinancialYear)iter.next();
				for(int i=0;i<monthAry.length;i++)
				{
					if(i<=8)
					{
						fYearVsmonthVsYear.put(monthAry[i],sdf.format(financialYear.getStartingDate()).substring(6,sdf.format(financialYear.getStartingDate()).length()));
					}
					else
					{
						fYearVsmonthVsYear.put(monthAry[i],sdf.format(financialYear.getEndingDate()).substring(6,sdf.format(financialYear.getEndingDate()).length()));
					}
				}

				monthVsYear.put(financialYear.getId(), fYearVsmonthVsYear);


			}


						RESETFyER = false;
		} catch (RuntimeException e) {
			
			
			throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
		}
	}

	public static Map getFYMap()
	{
		if (RESETFyER)
		{
			updateFYMonth();
		}

		return monthVsYear;
	}

	public static void  updateStrMonth()
	{
		monthStrVsDays = new TreeMap();
		monthStrVsDays.put(1, "Jan");
		monthStrVsDays.put(2, "Feb");
		monthStrVsDays.put(3, "Mar");
		monthStrVsDays.put(4, "Apr");
		monthStrVsDays.put(5, "May");
		monthStrVsDays.put(6, "Jun");
		monthStrVsDays.put(7, "Jul");
		monthStrVsDays.put(8, "Aug");
		monthStrVsDays.put(9, "Sep");
		monthStrVsDays.put(10, "Oct");
		monthStrVsDays.put(11, "Nov");
		monthStrVsDays.put(12, "Dec");
		RESETSTRMONTH = false;
	}

	//FIXME - donot use . Use Calendar instead.
	public static void  updateMonth()
	{
		monthsVsDays = new TreeMap();
		monthsVsDays.put(0, 31);
		monthsVsDays.put(1, 28); //what about leap years?
		monthsVsDays.put(2, 31);
		monthsVsDays.put(3, 30);
		monthsVsDays.put(4, 31);
		monthsVsDays.put(5, 30);
		monthsVsDays.put(6, 31);
		monthsVsDays.put(7, 31);
		monthsVsDays.put(8, 30);
		monthsVsDays.put(9, 31);
		monthsVsDays.put(10, 30);
		monthsVsDays.put(11, 31);
		RESETMONTH = false;
	}
	public static Integer getMonthsVsDays(int month)
	{
		if (RESETMONTH)
		{
			updateMonth();
		}

		return (Integer)monthsVsDays.get(month);
	}
	public static String getMonthsStrVsDays(int month)
	{
		if (RESETSTRMONTH)
		{
			updateStrMonth();
		}

		return (String)monthStrVsDays.get(month);
	}
	public static Map getMonthsStrVsDaysMap()
	{
		if (RESETSTRMONTH)
		{
			updateStrMonth();
		}

		return monthStrVsDays;
	}
	public static EmpLeaveService getEmpLeaveService()
    {
		return new EmpLeaveServiceImpl();
    }

	public static GenericCommonsService getGenericCommonsService()
    {
        return new GenericCommonsServiceImpl();
    }
	
	public static EisCommonsService getEisCommonsService()
    {
        return new EisCommonsServiceImpl();
    }

	public static DepartmentService getDeptService()
    {

        return new DepartmentServiceImpl();
    }

	public static UserService getUserService()
	{
		
		return new UserServiceImpl();
	}
		public static EmployeeService getEmployeeService()
		{
			return new EmployeeServiceImpl();
		}
		
		public static RbacService getRbacService()
		{
			return new RbacServiceImpl();
		}
		
		public static List getSundaysForGivenCurrentFinYear(CFinancialYear financialYear)
		{
			if(RESET)
			{
					update(financialYear);
			}
			if(financialYear.getId()!=getFinantialId())
			{
					update(financialYear);
			}
			
			return list;
		}
		private static synchronized void update(CFinancialYear financialYear)
		{
			synchronized(list) {
				list = new ArrayList();
			}
			
			java.util.Date startingDate=null;
			int year;
			
			
				CFinancialYear financial = financialYear;
				finId = financial.getId();
				startingDate = financial.getStartingDate();
				year = Integer.valueOf(startingDate.toString().substring(0, 4)).intValue();
				int[] monthAry = {3,4,5,6,7,8,9,10,11,0,1,2};
				Calendar calendar = Calendar.getInstance();
				for(int i=0;i<monthAry.length;i++)
				{
					if(i<=8)
					{
						calendar.set(year, monthAry[i], 1);
						setSundays(list,calendar,year,monthAry[i]);
					}
					else
					{
						calendar.set(year+1, monthAry[i], 1);
						setSundays(list,calendar,year+1,monthAry[i]);
					}
	
				}
			

			RESET = false;

		}
		private static int getSunday(String weekDay)
		{
			int day = 0;
			Map<String,Integer> dayMap = new HashMap<String,Integer>();
			dayMap.put("Mon",Integer.valueOf(7));
			dayMap.put("Tue",Integer.valueOf(6));
			dayMap.put("Wed",Integer.valueOf(5));
			dayMap.put("Thu",Integer.valueOf(4));
			dayMap.put("Fri",Integer.valueOf(3));
			dayMap.put("Sat",Integer.valueOf(2));
			dayMap.put("Sun",Integer.valueOf(1));
			day = ((Integer)dayMap.get(weekDay)).intValue();
			return day;
		}

		private static long  getFinantialId()
		{
			return finId;
		}

		private static void setSundays(List list,Calendar calendar,int year,int month)
		{
			int sun = getSunday(calendar.getTime().toString().substring(0, 3));
			calendar.set(year, month, sun);
			synchronized(list) {
				list.add(new java.sql.Date(calendar.getTime().getTime()).toString());
				int icrSun = sun;
				while(true)
				{
					icrSun = icrSun+7;
					calendar.set(year, month, icrSun);
					if(calendar.getTime().getMonth()==month)
					{
						list.add(new java.sql.Date(calendar.getTime().getTime()).toString());
						continue;
					}
					else
					{
						break;
					}
	
				}
			}
		}
		//FIXME api name
		public  static List<DatePeriodFY> getListOfFinYrandDate(java.util.Date fromDate,java.util.Date toDate)throws Exception
		{
			List list = new ArrayList();
			DatePeriodFY datePeriodFY = null;
			//Financial year for the given from and to dates 
			CFinancialYear fromFinancialYear =null;
			CFinancialYear toFinancialYear=null;
			
			//calendar year for the given from and to dates
			CalendarYear fromCalYear=null;
			CalendarYear toCalYear=null;
			Calendar calendarFrom = Calendar.getInstance();
			Calendar calendarTo = Calendar.getInstance();
			calendarFrom.setTime(fromDate);
			calendarTo.setTime(toDate);
			String fromFinId="";
			String toFinId="";
			SimpleDateFormat formatter  =new SimpleDateFormat("dd-MMM-yyyy",Locale.getDefault());
			fromFinId =formatter.format(calendarFrom.getTime());
			toFinId =formatter.format(calendarTo.getTime());
			fromFinancialYear=EisManagersUtill.getCommonsService().getFinancialYearByDate(fromDate);
			toFinancialYear =EisManagersUtill.getCommonsService().getFinancialYearByDate(toDate);
				
					if(fromFinancialYear!=null && toFinancialYear !=null)
					{
						if(fromFinancialYear.getId().intValue()==toFinancialYear.getId().intValue())
						{
							datePeriodFY = new DatePeriodFY();
							datePeriodFY.setFromDate(fromDate);
							datePeriodFY.setToDate(toDate);
							datePeriodFY.setFinancial(fromFinancialYear);
							list.add(datePeriodFY);
						}
						else if(fromFinancialYear.getId().intValue()!=toFinancialYear.getId().intValue())
						{
							java.util.Date toDateNext = new java.util.Date();
							java.util.Date fromDateNext = new java.util.Date();
							fromDateNext = fromDate;
							datePeriodFY = new DatePeriodFY();
							datePeriodFY.setFromDate(fromDateNext);
							Calendar calendartoDateNext = Calendar.getInstance();
							calendartoDateNext.setTime(toFinancialYear.getStartingDate());
							calendartoDateNext.add(Calendar.DATE,-1);
							toDateNext = calendartoDateNext.getTime();
							datePeriodFY.setToDate(toDateNext);
							datePeriodFY.setFinancial(fromFinancialYear);
							list.add(datePeriodFY);
							datePeriodFY = new DatePeriodFY();
							fromDateNext = toFinancialYear.getStartingDate();
							toDateNext = toDate;
							datePeriodFY.setToDate(toDateNext);
							datePeriodFY.setFromDate(fromDateNext);
							datePeriodFY.setFinancial(toFinancialYear);
							list.add(datePeriodFY);
						}
					}
			
			return list;
		}

		public static Map getStartingAndEndingDateForMonthAanFyer(int month,CFinancialYear financialYear)
		{
			Map<String,java.sql.Date> mp = new HashMap<String,java.sql.Date>();
			Calendar calender = Calendar.getInstance();
			CFinancialYear financial = financialYear;
			//finId = financial.getId(); //Commented this to fix the holidays bug : SundayHoliday set was loading for only one fin year (first time selected).
										//If we change the year, it was not loading the Holiday set for changed financial year.
										//We need to check thoroughly whether commenting finId here is affecting any other features.
			java.util.Date startingDate = financial.getStartingDate();
			int year = Integer.valueOf(startingDate.toString().substring(0, 4)).intValue();
			if(month-1<=2 )
			{
				calender.set(year+1, month-1, 1);
			}
			else
			{
				calender.set(year, month-1, 1);
			}
			java.sql.Date startDate =new  java.sql.Date(calender.getTime().getTime());
			
			Calendar cal = new GregorianCalendar(year, month-1, 1);  
	    	int noOfDaysInMonth= cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			calender.add(Calendar.DATE, noOfDaysInMonth-1);
			java.sql.Date endDate =new  java.sql.Date(calender.getTime().getTime());
			mp.put("startDate", startDate);
			mp.put("endDate", endDate);
			return mp;

		}
		
				
		public static void updateHolidaySetForYear(int year,int month)
		{
			LOGGER.info("Year"+year+"month"+month);
			update(year,month);
		}
		
		public static Set getHolidaySetForMonth(int year,int month)
		{
			LOGGER.info("Inside RESETHOLMON="+RESETHOLMON+" year ="+year + " month ="+month);
			if(RESETHOLMON)
			{
				update(year,month);
			}
							
			Map mp = getYearAndMonth();
			
			int mo = 0;
		 	int yr = 0;
			for (Iterator it = mp.entrySet().iterator(); it.hasNext(); )
		  	{
		  		Map.Entry entry = (Map.Entry) it.next();
		  		yr =(Integer) entry.getKey();
		  		mo =(Integer) entry.getValue();
		  	}
			if(yr!=year||mo!=month)
			{
				update(year,month);
			}
			return setOfHolForMon;
		}
		//FIXME cache holiday sets for atleast 3 months (SwarmCache/JBossCache ??)
		private static void update(int year,int month)
		{
			yearAndMon = new HashMap();
			setOfHolForMon = new HashSet();
			yearAndMon.put(year, month);
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
			try
			{
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(sdf.parse(01+"/"+month+"/"+year));
				SimpleDateFormat formatter  =new SimpleDateFormat("dd-MMM-yyyy",Locale.getDefault());
				String fid =formatter.format(calendar.getTime());
				CFinancialYear cFinancialYear =commonsService.findFinancialYearById(Long.valueOf(fid));

				Set hSet = getEmpLeaveService().getHolidaySet(cFinancialYear);
				for(Iterator iter =hSet.iterator();iter.hasNext();)
				{

					String dateStr = (String)iter.next();


					if(Integer.valueOf(dateStr.substring(5,7)).equals(Integer.valueOf(month)))
					{
						setOfHolForMon.add(dateStr);
					}
				}

			}
			catch (Exception he) {
				
				LOGGER.error(he.getMessage());
			}
			RESETHOLMON = false;

		}

		private static Map getYearAndMonth()
		{
			return yearAndMon;
		}
		public static Map getFinMap(List list, String field1, String field2)
		{
			if(list != null && field1 == null && field2 == null )
			{
				finYearMap = getEmployeeService().getMapForList(list);
			}
			else if(list != null && field1 != null && field2 != null)
			{
				finYearMap = getEmployeeService().getMapForList(list, field1, field2);
			}
			return finYearMap;
		}
		
		//TODO Remove this -  use API that u add in CommonsManager
		public static CFinancialYear getFinYearForGivenDate(java.util.Date dte)throws Exception
		{
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(dte);
			SimpleDateFormat formatter  =new SimpleDateFormat("dd-MMM-yyyy",Locale.getDefault());
			//String finId =EisManagersUtill.getCommonsService().getFinancialYearByDate(dte);
			CFinancialYear financialYear=null;
			/*if(StringUtils.trimToEmpty(finId).equals(""))
			{
				
				throw new FinancialYearFailureException("Financial year not found for the given dates!!");
			}
			else
			{
				financialYear=commonsService.findFinancialYearById(Long.valueOf(finId));
			}*/
			 financialYear =EisManagersUtill.getCommonsService().getFinancialYearByDate(dte);
			return financialYear;

		}

		public static CommonsService getCommonsService()
	    {
	        return new CommonsServiceImpl();
	    }

		
		
		
}
