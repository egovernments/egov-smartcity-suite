package org.egov.web.actions.report;

import java.math.BigDecimal;
import java.util.Date;

public class VoucherReportView {
	
	private String VoucherNumber;
	private String voucherName ;
	private String voucherType ;
	private Date voucherDate;
	private String source;
	private BigDecimal amount;
	private String status;
	private String deptName;
	private String owner;
	private String paymentMode;

	public Date getVoucherDate() {
		return voucherDate;
	}                     
	public void setVoucherDate(Date voucherDate) {
		this.voucherDate = voucherDate;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	
	public void setVoucherType(String voucherType) {
		this.voucherType = voucherType;
	}
	public String getVoucherType() {
		return voucherType;
	}
	public void setVoucherNumber(String voucherNumber) {
		VoucherNumber = voucherNumber;
	}
	public String getVoucherNumber() {
		return VoucherNumber;
	}
	public void setVoucherName(String voucherName) {
		this.voucherName = voucherName;
	}
	public String getVoucherName() {
		return voucherName;
	}                      
	public void setSource(String fundSource) {
		this.source = fundSource;
	}
	public String getSource() {
		return source;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatus() {
		return status;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getOwner() {
		return owner;
	}
	public String getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

}
