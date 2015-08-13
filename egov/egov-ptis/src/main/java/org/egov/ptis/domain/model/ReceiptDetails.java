package org.egov.ptis.domain.model;

import java.io.Serializable;
import java.math.BigDecimal;

@SuppressWarnings("serial")
public class ReceiptDetails implements Serializable {
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
	public String getReceiptNo() {
		return receiptNo;
	}
	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}
	public String getReceiptDate() {
		return receiptDate;
	}
	public void setReceiptDate(String receiptDate) {
		this.receiptDate = receiptDate;
	}
	public String getPayeeName() {
		return payeeName;
	}
	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}
	public String getPayeeAddress() {
		return payeeAddress;
	}
	public void setPayeeAddress(String payeeAddress) {
		this.payeeAddress = payeeAddress;
	}
	public String getBillReferenceNo() {
		return billReferenceNo;
	}
	public void setBillReferenceNo(String billReferenceNo) {
		this.billReferenceNo = billReferenceNo;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPaidBy() {
		return paidBy;
	}
	public void setPaidBy(String paidBy) {
		this.paidBy = paidBy;
	}
	public BigDecimal getTotalAmountPaid() {
		return totalAmountPaid;
	}
	public void setTotalAmountPaid(BigDecimal totalAmountPaid) {
		this.totalAmountPaid = totalAmountPaid;
	}
	public String getCollectionType() {
		return collectionType;
	}
	public void setCollectionType(String collectionType) {
		this.collectionType = collectionType;
	}
	public ErrorDetails getErrorDetails() {
		return errorDetails;
	}
	public void setErrorDetails(ErrorDetails errorDetails) {
		this.errorDetails = errorDetails;
	}
	@Override
	public String toString() {
		return "ReceiptDetails [receiptNo=" + receiptNo + ", receiptDate=" + receiptDate + ", payeeName=" + payeeName
				+ ", payeeAddress=" + payeeAddress + ", billReferenceNo=" + billReferenceNo + ", serviceName="
				+ serviceName + ", description=" + description + ", paidBy=" + paidBy + ", totalAmountPaid="
				+ totalAmountPaid + ", collectionType=" + collectionType + ", errorDetails=" + errorDetails + "]";
	}
}
