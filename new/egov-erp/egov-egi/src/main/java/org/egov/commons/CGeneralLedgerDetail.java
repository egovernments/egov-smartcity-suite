/*
 * @(#)CGeneralLedgerDetail.java 3.0, 6 Jun, 2013 3:11:17 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons;

import java.math.BigDecimal;

public class CGeneralLedgerDetail {
	private Long id;
	private Integer generalLedgerId;
	private Integer detailKeyId;
	private Integer detailTypeId;
	private BigDecimal amount;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getGeneralLedgerId() {
		return generalLedgerId;
	}
	public void setGeneralLedgerId(Integer generalLedgerId) {
		this.generalLedgerId = generalLedgerId;
	}
	public Integer getDetailKeyId() {
		return detailKeyId;
	}
	public void setDetailKeyId(Integer detailKeyId) {
		this.detailKeyId = detailKeyId;
	}
	public Integer getDetailTypeId() {
		return detailTypeId;
	}
	public void setDetailTypeId(Integer detailTypeId) {
		this.detailTypeId = detailTypeId;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	
}
