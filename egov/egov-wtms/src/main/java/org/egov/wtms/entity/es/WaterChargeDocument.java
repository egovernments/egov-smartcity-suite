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
package org.egov.wtms.entity.es;

import java.util.Date;

import org.egov.infra.config.core.ApplicationThreadLocals;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.fasterxml.jackson.annotation.JsonFormat;

@Document(indexName = "waterconncharges", type = "watercharges_details")
public class WaterChargeDocument {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm'Z'")
    @Field(type = FieldType.Date, index = FieldIndex.not_analyzed, format = DateFormat.date_optional_time, pattern = "yyyy-MM-dd'T'hh:mm'Z'")
    private Date createdDate;
    /*
     * private GeoPoint wardlocation; private GeoPoint propertylocation;
     */

    @Id
    private String id;

    @Field(type = FieldType.String)
    private String closureType;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String watersource;

    @Field(type = FieldType.Boolean)
    private boolean islegacy;

    @Field(type = FieldType.Long)
    private Long sumpcapacity;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String mobilenumber;

    @Field(type = FieldType.Long)
    private Long numberofperson;

    @Field(type = FieldType.Long)
    private Long totaldue;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String usage;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String propertytype;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String ulbname;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String consumercode;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String ward;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String applicationcode;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String districtname;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String zone;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String adminward;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String grade;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String bpaid;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String regionname;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String pipesize;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String doorno;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String category;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String connectiontype;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String propertyid;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String status;

    @Field(type = FieldType.Long)
    private Long monthlyRate;

    // Check for other properties given in json
    private String aadhaarnumber;

    @Field(type = FieldType.Long)
    private Long waterTaxDue;

    // Check for other properties given in json
    private String locality;

    @Field(type = FieldType.Long)
    private Long arrearsDue;

    // Check for other properties given in json
    private String consumername;

    @Field(type = FieldType.Long)
    private Long currentDue;

    @Field(type = FieldType.Long)
    private Long arrearsDemand;

    @Field(type = FieldType.Long)
    private Long currentDemand;

