package org.egov.mrs.domain.entity;

public class MaritalStatusReport {
	
	private String month;
	private String applicantType;
	private String married;
	private String unmarried;
	private String divorced;
	private String widower;
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getApplicantType() {
		return applicantType;
	}
	public void setApplicantType(String applicantType) {
		this.applicantType = applicantType;
	}
	public String getMarried() {
		return married;
	}
	public void setMarried(String married) {
		this.married = married;
	}
	public String getUnmarried() {
		return unmarried;
	}
	public void setUnmarried(String unmarried) {
		this.unmarried = unmarried;
	}
	public String getDivorced() {
		return divorced;
	}
	public void setDivorced(String divorced) {
		this.divorced = divorced;
	}
	public String getWidower() {
		return widower;
	}
	public void setWidower(String widower) {
		this.widower = widower;
	}
	
}
