package org.egov.erpcollection.integration.models;

import java.math.BigDecimal;

public class PaymentInfoCard implements PaymentInfo {

	private String instrumentNumber;
	private BigDecimal instrumentAmount;
	private String transactionNumber;
	private String expMonth;
	private String expYear;
	private String cvvNumber;
	//Enum for Card Type M: Master Card, V:Visa
	public enum CARDTYPE {M, V};
	public CARDTYPE cardTypeValue;
	
	/**
	 * Default Constructor
	 */
	public PaymentInfoCard(){
		
	}
	
	public PaymentInfoCard(String instrumentNumber,
			BigDecimal instrumentAmount, String transactionNumber,
			String expMonth, String expYear, String cvvNumber,CARDTYPE cardTypeValue) {
		this.instrumentNumber = instrumentNumber;
		this.instrumentAmount = instrumentAmount;
		this.transactionNumber = transactionNumber;
		this.expMonth = expMonth;
		this.expYear = expYear;
		this.cvvNumber = cvvNumber;
		this.cardTypeValue = cardTypeValue;
	}
	
	@Override
	public BigDecimal getInstrumentAmount() {
		return instrumentAmount;
	}

	@Override
	public TYPE getInstrumentType() {
		return TYPE.card;
	}

	/**
	 * @return the instrumentNumber
	 */
	public String getInstrumentNumber() {
		return instrumentNumber;
	}

	/**String
	 * @return the transactionNumber
	 */
	public String getTransactionNumber() {
		return transactionNumber;
	}

	/**
	 * @param instrumentNumber the instrumentNumber to set
	 */
	public void setInstrumentNumber(String instrumentNumber) {
		this.instrumentNumber = instrumentNumber;
	}

	/**
	 * @param instrumentAmount the instrumentAmount to set
	 */
	public void setInstrumentAmount(BigDecimal instrumentAmount) {
		this.instrumentAmount = instrumentAmount;
	}

	/**
	 * @param transactionNumber the transactionNumber to set
	 */
	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

	public String getExpMonth() {
		return expMonth;
	}

	public void setExpMonth(String expMonth) {
		this.expMonth = expMonth;
	}

	public String getExpYear() {
		return expYear;
	}

	public void setExpYear(String expYear) {
		this.expYear = expYear;
	}

	public String getCvvNumber() {
		return cvvNumber;
	}

	public void setCvvNumber(String cvvNumber) {
		this.cvvNumber = cvvNumber;
	}
	
	public CARDTYPE getCardTypeValue() {
		return cardTypeValue;
	}

	public void setCardTypeValue(CARDTYPE cardType) {
		this.cardTypeValue = cardType;
	}

}
