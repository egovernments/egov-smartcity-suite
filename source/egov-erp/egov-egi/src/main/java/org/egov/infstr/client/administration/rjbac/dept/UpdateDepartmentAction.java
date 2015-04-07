/*
 * @(#)UpdateDepartmentAction.java 3.0, 18 Jun, 2013 3:16:25 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.client.administration.rjbac.dept;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.exceptions.EGOVException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.client.EgovAction;
import org.egov.infstr.client.EgovActionForm;
import org.egov.infstr.client.delegate.DepartmentDelegate;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.infra.admin.master.entity.Department;

public class UpdateDepartmentAction extends EgovAction {
	private static final Logger logger = LoggerFactory.getLogger(UpdateDepartmentAction.class);

	/**
	 * This method updates an existing department
	 * @param ActionMapping mapping
	 * @param ActionForm form
	 * @param HttpServletRequest req
	 * @param HttpServletResponse res
	 * @return ActionForward
	 **/

	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws IOException, ServletException, EGOVException {
		String target = "";
		String message = "";
		Department department = null;
		final DepartmentForm deptform = (DepartmentForm) form;
		final DepartmentDelegate departmentDelegate = DepartmentDelegate.getInstance();
		final javax.servlet.http.HttpSession session = req.getSession();
		if (deptform.getBillingLocation().equalsIgnoreCase("on")) {
			deptform.setBillingLocation("1");
		} else {
			deptform.setBillingLocation("0");
		}
		if (req.getParameter("bool").equals("UPDATE")) {
			department = (Department) session.getAttribute("DEPARTMENT");
			final String old_deptName = department.getName();

			deptform.populate(department, EgovActionForm.TO_OBJECT);
			try {
				departmentDelegate.updateDepartment(department);
				target = "success";
				message = "Department " + old_deptName + " has been Updated Successfully!!";
				EgovMasterDataCaching.getInstance().removeFromCache("egi-department");
				req.setAttribute("MESSAGE", message);
				deptform.reset(mapping, req);
			} catch (final EGOVRuntimeException ere) {
				target = "error";
				logger.error("EGOVRuntimeException Encountered!!!" + ere.getMessage());
				deptform.reset(mapping, req);
				throw new EGOVRuntimeException("Exception occured -----> " + ere.getMessage());

			}
		}
		return mapping.findForward(target);
	}
}
