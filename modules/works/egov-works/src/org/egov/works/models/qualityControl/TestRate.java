package org.egov.works.models.qualityControl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.infstr.ValidationError;
import org.egov.infstr.models.BaseModel;
import org.egov.infstr.models.Money;
import org.egov.infstr.models.validator.Required;
import javax.validation.Valid;

public class TestRate extends BaseModel{
	
	@Required(message="testrate.rate.not.null")
	@Valid
	//private double rate;
	private Money rate=new Money(0.0);
	
	@Required(message="testrate.startDate.not.null")
	private Date startDate;
	
	private Date endDate;
	private TestMaster testMaster;
	
	public List<ValidationError> validate() {		

		List<ValidationError> validationErrors = new ArrayList<ValidationError>();
		if(rate == null || rate.getValue()==0.0 || (rate != null && rate.getValue()==0.0)){			
			validationErrors.add(new ValidationError("rate","rate.lessthan.0"));
		}	
	
		if(startDate==null){
			validationErrors.add(new ValidationError("validity","rate.startDate__empty"));
		}
		else {
			 if(!compareDates(startDate,endDate)){
				validationErrors.add(new ValidationError("validity","rate.invalid_date_range"));
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
	
	
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public TestMaster getTestMaster() {
		return testMaster;
	}
	public void setTestMaster(TestMaster testMaster) {
		this.testMaster = testMaster;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Money getRate() {
		return rate;
	}
	public void setRate(Money rate) {
		this.rate = rate;
	}
	
	
}