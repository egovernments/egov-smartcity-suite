package org.egov.web.actions.report;

import java.math.BigDecimal;



public class AutoRemittanceBeanReport{

	private String voucherNumber;
	private String billNumber;
	private BigDecimal billAmount;
	private String remittancePaymentNo;
	private BigDecimal remittedAmount;
	private String rtgsNoDate;
	private BigDecimal rtgsAmount;
	private String partyName;
	private String fundName;
	private String bankbranchAccount;    
    private BigDecimal remittanceDTId;
    private String remittanceCOA;  
    private BigDecimal detailKeyTypeId;
    private BigDecimal detailKeyId;
	private BigDecimal voucherId;
	private BigDecimal billId;
	private BigDecimal paymentVoucherId;
	private BigDecimal remittedAmountSubtotal=new BigDecimal("0");
	private String department;
	private String drawingOfficer;
	private String panNumber;
   
	public String getVoucherNumber() {  
		return voucherNumber;
	}
	public void setVoucherNumber(String voucherNumber) {
		this.voucherNumber = voucherNumber;
	}
	public String getBillNumber() {  
		return billNumber;
	}
	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}
	public BigDecimal getBillAmount() {
		return billAmount;
	}
	public void setBillAmount(BigDecimal billAmount) {
		this.billAmount = billAmount;
	}
	public String getRemittancePaymentNo() {
		return remittancePaymentNo;
	}
	public void setRemittancePaymentNo(String remittancePaymentNo) {
		this.remittancePaymentNo = remittancePaymentNo;
	}
	public BigDecimal getRemittedAmount() {
		return remittedAmount;
	}
	public void setRemittedAmount(BigDecimal remittedAmount) {
		this.remittedAmount = remittedAmount;
	}
	public String getRtgsNoDate() {
		return rtgsNoDate;
	}
	public void setRtgsNoDate(String rtgsNoDate) {
		this.rtgsNoDate = rtgsNoDate;
	}
	public BigDecimal getRtgsAmount() {
		return rtgsAmount;
	}
	public void setRtgsAmount(BigDecimal rtgsAmount) {
		this.rtgsAmount = rtgsAmount;
	}
	public String getPartyName() {
		return partyName;
	}
	public void setPartyName(String partyName) {
		this.partyName = partyName;
	}
	public String getFundName() {
		return fundName;
	}
	public void setFundName(String fundName) {
		this.fundName = fundName;
	}
	public String getBankbranchAccount() {
		return bankbranchAccount;
	}
	public void setBankbranchAccount(String bankbranchAccount) {
		this.bankbranchAccount = bankbranchAccount;
	}

	public String getRemittanceCOA() {
		return remittanceCOA;
	}
	public void setRemittanceCOA(String remittanceCOA) {
		this.remittanceCOA = remittanceCOA;
	}
	public BigDecimal getRemittanceDTId() {
		return remittanceDTId;
	}
	public void setRemittanceDTId(BigDecimal remittanceDTId) {
		this.remittanceDTId = remittanceDTId;
	}
	public BigDecimal getDetailKeyTypeId() {
		return detailKeyTypeId;
	}
	public void setDetailKeyTypeId(BigDecimal detailKeyTypeId) {
		this.detailKeyTypeId = detailKeyTypeId;
	}
	public BigDecimal getDetailKeyId() {
		return detailKeyId;
	}
	public void setDetailKeyId(BigDecimal detailKeyId) {
		this.detailKeyId = detailKeyId;
	}
	public BigDecimal getVoucherId() {
		return voucherId;
	}
	public void setVoucherId(BigDecimal voucherId) {
		this.voucherId = voucherId;
	}
	public BigDecimal getBillId() {
		return billId;
	}
	public void setBillId(BigDecimal billId) {
		this.billId = billId;
	}
	public BigDecimal getPaymentVoucherId() {
		return paymentVoucherId;
	}
	public void setPaymentVoucherId(BigDecimal paymentVoucherId) {      
		this.paymentVoucherId = paymentVoucherId;
	}
	public BigDecimal getRemittedAmountSubtotal() {
		return remittedAmountSubtotal;
	}
	public void setRemittedAmountSubtotal(BigDecimal remittedAmountSubtotal) {
		this.remittedAmountSubtotal = remittedAmountSubtotal;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getDrawingOfficer() {
		return drawingOfficer;
	}
	public void setDrawingOfficer(String drawingOfficer) {
		this.drawingOfficer = drawingOfficer;
	}
	public String getPanNumber() {
		return panNumber;
	}
	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}

}
