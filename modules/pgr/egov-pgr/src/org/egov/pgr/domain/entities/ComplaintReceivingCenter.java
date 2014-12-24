/*
 * @(#)ComplaintReceivingCenter.java 3.0, 23 Jul, 2013 3:29:42 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.pgr.domain.entities;

import java.math.BigDecimal;

import org.egov.infstr.models.BaseModel;

public class ComplaintReceivingCenter extends BaseModel {

	private static final long serialVersionUID = 1L;
	private BigDecimal centerType;
	private String centerName;
	private String centerAddress;
	private BigDecimal centerManagerId;
	private String centerNameLocal;
	private String centerAddressLocal;
	private Integer bndryId;
	private String isActive;

	public BigDecimal getCenterType() {
		return this.centerType;
	}

	public void setCenterType(final BigDecimal centerType) {
		this.centerType = centerType;
	}

	public String getCenterName() {
		return this.centerName;
	}

	public void setCenterName(final String centerName) {
		this.centerName = centerName;
	}

	public String getCenterAddress() {
		return this.centerAddress;
	}

	public void setCenterAddress(final String centerAddress) {
		this.centerAddress = centerAddress;
	}

	public BigDecimal getCenterManagerId() {
		return this.centerManagerId;
	}

	public void setCenterManagerId(final BigDecimal centerManagerId) {
		this.centerManagerId = centerManagerId;
	}

	public String getCenterNameLocal() {
		return this.centerNameLocal;
	}

	public void setCenterNameLocal(final String centerNameLocal) {
		this.centerNameLocal = centerNameLocal;
	}

	public String getCenterAddressLocal() {
		return this.centerAddressLocal;
	}

	public void setCenterAddressLocal(final String centerAddressLocal) {
		this.centerAddressLocal = centerAddressLocal;
	}

	public Integer getBndryId() {
		return this.bndryId;
	}

	public void setBndryId(final Integer bndryId) {
		this.bndryId = bndryId;
	}

	public String getIsActive() {
		return this.isActive;
	}

	public void setIsActive(final String isActive) {
		this.isActive = isActive;
	}

}