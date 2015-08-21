package org.egov.ptis.report.bean;

import java.math.BigDecimal;
import java.util.Date;

public class PropertyAckNoticeInfo {
    private String creationReason;
    private String ownerName;
    private String ownerAddress;
    private String applicationDate;
    private String applicationNo;
    private String approvedDate;
    private Date noticeDueDate;
    private Date noticeDate;
    private String doorNo;
    private String streetName;
    private String wardName;
    private String areaName;
    private String localityName;
    private String zoneName;
    private BigDecimal currentTax = BigDecimal.ZERO;
    private BigDecimal generalTax = BigDecimal.ZERO;
    private BigDecimal libraryTax = BigDecimal.ZERO;
    private BigDecimal educationTax = BigDecimal.ZERO;
    private String installmentYear;
    private String buildingClassification;
    private String natureOfUsage;
    private BigDecimal plinthArea = BigDecimal.ZERO;
    private String buildingAge;
    private BigDecimal monthlyRentalValue = BigDecimal.ZERO;
    private BigDecimal yearlyRentalValue = BigDecimal.ZERO;
    private BigDecimal taxPayableForCurrYear = BigDecimal.ZERO;
    private BigDecimal taxPayableForNewRates = BigDecimal.ZERO;
    private BigDecimal totalTax = BigDecimal.ZERO;
    private String assessmentNo;
    private String ulbName;
    private String applicationName;
    private String noOfDays;
    private String municipalityName;
    private String propertyTransfer;
    private String receivedDate;
    private String ulbLogo;
    private String loggedInUsername;
    private String oldOwnerName;
    private String oldOwnerParentName;
    private String newOwnerName;
    private String newOwnerParentName;
    private String regDocNo;
    private String regDocDate;
    private String currentInstallment;
    private BigDecimal amount = BigDecimal.ZERO;
    private BigDecimal rate = BigDecimal.ZERO;

    public String getCreationReason() {
        return creationReason;
    }

