package com.exilant.eGov.src.reports;

public class BillRegister
{
	private String po;
	private String billDate;
	private String billNumber;
	private String billAmount;
	private String billApprovalStatus;
	private String paymentDate;
	private String passedAmount;
	private String paidAmount;
	private String voucherNumbaer;
	private String voucherDate;
	public static String totalPassed;
	public static String totalBillAmount;
	public static String totalBill;
	

	/**
	 * 
	 */
	public BillRegister() {
		
		// TODO Auto-generated constructor stub
	}
	
	
	/**
	 * @return Returns the totalBill.
	 */
	public static String getTotalBill() {
		return totalBill;
	}
	/**
	 * @param totalBill The totalBill to set.
	 */
	public static void setTotalBill(String totalBill) {
		BillRegister.totalBill = totalBill;
	}
	/**
	 * @return Returns the totalBillAmount.
	 */
	public static String getTotalBillAmount() {
		return totalBillAmount;
	}
	/**
	 * @param totalBillAmount The totalBillAmount to set.
	 */
	public static void setTotalBillAmount(String totalBillAmount) {
		BillRegister.totalBillAmount = totalBillAmount;
	}
	/**
	 * @return Returns the totalPassed.
	 */
	public static String getTotalPassed() {
		return totalPassed;
	}
	/**
	 * @param totalPassed The totalPassed to set.
	 */
	public static void setTotalPassed(String totalPassed) {
		BillRegister.totalPassed = totalPassed;
	}
	/**
	 * @return Returns the billAmount.
	 */
	public String getBillAmount() {
		return billAmount;
	}
	/**
	 * @param billAmount The billAmount to set.
	 */
	public void setBillAmount(String billAmount) {
		this.billAmount = billAmount;
	}
	/**
	 * @return Returns the billApprovalStatus.
	 */
	public String getBillApprovalStatus() {
		return billApprovalStatus;
	}
	/**
	 * @param billApprovalStatus The billApprovalStatus to set.
	 */
	public void setBillApprovalStatus(String billApprovalStatus) {
		this.billApprovalStatus = billApprovalStatus;
	}
	/**
	 * @return Returns the billDate.
	 */
	public String getBillDate() {
		return billDate;
	}
	/**
	 * @param billDate The billDate to set.
	 */
	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}
	/**
	 * @return Returns the billNumber.
	 */
	public String getBillNumber() {
		return billNumber;
	}
	/**
	 * @param billNumber The billNumber to set.
	 */
	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}
	/**
	 * @return Returns the paidAmount.
	 */
	public String getPaidAmount() {
		return paidAmount;
	}
	/**
	 * @param paidAmount The paidAmount to set.
	 */
	public void setPaidAmount(String paidAmount) {
		this.paidAmount = paidAmount;
	}
	/**
	 * @return Returns the passedAmount.
	 */
	public String getPassedAmount() {
		return passedAmount;
	}
	/**
	 * @param passedAmount The passedAmount to set.
	 */
	public void setPassedAmount(String passedAmount) {
		this.passedAmount = passedAmount;
	}
	/**
	 * @return Returns the paymentDate.
	 */
	public String getPaymentDate() {
		return paymentDate;
	}
	/**
	 * @param paymentDate The paymentDate to set.
	 */
	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}
	/**
	 * @return Returns the po.
	 */
	public String getPo() {
		return po;
	}
	/**
	 * @param po The po to set.
	 */
	public void setPo(String po) {
		this.po = po;
	}
	/**
	 * @return Returns the voucherDate.
	 */
	public String getVoucherDate() {
		return voucherDate;
	}
	/**
	 * @param voucherDate The voucherDate to set.
	 */
	public void setVoucherDate(String voucherDate) {
		this.voucherDate = voucherDate;
	}
	/**
	 * @return Returns the voucherNumbaer.
	 */
	public String getVoucherNumbaer() {
		return voucherNumbaer;
	}
	/**
	 * @param voucherNumbaer The voucherNumbaer to set.
	 */
	public void setVoucherNumbaer(String voucherNumbaer) {
		this.voucherNumbaer = voucherNumbaer;
	}
}