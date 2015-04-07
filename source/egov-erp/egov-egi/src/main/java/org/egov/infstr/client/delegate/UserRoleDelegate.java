/*
 * @(#)UserRoleDelegate.java 3.0, 18 Jun, 2013 1:38:11 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.infstr.client.delegate;

import java.util.ArrayList;
import java.util.List;

import org.egov.exceptions.DuplicateElementException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.admin.master.service.RoleService;
import org.egov.infra.admin.master.entity.Department;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRoleDelegate {

	private static UserRoleDelegate userRoleDelegate = new UserRoleDelegate();
	private static Logger logger = LoggerFactory.getLogger(UserRoleDelegate.class);
	@Autowired
	private  RoleService roleService;
	@Autowired
	private DepartmentService departmentService;

	private UserRoleDelegate() {
	}

	public static UserRoleDelegate getInstance() {
		return userRoleDelegate;
	}

	/**
	 * This method sets the parent role,department for the role to be created
	 * @param role
	 * @param deptid
	 * @param parentRoleId
	 * @throws DuplicateElementException
	 */
	public void createRole(final Role role, final Integer parentRoleId) throws DuplicateElementException {
		// Set the role of the parent
		final Role parent = getRole(parentRoleId);
		//role.setParent(parent);

		createRole(role);
	}

	/**
	 * This method creates a role in the System
	 * @param role
	 * @throws DuplicateElementException
	 */
	public void createRole(final Role role) throws DuplicateElementException {

		// to be implemented by serverside code i.e dept.addRole(role)
		this.roleService.createRole(role);
	}

	/**
	 * This method removes a role from the System
	 * @param deptid
	 * @param roleid
	 */
	public void removeRole(final Integer roleid) {
		Role role = null;
		try {
			role = getRole(roleid);

			role = this.roleService.getRoleByName(role.getName());
			// role.removeAllReportees();
			this.roleService.remove(role);
		} catch (final Exception e) {
			logger.info("Exception Encountered!!!" + e.getMessage());
			throw new EGOVRuntimeException("Internal Server Error in deleting the role", e);
		}
	}

	/**
	 * This method is used for updating an existing role
	 * @param role
	 * @param parentRoleId
	 * @param deptid
	 */
	public void updateRole(final Role role, final Integer parentRoleId) {
		try {
			final Role parent = getRole(parentRoleId);
			//role.setParent(parent);

			this.roleService.update(role);
		} catch (final Exception exp) {
			logger.info("Exception Encountered!!!" + exp.getMessage());
			throw new EGOVRuntimeException("Internal Server Error in Updating Role", exp);
		}
	}

	/**
	 * This method is used for getting a department by departmentid
	 * @param deptid
	 * @return Department
	 */
	public Department getDepartment(final int deptid) {
		Department dept = null;
		try {
			dept = this.departmentService.getDepartmentById(Long.valueOf(deptid));
		} catch (final Exception e) {
			logger.info("Exception Encountered!!!" + e.getMessage());
			throw new EGOVRuntimeException("Internal Server Error in getting department", e);
		}
		return dept;
	}

	/**
	 * This method is used for getting a role by roleid
	 * @param roleId
	 * @return Role
	 */
	public Role getRole(final Integer roleId) {
		Role role = null;
		try {
			if (roleId != null && roleId != 0) {
				role = this.roleService.getRoleById(roleId.longValue());
			}
		} catch (final Exception e) {
			logger.info("Exception Encountered!!!" + e.getMessage());
			throw new EGOVRuntimeException("Internal Server Error in getting Role", e);
		}
		return role;
	}

	/**
	 * This method is used for getting a List of all the departments in the system
	 * @return a List of all the departments
	 */
	public List getAlldepartments() {
		List deptList = new ArrayList();
		try {
			deptList = this.departmentService.getAllDepartments();
		} catch (final Exception e) {
			logger.info("Exception Encountered!!!" + e.getMessage());
			throw new EGOVRuntimeException("Internal Server Error in getting DepartmentList", e);

		}
		return deptList;
	}

	/**
	 * This method is used for getting all th roles in the system
	 * @return aList of Roles
	 */
	public List getAllRoles() {
		List roleList = new ArrayList();
		try {
			roleList = this.roleService.getAllRoles();
		} catch (final Exception e) {
			logger.info("Exception Encountered!!!" + e.getMessage());
			throw new EGOVRuntimeException("Internal Server Error in getting DepartmentList", e);
		}
		return roleList;
	}

	/**
	 * This method is used for getting a department by departmentName
	 * @param dname
	 * @return Department
	 */
	public Department getDepartmentbyName(final String dname) {
		Department dept = null;
		try {
			dept = this.departmentService.getDepartmentById(Long.valueOf(dname));
		} catch (final Exception e) {
			logger.info("Exception Encountered!!!" + e.getMessage());
			throw new EGOVRuntimeException("Internal Server Error in getting DepartmentList", e);
		}
		return dept;
	}
}
