/*
 * @(#)DepartmentServiceImpl.java 3.0, 16 Jun, 2013 10:36:20 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.rjbac.dept.ejb.server;

import java.util.List;

import org.egov.exceptions.DuplicateElementException;
import org.egov.lib.rjbac.dept.Department;
import org.egov.lib.rjbac.dept.dao.DepartmentDAO;
import org.egov.lib.rjbac.dept.ejb.api.DepartmentService;
import org.egov.lib.rjbac.role.Role;
import org.egov.lib.rjbac.user.User;

public class DepartmentServiceImpl implements DepartmentService {

	@Override
	public void createDepartment(final Department dept) throws DuplicateElementException {
		new DepartmentDAO().createDepartment(dept);

	}

	@Override
	public void updateDepartment(final Department dept) {
		new DepartmentDAO().updateDepartment(dept);

	}

	@Override
	public Department getDepartment(final Integer deptId) {
		return new DepartmentDAO().getDepartment(deptId);
	}

	@Override
	public Department getDepartment(final String deptName) {
		return new DepartmentDAO().getDepartment(deptName);
	}

	@Override
	public List<Department> getAllDepartments() {
		return new DepartmentDAO().getAllDepartments();
	}

	@Override
	public void removeDepartment(final Department dept) {
		new DepartmentDAO().removeDepartment(dept);
		return;
	}

	@Override
	public Department getDepartmentById(final Long id) {
		return new DepartmentDAO().getDepartmentById(id);
	}

	@Override
	public Department getDepartmentByCode(final String deptCode) {
		return new DepartmentDAO().getDepartmentByCode(deptCode);
	}

	@Override
	public List<User> getAllUsersByDept(final Department dept, final int topBoundaryID) {
		return new DepartmentDAO().getAllUsersByDept(dept, topBoundaryID);
	}

	@Override
	public List<User> getAllUsersByDept(final Department dept, final List<Role> roles, final int topBoundaryID) {
		return new DepartmentDAO().getAllUsersByDept(dept, roles, topBoundaryID);
	}
}
