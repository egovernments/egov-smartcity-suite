/*
 * @(#)RulesHibernateDAO.java 3.0, 15 Jun, 2013 11:26:02 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.rrbac.dao;

import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.lib.rrbac.model.Rules;
import org.hibernate.Query;
import org.hibernate.Session;

public class RulesHibernateDAO extends GenericHibernateDAO implements RulesDAO {
	
	public RulesHibernateDAO() {
		super(Rules.class, null);
	}
	
	public RulesHibernateDAO(Class persistentClass, Session session) {
		super(persistentClass, session);
	}
	
	public Rules findRulesByName(String name) {
		Query qry = getSession().createQuery("from Rules rl where rl.name =:name ");
		qry.setString("name", name);
		return (Rules) qry.uniqueResult();
		
	}
}
