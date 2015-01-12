/*
 * @(#)RBCDAOFactory.java 3.0, 14 Jun, 2013 5:45:51 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.rrbac.dao;

/**
 * Defines all DAOs and the concrete factories to get the conrecte DAOs.
 * <p>
 * Implementation: If you write a new DAO, this class has to know about it. If you add a new persistence mechanism, add an additional concrete factory for it to the enumeration of factories.
 * <p>
 */
public abstract class RBCDAOFactory {

	private static final RBCDAOFactory HIBERNATE = new RBCHibernateDAOFactory();

	public static RBCDAOFactory getDAOFactory() {
		return HIBERNATE;
	}

	public abstract EntityDAO getEntityDAO();

	public abstract TaskDAO getTaskDAO();

	public abstract ActionDAO getActionDAO();

	public abstract RuleGroupDAO getRuleGroupDAO();

	public abstract RuleTypeDAO getRuleTypeDAO();

	public abstract RulesDAO getRulesDAO();

}
