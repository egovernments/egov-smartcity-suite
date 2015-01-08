/*
 * @(#)RelationHibernateDAO.java 3.0, 14 Jun, 2013 11:00:10 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons.dao;

import org.egov.commons.Relation;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.hibernate.Session;

public class RelationHibernateDAO extends GenericHibernateDAO {

	public RelationHibernateDAO() {
		super(Relation.class, null);
	}

	public RelationHibernateDAO(final Class persistentClass, final Session session) {
		super(persistentClass, session);
	}
}
