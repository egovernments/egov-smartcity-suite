/*
 * @(#)Relationtype.java 3.0, 6 Jun, 2013 4:35:39 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Relationtype implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;

	private String code;

	private String name;

	private String description;

	private Boolean isactive;

	private Date created;

	private BigDecimal modifiedby;

	private Date lastmodified;

	private Set relations = new HashSet(0);

	public Relationtype() {
	}

	public Relationtype(Integer id, String code, String name, Date created, BigDecimal modifiedby, Date lastmodified) {
		this.id = id;
		this.code = code;
		this.name = name;
		this.created = created;
		this.modifiedby = modifiedby;
		this.lastmodified = lastmodified;
	}

	public Relationtype(Integer id, String code, String name, String description, Boolean isactive, Date created, BigDecimal modifiedby, Date lastmodified, Set relations) {
		this.id = id;
		this.code = code;
		this.name = name;
		this.description = description;
		this.isactive = isactive;
		this.created = created;
		this.modifiedby = modifiedby;
		this.lastmodified = lastmodified;
		this.relations = relations;
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

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getIsactive() {
		return this.isactive;
	}

	public void setIsactive(Boolean isactive) {
		this.isactive = isactive;
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

	public Date getLastmodified() {
		return this.lastmodified;
	}

	public void setLastmodified(Date lastmodified) {
		this.lastmodified = lastmodified;
	}

	public Set getRelations() {
		return this.relations;
	}

	public void setRelations(Set relations) {
		this.relations = relations;
	}

}
