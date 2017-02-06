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
package org.egov.works.abstractestimate.entity;

import java.util.Date;

public class SearchAbstractEstimate {
    private Long id;
    private String abstractEstimateNumber;
    private Date fromDate;
    private Date toDate;
    private String status;
    private Long department;
    private Long createdBy;
    private String workIdentificationNumber;

    private String lineEstimateNumber;
    private String estimateNumberAndDate;
    private String estimateAmount;
    private String departmentName;
    private String ward;
    private String currentOwner;
    private Boolean spillOverFlag;

    public String getAbstractEstimateNumber() {
        return abstractEstimateNumber;
    }

    public void setAbstractEstimateNumber(final String abstractEstimateNumber) {
        this.abstractEstimateNumber = abstractEstimateNumber;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public Long getDepartment() {
        return department;
    }

    public void setDepartment(final Long department) {
        this.department = department;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(final Long createdBy) {
        this.createdBy = createdBy;
    }

    public String getWorkIdentificationNumber() {
        return workIdentificationNumber;
    }

    public void setWorkIdentificationNumber(final String workIdentificationNumber) {
        this.workIdentificationNumber = workIdentificationNumber;
    }

    public String getLineEstimateNumber() {
        return lineEstimateNumber;
    }

    public void setLineEstimateNumber(final String lineEstimateNumber) {
        this.lineEstimateNumber = lineEstimateNumber;
    }

    public String getEstimateNumberAndDate() {
        return estimateNumberAndDate;
    }

    public void setEstimateNumberAndDate(final String estimateNumberAndDate) {
        this.estimateNumberAndDate = estimateNumberAndDate;
    }

    public String getEstimateAmount() {
        return estimateAmount;
    }

    public void setEstimateAmount(final String estimateAmount) {
        this.estimateAmount = estimateAmount;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(final String departmentName) {
        this.departmentName = departmentName;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(final String ward) {
        this.ward = ward;
    }

    public String getCurrentOwner() {
        return currentOwner;
    }

    public void setCurrentOwner(final String currentOwner) {
        this.currentOwner = currentOwner;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Boolean getSpillOverFlag() {
        return spillOverFlag;
    }

    public void setSpillOverFlag(final Boolean spillOverFlag) {
        this.spillOverFlag = spillOverFlag;
    }
}