/*
 * @(#)CreateOrUpdateAction.java 3.0, 18 Jun, 2013 3:42:12 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.client.administration.rjbac.action;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.client.EgovAction;
import org.egov.infstr.commons.Module;
import org.egov.infstr.commons.service.GenericCommonsService;
import org.egov.infstr.utils.EgovUtils;
import org.egov.infstr.utils.StringUtils;
import org.egov.lib.rrbac.model.Action;
import org.egov.lib.rrbac.services.RbacService;

public class CreateOrUpdateAction extends EgovAction {

	private static final Logger LOG = LoggerFactory.getLogger(CreateOrUpdateAction.class);
	private GenericCommonsService genericCommonsService;
	private RbacService rbacService;

	public void setGenericCommonsService(final GenericCommonsService genericCommonsService) {
		this.genericCommonsService = genericCommonsService;
	}

	public void setRbacService(final RbacService rbacManager) {
		this.rbacService = rbacManager;
	}

	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) {

		String target = "failure";		
	
		try {
			final org.egov.infstr.client.administration.rjbac.action.ActionForm actionform = (org.egov.infstr.client.administration.rjbac.action.ActionForm) form;
			if (org.apache.commons.lang.StringUtils.isBlank(actionform.getModuleId())) {
				throw new EGOVRuntimeException("Could not get Module Id");
			}

			final Module module = this.genericCommonsService.getModuleByID(Integer.valueOf(actionform.getModuleId()));
			req.setAttribute("module", module);
			module.getActions();

			if ((actionform.getActionName() != null) && (actionform.getActionName().length > 0)) {
				
				// get the actions to be deleted
				if (actionform.getDeleteActionSet() != null) {
					final String[] delActionsSet = actionform.getDeleteActionSet();
					for (final String actionId : delActionsSet) {
						if (StringUtils.isNotBlank(actionId)) {
							final Action delAction = this.rbacService.getActionById(Integer.valueOf(actionId));
							if (delAction != null) {
								if (module.getActions() != null) {
									module.getActions().remove(delAction);
								}
							}

						}
					}
				}

				for (int i = 0; i < actionform.getActionName().length; i++) {
					if (StringUtils.isNotBlank(actionform.getActionName()[i])) {
						Action action = null;
						if ((actionform.getActionId()[i] != null) && !actionform.getActionId()[i].contains("row")) {
							if (StringUtils.isNotBlank(actionform.getActionId()[i])) {
								action = this.rbacService.getActionById(Integer.valueOf(actionform.getActionId()[i]));
								final boolean idExists = this.checkIfActionIdExists(actionform.getIsEnabled(), actionform.getActionId()[i]);
								if (idExists) {
									action.setIsEnabled(Integer.valueOf(1));
								} else {
									action.setIsEnabled(Integer.valueOf(0));
								}
							}
						} else {
							action = new Action();
							action.setModule(module);
							final boolean idExists = this.checkIfActionIdExists(actionform.getIsEnabled(), actionform.getActionId()[i]);
							if (idExists) {
								action.setIsEnabled(Integer.valueOf(1));
							} else {
								action.setIsEnabled(Integer.valueOf(0));
							}

						}
						
						action.setName(actionform.getActionName()[i]);
						
						if (StringUtils.isNotBlank(actionform.getBaseURL()[i])) {
							action.setUrl(actionform.getBaseURL()[i]);
						}

						if (StringUtils.isNotBlank(actionform.getQueryParams()[i])) {
							action.setQueryParams(actionform.getQueryParams()[i]);
						}

						if (StringUtils.isNotBlank(actionform.getUrlOrderId()[i])) {
							action.setUrlOrderId(Integer.valueOf(actionform.getUrlOrderId()[i]));
						}

						if (StringUtils.isNotBlank(actionform.getOrderNumber()[i])) {
							action.setOrderNumber(Integer.valueOf(actionform.getOrderNumber()[i]));
						}
						if (StringUtils.isNotBlank(actionform.getDisplayName()[i])) {
							action.setDisplayName(actionform.getDisplayName()[i]);
						}
						if (StringUtils.isNotBlank(actionform.getHelpURL()[i])) {
							action.setHelpURL(actionform.getHelpURL()[i]);
						}
						if (module.getActions() != null) {
							module.getActions().add(action);
						} else {
							final Set actionsSet = new HashSet();
							actionsSet.add(action);
							module.setActions(actionsSet);
						}
					}
				}
				this.genericCommonsService.updateModule(module);
				req.setAttribute("MESSAGE", "Action created or updated successfully...!");
				target = "success";
			}

		} catch (final Exception c) {
			LOG.error("Error occurred while creating or updating Action",c);
			req.setAttribute("MESSAGE", "Could not process the request due to an internal server error.");
			throw new EGOVRuntimeException("Error occurred while creating or updating Action");
		}

		return mapping.findForward(target);
	}

	/**
	 * Checks whether the given actionId exists in the checked items. If exists returns true, else returns false.
	 * @param isEnabled
	 * @param actionId
	 * @return
	 */
	private boolean checkIfActionIdExists(final String[] isEnabled, final String actionId) {
		boolean idExists = false;
		Integer intActionId = null;
		if ((isEnabled != null) && (actionId != null)) {
			
			/*
			 * If a new action is added, then the value set for isEnabled checkbox is "row1","row2", etc., 
			 * where 1 stands for first row, 2 for second row and so on. If the action already exists then action id is 
			 * set as the value for isEnabled checkbox.
			 */
			if (!actionId.contains("row") && EgovUtils.hasNumber(actionId)) {
				intActionId = new Integer(actionId);
			}

			for (int j = 0; j < isEnabled.length; j++) {
				if (StringUtils.isNotBlank(isEnabled[j])) {
					int intEnabledVal = 0;
					if (!isEnabled[j].contains("row") && EgovUtils.hasNumber(isEnabled[j])) {
						intEnabledVal = new Integer(isEnabled[j]).intValue();
					}
					if ((intActionId != null) && (intEnabledVal == intActionId.intValue())) {
						idExists = true;
					} else {
						if (isEnabled[j].equalsIgnoreCase(actionId)) {
							idExists = true;
						}
					}
				}
			}
		}
		return idExists;
	}
}
