/*
 * @(#)HibSessionServletFilter.java 3.0, 18 Jun, 2013 12:53:53 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.client.filter;

import org.egov.exceptions.AuthorizationException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.infstr.utils.RequestUtils;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessResourceFailureException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


/**
 * This filter handles the transaction management.
 * 
 * @author sahinab
 * @version 2.0
 * @since 1.00
 */
@Deprecated
public class HibSessionServletFilter implements Filter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HibSessionServletFilter.class);
	
	public void init(FilterConfig config) throws ServletException {
		
	}
	
	/**
	 * Begins a transaction before processing of the chain On successful completion commits the transaction.
	 * If any exception occurred during the request processing, the transaction is rolled back. On
	 * exception, also logs all the request parameter (debug level log)
	 * 
	 * @param request the request
	 * @param response the response
	 * @param chain the chain
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ServletException the servlet exception
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		try {
			HibernateUtil.beginTransaction();
			chain.doFilter(request, response);
			HibernateUtil.commitTransaction();
			
		} catch (AuthorizationException ae) {
			// throwing specific exception when it is AuthorizationException
			HibernateUtil.rollbackTransaction();
			throw new AuthorizationException(ae.getMessage(), ae);
		} catch (Throwable ex) {
			// Rollback only
			HibernateUtil.rollbackTransaction();
			// we log the request parameters to help debug the problem
			LOGGER.debug(RequestUtils.getRequestParamsAsXMLString(request));
			throw new EGOVRuntimeException(ex.getMessage(), ex);
		} finally {
			LOGGER.debug("Closing single Hibernate Session ");
			HibernateUtil.closeSession();
		}
		
	}
	
	/**
	 * Gets the session.
	 * 
	 * @param sessionFactory the session factory
	 * @return the session
	 * @throws DataAccessResourceFailureException the data access resource failure exception
	 */
	protected Session getSession(SessionFactory sessionFactory) throws DataAccessResourceFailureException {
		Session session = HibernateUtil.getCurrentSession();
		session.setFlushMode(FlushMode.AUTO);
		return session;
	}
	
	/**
	 * Lookup session factory.
	 * 
	 * @param request the request
	 * @return the session factory
	 */
	protected SessionFactory lookupSessionFactory(HttpServletRequest request) {
		return HibernateUtil.getSessionFactory();
	}
	
	/**
	 * Lookup session factory.
	 * 
	 * @return the session factory
	 */
	protected SessionFactory lookupSessionFactory() {
		return HibernateUtil.getSessionFactory();
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {
		
	}
	
}
