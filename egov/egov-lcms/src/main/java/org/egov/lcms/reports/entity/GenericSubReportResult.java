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

public class GenericSubReportResult {
    private String aggregatedBy;
    private LegalCase legalCase;
    private Long noOfCase;
    private String caseCategory;
    private String standingCounsel;
    private Integer courtType;
    private String courtName;
    private String judgmentType;
    private String petitionType;
    private Integer petitionTypeId;
    private String caseStatus;
    private Integer statusId;
    private Long officerIncharge;
    private Date fromDate;
    private Date toDate;
    private String aggregatedByValue;
    private String reportStatus;
    private Integer reportStatusId;
    private Integer casecategoryId;
    private Integer courtId;
    private Integer judgmentTypeId;

    public Integer getCourtId() {
        return courtId;
    }

    public void setCourtId(final Integer courtId) {
        this.courtId = courtId;
    }

    public Integer getJudgmentTypeId() {
        return judgmentTypeId;
    }

    public void setJudgmentTypeId(final Integer judgmentTypeId) {
        this.judgmentTypeId = judgmentTypeId;
    }

    public Integer getCasecategoryId() {
        return casecategoryId;
    }

    public void setCasecategoryId(final Integer casecategoryId) {
        this.casecategoryId = casecategoryId;
    }

    public String getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(final String reportStatus) {
        this.reportStatus = reportStatus;
    }

    public Integer getReportStatusId() {
        return reportStatusId;
    }

    public void setReportStatusId(final Integer reportStatusId) {
        this.reportStatusId = reportStatusId;
    }

    public String getAggregatedByValue() {
        return aggregatedByValue;
    }

    public void setAggregatedByValue(final String aggregatedByValue) {
        this.aggregatedByValue = aggregatedByValue;
    }

    public Integer getPetitionTypeId() {
        return petitionTypeId;
    }

    public void setPetitionTypeId(final Integer petitionTypeId) {
        this.petitionTypeId = petitionTypeId;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(final Integer statusId) {
        this.statusId = statusId;
    }

    public void setCaseStatus(final String caseStatus) {
        this.caseStatus = caseStatus;
    }

    public String getCaseStatus() {
        return caseStatus;
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

    public Integer getCourtType() {
        return courtType;
    }

    public void setCourtType(final Integer courtType) {
        this.courtType = courtType;
    }

    public String getJudgmentType() {
        return judgmentType;
    }

    public void setJudgmentType(final String judgmentType) {
        this.judgmentType = judgmentType;
    }

    public String getPetitionType() {
        return petitionType;
    }

    public void setPetitionType(final String petitionType) {
        this.petitionType = petitionType;
    }

    public String getCaseCategory() {
        return caseCategory;
    }

    public void setCaseCategory(final String caseCategory) {
        this.caseCategory = caseCategory;
    }

    public String getCourtName() {
        return courtName;
    }

    public void setCourtName(final String courtName) {
        this.courtName = courtName;
    }

    public String getStandingCounsel() {
        return standingCounsel;
    }

    public void setStandingCounsel(final String standingCounsel) {
        this.standingCounsel = standingCounsel;
    }

    public LegalCase getLegalCase() {
        return legalCase;
    }

    public void setLegalCase(final LegalCase legalCase) {
        this.legalCase = legalCase;
    }

    public Long getNoOfCase() {
        return noOfCase;
    }

    public void setNoOfCase(final Long noOfCase) {
        this.noOfCase = noOfCase;
    }

    public String getAggregatedBy() {
        return aggregatedBy;
    }

    public void setAggregatedBy(final String aggregatedBy) {
        this.aggregatedBy = aggregatedBy;
    }

    public Long getOfficerIncharge() {
        return officerIncharge;
    }

    public void setOfficerIncharge(final Long officerIncharge) {
        this.officerIncharge = officerIncharge;
    }

}
