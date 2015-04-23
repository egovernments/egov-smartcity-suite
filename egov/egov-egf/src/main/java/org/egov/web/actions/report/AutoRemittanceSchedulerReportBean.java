package org.egov.web.actions.report;

import java.util.Date;

public class AutoRemittanceSchedulerReportBean {
	 
	 private String    recoveryCoa;
	 private String    scheduleType ;
	 private Date    runDate;
	 private String    status;
	 private String    remarks;
	 private String    numberOfPayments;
	 private Date    nextRunDate;
	 
	public String getRecoveryCoa() {
		return recoveryCoa;
	}
	public void setRecoveryCoa(String recoveryCoa) {
		this.recoveryCoa = recoveryCoa;
	}
	public String getScheduleType() {
		return scheduleType;
	}
	public void setScheduleType(String scheduleType) {
		this.scheduleType = scheduleType;
	}
	public Date getRunDate() {
		return runDate;
	}
	public void setRunDate(Date runDate) {
		this.runDate = runDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getNumberOfPayments() {
		return numberOfPayments;
	}
	public void setNumberOfPayments(String numberOfPayments) {
		this.numberOfPayments = numberOfPayments;
	}
	public Date getNextRunDate() {
		return nextRunDate;
	}
	public void setNextRunDate(Date nextRunDate) {
		this.nextRunDate = nextRunDate;
	}
}