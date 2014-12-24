/*
 * @(#)WorkflowBean.java 3.0, 29 Jul, 2013 1:24:28 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.license.domain.entity;

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
		return this.actionName;
	}

	public String getActionState() {
		return this.actionState;
	}

	public Integer getApproverUserId() {
		return this.approverUserId;
	}

	public String getComments() {
		return this.comments;
	}

	public List<Department> getDepartmentList() {
		return this.departmentList;
	}

	public List<DesignationMaster> getDesignationList() {
		return this.designationList;
	}

	public void setActionName(final String actionName) {
		this.actionName = actionName;
	}

	public void setActionState(final String actionState) {
		this.actionState = actionState;
	}

	public List<UserImpl> getAppoverUserList() {
		return this.appoverUserList;
	}

	public void setAppoverUserList(final List<UserImpl> appoverUserList) {
		this.appoverUserList = appoverUserList;
	}

	public void setApproverUserId(final Integer approverUserId) {
		this.approverUserId = approverUserId;
	}

	public void setComments(final String comments) {
		this.comments = comments;
	}

	public void setDepartmentList(final List<Department> departmentList) {
		this.departmentList = departmentList;
	}

	public Integer getDepartmentId() {
		return this.departmentId;
	}

	public void setDepartmentId(final Integer departmentId) {
		this.departmentId = departmentId;
	}

	public Integer getDesignationId() {
		return this.designationId;
	}

	public void setDesignationId(final Integer designationId) {
		this.designationId = designationId;
	}

	public void setDesignationList(final List<DesignationMaster> designationList) {
		this.designationList = designationList;
	}

	@Override
	public String toString() {
		final StringBuilder str = new StringBuilder();
		str.append("WorkflowBean={ ");
		str.append("actionName=").append(this.actionName == null ? "null" : this.actionName.toString());
		str.append("actionState=").append(this.actionState == null ? "null" : this.actionState.toString());
		str.append("appoverUserList=").append(this.appoverUserList == null ? "null" : this.appoverUserList.toString());
		str.append("approverUserId=").append(this.approverUserId == null ? "null" : this.approverUserId.toString());
		str.append("comments=").append(this.comments == null ? "null" : this.comments.toString());
		str.append("departmentId=").append(this.departmentId == null ? "null" : this.departmentId.toString());
		str.append("departmentList=").append(this.departmentList == null ? "null" : this.departmentList.toString());
		str.append("designationId=").append(this.designationId == null ? "null" : this.designationId.toString());
		str.append("designationList=").append(this.designationList == null ? "null" : this.designationList.toString());
		str.append("}");
		return str.toString();
	}
}
