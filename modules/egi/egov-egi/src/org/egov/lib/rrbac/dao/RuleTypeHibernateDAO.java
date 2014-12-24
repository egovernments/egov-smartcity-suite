/*
 * @(#)RuleTypeHibernateDAO.java 3.0, 15 Jun, 2013 11:27:33 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.rrbac.dao;

import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.lib.rrbac.model.RuleType;
import org.hibernate.Session;

public class RuleTypeHibernateDAO extends GenericHibernateDAO implements RuleTypeDAO {

	public RuleTypeHibernateDAO() {
		super(RuleType.class, null);
	}

	public RuleTypeHibernateDAO(final Class persistentClass, final Session session) {
		super(persistentClass, session);
	}
}
