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
package org.egov.wtms.entity.es;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.util.Date;

@Document(indexName = "waterchargeconsumer", type = "waterchargeconsumer")
public class WaterChargeDocument {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Field(type = FieldType.Date, format = DateFormat.date_optional_time, pattern = "yyyy-MM-dd")
    private Date createdDate;

    @Field(index = FieldIndex.not_analyzed)
    @GeoPointField
    private GeoPoint wardlocation;

    @Field(index = FieldIndex.not_analyzed)
    @GeoPointField
    private GeoPoint propertylocation;

    @Id
    private String id;

    @Field(type = FieldType.String)
    private String closureType;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String waterSource;

    @Field(type = FieldType.Boolean)
    private boolean legacy;

    @Field(type = FieldType.Long)
    private Long sumpCapacity;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String mobileNumber;

    @Field(type = FieldType.Long)
    private Long numberOfPerson;

    @Field(type = FieldType.Long)
    private Long totalDue;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String usage;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String propertyType;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String consumerCode;
    
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String oldConsumerCode;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String applicationCode;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String districtName;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String cityGrade;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String cityName;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String cityCode;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String zone;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String adminWard;

    @Field(type = FieldType.String)
    private String bpaid;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String regionName;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String pipeSize;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String doorNo;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String category;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String connectionType;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String propertyId;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String status;

    @Field(type = FieldType.Long)
    private Long monthlyRate;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String aadhaarNumber;

    @Field(type = FieldType.Long)
    private Long waterTaxDue;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String locality;

    @Field(type = FieldType.Long)
    private Long arrearsDue;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String consumerName;

    @Field(type = FieldType.Long)
    private Long currentDue;

    @Field(type = FieldType.Long)
    private Long arrearsDemand;

    @Field(type = FieldType.Long)
    private Long currentDemand;

    @Field(type = FieldType.Long)
    private Long totalCollection;

    @Field(type = FieldType.Long)
    private Long totalDemand;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String revenueWard;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String duePeriod;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String billCollector;

