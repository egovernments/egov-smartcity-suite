package org.egov.works.models.rateContract;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.egov.commons.EgwStatus;
import org.egov.infstr.ValidationError;
import org.egov.infstr.models.StateAware;
import org.egov.infstr.models.validator.CheckDateFormat;
import org.egov.infstr.models.validator.Required;
import org.egov.works.models.masters.Contractor;
import javax.validation.Valid;

public class RateContract extends StateAware {

	private String rcNumber;
	
	@Required(message="rateContract.date.null")
	@CheckDateFormat(message="invalid.fieldvalue.rcDate") 
	private Date rcDate;
	
	private Indent indent;
	
	private Contractor contractor;
	
	private EgwStatus egwStatus;
	
	@Required(message = "rateContract.rcName.null")
	private String rcName;
	
	private String description;
	
	private String remarks;
	
	private String responseNumber;
	
	private Long documentNumber;
	
	private List<String> rcActions = new ArrayList<String>();

	@Valid
	private List<RateContractDetail> rateContractDetails = new LinkedList<RateContractDetail>();
	
	public String getRcNumber() {
		return rcNumber;
	}

	public void setRcNumber(String rcNumber) {
		this.rcNumber = rcNumber;
	}

	public Date getRcDate() {
		return rcDate;
	}

	public void setRcDate(Date rcDate) {
		this.rcDate = rcDate;
	}

	public Indent getIndent() {
		return indent;
	}

	public void setIndent(Indent indent) {
		this.indent = indent;
	}

	public Contractor getContractor() {
		return contractor;
	}

	public void setContractor(Contractor contractor) {
		this.contractor = contractor;
	}

	public EgwStatus getEgwStatus() {
		return egwStatus;
	}

	public void setEgwStatus(EgwStatus egwStatus) {
		this.egwStatus = egwStatus;
	}

	public List<RateContractDetail> getRateContractDetails() {
		return rateContractDetails;
	}

	public void setRateContractDetails(List<RateContractDetail> rateContractDetails) {
		this.rateContractDetails = rateContractDetails;
	}

	public String getRcName() {
		return rcName;
	}

	public void setRcName(String rcName) {
		this.rcName = rcName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	public String getResponseNumber() {
		return responseNumber;
	}

	public void setResponseNumber(String responseNumber) {
		this.responseNumber = responseNumber;
	}

	@Override
	public String getStateDetails() {
		return "Rate Contract : " + getRcNumber();
	}
	
	public void addRateContractDetails(RateContractDetail detail) {
		this.rateContractDetails.add(detail);
	}
	
	public List<ValidationError> validate()	{
		List<ValidationError> validationErrors = new ArrayList<ValidationError>();
		validationErrors.addAll(validateDetails());
		return validationErrors;
	}
		
	public List<ValidationError> validateDetails() {
		List<ValidationError> validationErrors = new ArrayList<ValidationError>();
		for(RateContractDetail detail: rateContractDetails) 
		{
			validationErrors.addAll(detail.validate());
		}
		return validationErrors;
		}

	public List<String> getRcActions() {
		return rcActions;
	}

	public void setRcActions(List<String> rcActions) {
		this.rcActions = rcActions;
	}
	
	public Collection<RateContractDetail> getSorRcDetails() {
		return CollectionUtils.select(rateContractDetails, new Predicate(){
			public boolean evaluate(Object rateContractDetails) {
					return ((RateContractDetail)rateContractDetails).getIndentDetail()!=null;
				}});
	}

	public Long getDocumentNumber() {
		return documentNumber;
	}

	public void setDocumentNumber(Long documentNumber) {
		this.documentNumber = documentNumber;
	}
}




