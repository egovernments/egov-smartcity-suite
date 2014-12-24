/*
 * @(#)AuthorizationRule.java 3.0, 14 Jun, 2013 4:37:29 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.rrbac.model;

import org.egov.infstr.models.BaseModel;
import org.egov.infstr.models.Script;

public class AuthorizationRule extends BaseModel {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Action action;
	private Script script;
	private String objectType;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Action getAction() {
		return this.action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public Script getScript() {
		return this.script;
	}

	public void setScript(Script script) {
		this.script = script;
	}

	public String getObjectType() {
		return this.objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}
}