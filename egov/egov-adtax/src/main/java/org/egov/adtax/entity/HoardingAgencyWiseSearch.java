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
