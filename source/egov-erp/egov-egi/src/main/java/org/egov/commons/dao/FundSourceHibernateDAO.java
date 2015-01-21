/*
 * @(#)FundSourceHibernateDAO.java 3.0, 11 Jun, 2013 3:13:35 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons.dao;

import java.util.List;

import org.egov.commons.Fundsource;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.hibernate.Query;
import org.hibernate.Session;

public class FundSourceHibernateDAO extends GenericHibernateDAO {
	public FundSourceHibernateDAO() {
		super(Fundsource.class, null);
	}

	public FundSourceHibernateDAO(final Class persistentClass, final Session session) {
		super(persistentClass, session);
	}

	public Fundsource fundsourceById(final Integer id) {
		return (Fundsource) getCurrentSession().get(Fundsource.class, id.intValue());
	}

	public List<Fundsource> findAllActiveIsLeafFundSources() {
		return getCurrentSession().createQuery("from org.egov.commons.Fundsource where isactive = 1 and isnotleaf=0 order by name").list();
	}

	public Fundsource getFundSourceByCode(final String code) {
		final Query query = getCurrentSession().createQuery("from Fundsource f where f.code=:code");
		query.setString("code", code);
		return (Fundsource) query.uniqueResult();
	}
}
