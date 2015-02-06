/*
 * @(#)RBCHibernateDAOFactory.java 3.0, 14 Jun, 2013 5:46:51 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.rrbac.dao;

import org.egov.EgovSpringContextHolder;
import org.egov.lib.rrbac.model.Action;
import org.egov.lib.rrbac.model.Entity;
import org.egov.lib.rrbac.model.RuleGroup;
import org.egov.lib.rrbac.model.RuleType;
import org.egov.lib.rrbac.model.Rules;
import org.egov.lib.rrbac.model.Task;
import org.hibernate.Session;

/**
 * Returns Hibernate-specific instances of DAOs.
 * <p>
 * One of the responsiblities of the factory is to inject a Hibernate Session
 * into the DAOs. You can customize the getCurrentSession() method if you
 * are not using the default strategy, which simply delegates to
 * <tt>HibernateUtil.getCurrentSession()</tt>, and also starts a transaction
 * lazily, if none exists already for the current thread or current EJB.
 * <p>
 * If for a particular DAO there is no additional non-CRUD functionality, we use
 * an inner class to implement the interface in a generic way. This allows clean
 * refactoring later on, should the interface implement business data access
 * methods at some later time. Then, we would externalize the implementation into
 * its own first-level class. We can't use anonymous inner classes for this trick
 * because they can't extend or implement an interface and they can't include
 * constructors.
 *
 */
public class RBCHibernateDAOFactory extends RBCDAOFactory {
	
	
	public EntityDAO getEntityDAO() {
		return new EntityHibernateDAO(Entity.class, getCurrentSession());
	}
	
	public TaskDAO getTaskDAO() {
		return new TaskHibernateDAO(Task.class, getCurrentSession());
	}
	
	public ActionDAO getActionDAO() {
		return new ActionHibernateDAO(Action.class, getCurrentSession());
	}
	
	public RuleGroupDAO getRuleGroupDAO() {
		return new RuleGroupHibernateDAO(RuleGroup.class, getCurrentSession());
	}
	
	public RuleTypeDAO getRuleTypeDAO() {
		return new RuleTypeHibernateDAO(RuleType.class, getCurrentSession());
	}
	
	public RulesDAO getRulesDAO() {
		return new RulesHibernateDAO(Rules.class, getCurrentSession());
	}
	
	protected Session getCurrentSession() {
		// Get a Session and begin a database transaction. If the current
		// thread/EJB already has an open Sessio n and an ongoing Transaction,
		// this is a no-op and only returns a reference to the current Session.
		// HibernateUtil.beginTransaction();
		return EgovSpringContextHolder.sessionFactory().getCurrentSession();
	}
	
}
