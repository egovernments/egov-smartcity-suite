package org.egov.works.models.contractoradvance;

import org.egov.model.advance.EgAdvanceRequisition;
import org.egov.pims.commons.DrawingOfficer;
import org.egov.works.models.workorder.WorkOrderEstimate;

public class ContractorAdvanceRequisition extends EgAdvanceRequisition{
	
	public enum ContractorAdvanceRequisitionStatus {   
		NEW,CREATED,CHECKED,REJECTED,RESUBMITTED,CANCELLED,APPROVED
	}
	
	private WorkOrderEstimate workOrderEstimate;
	private DrawingOfficer drawingOfficer;
	
	private Long workflowDepartmentId;
	private Integer workflowDesignationId;
	private Integer workflowApproverUserId;
	private Integer workflowWardId;
	private String workflowapproverComments;
	private String ownerName;
	
	@Override
	public String getStateDetails() {
		return "Contractor ARF : " + getAdvanceRequisitionNumber();
	}

	public WorkOrderEstimate getWorkOrderEstimate() {
		return workOrderEstimate;
	}

	public void setWorkOrderEstimate(WorkOrderEstimate workOrderEstimate) {
		this.workOrderEstimate = workOrderEstimate;
	}

	public DrawingOfficer getDrawingOfficer() {
		return drawingOfficer;
	}

	public void setDrawingOfficer(DrawingOfficer drawingOfficer) {
		this.drawingOfficer = drawingOfficer;
	}

	public Long getWorkflowDepartmentId() {
		return workflowDepartmentId;
	}

	public void setWorkflowDepartmentId(Long workflowDepartmentId) {
		this.workflowDepartmentId = workflowDepartmentId;
	}

	public Integer getWorkflowDesignationId() {
		return workflowDesignationId;
	}

	public void setWorkflowDesignationId(Integer workflowDesignationId) {
		this.workflowDesignationId = workflowDesignationId;
	}

	public Integer getWorkflowApproverUserId() {
		return workflowApproverUserId;
	}

	public void setWorkflowApproverUserId(Integer workflowApproverUserId) {
		this.workflowApproverUserId = workflowApproverUserId;
	}

	public Integer getWorkflowWardId() {
		return workflowWardId;
	}

	public void setWorkflowWardId(Integer workflowWardId) {
		this.workflowWardId = workflowWardId;
	}

	public String getWorkflowapproverComments() {
		return workflowapproverComments;
	}

	public void setWorkflowapproverComments(String workflowapproverComments) {
		this.workflowapproverComments = workflowapproverComments;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

}