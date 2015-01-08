/*
 * @(#)TaskHibernateDAO.java 3.0, 15 Jun, 2013 11:29:01 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.rrbac.dao;

import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.lib.rrbac.model.Task;
import org.hibernate.Query;
import org.hibernate.Session;

public class TaskHibernateDAO extends GenericHibernateDAO implements TaskDAO {

	public TaskHibernateDAO() {
		super(Task.class, null);
	}

	public TaskHibernateDAO(final Class persistentClass, final Session session) {
		super(persistentClass, session);
	}

	@Override
	public Task findTaskByName(final String name) {
		final Query qry = getSession().createQuery("from Task tsk where tsk.name =:name ");
		qry.setString("name", name);
		return (Task) qry.uniqueResult();

	}
}
