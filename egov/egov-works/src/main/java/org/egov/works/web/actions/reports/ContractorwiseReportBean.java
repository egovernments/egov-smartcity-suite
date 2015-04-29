package org.egov.works.web.actions.reports;

import java.math.BigDecimal;


public class ContractorwiseReportBean  {
	
	private Long contractorId;
	private String contractorName;
	private String contractorCode;
	private String contractorClass;
	private Integer takenUpEstimateCount = 0;
	private BigDecimal takenUpWOAmount = BigDecimal.ZERO;
	private Integer completedEstimateCount = 0;
	private BigDecimal completedWOAmount = BigDecimal.ZERO;
	private Integer inProgressEstimateCount = 0;
	private BigDecimal inProgressTenderNegotiatedAmt = BigDecimal.ZERO;
	private BigDecimal inProgressPaymentReleasedAmt = BigDecimal.ZERO;
	private BigDecimal inProgressBalanceAmount = BigDecimal.ZERO;
	private Integer notYetStartedEstimateCount = 0;
	private BigDecimal notYetStartedWOAmount = BigDecimal.ZERO;
	private Integer balanceEstimateCount = 0;
	private BigDecimal balanceAmount = BigDecimal.ZERO;
	
	public Long getContractorId() {
		return contractorId;
	}
	public void setContractorId(Long contractorId) {
		this.contractorId = contractorId;
	}
	
	public String getContractorName() {
		return contractorName;
	}
	public Integer getTakenUpEstimateCount() {
		return takenUpEstimateCount;
	}
	public BigDecimal getTakenUpWOAmount() {
		return takenUpWOAmount;
	}
	public Integer getCompletedEstimateCount() {
		return completedEstimateCount;
	}
	public BigDecimal getCompletedWOAmount() {
		return completedWOAmount;
	}
	public Integer getInProgressEstimateCount() {
		return inProgressEstimateCount;
	}
	public BigDecimal getInProgressBalanceAmount() {
		return inProgressBalanceAmount;
	}
	public Integer getNotYetStartedEstimateCount() {
		return notYetStartedEstimateCount;
	}
	public BigDecimal getNotYetStartedWOAmount() {
		return notYetStartedWOAmount;
	}
	public Integer getBalanceEstimateCount() {
		return balanceEstimateCount;
	}
	public BigDecimal getBalanceAmount() {
		return balanceAmount;
	}
	public void setContractorName(String contractorName) {
		this.contractorName = contractorName;
	}
	public void setTakenUpEstimateCount(Integer takenUpEstimateCount) {
		this.takenUpEstimateCount = takenUpEstimateCount;
	}
	public void setTakenUpWOAmount(BigDecimal takenUpWOAmount) {
		this.takenUpWOAmount = takenUpWOAmount;
	}
	public void setCompletedEstimateCount(Integer completedEstimateCount) {
		this.completedEstimateCount = completedEstimateCount;
	}
	public void setCompletedWOAmount(BigDecimal completedWOAmount) {
		this.completedWOAmount = completedWOAmount;
	}
	public void setInProgressEstimateCount(Integer inProgressEstimateCount) {
		this.inProgressEstimateCount = inProgressEstimateCount;
	}
	public void setInProgressBalanceAmount(BigDecimal inProgressBalanceAmount) {
		this.inProgressBalanceAmount = inProgressBalanceAmount;
	}
	public void setNotYetStartedEstimateCount(Integer notYetStartedEstimateCount) {
		this.notYetStartedEstimateCount = notYetStartedEstimateCount;
	}
	public void setNotYetStartedWOAmount(BigDecimal notYetStartedWOAmount) {
		this.notYetStartedWOAmount = notYetStartedWOAmount;
	}
	public void setBalanceEstimateCount(Integer balanceEstimateCount) {
		this.balanceEstimateCount = balanceEstimateCount;
	}
	public void setBalanceAmount(BigDecimal balanceAmount) {
		this.balanceAmount = balanceAmount;
	}
	public BigDecimal getInProgressTenderNegotiatedAmt() {
		return inProgressTenderNegotiatedAmt;
	}
	public BigDecimal getInProgressPaymentReleasedAmt() {
		return inProgressPaymentReleasedAmt;
	}
	public void setInProgressTenderNegotiatedAmt(
			BigDecimal inProgressTenderNegotiatedAmt) {
		this.inProgressTenderNegotiatedAmt = inProgressTenderNegotiatedAmt;
	}
	public void setInProgressPaymentReleasedAmt(
			BigDecimal inProgressPaymentReleasedAmt) {
		this.inProgressPaymentReleasedAmt = inProgressPaymentReleasedAmt;
	}
	public String getContractorCode() {
		return contractorCode;
	}
	public String getContractorClass() {
		return contractorClass;
	}
	public void setContractorCode(String contractorCode) {
		this.contractorCode = contractorCode;
	}
	public void setContractorClass(String contractorClass) {
		this.contractorClass = contractorClass;
	}
	
}
