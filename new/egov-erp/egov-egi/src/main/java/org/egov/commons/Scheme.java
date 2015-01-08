/*
 * @(#)Scheme.java 3.0, 6 Jun, 2013 4:36:26 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Scheme implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;

	private Fund fund;

	private String code;

	private String name;

	private Date validfrom;

	private Date validto;

	private String isactive;

	private String description;

	private BigDecimal sectorid;

	private BigDecimal aaes;

	private BigDecimal fieldid;

	private Set<SubScheme> subSchemes = new HashSet<SubScheme>(0);

	public Scheme() {
		 //For hibernate to work
	}

	public Scheme(Integer id) {
		this.id = id;
	}

	public Scheme(Integer id, Fund fund, String code, String name, Date validfrom, Date validto, String isactive, String description, BigDecimal sectorid, BigDecimal aaes, BigDecimal fieldid, Set<SubScheme> subSchemes) {
		this.id = id;
		this.fund = fund;
		this.code = code;
		this.name = name;
		this.validfrom = validfrom;
		this.validto = validto;
		this.isactive = isactive;
		this.description = description;
		this.sectorid = sectorid;
		this.aaes = aaes;
		this.fieldid = fieldid;
		this.subSchemes = subSchemes;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Fund getFund() {
		return this.fund;
	}

	public void setFund(Fund fund) {
		this.fund = fund;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getValidfrom() {
		return this.validfrom;
	}

	public void setValidfrom(Date validfrom) {
		this.validfrom = validfrom;
	}

	public Date getValidto() {
		return this.validto;
	}

	public void setValidto(Date validto) {
		this.validto = validto;
	}

	public String getIsactive() {
		return this.isactive;
	}

	public void setIsactive(String isactive) {
		this.isactive = isactive;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getSectorid() {
		return this.sectorid;
	}

	public void setSectorid(BigDecimal sectorid) {
		this.sectorid = sectorid;
	}

	public BigDecimal getAaes() {
		return this.aaes;
	}

	public void setAaes(BigDecimal aaes) {
		this.aaes = aaes;
	}

	public BigDecimal getFieldid() {
		return this.fieldid;
	}

	public void setFieldid(BigDecimal fieldid) {
		this.fieldid = fieldid;
	}

	public Set<SubScheme> getSubSchemes() {
		return this.subSchemes;
	}

	public void setSubSchemes(Set<SubScheme> subSchemes) {
		this.subSchemes = subSchemes;
	}

}
