/**
 * 
 */
package org.egov.assets.util;

import java.math.BigDecimal;

/**
 * @author manoranjan
 *
 */
public class AssetCommonBean {

	private String assetCode;
	
	private String assetName;
	
	private String capDate;
	
	private BigDecimal capAmount;

	private String createDate;
	
	private Long assetId;
	
	private BigDecimal assetValueAsOnDate;
	
	public String getAssetCode() {
		return assetCode;
	}

	public String getAssetName() {
		return assetName;
	}

	public String getCapDate() {
		return capDate;
	}

	public BigDecimal getCapAmount() {
		return capAmount;
	}

	public void setAssetCode(String assetCode) {
		this.assetCode = assetCode;
	}

	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}

	public void setCapDate(String capDate) {
		this.capDate = capDate;
	}

	public void setCapAmount(BigDecimal capAmount) {
		this.capAmount = capAmount;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public Long getAssetId() {
		return assetId;
	}

	public void setAssetId(Long assetId) {
		this.assetId = assetId;
	}

	public BigDecimal getAssetValueAsOnDate() {
		return assetValueAsOnDate;
	}

	public void setAssetValueAsOnDate(BigDecimal assetValueAsOnDate) {
		this.assetValueAsOnDate = assetValueAsOnDate;
	}
}
