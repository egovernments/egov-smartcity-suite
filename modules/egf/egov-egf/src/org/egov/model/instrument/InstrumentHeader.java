package org.egov.model.instrument;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.egov.commons.Accountdetailtype;
import org.egov.commons.Bank;
import org.egov.commons.Bankaccount;
import org.egov.commons.EgwStatus;
import org.egov.infstr.models.BaseModel;

/**
 * EgfInstrumenHeader entity.
 * 
 * @author Mani
 */

public class InstrumentHeader extends BaseModel {

	// Fields
	private Accountdetailtype detailTypeId;
	private Bankaccount bankAccountId;
	private EgwStatus statusId;
	private Bank bankId;
	private String instrumentNumber;
	private Date instrumentDate;
	private BigDecimal instrumentAmount;
	private String payTo;
	private String isPayCheque;
	private InstrumentType instrumentType;
	private Long detailKeyId;
	private String transactionNumber;
	private Date transactionDate;
	private String payee;
	private String bankBranchName;
	private 	String surrendarReason;
		private Set<InstrumentVoucher> instrumentVouchers=new HashSet<InstrumentVoucher>(0);
	
	// Property accessors

	

	
	/**
	 * @return the instrumentVouchers
	 */
	public Set<InstrumentVoucher> getInstrumentVouchers() {
		return instrumentVouchers;
	}

	/**
	 * @param instrumentVouchers the instrumentVouchers to set
	 */
	public void setInstrumentVouchers(Set<InstrumentVoucher> instrumentVouchers) {
		this.instrumentVouchers = instrumentVouchers;
	}

	/**
	 * @return the statusId
	 */
	public EgwStatus getStatusId() {
		return statusId;
	}

	public String getBankBranchName() {
		return bankBranchName;
	}

	public void setBankBranchName(String bankBranchName) {
		this.bankBranchName = bankBranchName;
	}

	/**
	 * @param statusId the statusId to set
	 */
	public void setStatusId(EgwStatus statusId) {
		this.statusId = statusId;
	}

	/**
	 * @return the bankId
	 */
	public Bank getBankId() {
		return bankId;
	}

	/**
	 * @param bankId the bankId to set
	 */
	public void setBankId(Bank bankId) {
		this.bankId = bankId;
	}

	/**
	 * @return the instrumentNumber
	 */
	public String getInstrumentNumber() {
		return instrumentNumber;
	}

	/**
	 * @param instrumentNumber the instrumentNumber to set
	 */
	public void setInstrumentNumber(String instrumentNumber) {
		this.instrumentNumber = instrumentNumber;
	}

	/**
	 * @return the instrumentDate
	 */
	public Date getInstrumentDate() {
		return instrumentDate;
	}

	/**
	 * @param instrumentDate the instrumentDate to set
	 */
	public void setInstrumentDate(Date instrumentDate) {
		this.instrumentDate = instrumentDate;
	}

	

	
	/**
	 * @return the payTo
	 */
	public String getPayTo() {
		return payTo;
	}

	/**
	 * @param payTo the payTo to set
	 */
	public void setPayTo(String payTo) {
		this.payTo = payTo;
	}

	

	/**
	 * @return the isPayCheque
	 */
	public String getIsPayCheque() {
		return isPayCheque;
	}

	/**
	 * @param isPayCheque the isPayCheque to set
	 */
	public void setIsPayCheque(String isPayCheque) {
		this.isPayCheque = isPayCheque;
	}

	/**
	 * @return the instrumentType
	 */
	public InstrumentType getInstrumentType() {
		return instrumentType;
	}

	/**
	 * @param instrumentType the instrumentType to set
	 */
	public void setInstrumentType(InstrumentType instrumentType) {
		this.instrumentType = instrumentType;
	}

	/**
	 * @return the detailKeyId
	 */
	public Long getDetailKeyId() {
		return detailKeyId;
	}

	/**
	 * @param detailKeyId the detailKeyId to set
	 */
	public void setDetailKeyId(Long detailKeyId) {
		this.detailKeyId = detailKeyId;
	}

	/**
	 * @return the transactionNumber
	 */
	public String getTransactionNumber() {
		return transactionNumber;
	}

	/**
	 * @param transactionNumber the transactionNumber to set
	 */
	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

	/**
	 * @return the transactionDate
	 */
	public Date getTransactionDate() {
		return transactionDate;
	}

	/**
	 * @param transactionDate the transactionDate to set
	 */
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	/**
	 * @return the bankAccountId
	 */
	public Bankaccount getBankAccountId() {
		return bankAccountId;
	}

	/**
	 * @param bankAccountId the bankAccountId to set
	 */
	public void setBankAccountId(Bankaccount bankAccountId) {
		this.bankAccountId = bankAccountId;
	}

	/**
	 * @return the detailTypeId
	 */
	public Accountdetailtype getDetailTypeId() {
		return detailTypeId;
	}

	/**
	 * @param detailTypeId the detailTypeId to set
	 */
	public void setDetailTypeId(Accountdetailtype detailTypeId) {
		this.detailTypeId = detailTypeId;
	}


	/**
	 * @return the payee
	 */
	public String getPayee()
	{
		return payee;
	}

	/**
	 * @param payee the payee to set
	 */
	public void setPayee(String payee)
	{
		this.payee = payee;
	}
	public BigDecimal getInstrumentAmount() {
		return instrumentAmount;
	}

	public void setInstrumentAmount(BigDecimal instrumentAmount) {
		this.instrumentAmount = instrumentAmount;
	}
	public InstrumentHeader clone()  {
		
		InstrumentHeader newInstrumentHeader=new InstrumentHeader();
		newInstrumentHeader.setBankAccountId(this.bankAccountId);
		newInstrumentHeader.setBankBranchName(this.bankBranchName);
		newInstrumentHeader.setBankId(this.bankId);
		newInstrumentHeader.setDetailKeyId(this.detailKeyId);
		newInstrumentHeader.setDetailTypeId(this.detailTypeId);
		newInstrumentHeader.setInstrumentAmount(this.instrumentAmount);
		newInstrumentHeader.setInstrumentDate(this.instrumentDate);
		newInstrumentHeader.setInstrumentNumber(this.instrumentNumber);
		newInstrumentHeader.setInstrumentType(this.instrumentType);
		newInstrumentHeader.setIsPayCheque(this.isPayCheque);
		newInstrumentHeader.setPayee(this.payee);
		newInstrumentHeader.setPayTo(this.payTo);
		newInstrumentHeader.setTransactionDate(this.transactionDate);
		newInstrumentHeader.setTransactionNumber(this.transactionNumber);
		newInstrumentHeader.setTransactionNumber(this.surrendarReason);
		return newInstrumentHeader;
		
	}
	public String getSurrendarReason() {
		return surrendarReason;
	}

	public void setSurrendarReason(String surrendarReason) {
		this.surrendarReason = surrendarReason;
	}



}