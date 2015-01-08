/*
 * @(#)SchemeHibernateDAO.java 3.0, 14 Jun, 2013 11:01:34 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons.dao;

import org.egov.commons.Scheme;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;

public class SchemeHibernateDAO extends GenericHibernateDAO implements SchemeDAO {

	public SchemeHibernateDAO() {
		super(Scheme.class, null);
	}

	public SchemeHibernateDAO(final Class persistentClass, final Session session) {
		super(persistentClass, session);
	}

	@Override
	public Scheme getSchemeById(final Integer id) {
		final Query query = HibernateUtil.getCurrentSession().createQuery("from Scheme s where s.id=:schemeid");
		query.setInteger("schemeid", id);
		return (Scheme) query.uniqueResult();
	}

	@Override
	public Scheme getSchemeByCode(final String code) {
		final Query query = HibernateUtil.getCurrentSession().createQuery("from Scheme s where s.code=:code");
		query.setString("code", code);
		return (Scheme) query.uniqueResult();
	}

}
