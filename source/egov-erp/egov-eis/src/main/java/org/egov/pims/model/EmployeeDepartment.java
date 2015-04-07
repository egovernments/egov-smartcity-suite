package org.egov.pims.model;

public class EmployeeDepartment implements java.io.Serializable
{
	private Integer id;
	private org.egov.infra.admin.master.entity.Department dept;
	private org.egov.infra.admin.master.entity.Department hodept;
	
	private Assignment assignment;
	public Assignment getAssignment() {
		return assignment;
	}
	public void setAssignment(Assignment assignment) {
		this.assignment = assignment;
	}
	public org.egov.infra.admin.master.entity.Department getDept() {
		return dept;
	}
	public void setDept(org.egov.infra.admin.master.entity.Department dept) {
		this.dept = dept;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public org.egov.infra.admin.master.entity.Department getHodept() {
		return hodept;
	}
	public void setHodept(org.egov.infra.admin.master.entity.Department hodept) {
		this.hodept = hodept;
	}
	
	
}
