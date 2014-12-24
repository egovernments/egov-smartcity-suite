/*
 * @(#)InternalUser.java 3.0, 15 Jul, 2013 9:34:38 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.dms.models;

import java.io.Serializable;

import javax.validation.Valid;

import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.commons.Position;
import org.egov.pims.model.PersonalInformation;

/**
 * The Class InternalUser.
 */
public class InternalUser implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The id. */
	private Long id;
	
	/** The designation. */
	@Valid
	private DesignationMaster designation;
	
	/** The department. */
	@Valid
	private DepartmentImpl department;
	
	/** The position. */
	@Valid
	private Position position;
	
	/** The employee info. */
	@Valid
	private PersonalInformation employeeInfo;
	
	/**
	 * Gets the id.
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * Sets the id.
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * Gets the designation.
	 * @return the designation
	 */
	public DesignationMaster getDesignation() {
		return designation;
	}
	
	/**
	 * Sets the designation.
	 * @param designation the designation to set
	 */
	public void setDesignation(DesignationMaster designation) {
		this.designation = designation;
	}
	
	/**
	 * Gets the department.
	 * @return the department
	 */
	public DepartmentImpl getDepartment() {
		return department;
	}
	
	/**
	 * Sets the department.
	 * @param department the department to set
	 */
	public void setDepartment(DepartmentImpl department) {
		this.department = department;
	}
	
	/**
	 * Gets the position.
	 * @return the position
	 */
	public Position getPosition() {
		return position;
	}
	
	/**
	 * Sets the position.
	 * @param position the position to set
	 */
	public void setPosition(Position position) {
		this.position = position;
	}
	
	/**
	 * Gets the employee info.
	 * @return the employee
	 */
	public PersonalInformation getEmployeeInfo() {
		return employeeInfo;
	}
	
	/**
	 * Sets the employee info.
	 * @param employeeInfo the new employee info
	 */
	public void setEmployeeInfo(PersonalInformation employeeInfo) {
		this.employeeInfo = employeeInfo;
	}
}
