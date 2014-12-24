package org.egov.works.models.qualityControl;

import org.egov.infstr.models.BaseModel;

public class JobDetails extends BaseModel {
	private JobHeader jobHeader;
	private SampleLetterDetails sampleLetterDetails;
	private double receivedQuantity;
	
	public JobHeader getJobHeader() {
		return jobHeader;
	}
	public void setJobHeader(JobHeader jobHeader) {
		this.jobHeader = jobHeader;
	}
	public SampleLetterDetails getSampleLetterDetails() {
		return sampleLetterDetails;
	}
	public void setSampleLetterDetails(SampleLetterDetails sampleLetterDetails) {
		this.sampleLetterDetails = sampleLetterDetails;
	}
	public double getReceivedQuantity() {
		return receivedQuantity;
	}
	public void setReceivedQuantity(double receivedQuantity) {
		this.receivedQuantity = receivedQuantity;
	}
	
}
