package org.egov.ptis.domain.entity.property;

import java.math.BigDecimal;
import java.util.Date;

import org.egov.infstr.models.BaseModel;

public class PropertyReceipt extends BaseModel{

	private BasicPropertyImpl basicProperty;
	private String bookNumber;
	private String receiptNumber;
	private Date receiptDate;
	private Date fromDate;
	private Date toDate;
	private BigDecimal receiptAmount;
	
	public BasicPropertyImpl getBasicProperty() {
		return basicProperty;
	}
	public void setBasicProperty(BasicPropertyImpl basicProperty) {
		this.basicProperty = basicProperty;
	}
	public String getBookNumber() {
		return bookNumber;
	}
	public void setBookNumber(String bookNumber) {
		this.bookNumber = bookNumber;
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
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	public BigDecimal getReceiptAmount() {
		return receiptAmount;
	}
	public void setReceiptAmount(BigDecimal receiptAmount) {
		this.receiptAmount = receiptAmount;
	}
	
}
