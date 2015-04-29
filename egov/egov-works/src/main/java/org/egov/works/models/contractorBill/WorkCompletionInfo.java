package org.egov.works.models.contractorBill;

import java.util.Date;
import java.util.List;

import org.egov.infra.workflow.entity.StateHistory;
import org.egov.works.models.workorder.WorkOrderEstimate;

public class WorkCompletionInfo {

	private WorkOrderEstimate workOrderEstimate;
	private String mbNumbers;
	private Date workCommencedOnDate;
	private List<StateHistory> workflowHistory;

	/**
	 * @param workOrderEstimate
	 */
	public WorkCompletionInfo(WorkOrderEstimate workOrderEstimate) {
		this.workOrderEstimate = workOrderEstimate;
	}

	public WorkCompletionInfo() {
		
	}
	/**
	 * @param contractorBillregister
	 * @param workOrderEstimate
	 */
	public WorkCompletionInfo(WorkOrderEstimate workOrderEstimate,String mbNumbers) {
		this.workOrderEstimate = workOrderEstimate;
		this.mbNumbers=mbNumbers;
	}
	
	
	/**
	 * @return name of work
	 */
	public String getWorkName() {
		return workOrderEstimate.getEstimate().getName();
	}

	/**
	 * @return estimate number
	 */
	public String getEstimateNo() {
		return workOrderEstimate.getEstimate().getEstimateNumber();
	}

	/**
	 * @return estimate amount
	 */
	public String getEstimateAmount() {
		return workOrderEstimate.getEstimate().getTotalAmount()
				.getFormattedString();
	}
	
	/**
	 * @return estimate amount value
	 */
	public Double getEstimateAmountValue() {
		return workOrderEstimate.getEstimate().getTotalAmount().getValue();
				
	}

	/**
	 * @return estimate name
	 */
	public String getEstimateName() {
		return workOrderEstimate.getEstimate().getName();
	}
	
	/**
	 * @return budgetheader
	 */
	public String getBudgetHeader() {
		return workOrderEstimate.getEstimate().getFinancialDetails()==null ? null :
				workOrderEstimate.getEstimate().getFinancialDetails().get(0).getBudgetGroup() == null ? null :
				workOrderEstimate.getEstimate().getFinancialDetails().get(0).getBudgetGroup().getName();
	}

	/**
	 * @return appr no
	 */
	public String getApprNo() {
		return workOrderEstimate.getEstimate().getBudgetApprNo();
	}

	/**
	 * @return project code
	 */
	public String getProjectCode() {
		return workOrderEstimate.getEstimate().getProjectCode()==null? null : workOrderEstimate.getEstimate().getProjectCode().getCode();
	}

	/**
	 * @return contractor name
	 */
	public String getContractorName() {
		return workOrderEstimate.getWorkOrder().getContractor().getName();
	}

	/**
	 * @return list of mbno as string
	 */
	public String getAllMBNO() {
		return mbNumbers;
	}

	/**
	 * @return work commenced date
	 */
	public Date getWorkCommencedOn() {
		return workCommencedOnDate;
	}
	
	/**
	 * @set the work commeced date
	 */
	public void setWorkCommencedOn(Date workCommencedOnDate){
		this.workCommencedOnDate=workCommencedOnDate;
	}
	
	/**
	 * @return work completed date
	 */
	public Date getWorkCompletedDate() {
		return workOrderEstimate.getWorkCompletionDate();
	}
	
	/**
	 * @set the history
	 */
	public void setWorkflowHistory(List<StateHistory> workflowHistory){
		this.workflowHistory=workflowHistory;
	}
	
	/**
	 * @return workflow history
	 */
	public List<StateHistory> getWorkflowHistory() {
		return workflowHistory;
	}
}
