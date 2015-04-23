package org.egov.ptis.domain.entity.property;

import java.util.List;

import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.User;
import org.egov.pims.commons.DesignationMaster;

public class WorkflowBean {
	private String actionName;
	private String actionState;
	private List<User> appoverUserList;
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

	public List<User> getAppoverUserList() {
		return appoverUserList;
	}

	public void setAppoverUserList(List<User> appoverUserList) {
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

	public String toString() {
		StringBuilder objStr = new StringBuilder();

		objStr.append("WorkflowBean[DepartmentId: ").append(this.getDepartmentId()).append(" , DesignationId: ")
				.append(this.designationId).append(" , User Id:").append(this.approverUserId).append(" , ActionName:")
				.append(this.actionName).append(" , departmentList(Size:").append(
						(departmentList != null) ? this.departmentList.size() : "").append("):").append(
						this.departmentList).append(" , designationList(Size:").append(
						(designationList != null) ? this.designationList.size() : "").append("):").append(
						this.designationList).append(" , appoverUserList(Size:").append(
						(appoverUserList != null) ? this.appoverUserList.size() : "").append("):").append(
						this.appoverUserList).append("]");

		return objStr.toString();
	}
}
