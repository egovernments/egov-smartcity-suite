package org.egov.model.payment;

import java.math.BigDecimal;
import java.util.Date;

public class ChequeAssignment {
	private Long voucherid;
	private Date voucherDate;
	private String voucherNumber;
	private BigDecimal paidAmount;
	private String paidTo;
	private String chequeNumber;
	private boolean isSelected;
	private Date chequeDate;
	private BigDecimal detailtypeid;
	private BigDecimal detailkeyid;
	
	public BigDecimal getDetailtypeid() {
		return detailtypeid;
	}
	public void setDetailtypeid(BigDecimal detailtypeid) {
		this.detailtypeid = detailtypeid;
	}
	public BigDecimal getDetailkeyid() {
		return detailkeyid;
	}
	public void setDetailkeyid(BigDecimal detailkeyid) {
		this.detailkeyid = detailkeyid;
	}
	
	
	public Long getVoucherid() {
		return voucherid;
	}
	public void setVoucherid(BigDecimal voucherid) {
		this.voucherid = voucherid.longValue();
	}
	public Long getVoucherHeaderId() {
		return voucherid;
	}
	public void setVoucherHeaderId(Long voucherHeaderId) {
		this.voucherid = voucherHeaderId;
	}
	public Date getVoucherDate() {
		return voucherDate;
	}
	public void setVoucherDate(Date voucherDate) {
		this.voucherDate = voucherDate;
	}
	public String getVoucherNumber() {
		return voucherNumber;
	}
	public void setVoucherNumber(String voucherNumber) {
		this.voucherNumber = voucherNumber;
	}
	public BigDecimal getPaidAmount() {
		return paidAmount;
	}
	public void setPaidAmount(BigDecimal paidAmount) {
		this.paidAmount = paidAmount;
	}
	public String getPaidTo() {
		return paidTo;
	}
	public void setPaidTo(String paidTo) {
		this.paidTo = paidTo;
	}
	public boolean getIsSelected() {
		return isSelected;
	}
	public void setIsSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	public String getChequeNumber() {
		return chequeNumber;
	}
	public void setChequeNumber(String chequeNumber) {
		this.chequeNumber = chequeNumber;
	}
	public Date getChequeDate() {
		return chequeDate;
	}
	public void setChequeDate(Date chequeDate) {
		this.chequeDate = chequeDate;
	}

}
