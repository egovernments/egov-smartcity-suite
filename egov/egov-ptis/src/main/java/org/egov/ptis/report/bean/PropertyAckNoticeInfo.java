package org.egov.ptis.report.bean;

import java.math.BigDecimal;
import java.util.Date;

public class PropertyAckNoticeInfo {
	private String creationReason;
	private String ownerName;
	private String ownerAddress;
	private Date applicationDate;
	private String applicationNo;
	private Date approvedDate;
	private Date noticeDueDate;
	private Date noticeDate;
	private String doorNo;
	private String streetName;
	private String wardName;
	private String areaName;
	private String localityName;
	private String zoneName;
	private BigDecimal currentTax=BigDecimal.ZERO;
	private BigDecimal generalTax=BigDecimal.ZERO;
	private BigDecimal libraryTax=BigDecimal.ZERO;
	private BigDecimal educationTax=BigDecimal.ZERO;
	private String installmentYear;
	private String buildingClassification;
	private String natureOfUsage;
	private BigDecimal plinthArea = BigDecimal.ZERO;
	private String buildingAge;
	private BigDecimal monthlyRentalValue= BigDecimal.ZERO;
	private BigDecimal yearlyRentalValue= BigDecimal.ZERO;
	private BigDecimal taxPayableForCurrYear= BigDecimal.ZERO;
	private BigDecimal taxPayableForNewRates= BigDecimal.ZERO;
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
	
