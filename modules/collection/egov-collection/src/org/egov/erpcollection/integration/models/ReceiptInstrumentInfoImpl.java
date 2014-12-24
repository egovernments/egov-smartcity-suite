package org.egov.erpcollection.integration.models;

import java.math.BigDecimal;
import java.util.Date;

import org.egov.commons.Bank;
import org.egov.commons.Bankaccount;
import org.egov.commons.EgwStatus;
import org.egov.erpcollection.web.constants.CollectionConstants;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.utils.FinancialConstants;

/**
 * Provides instrument information for a receipt
 */
public class ReceiptInstrumentInfoImpl implements ReceiptInstrumentInfo {
	/**
	 * The private instrument header. All the getters use this to provide the
	 * data.
	 */
	private final InstrumentHeader instrumentHeader;

	/**
	 * Creates the instrument information object from given instrument header
	 * 
	 * @param instrumentHeader
	 *            the instrument header object
	 */
	public ReceiptInstrumentInfoImpl(InstrumentHeader instrumentHeader) {
		this.instrumentHeader = instrumentHeader;
	}

	/* (non-Javadoc)
	 * @see org.egov.infstr.collections.integration.models.IReceiptInstrumentInfo#getInstrumentNumber()
	 */
	public String getInstrumentNumber() {
		return instrumentHeader.getInstrumentNumber();
	}

	/* (non-Javadoc)
	 * @see org.egov.infstr.collections.integration.models.IReceiptInstrumentInfo#getInstrumentDate()
	 */
	public Date getInstrumentDate() {
		return instrumentHeader.getInstrumentDate();
	}

	/* (non-Javadoc)
	 * @see org.egov.infstr.collections.integration.models.IReceiptInstrumentInfo#getInstrumentType()
	 */
	public String getInstrumentType() {
		return instrumentHeader.getInstrumentType() == null ? null : instrumentHeader
				.getInstrumentType().getType();
	}

	/* (non-Javadoc)
	 * @see org.egov.infstr.collections.integration.models.IReceiptInstrumentInfo#getInstrumentAmount()
	 */
	public BigDecimal getInstrumentAmount() {
		return instrumentHeader.getInstrumentAmount();
	}

	/* (non-Javadoc)
	 * @see org.egov.infstr.collections.integration.models.IReceiptInstrumentInfo#getInstrumentStatus()
	 */
	public EgwStatus getInstrumentStatus() {
		return instrumentHeader.getStatusId();
	}
	
	/* (non-Javadoc)
	 * @see org.egov.infstr.collections.integration.models.IReceiptInstrumentInfo#getTransactionNumber()
	 */
	public String getTransactionNumber() {
		return instrumentHeader.getTransactionNumber();
	}
	
	/* (non-Javadoc)
	 * @see org.egov.infstr.collections.integration.models.IReceiptInstrumentInfo#getTransactionDate()
	 */
	public Date getTransactionDate() {
		return instrumentHeader.getTransactionDate();
	}

	/* (non-Javadoc)
	 * @see org.egov.infstr.collections.integration.models.IReceiptInstrumentInfo#isBounced()
	 */
	public boolean isBounced() {
		return instrumentHeader.getStatusId().getDescription().equals(
				FinancialConstants.INSTRUMENT_DISHONORED_STATUS);
	}

	/* (non-Javadoc)
	 * @see org.egov.infstr.collections.integration.models.IReceiptInstrumentInfo#getBankName()
	 */
	public String getBankName() {
		if(CollectionConstants.INSTRUMENTTYPE_BANK.equals(instrumentHeader.getInstrumentType().getType())){
			Bankaccount bankAccount = instrumentHeader.getBankAccountId();
			return bankAccount.getBankbranch().getBank().getName();
		}
		Bank bank = instrumentHeader.getBankId();
		if (bank == null) {
			return null;
		} else {
			return bank.getName();
		}
	}
	
	/* (non-Javadoc)
	 * @see org.egov.infstr.collections.integration.models.IReceiptInstrumentInfo#getBankAccountNumber()
	 */
	public String getBankAccountNumber() {
		Bankaccount bankAccount = instrumentHeader.getBankAccountId();
		if (bankAccount == null) {
			return null;
		} else {
			return bankAccount.getAccountnumber();
		}
	}

	/* (non-Javadoc)
	 * @see org.egov.infstr.collections.integration.models.IReceiptInstrumentInfo#getBankBranchName()
	 */
	public String getBankBranchName() {
		return instrumentHeader.getBankBranchName();
	}
}
