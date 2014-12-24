/*
 * @(#)EGOVThreadLocals.java 3.0, 18 Jun, 2013 12:53:06 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.client.filter;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.egov.exceptions.EGOVRuntimeException;


/**
 * This class sets thread local variables for JNDI name, Hibernate Session factory name,
 * domain name ( for instance localhost or cityname based on the url the application is hosted at)
 * userId (the logged in userid for the current request thread.)
 *
 */
public class EGOVThreadLocals {

	/** The jndi name. */
	private static ThreadLocal <String> jndiName = new ThreadLocal<String>();
	
	/** The hibernate session factory name. */
	private static ThreadLocal <String> hibFactName = new ThreadLocal<String>();
	
	/** The domain name. */
	private static ThreadLocal <String> domainName = new ThreadLocal<String>();
	
	/** The user id. */
	private static ThreadLocal <String> userId = new ThreadLocal<String>();
	
	/** The user service. */
	private static ThreadLocal <String> userService = new ThreadLocal<String>();

	/** The user service. */
	private static ThreadLocal <ServletContext> servletContext = new ThreadLocal<ServletContext>();
	
	/** The LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(EGOVThreadLocals.class);

	/**
	 * Gets the jndi name.
	 * 
	 * @return the jndi name
	 */
	public static String getJndiName() {
		if (jndiName.get() == null) {
			LOG.error("jndiName is null in EgovThreadLocal");
			throw new EGOVRuntimeException("Internal Server Error");
		}
		return jndiName.get();
	}

	/**
	 * Sets the jndi name.
	 * 
	 * @param jndiNam the new jndi name
	 */
	public static void setJndiName(String jndiNam) {
		jndiName.set(jndiNam);
	}

	/**
	 * Gets the session factory name.
	 * 
	 * @return session factory name associated with request thread.
	 * Throws {@link EGOVRuntimeException} if the thread local variable for
	 * session factory name is null.
	 */
	public static String getHibFactName() {
		if (hibFactName.get() == null) {
			LOG.error("hibFactName is null in EGOVThreadLocals");
			throw new EGOVRuntimeException("Internal Server Error");
		}
		return  hibFactName.get();
	}

	/**
	 * Sets the session factory name.
	 * 
	 * @param jndiName the new session factory name
	 */
	public static void setHibFactName(String jndiName) {
		hibFactName.set(jndiName);
	}

	/**
	 * Gets the domain name.
	 * 
	 * @return domain name associated with request thread.
	 * Throws {@link EGOVRuntimeException} if the thread local variable for
	 * domain name is null.
	 */
	public static String getDomainName() {
		if (domainName.get() == null) {
			LOG.error("domainName is null in EGOVThreadLocals");
			throw new EGOVRuntimeException("Internal Server Error");
		}
		return domainName.get();
	}

	/**
	 * Sets the domain name.
	 * 
	 * @param domName the new domain name
	 */
	public static void setDomainName(String domName) {
		domainName.set(domName);
	}

	/**
	 * Gets the user id.
	 * 
	 * @return userId associated with request thread.
	 * Throws {@link EGOVRuntimeException} if the thread local variable for
	 * userId is null.
	 */
	public static String getUserId() {
		if (userId.get() == null) {
			LOG.error("userId is null in EGOVThreadLocals");
			throw new EGOVRuntimeException("Internal Server Error");
		}
		return userId.get();
	}

	/**
	 * Sets the user id.
	 * 
	 * @param userid the new user id
	 */
	public static void setUserId(String userid) {
		userId.set(userid);
	}
	
	/**
	 * Gets the user service.
	 * 
	 * @return userService associated with request thread.
	 * Throws {@link EGOVRuntimeException} if the thread local variable for
	 * userService is null.
	 * 
	 * userService is used by CAS SSO only
	 */	
	public static String getUserService() {
		if (userService.get() == null) {
			LOG.error("User Service is null in EGOVThreadLocals");
			throw new EGOVRuntimeException("Internal Server Error");
		}
		return userService.get();
	}

	/**
	 * Sets the user service.
	 * this API used for CAS SSO to set the service as a Request Param
	 * 
	 * @param userservice the new user service
	 */
	public static void setUserService(String userservice) {
		userService.set(userservice);
	}

	
	/**
	 * @return the servletContext
	 */
	public static ServletContext getServletContext() {
		if (servletContext.get() == null) {
			LOG.error("ServletContext not available in EgovThreadLocale");
			throw new EGOVRuntimeException("Internal Server Error");
		}
		return servletContext.get();
	}

	
	/**
	 * @param servletContext the servletContext to set
	 */
	public static void setServletContext(ServletContext servlContext) {
		servletContext.set(servlContext);
	}

}
