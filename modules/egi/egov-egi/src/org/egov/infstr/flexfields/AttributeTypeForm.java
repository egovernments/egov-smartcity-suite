/*
 * @(#)AttributeTypeForm.java 3.0, 17 Jun, 2013 12:54:29 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.flexfields;

public class AttributeTypeForm extends org.apache.struts.action.ActionForm {

	private static final long serialVersionUID = 1L;
	String id;
	String applDomainId;
	String attributeName;
	String attributeDataType;
	String forward;
	String isRequired;
	String isList;
	String key[];
	String value[];
	String defaultValue;

	public String getDefaultValue() {
		return this.defaultValue;
	}

	public void setDefaultValue(final String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getIsList() {
		return this.isList;
	}

	public void setIsList(final String isList) {
		this.isList = isList;
	}

	public String getIsRequired() {
		return this.isRequired;
	}

	public void setIsRequired(final String isRequired) {
		this.isRequired = isRequired;
	}

	public String[] getKey() {
		return this.key;
	}

	public void setKey(final String[] key) {
		this.key = key;
	}

	public String[] getValue() {
		return this.value;
	}

	public void setValue(final String[] value) {
		this.value = value;
	}

	public String getForward() {
		return this.forward;
	}

	public void setForward(final String forward) {
		this.forward = forward;
	}

	public String getAttributeDataType() {
		return this.attributeDataType;
	}

	public void setAttributeDataType(final String attributeDataType) {
		this.attributeDataType = attributeDataType;
	}

	public String getAttributeName() {
		return this.attributeName;
	}

	public void setAttributeName(final String attributeName) {
		this.attributeName = attributeName;
	}

	public String getApplDomainId() {
		return this.applDomainId;
	}

	public void setApplDomainId(final String domainId) {
		this.applDomainId = domainId;
	}

	public String getId() {
		return this.id;
	}

	public void setId(final String id) {
		this.id = id;
	}

}
