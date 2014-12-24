package org.egov.assets.model;

import java.util.Date;

import org.egov.infstr.models.BaseModel;

/**
 * AssetSale entity.
 * 
 * @author Nilesh
 */

public class AssetSale extends BaseModel {

	
	// Constructors

	/** default constructor */
	public AssetSale() {
	}

	/** minimal constructor */
	public AssetSale(Asset asset, Long voucherHeaderId) {
		this.asset = asset;
		this.voucherHeaderId = voucherHeaderId;
	}
	
	// Fields

	private Long saleValue;
	private Long wdv;
	private Long voucherHeaderId;
	private String reasonForSale;
	private String authorizedBy;
	private Date dateOfSale;
	private Asset asset;
	
	public Long getSaleValue() {
		return saleValue;
	}

	public void setSaleValue(Long saleValue) {
		this.saleValue = saleValue;
	}

	public Long getWdv() {
		return wdv;
	}

	public void setWdv(Long wdv) {
		this.wdv = wdv;
	}

	public Long getVoucherHeaderId() {
		return voucherHeaderId;
	}

	public void setVoucherHeaderId(Long voucherHeaderId) {
		this.voucherHeaderId = voucherHeaderId;
	}

	public String getReasonForSale() {
		return reasonForSale;
	}

	public void setReasonForSale(String reasonForSale) {
		this.reasonForSale = reasonForSale;
	}

	public String getAuthorizedBy() {
		return authorizedBy;
	}

	public void setAuthorizedBy(String authorizedBy) {
		this.authorizedBy = authorizedBy;
	}

	public Date getDateOfSale() {
		return dateOfSale;
	}

	public void setDateOfSale(Date dateOfSale) {
		this.dateOfSale = dateOfSale;
	}

	public Asset getAsset() {
		return asset;
	}

	public void setAsset(Asset asset) {
		this.asset = asset;
	}
	
		
}