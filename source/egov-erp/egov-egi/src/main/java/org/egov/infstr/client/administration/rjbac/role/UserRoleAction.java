/*
 * @(#)UserRoleAction.java 3.0, 18 Jun, 2013 3:12:22 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.client.administration.rjbac.role;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.exceptions.DuplicateElementException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infstr.client.EgovAction;
import org.egov.infstr.client.EgovActionForm;
import org.egov.infstr.client.delegate.UserRoleDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserRoleAction extends EgovAction {

	private static final Logger logger = LoggerFactory.getLogger(UserRoleAction.class);
	private final UserRoleDelegate userRoleDelegate = UserRoleDelegate.getInstance();

	/**
	 * This method creates,updates and deletes a role depending upon the client's request
	 * @param ActionMapping mapping
	 * @param ActionForm form
	 * @param HttpServletRequest req
	 * @param HttpServletResponse res
	 * @return ActionForward
	 **/
	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws Exception {
		String target = "";
		String message = "";
		Role role = null;
		final RoleForm roleForm = (RoleForm) form;
		final javax.servlet.http.HttpSession session = req.getSession();

		final String str = (req.getParameter("bool") == null || req.getParameter("bool").equals("null") ? "" : req.getParameter("bool"));

		if (str.equals("")) {
			try {

				role = new Role();
				final Integer roleid = new Integer(req.getParameter("roleid"));
				role = this.userRoleDelegate.getRole(roleid);
				session.setAttribute("ROLE", role);
				roleForm.setRoleDesc(role.getDescription());
				roleForm.setRoleName(role.getName());
				// This is commented while rewriting role master screen
                                // code must be corrected while rewriting this screen
				//req.setAttribute("editroleid", (role.getParent() != null) ? role.getParent().getId() : null);
				target = "toViewRole";
				roleForm.reset(mapping, req);
			} catch (final EGOVRuntimeException e) {
				logger.error("EGOVRuntimeException Encountered  IN UserRoleAction :::::::::::!!!" + e.getMessage());
				target = "error";
				req.setAttribute("MESSAGE", "Error  View Role Page !!");
				roleForm.reset(mapping, req);
				throw new EGOVRuntimeException("Exception occured -----> " + e.getMessage());

			} catch (final Exception c) {
				logger.error("Exception in UserRoleAction" + c.getMessage());
				target = "error";
				req.setAttribute("MESSAGE", "Error  View Role Page !!");
				throw new EGOVRuntimeException("Exception occured -----> " + c.getMessage());

			}
		} else if (str.equals("CREATE")) {
			try {
				role = new Role();
				roleForm.populate(role, EgovActionForm.TO_OBJECT);
				final String dName = req.getParameter("departmentId");
				this.userRoleDelegate.getDepartmentbyName(dName);
				this.userRoleDelegate.createRole(role, roleForm.getRoleId());
				target = "success";
				message = "Role has been created successfully";
				req.setAttribute("MESSAGE", message);
				roleForm.reset(mapping, req);
			} catch (final DuplicateElementException dupexp) {
				target = "success";
				message = "Duplicate Role exists";
				req.setAttribute("MESSAGE", message);
				roleForm.reset(mapping, req);
			} catch (final EGOVRuntimeException e) {
				logger.error("EGOVRuntimeException Encountered  IN UserRoleAction :::::::::::!!!" + e.getMessage());
				target = "error";
				req.setAttribute("MESSAGE", "Error Creating Role !!");
				throw new EGOVRuntimeException("Exception occured -----> " + e.getMessage());

			} catch (final Exception c) {
				logger.error("Exception in UserRoleAction" + c.getMessage());
				target = "error";
				req.setAttribute("MESSAGE", "Error Creating Role !!");
				throw new EGOVRuntimeException("Exception occured -----> " + c.getMessage());

			}
		} else if (str.equals("DELETE")) {
			target = "toconfDelete";
		}

		/**
		 * Forwards the request to the acknowledgement page after deletion of the Role
		 */
		else if (str.equals("ConfirmDelete")) {
			try {
				logger.info("In ConfirmDelete**********");
				final Integer roleid = new Integer(req.getParameter("roleid"));
				role = this.userRoleDelegate.getRole(roleid);
				this.userRoleDelegate.removeRole(roleid);
				target = "success";
				message = "Role has been Deleted Successfully";
				req.setAttribute("MESSAGE", message);
			}

			catch (final EGOVRuntimeException e) {
				logger.error("EGOVRuntimeException Encountered  IN UserRoleAction :::::::::::!!!" + e.getMessage());
				target = "error";
				req.setAttribute("MESSAGE", "Error Deleting Role !!");
				throw new EGOVRuntimeException("Exception occured -----> " + e.getMessage());

			} catch (final Exception c) {
				logger.error("Exception in UserRoleAction" + c.getMessage());
				target = "error";
				req.setAttribute("MESSAGE", "Error Deleting Role !!");
				throw new EGOVRuntimeException("Exception occured -----> " + c.getMessage());

			}
		}
		/**
		 * Forwards the request to the updateRole page
		 */
		else if (str.equals("EDIT")) {
			try {

				role = new Role();
				final Integer roleid = new Integer(req.getParameter("roleid"));
				role = this.userRoleDelegate.getRole(roleid);
				session.setAttribute("ROLE", role);
				roleForm.setRoleDesc(role.getDescription());
				roleForm.setRoleName(role.getName());
				// This is commented while rewriting role master screen
                                // code must be corrected while rewriting this screen
				//req.setAttribute("editroleid", (role.getParent() != null) ? role.getParent().getId() : null);
				target = "toUpdateRole";
			}

			catch (final EGOVRuntimeException e) {
				logger.error("EGOVRuntimeException Encountered  IN UserRoleAction :::::::::::!!!" + e.getMessage());
				target = "error";
				req.setAttribute("MESSAGE", "Error Updating Role !!");
				throw new EGOVRuntimeException("Exception occured -----> " + e.getMessage());

			} catch (final Exception c) {
				logger.error("Exception in UserRoleAction" + c.getMessage());
				target = "error";
				req.setAttribute("MESSAGE", "Error Updating Role !!");
				throw new EGOVRuntimeException("Exception occured -----> " + c.getMessage());

			}
		} else if (str.equals("UPDATE")) {

			try {
				role = (Role) session.getAttribute("ROLE");
				roleForm.populate(role, EgovActionForm.TO_OBJECT);
				this.userRoleDelegate.updateRole(role, roleForm.getRoleId());
				target = "success";
				message = "Role has been Updated Successfully!!";
				req.setAttribute("MESSAGE", message);
				roleForm.reset(mapping, req);
			}

			catch (final EGOVRuntimeException e) {
				logger.error("EGOVRuntimeException Encountered  IN UserRoleAction :::::::::::!!!" + e.getMessage());
				target = "error";
				req.setAttribute("MESSAGE", "Error Updating Role !!");
				roleForm.reset(mapping, req);
				throw new EGOVRuntimeException("Exception occured -----> " + e.getMessage());

			} catch (final Exception c) {
				logger.error("Exception in UserRoleAction" + c.getMessage());
				target = "error";
				req.setAttribute("MESSAGE", "Error Updating Role !!");
				throw new EGOVRuntimeException("Exception occured -----> " + c.getMessage());

			}

		}
		return mapping.findForward(target);
	}
}
