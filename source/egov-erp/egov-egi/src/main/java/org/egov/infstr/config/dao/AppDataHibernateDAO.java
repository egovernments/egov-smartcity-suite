/*
 * @(#)AppDataHibernateDAO.java 3.0, 17 Jun, 2013 11:38:41 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.config.dao;

import org.egov.infstr.config.AppData;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.hibernate.Query;
import org.hibernate.Session;

public class AppDataHibernateDAO extends GenericHibernateDAO implements AppDataDAO {

	public AppDataHibernateDAO() {
		super(AppData.class, null);
	}

	public AppDataHibernateDAO(final Class persistentClass, final Session session) {
		super(persistentClass, session);
	}

	@Override
	public AppData updateAppDataValue(final AppData appData) {
		final AppData updatedAppData = (AppData) update(appData);
		return updatedAppData;
	}

	@Override
	public AppData getValueByModuleAndKey(final String moduleName, final String key) {

		final Query qry = getCurrentSession().createQuery("from AppData a where a.module =:moduleName  and a.key=:key");
		qry.setString("key", key);
		qry.setString("moduleName", moduleName);
		return (AppData) qry.uniqueResult();

	}
}
