package org.egov.erpcollection.models;

import java.math.BigDecimal;

import org.egov.infstr.models.BaseModel;

/**
 * CollectionStgAccAmount entity. @author MyEclipse Persistence Tools
 */

public class CollectionStgAccAmount extends BaseModel {
	private static final long serialVersionUID = 1L;
	private CollectionStgReceipt collectionStgReceipt;
	private String description;
	private BigDecimal taxAmount;
	private BigDecimal penalty;
	private BigDecimal advance;
	private String glCode;
    private String functionCode;

	public CollectionStgReceipt getCollectionStgReceipt() {
		return this.collectionStgReceipt;
	}

	public void setCollectionStgReceipt(CollectionStgReceipt collectionStgReceipt) {
		this.collectionStgReceipt = collectionStgReceipt;
	}

	


	public BigDecimal getPenalty() {
		return this.penalty;
	}

	public void setPenalty(BigDecimal penalty) {
		this.penalty = penalty;
	}

	public String getGlCode() {
		return glCode;
	}

	public void setGlCode(String glCode) {
		this.glCode = glCode;
	}

	public BigDecimal getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(BigDecimal taxAmount) {
		this.taxAmount = taxAmount;
	}

	public BigDecimal getAdvance() {
		return advance;
	}

	public void setAdvance(BigDecimal advance) {
		this.advance = advance;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFunctionCode() {
		return functionCode;
	}

	public void setFunctionCode(String functionCode) {
		this.functionCode = functionCode;
	}
}