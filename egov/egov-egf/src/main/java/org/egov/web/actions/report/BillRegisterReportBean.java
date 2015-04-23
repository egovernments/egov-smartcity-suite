/**
 * 
 */
package org.egov.web.actions.report;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author manoranjan
 *
 */
public class BillRegisterReportBean {
	
	private String billNumber;
	private String voucherNumber;
	private String paymentVoucherNumber;
	private String partyName;
	private BigDecimal grossAmount;
	private BigDecimal netAmount;
	private BigDecimal deductionAmount;
	private BigDecimal paidAmount; 
	private String status;
	private String billDate;
	private String chequeNumAndDate;
	private String remittanceVoucherNumber;
	private String remittanceChequeNumberAndDate;
	private Date ChequeDate;
	
	
	public String getBillNumber() {
		return billNumber;
	}
	public String getVoucherNumber() {
		return voucherNumber;
	}
	public String getPaymentVoucherNumber() {
		return paymentVoucherNumber;
	}
	public String getPartyName() {
		return partyName;
	}
	public BigDecimal getGrossAmount() {
		return grossAmount;
	}
	public BigDecimal getNetAmount() {
		return netAmount;
	}
	public BigDecimal getDeductionAmount() {
		return deductionAmount;
	}
	public BigDecimal getPaidAmount() {
		return paidAmount;
	}
	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}
	public void setVoucherNumber(String voucherNumber) {
		this.voucherNumber = voucherNumber;
	}
	public void setPaymentVoucherNumber(String paymentVoucherNumber) {
		this.paymentVoucherNumber = paymentVoucherNumber;
	}
	public String getChequeNumAndDate() {
		return chequeNumAndDate;
	}
	public void setChequeNumAndDate(String chequeNumAndDate) {
		this.chequeNumAndDate = chequeNumAndDate;
	}
	public void setPartyName(String partyName) {
		this.partyName = partyName;
	}
	public void setGrossAmount(BigDecimal grossAmount) {
		this.grossAmount = grossAmount;
	}
	public void setNetAmount(BigDecimal netAmount) {
		this.netAmount = netAmount;
	}
	public void setDeductionAmount(BigDecimal deductionAmount) {
		this.deductionAmount = deductionAmount;
	}
	
	
	public void setPaidAmount(BigDecimal paidAmount) {
		this.paidAmount = paidAmount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getBillDate() {
		return billDate;
	}
	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}
	public String getRemittanceVoucherNumber() {
		return remittanceVoucherNumber;
	}
	public void setRemittanceVoucherNumber(String remittanceVoucherNumber) {
		this.remittanceVoucherNumber = remittanceVoucherNumber;
	}
	public Date getChequeDate() {
		return ChequeDate;
	}
	public void setChequeDate(Date chequeDate) {
		ChequeDate = chequeDate;
	}
	public String getRemittanceChequeNumberAndDate() {
		return remittanceChequeNumberAndDate;
	}
	public void setRemittanceChequeNumberAndDate(
			String remittanceChequeNumberAndDate) {
		this.remittanceChequeNumberAndDate = remittanceChequeNumberAndDate;
	}

}
