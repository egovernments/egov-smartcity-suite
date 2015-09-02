package org.egov.tradelicense.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ViolationReceiptDetails {

	private String receiptNumber;
	private String receiptdate;
	private String violationFee;
	private Date violationDate;

	public String getReceiptNumber() {
		return receiptNumber;
	}

	public void setReceiptNumber(String receiptNumber) {
		this.receiptNumber = receiptNumber;
	}

	public String getViolationDate() {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		return format.format(violationDate);
	}

	public void setViolationDate(Date violationDate) {
		this.violationDate = violationDate;
	}

	public String getReceiptdate() {
		return receiptdate;
	}

	public void setReceiptdate(String receiptdate) {
		this.receiptdate = receiptdate;
	}

	public String getViolationFee() {
		return violationFee;
	}

	public void setViolationFee(String violationFee) {
		this.violationFee = violationFee;
	}

}
