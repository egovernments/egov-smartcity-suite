package org.egov.web.actions.report;

import java.math.BigDecimal;
import java.sql.Date;

public class BudgetAppDisplay {
	
	private String serailNumber;
	private String billAndVoucherNumber;
	private BigDecimal billAmount;
	private BigDecimal cumulativeAmount;
	private BigDecimal balanceAvailableAmount;
	private BigDecimal debitAmount;
	private BigDecimal creditAmount;	
	private Date voucherDate;
	private String description;
	private String billNumber;
	private String VoucherNumber;
	private Date billDate;
	private String bdgApprNumber;
	
	
	public String getSerailNumber() {
		return serailNumber;
	}
	public void setSerailNumber(String serailNumber) {
		this.serailNumber = serailNumber;
	}
	public String getBillAndVoucherNumber() {
		return billAndVoucherNumber;
	}
	public void setBillAndVoucherNumber(String billAndVoucherNumber) {
		this.billAndVoucherNumber = billAndVoucherNumber;
	}
	public BigDecimal getBillAmount() {
		return billAmount;
	}
	public void setBillAmount(BigDecimal billAmount) {
		this.billAmount = billAmount;
	}
	public BigDecimal getCumulativeAmount() {
		return cumulativeAmount;
	}
	public void setCumulativeAmount(BigDecimal cumulativeAmount) {
		this.cumulativeAmount = cumulativeAmount;
	}
	public BigDecimal getBalanceAvailableAmount() {
		return balanceAvailableAmount;
	}
	public void setBalanceAvailableAmount(BigDecimal balanceAvailableAmount) {
		this.balanceAvailableAmount = balanceAvailableAmount;
	}
	public BigDecimal getDebitAmount() {
		return debitAmount;
	}
	public void setDebitAmount(BigDecimal debitAmount) {
		this.debitAmount = debitAmount;
	}
	public BigDecimal getCreditAmount() {
		return creditAmount;
	}
	public void setCreditAmount(BigDecimal creditAmount) {
		this.creditAmount = creditAmount;
	}
	public Date getVoucherDate() {
		return voucherDate;
	}
	public void setVoucherDate(Date voucherDate) {
		this.voucherDate = voucherDate;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getBillNumber() {
		return billNumber;
	}
	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}
	public String getVoucherNumber() {
		return VoucherNumber;
	}
	public void setVoucherNumber(String voucherNumber) {
		VoucherNumber = voucherNumber;
	}
	public Date getBillDate() {
		return billDate;
	}
	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}
	public String getBdgApprNumber() {
		return bdgApprNumber;
	}
	public void setBdgApprNumber(String bdgApprNumber) {
		this.bdgApprNumber = bdgApprNumber;
	}
	
}
