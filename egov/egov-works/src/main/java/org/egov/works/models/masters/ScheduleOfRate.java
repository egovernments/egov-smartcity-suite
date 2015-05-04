/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It
	   is required that all modified versions of this material be marked in
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program
	   with regards to rights under trademark law for use of the trade names
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.works.models.masters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.egov.common.entity.UOM;
import org.egov.commons.Period;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infstr.ValidationError;
import org.egov.infstr.models.BaseModel;
import org.egov.infstr.utils.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.LocalDate;

public class ScheduleOfRate extends BaseModel {
	private static final Logger logger = Logger.getLogger(ScheduleOfRate.class);
	static Integer MAX_DESCRIPTION_LENGTH = 100;

	@NotEmpty(message="sor.code.not.empty")  
	private String code;
	@Required(message="sor.category.not.null")
	
	private ScheduleCategory category; 
	
	@NotEmpty(message="sor.description.not.empty")
	private String description;
	@Required(message="sor.uom.not.null")
    private UOM uom; 
	
    private ScheduleOfRateType type;
    
    private boolean isDepositWorksSOR;
    
    public ScheduleOfRate(){}
    public ScheduleOfRate(String code,String description){
    	this.code = code;
    	this.description = description;
    }
    
   private List<Rate> rates = new LinkedList<Rate>();
   private List<MarketRate> marketRates = new LinkedList<MarketRate>();
   
	
   public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
    @Valid
    public ScheduleCategory getCategory() {
		return category;
	}
	public void setCategory(ScheduleCategory category) {
		this.category = category;
	}
	public String getDescription() {
		return description;
	}
	public String getDescriptionJS() {
		return StringUtils.escapeJavaScript(description);
	}
	
	public void setDescription(String description) {
		this.description = StringEscapeUtils.unescapeHtml(description);
	}
	public UOM getUom() {
		return uom;
	}
	public void setUom(UOM uomid) {
		this.uom = uomid;
	}
	public ScheduleOfRateType getType() {
		return type;
	}
	public void setType(ScheduleOfRateType type) {
		this.type = type;
	}

    public List<Rate> getRates() {
		return rates;
	}
	
	public void setRates(List<Rate> rates) {
		this.rates = rates;
	}
	
	public String getSummary(){
		if(description.length()<=MAX_DESCRIPTION_LENGTH){
			return description;
		}
		return first(MAX_DESCRIPTION_LENGTH/2,description)+"..."+last(MAX_DESCRIPTION_LENGTH/2,description);
		
	}
	
	public String getSummaryJS(){
		return StringUtils.escapeJavaScript(getSummary());		
	}

	public String getCategorId(){
		return String.valueOf(this.category.getId());
	}
	
	protected String first(int number,String description) {
		return description.substring(0, number>=description.length()?description.length():number);
	}
	protected String last(int number,String description) {
		int begin = description.length() - number;
		return description.substring(begin<0?description.length():begin, description.length());
	}
	
	public String getSearchableData(){
		StringBuilder builder = new StringBuilder();
		builder.append(getCode()).append(" ").append(getDescription());
		return builder.toString();
	}
	public Rate getRateOn(Date estimateDate) {
		if(estimateDate==null) {
			throw new EGOVRuntimeException("no.rate.for.date");
		}
		for (Rate rate : rates) {
			if(isWithin(rate.getValidity(), estimateDate)){
				return rate;
			}
		}
		throw new EGOVRuntimeException("no.rate.for.date");
	}
	
	public boolean isWithin(Period period,Date dateTime){
		LocalDate start=new LocalDate(period.getStartDate()); 
		LocalDate end=null;
		if(period.getEndDate()!=null){
			end=new LocalDate(period.getEndDate());
		}
		LocalDate date=new LocalDate(dateTime);
		
		if(end==null) {
			return start.compareTo(date)<=0;
		}
		else {
			return start.compareTo(date)<=0 && end.compareTo(date)>=0;
		}
		
		
		//return (end!=null)? start.compareTo(date)<=0 && end.compareTo(date)>=0 : start.compareTo(date)<=0;
		
	}
	public boolean hasValidRateFor(Date estimateDate) {
		try{
			Rate rate=getRateOn(estimateDate);
			return rate!=null;
		}catch(EGOVRuntimeException e){
			logger.error("Rate :"+e.getMessage());
			return false;
		}
	}
	private List<ValidationError> checkForNoRatePresent() {
		
		if(rates!= null && rates.isEmpty()) {
			return Arrays.asList(new ValidationError("sorRate","sor.rate.altleastone_sorRate_needed"));
		}
		else {
			return null;
		}
		
		//return (rates.isEmpty())?null:Arrays.asList(new ValidationError("sorRate","sor.rate.altleastone_sorRate_needed"));
	}	
	
