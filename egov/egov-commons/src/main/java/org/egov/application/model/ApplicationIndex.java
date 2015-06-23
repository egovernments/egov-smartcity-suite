package org.egov.application.model;

import org.egov.infra.search.elastic.Indexable;
import org.egov.search.domain.Searchable;
import org.joda.time.DateTime;

public class ApplicationIndex implements Indexable {

	private String indexId;

	@Searchable(name = "applicationnumber")
	private String applicationNumber;

	@Searchable(name = "applicationdate")
	private DateTime applicationDate;

	@Searchable(name = "applicationtype")
	private String applicationType;

	@Searchable(name = "applicantname")
	private String applicantName;

	private String applicantAddress;

	private DateTime disposalDate;

	@Searchable(name = "uldcode")
	private String ulbCode;

	@Searchable(name = "districtcode")
	private String districtCode;

	@Searchable(name = "status")
	private String status;

	private String url;
	
	@Searchable(name = "consumercode")
	private String consumerCode;

	public String getIndexId() {
		return indexId;
	}

	public void setIndexId(String indexId) {
		this.indexId = indexId;
	}

	public String getApplicationNumber() {
		return applicationNumber;
	}

	public void setApplicationNumber(String applicationNumber) {
		this.applicationNumber = applicationNumber;
	}

	public DateTime getApplicationDate() {
		return applicationDate;
	}

	public void setApplicationDate(DateTime applicationDate) {
		this.applicationDate = applicationDate;
	}

	public String getApplicationType() {
		return applicationType;
	}

	public void setApplicationType(String applicationType) {
		this.applicationType = applicationType;
	}

	public String getApplicantName() {
		return applicantName;
	}

	public void setApplicantName(String applicantName) {
		this.applicantName = applicantName;
	}

	public String getApplicantAddress() {
		return applicantAddress;
	}

	public void setApplicantAddress(String applicantAddress) {
		this.applicantAddress = applicantAddress;
	}

	public DateTime getDisposalDate() {
		return disposalDate;
	}

	public void setDisposalDate(DateTime disposalDate) {
		this.disposalDate = disposalDate;
	}

	public String getUlbCode() {
		return ulbCode;
	}

	public void setUlbCode(String ulbCode) {
		this.ulbCode = ulbCode;
	}

	public String getDistrictCode() {
		return districtCode;
	}

	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getConsumerCode() {
		return consumerCode;
	}

	public void setConsumerCode(String consumerCode) {
		this.consumerCode = consumerCode;
	}

}
