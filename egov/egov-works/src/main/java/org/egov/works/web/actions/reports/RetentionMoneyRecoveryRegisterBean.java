package org.egov.works.web.actions.reports;

import java.math.BigDecimal;

public class RetentionMoneyRecoveryRegisterBean {
	
	private String billDepartment;
	
	private String contractorCode;
	
	private String contractorName;
	
	private String projectCode;
	
	private String projectName;
	
	private String billNumber;
	
	private String billType;
	
	private String billDate;
	
	private String voucherNumber;
	
	private BigDecimal billAmount;
	
	private BigDecimal retentionMoneyRecoveredAmount;
	
	private String refundDate;

	
	public String getRefundDate() {
		return refundDate;
	}

	public void setRefundDate(String refundDate) {
		this.refundDate = refundDate;
	}

	public String getBillDepartment() {
		return billDepartment;
	}

	public void setBillDepartment(String billDepartment) {
		this.billDepartment = billDepartment;
	}

	public String getContractorCode() {
		return contractorCode;
	}

	public void setContractorCode(String contractorCode) {
		this.contractorCode = contractorCode;
	}

	public String getContractorName() {
		return contractorName;
	}

	public void setContractorName(String contractorName) {
		this.contractorName = contractorName;
	}

	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}

	public String getBillType() {
		return billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}

	public String getBillDate() {
		return billDate;
	}

	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}

	public String getVoucherNumber() {
		return voucherNumber;
	}

	public void setVoucherNumber(String voucherNumber) {
		this.voucherNumber = voucherNumber;
	}

	public BigDecimal getBillAmount() {
		return billAmount;
	}

	public void setBillAmount(BigDecimal billAmount) {
		this.billAmount = billAmount;
	}

	public BigDecimal getRetentionMoneyRecoveredAmount() {
		return retentionMoneyRecoveredAmount;
	}

	public void setRetentionMoneyRecoveredAmount(
			BigDecimal retentionMoneyRecoveredAmount) {
		this.retentionMoneyRecoveredAmount = retentionMoneyRecoveredAmount;
	}	
}