package org.egov.works.models.estimate;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.egov.infstr.ValidationError;
import org.egov.infstr.commonMasters.EgUom;
import org.egov.infstr.models.BaseModel;
import org.egov.infstr.models.Money;
import org.egov.works.models.masters.ScheduleOfRate;

public class EstimateTemplateActivity  extends BaseModel{
	
	private EstimateTemplate estimateTemplate;
	
	private ScheduleOfRate schedule;
	
	@Valid 
	private NonSor nonSor;
	
	private EgUom uom;
	
	private Money rate=new Money(0.0);

	public EstimateTemplate getEstimateTemplate() {
		return estimateTemplate;
	}

	public void setEstimateTemplate(EstimateTemplate estimateTemplate) {
		this.estimateTemplate = estimateTemplate;
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

	public EgUom getUom() {
		return uom;
	}

	public void setUom(EgUom uom) {
		this.uom = uom;
	}

	public Money getRate() {
		return rate;
	}

	public void setRate(Money rate) {
		this.rate = rate;
	}
	
	public List<ValidationError> validate() {
		List<ValidationError> validationErrors = new ArrayList<ValidationError>();
		/*if (rate.getValue() <= 0.0) {
			validationErrors.add(new ValidationError("estimateTemplateActivity.rate.not.null", "estimateTemplateActivity.rate.not.null"));
		}*/
		if(nonSor!=null && (nonSor.getUom()==null||nonSor.getUom().getId()==null ||nonSor.getUom().getId()==0)){
			validationErrors.add(new ValidationError("estimateTemplateActivity.nonsor.invalid", "estimateTemplateActivity.nonsor.invalid"));
		}
		return validationErrors;
	}

}
