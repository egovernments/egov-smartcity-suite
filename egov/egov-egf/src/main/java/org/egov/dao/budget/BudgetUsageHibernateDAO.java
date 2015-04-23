package org.egov.dao.budget;

import org.egov.infstr.dao.GenericHibernateDAO;
import org.hibernate.Session;

public class BudgetUsageHibernateDAO extends GenericHibernateDAO implements BudgetUsageDAO{

	public BudgetUsageHibernateDAO(Class persistentClass, Session session) {
		super(persistentClass, session);
	}

	
}
