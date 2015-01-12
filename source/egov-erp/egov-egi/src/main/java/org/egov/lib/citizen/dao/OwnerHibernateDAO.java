/*
 * @(#)OwnerHibernateDAO.java 3.0, 16 Jun, 2013 12:36:23 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.citizen.dao;

import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.lib.citizen.model.Owner;
import org.hibernate.Session;

public class OwnerHibernateDAO extends GenericHibernateDAO implements OwnerDAO {

	public OwnerHibernateDAO() {
		super(Owner.class, null);
	}

	/**
	 * @param persistentClass 
	 * @param session
	 */
	public OwnerHibernateDAO(final Class persistentClass, final Session session) {
		super(persistentClass, session);
	}
}