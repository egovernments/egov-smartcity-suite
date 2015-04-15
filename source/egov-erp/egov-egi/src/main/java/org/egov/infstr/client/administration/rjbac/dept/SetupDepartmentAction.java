/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.infstr.client.administration.rjbac.dept;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.exceptions.EGOVException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infstr.client.EgovAction;
import org.egov.infstr.client.delegate.DepartmentDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class SetupDepartmentAction extends EgovAction {
	private static final Logger logger = LoggerFactory.getLogger(SetupDepartmentAction.class);
	@Autowired
	private DepartmentService departmentService;
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

		Department department = new Department();
		List deptList = new ArrayList();
		final DepartmentForm deptform = (DepartmentForm) form;
		final DepartmentDelegate departmentDelegate = DepartmentDelegate.getInstance();
		String target = "";
		if (req.getParameter("bool").equals("VIEW")) {

			try {
				if (!deptform.getDeptid().equals("")) {

					department = departmentService.getDepartmentById(Long.valueOf(deptform.getDeptid()));
					deptform.setDeptName(department.getName());
					deptform.setDeptCode(department.getCode());
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
					department = departmentService.getDepartmentById(Long.valueOf(deptform.getDeptid()));
					deptform.setDeptName(department.getName());
					deptform.setDeptCode(department.getCode());
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
