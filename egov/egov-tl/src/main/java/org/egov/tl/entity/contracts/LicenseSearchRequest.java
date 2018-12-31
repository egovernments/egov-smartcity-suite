/*
 * eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
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
 *           Further, all user interfaces, including but not limited to citizen facing interfaces,
 *           Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *           derived works should carry eGovernments Foundation logo on the top right corner.
 *
 * 	       For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 * 	       For any further queries on attribution, including queries on brand guidelines,
 *           please contact contact@egovernments.org
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

package org.egov.tl.entity.contracts;

import org.egov.infra.admin.master.entity.User;
import org.egov.infra.persistence.entity.enums.UserType;
import org.egov.infra.web.support.search.DataTableSearchRequest;
import org.egov.tl.entity.TradeLicense;
import org.egov.tl.utils.Constants;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.egov.infra.utils.StringUtils.toYesOrNo;
import static org.egov.tl.utils.Constants.CLOSURE_APPTYPE_CODE;
import static org.egov.tl.utils.Constants.CSCOPERATOR;
import static org.egov.tl.utils.Constants.TL_APPROVER_ROLENAME;
import static org.egov.tl.utils.Constants.TL_CREATOR_ROLENAME;

public class LicenseSearchRequest extends DataTableSearchRequest {
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
    private List<String> actions;
    private String active;
    private Boolean inactive;
    private Long applicationTypeId;
    private Long natureOfBusinessId;
    private Date expiryDate;
    private String uid;

    public LicenseSearchRequest() {
        // For form binding
    }

    public LicenseSearchRequest(TradeLicense license, User user, String ownerName, String... feeCollectorRoles) {
        setLicenseId(license.getId());
        setUid(license.getUid());
        setApplicationNumber(license.getApplicationNumber());
        setLicenseNumber(license.getLicenseNumber());
        setCategoryName(license.getCategory().getName());
        setSubCategoryName(license.getTradeName().getName());
        setTradeTitle(license.getNameOfEstablishment());
        setTradeOwnerName(license.getLicensee().getApplicantName());
        setMobileNo(license.getLicensee().getMobilePhoneNumber());
        setStatus(license.getStatus().getName());
        setActive(toYesOrNo(license.getIsActive()));
        setOldLicenseNumber(license.getOldLicenseNumber());
        setExpiryDate(license.getDateOfExpiry());
        setOwnerName(ownerName);
        setApplicationTypeId(license.getLicenseAppType().getId());
        setNatureOfBusinessId(license.getNatureOfBusiness().getId());
        addActions(license, user, feeCollectorRoles);
    }

    private void addActions(TradeLicense license, User user, String... feeCollectorRoles) {
        List<String> licenseActions = new ArrayList<>();
        licenseActions.add("View Trade");
        licenseActions.add("View DCB");
        if (license.transitionInitialized())
            licenseActions.add("Print Acknowledgment");
        if (license.isClosureApplicable())
            licenseActions.add("Closure");
        if (license.isClosed())
            licenseActions.add("Closure Endorsement Notice");
        if (user.hasAnyRole(feeCollectorRoles) && (license.canCollectLicenseFee() || license.canCollectFee()))
            licenseActions.add("Collect Fees");
        if (user.hasRole(CSCOPERATOR))
            addPrintCertificatesOptions(license, licenseActions);
        if (user.getType().equals(UserType.EMPLOYEE)) {
            if (!license.isAppTypeClosure() && license.getIsActive() && license.getTotalBalance().signum() > 0)
                licenseActions.add("Generate Demand Notice");
            if (license.isLegacyWithNoState())
                licenseActions.add("Modify Legacy License");
            if (license.getStatus() != null)
                addRoleSpecificActions(license, user, licenseActions);
        } else if (license.isReadyForRenewal()) {
            licenseActions.add("Renew License");
        }
        setActions(licenseActions);
    }

    private void addRoleSpecificActions(TradeLicense license, User user, List<String> licenseActions) {
        if (user.hasAnyRole(TL_CREATOR_ROLENAME, TL_APPROVER_ROLENAME)) {
            addPrintCertificatesOptions(license, licenseActions);
            if (license.isReadyForRenewal())
                licenseActions.add("Renew License");
            Date fromRange = new DateTime().withMonthOfYear(1).withDayOfMonth(1).toDate();
            Date toRange = new DateTime().withMonthOfYear(4).withDayOfMonth(1).toDate();
            Date currentDate = new Date();
            if (currentDate.after(fromRange) && currentDate.before(toRange))
                demandGenerationOption(licenseActions, license);
        }
    }

    private void addPrintCertificatesOptions(TradeLicense license, List<String> licenseActions) {
        if (license.isStatusActive() && !license.isLegacy())
            licenseActions.add("Print Certificate");
        if (!CLOSURE_APPTYPE_CODE.equals(license.getLicenseAppType().getCode())
                && license.getStatus().getStatusCode().equals(Constants.STATUS_UNDERWORKFLOW))
            licenseActions.add("Print Provisional Certificate");
    }

    private void demandGenerationOption(List<String> licenseActions, TradeLicense license) {
        Date nextYearInstallment = new DateTime().withMonthOfYear(4).withDayOfMonth(1).toDate();
        Date currentYearInstallment = license.getDemand().getEgInstallmentMaster().getToDate();
        if (license.isNewPermanentApplication() && !license.isLegacyWithNoState() && license.getIsActive()
                && currentYearInstallment.before(nextYearInstallment))
            licenseActions.add("Generate Demand");
    }

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public void setApplicationNumber(String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getOldLicenseNumber() {
        return oldLicenseNumber;
    }

    public void setOldLicenseNumber(String oldLicenseNumber) {
        this.oldLicenseNumber = oldLicenseNumber;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public String getTradeTitle() {
        return tradeTitle;
    }

    public void setTradeTitle(String tradeTitle) {
        this.tradeTitle = tradeTitle;
    }

    public String getTradeOwnerName() {
        return tradeOwnerName;
    }

    public void setTradeOwnerName(String tradeOwnerName) {
        this.tradeOwnerName = tradeOwnerName;
    }

    public String getPropertyAssessmentNo() {
        return propertyAssessmentNo;
    }

    public void setPropertyAssessmentNo(String propertyAssessmentNo) {
        this.propertyAssessmentNo = propertyAssessmentNo;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public Long getLicenseId() {
        return licenseId;
    }

    public void setLicenseId(Long licenseId) {
        this.licenseId = licenseId;
    }

    public List<String> getActions() {
        return actions;
    }

    public void setActions(List<String> actions) {
        this.actions = actions;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getExpiryYear() {
        return expiryYear;
    }

    public void setExpiryYear(String expiryYear) {
        this.expiryYear = expiryYear;
    }

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(Long subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public Boolean getInactive() {
        return inactive;
    }

    public void setInactive(Boolean inactive) {
        this.inactive = inactive;
    }

    public Long getApplicationTypeId() {
        return applicationTypeId;
    }

    public void setApplicationTypeId(Long applicationTypeId) {
        this.applicationTypeId = applicationTypeId;
    }

    public Long getNatureOfBusinessId() {
        return natureOfBusinessId;
    }

    public void setNatureOfBusinessId(Long natureOfBusinessId) {
        this.natureOfBusinessId = natureOfBusinessId;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(final Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}