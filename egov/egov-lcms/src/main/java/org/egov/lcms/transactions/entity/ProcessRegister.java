package org.egov.lcms.transactions.entity;

import java.util.Date;

import org.egov.infstr.models.BaseModel;

public class ProcessRegister extends BaseModel {

    private static final long serialVersionUID = 1L;

    private Legalcase legalcase;
    private String detailedAddress;
    private Date processDate;
    private Date nextDateOfProcess;
    private Date receivedOn;
    private String processHandedOverTo;
    private Date dateOfHandingOver;
    private String processFilingAttorney;
    private String remarks;
    private boolean isProcessRegReqd;

    public Legalcase getLegalcase() {
        return legalcase;
    }

    public void setLegalcase(Legalcase legalcase) {
        this.legalcase = legalcase;
    }

    public String getDetailedAddress() {
        return detailedAddress;
    }

    public void setDetailedAddress(String detailedAddress) {
        this.detailedAddress = detailedAddress;
    }

    public Date getProcessDate() {
        return processDate;
    }

    public void setProcessDate(Date processDate) {
        this.processDate = processDate;
    }

    public Date getNextDateOfProcess() {
        return nextDateOfProcess;
    }

    public void setNextDateOfProcess(Date nextDateOfProcess) {
        this.nextDateOfProcess = nextDateOfProcess;
    }

    public Date getReceivedOn() {
        return receivedOn;
    }

    public void setReceivedOn(Date receivedOn) {
        this.receivedOn = receivedOn;
    }

    public String getProcessHandedOverTo() {
        return processHandedOverTo;
    }

    public void setProcessHandedOverTo(String processHandedOverTo) {
        this.processHandedOverTo = processHandedOverTo;
    }

    public Date getDateOfHandingOver() {
        return dateOfHandingOver;
    }

    public void setDateOfHandingOver(Date dateOfHandingOver) {
        this.dateOfHandingOver = dateOfHandingOver;
    }

    public String getProcessFilingAttorney() {
        return processFilingAttorney;
    }

    public void setProcessFilingAttorney(String processFilingAttorney) {
        this.processFilingAttorney = processFilingAttorney;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public void setIsProcessRegReqd(boolean isProcessRegReqd) {
        this.isProcessRegReqd = isProcessRegReqd;
    }

    public boolean getIsProcessRegReqd() {
        return isProcessRegReqd;
    }
}
