package org.egov.eb.domain.transaction.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.egov.infstr.models.BaseModel;

public class EbSchedulerLog extends BaseModel {
	public static final String STATUS_COMPLETED="Completed";
	public static final String STATUS_SCHEDULED="Scheduled";
	public static final String STATUS_RUNNING="Running";
	public static final String STATUS_FAILED="Failed";
	public static final String STATUS_CANCELLED="Cancelled";
	
	private Date startTime;
	private Date endTime;
	private Long noOfPendingBills;
	private Long noOfBillsProcessed;
	private Long noOfBillsFailed;
	private Long noOfBillsSuccess;
	private Long noOfBillsCreated;
	private String schedulerStatus;
	private String oddOrEvenBilling;
	
	
	
	private Set<EbSchedulerLogDetails> logDetails=new HashSet<EbSchedulerLogDetails>(0);
	
	public Long getNoOfPendingBills() {
		return noOfPendingBills;
	}
	public void setNoOfPendingBills(Long noOfPendingBills) {
		this.noOfPendingBills = noOfPendingBills;
	}
	public String getSchedulerStatus() {
		return schedulerStatus;
	}
	public void setSchedulerStatus(String schedulerStatus) {
		this.schedulerStatus = schedulerStatus;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	public Long getNoOfBillsProcessed() {
		return noOfBillsProcessed;
	}
	public void setNoOfBillsProcessed(Long noOfBillsProcessed) {
		this.noOfBillsProcessed = noOfBillsProcessed;
	}
	public Long getNoOfBillsCreated() {
		return noOfBillsCreated;
	}
	public void setNoOfBillsCreated(Long noOfBillsCreated) {
		this.noOfBillsCreated = noOfBillsCreated;
	}
	@Override
	public String toString() {
		return "EbSchedulerLog [ noOfPendingBills=" + noOfPendingBills
				+ ", noOfBillsProcessed=" + noOfBillsProcessed
				+ ", noOfBillsCreated=" + noOfBillsCreated + ", schedulerStatus=" + schedulerStatus
				+ ", startTime=" + startTime + ", endTime=" + endTime
				+ ", logDetails=" + logDetails + "]";
	}
	public Set<EbSchedulerLogDetails> getLogDetails() {
		return logDetails;
	}
	public void setLogDetails(Set<EbSchedulerLogDetails> logDetails) {
		this.logDetails = logDetails;
	}
	
	public Long getNoOfBillsFailed() {
			return noOfBillsFailed;
		}
	public String getOddOrEvenBilling() {
		return oddOrEvenBilling;
	}
	public void setOddOrEvenBilling(String oddOrEvenBilling) {
		this.oddOrEvenBilling = oddOrEvenBilling;
	}
	public void setNoOfBillsFailed(Long noOfBillsFailed) {
		this.noOfBillsFailed = noOfBillsFailed;
	}
	public Long getNoOfBillsSuccess() {
		if(noOfBillsProcessed!=null & noOfBillsFailed!=null)
			return noOfBillsProcessed-noOfBillsFailed;
		else
			return null;
			
		
	}
	public void setNoOfBillsSuccess(Long noOfBillsSuccess) {
		this.noOfBillsSuccess = noOfBillsSuccess;
	}
	

}
