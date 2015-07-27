package org.egov.pgr.web.controller.reports;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public class DrillDownReportResult {
    private String zoneName;
    private BigInteger registered = BigInteger.ZERO;
    private BigInteger inprocess = BigInteger.ZERO;
    private BigInteger Completed = BigInteger.ZERO;
    private BigInteger Rejected = BigInteger.ZERO;
    private BigInteger reopened = BigInteger.ZERO;
    private String complainttype;
    private String name;
    private String crn;
    private Date createddate;
    private String complaintname;
    private String details;
    private String status;
    private String boundaryname;
    private BigInteger complaintid;
    private BigDecimal complainttypeid; 



    public BigDecimal getComplainttypeid() {
        return complainttypeid;
    }

    public void setComplainttypeid(BigDecimal complainttypeid) {
        this.complainttypeid = complainttypeid;
    }

    public BigInteger getComplaintid() {
        return complaintid;
    }

    public void setComplaintid(final BigInteger complaintid) {
        this.complaintid = complaintid;
    }

    public String getCrn() {
        return crn;
    }

    public void setCrn(final String crn) {
        this.crn = crn;
    }

    public Date getCreateddate() {
        return createddate;
    }

    public void setCreateddate(final Date createddate) {
        this.createddate = createddate;
    }

    public String getComplaintname() {
        return complaintname;
    }

    public void setComplaintname(final String complaintname) {
        this.complaintname = complaintname;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(final String details) {
        this.details = details;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public String getBoundaryname() {
        return boundaryname;
    }

    public void setBoundaryname(final String boundaryname) {
        this.boundaryname = boundaryname;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(final String zoneName) {
        this.zoneName = zoneName;
    }

    public BigInteger getRegistered() {
        return registered;
    }

    public void setRegistered(final BigInteger registered) {
        this.registered = registered;
    }

    public BigInteger getInprocess() {
        return inprocess;
    }

    public void setInprocess(final BigInteger inprocess) {
        this.inprocess = inprocess;
    }

    public BigInteger getCompleted() {
        return Completed;
    }

    public void setCompleted(final BigInteger completed) {
        Completed = completed;
    }

    public BigInteger getRejected() {
        return Rejected;
    }

    public void setRejected(final BigInteger rejected) {
        Rejected = rejected;
    }

    public String getComplainttype() {
        return complainttype;
    }

    public void setComplainttype(final String complainttype) {
        this.complainttype = complainttype;
    }

    public BigInteger getTotal() {
        return registered.add(inprocess).add(Completed).add(Rejected).add(reopened);
    }

    public void setTotal(final BigInteger total) {
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public BigInteger getReopened() {
        return reopened;
    }

    public void setReopened(final BigInteger reopened) {
        this.reopened = reopened;
    }

}
