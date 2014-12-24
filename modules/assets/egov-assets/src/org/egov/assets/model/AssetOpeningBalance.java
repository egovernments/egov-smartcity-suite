package org.egov.assets.model;

import java.math.BigDecimal;

import org.egov.commons.CFinancialYear;
import org.egov.infstr.models.BaseModel;

// TODO: Auto-generated Javadoc
/**
 * The Class AssetOpeningBalance.
 */
public class AssetOpeningBalance extends BaseModel {
	
	/** The asset. */
	private Asset asset;
	
	/** The financial year. */
	private CFinancialYear financialYear;
	
	/** The gross opening balance. */
	private BigDecimal grossOpeningBalance;
	
	/** The deduction opening balance. */
	private BigDecimal deductionOpeningBalance;
	
	/**
	 * Gets the asset.
	 *
	 * @return the asset
	 */
	public Asset getAsset() {
		return asset;
	}
	
	/**
	 * Sets the asset.
	 *
	 * @param asset the new asset
	 */
	public void setAsset(Asset asset) {
		this.asset = asset;
	}
	
	/**
	 * Gets the financial year.
	 *
	 * @return the financial year
	 */
	public CFinancialYear getFinancialYear() {
		return financialYear;
	}
	
	/**
	 * Sets the financial year.
	 *
	 * @param financialYear the new financial year
	 */
	public void setFinancialYear(CFinancialYear financialYear) {
		this.financialYear = financialYear;
	}
	
	/**
	 * Gets the gross opening balance.
	 *
	 * @return the gross opening balance
	 */
	public BigDecimal getGrossOpeningBalance() {
		return grossOpeningBalance;
	}
	
	/**
	 * Sets the gross opening balance.
	 *
	 * @param grossOpeningBalance the new gross opening balance
	 */
	public void setGrossOpeningBalance(BigDecimal grossOpeningBalance) {
		this.grossOpeningBalance = grossOpeningBalance;
	}
	
	/**
	 * Gets the deduction opening balance.
	 *
	 * @return the deduction opening balance
	 */
	public BigDecimal getDeductionOpeningBalance() {
		return deductionOpeningBalance;
	}
	
	/**
	 * Sets the deduction opening balance.
	 *
	 * @param deductionOpeningBalance the new deduction opening balance
	 */
	public void setDeductionOpeningBalance(BigDecimal deductionOpeningBalance) {
		this.deductionOpeningBalance = deductionOpeningBalance;
	}
}
