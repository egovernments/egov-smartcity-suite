package org.egov.works.models.qualityControl;

import org.egov.infstr.models.BaseModel;

public class SampleLetterDetails extends BaseModel {
	private SampleLetterHeader sampleLetterHeader;
	private TestSheetDetails testSheetDetails;
	private double sampleQuantity;
	
	public SampleLetterHeader getSampleLetterHeader() {
		return sampleLetterHeader;
	}
	public void setSampleLetterHeader(SampleLetterHeader sampleLetterHeader) {
		this.sampleLetterHeader = sampleLetterHeader;
	}
	public TestSheetDetails getTestSheetDetails() {
		return testSheetDetails;
	}
	public void setTestSheetDetails(TestSheetDetails testSheetDetails) {
		this.testSheetDetails = testSheetDetails;
	}
	public double getSampleQuantity() {
		return sampleQuantity;
	}
	public void setSampleQuantity(double sampleQuantity) {
		this.sampleQuantity = sampleQuantity;
	}
	
}
