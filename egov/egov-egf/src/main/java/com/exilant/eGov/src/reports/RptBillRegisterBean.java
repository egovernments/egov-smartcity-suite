package com.exilant.eGov.src.reports;

import org.apache.log4j.Logger;

public class RptBillRegisterBean
{
	private static final Logger LOGGER = Logger.getLogger(RptBillRegisterBean.class);
	private String slno;
	private String billDate;
	private String conSupName;
	private String particulars;
	private String billAmount;
	private String approvedBy;
	private String sanctionedDate;
	private String sanctionedAmount;
	private String paymentDate;
	private String disallowedAmount;
	private String balanceAmount;
	public  String remarks;
	public int finId;
	public int conSupTypeId;
	
	public String fundId;
	public String fieldId;
	private String functionaryId;
	private String startDate;
	private String endDate;
	private String voucherNo;
	private String paidAmt ;
	private String fundName;
	private String fieldName;
	private String functionaryName;
	private String ulbname;

	/**
	 *
	 */
	public RptBillRegisterBean() {

		// TODO Auto-generated constructor stub
	}

	/**
	 * @return Returns the slno.
	 */
	public  String getSlno() {
		return slno;
	}
	/**
	 * @param slno The slno to set.
	 */
	public  void setSlno(String slno) {
		this.slno = slno;
	}
	
	/**
	 * @return Returns the billDate.
	 */
	public  String getBillDate() {
		return billDate;
	}
	/**
	 * @param billDate The billDate to set.
	 */
	public  void setBillDate(String billDate) {
		this.billDate = billDate;
	}
	/**
	 * @return Returns the conSupName.
	 */
	public  String getConSupName() {
		return conSupName;
	}
	/**
	 * @param conSupName The conSupName to set.
	 */
	public   void setConSupName(String conSupName) {
		this.conSupName = conSupName;
	}
	/**
	 * @return Returns the particulars.
	 */
	public  String getParticulars() {
		return particulars;
	}
	/**
	 * @param particulars The particulars to set.
	 */
	public  void setParticulars(String particulars) {
		this.particulars = particulars;
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
	 * @return Returns the approvedBy.
	 */
	public String getApprovedBy() {
		return approvedBy;
	}
	/**
	 * @param approvedBy The approvedBy to set.
	 */
	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}
	/**
	 * @return Returns the sanctionedDate.
	 */
	public String getSanctionedDate() {
		return billDate;
	}
	/**
	 * @param sanctionedDate The sanctionedDate to set.
	 */
	public void setSanctionedDate(String sanctionedDate) {
		this.sanctionedDate = sanctionedDate;
	}
	/**
	 * @return Returns the sanctionedAmount.
	 */
	public String getSanctionedAmount() {
		return sanctionedAmount;
	}
	/**
	 * @param sanctionedAmount The sanctionedAmount to set.
	 */
	public void setSanctionedAmount(String sanctionedAmount) {
		this.sanctionedAmount = sanctionedAmount;
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
	 * @return Returns the disallowedAmount.
	 */
	public String getDisallowedAmount() {
		return disallowedAmount;
	}
	/**
	 * @param disallowedAmount The disallowedAmount to set.
	 */
	public void setDisallowedAmount(String disallowedAmount) {
		this.disallowedAmount = disallowedAmount;
	}
	/**
	 * @return Returns the balanceAmount.
	 */
	public String getBalanceAmount() {
		return balanceAmount;
	}
	/**
	 * @param balanceAmount The balanceAmount to set.
	 */
	public void setBalanceAmount(String balanceAmount) {
		this.balanceAmount = balanceAmount;
	}
	/**
	 * @return Returns the delayReasons.
	 */
/*	public String getDelayReasons() {
		return delayReasons;
	}*/
	/**
	 * @param delayReasons The delayReasons to set.
	 */
	/*public void setDelayReasons(String delayReasons) {
		this.delayReasons = delayReasons;
	}*/

	/**
	 * @return Returns the finId.
	 */
	public int getFinId() {
		return finId;
	}
	/**
	 * @param finId The finId to set.
	 */
	public void setFinId(int finId) {
		if(LOGGER.isDebugEnabled())     LOGGER.debug("inside set finId");
		this.finId = finId;
	}
	/**
	 * @return Returns the conSupTypeId.
	 */
	public int getConSupTypeId() {
		if(LOGGER.isDebugEnabled())     LOGGER.debug("inside set conSupId");
				return conSupTypeId;
			}
	/**
	 * @param conSupTypeId The conSupTypeId to set.
	 */
	public void setConSupTypeId(int conSupTypeId) {
	this.conSupTypeId = conSupTypeId;
	}
	/**
	 * @return Returns the endDate.
	 */
	public String getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate The endDate to set.
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return Returns the fieldId.
	 */
	public String getFieldId() {
		return fieldId;
	}
	/**
	 * @param fieldId The fieldId to set.
	 */
	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}
	/**
	 * @return Returns the functionaryId.
	 */
	public String getFunctionaryId() {
		return functionaryId;
	}
	/**
	 * @param functionaryId The functionaryId to set.
	 */
	public void setFunctionaryId(String functionaryId) {
		this.functionaryId = functionaryId;
	}
	/**
	 * @return Returns the fundId.
	 */
	public String getFundId() {
		return fundId;
	}
	/**
	 * @param fundId The fundId to set.
	 */
	public void setFundId(String fundId) {
		this.fundId = fundId;
	}
	/**
	 * @return Returns the startDate.
	 */
	public String getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate The startDate to set.
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	/**
	 * @return Returns the voucherNo.
	 */
	public String getVoucherNo() {
		return voucherNo;
	}
	/**
	 * @param voucherNo The voucherNo to set.
	 */
	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}
	/**
	 * @return Returns the paidAmt.
	 */
	public String getPaidAmt() {
		return paidAmt;
	}
	/**
	 * @param paidAmt The paidAmt to set.
	 */
	public void setPaidAmt(String paidAmt) {
		this.paidAmt = paidAmt;
	}
	
	/**
	 * @return Returns the remarks.
	 */
	public  String getRemarks() {
		return remarks;
	}
	/**
	 * @param remarks The remarks to set.
	 */
	public  void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	/**
	 * @return Returns the fundName.
	 */
	public String getFundName() {
		return fundName;
	}
	/**
	 * @param fundName The fundName to set.
	 */
	public void setFundName(String fundName) {
		this.fundName = fundName;
	}
	
	/**
	 * @return Returns the fieldName.
	 */
	public String getFieldName() {
		return fieldName;
	}
	/**
	 * @param fieldName The fieldName to set.
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	/**
	 * @return Returns the functionaryName.
	 */
	public String getFunctionaryName() {
		return functionaryName;
	}
	/**
	 * @param functionaryName The functionaryName to set.
	 */
	public void setFunctionaryName(String functionaryName) {
		this.functionaryName = functionaryName;
	}
	/**
	 * @return Returns the ulbname.
	 */
	public String getUlbname() {
		return ulbname;
	}
	/**
	 * @param ulbname The ulbname to set.
	 */
	public void setUlbname(String ulbname) {
		this.ulbname = ulbname;
	}
}