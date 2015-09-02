/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency, 
 *     accountability and the service delivery of the government  organizations.
 *  
 *      Copyright (C) <2015>  eGovernments Foundation
 *  
 *      The updated version of eGov suite of products as by eGovernments Foundation 
 *      is available at http://www.egovernments.org
 *  
 *      This program is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      any later version.
 *  
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *  
 *      You should have received a copy of the GNU General Public License
 *      along with this program. If not, see http://www.gnu.org/licenses/ or 
 *      http://www.gnu.org/licenses/gpl.html .
 *  
 *      In addition to the terms of the GPL license to be adhered to in using this
 *      program, the following additional terms are to be complied with:
 *  
 *  	1) All versions of this program, verbatim or modified must carry this 
 *  	   Legal Notice.
 *  
 *  	2) Any misrepresentation of the origin of the material is prohibited. It 
 *  	   is required that all modified versions of this material be marked in 
 *  	   reasonable ways as different from the original version.
 *  
 *  	3) This license does not grant any rights to any user of the program 
 *  	   with regards to rights under trademark law for use of the trade names 
 *  	   or trademarks of eGovernments Foundation.
 *  
 *    In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.tradelicense.search.form;

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
		return noticeFromDate;
	}

	public void setNoticeFromDate(Date noticeFromDate) {
		this.noticeFromDate = noticeFromDate;
	}

	public Date getNoticeToDate() {
		return noticeToDate;
	}

	public void setNoticeToDate(Date noticeToDate) {
		this.noticeToDate = noticeToDate;
	}

	public String getNoticeNumber() {
		return noticeNumber;
	}

	public void setNoticeNumber(String noticeNumber) {
		this.noticeNumber = noticeNumber;
	}

	public String getDocNumber() {
		return docNumber;
	}

	public void setDocNumber(String docNumber) {
		this.docNumber = docNumber;
	}

	public String getNoticeType() {
		return noticeType;
	}

	public void setNoticeType(String noticeType) {
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
