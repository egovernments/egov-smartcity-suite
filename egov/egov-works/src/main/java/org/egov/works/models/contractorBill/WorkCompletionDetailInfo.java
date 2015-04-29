package org.egov.works.models.contractorBill;



import org.egov.works.models.workorder.WorkOrderActivity;

public class WorkCompletionDetailInfo {

	private WorkOrderActivity workOrderActivity;
	private double executionQuantity; 
	private double tenderAmount;
	private double executionAmount;
	private double executionRate;
	
	
	public WorkCompletionDetailInfo(WorkOrderActivity workOrderActivity,double executionQuantity){
		this.workOrderActivity=workOrderActivity;
		this.executionQuantity=executionQuantity;
	}
	
	public WorkOrderActivity getWorkOrderActivity(){
		return workOrderActivity;
	}
	
	public double getExecutionQuantity(){
		return executionQuantity;
	}

	public double getTenderAmount() {
		return tenderAmount;
	}

	public void setTenderAmount(double tenderAmount) {
		this.tenderAmount = tenderAmount;
	}
	
	public double getExecutionAmount() {
		return executionAmount;
	}

	public void setExecutionAmount(double executionAmount) {
		this.executionAmount = executionAmount;
	}

	public void setWorkOrderActivity(WorkOrderActivity workOrderActivity) {
		this.workOrderActivity = workOrderActivity;
	}

	public void setExecutionQuantity(double executionQuantity) {
		this.executionQuantity = executionQuantity;
	}

	public double getExecutionRate() {
		return executionRate;
	}

	public void setExecutionRate(double executionRate) {
		this.executionRate = executionRate;
	}
	
}
