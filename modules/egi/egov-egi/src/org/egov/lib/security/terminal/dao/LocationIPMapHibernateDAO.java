/*
 * @(#)LocationIPMapHibernateDAO.java 3.0, 14 Jun, 2013 3:19:12 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.security.terminal.dao;

import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.lib.security.terminal.model.LocationIPMap;
import org.hibernate.Session;

public class LocationIPMapHibernateDAO extends GenericHibernateDAO implements LocationIPMapDAO {

	public LocationIPMapHibernateDAO() {
		super(LocationIPMap.class, null);
	}

	public LocationIPMapHibernateDAO(final Class persistentClass, final Session session) {
		super(persistentClass, session);
	}

}
