/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
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

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

import static org.egov.wtms.utils.constants.WaterTaxConstants.WATER_CHARGES_SCHEME_INDEX;
import static org.springframework.data.elasticsearch.annotations.DateFormat.date_optional_time;
import static org.springframework.data.elasticsearch.annotations.FieldIndex.analyzed;
import static org.springframework.data.elasticsearch.annotations.FieldIndex.not_analyzed;
import static org.springframework.data.elasticsearch.annotations.FieldType.Date;
import static org.springframework.data.elasticsearch.annotations.FieldType.String;


@Document(indexName = WATER_CHARGES_SCHEME_INDEX, type = WATER_CHARGES_SCHEME_INDEX)
public class WaterChargesDashboardIndex {

    @Id
    private String id;

    @Field(type = String, index = not_analyzed)
    private String ulbCode;

    @Field(type = String, index = not_analyzed)
    private String ulbName;

    @Field(type = String, index = not_analyzed)
    private String districtName;

    @Field(type = String, index = not_analyzed)
    private String regionName;

    @Field(type = String, index = not_analyzed)
    private String revenueWard;

    @Field(type = String, index = not_analyzed)
    private String electionWard;

    @Field(type = String, index = not_analyzed)
    private String grade;

    @Field(type = String, index = not_analyzed)
    private String functionaryName;

    @Field(type = String, index = not_analyzed)
    private String applicationNumber;

    @Field(type = String, index = not_analyzed)
    private String applicationType;

    @Field(type = String, index = not_analyzed)
    private String connectionType;

    @Field(type = String, index = not_analyzed)
    private String connectionCategory;

    @Field(type = String, index = not_analyzed)
    private String connectionStatus;

    @Field(type = String, index = not_analyzed)
    private String applicationSource;

    @Field(type = String, index = not_analyzed)
    private String applicationUrl;

    @Field(type = Date, format = date_optional_time)
    private Date applicationDate;

    @Field(type = Date, format = date_optional_time)
    private Date applicationCreatedDate;

    @Field(type = Date, format = date_optional_time)
    private Date workOrderDate;

    @Field(type = FieldType.Boolean)
    private boolean workOrderIssued;

    @Field(type = Date, format = date_optional_time)
    private Date executionDate;

    @Field(type = FieldType.Boolean)
    private boolean connectionExecuted;

    @Field(type = FieldType.Boolean)
    private boolean legacy;

    @Field(type = FieldType.Double)
    private Double bplConnection;

    @Field(type = FieldType.Double)
    private Double nonBplConnection;

    @Field(type = FieldType.Double)
    private Double bplSanctionOrder;

    @Field(type = FieldType.Double)
    private Double nonBplSanctionOrder;

    @Field(type = FieldType.Double)
    private Double bplExecution;

    @Field(type = FieldType.Double)
    private Double nonBplExecution;

    @Field(type = FieldType.Double)
    private Double cancelledConnection;

    @Field(type = String, index = analyzed)
    private String address;

    @Field(type = String, index = not_analyzed)
    private String applicantName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUlbCode() {
        return ulbCode;
    }

    public void setUlbCode(String ulbCode) {
        this.ulbCode = ulbCode;
    }

    public String getUlbName() {
        return ulbName;
    }

    public void setUlbName(String ulbName) {
        this.ulbName = ulbName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getRevenueWard() {
        return revenueWard;
    }

    public void setRevenueWard(String revenueWard) {
        this.revenueWard = revenueWard;
    }

    public String getElectionWard() {
        return electionWard;
    }

    public void setElectionWard(String electionWard) {
        this.electionWard = electionWard;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getFunctionaryName() {
        return functionaryName;
    }

    public void setFunctionaryName(String functionaryName) {
        this.functionaryName = functionaryName;
    }

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public void setApplicationNumber(String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }

    public String getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(String applicationType) {
        this.applicationType = applicationType;
    }

    public String getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(String connectionType) {
        this.connectionType = connectionType;
    }

    public String getConnectionCategory() {
        return connectionCategory;
    }

    public void setConnectionCategory(String connectionCategory) {
        this.connectionCategory = connectionCategory;
    }

    public String getConnectionStatus() {
        return connectionStatus;
    }

    public void setConnectionStatus(String connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    public String getApplicationSource() {
        return applicationSource;
    }

    public void setApplicationSource(String applicationSource) {
        this.applicationSource = applicationSource;
    }

    public String getApplicationUrl() {
        return applicationUrl;
    }

    public void setApplicationUrl(String applicationUrl) {
        this.applicationUrl = applicationUrl;
    }

    public Date getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(Date applicationDate) {
        this.applicationDate = applicationDate;
    }

    public Date getApplicationCreatedDate() {
        return applicationCreatedDate;
    }

    public void setApplicationCreatedDate(Date applicationCreatedDate) {
        this.applicationCreatedDate = applicationCreatedDate;
    }

    public Date getWorkOrderDate() {
        return workOrderDate;
    }

    public void setWorkOrderDate(Date workOrderDate) {
        this.workOrderDate = workOrderDate;
    }

    public Date getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(Date executionDate) {
        this.executionDate = executionDate;
    }

    public boolean isLegacy() {
        return legacy;
    }

    public void setLegacy(boolean legacy) {
        this.legacy = legacy;
    }

    public boolean isWorkOrderIssued() {
        return workOrderIssued;
    }

    public void setWorkOrderIssued(boolean workOrderIssued) {
        this.workOrderIssued = workOrderIssued;
    }

    public boolean isConnectionExecuted() {
        return connectionExecuted;
    }

    public void setConnectionExecuted(boolean connectionExecuted) {
        this.connectionExecuted = connectionExecuted;
    }

    public Double getBplConnection() {
        return bplConnection;
    }

    public void setBplConnection(Double bplConnection) {
        this.bplConnection = bplConnection;
    }

    public Double getNonBplConnection() {
        return nonBplConnection;
    }

    public void setNonBplConnection(Double nonBplConnection) {
        this.nonBplConnection = nonBplConnection;
    }

    public Double getBplSanctionOrder() {
        return bplSanctionOrder;
    }

    public void setBplSanctionOrder(Double bplSanctionOrder) {
        this.bplSanctionOrder = bplSanctionOrder;
    }

    public Double getNonBplSanctionOrder() {
        return nonBplSanctionOrder;
    }

    public void setNonBplSanctionOrder(Double nonBplSanctionOrder) {
        this.nonBplSanctionOrder = nonBplSanctionOrder;
    }

    public Double getBplExecution() {
        return bplExecution;
    }

    public void setBplExecution(Double bplExecution) {
        this.bplExecution = bplExecution;
    }

    public Double getNonBplExecution() {
        return nonBplExecution;
    }

    public void setNonBplExecution(Double nonBplExecution) {
        this.nonBplExecution = nonBplExecution;
    }

    public Double getCancelledConnection() {
        return cancelledConnection;
    }

    public void setCancelledConnection(Double cancelledConnection) {
        this.cancelledConnection = cancelledConnection;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }
}