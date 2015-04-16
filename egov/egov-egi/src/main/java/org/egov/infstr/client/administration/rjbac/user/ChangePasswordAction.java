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

import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infstr.security.utils.ValidatorUtils;

public class ChangePasswordAction extends Action {

	private UserService userService;

	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws Exception {
		// Check its an Ajax request
		if (Boolean.valueOf(req.getParameter("AJAX"))) {
			res.setContentType("text/plain");
			final PrintWriter out = res.getWriter();
			out.println(String.valueOf(ValidatorUtils.isValidPassword(req.getParameter("pwd"), true)));
			out.flush();
			return null;
		} else {
			final ChangePasswordForm changePasswordForm = (ChangePasswordForm) form;
			final User user = this.getUserDao().getUserByUsername((String)req.getSession().getAttribute("LAST_USER_NAME_PWD_CHANGE"));
			/*if (user.getUserSignature() != null) {
				final byte [] currentSignature = CryptoHelper.decrypt(user.getUserSignature().getSignature(),CryptoHelper.decrypt(user.getPassword()));
				user.getUserSignature().setSignature(CryptoHelper.encrypt(currentSignature,changePasswordForm.getPwd().trim()));
			}*/
			user.setPassword(changePasswordForm.getPwd().trim());
			//user.setPwdReminder(changePasswordForm.getPwdReminder().trim());
			user.setPwdExpiryDate(new Date());
			//this.getUserDao().createOrUpdateUserWithPwdEncryption(user);
			req.setAttribute("MESSAGE", "Password successfully modified.");				
			return mapping.findForward("success");
		}

	}

	public UserService getUserDao() {
		return this.userService;
	}

	public void setUserService(final UserService userService) {
		this.userService = userService;
	}

}