	public String getCreationReason() {
		return creationReason;
	}
	public void setCreationReason(String creationReason) {
		this.creationReason = creationReason;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public String getOwnerAddress() {
		return ownerAddress;
	}
	public void setOwnerAddress(String ownerAddress) {
		this.ownerAddress = ownerAddress;
	}
	public Date getApplicationDate() {
		return applicationDate;
	}
	public void setApplicationDate(Date applicationDate) {
		this.applicationDate = applicationDate;
	}
	public String getApplicationNo() {
		return applicationNo;
	}
	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}
	public Date getApprovedDate() {
		return approvedDate;
	}
	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}
	public Date getNoticeDueDate() {
		return noticeDueDate;
	}
	public void setNoticeDueDate(Date noticeDueDate) {
		this.noticeDueDate = noticeDueDate;
	}
	public Date getNoticeDate() {
		return noticeDate;
	}
	public void setNoticeDate(Date noticeDate) {
		this.noticeDate = noticeDate;
	}
	public String getDoorNo() {
		return doorNo;
	}
	public void setDoorNo(String doorNo) {
		this.doorNo = doorNo;
	}
	public String getStreetName() {
		return streetName;
	}
	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}
	public String getWardName() {
		return wardName;
	}
	public void setWardName(String wardName) {
		this.wardName = wardName;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public String getLocalityName() {
		return localityName;
	}
	public void setLocalityName(String localityName) {
		this.localityName = localityName;
	}
	public String getZoneName() {
		return zoneName;
	}
	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}
	public BigDecimal getCurrentTax() {
		return currentTax;
	}
	public void setCurrentTax(BigDecimal currentTax) {
		this.currentTax = currentTax;
	}
	public BigDecimal getGeneralTax() {
		return generalTax;
	}
	public void setGeneralTax(BigDecimal generalTax) {
		this.generalTax = generalTax;
	}
	public BigDecimal getLibraryTax() {
		return libraryTax;
	}
	public void setLibraryTax(BigDecimal libraryTax) {
		this.libraryTax = libraryTax;
	}
	public BigDecimal getEducationTax() {
		return educationTax;
	}
	public void setEducationTax(BigDecimal educationTax) {
		this.educationTax = educationTax;
	}
	public String getInstallmentYear() {
		return installmentYear;
	}
	public void setInstallmentYear(String installmentYear) {
		this.installmentYear = installmentYear;
	}
	public String getBuildingClassification() {
		return buildingClassification;
	}
	public void setBuildingClassification(String buildingClassification) {
		this.buildingClassification = buildingClassification;
	}
	public String getNatureOfUsage() {
		return natureOfUsage;
	}
	public void setNatureOfUsage(String natureOfUsage) {
		this.natureOfUsage = natureOfUsage;
	}
	public BigDecimal getPlinthArea() {
		return plinthArea;
	}
	public void setPlinthArea(BigDecimal plinthArea) {
		this.plinthArea = plinthArea;
	}
	public String getBuildingAge() {
		return buildingAge;
	}
	public void setBuildingAge(String buildingAge) {
		this.buildingAge = buildingAge;
	}
	public BigDecimal getMonthlyRentalValue() {
		return monthlyRentalValue;
	}
	public void setMonthlyRentalValue(BigDecimal monthlyRentalValue) {
		this.monthlyRentalValue = monthlyRentalValue;
	}
	public BigDecimal getYearlyRentalValue() {
		return yearlyRentalValue;
	}
	public void setYearlyRentalValue(BigDecimal yearlyRentalValue) {
		this.yearlyRentalValue = yearlyRentalValue;
	}
	public BigDecimal getTaxPayableForCurrYear() {
		return taxPayableForCurrYear;
	}
	public void setTaxPayableForCurrYear(BigDecimal taxPayableForCurrYear) {
		this.taxPayableForCurrYear = taxPayableForCurrYear;
	}
	public BigDecimal getTaxPayableForNewRates() {
		return taxPayableForNewRates;
	}
	public void setTaxPayableForNewRates(BigDecimal taxPayableForNewRates) {
		this.taxPayableForNewRates = taxPayableForNewRates;
	}
	public BigDecimal getTotalTax() {
		return totalTax;
	}
	public void setTotalTax(BigDecimal totalTax) {
		this.totalTax = totalTax;
	}
	public String getAssessmentNo() {
		return assessmentNo;
	}
	public void setAssessmentNo(String assessmentNo) {
		this.assessmentNo = assessmentNo;
	}
	public String getUlbName() {
		return ulbName;
	}
	public void setUlbName(String ulbName) {
		this.ulbName = ulbName;
	}
	public String getApplicationName() {
		return applicationName;
	}
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	public String getNoOfDays() {
		return noOfDays;
	}
	public void setNoOfDays(String noOfDays) {
		this.noOfDays = noOfDays;
	}
	public String getMunicipalityName() {
		return municipalityName;
	}
	public void setMunicipalityName(String municipalityName) {
		this.municipalityName = municipalityName;
	}
	public String getPropertyTransfer() {
		return propertyTransfer;
	}
	public void setPropertyTransfer(String propertyTransfer) {
		this.propertyTransfer = propertyTransfer;
	}
	public String getReceivedDate() {
		return receivedDate;
	}
	public void setReceivedDate(String receivedDate) {
		this.receivedDate = receivedDate;
	}
	public String getUlbLogo() {
		return ulbLogo;
	}
	public void setUlbLogo(String ulbLogo) {
		this.ulbLogo = ulbLogo;
	}
	public String getLoggedInUsername() {
		return loggedInUsername;
	}
	public void setLoggedInUsername(String loggedInUsername) {
		this.loggedInUsername = loggedInUsername;
	}
	public String getOldOwnerName() {
		return oldOwnerName;
	}
	public void setOldOwnerName(String oldOwnerName) {
		this.oldOwnerName = oldOwnerName;
	}
	public String getOldOwnerParentName() {
		return oldOwnerParentName;
	}
	public void setOldOwnerParentName(String oldOwnerParentName) {
		this.oldOwnerParentName = oldOwnerParentName;
	}
	public String getNewOwnerName() {
		return newOwnerName;
	}
	public void setNewOwnerName(String newOwnerName) {
		this.newOwnerName = newOwnerName;
	}
	public String getNewOwnerParentName() {
		return newOwnerParentName;
	}
	public void setNewOwnerParentName(String newOwnerParentName) {
		this.newOwnerParentName = newOwnerParentName;
	}
	public String getRegDocNo() {
		return regDocNo;
	}
	public void setRegDocNo(String regDocNo) {
		this.regDocNo = regDocNo;
	}
	public String getRegDocDate() {
		return regDocDate;
	}
	public void setRegDocDate(String regDocDate) {
		this.regDocDate = regDocDate;
	}
	public String getCurrentInstallment() {
		return currentInstallment;
	}
	public void setCurrentInstallment(String currentInstallment) {
		this.currentInstallment = currentInstallment;
	}
}
