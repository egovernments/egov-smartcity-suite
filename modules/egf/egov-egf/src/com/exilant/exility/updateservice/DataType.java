package com.exilant.exility.updateservice;
/*
 * Provides predefined names for data types. These are identical to ones used in PageManager on the cient side
 * Called quite frequently, and can be completely static.
 * All methods are static. constructor is private to ensure that no one instantiates it 
 */

import java.util.Date;
import java.text.*; 
import java.util.regex.*;

public class DataType {
	
	 // pnemonic for convinience	
	public static final int ANYCHAR        = 0;
	public static final int ALPHA          = 1; 
	public static final int ALPHANUMERIC   = 2;
	public static final int UNSIGNEDINT    = 3;
	public static final int SIGNEDINT      = 4;
	public static final int UNSIGNEDDECIMAL= 5;
	public static final int SIGNEDDECIMAL  = 6;
	public static final int ANYDATE        = 7;
	public static final int PASTDATE       = 8;
	public static final int FUTUREDATE     = 9;
	public static final int EMAIL          = 10;
	public static final int BOOLEAN        = 11;
	public static final int REGEX	       = 12;
	
	public static final Date unknownDate = new Date(0);
	
	
	 //Allow use of strings instead of numbers 
	private static final String[]dataTypes = {"ANYCHAR",
		 "ALPHA",
		 "ALPHANUMERIC",
		 "UNSIGNEDINT",
		 "SIGNEDINT",
		 "UNSIGNEDDECIMAL",
		 "SIGNEDDECIMAL",
		 "ANYDATE",
		 "PASTDATE",
		 "FUTUREDATE",
		 "EMAIL",
		 "BOOLEAN",
		 "REGEX"
	 };
	
	//Regular Expresions for each data type.	
	 private static final String[] regexStrings = {
	 	"^.*$",
	 	"^[a-zA-Z]*$",
		"^\\w*$",
		"^\\+?\\d*$",
		"^[+-]?\\d*$",
		"^\\+?\\d*\\.?\\d*$",
		"^[+-]?\\d*\\.?\\d*$",
		"^\\d\\d?-\\w\\w\\w-\\d\\d\\d\\d$",
		"^\\d\\d?-\\w\\w\\w-\\d\\d\\d\\d$",
		"^\\d\\d?-\\w\\w\\w-\\d\\d\\d\\d$",
		"^.*^", // complex to write, let us do it easily in string comparison
		"^\\w+(\\.?\\w+)?@[\\w-]+(\\.[\\w-]+)*$" } ;

	// for effeciency, regexes are compiled and saved in static array
	private static final Pattern[] dataTypePatterns = new Pattern[regexStrings.length];
	private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
	
	static {
		for (int i=0; i<regexStrings.length; i++)dataTypePatterns[i] = Pattern.compile(regexStrings[i]);
		simpleDateFormat.setLenient(false);
	}

	private DataType(){
		super();
	}

	public static boolean isNumericType(String dataType){
		return isNumericType(getTypeInt(dataType));
	}

	public static boolean isNumericType(int dataType){
		if (dataType > 2 && dataType < 7 ) return true;
		return false;
	}

	public static boolean isDateType(String dataType){
		return isDateType(getTypeInt(dataType));
	}


	public static boolean isDateType(int dataType){
		if (dataType > 2 && dataType < 7 ) return true;
		return false;
	}


	public static boolean isValid(String dataType, String value){
		return isValid(getTypeInt(dataType),value);
	}


	public static boolean isValid(int dataType, String value){
		
		if (dataType < 0 || dataType >= dataTypes.length) return false; //invalid dataType
		boolean valid;
		if (dataType == DataType.BOOLEAN){
			value = value.toUpperCase();
			if (value.equals("Yes") || value.equals("NO")
				|| value.equals("TRUE") || value.equals("FALSE")
				|| value.equals("0") || value.equals("1")
				)return true;
			return false;
		}
		Matcher matcher = dataTypePatterns[dataType].matcher(value);
		valid =  matcher.find();
		if (!valid) return false;
		// date formats
		if ((dataType ==DataType.ANYDATE || 
					  dataType ==DataType.FUTUREDATE || 
					  dataType ==DataType.PASTDATE)){ //it is a date field
			Date date;
			try {
				date = simpleDateFormat.parse(value);
			} catch (ParseException e) {
				return false;
			}

			// but then it would have parsed 35-may-2003 !!!!
			Date today = getToday();
			if (dataType == DataType.FUTUREDATE && today.after(date)) return false;
			if (dataType == DataType.PASTDATE && today.before(date)) return false;
			return true;
		}
		return true ;
	}


	public static int getTypeInt(String type){
	   type = type.toUpperCase();
		for (int i = 0; i < dataTypes.length; i++){
			if (dataTypes[i].equals(type)) return i;
		}
		return -1; // indicates an invalid datatype string
	}

	public static Date getToday(){
		Date date = new Date();
		try {
			date = simpleDateFormat.parse(simpleDateFormat.format(date));
		} catch (ParseException e) {}
		return date;
	}
	
	public static Date getDate(String value){
		if (null == value) return null;
		try {
			return simpleDateFormat.parse(value);
		} catch (ParseException e) {
			return null;
		}
	}

	public static String getDateString(Date value){
		return simpleDateFormat.format(value);
	}
	
}
