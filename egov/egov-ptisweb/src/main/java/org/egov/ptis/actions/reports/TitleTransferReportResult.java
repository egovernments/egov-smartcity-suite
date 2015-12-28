package org.egov.ptis.actions.reports;

import java.math.BigDecimal;


public class TitleTransferReportResult {

    private String assessmentNo;
    private String ownerName;
    private String doorNo;
    private String location;
    private String propertyTax;
    private String oldTitle;
    private String changedTitle;
    private String dateOfTransfer;
    private String commissionerOrder;
    private BigDecimal mutationFee;

    public String getAssessmentNo() {
        return assessmentNo;
    }

    public void setAssessmentNo(final String assessmentNo) {
        this.assessmentNo = assessmentNo;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(final String ownerName) {
        this.ownerName = ownerName;
    }

    public String getDoorNo() {
        return doorNo;
    }

    public void setDoorNo(final String doorNo) {
        this.doorNo = doorNo;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(final String location) {
        this.location = location;
    }

    public String getOldTitle() {
        return oldTitle;
    }

    public void setOldTitle(final String oldTitle) {
        this.oldTitle = oldTitle;
    }

    public String getChangedTitle() {
        return changedTitle;
    }

    public void setChangedTitle(final String changedTitle) {
        this.changedTitle = changedTitle;
    }

    public String getDateOfTransfer() {
        return dateOfTransfer;
    }

    public void setDateOfTransfer(final String dateOfTransfer) {
        this.dateOfTransfer = dateOfTransfer;
    }

    public String getCommissionerOrder() {
        return commissionerOrder;
    }

    public void setCommissionerOrder(final String commissionerOrder) {
        this.commissionerOrder = commissionerOrder;
    }

    public String getPropertyTax() {
        return propertyTax;
    }

    public void setPropertyTax(final String propertyTax) {
        this.propertyTax = propertyTax;
    }

    public BigDecimal getMutationFee() {
        return mutationFee;
    }

    public void setMutationFee(BigDecimal mutationFee) {
        this.mutationFee = mutationFee;
    }

}
