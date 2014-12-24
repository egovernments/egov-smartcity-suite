package com.exilant.GLEngine;

public class DayBook
{
//	private DecimalFormat moneyFormat = new DecimalFormat("#,###,###.00");
	private String voucherdate;
	private String voucher;
	private String type;
	private String narration;
	private String status;
	private String glcode;
	private String particulars;
	private String debitamount;
	private String creditamount;
	private String cgn;
	
	/**
	 * 
	 */
	public DayBook() {
		
	}
	
	/**
	 * @return Returns the cgn.
	 */
	public String getCgn() {
		return cgn;
	}
	/**
	 * @param cgn The cgn to set.
	 */
	public void setCgn(String cgn) {
		this.cgn = cgn;
	}
	/**
	 * @return Returns the creditamount.
	 */
	public String getCreditamount() {
		return creditamount;
		}
	/**
	 * @param creditamount The creditamount to set.
	 */
	public void setCreditamount(String creditamount) {
		this.creditamount = creditamount;
		}
	/**
	 * @return Returns the debitamount.
	 */
	public String getDebitamount() {
		return debitamount;
		}
	/**
	 * @param debitamount The debitamount to set.
	 */
	public void setDebitamount(String debitamount) {
		this.debitamount = debitamount;
	}
	/**
	 * @return Returns the glcode.
	 */
	public String getGlcode() {
		return glcode;
	}
	/**
	 * @param glcode The glcode to set.
	 */
	public void setGlcode(String glcode) {
		this.glcode = glcode;
	}
	/**
	 * @return Returns the narration.
	 */
	public String getNarration() {
		return narration;
	}
	/**
	 * @param narration The narration to set.
	 */
	public void setNarration(String narration) {
		this.narration = narration;
	}
	/**
	 * @return Returns the particulars.
	 */
	public String getParticulars() {
		return particulars;
	}
	/**
	 * @param particulars The particulars to set.
	 */
	public void setParticulars(String particulars) {
		this.particulars = particulars;
	}
	/**
	 * @return Returns the status.
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status The status to set.
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return Returns the type.
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type The type to set.
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return Returns the voucherdate.
	 */
	public String getVoucherdate() {
		return voucherdate;
	}
	/**
	 * @param voucherdate The voucherdate to set.
	 */
	public void setVoucherdate(String voucherdate) {
		this.voucherdate = voucherdate;
	}
	/**
	 * @return Returns the vouchernumber.
	 */
	public String getVoucher() {
		return voucher;
	}
	/**
	 * @param vouchernumber The vouchernumber to set.
	 */
	public void setVoucher(String vouchernumber) {
		this.voucher = vouchernumber;
	}
}