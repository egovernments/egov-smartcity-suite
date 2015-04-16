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
package org.egov.infstr.rrbac.client;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.rrbac.model.Task;
import org.egov.lib.rrbac.services.RbacService;
import org.egov.lib.rrbac.services.RbacServiceImpl;

public class CreateTaskAction extends Action {
	private static final Logger LOG = LoggerFactory.getLogger(CreateTaskAction.class);
	private final RbacService rbacService = new RbacServiceImpl();

	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws Exception {
		String target = "";
		String alertMessage = null;
		try {

			final org.apache.struts.action.DynaActionForm mfb = (org.apache.struts.action.DynaActionForm) form;

			final String submitType = req.getParameter("submitType");
			if (submitType.equalsIgnoreCase("Create")) {
				final String taskName = (String) mfb.get("taskName");
				final Task dupTsk = this.rbacService.getTaskByName(taskName);
				if (dupTsk == null) {
					final Task tsk = new Task();
					tsk.setName(taskName);
					this.rbacService.createTask(tsk);

					HibernateUtil.getCurrentSession().flush();
					alertMessage = "Executed successfully";

				} else {
					alertMessage = "Duplicate Task name.";
				}
				req.setAttribute("alertMessage", alertMessage);
				target = "success";
			} else if (submitType.equalsIgnoreCase("Update")) {
				final Integer modTaskId = (Integer) mfb.get("modTaskId");
				final String modTaskName = (String) mfb.get("modTaskName");
				final Integer taskid = modTaskId;
				final Task tsk = this.rbacService.getTaskByID(taskid);
				tsk.setName(modTaskName);
				this.rbacService.updateTask(tsk);

				HibernateUtil.getCurrentSession().flush();
				alertMessage = "Executed successfully";
				req.setAttribute("alertMessage", alertMessage);

				target = "success";
			} else {
				target = "error";
				LOG.error("Submit type is not configured.It should be either 'Create' or 'Update'.");
				alertMessage = "Configuration error.plrase report this to administrator.";
				req.setAttribute("alertMessage", alertMessage);
			}
		} catch (final Exception ex) {
			target = "error";
			LOG.error("Exception Encountered!!!" + ex.getMessage());
			throw new EGOVRuntimeException("Exception occured -----> " + ex.getMessage());

		}
		return mapping.findForward(target);

	}
}
