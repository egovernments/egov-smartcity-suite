package org.egov.assets.model;

import java.util.Date;

import org.egov.infstr.models.BaseModel;

/**
 * AssetValueChange entity.
 * 
 * @author Nilesh
 */

public class AssetValueChange extends BaseModel {

	// Constructors
	
	/** default constructor */
	public AssetValueChange() {
	}

	/** minimal constructor */
	public AssetValueChange(Asset asset, String changeType,
			Date changeDate, Long voucherHeaderId) {
		this.asset = asset;
		this.changeType = changeType;
		this.changeDate = changeDate;
		this.voucherHeaderId = voucherHeaderId;
	}
	
	// Fields

	private Long changeAmount;
	private Long voucherHeaderId;
	private String changeReason;
	private String isValueAdded;
	private String approvedBy;
	private String changeType;
	private Date changeDate;
	private Asset asset;
	
	public Long getChangeAmount() {
		return changeAmount;
	}

	public void setChangeAmount(Long changeAmount) {
		this.changeAmount = changeAmount;
	}

	public Long getVoucherHeaderId() {
		return voucherHeaderId;
	}

	public void setVoucherHeaderId(Long voucherHeaderId) {
		this.voucherHeaderId = voucherHeaderId;
	}

	public String getChangeReason() {
		return changeReason;
	}

	public void setChangeReason(String changeReason) {
		this.changeReason = changeReason;
	}

	public String getIsValueAdded() {
		return isValueAdded;
	}

	public void setIsValueAdded(String isValueAdded) {
		this.isValueAdded = isValueAdded;
	}

	public String getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	public String getChangeType() {
		return changeType;
	}

	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}

	public Date getChangeDate() {
		return changeDate;
	}

	public void setChangeDate(Date changeDate) {
		this.changeDate = changeDate;
	}

	public Asset getAsset() {
		return asset;
	}

	public void setAsset(Asset asset) {
		this.asset = asset;
	}

	
}