package org.egov.works.models.qualityControl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.egov.commons.EgwStatus;
import org.egov.infstr.models.StateAware;
import org.egov.infstr.models.validator.Required;

public class TestResultHeader extends StateAware {
	
	private static final long serialVersionUID = 1L;

	@Required(message = "test.result.job.header.null")
	private JobHeader jobHeader;
	private EgwStatus egwStatus;
	private List<TestResultDetails> testResultDetails = new LinkedList<TestResultDetails>();
	private List<TestResultMis> testResultMis = new LinkedList<TestResultMis>();
	private transient String additionalWfRule;
	private transient BigDecimal amountWfRule;
	
	@Required(message = "test.result.date.null")
	private Date testResultDate;
	private List<String> testResultActions = new ArrayList<String>();

	@Override
	public String getStateDetails() {
		if(jobHeader!=null && jobHeader.getJobNumber()!=null)
			return "Test Result for Job Number: "+jobHeader.getJobNumber() ;
		else
			return "Test Result: ";
	}

	public JobHeader getJobHeader() {
		return jobHeader;
	}

	public void setJobHeader(JobHeader jobHeader) {
		this.jobHeader = jobHeader;
	}

	
	public EgwStatus getEgwStatus() {
		return egwStatus;
	}


	public void setEgwStatus(EgwStatus egwStatus) {
		this.egwStatus = egwStatus;
	}


	public String getAdditionalWfRule() {
		return additionalWfRule;
	}


	public void setAdditionalWfRule(String additionalWfRule) {
		this.additionalWfRule = additionalWfRule;
	}


	public BigDecimal getAmountWfRule() {
		return amountWfRule;
	}


	public void setAmountWfRule(BigDecimal amountWfRule) {
		this.amountWfRule = amountWfRule;
	}

	public List<TestResultDetails> getTestResultDetails() {
		return testResultDetails;
	}

	public void setTestResultDetails(List<TestResultDetails> testResultDetails) {
		this.testResultDetails = testResultDetails;
	}

	public List<TestResultMis> getTestResultMis() {
		return testResultMis;
	}

	public void setTestResultMis(List<TestResultMis> testResultMis) {
		this.testResultMis = testResultMis;
	}

	public List<String> getTestResultActions() {
		return testResultActions;
	}

	public void setTestResultActions(List<String> testResultActions) {
		this.testResultActions = testResultActions;
	}

	public Date getTestResultDate() {
		return testResultDate;
	}

	public void setTestResultDate(Date testResultDate) {
		this.testResultDate = testResultDate;
	}

}

	