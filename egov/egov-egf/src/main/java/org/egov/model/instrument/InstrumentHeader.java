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
import org.egov.infstr.models.ECSType;

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
	private String surrendarReason;
	private String serialNo;
	private ECSType ECSType;
	
	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

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
	
	/**
	 * @return the ECSType
	 */
	public ECSType getECSType() {
		return ECSType;
	}

	/**
	 * @param ECSType the ECSType to set
	 */
	public void setECSType(ECSType ECSType) {
		this.ECSType = ECSType;
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
		newInstrumentHeader.setSurrendarReason(this.surrendarReason);
		return newInstrumentHeader;
		
	}
	


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InstrumentHeader other = (InstrumentHeader) obj;
		if (bankAccountId == null) {
			if (other.bankAccountId != null)
				return false;
		} else if (!bankAccountId.equals(other.bankAccountId))
			return false;
		if (bankId == null) {
			if (other.bankId != null)
				return false;
		} else if (!bankId.equals(other.bankId))
			return false;
		if (instrumentAmount == null) {
			if (other.instrumentAmount != null)
				return false;
		} else if (!instrumentAmount.equals(other.instrumentAmount))
			return false;
		if (instrumentDate == null) {
			if (other.instrumentDate != null)
				return false;
		} else if (!instrumentDate.equals(other.instrumentDate))
			return false;
		if (instrumentNumber == null) {
			if (other.instrumentNumber != null)
				return false;
		} else if (!instrumentNumber.equals(other.instrumentNumber))
			return false;
		if (instrumentType == null) {
			if (other.instrumentType != null)
				return false;
		} else if (!instrumentType.equals(other.instrumentType))
			return false;
		if (isPayCheque == null) {
			if (other.isPayCheque != null)
				return false;
		} else if (!isPayCheque.equals(other.isPayCheque))
			return false;
		if (transactionDate == null) {
			if (other.transactionDate != null)
				return false;
		} else if (!transactionDate.equals(other.transactionDate))
			return false;
		if (transactionNumber == null) {
			if (other.transactionNumber != null)
				return false;
		} else if (!transactionNumber.equals(other.transactionNumber))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bankAccountId == null) ? 0 : bankAccountId.hashCode());
		result = prime * result + ((bankId == null) ? 0 : bankId.hashCode());
		result = prime * result + ((instrumentAmount == null) ? 0 : instrumentAmount.hashCode());
		result = prime * result + ((instrumentDate == null) ? 0 : instrumentDate.hashCode());
		result = prime * result + ((instrumentNumber == null) ? 0 : instrumentNumber.hashCode());
		result = prime * result + ((instrumentType == null) ? 0 : instrumentType.hashCode());
		result = prime * result + ((isPayCheque == null) ? 0 : isPayCheque.hashCode());
		result = prime * result + ((transactionDate == null) ? 0 : transactionDate.hashCode());
		result = prime * result + ((transactionNumber == null) ? 0 : transactionNumber.hashCode());
		return result;
	}

	@Override
	public String toString() {
		
		StringBuffer str=new StringBuffer(1024);
		str.append("id:").append(id)
		.append("Number:").append(instrumentNumber)
		.append("Date:").append(instrumentDate)
		.append("Amount:").append(instrumentAmount)
		.append("Type:").append(instrumentType);
		return str.toString();
	}

	public String getSurrendarReason() {
		return surrendarReason;
	}

	public void setSurrendarReason(String surrendarReason) {
		this.surrendarReason = surrendarReason;
	}



}