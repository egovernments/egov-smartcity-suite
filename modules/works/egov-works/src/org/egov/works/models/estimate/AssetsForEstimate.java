package org.egov.works.models.estimate;

import org.egov.assets.model.Asset;
import org.egov.infstr.models.BaseModel;


public class AssetsForEstimate extends BaseModel {

	private AbstractEstimate abstractEstimate;
	private Asset asset;
	
	public AssetsForEstimate() {}
	
	public AssetsForEstimate(AbstractEstimate abstractEstimate, Asset asset){
		this.abstractEstimate=abstractEstimate;
		this.asset=asset;
	}

	public AbstractEstimate getAbstractEstimate() {
		return abstractEstimate;
	}

	public void setAbstractEstimate(AbstractEstimate abstractEstimate) {
		this.abstractEstimate = abstractEstimate;
	}

	public Asset getAsset() {
		return asset;
	}

	public void setAsset(Asset asset) {
		this.asset = asset;
	}
}
