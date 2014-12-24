/*
 * @(#)EgUom.java 3.0, 17 Jun, 2013 11:14:49 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.commonMasters;

import java.math.BigDecimal;
import java.util.Date;

public class EgUom implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;

	private EgUomcategory egUomcategory;

	private String uom;

	private String narration;

	private BigDecimal convFactor;

	private boolean baseuom;

	private Date lastmodified;

	private Date createddate;

	private BigDecimal createdby;

	private BigDecimal lastmodifiedby;

	public EgUom() {
		// For Hibernate
	}

	public EgUom(Integer id, EgUomcategory egUomcategory, String uom, BigDecimal convFactor, boolean baseuom, Date lastmodified, Date createddate, BigDecimal createdby) {
		this.id = id;
		this.egUomcategory = egUomcategory;
		this.uom = uom;
		this.convFactor = convFactor;
		this.baseuom = baseuom;
		this.lastmodified = lastmodified;
		this.createddate = createddate;
		this.createdby = createdby;
	}

	public EgUom(Integer id, EgUomcategory egUomcategory, String uom, String narration, BigDecimal convFactor, boolean baseuom, Date lastmodified, Date createddate, BigDecimal createdby, BigDecimal lastmodifiedby) {
		this.id = id;
		this.egUomcategory = egUomcategory;
		this.uom = uom;
		this.narration = narration;
		this.convFactor = convFactor;
		this.baseuom = baseuom;
		this.lastmodified = lastmodified;
		this.createddate = createddate;
		this.createdby = createdby;
		this.lastmodifiedby = lastmodifiedby;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public EgUomcategory getEgUomcategory() {
		return this.egUomcategory;
	}

	public void setEgUomcategory(EgUomcategory egUomcategory) {
		this.egUomcategory = egUomcategory;
	}

	public String getUom() {
		return this.uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}

	public String getNarration() {
		return this.narration;
	}

	public void setNarration(String narration) {
		this.narration = narration;
	}

	public BigDecimal getConvFactor() {
		return this.convFactor;
	}

	public void setConvFactor(BigDecimal convFactor) {
		this.convFactor = convFactor;
	}

	public boolean isBaseuom() {
		return this.baseuom;
	}

	public void setBaseuom(boolean baseuom) {
		this.baseuom = baseuom;
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

}
