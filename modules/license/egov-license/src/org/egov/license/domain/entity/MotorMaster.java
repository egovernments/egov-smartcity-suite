/*
 * @(#)MotorMaster.java 3.0, 29 Jul, 2013 1:24:24 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.license.domain.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.egov.infstr.models.BaseModel;
import org.egov.infstr.models.validator.Required;

public class MotorMaster extends BaseModel {
	private static final long serialVersionUID = 1L;
	@Required(message = "masters.master.licensefee.effectivedate")
	private Date effectiveFrom;
	private Date effectiveTo;
	private Long id;
	@Required(message = "masters.erection.motorhpfrom")
	private BigDecimal motorHpFrom;
	@Required(message = "masters.erection.motorhpto")
	private BigDecimal motorHpTo;
	@Required(message = "masters.erection.usingfee")
	private BigDecimal usingFee;

	public Date getEffectiveFrom() {
		return this.effectiveFrom;
	}

	public Date getEffectiveTo() {
		return this.effectiveTo;
	}

	@Override
	public Long getId() {
		return this.id;
	}

	public BigDecimal getMotorHpFrom() {
		return this.motorHpFrom;
	}

	public BigDecimal getMotorHpTo() {
		return this.motorHpTo;
	}

	public BigDecimal getUsingFee() {
		return this.usingFee;
	}

	public void setEffectiveFrom(final Date effectiveFrom) {
		this.effectiveFrom = effectiveFrom;
	}

	public void setEffectiveTo(final Date effectiveTo) {
		this.effectiveTo = effectiveTo;
	}

	@Override
	public void setId(final Long id) {
		this.id = id;
	}

	public void setMotorHpFrom(final BigDecimal motorHpFrom) {
		this.motorHpFrom = motorHpFrom;
	}

	public void setMotorHpTo(final BigDecimal motorHpTo) {
		this.motorHpTo = motorHpTo;
	}

	public void setUsingFee(final BigDecimal usingFee) {
		this.usingFee = usingFee;
	}

	@Override
	public String toString() {
		final StringBuilder str = new StringBuilder();
		str.append("MotorMaster={ ");
		str.append("serialVersionUID=").append(serialVersionUID);
		str.append("Id=").append(this.id);
		str.append("effectiveFrom=").append(this.effectiveFrom == null ? "null" : this.effectiveFrom.toString());
		str.append("effectiveTo=").append(this.effectiveTo == null ? "null" : this.effectiveTo.toString());
		str.append("motorHpFrom=").append(this.motorHpFrom == null ? "null" : this.motorHpFrom.toString());
		str.append("motorHpTo=").append(this.motorHpTo == null ? "null" : this.motorHpTo.toString());
		str.append("usingFee=").append(this.usingFee == null ? "null" : this.usingFee.toString());
		str.append("}");
		return str.toString();
	}
}
