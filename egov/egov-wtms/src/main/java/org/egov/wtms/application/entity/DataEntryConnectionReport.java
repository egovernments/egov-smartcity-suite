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
package org.egov.wtms.application.entity;

import java.math.BigDecimal;
import java.util.Date;

public class DataEntryConnectionReport {

    private String zone;
    private String block;
    private String propertyType;
    private String applicationType;
    private String connectionType;
    private String usageType;
    private String category;
    private String pipeSizeInInch;
    private String hscNo;
    private String assessmentNo;
    private String ulbName;
    private String ownerName;
    private String revenueWard;
    private String houseNumber;
    private String locality;
    private String mobileNumber;
    private String address;
    private String aadharNumber;
    private Integer noOfPersons;
    private Integer noOfRooms;
    private double donationCharges;
    private double monthlyFee;
    private double waterTaxDue;
    private double propertyTaxDue;
    private BigDecimal sumpCapacity;
    private String email;
    private String waterSource;
    private Date connectionDate;

    public String getZone() {
        return zone;
    }

    public void setZone(final String zone) {
        this.zone = zone;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(final String propertyType) {
        this.propertyType = propertyType;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(final String block) {
        this.block = block;
    }

    public String getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(final String applicationType) {
        this.applicationType = applicationType;
    }

    public String getConnectionType() {
        return connectionType;
    }

    public double getPropertyTaxDue() {
        return propertyTaxDue;
    }

    public void setPropertyTaxDue(final double propertyTaxDue) {
        this.propertyTaxDue = propertyTaxDue;
    }

    public void setConnectionType(final String connectionType) {
        this.connectionType = connectionType;
    }

    public String getHscNo() {
        return hscNo;
    }

    public void setHscNo(final String hscNo) {
        this.hscNo = hscNo;
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

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(final String ownerName) {
        this.ownerName = ownerName;
    }

    public String getRevenueWard() {
        return revenueWard;
    }

    public void setRevenueWard(final String revenueWard) {
        this.revenueWard = revenueWard;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(final String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(final String locality) {
        this.locality = locality;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(final String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getAadharNumber() {
        return aadharNumber;
    }

    public void setAadharNumber(final String aadharNumber) {
        this.aadharNumber = aadharNumber;
    }

    public Integer getNoOfPersons() {
        return noOfPersons;
    }

    public void setNoOfPersons(final Integer noOfPersons) {
        this.noOfPersons = noOfPersons;
    }

    public Integer getNoOfRooms() {
        return noOfRooms;
    }

    public void setNoOfRooms(final Integer noOfRooms) {
        this.noOfRooms = noOfRooms;
    }

    public double getDonationCharges() {
        return donationCharges;
    }

    public void setDonationCharges(final double donationCharges) {
        this.donationCharges = donationCharges;
    }

    public double getMonthlyFee() {
        return monthlyFee;
    }

    public void setMonthlyFee(final double monthlyFee) {
        this.monthlyFee = monthlyFee;
    }

    public String getPipeSizeInInch() {
        return pipeSizeInInch;
    }

    public void setPipeSizeInInch(final String pipeSizeInInch) {
        this.pipeSizeInInch = pipeSizeInInch;
    }

    public String getUsageType() {
        return usageType;
    }

    public void setUsageType(final String usageType) {
        this.usageType = usageType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(final String category) {
        this.category = category;
    }

    public double getWaterTaxDue() {
        return waterTaxDue;
    }

    public void setWaterTaxDue(final double waterTaxDue) {
        this.waterTaxDue = waterTaxDue;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getWaterSource() {
        return waterSource;
    }

    public void setWaterSource(final String waterSource) {
        this.waterSource = waterSource;
    }

    public Date getConnectionDate() {
        return connectionDate;
    }

    public BigDecimal getSumpCapacity() {
        return sumpCapacity;
    }

    public void setSumpCapacity(final BigDecimal sumpCapacity) {
        this.sumpCapacity = sumpCapacity;
    }

    public void setConnectionDate(final Date connectionDate) {
        this.connectionDate = connectionDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

}
