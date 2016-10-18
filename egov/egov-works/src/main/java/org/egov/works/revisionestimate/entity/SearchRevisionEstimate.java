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
package org.egov.works.revisionestimate.entity;

import java.math.BigDecimal;
import java.util.Date;

public class SearchRevisionEstimate {
    private Long id;
    private Long woId;
    private Long aeId;
    private String revisionEstimateNumber;
    private Date fromDate;
    private Date toDate;
    private Long status;
    private String revisionEstimateStatus;
    private Long createdBy;
    private String loaNumber;

    private String lineEstimateNumber;
    private Date reDate;
    private String contractorName;
    private String estimateNumber;
    private BigDecimal reValue;
    private String currentOwner;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getWoId() {
        return woId;
    }

    public void setWoId(final Long woId) {
        this.woId = woId;
    }

    public Long getAeId() {
        return aeId;
    }

    public void setAeId(final Long aeId) {
        this.aeId = aeId;
    }

    public String getRevisionEstimateNumber() {
        return revisionEstimateNumber;
    }

    public void setRevisionEstimateNumber(final String revisionEstimateNumber) {
        this.revisionEstimateNumber = revisionEstimateNumber;
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

    public Long getStatus() {
        return status;
    }

    public void setStatus(final Long status) {
        this.status = status;
    }

    public String getRevisionEstimateStatus() {
        return revisionEstimateStatus;
    }

    public void setRevisionEstimateStatus(final String revisionEstimateStatus) {
        this.revisionEstimateStatus = revisionEstimateStatus;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(final Long createdBy) {
        this.createdBy = createdBy;
    }

    public String getLoaNumber() {
        return loaNumber;
    }

    public void setLoaNumber(final String loaNumber) {
        this.loaNumber = loaNumber;
    }

    public String getLineEstimateNumber() {
        return lineEstimateNumber;
    }

    public void setLineEstimateNumber(final String lineEstimateNumber) {
        this.lineEstimateNumber = lineEstimateNumber;
    }

    public Date getReDate() {
        return reDate;
    }

    public void setReDate(final Date reDate) {
        this.reDate = reDate;
    }

    public String getContractorName() {
        return contractorName;
    }

    public void setContractorName(final String contractorName) {
        this.contractorName = contractorName;
    }

    public String getEstimateNumber() {
        return estimateNumber;
    }

    public void setEstimateNumber(final String estimateNumber) {
        this.estimateNumber = estimateNumber;
    }

    public BigDecimal getReValue() {
        return reValue;
    }

    public void setReValue(final BigDecimal reValue) {
        this.reValue = reValue;
    }

    public String getCurrentOwner() {
        return currentOwner;
    }

    public void setCurrentOwner(final String currentOwner) {
        this.currentOwner = currentOwner;
    }

}
