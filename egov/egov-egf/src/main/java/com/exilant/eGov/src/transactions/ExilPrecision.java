/*
 * Created on Jan 13, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.transactions;

import org.apache.log4j.Logger;

/**
 * @author siddhu
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ExilPrecision 
{
	private static final Logger LOGGER = Logger.getLogger(ExilPrecision.class);
	public static double convertToDouble(double dNum,int precision)
	{
		//precision++;
		if(Double.isNaN(dNum))return 0;
		int afterPoint=(int)Math.pow(10,precision);
		String fraction="0.";
		for(int i=0;i<precision;i++)
			fraction+="0";
		fraction+="5";
		double adjustFraction=Double.parseDouble(fraction);
		double retNum=Math.floor((dNum+adjustFraction)*afterPoint)/afterPoint;
		return retNum;
	}
	public static String convertToString(double dNum,int precision)
	{
		//precision++;
		if(Double.isNaN(dNum))return "0";
		int afterPoint=(int)Math.pow(10,precision);
		String fraction="0.";
		for(int i=0;i<precision;i++)
			fraction+="0";
		fraction+="5";
		double adjustFraction=Double.parseDouble(fraction);
		double retNum=Math.floor((dNum+adjustFraction)*afterPoint)/afterPoint;
		return String.valueOf(retNum);
	}
	public static double convertToDouble(String sNum,int precision)
	{
		//precision++;
		double dNum=0;
		try{
			 dNum=Double.parseDouble(sNum);
		}catch(Exception e){
		LOGGER.error("There is error "+e.getMessage());
		return 0;
		}
		int afterPoint=(int)Math.pow(10,precision);
		String fraction="0.";
		for(int i=0;i<precision;i++)
			fraction+="0";
		fraction+="5";
		double adjustFraction=Double.parseDouble(fraction);
		double retNum=Math.floor((dNum+adjustFraction)*afterPoint)/afterPoint;
		return retNum;
	}
	public static String convertToString(String sNum,int precision)
	{
		//precision++;
		double dNum=0;
		try{
			dNum=Double.parseDouble(sNum);
		}catch(Exception e){
			LOGGER.error("There is error "+e.getMessage());
			return "0";}
		int afterPoint=(int)Math.pow(10,precision);
		String fraction="0.";
		for(int i=0;i<precision;i++)
			fraction+="0";
		fraction+="5";
		double adjustFraction=Double.parseDouble(fraction);
		double retNum=Math.floor((dNum+adjustFraction)*afterPoint)/afterPoint;
		return String.valueOf(retNum);
	}
	public static void main(String[] args)
	{
		 
		if(LOGGER.isDebugEnabled())     LOGGER.debug(ExilPrecision.convertToString("18.245287987",2));
		
	}
}
