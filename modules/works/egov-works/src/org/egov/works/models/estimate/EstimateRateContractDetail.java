package org.egov.works.models.estimate;

import org.egov.infstr.models.BaseModel;

public class EstimateRateContractDetail extends BaseModel{

	private EstimateRateContract estimateRC;
	private Activity activity;
	
	public EstimateRateContract getEstimateRC() {
		return estimateRC;
	}
	public void setEstimateRC(EstimateRateContract estimateRC) {
		this.estimateRC = estimateRC;
	}
	public Activity getActivity() {
		return activity;
	}
	public void setActivity(Activity activity) {
		this.activity = activity;
	}
}
