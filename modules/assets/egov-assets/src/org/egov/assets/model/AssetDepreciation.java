package org.egov.assets.model;

import java.math.BigDecimal;
import java.util.Date;

import org.egov.commons.CVoucherHeader;
import org.egov.infstr.models.BaseModel;

// TODO: Auto-generated Javadoc
/**
 * The Class AssetDepreciation.
 */
public class AssetDepreciation extends BaseModel {
	
	/** The asset. */
	private Asset asset;
	
	/** The from date. */
	private Date fromDate;
	
	/** The to date. */
	private Date toDate;
	
	/** The amount. */
	private BigDecimal amount;
	
	/** The voucher header. */
	private CVoucherHeader voucherHeader;
	
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
	 * Gets the from date.
	 *
	 * @return the from date
	 */
	public Date getFromDate() {
		return fromDate;
	}
	
	/**
	 * Sets the from date.
	 *
	 * @param fromDate the new from date
	 */
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	
	/**
	 * Gets the to date.
	 *
	 * @return the to date
	 */
	public Date getToDate() {
		return toDate;
	}
	
	/**
	 * Sets the to date.
	 *
	 * @param toDate the new to date
	 */
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	
	/**
	 * Gets the amount.
	 *
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return amount;
	}
	
	/**
	 * Sets the amount.
	 *
	 * @param amount the new amount
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	/**
	 * Gets the voucher header.
	 *
	 * @return the voucher header
	 */
	public CVoucherHeader getVoucherHeader() {
		return voucherHeader;
	}
	
	/**
	 * Sets the voucher header.
	 *
	 * @param voucherHeader the new voucher header
	 */
	public void setVoucherHeader(CVoucherHeader voucherHeader) {
		this.voucherHeader = voucherHeader;
	}
}
