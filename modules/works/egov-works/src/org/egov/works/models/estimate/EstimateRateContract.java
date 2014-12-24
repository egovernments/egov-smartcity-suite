package org.egov.works.models.estimate;

import java.util.LinkedList;
import java.util.List;

import org.egov.infstr.models.BaseModel;
import org.egov.infstr.models.Money;
import org.egov.works.models.rateContract.RateContract;


public class EstimateRateContract extends BaseModel{

	private AbstractEstimate estimate;
	private RateContract rateContract;
	private List<EstimateRateContractDetail> estimateRCDetailList = new LinkedList<EstimateRateContractDetail>();
	
	public AbstractEstimate getEstimate() {
		return estimate;
	}
	public void setEstimate(AbstractEstimate estimate) {
		this.estimate = estimate;
	}
	public RateContract getRateContract() {
		return rateContract;
	}
	public void setRateContract(RateContract rateContract) {
		this.rateContract = rateContract;
	}
	public List<EstimateRateContractDetail> getEstimateRCDetailList() {
		return estimateRCDetailList;
	}
	public void setEstimateRCDetailList(
			List<EstimateRateContractDetail> estimateRCDetailList) {
		this.estimateRCDetailList = estimateRCDetailList;
	}

	public void addEstimateRateContractDetail(EstimateRateContractDetail estimateRateContractDetail) {
		this.estimateRCDetailList.add(estimateRateContractDetail);
	}

	public Money getWorkValueIncludingTaxes() {
		double amt=0;
		for (EstimateRateContractDetail estimateRCDetail : estimateRCDetailList) {
			amt+=estimateRCDetail.getActivity().getAmount().getValue()+estimateRCDetail.getActivity().getTaxAmount().getValue();
		}
		return new Money(amt);
	}
}
