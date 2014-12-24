package com.exilant.exility.updateservice;

import java.util.Date;

/*
 * 
 * @author raghu.bhandi, Exilant Consulting
 *
 * Defines teh attributes of a column in the table.
 *  Object is instantaited and populated with the XML Loader.
 * Used excelusively by ColumnDefinition Class
 */

public class ColumnDefinition {
	public String name;
	public String dataType;
	public boolean isRequired;
	public String min;
	public String max;
	public int maxLength = 0; // 0 inplies no max limit
	public String defaultValue;
	public String descriptionId;

	//DataType has overloaded methods with dataType as string or int.
	// however, int is faster, hence the int is stored in the beginning.
	// Well why the H are we getting String?? Simple. Let prgrammers use string
	//   in XML lest they make mistakes..
	// TableDefinition sets this and the other ones after loading the class
	int dataTypeInt = 0;
	int dataSubType = 0; //0=String/boolean, 1=number, 2 = date
	double dblMin;
	double dblMax;
	boolean requiresQuote = true;

	//Know what? I did optimize the date min/max fields as well, but, before I started
	// testing, I realized that 'today' will be stale if cached..
	// Shoudl we cache that, and change when day passes ?? I will pass that..
	// dateMin and date Max are only the parsed Date objects from min/max String
	Date dateMin;
	Date dateMax;
	
	public ColumnDefinition(){
		super();
	}
	
	public boolean isValid(String value){
		if (null == value || value.length() == 0){
			// value not specified. if isRequired, it is invalid
			if (this.defaultValue == null && isRequired) return false;
			return true;
		}
		
		if(!DataType.isValid(this.dataTypeInt, value)) return false;
		
		if (maxLength != 0 && value.length() > maxLength) return false;

		//do type specific validations on min/max etc.. 
		if (DataType.isNumericType(this.dataTypeInt)){
			// we have already validated that the string contains numeric data type.
			// So, unless the routine has a bug, following parse should not genreate exceptions
			if (!isValidNumericField(value)) return false;
		}else if(DataType.isDateType(this.dataTypeInt)){
			if (!isValidDateField(value)) return false;
		}

		if (descriptionId != null && descriptionId.length() > 0){
				// use DescriptionService to validate whether this value is OK
				//valid = (SomeMethod(descriptionId, value) != null);
		}

		return true;
	}

	private boolean isValidNumericField(String value){
		if (null == min && null == max) return true;
		//instead of getting into teh exact type of value, let us use double
		double doubleValue = Double.parseDouble(value);;
	
		if (null != min && doubleValue < dblMin) return false;
		if (null != max && doubleValue > dblMax) return false;

		return true;
	}
	
	private boolean isValidDateField(String value){
		
		if (null == dateMin && null == dateMax && dataTypeInt == DataType.ANYDATE) return true;

		Date date = DataType.getDate(value);
		Date today = DataType.getToday();
		Date mdate;
		// I refactored the maze of conditions, and am not sure whether one of the 
		// earlier structures was better than this one..
		// any which way.. it is looking complex..
		
		//first check lower bound. Lower bound is decided by dateMin and FUTUREDATE
		if (null != dateMin){
			mdate = dateMin;
			if (dataTypeInt == DataType.FUTUREDATE && dateMin.before(today))mdate = today;
			if(date.before(mdate)) return false;
		}else{ //min not specified
			if(dataTypeInt == DataType.FUTUREDATE && date.before(today)) return false;
		}
		
		//check upper bound
		if (null != dateMax){
			mdate = dateMax;
			if (dataTypeInt == DataType.PASTDATE && dateMax.after(today))mdate = today;
			if(date.after(mdate)) return false;
		}else{ //min not specified
			if(dataTypeInt == DataType.PASTDATE && date.after(today)) return false;
		}
		
		return true;
	}

// optimize is called in the beginning by TableDefinition;
// note that other routines like isValid are executed only after this one executes..
	public void optimize(){
		dataTypeInt = DataType.getTypeInt(dataType);

		if (DataType.isNumericType(dataTypeInt)){
			dataSubType = 1;
			if (null != min || null != max){
				if (null == min)dblMin = Double.MIN_VALUE;
				else dblMin = Double.parseDouble(min);
				if (null == max)dblMax = Double.MAX_VALUE;
				else dblMax = Double.parseDouble(max);
			}
		}else if (DataType.isDateType(dataTypeInt)){
			dataSubType = 2;
			//DataType.getDate() returns null if max is not parsable
			if (null != max ) dateMax = DataType.getDate(max);
			if (null != min ) dateMin = DataType.getDate(min);
		}else dataSubType = 0;
		
		if (dataSubType == 1 || dataTypeInt == DataType.BOOLEAN) requiresQuote = false;
		else requiresQuote = true;
	}
}