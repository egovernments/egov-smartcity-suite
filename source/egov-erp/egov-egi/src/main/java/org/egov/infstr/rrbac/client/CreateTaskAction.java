/*
 * @(#)CreateTaskAction.java 3.0, 17 Jun, 2013 5:14:58 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
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
