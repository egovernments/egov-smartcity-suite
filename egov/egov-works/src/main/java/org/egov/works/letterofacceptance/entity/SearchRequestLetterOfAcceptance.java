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

    public void setWorkOrderNumber(final String workOrderNumber) {
        this.workOrderNumber = workOrderNumber;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(final Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(final Date toDate) {
        this.toDate = toDate;
    }

    public String getEstimateNumber() {
        return estimateNumber;
    }

    public void setEstimateNumber(final String estimateNumber) {
        this.estimateNumber = estimateNumber;
    }

    public Long getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(final Long departmentName) {
        this.departmentName = departmentName;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getEgwStatus() {
        return egwStatus;
    }

    public void setEgwStatus(final String egwStatus) {
        this.egwStatus = egwStatus;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(final String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public String getWorkIdentificationNumber() {
        return workIdentificationNumber;
    }

    public void setWorkIdentificationNumber(final String workIdentificationNumber) {
        this.workIdentificationNumber = workIdentificationNumber;
    }

}
