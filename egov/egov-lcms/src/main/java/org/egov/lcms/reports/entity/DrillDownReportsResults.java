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
package org.egov.lcms.reports.entity;

import java.util.Date;

import org.egov.lcms.transactions.entity.LegalCase;

public class DrillDownReportsResults {
    private String caseNumber;
    private String lcNumber;
    private Date caseFromDate;
    private LegalCase legalCase;
    private Date caseToDate;
    private String standingCouncil;
    private Integer casecategory;
    private String petitionType;
    private Integer petitionTypeId;
    private Integer courtType;
    private String courtName;
    private Integer courtId;
    private String govtDept;
    private String caseTitle;
    private String petName;
    private String resName;
    private String caseStatus;
    private Integer statusId;
    private String assignDept;
    private String aggregatedBy;
    private String period;
    private Date fromDate;
    private Date toDate;
    private Integer year;
    private String month;
    private Long count;

    public String getAggregatedBy() {
        return aggregatedBy;
    }

    public void setAggregatedBy(final String aggregatedBy) {
        this.aggregatedBy = aggregatedBy;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(final String period) {
        this.period = period;
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

    public Integer getYear() {
        return year;
    }

    public void setYear(final Integer year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(final String month) {
        this.month = month;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(final Long count) {
        this.count = count;
    }

    public String getStandingCouncil() {
        return standingCouncil;
    }

    public void setStandingCouncil(final String standingCouncil) {
        this.standingCouncil = standingCouncil;
    }

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(final String caseNumber) {
        this.caseNumber = caseNumber;
    }

    public String getLcNumber() {
        return lcNumber;
    }

    public Date getCaseFromDate() {
        return caseFromDate;
    }

    public void setCaseFromDate(final Date caseFromDate) {
        this.caseFromDate = caseFromDate;
    }

    public Date getCaseToDate() {
        return caseToDate;
    }

    public void setCaseToDate(final Date caseToDate) {
        this.caseToDate = caseToDate;
    }

    public void setLcNumber(final String lcNumber) {
        this.lcNumber = lcNumber;
    }

    public String getPetitionType() {
        return petitionType;
    }

    public void setPetitionType(final String petitionType) {
        this.petitionType = petitionType;
    }

    public Integer getCasecategory() {
        return casecategory;
    }

    public void setCasecategory(final Integer casecategory) {
        this.casecategory = casecategory;
    }

    public Integer getCourtType() {
        return courtType;
    }

    public void setCourtType(final Integer courtType) {
        this.courtType = courtType;
    }

    public String getCourtName() {
        return courtName;
    }

    public void setCourtName(final String courtName) {
        this.courtName = courtName;
    }

    public Integer getCourtId() {
        return courtId;
    }

    public void setCourtId(final Integer courtId) {
        this.courtId = courtId;
    }

    public String getGovtDept() {
        return govtDept;
    }

    public void setGovtDept(final String govtDept) {
        this.govtDept = govtDept;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(final String petName) {
        this.petName = petName;
    }

    public String getCaseStatus() {
        return caseStatus;
    }

    public void setCaseStatus(final String caseStatus) {
        this.caseStatus = caseStatus;
    }

    public String getCaseTitle() {
        return caseTitle;
    }

    public void setCaseTitle(final String caseTitle) {
        this.caseTitle = caseTitle;
    }

    public String getAssignDept() {
        return assignDept;
    }

    public void setAssignDept(final String assignDept) {
        this.assignDept = assignDept;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(final Integer statusId) {
        this.statusId = statusId;
    }

    public Integer getPetitionTypeId() {
        return petitionTypeId;
    }

    public void setPetitionTypeId(final Integer petitionTypeId) {
        this.petitionTypeId = petitionTypeId;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(final String resName) {
        this.resName = resName;
    }

    public LegalCase getLegalCase() {
        return legalCase;
    }

    public void setLegalCase(final LegalCase legalCase) {
        this.legalCase = legalCase;
    }

}
