package org.egov.assets.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.egov.assets.util.AssetIdentifier;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.EgwStatus;
import org.egov.infstr.models.StateAware;

/**
 * The Class AssetActivities.
 */
public class AssetActivities extends StateAware{
	
	/** The asset. */
	private Asset asset;
	
	/** The addition amount. */
	private BigDecimal additionAmount;
	
	/** The deduction amount. */
	private BigDecimal deductionAmount;
	
	/** The activity date. */
	private Date activityDate;
	
	/** The voucher header. */
	private CVoucherHeader voucherHeader;
	
	@Enumerated(value = EnumType.STRING)
	private AssetIdentifier identifier;
	
	private String description;
	
	private EgwStatus status;
	
	@Override
	public String getStateDetails() {
		return description;
	}
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
	 * Gets the addition amount.
	 *
	 * @return the addition amount
	 */
	public BigDecimal getAdditionAmount() {
		return additionAmount;
	}
	
	/**
	 * Sets the addition amount.
	 *
	 * @param additionAmount the new addition amount
	 */
	public void setAdditionAmount(BigDecimal additionAmount) {
		this.additionAmount = additionAmount;
	}
	
	/**
	 * Gets the deduction amount.
	 *
	 * @return the deduction amount
	 */
	public BigDecimal getDeductionAmount() {
		return deductionAmount;
	}
	
	/**
	 * Sets the deduction amount.
	 *
	 * @param deductionAmount the new deduction amount
	 */
	public void setDeductionAmount(BigDecimal deductionAmount) {
		this.deductionAmount = deductionAmount;
	}
	
	/**
	 * Gets the activity date.
	 *
	 * @return the activity date
	 */
	public Date getActivityDate() {
		return activityDate;
	}
	
	/**
	 * Sets the activity date.
	 *
	 * @param activityDate the new activity date
	 */
	public void setActivityDate(Date activityDate) {
		this.activityDate = activityDate;
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

	public AssetIdentifier getIdentifier() {
		return identifier;
	}

	public void setIdentifier(AssetIdentifier identifier) {
		this.identifier = identifier;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	public EgwStatus getStatus() {
		return status;
	}
	public void setStatus(EgwStatus status) {
		this.status = status;
	}
	
	
}
