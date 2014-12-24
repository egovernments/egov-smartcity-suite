/*
 * @(#)EgwTypeOfWork.java 3.0, 6 Jun, 2013 3:37:40 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons;

import java.util.Date;

public class EgwTypeOfWork {

	private Long id;
	private String code;
	private EgwTypeOfWork parentid;
	private EgPartytype egPartytype;
	private String description;
	private Integer createdby;
	private Integer lastmodifiedby;
	private Date createddate;
	private Date lastmodifieddate;

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the createdby
	 */
	public Integer getCreatedby() {
		return createdby;
	}

	/**
	 * @param createdby the createdby to set
	 */
	public void setCreatedby(Integer createdby) {
		this.createdby = createdby;
	}

	/**
	 * @return the createddate
	 */
	public Date getCreateddate() {
		return createddate;
	}

	/**
	 * @param createddate the createddate to set
	 */
	public void setCreateddate(Date createddate) {
		this.createddate = createddate;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the lastmodifiedby
	 */
	public Integer getLastmodifiedby() {
		return lastmodifiedby;
	}

	/**
	 * @param lastmodifiedby the lastmodifiedby to set
	 */
	public void setLastmodifiedby(Integer lastmodifiedby) {
		this.lastmodifiedby = lastmodifiedby;
	}

	/**
	 * @return the lastmodifieddate
	 */
	public Date getLastmodifieddate() {
		return lastmodifieddate;
	}

	/**
	 * @param lastmodifieddate the lastmodifieddate to set
	 */
	public void setLastmodifieddate(Date lastmodifieddate) {
		this.lastmodifieddate = lastmodifieddate;
	}

	/**
	 * @return the parentid
	 */
	public EgwTypeOfWork getParentid() {
		return parentid;
	}

	/**
	 * @param parentid the parentid to set
	 */
	public void setParentid(EgwTypeOfWork parentid) {
		this.parentid = parentid;
	}

	/**
	 * @return the egPartytype
	 */
	public EgPartytype getEgPartytype() {
		return egPartytype;
	}

	/**
	 * @param egPartytype the egPartytype to set
	 */
	public void setEgPartytype(EgPartytype egPartytype) {
		this.egPartytype = egPartytype;
	}

}
