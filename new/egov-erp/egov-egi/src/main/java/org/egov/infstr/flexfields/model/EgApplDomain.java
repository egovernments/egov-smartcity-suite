/*
 * @(#)EgApplDomain.java 3.0, 17 Jun, 2013 1:14:16 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.flexfields.model;

import java.util.HashSet;
import java.util.Set;

public class EgApplDomain implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private String description;
	private Set egAttributetypes = new HashSet(0);
	private Set egAttributevalueses = new HashSet(0);

	/** default constructor */
	public EgApplDomain() {
		// FOR Hibernate
	}

	/** minimal constructor */
	public EgApplDomain(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	/** full constructor */
	public EgApplDomain(Long id, String name, String description, Set egAttributetypes, Set egAttributevalueses) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.egAttributetypes = egAttributetypes;
		this.egAttributevalueses = egAttributevalueses;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set getEgAttributetypes() {
		return this.egAttributetypes;
	}

	public void setEgAttributetypes(Set egAttributetypes) {
		this.egAttributetypes = egAttributetypes;
	}

	public Set getEgAttributevalueses() {
		return this.egAttributevalueses;
	}

	public void setEgAttributevalueses(Set egAttributevalueses) {
		this.egAttributevalueses = egAttributevalueses;
	}

}