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

package org.egov.council.entity.es;

import java.util.Date;

public class CouncilMeetingDetailsSearchResult {

    private String committeeType;
    private Date meetingDate;
    private String meetingLocation;
    private String status;
    private String meetingNumber;
    private String meetingTime;
    private Integer meetingCount;
    private Integer totalPreambles;
    private Integer approvedPreambles;
    private Integer adjournedPreambles;
    private Integer rejectedPreambles;
    private Integer totalNoOfCommitteMembers;
    private Integer noOfCommitteMembersPresent;
    private Integer noOfCommitteMembersAbsent;
    private Long id;

    public String getCommitteeType() {
        return committeeType;
    }

    public void setCommitteeType(final String committeeType) {
        this.committeeType = committeeType;
    }

    public Date getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(final Date meetingDate) {
        this.meetingDate = meetingDate;
    }

    public String getMeetingLocation() {
        return meetingLocation;
    }

    public void setMeetingLocation(final String meetingLocation) {
        this.meetingLocation = meetingLocation;
    }

    public String getMeetingTime() {
        return meetingTime;
    }

    public void setMeetingTime(final String meetingTime) {
        this.meetingTime = meetingTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public String getMeetingNumber() {
        return meetingNumber;
    }

    public void setMeetingNumber(final String meetingNumber) {
        this.meetingNumber = meetingNumber;
    }

    public Integer getMeetingCount() {
        return meetingCount;
    }

    public void setMeetingCount(final Integer meetingCount) {
        this.meetingCount = meetingCount;
    }

    public Integer getTotalPreambles() {
        return totalPreambles;
    }

    public void setTotalPreambles(final Integer totalPreambles) {
        this.totalPreambles = totalPreambles;
    }

    public Integer getApprovedPreambles() {
        return approvedPreambles;
    }

    public void setApprovedPreambles(final Integer approvedPreambles) {
        this.approvedPreambles = approvedPreambles;
    }

    public Integer getAdjournedPreambles() {
        return adjournedPreambles;
    }

    public void setAdjournedPreambles(final Integer adjournedPreambles) {
        this.adjournedPreambles = adjournedPreambles;
    }

    public Integer getRejectedPreambles() {
        return rejectedPreambles;
    }

    public void setRejectedPreambles(final Integer rejectedPreambles) {
        this.rejectedPreambles = rejectedPreambles;
    }

    public Integer getTotalNoOfCommitteMembers() {
        return totalNoOfCommitteMembers;
    }

    public void setTotalNoOfCommitteMembers(final Integer totalNoOfCommitteMembers) {
        this.totalNoOfCommitteMembers = totalNoOfCommitteMembers;
    }

    public Integer getNoOfCommitteMembersPresent() {
        return noOfCommitteMembersPresent;
    }

    public void setNoOfCommitteMembersPresent(final Integer noOfCommitteMembersPresent) {
        this.noOfCommitteMembersPresent = noOfCommitteMembersPresent;
    }

    public Integer getNoOfCommitteMembersAbsent() {
        return noOfCommitteMembersAbsent;
    }

    public void setNoOfCommitteMembersAbsent(final Integer noOfCommitteMembersAbsent) {
        this.noOfCommitteMembersAbsent = noOfCommitteMembersAbsent;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

}
