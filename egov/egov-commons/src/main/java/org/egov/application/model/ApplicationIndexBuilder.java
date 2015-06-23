package org.egov.application.model;

import org.joda.time.DateTime;

/**
 * Builder class for Application Index
 * 
 * @author rishi
 *
 */
public class ApplicationIndexBuilder {

	private ApplicationIndex applicationIndex;

	public ApplicationIndexBuilder(String applicationNumber, DateTime applicationDate, String applicationType, String applicantName, String status, String url) {
		applicationIndex =  new ApplicationIndex();
		applicationIndex.setApplicationNumber(applicationNumber);
		applicationIndex.setApplicationDate(applicationDate);
		applicationIndex.setApplicationType(applicationType);
		applicationIndex.setApplicantName(applicantName);
		applicationIndex.setStatus(status);
		applicationIndex.setUrl(url);
	}
	
	public ApplicationIndexBuilder applicationAddress(String applicantAddress) {
		applicationIndex.setApplicantAddress(applicantAddress);
		return this;
	}
	
	public ApplicationIndexBuilder disposalDate(DateTime disposalDate) {
		applicationIndex.setDisposalDate(disposalDate);
		return this;
	}
	
	public ApplicationIndexBuilder consumerCode(String consumerCode) {
		applicationIndex.setConsumerCode(consumerCode);
		return this;
	}
}
