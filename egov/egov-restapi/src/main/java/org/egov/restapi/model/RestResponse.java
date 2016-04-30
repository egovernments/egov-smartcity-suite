package org.egov.restapi.model;

import org.egov.collection.integration.models.RestReceiptInfo;

import java.util.ArrayList;
import java.util.List;

public class RestResponse extends RestReceiptInfo  {
	
	private String status;
	private List<RestErrors> errorDetails=new ArrayList<RestErrors>();
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<RestErrors> getErrorDetails() {
		return errorDetails;
	}
	public void setErrorDetails(List<RestErrors> errorDetails) {
		this.errorDetails = errorDetails;
	}
	


}
