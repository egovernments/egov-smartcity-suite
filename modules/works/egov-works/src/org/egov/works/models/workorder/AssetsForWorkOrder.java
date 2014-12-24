package org.egov.works.models.workorder;

import org.egov.assets.model.Asset;
import org.egov.infstr.models.BaseModel;

public class AssetsForWorkOrder extends BaseModel {
	private Asset asset;
	private WorkOrderEstimate workOrderEstimate;

	public AssetsForWorkOrder() {}
	

	public AssetsForWorkOrder(WorkOrderEstimate workOrderEstimate, Asset asset){
		this.workOrderEstimate=workOrderEstimate;
		this.asset=asset;
	}
	
	public Asset getAsset() {
		return asset;
	}

	public void setAsset(Asset asset) {
		this.asset = asset;
	}


	public WorkOrderEstimate getWorkOrderEstimate() {
		return workOrderEstimate;
	}


	public void setWorkOrderEstimate(WorkOrderEstimate workOrderEstimate) {
		this.workOrderEstimate = workOrderEstimate;
	}

}
