package org.egov.pims.model;

public class EmployeeDepartment implements java.io.Serializable
{
	private Integer id;
	private org.egov.lib.rjbac.dept.Department dept;
	private org.egov.lib.rjbac.dept.Department hodept;
	
	private Assignment assignment;
	public Assignment getAssignment() {
		return assignment;
	}
	public void setAssignment(Assignment assignment) {
		this.assignment = assignment;
	}
	public org.egov.lib.rjbac.dept.Department getDept() {
		return dept;
	}
	public void setDept(org.egov.lib.rjbac.dept.Department dept) {
		this.dept = dept;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public org.egov.lib.rjbac.dept.Department getHodept() {
		return hodept;
	}
	public void setHodept(org.egov.lib.rjbac.dept.Department hodept) {
		this.hodept = hodept;
	}
	
	
}
