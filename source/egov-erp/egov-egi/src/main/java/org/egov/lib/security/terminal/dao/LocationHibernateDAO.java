/*
 * @(#)LocationHibernateDAO.java 3.0, 14 Jun, 2013 3:17:43 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.security.terminal.dao;

import java.util.ArrayList;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.security.terminal.client.ObjComparator;
import org.egov.lib.security.terminal.model.Location;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

public class LocationHibernateDAO extends GenericHibernateDAO implements LocationDAO {

	private static final Logger LOG = LoggerFactory.getLogger(LocationHibernateDAO.class);

	public LocationHibernateDAO() {
		super(Location.class, null);
	}

	/**
	 * Instantiates a new location hibernate dao.
	 * @param persistentClass the persistent class
	 * @param session the session
	 */
	public LocationHibernateDAO(final Class persistentClass, final Session session) {
		super(persistentClass, session);
	}

	@Override
	public Location getLocationIdByLocationNameAndCounter(final String locationName, final String counterName) {
		final Query qry = HibernateUtil.getCurrentSession().createQuery("select loc1 from Location loc,Location loc1 where loc.id=loc1.locationId and loc.name = :locationName and loc1.name= :counterName");
		qry.setString("locationName", locationName);
		qry.setString("counterName", counterName);
		return (Location) qry.uniqueResult();
	}

	@Override
	public ArrayList<Location> getCountersByLocation(final int locationId) {
		try {
			final Query qry = HibernateUtil.getCurrentSession().createQuery("from Location loc where loc.locationId=:locationId");
			qry.setLong("locationId", locationId);
			final ArrayList<Location> locList = new ArrayList<Location>(qry.list());
			Collections.sort(locList, new ObjComparator());
			return locList;
		} catch (final Exception e) {
			LOG.error("Exception in getting Counters By Location", e);
			throw new EGOVRuntimeException("Exception in getCountersByLocation", e);

		}
	}

	/**
	 * API checks whether user entered ip address name is already presented in database. returns true --> 
	 * If the record is found in the database for user input returns false --> If the record is not 
	 * found in the database for user input
	 * @param ipValue the ip value
	 * @return true, if successful
	 */
	@Override
	public boolean checkIPAddress(final String ipValue) {
		boolean b = false;
		try {
			final Query qry = HibernateUtil.getCurrentSession().createQuery("from LocationIPMap ip where ip.ipAddress=:ipValue");
			qry.setString("ipValue", ipValue);
			if (qry.uniqueResult() != null) {
				b = true;
			}

		} catch (final HibernateException he) {
			LOG.error("Exception occurred while check IP Address", he);
			throw new EGOVRuntimeException("Exception occurred in checkIPAddress", he);
		} catch (final Exception e) {
			LOG.error("Exception occurred while check IP Address", e);
			throw new EGOVRuntimeException("Exception occurred in checkIPAddress", e);
		}
		return b;

	}

	/**
	 * API checks whether user entered counter name is already presented in database. returns true --> 
	 * If the record is found in the database for user input returns false --> 
	 * If the record is not found in the database for user input
	 * @param name the name
	 * @return true, if successful
	 */
	@Override
	public boolean checkCounter(final String name) {
		boolean b = false;
		try {
			final Query qry = HibernateUtil.getCurrentSession().createQuery("from Location loc where loc.name=:name");
			qry.setString("name", name);
			if (qry.uniqueResult() != null) {
				b = true;
			}

		} catch (final HibernateException he) {
			LOG.error("Exception occurred while check Counter", he);
			throw new EGOVRuntimeException("Exception occurred in checkCounter", he);
		} catch (final Exception e) {
			LOG.error("Exception occurred while check Counter", e);
			throw new EGOVRuntimeException("Exception occurred in checkCounter", e);
		}
		return b;

	}
}
