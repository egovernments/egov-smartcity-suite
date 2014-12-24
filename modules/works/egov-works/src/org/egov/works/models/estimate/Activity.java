package org.egov.works.models.estimate;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.egov.infstr.ValidationError;
import org.egov.infstr.commonMasters.EgUom;
import org.egov.infstr.models.BaseModel;
import org.egov.infstr.models.Money;
import org.egov.infstr.models.validator.GreaterThan;
import org.egov.infstr.models.validator.Required;
import org.egov.infstr.utils.StringUtils;
import org.egov.tender.TenderableType;
import org.egov.tender.interfaces.Tenderable;
import org.egov.works.models.masters.ScheduleOfRate;
import org.egov.works.models.revisionEstimate.RevisionType;
import javax.validation.constraints.Min;
import javax.validation.Valid;

public class Activity extends BaseModel implements Tenderable {

	private AbstractEstimate abstractEstimate;
	
	private ScheduleOfRate schedule; 
	
	@Valid 
	private NonSor nonSor;
	
	private EgUom uom;

	@Required(message="activity.rate.not.null")
	private Money rate=new Money(0.0);
	
	@Required(message="activity.quantity.not.null")  
	@GreaterThan(value=-1,message="activity.quantity.non.negative")
	private double quantity;
	
	@Min(value=0,message="activity.servicetax.non.negative")
	private double serviceTaxPerc;
	
	private Activity parent;
	private RevisionType revisionType;
		
	private double amt;
	
	private long rcId;
		
	private Integer srlNo;
	private List<MeasurementSheet> measurementSheetList = new LinkedList<MeasurementSheet>();
	private String activityRecordId;
	
	//for Revision Estimate
	private double mbQuantity;
	private String signValue;
	
