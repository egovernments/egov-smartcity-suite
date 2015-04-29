package org.egov.works.models.masters;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.egov.commons.Period;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infstr.ValidationError;
import org.egov.infstr.models.BaseModel;
import org.egov.infstr.models.Money;

/**
 * This class represents the entity EGW_OVERHEAD_RATE
 * 
 * @author Divya
 *
 */
public class OverheadRate extends BaseModel 
{
	@Valid
	@Required(message="validity.period.not.null")
	private Period validity;
	
	private Overhead overhead;
	
	@Min(value=0,message="overhead.percentage.not.negative")
	private double percentage;
	
	@Valid
	private Money lumpsumAmount;
	
	private Long overheadId;
	
	public Long getOverheadId() {
		return overheadId;
	}

	public void setOverheadId(Long overheadId) {
		this.overheadId = overheadId;
	}

	public OverheadRate()
	{
		
	}

	public OverheadRate(double percentage, Money lumpsum)
	{
		this.percentage = percentage;
		this.lumpsumAmount = lumpsum;
	}
	public Overhead getOverhead() 
	{
		return overhead;
	}

	public void setOverhead(Overhead overhead) {
		this.overhead = overhead;
	}

	public Money getLumpsumAmount() {
		return lumpsumAmount;
	}

	public void setLumpsumAmount(Money lumpsumAmount) {
		this.lumpsumAmount = lumpsumAmount;
	}

	public Period getValidity() {
		return validity;
	}

	public void setValidity(Period validity) {
		this.validity = validity;
	}

	public double getPercentage() {
		return percentage;
	}

	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}
	
	/**
	 * This method validates the overhead rate values.
	 * Appropriate validation error is returned in each of the following scenarios:
	 * <ol>
	 * <li> If percentage is less than zero or greater than 100.</li>
	 * <li> If neither of percentage or lump sum amount is present.</li>
	 * <li> If start date is not present, or start date date falls after the end date.</li>
	 * </ol>
	 * 
	 *  @return a list of <code>ValidationError</code> containing the appropriate error messages
	 *  or null in case of no errors.
	 */
	public List<ValidationError> validate() {
		List<ValidationError> validationErrors = new ArrayList<ValidationError>();
		
		if(percentage<0.0 || percentage >100){
			validationErrors.add(new ValidationError("percentage","estimate.overhead.percentage.lessthan.100"));
		}
			
		if(percentage==0.0 && (lumpsumAmount == null || lumpsumAmount.getValue()==0.0)){
			validationErrors.add(new ValidationError("percentage","estimate.overhead.percentage_or_lumpsum_needed"));
			
		}
		
		if(percentage>0.0 && (lumpsumAmount != null && lumpsumAmount.getValue()>0.0)){
			validationErrors.add(new ValidationError("percentage","estimate.overhead.only_one_of_percentage_or_lumpsum_needed"));
			
		}

		if(validity == null || (validity !=null && !compareDates(validity.getStartDate(),validity.getEndDate()))){
			validationErrors.add(new ValidationError("validity","estimate.overhead.invalid_date_range"));
		}
		
		if(!validationErrors.isEmpty()){
			return validationErrors;
		}
		return null;
	}
	

	
	/**
	 * compares two date object
	 * 
	 * return type boolean
	 */
    public static boolean compareDates(java.util.Date startDate,java.util.Date endDate)
    {
    	if(startDate==null)
    	{
    		return false;
    	}
    	
    	if(endDate==null)
    	{
    		return true;
    	}
    	
    	if(endDate.before(startDate))
    	{
    		return false;
    	}
    	
    	return true;
    }
}
