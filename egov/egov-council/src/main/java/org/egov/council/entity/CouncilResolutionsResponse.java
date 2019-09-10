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
package org.egov.council.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class CouncilResolutionsResponse {

    private String meetingType;
    private String committeeType;
    private String department;
    private String agendaNo;
    private String serialNo;
    private String preambleNo;
    private String resolutionNo;
    private String status;
    private BigDecimal sanctionAmount;
    private String gistOfPreamble;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date resolutionDate;
    private String councilResolutionUrl;
    private String errorMessage;

    public String getMeetingType() {
        return meetingType;
    }

    public String getCommitteeType() {
        return committeeType;
    }

    public String getDepartment() {
        return department;
    }

    public String getAgendaNo() {
        return agendaNo;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public String getPreambleNo() {
        return preambleNo;
    }

    public String getResolutionNo() {
        return resolutionNo;
    }

    public String getStatus() {
        return status;
    }

    public BigDecimal getSanctionAmount() {
        return sanctionAmount;
    }

    public void setMeetingType(String meetingType) {
        this.meetingType = meetingType;
    }

    public void setCommitteeType(String committeeType) {
        this.committeeType = committeeType;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setAgendaNo(String agendaNo) {
        this.agendaNo = agendaNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public void setPreambleNo(String preambleNo) {
        this.preambleNo = preambleNo;
    }

    public void setResolutionNo(String resolutionNo) {
        this.resolutionNo = resolutionNo;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setSanctionAmount(BigDecimal sanctionAmount) {
        this.sanctionAmount = sanctionAmount;
    }

    public String getGistOfPreamble() {
        return gistOfPreamble;
    }

    public void setGistOfPreamble(String gistOfPreamble) {
        this.gistOfPreamble = gistOfPreamble;
    }

    public String getCouncilResolutionUrl() {
        return councilResolutionUrl;
    }

    public void setCouncilResolutionUrl(String councilResolutionUrl) {
        this.councilResolutionUrl = councilResolutionUrl;
    }

    public Date getResolutionDate() {
        return resolutionDate;
    }

    public void setResolutionDate(Date resolutionDate) {
        this.resolutionDate = resolutionDate;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
