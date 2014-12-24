package org.egov.erpcollection.integration.models;

import java.math.BigDecimal;

public interface ReceiptAccountInfo {

	/**
	 * @return the GL code
	 */
	public abstract String getGlCode();

	/**
	 * @return the Account name
	 */
	public abstract String getAccountName();

	/**
	 * @return the function
	 */
	public abstract String getFunction();

	/**
	 * @return the function name
	 */
	public abstract String getFunctionName();

	/**
	 * @return the debit amount
	 */
	public abstract BigDecimal getDrAmount();

	/**
	 * @return the credit amount
	 */
	public abstract BigDecimal getCrAmount();

	/**
	 * @return true if the account is a revenue account, else false
	 */
	public abstract boolean getIsRevenueAccount();

	/**
	 * @return the order number
	 */
	public abstract Long getOrderNumber();

	/**
	 * @return the account description
	 */
	public abstract String getDescription();

	/**
	 * @return the financial Year
	 */
	public abstract String getFinancialYear();

}