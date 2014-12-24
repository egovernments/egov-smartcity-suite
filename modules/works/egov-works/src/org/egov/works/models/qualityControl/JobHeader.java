package org.egov.works.models.qualityControl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.egov.commons.EgwStatus;
import org.egov.commons.utils.EntityType;
import org.egov.infstr.models.StateAware;

public class JobHeader extends StateAware implements EntityType{
	
	private String jobNumber;
	private SampleLetterHeader sampleLetterHeader;
	private Date jobDate;
	private EgwStatus egwStatus;
	private List<JobDetails> jobDetails = new LinkedList<JobDetails>();
	private transient String additionalWfRule;
	private transient BigDecimal amountWfRule;
	private double jhTotalAmount;

	@Override
	public String getStateDetails() {
		// TODO Auto-generated method stub
		return "Job Number : " + getJobNumber();
	}


	@Override
	public String getBankname() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getBankaccount() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getPanno() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getTinno() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getIfsccode() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return this.jobNumber;
	}


	@Override
	public String getModeofpay() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getCode() {
		// TODO Auto-generated method stub
		return this.jobNumber;
	}


	@Override
	public Integer getEntityId() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getEntityDescription() {
		// TODO Auto-generated method stub
		return null;
	}


	public String getJobNumber() {
		return jobNumber;
	}


	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}


	public SampleLetterHeader getSampleLetterHeader() {
		return sampleLetterHeader;
	}


	public void setSampleLetterHeader(SampleLetterHeader sampleLetterHeader) {
		this.sampleLetterHeader = sampleLetterHeader;
	}


	public Date getJobDate() {
		return jobDate;
	}


	public void setJobDate(Date jobDate) {
		this.jobDate = jobDate;
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


	public List<JobDetails> getJobDetails() {
		return jobDetails;
	}


	public void setJobDetails(List<JobDetails> jobDetails) {
		this.jobDetails = jobDetails;
	}
	
	public void addJobDetails(JobDetails jobDetails){
		this.jobDetails.add(jobDetails);
	}
	
	public double getJhTotalAmount() {
		for(JobDetails jobDtls : jobDetails){
			jhTotalAmount=jhTotalAmount+(jobDtls.getReceivedQuantity()*jobDtls.getSampleLetterDetails().getTestSheetDetails().getTestCharges().getValue());
		}
		return jhTotalAmount; 
	}


}

	