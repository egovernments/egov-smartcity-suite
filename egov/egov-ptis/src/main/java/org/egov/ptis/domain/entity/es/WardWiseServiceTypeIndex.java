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

package org.egov.ptis.domain.entity.es;

import static org.egov.infra.utils.ApplicationConstant.DEFAULT_TIMEZONE;
import static org.egov.infra.utils.ApplicationConstant.ES_DATE_FORMAT;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.fasterxml.jackson.annotation.JsonFormat;

@Document(indexName = "ptservicetype", type = "ptservicewisedetails")
public class WardWiseServiceTypeIndex {
    @Id
    private String id;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String applicationType;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String applicationNumber;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String applicantName;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String appStatus;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String ownerName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ES_DATE_FORMAT, timezone = DEFAULT_TIMEZONE)
    @Field(type = FieldType.Date, format = DateFormat.date_optional_time, pattern = ES_DATE_FORMAT)
    private Date applicationDate;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String cityCode;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String region;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String district;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String cityName;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String url;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String domainUrl;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String ward;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String propertyType;
    @Field(type = FieldType.Double)
    private Double taxBeforeAffctd;
    @Field(type = FieldType.Double)
    private Double taxAfterAffctd;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(String applicationType) {
        this.applicationType = applicationType;
    }

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public void setApplicationNumber(String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getAppStatus() {
        return appStatus;
    }

    public void setAppStatus(String appStatus) {
        this.appStatus = appStatus;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Date getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(Date applicationDate) {
        this.applicationDate = applicationDate;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDomainUrl() {
        return domainUrl;
    }

    public void setDomainUrl(String domainUrl) {
        this.domainUrl = domainUrl;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public Double getTaxBeforeAffctd() {
        return taxBeforeAffctd;
    }

    public void setTaxBeforeAffctd(Double taxBeforeAffctd) {
        this.taxBeforeAffctd = taxBeforeAffctd;
    }

    public Double getTaxAfterAffctd() {
        return taxAfterAffctd;
    }

    public void setTaxAfterAffctd(Double taxAfterAffctd) {
        this.taxAfterAffctd = taxAfterAffctd;
    }

}
