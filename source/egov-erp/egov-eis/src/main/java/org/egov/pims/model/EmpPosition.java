package org.egov.pims.model;

import org.egov.commons.EgwStatus;
import org.egov.infstr.models.StateAware;
import org.egov.infstr.models.validator.Required;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.commons.Position;

@SuppressWarnings("serial")
public class EmpPosition extends StateAware{

	@Required(message="postname.required")
	private String postName;
	@Required(message="desig.required")
	private DesignationMaster desigId;
	@Required(message="dept.required")
	private DepartmentImpl deptId;
	private EgwStatus status;
	private String qualificationDetails;
	private String remarks;
	private Position position;
	
	public String getPostName() {
		return postName;
	}

	public void setPostName(String postName) {
		this.postName = postName;
	}

	public DesignationMaster getDesigId() {
		return desigId;
	}

	public void setDesigId(DesignationMaster desigId) {
		this.desigId = desigId;
	}

	public DepartmentImpl getDeptId() {
		return deptId;
	}

	public void setDeptId(DepartmentImpl deptId) {
		this.deptId = deptId;
	}

	public EgwStatus getStatus() {
		return status;
	}

	public void setStatus(EgwStatus status) {
		this.status = status;
	}

	public String getQualificationDetails() {
		return qualificationDetails;
	}

	public void setQualificationDetails(String qualificationDetails) {
		this.qualificationDetails = qualificationDetails;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	@Override
	public String getStateDetails() {
		
		return ""+getDeptId().getDeptName()+"-"+getDesigId().getDesignationName()+"-"+getPostName();
	}

}
