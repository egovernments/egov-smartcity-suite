/*
 * @(#)SetupDepartmentAction.java 3.0, 18 Jun, 2013 3:18:35 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.client.administration.rjbac.dept;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.exceptions.EGOVException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.client.EgovAction;
import org.egov.infstr.client.delegate.DepartmentDelegate;
import org.egov.lib.rjbac.dept.Department;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.lib.rjbac.dept.ejb.api.DepartmentService;
import org.egov.lib.rjbac.dept.ejb.server.DepartmentServiceImpl;

public class SetupDepartmentAction extends EgovAction {
	private static final Logger logger = LoggerFactory.getLogger(SetupDepartmentAction.class);
	private DepartmentService departmentService = new DepartmentServiceImpl();
	/**
	 * This method creates a new department
	 * @param ActionMapping mapping
	 * @param ActionForm form
	 * @param HttpServletRequest req
	 * @param HttpServletResponse res
	 * @return ActionForward
	 **/
	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws IOException, ServletException, EGOVException, Exception {

		Department department = new DepartmentImpl();
		List deptList = new ArrayList();
		final DepartmentForm deptform = (DepartmentForm) form;
		final DepartmentDelegate departmentDelegate = DepartmentDelegate.getInstance();
		String target = "";
		if (req.getParameter("bool").equals("VIEW")) {

			try {
				if (!deptform.getDeptid().equals("")) {

					final int deptid = Integer.parseInt(deptform.getDeptid());
					department = departmentService.getDepartment(deptid);
					final int billinglocation = Integer.parseInt(department.getBillingLocation());
					req.getSession().setAttribute("billinglocation", billinglocation);
					deptform.setDeptName(department.getDeptName());
					deptform.setDeptCode(department.getDeptCode());
					deptform.setDeptDetails(department.getDeptDetails());
					deptform.setDeptAddress(department.getDeptAddress());
					deptform.setBillingLocation(department.getBillingLocation());
					target = "viewDept";
				} else {

					deptList = departmentService.getAllDepartments();
					target = "success";
				}
			} catch (final EGOVRuntimeException e) {
				logger.error("EGOVRuntimeException Encountered  IN SetupDepartmentAction :::::::::::!!!" + e.getMessage());
				target = "error";
				req.setAttribute("MESSAGE", "Error before deptAction !!");
				throw new EGOVRuntimeException("Exception occured -----> " + e.getMessage());
			} catch (final Exception c) {
				logger.error("Exception in DepartmentAction" + c.getMessage());
				target = "error";
				req.setAttribute("MESSAGE", "Error before DeptAction !!");
				throw new EGOVRuntimeException("Exception occured -----> " + c.getMessage());

			}

		}
		if (req.getParameter("bool").equals("UPDATE")) {

			try {
				final HttpSession session = req.getSession();
				if (!deptform.getDeptid().equals("")) {
					final int deptid = Integer.parseInt(deptform.getDeptid());
					session.setAttribute("deptid", deptid);
					department = departmentService.getDepartment(deptid);
					final int billinglocation = Integer.parseInt(department.getBillingLocation());
					req.getSession().setAttribute("billinglocation", billinglocation);
					deptform.setDeptName(department.getDeptName());
					deptform.setDeptCode(department.getDeptCode());
					deptform.setDeptDetails(department.getDeptDetails());
					deptform.setDeptAddress(department.getDeptAddress());
					target = "updateDept";
				}

			} catch (final EGOVRuntimeException e) {
				logger.error("EGOVRuntimeException Encountered  IN SetupDepartmentAction :::::::::::!!!" + e.getMessage());
				target = "error";
				req.setAttribute("MESSAGE", "Error before Dept Action !!");
				throw new EGOVRuntimeException("Exception occured -----> " + e.getMessage());

			} catch (final Exception c) {
				logger.error("Exception in SetupDepartmentAction" + c.getMessage());
				target = "error";
				req.setAttribute("MESSAGE", "Error before Dept Action !!");
				throw new EGOVRuntimeException("Exception occured -----> " + c.getMessage());

			}

		}

		req.getSession().setAttribute("deptList", deptList);
		return mapping.findForward(target);

	}
}
