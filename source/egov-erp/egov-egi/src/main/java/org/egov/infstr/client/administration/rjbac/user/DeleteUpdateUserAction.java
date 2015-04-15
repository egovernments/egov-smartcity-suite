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
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.infstr.client.EgovAction;
import org.egov.infstr.client.delegate.UserDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeleteUpdateUserAction extends EgovAction {

	private static final Logger LOG = LoggerFactory.getLogger(DeleteUpdateUserAction.class);
	private UserDelegate userDelegate;

	/**
	 * This method is used for deletion and updation of a user depending upon the client's request
	 * @param ActionMapping mapping
	 * @param ActionForm form
	 * @param HttpServletRequest req
	 * @param HttpServletResponse res
	 * @return ActionForward
	 */
	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws Exception {
		String target = "";
		/*User user = null;
		final javax.servlet.http.HttpSession session = req.getSession();
		final DeleteUserForm deleteEditUserForm = (DeleteUserForm) form;
		final String str = ((req.getParameter("bool") == null) || req.getParameter("bool").equals("null") ? "" : req.getParameter("bool"));
		*//**
		 * Forwards the request to the view page
		 *//*
		if (str.equals("")) {
			user = new User();
			final Long userid = Long.valueOf(req.getParameter("userid"));
			try {
				user = this.userDelegate.getUser(userid);
				session.setAttribute("USER", user);
				// jurisdiction of the user
				final Set jurlevel = user.getAllJurisdictions();
				final Map jurisdcn = new HashMap();
				for (final Iterator itr = jurlevel.iterator(); itr.hasNext();) {
					final BoundaryType bt = ((Jurisdiction) itr.next()).getJurisdictionLevel();
					final Set bndries = user.getAllJurisdictionsForLevel(bt);
					jurisdcn.put(bt, bndries);
				}
				session.setAttribute("jursidcnMap", jurisdcn);
				target = "toViewUser";
				deleteEditUserForm.reset(mapping, req);
			} catch (final EGOVRuntimeException rexp) {
				target = "error";
				LOG.error("Error occurred while updating User data", rexp);
				deleteEditUserForm.reset(mapping, req);
				throw new EGOVRuntimeException("Error occurred while updating User data", rexp);
			}
		}
		// Forwards the request to the confirm page before deletion
		if (str.equals("DELETE")) {
			target = "toconfDelete";
		}
		// Forwards the request to the acknowledgement page after deletion of the User
		if (str.equals("ConfirmDelete")) {
			this.userDelegate.removeUser(Long.valueOf(req.getParameter("userid")));
			target = "success";
			req.setAttribute("MESSAGE", "User Romoved successfully");
		}
		// Forwards the request to the updateUserpage
		if (str.equals("EDIT")) {
			user = new User();
			final Long userid = Long.valueOf(req.getParameter("userid"));
			try {
				user = this.userDelegate.getUser(userid);
				session.setAttribute("USER", user);
				deleteEditUserForm.setFirstName(user.getFirstName());
				deleteEditUserForm.setMiddleName(user.getMiddleName());
				deleteEditUserForm.setLastName(user.getLastName());
				deleteEditUserForm.setSalutation(user.getSalutation());
				deleteEditUserForm.setUserName(user.getUserName());
				final String decryptPass = CryptoHelper.decrypt(user.getPassword());
				deleteEditUserForm.setPwd(decryptPass);
				//deleteEditUserForm.setPwdReminder(user.getPwdReminder());
				// topBoundary of the user
				//req.setAttribute("topBndryid", user.getTopBoundaryID());
				//req.setAttribute("editdeptid", user.getDepartment().getId());
				// role of the user
				final Set userrole = user.getRoles();
				Role role = null;
				for (final Iterator<Role> itr = userrole.iterator(); itr.hasNext();) {
					role = itr.next();
				}
				req.setAttribute("editroleid", (role.getId() != null) ? role.getId() : null);
				// jurisdiction of the user
				final Set jurlevel = user.getAllJurisdictions();
				final Map jurisdcn = new HashMap();
				for (final Iterator itr = jurlevel.iterator(); itr.hasNext();) {
					final BoundaryType bt = ((Jurisdiction) itr.next()).getJurisdictionLevel();
					final Set bndries = user.getAllJurisdictionsForLevel(bt);
					jurisdcn.put(bt, bndries);
				}
				session.setAttribute("jursidcnMap", jurisdcn);
				session.setAttribute("jurSet", jurlevel);
				target = "toUpdateUser";
			} catch (final EGOVRuntimeException rexp) {
				target = "error";
				LOG.error("Error occurred while updating User data", rexp);
				throw new EGOVRuntimeException("Error occurred while updating User data", rexp);
			}
		}*/
		return mapping.findForward(target);
	}

	public void setUserDelegate(UserDelegate userDelegate) {
		this.userDelegate = userDelegate;
	}
}
