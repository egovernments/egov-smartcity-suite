/*
 * @(#)Script.java 3.0, 17 Jun, 2013 2:51:14 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.models;

import java.util.Date;

import javax.script.CompiledScript;

import org.egov.commons.Period;
import org.egov.infstr.utils.DateUtils;

public class Script extends BaseModel {
	private static final long serialVersionUID = 1L;
	public static final String BY_NAME = "SCRIPT";
	public static final String QRY_SCRIPT_BY_NAME_DATE = "EGI_SCRIPT_BY_NAME_DATE";
	private String type;
	private String script;
	private String name;
	private Period period;
	private CompiledScript compiledScript;

	Script() {
		//FOR Hibernate
	}

	/**
	 * Creates a script with the given name,type and script body, valid between 01/01/1900 and 01/01/2100
	 * @param name
	 * @param type
	 * @param script
	 */
	public Script(String name, String type, String script) {
		this(name, type, script, DateUtils.createDate(1900), DateUtils.createDate(2100));
	}

	public Script(String name, String type, String script, Date startDate, Date endDate) {
		this.name = name;
		this.type = type;
		this.script = script;
		this.period = new Period(startDate, endDate);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Period getPeriod() {
		return period;
	}

	public void setPeriod(Period periods) {
		this.period = periods;
	}

	/**
	 * @return the <code>CompiledScript</code> object for this script
	 */
	public CompiledScript getCompiledScript() {
		return compiledScript;
	}

	/**
	 * @param compiledScript the <code>CompiledScript</code> object to set
	 */
	public void setCompiledScript(CompiledScript compiledScript) {
		this.compiledScript = compiledScript;
	}
}
