/*
 * @(#)CreateEntityAction.java 3.0, 17 Jun, 2013 5:07:34 PM
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
import org.egov.lib.rrbac.model.Entity;
import org.egov.lib.rrbac.services.RbacService;
import org.egov.lib.rrbac.services.RbacServiceImpl;

public class CreateEntityAction extends Action {

	private static final Logger LOG = LoggerFactory.getLogger(CreateEntityAction.class);
	private final RbacService rbacService = new RbacServiceImpl();

	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws Exception {
		String target = "";
		String alertMessage = null;
		try {

			final org.apache.struts.action.DynaActionForm mfb = (org.apache.struts.action.DynaActionForm) form;
			final String submitType = req.getParameter("submitType");

			if (submitType.equalsIgnoreCase("Create")) {
				final String entityName = (String) mfb.get("entityName");
				final Entity dupEnt = this.rbacService.getEntityByName(entityName);
				if (dupEnt == null) {
					final Entity ent = new Entity();
					ent.setName(entityName);
					this.rbacService.createEntity(ent);
					HibernateUtil.getCurrentSession().flush();
					alertMessage = "Executed successfully";
				} else {
					alertMessage = "Duplicate Entity name.";
				}

				req.setAttribute("alertMessage", alertMessage);
				target = "success";

			} else if (submitType.equalsIgnoreCase("Update")) {
				final Integer modEntityId = (Integer) mfb.get("modEntityId");
				final String modEntityName = (String) mfb.get("modEntityName");
				final Integer entityid = modEntityId;
				final Entity ent = this.rbacService.getEntityByID(entityid);
				ent.setName(modEntityName);
				this.rbacService.updateEntity(ent);
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

		}

		catch (final Exception ex) {
			target = "error";
			LOG.error("Exception Encountered!!!" + ex.getMessage());
			throw new EGOVRuntimeException("Exception occured -----> " + ex.getMessage());

		}

		return mapping.findForward(target);

	}
}
