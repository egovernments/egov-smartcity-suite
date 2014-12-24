package org.egov.assets.model;

public class SubledgerInfo {
	
	private String glcode;
	private String detailType;
	private String detailKey;
	private String detailAmount="0";
	private String tds;
	
	public String getGlcode() {
		return glcode;
	}
	public void setGlcode(String glcode) {
		this.glcode = glcode;
	}
	public String getDetailType() {
		return detailType;
	}
	public void setDetailType(String detailType) {
		this.detailType = detailType;
	}
	public String getDetailKey() {
		return detailKey;
	}
	public void setDetailKey(String detailKey) {
		this.detailKey = detailKey;
	}
	public String getDetailAmount() {
		return detailAmount;
	}
	public void setDetailAmount(String detailAmount) {
		this.detailAmount = detailAmount;
	}
	public String getTds() {
		return tds;
	}
	public void setTds(String tds) {
		this.tds = tds;
	}

}
