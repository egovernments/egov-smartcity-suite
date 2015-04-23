package org.egov.model.deduction;

import org.egov.infra.admin.master.entity.Department;
import org.egov.pims.commons.DrawingOfficer;

public class DepartmentDOMapping {
   
	private Long id;
	private Department department;
	private DrawingOfficer drawingOfficer;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Department getDepartment() {
		return department;
	}
	public void setDepartment(Department department) {
		this.department = department;
	}
	public DrawingOfficer getDrawingOfficer() {
		return drawingOfficer;
	}
	public void setDrawingOfficer(DrawingOfficer drawingOfficer) {
		this.drawingOfficer = drawingOfficer;
	}
}
