package org.egov.assets.model;

import java.math.BigDecimal;

public class CapitaliseAsset {
	
	private Asset asset;
	private BigDecimal capitalisationValue = new BigDecimal("0.0");
	
	public Asset getAsset() {
		return asset;
	}
	public void setAsset(Asset asset) {
		this.asset = asset;
	}
	public BigDecimal getCapitalisationValue() {
		return capitalisationValue;
	}
	public void setCapitalisationValue(BigDecimal capitalisationValue) {
		this.capitalisationValue = capitalisationValue;
	}

}
