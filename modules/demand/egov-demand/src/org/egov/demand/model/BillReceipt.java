package org.egov.demand.model;

import java.math.BigDecimal;
import java.util.Date;

import org.egov.infstr.models.BaseModel;

public class BillReceipt extends BaseModel {
    private EgBill billId = null;
    private String receiptNumber = null;
    private Date receiptDate = null;
    private BigDecimal receiptAmt = null;
    private Boolean isCancelled = null;
    private String collectionStatus = null;

    public EgBill getBillId() {
        return billId;
    }

    public void setBillId(EgBill billId) {
        this.billId = billId;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public Date getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(Date receiptDate) {
        this.receiptDate = receiptDate;
    }

    public BigDecimal getReceiptAmt() {
        return receiptAmt;
    }

    public void setReceiptAmt(BigDecimal receiptAmt) {
        this.receiptAmt = receiptAmt;
    }

    public String getCollectionStatus() {
        return collectionStatus;
    }

    public void setCollectionStatus(String collectionStatus) {
        this.collectionStatus = collectionStatus;
    }

    public Boolean getIsCancelled() {
        return isCancelled;
    }

    public void setIsCancelled(Boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((billId == null) ? 0 : billId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        BillReceipt other = (BillReceipt) obj;
        if (id != null && other != null && id.equals(other.id)) {
            return true;
        }
        return false;
    }
}
