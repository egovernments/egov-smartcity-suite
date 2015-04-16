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

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorActionForm;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.repository.UserRepository;
import org.egov.infstr.security.utils.CryptoHelper;
import org.egov.infstr.security.utils.ValidatorUtils;

public class ChangePasswordForm extends ValidatorActionForm {

	private static final long serialVersionUID = 1L;
	private String oldPwd;
	private String pwd;
	private String pwdReminder;

	public String getOldPwd() {
		return this.oldPwd;
	}

	public void setOldPwd(final String oldPwd) {
		this.oldPwd = oldPwd;
	}

	public String getPwd() {
		return this.pwd;
	}

	public void setPwd(final String pwd) {
		this.pwd = pwd;
	}

	public String getPwdReminder() {
		return this.pwdReminder;
	}

	public void setPwdReminder(final String pwdReminder) {
		this.pwdReminder = pwdReminder;
	}

	/**
	 * checks for null values in ActionForm fields and also business validations like old password incorrect,password mismatch etc.,
	 */
	@Override
	public ActionErrors validate(final ActionMapping mapping, final HttpServletRequest request) {
		final ActionErrors errors = new ActionErrors();
		if (StringUtils.isBlank(this.getOldPwd())) {
			errors.add("message", new ActionMessage("oldPwd.null"));
		}
		if (StringUtils.isBlank(this.getPwd())) {
			errors.add("message", new ActionMessage("pwd.null"));
		}
		if (StringUtils.isBlank(this.getPwdReminder())) {
			errors.add("message", new ActionMessage("pwdReminder.null"));
		}

		if (StringUtils.isNotBlank(this.getPwd()) && StringUtils.isNotBlank(this.getPwdReminder())) {
			if (!this.getPwd().trim().equals(this.getPwdReminder().trim())) {
				errors.add("message", new ActionMessage("pwd.mismatch"));
			}
		}
		if (errors.isEmpty()) {
			if (!ValidatorUtils.isValidPassword(this.getPwd(), true)) {
				errors.add("message", new ActionMessage("new.pwd.invalid"));
			} else if (StringUtils.isNotBlank(this.getOldPwd())) {
				/*final UserRepository userDao = new UserRepository();
				final User user = userDao.getUserByUserName((String) request.getSession().getAttribute("LAST_USER_NAME_PWD_CHANGE"));
				final String decryptPwd = CryptoHelper.decrypt(user.getPassword().trim());
				if (!this.getOldPwd().trim().equals(decryptPwd)) {
					errors.add("message", new ActionMessage("old.pwd.Incorrect"));
				} else if (this.getPwd().trim().equals(decryptPwd)) {
					errors.add("message", new ActionMessage("pwd.old.new.same"));
				}*/
			}
		}
		return errors;
	}
}
