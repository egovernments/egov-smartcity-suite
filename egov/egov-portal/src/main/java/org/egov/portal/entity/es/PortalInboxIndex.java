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
package org.egov.portal.entity.es;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

import static org.egov.infra.utils.ApplicationConstant.DEFAULT_TIMEZONE;
import static org.egov.infra.utils.ApplicationConstant.ES_DATE_FORMAT;

@Document(indexName = "portalinbox", type = "portalinbox")
public class PortalInboxIndex {

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String id;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String module;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String applicationNumber;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String serviceType;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String entityReferenceNumber;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String entityReferenceId;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String headerMessage;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String detailedMessage;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String link;

    @Field(type = FieldType.Boolean)
    private Boolean read;

    @Field(type = FieldType.Boolean)
    private Boolean resolved;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ES_DATE_FORMAT, timezone = DEFAULT_TIMEZONE)
    @Field(type = FieldType.Date, format = DateFormat.date_optional_time, pattern = ES_DATE_FORMAT)
    private Date applicationDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ES_DATE_FORMAT, timezone = DEFAULT_TIMEZONE)
    @Field(type = FieldType.Date, format = DateFormat.date_optional_time, pattern = ES_DATE_FORMAT)
    private Date slaendDate;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String applicationStatus;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String priority;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String applicationCreatedBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ES_DATE_FORMAT, timezone = DEFAULT_TIMEZONE)
    @Field(type = FieldType.Date, format = DateFormat.date_optional_time, pattern = ES_DATE_FORMAT)
    private Date createdDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ES_DATE_FORMAT, timezone = DEFAULT_TIMEZONE)
    @Field(type = FieldType.Date, format = DateFormat.date_optional_time, pattern = ES_DATE_FORMAT)
    private Date resolvedDate;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String firmName;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String firmPan;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String firmAddress;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String ulbName;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String districtName;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String regionName;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String ulbGrade;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String ulbCode;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getModule() {
        return module;
    }

    public void setModule(final String module) {
        this.module = module;
    }

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public void setApplicationNumber(final String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(final String serviceType) {
        this.serviceType = serviceType;
    }

    public String getEntityReferenceNumber() {
        return entityReferenceNumber;
    }

    public void setEntityReferenceNumber(final String entityReferenceNumber) {
        this.entityReferenceNumber = entityReferenceNumber;
    }

    public String getEntityReferenceId() {
        return entityReferenceId;
    }

    public void setEntityReferenceId(final String entityReferenceId) {
        this.entityReferenceId = entityReferenceId;
    }

    public String getHeaderMessage() {
        return headerMessage;
    }

    public void setHeaderMessage(final String headerMessage) {
        this.headerMessage = headerMessage;
    }

    public String getDetailedMessage() {
        return detailedMessage;
    }

    public void setDetailedMessage(final String detailedMessage) {
        this.detailedMessage = detailedMessage;
    }

    public String getLink() {
        return link;
    }

    public void setLink(final String link) {
        this.link = link;
    }

    public Boolean getResolved() {
        return resolved;
    }

    public void setResolved(final Boolean resolved) {
        this.resolved = resolved;
    }

    public Date getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(final Date applicationDate) {
        this.applicationDate = applicationDate;
    }

    public Date getSlaendDate() {
        return slaendDate;
    }

    public void setSlaendDate(final Date slaendDate) {
        this.slaendDate = slaendDate;
    }

    public String getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(final String applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(final String priority) {
        this.priority = priority;
    }

    public String getApplicationCreatedBy() {
        return applicationCreatedBy;
    }

    public void setApplicationCreatedBy(final String applicationCreatedBy) {
        this.applicationCreatedBy = applicationCreatedBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(final Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getFirmName() {
        return firmName;
    }

    public void setFirmName(final String firmName) {
        this.firmName = firmName;
    }

    public String getFirmPan() {
        return firmPan;
    }

    public void setFirmPan(final String firmPan) {
        this.firmPan = firmPan;
    }

    public String getFirmAddress() {
        return firmAddress;
    }

    public void setFirmAddress(final String firmAddress) {
        this.firmAddress = firmAddress;
    }

    public String getUlbName() {
        return ulbName;
    }

    public void setUlbName(final String ulbName) {
        this.ulbName = ulbName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(final String districtName) {
        this.districtName = districtName;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(final String regionName) {
        this.regionName = regionName;
    }

    public String getUlbGrade() {
        return ulbGrade;
    }

    public void setUlbGrade(final String ulbGrade) {
        this.ulbGrade = ulbGrade;
    }

    public String getUlbCode() {
        return ulbCode;
    }

    public void setUlbCode(final String ulbCode) {
        this.ulbCode = ulbCode;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(final Boolean read) {
        this.read = read;
    }

    public Date getResolvedDate() {
        return resolvedDate;
    }

    public void setResolvedDate(final Date resolvedDate) {
        this.resolvedDate = resolvedDate;
    }

}
