package org.egov.works.models.rateContract;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.infstr.ValidationError;
import org.egov.infstr.commonMasters.EgUom;
import org.egov.infstr.models.BaseModel;
import org.egov.tender.TenderableType;
import org.egov.tender.interfaces.Tenderable;
import org.egov.works.models.estimate.NonSor;
import org.egov.works.models.masters.ScheduleOfRate;


public class IndentDetail extends BaseModel implements Tenderable{
		
	private Indent indent;
	
	private ScheduleOfRate scheduleOfRate;
	
	private NonSor nonSor;
	
	public Indent getIndent() {
		return indent;
	}

	public void setIndent(Indent indent) {
		this.indent = indent;
	}

	public ScheduleOfRate getScheduleOfRate() {
		return scheduleOfRate;
	}

	public void setScheduleOfRate(ScheduleOfRate scheduleOfRate) {
		this.scheduleOfRate = scheduleOfRate;
	}

	public NonSor getNonSor() {
		return nonSor;
	}

	public void setNonSor(NonSor nonSor) {
		this.nonSor = nonSor;
	}
	
	public List<ValidationError> validate() {
		List<ValidationError> validationErrors = new ArrayList<ValidationError>(); 
		return validationErrors;		
	}
	
	@Override
	public TenderableType getTenderableType(){
		if(scheduleOfRate!=null)
			return TenderableType.WORKS_ACTIVITY_SOR;
		else
			return TenderableType.WORKS_ACTIVITY_NONSOR;
	}
	
	@Override
	public String getNumber(){
		if(scheduleOfRate!=null)
			return scheduleOfRate.getCategory().getCode()+"^"+scheduleOfRate.getCode();
		else
			return nonSor.getId().toString();
	}
	
	@Override
	public String getName(){
		if(scheduleOfRate!=null)
			return scheduleOfRate.getDescription();
		else
			return nonSor.getDescription();
	}
	
	@Override
	public String getDescription(){
		if(scheduleOfRate!=null)
			return scheduleOfRate.getDescription();
		else
			return nonSor.getDescription();
	}
	
	@Override
	public BigDecimal getRequestedQty(){
		return BigDecimal.ZERO;
	}
	
	@Override
	public BigDecimal getRequestedValue(){
		return BigDecimal.ZERO;
	}
	
	@Override
	public Date getRequestedByDate(){
		return null;
	}
	
	@Override
	public EgUom getRequestedUOM(){
		if(scheduleOfRate!=null)
			return scheduleOfRate.getUom();
		else
			return nonSor.getUom();
	}
	
}
