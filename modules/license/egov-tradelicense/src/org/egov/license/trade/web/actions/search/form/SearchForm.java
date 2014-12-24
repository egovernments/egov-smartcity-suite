/*
 * @(#)SearchForm.java 3.0, 25 Jul, 2013 4:57:53 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.license.trade.web.actions.search.form;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author satyam
 */
public class SearchForm {

	private String licenseNumber;
	private String oldLicenseNumber;
	private String applNumber;
	private String applicantName;
	private String establishmentName;
	private String zone;
	private String division;
	private String tradeName;

	private Date applicationFromDate;
	private Date applicationToDate;

	private boolean licenseexpired;
	private boolean licenseCancelled;
	private boolean licenseSuspended;
	private boolean licenseObjected;
	private boolean licenseRejected;
	private boolean motorInstalled;
	private String noticeType;
	private String noticeNumber;
	private String docNumber;
	private Date noticeFromDate;
	private Date noticeToDate;
	private BigDecimal licenseFeeFrom;
	private BigDecimal licenseFeeTo;

	public Date getNoticeFromDate() {
		return this.noticeFromDate;
	}

	public void setNoticeFromDate(final Date noticeFromDate) {
		this.noticeFromDate = noticeFromDate;
	}

	public Date getNoticeToDate() {
		return this.noticeToDate;
	}

	public void setNoticeToDate(final Date noticeToDate) {
		this.noticeToDate = noticeToDate;
	}

	public String getNoticeNumber() {
		return this.noticeNumber;
	}

	public void setNoticeNumber(final String noticeNumber) {
		this.noticeNumber = noticeNumber;
	}

	public String getDocNumber() {
		return this.docNumber;
	}

	public void setDocNumber(final String docNumber) {
		this.docNumber = docNumber;
	}

	public String getNoticeType() {
		return this.noticeType;
	}

	public void setNoticeType(final String noticeType) {
		this.noticeType = noticeType;
	}

	public String getLicenseNumber() {
		return this.licenseNumber;
	}

	public void setLicenseNumber(final String licenseNumber) {
		this.licenseNumber = licenseNumber;
	}

	public String getOldLicenseNumber() {
		return this.oldLicenseNumber;
	}

	public void setOldLicenseNumber(final String oldLicenseNumber) {
		this.oldLicenseNumber = oldLicenseNumber;
	}

	public String getApplNumber() {
		return this.applNumber;
	}

	public void setApplNumber(final String applNumber) {
		this.applNumber = applNumber;
	}

	public String getApplicantName() {
		return this.applicantName;
	}

	public void setApplicantName(final String applicantName) {
		this.applicantName = applicantName;
	}

	public String getEstablishmentName() {
		return this.establishmentName;
	}

	public void setEstablishmentName(final String establishmentName) {
		this.establishmentName = establishmentName;
	}

	public String getZone() {
		return this.zone;
	}

	public void setZone(final String zone) {
		this.zone = zone;
	}

	public String getDivision() {
		return this.division;
	}

	public void setDivision(final String division) {
		this.division = division;
	}

	public String getTradeName() {
		return this.tradeName;
	}

	public void setTradeName(final String tradeName) {
		this.tradeName = tradeName;
	}

	public Date getApplicationFromDate() {
		return this.applicationFromDate;
	}

	public void setApplicationFromDate(final Date applicationFromDate) {
		this.applicationFromDate = applicationFromDate;
	}

	public Date getApplicationToDate() {
		return this.applicationToDate;
	}

	public void setApplicationToDate(final Date applicationToDate) {
		this.applicationToDate = applicationToDate;
	}

	public boolean isLicenseexpired() {
		return this.licenseexpired;
	}

	public void setLicenseexpired(final boolean licenseexpired) {
		this.licenseexpired = licenseexpired;
	}

	public boolean isLicenseCancelled() {
		return this.licenseCancelled;
	}

	public void setLicenseCancelled(final boolean licenseCancelled) {
		this.licenseCancelled = licenseCancelled;
	}

	public boolean isLicenseSuspended() {
		return this.licenseSuspended;
	}

	public void setLicenseSuspended(final boolean licenseSuspended) {
		this.licenseSuspended = licenseSuspended;
	}

	public boolean isLicenseObjected() {
		return this.licenseObjected;
	}

	public void setLicenseObjected(final boolean licenseObjected) {
		this.licenseObjected = licenseObjected;
	}

	public boolean isLicenseRejected() {
		return this.licenseRejected;
	}

	public void setLicenseRejected(final boolean licenseRejected) {
		this.licenseRejected = licenseRejected;
	}

	public boolean isMotorInstalled() {
		return this.motorInstalled;
	}

	public void setMotorInstalled(final boolean motorInstalled) {
		this.motorInstalled = motorInstalled;
	}

	public BigDecimal getLicenseFeeFrom() {
		return this.licenseFeeFrom;
	}

	public void setLicenseFeeFrom(final BigDecimal licenseFeeFrom) {
		this.licenseFeeFrom = licenseFeeFrom;
	}

	public BigDecimal getLicenseFeeTo() {
		return this.licenseFeeTo;
	}

	public void setLicenseFeeTo(final BigDecimal licenseFeeTo) {
		this.licenseFeeTo = licenseFeeTo;
	}

	@Override
	public String toString() {
		final StringBuilder str = new StringBuilder();
		str.append("SearchForm={");
		str.append("  licenseNumber=").append(this.licenseNumber == null ? "null" : this.licenseNumber.toString());
		str.append("  oldLicenseNumber=").append(this.oldLicenseNumber == null ? "null" : this.oldLicenseNumber.toString());
		str.append("  applNumber=").append(this.applNumber == null ? "null" : this.applNumber.toString());
		str.append("  applicantName=").append(this.applicantName == null ? "null" : this.applicantName.toString());
		str.append("  establishmentName=").append(this.establishmentName == null ? "null" : this.establishmentName.toString());
		str.append("  zone=").append(this.zone == null ? "null" : this.zone.toString());
		str.append("  division=").append(this.division == null ? "null" : this.division.toString());
		str.append("  tradeName=").append(this.tradeName == null ? "null" : this.tradeName.toString());
		str.append("  licenseexpired=").append(this.licenseexpired);
		str.append("  licenseCancelled=").append(this.licenseCancelled);
		str.append("  licenseSuspended=").append(this.licenseSuspended);
		str.append("  licenseObjected=").append(this.licenseObjected);
		str.append("  licenseRejected=").append(this.licenseRejected);
		str.append("  motorInstalled=").append(this.motorInstalled);
		str.append("  applicationFromDate=").append(this.applicationFromDate == null ? "null" : this.applicationFromDate.toString());
		str.append("  applicationToDate=").append(this.applicationToDate == null ? "null" : this.applicationToDate.toString());
		str.append("  noticeType=").append(this.noticeType == null ? "null" : this.noticeType.toString());
		str.append("  noticeNumber=").append(this.noticeNumber == null ? "null" : this.noticeNumber.toString());
		str.append("  docNumber=").append(this.docNumber == null ? "null" : this.docNumber.toString());
		str.append("  noticeFromDate=").append(this.noticeFromDate == null ? "null" : this.noticeFromDate.toString());
		str.append("  noticeToDate=").append(this.noticeToDate == null ? "null" : this.noticeToDate.toString());
		str.append("  licenseFeeFrom=").append(this.licenseFeeFrom == null ? "null" : this.licenseFeeFrom.toString());
		str.append("  licenseFeeTo=").append(this.licenseFeeTo == null ? "null" : this.licenseFeeTo.toString());
		str.append("}");
		return str.toString();
	}
}