    public void setCreationReason(final String creationReason) {
        this.creationReason = creationReason;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(final String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerAddress() {
        return ownerAddress;
    }

    public void setOwnerAddress(final String ownerAddress) {
        this.ownerAddress = ownerAddress;
    }

    public String getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(final String applicationDate) {
        this.applicationDate = applicationDate;
    }

    public String getApplicationNo() {
        return applicationNo;
    }

    public void setApplicationNo(final String applicationNo) {
        this.applicationNo = applicationNo;
    }

    public String getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(final String approvedDate) {
        this.approvedDate = approvedDate;
    }

    public Date getNoticeDueDate() {
        return noticeDueDate;
    }

    public void setNoticeDueDate(final Date noticeDueDate) {
        this.noticeDueDate = noticeDueDate;
    }

    public Date getNoticeDate() {
        return noticeDate;
    }

    public void setNoticeDate(final Date noticeDate) {
        this.noticeDate = noticeDate;
    }

    public String getDoorNo() {
        return doorNo;
    }

    public void setDoorNo(final String doorNo) {
        this.doorNo = doorNo;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(final String streetName) {
        this.streetName = streetName;
    }

    public String getWardName() {
        return wardName;
    }

    public void setWardName(final String wardName) {
        this.wardName = wardName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(final String areaName) {
        this.areaName = areaName;
    }

    public String getLocalityName() {
        return localityName;
    }

    public void setLocalityName(final String localityName) {
        this.localityName = localityName;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(final String zoneName) {
        this.zoneName = zoneName;
    }

    public BigDecimal getCurrentTax() {
        return currentTax;
    }

    public void setCurrentTax(final BigDecimal currentTax) {
        this.currentTax = currentTax;
    }

    public BigDecimal getGeneralTax() {
        return generalTax;
    }

    public void setGeneralTax(final BigDecimal generalTax) {
        this.generalTax = generalTax;
    }

    public BigDecimal getLibraryTax() {
        return libraryTax;
    }

    public void setLibraryTax(final BigDecimal libraryTax) {
        this.libraryTax = libraryTax;
    }

    public BigDecimal getEducationTax() {
        return educationTax;
    }

    public void setEducationTax(final BigDecimal educationTax) {
        this.educationTax = educationTax;
    }

    public String getInstallmentYear() {
        return installmentYear;
    }

    public void setInstallmentYear(final String installmentYear) {
        this.installmentYear = installmentYear;
    }

    public String getBuildingClassification() {
        return buildingClassification;
    }

    public void setBuildingClassification(final String buildingClassification) {
        this.buildingClassification = buildingClassification;
    }

    public String getNatureOfUsage() {
        return natureOfUsage;
    }

    public void setNatureOfUsage(final String natureOfUsage) {
        this.natureOfUsage = natureOfUsage;
    }

    public BigDecimal getPlinthArea() {
        return plinthArea;
    }

    public void setPlinthArea(final BigDecimal plinthArea) {
        this.plinthArea = plinthArea;
    }

    public String getBuildingAge() {
        return buildingAge;
    }

    public void setBuildingAge(final String buildingAge) {
        this.buildingAge = buildingAge;
    }

    public BigDecimal getMonthlyRentalValue() {
        return monthlyRentalValue;
    }

    public void setMonthlyRentalValue(final BigDecimal monthlyRentalValue) {
        this.monthlyRentalValue = monthlyRentalValue;
    }

    public BigDecimal getYearlyRentalValue() {
        return yearlyRentalValue;
    }

    public void setYearlyRentalValue(final BigDecimal yearlyRentalValue) {
        this.yearlyRentalValue = yearlyRentalValue;
    }

    public BigDecimal getTaxPayableForCurrYear() {
        return taxPayableForCurrYear;
    }

    public void setTaxPayableForCurrYear(final BigDecimal taxPayableForCurrYear) {
        this.taxPayableForCurrYear = taxPayableForCurrYear;
    }

    public BigDecimal getTaxPayableForNewRates() {
        return taxPayableForNewRates;
    }

    public void setTaxPayableForNewRates(final BigDecimal taxPayableForNewRates) {
        this.taxPayableForNewRates = taxPayableForNewRates;
    }

    public BigDecimal getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(final BigDecimal totalTax) {
        this.totalTax = totalTax;
    }

    public String getAssessmentNo() {
        return assessmentNo;
    }

    public void setAssessmentNo(final String assessmentNo) {
        this.assessmentNo = assessmentNo;
    }

    public String getUlbName() {
        return ulbName;
    }

    public void setUlbName(final String ulbName) {
        this.ulbName = ulbName;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(final String applicationName) {
        this.applicationName = applicationName;
    }

    public String getNoOfDays() {
        return noOfDays;
    }

    public void setNoOfDays(final String noOfDays) {
        this.noOfDays = noOfDays;
    }

    public String getMunicipalityName() {
        return municipalityName;
    }

    public void setMunicipalityName(final String municipalityName) {
        this.municipalityName = municipalityName;
    }

    public String getPropertyTransfer() {
        return propertyTransfer;
    }

    public void setPropertyTransfer(final String propertyTransfer) {
        this.propertyTransfer = propertyTransfer;
    }

    public String getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(final String receivedDate) {
        this.receivedDate = receivedDate;
    }

    public String getUlbLogo() {
        return ulbLogo;
    }

    public void setUlbLogo(final String ulbLogo) {
        this.ulbLogo = ulbLogo;
    }

    public String getLoggedInUsername() {
        return loggedInUsername;
    }

    public void setLoggedInUsername(final String loggedInUsername) {
        this.loggedInUsername = loggedInUsername;
    }

    public String getOldOwnerName() {
        return oldOwnerName;
    }

    public void setOldOwnerName(final String oldOwnerName) {
        this.oldOwnerName = oldOwnerName;
    }

    public String getOldOwnerParentName() {
        return oldOwnerParentName;
    }

    public void setOldOwnerParentName(final String oldOwnerParentName) {
        this.oldOwnerParentName = oldOwnerParentName;
    }

    public String getNewOwnerName() {
        return newOwnerName;
    }

    public void setNewOwnerName(final String newOwnerName) {
        this.newOwnerName = newOwnerName;
    }

    public String getNewOwnerParentName() {
        return newOwnerParentName;
    }

    public void setNewOwnerParentName(final String newOwnerParentName) {
        this.newOwnerParentName = newOwnerParentName;
    }

    public String getRegDocNo() {
        return regDocNo;
    }

    public void setRegDocNo(final String regDocNo) {
        this.regDocNo = regDocNo;
    }

    public String getRegDocDate() {
        return regDocDate;
    }

    public void setRegDocDate(final String regDocDate) {
        this.regDocDate = regDocDate;
    }

    public String getCurrentInstallment() {
        return currentInstallment;
    }

    public void setCurrentInstallment(final String currentInstallment) {
        this.currentInstallment = currentInstallment;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(final BigDecimal amount) {
        this.amount = amount;
    }

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}
    
}
