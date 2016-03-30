package org.egov.works.letterofacceptance.entity;

import java.util.Date;

public class SearchRequestLetterOfAcceptance {
    
    private String workOrderNumber;
    private Date fromDate;
    private Date toDate;
    private String name;
    private String tenderFileNumber;
    private Long departmentName;
    private String estimateNumber;
    private String egwStatus;
    
    public SearchRequestLetterOfAcceptance() {
    }

    public String getWorkOrderNumber() {
        return workOrderNumber;
    }

    public void setWorkOrderNumber(String workOrderNumber) {
        this.workOrderNumber = workOrderNumber;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public String getEstimateNumber() {
        return estimateNumber;
    }

    public void setEstimateNumber(String estimateNumber) {
        this.estimateNumber = estimateNumber;
    }

    public String getTenderFileNumber() {
        return tenderFileNumber;
    }

    public void setTenderFileNumber(String tenderFileNumber) {
        this.tenderFileNumber = tenderFileNumber;
    }

    public Long getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(Long departmentName) {
        this.departmentName = departmentName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEgwStatus() {
        return egwStatus;
    }

    public void setEgwStatus(String egwStatus) {
        this.egwStatus = egwStatus;
    }

/*    public Long getName() {
        return name;
    }

    public void setName(Long name) {
        this.name = name;
    }*/

}
