/*
 * @(#)EgAttributelist.java 3.0, 17 Jun, 2013 1:15:09 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.flexfields.model;

public class EgAttributelist implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private EgAttributetype egAttributetype;
	private String attKey;
	private String attValue;

	/** default constructor */
	public EgAttributelist() {
		// For Hibernate
	}

	/** full constructor */
	public EgAttributelist(Long id, EgAttributetype egAttributetype, String attKey, String attValue) {
		this.id = id;
		this.egAttributetype = egAttributetype;
		this.attKey = attKey;
		this.attValue = attValue;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EgAttributetype getEgAttributetype() {
		return this.egAttributetype;
	}

	public void setEgAttributetype(EgAttributetype egAttributetype) {
		this.egAttributetype = egAttributetype;
	}

	public String getAttKey() {
		return this.attKey;
	}

	public void setAttKey(String attKey) {
		this.attKey = attKey;
	}

	public String getAttValue() {
		return this.attValue;
	}

	public void setAttValue(String attValue) {
		this.attValue = attValue;
	}

}