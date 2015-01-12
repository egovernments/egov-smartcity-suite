/*
 * @(#)SecurityFilter.java 3.0, 18 Jun, 2013 4:02:22 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.security.device.filter;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.egov.infstr.security.device.services.SecurityProcessAdaptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * The Class SecurityFilter.
 * This Filter can be used with all devices login which basically doesn't have a HTTPSession<br/>
 * Login authentication and authorisation will be done at @see AbstractSecurityProcessAdaptor#authenticateLogin(String,String)<br/> 
 * Client specific request / response handling will be done at Client specific implementation of <br/>
 * {@see AbstractSecurityProcessAdaptor}.
 * This filter will dynamically get the appropriate client specific {@see SecurityProcessAdaptor} based <br/>
 * on the value of request parameter "channelID".<br/>
 * First it will query to client code to check whether its a Login request or not,<br/>
 * if its a Login request then request will be forwarded to the client specific handler else <br/>
 * it will chain the filter.
 */
public class SecurityFilter implements Filter {
	
	/**slf4j Logger handler*/
	private static final Logger LOG = LoggerFactory.getLogger(SecurityFilter.class);
	
	/** The bean context. Spring bean context */
	private WebApplicationContext beanContext;
	
	/**The FilterConfig to process filter specific logic*/
	private FilterConfig filterConfig;
	
	/** The Constant CHANNEL_. will be append with the request parameter value of "channelID"*/
	//private static final String CHANNEL_ = "CHANNEL_";
	
	private boolean logEnabled;
		
	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(final FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		this.beanContext = WebApplicationContextUtils.getWebApplicationContext(this.filterConfig.getServletContext());
		if (this.filterConfig.getInitParameter("logEnabled") != null) {
			logEnabled = Boolean.valueOf(this.filterConfig.getInitParameter("logEnabled"));
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain filterChain) throws IOException, ServletException {
		final HttpServletRequest request = (HttpServletRequest) req;
		final HttpServletResponse response = (HttpServletResponse) res;
		if (logEnabled) {
			LOG.info("SecurityFilter invoking request URL : \n {}",getFullRequestURL(request));
		}
		
		//Get the ChannelID from request parameter to get the appropriate SecurityProcessAdopter
		// The security process adaptor used to adopt client specific logic to handle request.
		//Since the Airtel handheld divice request can come with channelId and channelID
		//final String channelID = request.getParameter("channelID") == null ? request.getParameter("channelId") : request.getParameter("channelID");
		
		final SecurityProcessAdaptor securityProcessAdaptor = (SecurityProcessAdaptor) this.beanContext.getBean("securityProcessAdaptor");
		
		//Client specific response content type will be set
		response.setContentType(securityProcessAdaptor.getContentType());
		
		//if requested for login give handle to the client else chain the filter
		if (securityProcessAdaptor.isLoginRequest(request)) {
			final String responseValue = securityProcessAdaptor.processLoginRequest(request);
			response.getWriter().write(responseValue);
			if (logEnabled) {
				LOG.info("Handheld Login Response : \n {}",responseValue);
			}
			return;
		} else {
			securityProcessAdaptor.doBeforeFilterChain(request, response);
			filterChain.doFilter(request, response);
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {
		this.filterConfig = null;
		this.beanContext = null;
		this.logEnabled = false;
	}
	
	/**
	 * Since getQueryParam function doesn't work for Handheld device, This function <br/>
	 * will read all the parameter and reconstruct the URL with query param
	 **/
	private String getFullRequestURL (final HttpServletRequest request) {
		final Enumeration<String> paramNames = request.getParameterNames();
		final StringBuilder requestURL = new StringBuilder(request.getRequestURL());
		requestURL.append("?");
		while( paramNames.hasMoreElements()) {
			final String paramName = paramNames.nextElement();
			requestURL.append(paramName).append("=").append(request.getParameter(paramName)).append("&");
		}
		requestURL.deleteCharAt(requestURL.length()-1);
		return requestURL.toString();
	}
}
