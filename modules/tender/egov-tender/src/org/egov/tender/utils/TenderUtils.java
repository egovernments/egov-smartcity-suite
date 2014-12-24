package org.egov.tender.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class TenderUtils {
	
	public static String initCapString(String oldString)
	{
		String newString=new String();
		newString=(oldString.charAt(0)+"").toUpperCase();
		newString=newString+oldString.substring(1, oldString.length()).toLowerCase();
		return newString;
	}
	
	public static String getMonth(Date date)
	{
		SimpleDateFormat sdf;
		sdf = new SimpleDateFormat("MM");
		return sdf.format(date);
	}
	

}
