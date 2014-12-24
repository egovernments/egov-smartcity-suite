/**
 * 
 */
package org.egov.assets.model;

import java.math.BigDecimal;

import org.egov.commons.CChartOfAccounts;

/**
 * @author manoranjan
 *
 */
public class AssetVoucherDetail {

	private Long id;
	private AssetVoucherHeader assetVhId;
	private CChartOfAccounts glCodeId;
	private BigDecimal debitAmount;
	private BigDecimal creditAmount;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public AssetVoucherHeader getAssetVhId() {
		return assetVhId;
	}
	public BigDecimal getDebitAmount() {
		return debitAmount;
	}
	public BigDecimal getCreditAmount() {
		return creditAmount;
	}
	public void setAssetVhId(AssetVoucherHeader assetVhId) {
		this.assetVhId = assetVhId;
	}
	public void setDebitAmount(BigDecimal debitAmount) {
		this.debitAmount = debitAmount;
	}
	public void setCreditAmount(BigDecimal creditAmount) {
		this.creditAmount = creditAmount;
	}
	public CChartOfAccounts getGlCodeId() {
		return glCodeId;
	}
	public void setGlCodeId(CChartOfAccounts glCodeId) {
		this.glCodeId = glCodeId;
	}
}
