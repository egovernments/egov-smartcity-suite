/*
 * @(#)GenericHibernateDaoFactory.java 3.0, 17 Jun, 2013 11:25:23 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.commons.dao;

import org.egov.infstr.commons.Module;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.config.AppData;
import org.egov.infstr.config.dao.AppConfigValuesDAO;
import org.egov.infstr.config.dao.AppConfigValuesHibernateDAO;
import org.egov.infstr.config.dao.AppDataDAO;
import org.egov.infstr.config.dao.AppDataHibernateDAO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class GenericHibernateDaoFactory {

    private SessionFactory sessionFactory;

    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    public GenericHibernateDaoFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public AppConfigValuesDAO getAppConfigValuesDAO() {
        return new AppConfigValuesHibernateDAO(AppConfigValues.class, getCurrentSession());
    }


    public AppDataDAO getAppDataDAO() {
        return new AppDataHibernateDAO(AppData.class, getCurrentSession());
    }

}
