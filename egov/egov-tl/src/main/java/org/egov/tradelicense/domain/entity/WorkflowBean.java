package org.egov.tradelicense.domain.entity;

import java.util.List;

import org.egov.lib.rjbac.dept.Department;
import org.egov.lib.rjbac.user.UserImpl;
import org.egov.pims.commons.DesignationMaster;

public class WorkflowBean {
	private String actionName;
	private String actionState;
	private List<UserImpl> appoverUserList;
	private Integer approverUserId;
	private String comments;
	private Integer departmentId;
	private List<Department> departmentList;
	private Integer designationId;
	private List<DesignationMaster> designationList;

	public String getActionName() {
		return actionName;
	}
	public String getActionState() {
		return actionState;
	}
	
	public Integer getApproverUserId() {
		return approverUserId;
	}
	public String getComments() {
		return comments;
	}
	
	public List<Department> getDepartmentList() {
		return departmentList;
	}

	public List<DesignationMaster> getDesignationList() {
		return designationList;
	}
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	public void setActionState(String actionState) {
		this.actionState = actionState;
	}
	
	public List<UserImpl> getAppoverUserList() {
		return appoverUserList;
	}
	public void setAppoverUserList(List<UserImpl> appoverUserList) {
		this.appoverUserList = appoverUserList;
	}
	public void setApproverUserId(Integer approverUserId) {
		this.approverUserId = approverUserId;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	
	public void setDepartmentList(List<Department> departmentList) {
		this.departmentList = departmentList;
	}
	
	public Integer getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}
	public Integer getDesignationId() {
		return designationId;
	}
	public void setDesignationId(Integer designationId) {
		this.designationId = designationId;
	}
	public void setDesignationList(List<DesignationMaster> designationList) {
		this.designationList = designationList;
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("WorkflowBean={ ");
		str.append("actionName=").append(actionName == null ? "null" : actionName.toString());
		str.append("actionState=").append(actionState == null ? "null" : actionState.toString());
		str.append("appoverUserList=").append(appoverUserList == null ? "null" : appoverUserList.toString());
		str.append("approverUserId=").append(approverUserId == null ? "null" : approverUserId.toString());
		str.append("comments=").append(comments == null ? "null" : comments.toString());
		str.append("departmentId=").append(departmentId == null ? "null" : departmentId.toString());
		str.append("departmentList=").append(departmentList == null ? "null" : departmentList.toString());
		str.append("designationId=").append(designationId == null ? "null" : designationId.toString());
		str.append("designationList=").append(designationList == null ? "null" : designationList.toString());
		str.append("}");
		return str.toString();
	}
}
