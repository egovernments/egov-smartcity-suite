package org.egov.works.letterofacceptance.entity;

import java.util.Date;

public class SearchResultLetterOfAcceptance {
    private Integer srlNo;
    private String workOrderNumber;
    private String estimateNumber;
    private Long typeOfWork;
    private Long subTypeOfWork;
    private Date estimateDate;
    private String nameOfTheWork;
    private String workIdentificationNumber;
    private Date workOrderDate;
    private String workOrderAmount;

    public String getWorkOrderNumber() {
        return workOrderNumber;
    }

    public void setWorkOrderNumber(final String workOrderNumber) {
        this.workOrderNumber = workOrderNumber;
    }

    public String getEstimateNumber() {
        return estimateNumber;
    }

    public void setEstimateNumber(final String estimateNumber) {
        this.estimateNumber = estimateNumber;
    }

    public Date getEstimateDate() {
        return estimateDate;
    }

    public void setEstimateDate(final Date estimateDate) {
        this.estimateDate = estimateDate;
    }

    public String getNameOfTheWork() {
        return nameOfTheWork;
    }

    public void setNameOfTheWork(final String nameOfTheWork) {
        this.nameOfTheWork = nameOfTheWork;
    }

    public String getWorkIdentificationNumber() {
        return workIdentificationNumber;
    }

    public void setWorkIdentificationNumber(final String workIdentificationNumber) {
        this.workIdentificationNumber = workIdentificationNumber;
    }

    public Date getWorkOrderDate() {
        return workOrderDate;
    }

    public void setWorkOrderDate(final Date workOrderDate) {
        this.workOrderDate = workOrderDate;
    }

    public String getWorkOrderAmount() {
        return workOrderAmount;
    }

    public void setWorkOrderAmount(final String workOrderAmount) {
        this.workOrderAmount = workOrderAmount;
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

    public Integer getSrlNo() {
        return srlNo;
    }

    public void setSrlNo(Integer srlNo) {
        this.srlNo = srlNo;
    }

}
