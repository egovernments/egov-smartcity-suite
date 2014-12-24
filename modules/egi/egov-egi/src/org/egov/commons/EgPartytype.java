/*
 * @(#)EgPartytype.java 3.0, 6 Jun, 2013 3:33:02 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class EgPartytype implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	private Integer id;

	private EgPartytype egPartytype;

	private String code;

	private String description;

	private BigDecimal createdby;

	private Date createddate;

	private BigDecimal lastmodifiedby;

	private Date lastmodifieddate;

	private Set<EgwTypeOfWork> egwTypeofworks = new HashSet<EgwTypeOfWork>(0);

	private Set<EgPartytype> egPartytypes = new HashSet<EgPartytype>(0);

	public EgPartytype() {
	}

	public EgPartytype(String code, String description, BigDecimal createdby, Date createddate) {
		this.code = code;
		this.description = description;
		this.createdby = createdby;
		this.createddate = createddate;
	}

	public EgPartytype(EgPartytype egPartytype, String code, String description, BigDecimal createdby, Date createddate, BigDecimal lastmodifiedby, Date lastmodifieddate, Set<EgwTypeOfWork> egwTypeofworks, Set<EgPartytype> egPartytypes) {
		this.egPartytype = egPartytype;
		this.code = code;
		this.description = description;
		this.createdby = createdby;
		this.createddate = createddate;
		this.lastmodifiedby = lastmodifiedby;
		this.lastmodifieddate = lastmodifieddate;
		this.egwTypeofworks = egwTypeofworks;
		this.egPartytypes = egPartytypes;
	}

	public EgPartytype getEgPartytype() {
		return this.egPartytype;
	}

	public void setEgPartytype(EgPartytype egPartytype) {
		this.egPartytype = egPartytype;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getCreatedby() {
		return this.createdby;
	}

	public void setCreatedby(BigDecimal createdby) {
		this.createdby = createdby;
	}

	public Date getCreateddate() {
		return this.createddate;
	}

	public void setCreateddate(Date createddate) {
		this.createddate = createddate;
	}

	public BigDecimal getLastmodifiedby() {
		return this.lastmodifiedby;
	}

	public void setLastmodifiedby(BigDecimal lastmodifiedby) {
		this.lastmodifiedby = lastmodifiedby;
	}

	public Date getLastmodifieddate() {
		return this.lastmodifieddate;
	}

	public void setLastmodifieddate(Date lastmodifieddate) {
		this.lastmodifieddate = lastmodifieddate;
	}

	public Set<EgwTypeOfWork> getEgwTypeofworks() {
		return this.egwTypeofworks;
	}

	public void setEgwTypeofworks(Set<EgwTypeOfWork> egwTypeofworks) {
		this.egwTypeofworks = egwTypeofworks;
	}

	public Set<EgPartytype> getEgPartytypes() {
		return this.egPartytypes;
	}

	public void setEgPartytypes(Set<EgPartytype> egPartytypes) {
		this.egPartytypes = egPartytypes;
	}

}
