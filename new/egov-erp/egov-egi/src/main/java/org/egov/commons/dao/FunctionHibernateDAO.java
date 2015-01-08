/*
 * @(#)FunctionHibernateDAO.java 3.0, 11 Jun, 2013 2:59:38 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons.dao;

import java.util.List;

import org.egov.commons.CFunction;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.hibernate.Query;
import org.hibernate.Session;

public class FunctionHibernateDAO extends GenericHibernateDAO implements FunctionDAO {

	public FunctionHibernateDAO() {
		super(CFunction.class,null);
	}
	
	public FunctionHibernateDAO(final Class persistentClass, final Session session) {
		super(persistentClass, session);

	}

	@Override
	public List getAllActiveFunctions() {
		return getSession().createQuery("from CFunction where isactive = 1 and isnotleaf=0 order by name").list();

	}

	@Override
	public CFunction getFunctionByCode(final String functionCode) {
		final Query qry = getSession().createQuery("from CFunction where code=:code");
		qry.setString("code", functionCode);
		return (CFunction) qry.uniqueResult();
	}

	@Override
	public CFunction getFunctionById(final Long Id) {
		final Query qry = getSession().createQuery("from CFunction where id=:id");
		qry.setLong("id", Id);
		return (CFunction) qry.uniqueResult();
	}
}
