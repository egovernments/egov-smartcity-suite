package org.egov.assets.model;

import java.math.BigDecimal;

import org.egov.commons.Accountdetailkey;
import org.egov.commons.Accountdetailtype;



public class AssetSubledger {
	private Long id;
	private AssetVoucherDetail assetVd;
	private Accountdetailtype detailType;
	private Integer detailKey;
	private BigDecimal amount;
	public Long getId() {
		return id;
	}
	public AssetVoucherDetail getAssetVd() {
		return assetVd;
	}
	public Accountdetailtype getDetailType() {
		return detailType;
	}
	
	public BigDecimal getAmount() {
		return amount;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setAssetVd(AssetVoucherDetail assetVd) {
		this.assetVd = assetVd;
	}
	public void setDetailType(Accountdetailtype detailType) {
		this.detailType = detailType;
	}
	
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public Integer getDetailKey() {
		return detailKey;
	}
	public void setDetailKey(Integer detailKey) {
		this.detailKey = detailKey;
	}
	

}
