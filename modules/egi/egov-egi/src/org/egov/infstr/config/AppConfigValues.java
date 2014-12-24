/*
 * @(#)AppConfigValues.java 3.0, 17 Jun, 2013 11:28:36 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.config;

import java.io.Serializable;
import java.util.Date;

public class AppConfigValues implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private AppConfig key;
	private String value;
	private Date effectiveFrom;

	public Date getEffectiveFrom() {
		return effectiveFrom;
	}

	public void setEffectiveFrom(Date effectiveFrom) {
		this.effectiveFrom = effectiveFrom;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public AppConfig getKey() {
		return key;
	}

	public void setKey(AppConfig key) {
		this.key = key;
	}
}