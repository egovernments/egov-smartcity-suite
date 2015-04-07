/*
 * @(#)DepartmentAction.java 3.0, 18 Jun, 2013 3:22:12 PM
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
import org.egov.exceptions.DuplicateElementException;
import org.egov.exceptions.EGOVException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infstr.client.EgovAction;
import org.egov.infstr.client.EgovActionForm;
import org.egov.infstr.client.delegate.DepartmentDelegate;
import org.egov.infstr.utils.EgovMasterDataCaching;

public class DepartmentAction extends EgovAction {
	private static final Logger logger = LoggerFactory.getLogger(DepartmentAction.class);
	private DepartmentService departmentService = new DepartmentService();
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
		if (req.getParameter("bool").equals("CREATE")) {
			try {
				department = new Department();
				deptform.populate(department, EgovActionForm.TO_OBJECT);
				departmentDelegate.createDepartment(department);
				EgovMasterDataCaching.getInstance().removeFromCache("egi-department");
				target = "success";
				message = "Department " + department.getName() + " has been created successfully";
				req.setAttribute("MESSAGE", message);
				deptform.reset(mapping, req);
			} catch (final DuplicateElementException dupexp) {
				target = "success";
				message = "Duplicate Department " + department.getName() + " exists";
				req.setAttribute("MESSAGE", message);
				deptform.reset(mapping, req);
				throw new EGOVRuntimeException("Exception occured -----> " + dupexp.getMessage());
			} catch (final EGOVRuntimeException e) {
				logger.error("EGOVRuntimeException Encountered  IN DepartmentAction :::::::::::!!!" + e.getMessage());
				target = "error";
				req.setAttribute("MESSAGE", "Error Creating Department !!");
				throw new EGOVRuntimeException("Exception occured -----> " + e.getMessage());
			} catch (final Exception c) {
				logger.error("Exception in DepartmentAction" + c.getMessage());
				target = "error";
				req.setAttribute("MESSAGE", "Error Creating Department !!");
				throw new EGOVRuntimeException("Exception occured -----> " + c.getMessage());
			}
		}
		if (req.getParameter("bool").equals("UPDATE")) {
			try {

				final int deptid = (Integer) session.getAttribute("deptid");
				department = departmentService.getDepartmentById(Long.valueOf(deptid));
				department.setName(deptform.getName());
				department.setCode(deptform.getCode());

				departmentDelegate.updateDepartment(department);
				EgovMasterDataCaching.getInstance().removeFromCache("egi-department");
				target = "success";
				message = "Department " + department.getName() + " has been updated successfully";
				req.setAttribute("MESSAGE", message);
				deptform.reset(mapping, req);
			} catch (final EGOVRuntimeException e) {
				logger.error("EGOVRuntimeException Encountered  IN DepartmentAction :::::::::::!!!" + e.getMessage());
				target = "error";
				req.setAttribute("MESSAGE", "Error Updating dept !!");
				deptform.reset(mapping, req);
				throw new EGOVRuntimeException("Exception occured -----> " + e.getMessage());
			} catch (final Exception c) {
				logger.error("Exception in DepartmentAction" + c.getMessage());
				target = "error";
				req.setAttribute("MESSAGE", "Error Updating dept !!");
				throw new EGOVRuntimeException("Exception occured -----> " + c.getMessage());
			}
		}
		if (req.getParameter("bool").equals("DELETE")) {

			try {
				final int deptid = Integer.parseInt(deptform.getDeptid());
				departmentDelegate.removeDepartment(deptid);
				EgovMasterDataCaching.getInstance().removeFromCache("egi-department");
				target = "success";
				message = "Department has been deleted successfully";
				req.setAttribute("MESSAGE", message);
				deptform.reset(mapping, req);
			} catch (final EGOVRuntimeException e) {
				logger.error("EGOVRuntimeException Encountered  IN DepartmentAction :::::::::::!!!" + e.getMessage());
				target = "error";
				req.setAttribute("MESSAGE", "Error Deleting dept !!");
			} catch (final Exception c) {
				logger.error("Exception in DepartmentAction" + c.getMessage());
				target = "error";
				req.setAttribute("MESSAGE", "Error Deleting dept !!");
			}
		}
		return mapping.findForward(target);

	}
}
