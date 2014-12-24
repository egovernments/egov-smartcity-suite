package org.egov.works.models.workorder;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.egov.infstr.models.BaseModel;
import org.egov.infstr.models.Money;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.measurementbook.MBHeader;
import org.egov.works.models.milestone.Milestone;
import javax.validation.Valid;

public class WorkOrderEstimate extends BaseModel{
	private WorkOrder workOrder;
	private AbstractEstimate estimate;
	private Date workCompletionDate;
	private List<WorkOrderActivity> workOrderActivities = new LinkedList<WorkOrderActivity>();
	@Valid
	private List<AssetsForWorkOrder> assetValues = new LinkedList<AssetsForWorkOrder>();
	
	private Set<Milestone> milestone = new HashSet<Milestone>();
	
	public WorkOrder getWorkOrder() {
		return workOrder;
	}
	public void setWorkOrder(WorkOrder workOrder) {
		this.workOrder = workOrder;
	}
	public AbstractEstimate getEstimate() {
		return estimate;
	}
	public void setEstimate(AbstractEstimate estimate) {
		this.estimate = estimate;
	}
	public List<WorkOrderActivity> getWorkOrderActivities() {
		return workOrderActivities;
	}

	public void setWorkOrderActivities(List<WorkOrderActivity> workOrderActivities) {
		this.workOrderActivities = workOrderActivities;
	}

	public void addWorkOrderActivity(WorkOrderActivity workOrderActivity) {
		this.workOrderActivities.add(workOrderActivity);
	}
	
	public Money getTotalWorkValue() {
		double amt=0;
		for (WorkOrderActivity workOrderActivity : workOrderActivities) {
			amt+=workOrderActivity.getApprovedAmount();
		}
		return new Money(amt);
	}
	
	private Set<MBHeader> mbHeaders = new HashSet<MBHeader>();

	public Set<MBHeader> getMbHeaders() {
		return mbHeaders;
	}
	public void setMbHeaders(Set<MBHeader> mbHeaders) {
		this.mbHeaders = mbHeaders;
	}
	public Date getWorkCompletionDate() {
		return workCompletionDate;
	}
	public void setWorkCompletionDate(Date workCompletionDate) {
		this.workCompletionDate = workCompletionDate;
	}
	
	public List<AssetsForWorkOrder> getAssetValues() {
		return assetValues;
	}

	public void setAssetValues(List<AssetsForWorkOrder> assetValues) {
		this.assetValues = assetValues;
	}

	public void addAssetValue(AssetsForWorkOrder assetValue) {
		this.assetValues.add(assetValue);
	}
	public Set<Milestone> getMilestone() {
		return milestone;
	}
	public void setMilestone(Set<Milestone> milestone) {
		this.milestone = milestone;
	}
	
}
