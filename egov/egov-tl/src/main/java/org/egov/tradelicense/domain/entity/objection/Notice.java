package org.egov.tradelicense.domain.entity.objection;

import java.util.Date;

import org.egov.infstr.models.BaseModel;

public class Notice extends BaseModel {
	private static final long serialVersionUID = -6369641818759075953L;
	private	LicenseObjection objection;
	private	String noticeNumber;
	private Date noticeDate;
	private	String noticeType;
	private	String moduleName;
	// for uploaded document references
	private	String docNumber;
	public LicenseObjection getObjection() {
		return objection;
	}
	public void setObjection(LicenseObjection objection) {
		this.objection = objection;
	}
	public String getNoticeNumber() {
		return noticeNumber;
	}
	public void setNoticeNumber(String noticeNumber) {
		this.noticeNumber = noticeNumber;
	}
	public Date getNoticeDate() {
		return noticeDate;
	}
	public void setNoticeDate(Date noticeDate) {
		this.noticeDate = noticeDate;
	}
	public String getNoticeType() {
		return noticeType;
	}
	public void setNoticeType(String noticeType) {
		this.noticeType = noticeType;
	}
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	public String getDocNumber() {
		return docNumber;
	}
	public void setDocNumber(String docNumber) {
		this.docNumber = docNumber;
	}
	
	public String generateNumber(String runningNumber) {
		this.noticeNumber = "NTC" + runningNumber;
		return this.noticeNumber;
	}
	@Override
	public String toString() {
		final StringBuilder str = new StringBuilder();
		str.append("Notice={");
		str.append("objection=").append(objection == null ? "null" : objection.toString());
		str.append("noticeNumber=").append(noticeNumber == null ? "null" : noticeNumber.toString());
		str.append("noticeDate=").append(noticeDate == null ? "null" : noticeDate.toString());
		str.append("noticeType=").append(noticeType == null ? "null" : noticeType.toString());
		str.append("moduleName=").append(moduleName == null ? "null" : moduleName.toString());
		str.append("docNumber=").append(docNumber == null ? "null" : docNumber.toString());
		str.append("}");
		return str.toString();
	}
}
