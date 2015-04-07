/*
 * @(#)DeptDesig.java 3.0, 7 Jun, 2013 8:39:52 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.pims.commons;

import org.egov.infstr.models.BaseModel;
import org.egov.infra.admin.master.entity.Department;

public class DeptDesig extends BaseModel { 
	private static final long serialVersionUID = 1L;
	private DesignationMaster desigId;
	private Department deptId;
	private Integer sanctionedPosts;
	private Integer outsourcedPosts;
	
	public DesignationMaster getDesigId() {
		return desigId;
	}
	public void setDesigId(DesignationMaster desigId) {
		this.desigId = desigId;
	}
	public Department getDeptId() {
		return deptId;
	}
	public void setDeptId(Department deptId) {
		this.deptId = deptId;
	}
	public Integer getSanctionedPosts() {
		return sanctionedPosts;
	}
	public void setSanctionedPosts(Integer sanctionedPosts) {
		this.sanctionedPosts = sanctionedPosts;
	}
	public Integer getOutsourcedPosts() {
		return outsourcedPosts;
	}
	public void setOutsourcedPosts(Integer outsourcedPosts) {
		this.outsourcedPosts = outsourcedPosts;
	}

}
