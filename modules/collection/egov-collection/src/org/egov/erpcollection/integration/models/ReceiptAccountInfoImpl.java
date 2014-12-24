package org.egov.erpcollection.integration.models;

import java.math.BigDecimal;

import org.egov.erpcollection.models.ReceiptDetail;
import org.egov.erpcollection.util.FinancialsUtil;
/**
 * Provides account information for receipts
 */
public class ReceiptAccountInfoImpl implements ReceiptAccountInfo {
	/**
	 * This is used to check if an account is a revenue account.
	 */
	private final boolean isRevenueAccount;
	/**
	 * The private instance of receipt detail.
	 * This is used by all public getters.
	 */
	private final ReceiptDetail receiptDetail;
	
	/**
	 * Creates the receipt account info for given receipt detail.
	 * @param receiptDetail The receipt detail object
	 */
	public ReceiptAccountInfoImpl(ReceiptDetail receiptDetail) {
		this.receiptDetail = receiptDetail;
		this.isRevenueAccount = FinancialsUtil.isRevenueAccountHead(this.receiptDetail.getAccounthead(),FinancialsUtil.getBankChartofAccountCodeList());
	}
	
	@Override
	public String toString() {
	    return receiptDetail.toString();
	}
	
	/* (non-Javadoc)
	 * @see org.egov.infstr.collections.integration.models.IReceiptAccountInfo#getGlCode()
	 */
	public String getGlCode() {
		return receiptDetail.getAccounthead()==null?
				null:receiptDetail.getAccounthead().getGlcode();
	}
	
	/* (non-Javadoc)
	 * @see org.egov.infstr.collections.integration.models.IReceiptAccountInfo#getAccountName()
	 */
	public String getAccountName() {
		return receiptDetail.getAccounthead()==null?
				null : receiptDetail.getAccounthead().getName();
	}
	
	/* (non-Javadoc)
	 * @see org.egov.infstr.collections.integration.models.IReceiptAccountInfo#getFunction()
	 */
	public String getFunction() {
		return receiptDetail.getFunction()==null?
				null:receiptDetail.getFunction().getCode();
	}
	
	/* (non-Javadoc)
	 * @see org.egov.infstr.collections.integration.models.IReceiptAccountInfo#getFunctionName()
	 */
	public String getFunctionName() {
		return receiptDetail.getFunction()==null?
				null:receiptDetail.getFunction().getName();
	}
	
	/* (non-Javadoc)
	 * @see org.egov.infstr.collections.integration.models.IReceiptAccountInfo#getDrAmount()
	 */
	public BigDecimal getDrAmount() {
		return receiptDetail.getDramount();
	}
	
	/* (non-Javadoc)
	 * @see org.egov.infstr.collections.integration.models.IReceiptAccountInfo#getCrAmount()
	 */
	public BigDecimal getCrAmount() {
		return receiptDetail.getCramount();
	}

	
	/* (non-Javadoc)
	 * @see org.egov.infstr.collections.integration.models.IReceiptAccountInfo#getIsRevenueAccount()
	 */
	public boolean getIsRevenueAccount() {
		return this.isRevenueAccount;
	}

	/* (non-Javadoc)
	 * @see org.egov.infstr.collections.integration.models.IReceiptAccountInfo#getOrderNumber()
	 */
	public Long getOrderNumber() {
		return receiptDetail.getOrdernumber()==null?
				null:receiptDetail.getOrdernumber();
	}
	
	/* (non-Javadoc)
	 * @see org.egov.infstr.collections.integration.models.IReceiptAccountInfo#getDescription()
	 */
	public String getDescription() {
		return receiptDetail.getDescription();
	}
	
	/* (non-Javadoc)
	 * @see org.egov.infstr.collections.integration.models.IReceiptAccountInfo#getFinancialYear()
	 */
	public String getFinancialYear(){
		return receiptDetail.getFinancialYear()==null?
				null:receiptDetail.getFinancialYear().getFinYearRange();
	}
}
