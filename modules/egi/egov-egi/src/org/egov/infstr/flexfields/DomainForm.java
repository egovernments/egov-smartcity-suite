/*
 * @(#)DomainForm.java 3.0, 17 Jun, 2013 12:56:46 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.flexfields;

public class DomainForm extends org.apache.struts.action.ActionForm {

	private static final long serialVersionUID = 1L;
	private String id;
	private String domainName;
	private String domainDesc;
	private String forward;

	public String getForward() {
		return this.forward;
	}

	public void setForward(final String forward) {
		this.forward = forward;
	}

	public String getDomainDesc() {
		return this.domainDesc;
	}

	public void setDomainDesc(final String domainDesc) {
		this.domainDesc = domainDesc;
	}

	public String getDomainName() {
		return this.domainName;
	}

	public void setDomainName(final String domainName) {
		this.domainName = domainName;
	}

	public String getId() {
		return this.id;
	}

	public void setId(final String id) {
		this.id = id;
	}

}
