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
package org.egov.infstr.client.administration.rjbac.user;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.admin.master.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Set;

public class BeforeUserJurisdictionAction extends Action {

	private static final Logger logger = LoggerFactory.getLogger(BeforeUserJurisdictionAction.class);
	private final UserService userService = new UserService();

	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws Exception {
		String target = "";
		try {
			String userIdStr = "";
			if (req.getParameter("userid") != null) {
				userIdStr = req.getParameter("userid");
				//final User user = this.userService.getUserByID(Long.valueOf(userIdStr));
				//final Set jurObj1 = this.userService.getAllJurisdictionsForUser(Long.valueOf(userIdStr));
				/*if (jurObj1 != null) {
					req.setAttribute("jurObj1", jurObj1);
				}
				req.setAttribute("user", user);*/
			}
			target = "success";
		} catch (final EGOVRuntimeException rexp) {
			target = "failure";
			logger.error("Error occurred in user jurisdication setup", rexp);
			throw new EGOVRuntimeException("Error occurred in user jurisdication setup", rexp);
		} catch (final Exception c) {
			logger.error("Error occurred in user jurisdication setup", c);
			target = "error";
			req.setAttribute("MESSAGE", "Error Before UserJurisdiction !!");
			throw new EGOVRuntimeException("Error occurred in user jurisdication setup", c);
		}
		return mapping.findForward(target);
	}
}
