package org.egov.works.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.apache.log4j.Logger;

public class DateConversionUtil {
	private static final Logger logger = Logger.getLogger(DateConversionUtil.class);
	public DateConversionUtil(){
		
	}
	public void dummy() {
		//do nothing
	}
	public static boolean isWithinDateRange(Date dateToSearch, Date startdate, Date enddate) {
		
		if(enddate==null){
			if((startdate.before(dateToSearch)) || ((dateToSearch.compareTo(startdate)==0)|| (dateToSearch.compareTo(startdate)> 0))){			
				return true;
			}
		}else{
			if((startdate.before(dateToSearch) && enddate.after(dateToSearch)) || ((dateToSearch.compareTo(startdate)==0)|| (dateToSearch.compareTo(enddate)==0))){				
				return true;
			}
		}
		return false;
	}
	
	/**
     * Check the date is before or not ignoring time parameters. 
     * This method is similar to java.util.Date.before, only it will ignore the timestamp. 
     * @param actualDate -  Date that has to check
     * @param when - Date that has to refer as limit
     */
    public static boolean isBeforeByDate(Date actualDate, Date when){
    	SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy",new Locale("en","IN"));
    	String strFromDate = df.format(actualDate);
    	String strToDate = df.format(when);
    	Date tFromDate = null;
    	Date tToDate = null;
    	try
		{
    		// Retain the date without time
    		tFromDate = df.parse(strFromDate);
        	tToDate = df.parse(strToDate);
        	return tFromDate.before(tToDate);
		}
		catch (ParseException pe)
		{
			logger.error("Exp in isBeforeByDate() >>>"+pe);
			return false;
		}
		catch(IllegalArgumentException ie)
		{
			logger.error("Exp in isBeforeByDate() >>>"+ie);
			return false;
		}
    }
}
