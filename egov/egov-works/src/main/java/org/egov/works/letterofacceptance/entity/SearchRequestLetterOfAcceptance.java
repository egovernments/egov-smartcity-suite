package org.egov.works.letterofacceptance.entity;

import java.util.Date;

public class SearchRequestLetterOfAcceptance {
    
    private String workOrderNumber;
    private Date fromDate;
    private Date toDate;
    private String name;
    private String fileNumber;
    private Long departmentName;
    private String estimateNumber;
    private String egwStatus;
    private String workIdentificationNumber;
    
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

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public String getWorkIdentificationNumber() {
        return workIdentificationNumber;
    }

    public void setWorkIdentificationNumber(String workIdentificationNumber) {
        this.workIdentificationNumber = workIdentificationNumber;
    }

}
