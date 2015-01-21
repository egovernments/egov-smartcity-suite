/*
 * @(#)FunctionaryHibernateDAO.java 3.0, 11 Jun, 2013 2:57:30 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons.dao;

import java.math.BigDecimal;
import java.util.List;

import org.egov.commons.Functionary;
import org.hibernate.Query;
import org.hibernate.Session;

public class FunctionaryHibernateDAO extends FunctionaryDAO {

	public FunctionaryHibernateDAO() {
		super(Functionary.class,null);
	}
	
	public FunctionaryHibernateDAO(final Class persistentClass, final Session session) {
		super(persistentClass, session);
	}

	@Override
	public List findAllActiveFunctionary() {
		return getCurrentSession().createQuery("from Functionary f where isactive=1 order by code").list();

	}

	@Override
	public Functionary functionaryById(final Integer id) {

		return (Functionary) getCurrentSession().get(Functionary.class, id.intValue());
	}

	public Functionary getFunctionaryByCode(final BigDecimal functionaryCode) {
		final Query qry = getCurrentSession().createQuery("from Functionary where code=:code");
		qry.setBigDecimal("code", functionaryCode);
		return (Functionary) qry.uniqueResult();
	}

	public Functionary getFunctionaryByName(final String name) {
		final Query qry = getCurrentSession().createQuery("from Functionary where name=:name");
		qry.setString("name", name);
		return (Functionary) qry.uniqueResult();
	}

}
