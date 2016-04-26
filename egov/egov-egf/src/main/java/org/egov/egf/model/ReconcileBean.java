package org.egov.egf.model;

import java.math.BigDecimal;
import java.util.Date;


public class ReconcileBean {

	private Long branchId;
	private Long bankId;
	private Long accountId;
	private Date fromDate;
	private Date toDate;
	private String chequeNumber;
	private String chequeDate;
	private BigDecimal chequeAmount;
	private String type;
	private String txnType;
	private Date reconciliationDate;
	private Date bankStatementDate;
	private Long ihId;
	private String voucherNumber;
	private Integer limit;
	private String instrumentNo;
	
	private BigDecimal creditAmount;
	private BigDecimal debitAmount;
	private BigDecimal otherCreditAmount;
	private BigDecimal otherDebitAmount;
	
	
	public Long getBranchId() {
		return branchId;
	}
	public void setBranchId(Long branchId) {
		this.branchId = branchId;
	}
	public Long getBankId() {
		return bankId;
	}
	public void setBankId(Long bankId) {
		this.bankId = bankId;
	}
	public Long getAccountId() {
		return accountId;
	}
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public String getChequeNumber() {
		return chequeNumber;
	}
	public void setChequeNumber(String chequeNumber) {
		this.chequeNumber = chequeNumber;
	}
	public String getChequeDate() {
		return chequeDate;
	}
	public void setChequeDate(String chequeDate) {
		this.chequeDate = chequeDate;
	}
	public BigDecimal getChequeAmount() {
		return chequeAmount;
	}
	public void setChequeAmount(BigDecimal chequeAmount) {
		this.chequeAmount = chequeAmount;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTxnType() {
		return txnType;
	}
	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}
	
	public Date getBankStatementDate() {
		return bankStatementDate;
	}
	public void setBankStatementDate(Date bankStatementDate) {
		this.bankStatementDate = bankStatementDate;
	}
	public void setReconciliationDate(Date reconciliationDate) {
		this.reconciliationDate = reconciliationDate;
	}
	public Date getReconciliationDate() {
		return reconciliationDate;
	}
	public Long getIhId() {
		return ihId;
	}
	public void setIhId(Long ihId) {
		this.ihId = ihId;
	}
	public String getVoucherNumber() {
		return voucherNumber;
	}
	public void setVoucherNumber(String voucherNumber) {
		this.voucherNumber = voucherNumber;
	}
	public Integer getLimit() {
		return limit;
	}
	public void setLimit(Integer limit) {
		this.limit = limit;
	}
	public String getInstrumentNo() {
		return instrumentNo;
	}
	public void setInstrumentNo(String instrumentNo) {
		this.instrumentNo = instrumentNo;
	}
	public BigDecimal getCreditAmount() {
		return creditAmount;
	}
	public void setCreditAmount(BigDecimal creditAmount) {
		this.creditAmount = creditAmount;
	}
	public BigDecimal getDebitAmount() {
		return debitAmount;
	}
	public void setDebitAmount(BigDecimal debitAmount) {
		this.debitAmount = debitAmount;
	}
	public BigDecimal getOtherCreditAmount() {
		return otherCreditAmount;
	}
	public void setOtherCreditAmount(BigDecimal otherCreditAmount) {
		this.otherCreditAmount = otherCreditAmount;
	}
	public BigDecimal getOtherDebitAmount() {
		return otherDebitAmount;
	}
	public void setOtherDebitAmount(BigDecimal otherDebitAmount) {
		this.otherDebitAmount = otherDebitAmount;
	}
	
}
