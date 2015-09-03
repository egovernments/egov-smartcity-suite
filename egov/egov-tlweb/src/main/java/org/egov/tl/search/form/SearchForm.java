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
package org.egov.tl.search.form;

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

    public void setNoticeFromDate(final Date noticeFromDate) {
        this.noticeFromDate = noticeFromDate;
    }

    public Date getNoticeToDate() {
        return noticeToDate;
    }

    public void setNoticeToDate(final Date noticeToDate) {
        this.noticeToDate = noticeToDate;
    }

    public String getNoticeNumber() {
        return noticeNumber;
    }

    public void setNoticeNumber(final String noticeNumber) {
        this.noticeNumber = noticeNumber;
    }

    public String getDocNumber() {
        return docNumber;
    }

    public void setDocNumber(final String docNumber) {
        this.docNumber = docNumber;
    }

    public String getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(final String noticeType) {
        this.noticeType = noticeType;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(final String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getOldLicenseNumber() {
        return oldLicenseNumber;
    }

    public void setOldLicenseNumber(final String oldLicenseNumber) {
        this.oldLicenseNumber = oldLicenseNumber;
    }

    public String getApplNumber() {
        return applNumber;
    }

    public void setApplNumber(final String applNumber) {
        this.applNumber = applNumber;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(final String applicantName) {
        this.applicantName = applicantName;
    }

    public String getEstablishmentName() {
        return establishmentName;
    }

    public void setEstablishmentName(final String establishmentName) {
        this.establishmentName = establishmentName;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(final String zone) {
        this.zone = zone;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(final String division) {
        this.division = division;
    }

    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(final String tradeName) {
        this.tradeName = tradeName;
    }

    public Date getApplicationFromDate() {
        return applicationFromDate;
    }

    public void setApplicationFromDate(final Date applicationFromDate) {
        this.applicationFromDate = applicationFromDate;
    }

    public Date getApplicationToDate() {
        return applicationToDate;
    }

    public void setApplicationToDate(final Date applicationToDate) {
        this.applicationToDate = applicationToDate;
    }

    public boolean isLicenseexpired() {
        return licenseexpired;
    }

    public void setLicenseexpired(final boolean licenseexpired) {
        this.licenseexpired = licenseexpired;
    }

    public boolean isLicenseCancelled() {
        return licenseCancelled;
    }

    public void setLicenseCancelled(final boolean licenseCancelled) {
        this.licenseCancelled = licenseCancelled;
    }

    public boolean isLicenseSuspended() {
        return licenseSuspended;
    }

    public void setLicenseSuspended(final boolean licenseSuspended) {
        this.licenseSuspended = licenseSuspended;
    }

    public boolean isLicenseObjected() {
        return licenseObjected;
    }

    public void setLicenseObjected(final boolean licenseObjected) {
        this.licenseObjected = licenseObjected;
    }

    public boolean isLicenseRejected() {
        return licenseRejected;
    }

    public void setLicenseRejected(final boolean licenseRejected) {
        this.licenseRejected = licenseRejected;
    }

    public boolean isMotorInstalled() {
        return motorInstalled;
    }

    public void setMotorInstalled(final boolean motorInstalled) {
        this.motorInstalled = motorInstalled;
    }

    public BigDecimal getLicenseFeeFrom() {
        return licenseFeeFrom;
    }

    public void setLicenseFeeFrom(final BigDecimal licenseFeeFrom) {
        this.licenseFeeFrom = licenseFeeFrom;
    }

    public BigDecimal getLicenseFeeTo() {
        return licenseFeeTo;
    }

    public void setLicenseFeeTo(final BigDecimal licenseFeeTo) {
        this.licenseFeeTo = licenseFeeTo;
    }

    @Override
    public String toString() {
        final StringBuilder str = new StringBuilder();
        str.append("SearchForm={");
        str.append("  licenseNumber=").append(licenseNumber == null ? "null" : licenseNumber.toString());
        str.append("  oldLicenseNumber=").append(oldLicenseNumber == null ? "null" : oldLicenseNumber.toString());
        str.append("  applNumber=").append(applNumber == null ? "null" : applNumber.toString());
        str.append("  applicantName=").append(applicantName == null ? "null" : applicantName.toString());
        str.append("  establishmentName=").append(establishmentName == null ? "null" : establishmentName.toString());
        str.append("  zone=").append(zone == null ? "null" : zone.toString());
        str.append("  division=").append(division == null ? "null" : division.toString());
        str.append("  tradeName=").append(tradeName == null ? "null" : tradeName.toString());
        str.append("  licenseexpired=").append(licenseexpired);
        str.append("  licenseCancelled=").append(licenseCancelled);
        str.append("  licenseSuspended=").append(licenseSuspended);
        str.append("  licenseObjected=").append(licenseObjected);
        str.append("  licenseRejected=").append(licenseRejected);
        str.append("  motorInstalled=").append(motorInstalled);
        str.append("  applicationFromDate=").append(applicationFromDate == null ? "null" : applicationFromDate.toString());
        str.append("  applicationToDate=").append(applicationToDate == null ? "null" : applicationToDate.toString());
        str.append("  noticeType=").append(noticeType == null ? "null" : noticeType.toString());
        str.append("  noticeNumber=").append(noticeNumber == null ? "null" : noticeNumber.toString());
        str.append("  docNumber=").append(docNumber == null ? "null" : docNumber.toString());
        str.append("  noticeFromDate=").append(noticeFromDate == null ? "null" : noticeFromDate.toString());
        str.append("  noticeToDate=").append(noticeToDate == null ? "null" : noticeToDate.toString());
        str.append("  licenseFeeFrom=").append(licenseFeeFrom == null ? "null" : licenseFeeFrom.toString());
        str.append("  licenseFeeTo=").append(licenseFeeTo == null ? "null" : licenseFeeTo.toString());
        str.append("}");
        return str.toString();
    }
}
