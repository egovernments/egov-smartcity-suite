package org.egov.pgr.web.controller.reports;

import java.math.BigInteger;
import java.util.Date;

import org.joda.time.DateTime;

public class DrillDownReportResult {
    private String zoneName;
    private BigInteger registered=BigInteger.ZERO;
    private BigInteger inprocess=BigInteger.ZERO;
    private BigInteger Completed=BigInteger.ZERO;
    private BigInteger Rejected=BigInteger.ZERO;
    private String complainttype;
    private String name;
    private String crn;
    private Date createddate;
    private String complaintname;
    private String details;
    private String status;
    private String boundaryname;
    private BigInteger complaintid;
    
    
    public BigInteger getComplaintid() {
        return complaintid;
    }
    public void setComplaintid(BigInteger complaintid) {
        this.complaintid = complaintid;
    }
    public String getCrn() {
        return crn;
    }
    public void setCrn(String crn) {
        this.crn = crn;
    }
    public Date getCreateddate() {
        return createddate;
    }
    public void setCreateddate(Date createddate) {
        this.createddate = createddate;
    }
    public String getComplaintname() {
        return complaintname;
    }
    public void setComplaintname(String complaintname) {
        this.complaintname = complaintname;
    }
    public String getDetails() {
        return details;
    }
    public void setDetails(String details) {
        this.details = details;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getBoundaryname() {
        return boundaryname;
    }
    public void setBoundaryname(String boundaryname) {
        this.boundaryname = boundaryname;
    }
    private BigInteger total=BigInteger.ZERO;
    public String getZoneName() {
        return zoneName;
    }
    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }
   
    public BigInteger getRegistered() {
        return registered;
    }
    public void setRegistered(BigInteger registered) {
        this.registered = registered;
    }
    public BigInteger getInprocess() {
        return inprocess;
    }
    public void setInprocess(BigInteger inprocess) {
        this.inprocess = inprocess;
    }
    public BigInteger getCompleted() {
        return Completed;
    }
    public void setCompleted(BigInteger completed) {
        Completed = completed;
    }
    public BigInteger getRejected() {
        return Rejected;
    }
    public void setRejected(BigInteger rejected) {
        Rejected = rejected;
    }
    public String getComplainttype() {
        return complainttype;
    }
    public void setComplainttype(String complainttype) {
        this.complainttype = complainttype;
    }
    public BigInteger getTotal() {
        return registered.add(inprocess).add(Completed).add(Rejected);
    }
    public void setTotal(BigInteger total) {
        this.total = total;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
}
