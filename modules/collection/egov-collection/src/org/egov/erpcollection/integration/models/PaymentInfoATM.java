package org.egov.erpcollection.integration.models;

import java.math.BigDecimal;
import java.util.Date;

public class PaymentInfoATM implements PaymentInfo {
	
	private BigDecimal instrumentAmount;
	private Long bankId;
	private Integer transactionNumber;
	private Date transactionDate;
	
	public PaymentInfoATM(BigDecimal instrumentAmount,Long bankId,
			Integer transactionNumber,Date transactionDate){
		this.instrumentAmount=instrumentAmount;
		this.bankId=bankId;
		this.transactionNumber=transactionNumber;
		this.transactionDate=transactionDate;
	}
	
	/**
	 * Default Constructor
	 */
	public PaymentInfoATM() {
	}


	/* (non-Javadoc)
	 * @see org.egov.erpcollection.models.PaymentInfo#getInstrumentAmount()
	 */
	@Override
	public BigDecimal getInstrumentAmount() {
		return instrumentAmount;
	}

	/* (non-Javadoc)
	 * @see org.egov.erpcollection.models.PaymentInfo#getInstrumentType()
	 */
	@Override
	public TYPE getInstrumentType() {
		return TYPE.atm;
	}

	/**
	 * Gets the bank id.
	 *
	 * @return the bankId
	 */
	public Long getBankId() {
		return bankId;
	}

	/**
	 * Gets the transaction number.
	 *
	 * @return the transactionNumber
	 */
	public Integer getTransactionNumber() {
		return transactionNumber;
	}

	/**
	 * Gets the transaction date.
	 *
	 * @return the transactionDate
	 */
	public Date getTransactionDate() {
		return transactionDate;
	}

	/**
	 * @param instrumentAmount the instrumentAmount to set
	 */
	public void setInstrumentAmount(BigDecimal instrumentAmount) {
		this.instrumentAmount = instrumentAmount;
	}

	/**
	 * @param bankId the bankId to set
	 */
	public void setBankId(Long bankId) {
		this.bankId = bankId;
	}

	/**
	 * @param transactionNumber the transactionNumber to set
	 */
	public void setTransactionNumber(Integer transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

	/**
	 * @param transactionDate the transactionDate to set
	 */
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

}
