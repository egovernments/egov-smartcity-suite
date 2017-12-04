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

import java.util.Date;

public class WaterConnExecutionDetails {
    private Long id;
    private String applicationNumber;
    private String consumerNumber;
    private Date fromDate;
    private Date toDate;
    private String revenueWard;
    private String applicationType;
    private String ownerName;
    private String applicationStatus;
    private String approvalDate;
    private String executionDate;
    private String meterMaker;
    private String initialReading;
    private String meterSerialNumber;

    public String getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(final String executionDate) {
        this.executionDate = executionDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public void setApplicationNumber(final String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }

    public String getConsumerNumber() {
        return consumerNumber;
    }

    public void setConsumerNumber(final String consumerNumber) {
        this.consumerNumber = consumerNumber;
    }

    public String getRevenueWard() {
        return revenueWard;
    }

    public void setRevenueWard(final String revenueWard) {
        this.revenueWard = revenueWard;
    }

    public String getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(final String applicationType) {
        this.applicationType = applicationType;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(final String ownerName) {
        this.ownerName = ownerName;
    }

    public String getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(final String approvalDate) {
        this.approvalDate = approvalDate;
    }

    public String getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(final String applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(final Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(final Date toDate) {
        this.toDate = toDate;
    }

    public String getMeterMaker() {
        return meterMaker;
    }

    public void setMeterMaker(final String meterMaker) {
        this.meterMaker = meterMaker;
    }

    public String getInitialReading() {
        return initialReading;
    }

    public void setInitialReading(final String initialReading) {
        this.initialReading = initialReading;
    }

    public String getMeterSerialNumber() {
        return meterSerialNumber;
    }

    public void setMeterSerialNumber(final String meterSerialNumber) {
        this.meterSerialNumber = meterSerialNumber;
    }

}
