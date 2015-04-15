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
