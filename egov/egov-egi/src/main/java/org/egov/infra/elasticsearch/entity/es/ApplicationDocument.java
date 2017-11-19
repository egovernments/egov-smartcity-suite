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

package org.egov.infra.elasticsearch.entity.es;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.egov.infra.elasticsearch.entity.enums.ClosureStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

import static org.egov.infra.utils.ApplicationConstant.DEFAULT_TIMEZONE;
import static org.egov.infra.utils.ApplicationConstant.ES_DATE_FORMAT;
import static org.springframework.data.elasticsearch.annotations.DateFormat.date_optional_time;
import static org.springframework.data.elasticsearch.annotations.FieldIndex.not_analyzed;

@Document(indexName = "applications", type = "applications")
public class ApplicationDocument {

    @Id
    private String id;

    @Field(type = FieldType.String, index = not_analyzed)
    private String moduleName;

    @Field(type = FieldType.String, index = not_analyzed)
    private String applicationNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ES_DATE_FORMAT, timezone = DEFAULT_TIMEZONE)
    @Field(type = FieldType.Date, format = date_optional_time, pattern = ES_DATE_FORMAT)
    private Date applicationDate;

    @Field(type = FieldType.String, index = not_analyzed)
    private String applicationType;

    @Field(type = FieldType.String, index = not_analyzed)
    private String applicantName;

    @Field(type = FieldType.String)
    private String applicantAddress;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ES_DATE_FORMAT, timezone = DEFAULT_TIMEZONE)
    @Field(type = FieldType.Date, format = date_optional_time, pattern = ES_DATE_FORMAT)
    private Date disposalDate;

    @Field(type = FieldType.String, index = not_analyzed)
    private String status;

    @Field(type = FieldType.String)
    private String url;

    @Field(type = FieldType.String, index = not_analyzed)
    private String consumerCode;

    @Field(type = FieldType.String, index = not_analyzed)
    private String mobileNumber;

    @Field(type = FieldType.String, index = not_analyzed)
    private String ownerName;

    @Field(type = FieldType.String, index = not_analyzed)
    private String aadharNumber;

    @Field(type = FieldType.Integer)
    private Integer elapsedDays;

    @Field(type = FieldType.String, index = not_analyzed)
    private String closed;

    @Field(type = FieldType.String, index = not_analyzed)
    private String approved;

    @Field(type = FieldType.String, index = not_analyzed)
    private String channel;

    @Field(type = FieldType.String, index = not_analyzed)
    private String cityCode;

    @Field(type = FieldType.String, index = not_analyzed)
    private String cityName;

    @Field(type = FieldType.String, index = not_analyzed)
    private String cityGrade;

    @Field(type = FieldType.String, index = not_analyzed)
    private String districtName;

    @Field(type = FieldType.String, index = not_analyzed)
    private String regionName;

    @Field(type = FieldType.Integer)
    private Integer isClosed;

    @Field(type = FieldType.Integer)
    private Integer sla;

    @Field(type = FieldType.Integer)
    private Integer slaGap;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ES_DATE_FORMAT, timezone = DEFAULT_TIMEZONE)
    @Field(type = FieldType.Date, format = DateFormat.date_optional_time, pattern = ES_DATE_FORMAT)
    private Date createdDate;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(final String moduleName) {
        this.moduleName = moduleName;
    }

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public void setApplicationNumber(final String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }

    public Date getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(final Date applicationDate) {
        this.applicationDate = applicationDate;
    }

    public String getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(final String applicationType) {
        this.applicationType = applicationType;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(final String applicantName) {
        this.applicantName = applicantName;
    }

    public String getApplicantAddress() {
        return applicantAddress;
    }

    public void setApplicantAddress(final String applicantAddress) {
        this.applicantAddress = applicantAddress;
    }

    public Date getDisposalDate() {
        return disposalDate;
    }

    public void setDisposalDate(final Date disposalDate) {
        this.disposalDate = disposalDate;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(final String cityName) {
        this.cityName = cityName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(final String districtName) {
        this.districtName = districtName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public String getConsumerCode() {
        return consumerCode;
    }

    public void setConsumerCode(final String consumerCode) {
        this.consumerCode = consumerCode;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(final String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(final String ownerName) {
        this.ownerName = ownerName;
    }

    public void setIsClosed(final Integer isClosed) {
        this.isClosed = isClosed;
    }

    public String getAadharNumber() {
        return aadharNumber;
    }

    public void setAadharNumber(final String aadharNumber) {
        this.aadharNumber = aadharNumber;
    }

    public Integer getElapsedDays() {
        return elapsedDays;
    }

    public void setElapsedDays(final Integer elapsedDays) {
        this.elapsedDays = elapsedDays;
    }

    public String getClosed() {
        return closed;
    }

    public void setClosed(final String closed) {
        this.closed = closed;
        if (this.closed.equals(ClosureStatus.YES.toString()))
            isClosed = 1;
        else
            isClosed = 0;
    }

    public String getApproved() {
        return approved;
    }

    public void setApproved(final String approved) {
        this.approved = approved;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(final String channel) {
        this.channel = channel;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(final String cityCode) {
        this.cityCode = cityCode;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(final String regionName) {
        this.regionName = regionName;
    }

    public Integer getIsClosed() {
        return isClosed;
    }

    public String getCityGrade() {
        return cityGrade;
    }

    public void setCityGrade(final String cityGrade) {
        this.cityGrade = cityGrade;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(final Date createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getSla() {
        return sla;
    }

    public void setSla(final Integer sla) {
        this.sla = sla;
    }

    public Integer getSlaGap() {
        if (sla != null)
            return elapsedDays - sla;
        return null;
    }

}