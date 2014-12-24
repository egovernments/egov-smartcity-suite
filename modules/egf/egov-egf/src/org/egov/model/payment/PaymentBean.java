package org.egov.model.payment;

import java.math.BigDecimal;
import java.util.Date;

public class PaymentBean {
	private Long billId;
	private String billNumber;
	private Date billDate;
	private String payTo;
	private String expType;
	private BigDecimal netAmt;
	private BigDecimal earlierPaymentAmt;
	private BigDecimal payableAmt;
	private BigDecimal paymentAmt;
	private BigDecimal deductionAmt;
	private BigDecimal passedAmt;
	private String fundName;
	private String deptName;
	private String schemeName;
	private String subschemeName;
	private String functionaryName;
	private String fundsourceName;
	private String fieldName;
	private boolean isSelected;
	private String billVoucherNumber;
	private Date billVoucherDate;
	
	public String getExpType() {
		return expType;
	}
	public void setExpType(String expType) {
		this.expType = expType;
	}
	public Long getBillId() {
		return billId;
	}
	public void setBillId(BigDecimal billId) {
		this.billId = billId.longValue();
	}
	public Long getCsBillId() {
		return billId;
	}
	public void setCsBillId(Long voucherHeaderId) {
		this.billId = voucherHeaderId;
	}
	public String getBillNumber() {
		return billNumber;
	}
	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}
	public Date getBillDate() {
		return billDate;
	}
	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}
	public String getPayTo() {
		return payTo;
	}
	public void setPayTo(String payTo) {
		this.payTo = payTo;
	}
	public BigDecimal getNetAmt() {
		return netAmt;
	}
	public void setNetAmt(BigDecimal netAmt) {
		this.netAmt = netAmt;
	}
	public BigDecimal getEarlierPaymentAmt() {
		return earlierPaymentAmt;
	}
	public void setEarlierPaymentAmt(BigDecimal earlierPaymentAmt) {
		this.earlierPaymentAmt = earlierPaymentAmt;
	}
	public BigDecimal getPayableAmt() {
		return payableAmt;
	}
	public void setPayableAmt(BigDecimal payableAmt) {
		this.payableAmt = payableAmt;
	}
	public BigDecimal getPaymentAmt() {
		return paymentAmt;
	}
	public void setPaymentAmt(BigDecimal paymentAmt) {
		this.paymentAmt = paymentAmt;
	}
	public BigDecimal getDeductionAmt() {
		return deductionAmt;
	}
	public void setDeductionAmt(BigDecimal deductionAmt) {
		this.deductionAmt = deductionAmt;
	}
	public boolean getIsSelected() {
		return isSelected;
	}
	public void setIsSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	public String getFundName() {
		return fundName;
	}
	public void setFundName(String fundName) {
		this.fundName = fundName;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getSchemeName() {
		return schemeName;
	}
	public void setSchemeName(String schemeName) {
		this.schemeName = schemeName;
	}
	public String getSubschemeName() {
		return subschemeName;
	}
	public void setSubschemeName(String subschemeName) {
		this.subschemeName = subschemeName;
	}
	public String getFunctionaryName() {
		return functionaryName;
	}
	public void setFunctionaryName(String functionaryName) {
		this.functionaryName = functionaryName;
	}
	public String getFundsourceName() {
		return fundsourceName;
	}
	public void setFundsourceName(String fundsourceName) {
		this.fundsourceName = fundsourceName;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public BigDecimal getPassedAmt() {
		return passedAmt;
	}
	public void setPassedAmt(BigDecimal passedAmt) {
		this.passedAmt = passedAmt;
	}
	public String getBillVoucherNumber() {
		return billVoucherNumber;
	}
	public void setBillVoucherNumber(String billVoucherNumber) {
		this.billVoucherNumber = billVoucherNumber;
	}
	public Date getBillVoucherDate() {
		return billVoucherDate;
	}
	public void setBillVoucherDate(Date billVoucherDate) {
		this.billVoucherDate = billVoucherDate;
	}
}
