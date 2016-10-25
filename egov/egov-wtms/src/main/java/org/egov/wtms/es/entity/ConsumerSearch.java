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
package org.egov.wtms.es.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.elasticsearch.common.geo.GeoPoint;

public class ConsumerSearch {

    private final String consumerCode;
    private final String mobileNumber;
    private final String usageType;
    private final Date createdDate;
    private final String ulbName;
    private final String districtName;
    private final String regionName;
    private final String grade;
    private String zone;
    private String ward;
    private String adminWard;
    private String doorno;
    private String propertyId;
    private String bpaId;
    private String consumerName;
    private String locality;
    private BigDecimal totalDue;
    private String applicationCode;
    private String status;
    private String connectionType;
    private Boolean islegacy;
    private String closureType;
    private BigDecimal waterTaxDue;
    private String waterSourceType;

    private String propertyType;

    private String category;

    private Long sumpCapacity;

    private String aadhaarNumber;

    private String pipeSize;

    private Integer numberOfPerson;

    private GeoPoint propertyLocation;

    private GeoPoint wardLocation;

    private BigDecimal arrearsDue;

    private BigDecimal currentDue;

    private BigDecimal arrearsDemand;

    private BigDecimal currentDemand;

    private BigDecimal monthlyRate;

    public ConsumerSearch(final String consumerCode, final String mobileNumber, final String usageType, final String ulbName,
                          final Date createdDate, final String districtName, final String regionName, final String grade) {
        this.consumerCode = consumerCode;
        this.mobileNumber = mobileNumber;
        this.usageType = usageType;
        this.ulbName = ulbName;
        this.createdDate = createdDate;
        this.districtName = districtName;
        this.regionName = regionName;
        this.grade = grade;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(final String zone) {
        this.zone = zone;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(final String ward) {
        this.ward = ward;
    }

    public String getConsumerCode() {
        return consumerCode;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(final String propertyId) {
        this.propertyId = propertyId;
    }

    public String getBpaId() {
        return bpaId;
    }

    public void setBpaId(final String bpaId) {
        this.bpaId = bpaId;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getConsumerName() {
        return consumerName;
    }

    public void setConsumerName(final String consumerName) {
        this.consumerName = consumerName;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(final String locality) {
        this.locality = locality;
    }

    public String getUsageType() {
        return usageType;
    }

    public BigDecimal getTotalDue() {
        return totalDue;
    }

    public void setTotalDue(final BigDecimal totalDue) {
        this.totalDue = totalDue;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public String getApplicationCode() {
        return applicationCode;
    }

    public void setApplicationCode(final String applicationCode) {
        this.applicationCode = applicationCode;
    }

    public String getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(final String connectionType) {
        this.connectionType = connectionType;
    }

    public BigDecimal getWaterTaxDue() {
        return waterTaxDue;
    }

    public void setWaterTaxDue(final BigDecimal waterTaxDue) {
        this.waterTaxDue = waterTaxDue;
    }

    public String getClosureType() {
        return closureType;
    }

    public void setClosureType(final String closureType) {
        this.closureType = closureType;
    }

    public String getUlbName() {
        return ulbName;
    }

    public String getAdminWard() {
        return adminWard;
    }

    public void setAdminWard(final String adminWard) {
        this.adminWard = adminWard;
    }

    public String getDoorno() {
        return doorno;
    }

    public void setDoorno(final String doorno) {
        this.doorno = doorno;
    }

    public Boolean getIslegacy() {
        return islegacy;
    }

    public void setIslegacy(final Boolean islegacy) {
        this.islegacy = islegacy;
    }

    public String getDistrictName() {
        return districtName;
    }

    public String getRegionName() {
        return regionName;
    }

    public String getGrade() {
        return grade;
    }

    public String getWaterSourceType() {
        return waterSourceType;
    }

    public void setWaterSourceType(final String waterSourceType) {
        this.waterSourceType = waterSourceType;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(final String propertyType) {
        this.propertyType = propertyType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(final String category) {
        this.category = category;
    }

    public Long getSumpCapacity() {
        return sumpCapacity;
    }

    public void setSumpCapacity(final Long sumpCapacity) {
        this.sumpCapacity = sumpCapacity;
    }

    public String getAadhaarNumber() {
        return aadhaarNumber;
    }

    public void setAadhaarNumber(final String aadhaarNumber) {
        this.aadhaarNumber = aadhaarNumber;
    }

    public String getPipeSize() {
        return pipeSize;
    }

    public void setPipeSize(final String pipeSize) {
        this.pipeSize = pipeSize;
    }

    public Integer getNumberOfPerson() {
        return numberOfPerson;
    }

    public void setNumberOfPerson(final Integer numberOfPerson) {
        this.numberOfPerson = numberOfPerson;
    }

    public GeoPoint getPropertyLocation() {
        return propertyLocation;
    }

    public void setPropertyLocation(GeoPoint propertyLocation) {
        this.propertyLocation = propertyLocation;
    }

    public GeoPoint getWardLocation() {
        return wardLocation;
    }

    public void setWardLocation(GeoPoint wardLocation) {
        this.wardLocation = wardLocation;
    }

    public BigDecimal getArrearsDue() {
        return arrearsDue;
    }

    public void setArrearsDue(final BigDecimal arrearsDue) {
        this.arrearsDue = arrearsDue;
    }

    public BigDecimal getCurrentDue() {
        return currentDue;
    }

    public void setCurrentDue(final BigDecimal currentDue) {
        this.currentDue = currentDue;
    }

    public BigDecimal getMonthlyRate() {
        return monthlyRate;
    }

    public void setMonthlyRate(final BigDecimal monthlyRate) {
        this.monthlyRate = monthlyRate;
    }

    public BigDecimal getArrearsDemand() {
        return arrearsDemand;
    }

    public void setArrearsDemand(BigDecimal arrearsDemand) {
        this.arrearsDemand = arrearsDemand;
    }

    public BigDecimal getCurrentDemand() {
        return currentDemand;
    }

    public void setCurrentDemand(BigDecimal currentDemand) {
        this.currentDemand = currentDemand;
    }

}
