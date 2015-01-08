package org.egov.infstr.client.delegate;

import org.egov.exceptions.DuplicateElementException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.lib.rjbac.dept.Department;
import org.egov.lib.rjbac.dept.ejb.api.DepartmentService;
import org.egov.lib.rjbac.dept.ejb.server.DepartmentServiceImpl;

public class DepartmentDelegate {
	private static DepartmentDelegate departmentDelegate = new DepartmentDelegate();
	private final DepartmentService departmentService = new DepartmentServiceImpl();

	private DepartmentDelegate() {
	}

	public static DepartmentDelegate getInstance() {
		return departmentDelegate;
	}

	/**
	 * This method creates a department in the system
	 * @param dept
	 * @throws DuplicateElementException
	 */
	public void createDepartment(final Department dept) throws DuplicateElementException {
		this.departmentService.createDepartment(dept);

	}

	/**
	 * This method removes an existing department from the system
	 * @param deptid
	 */
	public void removeDepartment(final Integer deptid) {
		try {
			final Department dept = this.departmentService.getDepartment(deptid);
			this.departmentService.removeDepartment(dept);
		} catch (final Exception exp) {
			throw new EGOVRuntimeException("Internal Server Error deleting a department", exp);
		}
	}

	/**
	 * This method is used for updating an existing department in the system
	 * @param dept
	 */
	public void updateDepartment(final Department dept) {
		try {
			this.departmentService.updateDepartment(dept);
		} catch (final Exception exp) {
			throw new EGOVRuntimeException("Internal Server Error in Updating Department", exp);
		}
	}
}
