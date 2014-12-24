package org.egov.works.models.qualityControl;

import org.egov.infstr.models.BaseModel;
import org.egov.infstr.models.validator.Required;
import org.hibernate.validator.constraints.Length;

public class TestResultDetails extends BaseModel {

	private static final long serialVersionUID = 1L;

	private TestResultHeader testResultHeader;
	
	@Required(message = "test.result.jobdetails.null")
	private JobDetails jobDetails;

	@Length(max=1024,message="test.result.remarks.length")
	private String remarks;
	private Long documentNumber;
	
	public TestResultHeader getTestResultHeader() {
		return testResultHeader;
	}
	public void setTestResultHeader(TestResultHeader testResultHeader) {
		this.testResultHeader = testResultHeader;
	}
	public JobDetails getJobDetails() {
		return jobDetails;
	}
	public void setJobDetails(JobDetails jobDetails) {
		this.jobDetails = jobDetails;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public Long getDocumentNumber() {
		return documentNumber;
	}
	public void setDocumentNumber(Long documentNumber) {
		this.documentNumber = documentNumber;
	}
}
