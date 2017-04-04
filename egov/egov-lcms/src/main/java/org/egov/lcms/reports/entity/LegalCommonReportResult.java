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

public class LegalCommonReportResult {
    private String caseNumber;
    private String lcNumber;
    private String caseFromDate;
    private String caseToDate;
    private String courtName;
    private String hearingDate;
    private Date judgementImplDate;
    private String reportBy;
    private Boolean isStatusExcluded;
    private String caseStatus;
    private String petitionType;
    private String officerIncharge;
    private String judgmentImplDate;
    private String caseTitle;
    private String standingCounsel;
    private String nextDate;
    private String pwrDueDate;
    private String caDueDate;
    private String caseCategory;
    private String courtType;
    private String petitionerName;
    private String respondantName;
    private String aggregatedBy;
    private String period;
    private String year;
    private String month;
    private Long count;
    private String aggregatedByValue;
    private String judgmentType;
    private String reportStatus;

    public String getCaseFromDate() {
        return caseFromDate;
    }

    public void setCaseFromDate(String caseFromDate) {
        this.caseFromDate = caseFromDate;
    }

    public String getCaseToDate() {
        return caseToDate;
    }

    public void setCaseToDate(String caseToDate) {
        this.caseToDate = caseToDate;
    }

  
    public String getLcNumber() {
        return lcNumber;
    }

    public void setLcNumber(final String lcNumber) {
        this.lcNumber = lcNumber;
    }

    public String getPwrDueDate() {
        return pwrDueDate;
    }

    public void setPwrDueDate(final String pwrDueDate) {
        this.pwrDueDate = pwrDueDate;
    }

    public String getCaDueDate() {
        return caDueDate;
    }

    public void setCaDueDate(final String caDueDate) {
        this.caDueDate = caDueDate;
    }

    public String getCaseTitle() {
        return caseTitle;
    }

    public void setCaseTitle(final String caseTitle) {
        this.caseTitle = caseTitle;
    }

    public String getStandingCounsel() {
        return standingCounsel;
    }

    public void setStandingCounsel(final String standingCounsel) {
        this.standingCounsel = standingCounsel;
    }

    public String getNextDate() {
        return nextDate;
    }

    public void setNextDate(final String nextDate) {
        this.nextDate = nextDate;
    }

    public String getJudgmentImplDate() {
        return judgmentImplDate;
    }

    public void setJudgmentImplDate(final String judgmentImplDate) {
        this.judgmentImplDate = judgmentImplDate;
    }

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(final String caseNumber) {
        this.caseNumber = caseNumber;
    }

    public String getCourtName() {
        return courtName;
    }

    public void setCourtName(final String courtName) {
        this.courtName = courtName;
    }

    public String getCaseStatus() {
        return caseStatus;
    }

    public void setCaseStatus(final String caseStatus) {
        this.caseStatus = caseStatus;
    }

    public Boolean getIsStatusExcluded() {
        return isStatusExcluded;
    }

    public void setIsStatusExcluded(final Boolean isStatusExcluded) {
        this.isStatusExcluded = isStatusExcluded;
    }

    public String getReportBy() {
        return reportBy;
    }

    public void setReportBy(final String reportBy) {
        this.reportBy = reportBy;
    }

    public String getHearingDate() {
        return hearingDate;
    }

    public void setHearingDate(final String hearingDate) {
        this.hearingDate = hearingDate;
    }

    public Date getJudgementImplDate() {
        return judgementImplDate;
    }

    public void setJudgementImplDate(final Date judgementImplDate) {
        this.judgementImplDate = judgementImplDate;
    }

    public String getPetitionType() {
        return petitionType;
    }

    public void setPetitionType(final String petitionType) {
        this.petitionType = petitionType;
    }

    public String getOfficerIncharge() {
        return officerIncharge;
    }

    public void setOfficerIncharge(final String officerIncharge) {
        this.officerIncharge = officerIncharge;
    }

    public String getCaseCategory() {
        return caseCategory;
    }

    public void setCaseCategory(final String caseCategory) {
        this.caseCategory = caseCategory;
    }

    public String getCourtType() {
        return courtType;
    }

    public void setCourtType(final String courtType) {
        this.courtType = courtType;
    }

    public String getPetitionerName() {
        return petitionerName;
    }

    public void setPetitionerName(final String petitionerName) {
        this.petitionerName = petitionerName;
    }

    public String getRespondantName() {
        return respondantName;
    }

    public void setRespondantName(final String respondantName) {
        this.respondantName = respondantName;
    }

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

    public String getYear() {
        return year;
    }

    public void setYear(final String year) {
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

    public String getAggregatedByValue() {
        return aggregatedByValue;
    }

    public void setAggregatedByValue(final String aggregatedByValue) {
        this.aggregatedByValue = aggregatedByValue;
    }

    public String getJudgmentType() {
        return judgmentType;
    }

    public void setJudgmentType(final String judgmentType) {
        this.judgmentType = judgmentType;
    }

    public String getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(final String reportStatus) {
        this.reportStatus = reportStatus;
    }

}
