package org.egov.assets.model;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.egov.infstr.models.BaseModel;

/**
 * SecurityDeposit entity.
 * 
 * @author Nilesh
 */

public class SecurityDeposit extends BaseModel {

	// Constructors

	/** default constructor */
	public SecurityDeposit() {
	}
	
	// Fields

	private Long retainedAmount;
	private Date releaseDate;
	private Long releasedAmount;
	private List<Asset> assets = new LinkedList<Asset>();
	
	public Long getRetainedAmount() {
		return retainedAmount;
	}
	public void setRetainedAmount(Long retainedAmount) {
		this.retainedAmount = retainedAmount;
	}
	public Date getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}
	public Long getReleasedAmount() {
		return releasedAmount;
	}
	public void setReleasedAmount(Long releasedAmount) {
		this.releasedAmount = releasedAmount;
	}
	public List<Asset> getAssets() {
		return assets;
	}
	public void setAssets(List<Asset> assets) {
		this.assets = assets;
	}
	
		
}