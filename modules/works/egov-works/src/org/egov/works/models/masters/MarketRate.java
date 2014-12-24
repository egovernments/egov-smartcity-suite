package org.egov.works.models.masters;

import java.util.ArrayList;
import java.util.List;

import org.egov.commons.Period;
import org.egov.infstr.ValidationError;
import org.egov.infstr.models.BaseModel;
import org.egov.infstr.models.Money;
import javax.validation.Valid;

public class MarketRate extends BaseModel {
	private ScheduleOfRate schedule;
	
	@Valid
	private Money marketRate;
	private Period validity;
	public ScheduleOfRate getSchedule() {
		return schedule;
	}
	public void setSchedule(ScheduleOfRate schedule) {
		this.schedule = schedule;
	}
	public Money getMarketRate() {
		return marketRate;
	}
	public void setMarketRate(Money marketRate) {
		this.marketRate = marketRate;
	}
	public Period getValidity() {
		return validity;
	}
	public void setValidity(Period validity) {
		this.validity = validity;
	}
	
	public MarketRate()
	{
		
	}
	public MarketRate(Money sorrate){
		this.marketRate = sorrate;
	}
	
	public List<ValidationError> validate() {
		List<ValidationError> validationErrors = new ArrayList<ValidationError>();
		if(marketRate == null || marketRate.getValue()==0.0 ||(marketRate != null && marketRate.getValue()==0.0)){			
			validationErrors.add(new ValidationError("marketRate","sor.marketrate.lessthan.0"));
		}	
	
		if(validity == null || (validity !=null && validity.getStartDate()==null)){
			validationErrors.add(new ValidationError("validity","sor.marketrate.startDate__empty"));
		}
		else {
			 if(validity == null || (validity !=null && !compareDates(validity.getStartDate(),validity.getEndDate()))){
				validationErrors.add(new ValidationError("validity","sor.rate.invalid_date_range"));
			}
		}
	
		return (validationErrors.isEmpty())? null : validationErrors;
		
	}
	public static boolean compareDates(java.util.Date startDate,java.util.Date endDate) {
		if(startDate==null) {
			return false;
		}
		
		if(endDate==null) {
			return true;
		}
		
		if(endDate.before(startDate)) {
			return false;
		}    	
		return true;
	}	
}