/*
 * @(#)HibernateUtil.java 3.0, 18 Jun, 2013 12:05:53 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.utils;

import org.egov.EgovSpringContextHolder;
import org.hibernate.Session;

/**
 * Basic Hibernate helper class, handles SessionFactory, Session and Transaction.
 * <p>
 * Uses a static initializer to read startup options and initialize <tt>Configuration</tt> and <tt>SessionFactory</tt>.
 * <p>
 * This class tries to figure out if either ThreadLocal handling of the <tt>Session</tt> and <tt>Transaction</tt> should be used,
 * for resource local transactions or BMT, or if CMT with automatic <tt>Session</tt> handling is enabled.
 * <p>
 * To keep your DAOs free from any of this, just call <tt>HibernateUtil.getCurrentSession()</tt>in the constructor of each DAO.,
 * The recommended way to set resource local or BMT transaction boundaries is an interceptor, or a request filter.
 * <p>
 * This class also tries to figure out if JNDI binding of the <tt>SessionFactory</tt>is used, otherwise it falls back to a global static variable (Singleton).
 * <p>
 * If you want to assign a global interceptor, set its fully qualified class name with the system (or hibernate.properties/hibernate.cfg.xml) property
 * <tt>hibernate.util.interceptor_class</tt>. It will be loaded and instantiated on static initialization of HibernateUtil;
 * it has to have a no-argument constructor. You can call <tt>getInterceptor()</tt> if you need to provide settings before using the interceptor.
 * <p>
 */
@Deprecated
public class HibernateUtil {

	
	@Deprecated
	public static Session getCurrentSession() {
		return EgovSpringContextHolder.sessionFactory().createEntityManager().unwrap(Session.class);
	}

	}
