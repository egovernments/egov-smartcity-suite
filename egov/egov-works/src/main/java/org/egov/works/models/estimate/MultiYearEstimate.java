package org.egov.works.models.estimate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.constraints.Min;

import org.egov.commons.CFinancialYear;
import org.egov.infstr.ValidationError;
import org.egov.infstr.models.BaseModel;

public class MultiYearEstimate extends BaseModel {
	
	private AbstractEstimate abstractEstimate;
	private CFinancialYear financialYear;	
	@Min(value=0,message="multiYeareEstimate.percentage.not.negative")
	private double percentage;
	
	public MultiYearEstimate() {}
	public MultiYearEstimate(AbstractEstimate abstractEstimate,CFinancialYear financialYear,double percentage) {
		this.abstractEstimate=abstractEstimate;
		this.financialYear=financialYear;
		this.percentage=percentage;
	}
	public AbstractEstimate getAbstractEstimate() {
		return abstractEstimate;
	}
	public void setAbstractEstimate(AbstractEstimate abstractEstimate) {
		this.abstractEstimate = abstractEstimate;
	}
	public CFinancialYear getFinancialYear() { 
		return financialYear;
	}
	public void setFinancialYear(CFinancialYear financialYear) {
		this.financialYear = financialYear;
	}
	public double getPercentage() {
		return percentage;
	}
	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}
	public List<ValidationError> validate() {
		if(percentage<0.0){
			return Arrays.asList(new ValidationError("percentage","multiYeareEstimate.percentage.percentage_greater_than_0"));
		}
		if(percentage>100.0){
			return Arrays.asList(new ValidationError("percentage","multiYeareEstimate.percentage.percentage_less_than_100"));
		}
		return new ArrayList<ValidationError>();
	}	
}
