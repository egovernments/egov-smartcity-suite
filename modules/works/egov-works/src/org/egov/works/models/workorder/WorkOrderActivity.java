package org.egov.works.models.workorder;

import java.util.LinkedList;
import java.util.List;

import org.egov.infstr.models.BaseModel;
import org.egov.infstr.models.validator.GreaterThan;
import org.egov.infstr.models.validator.Required;
import org.egov.works.models.estimate.Activity;

public class WorkOrderActivity extends BaseModel{
	private WorkOrderEstimate workOrderEstimate;
	private Activity activity;
	
	@Required(message="WorkOrderActivity.approvedRate.not.null")
	@GreaterThan(value=0,message="WorkOrderActivity.approvedRate.non.negative")
	private double approvedRate;
	
	@Required(message="WorkOrderActivity.approvedQuantity.not.null")  
	@GreaterThan(value=-1,message="WorkOrderActivity.approvedQuantity.non.negative")
	private double approvedQuantity;
	
	
	private double approvedAmount;
	private String remarks;
	
	//Used in new/cancelled WO (for validating the approvedquantity)
	private double unAssignedQuantity;
	
	//in-memory variable for Change in quantity
	private WorkOrderActivity parent;
	
	//variable to keep selected tender response activity id for workorder
	private transient Long tenderResponseActivityId;
	
	private double totalEstQuantity;
	private double prevCumlvQuantity;
	// for RevisionWorkOrder PDF
	private double approvedTotalQuantityForRevWOs;
	private List<WorkOrderMeasurementSheet> woMeasurementSheetList = new LinkedList<WorkOrderMeasurementSheet>();

	public Activity getActivity() {
		return activity;
	}
	public void setActivity(Activity activity) {
		this.activity = activity;
	}
	public double getApprovedRate() {
		return approvedRate;
	}
	public void setApprovedRate(double approvedRate) {
		this.approvedRate = approvedRate;
	}
	public double getApprovedQuantity() {
		return approvedQuantity;
	}
	public void setApprovedQuantity(double approvedQuantity) {
		this.approvedQuantity = approvedQuantity;
	}
	public double getApprovedAmount() {
		return approvedAmount;
	}
	public void setApprovedAmount(double approvedAmount) {
		this.approvedAmount = approvedAmount;
	}
	public WorkOrderEstimate getWorkOrderEstimate() {
		return workOrderEstimate;
	}
	public void setWorkOrderEstimate(WorkOrderEstimate workOrderEstimate) {
		this.workOrderEstimate = workOrderEstimate;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public double getUnAssignedQuantity() {
		return unAssignedQuantity;
	}
	public void setUnAssignedQuantity(double unAssignedQuantity) {
		this.unAssignedQuantity = unAssignedQuantity;
	}
	public List<WorkOrderMeasurementSheet> getWoMeasurementSheetList() {
		return woMeasurementSheetList;
}
	public void setWoMeasurementSheetList(
			List<WorkOrderMeasurementSheet> woMeasurementSheetList) {
		this.woMeasurementSheetList = woMeasurementSheetList;
	}
	public void addWoMeasurementSheet(WorkOrderMeasurementSheet workOrderMeasurementSheet) {
		 this.woMeasurementSheetList.add(workOrderMeasurementSheet);
	}
	public WorkOrderActivity getParent() {
		return parent;
	}
	public void setParent(WorkOrderActivity parent) {
		this.parent = parent;
	}
	public Long getTenderResponseActivityId() {
		return tenderResponseActivityId;
	}
	public void setTenderResponseActivityId(Long tenderResponseActivityId) {
		this.tenderResponseActivityId = tenderResponseActivityId;
	}
	public double getTotalEstQuantity() {
		return totalEstQuantity;
	}
	public void setTotalEstQuantity(double totalEstQuantity) {
		this.totalEstQuantity = totalEstQuantity;
	}
	public double getPrevCumlvQuantity() {
		return prevCumlvQuantity;
	}
	public void setPrevCumlvQuantity(double prevCumlvQuantity) {
		this.prevCumlvQuantity = prevCumlvQuantity;
	}
	public double getApprovedTotalQuantityForRevWOs() {
		return approvedTotalQuantityForRevWOs;
	}
	public void setApprovedTotalQuantityForRevWOs(
			double approvedTotalQuantityForRevWOs) {
		this.approvedTotalQuantityForRevWOs = approvedTotalQuantityForRevWOs;
	}
	
}