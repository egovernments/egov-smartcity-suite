package org.egov.model.voucher;

import java.util.Date;


public class VoucherTypeBean {
	
	private String voucherName;
	private String voucherType;
	private String voucherNumType;
	private String cgnType;
	private String voucherNumFrom;
	private String voucherNumTo;
	private String voucherDateFrom;
	private String voucherSubType;
	
	/**
	 * @description - added properties for the contractor,supplier,salary and fixed asset JV manual creation screen.
	 * 
	 */
	
	 private String partyBillNum;
	 private String partyName;
	 private Date partyBillDate;
	 private String billNum;
	 private Date billDate;
	 
	 public String getVoucherSubType() {
			return voucherSubType;
	}
	public void setVoucherSubType(String voucherSubType) {
			this.voucherSubType = voucherSubType;
	}
	 public String getPartyBillNum() {
		return partyBillNum;
	}
	public void setPartyBillNum(String partyBillNum) {
		this.partyBillNum = partyBillNum;
	}
	public String getPartyName() {
		return partyName;
	}
	public void setPartyName(String partyName) {
		this.partyName = partyName;
	}
	public Date getPartyBillDate() {
		return partyBillDate;
	}
	public void setPartyBillDate(Date partyBillDate) {
		this.partyBillDate = partyBillDate;
	}
	public String getBillNum() {
		return billNum;
	}
	public void setBillNum(String billNum) {
		this.billNum = billNum;
	}
	public Date getBillDate() {
		return billDate;
	}
	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}
	
	public String getVoucherDateFrom() {
		return voucherDateFrom;
	}
	public void setVoucherDateFrom(String voucherDateFrom) {
		this.voucherDateFrom = voucherDateFrom;
	}
	public String getVoucherDateTo() {
		return voucherDateTo;
	}
	public void setVoucherDateTo(String voucherDateTo) {
		this.voucherDateTo = voucherDateTo;
	}
	private String voucherDateTo;
	public String getVoucherNumFrom() {
		return voucherNumFrom;
	}
	public void setVoucherNumFrom(String voucherNumFrom) {
		this.voucherNumFrom = voucherNumFrom;
	}
	public String getVoucherNumTo() {
		return voucherNumTo;
	}
	public void setVoucherNumTo(String voucherNumTo) {
		this.voucherNumTo = voucherNumTo;
	}
	public String getVoucherName() {
		return voucherName;
	}
	public void setVoucherName(String voucherName) {
		this.voucherName = voucherName;
	}
	public String getVoucherType() {
		return voucherType;
	}
	public void setVoucherType(String voucherType) {
		this.voucherType = voucherType;
	}
	public String getVoucherNumType() {
		return voucherNumType;
	}
	public void setVoucherNumType(String voucherNumType) {
		this.voucherNumType = voucherNumType;
	}
	public String getCgnType() {
		return cgnType;
	}
	public void setCgnType(String cgnType) {
		this.cgnType = cgnType;
	}
	
	
}
