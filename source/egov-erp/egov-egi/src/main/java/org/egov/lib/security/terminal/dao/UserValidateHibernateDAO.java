/*
 * @(#)UserValidateHibernateDAO.java 3.0, 14 Jun, 2013 3:25:22 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.security.terminal.dao;

import org.egov.infstr.security.utils.CryptoHelper;
import org.egov.lib.rjbac.user.User;
import org.egov.lib.security.terminal.model.Location;
import org.egov.lib.security.terminal.model.UserValidate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Date;

public class UserValidateHibernateDAO implements UserValidateDAO {

	private SessionFactory sessionFactory;

	public UserValidateHibernateDAO(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}


	@Override
	public boolean validateUser(final UserValidate obj) {
		final Query qry = getCurrentSession().createQuery("from UserImpl user where user.userName = :username and user.pwd = :password");
		qry.setString("username", obj.getUsername());
		qry.setString("password", CryptoHelper.encrypt(obj.getPassword()));
		return !qry.list().isEmpty();
	}

	@Override
	public boolean validateUserLocation(final UserValidate obj) {
		final StringBuilder queryStr = new StringBuilder();
		queryStr.append("Select user from Location loc,LocationIPMap locIP,UserImpl user, UserCounterMap map where map.userId = user.id and locIP.ipAddress = :ipaddress and  loc.id=locIP.location.id and ");
		queryStr.append("map.counterId = loc.id and loc.isActive=1 and user.isActive = 1 and user.userName = :username and user.pwd = :password ");
		queryStr.append("and loc.id = :locationId And ((map.toDate IS NULL AND map.fromDate <= :currDate) OR (map.fromDate <= :currDate AND map.toDate >= :currDate))");
		final Query qry = getCurrentSession().createQuery(queryStr.toString());
		qry.setString("username", obj.getUsername());
		qry.setString("password", CryptoHelper.encrypt(obj.getPassword()));
		qry.setString("ipaddress", obj.getIpAddress());
		qry.setDate("currDate", new Date());
		qry.setInteger("locationId", obj.getLocationId());
		return !qry.list().isEmpty();
	}

	@Override
	public boolean validateUserTerminal(final UserValidate obj) {
		final StringBuilder queryStr = new StringBuilder();
		queryStr.append("select user from Location loc,LocationIPMap locIP, UserImpl user, UserCounterMap map where map.userId = user.id and locIP.ipAddress = :ipaddress and  loc.id=locIP.location.id and ");
		queryStr.append("map.counterId = loc.id and loc.isActive=1 and user.isActive = 1 and user.userName = :username and user.pwd = :password ");
		queryStr.append("and loc.id = :counterId And ((map.toDate IS NULL AND map.fromDate <= :currDate) OR (map.fromDate <= :currDate AND map.toDate >= :currDate))");
		final Query qry = getCurrentSession().createQuery(queryStr.toString());
		qry.setString("username", obj.getUsername());
		qry.setString("password", CryptoHelper.encrypt(obj.getPassword()));
		qry.setString("ipaddress", obj.getIpAddress());
		qry.setDate("currDate", new Date());
		qry.setInteger("counterId", obj.getCounterId());
		return !qry.list().isEmpty();
	}

	@Override
	public Location getLocationByIP(final String ipAddress) {
		final Query qry = getCurrentSession().createQuery("select loc from Location loc,LocationIPMap locIP where locIP.ipAddress = :ipAddress and loc.locationId is null and loc.id=locIP.location.id and loc.isActive=1");
		qry.setString("ipAddress", ipAddress);
		return (Location) qry.uniqueResult();
	}

	@Override
	public Location getTerminalByIP(final String ipAddress) {
		final Query qry = getCurrentSession().createQuery("select loc from Location loc,LocationIPMap locIP where locIP.ipAddress = :ipAddress and loc.locationId is not null and loc.id=locIP.location.id and loc.isActive=1");
		qry.setString("ipAddress", ipAddress);
		return (Location) qry.uniqueResult();
	}

	/**
	 * API validtes whenther the given user is valid as if for current date and also the user should active in that period. 
	 * if true -- > the user is valid and acitve as of current date if false -- > 
	 * the user is not valid or not acitve as of current date
	 * @return boolean value
	 */
	@Override
	public boolean validateActiveUserForPeriod(final String userName) {
		final Query qry = getCurrentSession().createQuery(
				"select user from UserImpl user where user.userName = :username and user.isActive = 1 And ((user.toDate IS NULL AND user.fromDate <= :currDate) OR (user.fromDate <= :currDate AND user.toDate >= :currDate))");
		qry.setString("username", userName);
		qry.setDate("currDate", new Date());
		final User user = (User) qry.uniqueResult();
		return user != null;
	}
}
