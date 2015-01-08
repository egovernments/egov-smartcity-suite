/*
 * @(#)ERPWebApplicationContext.java 3.0, 7 Jun, 2013 11:04:22 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.web.utils;

import javax.servlet.ServletContext;

import org.egov.infstr.client.filter.EGOVThreadLocals;


public final class ERPWebApplicationContext {
	public static enum ContextName{egi,eis,EGF,payroll,erpcollections,works,ptis,assets,lcms,pgr,dms}
	private ERPWebApplicationContext() {}
	
	public static ServletContext getServletContext() {
		return EGOVThreadLocals.getServletContext();
	}
	
	public static ServletContext getServletContext(final ERPWebApplicationContext.ContextName webappCtxtName) {
		return EGOVThreadLocals.getServletContext().getContext("/"+webappCtxtName);
	}
}
