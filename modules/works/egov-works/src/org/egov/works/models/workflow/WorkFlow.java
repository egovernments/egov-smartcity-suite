package org.egov.works.models.workflow;

import org.egov.infstr.models.StateAware;


public class WorkFlow extends StateAware{
	
	private Integer workflowDepartmentId;
	private Integer workflowDesignationId;
	private Integer workflowApproverUserId;
	private Integer workflowWardId;
	private String workflowapproverComments;
	
	public Integer getWorkflowDepartmentId() {
		return workflowDepartmentId;
	}
	public void setWorkflowDepartmentId(Integer workflowDepartmentId) {
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
	public String getWorkflowapproverComments() {
		return workflowapproverComments;
	}
	public void setWorkflowapproverComments(String workflowapproverComments) {
		this.workflowapproverComments = workflowapproverComments;
	}
	
	public String getStateDetails() {
		return null;
	}
	public Integer getWorkflowWardId() {
		return workflowWardId;
	}
	public void setWorkflowWardId(Integer workflowWardId) {
		this.workflowWardId = workflowWardId;
	}
}
