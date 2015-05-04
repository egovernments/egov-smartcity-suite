package org.egov.common.entity;

import java.math.BigDecimal;
import java.util.Date;

public class UOM implements java.io.Serializable {

	private static final long serialVersionUID = 8964393733499647786L;

	private Integer id;

	private UOMCategory uomCategory;

	private String uom;

	private String narration;

	private BigDecimal convFactor;

	private boolean baseuom;

	private Date lastmodified;

	private Date createddate;

	private BigDecimal createdby;

	private BigDecimal lastmodifiedby;

	public UOM() {
	}

	public UOM(final Integer id, final UOMCategory egUomcategory, final String uom, final BigDecimal convFactor,
			final boolean baseuom, final Date lastmodified, final Date createddate, final BigDecimal createdby) {
		this.id = id;
		this.uomCategory = egUomcategory;
		this.uom = uom;
		this.convFactor = convFactor;
		this.baseuom = baseuom;
		this.lastmodified = lastmodified;
		this.createddate = createddate;
		this.createdby = createdby;
	}

	public UOM(final Integer id, final UOMCategory egUomcategory, final String uom, final String narration,
			final BigDecimal convFactor, final boolean baseuom, final Date lastmodified, final Date createddate,
			final BigDecimal createdby, final BigDecimal lastmodifiedby) {
		this.id = id;
		this.uomCategory = egUomcategory;
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
		return id;
	}

	public void setId(final Integer id) {
		this.id = id;
	}

	public UOMCategory getUomCategory() {
		return uomCategory;
	}

	public void setUomCategory(final UOMCategory uomCategory) {
		this.uomCategory = uomCategory;
	}

	public String getUom() {
		return uom;
	}

	public void setUom(final String uom) {
		this.uom = uom;
	}

	public String getNarration() {
		return narration;
	}

	public void setNarration(final String narration) {
		this.narration = narration;
	}

	public BigDecimal getConvFactor() {
		return convFactor;
	}

	public void setConvFactor(final BigDecimal convFactor) {
		this.convFactor = convFactor;
	}

	public boolean isBaseuom() {
		return baseuom;
	}

	public void setBaseuom(final boolean baseuom) {
		this.baseuom = baseuom;
	}

	public Date getLastmodified() {
		return lastmodified;
	}

	public void setLastmodified(final Date lastmodified) {
		this.lastmodified = lastmodified;
	}

	public Date getCreateddate() {
		return createddate;
	}

	public void setCreateddate(final Date createddate) {
		this.createddate = createddate;
	}

	public BigDecimal getCreatedby() {
		return createdby;
	}

	public void setCreatedby(final BigDecimal createdby) {
		this.createdby = createdby;
	}

	public BigDecimal getLastmodifiedby() {
		return lastmodifiedby;
	}

	public void setLastmodifiedby(final BigDecimal lastmodifiedby) {
		this.lastmodifiedby = lastmodifiedby;
	}

}
