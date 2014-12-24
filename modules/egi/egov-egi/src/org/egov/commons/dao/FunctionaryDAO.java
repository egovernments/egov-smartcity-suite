/*
 * @(#)FunctionaryDAO.java 3.0, 11 Jun, 2013 2:49:53 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons.dao;

import java.util.List;

import org.egov.commons.Functionary;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.hibernate.Session;

public class FunctionaryDAO extends GenericHibernateDAO {

	public FunctionaryDAO() {
		super(Functionary.class, null);
	}

	public FunctionaryDAO(final Class persistentClass, final Session session) {
		super(persistentClass, session);
	}

	public List<Functionary> findAllActiveFunctionary() {
		return getSession().createQuery("from Functionary f where isactive=1").list();
	}

	public Functionary functionaryById(final Integer id) {
		return (Functionary) getSession().get(Functionary.class, id.intValue());
	}
}
