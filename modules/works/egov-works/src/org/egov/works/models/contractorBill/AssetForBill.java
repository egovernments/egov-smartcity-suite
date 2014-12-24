package org.egov.works.models.contractorBill;

import java.math.BigDecimal;

import org.egov.assets.model.Asset;
import org.egov.commons.CChartOfAccounts;
import org.egov.model.bills.EgBillregister;
import org.egov.works.models.workorder.WorkOrderEstimate;

public class AssetForBill {
	
	private Long id;
	private Asset asset;
	private CChartOfAccounts coa;
	private String description;
	private BigDecimal amount;
	private EgBillregister egbill;
	private WorkOrderEstimate workOrderEstimate;
	
	
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	public Asset getAsset() {
		return asset;
	}
	public void setAsset(Asset asset) {
		this.asset = asset;
	}
	public CChartOfAccounts getCoa() {
		return coa;
	}
	public void setCoa(CChartOfAccounts coa) { 
		this.coa = coa;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public EgBillregister getEgbill() {
		return egbill;
	}
	public void setEgbill(EgBillregister egbill) {
		this.egbill = egbill;
	}
	public WorkOrderEstimate getWorkOrderEstimate() {
		return workOrderEstimate;
	}


	public void setWorkOrderEstimate(WorkOrderEstimate workOrderEstimate) {
		this.workOrderEstimate = workOrderEstimate;
	}
	
	
}