	private void removeEmptyRates() {
		List<Rate> emptyRateObjs = new LinkedList<Rate>();
		for(Rate rat : rates) {
			 if((rat.getRate() == null || rat.getRate().getValue() == 0.0) && (rat.getValidity() == null || (rat.getValidity().getStartDate() == null && rat.getValidity().getEndDate() == null))) {
				emptyRateObjs.add(rat);
			 }
		}
		rates.removeAll(emptyRateObjs);
	}
	
	protected List<ValidationError> validateRates() {
		List<ValidationError> errorList = null;
		boolean openEndedRangeFlag = false;
		
		for (Rate rate : rates) {
			if(rate.getValidity().getEndDate() == null && openEndedRangeFlag) {
				return Arrays.asList(new ValidationError("openendedrange","sor.rate.multiple.openendedrange"));
			}
			if(rate.getValidity().getEndDate() == null)	{
				openEndedRangeFlag = true;
			}
			
			errorList = rate.validate();
			
			if(errorList!=null)	{
				return errorList;
			}
		}		
		return errorList;
	}
	
	public void setRate(List<Rate> rates) {
		this.rates = rates;
	}
	public void addRate(Rate rat){
		this.rates.add(rat);
	}
	
	private List<ValidationError> validateDateRanges() {		
		List<Period> validDates = new ArrayList<Period>();		
		validDates.add(0,((Rate)rates.get(0)).getValidity());
		Date existingStartDate = null;
		Date existingEndDate = null;
		Date checkStartDate = null; 
		Date checkEndDate = null;
		Period existingPeriod = null;
		Period checkPeriod1 = null;
		boolean flag1 = true;
		int k=1;
		
		for(int i=1;i<rates.size();i++)	{
			checkStartDate = ((Rate)rates.get(i)).getValidity().getStartDate();
			checkEndDate = ((Rate)rates.get(i)).getValidity().getEndDate();
			checkPeriod1 = new Period(checkStartDate, checkEndDate);
			
			for(int j=0;j<validDates.size();j++) {
				existingStartDate = ((Period)validDates.get(j)).getStartDate();
				existingPeriod = (Period)validDates.get(j);
				
				if(validDates.get(j).getEndDate()==null) {
					existingEndDate=null;
				}else{
					existingEndDate=((Period)validDates.get(j)).getEndDate();
				}
				
				//existingEndDate=(validDates.get(j).getEndDate()!=null)?((Period)validDates.get(j)).getEndDate():null;
				
				//check if the period to be checked is within any of the existing periods.
				if(isWithin(existingPeriod, checkStartDate) || isWithin(checkPeriod1, existingStartDate) || (checkEndDate!=null && isWithin(existingPeriod, checkEndDate)) || (existingEndDate!=null && isWithin(checkPeriod1, existingEndDate)))	{
					flag1 = false;
					break;
				}
				else if((checkEndDate!=null && existingEndDate!=null) && (isWithin(existingPeriod, checkEndDate) ||  isWithin(checkPeriod1, existingEndDate)))	{
					flag1 = false;
					break; 
				}
			}
		
		if(flag1) {		
			validDates.add(k++, checkPeriod1);
		}
		else {			
			return Arrays.asList(new ValidationError("dateoverlap","sor.rate.dates.overlap"));
		}			
	}		
	return null;
	}
	

	/* start market rate */
	/**
	 * @return the marketRates
	 */
	public List<MarketRate> getMarketRates() {
		return marketRates;
	}
	/**
	 * @param marketRates the marketRates to set
	 */
	public void setMarketRates(List<MarketRate> marketRates) {
		this.marketRates = marketRates;
	}
	
	/* market rate */
	public MarketRate getMarketRateOn(Date estimateDate) {
		if(estimateDate==null) {
			return null;
		}
		for (MarketRate marketRate : marketRates) {
			if(isWithin(marketRate.getValidity(), estimateDate)){
				return marketRate;
			}
		}
		return null;
	}
	
