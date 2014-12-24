/*
 * @(#)ServletActionRedirectResult.java 3.0, 7 Jun, 2013 11:05:16 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.web.utils;

/**
 * Extension for {@link org.apache.struts2.dispatcher.ServletActionRedirectResult}<br/>
 * adding extra functionality to include model id, module name with request parameter.
 **/
public class ServletActionRedirectResult extends org.apache.struts2.dispatcher.ServletActionRedirectResult {
	
	private static final long serialVersionUID = 1L;
	
	public void setModelId(String id) {
		this.addParameter("model.id", id);
	}
	
	public void setModuleName(String moduleName) {
		this.addParameter("moduleName", moduleName);
	}
	
}
