/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */

package org.egov.restapi.web.contracts.tradelicense;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.egov.tl.entity.enums.OwnershipType;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.Date;

import static org.egov.infra.validation.constants.ValidationRegex.ALPHABETS_WITH_SPACE;
import static org.egov.infra.validation.constants.ValidationRegex.EMAIL;
import static org.egov.infra.validation.constants.ValidationRegex.NUMERIC;

public class LicenseCreateRequest {

    @Pattern(regexp = ALPHABETS_WITH_SPACE, message = "Invalid Applicant Name")
    @NotBlank(message = "Applicant Name is required")
    @Length(max = 256, message = "Applicant Name accepts maximum 256 characters")
    private String applicantName;

    @Pattern(regexp = ALPHABETS_WITH_SPACE, message = "Invalid Father/Spouse Name")
    @NotBlank(message = "Father/Spouse Name is required")
    @Length(max = 256, message = "Father/Spouse Name accepts maximum 256 characters")
    private String fatherOrSpouseName;

    @NotBlank(message = "Mobile Number is required")
    @Pattern(regexp = NUMERIC, message = "Invalid Mobile Number")
    private String mobilePhoneNumber;

    @Pattern(regexp = NUMERIC, message = "Invalid Aadhaar Number")
    @Length(max = 12, message = "Aadhaar Number must be 12 digit")
    private String aadhaarNumber;

    @NotBlank(message = "Email is required")
    @Email(regexp = EMAIL, message = "Invalid Email")
    @Length(max = 64, message = "Email accepts maximum 64 characters")
    private String emailId;

    @SafeHtml(message = "Invalid Trade Title")
    @NotBlank(message = "Trade Title is required")
    @Length(max = 256, message = "Trade Title accepts maximum 256 characters")
    private String tradeTitle;

    @NotNull(message = "Ownership Type is required")
    private OwnershipType ownershipType;

    @SafeHtml(message = "Invalid Assessment Number")
    @Length(max = 64, message = "Assessment Number accepts maximum 64 characters")
    private String assessmentNo;

    @NotNull(message = "Commencement Date is required")
    @JsonFormat(pattern = "dd-MM-yyyy", shape = JsonFormat.Shape.STRING)
    private Date commencementDate;

    @NotNull(message = "Trade Measure is required")
    @Min(value = 1, message = "Trade Measure must be greater than or equals to 1")
    private BigDecimal tradeMeasure;

    @NotNull(message = "Boundary is required")
    @Min(value = 1, message = "Invalid Boundary")
    private Long boundary;

    @NotNull(message = "Parent Boundary is required")
    @Min(value = 1, message = "Invalid Parent Boundary")
    private Long parentBoundary;

    @NotNull(message = "Nature Of Business is required")
    @Min(value = 1, message = "Invalid Nature of Business")
    private Long natureOfBusiness;

    @NotBlank(message = "Subcategory is required")
    @SafeHtml(message = "Invalid Subcategory")
    private String subCategory;

    @NotBlank(message = "Category is required")
    @SafeHtml(message = "Invalid Category")
    private String category;

    @NotBlank(message = "Trade Address is required")
    @SafeHtml(message = "Invalid Trade Address")
    @Length(max = 250, message = "Trade Address accepts maximum 250 characters")
    private String tradeAddress;

    @NotBlank(message = "Licensee Address is required")
    @SafeHtml(message = "Invalid Licensee Address")
    @Length(max = 250, message = "Licensee Address accepts maximum 250 characters")
    private String licenseeAddress;

    @Length(max = 512, message = "Remarks accepts maximum 512 characters")
    @SafeHtml(message = "Invalid Remarks")
    private String remarks;

    @JsonFormat(pattern = "dd-MM-yyyy", shape = JsonFormat.Shape.STRING)
    private Date agreementDate;

    @Length(max = 50, message = "Agreement Doc No. accepts maximum 50 characters")
    @SafeHtml(message = "Invalid Agreement Doc No.")
    private String agreementDocNo;

    @NotBlank(message = "Source is required")
    @SafeHtml(message = "Invalid Source")
    private String source;

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

    public OwnershipType getOwnershipType() {
        return ownershipType;
    }

    public void setOwnershipType(OwnershipType ownershipType) {
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}

