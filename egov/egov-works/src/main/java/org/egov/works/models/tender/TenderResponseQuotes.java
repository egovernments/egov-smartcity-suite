package org.egov.works.models.tender;

import javax.validation.Valid;

import org.egov.infra.persistence.validator.annotation.GreaterThan;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infstr.models.BaseModel;
import org.egov.works.models.masters.Contractor;

public class TenderResponseQuotes extends BaseModel{
	
	
	@Valid
	private TenderResponseActivity tenderResponseActivity;
	
	private Contractor contractor;
	
	@Required(message="tenderResponseQuotes.quotedRate.not.null")
	@GreaterThan(value=0,message="tenderResponseQuotes.quotedRate.non.negative")
	private double quotedRate;
	
	private double quotedQuantity;

	public TenderResponseActivity getTenderResponseActivity() {
		return tenderResponseActivity;
	}

	public void setTenderResponseActivity(TenderResponseActivity tenderResponseActivity) {
		this.tenderResponseActivity = tenderResponseActivity;
	}

	public Contractor getContractor() {
		return contractor;
	}

	public void setContractor(Contractor contractor) {
		this.contractor = contractor;
	}

	public double getQuotedRate() {
		return quotedRate;
	}

	public void setQuotedRate(double quotedRate) {
		this.quotedRate = quotedRate;
	}

	public double getQuotedQuantity() {
		return quotedQuantity;
	}

	public void setQuotedQuantity(double quotedQuantity) {
		this.quotedQuantity = quotedQuantity;
	}

}
