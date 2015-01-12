/*
 * @(#)AppConfig.java 3.0, 17 Jun, 2013 11:27:54 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.config;

import java.io.Serializable;
import java.util.Set;

public class AppConfig implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String keyName;
	private String module;
	private String description;
	private Set<AppConfigValues> appDataValues;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public Set<AppConfigValues> getAppDataValues() {
		return appDataValues;
	}

	public void setAppDataValues(Set<AppConfigValues> appDataValues) {
		this.appDataValues = appDataValues;
	}
}