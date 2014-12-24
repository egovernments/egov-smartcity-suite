/**
 * 
 */
package org.egov.model.deduction;

import java.math.BigDecimal;

import org.apache.commons.collections.Predicate;


/**
 * @author manoranjan
 *
 */
public class RemittanceBean implements Predicate{

	private Long recoveryId;
	private String voucherNumber;
	private String voucherName;
	private String voucherDate;
	private BigDecimal amount;
	private String partyName;
	private String partyCode;
	private String panNo;
	private Integer remittanceId;
	private String selectedrRemit;
	private BigDecimal totalAmount;
	private boolean chkremit;
	private Integer bank;
	private Integer detailTypeId;
	private Integer detailKeyid;
	private BigDecimal partialAmount;
	private Integer remittance_gl_dtlId;
	
	public Integer getRemittance_gl_dtlId()
	{
		return remittance_gl_dtlId;
	}
	public void setRemittance_gl_dtlId(Integer remittance_gl_dtlId) {
		this.remittance_gl_dtlId = remittance_gl_dtlId;
	}
	public BigDecimal getPartialAmount() {
		return partialAmount;
	}
	public void setPartialAmount(BigDecimal partialAmount) {
		this.partialAmount = partialAmount;
	}
	private Integer accountNumber;
		public boolean getChkremit() {
		return chkremit;
	}
	public void setChkremit(boolean chkremit) {
		this.chkremit = chkremit;
	}
	public Long getRecoveryId() {
		return recoveryId;
	}
	public void setRecoveryId(Long recoveryId) {
		this.recoveryId = recoveryId;
	}
	public String getVoucherNumber() {
		return voucherNumber;
	}

	public void setVoucherNumber(String voucherNumber) {
		this.voucherNumber = voucherNumber;
	}

	public String getVoucherName() {
		return voucherName;
	}

	public void setVoucherName(String voucherName) {
		this.voucherName = voucherName;
	}

	public String getVoucherDate() {
		return voucherDate;
	}

	public void setVoucherDate(String voucherDate) {
		this.voucherDate = voucherDate;
	}
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getPartyName() {
		return partyName;
	}

	public void setPartyName(String partyName) {
		this.partyName = partyName;
	}

	public String getPartyCode() {
		return partyCode;
	}

	public void setPartyCode(String partyCode) {
		this.partyCode = partyCode;
	}

	public String getPanNo() {
		return panNo;
	}

	public void setPanNo(String panNo) {
		this.panNo = panNo;
	}

	public Integer getRemittanceId() {
		return remittanceId;
	}

	public void setRemittanceId(Integer remittanceId) {
		this.remittanceId = remittanceId;
	}

	public String getSelectedrRemit() {
		return selectedrRemit;
	}
	public void setSelectedrRemit(String selectedrRemit) {
		this.selectedrRemit = selectedrRemit;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	@Override
	public boolean evaluate(Object arg0) {
		RemittanceBean  remittanceBean = (RemittanceBean)arg0;
		return remittanceBean.getChkremit();
	}
	public Integer getBank() {
		return bank;
	}
	public void setBank(Integer bank) {
		this.bank = bank;
	}
	public Integer getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(Integer accountNumber) {
		this.accountNumber = accountNumber;
	}
	public Integer getDetailTypeId() {
		return detailTypeId;
	}
	public void setDetailTypeId(Integer detailTypeId) {
		this.detailTypeId = detailTypeId;
	}
	public Integer getDetailKeyid() {
		return detailKeyid;
	}
	public void setDetailKeyid(Integer detailKeyid) {
		this.detailKeyid = detailKeyid;
	}
}
