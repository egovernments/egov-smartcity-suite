/*
 * @(#)FundHibernateDAO.java 3.0, 11 Jun, 2013 3:00:53 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons.dao;

import java.util.List;

import org.egov.commons.Fund;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.hibernate.Query;
import org.hibernate.Session;

public class FundHibernateDAO extends GenericHibernateDAO {

	public FundHibernateDAO() {
		super(Fund.class, null);
	}
	
	public FundHibernateDAO(final Class persistentClass, final Session session) {
		super(persistentClass, session);
	}

	public List findAllActiveFunds() {
		return getSession().createQuery("from Fund where isactive = 1 order by name").list();

	}

	public Fund fundById(final Integer id) {
		return (Fund) getSession().get(Fund.class, id.intValue());
	}

	/**
	 * This method returns all the <code>Fund</code> records which are active and is a leaf.
	 * @return a <code>List</code> of <code>Fund</code> objects.
	 */
	public List findAllActiveIsLeafFunds() {
		return getSession().createQuery("from Fund where isactive = 1 and isnotleaf=0 order by name").list();
	}

	public Fund fundByCode(final String fundCode) {
		final Query qry = getSession().createQuery("FROM Fund f WHERE f.code =:fundCode");
		qry.setString("fundCode", fundCode);
		return (Fund) qry.uniqueResult();
	}
}
