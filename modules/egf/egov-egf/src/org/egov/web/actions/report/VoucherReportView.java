package org.egov.web.actions.report;

import java.math.BigDecimal;

public class VoucherReportView {
	
	private int serialNumber;
	private String VoucherNumber;
	private String voucherName ;
	private String voucherType ;
	private String voucherDate;
	private String source;
	private BigDecimal amount;
	private String status;
	private String deptName;

	public void setSerialNumber(int sno) {
		this.serialNumber = sno;
	}
	public int getSerialNumber() {
		return serialNumber;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setVoucherDate(String voucherDate) {
		this.voucherDate = voucherDate;
	}
	public String getVoucherDate() {
		return voucherDate;
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

}
