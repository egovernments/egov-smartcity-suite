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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

import static org.apache.commons.lang.StringUtils.EMPTY;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WaterChargeSurveyDashboardResponse {

    private String ulbGrade = EMPTY;
    private String ulbCode = EMPTY;
    private String ulbName = EMPTY;
    private String regionName = EMPTY;
    private String districtName = EMPTY;
    private String wardName = EMPTY;
    private String localityName = EMPTY;
    private String functionaryName = EMPTY;
    private String applicationNumber = EMPTY;
    private String address = EMPTY;
    private String applicantName = EMPTY;
    private String connectionStatus = EMPTY;
    private String applicationURL = EMPTY;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date applicationDate;
    private Date workOrderDate;
    private Date executionDate;
    private WaterChargeConnectionCount applicaitonReceived = new WaterChargeConnectionCount();
    private WaterChargeConnectionCount sanctionOrderIssued = new WaterChargeConnectionCount();
    private WaterChargeConnectionCount connectionExecuted = new WaterChargeConnectionCount();
    private double pendingSanction;
    private double pendingExecution;
    private double sanctionAverageDays;
    private double connectionAverageDays;


    public String getUlbGrade() {
        return ulbGrade;
    }

    public void setUlbGrade(String ulbGrade) {
        this.ulbGrade = ulbGrade;
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

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getWardName() {
        return wardName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }

    public String getLocalityName() {
        return localityName;
    }

    public void setLocalityName(String localityName) {
        this.localityName = localityName;
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

    public Date getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(Date applicationDate) {
        this.applicationDate = applicationDate;
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

    public WaterChargeConnectionCount getApplicaitonReceived() {
        return applicaitonReceived;
    }

    public void setApplicaitonReceived(WaterChargeConnectionCount applicaitonReceived) {
        this.applicaitonReceived = applicaitonReceived;
    }

    public WaterChargeConnectionCount getSanctionOrderIssued() {
        return sanctionOrderIssued;
    }

    public void setSanctionOrderIssued(WaterChargeConnectionCount sanctionOrderIssued) {
        this.sanctionOrderIssued = sanctionOrderIssued;
    }

    public WaterChargeConnectionCount getConnectionExecuted() {
        return connectionExecuted;
    }

    public void setConnectionExecuted(WaterChargeConnectionCount connectionExecuted) {
        this.connectionExecuted = connectionExecuted;
    }

    public double getPendingSanction() {
        return pendingSanction;
    }

    public void setPendingSanction(double pendingSanction) {
        this.pendingSanction = pendingSanction;
    }

    public double getPendingExecution() {
        return pendingExecution;
    }

    public void setPendingExecution(double pendingExecution) {
        this.pendingExecution = pendingExecution;
    }

    public double getSanctionAverageDays() {
        return sanctionAverageDays;
    }

    public void setSanctionAverageDays(double sanctionAverageDays) {
        this.sanctionAverageDays = sanctionAverageDays;
    }

    public double getConnectionAverageDays() {
        return connectionAverageDays;
    }

    public void setConnectionAverageDays(double connectionAverageDays) {
        this.connectionAverageDays = connectionAverageDays;
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

    public String getConnectionStatus() {
        return connectionStatus;
    }

    public void setConnectionStatus(String connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    public String getApplicationURL() {
        return applicationURL;
    }

    public void setApplicationURL(String applicationURL) {
        this.applicationURL = applicationURL;
    }
}
