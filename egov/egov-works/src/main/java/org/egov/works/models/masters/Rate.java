package org.egov.works.models.masters;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.egov.commons.Period;
import org.egov.infstr.ValidationError;
import org.egov.infstr.models.BaseModel;
import org.egov.infstr.models.Money;

public class Rate extends BaseModel {
	private ScheduleOfRate schedule;
	
	@Valid
	private Money rate;
	private Period validity;
	public ScheduleOfRate getSchedule() {
		return schedule;
	}
	public void setSchedule(ScheduleOfRate schedule) {
		this.schedule = schedule;
	}
	public Money getRate() {
		return rate;
	}
	public void setRate(Money rate) {
		this.rate = rate;
	}
	public Period getValidity() {
		return validity;
	}
	public void setValidity(Period validity) {
		this.validity = validity;
	}
	
	public Rate() {	}
	public Rate(Money sorrate){
		this.rate = sorrate;
	}
	
	public List<ValidationError> validate() {		

		List<ValidationError> validationErrors = new ArrayList<ValidationError>();
		if(rate == null || rate.getValue()==0.0 || (rate != null && rate.getValue()==0.0)){			
			validationErrors.add(new ValidationError("rate","sor.rate.lessthan.0"));
		}	
	
		if(validity == null || (validity !=null && validity.getStartDate()==null)){
			validationErrors.add(new ValidationError("validity","sor.rate.startDate__empty"));
		}
		else {
			 if(validity == null || (validity !=null && !compareDates(validity.getStartDate(),validity.getEndDate()))){
				validationErrors.add(new ValidationError("validity","sor.rate.invalid_date_range"));
			}
		}		

		if(validationErrors.isEmpty()){
			return null;
		}
		else {
			return validationErrors;
		}
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