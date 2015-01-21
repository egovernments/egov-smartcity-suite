/*
 * @(#)RuleGroupHibernateDAO.java 3.0, 14 Jun, 2013 5:48:04 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.rrbac.dao;

import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.lib.rrbac.model.RuleGroup;
import org.hibernate.Query;
import org.hibernate.Session;

public class RuleGroupHibernateDAO extends GenericHibernateDAO implements RuleGroupDAO {

	public RuleGroupHibernateDAO() {
		super(RuleGroup.class, null);
	}

	public RuleGroupHibernateDAO(final Class persistentClass, final Session session) {
		super(persistentClass, session);
	}

	@Override
	public RuleGroup findRuleGroupByName(final String name) {
		final Query qry = getCurrentSession().createQuery("from RuleGroup rg where rg.name =:name ");
		qry.setString("name", name);
		return (RuleGroup) qry.uniqueResult();

	}

}
