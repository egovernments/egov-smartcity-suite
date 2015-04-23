/**
 * 
 */
package org.egov.model.voucher;

/**
 * @author msahoo
 *
 */
public class PayInBean {
	
	private int serialNo;
	private Long instId;
	private String instrumentNumber;
	private String instrumentDate;
	private String instrumentAmount;
	private String voucherNumber;
	private String voucherDate;
	private boolean selectChq;
	
	public boolean isSelectChq() {
		return selectChq;
	}
	public void setSelectChq(boolean selectChq) {
		this.selectChq = selectChq;
	}
	public int getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(int serialNo) {
		this.serialNo = serialNo;
	}
	public String getInstrumentNumber() {
		return instrumentNumber;
	}
	public void setInstrumentNumber(String instrumentNumber) {
		this.instrumentNumber = instrumentNumber;
	}
	public String getInstrumentDate() {
		return instrumentDate;
	}
	public void setInstrumentDate(String instrumentDate) {
		this.instrumentDate = instrumentDate;
	}
	public String getInstrumentAmount() {
		return instrumentAmount;
	}
	public void setInstrumentAmount(String instrumentAmount) {
		this.instrumentAmount = instrumentAmount;
	}
	public String getVoucherNumber() {
		return voucherNumber;
	}
	public void setVoucherNumber(String voucherNumber) {
		this.voucherNumber = voucherNumber;
	}
	public String getVoucherDate() {
		return voucherDate;
	}
	public void setVoucherDate(String voucherDate) {
		this.voucherDate = voucherDate;
	}
	public Long getInstId() {
		return instId;
	}
	public void setInstId(Long instId) {
		this.instId = instId;
	}
	

}
