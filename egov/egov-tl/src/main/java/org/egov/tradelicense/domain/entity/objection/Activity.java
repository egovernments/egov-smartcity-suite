package org.egov.tradelicense.domain.entity.objection;

import java.util.Date;

import org.egov.infstr.models.BaseModel;

public class Activity extends BaseModel {
	private static final long serialVersionUID = -6369641818759075953L;
	private	LicenseObjection objection;
	private	String details;
	private Date activityDate;
	private	String remarks;
	private	Date expectedDateOfResponse;
	// for uploaded document references
	private	String docNumber;
	private String type;

	public LicenseObjection getObjection() {
		return objection;
	}

	public void setObjection(LicenseObjection objection) {
		this.objection = objection;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public Date getActivityDate() {
		return activityDate;
	}

	public void setActivityDate(Date activityDate) {
		this.activityDate = activityDate;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Date getExpectedDateOfResponse() {
		return expectedDateOfResponse;
	}

	public void setExpectedDateOfResponse(Date expectedDateOfResponse) {
		this.expectedDateOfResponse = expectedDateOfResponse;
	}

	public String getDocNumber() {
		return docNumber;
	}

	public void setDocNumber(String docNumber) {
		this.docNumber = docNumber;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		final StringBuilder str = new StringBuilder();
		str.append("Activity={");
		str.append("objection=").append(objection == null ? "null" : objection.toString());
		str.append("details=").append(details == null ? "null" : details.toString());
		str.append("activityDate=").append(activityDate == null ? "null" : activityDate.toString());
		str.append("remarks=").append(remarks == null ? "null" : remarks.toString());
		str.append("expectedDateOfResponse=").append(expectedDateOfResponse == null ? "null" : expectedDateOfResponse.toString());
		str.append("docNumber=").append(docNumber == null ? "null" : docNumber.toString());
		str.append("type=").append(type == null ? "null" : type.toString());
		str.append("}");
		return str.toString();
	}
	
}
