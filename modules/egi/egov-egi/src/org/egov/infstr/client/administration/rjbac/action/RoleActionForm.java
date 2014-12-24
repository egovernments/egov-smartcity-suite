/*
 * @(#)RoleActionForm.java 3.0, 18 Jun, 2013 3:43:55 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.client.administration.rjbac.action;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

public class RoleActionForm extends ActionForm {
	
	private static final long serialVersionUID = 1L;
	private String roleId;
	private String roleName;
	private String[] selectedActions;

	/**
	 * @return Returns the roleId.
	 */
	public String getRoleId() {
		return this.roleId;
	}

	/**
	 * @param roleId The roleId to set.
	 */
	public void setRoleId(final String roleId) {
		this.roleId = roleId;
	}

	/**
	 * @return Returns the roleName.
	 */
	public String getRoleName() {
		return this.roleName;
	}

	/**
	 * @param roleName The roleName to set.
	 */
	public void setRoleName(final String roleName) {
		this.roleName = roleName;
	}

	/**
	 * @return Returns the selectedActions.
	 */
	public String[] getSelectedActions() {
		return this.selectedActions;
	}

	/**
	 * @param selectedActions The selectedActions to set.
	 */
	public void setSelectedActions(final String[] selectedActions) {
		this.selectedActions = selectedActions;
	}

	@Override
	public void reset(final ActionMapping mapping, final HttpServletRequest req) {
		this.roleId = "";
		this.roleName = "";
		this.selectedActions = new String[] {};
	}

}
