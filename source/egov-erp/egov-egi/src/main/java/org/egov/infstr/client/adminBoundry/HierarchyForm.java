/*
 * @(#)HierarchyForm.java 3.0, 18 Jun, 2013 2:21:59 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.client.adminBoundry;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.egov.infstr.client.EgovActionForm;

public class HierarchyForm extends EgovActionForm implements Serializable {

	private static final long serialVersionUID = 1L;
	private String name = "";
	private String code = "";
	private Integer hierarchyTypeid = null;

	/**
	 * @return Returns the code.
	 */
	public String getCode() {
		return this.code;
	}

	/**
	 * @param code The code to set.
	 */
	public void setCode(final String code) {
		this.code = code;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * @return Returns the hierarchyTypeid.
	 */
	public Integer getHierarchyTypeid() {
		return this.hierarchyTypeid;
	}

	/**
	 * @param hierarchyTypeid The hierarchyTypeid to set.
	 */
	public void setHierarchyTypeid(final Integer hierarchyTypeid) {
		this.hierarchyTypeid = hierarchyTypeid;
	}

	@Override
	public void reset(final ActionMapping mapping, final HttpServletRequest req) {
		this.name = "";
		this.code = "";

		this.hierarchyTypeid = null;

	}
}