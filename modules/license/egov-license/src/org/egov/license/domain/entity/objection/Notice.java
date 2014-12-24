/*
 * @(#)Notice.java 3.0, 29 Jul, 2013 1:24:25 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.license.domain.entity.objection;

import java.util.Date;

import org.egov.infstr.models.BaseModel;

public class Notice extends BaseModel {
	private static final long serialVersionUID = -6369641818759075953L;
	private LicenseObjection objection;
	private String noticeNumber;
	private Date noticeDate;
	private String noticeType;
	private String moduleName;
	// for uploaded document references
	private String docNumber;

	public LicenseObjection getObjection() {
		return this.objection;
	}

	public void setObjection(final LicenseObjection objection) {
		this.objection = objection;
	}

	public String getNoticeNumber() {
		return this.noticeNumber;
	}

	public void setNoticeNumber(final String noticeNumber) {
		this.noticeNumber = noticeNumber;
	}

	public Date getNoticeDate() {
		return this.noticeDate;
	}

	public void setNoticeDate(final Date noticeDate) {
		this.noticeDate = noticeDate;
	}

	public String getNoticeType() {
		return this.noticeType;
	}

	public void setNoticeType(final String noticeType) {
		this.noticeType = noticeType;
	}

	public String getModuleName() {
		return this.moduleName;
	}

	public void setModuleName(final String moduleName) {
		this.moduleName = moduleName;
	}

	public String getDocNumber() {
		return this.docNumber;
	}

	public void setDocNumber(final String docNumber) {
		this.docNumber = docNumber;
	}

	public String generateNumber(final String runningNumber) {
		this.noticeNumber = "NTC" + runningNumber;
		return this.noticeNumber;
	}

	@Override
	public String toString() {
		final StringBuilder str = new StringBuilder();
		str.append("Notice={");
		str.append("objection=").append(this.objection == null ? "null" : this.objection.toString());
		str.append("noticeNumber=").append(this.noticeNumber == null ? "null" : this.noticeNumber.toString());
		str.append("noticeDate=").append(this.noticeDate == null ? "null" : this.noticeDate.toString());
		str.append("noticeType=").append(this.noticeType == null ? "null" : this.noticeType.toString());
		str.append("moduleName=").append(this.moduleName == null ? "null" : this.moduleName.toString());
		str.append("docNumber=").append(this.docNumber == null ? "null" : this.docNumber.toString());
		str.append("}");
		return str.toString();
	}
}
