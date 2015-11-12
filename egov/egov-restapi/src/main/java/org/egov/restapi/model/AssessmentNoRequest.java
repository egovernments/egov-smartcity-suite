package org.egov.restapi.model;

public class AssessmentNoRequest {

	private String assessmentNo;
	private String ulbCode;

	public String getAssessmentNo() {
		return assessmentNo;
	}

	public void setAssessmentNo(String assessmentNo) {
		this.assessmentNo = assessmentNo;
	}

	@Override
	public String toString() {
		return "AssessmentNoRequest [assessmentNo=" + assessmentNo + "]";
	}

	public String getUlbCode() {
		return ulbCode;
	}

	public void setUlbCode(String ulbCode) {
		this.ulbCode = ulbCode;
	}

}
