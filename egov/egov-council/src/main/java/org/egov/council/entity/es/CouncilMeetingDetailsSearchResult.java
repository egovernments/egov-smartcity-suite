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
    
    
    public String getCommitteeType() {
        return committeeType;
    }
    public void setCommitteeType(String committeeType) {
        this.committeeType = committeeType;
    }
    
    public Date getMeetingDate() {
        return meetingDate;
    }
    public void setMeetingDate(Date meetingDate) {
        this.meetingDate = meetingDate;
    }
    public String getMeetingLocation() {
        return meetingLocation;
    }
    public void setMeetingLocation(String meetingLocation) {
        this.meetingLocation = meetingLocation;
    }
    public String getMeetingTime() {
        return meetingTime;
    }
    public void setMeetingTime(String meetingTime) {
        this.meetingTime = meetingTime;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getMeetingNumber() {
        return meetingNumber;
    }
    public void setMeetingNumber(String meetingNumber) {
        this.meetingNumber = meetingNumber;
    }
    public Integer getMeetingCount() {
        return meetingCount;
    }
    public void setMeetingCount(Integer meetingCount) {
        this.meetingCount = meetingCount;
    }
    public Integer getTotalPreambles() {
        return totalPreambles;
    }
    public void setTotalPreambles(Integer totalPreambles) {
        this.totalPreambles = totalPreambles;
    }
    public Integer getApprovedPreambles() {
        return approvedPreambles;
    }
    public void setApprovedPreambles(Integer approvedPreambles) {
        this.approvedPreambles = approvedPreambles;
    }
    public Integer getAdjournedPreambles() {
        return adjournedPreambles;
    }
    public void setAdjournedPreambles(Integer adjournedPreambles) {
        this.adjournedPreambles = adjournedPreambles;
    }
    public Integer getRejectedPreambles() {
        return rejectedPreambles;
    }
    public void setRejectedPreambles(Integer rejectedPreambles) {
        this.rejectedPreambles = rejectedPreambles;
    }
    public Integer getTotalNoOfCommitteMembers() {
        return totalNoOfCommitteMembers;
    }
    public void setTotalNoOfCommitteMembers(Integer totalNoOfCommitteMembers) {
        this.totalNoOfCommitteMembers = totalNoOfCommitteMembers;
    }
    public Integer getNoOfCommitteMembersPresent() {
        return noOfCommitteMembersPresent;
    }
    public void setNoOfCommitteMembersPresent(Integer noOfCommitteMembersPresent) {
        this.noOfCommitteMembersPresent = noOfCommitteMembersPresent;
    }
    public Integer getNoOfCommitteMembersAbsent() {
        return noOfCommitteMembersAbsent;
    }
    public void setNoOfCommitteMembersAbsent(Integer noOfCommitteMembersAbsent) {
        this.noOfCommitteMembersAbsent = noOfCommitteMembersAbsent;
    }
    
    
    
}
