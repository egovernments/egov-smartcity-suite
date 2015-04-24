/**
 * 
 */
package org.egov.collection.integration.models;

import java.math.BigDecimal;

/**
 * @author rishi
 *
 */
public class PaymentInfoCash implements PaymentInfo{
	
	private BigDecimal instrumentAmount;
	
	public PaymentInfoCash(BigDecimal instrumentAmount){
		this.instrumentAmount=instrumentAmount;
	}
		
	
	/**
	 * Default Constructor
	 */
	public PaymentInfoCash() {
	}


	/**
	 * This method returns the Instrument Type Name 
	 * @return Returns Long as Instrument Type Name
	 */
	public TYPE getInstrumentType() {
		return TYPE.cash;
	}
	
	/**
	 * @return the instrumentAmount
	 */
	public BigDecimal getInstrumentAmount() {
		return instrumentAmount;
	}

	/**
	 * @param instrumentAmount the instrumentAmount to set
	 */
	public void setInstrumentAmount(BigDecimal instrumentAmount) {
		this.instrumentAmount = instrumentAmount;
	}

/*	*//**
	 * @param paidBy the paidBy to set
	 *//*
	public void setPaidBy(String paidBy) {
		this.paidBy = paidBy;
	}*/

}
