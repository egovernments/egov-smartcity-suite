/*
 * @(#)Accountdetailtype.java 3.0, 6 Jun, 2013 2:35:39 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons;

import java.math.BigDecimal;
import java.util.Date;

public class Accountdetailtype implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;

	private String name;

	private String description;

	private String tablename;

	private String columnname;

	private String attributename;

	private BigDecimal nbroflevels;

	private Boolean isactive;

	private Date created;

	private Date lastmodified;

	private Long modifiedby;

	private String fullQualifiedName;

	private Accountdetailtype accountdetailtype;

	public Accountdetailtype() {
		//For hibernate to work
	}

	public Accountdetailtype(String name, String description, String attributename, BigDecimal nbroflevels) {
		this.name = name;
		this.description = description;
		this.attributename = attributename;
		this.nbroflevels = nbroflevels;
	}

	public Accountdetailtype(String name, String description, String tablename, String columnname, String attributename, BigDecimal nbroflevels, Boolean isactive, Date created, Date lastmodified, Long modifiedby) {
		this.name = name;
		this.description = description;
		this.tablename = tablename;
		this.columnname = columnname;
		this.attributename = attributename;
		this.nbroflevels = nbroflevels;
		this.isactive = isactive;
		this.created = created;
		this.lastmodified = lastmodified;
		this.modifiedby = modifiedby;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
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

	public String getTablename() {
		return this.tablename;
	}

	public void setTablename(String tablename) {
		this.tablename = tablename;
	}

	public String getColumnname() {
		return this.columnname;
	}

	public void setColumnname(String columnname) {
		this.columnname = columnname;
	}

	public String getAttributename() {
		return this.attributename;
	}

	public void setAttributename(String attributename) {
		this.attributename = attributename;
	}

	public BigDecimal getNbroflevels() {
		return this.nbroflevels;
	}

	public void setNbroflevels(BigDecimal nbroflevels) {
		this.nbroflevels = nbroflevels;
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

	public Date getLastmodified() {
		return this.lastmodified;
	}

	public void setLastmodified(Date lastmodified) {
		this.lastmodified = lastmodified;
	}

	public Long getModifiedby() {
		return this.modifiedby;
	}

	public void setModifiedby(Long modifiedby) {
		this.modifiedby = modifiedby;
	}

	public Accountdetailtype getAccountdetailtype() {
		return this.accountdetailtype;
	}

	public void setAccountdetailtype(Accountdetailtype accountdetailtype) {
		this.accountdetailtype = accountdetailtype;
	}

	public String getFullQualifiedName() {
		return fullQualifiedName;
	}

	public void setFullQualifiedName(String fullQualifiedName) {
		this.fullQualifiedName = fullQualifiedName;
	}

}
