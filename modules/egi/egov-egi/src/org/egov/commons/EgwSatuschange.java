/*
 * @(#)EgwSatuschange.java 3.0, 6 Jun, 2013 3:36:11 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons;

import java.util.Date;

public class EgwSatuschange implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;

	private Integer tostatus;

	private Integer fromstatus;

	private String moduletype;

	private Integer moduleid;

	private Integer createdby;

	private Date lastmodifieddate;

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getFromstatus() {
		return fromstatus;
	}

	public void setFromstatus(Integer fromstatus) {
		this.fromstatus = fromstatus;
	}

	public Integer getTostatus() {
		return tostatus;
	}

	public void setTostatus(Integer tostatus) {
		this.tostatus = tostatus;
	}

	public String getModuletype() {
		return this.moduletype;
	}

	public void setModuletype(String moduletype) {
		this.moduletype = moduletype;
	}

	public Integer getModuleid() {
		return this.moduleid;
	}

	public void setModuleid(Integer moduleid) {
		this.moduleid = moduleid;
	}

	public Integer getCreatedby() {
		return this.createdby;
	}

	public void setCreatedby(Integer createdby) {
		this.createdby = createdby;
	}

	public Date getLastmodifieddate() {
		return this.lastmodifieddate;
	}

	public void setLastmodifieddate(Date lastmodifieddate) {
		this.lastmodifieddate = lastmodifieddate;
	}

}
