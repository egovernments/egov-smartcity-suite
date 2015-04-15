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
