/*
 * @(#)ExceptionInterceptor.java 3.0, 14 Jun, 2013 12:29:45 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.web.interceptors;

import org.egov.infstr.utils.HibernateUtil;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.ExceptionHolder;
import com.opensymphony.xwork2.interceptor.ExceptionMappingInterceptor;

/**
 * This class overrides the default behaviour for transaction management. The transaction is marked for rollback, so that the HibSessionServletFilter further down the filter stack will rollback the
 * current transaction. For this to work, an exception mapping for the exception thrown needs to be defined in the struts.xml:
 * 
 * <pre>
 *       &lt;global-results&gt;
 *           &lt;result name=&quot;genericError&quot;&gt;/error/error.jsp&lt;/result&gt;
 *      &lt;/global-results&gt;
 * 
 *      &lt;global-exception-mappings&gt;
 *           &lt;exception-mapping exception=&quot;java.lang.Exception&quot; result=&quot;genericError&quot;/&gt;
 *      &lt;/global-exception-mappings&gt;
 * 
 * 
 * </pre>
 * 
 * @author Sahina Bose
 * 
 */
public class ExceptionInterceptor extends ExceptionMappingInterceptor {
	
	private static final long serialVersionUID = 0L;
	
	@Override
	protected void publishException(ActionInvocation invocation, ExceptionHolder exceptionHolder) {
		HibernateUtil.markForRollback();
		super.publishException(invocation, exceptionHolder);
	}
	
}
