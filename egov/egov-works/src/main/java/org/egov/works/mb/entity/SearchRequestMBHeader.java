package org.egov.works.mb.entity;

import java.util.Date;

public class SearchRequestMBHeader {
    private String mbReferenceNumber;
    private String workOrderNumber;
    private String contractorName;
    private Date fromDate;
    private Date toDate;
    private Long department;
    private Long createdBy;
    private String estimateNumber;
    private Long mbStatus;

    public String getMbReferenceNumber() {
        return mbReferenceNumber;
    }

    public void setMbReferenceNumber(final String mbReferenceNumber) {
        this.mbReferenceNumber = mbReferenceNumber;
    }

    public String getWorkOrderNumber() {
        return workOrderNumber;
    }

    public void setWorkOrderNumber(final String workOrderNumber) {
        this.workOrderNumber = workOrderNumber;
    }

    public String getContractorName() {
        return contractorName;
    }

    public void setContractorName(final String contractorName) {
        this.contractorName = contractorName;
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

    public Long getDepartment() {
        return department;
    }

    public void setDepartment(final Long department) {
        this.department = department;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(final Long createdBy) {
        this.createdBy = createdBy;
    }

    public String getEstimateNumber() {
        return estimateNumber;
    }

    public void setEstimateNumber(final String estimateNumber) {
        this.estimateNumber = estimateNumber;
    }

    public Long getMbStatus() {
        return mbStatus;
    }

    public void setMbStatus(final Long mbStatus) {
        this.mbStatus = mbStatus;
    }

}
