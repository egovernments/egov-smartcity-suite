/*
 * @(#)AppData.java 3.0, 17 Jun, 2013 11:29:05 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.config;

import java.io.Serializable;

public class AppData implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String key;
	private String module;
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String toString() {
		return "AppData[ Module : " + this.module + " , Key :" + this.key + " , Value : " + this.value + "]";
	}
}