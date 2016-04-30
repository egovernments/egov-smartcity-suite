package org.egov.api.model;

import org.egov.infra.admin.master.entity.Department;
import org.egov.pgr.entity.ComplaintStatus;

import java.util.List;

public class ComplaintAction {

	List<ComplaintStatus> status;
	List<Department> approvalDepartments;
	public List<ComplaintStatus> getStatus() {
		return status;
	}
	public void setStatus(List<ComplaintStatus> status) {
		this.status = status;
	}
	public List<Department> getApprovalDepartments() {
		return approvalDepartments;
	}
	public void setApprovalDepartments(List<Department> approvalDepartments) {
		this.approvalDepartments = approvalDepartments;
	}	

}
