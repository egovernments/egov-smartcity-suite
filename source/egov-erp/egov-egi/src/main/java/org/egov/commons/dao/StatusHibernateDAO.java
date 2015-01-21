/*
 * @(#)StatusHibernateDAO.java 3.0, 14 Jun, 2013 11:03:59 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons.dao;

import java.util.List;

import org.egov.commons.Status;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.hibernate.Query;
import org.hibernate.Session;

public class StatusHibernateDAO extends GenericHibernateDAO {
	public StatusHibernateDAO() {
		super(Status.class, null);
	}

	public StatusHibernateDAO(final Class persistentClass, final Session session) {
		super(persistentClass, session);
	}

	public List getStatusByModuleType(final String moduleType) {
		final Query qry = getCurrentSession().createQuery("from Status st where st.moduleType =:moduleType order by id");
		qry.setString("moduleType", moduleType);
		return qry.list();

	}
}
