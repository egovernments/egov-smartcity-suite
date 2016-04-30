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
    private Date adminSanctionFromDate;
    private Date adminSanctionToDate;
    private Long typeOfWork;
    private Long subTypeOfWork;

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

    public Date getAdminSanctionFromDate() {
        return adminSanctionFromDate;
    }

    public void setAdminSanctionFromDate(Date adminSanctionFromDate) {
        this.adminSanctionFromDate = adminSanctionFromDate;
    }

    public Date getAdminSanctionToDate() {
        return adminSanctionToDate;
    }

    public void setAdminSanctionToDate(Date adminSanctionToDate) {
        this.adminSanctionToDate = adminSanctionToDate;
    }

    public Long getTypeOfWork() {
        return typeOfWork;
    }

    public void setTypeOfWork(Long typeOfWork) {
        this.typeOfWork = typeOfWork;
    }

    public Long getSubTypeOfWork() {
        return subTypeOfWork;
    }

    public void setSubTypeOfWork(Long subTypeOfWork) {
        this.subTypeOfWork = subTypeOfWork;
    }

}
