/*
 * @(#)TerminalDAO.java 3.0, 16 Jun, 2013 12:49:23 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.rjbac.user.dao;

import java.util.List;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.lib.rjbac.user.Terminal;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TerminalDAO {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TerminalDAO.class);
	private SessionFactory sessionFactory;

	public TerminalDAO(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**
	 * This method called getTerminal()
	 * @param java.lang.String ipaddress
	 * @return java.util.List of array objects
	 * @exception:HibernateException
	 */
	public List<Terminal> getTerminal(final String ipAddr) {
		try {
			final Query qry = sessionFactory.getCurrentSession().createQuery("FROM TerminalImpl TI WHERE TI.ipAddress = :ipAddr ");
			qry.setString("ipAddr", ipAddr);
			return qry.list();
		} catch (final HibernateException e) {
			LOGGER.error("Exception encountered in getTerminal", e);
			throw new EGOVRuntimeException("Exception encountered in getTerminal", e);
		}
	}
}
