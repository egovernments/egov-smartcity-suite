package org.egov.tradelicense.domain.entity.objection;

import java.util.Date;
import java.util.List;

import org.egov.infstr.models.StateAware;
import org.egov.tradelicense.domain.entity.License;

public class LicenseObjection extends StateAware {
	private static final long serialVersionUID = 1L;
	public static final String BY_ID = "LISENSEOBJECTION_BY_ID"; 
	private License license;
	private String number;
	private Integer reason;
	private String name; // Name of the Objectioner
	private String address;// address of the Objectioner
	private String details;
	private Date objectionDate;
	private String docNumber;
	private List<Activity> activities;
	private List<Notice> notices;

	
	public List<Notice> getNotices() {
		return notices;
	}

	public void setNotices(List<Notice> notices) {
		this.notices = notices;
	}

	public License getLicense() {
		return this.license;
	}

	public void setLicense(License license) {
		this.license = license;
	}

	public String getNumber() {
		return this.number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDetails() {
		return this.details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public Date getObjectionDate() {
		return this.objectionDate;
	}

	public void setObjectionDate(Date objectionDate) {
		this.objectionDate = objectionDate;
	}

	public String getDocNumber() {
		return this.docNumber;
	}

	public void setDocNumber(String docNumber) {
		this.docNumber = docNumber;
	}

	@Override
	public String getStateDetails() {
		return this.getState().getText1();
	}

	public Integer getReason() {
		return this.reason;
	}

	public void setReason(Integer reason) {
		this.reason = reason;
	}

	public List<Activity> getActivities() {
		return this.activities;
	}

	public void setActivities(List<Activity> activities) {
		this.activities = activities;
	}

	public String generateNumber(String runningNumber) {
		this.number = "OBJ" + runningNumber;
		return this.number;
	}
	
	public String myLinkId() {
		return this.getCurrentState().getText2()+"/web/objection/objection!showForApproval.action?model.id="+this.getId();
	}
	@Override
	public String toString() {
		 final StringBuilder str = new StringBuilder();
		str.append("LicenseObjection={");
		str.append("number=").append(number == null ? "null" : number.toString());
		str.append("name=").append(name == null ? "null" : name.toString());
		str.append("address=").append(address == null ? "null" : address.toString());
		str.append("details=").append(details == null ? "null" : details.toString());
		str.append("objectionDate=").append(objectionDate == null ? "null" : objectionDate.toString());
		str.append("docNumber=").append(docNumber == null ? "null" : docNumber.toString());
		str.append("}");
		return str.toString();
	}
}
