package org.egov.erpcollection.models;

import org.egov.commons.CVoucherHeader;


/**
 * ReceiptVoucher entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ReceiptVoucher implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private CVoucherHeader voucherheader;
	private ReceiptHeader receiptHeader;
	private String internalrefno;

	/** default constructor */
	public ReceiptVoucher() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CVoucherHeader getVoucherheader() {
		return this.voucherheader;
	}

	public void setVoucherheader(CVoucherHeader voucherheader) {
		this.voucherheader = voucherheader;
	}

	public ReceiptHeader getReceiptHeader() {
		return this.receiptHeader;
	}

	public void setReceiptHeader(
			ReceiptHeader receiptHeader) {
		this.receiptHeader = receiptHeader;
	}

	public String getInternalrefno() {
		return this.internalrefno;
	}

	public void setInternalrefno(String internalrefno) {
		this.internalrefno = internalrefno;
	}
}