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
package org.egov.wtms.elasticSearch.entity;

import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.search.elastic.Indexable;
import org.egov.search.domain.Searchable;
import org.elasticsearch.common.geo.GeoPoint;

import java.math.BigDecimal;
import java.util.Date;

public class ConsumerSearch implements Indexable {

    @Searchable(name = "zone", group = Searchable.Group.CLAUSES)
    private String zone;

    @Searchable(name = "ward", group = Searchable.Group.CLAUSES)
    private String ward;

    @Searchable(name = "adminward", group = Searchable.Group.CLAUSES)
    private String adminWard;

    @Searchable(name = "doorno", group = Searchable.Group.CLAUSES)
    private String doorno;

    @Searchable(name = "consumercode", group = Searchable.Group.CLAUSES)
    private final String consumerCode;

    @Searchable(name = "propertyid", group = Searchable.Group.CLAUSES)
    private String propertyId;

    @Searchable(name = "bpaid", group = Searchable.Group.CLAUSES)
    private String bpaId;

    @Searchable(name = "mobilenumber", group = Searchable.Group.CLAUSES)
    private final String mobileNumber;

    @Searchable(name = "consumername", group = Searchable.Group.SEARCHABLE)
    private String consumerName;

    @Searchable(name = "locality", group = Searchable.Group.SEARCHABLE)
    private String locality;

    @Searchable(name = "usage", group = Searchable.Group.CLAUSES)
    private final String usageType;

    @Searchable(name = "totaldue", group = Searchable.Group.CLAUSES)
    private BigDecimal totalDue;

    @Searchable(name = "createdDate", group = Searchable.Group.COMMON)
    private final Date createdDate;

    @Searchable(name = "applicationcode", group = Searchable.Group.CLAUSES)
    private String applicationCode;

    @Searchable(name = "status", group = Searchable.Group.CLAUSES)
    private String status;

    @Searchable(name = "connectiontype", group = Searchable.Group.CLAUSES)
    private String connectionType;

    @Searchable(name = "islegacy", group = Searchable.Group.CLAUSES)
    private Boolean islegacy;

    @Searchable(name = "closureType", group = Searchable.Group.SEARCHABLE)
    private String closureType;

    @Searchable(name = "waterTaxDue", group = Searchable.Group.CLAUSES)
    private BigDecimal waterTaxDue;

    @Searchable(name = "ulbname", group = Searchable.Group.CLAUSES)
    private final String ulbName;

    @Searchable(name = "districtname", group = Searchable.Group.CLAUSES)
    private final String districtName;

    @Searchable(name = "regionname", group = Searchable.Group.CLAUSES)
    private final String regionName;

    @Searchable(name = "grade", group = Searchable.Group.CLAUSES)
    private final String grade;

    @Searchable(name = "watersource", group = Searchable.Group.CLAUSES)
    private String waterSourceType;

    @Searchable(name = "propertytype", group = Searchable.Group.CLAUSES)
    private String propertyType;

    @Searchable(name = "category", group = Searchable.Group.CLAUSES)
    private String category;

    @Searchable(name = "sumpcapacity", group = Searchable.Group.CLAUSES)
    private Long sumpCapacity;

    @Searchable(name = "aadhaarnumber", group = Searchable.Group.SEARCHABLE)
    private String aadhaarNumber;

    @Searchable(name = "pipesize", group = Searchable.Group.CLAUSES)
    private String pipeSize;

    @Searchable(name = "numberofperson", group = Searchable.Group.CLAUSES)
    private Integer numberOfPerson;
    
    @Searchable(name = "propertylocation", group = Searchable.Group.COMMON)
    private GeoPoint propertyLocation;
    
    @Searchable(name = "wardlocation", group = Searchable.Group.COMMON)
    private GeoPoint wardLocation;

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

    @Override
    public String getIndexId() {
        return ApplicationThreadLocals.getCityCode() + "-" + consumerCode;
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

}
