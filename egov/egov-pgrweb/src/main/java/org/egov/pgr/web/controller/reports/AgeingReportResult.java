package org.egov.pgr.web.controller.reports;

import java.math.BigInteger;

public class AgeingReportResult {
    private String zoneName;
    private BigInteger grtthn90=BigInteger.ZERO;
    private BigInteger btw45to90=BigInteger.ZERO;
    private BigInteger btw15to45=BigInteger.ZERO;
    private BigInteger lsthn15=BigInteger.ZERO;
    private String complainttype;
    private String name;
    
    private BigInteger total=BigInteger.ZERO;
    public String getZoneName() {
        return zoneName;
    }
    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }
    public BigInteger getGrtthn90() {
        return grtthn90;
    }
    public void setGrtthn90(BigInteger grtthn90) {
        this.grtthn90 = grtthn90;
    }
    public BigInteger getBtw45to90() {
        return btw45to90;
    }
    public void setBtw45to90(BigInteger btw45to90) {
        this.btw45to90 = btw45to90;
    }
    public BigInteger getBtw15to45() {
        return btw15to45;
    }
    public void setBtw15to45(BigInteger btw15to45) {
        this.btw15to45 = btw15to45;
    }
    public BigInteger getLsthn15() {
        return lsthn15;
    }
    public void setLsthn15(BigInteger lsthn15) {
        this.lsthn15 = lsthn15;
    }
    public String getComplainttype() {
        return complainttype;
    }
    public void setComplainttype(String complainttype) {
        this.complainttype = complainttype;
    }
    public BigInteger getTotal() {
        return grtthn90.add(btw45to90).add(btw15to45).add(lsthn15);
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
