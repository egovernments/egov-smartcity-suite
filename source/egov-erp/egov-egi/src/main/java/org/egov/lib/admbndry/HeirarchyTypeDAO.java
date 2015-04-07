/*
 * @(#)HeirarchyTypeDAO.java 3.0, 16 Jun, 2013 3:39:01 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.admbndry;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.egov.exceptions.DuplicateElementException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.exceptions.NoSuchObjectException;
import org.egov.exceptions.TooManyValuesException;
import org.egov.infra.admin.master.entity.HierarchyType;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

public class HeirarchyTypeDAO {

	private static final Logger LOGGER = LoggerFactory.getLogger(HeirarchyTypeDAO.class);
	private SessionFactory sessionFactory;

	public HeirarchyTypeDAO(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	/**
	 * @see org.egov.lib.admbndry.ejb.api.HeirarchyTypeManager#create(org.egov.infra.admin.master.entity.HierarchyType)
	 */
	public void create(final HierarchyType heirarchyType) throws DuplicateElementException {
		try {
			getSession().save(heirarchyType);
		} catch (final HibernateException e) {
			LOGGER.error("Error occurred in create", e);
			throw new EGOVRuntimeException("Error occurred in create", e);
		}
	}

	/**
	 * @see org.egov.lib.admbndry.ejb.api.HeirarchyTypeManager#update(org.egov.infra.admin.master.entity.HierarchyType)
	 */
	public void update(final HierarchyType heirarchyType) throws NoSuchElementException {
		try {
			getSession().saveOrUpdate(heirarchyType);
		} catch (final HibernateException e) {
			LOGGER.error("Error occurred in update", e);
			throw new EGOVRuntimeException("Error occurred in update", e);
		}

	}

	/**
	 * @see org.egov.lib.admbndry.ejb.api.HeirarchyTypeManager#remove(org.egov.infra.admin.master.entity.HierarchyType)
	 */
	public void remove(final HierarchyType heirarchyType) throws NoSuchElementException {
		try {
			getSession().delete(heirarchyType);
		} catch (final HibernateException e) {
			LOGGER.error("Error occurred in remove", e);
			throw new EGOVRuntimeException("Error occurred in remove", e);
		}

	}

	/**
	 * @see org.egov.lib.admbndry.ejb.api.HeirarchyTypeManager#getHeirarchyTypeByID(int)
	 */
	public HierarchyType getHeirarchyTypeByID(final int heirarchyTypeId) {
		try {
			return (HierarchyType) getSession().load(HierarchyType.class, heirarchyTypeId);
		} catch (final HibernateException e) {
			LOGGER.error("Error occurred in getHeirarchyTypeByID", e);
			throw new EGOVRuntimeException("Error occurred in getHeirarchyTypeByID", e);
		}
	}

	/**
	 * @see org.egov.lib.admbndry.ejb.api.HeirarchyTypeManager#getAllHeirarchyTypes()
	 */
	public Set<HierarchyType> getAllHeirarchyTypes() {
		try {
			return new LinkedHashSet(getSession().createQuery("from HeirarchyTypeImpl HT order by code asc").list());
		} catch (final HibernateException e) {
			LOGGER.error("Error occurred in getAllHeirarchyTypes", e);
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException("Error occurred in getAllHeirarchyTypes", e);
		}
	}

	public HierarchyType getHierarchyTypeByName(final String name) throws NoSuchObjectException, TooManyValuesException {

		if (name == null) {
			throw new EGOVRuntimeException("heirarchyType.name.null");
		}

		try {
			HierarchyType heirarchyType = null;
			final Query qry = getSession().createQuery("from HeirarchyTypeImpl HT where trim(upper(HT.name)) =:name ");
			final String ucaseName = name.toUpperCase();
			qry.setString("name", ucaseName);
			final List<HierarchyType> heirarchyTypeList = qry.list();
			if (heirarchyTypeList.size() == 0) {
				throw new NoSuchObjectException("heirarchyType.object.notFound");
			}
			if (heirarchyTypeList.size() > 1) {
				throw new TooManyValuesException("heirarchyType.multiple.objectsFound");
			}
			if (heirarchyTypeList.size() == 1) {
				heirarchyType = heirarchyTypeList.get(0);
			}
			return heirarchyType;

		} catch (final Exception e) {
			LOGGER.error("Error occurred in getHierarchyTypeByName", e);
			throw new EGOVRuntimeException("system.error", e);
		}
	}

}
