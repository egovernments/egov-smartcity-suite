/*
 * @(#)DepartmentServiceImpl.java 3.0, 16 Jun, 2013 10:36:20 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.rjbac.dept.ejb.server;

import org.egov.exceptions.DuplicateElementException;
import org.egov.lib.rjbac.dept.Department;
import org.egov.lib.rjbac.dept.dao.DepartmentDAO;
import org.egov.lib.rjbac.dept.ejb.api.DepartmentService;
import org.egov.lib.rjbac.role.Role;
import org.egov.lib.rjbac.user.User;

import java.util.List;

public class DepartmentServiceImpl implements DepartmentService {

	private DepartmentDAO departmentDAO;

	@Deprecated
	public DepartmentServiceImpl() {
	}

	public DepartmentServiceImpl(DepartmentDAO departmentDAO) {
		this.departmentDAO = departmentDAO;
	}

	@Override
	public void createDepartment(final Department dept) throws DuplicateElementException {
		departmentDAO.createDepartment(dept);

	}

	@Override
	public void updateDepartment(final Department dept) {
		departmentDAO.updateDepartment(dept);

	}

	@Override
	public Department getDepartment(final Integer deptId) {
		return departmentDAO.getDepartment(deptId);
	}

	@Override
	public Department getDepartment(final String deptName) {
		return departmentDAO.getDepartment(deptName);
	}

	@Override
	public List<Department> getAllDepartments() {
		return departmentDAO.getAllDepartments();
	}

	@Override
	public void removeDepartment(final Department dept) {
		departmentDAO.removeDepartment(dept);
	}

	@Override
	public Department getDepartmentById(final Long id) {
		return departmentDAO.getDepartmentById(id);
	}

	@Override
	public Department getDepartmentByCode(final String deptCode) {
		return departmentDAO.getDepartmentByCode(deptCode);
	}

	@Override
	public List<User> getAllUsersByDept(final Department dept, final int topBoundaryID) {
		return departmentDAO.getAllUsersByDept(dept, topBoundaryID);
	}

	@Override
	public List<User> getAllUsersByDept(final Department dept, final List<Role> roles, final int topBoundaryID) {
		return departmentDAO.getAllUsersByDept(dept, roles, topBoundaryID);
	}
}
