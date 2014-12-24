/*
 * @(#)SecurityProcessAdaptor.java 3.0, 18 Jun, 2013 4:03:50 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.security.device.services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The Interface SecurityProcessAdaptor.
 * The base interface to all Device specific security processing 
 */
public interface SecurityProcessAdaptor {
	
	/**
	 * Process the client specific login request and return the response message.
	 * @param request the request
	 * @return the string
	 */
	String processLoginRequest(HttpServletRequest request);
	
	/**
	 * Checks if its is login request or not.
	 * @param request the request
	 * @return true, if is login request
	 */
	boolean isLoginRequest(HttpServletRequest request);
	
	/**
	 * Do any other operation before invoking filter chain.
	 * @param request the request
	 * @param response the response
	 */
	void doBeforeFilterChain(HttpServletRequest request, HttpServletResponse response);
	
	/**
	 * Gets the content type.
	 * @return the content type
	 */
	String getContentType();
}
