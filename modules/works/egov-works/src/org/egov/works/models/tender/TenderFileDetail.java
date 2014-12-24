package org.egov.works.models.tender;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.egov.infstr.ValidationError;
import org.egov.infstr.models.BaseModel;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.rateContract.Indent;
import javax.validation.Valid;


public class TenderFileDetail extends BaseModel{
		
	private TenderFile tenderFile;
	
	private Indent indent;
	
	private AbstractEstimate abstractEstimate;
		
	@Valid
	private List<TenderFileIndentDetail> tenderFileIndentDetails = new LinkedList<TenderFileIndentDetail>();
	
	public TenderFile getTenderFile() {
		return tenderFile;
	}

	public void setTenderFile(TenderFile tenderFile) {
		this.tenderFile = tenderFile;
	}

	public Indent getIndent() {
		return indent;
	}

	public void setIndent(Indent indent) {
		this.indent = indent;
	}

	public AbstractEstimate getAbstractEstimate() {
		return abstractEstimate;
	}

	public void setAbstractEstimate(AbstractEstimate abstractEstimate) {
		this.abstractEstimate = abstractEstimate;
	}	
	
	public List<TenderFileIndentDetail> getTenderFileIndentDetails() {
		return tenderFileIndentDetails;
	}

	public void setTenderFileIndentDetails(
			List<TenderFileIndentDetail> tenderFileIndentDetails) {
		this.tenderFileIndentDetails = tenderFileIndentDetails;
	}
	
	public void addTenderFileIndentDetails(TenderFileIndentDetail tenderFileIndentDetail) {
		this.tenderFileIndentDetails.add(tenderFileIndentDetail);
	}
	
	public List<ValidationError> validate() {
		List<ValidationError> validationErrors = new ArrayList<ValidationError>(); 
		return validationErrors;		
	}

}
