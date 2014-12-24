/*
 * @(#)WardHibernateDAO.java 3.0, 14 Jun, 2013 11:44:04 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons.dao;

import org.egov.commons.CWard;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.hibernate.Session;

public class WardHibernateDAO extends GenericHibernateDAO implements WardDAO {

	public WardHibernateDAO() {
		super(CWard.class, null);
	}

	public WardHibernateDAO(final Class persistentClass, final Session session) {
		super(persistentClass, session);

	}
}
