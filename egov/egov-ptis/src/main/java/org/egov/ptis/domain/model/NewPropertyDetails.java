package org.egov.ptis.domain.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class NewPropertyDetails implements Serializable {

	private String applicationNo;
	private ErrorDetails errorDetails;

	public String getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}

	public ErrorDetails getErrorDetails() {
		return errorDetails;
	}

	public void setErrorDetails(ErrorDetails errorDetails) {
		this.errorDetails = errorDetails;
	}

	@Override
	public String toString() {
		return "NewPropertyDetails [applicationNo=" + applicationNo + ", errorDetails=" + errorDetails + "]";
	}
}
