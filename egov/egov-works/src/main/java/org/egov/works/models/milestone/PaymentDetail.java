package org.egov.works.models.milestone;

import java.math.BigDecimal;

public class PaymentDetail {

	String billNumber;
	String billDate;
	String billType;
	BigDecimal billAmount;
	String cjvNo;
	BigDecimal releasedAmount;
	BigDecimal outstandingAmount;

	public String getBillNumber() {
		return billNumber;
	}
	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}
	public String getBillDate() {
		return billDate;
	}
	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}
	public String getBillType() {
		return billType;
	}
	public void setBillType(String billType) {
		this.billType = billType;
	}
	public BigDecimal getBillAmount() {
		return billAmount;
	}
	public void setBillAmount(BigDecimal billAmount) {
		this.billAmount = billAmount;
	}
	public BigDecimal getReleasedAmount() {
		return releasedAmount;
	}
	public void setReleasedAmount(BigDecimal releasedAmount) {
		this.releasedAmount = releasedAmount;
	}
	public BigDecimal getOutstandingAmount() {
		return outstandingAmount;
	}
	public void setOutstandingAmount(BigDecimal outstandingAmount) {
		this.outstandingAmount = outstandingAmount;
	}
	public String getCjvNo() {
		return cjvNo;
	}
	public void setCjvNo(String cjvNo) {
		this.cjvNo = cjvNo;
	}

}
