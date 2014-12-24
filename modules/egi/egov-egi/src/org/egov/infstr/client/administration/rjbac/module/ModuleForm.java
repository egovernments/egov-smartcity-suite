/*
 * @(#)ModuleForm.java 3.0, 18 Jun, 2013 3:15:01 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.client.administration.rjbac.module;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.egov.infstr.client.EgovActionForm;

public class ModuleForm extends EgovActionForm implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String moduleId;
	private String moduleName;
	private String moduleNameLocal;
	private String moduleDescription;
	private String parentModuleId;
	boolean isEnabled;
	private String baseURL;
	private String orderNumber;

	/**
	 * @return Returns the moduleId.
	 */
	public String getModuleId() {
		return this.moduleId;
	}

	/**
	 * @param moduleId The moduleId to set.
	 */
	public void setModuleId(final String moduleId) {
		this.moduleId = moduleId;
	}

	/**
	 * @return Returns the baseURL.
	 */
	public String getBaseURL() {
		return this.baseURL;
	}

	/**
	 * @param baseURL The baseURL to set.
	 */
	public void setBaseURL(final String baseURL) {
		this.baseURL = baseURL;
	}

	/**
	 * @return Returns the isEnabled.
	 */
	public boolean getIsEnabled() {
		return this.isEnabled;
	}

	/**
	 * @param isEnabled The isEnabled to set.
	 */
	public void setIsEnabled(final boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	/**
	 * @return Returns the moduleDescription.
	 */
	public String getModuleDescription() {
		return this.moduleDescription;
	}

	/**
	 * @param moduleDescription The moduleDescription to set.
	 */
	public void setModuleDescription(final String moduleDescription) {
		this.moduleDescription = moduleDescription;
	}

	/**
	 * @return Returns the moduleName.
	 */
	public String getModuleName() {
		return this.moduleName;
	}

	/**
	 * @param moduleName The moduleName to set.
	 */
	public void setModuleName(final String moduleName) {
		this.moduleName = moduleName;
	}

	/**
	 * @return Returns the moduleNameLocal.
	 */
	public String getModuleNameLocal() {
		return this.moduleNameLocal;
	}

	/**
	 * @param moduleNameLocal The moduleNameLocal to set.
	 */
	public void setModuleNameLocal(final String moduleNameLocal) {
		this.moduleNameLocal = moduleNameLocal;
	}

	/**
	 * @return Returns the orderNumber.
	 */
	public String getOrderNumber() {
		return this.orderNumber;
	}

	/**
	 * @param orderNumber The orderNumber to set.
	 */
	public void setOrderNumber(final String orderNumber) {
		this.orderNumber = orderNumber;
	}

	/**
	 * @return Returns the parentModuleId.
	 */
	public String getParentModuleId() {
		return this.parentModuleId;
	}

	/**
	 * @param parentModuleId The parentModuleId to set.
	 */
	public void setParentModuleId(final String parentModuleId) {
		this.parentModuleId = parentModuleId;
	}

	@Override
	public void reset(final ActionMapping mapping, final HttpServletRequest req) {
		this.moduleName = "";
		this.moduleNameLocal = "";
		this.moduleDescription = "";
		this.parentModuleId = "";
		this.baseURL = "";
		this.orderNumber = "";
	}

}
