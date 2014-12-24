/*
 * @(#)CreateAction.java 3.0, 17 Jun, 2013 5:00:42 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.rrbac.client;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.rrbac.model.Action;

public class CreateAction extends org.apache.struts.action.Action {
	private static final Logger LOG = LoggerFactory.getLogger(CreateAction.class);
	private final ActionDelegate actionDelegate = ActionDelegate.getInstance();

	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws Exception {
		String target = "";
		String alertMessage = null;
		try {

			final org.apache.struts.action.DynaActionForm mfb = (org.apache.struts.action.DynaActionForm) form;
			final String submitType = req.getParameter("submitType");
			if (submitType.equalsIgnoreCase("Create")) {

				final String actionName = (String) mfb.get("actionName");
				final Action dupAct = this.actionDelegate.getAction(actionName);
				if (dupAct == null) {
					final Integer entityId = (Integer) mfb.get("entityId");
					final Integer taskId = (Integer) mfb.get("taskId");
					this.actionDelegate.createAction(actionName, entityId, taskId);

					HibernateUtil.getCurrentSession().flush();
					alertMessage = "Executed successfully";

				} else {
					alertMessage = "Duplicate Action name.";
				}

				req.setAttribute("alertMessage", alertMessage);
				target = "success";
			} else if (submitType.equalsIgnoreCase("Update")) {
				final String actionName = (String) mfb.get("actionName");
				final Integer actionId = (Integer) mfb.get("actionId");
				final Integer entityId = (Integer) mfb.get("entityId");
				final Integer taskId = (Integer) mfb.get("taskId");
				this.actionDelegate.updateAction(actionName, actionId, entityId, taskId);

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
			LOG.info("Exception Encountered!!!" + ex.getMessage());
			throw new EGOVRuntimeException("Exception occured -----> " + ex.getMessage());

		}
		return mapping.findForward(target);

	}
}
