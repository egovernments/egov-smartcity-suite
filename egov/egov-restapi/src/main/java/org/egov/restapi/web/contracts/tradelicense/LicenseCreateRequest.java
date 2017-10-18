/*
 * eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) <2017>  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 * 	Further, all user interfaces, including but not limited to citizen facing interfaces,
 *         Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *         derived works should carry eGovernments Foundation logo on the top right corner.
 *
 * 	For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 * 	For any further queries on attribution, including queries on brand guidelines,
 *         please contact contact@egovernments.org
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.restapi.web.contracts.tradelicense;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.Date;

public class LicenseCreateRequest {

    private static final String ALPHABETS = "^[A-Za-z]{1,256}$";
    private static final String EMAIL = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    private static final String ALPHANUMERIC_WITHLENGTH = "^[0-9a-zA-Z-& :,/.()@]{1,256}$";

    @NotBlank(message = "ULB code must be present")
    private String ulbCode;

    @Pattern(regexp = ALPHABETS, message = "Invalid Applicant Name")
    private String applicantName;

    @Pattern(regexp = ALPHABETS, message = "Invalid Father/Spouse Name")
    private String fatherOrSpouseName;

    @NotBlank(message = "Provide Mobile Number")
    @Pattern(regexp = "^[0-9]{10,10}$", message = "Invalid Mobile Number")
    private String mobilePhoneNumber;

    @Pattern(regexp = "^[0-9]{12,12}$", message = "Invalid Aadhaar Number")
    private String aadhaarNumber;

    @NotBlank(message = "Provide EmailId")
    @Email(regexp = EMAIL, message = "Invalid Email")
    private String emailId;

    @Pattern(regexp = ALPHANUMERIC_WITHLENGTH, message = "Invalid Trade Title")
    private String tradeTitle;

    @Pattern(regexp = "^[A-Za-z]{1,120}$", message = "Invalid Ownership Type")
    private String ownershipType;

    @Pattern(regexp = "^[0-9]{0,64}$", message = "Invalid Assessment Number")
    private String assessmentNo;

    @NotNull(message = "Invalid Commencement Date")
    @JsonFormat(pattern = "dd-MM-yyyy", shape = JsonFormat.Shape.STRING)
    private Date commencementDate;

    @NotNull(message = "Invalid Trade Measure")
    private BigDecimal tradeMeasure;

    @NotNull(message = "Invalid Locality")
    private Long boundary;

    @NotNull(message = "Invalid Ward")
    private Long parentBoundary;

    @NotNull(message = "Invalid Nature Of Business")
    private Long natureOfBusiness;

    @NotBlank(message = "Invalid Subcategory")
    private String subCategory;

    @NotBlank(message = "Invalid Category")
    private String category;

    @Length(min = 1, max = 256, message = "Invalid Trade Address")
    private String tradeAddress;

    @Length(min = 1, max = 256, message = "Invalid Licensee Address")
    private String licenseeAddress;

    @Length(max = 512)
    private String remarks;

    @JsonFormat(pattern = "dd-MM-yyyy", shape = JsonFormat.Shape.STRING)
    private Date agreementDate;

    @Length(max = 50, message = "Invalid Agreement Document No.")
    private String agreementDocNo;

    public String getUlbCode() {
        return ulbCode;
    }

    public void setUlbCode(String ulbCode) {
        this.ulbCode = ulbCode;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getFatherOrSpouseName() {
        return fatherOrSpouseName;
    }

    public void setFatherOrSpouseName(String fatherOrSpouseName) {
        this.fatherOrSpouseName = fatherOrSpouseName;
    }

    public String getMobilePhoneNumber() {
        return mobilePhoneNumber;
    }

    public void setMobilePhoneNumber(String mobilePhoneNumber) {
        this.mobilePhoneNumber = mobilePhoneNumber;
    }

    public String getAadhaarNumber() {
        return aadhaarNumber;
    }

    public void setAadhaarNumber(String aadhaarNumber) {
        this.aadhaarNumber = aadhaarNumber;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getTradeTitle() {
        return tradeTitle;
    }

    public void setTradeTitle(String tradeTitle) {
        this.tradeTitle = tradeTitle;
    }

    public String getOwnershipType() {
        return ownershipType;
    }

    public void setOwnershipType(String ownershipType) {
        this.ownershipType = ownershipType;
    }

    public String getAssessmentNo() {
        return assessmentNo;
    }

    public void setAssessmentNo(String assessmentNo) {
        this.assessmentNo = assessmentNo;
    }

    public Date getCommencementDate() {
        return commencementDate;
    }

    public void setCommencementDate(Date commencementDate) {
        this.commencementDate = commencementDate;
    }

    public BigDecimal getTradeMeasure() {
        return tradeMeasure;
    }

    public void setTradeMeasure(BigDecimal tradeMeasure) {
        this.tradeMeasure = tradeMeasure;
    }

    public Long getBoundary() {
        return boundary;
    }

    public void setBoundary(Long boundary) {
        this.boundary = boundary;
    }

    public Long getParentBoundary() {
        return parentBoundary;
    }

    public void setParentBoundary(Long parentBoundary) {
        this.parentBoundary = parentBoundary;
    }

    public Long getNatureOfBusiness() {
        return natureOfBusiness;
    }

    public void setNatureOfBusiness(Long natureOfBusiness) {
        this.natureOfBusiness = natureOfBusiness;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTradeAddress() {
        return tradeAddress;
    }

    public void setTradeAddress(String tradeAddress) {
        this.tradeAddress = tradeAddress;
    }

    public String getLicenseeAddress() {
        return licenseeAddress;
    }

    public void setLicenseeAddress(String licenseeAddress) {
        this.licenseeAddress = licenseeAddress;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Date getAgreementDate() {
        return agreementDate;
    }

    public void setAgreementDate(Date agreementDate) {
        this.agreementDate = agreementDate;
    }

    public String getAgreementDocNo() {
        return agreementDocNo;
    }

    public void setAgreementDocNo(String agreementDocNo) {
        this.agreementDocNo = agreementDocNo;
    }
}