    public static Builder builder() {
        return new Builder();
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(final Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getBpaid() {
        return bpaid;
    }

    public void setBpaid(final String bpaid) {
        this.bpaid = bpaid;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(final String zone) {
        this.zone = zone;
    }

    public String getDoorNo() {
        return doorNo;
    }

    public void setDoorNo(final String doorNo) {
        this.doorNo = doorNo;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(final String category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public Long getMonthlyRate() {
        return monthlyRate;
    }

    public void setMonthlyRate(final Long monthlyRate) {
        this.monthlyRate = monthlyRate;
    }

    public Long getWaterTaxDue() {
        return waterTaxDue;
    }

    public void setWaterTaxDue(final Long waterTaxDue) {
        this.waterTaxDue = waterTaxDue;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(final String locality) {
        this.locality = locality;
    }

    public Long getArrearsDue() {
        return arrearsDue;
    }

    public String getRevenueWard() {
        return revenueWard;
    }

    public void setRevenueWard(final String revenueWard) {
        this.revenueWard = revenueWard;
    }

    public Long getTotalCollection() {
        return totalCollection;
    }

    public void setTotalCollection(final Long totalCollection) {
        this.totalCollection = totalCollection;
    }

    public Long getTotalDemand() {
        return totalDemand;
    }

    public void setTotalDemand(final Long totalDemand) {
        this.totalDemand = totalDemand;
    }

    public void setArrearsDue(final Long arrearsDue) {
        this.arrearsDue = arrearsDue;
    }

    public Long getCurrentDue() {
        return currentDue;
    }

    public void setCurrentDue(final Long currentDue) {
        this.currentDue = currentDue;
    }

    public Long getArrearsDemand() {
        return arrearsDemand;
    }

    public void setArrearsDemand(final Long arrearsDemand) {
        this.arrearsDemand = arrearsDemand;
    }

    public Long getCurrentDemand() {
        return currentDemand;
    }

    public void setCurrentDemand(final Long currentDemand) {
        this.currentDemand = currentDemand;
    }

    public String getId() {
        return id;
    }

    public void setId() {
    	if(consumerCode!=null)
        id = ApplicationThreadLocals.getCityCode().concat("-").concat(consumerCode);
    }

    public String getClosureType() {
        return closureType;
    }

    public void setClosureType(final String closureType) {
        this.closureType = closureType;
    }

    public String getWaterSource() {
        return waterSource;
    }

    public void setWaterSource(final String waterSource) {
        this.waterSource = waterSource;
    }

    public Long getSumpCapacity() {
        return sumpCapacity;
    }

    public void setSumpCapacity(final Long sumpCapacity) {
        this.sumpCapacity = sumpCapacity;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(final String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Long getNumberOfPerson() {
        return numberOfPerson;
    }

    public void setNumberOfPerson(final Long numberOfPerson) {
        this.numberOfPerson = numberOfPerson;
    }

    public Long getTotalDue() {
        return totalDue;
    }

    public void setTotalDue(final Long totalDue) {
        this.totalDue = totalDue;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(final String usage) {
        this.usage = usage;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(final String propertyType) {
        this.propertyType = propertyType;
    }

    public String getConsumerCode() {
        return consumerCode;
    }

    public void setConsumerCode(final String consumerCode) {
        this.consumerCode = consumerCode;
    }

    public String getApplicationCode() {
        return applicationCode;
    }

    public void setApplicationCode(final String applicationCode) {
        this.applicationCode = applicationCode;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(final String districtName) {
        this.districtName = districtName;
    }

    public String getAdminWard() {
        return adminWard;
    }

    public void setAdminWard(final String adminWard) {
        this.adminWard = adminWard;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(final String regionName) {
        this.regionName = regionName;
    }

    public String getPipeSize() {
        return pipeSize;
    }

    public void setPipeSize(final String pipeSize) {
        this.pipeSize = pipeSize;
    }

    public String getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(final String connectionType) {
        this.connectionType = connectionType;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(final String propertyId) {
        this.propertyId = propertyId;
    }

    public String getAadhaarNumber() {
        return aadhaarNumber;
    }

    public void setAadhaarNumber(final String aadhaarNumber) {
        this.aadhaarNumber = aadhaarNumber;
    }

    public String getConsumerName() {
        return consumerName;
    }

    public void setConsumerName(final String consumerName) {
        this.consumerName = consumerName;
    }

    public GeoPoint getWardlocation() {
        return wardlocation;
    }

    public void setWardlocation(final GeoPoint wardlocation) {
        this.wardlocation = wardlocation;
    }

    public GeoPoint getPropertylocation() {
        return propertylocation;
    }

    public void setPropertylocation(final GeoPoint propertylocation) {
        this.propertylocation = propertylocation;
    }

    public String getCityGrade() {
        return cityGrade;
    }

    public void setCityGrade(final String cityGrade) {
        this.cityGrade = cityGrade;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(final String cityName) {
        this.cityName = cityName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(final String cityCode) {
        this.cityCode = cityCode;
    }

    public Boolean getLegacy() {
        return legacy;
    }

    public void setLegacy(final Boolean legacy) {
        this.legacy = legacy;
    }

    public String getDuePeriod() {
        return duePeriod;
    }

    public void setDuePeriod(final String duePeriod) {
        this.duePeriod = duePeriod;
    }

    public String getBillCollector() {
        return billCollector;
    }

    public void setBillCollector(final String billCollector) {
        this.billCollector = billCollector;
    }

    public String getOldConsumerCode() {
        return oldConsumerCode;
    }

    public void setOldConsumerCode(String oldConsumerCode) {
        this.oldConsumerCode = oldConsumerCode;
    }

    public static final class Builder {
        private String aadhaarNumber;
        private String adminWard;
        private String applicationCode;
        private Long arrearsDemand;
        private Long arrearsDue;
        private Long waterTaxDue;
        private Long totalDemand;
        private Long totalCollection;
        private Long totalDue;
        private String status;
        private String category;
        private String closureType;
        private String connectionType;
        private String consumerCode;
        private String oldConsumerCode;
        private String consumerName;
        private Date createdDate;
        private Long currentDemand;
        private Long currentDue;
        private String doorNo;
        private String districtName;
        private String cityName;
        private String cityGrade;
        private Boolean legacy;
        private Long sumpCapacity;
        private Long numberOfPerson;
        private String zone;
        private String regionName;
        private String pipeSize;
        private String propertyId;
        private Long monthlyRate;
        private String mobileNumber;
        private String locality;
        private String propertyType;
        private String waterSource;
        private String revenueWard;
        private String usage;
        private String cityCode;
        private GeoPoint wardLocation;
        private GeoPoint propertyLocation;
        private String duePeriod;
        private String billCollector;

        private Builder() {
        }

        public Builder withDuePeriod(final String duePeriod) {
            this.duePeriod = duePeriod;
            return this;
        }

        public Builder withBillCollector(final String billCollector) {
            this.billCollector = billCollector;
            return this;
        }

        public Builder withWardlocation(final GeoPoint wardlocation) {
            wardLocation = wardlocation;
            return this;
        }

        public Builder withPropertylocation(final GeoPoint propertylocation) {
            propertyLocation = propertylocation;
            return this;
        }

        public Builder withAadhaarnumber(final String aadharNumber) {
            aadhaarNumber = aadharNumber;
            return this;
        }

        public Builder withUsage(final String usage) {
            this.usage = usage;
            return this;
        }

        public Builder withAdminward(final String adminward) {
            adminWard = adminward;
            return this;
        }

        public Builder withApplicationcode(final String aapplicationcode) {
            applicationCode = aapplicationcode;
            return this;
        }

        public Builder withSumpcapacity(final Long sumpcapacity) {
            sumpCapacity = sumpcapacity;
            return this;
        }

        public Builder withArrearsDemand(final Long arrearsDemand) {
            this.arrearsDemand = arrearsDemand;
            return this;
        }

        public Builder withArrearsDue(final Long arrearsDue) {
            this.arrearsDue = arrearsDue;
            return this;
        }

        public Builder withWaterTaxDue(final Long waterTaxDue) {
            this.waterTaxDue = waterTaxDue;
            return this;
        }

        public Builder withTotalDemand(final Long totalDemand) {
            this.totalDemand = totalDemand;
            return this;
        }

        public Builder withTotalCollection(final Long totalCollection) {
            this.totalCollection = totalCollection;
            return this;
        }

        public Builder withTotaldue(final Long totaldue) {
            totalDue = totaldue;
            return this;
        }

        public Builder withStatus(final String status) {
            this.status = status;
            return this;
        }

        public Builder withCategory(final String category) {
            this.category = category;
            return this;
        }

        public Builder withClosureType(final String closureType) {
            this.closureType = closureType;
            return this;
        }

        public Builder withConnectiontype(final String connectiontype) {
            connectionType = connectiontype;
            return this;
        }

        public Builder withConsumerCode(final String consumerCode) {
            this.consumerCode = consumerCode;
            return this;
        }
        
        public Builder withOldConsumerCode(final String oldConsumerCode) {
            this.oldConsumerCode = oldConsumerCode;
            return this;
        }

        public Builder withConsumername(final String consumername) {
            consumerName = consumername;
            return this;
        }

        public Builder withCurrentDemand(final Long currentDemand) {
            this.currentDemand = currentDemand;
            return this;
        }

        public Builder withCreatedDate(final Date createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public Builder withCurrentDue(final Long currentDue) {
            this.currentDue = currentDue;
            return this;
        }

        public Builder withDistrictName(final String districtname) {
            districtName = districtname;
            return this;
        }

        public Builder withDoorNo(final String doorNo) {
            this.doorNo = doorNo;
            return this;
        }

        public Builder withCityGrade(final String cityGrade) {
            this.cityGrade = cityGrade;
            return this;
        }

        public Builder withNumberOfPerson(final Long numberofperson) {
            numberOfPerson = numberofperson;
            return this;
        }

        public Builder withMobileNumber(final String mobilenumber) {
            mobileNumber = mobilenumber;
            return this;
        }

        public Builder withMonthlyRate(final Long monthlyRate) {
            this.monthlyRate = monthlyRate;
            return this;
        }

        public Builder withRegionname(final String regionname) {
            regionName = regionname;
            return this;
        }

        public Builder withLegacy(final Boolean legacy) {
            this.legacy = legacy;
            return this;
        }

        public Builder withLocality(final String locality) {
            this.locality = locality;
            return this;
        }

        public Builder withPropertyid(final String propertyid) {
            propertyId = propertyid;
            return this;
        }

        public Builder withPropertytype(final String propertytype) {
            propertyType = propertytype;
            return this;
        }

        public Builder withPipesize(final String pipesize) {
            pipeSize = pipesize;
            return this;
        }

        public Builder withCityName(final String cityname) {
            cityName = cityname;
            return this;
        }

        public Builder withCityCode(final String citycode) {
            cityCode = citycode;
            return this;
        }

        public Builder withWatersource(final String watersource) {
            waterSource = watersource;
            return this;
        }

        public Builder withZone(final String zone) {
            this.zone = zone;
            return this;
        }

        public Builder withRevenueWard(final String revenueWard) {
            this.revenueWard = revenueWard;
            return this;
        }

        public Builder withWard(final Long waterTaxDue) {
            this.waterTaxDue = waterTaxDue;
            return this;
        }

        public WaterChargeDocument build() {
            final WaterChargeDocument waterChargeIndex = new WaterChargeDocument();
            waterChargeIndex.setWardlocation(wardLocation);
            waterChargeIndex.setConsumerCode(consumerCode);
            waterChargeIndex.setOldConsumerCode(oldConsumerCode);
            waterChargeIndex.setId();
            waterChargeIndex.setPropertylocation(propertyLocation);
            waterChargeIndex.setAadhaarNumber(aadhaarNumber);
            waterChargeIndex.setAdminWard(adminWard);
            waterChargeIndex.setApplicationCode(applicationCode);
            waterChargeIndex.setArrearsDemand(arrearsDemand);
            waterChargeIndex.setDuePeriod(duePeriod);
            waterChargeIndex.setBillCollector(billCollector);
            waterChargeIndex.setArrearsDue(arrearsDue);
            waterChargeIndex.setWaterTaxDue(waterTaxDue);
            waterChargeIndex.setTotalDemand(totalDemand);
            waterChargeIndex.setTotalCollection(totalCollection);
            waterChargeIndex.setTotalDue(totalDue);
            waterChargeIndex.setStatus(status);
            waterChargeIndex.setCategory(category);
            waterChargeIndex.setClosureType(closureType);
            waterChargeIndex.setConnectionType(connectionType);
            waterChargeIndex.setConsumerName(consumerName);
            waterChargeIndex.setCreatedDate(createdDate);
            waterChargeIndex.setCurrentDemand(currentDemand);
            waterChargeIndex.setCurrentDue(currentDue);
            waterChargeIndex.setDistrictName(districtName);
            waterChargeIndex.setDoorNo(doorNo);
            waterChargeIndex.setPipeSize(pipeSize);
            waterChargeIndex.setNumberOfPerson(numberOfPerson);
            waterChargeIndex.setCreatedDate(createdDate);
            waterChargeIndex.setCityName(cityName);
            waterChargeIndex.setCityGrade(cityGrade);
            waterChargeIndex.setCityCode(cityCode);
            waterChargeIndex.setUsage(usage);
            waterChargeIndex.setLegacy(legacy);
            waterChargeIndex.setLocality(locality);
            waterChargeIndex.setPropertyId(propertyId);
            waterChargeIndex.setPropertyType(propertyType);
            waterChargeIndex.setRegionName(regionName);
            waterChargeIndex.setMobileNumber(mobileNumber);
            waterChargeIndex.setMonthlyRate(monthlyRate);
            waterChargeIndex.setSumpCapacity(sumpCapacity);
            waterChargeIndex.setWaterSource(waterSource);
            waterChargeIndex.setRevenueWard(revenueWard);
            waterChargeIndex.setZone(zone);
            return waterChargeIndex;
        }

    }

}
