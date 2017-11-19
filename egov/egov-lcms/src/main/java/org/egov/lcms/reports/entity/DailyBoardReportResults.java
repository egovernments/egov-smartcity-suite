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
package org.egov.lcms.reports.entity;

import org.egov.lcms.transactions.entity.LegalCase;

public class DailyBoardReportResults {
    private String caseNumber;
    private String fromDate;
    private LegalCase legalCase;
    private String toDate;
    private String standingCouncil;
    private String caseCategory;
    private String petitionType;
    private String courtType;
    private String courtName;
    private String caseTitle;
    private String petitionerName;
    private String respondantName;
    private String caseStatus;
    private String nextDate;
    private String officerIncharge;
    private String lcNumber;

    public String getCourtType() {
        return courtType;
    }

    public String getLcNumber() {
        return lcNumber;
    }

    public void setLcNumber(final String lcNumber) {
        this.lcNumber = lcNumber;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(final String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(final String toDate) {
        this.toDate = toDate;
    }

    public String getCaseCategory() {
        return caseCategory;
    }

    public void setCaseCategory(final String caseCategory) {
        this.caseCategory = caseCategory;
    }

    public void setCourtType(final String courtType) {
        this.courtType = courtType;
    }

    public String getNextDate() {
        return nextDate;
    }

    public void setNextDate(final String nextDate) {
        this.nextDate = nextDate;
    }

    public String getOfficerIncharge() {
        return officerIncharge;
    }

    public void setOfficerIncharge(final String officerIncharge) {
        this.officerIncharge = officerIncharge;
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

    public String getPetitionType() {
        return petitionType;
    }

    public void setPetitionType(final String petitionType) {
        this.petitionType = petitionType;
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

    public String getCaseTitle() {
        return caseTitle;
    }

    public void setCaseTitle(final String caseTitle) {
        this.caseTitle = caseTitle;
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

    public LegalCase getLegalCase() {
        return legalCase;
    }

    public void setLegalCase(final LegalCase legalCase) {
        this.legalCase = legalCase;
    }

}
