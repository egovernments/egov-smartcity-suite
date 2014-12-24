package org.egov.erpcollection.integration.models;

import java.math.BigDecimal;



/**
 * Represents the payment information  
 *  
 * @author Rishi
 * @version 1.00
 * @see
 * @see
 * @since 1.00
 */


public interface PaymentInfo {
	
	public enum TYPE { cheque, dd, cash, card, bank, atm}; 
	/**
	 * @return Returns the Instrument Amount
	 */
	BigDecimal getInstrumentAmount();
	
	
	/**
	 * @return Returns the Instrument Type Name e.g. cheque/dd/cash/card/bank
	 */
	TYPE getInstrumentType();
	
	void setInstrumentAmount(BigDecimal instrumentAmount);
	
	//void setPaidBy(String paidBy);
	
	
	
	
	
}