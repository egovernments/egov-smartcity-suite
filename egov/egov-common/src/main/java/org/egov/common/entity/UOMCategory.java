package org.egov.common.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class UOMCategory implements java.io.Serializable {

	private static final long serialVersionUID = -5071889556823525112L;

	private Integer id;

	private String category;

	private String narration;

	private Date lastmodified;

	private Date createddate;

	private BigDecimal createdby;

	private BigDecimal lastmodifiedby;

	private Set<UOM> uoms = new HashSet<>();

	public UOMCategory() {
	}

	public UOMCategory(final Integer id, final String category, final Date lastmodified, final Date createddate,
			final BigDecimal createdby) {
		this.id = id;
		this.category = category;
		this.lastmodified = lastmodified;
		this.createddate = createddate;
		this.createdby = createdby;
	}

	public UOMCategory(final Integer id, final String category, final String narration, final Date lastmodified,
			final Date createddate, final BigDecimal createdby, final BigDecimal lastmodifiedby, final Set<UOM> uoms) {
		this.id = id;
		this.category = category;
		this.narration = narration;
		this.lastmodified = lastmodified;
		this.createddate = createddate;
		this.createdby = createdby;
		this.lastmodifiedby = lastmodifiedby;
		this.uoms = uoms;
	}

	public Integer getId() {
		return id;
	}

	public void setId(final Integer id) {
		this.id = id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(final String category) {
		this.category = category;
	}

	public String getNarration() {
		return narration;
	}

	public void setNarration(final String narration) {
		this.narration = narration;
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

	public Set<UOM> getUoms() {
		return uoms;
	}

	public void setUoms(final Set<UOM> uoms) {
		this.uoms = uoms;
	}

}
