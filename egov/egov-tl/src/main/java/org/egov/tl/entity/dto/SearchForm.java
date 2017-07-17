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

import static org.egov.tl.utils.Constants.CLOSURE_LIC_APPTYPE;
import static org.egov.tl.utils.Constants.CSCOPERATOR;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.egov.infra.web.support.search.DataTableSearchRequest;
import org.egov.tl.entity.License;
import org.egov.tl.utils.Constants;
import org.joda.time.DateTime;

public class SearchForm extends DataTableSearchRequest {
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
    private String active;
    private Boolean inactive;

    public SearchForm() {
        // For form binding
    }

    public SearchForm(final License license, final String userRoles, final String ownerName, final String expiryYear) {
        setLicenseId(license.getId());
        setApplicationNumber(license.getApplicationNumber());
        setLicenseNumber(license.getLicenseNumber());
        setOldLicenseNumber(StringUtils.defaultIfEmpty(license.getOldLicenseNumber(), ""));
        setCategoryName(license.getCategory().getName());
        setSubCategoryName(license.getTradeName().getName());
        setTradeTitle(license.getNameOfEstablishment());
        setTradeOwnerName(license.getLicensee().getApplicantName());
        setMobileNo(license.getLicensee().getMobilePhoneNumber());
        setPropertyAssessmentNo(license.getAssessmentNo() != null ? license.getAssessmentNo() : "");
        setStatus(license.getStatus().getName());
        setActive(license.getIsActive() ? "YES" : "NO");
        setOwnerName(ownerName);
        setExpiryYear(expiryYear);
        setDateOfExpiry(license.getDateOfExpiry());
        addActions(license, userRoles);
    }

    private void addActions(final License license, final String userRoles) {
        final List<String> licenseActions = new ArrayList<>();
        licenseActions.add("View Trade");
        if (license.isClosureApplicable())
            licenseActions.add("Closure");
        if (!userRoles.contains(CSCOPERATOR)) {
            licenseActions.add("Generate Demand Notice");
            if (license.isLegacyWithNoState())
                licenseActions.add("Modify Legacy License");
            if (license.getStatus() != null)
                addRoleSpecificActions(license, userRoles, licenseActions);
        } else if (license.isReadyForRenewal())
            licenseActions.add("Renew License");
        setActions(licenseActions);
    }

    private void addRoleSpecificActions(final License license, final String userRoles, final List<String> licenseActions) {

        if (userRoles.contains(Constants.ROLE_BILLCOLLECTOR) && license.canCollectFee()
                && !Constants.CLOSURE_NATUREOFTASK.equals(license.getState().getNatureOfTask()))
            licenseActions.add("Collect Fees");
        else if (userRoles.contains(Constants.TL_CREATOR_ROLENAME) || userRoles.contains(Constants.TL_APPROVER_ROLENAME)) {
            if (license.isStatusActive() && !license.isLegacy())
                licenseActions.add("Print Certificate");
            if (!CLOSURE_LIC_APPTYPE.equals(license.getLicenseAppType().getName()) && license.getStatus().getStatusCode().equals(Constants.STATUS_UNDERWORKFLOW))
                licenseActions.add("Print Provisional Certificate");
            if (license.isReadyForRenewal())
                licenseActions.add("Renew License");
            final Date fromRange = new DateTime().withMonthOfYear(1).withDayOfMonth(1).toDate();
            final Date toRange = new DateTime().withMonthOfYear(4).withDayOfMonth(1).toDate();
            final Date currentDate = new Date();
            if (currentDate.after(fromRange) && currentDate.before(toRange))
                demandGenerationOption(licenseActions, license);
        }

    }

    private void demandGenerationOption(final List<String> licenseActions, final License license) {
        final Date nextYearInstallment = new DateTime().withMonthOfYear(4).withDayOfMonth(1).toDate();
        final Date currentYearInstallment = license.getLicenseDemand().getEgInstallmentMaster().getToDate();
        if (license.isNewPermanentApplication() && !license.isLegacyWithNoState() && license.getIsActive()
                && currentYearInstallment.before(nextYearInstallment))
            licenseActions.add("Generate Demand");
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

    public String getActive() {
        return active;
    }

    public void setActive(final String active) {
        this.active = active;
    }

    public Boolean getInactive() {
        return inactive;
    }

    public void setInactive(final Boolean inactive) {
        this.inactive = inactive;
    }
}