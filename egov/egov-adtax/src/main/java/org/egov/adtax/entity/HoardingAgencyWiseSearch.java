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

package org.egov.adtax.entity;

import java.math.BigDecimal;

public class HoardingAgencyWiseSearch {
    
    private Long agency;
    private String agencyName;
    private BigDecimal pendingDemandAmount;
    private BigDecimal penaltyAmount;
    private int totalHoardingInAgency;
    private BigDecimal collectedAmount;
    private String categoryName;
    private String subCategoryName;
    private String financialYear;
    private String advertisementNumber;
    private String hordingIdsSearchedByAgency;
    private BigDecimal totalDemand;
    private BigDecimal pendingAmount;
    private Long category;
    private Long subCategory;
    private Long adminBoundaryParent;
    private Long adminBoundary;
    private String ownerDetail;

    
    
    public String getOwnerDetail() {
        return ownerDetail;
    }
    public void setOwnerDetail(String ownerDetail) {
        this.ownerDetail = ownerDetail;
    }
    public String getAgencyName() {
        return agencyName;
    }
    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }
    public BigDecimal getPendingDemandAmount() {
        return pendingDemandAmount;
    }
    public void setPendingDemandAmount(BigDecimal pendingDemandAmount) {
        this.pendingDemandAmount = pendingDemandAmount;
    }
    public BigDecimal getPenaltyAmount() {
        return penaltyAmount;
    }
    public void setPenaltyAmount(BigDecimal penaltyAmount) {
        this.penaltyAmount = penaltyAmount;
    }
    public int getTotalHoardingInAgency() {
        return totalHoardingInAgency;
    }
    public void setTotalHoardingInAgency(int totalHoardingInAgency) {
        this.totalHoardingInAgency = totalHoardingInAgency;
    }
    public BigDecimal getCollectedAmount() {
        return collectedAmount;
    }
    public void setCollectedAmount(BigDecimal collectedAmount) {
        this.collectedAmount = collectedAmount;
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
    public String getFinancialYear() {
        return financialYear;
    }
    public void setFinancialYear(String financialYear) {
        this.financialYear = financialYear;
    }
    public String getAdvertisementNumber() {
        return advertisementNumber;
    }
    public void setAdvertisementNumber(String advertisementNumber) {
        this.advertisementNumber = advertisementNumber;
    }
    public String getHordingIdsSearchedByAgency() {
        return hordingIdsSearchedByAgency;
    }
    public void setHordingIdsSearchedByAgency(String hordingIdsSearchedByAgency) {
        this.hordingIdsSearchedByAgency = hordingIdsSearchedByAgency;
    }
    public Long getAgency() {
        return agency;
    }
    public void setAgency(Long agency) {
        this.agency = agency;
    }
    public BigDecimal getTotalDemand() {
        return totalDemand;
    }
    public void setTotalDemand(BigDecimal totalDemand) {
        this.totalDemand = totalDemand;
    }
    public BigDecimal getPendingAmount() {
        return pendingAmount;
    }
    public void setPendingAmount(BigDecimal pendingAmount) {
        this.pendingAmount = pendingAmount;
    }
    public Long getCategory() {
        return category;
    }
    public void setCategory(Long category) {
        this.category = category;
    }
    public Long getSubCategory() {
        return subCategory;
    }
    public void setSubCategory(Long subCategory) {
        this.subCategory = subCategory;
    }
    public Long getAdminBoundaryParent() {
        return adminBoundaryParent;
    }
    public void setAdminBoundaryParent(Long adminBoundaryParent) {
        this.adminBoundaryParent = adminBoundaryParent;
    }
    public Long getAdminBoundary() {
        return adminBoundary;
    }
    public void setAdminBoundary(Long adminBoundary) {
        this.adminBoundary = adminBoundary;
    }
    
  

}
