/*
 * @(#)SubSchemeHibernateDAO.java 3.0, 14 Jun, 2013 11:05:49 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons.dao;

import org.egov.commons.SubScheme;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;

public class SubSchemeHibernateDAO extends GenericHibernateDAO implements SubSchemeDAO {

	public SubSchemeHibernateDAO() {
		super(SubScheme.class, null);
	}

	public SubSchemeHibernateDAO(final Class persistentClass, final Session session) {
		super(persistentClass, session);
	}

	@Override
	public SubScheme getSubSchemeById(final Integer id) {
		final Query query = HibernateUtil.getCurrentSession().createQuery("from SubScheme s where s.id=:subschemeid");
		query.setInteger("subschemeid", id);
		return (SubScheme) query.uniqueResult();
	}

	@Override
	public SubScheme getSubSchemeByCode(final String code) {
		final Query query = HibernateUtil.getCurrentSession().createQuery("from SubScheme s where s.code=:subschemecode");
		query.setString("subschemecode", code);
		return (SubScheme) query.uniqueResult();
	}
}
