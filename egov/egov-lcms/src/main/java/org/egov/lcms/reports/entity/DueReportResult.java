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

public class DueReportResult {
    private String caseNumber;

    private String lcNumber;
    private Date caseFromDate;
    private LegalCase legalCase;
    private Date caseToDate;
    private String courtName;
    private Date hearingDate;
    private Date judgementImplDate;
    private String reportBy;
    private Boolean isStatusExcluded;
    private String caseStatus;
    private String petitionType;
    private Long officerIncharge;
    private String officerInChargeName;

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

    public LegalCase getLegalCase() {
        return legalCase;
    }

    public void setLegalCase(final LegalCase legalCase) {
        this.legalCase = legalCase;
    }

    public String getReportBy() {
        return reportBy;
    }

    public void setReportBy(final String reportBy) {
        this.reportBy = reportBy;
    }

    public Date getHearingDate() {
        return hearingDate;
    }

    public void setHearingDate(final Date hearingDate) {
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

    public Long getOfficerIncharge() {
        return officerIncharge;
    }

    public void setOfficerIncharge(final Long officerIncharge) {
        this.officerIncharge = officerIncharge;
    }

    public String getOfficerInChargeName() {
        return officerInChargeName;
    }

    public void setOfficerInChargeName(final String officerInChargeName) {
        this.officerInChargeName = officerInChargeName;
    }

}
