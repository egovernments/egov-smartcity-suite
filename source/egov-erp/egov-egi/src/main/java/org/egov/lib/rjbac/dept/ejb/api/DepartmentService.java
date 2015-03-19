/*
 * @(#)IDepartmentManager.java 3.0, 16 Jun, 2013 10:19:50 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.rjbac.dept.ejb.api;

import java.util.List;

import org.egov.exceptions.DuplicateElementException;
import org.egov.infra.admin.master.entity.User;
import org.egov.lib.rjbac.dept.Department;
import org.egov.lib.rjbac.role.Role;

public interface DepartmentService {
	/**
	 * Creates a new Department in the System
	 * @param Department.
	 */

	void createDepartment(Department dept) throws DuplicateElementException;;

	void updateDepartment(Department dept);

	/**
	 * Returns the department with the given id
	 * @param deptID the departmentID
	 * @return Department
	 */
	Department getDepartment(Integer deptId);

	/**
	 * Returns the department which matches the deptname exactly
	 * @param deptName Department name
	 * @return Department
	 */
	Department getDepartment(String deptName);

	/**
	 * Gets the List of all the departments in the System
	 * @return List of all departments in the System
	 */
	List<Department> getAllDepartments();

	/**
	 * Gets the List of all the users in the given department
	 * @param Dept Department
	 * @return List of all users in the System with the given department
	 */
	List<User> getAllUsersByDept(Department dept, int topBoundaryID);

	/**
	 * Removes the department and associated Roles and Users from the System
	 * @param deptID Department ID
	 */
	void removeDepartment(Department department);

	List<User> getAllUsersByDept(Department dept, List<Role> roleList, int topBoundaryID);

	Department getDepartmentById(Long id);

	Department getDepartmentByCode(String deptCode);

}
