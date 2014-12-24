/*
 * @(#)RoleDAO.java 3.0, 16 Jun, 2013 10:02:12 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.rjbac.role.dao;

import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.egov.exceptions.DuplicateElementException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.rjbac.role.Role;
import org.egov.lib.rjbac.role.RoleImpl;
import org.hibernate.HibernateException;
import org.hibernate.Query;

public class RoleDAO {

	public static final Logger LOGGER = LoggerFactory.getLogger(RoleDAO.class);

	/**
	 * Creates the role.
	 * @param role the role
	 * @throws DuplicateElementException the duplicate element exception
	 */
	public void createRole(final Role role) throws DuplicateElementException {
		try {
			final Role checkRole = getRoleByRoleName(role.getRoleName());
			if (checkRole != null) {
				throw new DuplicateElementException("The Role by " + role.getRoleName() + " already exists.");
			}

			HibernateUtil.getCurrentSession().save(role);
		} catch (final HibernateException e) {
			LOGGER.error("Exception occurred in createRole", e);
			throw new EGOVRuntimeException("Exception occurred in createRole", e);
		}
	}

	/**
	 * Update role.
	 * @param role the role
	 */
	public void updateRole(final Role role) {
		try {
			HibernateUtil.getCurrentSession().update(role);
		} catch (final HibernateException e) {
			LOGGER.error("Exception occurred in updateRole", e);
			throw new EGOVRuntimeException("Exception occurred in updateRole", e);
		}

	}

	/**
	 * Gets the role by id.
	 * @param roleID the role id
	 * @return the role by id
	 */
	public Role getRoleByID(final Integer roleID) {
		try {
			return (Role) HibernateUtil.getCurrentSession().load(RoleImpl.class, roleID);
		} catch (final HibernateException e) {
			LOGGER.error("Exception occurred in getRoleByID", e);
			throw new EGOVRuntimeException("Exception occurred in getRoleByID", e);
		}
	}

	/**
	 * Gets the all roles.
	 * @return the all roles
	 */
	public List<Role> getAllRoles() {
		try {
			return HibernateUtil.getCurrentSession().createQuery("FROM RoleImpl RI fetch all properties order by ROLE_NAME").list();
		} catch (final HibernateException e) {
			LOGGER.error("Exception occurred in getAllRoles", e);
			throw new EGOVRuntimeException("Exception occurred in getAllRoles", e);
		}
	}

	/**
	 * Gets the all top level roles.
	 * @return the all top level roles
	 */
	public List<Role> getAllTopLevelRoles() {
		try {
			return HibernateUtil.getCurrentSession().createQuery("FROM RoleImpl RI where RI.parent = null").list();
		} catch (final HibernateException e) {
			LOGGER.error("Exception occurred in getAllTopLevelRoles", e);
			throw new EGOVRuntimeException("Exception occurred in getAllTopLevelRoles", e);
		}
	}

	/**
	 * Removes the role.
	 * @param role the role
	 * @throws NoSuchElementException the no such element exception
	 */
	public void removeRole(final Role role) throws NoSuchElementException {
		try {
			HibernateUtil.getCurrentSession().delete(role);
		} catch (final HibernateException e) {
			LOGGER.error("Exception occurred in removeRole", e);
			throw new EGOVRuntimeException("Exception occurred in removeRole", e);
		}

	}

	/**
	 * Gets the role by role name.
	 * @param roleName the role name
	 * @return the role by role name
	 */
	public Role getRoleByRoleName(final String roleName) {
		try {
			final Query qry = HibernateUtil.getCurrentSession().createQuery("FROM RoleImpl RI WHERE RI.roleName=:roleName");
			qry.setString("roleName", roleName);
			return (Role) qry.uniqueResult();
		} catch (final HibernateException e) {
			LOGGER.error("Exception occurred in getRoleByRoleName", e);
			throw new EGOVRuntimeException("Exception occurred in getRoleByRoleName", e);
		}

	}

}
