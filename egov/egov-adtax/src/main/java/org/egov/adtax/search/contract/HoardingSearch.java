/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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

package org.egov.adtax.search.contract;

import java.math.BigDecimal;
import java.util.Date;

import org.egov.adtax.entity.enums.AdvertisementStatus;

public class HoardingSearch {
    private String advertisementNumber;
    private String applicationNumber;
    private String permissionNumber;
    private String agencyName;
    private Long agency;
    private Date applicationFromDate;
    private Date applicationToDate;
    private Long category;
    private Long subCategory;
    private Long revenueInspector;
    private Long adminBoundryParent;
    private Long adminBoundry;
    private AdvertisementStatus status;
    private BigDecimal pendingDemandAmount;
    private BigDecimal penaltyAmount;
    private BigDecimal additionalTaxAmount;
    private BigDecimal totalAmount;
    private int totalHoardingInAgency;
    private String hordingIdsSearchedByAgency;
    private String permitStatus;
    private Long id;
    private String categoryName;
    private String subCategoryName;
    private String financialYear;
    private String ownerDetail;
    private boolean isLegacy;
    private String userName;
    private String pendingAction;

    public String getAdvertisementNumber() {
        return advertisementNumber;
    }

    public void setAdvertisementNumber(final String advertisementNumber) {
        this.advertisementNumber = advertisementNumber;
    }

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public void setApplicationNumber(final String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }

    public String getPermissionNumber() {
        return permissionNumber;
    }

    public void setPermissionNumber(final String permissionNumber) {
        this.permissionNumber = permissionNumber;
    }

    public String getAgencyName() {
        return agencyName;
    }

    public void setAgencyName(final String agencyName) {
        this.agencyName = agencyName;
    }

    public Long getAgency() {
        return agency;
    }

    public void setAgency(final Long agency) {
        this.agency = agency;
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

    public Long getCategory() {
        return category;
    }

    public void setCategory(final Long category) {
        this.category = category;
    }

    public Long getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(final Long subCategory) {
        this.subCategory = subCategory;
    }

    public Long getRevenueInspector() {
        return revenueInspector;
    }

    public void setRevenueInspector(final Long revenueInspector) {
        this.revenueInspector = revenueInspector;
    }

    public Long getAdminBoundryParent() {
        return adminBoundryParent;
    }

    public void setAdminBoundryParent(final Long adminBoundryParent) {
        this.adminBoundryParent = adminBoundryParent;
    }

    public Long getAdminBoundry() {
        return adminBoundry;
    }

    public void setAdminBoundry(final Long adminBoundry) {
        this.adminBoundry = adminBoundry;
    }

    public AdvertisementStatus getStatus() {
        return status;
    }

    public void setStatus(final AdvertisementStatus status) {
        this.status = status;
    }

    public BigDecimal getPendingDemandAmount() {
        return pendingDemandAmount;
    }

    public void setPendingDemandAmount(final BigDecimal pendingDemandAmount) {
        this.pendingDemandAmount = pendingDemandAmount;
    }

    public BigDecimal getPenaltyAmount() {
        return penaltyAmount;
    }

    public void setPenaltyAmount(final BigDecimal penaltyAmount) {
        this.penaltyAmount = penaltyAmount;
    }

    public int getTotalHoardingInAgency() {
        return totalHoardingInAgency;
    }

    public void setTotalHoardingInAgency(final int totalHoardingInAgency) {
        this.totalHoardingInAgency = totalHoardingInAgency;
    }

    public String getHordingIdsSearchedByAgency() {
        return hordingIdsSearchedByAgency;
    }

    public void setHordingIdsSearchedByAgency(final String hordingIdsSearchedByAgency) {
        this.hordingIdsSearchedByAgency = hordingIdsSearchedByAgency;
    }

    public String getPermitStatus() {
        return permitStatus;
    }

    public void setPermitStatus(final String permitStatus) {
        this.permitStatus = permitStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
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

    public String getFinancialYear() {
        return financialYear;
    }

    public void setFinancialYear(final String financialYear) {
        this.financialYear = financialYear;
    }

    public String getOwnerDetail() {
        return ownerDetail;
    }

    public void setOwnerDetail(final String ownerDetail) {
        this.ownerDetail = ownerDetail;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(final BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public boolean isLegacy() {
        return isLegacy;
    }

    public void setLegacy(final boolean isLegacy) {
        this.isLegacy = isLegacy;
    }

    public BigDecimal getAdditionalTaxAmount() {
        return additionalTaxAmount;
    }

    public void setAdditionalTaxAmount(final BigDecimal additionalTaxAmount) {
        this.additionalTaxAmount = additionalTaxAmount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPendingAction() {
        return pendingAction;
    }

    public void setPendingAction(String pendingAction) {
        this.pendingAction = pendingAction;
    }

}
