/*
 * @(#)JurisdictionDAO.java 3.0, 16 Jun, 2013 10:13:22 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.rjbac.jurisdiction.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.rjbac.jurisdiction.Jurisdiction;
import org.egov.lib.rjbac.jurisdiction.JurisdictionValues;
import org.hibernate.HibernateException;

public class JurisdictionDAO {

	public static final Logger LOGGER = LoggerFactory.getLogger(JurisdictionDAO.class);

	public void removeJurisdiction(final Jurisdiction jur) {
		try {
			HibernateUtil.getCurrentSession().delete(jur);
		} catch (final HibernateException e) {
			LOGGER.error("Exception occurred in removeJurisdiction", e);
			throw new EGOVRuntimeException("Exception occurred in removeJurisdiction", e);
		}
	}

	public void updateJurisdiction(final Jurisdiction jur) {
		try {
			HibernateUtil.getCurrentSession().saveOrUpdate(jur);
		} catch (final HibernateException e) {
			LOGGER.error("Exception occurred in updateJurisdiction", e);
			throw new EGOVRuntimeException("Exception occurred in updateJurisdiction", e);
		}
	}

	public void deleteJurisdictionValues(final JurisdictionValues jur) {
		try {
			HibernateUtil.getCurrentSession().delete(jur);
		} catch (final HibernateException e) {
			LOGGER.error("Exception occurred in deleteJurisdictionValues", e);
			throw new EGOVRuntimeException("Exception occurred in deleteJurisdictionValues", e);
		}

	}

	public void deleteJurisdiction(final Jurisdiction jur) {
		try {
			HibernateUtil.getCurrentSession().delete(jur);
		} catch (final HibernateException e) {
			LOGGER.error("Exception occurred in deleteJurisdiction", e);
			throw new EGOVRuntimeException("Exception occurred in deleteJurisdiction", e);
		}

	}

}
