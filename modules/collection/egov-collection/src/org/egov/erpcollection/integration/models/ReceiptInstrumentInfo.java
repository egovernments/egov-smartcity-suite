package org.egov.erpcollection.integration.models;

import java.math.BigDecimal;
import java.util.Date;

import org.egov.commons.EgwStatus;

public interface ReceiptInstrumentInfo {

	/**
	 * @return the instrumentNumber
	 */
	public abstract String getInstrumentNumber();

	/**
	 * @return the instrumentDate
	 */
	public abstract Date getInstrumentDate();

	/**
	 * @return the instrumentType
	 */
	public abstract String getInstrumentType();

	/**
	 * @return the instrumentAmount
	 */
	public abstract BigDecimal getInstrumentAmount();

	/**
	 * @return the instrument status
	 */
	public abstract EgwStatus getInstrumentStatus();

	/**
	 * @return the transaction Number
	 */
	public abstract String getTransactionNumber();

	/**
	 * @return the transaction date
	 */
	public abstract Date getTransactionDate();

	/**
	 * @return true if the instrument is in bounced (dishonored) status, else
	 *         false
	 */
	public abstract boolean isBounced();

	/**
	 * @return Bank name of the instrument (in case of cheque/dd/bank). 
	 * 
	 * Returns null
	 *         in case of other types of instruments.
	 */
	public abstract String getBankName();

	/**
	 * @return Bank account number of the instrument (in case of bank challan). 
	 * Returns null
	 *         in case of other types of instruments.
	 */
	public abstract String getBankAccountNumber();

	/**
	 * @return Bank branch name of the instrument (in case of cheque/dd)
	 */
	public abstract String getBankBranchName();

}