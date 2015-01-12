/*
 * @(#)Functionary.java 3.0, 6 Jun, 2013 3:39:16 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons;

import java.math.BigDecimal;
import java.util.Date;

public class Functionary implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;

	private BigDecimal code;

	private String name;

	private Date createtimestamp;

	private Date updatetimestamp;

	private Boolean isactive;

	public Functionary() {
		// For hibernate to work
	}

	public Functionary(Integer id, BigDecimal code, String name) {
		this.id = id;
		this.code = code;
		this.name = name;
	}

	public Functionary(Integer id, BigDecimal code, String name, Date createtimestamp, Date updatetimestamp, Boolean isactive) {
		this.id = id;
		this.code = code;
		this.name = name;
		this.createtimestamp = createtimestamp;
		this.updatetimestamp = updatetimestamp;
		this.isactive = isactive;
	}

	public Integer getId() {
		return id;

	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BigDecimal getCode() {
		return this.code;
	}

	public void setCode(BigDecimal code) {
		this.code = code;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreatetimestamp() {
		return this.createtimestamp;
	}

	public void setCreatetimestamp(Date createtimestamp) {
		this.createtimestamp = createtimestamp;
	}

	public Date getUpdatetimestamp() {
		return this.updatetimestamp;
	}

	public void setUpdatetimestamp(Date updatetimestamp) {
		this.updatetimestamp = updatetimestamp;
	}

	public Boolean getIsactive() {
		return this.isactive;
	}

	public void setIsactive(Boolean isactive) {
		this.isactive = isactive;
	}

}
