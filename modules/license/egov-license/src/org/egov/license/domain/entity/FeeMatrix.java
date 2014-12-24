/*
 * @(#)FeeMatrix.java 3.0, 29 Jul, 2013 1:24:25 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.license.domain.entity;

import java.math.BigDecimal;

import org.egov.demand.model.EgDemandReasonMaster;
import org.egov.infstr.models.BaseModel;

public class FeeMatrix extends BaseModel {
	private static final long serialVersionUID = 1L;
	private NatureOfBusiness businessNature;
	private SubCategory subcategory;
	private LicenseAppType applType;
	private FeeType feeType;
	private BigDecimal amount;
	private EgDemandReasonMaster demandReasonMaster;

	public EgDemandReasonMaster getDemandReasonMaster() {
		return this.demandReasonMaster;
	}

	public void setDemandReasonMaster(final EgDemandReasonMaster demandReasonMaster) {
		this.demandReasonMaster = demandReasonMaster;
	}

	public NatureOfBusiness getBusinessNature() {
		return this.businessNature;
	}

	public void setBusinessNature(final NatureOfBusiness businessNature) {
		this.businessNature = businessNature;
	}

	public SubCategory getSubcategory() {
		return this.subcategory;
	}

	public void setSubcategory(final SubCategory subcategory) {
		this.subcategory = subcategory;
	}

	public LicenseAppType getApplType() {
		return this.applType;
	}

	public void setApplType(final LicenseAppType applType) {
		this.applType = applType;
	}

	public NatureOfBusiness getTradeNatureId() {
		return this.businessNature;
	}

	public FeeType getFeeType() {
		return this.feeType;
	}

	public void setFeeType(final FeeType feeType) {
		this.feeType = feeType;
	}

	public void setTradeNatureId(final NatureOfBusiness tradeNatureId) {
		this.businessNature = tradeNatureId;
	}

	public BigDecimal getAmount() {
		return this.amount;
	}

	public void setAmount(final BigDecimal amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		final StringBuilder str = new StringBuilder();
		str.append("FeeMatrix={");
		str.append("businessNature=").append(this.businessNature == null ? "null" : this.businessNature.toString());
		str.append("subcategory=").append(this.subcategory == null ? "null" : this.subcategory.toString());
		str.append("applType=").append(this.applType == null ? "null" : this.applType.toString());
		str.append("feeType=").append(this.feeType == null ? "null" : this.feeType.toString());
		str.append("amount=").append(this.amount == null ? "null" : this.amount.toString());
		str.append("}");
		return str.toString();
	}
}
