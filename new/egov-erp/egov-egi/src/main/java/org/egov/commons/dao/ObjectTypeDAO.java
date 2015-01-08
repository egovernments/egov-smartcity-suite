/*
 * @(#)ObjectTypeDAO.java 3.0, 14 Jun, 2013 10:56:09 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons.dao;

import org.egov.commons.ObjectType;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;

public class ObjectTypeDAO {

	private Session getSession() {
		return HibernateUtil.getCurrentSession();
	}

	public void createObjectType(final ObjectType objectType) {
		try {
			getSession().save(objectType);
		} catch (final Exception e) {
			throw new EGOVRuntimeException("Error occurred while creating Object Type",e);
		}
	}

	public void updateObjectType(final ObjectType objectType) {
		try {
			getSession().saveOrUpdate(objectType);
		} catch (final Exception e) {
			throw new EGOVRuntimeException("Error occurred while updating Object Type",e);
		}
	}

	public void removeObjectType(final ObjectType objectType) {
		try {
			getSession().delete(objectType);
		} catch (final Exception e) {
			throw new EGOVRuntimeException("Error occurred while deleting Object Type",e);
		}
	}

	public ObjectType getObjectType(final int objType) {
		try {
			return (ObjectType) getSession().get(ObjectType.class, new Integer(objType));
		} catch (final Exception e) {
			throw new EGOVRuntimeException("Error occurred while getting Object Type",e);
		}
	}

	public ObjectType getObjectType(final String objTypeName) {
		final Query qry = getSession().createQuery("from ObjectType D where D.type =:objTypeName ");
		qry.setString("objTypeName", objTypeName);
		return (ObjectType) qry.uniqueResult();
	}

}
