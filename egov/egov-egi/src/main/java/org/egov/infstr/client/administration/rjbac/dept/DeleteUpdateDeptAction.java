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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infstr.client.EgovAction;
import org.egov.infstr.client.delegate.DepartmentDelegate;
import org.egov.infstr.client.delegate.UserRoleDelegate;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
			department = new Department();
			final String deptid = req.getParameter("deptid");
			try {
				department = this.roleDelegate.getDepartment(Integer.parseInt(deptid));
				session.setAttribute("DEPARTMENT", department);
				deptForm.setDeptName(department.getName());
				deptForm.setDeptCode(department.getCode());
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
				message = "Department " + dept.getName() + " has been Romoved successfully";
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
			department = new Department();
			final String deptid = req.getParameter("deptid");
			try {
				department = this.roleDelegate.getDepartment(Integer.parseInt(deptid));
				session.setAttribute("DEPARTMENT", department);
				deptForm.setDeptName(department.getName());
				deptForm.setDeptCode(department.getCode());
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
