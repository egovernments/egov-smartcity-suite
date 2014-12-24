package org.egov.pims.commons;

import org.egov.commons.ObjectType;
import org.egov.infstr.models.BaseModel;
import org.egov.lib.rjbac.dept.DepartmentImpl;


/**
 * @author jagadeesan
 *
 */
public class DesignationHierarchy extends BaseModel implements java.io.Serializable
{
	private static final long serialVersionUID = 1L;
	private ObjectType objectType;
	private DepartmentImpl department;
	private DesignationMaster fromDesig;
	private DesignationMaster toDesig;
	
	public ObjectType getObjectType() {
		return objectType;
	}
	public void setObjectType(ObjectType objectType) {
		this.objectType = objectType;
	}
	public DepartmentImpl getDepartment() {
		return department;
	}
	public void setDepartment(DepartmentImpl department) {
		this.department = department;
	}
	public DesignationMaster getFromDesig() {
		return fromDesig;
	}
	public void setFromDesig(DesignationMaster fromDesig) {
		this.fromDesig = fromDesig;
	}
	public DesignationMaster getToDesig() {
		return toDesig;
	}
	public void setToDesig(DesignationMaster toDesig) {
		this.toDesig = toDesig;
	}
}
