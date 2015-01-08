/*
 * @(#)RoleForm.java 3.0, 18 Jun, 2013 3:05:16 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.client.administration.rjbac.role;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.egov.infstr.client.EgovActionForm;

public class RoleForm extends EgovActionForm implements Serializable {

	private static final long serialVersionUID = 1L;
	private String roleName = "";
	private String roleDesc = "";
	private Integer roleId = null;
	private String roleNameLocal = "";
	private String roleDescLocal = "";

	/**
	 * @return Returns the roleDescLocal.
	 */
	public String getRoleDescLocal() {
		return this.roleDescLocal;
	}

	/**
	 * @param roleDescLocal The roleDescLocal to set.
	 */
	public void setRoleDescLocal(final String roleDescLocal) {
		this.roleDescLocal = roleDescLocal;
	}

	/**
	 * @return Returns the roleNameLocal.
	 */
	public String getRoleNameLocal() {
		return this.roleNameLocal;
	}

	/**
	 * @param roleNameLocal The roleNameLocal to set.
	 */
	public void setRoleNameLocal(final String roleNameLocal) {
		this.roleNameLocal = roleNameLocal;
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
	 * @return Returns the roleDesc.
	 */
	public String getRoleDesc() {
		return this.roleDesc;
	}

	/**
	 * @param roleDesc The roleDesc to set.
	 */
	public void setRoleDesc(final String roleDesc) {
		this.roleDesc = roleDesc;
	}

	/**
	 * @return Returns the roleId.
	 */
	public Integer getRoleId() {
		return this.roleId;
	}

	/**
	 * @param roleId The roleId to set.
	 */
	public void setRoleId(final Integer roleId) {
		this.roleId = roleId;
	}

	/**
	 * resets properties of this form
	 */
	@Override
	public void reset(final ActionMapping mapping, final HttpServletRequest req) {
		this.roleName = "";
		this.roleDesc = "";
		this.roleId = null;
		this.roleDescLocal = "";
		this.roleNameLocal = "";

	}

}
