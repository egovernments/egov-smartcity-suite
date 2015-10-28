package org.egov.ptis.domain.model;

import java.io.Serializable;
import java.math.BigDecimal;

@SuppressWarnings("serial")
public class ReceiptDetails implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -722408244012315773L;
    private String receiptNo;
    private String receiptDate;
    private String payeeName;
    private String payeeAddress;
    private String billReferenceNo;
    private String serviceName;
    private String description;
    private String paidBy;
    private BigDecimal totalAmountPaid;
    private String collectionType;
    private ErrorDetails errorDetails;
    private String transactionId;

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(final String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public String getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(final String receiptDate) {
        this.receiptDate = receiptDate;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(final String payeeName) {
        this.payeeName = payeeName;
    }

    public String getPayeeAddress() {
        return payeeAddress;
    }

    public void setPayeeAddress(final String payeeAddress) {
        this.payeeAddress = payeeAddress;
    }

    public String getBillReferenceNo() {
        return billReferenceNo;
    }

    public void setBillReferenceNo(final String billReferenceNo) {
        this.billReferenceNo = billReferenceNo;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(final String serviceName) {
        this.serviceName = serviceName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getPaidBy() {
        return paidBy;
    }

    public void setPaidBy(final String paidBy) {
        this.paidBy = paidBy;
    }

    public BigDecimal getTotalAmountPaid() {
        return totalAmountPaid;
    }

    public void setTotalAmountPaid(final BigDecimal totalAmountPaid) {
        this.totalAmountPaid = totalAmountPaid;
    }

    public String getCollectionType() {
        return collectionType;
    }

    public void setCollectionType(final String collectionType) {
        this.collectionType = collectionType;
    }

    public ErrorDetails getErrorDetails() {
        return errorDetails;
    }

    public void setErrorDetails(final ErrorDetails errorDetails) {
        this.errorDetails = errorDetails;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(final String transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public String toString() {
        return "ReceiptDetails [receiptNo=" + receiptNo + ", receiptDate=" + receiptDate + ", payeeName=" + payeeName
                + ", payeeAddress=" + payeeAddress + ", billReferenceNo=" + billReferenceNo + ", serviceName="
                + serviceName + ", description=" + description + ", paidBy=" + paidBy + ", totalAmountPaid="
                + totalAmountPaid + ", collectionType=" + collectionType + ", errorDetails=" + errorDetails + "]";
    }

}
