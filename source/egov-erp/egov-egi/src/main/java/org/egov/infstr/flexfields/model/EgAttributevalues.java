/*
 * @(#)EgAttributevalues.java 3.0, 17 Jun, 2013 1:16:55 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.flexfields.model;

public class EgAttributevalues implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private EgAttributetype egAttributetype;
	private EgApplDomain egApplDomain;
	private String attValue;
	private Long domaintxnid;

	/** default constructor */
	public EgAttributevalues() {
		// For Hibernate
	}

	/** full constructor */
	public EgAttributevalues(Long id, EgAttributetype egAttributetype, EgApplDomain egApplDomain, String attValue, Long domaintxnid) {
		this.id = id;
		this.egAttributetype = egAttributetype;
		this.egApplDomain = egApplDomain;
		this.attValue = attValue;
		this.domaintxnid = domaintxnid;
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

	public EgApplDomain getEgApplDomain() {
		return this.egApplDomain;
	}

	public void setEgApplDomain(EgApplDomain egApplDomain) {
		this.egApplDomain = egApplDomain;
	}

	public String getAttValue() {
		return this.attValue;
	}

	public void setAttValue(String attValue) {
		this.attValue = attValue;
	}

	public Long getDomaintxnid() {
		return this.domaintxnid;
	}

	public void setDomaintxnid(Long domaintxnid) {
		this.domaintxnid = domaintxnid;
	}

}