	private double partRate;
	private double reducedRate;
	
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
		return  quantity;
	}
	
	public String getQuantityString() {
		return numberFormat(Double.toString(quantity)).toString();
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
		getTotalAmount(amt);
		return new Money(amt);
	}
	
	public Money getTotalAmount(double amt){
		double amount=0.0D;
		amount = amount+amt;
		return new Money(amount);
	}
	public Money getTaxAmount() {
		return new Money(rate.getValue() * quantity * serviceTaxPerc /100.0);
	}
	public Money getAmountIncludingTax() {
		return new Money(getAmount().getValue() + getTaxAmount().getValue());
	}
	
	public Money getSORCurrentRate() {
		Money sorCurrentRate=new Money(0.0);
		if(!abstractEstimate.getEstimateRateContractList().isEmpty()){
             sorCurrentRate=rate;
		}
		else if(schedule!=null) {
			
			Date estDate=abstractEstimate.getEstimateDate();
			if(abstractEstimate.getParent()!=null && revisionType!=null && revisionType.ADDITITONAL_QUANTITY.equals(RevisionType.ADDITITONAL_QUANTITY) && revisionType.REDUCED_QUANTITY.equals(RevisionType.REDUCED_QUANTITY))
				estDate=abstractEstimate.getParent().getEstimateDate();
			
			sorCurrentRate=schedule.getRateOn(estDate).getRate(); 
			if(sorCurrentRate!=null){
				BigDecimal currentRate=BigDecimal.valueOf(sorCurrentRate.getValue());
				currentRate=currentRate.setScale(2, BigDecimal.ROUND_UP);
				sorCurrentRate=new Money(currentRate.doubleValue());
			}
		}
		
		return sorCurrentRate;
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
	
	public double getConversionFactor() {
		if(schedule==null){
			return Double.valueOf(1);
		}
		else if(!abstractEstimate.getEstimateRateContractList().isEmpty()){
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
	
	public static StringBuffer numberFormat(final String strNumberToConvert) {
        String strNumber="",signBit="";
        if(strNumberToConvert.startsWith("-")) {
            strNumber=""+strNumberToConvert.substring(1,strNumberToConvert.length());
            signBit="-";
        }
        else strNumber=""+strNumberToConvert;
        DecimalFormat dft = new DecimalFormat("##############0.#####");
        String strtemp=""+dft.format(Double.parseDouble(strNumber));
        StringBuffer strbNumber=new StringBuffer(strtemp);
       if(signBit.equals("-"))strbNumber=strbNumber.insert(0,"-");
        return strbNumber;
    }
	
	public List<ValidationError> validate() {
		List<ValidationError> validationErrors = new ArrayList<ValidationError>();
		if (schedule!=null && (rate==null || rate.getValue()==0.0)) {
			validationErrors.add(new ValidationError("activity.sor.rate.not.null", "activity.sor.rate.not.null"));
		}
		if (rate.getValue() < 0.0) {
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
	public List<MeasurementSheet> getMeasurementSheetList() {
		return measurementSheetList;
	}
	public void setMeasurementSheetList(List<MeasurementSheet> measurementSheetList) {
		this.measurementSheetList = measurementSheetList;
	}
	public void addMeasurementSheet(MeasurementSheet measurementSheet) {
		this.measurementSheetList.add(measurementSheet);
	}
	public String getActivityRecordId() {
		return activityRecordId;
	}
	public void setActivityRecordId(String activityRecordId) {
		this.activityRecordId = activityRecordId;
	}
	
	@Override
	public TenderableType getTenderableType(){
		if(schedule!=null)
			return TenderableType.WORKS_ACTIVITY_SOR;
		else
			return TenderableType.WORKS_ACTIVITY_NONSOR;
	}
	
	@Override
	public String getNumber(){
		if(schedule!=null)
			return schedule.getCategory().getCode()+"^"+schedule.getCode();
		else
			return nonSor.getId().toString();
	}
	
	@Override
	public String getName(){
		if(schedule!=null)
			return schedule.getDescription();
		else
			return nonSor.getDescription();
	}
	
	@Override
	public String getDescription(){
		if(schedule!=null)
			return schedule.getDescription();
		else
			return nonSor.getDescription();
	}
	
	@Override
	public BigDecimal getRequestedQty(){
		return new BigDecimal(this.quantity).multiply(this.uom.getConvFactor());
	}
	
	@Override
	public BigDecimal getRequestedValue(){
		if(schedule!=null){
			return new BigDecimal(getSORCurrentRate().getValue()/this.uom.getConvFactor().doubleValue());
		}
		else{
			return new BigDecimal(rate.getValue()/this.uom.getConvFactor().doubleValue());
		}
	}
	
	@Override
	public Date getRequestedByDate(){
		return null;
	}
	
	@Override
	public EgUom getRequestedUOM(){
		return this.uom;
	}
	public Activity getParent() {
		return parent;
	}
	public void setParent(Activity parent) {
		this.parent = parent;
	}
	public RevisionType getRevisionType() {
		return revisionType;
	}
	public void setRevisionType(RevisionType revisionType) {
		this.revisionType = revisionType;
	}
	public long getRcId() {
		return rcId;
	}
	public void setRcId(long rcId) {
		this.rcId = rcId;
	}
	public double getMbQuantity() {
		return mbQuantity;
	}
	public void setMbQuantity(double mbQuantity) {
		this.mbQuantity = mbQuantity;
	}
	public String getSignValue() {
		return signValue;
	}
	public void setSignValue(String signValue) {
		this.signValue = signValue;
	}
	public double getPartRate() {
		return partRate;
	}
	public void setPartRate(double partRate) {
		this.partRate = partRate;
	}
	public double getReducedRate() {
		return reducedRate;
	}
	public void setReducedRate(double reducedRate) {
		this.reducedRate = reducedRate;
	}
	
    public String getDescriptionJS() {
        if (schedule != null)
            return StringUtils.escapeJavaScript(schedule.getDescription());
        else
            return StringUtils.escapeJavaScript(nonSor.getDescription());
}
    
}
