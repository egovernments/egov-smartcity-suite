/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency, 
 *     accountability and the service delivery of the government  organizations.
 *  
 *      Copyright (C) <2015>  eGovernments Foundation
 *  
 *      The updated version of eGov suite of products as by eGovernments Foundation 
 *      is available at http://www.egovernments.org
 *  
 *      This program is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      any later version.
 *  
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *  
 *      You should have received a copy of the GNU General Public License
 *      along with this program. If not, see http://www.gnu.org/licenses/ or 
 *      http://www.gnu.org/licenses/gpl.html .
 *  
 *      In addition to the terms of the GPL license to be adhered to in using this
 *      program, the following additional terms are to be complied with:
 *  
 *  	1) All versions of this program, verbatim or modified must carry this 
 *  	   Legal Notice.
 *  
 *  	2) Any misrepresentation of the origin of the material is prohibited. It 
 *  	   is required that all modified versions of this material be marked in 
 *  	   reasonable ways as different from the original version.
 *  
 *  	3) This license does not grant any rights to any user of the program 
 *  	   with regards to rights under trademark law for use of the trade names 
 *  	   or trademarks of eGovernments Foundation.
 *  
 *    In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.tradelicense.citizen.newapp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infra.admin.master.entity.User;
import org.egov.lib.rjbac.user.dao.UserDAO;
import org.egov.license.utils.Constants;
import org.egov.tradelicense.newtradelicense.web.NewTradeLicenseAction;
import org.egov.web.annotation.ValidationErrorPage;

@ParentPackage("egov")
public class NewCitizenTradeLicenseAction extends NewTradeLicenseAction
		implements ServletRequestAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HttpSession session = null;
	private HttpServletRequest request;
	private Integer userId;

	@Override
	@SkipValidation
	public void prepareNewForm() {
		super.prepareNewForm();
		setUserDetails();
	}

	@Override
	@ValidationErrorPage(Constants.NEW)
	public String create() {
		setUserDetails();
		return super.create();
	}

	@Override
	public void setServletRequest(HttpServletRequest arg0) {
		this.request = arg0;
	}

	private void setUserDetails() {
		session = request.getSession();
		String userName = (String) session
				.getAttribute("com.egov.user.LoginUserName");
		final User user;
		if (userName != null) {
			user = new UserDAO().getUserByUserName(userName);
		} else {
			user = new UserDAO().getUserByUserName(Constants.CITIZENUSER);
			session.setAttribute("com.egov.user.LoginUserName",
					user.getUserName());
		}
		userId = user.getId();
		EGOVThreadLocals.setUserId(userId.toString());
	}
}
