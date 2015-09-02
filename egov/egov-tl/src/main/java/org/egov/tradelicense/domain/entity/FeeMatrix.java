package org.egov.tradelicense.domain.entity;

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
		return demandReasonMaster;
	}

	public void setDemandReasonMaster(EgDemandReasonMaster demandReasonMaster) {
		this.demandReasonMaster = demandReasonMaster;
	}

	public NatureOfBusiness getBusinessNature() {
		return businessNature;
	}

	public void setBusinessNature(NatureOfBusiness businessNature) {
		this.businessNature = businessNature;
	}

	public SubCategory getSubcategory() {
		return subcategory;
	}

	public void setSubcategory(SubCategory subcategory) {
		this.subcategory = subcategory;
	}

	public LicenseAppType getApplType() {
		return applType;
	}

	public void setApplType(LicenseAppType applType) {
		this.applType = applType;
	}

	public NatureOfBusiness getTradeNatureId() {
		return businessNature;
	}

	public FeeType getFeeType() {
		return feeType;
	}

	public void setFeeType(FeeType feeType) {
		this.feeType = feeType;
	}

	public void setTradeNatureId(NatureOfBusiness tradeNatureId) {
		this.businessNature = tradeNatureId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("FeeMatrix={");
		str.append("businessNature=").append(businessNature == null ? "null" : businessNature.toString());
		str.append("subcategory=").append(subcategory == null ? "null" : subcategory.toString());
		str.append("applType=").append(applType == null ? "null" : applType.toString());
		str.append("feeType=").append(feeType == null ? "null" : feeType.toString());
		str.append("amount=").append(amount == null ? "null" : amount.toString());
		str.append("}");
		return str.toString();
	}
}
