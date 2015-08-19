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
    public String getAssessmentNo() {
        return assessmentNo;
    }
    public void setAssessmentNo(String assessmentNo) {
        this.assessmentNo = assessmentNo;
    }
    public String getOwnerName() {
        return ownerName;
    }
    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
    public String getDoorNo() {
        return doorNo;
    }
    public void setDoorNo(String doorNo) {
        this.doorNo = doorNo;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public String getOldTitle() {
        return oldTitle;
    }
    public void setOldTitle(String oldTitle) {
        this.oldTitle = oldTitle;
    }
    public String getChangedTitle() {
        return changedTitle;
    }
    public void setChangedTitle(String changedTitle) {
        this.changedTitle = changedTitle;
    }
    public String getDateOfTransfer() {
        return dateOfTransfer;
    }
    public void setDateOfTransfer(String dateOfTransfer) {
        this.dateOfTransfer = dateOfTransfer;
    }
    public String getCommissionerOrder() {
        return commissionerOrder;
    }
    public void setCommissionerOrder(String commissionerOrder) {
        this.commissionerOrder = commissionerOrder;
    }
    public String getPropertyTax() {
        return propertyTax;
    }
    public void setPropertyTax(String propertyTax) {
        this.propertyTax = propertyTax;
    }
    
}
