package org.egov.model.payment;

import java.math.BigDecimal;
import java.util.Date;

import org.egov.commons.Bankaccount;

public class ChequeAssignment {
	private Long voucherid;
	private Date voucherDate;
	private String voucherNumber;
	private String bankAccNumber;
	private BigDecimal paidAmount;
	private String paidTo;
	private String bankName;          
	private String chequeNumber;
	private String departmentName;
	private boolean isSelected;
	private Date chequeDate;
	private BigDecimal detailtypeid;
	private BigDecimal detailkeyid;
	private BigDecimal billVHId;
	private String serialNo;
	private BigDecimal bankAccountId;                         
	private Bankaccount bankAcc;
	private BigDecimal rtgsTotalPaidAmt;   
	private String bankBranchHeader;
	private boolean addRtgs;
	private boolean headerval;
	private BigDecimal billId;
	private String billNumber;
	private String expenditureType;
	private BigDecimal glcodeId;
	private String drawingOfficerNameTAN;
	private BigDecimal receiptAmount;
	private BigDecimal deductedAmount;
	
	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
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
	public void setBillVHId(BigDecimal billVHId) {
		this.billVHId = billVHId;
	}
	public BigDecimal getBillVHId() {
		return billVHId;
	}
	public Bankaccount getBankAcc() {
		return bankAcc;
	} 
	public void setBankAcc(Bankaccount bankAcc) {
		this.bankAcc = bankAcc;
	}
	public BigDecimal getBankAccountId() {
		return bankAccountId;
	}
	public void setBankAccountId(BigDecimal bankAccountId) {
		this.bankAccountId = bankAccountId;          
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public String getBankAccNumber() {
		return bankAccNumber;
	}
	public void setBankAccNumber(String bankAccNumber) {
		this.bankAccNumber = bankAccNumber;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;                   
	}
	public BigDecimal getRtgsTotalPaidAmt() {
		return rtgsTotalPaidAmt;
	}
	public void setRtgsTotalPaidAmt(BigDecimal rtgsTotalPaidAmt) {
		this.rtgsTotalPaidAmt = rtgsTotalPaidAmt;
	}
	public String getBankBranchHeader() {
		return bankBranchHeader;
	}
	public void setBankBranchHeader(String bankBranchHeader) {
		this.bankBranchHeader = bankBranchHeader;
	}
	public boolean isAddRtgs() {
		return addRtgs;
	}
	public void setAddRtgs(boolean addRtgs) {
		this.addRtgs = addRtgs;
	}
	public boolean isHeaderval() {
		return headerval;
	}
	public void setHeaderval(boolean headerval) {
		this.headerval = headerval;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	public BigDecimal getBillId() {
		return billId;
	}
	public String getBillNumber() {
		return billNumber;
	}
	public void setBillId(BigDecimal billId) {
		this.billId = billId;
	}
	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}
	public String getExpenditureType() {
		return expenditureType;
	}
	public void setExpenditureType(String expenditureType) {
		this.expenditureType = expenditureType;
	}
	public BigDecimal getGlcodeId() {
		return glcodeId;
	}
	public void setGlcodeId(BigDecimal glcodeId) {
		this.glcodeId = glcodeId;
	}
	public String getDrawingOfficerNameTAN() {
		return drawingOfficerNameTAN;
	}
	public void setDrawingOfficerNameTAN(String drawingOfficerNameTAN) {
		this.drawingOfficerNameTAN = drawingOfficerNameTAN;
	}
	public BigDecimal getReceiptAmount() {
		return receiptAmount;
	}
	public void setReceiptAmount(BigDecimal receiptAmount) {
		this.receiptAmount = receiptAmount;
	}
	public BigDecimal getDeductedAmount() {
		return deductedAmount;
	}
	public void setDeductedAmount(BigDecimal deductedAmount) {
		this.deductedAmount = deductedAmount;
	}
	
	
}
