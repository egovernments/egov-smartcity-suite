/*
 * @(#)Bank.java 3.0, 6 Jun, 2013 2:44:35 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Bank implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;

	private String code;

	private String name;

	private String narration;

	private boolean isactive;

	private Date lastmodified;

	private Date created;

	private BigDecimal modifiedby;

	private String type;

	private Set<Bankbranch> bankbranchs = new HashSet<Bankbranch>(0);

	public Bank() {
		//For hibernate to work
	}

	public Bank(String code, String name, boolean isactive, Date lastmodified, Date created, BigDecimal modifiedby) {
		this.code = code;
		this.name = name;
		this.isactive = isactive;
		this.lastmodified = lastmodified;
		this.created = created;
		this.modifiedby = modifiedby;
	}

	public Bank(String code, String name, String narration, boolean isactive, Date lastmodified, Date created, BigDecimal modifiedby, String type, Set<Bankbranch> bankbranchs) {
		this.code = code;
		this.name = name;
		this.narration = narration;
		this.isactive = isactive;
		this.lastmodified = lastmodified;
		this.created = created;
		this.modifiedby = modifiedby;
		this.type = type;
		this.bankbranchs = bankbranchs;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getNarration() {
		return this.narration;
	}

	public void setNarration(String narration) {
		this.narration = narration;
	}

	public boolean isIsactive() {
		return this.isactive;
	}

	public void setIsactive(boolean isactive) {
		this.isactive = isactive;
	}

	public Date getLastmodified() {
		return this.lastmodified;
	}

	public void setLastmodified(Date lastmodified) {
		this.lastmodified = lastmodified;
	}

	public Date getCreated() {
		return this.created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public BigDecimal getModifiedby() {
		return this.modifiedby;
	}

	public void setModifiedby(BigDecimal modifiedby) {
		this.modifiedby = modifiedby;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Set<Bankbranch> getBankbranchs() {
		return this.bankbranchs;
	}

	public void setBankbranchs(Set<Bankbranch> bankbranchs) {
		this.bankbranchs = bankbranchs;
	}

}
