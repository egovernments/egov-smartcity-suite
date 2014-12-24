/*
 * @(#)EgAttributetype.java 3.0, 17 Jun, 2013 1:16:00 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.flexfields.model;

import java.util.HashSet;
import java.util.Set;

public class EgAttributetype implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private EgApplDomain egApplDomain;
	private String attName;
	private String attDatatype;
	private String defaultValue;
	private Long isrequired;
	private Set egAttributevalueses = new HashSet(0);

	/** default constructor */
	public EgAttributetype() {
		// For Hibernate
	}

	/** minimal constructor */
	public EgAttributetype(Long id, EgApplDomain egApplDomain, String attName, String attDatatype, Long isrequired) {
		this.id = id;
		this.egApplDomain = egApplDomain;
		this.attName = attName;
		this.attDatatype = attDatatype;
		this.isrequired = isrequired;
	}

	/** full constructor */
	public EgAttributetype(Long id, EgApplDomain egApplDomain, String attName, String attDatatype, String defaultValue, Long isrequired, Set egAttributevalueses) {
		this.id = id;
		this.egApplDomain = egApplDomain;
		this.attName = attName;
		this.attDatatype = attDatatype;
		this.defaultValue = defaultValue;
		this.isrequired = isrequired;
		this.egAttributevalueses = egAttributevalueses;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EgApplDomain getEgApplDomain() {
		return this.egApplDomain;
	}

	public void setEgApplDomain(EgApplDomain egApplDomain) {
		this.egApplDomain = egApplDomain;
	}

	public String getAttName() {
		return this.attName;
	}

	public void setAttName(String attName) {
		this.attName = attName;
	}

	public String getAttDatatype() {
		return this.attDatatype;
	}

	public void setAttDatatype(String attDatatype) {
		this.attDatatype = attDatatype;
	}

	public String getDefaultValue() {
		return this.defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public Long getIsrequired() {
		return this.isrequired;
	}

	public void setIsrequired(Long isrequired) {
		this.isrequired = isrequired;
	}

	public Set getEgAttributevalueses() {
		return this.egAttributevalueses;
	}

	public void setEgAttributevalueses(Set egAttributevalueses) {
		this.egAttributevalueses = egAttributevalueses;
	}

}