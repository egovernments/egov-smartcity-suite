package org.egov.asset.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.egov.asset.util.AssetIdentifier;

/**
 * The Class AssetActivities.
 */
public class AssetActivities {
	
	/** The asset. */
	private Asset asset;
	
	/** The addition amount. */
	private BigDecimal additionAmount;
	
	/** The deduction amount. */
	private BigDecimal deductionAmount;
	
	/** The activity date. */
	private Date activityDate;
		
	@Enumerated(value = EnumType.STRING)
	private AssetIdentifier identifier;
	
	private String description;
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
	
	
}
