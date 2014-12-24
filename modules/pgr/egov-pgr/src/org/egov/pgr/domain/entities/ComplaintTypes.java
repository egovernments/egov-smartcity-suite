/*
 * @(#)ComplaintTypes.java 3.0, 23 Jul, 2013 3:29:44 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.pgr.domain.entities;

import org.egov.infstr.models.BaseModel;
import org.egov.lib.rjbac.dept.DepartmentImpl;

public class ComplaintTypes extends BaseModel {

	private static final long serialVersionUID = 1L;
	private DepartmentImpl department;
	private ComplaintGroup complaintGroup;
	private Category category;
	private String name;
	private String nameLocal;
	private String code;
	private Integer noofdays;
	private String isActive;

	public DepartmentImpl getDepartment() {
		return this.department;
	}

	public void setDepartment(final DepartmentImpl department) {
		this.department = department;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getNameLocal() {
		return this.nameLocal;
	}

	public void setNameLocal(final String nameLocal) {
		this.nameLocal = nameLocal;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(final String code) {
		this.code = code;
	}

	public Integer getNoofdays() {
		return this.noofdays;
	}

	public void setNoofdays(final Integer noofdays) {
		this.noofdays = noofdays;
	}

	public String getIsActive() {
		return this.isActive;
	}

	public void setIsActive(final String isActive) {
		this.isActive = isActive;
	}

	public ComplaintGroup getComplaintGroup() {
		return this.complaintGroup;
	}

	public void setComplaintGroup(final ComplaintGroup complaintGroup) {
		this.complaintGroup = complaintGroup;
	}

	public Category getCategory() {
		return this.category;
	}

	public void setCategory(final Category category) {
		this.category = category;
	}

}