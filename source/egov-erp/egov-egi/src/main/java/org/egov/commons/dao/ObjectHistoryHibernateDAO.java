/*
 * @(#)ObjectHistoryHibernateDAO.java 3.0, 11 Jun, 2013 4:12:17 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons.dao;

import org.egov.commons.ObjectHistory;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.hibernate.Session;

public class ObjectHistoryHibernateDAO extends GenericHibernateDAO implements ObjectHistoryDAO {

	public ObjectHistoryHibernateDAO() {
		super(ObjectHistory.class,null);
	}
	
	public ObjectHistoryHibernateDAO(Class persistentClass, Session session) {
		super(persistentClass, session);
	}

}
