package org.egov.works.models.estimate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.egov.infra.persistence.validator.annotation.GreaterThan;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infstr.ValidationError;
import org.egov.infstr.commonMasters.EgUom;
import org.egov.infstr.models.BaseModel;
import org.egov.infstr.models.Money;
import org.egov.works.models.masters.ScheduleOfRate;
import org.egov.works.models.revisionEstimate.RevisionType;

public class Activity extends BaseModel {

	private AbstractEstimate abstractEstimate;
	
	private ScheduleOfRate schedule;
	
	@Valid 
	private NonSor nonSor;
	
	private EgUom uom;

	@Required(message="activity.rate.not.null")
	private Money rate=new Money(0.0);
	
	@Required(message="activity.quantity.not.null")  
	@GreaterThan(value=0,message="activity.quantity.non.negative")
	private double quantity;
	
	@Min(value=0,message="activity.servicetax.non.negative")
	private double serviceTaxPerc;
	
	private double amt;
	
	private Integer srlNo;
	
	private RevisionType revisionType;
	
	private Money sORCurrentRate = new Money(0.0);
	
	private Activity parent;
	
	private String signValue;
	
	public Activity(){};
	public Activity(AbstractEstimate abstractEstimate,ScheduleOfRate schedule,Money rate,Double quantity,Double serviceTaxPerc, NonSor nonSor){
		super();
		this.abstractEstimate=abstractEstimate;
		this.schedule=schedule;
		this.nonSor=nonSor;
		this.rate=rate;
		this.quantity=quantity;
		this.serviceTaxPerc=serviceTaxPerc;
	}
	public AbstractEstimate getAbstractEstimate() {
		return abstractEstimate;
	}
	public void setAbstractEstimate(AbstractEstimate abstractEstimate) {
		this.abstractEstimate = abstractEstimate;
	}
	public ScheduleOfRate getSchedule() {
		return schedule;
	}
	public void setSchedule(ScheduleOfRate schedule) {
		this.schedule = schedule;
	}
	public NonSor getNonSor() {
		return nonSor;
	}
	public void setNonSor(NonSor nonSor) {
		this.nonSor = nonSor;
	}
	public Money getRate() {
		return rate;
	}
	public void setRate(Money rate) {
		this.rate = rate;
	}
	public Double getQuantity() {
		return quantity;
	}
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}
	public Double getServiceTaxPerc() {
		return serviceTaxPerc;
	}
	public void setServiceTaxPerc(Double serviceTaxPerc) {
		this.serviceTaxPerc = serviceTaxPerc;
	}
	public Money getAmount() {
		double amt = rate.getValue() * quantity;
		return new Money(amt);
	}
	public Money getTaxAmount() {
		return new Money(rate.getValue() * quantity * serviceTaxPerc /100.0);
	}
	public Money getAmountIncludingTax() {
		return new Money(getAmount().getValue() + getTaxAmount().getValue());
	}
	
	public Money getSORCurrentRate() {
		Money sorCurrentRate=schedule.getRateOn(abstractEstimate.getEstimateDate()).getRate();
		if(sorCurrentRate!=null){
			BigDecimal currentRate=BigDecimal.valueOf(sorCurrentRate.getValue());
			currentRate=currentRate.setScale(2, BigDecimal.ROUND_UP);
			sorCurrentRate=new Money(currentRate.doubleValue());
		}
		return sorCurrentRate;
	}
	
	public void setSORCurrentRate(Money sORCurrentRate) {
		this.sORCurrentRate = sORCurrentRate;
	}
	
	public Money getSORCurrentMarketRate() {
		Money sorCurrentMarketRate=schedule.getMarketRateOn(abstractEstimate.getEstimateDate()).getMarketRate();
		if(sorCurrentMarketRate!=null){
			BigDecimal marketRate=BigDecimal.valueOf(sorCurrentMarketRate.getValue());
			marketRate=marketRate.setScale(2, BigDecimal.ROUND_UP);
			sorCurrentMarketRate=new Money(marketRate.doubleValue());
		}
		return sorCurrentMarketRate;
	}
	
	public double getConversionFactorForRE(Date asOnDate) {		
		if(revisionType != null && RevisionType.LUMP_SUM_ITEM.equals(revisionType) && schedule==null){
			return Double.valueOf(1);
		}
		if(revisionType != null && (RevisionType.ADDITIONAL_QUANTITY.equals(revisionType) || RevisionType.REDUCED_QUANTITY.equals(revisionType)) && schedule==null){
			return Double.valueOf(1);
		}
		else if(revisionType != null && (RevisionType.NON_TENDERED_ITEM.equals(revisionType) || 
				RevisionType.ADDITIONAL_QUANTITY.equals(revisionType) || RevisionType.REDUCED_QUANTITY.equals(revisionType)) && schedule!=null){
			double masterRate = getSORRateForDate(asOnDate)==null?Double.valueOf(0):getSORRateForDate(asOnDate).getValue();
			double unitRate = rate==null?Double.valueOf(0):rate.getValue();
			if(unitRate>0 && masterRate>0)
				return unitRate/masterRate;
			else
				return Double.valueOf(1);
		}
		else
			return Double.valueOf(1);
	}
	
	public Money getSORRateForDate(Date asOnDate) {
		Money sorCurrentRate=schedule.getRateOn(asOnDate).getRate();
		if(sorCurrentRate!=null){
			BigDecimal currentRate=BigDecimal.valueOf(sorCurrentRate.getValue());
			currentRate=currentRate.setScale(2, BigDecimal.ROUND_UP);
			sorCurrentRate=new Money(currentRate.doubleValue());
		}
		return sorCurrentRate;
	}
	
	public double getConversionFactor() {
		if(schedule==null){
			return Double.valueOf(1);
		}
		else{
			double masterRate = getSORCurrentRate()==null?Double.valueOf(0):getSORCurrentRate().getValue();
			double unitRate = rate==null?Double.valueOf(0):rate.getValue();
			if(unitRate>0 && masterRate>0)
				return unitRate/masterRate;
			else
				return Double.valueOf(1);
		}
	}
	
	public List<ValidationError> validate() {
		List<ValidationError> validationErrors = new ArrayList<ValidationError>();
		if (rate.getValue() <= 0.0) {
			validationErrors.add(new ValidationError("activity.rate.not.null", "activity.rate.not.null"));
		}
		if(nonSor!=null && (nonSor.getUom()==null||nonSor.getUom().getId()==null ||nonSor.getUom().getId()==0)){
			validationErrors.add(new ValidationError("activity.nonsor.invalid", "activity.nonsor.invalid"));
		}
		return validationErrors;
	}
	
	public EgUom getUom() {
		return uom;
	}
	public void setUom(EgUom uom) {
		this.uom = uom;
	}
	public double getAmt() {
		return amt;
	}
	public void setAmt(double amt) {
		this.amt = amt;
	}
	 	
	
	public Integer getSrlNo() {
		return srlNo;
	}
	public void setSrlNo(Integer srlNo) {
		this.srlNo = srlNo;
	}
	public RevisionType getRevisionType() {
		return revisionType;
	}
	public void setRevisionType(RevisionType revisionType) {
		this.revisionType = revisionType;
	}
	public Activity getParent() {
		return parent;
	}
	public void setParent(Activity parent) {
		this.parent = parent;
	}
	public String getSignValue() {
		return signValue;
	}
	public void setSignValue(String signValue) {
		this.signValue = signValue;
	}
	
}
