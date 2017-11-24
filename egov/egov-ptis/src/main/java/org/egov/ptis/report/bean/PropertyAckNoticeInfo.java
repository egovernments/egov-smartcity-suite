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
    private String installmentYear;
    private String buildingClassification;
    private String natureOfUsage;
    private BigDecimal plinthArea = BigDecimal.ZERO;
    private String buildingAge;
    private BigDecimal monthlyRentalValue = BigDecimal.ZERO;
    private BigDecimal yearlyRentalValue = BigDecimal.ZERO;
    private BigDecimal taxPayableForCurrYear = BigDecimal.ZERO;
    private BigDecimal taxPayableForNewRates = BigDecimal.ZERO;
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
    private String assessmentDate;
    private BigDecimal newPropertyTax = BigDecimal.ZERO;
    private BigDecimal newLibraryCess = BigDecimal.ZERO;
    private BigDecimal newUCPenalty = BigDecimal.ZERO;
    private BigDecimal newTotalTax = BigDecimal.ZERO;
    private BigDecimal revPropertyTax = BigDecimal.ZERO;
    private BigDecimal revLibraryCess = BigDecimal.ZERO;
    private BigDecimal revUCPenalty = BigDecimal.ZERO;
    private BigDecimal revTotalTax = BigDecimal.ZERO;
    private BigDecimal new_rev_ARV = BigDecimal.ZERO;
    private BigDecimal existingPropertyTax = BigDecimal.ZERO;
    private BigDecimal existingLibraryCess = BigDecimal.ZERO;
    private BigDecimal existingUCPenalty = BigDecimal.ZERO;
    private BigDecimal existingTotalTax = BigDecimal.ZERO;
    private BigDecimal existingARV = BigDecimal.ZERO;
    private String bldngFloorNo;
    private String meesevaNo;
    private String newOwnerGuardianRelation;
    private String oldOwnerGuardianRelation;
    private String ownerTypeForReport;
    private String applicationType;
    private String transferpropertyText;
    private String transferpropertyTextEnd;
    private String approverName;
    private String noticeNumber;
    private String hearingDate;
    private String hearingTime;
    private String actualHearingDate;
    private BigDecimal extentOfSite = BigDecimal.ZERO;
    private String surveyNumber;
    private String northBoundary;
    private String southBoundary;
    private String eastBoundary;
    private String westBoundary;
    private String ownershipType;

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

    public String getAssessmentDate() {
        return assessmentDate;
    }

    public void setAssessmentDate(String assessmentDate) {
        this.assessmentDate = assessmentDate;
    }

    public String getBldngFloorNo() {
        return bldngFloorNo;
    }

    public void setBldngFloorNo(String bldngFloorNo) {
        this.bldngFloorNo = bldngFloorNo;
    }

    public String getMeesevaNo() {
        return meesevaNo;
    }

    public void setMeesevaNo(String meesevaNo) {
        this.meesevaNo = meesevaNo;
    }

    public BigDecimal getNew_rev_ARV() {
        return (new_rev_ARV.compareTo(BigDecimal.ZERO) == 0) ? null : new_rev_ARV;
    }

    public void setNew_rev_ARV(BigDecimal new_rev_ARV) {
        this.new_rev_ARV = new_rev_ARV;
    }

    public BigDecimal getNewPropertyTax() {
        return (newPropertyTax.compareTo(BigDecimal.ZERO) == 0) ? null : newPropertyTax;
    }

    public void setNewPropertyTax(BigDecimal newPropertyTax) {
        this.newPropertyTax = newPropertyTax;
    }

    public BigDecimal getNewLibraryCess() {
        return (newLibraryCess.compareTo(BigDecimal.ZERO) == 0) ? null : newLibraryCess;
    }

    public void setNewLibraryCess(BigDecimal newLibraryCess) {
        this.newLibraryCess = newLibraryCess;
    }

    public BigDecimal getNewUCPenalty() {
        return (newUCPenalty.compareTo(BigDecimal.ZERO) == 0) ? null : newUCPenalty;
    }

    public void setNewUCPenalty(BigDecimal newUCPenalty) {
        this.newUCPenalty = newUCPenalty;
    }

    public BigDecimal getNewTotalTax() {
        return (newTotalTax.compareTo(BigDecimal.ZERO) == 0) ? null : newTotalTax;
    }

    public void setNewTotalTax(BigDecimal newTotalTax) {
        this.newTotalTax = newTotalTax;
    }

    public BigDecimal getRevPropertyTax() {
        return (revPropertyTax.compareTo(BigDecimal.ZERO) == 0) ? null : revPropertyTax;
    }

    public void setRevPropertyTax(BigDecimal revPropertyTax) {
        this.revPropertyTax = revPropertyTax;
    }

    public BigDecimal getRevLibraryCess() {
        return (revLibraryCess.compareTo(BigDecimal.ZERO) == 0) ? null : revLibraryCess;
    }

    public void setRevLibraryCess(BigDecimal revLibraryCess) {
        this.revLibraryCess = revLibraryCess;
    }

    public BigDecimal getRevUCPenalty() {
        return (revUCPenalty.compareTo(BigDecimal.ZERO) == 0) ? null : revUCPenalty;
    }

    public void setRevUCPenalty(BigDecimal revUCPenalty) {
        this.revUCPenalty = revUCPenalty;
    }

    public BigDecimal getRevTotalTax() {
        return (revTotalTax.compareTo(BigDecimal.ZERO) == 0) ? null : revTotalTax;
    }

    public void setRevTotalTax(BigDecimal revTotalTax) {
        this.revTotalTax = revTotalTax;
    }

    public BigDecimal getExistingPropertyTax() {
        return (existingPropertyTax.compareTo(BigDecimal.ZERO) == 0) ? null : existingPropertyTax;
    }

    public void setExistingPropertyTax(BigDecimal existingPropertyTax) {
        this.existingPropertyTax = existingPropertyTax;
    }

    public BigDecimal getExistingLibraryCess() {
        return (existingLibraryCess.compareTo(BigDecimal.ZERO) == 0) ? null : existingLibraryCess;
    }

    public void setExistingLibraryCess(BigDecimal existingLibraryCess) {
        this.existingLibraryCess = existingLibraryCess;
    }

    public BigDecimal getExistingUCPenalty() {
        return (existingUCPenalty.compareTo(BigDecimal.ZERO) == 0) ? null : existingUCPenalty;
    }

    public void setExistingUCPenalty(BigDecimal existingUCPenalty) {
        this.existingUCPenalty = existingUCPenalty;
    }

    public BigDecimal getExistingTotalTax() {
        return (existingTotalTax.compareTo(BigDecimal.ZERO) == 0) ? null : existingTotalTax;
    }

    public void setExistingTotalTax(BigDecimal existingTotalTax) {
        this.existingTotalTax = existingTotalTax;
    }

    public BigDecimal getExistingARV() {
        return (existingARV.compareTo(BigDecimal.ZERO) == 0) ? null : existingARV;
    }

    public void setExistingARV(BigDecimal existingARV) {
        this.existingARV = existingARV;
    }

    public String getNewOwnerGuardianRelation() {
        return newOwnerGuardianRelation;
    }

    public void setNewOwnerGuardianRelation(String newOwnerGuardianRelation) {
        this.newOwnerGuardianRelation = newOwnerGuardianRelation;
    }

    public String getOldOwnerGuardianRelation() {
        return oldOwnerGuardianRelation;
    }

    public void setOldOwnerGuardianRelation(String oldOwnerGuardianRelation) {
        this.oldOwnerGuardianRelation = oldOwnerGuardianRelation;
    }

    public String getOwnerTypeForReport() {
        return ownerTypeForReport;
    }

    public void setOwnerTypeForReport(String ownerTypeForReport) {
        this.ownerTypeForReport = ownerTypeForReport;
    }

    public String getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(String applicationType) {
        this.applicationType = applicationType;
    }

    public String getTransferpropertyText() {
        return transferpropertyText;
    }

    public void setTransferpropertyText(String transferpropertyText) {
        this.transferpropertyText = transferpropertyText;
    }

    public String getTransferpropertyTextEnd() {
        return transferpropertyTextEnd;
    }

    public void setTransferpropertyTextEnd(String transferpropertyTextEnd) {
        this.transferpropertyTextEnd = transferpropertyTextEnd;
    }

    public String getApproverName() {
        return approverName;
    }

    public void setApproverName(String approverName) {
        this.approverName = approverName;
    }

    public String getNoticeNumber() {
        return noticeNumber;
    }

    public void setNoticeNumber(String noticeNumber) {
        this.noticeNumber = noticeNumber;
    }

    public String getHearingDate() {
        return hearingDate;
    }

    public void setHearingDate(String hearingDate) {
        this.hearingDate = hearingDate;
    }

    public String getHearingTime() {
        return hearingTime;
    }

    public void setHearingTime(String hearingTime) {
        this.hearingTime = hearingTime;
    }

    public String getActualHearingDate() {
		return actualHearingDate;
	}

	public void setActualHearingDate(String actualHearingDate) {
		this.actualHearingDate = actualHearingDate;
	}

	public BigDecimal getExtentOfSite() {
        return extentOfSite;
    }

    public void setExtentOfSite(BigDecimal extentOfSite) {
        this.extentOfSite = extentOfSite;
    }

    public String getSurveyNumber() {
        return surveyNumber;
    }

    public void setSurveyNumber(String surveyNumber) {
        this.surveyNumber = surveyNumber;
    }

    public String getNorthBoundary() {
        return northBoundary;
    }

    public void setNorthBoundary(String northBoundary) {
        this.northBoundary = northBoundary;
    }

    public String getSouthBoundary() {
        return southBoundary;
    }

    public void setSouthBoundary(String southBoundary) {
        this.southBoundary = southBoundary;
    }

    public String getEastBoundary() {
        return eastBoundary;
    }

    public void setEastBoundary(String eastBoundary) {
        this.eastBoundary = eastBoundary;
    }

    public String getWestBoundary() {
        return westBoundary;
    }

    public void setWestBoundary(String westBoundary) {
        this.westBoundary = westBoundary;
    }

    public String getOwnershipType() {
        return ownershipType;
    }

    public void setOwnershipType(String ownershipType) {
        this.ownershipType = ownershipType;
    }

}
