package org.egov.restapi.model;

public class AssessmentNoRequest {

	private String assessmentNo;

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

}
