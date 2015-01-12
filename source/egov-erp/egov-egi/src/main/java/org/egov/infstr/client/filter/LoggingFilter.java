/*
 * @(#)LoggingFilter.java 3.0, 18 Jun, 2013 12:54:48 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.client.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.NDC;

/**
 * Filter use to sent NDC out put to the written log without user intervention
 * 
 * @IMP : This Filter must be applied after SetThreadLocals Filter
 * @IMP : In <filter-mapping> the <url-pattern> must match exactly as SetThreadLocals's <filter-mapping> <url-pattern>
 */
public class LoggingFilter implements Filter {
    
    private FilterConfig config;
    
    @Override
    public void destroy() {
	config = null;
	
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
	try {
	    NDC.push(EGOVThreadLocals.getDomainName());
	    chain.doFilter(request, response);
	} finally {
	    NDC.pop();
	    NDC.remove();
	}
	
    }
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
	this.config = filterConfig;
	
    }
    
}
