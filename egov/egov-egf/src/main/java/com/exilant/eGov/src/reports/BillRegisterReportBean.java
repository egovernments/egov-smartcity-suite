/*
 * Created on Jan 24, 2005
 * @author Sumit
 */
package com.exilant.eGov.src.reports;

public class BillRegisterReportBean 
{
	private String bill_Po;
	private String bill_Creditor;
	private String bill_Status;
	private String bill_AppStaus;	
	private String startDate;
	private String endDate;
	
	
	/**
	 * @param procurementOrder
	 * @param creditor
	 * @param billStatus
	 * @param billApprovalStatus
	 * @param startDate
	 * @param endDate
	 */
	
	/**
	 * 
	 */
	
	public BillRegisterReportBean() {
		
		// TODO Auto-generated constructor stub
		this.bill_Po = "";
		this.bill_Creditor = "";
		this.bill_Status = "";
		this.bill_AppStaus = "";
		this.startDate = "";
		this.endDate = "";
	}
	
	/**
	 * @return Returns the bill_AppStaus.
	 */
	public String getBill_AppStaus() {
		return bill_AppStaus;
	}
	/**
	 * @param bill_AppStaus The bill_AppStaus to set.
	 */
	public void setBill_AppStaus(String bill_AppStaus) {
		this.bill_AppStaus = bill_AppStaus;
	}
	/**
	 * @return Returns the bill_Creditor.
	 */
	public String getBill_Creditor() {
		return bill_Creditor;
	}
	/**
	 * @param bill_Creditor The bill_Creditor to set.
	 */
	public void setBill_Creditor(String bill_Creditor) {
		this.bill_Creditor = bill_Creditor;
	}
	/**
	 * @return Returns the bill_Po.
	 */
	public String getBill_Po() {
		return bill_Po;
	}
	/**
	 * @param bill_Po The bill_Po to set.
	 */
	public void setBill_Po(String bill_Po) {
		this.bill_Po = bill_Po;
	}
	/**
	 * @return Returns the bill_Status.
	 */
	public String getBill_Status() {
		return bill_Status;
	}
	/**
	 * @param bill_Status The bill_Status to set.
	 */
	public void setBill_Status(String bill_Status) {
		this.bill_Status = bill_Status;
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
}