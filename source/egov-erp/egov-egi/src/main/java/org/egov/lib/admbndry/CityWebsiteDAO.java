/*
 * @(#)CityWebsiteDAO.java 3.0, 16 Jun, 2013 3:32:45 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.admbndry;

import org.egov.exceptions.DuplicateElementException;
import org.egov.exceptions.EGOVRuntimeException;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.NoSuchElementException;

public class CityWebsiteDAO {

	private static final Logger LOG = LoggerFactory.getLogger(CityWebsiteDAO.class);

	private SessionFactory sessionFactory;

	public CityWebsiteDAO(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private Session getSession() {
		return this.sessionFactory.getCurrentSession();
	}

	public void create(final CityWebsite cityWebsite) throws DuplicateElementException {
		try {
			getSession().save(cityWebsite);
		} catch (final HibernateException e) {
			LOG.error("Exception occurred in create", e);
			throw new EGOVRuntimeException("Exception occurred in create", e);
		}
	}

	public List<CityWebsite> getCityWebsite(final Integer bndryid) {
		try {
			final Query qry = getSession().createQuery("from CityWebsiteImpl CW where bndryid=:bndryid");
			qry.setInteger("bndryid", bndryid);
			return qry.list();
		} catch (final HibernateException e) {
			LOG.error("Exception occurred in getCityWebsite", e);
			throw new EGOVRuntimeException("Exception occurred in getCityWebsite", e);
		}
	}

	public void remove(final CityWebsite cityWebsite) throws NoSuchElementException {
		try {
			getSession().delete(cityWebsite);
		} catch (final HibernateException e) {
			LOG.error("Exception occurred in remove", e);
			throw new EGOVRuntimeException("Exception occurred in remove", e);
		}

	}

	public CityWebsite getCityWebSiteByURL(final String cityBaseURL) {
		try {
			final Query qry = getSession().createQuery("from CityWebsiteImpl CW where CW.cityBaseURL=:cityBaseURL");
			qry.setString("cityBaseURL", cityBaseURL);
			return (CityWebsite) qry.uniqueResult();
		} catch (final HibernateException e) {
			LOG.error("Exception occurred in getCityWebSiteByURL", e);
			throw new EGOVRuntimeException("Exception occurred in getCityWebSiteByURL", e);
		}
	}

}
