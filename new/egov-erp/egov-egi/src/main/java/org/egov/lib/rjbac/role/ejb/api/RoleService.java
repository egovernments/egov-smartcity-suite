/*
 * @(#)RoleService.java 3.0, 16 Jun, 2013 9:39:13 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.rjbac.role.ejb.api;

import java.util.List;

import org.egov.exceptions.DuplicateElementException;
import org.egov.lib.rjbac.role.Role;
import org.egov.lib.rjbac.user.User;

public interface RoleService {

	/**
	 * Creates a new Role in the System.
	 * @param role the role
	 * @throws DuplicateElementException the duplicate element exception
	 */
	void createRole(Role role) throws DuplicateElementException;

	/**
	 * Update role.
	 * @param role the role
	 */
	void updateRole(Role role);

	/**
	 * Removes the role.
	 * @param role the role
	 */
	void removeRole(Role role);

	/**
	 * Returns the role with the given id.
	 * @param roleId the role id
	 * @return Role
	 */
	Role getRole(Integer roleId);

	/**
	 * Gets the List of all the roles in the System.
	 * @return Set of all roles in the System
	 */
	List<Role> getAllRoles();

	/**
	 * Gets the List of all the top level roles in the System.
	 * @return Set of all top level roles in the System
	 */
	List<Role> getAllTopLevelRoles();

	/**
	 * Gets the role by role name.
	 * @param roleName the role name
	 * @return the role by role name
	 */
	Role getRoleByRoleName(String roleName);
}
