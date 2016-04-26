package org.egov.works.contractorbill.entity;

import java.util.Date;

public class SearchRequestContractorBill {

    private Date billFromDate;
    private Date billToDate;
    private String billType;
    private String billNumber;
    private String status;
    private String workIdentificationNumber;
    private String contractorName;
    private Long department;
    private boolean spillOverFlag;

    public SearchRequestContractorBill() {
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(final String billType) {
        this.billType = billType;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(final String billNumber) {
        this.billNumber = billNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public String getWorkIdentificationNumber() {
        return workIdentificationNumber;
    }

    public void setWorkIdentificationNumber(final String workIdentificationNumber) {
        this.workIdentificationNumber = workIdentificationNumber;
    }

    public String getContractorName() {
        return contractorName;
    }

    public void setContractorName(final String contractorName) {
        this.contractorName = contractorName;
    }

    public void setDepartment(final Long department) {
        this.department = department;
    }

    public Long getDepartment() {
        return department;
    }

    public Date getBillFromDate() {
        return billFromDate;
    }

    public void setBillFromDate(final Date billFromDate) {
        this.billFromDate = billFromDate;
    }

    public Date getBillToDate() {
        return billToDate;
    }

    public void setBillToDate(final Date billToDate) {
        this.billToDate = billToDate;
    }

    public boolean isSpillOverFlag() {
        return spillOverFlag;
    }

    public void setSpillOverFlag(final boolean spillOverFlag) {
        this.spillOverFlag = spillOverFlag;
    }

}
