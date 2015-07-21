package org.egov.pgr.web.controller.reports;

import java.math.BigInteger;

public class DrillDownReportResult {
    private String zoneName;
    private BigInteger registered=BigInteger.ZERO;
    private BigInteger inprocess=BigInteger.ZERO;
    private BigInteger Completed=BigInteger.ZERO;
    private BigInteger Rejected=BigInteger.ZERO;
    private String complainttype;
    private String name;
    
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
