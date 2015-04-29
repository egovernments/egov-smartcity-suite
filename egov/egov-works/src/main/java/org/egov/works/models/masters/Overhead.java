package org.egov.works.models.masters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.egov.commons.CChartOfAccounts;
import org.egov.commons.Period;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.egov.infstr.ValidationError;
import org.egov.infstr.models.BaseModel;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.LocalDate;

/**
 * This class represents the entity EGW_OVERHEAD
 * 
 * @author Divya
 * @version 1.0, 20th May 2009
 */
@Unique(fields="name", id="id",tableName="EGW_OVERHEAD",columnName="NAME",message="overhead.name.isunique")
public class Overhead extends BaseModel {
	
	public static final String BY_DATE_AND_TYPE = "BY_DATE_AND_TYPE";
	public static final String OVERHEADS_BY_DATE = "OVERHEADS_BY_DATE";
	private String name;
	private String description;

	private CChartOfAccounts account;

	private ExpenditureType expenditureType;
	
	private List<OverheadRate> overheadRates = new LinkedList<OverheadRate>();
	
	public Overhead(){} 
	public Overhead(String name, String description) {
		this.name = name;
		this.description = description;
	}

	@NotEmpty(message="overhead.name.not.empty")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@NotEmpty(message="overhead.description.not.empty")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@NotNull(message="overhead.account.not.empty")
	public CChartOfAccounts getAccount() {
		return account;
	}

	public void setAccount(CChartOfAccounts account) {
		this.account = account;
	}

	@NotNull(message="overhead.expenditure.not.empty")
	public ExpenditureType getExpenditureType() {
		return expenditureType;
	}

	public void setExpenditureType(ExpenditureType expenditureType) {
		this.expenditureType = expenditureType;
	}

	public List<OverheadRate> getOverheadRates() {
		return overheadRates;
	}

	public void setOverheadRates(List<OverheadRate> overheadRates) {
		this.overheadRates = overheadRates;
	}
	
	/**
	 * This method checks if no overhead rates have been entered for the Overhead.
	 * 
	 * @return list of <code>ValidationError</code> indicating that at least one 
	 * Overhead Rate should be entered for teh Overhead
	 */
	private List<ValidationError> checkForNoRatesPresent()
	{
		if(overheadRates!= null && overheadRates.isEmpty())
		{
			return Arrays.asList(new ValidationError("overheadrate","estimate.overhead.altleastone_overheadrate_needed"));
		}
		else 
		{
			return null;
		}
	}
	
	private List<ValidationError> validateOverheadRates()
	{
		List<ValidationError> errorList = null;
		boolean openEndedRangeFlag = false;

		for (OverheadRate overheadRate : overheadRates) 
		{
				//check if multiple open ended date ranges are present
				if(overheadRate.getValidity().getEndDate() == null && openEndedRangeFlag)
				{
					return Arrays.asList(new ValidationError("openendedrange","estimate.overheadrate.multiple.openendedrange"));
				}
				if(overheadRate.getValidity().getEndDate() == null)
				{
					openEndedRangeFlag = true;
				}
				
				//validation for percentage-lumpsum amount and invalid date ranges 
				errorList = overheadRate.validate();
				if(errorList!=null)
				{
					return errorList;
				}
		}
		return errorList;
	}
	
	private List<ValidationError> validateDateRanges()
	{
		List<Period> validDates = new ArrayList<Period>();
		//check for date range over lap
		validDates.add(0,((OverheadRate)overheadRates.get(0)).getValidity());
		Date existingStartDate = null;
		Date checkStartDate = null;
		Date checkEndDate = null;
		Period existingPeriod = null;
		Period checkPeriod1 = null;
		boolean flag1 = true;
		int k=1;
		for(int i=1;i<overheadRates.size();i++)
		{
			checkStartDate = ((OverheadRate)overheadRates.get(i)).getValidity().getStartDate();
			checkEndDate = ((OverheadRate)overheadRates.get(i)).getValidity().getEndDate();
			checkPeriod1 = new Period(checkStartDate, checkEndDate);
			for(int j=0;j<validDates.size();j++)
			{
				existingStartDate = ((Period)validDates.get(j)).getStartDate();
				existingPeriod = (Period)validDates.get(0);
				
				//check if the period to be checked is within any of the existing periods.
				if(isWithin(existingPeriod, checkStartDate) || isWithin(checkPeriod1, existingStartDate)) { 
					flag1 = false;
					break;
				}
				else if(checkEndDate!=null && isWithin(existingPeriod, checkEndDate)) {
					flag1 = false;
					break;
				}
			}
			if(flag1) {
				validDates.add(k++, checkPeriod1);
			}
			else {
				return Arrays.asList(new ValidationError("dateoverlap","estimate.overhead.dates.overlap"));				
			}			
		}
		
		return null;
	}
	
	/**
	 * This method removes any empty over head rate from the list of over head rates.
	 */
	private void removeEmptyRates()
	{
		List<OverheadRate> emptyRateObjs = new LinkedList<OverheadRate>();
		
		for(OverheadRate overheadRate : overheadRates)
		{
			if(overheadRate.getPercentage() == 0.0 && 
					(overheadRate.getLumpsumAmount() == null || overheadRate.getLumpsumAmount().getValue() == 0.0) && 
					(overheadRate.getValidity() == null || 
							(overheadRate.getValidity().getStartDate() == null || overheadRate.getValidity().getEndDate() == null)))
			{
				
				emptyRateObjs.add(overheadRate);
			}
		}
		
		overheadRates.removeAll(emptyRateObjs);
	}
	
	/**
	 * This method performs the validations for the over head rates entered by the user.
	 */
	public List<ValidationError> validate()
	{
		List<ValidationError> errorList = new ArrayList<ValidationError>();
		
		removeEmptyRates();
		if((errorList = checkForNoRatesPresent()) != null)
		{
			return errorList;
		}
		
		if((errorList = validateOverheadRates())!=null)
		{
			return errorList;
		}
		
		if((errorList = validateDateRanges())!=null)
		{
			return errorList;
		}
		
		
		return errorList;
		
	}
	
	
	public boolean isWithin(Period period,Date dateTime)
	{
		LocalDate start=new LocalDate(period.getStartDate());
		LocalDate end=new LocalDate(period.getEndDate());
		LocalDate date=new LocalDate(dateTime);
		if(period.getEndDate()==null)
			return start.compareTo(date)<=0;  
		else
			return start.compareTo(date)<=0 && end.compareTo(date)>=0;  
	}

	public void setOverheadRate(List<OverheadRate> overheadRates) 
	{
		this.overheadRates = overheadRates;
	}

	public void addOverheadRate(OverheadRate overheadRate) 
	{
		this.overheadRates.add(overheadRate);
	}
	
	public String getValidPercentage(Date estimateDate)
	{
		for (OverheadRate overheadRate : overheadRates) 
		{
			if(overheadRate != null && 
					isWithin(overheadRate.getValidity(), estimateDate) && 
					overheadRate.getPercentage() > 0.0)
			{
				return String.valueOf(overheadRate.getPercentage());
			}
		}
		
		return "";
	}
	
	public OverheadRate getOverheadRateOn(Date estimateDate) 
	{
		if(estimateDate==null) 
		{
			throw new EGOVRuntimeException("no.rate.for.date");
		}
		
		for (OverheadRate overheadRate : overheadRates) 
		{
			if(overheadRate != null && isWithin(overheadRate.getValidity(), estimateDate)) {
				return overheadRate;
			}
		}
		
		return null;
	}
}
