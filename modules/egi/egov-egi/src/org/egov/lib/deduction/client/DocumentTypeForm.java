/*
 * @(#)DocumentTypeForm.java 3.0, 16 Jun, 2013 10:43:37 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.deduction.client;

import org.apache.struts.action.ActionForm;

public class DocumentTypeForm extends ActionForm {
	private static final long serialVersionUID = 1L;

	public String getPartyTypeId() {
		return this.partyTypeId;
	}

	public void setPartyTypeId(final String partyTypeId) {
		this.partyTypeId = partyTypeId;
	}

	public String getMode() {
		return this.mode;
	}

	public void setMode(final String mode) {
		this.mode = mode;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(final String code) {
		this.code = code;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public String getId() {
		return this.id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public String getParentCode() {
		return this.parentCode;
	}

	public void setParentCode(final String parentCode) {
		this.parentCode = parentCode;
	}

	public String getParentId() {
		return this.parentId;
	}

	public void setParentId(final String parentId) {
		this.parentId = parentId;
	}

	String id;
	String code;
	String parentId;
	String partyTypeId;
	String description;
	String parentCode;
	String appliedToCode;
	String mode;

	public String getAppliedToCode() {
		return this.appliedToCode;
	}

	public void setAppliedToCode(final String appliedToCode) {
		this.appliedToCode = appliedToCode;
	}
}