	public boolean hasValidMarketRateFor(Date estimateDate) {
		MarketRate marketRate=getMarketRateOn(estimateDate);
		return marketRate!=null;
	}
	/*
	private List<ValidationError> checkForNoMarketRatePresent() {	
		if(marketRates!= null && marketRates.size() == 0) {			
			return Arrays.asList(new ValidationError("sorMarketRate","sor.marketrate.altleastone_sorRate_needed"));		
		}
		else {			
			return null;
		}	
	}
	*/
	private void removeEmptyMarketRates() {
		List<MarketRate> emptyMarketRateObjs = new LinkedList<MarketRate>();		
		for(MarketRate marketRate : marketRates) {
			if((marketRate.getMarketRate() == null || marketRate.getMarketRate().getValue() == 0.0) && (marketRate.getValidity() == null || (marketRate.getValidity().getStartDate() == null && marketRate.getValidity().getEndDate() == null))) {								
				emptyMarketRateObjs.add(marketRate);
			}
		}
		marketRates.removeAll(emptyMarketRateObjs);		
	}
	
	protected List<ValidationError> validateMarketRates() {
		List<ValidationError> errorList = null;
		boolean openEndedRangeFlag = false;
		
		for (MarketRate marketRate : marketRates) {
			if(marketRate!=null){
			if(marketRate.getValidity().getEndDate() == null && openEndedRangeFlag) {
				return Arrays.asList(new ValidationError("openendedrange","sor.marketrate.multiple.openendedrange"));
			}
			if(marketRate.getValidity().getEndDate() == null)	{
				openEndedRangeFlag = true;
			}
			errorList = marketRate.validate();
			if(errorList!=null)	{
				return errorList;
			}
			}
		}		
		return errorList;
	}	
	
	public void setMarketRate(List<MarketRate> marketRates) {
		this.marketRates = marketRates;
	}
	
	public void addMarketRate(MarketRate marketRate){
		this.marketRates.add(marketRate);
	}
	
	private List<ValidationError> validateDateRangesForMarketRate() {
		List<Period> validDates = new ArrayList<Period>();		
		validDates.add(0,((MarketRate)marketRates.get(0)).getValidity());
		Date existingStartDate = null;
		Date existingEndDate = null;
		Date checkStartDate = null; 
		Date checkEndDate = null;
		Period existingPeriod = null;
		Period checkPeriod1 = null;
		boolean flag1 = true;
		int k=1;
		
		for(int i=1;i<marketRates.size();i++)	{
			checkStartDate = ((MarketRate)marketRates.get(i)).getValidity().getStartDate();
			checkEndDate = ((MarketRate)marketRates.get(i)).getValidity().getEndDate();
			checkPeriod1 = new Period(checkStartDate, checkEndDate);
			
			for(int j=0;j<validDates.size();j++) {
				existingStartDate = ((Period)validDates.get(j)).getStartDate();
				existingPeriod = (Period)validDates.get(j);
				if(validDates.get(j).getEndDate()==null) {

					existingEndDate=null;
				}else{
					existingEndDate=((Period)validDates.get(j)).getEndDate();
				}
				
				//check if the period to be checked is within any of the existing periods.
				if(isWithin(existingPeriod, checkStartDate) || isWithin(checkPeriod1, existingStartDate) || (checkEndDate!=null && isWithin(existingPeriod, checkEndDate)) || (existingEndDate!=null && isWithin(checkPeriod1, existingEndDate)))	{
					flag1 = false;
					break;
				}
				else if((checkEndDate!=null && existingEndDate!=null) && (isWithin(existingPeriod, checkEndDate) ||  isWithin(checkPeriod1, existingEndDate)))	{
					flag1 = false;
					break; 
				}
			}
			if(flag1) {
				validDates.add(k++, checkPeriod1);
			}
			else {
				return Arrays.asList(new ValidationError("dateoverlap","sor.marketrate.dates.overlap"));
			}			
		}	
		
		return null;
	}
	/* ends market rate */
	
	public List<ValidationError> validate()	{
		List<ValidationError> errorList = null;
		removeEmptyRates();
		if(marketRates!=null && !marketRates.isEmpty()){
			removeEmptyMarketRates();
		}
		
		if((errorList = checkForNoRatePresent()) != null) {
			return errorList;
		}
		
		if((errorList = validateDateRanges())!=null) {
			return errorList;
		}

		if((errorList = validateRates())!=null) {
			return errorList;
		}
		
		/* for market rate */
		if(marketRates!=null && !marketRates.isEmpty()){
			if((errorList = validateDateRangesForMarketRate())!=null) {
				return errorList;
			}
			if((errorList = validateMarketRates())!=null) {
				return errorList;
			}
		}
			
		return errorList;
		
	}
	public boolean getIsDepositWorksSOR() {
		return isDepositWorksSOR;
	}
	public void setIsDepositWorksSOR(boolean isDepositWorksSOR) {
		this.isDepositWorksSOR = isDepositWorksSOR;
	}
}