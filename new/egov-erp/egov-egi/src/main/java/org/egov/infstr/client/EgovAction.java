/*
 * @(#)EgovAction.java 3.0, 18 Jun, 2013 12:32:46 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.client;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.egov.infstr.client.delegate.UserRoleDelegate;

/**
 * A Base class for our Actions.
 */
public class EgovAction extends Action {
	private final UserRoleDelegate userRoleDelegate = UserRoleDelegate.getInstance();

	protected void postGlobalError(final String errorKey, final HttpServletRequest request) {
		this.postGlobalError(errorKey, null, request);
	}

	protected void postGlobalError(final String errorKey, final Object arg, final HttpServletRequest request) {
		final ActionMessage error = (arg == null ? new ActionMessage(errorKey) : new ActionMessage(errorKey, arg));
		final ActionMessages errors = new ActionMessages();
		errors.add(ActionMessages.GLOBAL_MESSAGE, error);
		this.saveErrors(request, errors);
	}

	protected void postGlobalMessage(final String messageKey, final HttpServletRequest request) {
		this.postGlobalMessage(messageKey, null, request);
	}

	protected void postGlobalMessage(final String messageKey, final Object arg, final HttpServletRequest request) {
		final ActionMessage message = (arg == null ? new ActionMessage(messageKey) : new ActionMessage(messageKey, arg));
		final ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE, message);
		this.saveMessages(request, messages);
	}

	public void setup(final HttpServletRequest req) throws Exception {
		req.setAttribute("departmentList", this.userRoleDelegate.getAlldepartments());
	}

}
