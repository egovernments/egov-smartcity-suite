/*
 * @(#)RoleImpl.java 3.0, 16 Jun, 2013 9:37:14 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.rjbac.role;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.egov.lib.rrbac.model.Action;

public class RoleImpl implements Role, Comparable<Role> {

	private Integer id;
	private String roleName;
	private String roleDesc;
	private Integer createdby;
	private Date createddate;
	private String roleNameLocal;
	private String roleDescLocal;
	private Role parent;
	private Set actions = new HashSet();

	/**
	 * Gets the id.
	 * @return Returns the id.
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Sets the id.
	 * @param id The id to set.
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Gets the parent.
	 * @return Returns the reportsTo.
	 */
	public Role getParent() {
		return parent;
	}

	/**
	 * Sets the parent.
	 * @param parent the new parent
	 */
	public void setParent(Role parent) {
		this.parent = parent;
	}

	/**
	 * Gets the role desc.
	 * @return Returns the roleDesc.
	 */
	public String getRoleDesc() {
		return roleDesc;
	}

	/**
	 * Sets the role desc.
	 * @param roleDesc The roleDesc to set.
	 */
	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}

	/**
	 * Gets the role desc local.
	 * @return Returns the roleDescLocal.
	 */
	public String getRoleDescLocal() {
		return roleDescLocal;
	}

	/**
	 * Sets the role desc local.
	 * @param roleDescLocal The roleDescLocal to set.
	 */
	public void setRoleDescLocal(String roleDescLocal) {
		this.roleDescLocal = roleDescLocal;
	}

	/**
	 * Gets the role name.
	 * @return Returns the roleName.
	 */
	public String getRoleName() {
		return roleName;
	}

	/**
	 * Sets the role name.
	 * @param roleName The roleName to set.
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	/**
	 * Gets the role name local.
	 * @return Returns the roleNameLocal.
	 */
	public String getRoleNameLocal() {
		return roleNameLocal;
	}

	/**
	 * Sets the role name local.
	 * @param roleNameLocal The roleNameLocal to set.
	 */
	public void setRoleNameLocal(String roleNameLocal) {
		this.roleNameLocal = roleNameLocal;
	}

	/**
	 * Gets the update time.
	 * @return Returns the createddate.
	 */
	public Date getCreateddate() {
		return createddate;
	}
	/**
	 * Sets the update time.
	 * @param updateTime The createddate to set.
	 */
	public void setCreateddate(Date createddate) {
		this.createddate = createddate;
	}

	/**
	 * Gets the update user id.
	 * @return Returns the createdby.
	 */
	public Integer getCreatedby() {
		return createdby;
	}
	/**
	 * Sets the update user id.
	 * @param updateUserid The createdby to set.
	 */
	public void setCreatedby(Integer createdby) {
		this.createdby = createdby;
	}

	/*
	 * (non-Javadoc)
	 * @see org.egov.lib.rjbac.role.Role#getActions()
	 */
	public Set getActions() {
		return actions;
	}

	/*
	 * (non-Javadoc)
	 * @see org.egov.lib.rjbac.role.Role#setActions(java.util.Set)
	 */
	public void setActions(Set actions) {
		this.actions = actions;
	}

	/*
	 * (non-Javadoc)
	 * @see org.egov.lib.rjbac.role.Role#addAction(org.egov.lib.rrbac.model.Action)
	 */
	public void addAction(Action action) {
		getActions().add(action);
	}

	/*
	 * (non-Javadoc)
	 * @see org.egov.lib.rjbac.role.Role#removeAction(org.egov.lib.rrbac.model.Action)
	 */
	public void removeAction(Action action) {
		if (getActions().contains(action))
			getActions().remove(action);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {

		if (this == obj)
			return true;

		if (!(obj instanceof RoleImpl))
			return false;

		final Role other = (Role) obj;

		if (this.getRoleName().equals(other.getRoleName())) {
			return true;
		} else
			return false;

	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		int hashCode = 0;
		if (this.getRoleName() != null)
			hashCode = hashCode + this.getRoleName().hashCode();

		return hashCode;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Role role) throws ClassCastException {
		return (role == null || role.getRoleName() == null ) ? -1 : this.roleName.compareTo(role.getRoleName());
	}

}