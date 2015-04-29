package org.egov.works.models.workorder;

import java.util.Date;

import org.egov.infra.persistence.validator.annotation.GreaterThan;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infstr.models.BaseModel;
import org.egov.works.models.estimate.Activity;
import org.egov.works.models.revisionEstimate.RevisionType;

public class WorkOrderActivity extends BaseModel{
	private WorkOrderEstimate workOrderEstimate;
	private Activity activity;
	
	@Required(message="WorkOrderActivity.approvedRate.not.null")
	@GreaterThan(value=0,message="WorkOrderActivity.approvedRate.non.negative")
	private double approvedRate;
	
	@Required(message="WorkOrderActivity.approvedQuantity.not.null")  
	@GreaterThan(value=0,message="WorkOrderActivity.approvedQuantity.non.negative")
	private double approvedQuantity;
	
	
	private double approvedAmount;
	private String remarks;
	
	//Used in new/cancelled WO (for validating the approvedquantity)
	private double unAssignedQuantity;
	
	//in-memory variable for Change in quantity
	private WorkOrderActivity parent;
	private double totalEstQuantity;
	private double prevCumlvQuantity;
	
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
	
	public double getConversionFactor() {
		if(workOrderEstimate.getWorkOrder().getParent() != null && activity.getRevisionType()!=null &&
				(activity.getRevisionType().toString().equalsIgnoreCase(RevisionType.NON_TENDERED_ITEM.toString()) 
						|| activity.getRevisionType().toString().equalsIgnoreCase(RevisionType.LUMP_SUM_ITEM.toString()))) {
				return activity.getConversionFactorForRE(workOrderEstimate.getWorkOrder().getParent().getWorkOrderDate());
			}
		else if(workOrderEstimate.getWorkOrder().getParent() != null && workOrderEstimate.getEstimate().getParent() !=null
				&& activity.getRevisionType()!=null &&
				(activity.getRevisionType().toString().equalsIgnoreCase(RevisionType.ADDITIONAL_QUANTITY.toString()) 
						|| activity.getRevisionType().toString().equalsIgnoreCase(RevisionType.REDUCED_QUANTITY.toString()))) {
			return activity.getConversionFactorForRE(workOrderEstimate.getEstimate().getParent().getEstimateDate());
		}
		else
			return activity.getConversionFactor();
	}
	
	
	/** This method is used to return the ScheduleOfRate based on if its AbstractEstimate(estimateDate is used) 
	 *  or RevisionEstimate(original parent workorderDate is used)
	 * @return a double value of sorRate
	 */
	public double getScheduleOfRate() {
		double sorRate = 0.0;
		if(getActivity().getAbstractEstimate().getParent() == null) {
  			//Original AbstractEstimate
			sorRate = getActivity().getSORCurrentRate().getValue();
  		} 
  		else {
  			Date workOrderDate = new Date();
  			//RevisionEstimate 
  			//If parent is null or if revision type is ADDITIONAL_QUANTITY or REDUCED_QUANTITY then its original WorkOrder 
  			//else if its Revision WorkOrder and revision type is NON_TENDERED_ITEM or LUMP_SUM_ITEM then, get the WorkOrderDate from parent
  			workOrderDate = getWorkOrderEstimate().getWorkOrder().getParent().getWorkOrderDate();
  			if(activity.getRevisionType()!=null &&
  					(activity.getRevisionType().toString().equalsIgnoreCase(RevisionType.NON_TENDERED_ITEM.toString()) 
  							|| activity.getRevisionType().toString().equalsIgnoreCase(RevisionType.LUMP_SUM_ITEM.toString()))) {
  				sorRate = getActivity().getSORRateForDate(workOrderDate).getValue();
  			}
  			else if(getActivity().getAbstractEstimate().getParent() !=null && activity.getRevisionType()!=null &&
  					(activity.getRevisionType().toString().equalsIgnoreCase(RevisionType.ADDITIONAL_QUANTITY.toString()) 
  							|| activity.getRevisionType().toString().equalsIgnoreCase(RevisionType.REDUCED_QUANTITY.toString()))) {
  				sorRate = getActivity().getSORRateForDate(workOrderEstimate.getEstimate().getParent().getEstimateDate()).getValue();
  			}
  			else
  				sorRate = getActivity().getSORCurrentRate().getValue();
  		}
		return sorRate;
	}
	public WorkOrderActivity getParent() {
		return parent;
	}
	public void setParent(WorkOrderActivity parent) {
		this.parent = parent;
	}
	public double getTotalEstQuantity() {
		return totalEstQuantity;
	}
	public double getPrevCumlvQuantity() {
		return prevCumlvQuantity;
	}
	public void setTotalEstQuantity(double totalEstQuantity) {
		this.totalEstQuantity = totalEstQuantity;
	}
	public void setPrevCumlvQuantity(double prevCumlvQuantity) {
		this.prevCumlvQuantity = prevCumlvQuantity;
	}
	
}
