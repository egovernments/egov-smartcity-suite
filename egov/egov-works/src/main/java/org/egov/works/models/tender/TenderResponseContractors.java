package org.egov.works.models.tender;

import javax.validation.Valid;

import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infstr.models.BaseModel;
import org.egov.works.models.masters.Contractor;

public class TenderResponseContractors extends BaseModel{
	
	
	@Valid
	private TenderResponse tenderResponse;
	
	@Required(message="tenderResponseContractors.contractor.not.null")
	private Contractor contractor;
	
	private String status;

	private String statusCode;

	public TenderResponse getTenderResponse() {
		return tenderResponse;
	}

	public void setTenderResponse(TenderResponse tenderResponse) {
		this.tenderResponse = tenderResponse;
	}

	public Contractor getContractor() {
		return contractor;
	}

	public void setContractor(Contractor contractor) {
		this.contractor = contractor;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) { 
		this.status = status;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	
}
