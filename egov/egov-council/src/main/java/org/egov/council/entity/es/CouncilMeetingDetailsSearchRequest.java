package org.egov.council.entity.es;

import java.util.Date;

public class CouncilMeetingDetailsSearchRequest {
    private String committeeType;
    private String meetingNumber;
    private Date from;
    private Date to;
    private String ulbName;


    public String getCommitteeType() {
        return committeeType;
    }

    public void setCommitteeType(String committeeType) {
        this.committeeType = committeeType;
    }

    public String getMeetingNumber() {
        return meetingNumber;
    }

    public void setMeetingNumber(String meetingNumber) {
        this.meetingNumber = meetingNumber;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public String getUlbName() {
        return ulbName;
    }

    public void setUlbName(String ulbName) {
        this.ulbName = ulbName;
    }

}
