package org.egov.erpcollection.integration.models;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import org.egov.commons.EgwStatus;
import org.egov.lib.rjbac.user.User;
import org.egov.lib.security.terminal.model.Location;

public interface BillReceiptInfo {

	/**
	 * @return the reference number
	 */
	public abstract String getBillReferenceNum();

	/**
	 * 
	 * @return the event for this bill receipt info
	 */
	public abstract String getEvent();

	/**
	 * @return the receipt number
	 */
	public abstract String getReceiptNum();

	/**
	 * @return the receipt date
	 */
	public abstract Date getReceiptDate();

	/**
	 * @return the receipt location
	 */
	public abstract Location getReceiptLocation();

	/**
	 * @return the receipt status
	 */
	public abstract EgwStatus getReceiptStatus();

	/**
	 * @return the payee name
	 */
	public abstract String getPayeeName();

	/**
	 * @return the payee address
	 */
	public abstract String getPayeeAddress();

	/**
	 * @return the account details for the bill receipt
	 */
	public abstract Set<ReceiptAccountInfo> getAccountDetails();

	/**
	 * @return the instrument details for the receipt
	 */
	public abstract Set<ReceiptInstrumentInfo> getInstrumentDetails();

	/**
	 * @return set of all Bounced Instruments belonging to this receipt
	 */
	public abstract Set<ReceiptInstrumentInfo> getBouncedInstruments();

	/**
	 * @return The service name for this receipt
	 */
	public abstract String getServiceName();

	/**
	 * @return Name of person who has paid for this receipt
	 */
	public abstract String getPaidBy();

	/**
	 * @return The receipt description
	 */
	public abstract String getDescription();

	/**
	 * @return Total amount of the receipt
	 */
	public abstract BigDecimal getTotalAmount();

	/**
	 * @return User who has created the receipt
	 */
	public abstract User getCreatedBy();

	/**
	 * @return User who has last modified the receipt
	 */
	public abstract User getModifiedBy();

	/**
	 * @return URL to view the receipt
	 */
	public abstract String getReceiptURL();

	/**
	 * @return the collectiontype 
	 */
	public abstract String getCollectionType();

}