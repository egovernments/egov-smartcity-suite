/*
 * @(#)EgUomcategory.java 3.0, 17 Jun, 2013 11:16:00 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.commonMasters;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class EgUomcategory implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;

	private String category;

	private String narration;

	private Date lastmodified;

	private Date createddate;

	private BigDecimal createdby;

	private BigDecimal lastmodifiedby;

	private Set egUoms = new HashSet(0);

	public EgUomcategory() {
		// For Hibernate
	}

	public EgUomcategory(Integer id, String category, Date lastmodified, Date createddate, BigDecimal createdby) {
		this.id = id;
		this.category = category;
		this.lastmodified = lastmodified;
		this.createddate = createddate;
		this.createdby = createdby;
	}

	public EgUomcategory(Integer id, String category, String narration, Date lastmodified, Date createddate, BigDecimal createdby, BigDecimal lastmodifiedby, Set egUoms) {
		this.id = id;
		this.category = category;
		this.narration = narration;
		this.lastmodified = lastmodified;
		this.createddate = createddate;
		this.createdby = createdby;
		this.lastmodifiedby = lastmodifiedby;
		this.egUoms = egUoms;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getNarration() {
		return this.narration;
	}

	public void setNarration(String narration) {
		this.narration = narration;
	}

	public Date getLastmodified() {
		return this.lastmodified;
	}

	public void setLastmodified(Date lastmodified) {
		this.lastmodified = lastmodified;
	}

	public Date getCreateddate() {
		return this.createddate;
	}

	public void setCreateddate(Date createddate) {
		this.createddate = createddate;
	}

	public BigDecimal getCreatedby() {
		return this.createdby;
	}

	public void setCreatedby(BigDecimal createdby) {
		this.createdby = createdby;
	}

	public BigDecimal getLastmodifiedby() {
		return this.lastmodifiedby;
	}

	public void setLastmodifiedby(BigDecimal lastmodifiedby) {
		this.lastmodifiedby = lastmodifiedby;
	}

	public Set getEgUoms() {
		return this.egUoms;
	}

	public void setEgUoms(Set egUoms) {
		this.egUoms = egUoms;
	}

}