    public static Builder builder() {
        return new Builder();
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(final Date createdDate) {
        this.createdDate = createdDate;
    }

    /*
     * public String getWardlocation() { return wardlocation; } public void
     * setWardlocation(String wardlocation) { this.wardlocation = wardlocation;
     * }
     */

    public String getWatersource() {
        return watersource;
    }

    public String getBpaid() {
        return bpaid;
    }

    public void setBpaid(final String bpaid) {
        this.bpaid = bpaid;
    }

    public void setWatersource(final String watersource) {
        this.watersource = watersource;
    }

    public boolean isIslegacy() {
        return islegacy;
    }

    public void setIslegacy(final boolean islegacy) {
        this.islegacy = islegacy;
    }

    public Long getSumpcapacity() {
        return sumpcapacity;
    }

    public void setSumpcapacity(final Long sumpcapacity) {
        this.sumpcapacity = sumpcapacity;
    }

    public String getMobilenumber() {
        return mobilenumber;
    }

    public void setMobilenumber(final String mobilenumber) {
        this.mobilenumber = mobilenumber;
    }

    public Long getNumberofperson() {
        return numberofperson;
    }

    public void setNumberofperson(final Long numberofperson) {
        this.numberofperson = numberofperson;
    }

    public Long getTotaldue() {
        return totaldue;
    }

    public void setTotaldue(final Long totaldue) {
        this.totaldue = totaldue;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(final String usage) {
        this.usage = usage;
    }

    public String getPropertytype() {
        return propertytype;
    }

    public void setPropertytype(final String propertytype) {
        this.propertytype = propertytype;
    }

    public String getUlbname() {
        return ulbname;
    }

    public void setUlbname(final String ulbname) {
        this.ulbname = ulbname;
    }

    public String getConsumercode() {
        return consumercode;
    }

    public void setConsumercode(final String consumercode) {
        this.consumercode = consumercode;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(final String ward) {
        this.ward = ward;
    }

    public String getApplicationcode() {
        return applicationcode;
    }

    public void setApplicationcode(final String applicationcode) {
        this.applicationcode = applicationcode;
    }

    public String getDistrictname() {
        return districtname;
    }

    public void setDistrictname(final String districtname) {
        this.districtname = districtname;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(final String zone) {
        this.zone = zone;
    }

    public String getAdminward() {
        return adminward;
    }

    public void setAdminward(final String adminward) {
        this.adminward = adminward;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(final String grade) {
        this.grade = grade;
    }

    public String getRegionname() {
        return regionname;
    }

    public void setRegionname(final String regionname) {
        this.regionname = regionname;
    }

    public String getPipesize() {
        return pipesize;
    }

    public void setPipesize(final String pipesize) {
        this.pipesize = pipesize;
    }

    public String getDoorno() {
        return doorno;
    }

    public void setDoorno(final String doorno) {
        this.doorno = doorno;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(final String category) {
        this.category = category;
    }

    public String getConnectiontype() {
        return connectiontype;
    }

    public void setConnectiontype(final String connectiontype) {
        this.connectiontype = connectiontype;
    }

    public String getPropertyid() {
        return propertyid;
    }

    public void setPropertyid(final String propertyid) {
        this.propertyid = propertyid;
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

    public String getAadhaarnumber() {
        return aadhaarnumber;
    }

    public void setAadhaarnumber(final String aadhaarnumber) {
        this.aadhaarnumber = aadhaarnumber;
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

    public void setArrearsDue(final Long arrearsDue) {
        this.arrearsDue = arrearsDue;
    }

    public String getConsumername() {
        return consumername;
    }

    public void setConsumername(final String consumername) {
        this.consumername = consumername;
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
        return ApplicationThreadLocals.getCityCode() + "-" + consumercode;
    }

    public String setId() {
        return id;
    }

    public String getClosureType() {
        return closureType;
    }

    public void setClosureType(final String closureType) {
        this.closureType = closureType;
    }

    
    /*
     * public GeoPoint getWardlocation() { return wardlocation; } public void
     * setWardlocation(GeoPoint wardlocation) { this.wardlocation =
     * wardlocation; } public GeoPoint getPropertylocation() { return
     * propertylocation; } public void setPropertylocation(GeoPoint
     * propertylocation) { this.propertylocation = propertylocation; }
     */

    public static final class Builder {
        private String aadhaarnumber;
        private String adminward;
        private String applicationcode;
        private Long arrearsDemand;
        private Long arrearsDue;
        private Long waterTaxDue;
        private Long totaldue;
        private String status;
        private String category;
        private String closureType;
        private String connectiontype;
        private String consumercode;
        private String consumername;
        private Date createdDate;
        private Long currentDemand;
        private Long currentDue;
        private String doorno;
        private String districtname;
        private String ulbname;
        private String grade;
        private Boolean islegacy;
        private Long sumpcapacity;
        private Long numberofperson;
        private String zone;
        private String regionname;
        private String pipesize;
        private String propertyid;
        private Long monthlyRate;
        private String mobilenumber;
        private String locality;
        private String propertytype;
        private String watersource;
        private String ward;
        private String usage;

        private Builder() {
        }

        public Builder withAadhaarnumber(final String aadhaarnumber) {
            this.aadhaarnumber = aadhaarnumber;
            return this;
        }

        public Builder withUsage(final String usage) {
            this.usage = usage;
            return this;
        }

        public Builder withAdminward(final String adminward) {
            this.adminward = adminward;
            return this;
        }

        public Builder with(final String aapplicationcode) {
            applicationcode = aapplicationcode;
            return this;
        }

        public Builder withApplicationcode(final String aapplicationcode) {
            applicationcode = aapplicationcode;
            return this;
        }

        public Builder withSumpcapacity(final Long sumpcapacity) {
            this.sumpcapacity = sumpcapacity;
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

        public Builder withTotaldue(final Long totaldue) {
            this.totaldue = totaldue;
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
            this.connectiontype = connectiontype;
            return this;
        }

        public Builder withConsumercode(final String consumercode) {
            this.consumercode = consumercode;
            return this;
        }

        public Builder withConsumername(final String consumername) {
            this.consumername = consumername;
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

        public Builder withDistrictname(final String districtname) {
            this.districtname = districtname;
            return this;
        }

        public Builder withDoorno(final String doorno) {
            this.doorno = doorno;
            return this;
        }

        public Builder withGrade(final String grade) {
            this.grade = grade;
            return this;
        }

        public Builder withNumberofperson(final Long numberofperson) {
            this.numberofperson = numberofperson;
            return this;
        }

        public Builder withMobilenumber(final String mobilenumber) {
            this.mobilenumber = mobilenumber;
            return this;
        }

        public Builder withMonthlyRate(final Long monthlyRate) {
            this.monthlyRate = monthlyRate;
            return this;
        }

        public Builder withRegionname(final String regionname) {
            this.regionname = regionname;
            return this;
        }

        public Builder withIslegacy(final Boolean islegacy) {
            this.islegacy = islegacy;
            return this;
        }

        public Builder withLocality(final String locality) {
            this.locality = locality;
            return this;
        }

        public Builder withPropertyid(final String propertyid) {
            this.propertyid = propertyid;
            return this;
        }

        public Builder withPropertytype(final String propertytype) {
            this.propertytype = propertytype;
            return this;
        }

        public Builder withPipesize(final String pipesize) {
            this.pipesize = pipesize;
            return this;
        }

        public Builder withUlbname(final String ulbname) {
            this.ulbname = ulbname;
            return this;
        }

        public Builder withWatersource(final String watersource) {
            this.watersource = watersource;
            return this;
        }

        public Builder withZone(final String zone) {
            this.zone = zone;
            return this;
        }

        public Builder withWard(final String ward) {
            this.ward = ward;
            return this;
        }

        public Builder withWard(final Long waterTaxDue) {
            this.waterTaxDue = waterTaxDue;
            return this;
        }

        public WaterChargeDocument build() {
            final WaterChargeDocument waterChargeIndex = new WaterChargeDocument();
            waterChargeIndex.setAadhaarnumber(aadhaarnumber);
            waterChargeIndex.setAdminward(adminward);
            waterChargeIndex.setApplicationcode(applicationcode);
            waterChargeIndex.setArrearsDemand(arrearsDemand);
            waterChargeIndex.setArrearsDue(arrearsDue);
            waterChargeIndex.setWaterTaxDue(waterTaxDue);
            waterChargeIndex.setTotaldue(totaldue);
            waterChargeIndex.setStatus(status);
            waterChargeIndex.setCategory(category);
            waterChargeIndex.setClosureType(closureType);
            waterChargeIndex.setConnectiontype(connectiontype);
            waterChargeIndex.setConsumercode(consumercode);
            waterChargeIndex.setConsumername(consumername);
            waterChargeIndex.setCreatedDate(createdDate);
            waterChargeIndex.setCurrentDemand(currentDemand);
            waterChargeIndex.setCurrentDue(currentDue);
            waterChargeIndex.setDistrictname(districtname);
            waterChargeIndex.setDoorno(doorno);
            waterChargeIndex.setPipesize(pipesize);
            waterChargeIndex.setNumberofperson(numberofperson);
            waterChargeIndex.setCreatedDate(createdDate);
            waterChargeIndex.setUlbname(ulbname);
            waterChargeIndex.setGrade(grade);
            waterChargeIndex.setUsage(usage);
            waterChargeIndex.setIslegacy(islegacy);
            waterChargeIndex.setLocality(locality);
            waterChargeIndex.setPropertyid(propertyid);
            waterChargeIndex.setPropertytype(propertytype);
            waterChargeIndex.setRegionname(regionname);
            waterChargeIndex.setMobilenumber(mobilenumber);
            waterChargeIndex.setMonthlyRate(monthlyRate);
            waterChargeIndex.setSumpcapacity(sumpcapacity);
            waterChargeIndex.setWatersource(watersource);
            waterChargeIndex.setWard(ward);
            waterChargeIndex.setZone(zone);
            return waterChargeIndex;
        }
    }

}
