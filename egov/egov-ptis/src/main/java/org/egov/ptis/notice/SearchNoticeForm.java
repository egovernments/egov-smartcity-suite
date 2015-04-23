package org.egov.ptis.notice;

import org.apache.struts.action.ActionForm;

public class SearchNoticeForm extends ActionForm
{
	private String noticeType = null;
	private String noticeNumber = null;
	private String billNumber = null;
	private String fromDate = null;
	private String toDate = null;
	private String propAddr = null;
	private String dateOfGeneration = null;
	private String ownerNames = null;
	
	
	public String getPropAddr() {
		return propAddr;
	}
	public void setPropAddr(String propAddr) {
		this.propAddr = propAddr;
	}
	public String getDateOfGeneration() {
		return dateOfGeneration;
	}
	public void setDateOfGeneration(String dateOfGeneration) {
		this.dateOfGeneration = dateOfGeneration;
	}
	public String getOwnerNames() {
		return ownerNames;
	}
	public void setOwnerNames(String ownerNames) {
		this.ownerNames = ownerNames;
	}
	public String getNoticeType() {
		return noticeType;
	}
	public void setNoticeType(String noticeType) {
		this.noticeType = noticeType;
	}
	public String getNoticeNumber() {
		return noticeNumber;
	}
	public void setNoticeNumber(String noticeNumber) {
		this.noticeNumber = noticeNumber;
	}
	public String getBillNumber() {
		return billNumber;
	}
	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
}
