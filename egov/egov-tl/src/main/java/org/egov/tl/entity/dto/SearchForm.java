/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 */

package org.egov.tl.entity.dto;

import org.egov.tl.entity.License;
import org.egov.tl.utils.Constants;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SearchForm {
    private Long licenseId;
    private String applicationNumber;
    private String licenseNumber;
    private String oldLicenseNumber;
    private String categoryName;
    private String subCategoryName;
    private String tradeTitle;
    private String tradeOwnerName;
    private String propertyAssessmentNo;
    private String mobileNo;
    private String status;
    private String ownerName;
    private String expiryYear;
    private Long categoryId;
    private Long subCategoryId;
    private Long statusId;
    private Date dateOfExpiry;
    private List<String> actions;

    public SearchForm() {
        // For form binding
    }

    public SearchForm(final License license, final String userRoles, final String ownerName, final String expiryYear) {
        setLicenseId(license.getId());
        setApplicationNumber(license.getApplicationNumber());
        setLicenseNumber(license.getLicenseNumber());
        setOldLicenseNumber(license.getOldLicenseNumber());
        setCategoryName(license.getCategory().getName());
        setSubCategoryName(license.getTradeName().getName());
        setTradeTitle(license.getNameOfEstablishment());
        setTradeOwnerName(license.getLicensee().getApplicantName());
        setMobileNo(license.getLicensee().getMobilePhoneNumber());
        setPropertyAssessmentNo(license.getAssessmentNo() != null ? license.getAssessmentNo() : "");
        setStatus(license.getStatus().getName());
        setOwnerName(ownerName);
        setExpiryYear(expiryYear);
        setDateOfExpiry(license.getDateOfExpiry());
        addActions(license, userRoles);
    }

    private void addActions(final License license, final String userRoles) {
        final List<String> licenseActions = new ArrayList<>();
        licenseActions.add("View Trade");
        licenseActions.add("Generate Demand Notice");
        if (license.isLegacy() && !license.hasState())
            licenseActions.add("Modify Legacy License");
        if (license.getStatus() != null) {
            addRoleSpecificActions(license, userRoles, licenseActions);
            if (license.isStatusActive())
                licenseActions.add("Closure");
        }
        setActions(licenseActions);
    }

    private void addRoleSpecificActions(License license, String userRoles, List<String> licenseActions) {

        if (userRoles.contains(Constants.ROLE_BILLCOLLECTOR) && license.canCollectFee())
            licenseActions.add("Collect Fees");
        else if (userRoles.contains(Constants.TL_CREATOR_ROLENAME) || userRoles.contains(Constants.TL_APPROVER_ROLENAME)) {
            if (license.isStatusActive())
                licenseActions.add("Print Certificate");
            if (license.getStatus().getStatusCode().equals(Constants.STATUS_UNDERWORKFLOW))
                licenseActions.add("Print Provisional Certificate");
            if (!license.isPaid() && !license.getLicenseAppType().getName().equals(Constants.RENEWAL_LIC_APPTYPE)
                    && license.isStatusActive())
                licenseActions.add("Renew License");
        }

    }

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public void setApplicationNumber(final String applicationNumber) {
        this.applicationNumber = applicationNumber;
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(final String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(final String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public String getTradeTitle() {
        return tradeTitle;
    }

    public void setTradeTitle(final String tradeTitle) {
        this.tradeTitle = tradeTitle;
    }

    public String getTradeOwnerName() {
        return tradeOwnerName;
    }

    public void setTradeOwnerName(final String tradeOwnerName) {
        this.tradeOwnerName = tradeOwnerName;
    }

    public String getPropertyAssessmentNo() {
        return propertyAssessmentNo;
    }

    public void setPropertyAssessmentNo(final String propertyAssessmentNo) {
        this.propertyAssessmentNo = propertyAssessmentNo;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(final String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public Long getLicenseId() {
        return licenseId;
    }

    public void setLicenseId(final Long licenseId) {
        this.licenseId = licenseId;
    }

    public List<String> getActions() {
        return actions;
    }

    public void setActions(final List<String> actions) {
        this.actions = actions;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(final String ownerName) {
        this.ownerName = ownerName;
    }

    public String getExpiryYear() {
        return expiryYear;
    }

    public void setExpiryYear(final String expiryYear) {
        this.expiryYear = expiryYear;
    }

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(final Long statusId) {
        this.statusId = statusId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(final Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(final Long subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public Date getDateOfExpiry() {
        return dateOfExpiry;
    }

    public void setDateOfExpiry(final Date dateOfExpiry) {
        this.dateOfExpiry = dateOfExpiry;
    }
}