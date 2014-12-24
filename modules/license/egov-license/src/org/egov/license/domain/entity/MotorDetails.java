/*
 * @(#)MotorDetails.java 3.0, 29 Jul, 2013 1:24:28 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.license.domain.entity;

import java.math.BigDecimal;

import org.egov.infstr.models.BaseModel;
import org.egov.infstr.models.validator.Required;

public class MotorDetails extends BaseModel {
	private static final long serialVersionUID = 1L;
	@Required(message = "tradelicense.error.trader.tradedetails")
	private License license;
	@Required(message = "tradelicense.error.trader.motorhorsepower")
	private BigDecimal hp;
	private Long noOfMachines;
	private boolean history;

	public License getLicense() {
		return this.license;
	}

	public void setLicense(final License license) {
		this.license = license;
	}

	public MotorDetails() {
	}

	public boolean isHistory() {
		return this.history;
	}

	public void setHistory(final boolean history) {
		this.history = history;
	}

	public BigDecimal getHp() {
		return this.hp;
	}

	public void setHp(final BigDecimal hp) {
		this.hp = hp;
	}

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(final Long id) {
		this.id = id;
	}

	public Long getNoOfMachines() {
		return this.noOfMachines;
	}

	public void setNoOfMachines(final Long noOfMachines) {
		this.noOfMachines = noOfMachines;
	}

	@Override
	public String toString() {
		final StringBuilder str = new StringBuilder();
		str.append("MotorDetails={");
		str.append("  id=").append(this.id);
		str.append("  hp=").append(this.hp == null ? "null" : this.hp.toString());
		str.append("  noOfMachines=").append(this.noOfMachines == null ? "null" : this.noOfMachines.toString());
		str.append("  history=").append(this.history);
		str.append("}");
		return str.toString();

	}
}