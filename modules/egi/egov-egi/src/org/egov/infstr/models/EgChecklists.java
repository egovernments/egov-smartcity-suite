/*
 * @(#)EgChecklists.java 3.0, 17 Jun, 2013 2:46:37 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.models;

import java.io.Serializable;
import java.util.Date;

import org.egov.infstr.config.AppConfigValues;

public class EgChecklists implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private Long objectid;
	private String checklistvalue;
	private AppConfigValues appconfigvalue;
	private Date lastmodified;
	private String remarks;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getObjectid() {
		return objectid;
	}

	public void setObjectid(Long objectid) {
		this.objectid = objectid;
	}

	public String getChecklistvalue() {
		return checklistvalue;
	}

	public void setChecklistvalue(String checklistvalue) {
		this.checklistvalue = checklistvalue;
	}

	public Date getLastmodified() {
		return lastmodified;
	}

	public void setLastmodified(Date lastmodified) {
		this.lastmodified = lastmodified;
	}

	public AppConfigValues getAppconfigvalue() {
		return appconfigvalue;
	}

	public void setAppconfigvalue(AppConfigValues appconfigvalue) {
		this.appconfigvalue = appconfigvalue;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}
