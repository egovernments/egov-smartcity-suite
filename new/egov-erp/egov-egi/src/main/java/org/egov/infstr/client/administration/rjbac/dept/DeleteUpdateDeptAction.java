/*
 * @(#)DeleteUpdateDeptAction.java 3.0, 18 Jun, 2013 3:23:11 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.client.administration.rjbac.dept;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.client.EgovAction;
import org.egov.infstr.client.delegate.DepartmentDelegate;
import org.egov.infstr.client.delegate.UserRoleDelegate;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.lib.rjbac.dept.Department;
import org.egov.lib.rjbac.dept.DepartmentImpl;

public class DeleteUpdateDeptAction extends EgovAction {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	DepartmentDelegate deptDelegate = DepartmentDelegate.getInstance();
	UserRoleDelegate roleDelegate = UserRoleDelegate.getInstance();

	/**
	 * This method deletes,sets up details for updating a department depending upon the client's request
	 * @param ActionMapping mapping
	 * @param ActionForm form
	 * @param HttpServletRequest req
	 * @param HttpServletResponse res
	 * @return ActionForward
	 **/

	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws Exception {
		String target = "";
		String message = "";
		final javax.servlet.http.HttpSession session = req.getSession();
		final DepartmentForm deptForm = (DepartmentForm) form;
		Department department = null;
		final String str = (req.getParameter("bool") == null || req.getParameter("bool").equals("null") ? "" : req.getParameter("bool"));
		if (str.equals("")) {

			this.logger.info("execute::In view department ");
			department = new DepartmentImpl();
			final String deptid = req.getParameter("deptid");
			try {
				department = this.roleDelegate.getDepartment(Integer.parseInt(deptid));
				session.setAttribute("DEPARTMENT", department);
				deptForm.setDeptDetails(department.getDeptDetails());
				deptForm.setDeptName(department.getDeptName());
				deptForm.setDeptCode(department.getDeptCode());
				deptForm.setDeptid(department.getId().toString());
				target = "toViewDept";
				deptForm.reset(mapping, req);
			} catch (final EGOVRuntimeException ere) {
				target = "error";
				this.logger.info("EGOVRuntimeException Encountered!!!" + ere.getMessage());
				deptForm.reset(mapping, req);
				throw new EGOVRuntimeException("Exception occured -----> " + ere.getMessage());
			}

		}
		// request is forwarded to confirm page before deletion
		if (str.equals("DELETE")) {
			this.logger.info("execute::In delete ");
			target = "toconfDelete";
		}
		if (str.equals("ConfirmDelete")) {
			try {
				final Department dept = (Department) session.getAttribute("DEPARTMENT");
				this.deptDelegate.removeDepartment(new Integer(req.getParameter("deptid")));
				EgovMasterDataCaching.getInstance().removeFromCache("egi-department");
				target = "success";
				message = "Department " + dept.getDeptName() + " has been Romoved successfully";
				req.setAttribute("MESSAGE", message);
			}

			catch (final EGOVRuntimeException e) {
				this.logger.error("EGOVRuntimeException Encountered  IN DeleteUpdateDeptAction :::::::::::!!!" + e.getMessage());
				target = "error";
				req.setAttribute("MESSAGE", "Error Deleting dept !!");
				throw new EGOVRuntimeException("Exception occured -----> " + e.getMessage());
			} catch (final Exception c) {
				this.logger.error("Exception in BeforeCreateOrUpdateRoleAction" + c.getMessage());
				target = "error";
				req.setAttribute("MESSAGE", "Error Deleting dept !!");
				throw new EGOVRuntimeException("Exception occured -----> " + c.getMessage());
			}
		}

		/**
		 * Gets the details of the department depending on the client's request and sets the Form for updation. Sets the Department object to be modified in session.
		 */
		if (str.equals("EDIT")) {
			this.logger.info("execute::In Edit ");
			department = new DepartmentImpl();
			final String deptid = req.getParameter("deptid");
			try {
				department = this.roleDelegate.getDepartment(Integer.parseInt(deptid));
				session.setAttribute("DEPARTMENT", department);
				deptForm.setDeptDetails(department.getDeptDetails());
				deptForm.setDeptName(department.getDeptName());
				deptForm.setDeptCode(department.getDeptCode());
				deptForm.setDeptid(department.getId().toString());
				EgovMasterDataCaching.getInstance().removeFromCache("egi-department");
				target = "toUpdateDept";
			}

			catch (final EGOVRuntimeException e) {
				this.logger.error("EGOVRuntimeException Encountered  IN DeleteUpdateDeptAction :::::::::::!!!" + e.getMessage());
				target = "error";
				req.setAttribute("MESSAGE", "Error updating dept !!");
				throw new EGOVRuntimeException("Exception occured -----> " + e.getMessage());
			} catch (final Exception c) {
				this.logger.error("Exception in BeforeCreateOrUpdateRoleAction" + c.getMessage());
				target = "error";
				req.setAttribute("MESSAGE", "Error updating dept !!");
				throw new EGOVRuntimeException("Exception occured -----> " + c.getMessage());
			}
		}
		return mapping.findForward(target);
	}
}
